package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.AbstractTagWithTypeConstraint;
import musaico.foundation.typing.ConstantTermID;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.NamespaceID;
import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.OperationType1;
import musaico.foundation.typing.OperationType2;
import musaico.foundation.typing.StandardType;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TermType;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.typing.typeclass.TypeClass;

import musaico.foundation.value.No;
import musaico.foundation.value.One;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A class of Types which can be used to represent topological points
 * in space.
 * </p>
 *
 *
 * <p>
 * In Java every TypeClass must be Serializable in order to play nicely
 * over RMI.  However be warned that instances of any given TypeClass
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
public class PointTypeClass
    extends TypeClass
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( PointTypeClass.class );

    // !!!
    public static final TypeID<Object> POINT_TYPE_ID =
        new TypeID<Object> ( "#point_type" );

    // !!!
    private static final UnknownType POINT_TYPE =
        new UnknownType ( Namespace.NONE, "#point_type" );

    // The "max" constant is the maximum allowed POINT in this topology.
    public static final ConstantTermID<Object> MAX =
        new ConstantTermID<Object> ( POINT_TYPE, "max" );

    // The "min" constant is the minimum allowed POINT for this topology.
    public static final ConstantTermID<Object> MAX =
        new ConstantTermID<Object> ( POINT_TYPE, "min" );

    // The "order" constant defines how POINTs should be compared and
    // sorted in this topology.
    public static final ConstantTermID<Object> MAX =
        new ConstantTermID<Object> ( POINT_TYPE, "order" );

    // The "origin" constant defines the starting point of new Regions
    // in this topology, such as the centre POINT, or
    // the minimum POINT, and so on.
    public static final ConstantTermID<Object> MAX =
        new ConstantTermID<Object> ( POINT_TYPE, "origin" );

    // OperationType: ( point, measure ): measure.
    private static final OperationType2<Object, Object, Object> OP_PMM =
        new OperationType2<Object, Object, Object> ( POINT_TYPE,
                                                     MEASURE_TYPE,
                                                     MEASURE_TYPE );

    // OperationType: ( point, measure ): point.
    private static final OperationType2<Object, Object, Object> OP_PMP =
        new OperationType2<Object, Object, Object> ( POINT_TYPE,
                                                     MEASURE_TYPE,
                                                     POINT_TYPE );

    // OperationType: ( point ): point.
    private static final OperationType1<Object, Object> OP_PP =
        new OperationType1<Object, Object> ( POINT_TYPE,
                                             POINT_TYPE );

    // !!!
    public static final OperationID<Operation2<Object, Object, Object>, Object> ADD =
        new OperationID<Operation2<Object, Object, Object>, Object> ( "add",
                                                                      OP_PMP );
    // The <code> + ( POINT, MEASURE ) : POINT </code>
    // binary operation.
    private final Operation2<POINT, MEASURE, POINT> add;

    // The <code> % ( POINT, MEASURE ) : MEASURE </code>
    // binary operation.
    private final Operation2<POINT, MEASURE, MEASURE> modulo;

    // The <code> ++ ( POINT ) : POINT </code>
    // unary operation.
    private final Operation1<POINT, POINT> next;

    // The <code> -- ( POINT ) : POINT </code>
    // unary operation.
    private final Operation1<POINT, POINT> previous;

    // The <code> - ( POINT, MEASURE ) : POINT </code>
    // binary operation.
    private final Operation2<POINT, MEASURE, POINT> subtractMeasure;

    // The <code> - ( POINT, POINT ) : MEASURE </code>
    // binary operation.
    private final Operation2<POINT, POINT, MEASURE> subtractPoint;

    // The <code> .. ( POINT, POINT ) : Region </code>
    // binary operation.
    private final Operation2<POINT, POINT, Region<POINT, MEASURE>> to;



    /** The default PointTypeClass.
     *  You can rename this, and add the Symbols required to the
     *  SymbolTable of the renamed PointTypeClass, then
     *  sub-type an existing Type with the renamed PointTypeClass tag
     *  in order to add an existing Type to the TypeClass. */
    public static final PointTypeClass TYPE_CLASS =
        new PointTypeClass ( Namespace.ROOT,
                               "typeclass(point)",
                               new SymbolTable (),
                               new StandardMetadata () );


    /**
     * <p>
     * Creates a new PointTypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
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
     */
    public PointTypeClass (
                           Namespace parent_namespace,
                           String name
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               new SymbolTable () );
    }


    /**
     * <p>
     * Creates a new PointTypeClass with the specified name, to be used
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
     *                     SymbolTable after constructing this
     *                     PointTypeClass, but is expected to
     *                     cease additions to the SymbolTable
     *                     before anyone begins using this tag.
     *                     Must not be null.
     */
    public PointTypeClass (
                           Namespace parent_namespace,
                           String name,
                           SymbolTable symbol_table
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new PointTypeClass with the specified name, to be used
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
     *                     SymbolTable after constructing this
     *                     PointTypeClass, but is expected to
     *                     cease additions to the SymbolTable
     *                     before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this PointTypeClass.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public PointTypeClass (
                           Namespace parent_namespace,
                           String name,
                           SymbolTable symbol_table,
                           Metadata metadata
                           )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        super ( parent_namespace, name,
                new SymbolID<?> []
                {
                    new TypeID ( "#measure_type" ),
                    new TypeID ( "#point_type" ),
                        !!!,

                },
, // No static reqs, only type-dependent.
                symbol_table, metadata );
    }


    /**
     * @see musaico.foundation.typing.typeclass.ypeClass#instance(musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast PointTypeClassInstance<VALUE>.getClass ()
    public <VALUE extends Object>
        ZeroOrOne<PointTypeClassInstance<VALUE>> instance (
                                                             Type<VALUE> type
                                                             )
        throws ReturnNeverNull.Violation
    {
        PointTypeClassInstance<VALUE> instance;
        try
        {
            instance = new PointTypeClassInstance<VALUE> ( this, type );
        }
        catch ( TypingViolation violation )
        {
            final PointTypeClassInstance<VALUE> none =
                new PointTypeClassInstance<VALUE> ( type );
            return new No<PointTypeClassInstance<VALUE>> ( (Class<PointTypeClassInstance<VALUE>>) none.getClass (),
                                                             violation,
                                                             none );
        }

        return new One<PointTypeClassInstance<VALUE>> ( (Class<PointTypeClassInstance<VALUE>>) instance.getClass (),
                                                          instance );
    }


    /**
     * @return The placeholder Type for MEASUREs in this PointTypeClass.
     *         Never null.
     */
    public Type<Object> measureType ()
    {
        !!!
        return this.measureType;
    }


    /**
     * @return The placeholder Type for POINTs in this PointTypeClass.
     *         Never null.
     */
    public Type<Object> pointType ()
    {
        return this.pointType;
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public PointTypeClass rename (
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

        return new PointTypeClass ( parent_namespace,
                                    name,
                                    symbol_table,
                                    new StandardMetadata () );
    }


        // The TermType of all Terms required for this Type:
        final TermType<Object> term_type =
            new TermType<POINT> ( this.pointType );

        // The "max" constant is the maximum allowed POINT in this topology.
        return new TermID<POINT> ( term_type, "max" );

        // The "min" constant is the minimum allowed POINT for this topology.
        return new TermID<POINT> ( term_type, "min" );

        // The "order" constant defines how POINTs should be compared and
        // sorted in this topology.
        return new TermID<POINT> ( term_type, "order" );

        // The "origin" constant defines the starting point of new Regions
        // in this topology, such as the centre POINT, or
        // the minimum POINT, and so on.
        return new TermID<POINT> ( term_type, "origin" );

        // !!!
        final OperationType2<POINT, MEASURE, POINT> op_type =
            new OperationType2<POINT, MEASURE, POINT> (
                point_type,
                measure_type,
                point_type );
        return new OperationID<Operation2<POINT, MEASURE, POINT>, POINT> ( "add", op_type );

!!!!!!!!!!!!!!!!!!!!!!


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "decrement" Symbol.
     * </p>
     *
     * <p>
     * Decrement a MEASURE to get a new, lesser MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <POINT extends Object>
        OperationID<Operation1<MEASURE, MEASURE>, MEASURE> symbolIDDecrement (
            Type<POINT> point_type,
            Type<MEASURE> measure_type
            )
    {
        final OperationType1<MEASURE, MEASURE> op_type =
            new OperationType1<MEASURE, MEASURE> (
                type,
                type );

        return new OperationID<Operation1<MEASURE, MEASURE>, MEASURE> ( "decrement", op_type );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "increment" Symbol.
     * </p>
     *
     * <p>
     * Increment a MEASURE to get a new, greater MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <POINT extends Object>
        OperationID<Operation1<MEASURE, MEASURE>, MEASURE> symbolIDIncrement (
            Type<POINT> point_type,
            Type<MEASURE> measure_type
            )
    {
        final OperationType1<MEASURE, MEASURE> op_type =
            new OperationType1<MEASURE, MEASURE> (
                type,
                type );

        return new OperationID<Operation1<MEASURE, MEASURE>, MEASURE> ( "increment", op_type );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "modulo" Symbol.
     * </p>
     *
     * <p>
     * Modulo divide two MEASURES to get a new MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <POINT extends Object>
        OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> symbolIDModulo (
            Type<POINT> point_type,
            Type<MEASURE> measure_type
            )
    {
        return new OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> ( "modulo", op_type );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "subtract" Symbol.
     * </p>
     *
     * <p>
     * Subtract two MEASURES to get a new MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <POINT extends Object>
        OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> symbolIDSubtractMeasure (
            Type<POINT> point_type,
            Type<MEASURE> measure_type
            )
    {
        return new OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> ( "subtract", op_type );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "subtract" Symbol.
     * </p>
     *
     * <p>
     * Subtract two MEASURES to get a new MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <POINT extends Object>
        OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> symbolIDSubtractPoint (
            Type<POINT> point_type,
            Type<MEASURE> measure_type
            )
    {
        return new OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> ( "subtract", op_type );
    }
}
