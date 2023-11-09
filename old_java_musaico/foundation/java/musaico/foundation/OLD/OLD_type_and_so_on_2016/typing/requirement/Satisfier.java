package musaico.foundation.typing.requirement;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBody1;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.TypingViolation;

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
 * @see musaico.foundation.typing.requirement.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.requirement.MODULE#LICENSE
 */
public class Satisfier<OUTPUT, KEY>
    extends StandardOperation1<OUTPUT, OUTPUT>
    implements OperationBody1<OUTPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Satisfier.class );


    private final Class<KEY> requirementClass;
    private final KEY requiredValue;

    public Satisfier ( Type<OUTPUT> type,
                       Class<KEY> requirement_class,
                       KEY required_value )
    {
        this ( "satisfier<"
               + ClassName.of ( requirement_class )
               + ">",
               type,
               requirement_class,
               required_value );
    }

    public Satisfier ( String name,
                       Type<OUTPUT> type,
                       Class<KEY> requirement_class,
                       KEY required_value )
    {
        super ( name,
                type,
                type,
                null ); // body = this.

        this.requirementClass = requirement_class;
        this.requiredValue = required_value;
    }

    @Override   
    public final Value<OUTPUT> evaluateBody (
                                             Value<OUTPUT> output
                                             )
    {
        if ( ! ( output instanceof Unsatisfied ) )
        {
            // Wasted Satisfier call, no requirement.
            return output;
        }

        final Unsatisfied<OUTPUT> unsatisfied =
            (Unsatisfied<OUTPUT>) output;

        final Value<OUTPUT> satisfied_output =
            unsatisfied.satisfy ( this.requirementClass, this.requiredValue );

        return satisfied_output;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Satisfier<OUTPUT, KEY> rename (
                                          String name
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new Satisfier<OUTPUT, KEY> ( name,
                                            this.outputType (),
                                            this.requirementClass,
                                            this.requiredValue );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Satisfier<OUTPUT, KEY> retype (
                                          String name,
                                          OperationType<? extends Operation<OUTPUT>, OUTPUT> type
                                          )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Satisfier<OUTPUT, KEY> ( name,
                                            type.outputType (),
                                            this.requirementClass,
                                            this.requiredValue );
    }
}
