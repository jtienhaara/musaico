package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Value;


/**
 * <p>
 * One input Type, and one output Type.  Used by Operations
 * to describe their input parameters and return (output) type.
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
public class OperationType2<INPUT1 extends Object, INPUT2 extends Object, OUTPUT extends Object>
    extends OperationType<Operation2<INPUT1, INPUT2, OUTPUT>, OUTPUT>
    implements OperationInputs2<INPUT1, INPUT2>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( OperationType2.class );


    /**
     * <p>
     * Creates a new OperationType2 with the specified name,
     * input Type(s) and output Type, and uses defaults for the
     * remaining values.
     * </p>
     *
     * @param input1_type The Type of input parameter # 1.
     *                    Must not be null.
     *
     * @param input2_type The Type of input parameter # 2.
     *                    Must not be null.
     *
     * @param output_type The output Type for the Operations governed
     *                    by this OperationType2.  Must not be null.
     */
    public OperationType2 (
                           Type<INPUT1> input1_type,
                           Type<INPUT2> input2_type,
                           Type<OUTPUT> output_type
                           )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new SymbolTable (),
               new StandardMetadata (),
               input1_type,
               input2_type,
               output_type );
    }


    /**
     * <p>
     * Creates a new OperationType2 with the specified SymbolTable
     * and Metadata, and the default name
     * "(input1type, input2type, ...): outputtype".
     * </p>
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this OperationType2.
     *                 Can be Metadata.NONE.  Must not be null.
     *
     * @param input1_type The Type of input parameter # 1.
     *                    Must not be null.
     *
     * @param input2_type The Type of input parameter # 2.
     *                    Must not be null.
     *
     * @param output_type The output Type for the Operations governed
     *                    by this OperationType2.  Must not be null.
     */
    public OperationType2 (
                           SymbolTable symbol_table,
                           Metadata metadata,
                           Type<INPUT1> input1_type,
                           Type<INPUT2> input2_type,
                           Type<OUTPUT> output_type
                           )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "("
               + input1_type.id ().name ()
               + ", "
               + input2_type.id ().name ()
               + "):"
               + output_type.id ().name (),
               symbol_table,
               metadata,
               input1_type,
               input2_type,
               output_type );
    }


    /**
     * <p>
     * Creates a new OperationType2 with the specified type name and
     * SymbolTable.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this OperationType.
     *                      For example, "(int)" or "(int,string,int)".
     *                      Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this OperationType2.
     *                 Can be Metadata.NONE.  Must not be null.
     *
     * @param input1_type The Type of input parameter # 1.
     *                    Must not be null.
     *
     * @param input2_type The Type of input parameter # 2.
     *                    Must not be null.
     *
     * @param output_type The output Type for the Operations governed
     *                    by this OperationType2.  Must not be null.
     */
    private OperationType2 (
                            String raw_type_name,
                            SymbolTable symbol_table,
                            Metadata metadata,
                            Type<INPUT1> input1_type,
                            Type<INPUT2> input2_type,
                            Type<OUTPUT> output_type
                            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( raw_type_name,
                symbol_table,
                metadata,
                // Check nulls and create List<Type<?>>:
                OperationType.createList (
                                          new Type<?> []
                                          {
                                              input1_type,
                                              input2_type
                                          } ),
                output_type );
    }


    /**
     * @see musaico.foundation.typing.OperationInputs1#input1Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<INPUT1>
    public final Type<INPUT1> input1Type ()
        throws ReturnNeverNull.Violation
    {
        final TypeID input_type_id = new TypeID ( "#input" + 1,
                                                  Visibility.PRIVATE );
        final Type<?> input_type =
            this.symbolTable ().symbol ( input_type_id )
                               .orNone ();
        return (Type<INPUT1>) input_type;
    }


    /**
     * @see musaico.foundation.typing.OperationInputs2#input2Type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<INPUT2>
    public final Type<INPUT2> input2Type ()
        throws ReturnNeverNull.Violation
    {
        final TypeID input_type_id = new TypeID ( "#input" + 2,
                                                  Visibility.PRIVATE );
        final Type<?> input_type =
            this.symbolTable ().symbol ( input_type_id )
                               .orNone ();
        return (Type<INPUT2>) input_type;
    }
}
