package musaico.foundation.search;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Steps through the results of a find operation, searching for needles
 * in a haystack.
 * </p>
 *
 *
 * <p>
 * In Java every FoundIterator must be Serializable in order
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
public class FoundIterator<NEEDLE extends Object, HAYSTACK extends Object>
    implements IndexedIterator<NEEDLE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FoundIterator.class );


    // Synchronize all critical sections on this object:
    private final Serializable lock = new String ();

    // What we're searching for, and where we're searching for it.
    private final Find<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> find;

    // Makes sure we don't iterate forever.
    private final InfiniteLoopProtector finite;

    // MUTABLE:
    // The most recent FilterState (possibly a Comparison) returned
    // by the Find filter.
    private FilterState filtered = FilterState.DISCARDED;

    // MUTABLE:
    // The index of the most recently returned element from next ().
    private long foundIndex = -1L;

    // MUTABLE:
    // The element most recently returned from next ().
    private NEEDLE found = null;

    // MUTABLE:
    // The index of the element that will be returned from the next
    // call to next (), if it has already been determined (for example
    // by hasNext () ).
    private long foundNextIndex = -1L;

    // MUTABLE:
    // The element that will be returned from the next
    // call to next (), if it has already been determined (for example
    // by hasNext () ).
    private NEEDLE foundNext = null;

    // MUTABLE:
    // Search terminated by must have more elements violation.
    private IteratorMustHaveNextObject.Violation nextViolation = null;


    /**
     * <p>
     * Creates a new FoundIterator to step through the results found by
     * the specified Find operation, looking for specific needles in a
     * specific haystack, stepping through the haystack with the help
     * of a specific search strategy (such as linear search, binary
     * search, and so on).
     * </p>
     *
     * @param find The find operation.  Must not be null.
     */
    public FoundIterator (
                          Find<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> find
                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( find,
               0L,
               new InfiniteLoopProtector () );
    }


    /**
     * <p>
     * Creates a new FoundIterator to step through the results found by
     * the specified Find operation, looking for specific needles in a
     * specific haystack, stepping through the haystack with the help
     * of a specific search strategy (such as linear search, binary
     * search, and so on).
     * </p>
     *
     * @param find The find operation.  Must not be null.
     *
     * @param start_index The index number from which this iterator is
     *                    starting, such as 0L to start at the beginning.
     *                    Must be greater than or equal to 0L.
     */
    public FoundIterator (
                          Find<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> find,
                          long start_index
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        this ( find,
               start_index,
               new InfiniteLoopProtector () );
    }


    /**
     * <p>
     * Creates a new FoundIterator to step through the results found by
     * the specified Find operation, looking for specific needles in a
     * specific haystack, stepping through the haystack with the help
     * of a specific search strategy (such as linear search, binary
     * search, and so on).
     * </p>
     *
     * @param find The find operation.  Must not be null.
     *
     * @param finite An InfiniteLoopProtector, which prevents this
     *               FoundIterator from searching forever.
     *               Must not be null.
     */
    public FoundIterator (
                          Find<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> find,
                          InfiniteLoopProtector finite
                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( find,
               0L,
               finite );
    }


    /**
     * <p>
     * Creates a new FoundIterator to step through the results found by
     * the specified Find operation, looking for specific needles in a
     * specific haystack, stepping through the haystack with the help
     * of a specific search strategy (such as linear search, binary
     * search, and so on).
     * </p>
     *
     * @param find The find operation.  Must not be null.
     *
     * @param start_index The index number from which this iterator is
     *                    starting, such as 0L to start at the beginning.
     *                    Must be greater than or equal to 0L.
     *
     * @param finite An InfiniteLoopProtector, which prevents this
     *               FoundIterator from searching forever.
     *               Must not be null.
     */
    public FoundIterator (
                          Find<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> find,
                          long start_index,
                          InfiniteLoopProtector finite
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               find, finite );

        classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               start_index );

        this.find = find;
        this.foundIndex = -1L;
        this.found = null;
        this.foundNextIndex = -1L;
        this.foundNext = null;

        this.nextViolation = null;

        this.finite = finite;

        this.filtered = this.find.strategy ().start ( this.find );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof FoundIterator ) )
        {
            return false;
        }

        final FoundIterator<?, ?> that = (FoundIterator<?, ?>) obj;
        // This iterator is the same as that one if and only if
        // they refer to Find operations that are equal.
        // It doesn't matter where in the iterations they are,
        // the end state will be the same.
        if ( ! this.find.equals ( that.find ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @return The state of the most recently filtered candidate,
     *         or FilterState.DISCARDED if searching has not even started
     *         yet.  Never null.
     */
    public FilterState filtered ()
    {
        synchronized ( this.lock )
        {
            return this.filtered;
        }
    }


    /**
     * <p>
     * Steps ahead to the next found element in the search space
     * (if any).
     * </p>
     */
    protected void findNext ()
        throws IteratorMustBeFinite.Violation
    {
        final Filter<NEEDLE> criterion = this.find.criterion ();

        synchronized ( this.lock )
        {
            if ( this.nextViolation != null )
            {
                // We've already failed to find the next needle.
                // We're done.
                this.foundNext = null;
                this.foundNextIndex = -1L;
                return;
            }
            else if ( this.foundNextIndex >= 0L )
            {
                // We've already found the next needle.
                // We're done.
                return;
            }

            // We have not yet tried to find the next needle.
            // Do so now.
            for ( IndexedIterator<NEEDLE> step =
                      this.find.strategy ().step ( this.find,
                                                   this,
                                                   this.filtered );
                  step != null;
                  step =
                      this.find.strategy ().step ( this.find,
                                                   step,
                                                   this.filtered ) )
            {
                final NEEDLE candidate = step.next ();
                final long step_index = step.index ();

                // Protect against infinite searches:
                if ( this.finite.step ( this ) != null )
                {
                    throw IteratorMustBeFinite.CONTRACT.violation (
                        this.finite,
                        this,
                        this.finite.violation () );
                }

                this.filtered = criterion.filter ( candidate );

                // Does the find filter match the current element?
                if ( this.filtered.isKept () )
                {
                    // Found the next position.
                    this.foundNextIndex = step_index;
                    this.foundNext = candidate;
                    return;
                }

                // Step to the next candidate to keep searching
                // for a matching needle.
            }

            // Finished searching the entire search space.
            // No more needles matching the criterion.
            this.foundNext = null;
            this.foundNextIndex = -1;
        }
    }


    /**
     * @return The current found element, or null if either there is none
     *         or next () has not yet been called.  Can be null.
     */
    public NEEDLE found ()
    {
        synchronized ( this.lock )
        {
            return this.found;
        }
    }


    /**
     * @return The index of the current found element, or -1L if either
     *         there is none or next () has not yet been called.
     *         Always greater than or equal to -1L.
     */
    public long foundIndex ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        synchronized ( this.lock )
        {
            return this.foundIndex;
        }
    }


    /**
     * <p>
     * Protected for use by derived implementations only.
     * </p>
     *
     * @return The next found element, if findNext () has been called
     *         to search ahead, or null if either there is none
     *         or findNext () has not yet been called.  Can be null.
     */
    protected NEEDLE foundNext ()
    {
        synchronized ( this.lock )
        {
            return this.foundNext;
        }
    }


    /**
     * <p>
     * Protected for use by derived implementations only.
     * </p>
     *
     * @return The index of the next found element, if findNext ()
     *         has been called to search ahead, or -1L if either
     *         there is none or findNext () has not yet been called.
     *         Always greater than or equal to -1L.
     */
    protected long foundNextIndex ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        synchronized ( this.lock )
        {
            return this.foundNextIndex;
        }
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext ()
        throws IteratorMustBeFinite.Violation
    {
        synchronized ( this.lock )
        {
            // Throws IteratorMustBeFinite.Violation:
            this.findNext ();

            if ( this.foundNextIndex < 0 )
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.find.hashCode ();

        return hash_code;
    }


    /**
     * @see musaico.foundation.search.IndexedIterator#index()
     */
    @Override
    public final long index ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        return this.foundIndex ();
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public final NEEDLE next ()
        throws IteratorMustBeFinite.Violation,
               IteratorMustHaveNextObject.Violation
    {
        synchronized ( this.lock )
        {
            // Throws IteratorMustBeFinite.Violation:
            this.findNext ();

            if ( this.nextViolation != null )
            {
                throw this.nextViolation;
            }
            else if ( this.foundNextIndex < 0 )
            {
                // If we run out of needles found in the haystack, throw
                // an exception.  (Probably an internal bug in this iterator
                // implementation, or the caller didn't check
                // with hasNext () first.)
                this.nextViolation =
                    IteratorMustHaveNextObject.CONTRACT.violation ( this,
                                                                    this );
                throw this.nextViolation;
            }

            this.foundIndex = this.foundNextIndex;
            this.found = this.foundNext;

            // Now reset the "next found needle", so that we have
            // to search for another one the next time findNext () is called.
            this.foundNextIndex = -1;
            this.foundNext = null;

            return this.found;
        }
    }


    /**
     * @see java.util.Iterator#remove()
     */
    @Override
    public final void remove ()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException ( ClassName.of ( this.getClass () )
                                                  + ".remove () not supported" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "" + this.find );
        sbuf.append ( ".iterator" );

        synchronized ( this.lock )
        {
            if ( this.nextViolation != null )
            {
                sbuf.append ( " [ end ]" );
            }
            else if ( this.foundIndex < 0L )
            {
                sbuf.append ( " [ not yet started ]" );
            }
            else
            {
                sbuf.append ( " [ " + this.foundIndex + " ]" );
            }
        }

        return sbuf.toString ();
    }
}
