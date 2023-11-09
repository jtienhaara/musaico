package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.ArrayElements;
import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Provides the boilerplate code for ArrayDomains which check
 * each element of each array, independently from the other elements.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must implement equals (), hashCode () and
 * toString ().
 * </p>
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
public abstract class AbstractArrayDomain
    implements ArrayDomain, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final AbstractArrayDomain that = (AbstractArrayDomain) obj;
        return this.equalsDetails ( that );
    }


    /**
     * @return True if this AbstractArrayDomain equals the specified one,
     *         false if not.  Note that this is only ever called
     *         from AbstractArrayDomain.equals(...), so boilerplate
     *         checks like null and same class have already been performed.
     */
    protected abstract boolean equalsDetails (
                                              AbstractArrayDomain that
                                              );


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     Elements<?> array
                                     )
    {
        if ( array == null )
        {
            return FilterState.DISCARDED;
        }

        final long length = array.length ();
        if ( length < 0
             || length > Integer.MAX_VALUE )
        {
            // Can't be handled by this domain.
            return FilterState.DISCARDED;
        }

        final BitSet kept_elements = new BitSet ( (int) length );
        kept_elements.clear ();

        final FilterState result =
            this.filterElements ( array,
                                  kept_elements,
                                  true ); // is_abort_on_discard

        return result;
    }


    /**
     * <p>
     * Returns a bit set indicating which elements in the specified array
     * are disallowed, if any.
     * </p>
     *
     * <p>
     * A bitset with all true values represents an array which is a member
     * of this domain.  A bitset with one or more false values represents
     * an array which is NOT a member of this domain, because of its
     * element(s) indexed by the false flags.
     * </p>
     *
     * <p>
     * For example, if an array of 3 elements returns a bitset of
     * { true, true, true }, then the array is a member of this domain.
     * On the other hand, if an array of 3 elements returns a
     * bitset of { false, true, false }, then the array is not a member,
     * because of the 0th and 2nd elements of the array.
     * </p>
     *
     * <p>
     * Initially the bits of <code> kept_elements </code> are
     * all <code> false </code>.
     * </p>
     *
     * @param array The array to filter.  Always of <code> length () </code>
     *              greater than or equal to 0L and less than
     *              or equal to <code> (long) Integer.MAX_VALUE </code>.
     *              Must not be null.
     *
     * @param kept_elements The mutable flags representing which
     *                      elements of the specified array match
     *                      this filter, and which ones do not.
     *                      Modified by this method.  Must not be null.
     *
     * @param is_abort_on_discard True if filtering should stop at the
     *                            first element that causes the result
     *                            to be discarded; false if every element
     *                            should be filtered.  If the abort flag
     *                            is true, then the kept_elements
     *                            bitset will be incomplete, and only
     *                            the final result FilterState will
     *                            contain reliable data.  If the abort flag
     *                            if false, then both the kept_elements
     *                            bitset and the final result will be
     *                            complete and accurate.
     *
     * @return The FilterState result of filtering the elements in the
     *         specified array.  FilterState.KEPT is typically returned
     *         when this filter matches the array, and every bit
     *         in kept_elements is set to true;
     *         FilterState.DISCARDED is usually returned when this filter
     *         does not match the array, so at least one bit
     *         in kept_elements has been set to false.  Never null.
     */
    public abstract FilterState filterElements (
                                                Elements<?> array,
                                                BitSet kept_elements,
                                                boolean is_abort_on_discard
                                                );


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 17 * this.getClass ().getName ().hashCode ();
        hash_code += this.hashCodeDetails ();
        return hash_code;
    }


    /**
     * @return Extra hash code value to add to the default hash code
     *         generated by AbstractArrayDomain.  Can be 0.
     */
    protected abstract int hashCodeDetails ();


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
     * @see musaico.foundation.domains.array.ArrayDomain#nonMember(java.lang.Object)
     */
    @Override
    public final List<Elements<?>> nonMember (
            Elements<?> maybe_non_member
            )
    {
        final List<Elements<?>> non_members =
            new ArrayList<Elements<?>> ();
        final List<Object> non_member_elements =
            new ArrayList<Object> ();

        final long length = maybe_non_member.length ();
        if ( length < 0L
             || length > (long) Integer.MAX_VALUE )
        {
            non_members.add ( maybe_non_member );
            return non_members;
        }

        final BitSet kept_elements = new BitSet ( (int) length );
        this.filterElements ( maybe_non_member,
                              kept_elements,
                              false ); // is_abort_on_discard
        for ( long e = 0L; e < length; e ++ )
        {
            if ( ! kept_elements.get ( (int) e ) )
            {
                final Object [] maybe_element = maybe_non_member.at ( e );
                if ( maybe_element.length > 0 )
                {
                    final Object element = maybe_element [ 0 ];
                    non_member_elements.add ( element );
                }
            }
        }

        if ( non_member_elements.size () > 0 )
        {
            // Only add an array of the bad elements to the result.
            non_members.add (
                new ArrayElements<Object> ( Object.class,
                                            non_member_elements )
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
        final String class_name = ClassName.of ( this.getClass () );
        return this.toStringDetails ( class_name );
    }


    /**
     * <p>
     * Returns a String representation of this AbstractArrayDomain.
     * </p>
     *
     * @param class_name The printable class name of this domain, always
     *                   <code> ClassName.of ( this.getClass () ) </code>.
     *                   Must not be null.
     *
     * @return The string representation of this domain.  Often just
     *         <code> class_name <code> is returned.  Never null.
     */
    protected abstract String toStringDetails (
                                               String class_name
                                               );
}
