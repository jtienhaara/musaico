package musaico.foundation.filter.elements;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.KeepAll;

import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.filter.stream.KeptAllElements;


/**
 * <p>
 * A Filter that keeps any container that has only elements
 * that belong to some other set of objects.
 * </p>
 *
 * <p>
 * For example, an IncludesOnlyMembers filter that includes only
 * members of the set <code> { "a", 1 } </code> would keep the
 * containers <code> { "a", 1 } </code>, <code> { "a" } </code>
 * and <code> { 1 } </code>, because all of their elements
 * are members of the set, but discard the containers
 * <code> { "a", "b", "c" } </code>, <code> { 1, 2, 3 } </code>,
 * <code> { "a", 1, new Date () } </code> and 
 * <code> { new Date ( 123L ), new BigDecimal ( "1.23" ) } </code>,
 * because each has elements that are not members of the set.
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
public class IncludesOnlyMembers<MEMBER extends Object>
    extends AllElementsFilter<MemberOf<MEMBER>, MEMBER>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new IncludesOnlyMembers filter.
     * </p>
     *
     * @param only_members The array whose members will, when
     *                     they are elements of a container passed
     *                     to this filter, result in the container
     *                     being KEPT by this filter if and only
     *                     if all of the container's elements
     *                     are members.
     *                     See the notes about nulls in the
     *                     MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public IncludesOnlyMembers (
            MEMBER ... only_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        this ( new MemberOf<MEMBER> ( only_members ) );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyMembers filter.
     * </p>
     *
     * @param only_members The Collection whose members will, when
     *                     they are elements of a container passed
     *                     to this filter, result in the container
     *                     being KEPT by this filter if and only
     *                     if all of the container's elements
     *                     are members.
     *                     See the notes about nulls in the
     *                     MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    public IncludesOnlyMembers (
            Collection<MEMBER> only_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        this ( new MemberOf<MEMBER> ( only_members ) );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyMembers filter.
     * </p>
     *
     * @param only_members The Iterable whose members will, when
     *                     they are elements of a container passed
     *                     to this filter, result in the container
     *                     being KEPT by this filter if and only
     *                     if all of the container's elements
     *                     are members.
     *                     See the notes about nulls in the
     *                     MemberOf constructor.
     *
     * @see musaico.foundation.filter.membership.MemberOf
     */
    public IncludesOnlyMembers (
            Iterable<MEMBER> only_members
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        this ( new MemberOf<MEMBER> ( only_members ) );
    }


    /**
     * <p>
     * Creates a new IncludesOnlyMembers filter.
     * </p>
     *
     * @param qualifier The MemberOf filter for each element of
     *                       each container passed to this filter.  When
     *                       a container's elements are all KEPT
     *                       by the qualifier, then
     *                       the whole container will be KEPT.  If null,
     *                       then a default element filter will be used
     *                       so that all elements of all containers will
     *                       be KEPT.  (DO NOT PASS NULL.)
     */
    @SuppressWarnings("unchecked") // Generic varargs array creation:
                                   // new MemberOf<MEMBER> ( MEMBER [ 0 ] ).
    public IncludesOnlyMembers (
            MemberOf<MEMBER> qualifier
            )
    {
        // The qualifier keeps every included member;
        // the quantifier makes sure each filtered container is only KEPT
        // if all elements from that container are KEPT.
        super ( qualifier == null
                    ? new MemberOf<MEMBER> () // default qualifier: keep every element.
                    : qualifier,
                new KeptAllElements<MEMBER> () ); // quantifier: only keep the container if all elements are KEPT.
    }
}
