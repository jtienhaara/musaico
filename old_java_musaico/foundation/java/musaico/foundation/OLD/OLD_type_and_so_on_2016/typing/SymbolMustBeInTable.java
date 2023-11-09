package musaico.foundation.typing;

import java.io.Serializable;

import java.util.Set;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A Contract which ensures a SymbolTable has a Symbol with a
 * specific SymbolID.
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
public class SymbolMustBeInTable
    implements Contract<SymbolID<?>, SymbolMustBeInTable.Violation>, Serializable
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
            SymbolMustBeInTable.serialVersionUID;

        /**
         * <p>
         * Creates a new SymbolMustBeInTable.Violation for some typing contract
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


    /** The SymbolTable to check against. */
    private final SymbolTable symbolTable;


    /**
     * <p>
     * Creates a new SymbolMustBeInTable contract for the specified
     * SymbolTable.
     * </p>
     *
     * @param symbol_table The SymbolTable which must contain
     *                     specific SymbolIDs.
     *                     Must not be null.
     */
    public SymbolMustBeInTable (
                                SymbolTable symbol_table
                                )
    {
        this.symbolTable = symbol_table;
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
            // Any SymbolMustBeInTable != null.
            return false;
        }
        else if ( object == this )
        {
            // Any SymbolMustBeInTable == itself.
            return true;
        }
        else if ( ! ( object instanceof SymbolMustBeInTable ) )
        {
            // Any SymbolMustBeInTable != any other object.
            return false;
        }

        final SymbolMustBeInTable that = (SymbolMustBeInTable) object;
        if ( ! this.symbolTable.equals ( that.symbolTable ) )
        {
            // Not the same SymbolTable.
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
                               SymbolID<?> id
                               )
    {
        if ( this.symbolTable.containsSymbol ( id ) )
        {
            // In SymbolTable.
            return FilterState.KEPT;
        }
        else
        {
            // Not registered.
            return FilterState.DISCARDED;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ()
            + this.symbolTable.hashCode () * 31;
    }


    /**
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    public final SymbolMustBeInTable.Violation violation (
                                                          Object plaintiff,
                                                          SymbolID<?> inspectable_data,
                                                          Throwable cause
                                                          )
    {
        final SymbolMustBeInTable.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public SymbolMustBeInTable.Violation violation (
                                                    Object plaintiff,
                                                    SymbolID<?> inspectable_data
                                                    )
    {
        return new SymbolMustBeInTable.Violation ( this,
                                                   Contracts.makeSerializable ( plaintiff ),
                                                   Contracts.makeSerializable ( inspectable_data ) );
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
