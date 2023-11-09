package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.No;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * The Type representing all Constraints.
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
public class ConstraintType
    extends AbstractSymbolType<Constraint>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( ConstraintType.class );


    // The unique identifier for this NoType.
    private final TypeID id;


    /**
     * <p>
     * Creates a new ConstraintType with the specified type name and
     * SymbolTable.
     * </p>
     *
     * <p>
     * Package-private.  For Constraint.TYPE only.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "primitives" or "int" or "mytag".
     *                      Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this ConstraintType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    ConstraintType (
                    String raw_type_name,
                    SymbolTable symbol_table,
                    Metadata metadata
                    )
    {
        super ( raw_type_name, symbol_table, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name );

        this.id = new TypeID ( raw_type_name,       // raw type name
                               "",                  // tag_names
                               Visibility.PUBLIC ); // visibility

        this.symbolTable ().set ( NamespaceID.PARENT, Namespace.NONE );
    }


    /**
     * @see musaico.foundation.typing.Symbol#id()
     */
    @Override
    public final TypeID id ()
        throws ReturnNeverNull.Violation
    {
        return this.id;
    }


    /**
     * @see musaico.foundation.typing.Type#none()
     */
    @Override
    public final Constraint none ()
        throws ReturnNeverNull.Violation
    {
        return Constraint.NONE;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final ConstraintType rename (
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
    public ConstraintType rename (
                                  String name,
                                  SymbolTable symbol_table
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, symbol_table );

        symbol_table.addAll ( this.symbolTable () );

        return new ConstraintType ( name,
                                    symbol_table,
                                    this.metadata ().renew () );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.id ().name ();
    }


    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    public final Class<Constraint> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return Constraint.class;
    }
}
