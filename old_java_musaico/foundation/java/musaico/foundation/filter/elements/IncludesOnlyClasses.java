package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.membership.InstanceOf;

import musaico.foundation.filter.stream.KeptAllElements;


/**
 * <p>
 * A Filter that keeps any container whose elements are all instances
 * of a specific class or classes.
 * </p>
 *
 * <p>
 * For example, an IncludesOnlyClasses filter that includes
 * <code> { String.class, Integer.class } </code> would keep the
 * containers <code> { "a", "b", "c" } </code> and
 * <code> { 1, 2, 3 } </code> because each container's elements
 * are all instances, but discard the containers
 * <code> { "a", 1, new Date () } </code> and
 * <code> { new Date ( 123L ), new BigDecimal ( "1.23" ) } </code>
 * because each has at least one element that is not an instance.
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
public class IncludesOnlyClasses<ELEMENT extends Object>
    extends AllElementsFilter<InstanceOf<ELEMENT>, ELEMENT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new IncludesOnlyClasses filter.
     * </p>
     *
     * @param only_classes The classes whose instances will, when
     *                     they are elements of a container passed
     *                     to this filter, result in the container
     *                     being KEPT by this filter if and only
     *                     if all of the container's elements
     *                     are instances.
     *                     See the notes about nulls in the
     *                     InstanceOf constructor.
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     */
    public IncludesOnlyClasses (
            Class<?> ... only_classes
            )
    {
        // The qualifier keeps every included instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        this (
            new InstanceOf<ELEMENT> ( only_classes ) // qualifier
            );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyClasses filter.
     * </p>
     *
     * @param only_classes The Collection of classes whose
     *                     instances will, when
     *                     they are elements of a container passed
     *                     to this filter, result in the container
     *                     being KEPT by this filter if and only
     *                     if all of the container's elements
     *                     are instances.
     *                     See the notes about nulls in the
     *                     InstanceOf constructor.
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     */
    public IncludesOnlyClasses (
            Collection<Class<?>> only_classes
            )
    {
        // The qualifier keeps every included instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        this (
            new InstanceOf<ELEMENT> ( only_classes ) // qualifier
            );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyClasses filter.
     * </p>
     *
     * @param only_classes The Iterable classes whose
     *                     instances will, when
     *                     they are elements of a container passed
     *                     to this filter, result in the container
     *                     being KEPT by this filter if and only
     *                     if all of the container's elements
     *                     are instances.
     *                     See the notes about nulls in the
     *                     InstanceOf constructor.
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     */
    public IncludesOnlyClasses (
            Iterable<Class<?>> only_classes
            )
    {
        // The qualifier keeps every included instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        this (
            new InstanceOf<ELEMENT> ( only_classes ) // qualifier
            );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyClasses filter.
     * </p>
     *
     * @param qualifier The InstanceOf filter for each element of
     *                  each container passed to this filter.  When
     *                  a container's elements are all KEPT
     *                  by the qualifier, then
     *                  the whole container will be KEPT.  If null,
     *                  then a default element filter will be used
     *                  so that all elements of all containers will
     *                  be KEPT.  (DO NOT PASS NULL.)
     */
    public IncludesOnlyClasses (
        InstanceOf<ELEMENT> qualifier
        )
    {
        // The qualifier keeps every included instance;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        super ( qualifier == null
                    ? new InstanceOf<ELEMENT> ( Object.class ) // default: keep every element.
                    : qualifier,
                new KeptAllElements<ELEMENT> () ); // quantifier: only keep the container if all elements were KEPT.
    }
}
