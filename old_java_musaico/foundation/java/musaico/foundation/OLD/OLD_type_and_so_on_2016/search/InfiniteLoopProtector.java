package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.BeforeAndAfter;
import musaico.foundation.domains.HashedObject;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * Ensures an iterator will terminate after a finite number of steps.
 * </p>
 *
 * <p>
 * To use an InfiniteLoopProtector:
 * </p>
 *
 * <pre>
 *     final InfiniteLoopProtector finite =
 *         new InfiniteLoopProtector ( ...optional parameters... );
 *     final Iterator<Wheel> iterator = vehicle.wheels ().iterator ();
 *     while ( iterator.hasNext () )
 *     {
 *         if ( finite.step () != null )
 *         {
 *             throw finite.violation ();
 *         }
 *     }
 * </pre>
 *
 *
 * <p>
 * In Java every InfiniteLoopProtector must be Serializable in order
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
public class InfiniteLoopProtector
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static method and constructor obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( InfiniteLoopProtector.class );


    // Enforces method parameter obligations and so on for us.
    private final ObjectContracts contracts;

    // Synchronize all critical sections on this object:
    private final Serializable lock = new String ();

    // Ensures the iterator will not go on forever.
    private final Contract<Iterator<? extends Object>, IteratorMustBeFinite.Violation> iteratorMustBeFinite;

    // MUTABLE:
    // Can be used to help prevent infinite loops.
    private long finiteCounter = 0L;

    // MUTABLE:
    // Search terminated by finite search space violation.
    private IteratorMustBeFinite.Violation finiteViolation = null;


    /**
     * <p>
     * Creates a new InfiniteLoopProtector which will count each step
     * through an iterator, and return a violation of
     * the <code> IteratorMustBeFinite </code> default contract if
     * the iterator continues stepping for too long.
     * </p>
     */
    public InfiniteLoopProtector ()
        throws ParametersMustNotBeNull.Violation
    {
        this ( IteratorMustBeFinite.CONTRACT );
    }


    /**
     * <p>
     * Creates a new InfiniteLoopProtector which will count each step
     * through the specified iterator, and return a violation of
     * an <code> IteratorMustBeFinite </code> contract if
     * the iterator continues stepping beyond the specified maximum step.
     * </p>
     *
     * @param max_step The maximum step number, beyond which the iterator
     *                 will be assumed to be infinite.  Must be greater
     *                 than or equal to 1L.
     */
    public InfiniteLoopProtector (
                                  long max_step
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToOne.Violation
    {
        this ( new IteratorMustBeFinite (
                   classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToOne.CONTRACT,
                                          max_step ) ) );
    }


    /**
     * <p>
     * Creates a new InfiniteLoopProtector which will check the
     * specified iterator against the specified contract in order
     * to ensure the Iterator will terminate after some finite number
     * of steps.
     * </p>
     *
     * @param iterator_must_be_finite The contract which must be adhered
     *                                to by the iterator.
     *                                The Iterator must terminate, and
     *                                this contract enforces the behaviour.
     *                                Must not be null.
     */
    public InfiniteLoopProtector (
                                  Contract<Iterator<? extends Object>, IteratorMustBeFinite.Violation> iterator_must_be_finite
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               iterator_must_be_finite );

        this.iteratorMustBeFinite = iterator_must_be_finite;
        this.finiteCounter = 0L;
        this.finiteViolation = null;

        this.contracts = new ObjectContracts ( this );
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
        else if ( ! ( obj instanceof InfiniteLoopProtector ) )
        {
            return false;
        }

        final InfiniteLoopProtector that = (InfiniteLoopProtector) obj;

        if ( ! this.iteratorMustBeFinite.equals ( that.iteratorMustBeFinite ) )
        {
            return false;
        }

        // It doesn't matter what state these two iterators are currently
        // in, they are stepping through the exact same path and will
        // eventually reach the exact same termination point and state.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.iteratorMustBeFinite.hashCode () * 31;

        return hash_code;
    }


    /**
     * <p>
     * Counts another step, and returns a violation of the finite contract if
     * stepping has gone on for too long.
     * </p>
     *
     * @param iterator The Iterator which causes the violation of the
     *                 IteratorMustBeFinite contract (if any
     *                 violation is caused).  Must not be null.
     */
    public IteratorMustBeFinite.Violation step (
                                                Iterator<?> iterator
                                                )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               iterator );

        final IteratorMustBeFinite simple_finite_contract;

        synchronized ( this.lock )
        {
            if ( this.finiteViolation != null )
            {
                return this.finiteViolation;
            }
            else if ( this.iteratorMustBeFinite instanceof IteratorMustBeFinite )
            {
                simple_finite_contract = (IteratorMustBeFinite)
                    this.iteratorMustBeFinite;
            }
            else
            {
                // Already checked this contract during start ().
                return this.finiteViolation;
            }

            this.finiteCounter ++;
            if ( this.finiteCounter > simple_finite_contract.maxIndex ()
                 || this.finiteCounter < 0L ) // If index wraps past Long.MAX.
            {
                this.finiteViolation =
                    IteratorMustBeFinite.CONTRACT.violation (
                        this,
                        iterator );
                return this.finiteViolation;
            }
        }

        return null;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "InfiniteLoopProtector(" + this.iteratorMustBeFinite + ")";
    }


    /**
     * @return The violation of the "iterator must be finite" contract,
     *         if any was induced by the iterator; or null if the
     *         iterator has abided by the contract so far.
     *         Can be null.
     */
    public final IteratorMustBeFinite.Violation violation ()
    {
        synchronized ( this.lock )
        {
            return this.finiteViolation;
        }
    }


    /**
     * @return The violation of the "iterator must be finite" contract,
     *         if any was induced by the iterator; or null if the
     *         iterator has abided by the contract so far.
     *         Can be null.
     */
    protected final void violation (
                                    IteratorMustBeFinite.Violation violation
                                    )
    {
        synchronized ( this.lock )
        {
            if ( this.finiteViolation != null )
            {
                // Sorry bub, write once then read-only.
                return;
            }

            this.finiteViolation = violation;
        }
    }
}
