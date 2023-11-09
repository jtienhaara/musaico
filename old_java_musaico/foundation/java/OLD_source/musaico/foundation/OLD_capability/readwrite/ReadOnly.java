package musaico.foundation.capability.administrative;

import java.io.Serializable;


import musaico.foundation.capability.StandardCapability;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * A Processor accepts Control requests, possibly of only a specific
 * type (such as ReadControl or WriteControl or OperationalControl
 * or AdministrativeControl).
 * </p>
 *
 * <p>
 * A Processor might be an asynchronous task calculating some value
 * or performing some operation, or it might be a more general purpose
 * Processor, such as an application or a server or a messaging service,
 * or a physical or virtual machine, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Control must be Serializable in order to play nicely
 * over RMI.  WARNING: Parameters such as Operations sent over RMI Controls
 * (requesters, subscribers, and so on) generally must be UnicastRemoteObjects
 * in order to properly pass messages from one machine to the other, rather
 * than simply serializing each requester or subscriber and replying locally
 * on the controlled machine.
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
 * @see musaico.foundation.capability.administrative.MODULE#COPYRIGHT
 * @see musaico.foundation.capability.administrative.MODULE#LICENSE
 */
public interface Processor<CAPABILITY extends StandardCapability>
    extends Operation<CONTROL, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Every Processor must implement apply ().
     *  @see musaico.foundation.value.Operation#apply(musaico.foundation.value.Value) */

    /** Every Processor must implement equals ().
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Processor must implement hashCode ().
     *  @see java.lang.Object#hashCode() */


    /** Every Processor must implement inputType ().
     *  @see musaico.foundation.value.Operation#inputType() */


    /** Every Processor must implement outputType ().
     *  @see musaico.foundation.value.Operation#outputType() */


    /** Every Processor must implement toString ().
     *  @see java.lang.Object#toString() */
}
