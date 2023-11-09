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
 * Implements a specific BehaviourControl for a specific type of
 * Pipeline, allowing behaviour-specific controls to be introduced
 * into an otherwise generic chain of operations.
 * </p>
 *
 * <p>
 * For example, a <code> BlockingPipeline<VALUE, BUILT, PARENT> </code>
 * might add an Operation to the parent Pipeline to either
 * <code> async ( ... ) </code> or <code> await ( ... ) </code>
 * for blocking results.
 * </p>
 *
 * <pre>
 *     final Term<VALUE> maybe_blocking = ...;
 *     final Term<VALUE> result = maybe_blocking
 *         .on ( Blocking.BEHAVIOUR ) // Start conditional pipeline.
 *         .always ()                 // Get a BlockingPipeline.
 *         .await ( BigDecimal.TEN )  // Control behaviour.
 *         .off ()                    // End conditional pipeline.
 *         .build ();
 * </pre>
 *
 * <p>
 * Or each input Term to a chain of Operations might be compressed:
 * </p>
 *
 * <pre>
 *     final OperationPipeline<VALUE> uncompressed = ...;
 *     final OperationPipeline<VALUE> compressed = uncompressed
 *         .make ( Compressable.BEHAVIOUR ) // Start unconditional pipeline.
 *         .always ()                       // Get CompressablePipeline.
 *         .compress ( ... )                // Control behaviour.
 *         .build ();
 * </pre>
 *
 * <p>
 * Each BehaviourPipeline implements the BehaviourControl interface
 * which exposes the behaviour-specific control methods, each one
 * returning the parent Pipeline, and does so with certain knowledge
 * of the VALUE of each Term it will be processing (whereas the
 * BehaviourControl interface is VALUE-agnostic).
 * </p>
 *
 * <p>
 * For more information on the implementation pattern for
 * Behaviours, BehaviourControls and BehaviourPipelines:
 * </p>
 *
 * @see musaico.foundation.term.Behaviour
 *
 *
 * <p>
 * In Java every BehaviourPipeline must be Serializable in order to
 * play nicely across RMI.
 * </p>
 *
 * <p>
 * In Java every BehaviourPipeline must implement equals (), hashCode ()
 * and toString ().
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
public interface BehaviourPipeline<VALUE extends Object, BUILT extends Object, PARENT extends Pipeline<VALUE, BUILT, PARENT>, PIPELINE extends BehaviourControl & BehaviourPipeline<VALUE, BUILT, PARENT, PIPELINE>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return This BehaviourPipeline.  Each implementation
     *         of BehaviourPipeline returns its behaviour-specific self.
     *         For example,
     *         <code> public BlockingPipeline<VALUE, BUILT, PARENT> always () </code>
     *         allows users of pipelines to invoke
     *         <code> Pipeline.on ( Blocking.BEHAVIOUR ).always ().await ( BigDecimal.TEN ) </code>.
     *         This <code> always () </code> method is the bridge between
     *         the ignoramous Pipeline, which knows nothing about Blocking
     *         and so can only return a generic
     *         <code> BehaviourPipeline<VALUE, BUILT, PARENT, BlockingPipeline<VALUE, BUILT, PARENT>> </code>,
     *         and the behaviour-specific controls exposed by the
     *         behavioural pipeline.  Without the extra call to
     *         <code> always () <code>, users of the BlockingPipeline would
     *         be forced to do ugly unsafe casting.  For more information
     *         on the implementation pattern of Behaviours, BehaviourControls
     *         and BehaviourPipelines, see the comments
     *         in the Behaviour interface.
     *         Never null.
     *
     * @see musaico.foundation.term.Behaviour
     */
    public PIPELINE always ()
        throws ReturnNeverNull.Violation;


    // Every BehaviourPipeline must implement
    // musaico.foundation.term.BehaviourControl#behaviour()


    // Every BehaviourPipeline must implement java.lang.Object#equals(java.lang.Object)


    // Every BehaviourPipeline must implement java.lang.Object#hashCode()


    // Every BehaviourPipeline must implement java.lang.Object#toString()
}
