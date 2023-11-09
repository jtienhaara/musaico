package musaico.foundation.topology;

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

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.order.Order;

import musaico.foundation.typing.AbstractTag;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.NamespaceID;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypeID;
import musaico.foundation.typing.Typing;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.typing.typeclass.SymbolMustBeRegisteredInTypeClass;
import musaico.foundation.typing.typeclass.TypeClass;
import musaico.foundation.typing.typeclass.TypeClassInstance;
import musaico.foundation.typing.typeclass.TypeMustContainChildTypeClassInstance;
import musaico.foundation.typing.typeclass.TypeMustInstantiateTypeClass;

import musaico.foundation.value.No;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * An instance of a TopologyTypeClass, providing various operations
 * and a POINT type and a MEASURE type.
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
public class Topology<POINT extends Object, MEASURE extends Object>
    extends TypeClassInstance
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Topology.class );


    /** No Topology at all, does not even instantiate its
     *  TopologyTypeClass.NONE.  Not very useful. */
    public static final Topology<Object, Object> NONE =
        new Topology<Object, Object> (); // None constructor.


    // Convenience methods for looking up topology operations and so on.
    private final TopologyOperations<POINT, MEASURE> operations;


    /**
     * <p>
     * Creates a new Topology for the specified Type
     * instantiating the specified TopologyTypeClass.
     * </p>
     *
     * @param type_class The TopologyTypeClass which provides the SymbolIDs
     *                   for this Topology.  Must not be null.
     *
     * @param instance_type The Type which instantiates the Symbols
     *                      required by the TopologyTypeClass.  Must not be null.
     *
     * @param child_type_class_instances For every child TopologyTypeClass of
     *                                   the TopologyTypeClass being
     *                                   instantiated, a Topology must be
     *                                   provided in this lookup.
     *                                   Must not be null.  Must not
     *                                   contain any null keys or values.
     *
     * @param symbols_map The lookup of actual instantiation Symbols
     *                    by the TopologyTypeClass required SymbolIDs
     *                    they instantiate.  Note that the Type of
     *                    each actual Symbol might be different from
     *                    the Type of the SymbolID it instantiates.
     *                    For example, a TopologyTypeClass might require
     *                    a ConstantID "default_value" of UnknownType "x",
     *                    instantiated by a Type by Constant "default_value"
     *                    of Type "string".  The UnknownType "x" and the Type
     *                    "string" are different Types, but the Constant
     *                    instantiates the required ConstantID by
     *                    substitution of the UnknownType.  Must not be null.
     *                    Must not contain any null keys or values.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     Topology, but is expected to cease
     *                     additions to the SymbolTable before anyone
     *                     begins using this Topology.
     *                     Must not be null.
     *
     * @param point_type The Type of POINTs in this Topology.
     *                   Must not be null.
     *
     * @param measure_type The Type of MEASUREs in this Topology.
     *                     Must not be null.
     *
     * <p>
     * Protected.  Used only by TopologyTypeClass, and by derived
     * implementations of TopologyTypeClass and Topology.
     * </p>
     */
    protected Topology (
                        TopologyTypeClass type_class,
                        Type<?> instance_type,
                        Map<TypeClass, TypeClassInstance> child_type_class_instances,
                        Map<SymbolID<?>, Symbol> symbols_map,
                        SymbolTable symbol_table,
                        Type<POINT> point_type,
                        Type<MEASURE> measure_type
                        )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               Parameter4.MustContainNoNulls.Violation,
               TypeMustContainChildTypeClassInstance.Violation,
               TypeMustInstantiateTypeClass.Violation,
               SymbolMustBeRegisteredInTypeClass.Violation
    {
        super ( type_class,
                instance_type,
                child_type_class_instances,
                symbols_map,
                symbol_table );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point_type, measure_type );

        symbol_table.set ( TopologyTypeClass.POINT_TYPE_ID,
                           point_type );

        symbol_table.set ( TopologyTypeClass.MEASURE_TYPE_ID,
                           measure_type );

        this.operations = this.createOperations ();

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               this.operations );
    }


    /**
     * <p>
     * Creates a new Topology for the specified Type,
     * not actually instantiating the specified TopologyTypeClass.
     * </p>
     *
     * <p>
     * Used to create "none" Topologies.
     * </p>
     *
     * @param type_class The TopologyTypeClass instantiated by this
     *                   "none" Topology, such as TopologyTypeClass.NONE.
     *                   Must not be null.
     *
     * @param instance_type The Type which does not provide the Symbols for
     *                      this Topology.  Must not be null.
     */
    public Topology (
                     TopologyTypeClass type_class,
                     Type<?> instance_type
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type_class,
                instance_type );

        this.symbolTable ().set ( TopologyTypeClass.POINT_TYPE_ID,
                                  Type.NONE );

        this.symbolTable ().set ( TopologyTypeClass.MEASURE_TYPE_ID,
                                  Type.NONE );

        this.operations = this.createOperations ();

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               this.operations );
    }


    /**
     * <p>
     * Creates Topology.NONE.
     * </p>
     */
    private Topology ()
    {
        super (); // None constructor.

        // For Topology.NONE, every get method returns the none result.
        this.operations = this.createOperations ();

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               this.operations );
    }


    /**
     * <p>
     * Creates the convenience TopologyOperations to make it easier
     * to access add (), subtract (), and so on topology operations.
     * </p>
     *
     * <p>
     * This method can be overridden by derived classes to extend
     * the range of topological operations available.
     * </p>
     *
     * @return A newly created TopologyOperations for this Topology.
     *         Never null.
     */
    protected TopologyOperations<POINT, MEASURE> createOperations ()
    {
        return new TopologyOperations<POINT, MEASURE> ( this );
    }


    /**
     * @return The TypeClassInstance describing how the MEASURE Type
     *         in this Topology instantiates the measure TypeClass.
     *         Never null.
     */
    public final TypeClassInstance measure ()
        throws ReturnNeverNull.Violation
    {
        final TypeClass measure_type_class =
            this.typeClass ().measureTypeClass ();
        final TypeClassInstance measure =
            this.child ( measure_type_class ).orNull ();

        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                 measure );

        return measure;
    }


    /**
     * @return The Type describing MEASUREs in this Topology.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<MEASURE>.
    public final Type<MEASURE> measureType ()
        throws ReturnNeverNull.Violation
    {
        final Type<MEASURE> measure_type = (Type<MEASURE>)
            this.symbolTable ().symbol ( TopologyTypeClass.MEASURE_TYPE_ID )
                               .orNull ();
        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  measure_type );
        return measure_type;
    }


    /**
     * @return The TopologyOperations convenience methods for this
     *         Topology.  Provides quick access to the add, subtract
     *         and so on operations, without calling
     *         <code> Topology.requiredSymbol ( ... ) </code.
     *         Never null.
     */
    public TopologyOperations<POINT, MEASURE> operations ()
    {
        return this.operations;
    }


    /**
     * @return The TypeClassInstance describing how the POINT Type
     *         in this Topology instantiates the point TypeClass.
     *         Never null.
     */
    public final TypeClassInstance point ()
        throws ReturnNeverNull.Violation
    {
        final TypeClass point_type_class =
            this.typeClass ().pointTypeClass ();
        final TypeClassInstance point =
            this.child ( point_type_class ).orNull ();

        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  point );

        return point;
    }


    /**
     * @return The Order of POINTs in this Topology.  Never null.
     */
    public final Order<Object> pointOrder ()
        throws ReturnNeverNull.Violation
    {
        final Order<Object> order =
            this.pointType ()
                .symbol ( TopologyTypeClass.Point.ORDER )
                .orNone () // Returns the Constant<Order>.
                .value ()  // Returns the Synchronous<Order>.
                .orNone (); // Returns the Order.

        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  order );
        return order;
    }


    /**
     * @return The Type describing POINTs in this Topology.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<POINT>.
    public final Type<POINT> pointType ()
        throws ReturnNeverNull.Violation
    {
        final Type<POINT> point_type = (Type<POINT>)
            this.symbolTable ().symbol ( TopologyTypeClass.POINT_TYPE_ID )
                               .orNull ();
        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  point_type );
        return point_type;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final Topology<POINT, MEASURE> rename (
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
    @SuppressWarnings("unchecked") // Cast Tag - TopologyTypeClass.
    public Topology<POINT, MEASURE> rename (
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

        final TopologyTypeClass type_class = (TopologyTypeClass)
            symbol_table.symbol ( TYPE_CLASS_ID )
                .orDefault ( TopologyTypeClass.NONE );

        final Type<?> instance_type = symbol_table.symbol ( TYPE_ID )
                                                  .orNone ();

        final Type<POINT> point_type = this.pointType ();
        final Type<MEASURE> measure_type = this.measureType ();

        try
        {
            return new Topology<POINT, MEASURE> ( type_class,
                                                  instance_type,
                                                  this.childTypeClassInstances (),
                                                  this.requiredSymbols (),
                                                  symbol_table,
                                                  point_type,
                                                  measure_type );
        }
        catch ( TypingViolation violation )
        {
            final ReturnNeverNull.Violation violation2 =
                ReturnNeverNull.CONTRACT.violation ( ReturnNeverNull.CONTRACT,
                                                     this,
                                                     "rename ( \""
                                                     + name
                                                     + "\", symbol_table )" );
            violation2.initCause ( violation );

            throw violation2;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "Topology { instanceType: " + this.instanceType ().id ().name ()
            + " }";
    }


    /**
     * @return The TopologyTypeClass which provides the SymbolIDs
     *         for this Topology.  Never null.
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Tag - TopologyTypeClass.
    public TopologyTypeClass typeClass ()
        throws ReturnNeverNull.Violation
    {
        final TopologyTypeClass type_class = (TopologyTypeClass)
            this.symbolTable ().symbol ( TYPE_CLASS_ID )
                .orNull ();

        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  type_class );

        return type_class;
    }
}
