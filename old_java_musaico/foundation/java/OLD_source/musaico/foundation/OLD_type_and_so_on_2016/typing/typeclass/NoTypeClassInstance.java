package musaico.foundation.typing.typeclass;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.typing.AbstractTag;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.NamespaceID;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypeID;
import musaico.foundation.typing.Visibility;

import musaico.foundation.value.No;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A "none" TypeClassInstance, which does not actually instantiate
 * a specific TypeClass.
 * </p>
 *
 *
 * <p>
 * In Java every TypeClassInstance must be Serializable in order to play nicely
 * over RMI.  However be warned that any given TypeClassInstance
 * might contain non-Serializable Symbols.
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
public class NoTypeClassInstance
    extends TypeClassInstance
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoTypeClassInstance.class );


    /**
     * <p>
     * Creates a new NoTypeClassInstance for the specified Type,
     * not actually instantiating the specified TypeClass.
     * </p>
     *
     * @param type_class The TypeClass not actually instantiated by this
     *                   NoTypeClassInstance, such as TypeClass.NONE.
     *                   Must not be null.
     *
     * @param instance_type The Type which does not provide the Symbols for
     *                      this NoTypeClassInstance.  Must not be null.
     */
    public NoTypeClassInstance (
                                TypeClass type_class,
                                Type<?> instance_type
                                )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type_class,
                instance_type );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public NoTypeClassInstance rename (
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
    @SuppressWarnings("unchecked") // Cast Tag - TypeClass.
    public NoTypeClassInstance rename (
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

        final TypeClass type_class = (TypeClass)
            symbol_table.symbol ( TYPE_CLASS_ID )
                .orDefault ( TypeClass.NONE );

        final Type<?> instance_type = symbol_table.symbol ( TYPE_ID )
                                                  .orNone ();

        return new NoTypeClassInstance ( type_class,
                                         instance_type );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "No" + super.toString ();
    }
}
