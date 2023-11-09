package musaico.foundation.domains.array;

import java.io.Serializable;

import java.util.BitSet;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all objects, arrays and iterables which do not contain any
 * duplicate elements.
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
public class NoDuplicates
    extends AbstractArrayDomain
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The NoDuplicates domain singleton. */
    public static final NoDuplicates DOMAIN =
        new NoDuplicates ();


    protected NoDuplicates ()
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

        for ( long e = 0L; e < length; e ++ )
        {
            kept_elements.set ( (int) e, true );
        }

        FilterState result = FilterState.KEPT;
        for ( long e1 = 0L; e1 < length; e1 ++ )
        {
            final Object element1 = array.at ( e1 ) [ 0 ];

            for ( long e2 = e1 + 1L; e2 < length; e2 ++ )
            {
                final Object element2 = array.at ( e2 ) [ 0 ];

                if ( element1 == null )
                {
                    if ( element2 == null )
                    {
                        // Duplicate nulls.
                        kept_elements.set ( (int) e1, false );
                        kept_elements.set ( (int) e2, false );

                        result = FilterState.DISCARDED;
                        if ( is_abort_on_discard )
                        {
                            return result;
                        }
                    }
                }
                else if ( element2 != null
                          && element1.equals ( element2 ) )
                {
                    // Duplicate non-null elements.
                    kept_elements.set ( (int) e1, false );
                    kept_elements.set ( (int) e2, false );

                    result = FilterState.DISCARDED;
                    if ( is_abort_on_discard )
                    {
                        return result;
                    }
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
