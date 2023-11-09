package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import musaico.foundation.filter.Filter;

import musaico.foundation.structure.ClassName;


public abstract class AbstractConductor
    extends AbstractTagged<Conductor>
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Serializable lock = new String ( "lock" );
    private Conductivity state = Conductivity.OFF;

    public AbstractConductor ()
    {
        super ();
    }

    public AbstractConductor (
            Tag ... tags
            )
    {
        super ( tags );
    }

    /**
     * @see musaico.foundation.wiring.Conductor#state(musaico.foundation.wiring.Board)
     */
    @Override
    public final Conductivity state (
            Board board
            )
    {
        synchronized ( this.lock )
        {
            return this.state;
        }
    }

    /**
     * @see musaico.foundation.wiring.Conductor#on(musaico.foundation.wiring.Board)
     */
    @Override
    public final Conductor on ( Board board )
    {
        final Conductivity old_state;
        final Conductivity intermediate_state;
        final Conductivity new_state;
        synchronized ( this.lock )
        {
            old_state = this.state;
            if ( old_state == Conductivity.OFF )
            {
                new_state = Conductivity.STOPPED;
                intermediate_state = Conductivity.TURNING_ON;
                this.state = intermediate_state;
            }
            else
            {
                new_state = old_state;
                intermediate_state = old_state;
            }
        }

        if ( new_state != old_state )
        {
            this.transition ( board, old_state, new_state );
        }

        synchronized ( this.lock )
        {
            if ( this.state == intermediate_state )
            {
                this.state = new_state;
            }
            else
            {
                // Uh-oh!
                this.state = Conductivity.BROKEN;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#off(musaico.foundation.wiring.Board)
     */
    @Override
    public final Conductor off ( Board board )
    {
        for ( int stop_first = 0; stop_first < 2; stop_first ++ )
        {
            final Conductivity old_state;
            final Conductivity intermediate_state;
            final Conductivity new_state;
            synchronized ( this.lock )
            {
                old_state = this.state;
                if ( old_state == Conductivity.STOPPED )
                {
                    new_state = Conductivity.OFF;
                    intermediate_state = Conductivity.TURNING_OFF;
                    this.state = intermediate_state;
                }
                else
                {
                    new_state = old_state;
                    intermediate_state = old_state;
                }
            }

            if ( new_state == old_state
                 && old_state == Conductivity.RUNNING
                 && stop_first == 0 ) // Abort if we keep going back into the RUNNING state.
            {
                this.stop ( board );
                continue;
            }

            if ( new_state != old_state )
            {
                this.transition ( board, old_state, new_state );
            }

            synchronized ( this.lock )
            {
                if ( this.state == intermediate_state )
                {
                    this.state = new_state;
                }
                else
                {
                    // Uh-oh!
                    this.state = Conductivity.BROKEN;
                }
            }

            break;
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#start(musaico.foundation.wiring.Board)
     */
    @Override
    public final Conductor start ( Board board )
    {
        for ( int on_first = 0; on_first < 2; on_first ++ )
        {
            final Conductivity old_state;
            final Conductivity intermediate_state;
            final Conductivity new_state;
            synchronized ( this.lock )
            {
                old_state = this.state;
                if ( old_state == Conductivity.STOPPED )
                {
                    new_state = Conductivity.RUNNING;
                    intermediate_state = Conductivity.STARTING;
                    this.state = intermediate_state;
                }
                else
                {
                    new_state = old_state;
                    intermediate_state = old_state;
                }
            }

            if ( new_state == old_state
                 && old_state == Conductivity.OFF
                 && on_first == 0 ) // Abort if we keep going back into the OFF state.
            {
                this.on ( board );
                continue;
            }

            if ( new_state != old_state )
            {
                this.transition ( board, old_state, new_state );
            }

            synchronized ( this.lock )
            {
                if ( this.state == intermediate_state )
                {
                    this.state = new_state;
                }
                else
                {
                    // Uh-oh!
                    this.state = Conductivity.BROKEN;
                }
            }

            break;
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#stop(musaico.foundation.wiring.Board)
     */
    @Override
    public final Conductor stop ( Board board )
    {
        final Conductivity old_state;
        final Conductivity intermediate_state;
        final Conductivity new_state;
        synchronized ( this.lock )
        {
            old_state = this.state;
            if ( old_state == Conductivity.RUNNING )
            {
                new_state = Conductivity.STOPPED;
                intermediate_state = Conductivity.STOPPING;
                this.state = intermediate_state;
            }
            else
            {
                new_state = old_state;
                intermediate_state = old_state;
            }
        }

        if ( new_state != old_state )
        {
            this.transition ( board, old_state, new_state );
        }

        synchronized ( this.lock )
        {
            if ( this.state == intermediate_state )
            {
                this.state = new_state;
            }
            else
            {
                // Uh-oh!
                this.state = Conductivity.BROKEN;
            }
        }

        return this;
    }

    protected abstract void transition (
            Board board,
            Conductivity old_state,
            Conductivity new_state
            );

    // Every AbstractConductor must implement
    // @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector)

    protected final Carrier [] pullHelper (
            Board board,
            Selector [] excluded_selectors,
            Selector ... pull_from_selectors
            )
    {
        if ( board == null
             || excluded_selectors == null
             || pull_from_selectors == null
             || pull_from_selectors.length == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Set<Selector> exclusions =
            new HashSet<Selector> ();
        for ( Selector excluded_selector : excluded_selectors )
        {
            if ( excluded_selector == null )
            {
                continue;
            }

            exclusions.add ( excluded_selector );
        }

        int total_length = 0;
        final List<Carrier []> pulled_list = new ArrayList<Carrier []> ();
        for ( Selector selector : pull_from_selectors )
        {
            if ( selector == null
                 || exclusions.contains ( selector ) )
            {
                continue;
            }

            final Conductor [] pull_from_conductors;
            if ( selector.filter ().filter ( this ).isKept () )
            {
                // This Conductor is, itself, at the children
                // end of the Selector.
                // Pull from the parents end of the Selector:
                pull_from_conductors = board.selectorOwners ( selector );
            }
            else
            {
                // Pull from the children end of the Selector:
                pull_from_conductors = board.select ( selector );
            }

            for ( Conductor conductor : pull_from_conductors )
            {
                if ( conductor == null )
                {
                    continue;
                }

                final Carrier [] pulled_from_one =
                    conductor.pull ( board, selector );
            
                if ( pulled_from_one == null
                     || pulled_from_one.length == 0 )
                {
                    continue;
                }

                total_length += pulled_from_one.length;

                pulled_list.add ( pulled_from_one );
            }
        }

        if ( pulled_list.size () == 0 )
        {
            return new Carrier [ 0 ];
        }

        final Carrier [] pulled;
        if ( pulled_list.size () == 1 )
        {
            pulled = pulled_list.get ( 0 );
        }
        else
        {
            pulled = new Carrier [ total_length ];
            int offset = 0;
            for ( Carrier [] some_pulled : pulled_list )
            {
                System.arraycopy ( some_pulled, 0,
                                   pulled, offset, some_pulled.length );
                offset += some_pulled.length;
            }
        }

        return pulled;
    }

    // Every AbstractConductor must implement
    // @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Board, musaico.foundation.wiring.Selector, musaico.foundation.Carrier[])

    protected final Carrier [] pushHelper (
            Board board,
            Selector [] excluded_selectors,
            Carrier [] data,
            Selector ... push_to_selectors
            )
    {
        if ( board == null
             || excluded_selectors == null
             || data == null
             || data.length == 0
             || push_to_selectors == null
             || push_to_selectors.length == 0)
        {
            return new Carrier [ 0 ];
        }

        final Set<Selector> exclusions =
            new HashSet<Selector> ();
        for ( Selector excluded_selector : excluded_selectors )
        {
            if ( excluded_selector == null )
            {
                continue;
            }

            exclusions.add ( excluded_selector );
        }

        for ( Selector selector : push_to_selectors )
        {
            if ( selector == null
                 || exclusions.contains ( selector ) )
            {
                continue;
            }

            final Conductor [] push_to_conductors;
            if ( selector.filter ().filter ( this ).isKept () )
            {
                // Pull from the other end of the Selector:
                push_to_conductors = board.selectorOwners ( selector );
            }
            else
            {
                // Pull from the other end of the Selector:
                push_to_conductors = board.select ( selector );
            }

            for ( Conductor conductor : push_to_conductors )
            {
                if ( conductor == null )
                {
                    continue;
                }

                conductor.push ( board,
                                 selector,
                                 data );
            }
        }

        return data;
    }

    // Every AbstractConductor must implement
    // @see musaico.foundation.wiring.Conductor#selectors(musaico.foundation.wiring.Board)

    public final Conductor [] selectorConnections (
            Board board
            )
    {
        if ( board == null )
        {
            return new Conductor [ 0 ];
        }

        final List<Conductor []> connections_list =
            new ArrayList<Conductor []> ();
        int total_length = 0;
        for ( Selector selector : this.selectors ( board ) )
        {
            final Conductor [] selection =
                board.select ( selector );
            if ( selection == null
                 || selection.length == 0 )
            {
                continue;
            }

            connections_list.add ( selection );
            total_length += selection.length;
        }

        if ( connections_list.size () == 0 )
        {
            return new Conductor [ 0 ];
        }
        else if ( connections_list.size () == 1 )
        {
            final Conductor [] connections = connections_list.get ( 0 );
            return connections;
        }

        final Conductor [] connections = new Conductor [ total_length ];
        int offset = 0;
        for ( Conductor [] selection : connections_list )
        {
            System.arraycopy ( selection, 0,
                               connections, offset, selection.length );
            offset += selection.length;
        }

        return connections;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();

        sbuf.append ( ClassName.of ( this.getClass () ) );

        final Tag [] maybe_name_tag = this.tags ( Tag.NAME_TAG_NAME );
        if ( maybe_name_tag != null
             && maybe_name_tag.length == 1
             && maybe_name_tag [ 0 ] != null )
        {
            sbuf.append ( "" + maybe_name_tag [ 0 ] );
            return sbuf.toString ();
        }

        final Tag [] maybe_id_tag = this.tags ( Tag.ID_TAG_NAME );
        if ( maybe_id_tag != null
             && maybe_id_tag.length == 1
             && maybe_id_tag [ 0 ] != null )
        {
            sbuf.append ( "" + maybe_id_tag [ 0 ] );
            return sbuf.toString ();
        }

        sbuf.append ( "#" + this.hashCode () );

        return sbuf.toString ();
    }
}
