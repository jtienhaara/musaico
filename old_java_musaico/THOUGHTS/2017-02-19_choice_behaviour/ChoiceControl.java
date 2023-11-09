package musaico.foundation.term;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;


/**
 * <p>
 * Specifies the interface for controlling a specific Behaviour
 * in each input Term.
 * </p>
 *
 * <p>
 * A BehaviourControl specifies the methods which can be used to
 * control behaviour.  For example, BlockingControl might provide
 * an <code> async ( ... ) </code> control, for handling the
 * eventual result of each input term in the background,
 * as well as an <code> await ( ... ) </code> control, in order to
 * wait for each input Term's final result before proceeding with
 * the operations in the pipeline.
 * </p>
 *
 * <p>
 * This allows the user of a Term to specify how to handle blocking,
 * for example, in case the Term has to perform I/O
 * or some similar blocking operation:
 * </p>
 *
 * <pre>
 *     final Term<String> initial = ...;
 *     final Term<String> result = initial
 *         .on ( Blocking.BEHAVIOUR ) // Start conditional pipeline.
 *         .always ()                 // Get BlockingPipeline.
 *         .await ( BigDecimal.TEN )  // Control Blocking.
 *         .off ();                   // End conditional pipeline.
 * </pre>
 *
 * <p>
 * Or the user of an OperationPipeline might localize each input
 * Term to the pipeline:
 * </p>
 *
 * <pre>
 *     final OperationPipeline<String> not_localized = ...;
 *     final OperationPipeline<String> localized = not_localized
 *         .make ( Localizable.BEHAVIOUR ) // Unconditionally start pipeline.
 *         .always ()                      // Get LocalizablePipeline.
 *         .localize ( Locale.CANADA_FRENCH ) // Control behaviour.
 *         .build ();
 * </pre>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java every BehaviourControl is an interface which exposes
 * Behaviour-specific control methods to be implemented by the
 * corresponding BehaviourPipeline.
 * </p>
 *
 * <p>
 * In Java every BehaviourControl must be Serializable in order to
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface BehaviourControl
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The Behaviour pattern that is handled by this
     *         BehaviourControl.  Never null.
     */
    public abstract Behaviour<? extends BehaviourControl> behaviour ();
}
