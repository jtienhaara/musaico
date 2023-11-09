package musaico.foundation.domains;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * A particular implementation of Bloom filter, for quick but incomplete
 * set intersections.
 * </p>
 *
 * <p>
 * See, for example:
 * <a href="https://en.wikipedia.org/wiki/Bloom_filter">https://en.wikipedia.org/wiki/Bloom_filter</a>.
 * </p>
 *
 * <p>
 * False positives (X is a member of the set) are possible.  False negatives
 * are not.
 * </p>
 *
 * <p>
 * 10 hashes per object, with the recommended number of "bits"
 * (actually buckets) 14-15 * the input size.  The hashing algorithms
 * are arbitrary and, empirically, produce good results when intersecting
 * sets of sizes 32 - 32,768.  For example, two sets with 32 members each
 * will perform well; and two sets with 32,768 members each
 * will perform well.  Beyond that, memory starts getting piggy;
 * and with fewer than 32 x 32 elements to compare for an intersection,
 * you're better off doing a direct, dumb comparison.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public class BloomFilter
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The number of hash buckets.
    private final int numBuckets;

    // The hash buckets.  Each one contains the OR'ed together
    // "signatures" of all objects which hash to the bucket #.
    // When each of an object's hash buckets contains its b'th
    // "signature" (b = index of the hash, 0 - 9), then the
    // object is guaranteed to be in the set.  Otherwise, it
    // might or might not be in the set.
    private final int [] buckets;


    /**
     * <p>
     * Creates a new BloomFilter.
     * </p>
     *
     * @param num_buckets The number of hash buckets to use for this
     *                    Bloom filter.  14 or 15 times the input length
     *                    is a good starting point.  Must be greater than 0.
     */
    public BloomFilter (
            int num_buckets
            )
    {
        this.numBuckets = num_buckets;
        this.buckets = new int [ this.numBuckets ];
    }


    /**
     * <p>
     * Adds the specified object to the set.
     * </p>
     *
     * @param object The object to add to the set.  Must not be null.
     */
    public final void add (
            Object object
            )
    {
        final int [] hashes = this.hash ( object );
        final int hashes_summed = this.sum ( hashes );
        for ( int h = 0; h < hashes.length; h ++ )
        {
            final int bucket_num = hashes [ h ];
            if ( bucket_num < 0
                 || bucket_num >= this.numBuckets )
            {
                throw new IllegalArgumentException (
                    "    ERROR invalid bucket # "
                    + hashes [ h ]
                    + " (hash # " + h + ")" );
            }

            final int signature =
                this.signature ( hashes,
                                 hashes_summed,
                                 h );

            this.buckets [ bucket_num ] =
                this.buckets [ bucket_num ]
                | signature;
        }
    }


    /**
     * <p>
     * Returns True if the specified object is probably a member of the set;
     * false if it is definitely not a member of the set.
     * </p>
     *
     * @param object The object to test for set membership.  CAN be null.
     *
     * @return True if the specified object is probably a member of the set;
     *         false if it is definitely not a member of the set.
     */
    public final boolean contains (
            Object object
            )
    {
        final int [] hashes = this.hash ( object );
        final int hashes_summed = this.sum ( hashes );
        for ( int h = 0; h < hashes.length; h ++ )
        {
            final int bucket_num = hashes [ h ];
            if ( bucket_num < 0
                 || bucket_num >= this.numBuckets )
            {
                throw new IllegalArgumentException (
                    "    ERROR invalid bucket # "
                    + hashes [ h ]
                    + " (hash # " + h + ")" );
            }

            final int signature =
                this.signature ( hashes,
                                 hashes_summed,
                                 h );

            final int anded =
                this.buckets [ bucket_num ]
                & signature;
            if ( anded != signature )
            {
                // The specified object is definitely not in the set.
                return false;
            }
        }

        // Could be a false positive: the specified object might not
        // actually be in the set.
        return true;
    }


    /**
     * <p>
     * Produces the 10 hashes for the specified object.
     * </p>
     *
     * <p>
     * Can be overridden by derived implementations.
     * </p>
     *
     * @param object The object to hash.  CAN be null.
     *
     * @return The 10 int hashes.  Always exactly 10 ints.  Each hash
     *         can be any value.  Never null.
     */
    protected int [] hash (
        Object object
        )
    {
        if ( object == null )
        {
            return new int [] { 0, 0, 0 };
        }

        final Class<?> object_class = object.getClass ();
        final int class_hash = object_class.hashCode ();
        final int class_bucket_num = class_hash % this.numBuckets;

        final int object_hash = object.hashCode ();
        final int object_bucket_num = object_hash % this.numBuckets;

        final int extra_bucket_num;
        if ( object instanceof CharSequence )
        {
            final CharSequence chars = (CharSequence) object;
            final int length = chars.length ();
            extra_bucket_num = length % this.numBuckets;
        }
        else if ( object instanceof Date )
        {
            final Date date = (Date) object;
            final long time = date.getTime ();
            extra_bucket_num = (int) ( time % (long) this.numBuckets );
        }
        else if ( object instanceof Calendar )
        {
            final Calendar calendar = (Calendar) object;
            final Date date = calendar.getTime ();
            final long time = date.getTime ();
            extra_bucket_num = (int) ( time % (long) this.numBuckets );
        }
        /*
        else if ( object instanceof TermPipeline )
        {
            final TermPipeline<?, ?> term_pipeline =
                (TermPipeline<?, ?>) object;
            final Type<?> type = term_pipeline.type ();
            final int type_hash = type.hashCode ();
            extra_bucket_num = type_hash % this.numBuckets;
        }
        else if ( object instanceof Operation )
        {
            final Operation<?, ?> operation = (Operation<?, ?>) object;
            final Type<?> input_type = operation.inputType ();
            final Type<?> output_type = operation.outputType ();
            final Type<?> error_type = operation.errorType ();
            final int types_hash =
                input_type.hashCode ()
                + output_type.hashCode ()
                + error_type.hashCode ();
            extra_bucket_num = types_hash % this.numBuckets;
        }
        else if ( object instanceof OperationPipeline )
        {
            final OperationPipeline<?, ?> operation_pipeline =
                (OperationPipeline<?, ?>) object;
            final Type<?> input_type = operation_pipeline.inputType ();
            final Type<?> output_type = operation_pipeline.outputType ();
            final Type<?> error_type = operation_pipeline.errorType ();
            final int types_hash =
                input_type.hashCode ()
                + output_type.hashCode ()
                + error_type.hashCode ();
            extra_bucket_num = types_hash % this.numBuckets;
        }
        */
        else
        {
            // class hash XOR object hash:
            final int xor =
                ( class_hash | object_hash )
                & ( ~ ( class_hash & object_hash ) );
            extra_bucket_num = xor % this.numBuckets;
        }

        final int h0 = class_bucket_num;
        final int h1 = object_bucket_num;
        final int h2 = extra_bucket_num;
        final int h3 = ( h2 + 7919 ) % this.numBuckets;
        final int h4 = ( h3 | ( extra_bucket_num + 5659 ) ) % this.numBuckets;
        final int h5 = ( 7907 * ( class_hash - object_hash - extra_bucket_num ) ) % this.numBuckets;
        final int h6 = ( 7901 + ( object_hash - class_hash - extra_bucket_num ) ) % this.numBuckets;
        final int h7 = ( -6079 + 7883 * ( extra_bucket_num - class_hash - object_hash ) ) % this.numBuckets;
        final int h8 = ( 7879 * (  ( class_hash | ( ~ extra_bucket_num ) ) & object_hash ) ) % this.numBuckets;
        final int h9 = ( 7877 + ( class_hash + 1 ) * ( object_hash * -1 ) * ( extra_bucket_num - 6997 ) ) % this.numBuckets;

        final int [] buckets = new int []
        {
            h0, h1, h2, h3, h4, h5, h6, h7, h8, h9
        };

        // Tweak the class hash code, since it's likely to remain
        // constant across all members:
        for ( int h = 1; h < buckets.length; h ++ )
        {
            buckets [ 0 ] += ( buckets [ h ] << ( h - 1 ) );
            buckets [ 0 ] = buckets [ 0 ] % this.numBuckets;
        }

        for ( int h = 0; h < buckets.length; h ++ )
        {
            if ( buckets [ h ] < 0 )
            {
                buckets [ h ] += this.numBuckets;
            }
        }

        return buckets;
    }


    /**
     * <p>
     * Returns true if the specified objects hash to exactly the same
     * hash number arrays; false if they hash differently.
     * </p>
     *
     * @param left The first object to hash.  CAN be null.
     *
     * @param right The second object to hash.  CAN be null.
     *
     * @return True if the 2 objects have the same hashes;
     *         false if not.
     */
    public final boolean hashEquals ( Object left, Object right )
    {
        final int [] left_hash = this.hash ( left );
        final int [] right_hash = this.hash ( right );

        if ( left_hash.length != right_hash.length )
        {
            // Should be impossible...
            return false;
        }

        for ( int h = 0; h < left_hash.length; h ++ )
        {
            if ( left_hash [ h ] != right_hash [ h ] )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * <p>
     * Returns a String representation of the specified object's hash.
     * </p>
     *
     * @param object The object whose hash will be represented as a String.
     *               CAN be null.
     *
     * @return The String representation of the specified hash.
     *         Never null.
     */
    protected final String hashString (
            Object object
            )
    {
        final int [] hash = this.hash ( object );

        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Hash:" );

        for ( int hash_element : hash )
        {
            sbuf.append ( " " + hash_element );
        }

        return sbuf.toString ();
    }


    // Returns the h'th signature of an object with the specified hashes
    private final int signature (
            int [] hashes,
            int hashes_summed,
            int h
            )
    {
        final int signature =
            // Bits 0 - 9:
            // Which hash (# 0-9) this bucket # is stored in.
            ( 1 << h )
            // Bits 10-19:
            // Which hash (# 0-9) this bucket # is stored in, bits reversed.
            | ( 1 << ( 19 - h ) )
            // Bits 20-29:
            // Each bucket # modulo 2.
            | ( ( hashes [ 0 ] % 2 ) << 20 )
            | ( ( hashes [ 1 ] % 2 ) << 21 )
            | ( ( hashes [ 2 ] % 2 ) << 22 )
            | ( ( hashes [ 3 ] % 2 ) << 23 )
            | ( ( hashes [ 4 ] % 2 ) << 24 )
            | ( ( hashes [ 5 ] % 2 ) << 25 )
            | ( ( hashes [ 6 ] % 2 ) << 26 )
            | ( ( hashes [ 7 ] % 2 ) << 27 )
            | ( ( hashes [ 8 ] % 2 ) << 28 )
            | ( ( hashes [ 9 ] % 2 ) << 29 )
            // Bit 30:
            | ( ( h % 2 ) << 30 )
            // Bit 31:
            | ( ( hashes_summed % 2 ) << 31 );

        /*
        final StringBuilder sbuf = new StringBuilder ();  final int positive; if ( signature < 0 ) { positive = signature & ( 0b01111111111111111111111111111111 ); sbuf.append ( " 1" ); } else { positive = signature; sbuf.append ( " 0" ); }; for ( int ttt = 30; ttt >= 0; ttt -- ) { final int shifted = ( positive >> ttt );  final int bit = shifted & 1; if ( ( ttt % 10 ) == 9 ) { sbuf.append ( " |" ); } sbuf.append ( " " + bit ); }; System.out.println ( "!!! SIG: "+ sbuf.toString () );
        */
        return signature;
    }


    // Adds up all of the hashes.
    private final int sum (
            int [] hashes
            )
    {
        int sum = 0;
        for ( int hash : hashes )
        {
            sum += hash;
        }

        return sum;
    }






    // @return True if the specified array contains the specified value;
    //         false if not.  Used for quickie tests, to verify results.
    private static final boolean arrayContains ( Integer [] array, int value )
    {
        for ( Integer element : array )
        {
            if ( element == null )
            {
                // Assume that the first null indicates the rest
                // of the elements will all be nulls, and abort.
                return false;
            }
            else if ( element.intValue () == value )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * <p>
     * Quickie tests of the BloomFilter: sets of 1-65536 members.
     * </p>
     */
    public static void main (
            String [] args 
            )
    {
        for ( int length = 1;
              length <= 65536;
              length *= 2 )
        {
            System.out.println ( "Length = " + length + " x 2:" );

            int next;

            final Integer [] left = new Integer [ length ];
            next = 0;
            for ( int l = 0; l < left.length; l ++ )
            {
                int current = next % length;
                while ( BloomFilter.arrayContains ( left, current ) )
                {
                    current = ( current + 1 ) % length;
                }

                left [ l ] = new Integer ( current );

                if ( next == 0 )
                {
                    next = 1;
                }
                else
                {
                    next *= 7;
                }
            }

            final Integer [] right = new Integer [ length ];
            next = 0;
            for ( int r = 0; r < right.length; r ++ )
            {
                int current = next % length;
                while ( BloomFilter.arrayContains ( right, current ) )
                {
                    current = ( current + 1 ) % length;
                }

                right [ r ] = new Integer ( current );

                if ( next == 0 )
                {
                    next = 1;
                }
                else
                {
                    next *= 5;
                }
            }

            final int max = 65536;
            final int num_buckets;
            if ( ( length * 15 ) < max )
            {
                num_buckets = length * 15;
            }
            else
            {
                num_buckets = max;
            }

            final BloomFilter left_filter = new BloomFilter ( num_buckets );
            final BloomFilter right_filter = new BloomFilter ( num_buckets - 7 );

            for ( Integer left_element : left )
            {
                left_filter.add ( left_element );
            }

            final List<Integer> intersection = new ArrayList<Integer> ();
            final List<Integer> difference = new ArrayList<Integer> ();
            for ( Integer right_element : right )
            {
                right_filter.add ( right_element );

                if ( left_filter.contains ( right_element ) )
                {
                    intersection.add ( right_element );
                }
                else
                {
                    difference.add ( right_element );
                }
            }

            for ( Integer left_element : left )
            {
                if ( right_filter.contains ( left_element ) )
                {
                    intersection.add ( left_element );
                }
                else
                {
                    difference.add ( left_element );
                }
            }

            Collections.sort ( difference );
            Collections.sort ( intersection );

            boolean is_ok = true;

            for ( int d = 1; d < difference.size (); d ++ )
            {
                if ( difference.get ( d - 1 ).equals ( difference.get ( d ) ) )
                {
                    final Integer bad = difference.get ( d );
                    final int [] bad_left_hash = left_filter.hash ( bad );
                    final int [] bad_right_hash = right_filter.hash ( bad );
                    final int [] bad_left_matches =
                        new int [ bad_left_hash.length ];
                    final int [] bad_right_matches =
                        new int [ bad_right_hash.length ];
                    final String hash_left_string =
                        left_filter.hashString ( bad );
                    final String hash_right_string =
                        right_filter.hashString ( bad );
                    System.out.println ( "    ERROR false difference: "
                                         + bad );

                    /*
                    final StringBuilder matches = new StringBuilder ();
                    for ( Integer left_element : left )
                    {
                        final int [] maybe_common =
                            left_filter.hash ( left_element );
                        final List<Integer> common_hashes =
                            new ArrayList<Integer> ();
                        for ( int h = 0; h < bad_left_hash.length; h ++ )
                        {
                            for ( int mc = 0; mc < maybe_common.length; mc ++ )
                            {
                                if ( maybe_common [ mc ]
                                     == bad_left_hash [ h ] )
                                {
                                    common_hashes.add ( mc );
                                    bad_left_matches [ h ] ++;
                                }
                            }
                        }

                        if ( common_hashes.size () == 0 )
                        {
                            continue;
                        }
                    }

                    for ( Integer right_element : right )
                    {
                        final int [] maybe_common =
                            right_filter.hash ( right_element );
                        final List<Integer> common_hashes =
                            new ArrayList<Integer> ();
                        for ( int h = 0; h < bad_right_hash.length; h ++ )
                        {
                            for ( int mc = 0; mc < maybe_common.length; mc ++ )
                            {
                                if ( maybe_common [ mc ]
                                     == bad_right_hash [ h ] )
                                {
                                    common_hashes.add ( mc );
                                    bad_right_matches [ h ] ++;
                                }
                            }
                        }

                        if ( common_hashes.size () == 0 )
                        {
                            continue;
                        }
                    }

                    matches.append ( "          Bucket match counts LEFT :" );
                    for ( int h = 0; h < bad_left_matches.length; h ++ )
                    {
                        matches.append ( " " + bad_left_matches [ h ] );
                    }
                    matches.append ( "\n" );

                    matches.append ( "          Bucket match counts RIGHT:" );
                    for ( int h = 0; h < bad_right_matches.length; h ++ )
                    {
                        matches.append ( " " + bad_right_matches [ h ] );
                    }
                    */

                    System.out.println ( "          "
                                         + "Left " + hash_left_string );
                    System.out.println ( "          "
                                         + "Right " + hash_right_string );
                    /*
                    System.out.println ( "" + matches.toString () );
                    */

                    is_ok = false;
                }
            }

            if ( ( intersection.size () % 2 ) != 0 )
            {
                System.out.println ( "    ERROR Dangling intersection # "
                                     + ( ( intersection.size () + 1 ) / 2 )
                                     + " = "
                                     + intersection.get ( intersection.size () - 1 ) );
            }

            if ( ( difference.size () + intersection.size () )
                 != ( 2 * length ) )
            {
                System.out.println ( "    ERROR impossible # of elements "
                                     + ( difference.size ()
                                         + intersection.size () )
                                     + " should be "
                                     + ( 2 * length ) );
            }

            for ( int i = 1; i <= intersection.size (); i += 2 )
            {
                if ( i == intersection.size ()
                     || ! intersection.get ( i - 1 ).equals ( intersection.get ( i ) ) )
                {
                    final Integer bad = intersection.get ( i - 1 );
                    final int [] bad_left_hash = left_filter.hash ( bad );
                    final int [] bad_right_hash = right_filter.hash ( bad );
                    final int [] bad_left_matches =
                        new int [ bad_left_hash.length ];
                    final int [] bad_right_matches =
                        new int [ bad_right_hash.length ];
                    final String hash_left_string =
                        left_filter.hashString ( bad );
                    final String hash_right_string =
                        left_filter.hashString ( bad );
                    System.out.println ( "    ERROR false intersection: "
                                         + bad );
                    i --;

                    System.out.println ( "        "
                                         + "LEFT  buckets contains it? "
                                         + left_filter.contains ( bad )
                                         + " RIGHT buckets contains it? "
                                         + right_filter.contains ( bad ) );
                    boolean is_in_left = false;
                    for ( Integer maybe_in_left : left )
                    {
                        if ( maybe_in_left.equals ( bad ) )
                        {
                            System.out.println ( "        "
                                                 + "LEFT  contains integer" );
                            is_in_left = true;
                            break;
                        }
                    }
                    if ( ! is_in_left )
                    {
                        System.out.println ( "        "
                                             + "LEFT  does NOT contain integer" );
                    }

                    boolean is_in_right = false;
                    for ( Integer maybe_in_right : right )
                    {
                        if ( maybe_in_right.equals ( bad ) )
                        {
                            System.out.println ( "        "
                                                 + "RIGHT contains integer" );
                            is_in_right = true;
                            break;
                        }
                    }
                    if ( ! is_in_right )
                    {
                        System.out.println ( "        "
                                             + "RIGHT does NOT contain integer" );
                    }

                    /*
                    final StringBuilder matches = new StringBuilder ();
                    for ( Integer left_element : left )
                    {
                        final int [] maybe_common =
                            left_filter.hash ( left_element );
                        final List<Integer> common_hashes =
                            new ArrayList<Integer> ();
                        for ( int h = 0; h < bad_left_hash.length; h ++ )
                        {
                            for ( int mc = 0; mc < maybe_common.length; mc ++ )
                            {
                                if ( maybe_common [ mc ]
                                     == bad_left_hash [ h ] )
                                {
                                    common_hashes.add ( mc );
                                    bad_left_matches [ h ] ++;
                                }
                            }
                        }

                        if ( common_hashes.size () == 0 )
                        {
                            continue;
                        }
                    }

                    for ( Integer right_element : right )
                    {
                        final int [] maybe_common =
                            right_filter.hash ( right_element );
                        final List<Integer> common_hashes =
                            new ArrayList<Integer> ();
                        for ( int h = 0; h < bad_right_hash.length; h ++ )
                        {
                            for ( int mc = 0; mc < maybe_common.length; mc ++ )
                            {
                                if ( maybe_common [ mc ]
                                     == bad_right_hash [ h ] )
                                {
                                    common_hashes.add ( mc );
                                    bad_right_matches [ h ] ++;
                                }
                            }
                        }

                        if ( common_hashes.size () == 0 )
                        {
                            continue;
                        }
                    }

                    matches.append ( "          Bucket match counts LEFT :" );
                    for ( int h = 0; h < bad_left_matches.length; h ++ )
                    {
                        matches.append ( " " + bad_left_matches [ h ] );
                    }
                    matches.append ( "\n" );

                    matches.append ( "          Bucket match counts RIGHT:" );
                    for ( int h = 0; h < bad_right_matches.length; h ++ )
                    {
                        matches.append ( " " + bad_right_matches [ h ] );
                    }
                    */

                    System.out.println ( "          "
                                         + "Left " + hash_left_string );
                    System.out.println ( "          "
                                         + "Right " + hash_right_string );
                    /*
                    System.out.println ( "" + matches.toString () );
                    */

                    is_ok = false;
                }
            }

            System.out.println ( "    "
                                 + difference.size () + " differences,"
                                 + " "
                                 + ( intersection.size () / 2 )
                                 + " intersections");
        }
    }
}
