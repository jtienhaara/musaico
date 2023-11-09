package musaico.foundation.filter;

import java.io.Serializable;


/**
 * <p>
 * A Domain is a Filter which can also provide information about its
 * boundaries, including reporting on aspects of objects which prevent
 * them from being members of the Domain.
 * </p>
 *
 * <p>
 * For example, a Domain which includes all numbers greater than 0
 * might return -1 when <code> outlyers ( -1 ) </code> is called,
 * or 0 when <code> outlyers ( 0 ) </code> is called, and so on.
 * An array Domain whose elements must all be non-null might
 * return { 1, 3 } when <code> outlyers ( { A, null, B, null, C } </code>
 * is called, indicating the indices of elements which prevent
 * that array's membership.  And so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public interface Domain<MEMBER extends Object>
    extends Filter<MEMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns one object only if it is contained in this Domain;
     * otherwise an empty Iterable is returned.
     * </p>
     *
     * @param object The object to inspect.  If a member of this
     *               Domain, then one object will be returned.
     *               If not a member of this domain, then zero objects
     *               will be returned.  May be null.
     *
     * @return One object if the specified object is a member
     *         of this Domain, or an empty Iterable if the specified object
     *         is not a member of this domain.  May be empty.
     *         May contain a null element (if null is a member of this
     *         domain and null was passed in).  Never null.
     */
    public abstract Iterable<? extends MEMBER> member (
                                                       MEMBER object
                                                       );


    /**
     * <p>
     * Returns one object only if it is not contained in this Domain;
     * if the object is a member, then an empty Iterable is returned.
     * </p>
     *
     * <p>
     * Note that certain domains will not return the exact input
     * objects.  For example, if a Domain covers all Strings
     * that do not have any numerical digits in them, then each member
     * of the Domain might be something like "...8.3...7..5",
     * replacing all of the characters that were allowed in the String
     * Domain with dots, leaving only those characters which prevented
     * the String from being a member of the Domain.  Or if a Domain covers
     * all Collections that contain no null elements,
     * then each member of the Domain might be something
     * like <code> List<?> { 2, 3, 4, 5, 6, 8 } </code>, indicating that
     * the elements # 2 - # 6 and # 8 of a given List
     * prevent that object from being a member
     * of the Domain.  And so on.
     * </p>
     *
     * @param object The object to inspect.  If not a member of this
     *               Domain, then one object will be returned.
     *               If the specified object is a member of this domain,
     *               then zero objects will be returned.  May be null.
     *
     * @return One object if the specified object is not a member
     *         of this Domain, or an empty Iterable if the specified object
     *         is a member of this domain.  May be empty.
     *         May contain a null element (if null is not a member of this
     *         domain and null was passed in).  Never null.
     */
    public abstract Iterable<? extends MEMBER> nonMember (
                                                          MEMBER object
                                                          );
}
