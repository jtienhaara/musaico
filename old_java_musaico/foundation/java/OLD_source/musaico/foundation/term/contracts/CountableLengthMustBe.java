package musaico.foundation.term.contracts;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;
import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.array.ArrayObject;
import musaico.foundation.domains.array.Length;

import musaico.foundation.domains.comparability.BoundedDomain;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * Each inspected Term must have a <code> Countable </code> value, which must
 * have a <code> length () </code> matching some domain (such as
 * an exact length, or a range of minimum - maximum lengths, and so on).
 * </p>
 *
 * @see musaico.foundation.domains.array.Length
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
 * @see musaico.foundation.term.contracts.MODULE#COPYRIGHT
 * @see musaico.foundation.term.contracts.MODULE#LICENSE
 */
public class CountableLengthMustBe
    implements Contract<Term<?>, CountableLengthMustBe.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( CountableLengthMustBe.class );


    /** Each Countable must be more than 1 element in length. */
    public static final CountableLengthMustBe GREATER_THAN_ONE =
        new CountableLengthMustBe ( Length.GREATER_THAN_ONE_DOMAIN );

    /** Each Countable must be more than 0 elements in length. */
    public static final CountableLengthMustBe GREATER_THAN_ZERO =
        new CountableLengthMustBe ( Length.GREATER_THAN_ZERO_DOMAIN );

    /** Each Countable must be 0 - Integer.MAX_VALUE elements, no longer. */
    public static final CountableLengthMustBe INT_32_SIZED =
        new CountableLengthMustBe ( Length.INT_32_SIZED );


    // The Length Domain to which every Term must belong.
    private final Length domain;


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param exact_length The exact <code> length () </code> of
     *                     every Countable term in this domain.  Must be
     *                     greater than or equal to 0.
     */
    public CountableLengthMustBe (
                                  int exact_length
                                  )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        this ( (long) exact_length );
    }


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param minimum_length The minimum <code> length () </code> of
     *                       every Countable term in this domain 
     *                       (inclusive).
     *                       Must be greater than or equal to 0.
     *
     * @param maximum_length The maximum <code> length () </code> of
     *                       every Countable term in this domain
     *                       (inclusive).
     *                       Must be greater than or equal to 0.
     */
    public CountableLengthMustBe (
                                  int minimum_length,
                                  int maximum_length
                                  )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation
    {
        this ( (long) minimum_length,
               (long) maximum_length );
    }


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param exact_length The exact <code> length () </code> of
     *                     every Countable term in this domain.
     *                     Must be greater than or equal to 0.
     */
    public CountableLengthMustBe (
                                  long exact_length
                                  )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        this (
            new Length (
                classContracts.check (
                    Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                    exact_length )
                )
            );
    }


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param minimum_length The minimum <code> length () </code> of
     *                       every Countable term in this domain
     *                       (inclusive).
     *                       Must be greater than or equal to 0.
     *
     * @param maximum_length The maximum <code> length () </code> of
     *                       every Countable term in this domain
     *                       (inclusive).
     *                       Must be greater than or equal to 0.
     */
    public CountableLengthMustBe (
                                  long minimum_length,
                                  long maximum_length
                                  )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation
    {
        this ( BoundedDomain.EndPoint.CLOSED,
               minimum_length,
               BoundedDomain.EndPoint.CLOSED,
               maximum_length );
    }


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param minimum_end_point Whether the minimum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param minimum_length The minimum <code> length () </code> of
     *                       every Countable term in this domain.
     *                       Must be greater than or equal to 0.
     *
     * @param maximum_end_point Whether the maximum is open, closed,
     *                          or none (ignored completely).
     *                          Must not be null.
     *
     * @param maximum_length The maximum <code> length () </code> of
     *                       every Countable term in this domain.
     *                       Must be greater than or equal to 0.
     */
    public CountableLengthMustBe (
            BoundedDomain.EndPoint minimum_end_point,
            long minimum_length,
            BoundedDomain.EndPoint maximum_end_point,
            long maximum_length
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeGreaterThanOrEqualTo.Violation
    {
        this (
            new Length (
                minimum_end_point,
                classContracts.check (
                    Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                    minimum_length ),
                maximum_end_point,
                classContracts.check (
                    new Parameter2.MustBeGreaterThanOrEqualTo ( minimum_length ),
                    maximum_length )
                )
            );
    }


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param length_filter A filter which must be matched by every Countable
     *                      <code> length () </code>.  Must not be null.
     */
    public CountableLengthMustBe (
                                  Filter<Number> length_filter
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new Length ( length_filter ) );
    }


    /**
     * <p>
     * Creates a new CountableLengthMustBe contract.
     * </p>
     *
     * @param domain The Length Domain to which every inspected
     *               Term must belong.  Must not be null.
     */
    public CountableLengthMustBe (
                                  Length domain
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               domain );

        this.domain = domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "The term must be Countable, with elements of "
            + this.domain
            + ".";
    }


    /**
     * @return The Domain to which every Term must belong.
     *         Never null.
     */
    public final Length domain ()
    {
        return this.domain;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final CountableLengthMustBe that =
            (CountableLengthMustBe) obj;

        if ( this.domain == null )
        {
            if ( that.domain != null )
            {
                return false;
            }
        }
        else if ( that.domain == null )
        {
            return false;
        }
        else if ( ! this.domain.equals ( that.domain ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Term<?> (which is an
        // Iterable<?>) to Iterable<Object>.
    public final FilterState filter (
            Term<?> term
            )
    {
        if ( term == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( ! ( term instanceof Countable ) )
        {
            return FilterState.DISCARDED;
        }

        final Countable<?> countable = (Countable<?>) term;
        @SuppressWarnings("unchecked") // Cast C<?> - C<Object>.
        final ArrayObject<?> array_object =
            new ArrayObject<Object> (
                Object.class,
                (Countable<Object>) countable );
        return this.domain.filter ( array_object );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ()
            + this.domain.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "Term must be "
            + this.domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public CountableLengthMustBe.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new CountableLengthMustBe.Violation ( this,
                                                     plaintiff,
                                                     evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public CountableLengthMustBe.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final CountableLengthMustBe.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the CountableLengthMustBe contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            CountableLengthMustBe.serialVersionUID;


        /**
         * <p>
         * Creates a new CountableLengthMustBe.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Term which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          CountableLengthMustBe contract,
                          Object plaintiff,
                          Term<?> evidence
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
         * Creates a new CountableLengthMustBe.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Term which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        @SuppressWarnings("unchecked") // Cast Countable<?> - Countable<Object>
        public Violation (
                          CountableLengthMustBe contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term did not belong to the domain: "
                    + ( ( evidence instanceof Countable )
                              ? contract.domain ().nonMember (
                                    new ArrayObject<Object> (
                                        Object.class,
                                        (Countable<Object>)
                                            evidence
                                    )
                                )
                              : evidence
                      )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
