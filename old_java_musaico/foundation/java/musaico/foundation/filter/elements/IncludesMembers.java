package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.filter.stream.KeptSomeElements;


/**
 * <p>
 * A Filter that keeps any container that has at least one element
 * that belongs to some other set of objects (though other elements
 * of the container might not be members of the set).
 * </p>
 *
 * <p>
 * For example, an IncludesMembers filter that includes
 * members of the set <code> { "a", 1 } </code> would keep the
 * containers <code> { "a" } </code>, <code> { 1 } </code>,
 * <code> { "a", 1 } </code>, <code> { "a", "b", "c" } </code>,
 * <code> { 1, 2, 3 } </code> and <code> { "a", 1, new Date () } </code>
 * because each has at least one member of the set, but discard the container
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
public class IncludesMembers<MEMBER extends Object>
    extends AllElementsFilter<MemberOf<MEMBER>, MEMBER>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new IncludesMembers filter.
     * </p>
     *
     * @param included_members The array whose members will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being KEPT by this filter.
     *                         See the notes about nulls in the
     *                         MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public IncludesMembers (
            MEMBER ... included_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if at least 1 element from that container is KEPT.
        this ( new MemberOf<MEMBER> ( included_members ) );
    }


    /**
     * <p>
     * Creates a new IncludesMembers filter.
     * </p>
     *
     * @param included_members The Collection whose members will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being KEPT by this filter.
     *                         See the notes about nulls in the
     *                         MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    public IncludesMembers (
            Collection<MEMBER> included_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if at least 1 element from that container is KEPT.
        this ( new MemberOf<MEMBER> ( included_members ) );
    }


    /**
     * <p>
     * Creates a new IncludesMembers filter.
     * </p>
     *
     * @param included_members The Iterable whose members will, when
     *                         they are elements of a container passed
     *                         to this filter, result in the container
     *                         being KEPT by this filter.
     *                         See the notes about nulls in the
     *                         MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    public IncludesMembers (
            Iterable<MEMBER> included_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if at least 1 element from that container is KEPT.
        this ( new MemberOf<MEMBER> ( included_members ) );
    }


    /**
     * <p>
     * Creates a new IncludesMembers filter.
     * </p>
     *
     * @param qualifier The MemberOf filter for each element of
     *                       each container passed to this filter.  When
     *                       a container contains at least one element
     *                       that is KEPT by the qualifier, then
     *                       the whole container will be KEPT.  If null,
     *                       then a default element filter will be used
     *                       so that all elements of all containers will
     *                       be KEPT.  (DO NOT PASS NULL.)
     */
    @SuppressWarnings("unchecked") // Generic varargs array creation:
                                   // new MemberOf<MEMBER> ( MEMBER [ 0 ] ).
    public IncludesMembers (
        MemberOf<MEMBER> qualifier
        )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if at least 1 element from that container is KEPT.
        super ( qualifier == null
                    ? new MemberOf<MEMBER> () // default qualifier: keep every element.
                    : qualifier,
                new KeptSomeElements<MEMBER> () ); // quantifier: only keep the container if at least 1 element is KEPT.
    }
}
