package musaico.foundation.buffer;


import java.io.Serializable;


import musaico.foundation.condition.Conditional;

import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.region.Position;
import musaico.foundation.region.Region;

import musaico.foundation.typing.Instance;
import musaico.foundation.typing.Typing;

import musaico.foundation.value.NoValue;
import musaico.foundation.value.Value;


package musaico.foundation.buffer;

import java.io.Serializable;


/**
 * <p>
 * An empty Buffer.
 * </p>
 *
 * <p>
 * A NoBuffer is akin to a NULL pointer in C.
 * It has no Region and no content.
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
 * @see musaico.foundation.buffer.MODULE#COPYRIGHT
 * @see musaico.foundation.buffer.MODULE#LICENSE
 */
public class NoBuffer
    extends NoValue<Instance<?>, Typing.Violation>
    implements Buffer, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoBuffer with the specified contract violation.
     * </p>
     *
     * @param violation The contract violation which led to the creation
     *                  of this NoBuffer.  Must not be null.
     */
    public NoBuffer (
                     Typing.Violation violation
                     )
    {
        super ( Instance.class,
                violation,
                Instance.NONE );
    }


    /**
     * @see musaico.foundation.buffer.Buffer#get(musaico.foundation.region.Position)
     */
    public Failed<Instance<?>, BufferViolation> get (
                                                     Position position
                                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new Failed!!!;
    }


    /**
     * @see musaico.foundation.buffer.Buffer#region()
     */
    public Region region ()
    {
        return new NoRegion ( !!! );
    }


    /**
     * @see musaico.foundation.buffer.Buffer#resize(musaico.foundation.region.Region)
     */
    public NoBuffer resize (
                            Region region
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.buffer.Buffer#set(musaico.foundation.region.Position, musaico.foundation.typing.Instance)
     */
    public abstract Buffer set (
                                Position position,
                                Instance<?> instance
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.buffer.Buffer#value(musaico.foundation.region.Position)
     */
    public NoValue<?, Typing.Violation> value (
                                               Position position
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return
            new NoValue<Object, Typing.Violation> ( Object.class,
                                                    this.checkedException (),
                                                    new Object () );
    }
}
