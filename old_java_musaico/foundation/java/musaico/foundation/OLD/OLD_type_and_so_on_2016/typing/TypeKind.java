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
 * The Type representing all Kinds.
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
public class TypeKind
    extends AbstractSymbolType<Type<?>>
    implements Kind, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeKind.class );


    // The unique identifier for this NoType.
    private final TypeID id;


    /**
     * <p>
     * Creates a new TypeKind with the specified type name and
     * SymbolTable.
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
     * @param metadata The Metadata for this TypeKind.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public TypeKind (
                     String raw_type_name,
                     SymbolTable symbol_table,
                     Metadata metadata
                     )
    {
        super ( raw_type_name, symbol_table, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name );

        this.id =
            new TypeID ( this,
                         raw_type_name,       // raw_type_name
                         "",                  // tag_names
                         Visibility.PUBLIC ); // visibility
    }


    /**
     * <p>
     * Creates the root kind, Kind.ROOT.
     * </p>
     *
     * <p>
     * Package-private.  For Kind.ROOT only.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "primitives" or "int" or "mytag".
     *                      Must not be null.
     *
     * Package-private.  For use by Kind.ROOT only.
     */
    TypeKind (
              String raw_type_name
              )
    {
        super ( raw_type_name,
                new SymbolTable (),
                new StandardMetadata () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name );

        this.id =
            new TypeID ( this,
                         raw_type_name,       // raw type name
                         "",                  // tag names
                         Visibility.PUBLIC ); // visibility
    }


    /**
     * For Kind.TYPE only.
     */
    TypeKind finishConstructing ()
    {
        this.symbolTable ().set ( NamespaceID.PARENT, Namespace.NONE );

        return this;
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
    public final Type<?> none ()
        throws ReturnNeverNull.Violation
    {
        return Type.NONE;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final TypeKind rename (
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
    public TypeKind rename (
                            String name,
                            SymbolTable symbol_table
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, symbol_table );

        symbol_table.addAll ( this.symbolTable () );

        return new TypeKind ( name,
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
     * @see musaico.foundation.typing.Kind#typeBuilder()
     */
    @Override
    public <NEW_TYPE_VALUE extends Object>
        TypeBuilder<NEW_TYPE_VALUE> typeBuilder (
                                                 Class<NEW_TYPE_VALUE> value_class,
                                                 SymbolTable symbol_table
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  value_class, symbol_table );

        return new TypeBuilder<NEW_TYPE_VALUE> ( this,
                                                 value_class,
                                                 symbol_table );
    }


    // Ugly hack for @*%# generics.
    private static class Silly<TYPE_CLASS>
    {
        @SuppressWarnings("unchecked")
        public Class<TYPE_CLASS> getTypeClass ()
        {
            return (Class<TYPE_CLASS>) Type.class;
        }
    }

    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    public final Class<Type<?>> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return new Silly<Type<?>> ().getTypeClass ();
    }
}
