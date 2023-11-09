package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.obligations.EveryParameter;

import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.stream.FilterStream;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Keeps all Components while filtering, but then, at
 * <code> filterEnd () </code>, discards the whole set
 * if there are any that are not connected to the rest
 * by input / output wires..
 * </p>
 *
 *
 * <p>
 * In Java, every FilterStream must be Serializable in order to play
 * nicely over RMI.
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
!!! Need more than a Component to figure out what the wires are.
    Need a Circuit.  This should probably be a filter stream
    on Wires;
public class ConnectedComponentsFilterStream
    implements FilterStream<Component>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The state of each wiring Component streamed so far.
    // Initially the List is empty.
    // Each time a Component is streamed in, the indices of
    // its outputs are set to FilterState.KEPT, while anything
    // that has not yet been referenced (including the
    // Component passed in) is set to FilterState.DISCARDED.
    // Once filtering ends, every Component index should
    // have been set to FilterState.KEPT, except for the
    // entry (0th) Component.
    // MUTABLE.
    private final List<FilterState> componentStates =
        new ArrayList<FilterState> ();

    // The index of the next Component in the stream.
    // MUTABLE.
    private int nextComponentIndex = 0;
        


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final ConnectedComponentsFilterStream that =
            (ConnectedComponentsFilterStream) object;
        if ( this.componentStates.size () != that.componentStates.size ()
             || ! this.componentStates.containsAll ( that.componentStates ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filtered(java.lang.Object, musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filtered (
            Component component,
            FilterState component_filter_state
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        final int component_index = this.nextComponentIndex;
        this.nextComponentIndex ++;

        if ( component == null
             || component_filter_state == null
             || component_filter_state == FilterStream.CONTINUE
             || component_filter_state == FilterStream.END )
        {
            // Invalid individual Component filter states.
            // These should theoretically never happen.
            return FilterState.DISCARDED;
        }
        else if ( ! component_filter_state.isKept () )
        {
            // For whatever reason, we aren't going to count
            // the wires into / out of this Component.  Just skip
            // it but carry on.
            return FilterStream.CONTINUE;
        }

        // Go through the output wires of this Component,
        // and mark every referenced Component as connected.
        for ( int connected_index : component.wiresOut () )
        {
            if ( connected_index < 0 )
            {
                continue;
            }
            else if ( connected_index >= this.componentStates.size () )
            {
                for ( int ci = this.componentStates.size ();
                      ci <= component_index;
                      ci ++ )
                {
                    this.componentStates.add ( FilterState.DISCARDED );
                }
            }

            this.componentStates.set ( connected_index, FilterState.KEPT );
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterEnd(musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filterEnd (
            FilterState container_filter_state
            )
        throws NullPointerException
    {
        if ( container_filter_state == null
             || container_filter_state == FilterStream.CONTINUE
             || container_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }

        if ( this.componentStates.size () < 2 )
        {
            // No entry or exit Components!
            return FilterState.DISCARDED;
        }

        !!! the following obvious scenario fails this implementation:
            component 1 -> component 2;
        component 3 -> component 4;
        It is disconnected, yet passes as if it is fully connected;
        However maybe this is actually OK, as long as every chain
            of Components starts at a terminal and ends at a terminal?;
        // Skip the entry node, it should not be connected
        // (but we don't care if it is, that's someone else's problem).
        for ( int cs = 1; cs < this.componentStates.size (); cs ++ )
        {
            final FilterState component_state =
                this.componentStates.get ( cs );
            if ( ! component_state.isKept () )
            {
                return component_state;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterStart()
     */
    @Override
    public final FilterState filterStart ()
    {
        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
