package musaico.foundation.wiring;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * The conductance state of a Wire, such as blocked and waiting
 * for input, closed, ready, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every WireState must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public enum WireState
    implements Serializable
{
    /** The Component at the other end of the Wire is not ready
     *  to push / pull data. */
    BLOCKED,

    /** The Wire has ended conducting data, no more input or output. */
    CLOSED,

    /** The Component at the other end of the Wire is
     *  ready to push / pull data data without blocking.
     *  It might return empty right now, but it is guaranteed
     *  not to block. */
    READY;


    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;
}
