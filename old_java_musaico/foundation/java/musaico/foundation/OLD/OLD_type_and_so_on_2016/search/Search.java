package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A strategy for searching some type of space for some type of
 * criterion in some kind of search space.
 * </p>
 *
 *
 * <p>
 * In Java, every Search strategy must be Serializable in order
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
public interface Search<NEEDLE extends Object, HAYSTACK extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The builtin family of linear searches, which can search any
     *  Iterable haystack, sorted or not. */
    public static final SearchFamily LINEAR = LinearSearch.FAMILY;

    /** The builtin family of binary searches, which can search any
     *  Collection haystack which has been sorted. */
    // !!! public static final SearchFamily BINARY = BinarySearch.FAMILY;


    /**
     * @return The family of Search strategies which created
     *         this Search strategy over a specific class of needles.
     *         Never null.
     */
    public abstract SearchFamily family ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new find operation to search through the specified haystack
     * for needle(s) matching the specified criterion (criteria).
     * </p>
     *
     * @param criterion Used by the Find operation to match needles
     *                  in the haystack.  Must not be null.
     *
     * @param haystack The search space containing zero or more needles
     *                 being searched for.  Must not be null.
     *
     * @return The new find operation, which will use this search strategy
     *         to step through the haystack, searching for needles which
     *         match the specified criterion.  Never null.
     */
    public abstract Find<NEEDLE, HAYSTACK> find (
            Filter<NEEDLE> criterion,
            HAYSTACK haystack
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the initial FindState for one execution of
     * the specified Find operation.
     * </p>
     *
     * @param find The find operation.  Must not be null.
     *
     * @return The initial search state, such as FilterState.DISCARDED
     *         for a linear search, or Comparison.LEFT_LESS_THAN_RIGHT
     *         to hint at the direction for a binary search to start
     *         searching in, and so on.  Never null.
     */
    !!!!!!!!!!!!!!;
    haystack, needle, criterion, strategy, find state;
    Haystack<X> extends Iterable<X>;
    Haystack.ARRAY.create ( FooBar [] ): IndexedHaystack1D<FooBar>;
    Haystack.COLLECTION.create ( Collection<FooBar> ): IndexedHaystack1D<FooBar>;
    Haystack.ITERABLE.create ( Iterable<FooBar> ): Haystack<FooBar>;
    Haystack.RESULT_SET.create ( ResultSet ): IndexedHaystack2D<Object>;
    Search<NEEDLE extends Object, HAYSTACK extends Haystack<NEEDLE>, STATE extends FindState<NEEDLE, HAYSTACK>>
        {
            public Find<NEEDLE> find ( NEEDLE, HAYSTACK );
            public STATE step ( STATE );
        }
    Find<NEEDLE, HAYSTACK, STATE> extends Iterable<NEEDLE>
        {
            public FoundIterator<NEEDLE> iterator ();
        }
    FoundIterator<NEEDLE, HAYSTACK, STATE>;
    FindState<NEEDLE, HAYSTACK>
        {
            NEEDLE candidate ();
            HAYSTACK haystack ();
            Filter<NEEDLE> criterion ();
        }
    IndexedFindState<...>;
    Maybe every Haystack has 0 or more dimensions, and we keep track of
        position in FindState without knowing anything about what it means.;
    !!! A Haystack<V> is just a Value<V>.  Take the 1D, 2D, etc dimensions
        !!! to Countable<V>. ;
    public abstract FilterState start (
                                       Find<NEEDLE, HAYSTACK> find
                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Given the specified position in a haystack, step to the next
     * position from which to search.
     * </p>
     *
     * @param find The find operation, including the haystack to search
     *             through.  Must not be null.
     *
     * @param from_step The previous position in the haystack from which
     *                  to continue the search.  Must not be null.
     *
     * @param filtered The FilterState of the previously filtered
     *                 candidate needle, if any, or the
     *                 <code> start () </code>ing state, if the find
     *                 operation has not yet filtered any candidate needles.
     *                 Some search strategies, such as linear search,
     *                 ignore the previous filtered state.  Other strategies,
     *                 such as binary search, use the previous state
     *                 to decide which direction to search in
     *                 (to the right if the previous state was
     *                 <code> Comparison.LEFT_LESS_THAN_RIGHT </code>,
     *                 or to the left if the previous state was
     *                 <code> Comparison.LEFT_GREATER_THAN_RIGHT </code>,
     *                 and so on).  Must not be null.
     *
     * @return An iterator (possibly the same one, or possibly a newly
     *         created iterator) which begins at the next search candidate,
     *         and steps over its neighbours, one candidate
     *         to the next.  When the Find operation steps through
     *         more than one candidate in a neighbourhood (unlike
     *         the simple Find operations returned by linear and binary
     *         Searches, which only ever inspect the first candidate
     *         in the neighbourhood before proceeding to the next
     *         <code> step () </code>), this iterator can be used
     *         to step through the neighbourhood in consecutive order,
     *         before proceeding to a new neighbourhood with another
     *         call to <code> step () <code>.  Find operations which
     *         jump around the search space, such as those returned by
     *         local search strategies, will often step through more
     *         than one candidate in a given neighbourhood before
     *         jumping to the next neighbourhood.
     *         If there are no further steps for this Search to take,
     *         then null is returned.  Can be null.
     */
    public abstract NEIGHBOURHOOD_ITERATOR step (
            Find<NEEDLE, HAYSTACK, NEIGHBOURHOOD_ITERATOR> find,
            NEIGHBOURHOOD_ITERATOR from_step,
            FilterState filtered
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
