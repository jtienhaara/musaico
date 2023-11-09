package musaico.foundation.pipeline;

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
 * Specifies a pattern of behaviour in a certain class of specimen objects,
 * such as blocking or decryptable or localizable behaviour in Terms,
 * or Mutable or Immutable behaviour, and so on.
 * </p>
 *
 * <p>
 * A Behaviour is typically used in one of two ways:
 * </p>
 *
 * <ul>
 *   <li> Conditionally, only if the input exhibits the Behaviour; or </li>
 *   <li> Unconditionally, always controlling the Behaviour,
 *        possibly by inducing it in the input, or possibly
 *        by controlling the Behaviour that is already exhibited
 *        by the input. </li>
 * </ul>
 *
 * <p>
 * Conditional Behaviour control typically looks like:
 * </p>
 *
 * <pre>
 *     pipeline.when ( Behaviour as a filter )
 *                 .pipe ( Behaviour control )
 *             .end ();
 * </pre>
 *
 * <p>
 * Unconditional Behaviour control typically looks like:
 * </p>
 *
 * <pre>
 *     pipeline.pipe ( Behaviour control );
 * </pre>
 *
 * <p>
 * So, for example, controlling an input Term that is Blocking
 * by waiting up to 10 seconds for the blocking operation to finish:
 * </p>
 *
 * <pre>
 *     parent.when ( Blocking.BEHAVIOUR ) // Start conditional pipeline.
 *                                        // Block up to 10 seconds:
 *               .pipe ( Blocking.BEHAVIOUR.await ( BigDecimal.TEN ),
 *                       pipeline.type () )
 *           .end ();                     // End conditional pipeline.
 * </pre>
 *
 * <p>
 * And might look something like:
 * </p>
 *
 * <pre>
 *     ...--[ Parent Pipeline ]-- ( When SubPipeline )
 *                                        |
 *                                        ^
 *                                       / \  Blocking
 *                                       \ /   Filter
 *                                        V
 *                                       / \
 *                                      /   \
 *                                KEPT |     | DISCARDED
 *                                     |     |
 *                       ( Await Operation   |
 *                         or SubPipeline )  |
 *                                     |     |
 *                                      \   /
 *                                       \ /
 *                                        |
 *                               [ Parent Pipeline ]
 *                                        |
 *                                       ...
 * </pre>
 *
 * <p>
 * Or to unconditionally encrypt each input Term, something like
 * the following might be used:
 * </p>
 *
 * <pre>
 *     final byte [] private_key = ...;
 *     final byte [] public_key = ...;
 *     pipeline
 *         .pipe ( Encryptable.BEHAVIOUR
 *                            .encrypt ( private_key,    // Control encryption
 *                                       public_key,     //   unconditionally.
 *                                       pipeline.type () ) );
 * </pre>
 *
 *
 * <p>
 * In Java every Behaviour must be Serializable in order to
 * play nicely across RMI.
 * </p>
 *
 * <p>
 * In Java every Behaviour must implement equals (), hashCode ()
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Behaviour
    extends Filter<Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Behaviour should provide its own methods.
    // Term Behaviours take the form:
    //     public Pipeline<...> doSomething ( ..., Type<...> )
    // Or:
    //     public Operation<...> doSomething ( ..., Type<...> )
    // The returned Pipeline (or Operation) then pipe()ed
    // to a parent pipeline by the caller.
    // For example, Blocking Behaviour provides:
    //     public Operation<BLOCKING> async ( Type<BLOCKING> );
    //     public Operation<BLOCKING> await ( BigDecimal max_seconds,
    //                                        Type<BLOCKING> );
    // Accepting the Pipeline's Type as the last parameter of
    // the Behaviour-specific control method is typical
    // of Term Behaviours, but is just a pattern, not a requirement.
    // The actual names and natures of the pipeline-creating
    // methods are entirely up to the implementer of the Behaviour.

    // Every Behaviour must implement java.lang.Object#equals(java.lang.Object)

    // Every Behaviour must implement
    // musaico.foundation.filter.Filter#filter(java.lang.Object)

    // Every Behaviour must implement java.lang.Object#hashCode()

    // Every Behaviour must implement java.lang.Object#toString()
}
