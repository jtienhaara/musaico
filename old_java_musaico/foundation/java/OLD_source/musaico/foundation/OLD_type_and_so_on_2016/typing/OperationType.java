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
 * One or more input Type(s), and one output Type.  Used by Operations
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
@SuppressWarnings("rawtypes") // Op.class != Class<Op<?, ?>>, so use rawtype.
public class OperationType<OPERATION extends Operation<OUTPUT>, OUTPUT extends Object>
    extends AbstractSymbolType<OPERATION>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( OperationType.class );


    // The unique identifier for this OperationType.
    private final TypeID id;

    // The number of input Type(s).  The input Type(s), as well as
    // the output Type, are stored in the SymbolTable under "input1",
    // "input2", ... and "output".
    private final int numInputs;

    // The default "no value" Operation for this OperationType.
    private final OPERATION none;


    /**
     * <p>
     * Creates a new OperationType with the specified type name and
     * SymbolTable.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
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
     * @param metadata The Metadata for this OperationType.
     *                 Can be Metadata.NONE.  Must not be null.
     *
     * @param input_types The List of 1 or more input Type(s) for the
     *                    Operations governed by this OperationType.
     *                    Must not contain any null elements.
     *                    Must not be null.
     *
     * @param output_type The output Type for the Operations governed
     *                    by this OperationType.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast AlwaysFail - OPERATION.
    public OperationType (
                          String raw_type_name,
                          SymbolTable symbol_table,
                          Metadata metadata,
                          List<Type<?>> input_types,
                          Type<OUTPUT> output_type
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustContainNoNulls.Violation
    {
        super ( raw_type_name, symbol_table, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name, input_types, output_type );
        classContracts.check ( Parameter4.MustContainNoNulls.CONTRACT,
                               input_types );

        this.id = new TypeID ( raw_type_name,       // raw type name
                               "",                  // tag_names
                               Visibility.PUBLIC ); // visibility

        this.numInputs = input_types.size ();

        int input_num = 0;
        for ( Type<?> input_type : input_types )
        {
            final TypeID input_type_id = new TypeID ( "#input"
                                                      + ( input_num + 1 ),
                                                      Visibility.PRIVATE );

            this.symbolTable ().set ( input_type_id, input_type );

            input_num ++;
        }

        final TypeID output_type_id = new TypeID ( "#output",
                                                   Visibility.PRIVATE );
        this.symbolTable ().set ( output_type_id, output_type );

        if ( ! symbol_table.containsSymbol ( NamespaceID.PARENT ) )
        {
            symbol_table.set ( NamespaceID.PARENT, Namespace.NONE );
        }

        this.none = (OPERATION)
            new AlwaysFail<OUTPUT> ( Operation.NO_NAME,
                                     this );
    }


    /**
     * <p>
     * Creates a List from the varargs inputs.
     * </p>
     *
     * @param array Zero or more inputs to turn into a List.
     *              Must not be null.  Must not contain any nulls.
     *
     * @return The newly created List.  Never null.
     */
    @SuppressWarnings("unchecked") // Varargs possible heap pollution
    public static <ELEMENT>
        List<ELEMENT> createList (
                                  ELEMENT ... elements
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) elements );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        final List<ELEMENT> list = new ArrayList<ELEMENT> ();
        for ( ELEMENT element : elements )
        {
            list.add ( element );
        }

        return list;
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
     * @param input_num The index of the input whose Type will be returned.
     *                  Pass 0 for the first input, 1 for the second,
     *                  and so on.
     *
     * @return The Type of value(s) accepted as input # N to the Operation,
     *         or Type.NONE if the Operation does not allow that many inputs.
     *         Never null.
     *
     * @throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation
     *                                 If input_num is less than 0.
     *
     * @throws Parameter1.DependsOn.This.Violation If input_num is greater
     *                                             than or equal to the number
     *                                             of inputs specified by
     *                                             this OperationType.
     */
    public final Type<?> inputType (
                                    int input_num
                                    )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter1.DependsOn.This.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                  input_num );
        // TODO !!! check new Parameter1.DependsOn.This ( <= this.numInputs ).

        final TypeID input_type_id = new TypeID ( "#input"
                                                  + ( input_num + 1 ),
                                                  Visibility.PRIVATE );
        final Type<?> input_type =
            this.symbolTable ().symbol ( input_type_id )
                .orNull ();
        if ( input_type != null )
        {
            return input_type;
        }

        return Type.NONE;
    }


    /**
     * @see musaico.foundation.typing.Type#none()
     */
    @Override
    public final OPERATION none ()
        throws ReturnNeverNull.Violation
    {
        return this.none;
    }


    /**
     * @return The number of inputs to this Operation.
     *         Always greater than zero.
     */
    public final int numInputs ()
        throws Return.AlwaysGreaterThanZero.Violation
    {
        return this.numInputs;
    }


    /**
     * @return The Type of value(s) returned by this Operation when all
     *         inputs are passed in.  Can be Type.NONE.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<OUTPUT>
    public final Type<OUTPUT> outputType ()
        throws ReturnNeverNull.Violation
    {
        final TypeID output_type_id = new TypeID ( "#output",
                                                   Visibility.PRIVATE );
        final Type<?> output_type =
            this.symbolTable ().symbol ( output_type_id )
                .orNone ();

        return (Type<OUTPUT>) output_type;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final OperationType<OPERATION, OUTPUT> rename (
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
    public OperationType<OPERATION, OUTPUT> rename (
                                                    String name,
                                                    SymbolTable symbol_table
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name, symbol_table );

        symbol_table.addAll ( this.symbolTable () );

        final List<Type<?>> input_types = new ArrayList<Type<?>> ();
        for ( int it = 0; it < this.numInputs; it ++ )
        {
            final TypeID input_type_id = new TypeID ( "#input"
                                                      + ( it + 1 ),
                                                      Visibility.PRIVATE );
            final Type<?> input_type =
                this.symbolTable ().symbol ( input_type_id )
                    .orNone ();
            input_types.add ( input_type );
        }

        final Type<OUTPUT> output_type = this.outputType ();

        return new OperationType<OPERATION, OUTPUT> ( name,
                                                      symbol_table,
                                                      this.metadata ().renew (),
                                                      input_types,
                                                      output_type );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.id ().name ();
    }


    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Op.class to generic OPERATION class
    public final Class<OPERATION> valueClass ()
        throws ReturnNeverNull.Violation
    {
        if ( this instanceof OperationType1 )
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation1.class );
        }
        else if ( this instanceof OperationType2 )
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation2.class );
        }
        else if ( this instanceof OperationType3 )
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation3.class );
        }
        else if ( this instanceof OperationType4 )
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation4.class );
        }
        else if ( this instanceof OperationType5 )
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation5.class );
        }
        else if ( this instanceof OperationType6 )
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation6.class );
        }
        /* !!!
            else if ( this instanceof OperationType7 )
        {
            return (Class<OPERATION>)
            ( (Class<?>) Operation7.class );
        }
        else if ( this instanceof OperationType8 )
        {
            return (Class<OPERATION>)
            ( (Class<?>) Operation8.class );
        }
        else if ( this instanceof OperationType9 )
        {
            return (Class<OPERATION>)
            ( (Class<?>) Operation9.class );
        }
        !!! */
        else
        {
            return (Class<OPERATION>)
                ( (Class<?>) Operation.class );
        }
    }
}
