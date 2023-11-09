package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.composite.And;

import musaico.foundation.filter.stream.KeptAllElements;


/**
 * <p>
 * A Filter that keeps any container whose elements all
 * match a specific qualifier Filter.
 * </p>
 *
 * <p>
 * For example, an IncludesOnly that includes numbers matched by the
 * <code> GreaterThanOrEqualToZero.FILTER </code> will keep a container
 * with only non-negative elements, but discard any container with one
 * or more negative number elements.
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
 * @see musaico.foundation.filter.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.elements.MODULE#LICENSE
 */
public class IncludesOnly<ELEMENT extends Object>
    extends AllElementsFilter<Filter<ELEMENT>, ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Includes filter from zero or more
     * element qualifiers.
     * </p>
     *
     * @param qualifiers The filter(s) for each element of
     *                   each container passed to this filter.  When
     *                   a container contains at least one element
     *                   that is KEPT all the qualifiers, then
     *                   the whole container will be KEPT.  If null,
     *                   then a default element filter will be used
     *                   so that all elements of all containers will
     *                   be KEPT.  (DO NOT PASS NULL.)
     */
    @SafeVarargs
    @SuppressWarnings("varargs") // Heap pollution parameterized vararg type
    public IncludesOnly (
        Filter<ELEMENT> ... qualifiers
        )
    {
        this ( IncludesOnly.oneQualifier ( qualifiers ) );
    }


    // Creates one qualifier Filter out of the specified
    // element qualifier(s).
    @SafeVarargs
    @SuppressWarnings("varargs") // Heap pollution parameterized vararg type
    private static final <FILTER_ELEMENT extends Object>
        Filter<FILTER_ELEMENT> oneQualifier (
            Filter<FILTER_ELEMENT> ... qualifiers
            )
    {
        final Filter<FILTER_ELEMENT> one_qualifier;
        if ( qualifiers == null
             || qualifiers.length == 0 )
        {
            one_qualifier = new KeepAll<FILTER_ELEMENT> ();
        }
        else if ( qualifiers.length == 1 )
        {
            one_qualifier = qualifiers [ 0 ];
        }
        else
        {
            one_qualifier = new And<FILTER_ELEMENT> ( qualifiers );
        }

        return one_qualifier;
    }


    /**
     * <p>
     * Creates a new IncludesOnly filter.
     * </p>
     *
     * @param qualifier The filter for each element of
     *                  each container passed to this filter.  When
     *                  a container's elements are all KEPT
     *                  by the qualifier, then
     *                  the whole container will be KEPT.  If null,
     *                  then a default element filter will be used
     *                  so that all elements of all containers will
     *                  be KEPT.  (DO NOT PASS NULL.)
     */
    public IncludesOnly (
        Filter<ELEMENT> qualifier
        )
    {
        // The qualifier keeps every included instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        super ( qualifier == null
                    ? new KeepAll<ELEMENT> () // default qualifier: keep every element.
                    : qualifier,
                new KeptAllElements<ELEMENT> () ); // quantifier: only keep the container if all elements were KEPT.
    }
}
