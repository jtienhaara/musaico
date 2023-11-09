package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.equality.NotEqualTo;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Type;


/**
 * <p>
 * Each inspected Type must be <code> equal ( ... ) </code> to a
 * specific Type, or an UncheckedViolation will be generated.
 * </p>
 *
 * @see musaico.foundation.domains.NotEqualTo
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
public class TypeMustNotEqual
    implements Contract<Type<?>, TypeMustNotEqual.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( TypeMustNotEqual.class );


    // The NotEqualTo Domain to which ever Type must belong.
    private final NotEqualTo domain;


    /**
     * <p>
     * Creates a new TypeMustNotEqual contract.
     * </p>
     *
     * @param type The Type to which every inspected Type
     *              must be equal.  Must not be null.
     */
    public TypeMustNotEqual (
                              Type<?> type
                              )
        throws ParametersMustNotBeNull.Violation
    {
        this (
              new NotEqualTo ( type ) );
    }


    /**
     * <p>
     * Creates a new TypeMustNotEqual contract.
     * </p>
     *
     * @param domain The NotEqualTo Domain to which every inspected Type
     *               must belong.  Must not be null.
     */
    public TypeMustNotEqual (
                              NotEqualTo domain
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
        return "The type must belong to the domain of objects "
            + this.domain
            + ".";
    }


    /**
     * @return The Domain to which every Type must belong.
     *         Never null.
     */
    public final NotEqualTo domain ()
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

        final TypeMustNotEqual that =
            (TypeMustNotEqual) obj;

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
    public final FilterState filter (
            Type<?> type
            )
    {
        if ( type == null )
        {
            return FilterState.DISCARDED;
        }

        return this.domain.filter ( type );
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
        return "Type must be "
            + this.domain;
    }


    /**
     * @see musaico.foundation.domains.NotEqualTo#value()
     */
    public final Object value ()
    {
        return this.domain;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypeMustNotEqual.Violation violation (
            Object plaintiff,
            Type<?> evidence
            )
    {
        return new TypeMustNotEqual.Violation ( this,
                                              plaintiff,
                                              evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public TypeMustNotEqual.Violation violation (
            Object plaintiff,
            Type<?> evidence,
            Throwable cause
            )
    {
        final TypeMustNotEqual.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TypeMustNotEqual contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypeMustNotEqual.serialVersionUID;


        /**
         * <p>
         * Creates a new TypeMustNotEqual.Violation.
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
         * @param evidence The Type which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          TypeMustNotEqual contract,
                          Object plaintiff,
                          Type<?> evidence
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
         * Creates a new TypeMustNotEqual.Violation.
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
         * @param evidence The Type which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          TypeMustNotEqual contract,
                          Object plaintiff,
                          Type<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The type did not belong to the domain: "
                    + evidence
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
