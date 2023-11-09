package musaico.foundation.typing;

import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.value.DefaultValues;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.NoneGuesser;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * The topmost Namespace in this runtime environment, the root
 * of all other Namespaces.
 * </p>
 *
 * <p>
 * In any runtime environment no more than one ToplevelNamespace
 * identified as <code> Namespace.ROOT </code> can exist.
 * </p>
 *
 * <p>
 * (Note that this class itself does not implement the restriction,
 * so it is possible to create multiple ToplevelNamespaces, if you
 * see some bizarre point in doing so.  Also because a given runtime
 * environment can have multiple ClassLoaders, you could conceivably
 * load Namespace.ROOT separately in each one.  However these Namespaces
 * would not be able to speak to each other's typing systems.)
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
public class ToplevelNamespace
    implements RootNamespace, DefaultValues, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // Lock critical sections on this:
    private final Serializable lock = new String ();

    // The SymbolTable containing any child Types, Constraints,
    // Operations, and so on for this Namespace, as well
    // as the parent Namespace and so on ancestors.
    private final SymbolTable symbolTable;

    // Lookup of Types by Classes.  For example String -> Type "string".
    private final Map<Class<?>, Type<?>> typesByClasses =
        new HashMap<Class<?>, Type<?>> ();

    // Metadata for the ToplevelNamespace.
    private final Metadata metadata;


    /**
     * <p>
     * Creates the toplevel Namespace.
     * </p>
     */
    ToplevelNamespace ()
    {
        this.symbolTable = new SymbolTable ();

        this.metadata = new StandardMetadata ();

        this.contracts = new TrackingContracts ( this, this.metadata );
    }




    /**
     * <p>
     * Registers a Symbol in <code> Namespace.ROOT </code>.
     * </p>
     *
     * <p>
     * Package-private.
     * </p>
     *
     * @param symbol The Symbol to register in the root namespace.
     *               Must not be null.
     *
     * @return The successfully registered Symbol.  Never null.
     *
     * @throws IllegalStateException If the specified Symbol has already
     *                               been registered.
     */
    final <SYMBOL extends Symbol>
        SYMBOL registerSymbol (
                               SYMBOL symbol
                               )
        throws IllegalStateException
    {
        try
        {
            if ( symbol instanceof Namespace )
            {
                final Namespace namespace = (Namespace) symbol;
                Namespace.ROOT.add (namespace );
            }
            else
            {
                this.symbolTable.add ( symbol );
            }
        }
        catch ( SymbolMustBeUnique.Violation violation )
        {
            throw new IllegalStateException ( "Could not register "
                                              + symbol.id (),
                                              violation );
        }

        return symbol;
    }


    /**
     * <p>
     * Registers a Symbol Type so that
     * <code> Namespace.ROOT.typeOf ( symbol_type.valueClass () )
     *            = symbol_type </code>.
     * </p>
     *
     * <p>
     * Package-private.
     * </p>
     *
     * @param type The Symbol Type to register in the root namespace.
     *             Must not be null.
     *
     * @return The successfully registered root Symbol Type.  Never null.
     *
     * @throws IllegalStateException If the specified Symbol Type has already
     *                               been registered, or if its value
     *                               Class has already been registered.
     */
    final <SYMBOL_TYPE extends Type<? extends Symbol> & SymbolType>
        SYMBOL_TYPE registerSymbolType (
                                        SYMBOL_TYPE type
                                        )
        throws IllegalStateException
    {
        try
        {
            Namespace.ROOT.add ( type );
        }
        catch ( SymbolMustBeUnique.Violation violation )
        {
            throw new IllegalStateException ( "Could not register "
                                              + type.id (),
                                              violation );
        }

        return type;
    }




    /**
     * @see musaico.foundation.typing.RootNamespace#add(musaico.foundation.typing.Namespace)
     */
    public final ToplevelNamespace add (
                                        Namespace namespace
                                        )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeUnique.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               namespace );

        final Type<?> type;
        final Class<?> value_class;
        if ( namespace instanceof Type )
        {
            type = (Type<?>) namespace;
            value_class = type.valueClass ();
        }
        else
        {
            // Not a Type.
            type = null;
            value_class = null;
        }

        boolean is_add_to_types_by_classes = false;
        synchronized ( this.lock )
        {
            if ( type != null )
            {
                is_add_to_types_by_classes = true;
                final Type<?> existing_type =
                    this.typesByClasses.get ( value_class );
                if ( existing_type != null
                     && existing_type.id ().rawTypeName ().equals ( type.id ().rawTypeName () ) )
                {
                    // Leave the parent Type as the default type by class.
                    is_add_to_types_by_classes = false;
                }
                else if ( existing_type != null )
                {
                    final SymbolID<? extends Symbol> existing_type_id =
                        existing_type.id ();
                    final Map<SymbolID<? extends Symbol>, Symbol> existing_symbols =
                        new HashMap<SymbolID<? extends Symbol>, Symbol> ();
                    existing_symbols.put ( existing_type_id,
                                           existing_type );

                    final SymbolMustBeUnique symbol_must_be_unique =
                        new SymbolMustBeUnique ( existing_symbols );
                    throw symbol_must_be_unique.violation ( this,
                                                            existing_type_id );
                }
            }

            this.symbolTable.add ( namespace );

            if ( is_add_to_types_by_classes )
            {
                this.typesByClasses.put ( value_class, type );
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.typing.Symbol#containsSymbol(musaico.foundation.typing.SymbolID)
     */
    @Override
    public final boolean containsSymbol (
                                         SymbolID<?> id
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        return this.symbolTable.containsSymbol ( id );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    @SuppressWarnings("unchecked") // Try...catch is apparently unchecked.
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            // ToplevelNamespace != null.
            return false;
        }
        else if ( object == this )
        {
            // ToplevelNamespace == itself.
            return true;
        }
        else
        {
            // ToplevelNamespace != any other object.
            return false;
        }
    }


    /**
     * @see musaico.foundation.Namespace#findMetadatum(java.lang.Class)
     */
    @Override
    public final <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> findMetadatum (
                                            Class<METADATUM> metadatum_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Metadata metadata = this.metadata ();
        return metadata.get ( metadatum_class );
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Final for speed.
     */
    @Override
    public final int hashCode ()
    {
        return this.id ().hashCode ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#id()
     */
    @Override
    public final SymbolID<Namespace> id ()
        throws ReturnNeverNull.Violation
    {
        return NamespaceID.ROOT;
    }


    /**
     * @see musaico.foundation.typing.Namespace#metadata()
     */
    @Override
    public final Metadata metadata ()
    {
        return this.metadata;
    }


    /**
     * @see musaico.foundation.value.DefaultValues#noneGenerator(musaico.foundation.value.Value)
     */
    @Override
    public final <VALUE extends Object>
            NoneGenerator<VALUE> noneGenerator (
                                                Value<VALUE> value
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        final Class<VALUE> value_class = value.expectedClass ();
        final Type<VALUE> type = this.typeOf ( value_class );

        if ( ! ReturnNeverNull.CONTRACT.filter ( type ).isKept () )
        {
            throw ReturnNeverNull.CONTRACT.violation ( this,
                                                       "typeOf ( "
                                                       + ClassName.of ( value_class )
                                                       + " returned null" );
        }

        // Type implements NoneGenerator.
        return type;
    }


    /**
     * @see musaico.foundation.typing.Namespace#printSymbolTable()
     */
    @Override
    public final String printSymbolTable ()
        throws ReturnNeverNull.Violation
    {
        return this.symbolTable.printSymbolTable ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     *
     * <p>
     * There can be only one ToplevelNamespace per JVM, because
     * of the way it is built in order to compile properly.
     * We are forced to return a StandardRootNamespace instead
     * of a ToplevelNamespace.
     * </p>
     */
    @Override
    public StandardRootNamespace rename (
                                         String name
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     *
     * <p>
     * There can be only one ToplevelNamespace per JVM, because
     * of the way it is built in order to compile properly.
     * We are forced to return a StandardRootNamespace instead
     * of a ToplevelNamespace.
     * </p>
     */
    @Override
    public StandardRootNamespace rename (
                                         String name,
                                         SymbolTable symbol_table
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, symbol_table );

        final Namespace parent_namespace;
        if ( symbol_table.containsSymbol ( NamespaceID.PARENT ) )
        {
            parent_namespace =
                symbol_table.symbol ( NamespaceID.PARENT ).orNone ();
        }
        else
        {
            // Default to same parent Namespace.
            parent_namespace =
                this.symbol ( NamespaceID.PARENT ).orNone ();
        }

        symbol_table.addAll ( this.symbolTable );

        return new StandardRootNamespace ( parent_namespace,
                                           name,
                                           symbol_table,
                                           this.metadata.renew () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbol(musaico.foundation.typing.SymbolID)
     */
    @Override
    public final <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        return this.symbolTable.symbol ( id );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbolIDs(musaico.foundation.typing.Type)
     */
    @Override
    public final <SYMBOL extends Symbol>
        Value<SymbolID<SYMBOL>> symbolIDs (
                                           Type<SYMBOL> symbol_type
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        return this.symbolTable.symbolIDs ( symbol_type );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbols(musaico.foundation.typing.Type)
     */
    @Override
    public final <SYMBOL extends Symbol>
        Value<SYMBOL> symbols (
                               Type<SYMBOL> symbol_type
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type );

        return this.symbolTable.symbols ( symbol_type );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbolTypes()
     */
    @Override
    public final Value<SymbolType> symbolTypes ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.symbolTable.symbolTypes ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Toplevel Namespace";
    }


    /**
     * @see musaico.foundation.typing.Symbol#type()
     */
    @Override
    public final SymbolType type ()
        throws ReturnNeverNull.Violation
    {
        return Namespace.TYPE;
    }


    /**
     * @see musaico.foundation.typing.Namespace#typeOf(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type looked up by class.
    public final <VALUE extends Object>
                          Type<VALUE> typeOf (
                                              Class<VALUE> value_class
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_class );

        final Type<VALUE> registered_type;
        synchronized ( this.lock )
        {
            registered_type = (Type<VALUE>)
                this.typesByClasses.get ( value_class );
        }

        if ( registered_type != null )
        {
            return registered_type;
        }

        // No registered type.  Give up and return a NoType.
        final String no_such_type_name =
            "type(" + value_class.getName () + ")";
        final TypeID type_id =
            new TypeID ( no_such_type_name,   // raw type name
                         "",                  // tag names
                         Visibility.PUBLIC ); // visibility
        final Unregistered unregistered_type =
            new Unregistered ( type_id );
        final TypingViolation violation =
            SymbolMustBeRegistered.CONTRACT.violation ( this,
                                                        unregistered_type );
        final NoType<VALUE> no_type =
            new NoType<VALUE> ( this, // parent_namespace
                                no_such_type_name,
                                value_class,
                                violation,
                                new NoneGuesser<VALUE> ( value_class ),
                                Metadata.NONE );

        return no_type;
    }
}
