package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * A guarantee that a Dimensional value has element(s) at a specific
 * set of indices.
 * </p>
 *
 * <p>
 * For example, a 3-dimensional matrix might guarantee that it
 * has element(s) at indices <code> { 1, 2, 3 } </code>.  The
 * same 3-dimensional matrix, by definition, would also have
 * indices at indices <code> { 1 } </code>.
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
public class DimensionalMustHaveIndices
    implements Contract<Dimensional<?>, DimensionalMustHaveIndices.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( DimensionalMustHaveIndices.class );


    // The indices at which each dimensional value must have element(s).
    private final long [] indices;


    /**
     * <p>
     * Creates a new DimensionalMustHaveIndices contract,
     * requiring every inspected value have element(s) at the
     * specified indices.
     * </p>
     *
     * @param indices The position at which each dimensional value
     *                must have element(s).  Must not be null.
     */
    public DimensionalMustHaveIndices (
                                       long... indices
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) indices );

        this.indices = indices;
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

        final DimensionalMustHaveIndices that =
            (DimensionalMustHaveIndices) obj;
        if ( this.indices == null )
        {
            if ( that.indices == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.indices == null )
        {
            return false;
        }
        else if ( this.indices.length != that.indices.length )
        {
            return false;
        }

        for ( int i = 0; i < this.indices.length; i ++ )
        {
            if ( this.indices [ i ] != that.indices [ i ] )
            {
                return false;
            }
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
        else if ( dimensional.dimensions () < (long) this.indices.length )
        {
            return FilterState.DISCARDED;
        }
        else if ( dimensional.has ( this.indices ) )
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
        int hash_code = 0;
        hash_code += 31 * ClassName.of ( this.getClass () ).hashCode ();
        hash_code += this.indices.length * 17;
        for ( int i = 0; i < this.indices.length; i ++ )
        {
            hash_code += (int) this.indices [ i ];
        }

        return hash_code;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{" );
        boolean is_first = true;
        for ( int i = 0; i < this.indices.length; i ++ )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " );
            sbuf.append ( "" + this.indices [ i ] );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return ClassName.of ( this.getClass () ) + " " + sbuf.toString ();
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public DimensionalMustHaveIndices.Violation violation (
            Object plaintiff,
            Dimensional<?> inspectable
            )
    {
        return new DimensionalMustHaveIndices.Violation (
                       this,
                       plaintiff,
                       inspectable );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.value.Value, java.lang.Throwable)
     */
    @Override
    public DimensionalMustHaveIndices.Violation violation (
            Object plaintiff,
            Dimensional<?> inspectable,
            Throwable cause
            )
    {
        final DimensionalMustHaveIndices.Violation violation =
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
     * A violation of the DimensionalMustHaveIndices contract.
     * </p>
     */
    public static class Violation
        extends ValueViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            DimensionalMustHaveIndices.serialVersionUID;


        /**
         * <p>
         * Creates a new DimensionalMustHaveIndices.Violation.
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
         * Creates a new DimensionalMustHaveIndices.Violation.
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
