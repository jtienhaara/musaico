package musaico.foundation.typing;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts; 

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.metadata.Origin;


/**
 * <p>
 * A Contract on Symbol Types which ensures that every checked
 * Type's #input1, #output, #value and so on Types have the same value classes
 * as the ones in a specific Symbol Type.
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
public class TypesMustHaveSameValueClasses
    implements Contract<Type<? extends Symbol>, TypesMustHaveSameValueClasses.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypesMustHaveSameValueClasses.class );


    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypesMustHaveSameValueClasses.serialVersionUID;

        /**
         * <p>
         * Creates a new TypesMustHaveSameValueClasses.Violation
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


    // The Symbol Type to check against.
    private final Type<? extends Symbol> symbolType;


    /**
     * <p>
     * Creates a new TypesMustHaveSameValueClasses to ensure every
     * checked Symboll Type has the same #input1, #output, #value and so on
     * value classes as the specified Symbol Type.
     * </p>
     */
    public TypesMustHaveSameValueClasses (
                                          Type<? extends Symbol> symbol_type
                                          )
    {
        this.symbolType = symbol_type;
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
            // Any TypesMustHaveSameValueClasses != null.
            return false;
        }
        else if ( object == this )
        {
            // Any TypesMustHaveSameValueClasses == itself.
            return true;
        }
        else if ( ! ( object instanceof TypesMustHaveSameValueClasses ) )
        {
            // Any TypesMustHaveSameValueClasses != any other object.
            return false;
        }

        final TypesMustHaveSameValueClasses that =
            (TypesMustHaveSameValueClasses) object;

        if ( ! this.symbolType.equals ( that.symbolType ) )
        {
            // TypesMustHaveSameValueClasses with OpType X
            // != TypesMustHaveSameValueClasses with OpType Y.
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
                               Type<? extends Symbol> symbol_type
                               )
    {
        if ( symbol_type == null )
        {
            // Filter out nulls.
            return FilterState.DISCARDED;
        }

        final Map<SymbolID<?>, Class<?>> expected_value_classes =
            TypesMustHaveSameValueClasses.valueClasses ( this.symbolType );
        final Map<SymbolID<?>, Class<?>> actual_value_classes =
            TypesMustHaveSameValueClasses.valueClasses ( symbol_type );

        if ( expected_value_classes.size ()
             != actual_value_classes.size () )
        {
            // Filter out every Symbol Type that has a different number
            // of #input1, #output, #value and so on Types.
            return FilterState.DISCARDED;
        }

        for ( SymbolID<?> expected_symbol_id
                  : expected_value_classes.keySet () )
        {
            final Class<?> expected_value_class =
                expected_value_classes.get ( expected_symbol_id );
            final Class<?> actual_value_class =
                actual_value_classes.get ( expected_symbol_id );

            if ( ! expected_value_class.equals ( actual_value_class ) )
            {
                // Value classes do not match.
                return FilterState.DISCARDED;
            }
        }

        // The Symbol Type has all the right value classes.
        // We keep it.
        return FilterState.KEPT;
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
     * @return A Map of SymbolIDs pointing to the value classes of the
     *         Types identified by the specified #input1, #output, #value
     *         and so on SymbolIDs.  Never null.  Never contains
     *         any null elements.
     */
    public static Map<SymbolID<?>, Class<?>> valueClasses (
                                                           Type<? extends Symbol> symbol_type
                                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        final Map<SymbolID<?>, Class<?>> value_classes =
            new HashMap<SymbolID<?>, Class<?>> ();
        for ( SymbolType child_symbol_type : symbol_type.symbolTypes () )
        {
            for ( SymbolID<?> symbol_id
                      : symbol_type.symbolIDs ( child_symbol_type.asType () ) )
            {
                if ( symbol_id.visibility () != Visibility.PRIVATE )
                {
                    // We're only interested in PRIVATE input1, output,
                    // value, and so on Types.
                    continue;
                }

                final Symbol symbol = symbol_type.symbol ( symbol_id )
                    .orNull ();
                if ( ! ( symbol instanceof Type  ) )
                {
                    // We're only interested in Types, such as #input1 Type,
                    // #output Type, #value Type, and so on.
                    continue;
                }

                final Type<?> type = (Type<?>) symbol;
                final Class<?> value_class = type.valueClass ();
                value_classes.put ( symbol_id, value_class );
            }
        }

        return value_classes;
    }


    /**
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    public final TypesMustHaveSameValueClasses.Violation violation (
        Object plaintiff,
        Type<? extends Symbol> inspectable_data,
        Throwable cause
        )
    {
        final TypesMustHaveSameValueClasses.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TypesMustHaveSameValueClasses.Violation violation (
        Object plaintiff,
        Type<? extends Symbol> inspectable_data
        )
    {
        final Serializable serialized =
            Contracts.makeSerializable ( inspectable_data );

        return new TypesMustHaveSameValueClasses.Violation (
                       this,
                       Contracts.makeSerializable ( plaintiff ),
                       serialized );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + "(" + this.symbolType.id ().name () + ")";
    }
}
