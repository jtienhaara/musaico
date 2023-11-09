package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * A guarantee that a Dimensional value has exactly N dimensions.
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public class DimensionalMustHaveExactlyNDimensions
    implements Contract<Dimensional<?>, DimensionalMustHaveExactlyNDimensions.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( DimensionalMustHaveExactlyNDimensions.class );


    /** Each value must have exactly 1 dimension. */
    public static final DimensionalMustHaveExactlyNDimensions CONTRACT_1D =
        new DimensionalMustHaveExactlyNDimensions ( 1L );

    /** Each value must have exactly 2 dimensions. */
    public static final DimensionalMustHaveExactlyNDimensions CONTRACT_2D =
        new DimensionalMustHaveExactlyNDimensions ( 2L );

    /** Each value must have exactly 3 dimensions. */
    public static final DimensionalMustHaveExactlyNDimensions CONTRACT_3D =
        new DimensionalMustHaveExactlyNDimensions ( 3L );

    /** Each value must have exactly 4 dimensions. */
    public static final DimensionalMustHaveExactlyNDimensions CONTRACT_4D =
        new DimensionalMustHaveExactlyNDimensions ( 4L );


    // Each Dimensional value must have exactly this many dimensions.
    private final long dimensions;


    /**
     * <p>
     * Creates a new DimensionalMustHaveExactlyNDimensions contract,
     * requiring every inspected value have the specified number
     * of dimensions.
     * </p>
     *
     * @param dimensions The exact number of dimensions.
     *                   Must be greater than or equal to 0L.
     */
    public DimensionalMustHaveExactlyNDimensions (
                                                  long dimensions
                                                  )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation
    {
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               dimensions );

        this.dimensions = dimensions;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return !!!;
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

        final DimensionalMustHaveExactlyNDimensions that =
            (DimensionalMustHaveExactlyNDimensions) obj;
        if ( this.dimensions != that.dimensions )
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
                                Dimensional<?> dimensional
                                )
    {
        if ( dimensional == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( dimensional.dimensions () == this.dimensions )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return
            ClassName.of ( this.getClass () ).hashCode ()
            + (int) this.dimensions * 17;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + "[" + this.dimensions + "]";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public DimensionalMustHaveExactlyNDimensions.Violation violation (
            Object plaintiff,
            Dimensional<?> inspectable
            )
    {
        return new DimensionalMustHaveExactlyNDimensions.Violation (
                       this,
                       plaintiff,
                       inspectable );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.value.Value, java.lang.Throwable)
     */
    @Override
    public DimensionalMustHaveExactlyNDimensions.Violation violation (
            Object plaintiff,
            Dimensional<?> inspectable,
            Throwable cause
            )
    {
        final DimensionalMustHaveExactlyNDimensions.Violation violation =
            this.violation ( plaintiff,
                             inspectable );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }




    /**
     * <p>
     * A violation of the DimensionalMustHaveExactlyNDimensions contract.
     * </p>
     */
    public static class Violation
        extends ValueViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            DimensionalMustHaveExactlyNDimensions.serialVersionUID;


        /**
         * <p>
         * Creates a new DimensionalMustHaveExactlyNDimensions.Violation.
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
         * @param inspectable The Value which violated the contract.
         *                    Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Value<?> inspectable
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this ( contract,
                   plaintiff,
                   inspectable,
                   null ); // cause
        }


        /**
         * <p>
         * Creates a new DimensionalMustHaveExactlyNDimensions.Violation.
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
         * @param inspectable The Value which violated the contract.
         *                    Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Value<?> inspectable,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    !!!, // description
                    plaintiff,
                    inspectable,
                    cause );
        }
    }
}
