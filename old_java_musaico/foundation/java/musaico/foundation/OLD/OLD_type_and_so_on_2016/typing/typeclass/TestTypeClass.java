package musaico.foundation.typing.typeclass;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.OperationBody1;
import musaico.foundation.typing.OperationBody2;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.OperationType1;
import musaico.foundation.typing.OperationType2;
import musaico.foundation.typing.RootNamespace;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.StandardOperation2;
import musaico.foundation.typing.StandardRootNamespace;
import musaico.foundation.typing.StandardType;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.UnknownType;

import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * !!!
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
public class TestTypeClass
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final UnknownType UNKNOWN_TYPE =
        new UnknownType ( Namespace.NONE, "unknown" );

    private static final OperationType2<Object, Object, Object> OP_VVV =
        new OperationType2<Object, Object, Object> ( UNKNOWN_TYPE,
                                                     UNKNOWN_TYPE,
                                                     UNKNOWN_TYPE );

    /** Add: ( VALUE, VALUE ): VALUE. */
    public static final OperationID<Operation2<Object, Object, Object>, Object> ADD =
        new OperationID<Operation2<Object, Object, Object>, Object> ( "add",
                                                                      OP_VVV );


    public static final UnknownType UNKNOWN_TYPE_SIDE1 =
        new UnknownType ( Namespace.NONE, "unknown1" );

    public static final UnknownType UNKNOWN_TYPE_SIDE2 =
        new UnknownType ( Namespace.NONE, "unknown2" );

    private static final OperationType1<Object, Object> OP_12 =
        new OperationType1<Object, Object> ( UNKNOWN_TYPE_SIDE1,
                                             UNKNOWN_TYPE_SIDE2 );

    private static final OperationType1<Object, Object> OP_21 =
        new OperationType1<Object, Object> ( UNKNOWN_TYPE_SIDE2,
                                             UNKNOWN_TYPE_SIDE1 );

    /** Flip1: ( SIDE_1 ): SIDE_2. */
    public static final OperationID<Operation1<Object, Object>, Object> FLIP1 =
        new OperationID<Operation1<Object, Object>, Object> ( "flip1",
                                                              OP_12 );

    /** Flip2: ( SIDE_2 ): SIDE_1. */
    public static final OperationID<Operation1<Object, Object>, Object> FLIP2 =
        new OperationID<Operation1<Object, Object>, Object> ( "flip2",
                                                              OP_21 );




    /**
     * <p>
     * Implements the ADD operation for UnknownTypes, adding two Object
     * Values together by creating a new Value which begins with the
     * objects from the first input and ends with the object from
     * the second input.
     * </p>
     */
    public static class AddObject
        implements OperationBody2<Object, Object, Object>
    {
        private static final long serialVersionUID =
            TestTypeClass.serialVersionUID;


        private final Type<Object> objectType;
        private final Operation2<Object, Object, Object> operation;


        /**
         * <p>
         * Creates a new AddObject operation with the specified name,
         * for the specified Type of Object values.
         * </p>
         *
         * @param name The name of the operation to create.
         *             Must not be null.
         *
         * @param object_type The Type of Object Values that will be added.
         *                    Must not be null.
         */
        public AddObject (
                          String name,
                          Type<Object> object_type
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this.objectType = object_type;
            this.operation =
                new StandardOperation2<Object, Object, Object> (
                    name,        // name
                    object_type, // input1_type
                    object_type, // input2_type
                    object_type, // output_type
                    this );      // body
        }


        /**
         * @see musaico.foundation.typing.OperationBody2#evaluateBody(musaico.foundation.value.Value, musaico.foundation.value.Value)
         */
        public Value<Object> evaluateBody (
                                           Value<Object> input1,
                                           Value<Object> input2
                                           )
        {
            final TypedValueBuilder<Object> builder =
                new TypedValueBuilder<Object> ( this.objectType );
            for ( Object object : input1 )
            {
                builder.add ( object );
            }

            for ( Object object : input2 )
            {
                builder.add ( object );
            }

            return builder.build ();
        }


        public final Operation2<Object, Object, Object> parentOperation ()
        {
            return this.operation;
        }
    }




    /**
     * <p>
     * Implements the FLIP operation for heads or tails.
     * </p>
     */
    public static class Flip
        implements OperationBody1<String, String>
    {
        private static final long serialVersionUID =
            TestTypeClass.serialVersionUID;


        private final Type<String> stringTypeIn;
        private final Type<String> stringTypeOut;
        private final Operation1<String, String> operation;


        /**
         * <p>
         * Creates a new Flip operation with the specified name,
         * for the specified Types of String values.
         * </p>
         *
         * @param name The name of the operation to create.
         *             Must not be null.
         *
         * @param string_type_in The input Type of String Values that will
         *                       be flipped.  Must not be null.
         *
         * @param string_type_in The output Type of String Values after
         *                       flipping.  Must not be null.
         */
        public Flip (
                     String name,
                     Type<String> string_type_in,
                     Type<String> string_type_out
                     )
            throws ParametersMustNotBeNull.Violation
        {
            this.stringTypeIn = string_type_in;
            this.stringTypeOut = string_type_out;
            this.operation =
                new StandardOperation1<String, String> (
                    name,            // name
                    string_type_in,  // input1_type
                    string_type_out, // output_type
                    this );          // body
        }


        /**
         * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
         */
        public Value<String> evaluateBody (
                                           Value<String> input1
                                           )
        {
            final TypedValueBuilder<String> builder =
                new TypedValueBuilder<String> ( this.stringTypeOut );
            for ( String unflipped : input1 )
            {
                final String flipped;
                if ( unflipped.equals ( this.stringTypeIn.id ().name () ) )
                {
                    flipped = this.stringTypeOut.id ().name ();
                }
                else
                {
                    flipped = unflipped;
                }

                builder.add ( flipped );
            }

            return builder.build ();
        }


        public final Operation1<String, String> parentOperation ()
        {
            return this.operation;
        }
    }




    /**
     * <p>
     * Implements the ADD operation for String Types, adding two String
     * Values together by concatenating them into one big long String.
     * </p>
     */
    public static class AddString
        implements OperationBody2<String, String, String>
    {
        private static final long serialVersionUID =
            TestTypeClass.serialVersionUID;


        private final Type<String> stringType;
        private final Operation2<String, String, String> operation;


        /**
         * <p>
         * Creates a new AddString operation with the specified name,
         * for the specified Type of String values.
         * </p>
         *
         * @param name The name of the operation to create.
         *             Must not be null.
         *
         * @param string_type The Type of String Values that will be added.
         *             Must not be null.
         */
        public AddString (
                          String name,
                          Type<String> string_type
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this.stringType = string_type;
            this.operation =
                new StandardOperation2<String, String, String> (
                    name,        // name
                    string_type, // input1_type
                    string_type, // input2_type
                    string_type, // output_type
                    this );      // body
        }


        /**
         * @see musaico.foundation.typing.OperationBody2#evaluateBody(musaico.foundation.value.Value, musaico.foundation.value.Value)
         */
        public Value<String> evaluateBody (
                                           Value<String> input1,
                                           Value<String> input2
                                           )
        {
            final StringBuilder sbuf = new StringBuilder ();

            for ( String string : input1 )
            {
                sbuf.append ( string );
            }

            for ( String string : input2 )
            {
                sbuf.append ( string );
            }

            final TypedValueBuilder<String> builder =
                new TypedValueBuilder<String> ( this.stringType );

            builder.add ( sbuf.toString () );

            return builder.build ();
        }


        public final Operation2<String, String, String> parentOperation ()
        {
            return this.operation;
        }
    }




    public void testTypeClassRequiredSymbols ()
        throws Exception
    {
        System.out.println ( "" );

        final TypeClass addable =
            new TypeClass ( Namespace.ROOT,
                            "addable",
                            new SymbolID<?> []
                            {
                                ADD
                            } );

        System.out.println ( "" + addable );
        System.out.println ( "" );


        final SymbolTable string_type_symbol_table = new SymbolTable ();
        final Type<String> string_type =
            Kind.ROOT.typeBuilder ( String.class, string_type_symbol_table )
                .rawTypeName ( "string" )
                .namespace ( Namespace.ROOT )
                .none ( "" )
                .defaultSubTyping ()
                .build ()
                .orThrowUnchecked ();

        final TypedValueBuilder<String> string_input1_builder =
            new TypedValueBuilder<String> ( string_type );
        string_input1_builder.add ( "1" );
        string_input1_builder.add ( "2" );
        string_input1_builder.add ( "3" );
        final Value<String> string_input1 = string_input1_builder.build ();

        final TypedValueBuilder<String> string_input2_builder =
            new TypedValueBuilder<String> ( string_type );
        string_input2_builder.add ( "4" );
        string_input2_builder.add ( "5" );
        final Value<String> string_input2 = string_input2_builder.build ();

        final TypedValueBuilder<Object> object_input1_builder =
            new TypedValueBuilder<Object> ( TestTypeClass.UNKNOWN_TYPE );
        object_input1_builder.add ( 1 );
        object_input1_builder.add ( 2 );
        object_input1_builder.add ( 3 );
        final Value<Object> object_input1 = object_input1_builder.build ();

        final TypedValueBuilder<Object> object_input2_builder =
            new TypedValueBuilder<Object> ( TestTypeClass.UNKNOWN_TYPE );
        object_input2_builder.add ( 4 );
        object_input2_builder.add ( 5 );
        final Value<Object> object_input2 = object_input2_builder.build ();


        System.out.println ( "=============================================" );
        System.out.println ( "TypeClassInstances before implementing add:" );
        final Value<TypeClassInstance> instances_failure =
            addable.instance ( string_type );
        System.out.println ( "    " + instances_failure );
        if ( instances_failure.hasValue () )
        {
            throw new Exception ( "Failed!  The string_type does not provide the add operation, yet it produced an instance of addable." );
        }

        int instances_count = 0;
        for ( TypeClassInstance type_class_instance
                  : instances_failure )
        {
            // First try add() with string inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using String inputs:" );
            final Value<Object> v_output_for_string_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                string_input1, string_input2 );
            boolean has_output = false;
            for ( Object output : v_output_for_string_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_string_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );
            }

            // Now try add() with object inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using Object inputs:" );
            final Value<Object> v_output_for_object_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                object_input1, object_input2 );
            has_output = false;
            for ( Object output : v_output_for_object_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_object_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );
            }

            instances_count ++;
        }

        if ( instances_count != 0 )
        {
            throw new Exception ( "TypeClass should have provided 0 TypeClassInstances" );
        }

        System.out.println ( "OK" );
        System.out.println ( "" );


        System.out.println ( "=============================================" );
        final Operation2<Object, Object, Object> add_object =
            new TestTypeClass.AddObject ( TestTypeClass.ADD.name (),
                                          TestTypeClass.UNKNOWN_TYPE )
                .parentOperation ();

        // Create a new TypeClass that provides all the
        // operations for *any* type, and make sure the
        // string type is able to instantiate this TypeClass, even though
        // it does not have its own String-based operation instantiations.
        final SymbolTable unknown_provider_symbol_table =
            new SymbolTable ();
        unknown_provider_symbol_table.add ( add_object );
        final TypeClass addable_unknown_provider =
            addable.rename ( "addable_unknown_provider",
                             unknown_provider_symbol_table );

        System.out.println ( "New TypeClass provides Operations"
                             + " for any Type:"
                             + "\n"
                             + addable_unknown_provider );
        System.out.println ( "" );

        System.out.println ( "TypeClassInstances with generic add provided by"
                             + " the TypeClass:" );
        final Value<TypeClassInstance> instances_unknown_provided =
            addable_unknown_provider.instance ( string_type );
        System.out.println ( "    " + instances_unknown_provided );
        if ( ! instances_unknown_provided.hasValue () )
        {
            throw new Exception ( "Failed!  The addable_unknown_provider TypeClass provides a generic add operation, yet the string_type did NOT produce an instance of addable_unknown_provider." );
        }

        instances_count = 0;
        for ( TypeClassInstance type_class_instance
                  : instances_unknown_provided )
        {
            // First try add() with string inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using String inputs:" );
            final Value<Object> v_output_for_string_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                string_input1, string_input2 );
            boolean has_output = false;
            for ( Object output : v_output_for_string_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_string_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );

                throw new Exception ( "There should have been output." );
            }

            // Now try add() with object inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using Object inputs:" );
            final Value<Object> v_output_for_object_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                object_input1, object_input2 );
            has_output = false;
            for ( Object output : v_output_for_object_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_object_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );

                throw new Exception ( "There should have been output." );
            }

            instances_count ++;
        }

        if ( instances_count != 1 )
        {
            throw new Exception ( "TypeClass should have provided 1 TypeClassInstances" );
        }

        System.out.println ( "OK" );
        System.out.println ( "" );


        final Operation2<String, String, String> add_string =
            new TestTypeClass.AddString ( TestTypeClass.ADD.name (),
                                          string_type )
                .parentOperation ();


        System.out.println ( "=============================================" );
        // Create a new TypeClass that actually provides all the
        // String operations for the string type, and make sure the
        // string type is able to instantiate this TypeClass, even though
        // it does not have its own operation instantiations.
        final SymbolTable provider_symbol_table =
            new SymbolTable ();
        provider_symbol_table.add ( add_string );
        final TypeClass addable_provider =
            addable.rename ( "addable_provider",
                             provider_symbol_table );

        System.out.println ( "New TypeClass provides the Operations"
                             + " specifically for Strings:"
                             + "\n"
                             + addable_provider );
        System.out.println ( "" );

        System.out.println ( "TypeClassInstances with String-specific add"
                             + "\n"
                             + "provided by the TypeClass"
                             + " (not by the Type):" );
        final Value<TypeClassInstance> instances_provided =
            addable_provider.instance ( string_type );
        System.out.println ( "    " + instances_provided );
        if ( ! instances_provided.hasValue () )
        {
            throw new Exception ( "Failed!  The addable_provider TypeClass provides the add operation specifically for Strings, yet the string_type did NOT produce an instance of addable_provider." );
        }

        instances_count = 0;
        for ( TypeClassInstance type_class_instance
                  : instances_provided )
        {
            // First try add() with string inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using String inputs:" );
            final Value<Object> v_output_for_string_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                string_input1, string_input2 );
            boolean has_output = false;
            for ( Object output : v_output_for_string_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_string_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );

                throw new Exception ( "There should have been output." );
            }

            // Now try add() with object inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using Object inputs:" );
            final Value<Object> v_output_for_object_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                object_input1, object_input2 );
            has_output = false;
            for ( Object output : v_output_for_object_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_object_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );
            }
            else
            {
                throw new Exception ( "There should NOT have been output." );
            }

            instances_count ++;
        }

        if ( instances_count != 1 )
        {
            throw new Exception ( "TypeClass should have provided 1 TypeClassInstances" );
        }

        System.out.println ( "OK" );
        System.out.println ( "" );


        System.out.println ( "=============================================" );
        // Now add the Operation directly to the string type, and
        // make sure it can implement the original TypeClass.
        string_type_symbol_table.add ( add_string );

        System.out.println ( "Type " + string_type.id ().name ()
                             + " has now implemented "
                             + add_string.id () );
        System.out.println ( "" );

        System.out.println ( "TypeClassInstances after implementing add:" );
        final Value<TypeClassInstance> instances_success =
            addable.instance ( string_type );
        System.out.println ( "    " + instances_success );
        if ( ! instances_success.hasValue () )
        {
            throw new Exception ( "Failed!  The string_type provides the add operation, yet it did NOT produce an instance of addable." );
        }

        instances_count = 0;
        for ( TypeClassInstance type_class_instance
                  : instances_success )
        {
            // First try add() with string inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using String inputs:" );
            final Value<Object> v_output_for_string_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                string_input1, string_input2 );
            boolean has_output = false;
            for ( Object output : v_output_for_string_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_string_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );

                throw new Exception ( "There should have been output." );
            }

            // Now try add() with object inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using Object inputs:" );
            final Value<Object> v_output_for_object_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                object_input1, object_input2 );
            has_output = false;
            for ( Object output : v_output_for_object_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_object_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );
            }
            else
            {
                throw new Exception ( "There should NOT have been output." );
            }

            instances_count ++;
        }

        if ( instances_count != 1 )
        {
            throw new Exception ( "TypeClass should have provided 1 TypeClassInstances" );
        }

        System.out.println ( "OK" );
        System.out.println ( "" );


        System.out.println ( "=============================================" );
        // Using the Type with the String Operation, instantiate the
        // TypeClass with the Object Operation, to see what happens...
        System.out.println ( "TypeClassInstances of the TypeClass which"
                             + " provides add(unknown)"
                             + "\n"
                             + "after the Type has implemented add(string):" );
        final Value<TypeClassInstance> instances_multiple =
            addable_unknown_provider.instance ( string_type );
        System.out.println ( "    " + instances_multiple );
        if ( ! instances_multiple.hasValue () )
        {
            throw new Exception ( "Failed!  The string_type provides the add(string) operation"
                                  + "\n"
                                  + "AND the TypeClass itself provides the add(unknown) operation,"
                                  + "\n"
                                  + "yet it did NOT produce an instance of addable_unknown_provider." );
        }

        instances_count = 0;
        for ( TypeClassInstance type_class_instance
                  : instances_multiple )
        {
            // First try add() with string inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using String inputs:" );
            final Value<Object> v_output_for_string_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                string_input1, string_input2 );
            boolean has_output = false;
            for ( Object output : v_output_for_string_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_string_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );

                throw new Exception ( "There should have been output." );
            }

            // Now try add() with object inputs.
            System.out.println ( "    Running " + TestTypeClass.ADD
                                 + " with TypeClassInstance"
                                 + "\n"
                                 + "      "
                                 + type_class_instance
                                 + "\n"
                                 + "      "
                                 + "using Object inputs:" );
            final Value<Object> v_output_for_object_inputs =
                type_class_instance
                    .evaluate ( TestTypeClass.ADD,
                                object_input1, object_input2 );
            has_output = false;
            for ( Object output : v_output_for_object_inputs )
            {
                System.out.println ( "        Output: "
                                     + ClassName.of ( output.getClass () )
                                     + " "
                                     + output );
                has_output = true;
            }
            if ( ! has_output )
            {
                final Violation violation =
                    ( (NotOne<Object>) v_output_for_object_inputs )
                    .valueViolation ()
                    .causeViolation ();
                System.out.println ( "        (no output because of "
                                     + violation.contract ()
                                     + " violation:"
                                     + "\n"
                                     + "          "
                                     + violation.inspectableData ()
                                     + ")" );

                if ( instances_count == 0 ) // add(object)
                {
                    throw new Exception ( "There should have been output." );
                }
            }
            else
            {
                if ( instances_count == 1 ) // add(string)
                {
                    throw new Exception ( "There should NOT have been output." );
                }
            }

            instances_count ++;
        }

        if ( instances_count != 2 )
        {
            throw new Exception ( "TypeClass should have provided 2 TypeClassInstances" );
        }

        System.out.println ( "OK" );
        System.out.println ( "" );
    }




    public void testTypeClassChildTypeClasses ()
        throws Exception
    {
        // A parent TypeClass "flippable" which has "side_1"
        // and "side_2" sub-TypeClasses.
        final SymbolTable flippable_symbol_table = new SymbolTable ();
        final TypeClass flippable =
            new TypeClass ( Namespace.ROOT,
                            "flippable",
                            new SymbolID<?> []
                            {
                                FLIP1,
                                FLIP2
                            },
                            flippable_symbol_table );

        final TypeClass side_1 =
            new TypeClass ( flippable,
                            "side_1",
                            new SymbolID<?> []
                            {
                                FLIP1
                            } );
        flippable_symbol_table.add ( side_1 );

        final TypeClass side_2 =
            new TypeClass ( flippable,
                            "side_2",
                            new SymbolID<?> []
                            {
                                FLIP2
                            } );
        flippable_symbol_table.add ( side_2 );

        System.out.println ( "" + flippable + " :" );
        System.out.println ( flippable.printSymbolTable () );
        System.out.println ( "" );


        final RootNamespace test_namespace =
            new StandardRootNamespace ( Namespace.ROOT,
                                        "testTypeClassChildTypeClasses",
                                        new SymbolTable () );

        final SymbolTable coin_type_symbol_table = new SymbolTable ();
        final Type<String> coin_type;
        final musaico.foundation.typing.TypeBuilder<String> type_builder =
            Kind.ROOT.typeBuilder ( String.class, coin_type_symbol_table )
                .rawTypeName ( "coin" )
                .namespace ( test_namespace )
                .none ( "" )
            .defaultSubTyping ();
        coin_type = type_builder
                .build ()
                .orThrowUnchecked ();

        final SymbolTable heads_type_symbol_table = new SymbolTable ();
        final Type<String> heads_type =
            Kind.ROOT.typeBuilder ( String.class, heads_type_symbol_table )
                .rawTypeName ( "heads" )
                .namespace ( coin_type )
                .none ( "" )
                .defaultSubTyping ()
                .build ()
                .orThrowUnchecked ();

        final SymbolTable tails_type_symbol_table = new SymbolTable ();
        final Type<String> tails_type =
            Kind.ROOT.typeBuilder ( String.class, tails_type_symbol_table )
                .rawTypeName ( "tails" )
                .namespace ( coin_type )
                .none ( "" )
                .defaultSubTyping ()
                .build ()
                .orThrowUnchecked ();

        final Operation1<String, String> heads_flip =
            new TestTypeClass.Flip ( TestTypeClass.FLIP1.name (),
                                     heads_type, tails_type )
                .parentOperation ();
        heads_type_symbol_table.add ( heads_flip );

        final Operation1<String, String> tails_flip =
            new TestTypeClass.Flip ( TestTypeClass.FLIP2.name (),
                                     tails_type, heads_type )
                .parentOperation ();
        tails_type_symbol_table.add ( tails_flip );


        final Operation1<String, String> heads_flip_coin =
            new TestTypeClass.Flip ( TestTypeClass.FLIP1.name (),
                                     heads_type, tails_type )
                .parentOperation ();
        final Operation1<String, String> tails_flip_coin =
            new TestTypeClass.Flip ( TestTypeClass.FLIP2.name (),
                                     tails_type, heads_type )
                .parentOperation ();
        coin_type_symbol_table.addAll ( heads_type,
                                        tails_type,
                                        heads_flip_coin,
                                        tails_flip_coin );


        System.out.println ( "  Type 'heads' typeclass instance(s)"
                             + " of side_1:" );
        int heads_side_1_typeclass_count = 0;
        for ( TypeClassInstance heads_instance
                  : side_1.instance ( heads_type ) )
        {
            System.out.println ( "    " + heads_instance );
            heads_side_1_typeclass_count ++;
        }
        if ( heads_side_1_typeclass_count != 1 )
        {
            throw new Exception ( "  Type 'heads' should have"
                                  + " 1 TypeClassInstances of"
                                  + " typeclass 'side_1'\n"
                                  + "  but has "
                                  + heads_side_1_typeclass_count );
        }
        System.out.println ( "        OK." );

        System.out.println ( "  Type 'heads' typeclass instance(s)"
                             + " of side_2:" );
        int heads_side_2_typeclass_count = 0;
        for ( TypeClassInstance heads_instance
                  : side_2.instance ( heads_type ) )
        {
            System.out.println ( "    " + heads_instance );
            heads_side_2_typeclass_count ++;
        }
        if ( heads_side_2_typeclass_count != 0 )
        {
            throw new Exception ( "  Type 'heads' should have"
                                  + " 0 TypeClassInstances of"
                                  + " typeclass 'side_2'\n"
                                  + "  but has "
                                  + heads_side_2_typeclass_count );
        }
        System.out.println ( "        OK." );


        System.out.println ( "  Type 'tails' typeclass instance(s)"
                             + " of side_1:" );
        int tails_side_1_typeclass_count = 0;
        for ( TypeClassInstance tails_instance
                  : side_1.instance ( tails_type ) )
        {
            System.out.println ( "    " + tails_instance );
            tails_side_1_typeclass_count ++;
        }
        if ( tails_side_1_typeclass_count != 0 )
        {
            throw new Exception ( "  Type 'tails' should have"
                                  + " 0 TypeClassInstances of"
                                  + " typeclass 'side_1'\n"
                                  + "  but has "
                                  + tails_side_1_typeclass_count );
        }
        System.out.println ( "        OK." );

        System.out.println ( "  Type 'tails' typeclass instance(s)"
                             + " of side_2:" );
        int tails_side_2_typeclass_count = 0;
        for ( TypeClassInstance tails_instance
                  : side_2.instance ( tails_type ) )
        {
            System.out.println ( "    " + tails_instance );
            tails_side_2_typeclass_count ++;
        }
        if ( tails_side_2_typeclass_count != 1 )
        {
            throw new Exception ( "  Type 'tails' should have"
                                  + " 1 TypeClassInstances of"
                                  + " typeclass 'side_2'\n"
                                  + "  but has "
                                  + tails_side_2_typeclass_count );
        }
        System.out.println ( "        OK." );


        System.out.println ( "  Type 'coin' typeclass instance(s)"
                             + " of flippable:" );
        int coin_flippable_typeclass_count = 0;
        for ( TypeClassInstance coin_instance
                  : flippable.instance ( coin_type ) )
        {
            System.out.println ( "    " + coin_instance );
            coin_flippable_typeclass_count ++;
        }
        if ( coin_flippable_typeclass_count != 1 )
        {
            throw new Exception ( "  Type 'coin' should have"
                                  + " 1 TypeClassInstance of"
                                  + " typeclass 'flippable'\n"
                                  + "  (side_1=heads, side_2=tails)\n"
                                  + "  but has "
                                  + coin_flippable_typeclass_count );
        }
        System.out.println ( "        OK." );

        final TypedValueBuilder<String> heads_input_builder =
            new TypedValueBuilder<String> ( heads_type );
        heads_input_builder.add ( heads_type.id ().name () );
        heads_input_builder.add ( heads_type.id ().name () );
        heads_input_builder.add ( heads_type.id ().name () );
        final Value<String> heads_input = heads_input_builder.build ();

        final TypedValueBuilder<String> tails_input_builder =
            new TypedValueBuilder<String> ( tails_type );
        tails_input_builder.add ( tails_type.id ().name () );
        tails_input_builder.add ( tails_type.id ().name () );
        final Value<String> tails_input = tails_input_builder.build ();

        System.out.println ( "Flipping a bunch of coins:" );

        for ( TypeClassInstance coin_instance
                  : flippable.instance ( coin_type ) )
        {
            System.out.println ( "  Flipping " + coin_instance );

            final TypeClassInstance side_1_instance =
                coin_instance.child ( side_1 ).orNone ();
            System.out.println ( "    Side 1 = " + side_1_instance );

            final TypeClassInstance side_2_instance =
                coin_instance.child ( side_2 ).orNone ();
            System.out.println ( "    Side 2 = " + side_2_instance );

            System.out.println ( "      Side 1:" );
            final Value<Object> flipped_heads_side_1 =
                                 side_1_instance.evaluate ( TestTypeClass.FLIP1,
                                                            heads_input );
            int num_flipped_heads_side_1 = 0;
            for ( Object flipped : flipped_heads_side_1 )
            {
                System.out.println ( "        heads -> " + flipped );
                if ( ! flipped.equals ( "tails" ) )
                {
                    throw new Exception ( "  Heads should flip to tails." );
                }
                num_flipped_heads_side_1 ++;
            }
            if ( num_flipped_heads_side_1 != 3 )
            {
                throw new Exception ( "  Heads should have flipped to 3 tails" );
            }
            System.out.println ( "        OK." );

            final Value<Object> flipped_tails_side_1 =
                                 side_1_instance.evaluate ( TestTypeClass.FLIP2,
                                                            tails_input );
            int num_flipped_tails_side_1 = 0;
            for ( Object flipped : flipped_tails_side_1 )
            {
                System.out.println ( "        tails -> " + flipped );
                if ( ! flipped.equals ( "heads" ) )
                {
                    throw new Exception ( "  Tails should flip to heads." );
                }
                num_flipped_tails_side_1 ++;
            }
            if ( num_flipped_tails_side_1 != 0 )
            {
                throw new Exception ( "  Heads should not have a flip ( tails ) operation at all" );
            }
            System.out.println ( "        [No flip(tails)]" );
            System.out.println ( "        OK." );

            System.out.println ( "      Side 2:" );
            final Value<Object> flipped_heads_side_2 =
                                 side_2_instance.evaluate ( TestTypeClass.FLIP1,
                                                            heads_input );
            int num_flipped_heads_side_2 = 0;
            for ( Object flipped : flipped_heads_side_2 )
            {
                System.out.println ( "        heads -> " + flipped );
                if ( ! flipped.equals ( "tails" ) )
                {
                    throw new Exception ( "  Heads should flip to tails." );
                }
                num_flipped_heads_side_2 ++;
            }
            if ( num_flipped_heads_side_2 != 0 )
            {
                throw new Exception ( "  Tails should not have a flip ( heads ) operation at all" );
            }
            System.out.println ( "        [No flip(heads)]" );
            System.out.println ( "        OK." );

            final Value<Object> flipped_tails_side_2 =
                                 side_2_instance.evaluate ( TestTypeClass.FLIP2,
                                                            tails_input );
            int num_flipped_tails_side_2 = 0;
            for ( Object flipped : flipped_tails_side_2 )
            {
                System.out.println ( "        tails -> " + flipped );
                if ( ! flipped.equals ( "heads" ) )
                {
                    throw new Exception ( "  Tails should flip to heads." );
                }
                num_flipped_tails_side_2 ++;
            }
            if ( num_flipped_tails_side_2 != 2 )
            {
                throw new Exception ( "  Tails should have flipped to 2 heads" );
            }
            System.out.println ( "        OK." );


            System.out.println ( "      Coin:" );
            final Value<Object> flipped_heads_coin =
                                 coin_instance.evaluate ( TestTypeClass.FLIP1,
                                                          heads_input );
            int num_flipped_heads_coin = 0;
            for ( Object flipped : flipped_heads_coin )
            {
                System.out.println ( "        heads -> " + flipped );
                if ( ! flipped.equals ( "tails" ) )
                {
                    throw new Exception ( "  Heads should flip to tails." );
                }
                num_flipped_heads_coin ++;
            }
            if ( num_flipped_heads_side_1 != 3 )
            {
                throw new Exception ( "  Heads should have flipped to 3 tails" );
            }
            System.out.println ( "        OK." );

            final Value<Object> flipped_tails_coin =
                                 coin_instance.evaluate ( TestTypeClass.FLIP2,
                                                          tails_input );
            int num_flipped_tails_coin = 0;
            for ( Object flipped : flipped_tails_coin )
            {
                System.out.println ( "        tails -> " + flipped );
                if ( ! flipped.equals ( "heads" ) )
                {
                    throw new Exception ( "  Tails should flip to heads." );
                }
                num_flipped_tails_coin ++;
            }
            if ( num_flipped_tails_side_2 != 2 )
            {
                throw new Exception ( "  Tails should have flipped to 2 heads" );
            }
            System.out.println ( "        OK." );
        }
    }




    public static void main (
                             String [] args
                             )
        throws Exception
    {
        final TestTypeClass test = new TestTypeClass ();

        System.out.println ( "" );
        System.out.println ( "" );

        test.testTypeClassRequiredSymbols ();

        System.out.println ( "" );
        System.out.println ( "" );
        System.out.println ( "" );
        System.out.println ( "" );

        test.testTypeClassChildTypeClasses ();
    }
}
