package musaico.foundation.typing;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.NoneGuesser;
import musaico.foundation.value.Value;


/**
 * <p>
 * A parent Namespace for a set of Types and child Namespaces,
 * as well as Terms.
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
public class StandardRootNamespace
    extends StandardNamespace
    implements RootNamespace, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Lock critical sections on this:
    private final Serializable lock = new String ();

    // Lookup of Types by Classes.  For example String -> Type "string".
    private final Map<Class<?>, Type<?>> typesByClasses =
        new HashMap<Class<?>, Type<?>> ();


    /**
     * <p>
     * Creates a new StandardRootNamespace with the specified name, to be used
     * for a Namespace identifier which is unique in every SymbolTable.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Namespace will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Namespace.
     *             Every Namespace must have an ID which is unique
     *             within every SymbolTable in which it resides.
     *             Must not be null.
     *
     * @param symbol_table The children of this generic Namespace, such
     *                     as Types and Tags and so on.  The caller
     *                     may continue to add to the SymbolTable
     *                     after constructing this StandardRootNamespace,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this
     *                     StandardRootNamespace.  Must not be null.
     */
    public StandardRootNamespace (
                                  Namespace parent_namespace,
                                  String name,
                                  SymbolTable symbol_table
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace, name, symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new StandardRootNamespace with the specified SymbolID,
     * which is unique in every SymbolTable.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Namespace will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Namespace.
     *             Every Namespace must have an ID which is unique
     *             within every SymbolTable in which it resides.
     *             Must not be null.
     *
     * @param symbol_table The children of this generic Namespace, such
     *                     as Types and Tags and so on.  The caller
     *                     may continue to add to the SymbolTable
     *                     after constructing this StandardRootNamespace,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this
     *                     StandardRootNamespace.  Must not be null.
     *
     * @param metadata The Metadata for this StandardRootNamespace.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardRootNamespace (
                                  Namespace parent_namespace,
                                  String name,
                                  SymbolTable symbol_table,
                                  Metadata metadata
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace, 
               new NamespaceID ( name ), symbol_table,
               metadata );
    }


    /**
     * <p>
     * Creates a new StandardRootNamespace with the specified SymbolID,
     * which is unique in every SymbolTable.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Namespace will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param id The unique identifier of this Namespace.
     *           Every Namespace must have an ID which is unique
     *           within every SymbolTable in which it resides.
     *           Must not be null.
     *
     * @param symbol_table The children of this generic Namespace, such
     *                     as Types and Tags and so on.  The caller
     *                     may continue to add to the SymbolTable
     *                     after constructing this StandardRootNamespace,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this
     *                     StandardRootNamespace.  Must not be null.
     *
     * @param metadata The Metadata for this StandardRootNamespace.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardRootNamespace (
                                  Namespace parent_namespace,
                                  NamespaceID id,
                                  SymbolTable symbol_table,
                                  Metadata metadata
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent_namespace, id, symbol_table, metadata );
    }


    /**
     * @see musaico.foundation.typing.RootNamespace#add(musaico.foundation.typing.Namespace)
     */
    public StandardRootNamespace add (
                                      Namespace namespace
                                      )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeUnique.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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

        synchronized ( this.lock )
        {
            if ( type != null )
            {
                if ( this.typesByClasses.containsKey ( value_class ) )
                {
                    final Symbol existing_type =
                        this.typesByClasses.get ( value_class );
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

            this.symbolTable ().add ( namespace );

            if ( type != null )
            {
                this.typesByClasses.put ( value_class, type );
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
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
     */
    @Override
    public StandardRootNamespace rename (
                                         String name,
                                         SymbolTable symbol_table
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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

        symbol_table.addAll ( this.symbolTable () );

        return new StandardRootNamespace ( parent_namespace,
                                           name,
                                           symbol_table,
                                           this.metadata ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#type(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast to Type<VALUE>.
    public final <VALUE extends Object>
        Type<VALUE> typeOf (
                            Class<VALUE> value_class
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
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

        // No type has been registered in this StandardRootNamespace.
        // Fall back on the default Namespace behaviour (look up the
        // tree until the root, then return a NoType).
        return super.typeOf ( value_class );
    }
}
