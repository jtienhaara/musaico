package musaico.foundation.typing.typeclass;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;


/**
 * <p>
 * A Type must have instance(s) of the TypeClass child(ren) of
 * the parent TypeClass before it can produce a TypeClassInstance of
 * the parent TypeClass.
 * </p>
 *
 * <p>
 * For example, in order to be an instance of a "traveler" TypeClass,
 * which requires SymbolIDs OperationID "travel",
 * and which also requires an instance of TypeClass "vehicle", a
 * Type "commuter" might provide a Type "bicycle" which instantiates
 * the "vehicle" TypeClass, or a Type "globetrotter" might provide
 * a type "airplane" which instantiates "vehicle", and so on.
 * If an attempt is made to create a TypeClassInstance for a Type
 * which is missing one or more instances of the parent TypeClass's
 * child TypeClass(es), then this contract is violated.
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
public class TypeMustContainChildTypeClassInstance
    implements Contract<List<TypeClass>, TypeMustContainChildTypeClassInstance.Violation>, Serializable
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
            TypeMustContainChildTypeClassInstance.serialVersionUID;

        /**
         * <p>
         * Creates a new TypeMustContainChildTypeClassInstance.Violation
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
    private final TypeClass parentTypeClass;

    /** The Type which must instantiate the specific TypeClass. */
    private final Type<?> type;


    /**
     * <p>
     * Creates a new TypeMustContainChildTypeClassInstance contract
     * for the specified TypeClass.
     * </p>
     *
     * @param parent_type_class The parent TypeClass whose
     *                          child TypeClass(es) must be instantiated
     *                          by the specified Type in order to
     *                          produce a parent TypeClassInstance.
     *                          Must not be null.
     *
     * @param type The Type which must contain instances of the child
     *             TypeClass(es) in order to produce a TypeClassInstance
     *             of the parent TypeClass.  Must not be null.
     */
    public TypeMustContainChildTypeClassInstance (
                                                  TypeClass parent_type_class,
                                                  Type<?> type
                                                  )
    {
        this.parentTypeClass = parent_type_class;
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
            // Any TypeMustContainChildTypeClassInstance != null.
            return false;
        }
        else if ( object == this )
        {
            // Any TypeMustContainChildTypeClassInstance == itself.
            return true;
        }
        else if ( ! ( object instanceof TypeMustContainChildTypeClassInstance ) )
        {
            // Any TypeMustContainChildTypeClassInstance != any other object.
            return false;
        }

        final TypeMustContainChildTypeClassInstance that =
            (TypeMustContainChildTypeClassInstance) object;
        if ( ! this.parentTypeClass.equals ( that.parentTypeClass ) )
        {
            // Not the same parent TypeClass.
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
                               List<TypeClass> missing_type_class_instances
                               )
    {
        if ( missing_type_class_instances == null )
        {
            // Error, fail.
            return FilterState.DISCARDED;
        }
        else if ( missing_type_class_instances.size () > 0 )
        {
            // The Type is missing one or more child TypeClassInstance(s)
            // that are required to instantiate the parent TypeClass.
            // Discard.
            return FilterState.DISCARDED;
        }
        else
        {
            // The Type instantiates all child TypeClasses from the
            // parent TypeClass.  It's good, keep it.
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
            + this.parentTypeClass.hashCode () * 31
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
    public final TypeMustContainChildTypeClassInstance.Violation violation (
                                                                            Object plaintiff,
                                                                            List<TypeClass> inspectable_data,
                                                                            Throwable cause
                                                                            )
    {
        final TypeMustContainChildTypeClassInstance.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypeMustContainChildTypeClassInstance.Violation violation (
            Object plaintiff,
            List<TypeClass> inspectable_data
            )
    {
        return new TypeMustContainChildTypeClassInstance.Violation (
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
            + ", parent TypeClass "
            + this.parentTypeClass.id ().name ();
    }


    /**
     * @return The TypeClass which must be instantiated by the Type.
     *         Never null.
     */
    public final TypeClass parentTypeClass ()
    {
        return this.parentTypeClass;

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
