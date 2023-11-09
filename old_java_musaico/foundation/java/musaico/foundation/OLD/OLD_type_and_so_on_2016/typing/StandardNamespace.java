package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Value;


/**
 * <p>
 * A generic Namespace containing Types and Tags and so on.
 * </p>
 *
 * <p>
 * Unlike specific Namespaces (Types, Tags and so on), a generic
 * StandardNamespace may continue to add children -- specifically
 * child generic Namespaces -- long after creation.  However its
 * specific, behavioural children (Types, Tags and so on) must remain fixed
 * once callers begin using it.
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
public class StandardNamespace
    extends AbstractNamespace<NamespaceID, Namespace>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new StandardNamespace with the specified name, to be used
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
     *                     after constructing this StandardNamespace,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this
     *                     StandardNamespace.  Must not be null.
     */
    public StandardNamespace (
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
     * Creates a new StandardNamespace with the specified name, to be used
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
     *                     after constructing this StandardNamespace,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this
     *                     StandardNamespace.  Must not be null.
     *
     * @param metadata The Metadata for this StandardNamespace.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardNamespace (
                              Namespace parent_namespace,
                              String name,
                              SymbolTable symbol_table,
                              Metadata metadata
                              )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace, new NamespaceID ( name ), symbol_table,
               metadata );
    }


    /**
     * <p>
     * Creates a new StandardNamespace with the specified name, to be used
     * for a Namespace identifier which is unique in every SymbolTable.
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
     *                     after constructing this StandardNamespace,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this
     *                     StandardNamespace.  Must not be null.
     *
     * @param metadata The Metadata for this StandardNamespace.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardNamespace (
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
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public StandardNamespace rename (
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
    public StandardNamespace rename (
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

        return new StandardNamespace ( parent_namespace,
                                       name,
                                       symbol_table,
                                       this.metadata ().renew () );
    }
}
