package musaico.foundation.domains.array;

import java.io.Serializable;


import musaico.foundation.domains.elements.Elements;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A Domain over arrays, Collections, singletons and Iterables with
 * 0 or more elements, such as the domain of
 * "all arrays containing sorted elements", or the domain of
 * "all arrays containing no null elements", and so on.
 * </p>
 *
 * <p>
 * Every ArrayDomain's <code> nonMembers ( ... ) </code> method
 * returns only a partial Elements for each non-member, containing
 * only the element(s) which prevented the container from
 * being a member of the domain.
 * </p>
 *
 * <p>
 * For example, if an ArrayDomain
 * covers all values containing only elements of the String class,
 * and a possible member of that domain
 * <code> Object [] { "A", 123, "B", 456 } </code> is one of the values
 * passed to its <code> nonMembers ( ... ) </code> method, the result
 * would contain Elements <code> { 123, 456 } </code>, where 123
 * and 456 are the outlying elements preventing the array value
 * from being a member of the domain.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must implement equals (), hashCode () and
 * toString ().
 * </p>
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
public interface ArrayDomain
    extends Domain<Elements<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
