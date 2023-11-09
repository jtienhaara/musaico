package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.filter.stream.KeptNoElements;


/**
 * <p>
 * A Filter that keeps any container that has no elements
 * that belong to some other set of objects.
 * </p>
 *
 * <p>
 * For example, an ExcludesMembers filter that excludes
 * members of the set <code> { "a", 1 } </code> would discard the
 * containers <code> { "a" } </code>, <code> { 1 } </code>,
 * <code> { "a", 1 } </code>, <code> { "a", "b", "c" } </code>,
 * <code> { 1, 2, 3 } </code> and <code> { "a", 1, new Date () } </code>
 * because each has at least one member of the set, but keep the container
 * <code> { new Date ( 123L ), new BigDecimal ( "1.23" ) } </code>
 * because it has no members of the set.
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
public class ExcludesMembers<MEMBER extends Object>
    extends AllElementsFilter<MemberOf<MEMBER>, MEMBER>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ExcludesMembers filter.
     * </p>
     *
     * @param excluded_members The array whose members will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being DISCARDED by this filter.
     *                         See the notes about nulls in the
     *                         MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public ExcludesMembers (
            MEMBER ... excluded_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        this ( new MemberOf<MEMBER> ( excluded_members ) );
    }


    /**
     * <p>
     * Creates a new ExcludesMembers filter.
     * </p>
     *
     * @param excluded_members The Collection whose members will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being DISCARDED by this filter.
     *                         See the notes about nulls in the
     *                         MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    public ExcludesMembers (
            Collection<MEMBER> excluded_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        this ( new MemberOf<MEMBER> ( excluded_members ) );
    }


    /**
     * <p>
     * Creates a new ExcludesMembers filter.
     * </p>
     *
     * @param excluded_members The Iterable whose members will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being DISCARDED by this filter.
     *                         See the notes about nulls in the
     *                         MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    public ExcludesMembers (
            Iterable<MEMBER> excluded_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        this ( new MemberOf<MEMBER> ( excluded_members ) );
    }


    /**
     * <p>
     * Creates a new ExcludesMembers filter.
     * </p>
     *
     * @param qualifier The MemberOf filter for each element of
     *                  each container passed to this filter.  When
     *                  a container contains at least one element
     *                  that is KEPT by the qualifier, then
     *                  the whole container will be DISCARDED.  If null,
     *                  then a default element filter will be used
     *                  so that all non-empty containers will
     *                  be DISCARDED.  (DO NOT PASS NULL.)
     */
    @SuppressWarnings("unchecked") // Generic varargs array creation:
                                   // new MemberOf<MEMBER> ( MEMBER [ 0 ] ).
    public ExcludesMembers (
        MemberOf<MEMBER> qualifier
        )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if no elements from that container are KEPT.
        super ( qualifier == null
                    ? new MemberOf<MEMBER> () // default qualifier: keep every element.
                    : qualifier,
                new KeptNoElements<MEMBER> () ); // quantifier: only keep the container if no elements are KEPT.
    }
}
