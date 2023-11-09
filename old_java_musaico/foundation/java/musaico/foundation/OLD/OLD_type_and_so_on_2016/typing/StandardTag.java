package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Value;


/**
 * <p>
 * The basis for most tags, with no interesting behaviour beyond whatever
 * is stored in the SymbolTable of the Tag.
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
public class StandardTag
    extends AbstractTag 
    implements Tag, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardTag.class );


    /**
     * <p>
     * Creates a new StandardTag with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardTag,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     */
    public StandardTag (
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
     * Creates a new StandardTag with the specified unique identifier
     * for the tag's typing environment.
     * </p>
     *
     * <p>
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardTag,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this StandardTag.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardTag (
                        Namespace parent_namespace,
                        String name,
                        SymbolTable symbol_table,
                        Metadata metadata
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace, new TagID ( name ), symbol_table,
               metadata );
    }


    /**
     * <p>
     * Creates a new StandardTag with the specified unique identifier
     * for the tag's typing environment.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param id The unique identifier of this Tag.  Every Tag
     *           must have a unique ID within each SymbolTable.
     *           Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardTag,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this StandardTag.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public StandardTag (
                        Namespace parent_namespace,
                        TagID id,
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
    public StandardTag rename (
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
    public StandardTag rename (
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

        symbol_table.addAll ( this.symbolTable () );

        return new StandardTag ( parent_namespace,
                                 name,
                                 symbol_table,
                                 this.metadata ().renew () );
    }
}
