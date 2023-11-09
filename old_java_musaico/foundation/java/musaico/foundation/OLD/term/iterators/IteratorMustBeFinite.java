package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A guarantee that Iterator.next () will return a non-null value.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
 * @see musaico.foundation.term.iterators.MODULE#COPYRIGHT
 * @see musaico.foundation.term.iterators.MODULE#LICENSE
 */
public class IteratorMustBeFinite
    implements Contract<Iterator<?>, IteratorMustBeFinite.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on constructors and static methods for us. */
    private static final Advocate classContracts =
        new Advocate ( IteratorMustBeFinite.class );


    /** Prevents iterating forever.  Integer.MAX_VALUE is a reasonable
     *  upper limit before we assume infinite length. */
    public static final long DEFAULT_MAX_INDEX = (long) Integer.MAX_VALUE;

    /** The default IteratorMustBeFinite contract.
     *  After stepping past index number
     *  <code> IteratorMustBeFinite.MAX_INDEX </code>,
     *  an Iterator is assumed to go on infinitely. */
    public static final IteratorMustBeFinite CONTRACT =
        new IteratorMustBeFinite ( IteratorMustBeFinite.DEFAULT_MAX_INDEX );


    // The index at which we give up and decide the iterator is going
    // to go on infinitely.
    private final long maxIndex;


    /**
     * <p>
     * Creates a new IteratorMustBeFinite which will give up and
     * assume an Iterator is infinite once it has passed the specified
     * maximum index number.
     * </p>
     *
     * @param max_index The maximum index number an Iterator can
     *                  step over before it is assumed to be infinite.
     *                  Must be greater than or equal to 1L.
     */
    public IteratorMustBeFinite (
                                 long max_index
                                 )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation
    {
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToOne.CONTRACT,
                               max_index );

        this.maxIndex = max_index;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "The Iterator must finish stepping after a finite"
            + " number of iterations, to prevent infinite loops."
            + "  After " + this.maxIndex + " iterations, it is"
            + " assumed that the Iterator is not going to stop.";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Iterator<?> iterator
                               )
    {
        if ( iterator == null )
        {
            return FilterState.DISCARDED;
        }

        for ( long index = 0L; index < this.maxIndex; index ++ )
        {
            if ( ! iterator.hasNext () )
            {
                // Finite end.
                return FilterState.KEPT;
            }
        }

        // No end in sight.
        return FilterState.DISCARDED;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @return The maximum index beyond which an Iterator is assumed to
     *         go on to infinity.  Always greater than or equal to 1L.
     */
    public final long maxIndex ()
        throws Return.AlwaysGreaterThanOrEqualToOne.Violation
    {
        return this.maxIndex;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.contract.Contract#violation(musaico.contract.Contract, java.lang.Object, java.lang.Object)
     */
    @Override
    public IteratorMustBeFinite.Violation violation (
        Object plaintiff,
        Iterator<?> evidence
        )
    {
        return new IteratorMustBeFinite.Violation (
                this,
                plaintiff,
                evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.term.Term, java.lang.Throwable)
     */
    @Override
    public IteratorMustBeFinite.Violation violation (
            Object plaintiff,
            Iterator<?> evidence,
            Throwable cause
            )
    {
        final IteratorMustBeFinite.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }




    /**
     * <p>
     * A violation of the IteratorMustBeFinite contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            IteratorMustBeFinite.serialVersionUID;


        /**
         * <p>
         * Creates a new IteratorMustBeFinite.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param description A human-readable, non-internationalized
         *                    explanation of why the contract was violated.
         *                    Used by developers, testers and maintainers
         *                    to troubleshoot and debug exceptions and errors.
         *                    Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Iterator which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Iterator<?> evidence
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this ( contract,
                   plaintiff,
                   evidence,
                   null ); // cause
        }


        /**
         * <p>
         * Creates a new IteratorMustBeFinite.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param description A human-readable, non-internationalized
         *                    explanation of why the contract was violated.
         *                    Used by developers, testers and maintainers
         *                    to troubleshoot and debug exceptions and errors.
         *                    Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Iterator which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Iterator<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The iterator appears to be stepping forever.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
