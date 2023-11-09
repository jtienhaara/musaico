package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.TermPipeline;
import musaico.foundation.term.Type;


/**
 * <p>
 * An input or output Port to/from the invocation of an Operation.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public Port<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Connects this port to the specified input source.
     * </p>
     *
     * @param source The source of input through this Port, such as a Term
     *               or a TermPipeline.  Must not be null.
     *
     * @return The new Connection to the specified input source.  Never null.
     */
    public abstract Connection<VALUE> connect (
            TermPipeline.TermTap<VALUE> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The Type of this Port, which defines what Terms can
     *         pass in / out.  Never null.
     */
    public abstract Type<VALUE> type ()
        throws ReturnNeverNull.Violation;
}
