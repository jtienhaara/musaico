package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.filter.Filter;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * The current find state during a search of some haystack for
 * needle(s) matching some criteria.
 * </p>
 *
 * <p>
 * The found needles can be iterated over as follows:
 * </p>
 *
 * <pre>
 *     final Find<String, ?, Iterator<String>> find_a_string = ...;
 *     for ( String found : find_a_string )
 *     {
 *         System.out.println ( "Found: '" + found + "'" );
 *     }
 * </pre>
 *
 * <p>
 * However keep in mind that every time an <code> iterator () </code>
 * is created from the Find state, most classes will perform the find
 * algorithm from scratch.  This ensures that mutable haystacks
 * return up-to-date results every time the caller iterates
 * over the found needles.
 * </p>
 *
 *
 * <p>
 * In Java, every Find must be Serializable in order
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
public interface Find<NEEDLE extends Object, HAYSTACK extends Object>
    extends Iterable<NEEDLE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The criterion to find.  Can be empty.
     *         Never null.
     */
    public abstract Filter<NEEDLE> criterion ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The haystack in which to find the matching needle(s).
     *         Never null.
     */
    public abstract HAYSTACK haystack ()
        throws ReturnNeverNull.Violation;


    /**
     * @see java.lang.Iterable#iterator()
     *
     * <p>
     * Returns the matching needles found in the haystack.
     * </p>
     */
    @Override
    public abstract Iterator<NEEDLE> iterator ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The search strategy, which determines the order
     *         which this find operation steps through candidates in
     *         the haystack in order to find matching needle(s).
     *         Never null.
     */
    public abstract Search<NEEDLE, HAYSTACK> strategy ()
        throws ReturnNeverNull.Violation;
}
