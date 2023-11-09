package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.InstanceOfClass;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Type;


/**
 * <p>
 * A general-purpose Contract for Types: each Type
 * must NOT be an instance of a specific class or interface,
 * or an UncheckedViolation will be generated.
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
 * @see musaico.foundation.term.contracts.MODULE#COPYRIGHT
 * @see musaico.foundation.term.contracts.MODULE#LICENSE
 */
public class TypeMustNotBeInstanceOfClass
    implements Contract<Type<?>, TypeMustNotBeInstanceOfClass.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( TypeMustNotBeInstanceOfClass.class );


    // The Domain to which every Type must belong.
    private final InstanceOfClass domain;


    /**
     * <p>
     * Creates a new TypeMustNotBeInstanceOfClass contract.
     * </p>
     *
     * @param type_class The Class which each inspected Type must instantiate.
     *                   Must not be null.
     */
    public TypeMustNotBeInstanceOfClass (
            Class<?> type_class
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new InstanceOfClass ( type_class ) );
    }


    /**
     * <p>
     * Creates a new TypeMustNotBeInstanceOfClass contract.
     * </p>
     *
     * @param domain The InstanceOfClass domain to which
     *               each  inspected Type must belong.  Must not be null.
     */
    public TypeMustNotBeInstanceOfClass (
            InstanceOfClass domain
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
        return "Each type must NOT be "
            + this.domain
            + ".";
    }


    /**
     * @return The InstanceOfClass domain to which each Type must NOT belong.
     *         Never null.
     */
    public final InstanceOfClass domain ()
    {
        return this.domain;
    }


    /**
     * @see musaico.foundation.domains.InstanceOfClass#domainClass()
     */
    public final Class<?> domainClass ()
    {
        return this.domain.domainClass ();
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

        final TypeMustNotBeInstanceOfClass that =
            (TypeMustNotBeInstanceOfClass) obj;

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

        final FilterState result = this.domain.filter ( type )
            .opposite ();
        return result;
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
        return ClassName.of ( this.getClass () )
            + " { "
            + this.domain
            + " }";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final TypeMustNotBeInstanceOfClass.Violation violation (
            Object plaintiff,
            Type<?> evidence
            )
    {
        return new TypeMustNotBeInstanceOfClass.Violation ( this,
                                                          plaintiff,
                                                          evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public final TypeMustNotBeInstanceOfClass.Violation violation (
            Object plaintiff,
            Type<?> evidence,
            Throwable cause
            )
    {
        final TypeMustNotBeInstanceOfClass.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TypeMustNotBeInstanceOfClass contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypeMustNotBeInstanceOfClass.serialVersionUID;


        /**
         * <p>
         * Creates a new TypeMustNotBeInstanceOfClass.Violation.
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
                          TypeMustNotBeInstanceOfClass contract,
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
         * Creates a new TypeMustNotBeInstanceOfClass.Violation.
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
                          TypeMustNotBeInstanceOfClass contract,
                          Object plaintiff,
                          Type<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The type instantiates the class: "
                    + evidence
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
