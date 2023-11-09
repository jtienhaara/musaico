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
 * A Contract which ensures only Symbols with unique SymbolIDs can be added
 * to a SymbolTable.
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
public class SymbolMustBeUnique
    implements Contract<SymbolID<?>, SymbolMustBeUnique.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SymbolMustBeUnique.class );


    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            SymbolMustBeUnique.serialVersionUID;

        /**
         * <p>
         * Creates a new SymbolMustBeUnique.Violation for some typing contract
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


    /** The set of unique Symbols to check against. */
    private final Map<SymbolID<? extends Symbol>, Symbol> existingSymbols;


    /**
     * <p>
     * Creates a new SymbolMustBeUnique contract for the specified
     * existing unique Symbols.
     * </p>
     *
     * @param existing_symbol The existing unique SymbolIDs, pointing to their
     *                        corresponding Symbols.
     *                        Must not be null.
     *                        Must not contain any null elements.
     */
    public SymbolMustBeUnique (
                               Map<SymbolID<? extends Symbol>, Symbol> existing_symbols
                               )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               existing_symbols );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               existing_symbols.keySet () );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               existing_symbols.values () );

        this.existingSymbols = existing_symbols;
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
            // Any SymbolMustBeUnique != null.
            return false;
        }
        else if ( object == this )
        {
            // Any SymbolMustBeUnique == itself.
            return true;
        }
        else if ( ! ( object instanceof SymbolMustBeUnique ) )
        {
            // Any SymbolMustBeUnique != any other object.
            return false;
        }

        final SymbolMustBeUnique that = (SymbolMustBeUnique) object;
        for ( SymbolID<? extends Symbol> this_symbol_id
                  : this.existingSymbols.keySet () )
        {
            if ( ! that.existingSymbols.containsKey ( this_symbol_id ) )
            {
                // This contains a SymbolID not contained in That.
                return false;
            }

            final Symbol this_symbol =
                this.existingSymbols.get ( this_symbol_id );
            final Symbol that_symbol =
                that.existingSymbols.get ( this_symbol_id );
            if ( this_symbol == null )
            {
                if ( that_symbol != null )
                {
                    // This class maps to a different Symbol than that.
                    return false;
                }
            }
        }

        for ( SymbolID<? extends Symbol> that_symbol_id
                  : that.existingSymbols.keySet () )
        {
            if ( ! this.existingSymbols.containsKey ( that_symbol_id ) )
            {
                // That does not contain a SymbolID which is contained in this.
                return false;
            }
        }

        // Everything is all matchy-matchy.
        return true;
    }

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               SymbolID<?> symbol_id
                               )
    {
        if ( this.existingSymbols.containsKey ( symbol_id ) )
        {
            // Not unique, the same symbol ID already exists in the set.
            return FilterState.DISCARDED;
        }
        else
        {
            // Unique.
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
            + this.existingSymbols.hashCode () * 31;
    }

    /**
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    public final SymbolMustBeUnique.Violation violation (
                                                         Object plaintiff,
                                                         SymbolID<?> inspectable_data,
                                                         Throwable cause
                                                         )
    {
        final SymbolMustBeUnique.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    @Override
    public SymbolMustBeUnique.Violation violation (
                                                   Object plaintiff,
                                                   SymbolID<?> inspectable_data
                                                   )
    {
        final Serializable serialized =
            Contracts.makeSerializable ( inspectable_data );

        return new SymbolMustBeUnique.Violation ( this,
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
