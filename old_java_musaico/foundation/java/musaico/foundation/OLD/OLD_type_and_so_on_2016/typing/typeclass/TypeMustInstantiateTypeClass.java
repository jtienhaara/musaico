package musaico.foundation.typing.typeclass;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;


/**
 * <p>
 * Specific SymbolIDs must be registered in a Type before it can
 * produce a TypeClassInstance of a TypeClass which requires them.
 * </p>
 *
 * <p>
 * For example, in order to be an instance of a "vehicle" TypeClass which
 * requires SymbolIDs ConstantID("max_speed"), OperationID("drive"),
 * and OperationID("brake"), a Type "bicycle" must provide a Constant and two
 * Operations with the corresponding IDs.  If an attempt is made to
 * create a TypeClassInstance for a Type which is missing one or more
 * required SymbolIDs, then this contract is violated.
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
public class TypeMustInstantiateTypeClass
    implements Contract<List<SymbolID<?>>, TypeMustInstantiateTypeClass.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;

    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypeMustInstantiateTypeClass.serialVersionUID;

        /**
         * <p>
         * Creates a new TypeMustInstantiateTypeClass.Violation
         * for some typing contract that was breached.
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


    /** The TypeClass which the Type must instantiate. */
    private final TypeClass typeClass;

    /** The Type which must instantiate the specific TypeClass. */
    private final Type<?> type;


    /**
     * <p>
     * Creates a new TypeMustInstantiateTypeClass contract for the specified
     * TypeClass.
     * </p>
     *
     * @param type_class The TypeClass whose required SymbolIDs must
     *                   be instantiated by the specified Type in order
     *                   to produce a TypeClassInstance.  Must not be null.
     *
     * @param type The Type which must include SymbolIDs to instantiate
     *             the specified TypeClass in order to produce
     *             a TypeClassInstance.  Must not be null.
     */
    public TypeMustInstantiateTypeClass (
                                         TypeClass type_class,
                                         Type<?> type
                                         )
    {
        this.typeClass = type_class;
        this.type = type;
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
            // Any TypeMustInstantiateTypeClass != null.
            return false;
        }
        else if ( object == this )
        {
            // Any TypeMustInstantiateTypeClass == itself.
            return true;
        }
        else if ( ! ( object instanceof TypeMustInstantiateTypeClass ) )
        {
            // Any TypeMustInstantiateTypeClass != any other object.
            return false;
        }

        final TypeMustInstantiateTypeClass that =
            (TypeMustInstantiateTypeClass) object;
        if ( ! this.typeClass.equals ( that.typeClass ) )
        {
            // Not the same TypeClass.
            return false;
        }
        else if ( ! this.type.equals ( that.type ) )
        {
            // Not the same Type.
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
                               List<SymbolID<?>> missing_symbol_ids
                               )
    {
        if ( missing_symbol_ids == null )
        {
            // Error, fail.
            return FilterState.DISCARDED;
        }
        else if ( missing_symbol_ids.size () > 0 )
        {
            // The Type is missing one or more SymbolIDs that are
            // required to instantiate the TypeClass.  Discard.
            return FilterState.DISCARDED;
        }
        else
        {
            // The Type instantiates all required SymbolIDs from the
            // TypeClass.  It's good, keep it.
            return FilterState.KEPT;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ()
            + this.typeClass.hashCode () * 31
            + this.type.hashCode () * 17;
    }

    /**
     * <p>
     * Convenience method.  Creates a violation then sets the root
     * cause of the newly created violation.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    public final TypeMustInstantiateTypeClass.Violation violation (
                                                                   Object plaintiff,
                                                                   List<SymbolID<?>> inspectable_data,
                                                                   Throwable cause
                                                                   )
    {
        final TypeMustInstantiateTypeClass.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypeMustInstantiateTypeClass.Violation violation (
            Object plaintiff,
            List<SymbolID<?>> inspectable_data
            )
    {
        return new TypeMustInstantiateTypeClass.Violation (
            this,
            Contracts.makeSerializable ( plaintiff ),
            Contracts.makeSerializable ( inspectable_data ) );
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + ": Type "
            + this.type.id ().name ()
            + ", TypeClass "
            + this.typeClass.id ().name ();
    }


    /**
     * @return The TypeClass which must be instantiated by the Type.
     *         Never null.
     */
    public final TypeClass typeClass ()
    {
        return this.typeClass;

    }


    /**
     * @return The Type which must instantiate the TypeClass.
     *         Never null.
     */
    public final Type<?> type ()
    {
        return this.type;

    }
}
