package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all elements of a specific array, Collection
 * or Iterable.
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
 * across RMI.  However the user of an ElementOf domain
 * must be careful because the elements in the domain do not
 * have to be Serializable.  Attempting to serialize an ElementOf
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
public class ElementOf
    implements Domain<Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Elements of which every object in this domain is a member.
    private final Elements<Object> array;


    /**
     * <p>
     * Creates a new domain of elements of the specified array.
     * </p>
     *
     * @param element_class The class of elements in this domain.
     *                      Must not be null.
     *
     * @param array The specific array of which every object
     *              in this domain is an element.  Must not be null.
     *              Must not contain any null elements.
     */
    @SafeVarargs
    @SuppressWarnings("varargs") // ELEMENT ...
    public <ELEMENT extends Object> ElementOf (
            Class<ELEMENT> element_class,
            ELEMENT ... array
            )
        throws NullPointerException
    {
        this ( new Elements<ELEMENT []> ( element_class,
                                          array ) );
    }


    /**
     * <p>
     * Creates a new domain of elements of the specified Collection.
     * </p>
     *
     * @param element_class The class of elements in this domain.
     *                      Must not be null.
     *
     * @param collection The specific Collection of which every object
     *                   in this domain is an element.  Must not be null.
     *                   Must not contain any null elements.
     */
    public <ELEMENT extends Object> ElementOf (
            Class<ELEMENT> element_class,
            Collection<ELEMENT> collection
            )
        throws NullPointerException
    {
        this ( new Elements<Collection<ELEMENT>, ELEMENT> (
                   element_class,
                   collection ) );
    }


    /**
     * <p>
     * Creates a new domain of elements of the specified Iterable.
     * </p>
     *
     * @param element_class The class of elements in this domain.
     *                      Must not be null.
     *
     * @param Iterable The specific Iterable of which every object
     *                 in this domain is an element.  Must not be null.
     *                 Must not contain any null elements.
     */
    public <ELEMENT extends Object> ElementOf (
            Class<ELEMENT> element_class,
            Iterable<ELEMENT> iterable
            )
        throws NullPointerException
    {
        this ( new Elements<Iterable<ELEMENT>, ELEMENT> (
                   element_class,
                   iterable ) );
    }


    /**
     * <p>
     * Creates a new domain of elements of the specified Elements.
     * </p>
     *
     * @param array The specific array or Collection or Iterable
     *              of which every object in this domain is an element.
     *              Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings("unchecked") // Cast AO<?> to AO<Object>.
    public ElementOf (
            Elements<?> array
            )
        throws NullPointerException
    {
        if ( array == null )
        {
            throw new NullPointerException ( "Cannot create an ElementOf ( " + array + " )" );
        }

        this.array = (Elements<Object>) array;
    }


    /**
     * @return The specific array of elements covered by this domain.
     *         Never null.  Never contains any null elements.
     */
    @SuppressWarnings("unchecked") // Cast AO<Object> to AO<?>.
    public final Elements<?> array ()
    {
        return (Elements<?>) this.array;
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

        final ElementOf that = (ElementOf) obj;
        if ( this.array == null )
        {
            if ( that.array != null )
            {
                return false;
            }
        }
        else if ( that.array == null )
        {
            return false;
        }
        else if ( ! this.array.equals ( that.array ) )
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
            Object element
            )
    {
        if ( this.array.indexOf ( element ) < 0L )
        {
            return FilterState.DISCARDED;
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
            + this.array.hashCode ();
    }


    /**
     * @see musaico.foundation.filter.Domain#member(java.lang.Object)
     */
    @Override
    public final List<Object> member (
            Object maybe_member
            )
    {
        final List<Object> members =
            new ArrayList<Object> ();
        if ( this.array.indexOf ( maybe_member ) >= 0L )
        {
            members.add ( maybe_member );
        }

        return members;
    }


    /**
     * @see musaico.foundation.filter.Domain#nonMember(java.lang.Object)
     */
    @Override
    public final List<Object> nonMember (
            Object maybe_non_member
            )
    {
        final List<Object> non_members =
            new ArrayList<Object> ();
        if ( this.array.indexOf ( maybe_non_member ) < 0L )
        {
            non_members.add ( maybe_non_member );
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
            + this.array;
    }
}
