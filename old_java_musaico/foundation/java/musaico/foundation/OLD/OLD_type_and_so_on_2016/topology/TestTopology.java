package musaico.foundation.topology;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.AbstractOrder;
import musaico.foundation.order.Comparison;
import musaico.foundation.order.Order;

import musaico.foundation.typing.Constant;
import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBody1;
import musaico.foundation.typing.OperationBody2;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.StandardOperation2;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypedValueBuilder;

import musaico.foundation.typing.typeclass.TypeClass;
import musaico.foundation.typing.typeclass.TypeClassInstance;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;


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
public class TestTopology
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * !!!
     */
    @SuppressWarnings("unchecked") // Cast Class<Order> - Class<Order<Int>>,
                                   // cast OpID<?, ?> - OpID<?, Integer>,
                                   // cast Symbol - Constant<Order<Integer>>.
    public void testTopologyTypeClass ()
        throws Exception
    {
        final TopologyTypeClass topology_type_class =
            new TopologyTypeClass ( Namespace.ROOT,
                                    "topology" );

        final SymbolTable integer_type_symbol_table = new SymbolTable ();
        final Type<Integer> integer_type =
            Kind.ROOT.typeBuilder ( Integer.class, integer_type_symbol_table )
                .rawTypeName ( "integer" )
                .namespace ( Namespace.ROOT )
                .none ( -1 )
                .defaultSubTyping ()
                .build ()
                .orThrowUnchecked ();

        // Order integers from lowest to highest:
        final Order<Integer> ascending_integers =
            new AbstractOrder<Integer> ( "ascending_integers" )
        {
            private static final long serialVersionUID = 1L;

            /**
             * @see musaico.foundation.order.Order#compareValues(java.lang.Object, java.lang.Object)
             */
            @Override
            public final Comparison compareValues (
                                                   Integer left,
                                                   Integer right
                                                   )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
            {
                final int java_comparison = left.compareTo ( right );
                return Comparison.fromComparatorValue ( java_comparison );
            }
        };

        // Order all integers equal:
        final Order<Integer> no_integer_order =
            new AbstractOrder<Integer> ( "no_integer_order" )
        {
            private static final long serialVersionUID = 1L;

            /**
             * @see musaico.foundation.order.Order#compareValues(java.lang.Object, java.lang.Object)
             */
            @Override
            public final Comparison compareValues (
                                                   Integer left,
                                                   Integer right
                                                   )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
            {
                return Comparison.LEFT_EQUALS_RIGHT;
            }
        };

        // The Type for all Order<Integer>s.
        final Type<Order<Integer>> integer_order_type =
            Kind.ROOT.typeBuilder ( (Class<Order<Integer>>) no_integer_order.getClass (),
                                    new SymbolTable () )
                .rawTypeName ( "integer_order" )
                .namespace ( Namespace.ROOT )
                .none ( no_integer_order )
                .defaultSubTyping ()
                .build ()
                .orThrowUnchecked ();


        // BigDecimal: BigDecimal Type.
        integer_type_symbol_table.set ( TopologyTypeClass.BIG_DECIMAL_TYPE_ID,
                                        TopologyTypeClass.BIG_DECIMAL_TYPE );

        // max : Integer constant
        integer_type_symbol_table.add (
            new Constant<Integer> (
                TopologyTypeClass.Point.MAX.name (),
                integer_type,
                new TypedValueBuilder<Integer> ( integer_type )
                    .add ( Integer.MAX_VALUE )
                    .buildZeroOrOne () )
            );

        // min: Integer constant
        integer_type_symbol_table.add (
            new Constant<Integer> (
                TopologyTypeClass.Point.MIN.name (),
                integer_type,
                new TypedValueBuilder<Integer> ( integer_type )
                    .add ( Integer.MIN_VALUE )
                    .buildZeroOrOne () )
            );

        // order : ordering of Integers constant
        integer_type_symbol_table.add (
            new Constant<Order<Integer>> (
                TopologyTypeClass.Point.ORDER.name (),
                integer_order_type,
                new TypedValueBuilder<Order<Integer>> ( integer_order_type )
                    .add ( ascending_integers )
                    .buildZeroOrOne () )
            );

        // origin : Integer constant
        integer_type_symbol_table.add (
            new Constant<Integer> (
                TopologyTypeClass.Point.ORIGIN.name (),
                integer_type,
                new TypedValueBuilder<Integer> ( integer_type )
                    .add ( 0 )
                    .buildZeroOrOne () )
            );

        // unit : Integer constant
        integer_type_symbol_table.add (
            new Constant<Integer> (
                TopologyTypeClass.Measure.UNIT.name (),
                integer_type,
                new TypedValueBuilder<Integer> ( integer_type )
                    .add ( 1 )
                    .buildZeroOrOne () )
            );

        // add ( Integer, Integer ): Integer
        //     (Implements both Point.ADD and Measure.ADD)
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, Integer, Integer> (
                TopologyTypeClass.Point.ADD.name (),
                integer_type,
                integer_type,
                integer_type,
                new OperationBody2<Integer, Integer, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs1,
                            Value<Integer> inputs2
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<Integer> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final int input2_int;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = 0;
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_int = input2.next ().intValue ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = input2.next ().intValue ();
                            }

                            final Integer output = input1_int + input2_int;
                            // System.out.println ( "        " + input1_int
                            //                      + " + "
                            //                      + input2_int
                            //                      + " = "
                            //                      + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // divide ( Integer, Integer ): Integer
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, Integer, Integer> (
                TopologyTypeClass.Measure.DIVIDE.name (),
                integer_type,
                integer_type,
                integer_type,
                new OperationBody2<Integer, Integer, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs1,
                            Value<Integer> inputs2
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<Integer> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final int input2_int;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = Integer.MAX_VALUE;
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_int = input2.next ().intValue ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = input2.next ().intValue ();
                            }

                            if ( input2_int == 0 )
                            {
                                final Parameter2.MustNotEqual contract =
                                    new Parameter2.MustNotEqual ( 0 );
                                final Parameter2.MustNotEqual.Violation violation =
                                    contract.violation (
                                        contract,
                                        this,
                                        input2_int );
                                return builder.buildViolation ( violation );
                            }

                            final Integer output = input1_int / input2_int;
                            // System.out.println ( "        " + input1_int
                            //                      + " / "
                            //                      + input2_int
                            //                      + " = "
                            //                      + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // modulo ( Integer, Integer ): Integer
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, Integer, Integer> (
                TopologyTypeClass.Measure.MODULO.name (),
                integer_type,
                integer_type,
                integer_type,
                new OperationBody2<Integer, Integer, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs1,
                            Value<Integer> inputs2
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<Integer> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final int input2_int;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = Integer.MAX_VALUE;
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_int = input2.next ().intValue ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = input2.next ().intValue ();
                            }

                            if ( input2_int <= 0 )
                            {
                                final Parameter2.MustBeGreaterThanZero.Violation violation =
                                    Parameter2.MustBeGreaterThanZero.CONTRACT.violation (
                                        Parameter2.MustBeGreaterThanZero.CONTRACT,
                                        this,
                                        input2_int );
                                return builder.buildViolation ( violation );
                            }

                            final Integer output = input1_int % input2_int;
                            // System.out.println ( "        " + input1_int
                            //                      + " % "
                            //                      + input2_int
                            //                      + " = "
                            //                      + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // multiply ( Integer, BigDecimal ): Integer
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, BigDecimal, Integer> (
                TopologyTypeClass.Measure.MULTIPLY.name (),
                integer_type,
                TopologyTypeClass.BIG_DECIMAL_TYPE,
                integer_type,
                new OperationBody2<Integer, BigDecimal, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs1,
                            Value<BigDecimal> inputs2
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<BigDecimal> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final BigDecimal input2_bd;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_bd =
                                    new BigDecimal ( "" + Integer.MAX_VALUE );
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_bd = input2.next ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_bd = input2.next ();
                            }

                            final BigDecimal output_bd =
                                new BigDecimal ( "" + input1_int )
                                .multiply ( input2_bd );
                            final Integer output =
                                new Integer ( output_bd.intValue () );
                            // System.out.println ( "        " + input1_int
                            //                      + " % "
                            //                      + input2_bd
                            //                      + " = "
                            //                      + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // next ( Integer ): Integer
        //     (Implements both Point.NEXT and Measure.NEXT)
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation1<Integer, Integer> (
                TopologyTypeClass.Point.NEXT.name (),
                integer_type,
                integer_type,
                new OperationBody1<Integer, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        for ( Integer input : inputs )
                        {
                            final int next = input.intValue () + 1;
                            // System.out.println ( "        " + input
                            //                      + ".next() = "
                            //                      + next );
                            builder.add ( next );
                        }

                        return builder.build ();
                    }
                } )
            );

        // previous ( Integer ): Integer
        //     (Implements both Point.PREVIOUS and Measure.PREVIOUS)
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation1<Integer, Integer> (
                TopologyTypeClass.Point.PREVIOUS.name (),
                integer_type,
                integer_type,
                new OperationBody1<Integer, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        for ( Integer input : inputs )
                        {
                            final int previous = input.intValue () - 1;
                            // System.out.println ( "        " + input
                            //                      + ".previous() = "
                            //                      + previous );
                            builder.add ( previous );
                        }

                        return builder.build ();
                    }
                } )
            );

        // ratio ( Integer, Integer ): BigDecimal
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, Integer, BigDecimal> (
                TopologyTypeClass.Measure.RATIO.name (),
                integer_type,
                integer_type,
                TopologyTypeClass.BIG_DECIMAL_TYPE,
                new OperationBody2<Integer, Integer, BigDecimal> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<BigDecimal> evaluateBody (
                            Value<Integer> inputs1,
                            Value<Integer> inputs2
                            )
                    {
                        final TypedValueBuilder<BigDecimal> builder =
                            TopologyTypeClass.BIG_DECIMAL_TYPE.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<Integer> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final int input2_int;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = Integer.MAX_VALUE;
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_int = input2.next ().intValue ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = input2.next ().intValue ();
                            }

                            if ( input2_int == 0 )
                            {
                                final Parameter2.MustNotEqual contract =
                                    new Parameter2.MustNotEqual ( 0 );
                                final Parameter2.MustNotEqual.Violation violation =
                                    contract.violation (
                                        contract,
                                        this,
                                        input2_int );
                                return builder.buildViolation ( violation );
                            }

                            final BigDecimal input1_bd =
                                new BigDecimal ( "" + input1_int );
                            final BigDecimal input2_bd =
                                new BigDecimal ( "" + input2_int );
                            final BigDecimal output =
                                input1_bd.divide ( input2_bd, 32 );
                            // System.out.println ( "        " + input1_int
                            //                      + " / "
                            //                      + input2_int
                            //                      + " = "
                            //                      + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // region ( Integer, Integer ): Region
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, Integer, Region> (
                TopologyTypeClass.Point.REGION.name (),
                integer_type,
                integer_type,
                Region.TYPE,
                new OperationBody2<Integer, Integer, Region> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Region> evaluateBody (
                            Value<Integer> inputs1,
                            Value<Integer> inputs2
                            )
                    {
                        final TypedValueBuilder<Region> builder =
                            Region.TYPE.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<Integer> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final int input2_int;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = Integer.MAX_VALUE;
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_int = input2.next ().intValue ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = input2.next ().intValue ();
                            }

                            final Points<Integer, Integer> endpoints =
                                new Points<Integer, Integer> ( integer_type,
                                                               input1_int,
                                                               input2_int );
                            final Topology<Integer, Integer> topology = null; // !!!!!!!!!!!!!!!!!!!!!!!!!!!
                            final Region output =
                                new Region ( topology,
                                             endpoints );
                            // System.out.println ( "        " + input1_int
                            //                      + " / "
                            //                      + input2_int
                            //                      + " = "
                            //                      + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // subtract ( Integer, Integer ): Integer
        //     (Implements Point.SUBTRACT_MEASURE, Point.SUBTRACT_POINT
        //      and Measure.SUBTRACT)
        // ============================================================
        integer_type_symbol_table.add (
            new StandardOperation2<Integer, Integer, Integer> (
                TopologyTypeClass.Point.SUBTRACT_MEASURE.name (),
                integer_type,
                integer_type,
                integer_type,
                new OperationBody2<Integer, Integer, Integer> ()
                {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Value<Integer> evaluateBody (
                            Value<Integer> inputs1,
                            Value<Integer> inputs2
                            )
                    {
                        final TypedValueBuilder<Integer> builder =
                            integer_type.builder ();
                        final Iterator<Integer> input1 =
                            inputs1.iterator ();
                        final Iterator<Integer> input2 =
                            inputs2.iterator ();
                        while ( input1.hasNext ()
                                || input2.hasNext () )
                        {
                            final int input1_int;
                            final int input2_int;
                            if ( ! input2.hasNext () )
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = 0;
                            }
                            else if ( ! input1.hasNext () )
                            {
                                input1_int = 0;
                                input2_int = input2.next ().intValue ();
                            }
                            else
                            {
                                input1_int = input1.next ().intValue ();
                                input2_int = input2.next ().intValue ();
                            }

                            final Integer output = input1_int - input2_int;
                            // System.out.println ( "        " + input1_int
                            //                   + " - "
                            //                   + input2_int
                            //                   + " = "
                            //                   + output );
                            builder.add ( output );
                        }

                        return builder.build ();
                    }
                } )
            );

        // Create the Integer TypeClassInstance of the toplogy type class.
        final Value<TypeClassInstance> instances =
            topology_type_class.instance ( integer_type );
        System.out.println ( "" + instances );

        if ( ! instances.hasValue ()
             || ! ( instances instanceof One ) )
        {
            throw new Exception ( "Failed to create exactly one instance of "
                                  + topology_type_class
                                  + " for Type "
                                  + integer_type );
        }

        System.out.println ( "OK." );
        System.out.println ( "" );
        System.out.println ( "" );


        // Inputs to the operations we're about to try out.
        final Value<Integer> integer_inputs3 =
            new TypedValueBuilder<Integer> ( integer_type )
                .add ( 7 )
                .add ( 5 )
                .add ( 3 )
                .build ();
        final Value<Integer> integer_inputs2 =
            new TypedValueBuilder<Integer> ( integer_type )
                .add ( 2 )
                .add ( 4 )
                .build ();

        TypeClass point_type_class = null;
        TypeClass measure_type_class = null;
        for ( TypeClass child_type_class
                  : topology_type_class.childTypeClasses () )
        {
            if ( TopologyTypeClass.Point
                     .TYPE_CLASS_ID.equals ( child_type_class.id () ) )
            {
                point_type_class = child_type_class;
            }
            else if ( TopologyTypeClass.Measure
                          .TYPE_CLASS_ID.equals ( child_type_class.id () ) )
            {
                measure_type_class = child_type_class;
            }
        }

        // Try out the type class operations on some integer inputs.
        Value<Integer> integer_input_a;
        Value<Integer> integer_input_b;
        Value<Object> output;
        for ( TypeClassInstance instance : instances )
        {
            System.out.println ( "" );
            System.out.println ( "" + instance );

            for ( TypeClass child_type_class
                      : topology_type_class.childTypeClasses () )
            {
                for ( TypeClassInstance child_instance
                          :instance.child ( child_type_class ) )
                {
                    System.out.println ( "    Child " + child_instance );
                }
            }

            for ( SymbolID<?> required_symbol_id
                      : topology_type_class.requiredSymbolIDs () )
            {
                for ( Symbol actual_symbol
                          : instance.requiredSymbol ( required_symbol_id ) )
                {
                    System.out.println ( "    " + required_symbol_id
                                         + " = "
                                         + actual_symbol
                                         + " ["
                                         + actual_symbol.hashCode ()
                                         + "]" );
                }
            }

            System.out.println ( "=========================================" );

            // Sort:
            for ( Symbol order_symbol
                      : instance.child ( point_type_class )
                            .orThrowUnchecked ()
                            .requiredSymbol ( TopologyTypeClass.Point.ORDER ) )
            {
                final Constant<Order<Integer>> order_constant =
                    (Constant<Order<Integer>>) order_symbol;
                for ( Order<Integer> order : order_constant.value ().await () )
                {
                    integer_input_a = integer_inputs3;
                    System.out.println ( "    Order " + order
                                         + " sort "
                                         + integer_input_a
                                         + " : "
                                         + order.sort ( integer_input_a ) );

                    integer_input_a = integer_inputs2;
                    System.out.println ( "    Order " + order
                                         + " sort "
                                         + integer_input_a
                                         + " : "
                                         + order.sort ( integer_input_a ) );
                }
            }

            for ( SymbolID<?> required_symbol_id
                      : topology_type_class.requiredSymbolIDs () )
            {
                if ( ! ( required_symbol_id instanceof OperationID ) )
                {
                    continue;
                }

                final OperationID<?, ?> operation_id_unknown_output =
                    (OperationID<?, ?>) required_symbol_id;
                if ( operation_id_unknown_output.operationType ().outputType ()
                     == TopologyTypeClass.BIG_DECIMAL_TYPE )
                {
                    // Skip this for now. !!!
                    continue;
                }

                final OperationID<?, Integer> operation_id =
                    (OperationID<?, Integer>) operation_id_unknown_output;

                if ( operation_id.operationType ().inputType ( 1 )
                     == TopologyTypeClass.BIG_DECIMAL_TYPE )
                {
                    // Skip this for now. !!!
                    continue;
                }

                System.out.println ( "    "
                                     + operation_id
                                     + " : " );

                integer_input_a = integer_inputs3;
                integer_input_b = integer_inputs2;
                output =
                    instance.evaluate ( operation_id,
                                        integer_input_a,
                                        integer_input_b );
                System.out.println ( "        "
                                     + operation_id.name ()
                                     + "("
                                     + integer_input_a
                                     + ","
                                     + integer_input_b
                                     + ") = "
                                     + output );

                integer_input_a = integer_inputs2;
                integer_input_b = integer_inputs3;
                output =
                    instance.evaluate ( operation_id,
                                        integer_input_a,
                                        integer_input_b );
                System.out.println ( "        "
                                     + operation_id.name ()
                                     + "("
                                     + integer_input_a
                                     + ","
                                     + integer_input_b
                                     + ") = "
                                     + output );

                integer_input_a = integer_inputs3;
                integer_input_b = integer_inputs3;
                output =
                    instance.evaluate ( operation_id,
                                        integer_input_a,
                                        integer_input_b );
                System.out.println ( "        "
                                     + operation_id.name ()
                                     + "("
                                     + integer_input_a
                                     + ","
                                     + integer_input_b
                                     + ") = "
                                     + output );
            }
        }
    }


    /**
     * !!!
     */
    public static void main (
                             String [] args
                             )
        throws Exception
    {
        final TestTopology tests = new TestTopology ();
        tests.testTopologyTypeClass ();
    }
}
