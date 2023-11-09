package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A family of search strategies, each strategy being specific
 * to a particular class of "needles" to search for.
 * </p>
 *
 * <p>
 * Different search strategies work over different "haystacks"
 * (search spaces), but in Java a haystack is dependent on the
 * needle.  For example, a linear search might work inside
 * an <code> Iterable&lt;NEEDLE&gt; </code> haystack; but a
 * binary search might work inside a <code> Collection&lt;NEEDLE&gt; </code>
 * haystack.  The SearchFamily narrows the broad class of
 * haystacks searchable by it search strategy; but each Search
 * strategy narrows the class of needles it can search within
 * those haystacks.
 * </p>
 *
 *
 * <p>
 * In Java, every SearchFamily must be Serializable in order
 * to play nicely over RMI.
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
public interface SearchFamily
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns a Search strategy over the specified class of needles
     * (type of objects to search through and for).
     * </p>
     *
     * @param needle_class The class of needles to search through and
     *                     to search for.  Must not be null.
     *
     * @return A Search strategy for the specified type of needles.
     *         Never null.
     */
    public abstract <NEEDLE extends Object, HAYSTACK extends Object, FOUND_ITERATOR extends Iterator<NEEDLE>>
            Search<NEEDLE, HAYSTACK, FOUND_ITERATOR> over (
                Class<NEEDLE> needle_class
                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
