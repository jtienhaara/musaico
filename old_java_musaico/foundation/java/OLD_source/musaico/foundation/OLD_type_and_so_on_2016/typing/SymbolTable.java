package musaico.foundation.typing;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A lookup of Symbols, such as Operations, Types, Tags and so on,
 * by SymbolID.
 * </p>
 *
 * <p>
 * Typically a Type is constructed with an empty SymbolTable, then
 * immediately after constructing the Type, the SymbolTable is
 * filled in with Operations and Tags.  Thereafter the Type itself might
 * add Symbols dynamically, but no other object should access the
 * SymbolTable directly.
 * </p>
 *
 *
 * <p>
 * In Java every SymbolTable must be Serializable in order to
 * play nicely over RMI.  Its component Symbols, and their component
 * objects, must also be Serializable.
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
public class SymbolTable
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks contracts on methods for us.
    private final ObjectContracts contracts;

    // Contract can be violated when looking up Symbols.
    private final SymbolMustBeInTable symbolMustBeInTable;

    // Synchronize critial sections on this lock:
    private final Serializable lock = new String ();

    // Lookup of lookups: SymbolTypes - SymbolIDs - Symbols.
    //  SymbolTypes are in the order they were added.
    //  Within each SymbolType, SymbolIDs are in the order they were added.
    private final LinkedHashMap<Type<? extends Symbol>, LinkedHashMap<SymbolID<?>, Symbol>> symbols;

    // MUTABLE hash code changes with each symbol added.
    private int hashCode = 0;


    /**
     * <p>
     * Creates a new empty SymbolTable.
     * </p>
     */
    public SymbolTable ()
    {
        this.symbols =
            new LinkedHashMap<Type<? extends Symbol>, LinkedHashMap<SymbolID<?>, Symbol>> ();

        this.symbolMustBeInTable = new SymbolMustBeInTable ( this );

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a copy of the specified SymbolTable.
     * </p>
     *
     * <p>
     * Subsequent changes made to the specified SymbolTable shall NOT
     * be reflected in this one.
     * </p>
     *
     * @param that The SymbolTable to copy.  Must not be null.
     */
    public SymbolTable (
                        SymbolTable that
                        )
    {
        this ();

        synchronized ( that.lock )
        {
            for ( Type<? extends Symbol> symbol_type : that.symbols.keySet () )
            {
                final LinkedHashMap<SymbolID<?>, Symbol> that_by_symbol_type =
                    that.symbols.get ( symbol_type );
                final LinkedHashMap<SymbolID<?>, Symbol> this_by_symbol_type =
                    new LinkedHashMap<SymbolID<?>, Symbol> ();
                this.symbols.put ( symbol_type, this_by_symbol_type );

                int s = 0;
                for ( SymbolID<?> symbol_id : that_by_symbol_type.keySet () )
                {
                    if ( ! symbol_id.visibility ().isCopyable () )
                    {
                        continue;
                    }

                    final Symbol symbol  =
                        that_by_symbol_type.get ( symbol_id );

                    hashCode += ( s + 1 ) * symbol.hashCode ();
                    s ++;
                }
            }
        }
    }


    /**
     * <p>
     * Adds the specified Symbol to this SymbolTable.
     * </p>
     *
     * <p>
     * If a Symbol with the same SymbolID already exists in this
     * SymbolTable, then a violation will be thrown.
     * </p>
     *
     * @param symbol The Symbol to add to this SymbolTable.
     *               Must not be null.
     *
     * @return This SymbolTable.  Never null.
     *
     * @throws SymbolMustBeUnique.Violation If a Symbol with the same id
     *                                      already exists in this table.
     */
    public SymbolTable add (
                            Symbol symbol
                            )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeUnique.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol );

        final SymbolID<? extends Symbol> id = symbol.id ();
        if ( id.visibility () == Visibility.INVISIBLE )
        {
            // Sorry, bub, no can do.
            final LinkedHashMap<SymbolID<?>, Symbol> symbol_ids =
                new LinkedHashMap<SymbolID<?>, Symbol> ();
            symbol_ids.put ( id, id.type ().none () );

            final SymbolMustBeUnique must_be_unique =
                new SymbolMustBeUnique ( symbol_ids );
            final SymbolMustBeUnique.Violation violation =
                must_be_unique.violation ( this, id );
            throw violation;
        }

        final Type<? extends Symbol> symbol_type = id.type ();
        synchronized ( this.lock )
        {
            LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                this.symbols.get ( symbol_type );
            if ( by_symbol_type == null )
            {
                by_symbol_type = new LinkedHashMap<SymbolID<?>, Symbol> ();
                this.symbols.put ( symbol_type, by_symbol_type );
            }
            else
            {
                final SymbolMustBeUnique must_be_unique =
                    new SymbolMustBeUnique ( by_symbol_type );
                if ( ! must_be_unique.filter ( id ).isKept () )
                {
                    throw must_be_unique.violation ( this,
                                                     id );
                }
            }

            this.hashCode +=
                ( by_symbol_type.size () + 1 ) * symbol.hashCode ();

            by_symbol_type.put ( id, symbol );
        }

        return this;
    }


    /**
     * <p>
     * Adds all the Symbols for SymbolIDs from the specified SymbolTable
     * which are not already in this SymbolTable.
     * </p>
     *
     * <p>
     * If a Symbol with the same SymbolID already exists in this
     * SymbolTable, then it will remain as is, and the Symbol from
     * the specified SymbolTable will be ignored.
     * </p>
     *
     * @param that The SymbolTable whose Symbols will be added
     *             to this SymbolTable.  Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast SymbolID<?> - SymbolID<Symbol>.
    public SymbolTable addAll (
                               SymbolTable that
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        // Prevent starvation by making a temporary copy of that,
        // in order to prevent concurrent modification to that's symbols.
        // Then lock ourselves to prevent concurrent modification to this
        // while we're adding all the symbols.
        final SymbolTable temporary = new SymbolTable ( that );
        synchronized ( this.lock )
        {
            for ( Type<? extends Symbol> symbol_type : temporary.symbols.keySet () )
            {
                final LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                    temporary.symbols.get ( symbol_type );

                for ( SymbolID<?> symbol_id : by_symbol_type.keySet () )
                {
                    if ( ! symbol_id.visibility ().isCopyable () )
                    {
                        continue;
                    }

                    if ( ! this.containsSymbol ( symbol_id ) )
                    {
                        final Symbol symbol =
                            by_symbol_type.get ( symbol_id );
                        final SymbolID<Symbol> compilable_id =
                            (SymbolID<Symbol>) symbol_id;
                        this.set ( compilable_id, symbol );
                    }
                }
            }
        }

        return this;
    }


    /**
     * <p>
     * Adds all the Symbols for SymbolIDs from the specified Namespace
     * which are not already in this SymbolTable.
     * </p>
     *
     * <p>
     * If a Symbol with the same SymbolID already exists in this
     * SymbolTable, then it will remain as is, and the Symbol from
     * the specified Namespace will be ignored.
     * </p>
     *
     * @param namespace The Namespace whose Symbols will be added
     *                  to this SymbolTable.  Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast SymbolID<?> - SymbolID<Symbol>.
    public SymbolTable addAll (
                               Namespace namespace
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               namespace );

        // Assume the Namespace is not changing while we copy it.
        // Lock ourselves to prevent concurrent modification to this
        // while we're adding all the symbols.
        synchronized ( this.lock )
        {
            for ( SymbolType symbol_type : namespace.symbolTypes () )
            {
                for ( SymbolID<?> symbol_id : namespace.symbolIDs ( symbol_type.asType () ) )
                {
                    if ( ! symbol_id.visibility ().isCopyable () )
                    {
                        continue;
                    }

                    if ( ! this.containsSymbol ( symbol_id ) )
                    {
                        final Symbol symbol =
                            namespace.symbol ( symbol_id ).orNone ();
                        final SymbolID<Symbol> compilable_id =
                            (SymbolID<Symbol>) symbol_id;
                        this.set ( compilable_id, symbol );
                    }
                }
            }
        }

        return this;
    }


    /**
     * <p>
     * Adds all the specified Symbols to this SymbolTable.
     * </p>
     *
     * <p>
     * If a Symbol with the same SymbolID already exists in this
     * SymbolTable, then it will remain as is, and the Symbol from
     * the specified Namespace will be ignored.
     * </p>
     *
     * @param Symbols The Symbols to add to this SymbolTable.
     *                Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast SymbolID<?> - SymbolID<Symbol>.
    public SymbolTable addAll (
                               Symbol ... symbols
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) symbols );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               symbols );

        // Lock ourselves to prevent concurrent modification to this
        // while we're adding all the symbols.
        synchronized ( this.lock )
        {
            for ( Symbol symbol : symbols )
            {
                final SymbolID<?> symbol_id = symbol.id ();
                if ( symbol_id.visibility () == Visibility.INVISIBLE )
                {
                    // Skip invisible symbols!
                    continue;
                }

                if ( ! this.containsSymbol ( symbol_id ) )
                {
                    final SymbolID<Symbol> compilable_id =
                        (SymbolID<Symbol>) symbol_id;
                    this.set ( compilable_id, symbol );
                }
            }
        }

        return this;
    }


    /**
     * <p>
     * Adds all the specified Symbols to this SymbolTable.
     * </p>
     *
     * <p>
     * If a Symbol with the same SymbolID already exists in this
     * SymbolTable, then it will remain as is, and the Symbol from
     * the specified Namespace will be ignored.
     * </p>
     *
     * @param Symbols The Symbols to add to this SymbolTable.
     *                Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast SymbolID<?> - SymbolID<Symbol>.
    public SymbolTable addAll (
                               Iterable<Symbol> symbols
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbols );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               symbols );

        // Lock ourselves to prevent concurrent modification to this
        // while we're adding all the symbols.
        synchronized ( this.lock )
        {
            for ( Symbol symbol : symbols )
            {
                final SymbolID<?> symbol_id = symbol.id ();
                if ( symbol_id.visibility () == Visibility.INVISIBLE )
                {
                    // Skip invisible symbols!
                    continue;
                }

                if ( ! this.containsSymbol ( symbol_id ) )
                {
                    final SymbolID<Symbol> compilable_id =
                        (SymbolID<Symbol>) symbol_id;
                    this.set ( compilable_id, symbol );
                }
            }
        }

        return this;
    }


    /**
     * <p>
     * Returns true if this SymbolTable has a Symbol by the
     * specified identifier, false if not.
     * </p>
     *
     * @param id The SymbolID to look up.  Must not be null.
     *
     * @return True if this SymbolTable contains a Symbol identified
     *         by the specified identifier, false if not.
     */
    public boolean containsSymbol (
                                   SymbolID<?> id
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        if ( ! id.visibility ().isContainsQueryable () )
        {
            // Sorry, bub, no can do.
            return false;
        }

        final Type<? extends Symbol> symbol_type = id.type ();
        try
        {
            synchronized ( this.lock )
            {
                final LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                    this.symbols.get ( symbol_type );
                if ( by_symbol_type != null )
                {
                    if ( by_symbol_type.get ( id ) != null )
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        catch ( Exception e ) // In case of unlikely ClassCastException.
        {
            return false;
        }
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
            return false;
        }
        else if ( this == object )
        {
            return true;
        }
        else if ( ! ( object instanceof SymbolTable ) )
        {
            return false;
        }

        final SymbolTable that = (SymbolTable) object;
        if ( this.hashCode () == that.hashCode () )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        synchronized ( this.lock )
        {
            return this.hashCode;
        }
    }


    /**
     * @return The atomic lock for this SymbolTable.  Can be used
     *         to ensure atomicity when querying and adding to
     *         this SymbolTable.  Never null.
     */
    public Serializable lock ()
    {
        return this.lock;
    }


    /**
     * @return All the contents of this SymbolTable as one big String.
     *         Never null.
     */
    public String printSymbolTable ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        synchronized ( this.lock )
        {
            sbuf.append ( "Symbol table " + super.hashCode () + ":\n" );
            for ( Type<? extends Symbol> symbol_type : this.symbols.keySet () )
            {
                final String prefix;
                if ( symbol_type instanceof OperationType )
                {
                    prefix = "operation ";
                }
                else
                {
                    prefix = "";
                }

                sbuf.append ( "  Type " + prefix + symbol_type.id ().name ()
                              + " symbols:\n" );

                final LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                    this.symbols.get ( symbol_type );

                for ( SymbolID<?> symbol_id : by_symbol_type.keySet () )
                {
                    if ( ! symbol_id.visibility ().isPrintable () )
                    {
                        continue;
                    }

                    final Symbol symbol = by_symbol_type.get ( symbol_id );

                    sbuf.append ( "    " + symbol_id
                                  + " --> "
                                  + ClassName.of ( symbol.getClass () )
                                  + "#"
                                  + symbol.hashCode ()
                                  + " ("
                                  + symbol.id ()
                                  + ")\n" );
                }
            }
        }

        return sbuf.toString ();
    }


    /**
     * <p>
     * Removes the specified Symbol from this SymbolTable.
     * </p>
     *
     * @param symbol_id The unique identifier which will be removed
     *                  such as the Symbol's actual id, or
     *                  <code> NamespaceID.PARENT </code>, or
     *                  <code> TypeIDs.ROOT_TYPE </code>, and so on.
     *                  Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    public <SYMBOL extends Symbol>
                           SymbolTable remove (
                                               SymbolID<SYMBOL> symbol_id
                                               )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeInTable.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_id );

        if ( ! symbol_id.visibility ().isRemovable () )
        {
            // Sorry, bub, no can do.
            final SymbolMustBeInTable.Violation violation =
                new SymbolMustBeInTable ( this )
                    .violation ( this, symbol_id );
            throw violation;
        }

        final Type<? extends Symbol> symbol_type = symbol_id.type ();
        synchronized ( this.lock )
        {
            LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                this.symbols.get ( symbol_type );
            if ( by_symbol_type == null )
            {
                final SymbolMustBeInTable.Violation violation =
                    new SymbolMustBeInTable ( this )
                        .violation ( this, symbol_id );
                throw violation;
            }

            int position = -1;
            int p = 0;
            for ( SymbolID<?> maybe_symbol_id : by_symbol_type.keySet () )
            {
                if ( maybe_symbol_id.equals ( symbol_id ) )
                {
                    position = p;
                    break;
                }

                p ++;
            }

            if ( position < 0 )
            {
                final SymbolMustBeInTable.Violation violation =
                    new SymbolMustBeInTable ( this )
                        .violation ( this, symbol_id );
                throw violation;
            }

            final Symbol symbol = by_symbol_type.get ( symbol_id );

            this.hashCode -=
                ( position + 1 ) * symbol.hashCode ();

            by_symbol_type.remove ( symbol_id );

            if ( by_symbol_type.size () == 0 )
            {
                this.symbols.remove ( symbol_type );
            }
        }

        return this;
    }


    /**
     * <p>
     * Sets the specified Symbol in this SymbolTable, possibly
     * adding it, or possibly overwriting the existing Symbol under
     * the specified ID.
     * </p>
     *
     * <p>
     * The SymbolID need not match the Symbol's actual ID.  This is used,
     * for example, to store the parent Namespace of a Namespace under
     * "#parent", or the root un-Tagged Type of a Type under "#root",
     * and so on.
     * </p>
     *
     * @param symbol_id The unique identifier under which the Symbol
     *                  will be stored in this SymbolTable, such as the
     *                  Symbol's actual id, or
     *                  <code> NamespaceID.PARENT </code>, or
     *                  <code> TypeIDs.ROOT_TYPE </code>, and so on.
     *                  Must not be null.
     *
     * @param symbol The Symbol to add or overwrite in this SymbolTable.
     *               Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    public <SYMBOL extends Symbol>
                           SymbolTable set (
                                            SymbolID<SYMBOL> symbol_id,
                                            SYMBOL symbol
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_id, symbol );

        if ( symbol_id.visibility () == Visibility.INVISIBLE )
        {
            // Sorry, bub, no can do.
            return this;
        }

        final Type<? extends Symbol> symbol_type = symbol_id.type ();
        synchronized ( this.lock )
        {
            LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                this.symbols.get ( symbol_type );
            if ( by_symbol_type == null )
            {
                by_symbol_type = new LinkedHashMap<SymbolID<?>, Symbol> ();
                this.symbols.put ( symbol_type, by_symbol_type );
            }

            this.hashCode +=
                ( by_symbol_type.size () + 1 ) * symbol.hashCode ();
            by_symbol_type.put ( symbol_id, symbol );
        }

        return this;
    }


    /**
     * <p>
     * Sets the specified Symbol in this SymbolTable, possibly
     * adding it, or possibly overwriting the existing Symbol under
     * the specified ID.
     * </p>
     *
     * <p>
     * The SymbolID need not match the Symbol's actual ID.  This is used,
     * for example, to store the parent Namespace of a Namespace under
     * "#parent", or the root un-Tagged Type of a Type under "#root",
     * and so on.
     * </p>
     *
     * <p>
     * The class of the symbol parameter MUST extend the generic parameter
     * class of the id parameter.  For example, SymbolID&lt;Constraint&gt;
     * and MyConstraint (extends Constraint) work fine together, but
     * OperationID and MyConstraint do not, and will lead
     * to a violation of the ReturnNeverNull contract.
     * </p>
     *
     * <p>
     * This method is used as a convenience when copying the contents
     * from one SymbolTable to another, without knowing anything about
     * the Symbol Types involved.  Use the <code> set ( ... ) </code>
     * method, insted, where possible.
     * </p>
     *
     * @see musaico.foundation.typing.SymbolTable#set(musaico.foundation.typing.SymbolID, musaico.foundation.typing.Symbol)
     *
     * @param symbol_id The unique identifier under which the Symbol
     *                  will be stored in this SymbolTable, such as the
     *                  Symbol's actual id, or
     *                  <code> NamespaceID.PARENT </code>, or
     *                  <code> TypeIDs.ROOT_TYPE </code>, and so on.
     *                  Must not be null.
     *
     * @param symbol The Symbol to add or overwrite in this SymbolTable.
     *               Must not be null.
     *
     * @return This SymbolTable.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Symbol - SYMBOL.
    public <SYMBOL extends Symbol>
                           SymbolTable setWithCast (
                                                    SymbolID<SYMBOL> symbol_id,
                                                    Symbol symbol
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        try
        {
            final SYMBOL cast_symbol = (SYMBOL) symbol;
            return this.set ( symbol_id, cast_symbol );
        }
        catch ( ClassCastException e )
        {
            // Caller lied to us.  The symbol specified is not a SYMBOL,
            // but it must be in order to be stored with a
            // SymbolID<SYMBOL>.
            final ReturnNeverNull.Violation violation =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     null ); // inspectable.
            violation.initCause ( e );
            throw violation;
        }
    }


    /**
     * <p>
     * Looks up the Symbol in this SymbolTable by the specified identifier.
     * </p>
     *
     * @param id The SymbolID to look up.  Must not be null.
     *
     * @return The One Symbol identified by the specified identifier,
     *         or No Symbol if it does not exist in this SymbolTable.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Try...cast...catch,
    // Cast ValueBuilder.build() Value<SYMBOL> to One<SYMBOL>.
    public <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        final Type<SYMBOL> symbol_type = id.type ();

        if ( ! id.visibility ().isQueryableByID () )
        {
            // Sorry, bub, no can do.
            final SymbolMustBeInTable.Violation violation =
                this.symbolMustBeInTable.violation ( this,
                                                     id );
            return symbol_type.noValue ( violation );
        }

        SYMBOL symbol = null;
        try
        {
            synchronized ( this.lock )
            {
                final LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                    this.symbols.get ( symbol_type );
                if ( by_symbol_type != null )
                {
                    symbol = (SYMBOL) by_symbol_type.get ( id );
                }
            }
        }
        catch ( Exception e ) // In case of unlikely ClassCastException.
        {
            symbol = null;
        }

        final ZeroOrOne<SYMBOL> symbol_value;
        if ( symbol == null )
        {
            final SymbolMustBeInTable.Violation violation =
                this.symbolMustBeInTable.violation ( this,
                                                     id );
            symbol_value = symbol_type.noValue ( violation );
        }
        else
        {
            symbol_value = (One<SYMBOL>) symbol_type.builder ()
                .add ( symbol )
                .build ();
        }

        return symbol_value;
    }


    /**
     * <p>
     * Returns all SymbolIDs of a specific Symbol Type from
     * this SymbolTable.  For example, to retrieve all Tag IDs, call
     * <code> symbolIDs ( Tag.TYPE ) </code>.
     * </p>
     *
     * <p>
     * If no SymbolIDs of the requested Symbol Type are in
     * this SymbolTable then No SymbolID will be returned.
     * </p>
     *
     * @param symbol_type The Type of Symbols to return, such as
     *                    Type.KIND, Tag.TYPE,
     *                    an OperationType, Constraint.TYPE,
     *                    and so on.  Must not be null.
     *
     * @return All SymbolIDs of the requested Symbol Type from this
     *         SymbolTable.  Can be empty.  Never null.  Never contains
     *         any null elements.
     */
    @SuppressWarnings("unchecked") // SymbolID ensures Symbol is a SYMBOL.
    public <SYMBOL extends Symbol>
        Value<SymbolID<SYMBOL>> symbolIDs (
                                           Type<SYMBOL> symbol_type
                                           )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        final ValueBuilder<SymbolID<SYMBOL>> symbol_ids_builder =
            new ValueBuilder<SymbolID<SYMBOL>> ( new SillySymbolID<SymbolID<SYMBOL>> ().getSymbolIDClass () );
        synchronized ( this.lock )
        {
            final LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                this.symbols.get ( symbol_type );
            if ( by_symbol_type != null )
            {
                for ( SymbolID<?> symbol_id : by_symbol_type.keySet () )
                {
                    if ( symbol_id.visibility ().isQueryableIDsByType () )
                    {
                        symbol_ids_builder.add ( (SymbolID<SYMBOL>) symbol_id );
                    }
                }
            }
        }

        final Value<SymbolID<SYMBOL>> requested_symbol_ids =
            symbol_ids_builder.build ();
        return requested_symbol_ids;
    }


    // Ugly hack for @*%# generics.
    private static class SillySymbolID<SYMBOL_ID_CLASS> // SymbolID<?>
    {
        @SuppressWarnings("unchecked")
        public Class<SYMBOL_ID_CLASS> getSymbolIDClass ()
        {
            return (Class<SYMBOL_ID_CLASS>) SymbolID.class;
        }
    }


    /**
     * <p>
     * Returns all Symbols of a specific Symbol Type from
     * this SymbolTable.  For example, to retrieve all Tags, call
     * <code> symbols ( Tag.TYPE ) </code>.
     * </p>
     *
     * <p>
     * No "private" Symbols are ever returned (those indexed by
     * an identifier beginning with "#").
     * </p>
     *
     * <p>
     * If no Symbols of the requested Symbol Type are in
     * this SymbolTable then No Symbol will be returned.
     * </p>
     *
     * @param symbol_type The Type of Symbols to return, such as
     *                    Type.KIND, Tag.TYPE,
     *                    an OperationType, Constraint.TYPE,
     *                    and so on.  Must not be null.
     *
     * @return All Symbols of the requested Symbol Type from this
     *         SymbolTable.  Can be empty.  Never null.  Never contains
     *         any null elements.
     */
    @SuppressWarnings("unchecked") // SymbolID ensures Symbol is a SYMBOL.
    public <SYMBOL extends Symbol>
        Value<SYMBOL> symbols (
                               Type<SYMBOL> symbol_type
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        final ValueBuilder<SYMBOL> symbols_builder =
            new ValueBuilder<SYMBOL> ( symbol_type.valueClass () );
        synchronized ( this.lock )
        {
            final LinkedHashMap<SymbolID<?>, Symbol> by_symbol_type =
                this.symbols.get ( symbol_type );
            if ( by_symbol_type != null )
            {
                for ( SymbolID<?> symbol_id : by_symbol_type.keySet () )
                {
                    if ( symbol_id.visibility ().isQueryableSymbolsByType () )
                    {
                        final Symbol symbol = by_symbol_type.get ( symbol_id );
                        symbols_builder.add ( (SYMBOL) symbol );
                    }
                }
            }
        }

        final Value<SYMBOL> requested_symbols =
            symbols_builder.build ();
        return requested_symbols;
    }


    /**
     * <p>
     * Returns all Types of the Symbols stored in this SymbolTable.
     * </p>
     *
     * @return All standard value class Types of Symbols from this
     *         SymbolTable.  Can be empty.  Never null.
     *         Never contains any null elements.
     */
    @SuppressWarnings("unchecked") // cast Type<? extends Symbol> - SymbolType.
    public Value<SymbolType> symbolTypes ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Set<SymbolType> symbol_types =
            new LinkedHashSet<SymbolType> ();
        synchronized ( this.lock )
        {
            for ( Type<? extends Symbol> symbol_type : this.symbols.keySet () )
            {
                symbol_types.add ( (SymbolType) symbol_type );
            }
        }

        final Value<SymbolType> v_symbol_types =
            new ValueBuilder<SymbolType> ( SymbolType.class,
                                           symbol_types )
            .build ();

        return v_symbol_types;
    }
}
