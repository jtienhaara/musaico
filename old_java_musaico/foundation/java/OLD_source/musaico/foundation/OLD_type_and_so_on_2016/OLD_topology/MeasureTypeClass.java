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
 * A class of Types which can be used to represent topological sizes
 * or distances, such as the size of a Region, or the distance between
 * two topological points.
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
public class MeasureTypeClass
    extends TypeClass
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MeasureTypeClass.class );


    /** The default MeasureTypeClass.
     *  You can rename this, and add the Symbols required to the
     *  SymbolTable of the renamed MeasureTypeClass, then
     *  sub-type an existing Type with the renamed MeasureTypeClass tag
     *  in order to add an existing Type to the TypeClass. */
    public static final MeasureTypeClass TYPE_CLASS =
        new MeasureTypeClass ( Namespace.ROOT,
                               "typeclass(measure)",
                               new SymbolTable (),
                               new StandardMetadata () );

    /** BigDecimals are required in some topology operations.
     *  This is a bare-bones Type. */
    public static final Type<BigDecimal> BIG_DECIMAL_TYPE =
        new StandardType<BigDecimal> ( Namespace.ROOT,   // parent_namespace
                                       "BigDecimal",     // raw_type_name
                                       BigDecimal.class, // value_class
                                       BigDecimal.ZERO,  // none
                                       new SymbolTable () ); // symbol_table


    /**
     * <p>
     * Creates a new MeasureTypeClass with the specified name, to be used
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
     * @param required_symbol_ids All SymbolIDs which
     *                            must be provided by every Type in this
     *                            MeasureTypeClass.  Must not be null.
     *                            Must not contain any null elements.
     */
    public MeasureTypeClass (
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
     * Creates a new MeasureTypeClass with the specified name, to be used
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
     * @param required_symbol_ids All SymbolIDs which
     *                            must be provided by every Type in this
     *                            MeasureTypeClass.  Must not be null.
     *                            Must not contain any null elements.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     MeasureTypeClass, but is expected to
     *                     cease additions to the SymbolTable
     *                     before anyone begins using this tag.
     *                     Must not be null.
     */
    public MeasureTypeClass (
                             Namespace parent_namespace,
                             String name,
                             SymbolTable symbol_table
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               required_symbol_ids,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new MeasureTypeClass with the specified name, to be used
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
     * @param required_symbol_ids All SymbolIDs which
     *                            must be provided by every Type in this
     *                            MeasureTypeClass.  Must not be null.
     *                            Must not contain any null elements.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     MeasureTypeClass, but is expected to
     *                     cease additions to the SymbolTable
     *                     before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this MeasureTypeClass.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public MeasureTypeClass (
                             Namespace parent_namespace,
                             String name,
                             SymbolTable symbol_table,
                             Metadata metadata
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        super ( parent_namespace, name,
                new SymbolID<?> [ 0 ], // No static reqs, only type-dependent.
                symbol_table, metadata );
    }


    /**
     * @see musaico.foundation.typing.typeclass.ypeClass#instance(musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast MeasureTypeClassInstance<MEASURE>.getClass ()
    public <MEASURE extends Object>
        ZeroOrOne<MeasureTypeClassInstance<MEASURE>> instance (
            Type<MEASURE> measure_type
            )
        throws ReturnNeverNull.Violation
    {
        MeasureTypeClassInstance<MEASURE> instance;
        try
        {
            instance =
                new MeasureTypeClassInstance<MEASURE> ( this, measure_type );
        }
        catch ( TypingViolation violation )
        {
            final MeasureTypeClassInstance<MEASURE> none =
                new MeasureTypeClassInstance<MEASURE> ( measure_type );
            return new No<MeasureTypeClassInstance<MEASURE>> (
                (Class<MeasureTypeClassInstance<MEASURE>>) none.getClass (),
                violation,
                none );
        }

        return new One<MeasureTypeClassInstance<MEASURE>> (
            (Class<MeasureTypeClassInstance<MEASURE>>) instance.getClass (),
            instance );
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public MeasureTypeClass rename (
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

        return new MeasureTypeClass ( parent_namespace,
                                      name,
                                      symbol_table,
                                      new StandardMetadata () );
    }


    /**
     * @see musaico.foundation.typing.typeclass.TypeClass#requiredSymbolIDs(musaico.foundation.typing.Type, musaico.foundation.typing.Type[])
     */
    @Override
    public <MEASURE extends Object>
        SymbolID<?> [] requiredSymbolIDs (
                                          Type<MEASURE> measure_type,
                                          Type<?> ... dependent_types
                                          )
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        // We have no static symbol id requirements, so don't bother
        // calling super.requiredSymbolIDs ( measure_type ).

        return new SymbolID<?> []
            {
                // Unit constant, the smallest unit of MEASURE:
                this.symbolIDUnit ( measure_type ),

                // How to add two MEASURES to get a new MEASURE:
                this.symbolIDAdd ( measure_type ),

                // How to decrement a MEASURE to get a new, lesser MEASURE:
                this.symbolIDDecrement ( measure_type ),

                // How to divide two MEASURES to get a new MEASURE:
                this.symbolIDDivide ( measure_type ),

                // How to increment a MEASURE to get a new, greater MEASURE:
                this.symbolIDIncrement ( measure_type ),

                // How to modulo divide two MEASURES to get a new MEASURE:
                this.symbolIDModulo ( measure_type ),

                // How to multiply MEASURE * BigDecimal to get a new MEASURE:
                this.symbolIDMultiply ( measure_type ),

                // How to take the ratio between two MEASURES as a BigDecimal:
                this.symbolIDRatio ( measure_type ),

                // How to subtract two MEASURES to get a new MEASURE:
                this.symbolIDSubtract ( measure_type )
            };
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "unit" Symbol.
     * </p>
     *
     * <p>
     * The unit constant is the smallest unit of MEASURE,
     * such as an integer 1, or 1 cm, or 8 bits, and so on.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <MEASURE extends Object>
        TermID<MEASURE> symbolIDUnit (
                                      Type<MEASURE> type
                                      )
    {
        // The TermType of all Terms required for this Type:
        final TermType<MEASURE> term_type = new TermType<MEASURE> ( type );

        return new TermID<MEASURE> ( term_type, "unit" );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "add" Symbol.
     * </p>
     *
     * <p>
     * Add two MEASURES to get a new MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <MEASURE extends Object>
        OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> symbolIDAdd (
            Type<MEASURE> type
            )
    {
        final OperationType2<MEASURE, MEASURE, MEASURE> op_type =
            new OperationType2<MEASURE, MEASURE, MEASURE> (
                type,
                type,
                type );

        return new OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> ( "add", op_type );
    }


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
    public final <MEASURE extends Object>
        OperationID<Operation1<MEASURE, MEASURE>, MEASURE> symbolIDDecrement (
            Type<MEASURE> type
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
     * the "divide" Symbol.
     * </p>
     *
     * <p>
     * Divide two MEASURES to get a new MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <MEASURE extends Object>
        OperationID<Operation2<MEASURE, BigDecimal, MEASURE>, MEASURE> symbolIDDivide (
            Type<MEASURE> type
            )
    {
        final OperationType2<MEASURE, MEASURE, MEASURE> op_type =
            new OperationType2<MEASURE, MEASURE, MEASURE> (
                type,
                type,
                type );
        return new OperationID<Operation2<MEASURE, BigDecimal, MEASURE>, MEASURE> ( "divide", op_type );
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
    public final <MEASURE extends Object>
        OperationID<Operation1<MEASURE, MEASURE>, MEASURE> symbolIDIncrement (
            Type<MEASURE> type
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
    public final <MEASURE extends Object>
        OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> symbolIDModulo (
            Type<MEASURE> type
            )
    {
        final OperationType2<MEASURE, MEASURE, MEASURE> op_type =
            new OperationType2<MEASURE, MEASURE, MEASURE> (
                type,
                type,
                type );

        return new OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> ( "modulo", op_type );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "multiply" Symbol.
     * </p>
     *
     * <p>
     * Multiply MEASURE * BigDecimal to get a new MEASURE.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <MEASURE extends Object>
        OperationID<Operation2<MEASURE, BigDecimal, MEASURE>, MEASURE> symbolIDMultiply (
            Type<MEASURE> type
            )
    {
        final OperationType2<MEASURE, BigDecimal, MEASURE> op_type =
            new OperationType2<MEASURE, BigDecimal, MEASURE> (
                type,
                BIG_DECIMAL_TYPE,
                type );

        return new OperationID<Operation2<MEASURE, BigDecimal, MEASURE>, MEASURE> ( "multiply", op_type );
    }


    /**
     * <p>
     * Returns the SymbolID required for the specified Type to provide
     * the "ratio" Symbol.
     * </p>
     *
     * <p>
     * Take the ratio between two MEASURES as a BigDecimal.
     * </p>
     *
     * @param type The Type which must provide a Symbol corresponding
     *             to the SymbolID returned by this method.  Must not be null.
     *
     * @return The required SymbolID for the specified Type.
     *         Never null.
     */
    public final <MEASURE extends Object>
        OperationID<Operation2<MEASURE, MEASURE, BigDecimal>, MEASURE> symbolIDRatio (
            Type<MEASURE> type
            )
    {
        final OperationType2<MEASURE, MEASURE, BigDecimal> op_type =
            new OperationType2<MEASURE, MEASURE, BigDecimal> (
                type,
                type,
                BIG_DECIMAL_TYPE );

        return new OperationID<Operation2<MEASURE, MEASURE, BigDecimal>, MEASURE> ( "ratio", op_type );
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
    public final <MEASURE extends Object>
        OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> symbolIDSubtract (
            Type<MEASURE> type
            )
    {
        final OperationType2<MEASURE, MEASURE, MEASURE> op_type =
            new OperationType2<MEASURE, MEASURE, MEASURE> (
                type,
                type,
                type );

        return new OperationID<Operation2<MEASURE, MEASURE, MEASURE>, MEASURE> ( "subtract", op_type );
    }
}
