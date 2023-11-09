package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.BitSet;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all objects, arrays and collections which do not
 * contain any null elements.
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
public class NoNulls
    extends AbstractArrayDomain
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The NoNulls domain singleton. */
    public static final NoNulls DOMAIN =
        new NoNulls ();


    protected NoNulls ()
    {
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#equalsDetails(musaico.foundation.domains.array.AbstractArrayDomain)
     */
    @Override
    protected final boolean equalsDetails(
                                          AbstractArrayDomain object
                                          )
    {
        return true;
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
            if ( element == null )
            {
                kept_elements.set ( (int) e, false );

                result = FilterState.DISCARDED;
                if ( is_abort_on_discard )
                {
                    return result;
                }
            }
            else
            {
                kept_elements.set ( (int) e, true );
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
        return 0;
    }


    /**
     * @see musaico.foundation.domains.array.AbstractArrayDomain#toStringDetails(java.lang.String)
     */
    @Override
    protected String toStringDetails (
                                      String class_name
                                      )
    {
        return class_name;
    }
}
