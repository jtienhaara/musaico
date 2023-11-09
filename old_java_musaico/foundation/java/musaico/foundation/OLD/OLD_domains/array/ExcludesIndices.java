package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.ArrayElements;
import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all single objects, arrays and iterables which do NOT
 * contain specific indices.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
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
 * @see musaico.foundation.domains.array.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.array.MODULE#LICENSE
 */
public class ExcludesIndices
    implements ArrayDomain, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The indices which must be excluded from the indexable elements
    // of every array / collection in this domain.
    private final long [] indices;


    /**
     * <p>
     * Creates a new domain of arrays/collections which do not contain
     * any of the specified indices.
     * </p>
     *
     * @param indices The indices which must be excluded from the indexable
     *                elements of every array/collection in this domain.
     *                Must not be null.
     */
    public ExcludesIndices (
                            int ... indices
                            )
        throws NullPointerException
    {
        if ( indices == null )
        {
            throw new NullPointerException ( "Cannot create a ExcludesIndices ( " + indices + " )" );
        }

        this.indices = new long [ indices.length ];
        for ( int i = 0; i < indices.length; i ++ )
        {
            this.indices [ i ] = (long) indices [ i ];
        }
    }


    /**
     * <p>
     * Creates a new domain of arrays/collections which do not contain
     * any of the specified indices.
     * </p>
     *
     * @param indices The indices which form a subset of the indexable
     *                elements of every array/collection in this domain.
     *                Must not be null.
     */
    public ExcludesIndices (
                            long ... indices
                            )
        throws NullPointerException
    {
        if ( indices == null )
        {
            throw new NullPointerException ( "Cannot create a ExcludesIndices ( " + indices + " )" );
        }

        this.indices = new long[ indices.length ];
        System.arraycopy ( indices, 0,
                           this.indices, 0, indices.length );
    }


    /**
     * @return The bitset indicating which required indices are
     *         contained in the specified array (true), and which
     *         ones are missing (false).  The Nth bit corresponds
     *         to the Nth required index.  Never null.
     */
    private final BitSet containedIndices (
            Elements<?> array
            )
    {
        final BitSet found_flags = new BitSet ( this.indices.length );

        // When we find all of the required indices in the array,
        // we might as well stop searching.
        int remaining = this.indices.length;

        if ( array == null )
        {
            return found_flags;
        }

        final long length = array.length ();
        for ( int i = 0; i < this.indices.length; i ++ )
        {
            final long required_index = this.indices [ i ];
            if ( required_index >= 0L
                 && required_index < length )
            {
                found_flags.set ( i, true );
            }
        }

        return found_flags;
    }


    /**
     * @return The indices in this array domain.  Never null.
     *         Never contains any null indices.
     */
    public final long [] indices ()
    {
        final long [] defensive_copy = new long [ this.indices.length ];
        System.arraycopy ( this.indices, 0,
                           defensive_copy, 0, this.indices.length );

        return defensive_copy;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        final ExcludesIndices that = (ExcludesIndices) obj;
        if ( this.indices == null )
        {
            if ( that.indices != null )
            {
                return false;
            }
        }
        else if ( that.indices == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.indices, that.indices ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            Elements<?> array
            )
    {
        final BitSet found_flags =
            this.containedIndices ( array );

        for ( int i = 0; i < this.indices.length; i ++ )
        {
            if ( found_flags.get ( i ) )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ()
            + Arrays.hashCode ( this.indices );
    }


    /**
     * @see musaico.foundation.filter.Domain#member(java.lang.Object)
     */
    @Override
        public final List<Elements<?>> member (
            Elements<?> maybe_member
            )
    {
        final List<Elements<?>> members =
            new ArrayList<Elements<?>> ();
        if ( this.filter ( maybe_member ).isKept () )
        {
            members.add ( maybe_member );
        }

        return members;
    }


    /**
     * @return The required indices which are missing from each
     *         non-member array.
     *
     * @see musaico.foundation.domains.array.ArrayDomain#nonMember(java.lang.Object)
     */
    @Override
        public final List<Elements<?>> nonMember (
            Elements<?> maybe_non_member
            )
    {
        final List<Elements<?>> non_members =
            new ArrayList<Elements<?>> ();
        final BitSet found_flags =
            this.containedIndices ( maybe_non_member );
        final List<Long> disallowed_indices =
            new ArrayList<Long> ();
        for ( int e = 0; e < this.indices.length; e ++ )
        {
            if ( found_flags.get ( e ) )
            {
                disallowed_indices.add ( this.indices [ e ] );
            }
        }

        if ( disallowed_indices.size () > 0 )
        {
            // Only add an array of the disallowed indices to the result.
            non_members.add (
                             new ArrayElements<Long> ( Long.class,
                                                       disallowed_indices )
                             );
        }

        return non_members;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this ) );
        sbuf.append ( " {" );
        boolean is_first_index = true;
        for ( long index : this.indices )
        {
            if ( is_first_index )
            {
                is_first_index = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + index );
        }

        if ( ! is_first_index )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
