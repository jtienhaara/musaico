package musaico.foundation.term.countable;

import java.io.Serializable;

import java.math.BigDecimal;
import java.math.BigInteger;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Every index must be clamp-able to the range of valid indices
 * in a specific set of Elements.
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public class IndexMustBeValid
    implements Contract<Number, IndexMustBeValid.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( IndexMustBeValid.class );


    // The Elements for which every index must be valid.
    private final Elements<?> elements;


    /**
     * <p>
     * Creates a new IndexMustBeValid.
     * </p>
     *
     * @param elements The Elements for which every index
     *                  must be valid.  Must not be null.
     */
    public IndexMustBeValid (
            Elements<?> elements
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );

        this.elements = elements;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
        throws ReturnNeverNull.Violation
    {
        return "Each index must be within range of the valid indices"
            + " in " + this.elements;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final IndexMustBeValid that = (IndexMustBeValid) object;
        if ( this.elements == null )
        {
            if ( that.elements != null )
            {
                return false;
            }
        }
        else if ( that.elements == null )
        {
            return false;
        }
        else if ( this.elements == that.elements )
        {
            // Fall through.
        }
        else if ( ! this.elements.isFixedLength ()
                  || ! that.elements.isFixedLength () )
        {
            return false;
        }
        else if ( this.elements.start () != that.elements.start () )
        {
            return false;
        }
        else if ( this.elements.end () != that.elements.end () )
        {
            return false;
        }

        // We don't care about the contents, the valid index ranges are fixed
        // and always the same.

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Number index_object
                               )
        throws ReturnNeverNull.Violation
    {
        if ( index_object == null )
        {
            return FilterState.DISCARDED;
        }

        final long index;
        final String index_string;
        if ( index_object instanceof Long )
        {
            final Long index_long = (Long) index_object;
            index = index_long.longValue ();
        }
        else if ( index_object instanceof BigDecimal )
        {
            try
            {
                final BigDecimal index_big_decimal =
                    (BigDecimal) index_object;
                index = index_big_decimal.longValueExact (); // Throws ex.
            }
            catch ( Exception e )
            {
                // Not an integral number, or not small enough
                // to fit in a long, so not a valid index.
                return FilterState.DISCARDED;
            }
        }
        else if ( index_object instanceof BigInteger )
        {
            final BigInteger index_big_integer =
                (BigInteger) index_object;
            index = index_big_integer.longValue ();
            if ( BigInteger.valueOf ( index )
                 .compareTo ( index_big_integer ) != 0 )
            {
                // Not small enough to fit in a long,
                // so not a valid index.
                return FilterState.DISCARDED;
            }
        }
        else if ( index_object instanceof Double )
        {
            final Double index_double = (Double) index_object;
            index = index_double.longValue ();
            if ( ( (double) index ) != index_double )
            {
                // Not an integral number, so not a valid index.
                return FilterState.DISCARDED;
            }
        }
        else if ( index_object instanceof Float )
        {
            final Float index_float = (Float) index_object;
            index = index_float.longValue ();
            if ( ( (double) index ) != index_float )
            {
                // Not an integral number, so not a valid index.
                return FilterState.DISCARDED;
            }
        }
        else
        {
            index = index_object.longValue ();
        }

        // Now clamp the index and see if it's in range.
        final long clamped =
            this.elements.clamp ( index,            // index
                                  0L,               // start
                                  Long.MAX_VALUE ); // end

        if ( clamped < 0L )
        {
            return FilterState.DISCARDED;
        }
        else
        {
            return FilterState.KEPT;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 23 * ClassName.of ( this.getClass () ).hashCode ()
            + this.elements.getClass ().getName ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.elements + " )";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public IndexMustBeValid.Violation violation (
            Object plaintiff,
            Number evidence
            )
        throws ReturnNeverNull.Violation
    {
        return new IndexMustBeValid.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object java.lang.Object, java.lang.Throwable)
     */
    @Override
    public IndexMustBeValid.Violation violation (
            Object plaintiff,
            Number evidence,
            Throwable cause
            )
        throws ReturnNeverNull.Violation
    {
        final IndexMustBeValid.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the IndexMustBeValid contract.
     * </p>
     */
    public static class Violation
        extends CheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            IndexMustBeValid.serialVersionUID;


        /**
         * <p>
         * Creates a new IndexMustBeValid.Violation.
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
         * @param evidence The index which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Number evidence
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
         * Creates a new IndexMustBeValid.Violation.
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
         * @param evidence The index which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          Number evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The index's value was invalid.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
