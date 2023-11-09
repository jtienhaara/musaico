package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.membership.InstanceOf;

import musaico.foundation.filter.stream.KeptNoElements;


/**
 * <p>
 * A Filter that keeps any container that has no elements that are
 * instances of a specific class or classes.
 * </p>
 *
 * <p>
 * For example, an ExcludesClasses filter that excludes
 * <code> { String.class, Integer.class } </code> would discard the
 * containers <code> { "a", "b", "c" } </code>,
 * <code> { 1, 2, 3 } </code> and <code> { "a", 1, new Date () } </code>
 * because each has at least one instance of an excluded class,
 * but keep the container
 * <code> { new Date ( 123L ), new BigDecimal ( "1.23" ) } </code>
 * because it has no instances of any excluded classes.
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
public class ExcludesClasses<ELEMENT extends Object>
    extends AllElementsFilter<InstanceOf<ELEMENT>, ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ExcludesClasses filter.
     * </p>
     *
     * @param excluded_classes The classes whose instances will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being DISCARDED by this filter.
     *                         See the notes about nulls in the
     *                         InstanceOf constructor.
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     */
    public ExcludesClasses (
            Class<?> ... excluded_classes
            )
    {
        // The qualifier keeps every excluded instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        this (
            new InstanceOf<ELEMENT> ( excluded_classes ) // qualifier
            );
    }


    /**
     * <p>
     * Creates a new ExcludesClasses filter.
     * </p>
     *
     * @param excluded_classes The Collection of classes whose instances
     *                         will, when they are elements of
     *                         a container passed to this filter, result
     *                         in the container being DISCARDED
     *                         by this filter.
     *                         See the notes about nulls in the
     *                         InstanceOf constructor.
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     */
    public ExcludesClasses (
            Collection<Class<?>> excluded_classes
            )
    {
        // The qualifier keeps every excluded instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        this (
            new InstanceOf<ELEMENT> ( excluded_classes ) // qualifier
            );
    }


    /**
     * <p>
     * Creates a new ExcludesClasses filter.
     * </p>
     *
     * @param excluded_classes The Iterable classes whose instances
     *                         will, when they are elements of
     *                         a container passed to this filter, result
     *                         in the container being DISCARDED
     *                         by this filter.
     *                         See the notes about nulls in the
     *                         InstanceOf constructor.
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     */
    public ExcludesClasses (
            Iterable<Class<?>> excluded_classes
            )
    {
        // The qualifier keeps every excluded instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        this (
            new InstanceOf<ELEMENT> ( excluded_classes ) // qualifier
            );
    }


    /**
     * <p>
     * Creates a new ExcludesClasses filter.
     * </p>
     *
     * @param qualifier The InstanceOf filter for each element of
     *                       each container passed to this filter.  When
     *                       a container contains at least one element
     *                       that is KEPT by the qualifier, then
     *                       the whole container will be DISCARDED.  If null,
     *                       then a default element filter will be used
     *                       so that all non-empty containers will
     *                       be DISCARDED.  (DO NOT PASS NULL.)
     */
    public ExcludesClasses (
        InstanceOf<ELEMENT> qualifier
        )
    {
        // The qualifier keeps every excluded instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        super ( qualifier == null
                    ? new InstanceOf<ELEMENT> ( Object.class ) // default qualifier: keep every element.
                    : qualifier,
                new KeptNoElements<ELEMENT> () ); // quantifier: only keep the container if no elements were KEPT.
    }
}
