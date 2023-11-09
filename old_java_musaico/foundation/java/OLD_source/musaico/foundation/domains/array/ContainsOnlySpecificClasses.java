package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all arrays and collections which do not contain any
 * instances of one or more specific classes.
 * </p>
 *
 * <p>
 * For example, the domain of all arrays and collections of
 * Numbers which do not include any BigDecimals.
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
public class ContainsOnlySpecificClasses
    extends AbstractArrayDomain
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The classes which are not allowed among the elements of
     *  values of this domain. */
    private final Class<?> [] requiredClasses;


    /**
     * <p>
     * Creates a new ContainsOnlySpecificClasses for the specified
     * classes.
     * </p>
     *
     * <p>
     * Only arrays and Collections which do NOT contain any instances
     * of the specified classes will be members of this domain.
     * </p>
     *
     * @param required_classes The classes to exclude from this domain.
     *                         Must not be null.  Must not contain any
     *                         null elements.
     */
    public ContainsOnlySpecificClasses (
                                        Class<?> ... required_classes
                                        )
    {
        boolean is_valid = true;
        if ( required_classes == null
             || required_classes.length == 0 )
        {
            is_valid = false;
        }
        else
        {
            for ( Class<?> required_class : required_classes )
            {
                if ( required_class == null )
                {
                    is_valid = false;
                    break;
                }
            }

        }

        if ( ! is_valid )
        {
            throw new IllegalArgumentException ( "Cannot create an ContainsOnlySpecificClasses from classes { " + ContainsOnlySpecificClasses.classesToString ( required_classes ) + " }" );
        }

        this.requiredClasses = new Class<?> [ required_classes.length ];
        System.arraycopy ( required_classes, 0,
                           this.requiredClasses, 0, required_classes.length );
    }


    /**
     * <p>
     * Makes a nice human-readable String out of this domain's
     * required classes.  Never null.
     * </p>
     */
    public final String classesToString ()
    {
        return ContainsOnlySpecificClasses.classesToString (
            this.requiredClasses );
    }


    /**
     * @return A nice human-readable String out of this domain's
     *         required classes.  Never null.
     */
    public static final String classesToString (
                                                Class<?> [] required_classes
                                                )
    {
        if ( required_classes == null )
        {
            return "";
        }

        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first_class = true;
        for ( Class<?> required_class : required_classes )
        {
            if ( is_first_class )
            {
                is_first_class = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            if ( required_class == null )
            {
                sbuf.append ( "null" );
            }
            else
            {
                sbuf.append ( "" + ClassName.of ( required_class ) );
            }
        }

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#equalsDetails(musaico.foundation.domains.array.AbstractArrayDomain)
     */
    @Override
    protected final boolean equalsDetails(
                                          AbstractArrayDomain object
                                          )
    {
        final ContainsOnlySpecificClasses that =
            (ContainsOnlySpecificClasses) object;
        return Arrays.equals ( this.requiredClasses, that.requiredClasses );
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#filterElements(musaico.foundation.domains.array.Elements, java.util.BitSet, boolean)
     */
    @Override
    public final FilterState filterElements (
            Elements<?> array,
            BitSet kept_elements,
            boolean is_abort_on_discard
            )
    {
        final long length = array.length ();

        FilterState result = FilterState.KEPT;
        for ( long e = 0L; e < length; e ++ )
        {
            final Object element = array.at ( e ) [ 0 ];
            boolean is_instance = false;
            for ( Class<?> required_class : this.requiredClasses )
            {
                if ( required_class.isInstance ( element ) )
                {
                    is_instance = true;
                    break;
                }
            }

            kept_elements.set ( (int) e, is_instance );

            if ( ! is_instance )
            {
                result = FilterState.DISCARDED;
                if ( is_abort_on_discard )
                {
                    return result;
                }
            }
        }

        return result;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#hashCodeDetails()
     */
    @Override
    protected final int hashCodeDetails ()
    {
        return Arrays.hashCode ( this.requiredClasses );
    }


    /**
     * @return The set of Classes, one of which must be the class of
     *         each element in an array in this domain.
     *         A defensive copy is made, so you can modify the
     *         array to your heart's content.  Never null.
     *         Never contains any null elements.
     */
    public final Class<?> [] requiredClasses ()
    {
        final Class<?> [] required_classes =
            new Class<?> [ this.requiredClasses.length ];
        System.arraycopy ( this.requiredClasses, 0,
                           required_classes, 0, this.requiredClasses.length );

        return required_classes;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#toStringDetails(java.lang.String)
     */
    @Override
    protected String toStringDetails (
                                      String class_name
                                      )
    {
        return class_name
            + " { "
            + this.classesToString ()
            + " }";
    }
}
