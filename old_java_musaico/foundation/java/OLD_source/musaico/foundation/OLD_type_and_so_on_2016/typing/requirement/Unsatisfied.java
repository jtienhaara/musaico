package musaico.foundation.typing.requirement;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.domains.ClassName;

import musaico.foundation.typing.Operation;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.value.No;
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
public final class Unsatisfied<OUTPUT extends Object>
    extends No<OUTPUT>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    private final Serializable lock = new String ();
    private final Operation<OUTPUT> operation;
    private final List<Value<Object>> inputs;
    private final LinkedHashMap<Class<?>, Requirement<?>> unsatisfiedRequirements;
    private Value<OUTPUT> value =
        null;  // Operation has not yet been invoked.

    public Unsatisfied (
                        Operation<OUTPUT> operation,
                        List<Value<Object>> inputs,
                        Set<Requirement<?>> requirements
                        )
    {
        super ( operation.outputType ().valueClass (),
                new TypingViolation (
                    requirements.iterator ().next (), //contract
                    requirements.iterator ().next (), //plaintiff
                    requirements ) ); // inspectable_data
        this.operation = operation;
        this.inputs = inputs;
        this.unsatisfiedRequirements =
            new LinkedHashMap<Class<?>, Requirement<?>> ();
        for ( Requirement<?> requirement : requirements )
        {
            this.unsatisfiedRequirements.put ( requirement.requirementClass (),
                                               requirement );
        }
    }

    @SuppressWarnings("unchecked") // Cast to Requirement<KEY>.
    public <KEY extends Object>
        Value<OUTPUT> satisfy ( Class<KEY> requirement_class,
                                KEY required_value )
    {
        final Requirement<KEY> requirement;
        synchronized ( this.lock )
        {
            requirement = (Requirement<KEY>)
                this.unsatisfiedRequirements.get ( requirement_class );
        }

        if ( requirement != null )
        {
            final SatisfactionAttempt<KEY> attempt =
                new SatisfactionAttempt<KEY> ( this.operation,
                                               required_value );
            if ( requirement.filter ( attempt ).isKept () )
            {
                synchronized ( this.lock )
                {
                    this.unsatisfiedRequirements.remove ( requirement_class );
                }
            }
            else
            {
                final TypingViolation violation =
                    requirement.violation ( this, // plaintiff
                                            attempt ); // inspectable

                final Type<OUTPUT> output_type =
                    this.operation.outputType ();
                return output_type.noValue ( violation );
            }
        }

        synchronized ( this.lock )
        {
            if ( this.unsatisfiedRequirements.size () == 0 )
            {
                if ( this.value == null )
                {
                    this.value = this.operation.evaluate ( this.inputs );
                }

                return this.value;
            }
            else
            {
                // Either the requirement was not satisfied by the
                // required value provided, or there are still
                // more requirements to satisfy.
                return this;
            }
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        if ( this.unsatisfiedRequirements == null ) // Constructor time only.
        {
            return "Unsatisfied";
        }

        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Unsatisfied<"
                      + ClassName.of ( this.expectedClass () )
                      + "> requirements {" );
        boolean is_first = true;

        synchronized ( this.lock )
        {
            for ( Requirement<?> requirement
                      : this.unsatisfiedRequirements.values () )
            {
                if ( is_first )
                {
                    is_first = false;
                }
                else
                {
                    sbuf.append ( "," );
                }

                sbuf.append ( " " + requirement.id ().name () );
            }
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
