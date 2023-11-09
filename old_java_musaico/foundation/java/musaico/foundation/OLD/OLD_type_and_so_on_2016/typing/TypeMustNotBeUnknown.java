package musaico.foundation.typing;

import java.io.Serializable;

import java.util.Map;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts; 

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.metadata.Origin;


/**
 * <p>
 * A Contract on Types which does not permit UnknownTypes.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TypeMustNotBeUnknown
    implements Contract<Type<?>, TypeMustNotBeUnknown.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton TypeMustNotBeUnknown contract. */
    public static final TypeMustNotBeUnknown CONTRACT =
        new TypeMustNotBeUnknown ();


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeMustNotBeUnknown.class );


    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypeMustNotBeUnknown.serialVersionUID;

        /**
         * <p>
         * Creates a new TypeMustNotBeUnknown.Violation for some typing contract
         * that was breached.
         * </p>
         */
        public Violation (
                          Contract<?, ?> contract,
                          Serializable plaintiff,
                          Object value
                          )
        {
            this ( contract, plaintiff, value, null );
        }

        /**
         * <p>
         * Creates a new Violation for some typing contract
         * that was breached.
         * </p>
         */
        public Violation (
                          Contract<?, ?> contract,
                          Serializable plaintiff,
                          Object value,
                          Throwable cause
                          )
        {
            super ( contract,
                    plaintiff,
                    Contracts.makeSerializable ( value ) );

            if ( cause != null )
            {
                this.initCause ( cause );
            }
        }
    }


    /**
     * <p>
     * Only constructed by TypeMustNotBeUnknown.CONTRACT.
     * </p>
     */
    private TypeMustNotBeUnknown ()
    {
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            // Any TypeMustNotBeUnknown != null.
            return false;
        }
        else if ( object == this )
        {
            // Any TypeMustNotBeUnknown == itself.
            return true;
        }
        else if ( ! ( object instanceof TypeMustNotBeUnknown ) )
        {
            // Any TypeMustNotBeUnknown != any other object.
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Type<?> type
                               )
    {
        if ( type == null )
        {
            // Filter out nulls.
            return FilterState.DISCARDED;
        }
        else if ( type instanceof UnknownType )
        {
            // Filter out UnknownTypes.
            return FilterState.DISCARDED;
        }
        else
        {
            // Keep all other Types.
            return FilterState.KEPT;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }

    /**
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    public final TypeMustNotBeUnknown.Violation violation (
                                                           Object plaintiff,
                                                           Type<?> inspectable_data,
                                                           Throwable cause
                                                           )
    {
        final TypeMustNotBeUnknown.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypeMustNotBeUnknown.Violation violation (
                                                     Object plaintiff,
                                                     Type<?> inspectable_data
                                                     )
    {
        final Serializable serialized =
            Contracts.makeSerializable ( inspectable_data );

        return new TypeMustNotBeUnknown.Violation ( this,
                                                    Contracts.makeSerializable ( plaintiff ),
                                                    serialized );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () );
    }
}
