package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.Origin;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.StandardOrigin;

import musaico.foundation.value.No;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * The Type describing an Expression.
 * </p>
 *
 * <p>
 * An ExpressionTermType describes the actual Expression,
 * its <code> valueType () </code> describes the value Type of
 * the Expression (such as a string Type, or an integer Type, and so on),
 * and its <code> operationType () </code> describes the Operation which
 * will be executed whenever an Expression of this ExpressionType
 * is evaluated (such as "(string, int): string", or "(int): int",
 * and so on).
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
public class ExpressionTermType<VALUE extends Object>
    extends AbstractTermType<Expression<VALUE>, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( ExpressionTermType.class );


    /**
     * <p>
     * Generates the "none" Expression for an ExpressionTermType,
     * avoiding infinite recursion at constructor time.
     * </p>
     */
    public static class NoneExpressionTermGenerator<NONE_VALUE extends Object>
        implements NoneGenerator<Expression<NONE_VALUE>>, Serializable
    {
        private static final long serialVersionUID =
            ExpressionTermType.serialVersionUID;

        // Lock critical sections on this token:
        private final Serializable lock = new String ();

        // The value Type for Expressions of this ExpressionTermType.
        private final Type<NONE_VALUE> valueType;

        // Generated once, return the same value after that.
        // MUTABLE.
        private Expression<NONE_VALUE> none = null;


        /**
         * <p>
         * Creates a new NoneGenerator for an ExpressionTermType.
         * </p>
         *
         * @param value_type The value Type of the ExpressionTermType.
         *                   Used to create the "none" value for the
         *                   "none" Expression.  Must not be null.
         */
        public NoneExpressionTermGenerator (
                                            Type<NONE_VALUE> value_type
                                            )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   value_type );

            this.valueType = value_type;
        }

        /**
         * @see musaico.foundation.VALUE.NoneGenerator#none()
         */
        public final Expression<NONE_VALUE> none ()
            throws ReturnNeverNull.Violation
        {
            synchronized ( this.lock )
            {
                if ( this.none == null )
                {
                    final OperationType1<NONE_VALUE, NONE_VALUE>
                        operation_type =
                            new OperationType1<NONE_VALUE, NONE_VALUE> (
                                this.valueType,
                                this.valueType );
                    final AlwaysFail<NONE_VALUE> always_fail =
                        new AlwaysFail<NONE_VALUE> ( "none",
                                                     operation_type );
                    final Unregistered unregistered_id =
                        new Unregistered ( SymbolID.NONE );
                    final TypingViolation violation =
                        SymbolMustBeRegistered.CONTRACT.violation ( this,
                                                                    unregistered_id );
                    final No<NONE_VALUE> no_value =
                        this.valueType.noValue ( violation );
                    final Term<NONE_VALUE> input =
                        this.valueType.instance ( no_value );
                    this.none =
                        new Expression<NONE_VALUE> ( always_fail, input );
                }

                return this.none;
            }
        }
    }




    /**
     * <p>
     * Creates a new ExpressionTermType with the specified OperationType,
     * an empty SymbolTable, and StandardMetadata,
     * with an automatically generated name for this new
     * ExpressionTermType.
     * </p>
     *
     * @param operation_type The Type of Operation that will be executed
     *                       whenever an Expression of the new ExpressionType
     *                       is evaluated.  For example, a
     *                       "(string, int): string" OperationType,
     *                       or an "(int): int" OperationType, and so on.
     *                       Must not be null.
     */
    ExpressionTermType (
                        OperationType<?, VALUE> operation_type
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( operation_type,
               new SymbolTable () );
    }


    /**
     * <p>
     * Creates a new ExpressionTermType with the specified OperationType,
     * and SymbolTable, and StandardMetadata,
     * with an automatically generated name for this new
     * ExpressionTermType.
     * </p>
     *
     * @param operation_type The Type of Operation that will be executed
     *                       whenever an Expression of the new ExpressionType
     *                       is evaluated.  For example, a
     *                       "(string, int): string" OperationType,
     *                       or an "(int): int" OperationType, and so on.
     *                       Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     */
    ExpressionTermType (
                        OperationType<?, VALUE> operation_type,
                        SymbolTable symbol_table
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( operation_type,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new ExpressionTermType with the specified OperationType,
     * SymbolTable and Metadata,
     * with an automatically generated name for this new
     * ExpressionTermType.
     * </p>
     *
     * @param operation_type The Type of Operation that will be executed
     *                       whenever an Expression of the new ExpressionType
     *                       is evaluated.  For example, a
     *                       "(string, int): string" OperationType,
     *                       or an "(int): int" OperationType, and so on.
     *                       Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this ExpressionTermType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    ExpressionTermType (
                        OperationType<?, VALUE> operation_type,
                        SymbolTable symbol_table,
                        Metadata metadata
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "expression "
               + ( operation_type == null
                   ? null
                   : operation_type.id ().name () ),
               operation_type,
               symbol_table,
               metadata );
    }


    /**
     * <p>
     * Creates a new ExpressionTermType with the specified type name,
     * OperationType, SymbolTable and Metadata.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "expression doSomething(1,2,3)"
     *                      or "constant 5", and so on.
     *                      Must not be null.
     *
     * @param operation_type The Type of Operation that will be executed
     *                       whenever an Expression of the new ExpressionType
     *                       is evaluated.  For example, a
     *                       "(string, int): string" OperationType,
     *                       or an "(int): int" OperationType, and so on.
     *                       Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this ExpressionTermType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public ExpressionTermType (
                               String raw_type_name,
                               OperationType<?, VALUE> operation_type,
                               SymbolTable symbol_table,
                               Metadata metadata
                               )
        throws ParametersMustNotBeNull.Violation
    {
        super ( raw_type_name,
                new ExpressionTermType.NoneExpressionTermGenerator<VALUE> (
                    operation_type == null
                        ? null
                        : operation_type.outputType () ),
                operation_type == null         // value_type
                    ? null
                    : operation_type.outputType (),
                symbol_table,
                metadata );

        final TypeID operation_type_id = new TypeID ( "#operation",
                                                      Visibility.PRIVATE );
        symbol_table.set ( operation_type_id,
                           operation_type );
    }


    /**
     * @return The OperationType executed whenever an Expression
     *         of this ExpressionTermType is evaluated,
     *         such as "(string, int): string" OperationType,
     *         or an "(int): int" OperationType, and so on.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<?> - OperationType<?, VALUE>
        public final OperationType<?, VALUE> operationType ()
    {
        final TypeID operation_type_id = new TypeID ( "#operation",
                                                      Visibility.PRIVATE );
        final Type<?> operation_type =
            this.symbolTable ().symbol ( operation_type_id )
                .orNone ();

        return (OperationType<?, VALUE>) operation_type;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final ExpressionTermType<VALUE> rename (
                                                   String name
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Term#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public ExpressionTermType<VALUE> rename (
                                             String name,
                                             SymbolTable symbol_table
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name, symbol_table );

        symbol_table.addAll ( this.symbolTable () );

        return new ExpressionTermType<VALUE> ( name,
                                               this.operationType (),
                                               symbol_table,
                                               this.metadata ().renew () );
    }


    // Ugly hack for @*%# generics.
    private static class Silly<TERM_CLASS>
    {
        @SuppressWarnings("unchecked")
        public Class<TERM_CLASS> getTermClass ()
        {
            return (Class<TERM_CLASS>) Expression.class;
        }
    }

    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    public final Class<Expression<VALUE>> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return new Silly<Expression<VALUE>> ().getTermClass ();
    }
}
