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
 * The domain of all single objects, arrays and Iterables which do NOT contain
 * specific elements.
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
 * across RMI.  However the user of a ExcludesElements domain
 * must be careful because the elements in the domain do not
 * have to be Serializable.  Attempting to serialize a ExcludesElements
 * domain with non-Serializable elements will cause an exception.
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
public class ExcludesElements
    implements ArrayDomain, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The elements which must be excluded from every array / collection
    // in this domain.
    private final Object [] elements;


    /**
     * <p>
     * Creates a new domain of arrays/collections which do not contain
     * any of the specified elements.
     * </p>
     *
     * @param elements The elements which must be excluded from every
     *                 array/collection in this domain.  Must not be null.
     */
    public ExcludesElements (
                             Object ... elements
                             )
        throws NullPointerException
    {
        if ( elements == null )
        {
            throw new NullPointerException ( "Cannot create a ExcludesElements ( " + elements + " )" );
        }

        this.elements = new Object [ elements.length ];
        System.arraycopy ( elements, 0,
                           this.elements, 0, elements.length );
    }


    /**
     * @return The bitset indicating which disallowed elements are
     *         contained in the specified array (true), and which
     *         ones are missing (false).  The Nth bit corresponds
     *         to the Nth verboten element.  Never null.
     */
    private final BitSet containedElements (
            Elements<?> array
            )
    {
        final BitSet found_flags = new BitSet ( this.elements.length );

        // When we find all of the disalllowed elements in the array,
        // we might as well stop searching.
        int remaining = this.elements.length;

        if ( array == null )
        {
            return found_flags;
        }

        for ( Object element : array )
        {
            for ( int i = 0; i < this.elements.length; i ++ )
            {
                if ( found_flags.get ( i ) )
                {
                    continue;
                }

                final Object included = this.elements [ i ];
                if ( included == null )
                {
                    if ( element == null )
                    {
                        found_flags.set ( i, true );
                        remaining --;

                        if ( remaining == 0 )
                        {
                            return found_flags;
                        }
                    }
                }
                else if ( included.equals ( element )  )
                {
                    found_flags.set ( i, true );
                    remaining --;

                    if ( remaining == 0 )
                    {
                        return found_flags;
                    }
                }
            }
        }

        return found_flags;
    }


    /**
     * @return The elements in this array domain.  Never null.
     *         Never contains any null elements.
     */
    public final Object [] elements ()
    {
        final Object [] defensive_copy = new Object [ this.elements.length ];
        System.arraycopy ( this.elements, 0,
                           defensive_copy, 0, this.elements.length );

        return defensive_copy;
    }


    /**
     * <p>
     * Makes a nice human-readable String out of the elements which are
     * never found in any array belonging to this domain.  Never null.
     * </p>
     */
    public final String elementsToString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( " {" );
        boolean is_first_element = true;
        for ( Object element : this.elements )
        {
            if ( is_first_element )
            {
                is_first_element = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + element );
        }

        if ( ! is_first_element )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
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

        final ExcludesElements that = (ExcludesElements) obj;
        if ( this.elements == null )
        {
            if ( that.elements != null )
            {
                return false;
            }
        }
        else if ( that.elements == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.elements, that.elements ) )
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
            this.containedElements ( array );

        for ( int i = 0; i < this.elements.length; i ++ )
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
            + Arrays.hashCode ( this.elements );
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
     * @return The disallowed elements which are present in each
     *         non-member array.
     *
     * @see musaico.foundation.domains.array.ArrayDomain#nonMember(java.lang.Object[])
     */
    @Override
        public final List<Elements<?>> nonMember (
            Elements<?> maybe_non_member
            )
    {
        final List<Elements<?>> non_members =
            new ArrayList<Elements<?>> ();
        final BitSet found_flags =
            this.containedElements ( maybe_non_member );
        final List<Object> disallowed_elements =
            new ArrayList<Object> ();
        for ( int e = 0; e < this.elements.length; e ++ )
        {
            if ( found_flags.get ( e ) )
            {
                disallowed_elements.add ( this.elements [ e ] );
            }
        }

        if ( disallowed_elements.size () > 0 )
        {
            // Only add an array of the disallowed elements to the result.
            non_members.add (
                             new ArrayElements<Object> ( Object.class,
                                                         disallowed_elements )
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
        return ClassName.of ( this )
            + " "
            + this.elementsToString ();
    }
}
