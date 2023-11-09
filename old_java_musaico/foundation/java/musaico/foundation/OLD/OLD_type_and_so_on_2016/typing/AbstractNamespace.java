package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;

import musaico.foundation.value.NoneGuesser;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * The basis for most generic Namespaces as well as most specific Namespaces,
 * such as Tags and Types.
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
public abstract class AbstractNamespace<ID extends SymbolID<NAMESPACE>, NAMESPACE extends Namespace>
    extends AbstractSymbol<ID, NAMESPACE>
    implements Namespace, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks parameters to constructors and static methods for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractNamespace.class );


    // The SymbolTable containing any child Types, Constraints,
    // Operations, and so on for this Namespace, as well
    // as the parent Namespace and so on ancestors.
    private final SymbolTable symbolTable;

    // The metadata for this AbstractNamespace, such as when and where
    // it was created, statistics, and so on.
    private final Metadata metadata;


    /**
     * <p>
     * Creates a new AbstractNamespace with the specified ID,
     * a unique identifier in every SymbolTable.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Namespace will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param id The id of this Namespace.  Every Namespace
     *           must have a unique ID within each SymbolTable.
     *           Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Namespace.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     AbstractNamespace, but is expected to
     *                     cease additions to the SymbolTable
     *                     before anyone begins using this Namespace.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this AbstractNamespace.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public AbstractNamespace (
                              Namespace parent_namespace,
                              ID id,
                              SymbolTable symbol_table,
                              Metadata metadata
                              )
        throws ParametersMustNotBeNull.Violation
    {
        super ( id, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent_namespace, symbol_table,
                               metadata );

        this.symbolTable = symbol_table;
        this.symbolTable.set ( NamespaceID.PARENT, parent_namespace );
        this.metadata = metadata;
    }


    /**
     * @see musaico.foundation.typing.Namespace#containsSymbol()
     */
    @Override
    public final boolean containsSymbol (
                                         SymbolID<?> id
                                         )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  id );

        return this.symbolTable.containsSymbol ( id );
    }


    /**
     * <p>
     * An optional refinement on the standard
     * AbstractNamespace#equalsSymbol(musaico.foundation.typing.Symbol).
     * </p>
     *
     * <p>
     * Once <code> equals () </code> and <code> equalsSymbol () </code>
     * have both determined that two AbstractNamespaces
     * have the same class and SymbolID, this method is invoked to check
     * contents further.
     * </p>
     *
     * <p>
     * By default this method does nothing.  It can be overridden to
     * specify extra equals checks.  For example, a Type might compare
     * Tags, or an Operation might compare input and output Types,
     * and so on.
     * </p>
     *
     * @param namespace The AbstractNamespace of exactly the same class as this
     *                  AbstractNamespace which will be compared.
     *                  Must not be null.
     *
     * @return True if this equals that, false if not.
     */
    protected boolean equalsNamespace (
                                       NAMESPACE that
                                       )
    {
        return true;
    }


    /**
     * @see musaico.foundation.typing.AbstractSymbol#equalsSymbol(musaico.foundation.typing.Symbol)
     */
    @Override
    @SuppressWarnings("unchecked") // we assume this class implements NAMESPACE.
    protected final boolean equalsSymbol (
                                          NAMESPACE other_namespace
                                          )
    {
        if ( other_namespace.getClass () != this.getClass () )
        {
            return false;
        }

        AbstractNamespace<?, ?> that =
            (AbstractNamespace<?, ?>) other_namespace;

        if ( ! this.symbolTable.equals ( that.symbolTable ) )
        {
            // AbstractNamespace w/ symbols A,B,C
            //     != AbstractNamespace w/ symbolx X,Y,Z.
            return false;
        }

        // Everything is all matchy-matchy.  Check the specific Namespace
        // equality method.
        return this.equalsNamespace ( (NAMESPACE) that );
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
        return this.symbolTable.printSymbolTable ();
    }


    /**
     * @see musaico.foundation.typing.Namespace#symbol(musaico.foundation.typing.SymbolID, musaico.foundation.typing.Symbol)
     */
    @Override
    public final <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  symbol_type );

        return this.symbolTable.symbols ( symbol_type );
    }


    /**
     * @return This Namespace's SymbolTable, for use by the implementing class.
     *         Never null.
     *
     * Final for speed.
     */
    protected final SymbolTable symbolTable ()
        throws ReturnNeverNull.Violation
    {
        return this.symbolTable;
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
     * @see musaico.foundation.typing.Namespace#type(java.lang.Class)
     */
    @Override
    public <VALUE extends Object>
        Type<VALUE> typeOf (
                            Class<VALUE> value_class
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  value_class );

        // Is there a parent Namespace?  If so, forward the request to it.
        final Namespace parent_namespace =
            this.symbol ( NamespaceID.PARENT ).orNone ();
        if ( parent_namespace != this )
        {
            return parent_namespace.typeOf ( value_class );
        }

        // No parent Namespace.  Give up and return a NoType.
        final String no_such_type_name =
            "type(" + value_class.getName () + ")";
        final TypeID type_id =
            new TypeID ( no_such_type_name, // raw type name
                         "",                // tag_names
                         Visibility.NONE ); // visibility
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
