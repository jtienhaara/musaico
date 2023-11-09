package musaico.foundation.domains.generators;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Generator;


/**
 * <p>
 * Generates Long numbers from 0L to (length - 1L).
 * </p>
 *
 * <p>
 * Every Generator is immutable and idempotent.
 * </p>
 *
 *
 * <p>
 * In Java every Generator must implement <code> equals ( ... ) </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Generator must be Serializable in order to
 * play nicely across RMI.  However users of the Generator
 * must be careful, since the elements and any other data or metadata
 * stored inside might not be Serializable.
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
 * @see musaico.foundation.domains.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.elements.MODULE#LICENSE
 */
public class IndexGenerator
    implements Generator<Long>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;




    /** The comparator / order for indices (ascending long integers). */
    public static final IndexGenerator.IndexComparator ORDER =
        new IndexGenerator.IndexComparator ();




    /**
     * <p>
     * The order in which indexes are generated (0L, 1L, ... length - 1).
     * Compares using <code> left.compareTo ( right ) </code>.
     * </p>
     */
    private static class IndexComparator
        implements Comparator<Long>, Serializable
    {
        private static final long serialVersionUID =
            IndexGenerator.serialVersionUID;

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public final int compare (
            Long left,
            Long right
            )
        {
            if ( left == null )
            {
                if ( right == null )
                {
                    return 0;
                }
                else
                {
                    return Integer.MAX_VALUE;
                }
            }
            else if ( right == null )
            {
                return Integer.MIN_VALUE + 1;
            }

            final int comparison = left.compareTo ( right );

            return comparison;
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public final boolean equals (
                Object object
                )
        {
            if ( object == null )
            {
                return false;
            }
            else if ( object == this )
            {
                return true;
            }
            else if ( this.getClass () != object.getClass () )
            {
                return false;
            }

            return true;
        }
    }




    // The number of indices to be generated (0 to [length - 1]).
    private final long length;


    /**
     * <p>
     * Creates a new IndexGenerator for the specified number of indices.
     * </p>
     *
     * @param length The number of indices to generate.
     *               Must be greater than or equal to 0L.
     *
     * @throws IndexOutOfBoundsException If the specified length
     *                                   is less than 0L.
     */
    public IndexGenerator (
            long length
            )
        throws IndexOutOfBoundsException
    {
        if ( length < 0L )
        {
            throw new IndexOutOfBoundsException ( "ERROR "
                                                  + ClassName.of ( this.getClass () )
                                                  + " must have a length"
                                                  + " >= 0 but was"
                                                  + " constructed with length "
                                                  + length );
        }

        this.length = length;
    }


    /**
     * @see musaico.foundation.domains.elements.Generator#at(long)
     */
    @Override
    public final Long [] at (
            long index
            )
    {
        if ( index < 0L
             || index >= this.length )
        {
            return new Long [ 0 ];
        }
        else
        {
            return new Long [] { index };
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final IndexGenerator that = (IndexGenerator) object;

        if ( that.length != this.length )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return (int) this.length;
    }


    /**
     * @see musaico.foundation.domains.elements.Generator#length()
     */
    @Override
    public final long length ()
    {
        return this.length;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " [ " + this.length + " ]";
    }
}
