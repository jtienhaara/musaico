package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A Namespace which does absolutely nothing, requires nothing,
 * guarantees nothing, and has no Symbols.
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
public class NoNamespace
    implements RootNamespace, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks parameters to constructors and static methods for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoNamespace.class );


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // The parent Namespace of this NoNamespace, such as Namespace.ROOT.
    private final Namespace parentNamespace;

    // The unique identifier of this NoNamespace, or null to
    // use NamespaceID.NONE.
    private final NamespaceID id;

    // The metadata for this NoNamespace.
    private final Metadata metadata;


    /**
     * <p>
     * Creates a new NoNamespace with the specified name.
     * </p>
     *
     * @param parent_namespace The parent Namespace in which this
     *                         "none" Namespace will eventually be
     *                         registered.  Must not be null.
     *
     * @param name The name which uniquely identifies
     *             this Namespace within a SymbolTable.  Must not be null.
     *
     * @param metadata The Metadata for this NoNamespace.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public NoNamespace (
                        Namespace parent_namespace,
                        String name,
                        Metadata metadata
                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent_namespace, name,
                               metadata );

        this.id = new NamespaceID ( name );
        this.parentNamespace = parent_namespace;

        this.metadata = metadata;

        this.contracts = new TrackingContracts ( this, this.metadata );
    }


    /**
     * <p>
     * Creates the default NoNamespace with the default NamespaceID.NONE.
     * </p>
     *
     * <p>
     * Package-private.  Should only ever be used to construct
     * Namespace.NONE.
     * </p>
     */
    NoNamespace ()
        throws ParametersMustNotBeNull.Violation
    {
        this.id = null; // Will be resolved to NamespaceID.NONE by id ().
        this.parentNamespace = Namespace.ROOT;

        this.metadata = new StandardMetadata ();

        this.contracts = new TrackingContracts ( this, this.metadata );
    }


    /**
     * see musaico.foundation.typing.RootNamespace#add(musaico.foundation.typing.Namespace)
     */
    @Override
    public NoNamespace add (
                            Namespace namespace
                            )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeUnique.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               namespace );

        // Do nothing.  A NoNamespace can never add any children.

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

        if ( NamespaceID.PARENT.equals ( id ) )
        {
            return true;
        }

        return false;
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
            // Any NoNamespace != null.
            return false;
        }
        else if ( object == this )
        {
            // Any NoNamespace == itself.
            return true;
        }
        else if ( ! this.getClass ().equals ( object.getClass () ) )
        {
            // Any NoNamespace of class X != any NoNamespace of class Y.
            return false;
        }

        final NoNamespace that = (NoNamespace) object;
        if ( ! this.id ().equals ( that.id () ) )
        {
            // NoNamespace "a" != NoNamespace "b".
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
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
        if ( metadata.has ( metadatum_class ) )
        {
            return metadata.get ( metadatum_class );
        }

        final ZeroOrOne<Namespace> v_parent_namespace =
            this.symbol ( NamespaceID.PARENT );
        final Namespace parent_namespace = v_parent_namespace.orNone ();
        if ( ! v_parent_namespace.hasValue ()
             || parent_namespace == this )
        {
            // Let our Metadata generate a No<METADATUM>.
            return metadata.get ( metadatum_class );
        }

        // Search up the hierarchy of Namespaces.
        return parent_namespace.findMetadatum ( metadatum_class );
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
        if ( this.id == null )
        {
            return NamespaceID.NONE;
        }
        else
        {
            return this.id;
        }
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
     * @see musaico.foundation.typing.Namespace#printSymbolTable()
     */
    @Override
    public final String printSymbolTable ()
        throws ReturnNeverNull.Violation
    {
        return "No SymbolTable";
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public NoNamespace rename (
                               String name
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public NoNamespace rename (
                               String name,
                               SymbolTable symbol_table
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
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

        // NoNamespace has no SymbolTable at all to copy from.
        // Ignore the symbol_table.

        return new NoNamespace ( parent_namespace,
                                 name,
                                 this.metadata.renew () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbol(musaico.foundation.typing.SymbolID)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Value<Namespace> to One<SYMBOL>.
    public final <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id );

        if ( NamespaceID.PARENT.equals ( id ) )
        {
            return (One<SYMBOL>)
                new ValueBuilder<Namespace> ( Namespace.class,
                                              Namespace.NONE )
                    .add ( this.parentNamespace )
                    .build ();
        }
        else
        {
            final Unregistered unregistered =
                new Unregistered ( id );
            final TypingViolation violation =
                SymbolMustBeRegistered.CONTRACT.violation ( this,
                                                            unregistered );
            return id.type ().noValue ( violation );
        }
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbolIDs(musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Val<Namespace> to Val<SYMBOL>.
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

        if ( Namespace.TYPE.equals ( symbol_type ) )
        {
            return (Value<SymbolID<SYMBOL>>) NamespaceID.PARENT;
        }
        else
        {
            return
                new ValueBuilder<SymbolID<SYMBOL>> ( (Class<SymbolID<SYMBOL>>) ( (SymbolID<SYMBOL>) symbol_type.id () ).getClass (),
                                                     (SymbolID<SYMBOL>) symbol_type.none ().id () )
                .build ();
        }
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbols(musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Namespace to SYMBOL.
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

        if ( Namespace.TYPE.equals ( symbol_type ) )
        {
            return
                new ValueBuilder<SYMBOL> ( symbol_type.valueClass (),
                                           symbol_type.none () )
                .add ( (SYMBOL) this.parentNamespace )
                .build ();
        }
        else
        {
            return
                new ValueBuilder<SYMBOL> ( symbol_type.valueClass (),
                                           symbol_type.none () )
                .build ();
        }
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbolTypes()
     */
    @Override
    public final Value<SymbolType> symbolTypes ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new ValueBuilder<SymbolType> ( SymbolType.class,
                                              SymbolType.NONE )
            .add ( Namespace.TYPE )
            .build ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        if ( this.id == null )
        {
            return "NoNamespace";
        }
        else
        {
            return "NoNamespace " + this.id.name ();
        }
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

        return this.parentNamespace.typeOf ( value_class );
    }
}
