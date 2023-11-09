package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;


/**
 * <p>
 * The Type representing all Metadata.
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
public class MetadataType
    extends StandardType<Metadata>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The default type of all Metadata. */
    public static final MetadataType TYPE =
        Namespace.ROOT.registerSymbol (
            new MetadataType ( "metadata",
                               new SymbolTable () ) );


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MetadataType.class );


    /**
     * <p>
     * Creates a new MetadataType with the specified type name and
     * SymbolTable.
     * </p>
     *
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "metadata".
     *                      Note that if any Tags are currently in the
     *                      SymbolTable then the tag names will be appended
     *                      to the name.  For example, "metadata[tag1, tag2]"
     *                      and so on.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     */
    public MetadataType (
                         String raw_type_name,
                         SymbolTable symbol_table
                         )
    {
        super ( Kind.ROOT,
                raw_type_name,
                Metadata.class,
                Metadata.NONE,
                symbol_table );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final MetadataType rename (
                                      String name
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public MetadataType rename (
                                String name,
                                SymbolTable symbol_table
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, symbol_table );

        symbol_table.addAll ( this.symbolTable () );

        return new MetadataType ( name,
                                  symbol_table );
    }
}
