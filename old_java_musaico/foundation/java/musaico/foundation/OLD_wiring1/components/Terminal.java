package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.logging.Level;


import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;


/**
 * <p>
 * A Component that provides an input source and/or an output
 * destination to/from a Board.
 * </p>
 *
 * <p>
 * A Terminal has wiresIn and / or wiresOut to/from
 * other Components on the same Board.  External Boards can then
 * connect Wires to the Terminal to send inputs to / receive
 * outputs from the Terminal, providing input/output
 * to/from the Board in which the Terminal is placed.
 * </p>
 *
 * <p>
 * If no parent Board wires any PointToPoint connection to a Terminal,
 * then at runtime it is wired automatically to Circuit.ground (),
 * so that it receives no inputs, and its outputs are discarded.
 * </p>
 *
 *
 * <p>
 * In Java every Board must be Serializable in order to
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
public class Terminal
    extends AbstractComponent
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new Terminal.
     * </p>
     *
     * @param name A short, distinguishing name for this Terminal.
     *             Must not be null.
     *
     * @param wires_in The input leads into this Terminal
     *                 from the Board in which it will be placed.
     *                 Each Class defines the type of data
     *                 that this Terminal
     *                 will pull in a given circuit context
     *                 over a specific wire #, such as String data
     *                 or Integer data or some FooBar object data
     *                 or other, and so on.
     *                 Each lead has a specific data requirement,
     *                 or Object.class if any data is acceptable
     *                 to be pulled as input from the given wire..
     *                 Can be empty, if this Terminal has
     *                 no inputs from the Board in which it is placed.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param wires_out The output leads from this Terminal
     *                  to the Board in which it will be placed.
     *                  Each Class defines the type of data
     *                  that this Terminal
     *                  will push to a given circuit context
     *                  over a specific wire #, such as String data
     *                  or Integer data or some FooBar object data
     *                  or other, and so on.
     *                  Each lead has a specific data requirement,
     *                  or Object.class if any data will be pushed
     *                  as output on the given wire.
     *                  Can be empty, if this Terminal has
     *                  no outputs to the Board in which it is placed.
     *                  Must not be null.  Must not contain any null elements.
     */
    public Terminal (
            String name,
            Class<?> [] wires_in,
            Class<?> [] wires_out
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        super ( name,        // name
                wires_in,    // wires_in
                wires_out ); // wires_out
    }


    /**
     * @see musaico.foundation.wiring.Component#conduct(musaico.foundation.wiring.Circuit, java.lang.String)
     */
    public final WireState conduct (
            Circuit circuit,
            String component_id
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation
    {
        this.advocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              circuit, component_id );

        // For each external source that has readily available input,
        // output it to the hard-wired Board sinks (one output
        // for every sink).
        final Class<?> [] wires_in = this.wiresIn ();
        final int num_hardwired_sources = wires_in.length;
        final Class<?> [] wires_out = this.wiresOut ();
        final int num_hardwired_sinks = wires_out.length;
        final int num_external_sources =
            circuit.numSources ( component_id ) - num_hardwired_sources;
        final int num_external_sinks =
            circuit.numSinks ( component_id ) - num_hardwired_sinks;
        WireState component_state = WireState.CLOSED;
        for ( int ei = 0; ei < num_external_sources; ei ++ )
        {
            int wire_index = num_hardwired_sources + ei;
            final Source<?> source =
                circuit.source ( component_id,   // component_id
                                 wire_index,     // wire_in
                                 Object.class ); // data_class
            final WireState source_state = source.state ();
            switch ( source_state )
            {
            case WireState.BLOCKED:
                if ( component_state != WireState.READY )
                {
                    component_state = WireState.BLOCKED;
                }
                break;

            case WireState.READY:
                component_state = WireState.READY;
                break;

            // Default: don't change the component state.
            }

            if ( source_state != WireState.READY )
            {
                continue;
            }

            final Object [] inputs = source.pull ();
            for ( int io = 0; io < num_hardwired_sinks; io ++ )
            {
                final Class<?> output_class = wires_out [ io ];
                Set<Integer> maybe_filtered_out_indices = null;
                for ( int i = 0; i < inputs.length; i ++ )
                {
                    final Object input = inputs [ i ];
                    if ( ! output_class.isInstance ( input ) )
                    {
                        if ( maybe_filtered_out_indices == null )
                        {
                            maybe_filtered_out_indices =
                                new HashSet<Integer> ();
                        }

                        maybe_filtered_out_indices.add ( i );
                    }
                }

                final Object [] outputs;
                if ( maybe_filtered_out_indices == null )
                {
                    outputs = inputs;
                }
                else
                {
                    outputs =
                        new Object [ inputs.length
                                     - maybe_filtered_out_indices.size () ];
                    int o = 0;
                    for ( int i = 0; i < inputs.length; i ++ )
                    {
                        if ( maybe_filtered_out_indices.contains ( i ) )
                        {
                            continue;
                        }

                        outputs [ o ] = inputs [ i ];
                        o ++;
                    }
                }

                sink.push ( outputs );
            }
        }

        !!! Now do the reverse: pull from the hardwired sources,
                       push to the external sinks.;
    }


    /**
     * @see musaico.foundation.wiring.AbstractComponent#equalsComponent(musaico.foundation.wiring.Component)
     */
    protected final boolean equalsComponent (
            AbstractComponent component
            )
        throws Parameter1.MustBeInstanceOf.Violation,
               EveryParameter.MustNotBeNull.Violation
    {
        // All the input wires and output wires have already been checked.
        // Nothing else to check for equality.
        return true;
    }
}
