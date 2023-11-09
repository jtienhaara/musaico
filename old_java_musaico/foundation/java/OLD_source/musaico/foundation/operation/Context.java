package musaico.foundation.operation;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.graph.ImmutableGraph;

import musaico.foundation.term.Countable;


/**
 * <p>
 * The configuration settings and runtime state of a particular
 * pipeline.
 * </p>
 *
 * <p>
 * Each configuration of Pipes can represent an OperationPipeline;
 * but when an input is fed to that pipeline, a Context must be
 * generated, connecting each individual section of Pipe to its
 * inputs and outputs via Streams of data.  Each Stream can be a
 * simple Buffer, or some other type of Stream can be used in a given
 * Context to achieve specific goals at runtime.
 * </p>
 *
 * <p>
 * Because the pipeline is connected via a Graph in each Context, and each
 * output/input connection is an Arc, a Context can be built
 * up using special Arcs (SubGraphs, in particular), each of which
 * passes the output from one Pipe through an entire sub-Pipeline
 * to the input of the next Pipe.  Of course, care must be
 * taken to avoid unwanted side-effects or defects when connecting
 * Pipes via sub-pipeline streams in a Context.
 * </p>
 *
 * <p>
 * Some examples of using Context-specific sub-pipelines to trigger
 * side-effects (or to "instrument the code", or to introduce "aspects"
 * into a pipeline, and so on):
 * </p>
 *
 * <ul>
 *   <li> Logging the data or throughput or timing between specific
 *        or all Pipes in the graph. </li>
 *
 *   <li> Scheduling, allowing the owner of the Context to stop
 *        processing in one section of the pipeline by intercepting
 *        the outputs from one Pipe and forcing the next Pipe to block
 *        on <code> read () </code> for some time. </li>
 *
 *   <li> Security, intercepting unpermitted requests, or obfuscating
 *        or encrypting unprotected private data. <li>
 *
 *   <li> Enforcing contracts, such as requesting payment before
 *        data will be released, or adding to a running tab, or
 *        releasing data in another pipeline based on payment inputs,
 *        and so on. </li>
 *
 *   <li> Manual control flow, allowing document workflow,
 *        or a primitive debugger which steps through a pipeline,
 *        and so on, based on users' interventions. </li>
 *
 *   <li> Visualization, displaying to users the progress of data
 *        through a pipeline, or hotspots, or watch conditions,
 *        and so on. </li>
 * </ul>
 *
 *
 * <p>
 * In Java every Context must be Serializable in order to play nicely
 * over RMI.  However users of Contexts should be cautious, because
 * the Pipes and Streams contained therein can contain Terms which
 * contain non-Serializable data, and will cause exceptions the first
 * time you try to pass them over remote invocation.
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
public interface Context
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The graph of Pipe nodes connected by Stream arcs
     *         which describe the pipeline in this Context.
     *         Never null.
     */
    public abstract ImmutableGraph<Pipe<?, ?>, Stream<?>> graph ()
        throws ReturnNeverNull.Violation;


    /**
     *
     * <p>
     * The input Stream(s) into the specified Pipe
     * under this Context.
     * </p>
     *
     * <p>
     * A typical Pipe has exactly one input.  However some Pipes
     * do not require inputs, such as TermPipes or Pipes from outside
     * the system; and other Pipes require multiple inputs, such
     * as Pipes which merge or compare source streams.
     * </p>
     *
     * @param pipe The Pipe whose input Stream(s) will be returned.
     *             Must not be null.
     *
     * @return The input Stream(s) into the specified Pipe.
     *         Always the same number of Streams as the number of
     *         <code> inputPipes () </code> of the specified pipe.
     *         Never null.
     *
     * @throws Return.Length.AlwaysEqualTo.Violation If this Context is
     *             either inept or corrupt, and cannot build exactly
     *             the same number of input Streams as the specified
     *             Pipe's <code> inputPipes () </code>.
     */
    public abstract <INPUT extends Object>
        Countable<Stream<INPUT>> inputs (
            Pipe<INPUT, ?> pipe
            )
        throws ParametersMustNotBeNull.Violation,
               Return.Length.AlwaysEqualTo.Violation,
               ReturnNeverNull.Violation;


    /**
     *
     * <p>
     * The output Stream(s) from the specified Pipe
     * under this Context.
     * </p>
     *
     * <p>
     * A typical Pipe has exactly one output.  However some Pipes
     * do not produce outputs, such as Pipes to external systems;
     * and other Pipes produce multiple outputs, such
     * as Pipes which split or filter their source streams.
     * </p>
     *
     * @param pipe The Pipe whose output Stream(s) will be returned.
     *             Must not be null.
     *
     * @return The output Stream(s) from the specified Pipe.
     *         Always the same number of Streams as the
     *         <code> numOuputPipes () </code> of the specified pipe.
     *         Never null.
     *
     * @throws Return.Length.AlwaysEqualTo.Violation If this Context is
     *             either inept or corrupt, and cannot build exactly
     *             the same number of output Streams as the specified
     *             Pipe's <code> numOputPipes () </code>.
     */
    public abstract <OUTPUT extends Object>
        Countable<Stream<OUTPUT>> outputs (
            Pipe<?, OUTPUT> pipe
            )
        throws ParametersMustNotBeNull.Violation,
               Return.Length.AlwaysEqualTo.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Stream(s) connecting the two specified Pipes, if any;
     * or if the two specified Pipes are not connected in the
     * <code> graph () </code> for this Context, then No Stream
     * will be returned.
     * </p>
     *
     * @param from The source Pipe of the Stream(s) to return.
     *             Must not be null.
     *
     * @param to The sink Pipe of the Stream(s) to return.
     *           Must not be null.
     *
     * @return Either the One Stream connecting the two Pipes,
     *         or more than one Streams if the Pipes have multiple
     *         connections; or No Stream, if the two Pipes are
     *         not connected.  Never null.
     */
    public abstract <VALUE extends Object>
        Countable<Stream<VALUE>> stream (
            Pipe<?, VALUE> from,
            Pipe<VALUE, ?> to
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
