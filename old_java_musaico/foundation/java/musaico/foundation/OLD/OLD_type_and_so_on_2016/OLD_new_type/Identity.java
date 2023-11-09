package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;


/**
 * <p>
 * Returns every input Value as-is.
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
public class Identity<OBJECT extends Object>
    extends StandardOperation1<OBJECT, OBJECT>
    implements OperationBody1<OBJECT, OBJECT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Identity.class );


    /**
     * <p>
     * Creates a new Identity which returns every input as-is.
     * </p>
     *
     * @param type The Type of objects for both input and output.
     *             Must not be null.
     */
    public Identity (
                     Type<OBJECT> type
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new OperationID<Operation1<OBJECT, OBJECT>, OBJECT> (
                    Operation.CAST, // Default to cast operation id.
                    new OperationType1<OBJECT, OBJECT> ( type, type ) ),
                null ); // body = this.
    }


    /**
     * <p>
     * Creates a new Identity which returns every input as-is.
     * </p>
     *
     * @param name The name of the Identity Operation.  This will be used to
     *             construct an OperationID which is unique in each
     *             SymbolTable.  Must not be null.
     *
     * @param type The Type of objects for both input and output.
     *             Must not be null.
     */
    public Identity (
                     String name,
                     Type<OBJECT> type
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new OperationID<Operation1<OBJECT, OBJECT>, OBJECT> (
                    name,
                    new OperationType1<OBJECT, OBJECT> ( type, type ) ),
                null ); // body = this.
    }


    /**
     * <p>
     * Creates a new Identity which returns every input as-is,
     * for different source and target Types (for example
     * where both Types have the same base Type, but one has
     * additional Tags to modify its behaviour).
     * </p>
     *
     * @param source_type The Type of input objects.
     *                    Must not be null.
     *
     * @param target_type The Type of output objects.
     *                    Must not be null.
     */
    public Identity (
                     Type<OBJECT> source_type,
                     Type<OBJECT> target_type
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new OperationID<Operation1<OBJECT, OBJECT>, OBJECT> (
                    Operation.CAST, // Default to cast operation id.
                    new OperationType1<OBJECT, OBJECT> ( source_type,
                                                         target_type ) ),
                null ); // body = this.
    }


    /**
     * <p>
     * Creates a new Identity which returns every input as-is,
     * for different source and target Types (for example
     * where both Types have the same base Type, but one has
     * additional Tags to modify its behaviour).
     * </p>
     *
     * @param name The name of this Identity Operation.  This will be used
     *             to construct an OperationID which is unique in each
     *             SymbolTable.  Must not be null.
     *
     * @param source_type The Type of input objects.
     *                    Must not be null.
     *
     * @param target_type The Type of output objects.
     *                    Must not be null.
     */
    public Identity (
                     String name,
                     Type<OBJECT> source_type,
                     Type<OBJECT> target_type
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new OperationID<Operation1<OBJECT, OBJECT>, OBJECT> (
                    name,
                    new OperationType1<OBJECT, OBJECT> ( source_type,
                                                         target_type ) ),
                null ); // body = this.
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
     */
    @Override
    public final Value<OBJECT> evaluateBody (
                                             Value<OBJECT> source
                                             )
    {
        return source;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Identity<OBJECT> rename (
                                    String name
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Identity<OBJECT> ( name,
                                      this.input1Type (),
                                      this.outputType () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<OBJECT>.
    public Identity<OBJECT> retype (
                                    String name,
                                    OperationType<? extends Operation<OBJECT>, OBJECT> type
                                    )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Identity<OBJECT> ( name,
                                      (Type<OBJECT>) type.inputType ( 0 ),
                                      type.outputType () );
    }
}
