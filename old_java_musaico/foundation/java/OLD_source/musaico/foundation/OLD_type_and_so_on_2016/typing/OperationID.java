package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;


/**
 * <p>
 * A unique Operation identifier within a SymbolTable.
 * </p>
 *
 * <p>
 * Each OperationID is a signature comprising a name followed by the
 * Types of input and output to/from the operation.  Curried operations
 * and any other type of Operation which allows multiple arguments
 * will each have a signature with more than one input Type.
 * </p>
 *
 * <p>
 * Operation signatures are loosely based on the notation used to
 * describe methods in UML, such as "method(parameter):result".
 * This decision (over, say, a more functional signature, such
 * as "operation:input-&gt;output") was completely arbitrary.
 * </p>
 *
 * <p>
 * For example, a "plusplus" OperationID which accepts the "number" Type
 * and outputs the "number" Type would have signature
 * "plusplus(number):number".  A curried "add" Operation with 2 inputs
 * might have an OperationID with signature "add(number,number):number".
 * And so on.
 * </p>
 *
 * <p>
 * Each signature is unique, so "add(number,number):number" is not
 * the same as "add(string,string):string", nor is either signature
 * the same as "add(string,string):number", and so on.  Different
 * input Types and/or different output Types imply different
 * OperationIDs.
 * </p>
 *
 *
 * <p>
 * In Java every SymbolID must be Serializable in order to
 * play nicely with RMI.
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
public class OperationID<OPERATION extends Operation<OUTPUT>, OUTPUT>
    extends SymbolID<OPERATION>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( OperationID.class );


    /** The OperationType of this OperationID.  Herein lies
     *  the input Type(s) for any Operation with this ID, the output
     *  Type, and possibly additional Tags and so on. */
    private final OperationType<OPERATION, OUTPUT> operationType;


    /**
     * <p>
     * Creates a new OperationID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param name The String base name of this identifier,
     *             such as "doSomething".  Must not be null.
     *
     * @param operation_type The OperationType of this OperationID,
     *                       which specifies one or more input Type(s),
     *                       one output Type, and possibly other Tags
     *                       and Symbols to provide execution hints and
     *                       so on.  Must not be null.
     */
    @SuppressWarnings("unchecked") // OP_TYPE --> SYMBOL_TYPE
    public OperationID (
                        String name,
                        OperationType<OPERATION, OUTPUT> operation_type
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               operation_type,
               Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new OperationID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param name The String base name of this identifier,
     *             such as "doSomething".  Must not be null.
     *
     * @param operation_type The OperationType of this OperationID,
     *                       which specifies one or more input Type(s),
     *                       one output Type, and possibly other Tags
     *                       and Symbols to provide execution hints and
     *                       so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    @SuppressWarnings("unchecked") // OP_TYPE --> SYMBOL_TYPE
    public OperationID (
                        String name,
                        OperationType<OPERATION, OUTPUT> operation_type,
                        Visibility visibility
                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( operation_type, // The symbol type represented by this ID.
                name,
                visibility );

        this.operationType = operation_type;
    }


    /**
     * @return The OperationType of the Operation identified by this ID.
     *         Contains one or more input Type(s) to the Operation, one
     *         output Type, and optionally Tags and other symbols, for example
     *         to be used as execution hints ("long operation", "short
     *         operation", and so on).  Never null.
     */
    public final OperationType<OPERATION, OUTPUT> operationType ()
        throws ReturnNeverNull.Violation
    {
        return this.operationType;
    }


    /**
     * @see musaico.foundation.typing.SymbolID#rename(java.lang.String, musaico.foundation.typing.Visibility)
     */
    @Override
    public OperationID<OPERATION, OUTPUT> rename (
                                                  String name,
                                                  Visibility visibility
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new OperationID<OPERATION, OUTPUT> ( name,
                                                    this.operationType,
                                                    visibility );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        // Type and name are in the reverse order from regular SymbolID.
        // SymbolID: type "name"
        // OperationID: nametype e.g. name(int, string):string
        return  this.name () + this.type ().id ().name ();
    }
}
