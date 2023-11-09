package musaico.kernel.driver;


import java.util.HashMap;
import java.util.Map;


import musaico.io.Path;
import musaico.io.Reference;
import musaico.io.SoftReference;

import musaico.io.references.SimpleSoftReference;

import musaico.state.Arc;
import musaico.state.Event;
import musaico.state.Graph;
import musaico.state.Machine;
import musaico.state.Node;
import musaico.state.SimpleGraph;
import musaico.state.SimpleNode;
import musaico.state.StringLabel;
import musaico.state.Traversal;
import musaico.state.TraversalException;
import musaico.state.Traverser;


/**
 * <p>
 * Represents the standard graph of a Driver (uninitialized, configured,
 * initialized, ejected, and so on states, and the arcs between them).
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
 * <pre>
 * Copyright (c) 2009 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public final class DriverGraph
    extends SimpleGraph
{
    /** The standard state graph for Drivers. */
    public static final Graph GRAPH = new DriverGraph ();


    /** Empty request command, always does nothing but does not fail. */
    public static final SoftReference<Path> NOOP =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/noop" ) );
    /** Invalid request command, always generates a Failure.INVALID_COMMAND. */
    public static final SoftReference<Path> FAIL =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/fail" ) );

    /** Request command: Configure the Driver.  Once before initializing,
     *                   and optionally afterwards to re-configure.
     *                   Overrides any previously configured settings. */
    public static final SoftReference<Path> CONFIGURE =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/configure" ) );
    /** Request command: Auto-configure the Driver.  Once before initializing,
     *                   and optionally afterwards to re-configure.
     *                   Overrides any previously configured settings. */
    public static final SoftReference<Path> AUTO_CONFIGURE =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/auto_configure" ) );

    /** Request command: Load the Driver's resources into memory. */
    public static final SoftReference<Path> INITIALIZE =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/initialize" ) );
    /** Request command: Shutdown, cleaning up along the way. */
    public static final SoftReference<Path> SHUTDOWN =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/shutdown" ) );

    /** Request command: Increment the reference count for this Driver. */
    public static final SoftReference<Path> OPEN =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/open" ) );
    /** Request command: Decrement the reference count for this Driver. */
    public static final SoftReference<Path> CLOSE =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/close" ) );

    /** Request command: Read fields from the Driver. */
    public static final SoftReference<Path> READ =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/read" ) );
    /** Request command: Write fields to the Driver. */
    public static final SoftReference<Path> WRITE =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/write" ) );

    /** Request command: Seek to a particular position and store that position
     *                   in the connection. */
    public static final SoftReference<Path> SEEK =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/seek" ) );

    /** Request command: Start a transaction with the driver. */
    public static final SoftReference<Path> TRANSACTION_START =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/transaction_start" ) );
    /** Request command: Prepare to commit a transaction with
     *                   the driver, after all the transactional
     *                   requests have been made. */
    public static final SoftReference<Path> TRANSACTION_PREPARE_COMMIT =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/transaction_prepare_commit" ) );
    /** Request command: Successfully complete a transaction
     *                   with the driver, after "prepare to
     *                   "commit" has already been requested. */
    public static final SoftReference<Path> TRANSACTION_COMMIT =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/transaction_commit" ) );
    /** Request command: Rollback a transaction with the
     *                   driver, any time after "transaction
     *                   start" has been requested but before
     *                   "transaction commit" has been
     *                   requested. */
    public static final SoftReference<Path> TRANSACTION_ROLLBACK =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/transaction_rollback" ) );

    /** Request command: Flush buffers for this Driver.  Blocks even if
     *                   opened in non-blocking mode. */
    public static final SoftReference<Path> FLUSH =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/flush" ) );

    /** Request command: Returns the size of sectors for the driver. */
    public static final SoftReference<Path> SECTOR_SIZE =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/sector_size" ) );
    /** Request command: Return field with number of sectors in the Driver. */
    public static final SoftReference<Path> SECTOR_COUNT =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/sector_count" ) );
    /** Request command: Return field with suggested # of sectors to read. */
    public static final SoftReference<Path> SECTOR_READAHEAD =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/sector_readahead" ) );
    /** Request command: Search for the sector containing a set of fields.
     *                   Store that sector in the connection. */
    public static final SoftReference<Path> SECTOR_SEARCH =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/sector_search" ) );

    /** Request command: Suspend requests until further notice. */
    public static final SoftReference<Path> SUSPEND =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/suspend" ) );
    /** Request command: Resume requests. */
    public static final SoftReference<Path> RESUME =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/resume" ) );

    /** Request command: Eject the Driver's media (CD, floppy disk, ...). */
    public static final SoftReference<Path> EJECT =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/eject" ) );
    /** Request command: Insert the Driver's media (CD, floppy disk, ...). */
    public static final SoftReference<Path> INSERT =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/insert" ) );

    /** Request command: Driver-defined request command. */
    public static final SoftReference<Path> SPECIAL =
        new SimpleSoftReference<Path> ( new Path ( "/driver/request/special" ) );


    /** Default commands handled by Drivers.
     *  Deliberately excludes "pre" and "post" commands, which are
     *  automagically triggered by the Driver itself. */
    public static final SoftReference [] COMMANDS =
        new SoftReference []
    {
        DriverGraph.NOOP,
        DriverGraph.FAIL,

        DriverGraph.CONFIGURE,
        DriverGraph.AUTO_CONFIGURE,

        DriverGraph.INITIALIZE,
        DriverGraph.SHUTDOWN,

        DriverGraph.OPEN,
        DriverGraph.CLOSE,

        DriverGraph.READ,
        DriverGraph.WRITE,

        DriverGraph.TRANSACTION_START,
        DriverGraph.TRANSACTION_PREPARE_COMMIT,
        DriverGraph.TRANSACTION_COMMIT,
        DriverGraph.TRANSACTION_ROLLBACK,

        DriverGraph.FLUSH,

        DriverGraph.SECTOR_COUNT,
        DriverGraph.SECTOR_READAHEAD,
        DriverGraph.SEEK,
        DriverGraph.SECTOR_SEARCH,

        DriverGraph.SUSPEND,
        DriverGraph.RESUME,

        DriverGraph.EJECT,
        DriverGraph.INSERT,

        DriverGraph.SPECIAL
    };




    /** Executed whenever an error occurs. */
    private Traverser errorTransition;

    /** Lookup of Node/NodeBuilder by Driver.XYZ identifier. */
    private final Map<Reference,SimpleNode> nodes;


    /**
     * <p>
     * Creates the standard state graph for drivers.
     * </p>
     */
    protected DriverGraph ()
    {
        super ( "driver" );

        this.errorTransition = this.createErrorTraverser ();
        this.nodes = createNodesLookup ();
        this.initialize ();
    }


    /**
     * <p>
     * Creates a Traverser to generate errors (for example,
     * by throwing a TraversalException while attempting to
     * cross an arc).
     * </p>
     */
    protected Traverser createErrorTraverser ()
    {
        return new Traverser ()
            {
                public void execute (
                                     Traversal transition
                                     )
                    throws TraversalException
                {
                    final Node from_state = transition.arc ().source ();
                    final Reference trigger = transition.label ();

                    Reference [] exit_state_labels = from_state.labels ();

                    throw new TraversalException ( "Error during transition [%transition%]: cannot execute [%trigger%] while in state [%from_state%].  Valid exit states: [%exit_state_labels_array%]",
                                                   "transition", transition,
                                                   "trigger", trigger,
                                                   "from_state", from_state,
                                                   "exit_state_labels_array", exit_state_labels );
                }
            };
    }


    /**
     * <p>
     * Creates the map of Nodes/NodeBuilders by Driver.XYZ Reference lookups.
     * </p>
     */
    protected Map<Reference,SimpleNode> createNodesLookup ()
    {
        Map<Reference,SimpleNode> nodes_lookup =
            new HashMap<Reference,SimpleNode> ();

        nodes_lookup.put ( Driver.UNINITIALIZED,
                           new SimpleNode ( "" + Driver.UNINITIALIZED ) );
        nodes_lookup.put ( Driver.CONFIGURED,
                           new SimpleNode ( "" + Driver.CONFIGURED ) );
        nodes_lookup.put ( Driver.AUTO_CONFIGURED,
                           new SimpleNode ( "" + Driver.AUTO_CONFIGURED ) );
        nodes_lookup.put ( Driver.INITIALIZED,
                           new SimpleNode ( "" + Driver.INITIALIZED ) );

        nodes_lookup.put ( SuspendableDriver.SUSPENDED,
                           new SimpleNode ( "" + SuspendableDriver.SUSPENDED ) );
        nodes_lookup.put ( EjectableDriver.EJECTED,
                           new SimpleNode ( "" + EjectableDriver.EJECTED ) );

        return nodes_lookup;
    }


    /**
     * <p>
     * Initializes this state graph for drivers.
     * </p>
     */
    protected DriverGraph initialize ()
    {
        // Initially: go to uninitialized state.
        this.onEnter ().go ( this.nodes.get ( Driver.UNINITIALIZED ) );

        // UNINITIALIZED      -> CONFIGURED
        this.nodes.get ( Driver.UNINITIALIZED )
            .on ( DriverGraph.CONFIGURE )
            .go ( this.nodes.get ( Driver.CONFIGURED ) );
        //                    -> AUTO_CONFIGURED
        this.nodes.get ( Driver.UNINITIALIZED )
            .on ( DriverGraph.AUTO_CONFIGURE )
            .go ( this.nodes.get ( Driver.AUTO_CONFIGURED ) );
        //                    otherwise ERROR
        this.nodes.get ( Driver.UNINITIALIZED )
            .otherwise ()
            .executes ( this.errorTransition )
            .build ();

        // CONFIGURED         -> INITIALIZED
        this.nodes.get ( Driver.CONFIGURED )
            .on ( DriverGraph.INITIALIZE )
            .go ( this.nodes.get ( Driver.INITIALIZED ) );
        //                    -> AUTO_CONFIGURED
        this.nodes.get ( Driver.CONFIGURED )
            .on ( DriverGraph.AUTO_CONFIGURE )
            .go ( this.nodes.get ( Driver.AUTO_CONFIGURED ) );
        //                    otherwise ERROR
        this.nodes.get ( Driver.CONFIGURED )
            .otherwise ()
            .executes ( this.errorTransition )
            .build ();

        // AUTO_CONFIGURED    -> INITIALIZED
        this.nodes.get ( Driver.AUTO_CONFIGURED )
            .on ( DriverGraph.INITIALIZE )
            .go ( this.nodes.get ( Driver.INITIALIZED ) );
        //                    -> CONFIGURED
        this.nodes.get ( Driver.AUTO_CONFIGURED )
            .on ( DriverGraph.CONFIGURE )
            .go ( this.nodes.get ( Driver.CONFIGURED ) );
        //                    otherwise ERROR
        this.nodes.get ( Driver.AUTO_CONFIGURED )
            .otherwise ()
            .executes ( this.errorTransition )
            .build ();

        // INITIALIZED        -> SHUTDOWN
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.SHUTDOWN )
            .go ( this.nodes.get ( Driver.CONFIGURED ) );
        //                    -> SUSPENDED
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.SUSPEND )
            .go ( this.nodes.get ( SuspendableDriver.SUSPENDED ) );
        //                    -> EJECTED
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.EJECT )
            .go ( this.nodes.get ( EjectableDriver.EJECTED ) );
        //                    read ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.READ )
            .build ();
        //                    write ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.WRITE )
            .build ();
        //                    sector_size ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.SECTOR_SIZE )
            .build ();
        //                    sector_count ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.SECTOR_COUNT )
            .build ();
        //                    sector_search ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.SECTOR_SEARCH )
            .build ();
        //                    seek ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.SEEK )
            .build ();
        //                    transaction_start ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.TRANSACTION_START )
            .build ();
        //                    transaction_prepare_to_commit ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.TRANSACTION_PREPARE_COMMIT )
            .build ();
        //                    transaction_commit ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.TRANSACTION_COMMIT )
            .build ();
        //                    transaction_rollback ()
        this.nodes.get ( Driver.INITIALIZED )
            .on ( DriverGraph.TRANSACTION_ROLLBACK )
            .build ();
        //                    otherwise ERROR
        this.nodes.get ( Driver.INITIALIZED )
            .otherwise ()
            .executes ( this.errorTransition )
            .build ();

        // SUSPENDED          -> INITIALIZED
        this.nodes.get ( SuspendableDriver.SUSPENDED )
            .on ( DriverGraph.RESUME )
            .go ( this.nodes.get ( Driver.INITIALIZED ) );
        //                    -> CONFIGURED
        this.nodes.get ( SuspendableDriver.SUSPENDED )
            .on ( DriverGraph.SHUTDOWN )
            .go ( this.nodes.get ( Driver.CONFIGURED ) );
        //                    otherwise ERROR
        this.nodes.get ( SuspendableDriver.SUSPENDED )
            .otherwise ()
            .executes ( this.errorTransition )
            .build ();

        // EJECTED            -> INITIALIZED
        this.nodes.get ( EjectableDriver.EJECTED )
            .on ( DriverGraph.INSERT )
            .go ( this.nodes.get ( Driver.INITIALIZED ) );
        //                    -> CONFIGURED
        this.nodes.get ( EjectableDriver.EJECTED )
            .on ( DriverGraph.SHUTDOWN )
            .go ( this.nodes.get ( Driver.CONFIGURED ) );
        //                    -> SUSPENDED
        this.nodes.get ( EjectableDriver.EJECTED )
            .on ( DriverGraph.SUSPEND )
            .go ( this.nodes.get ( SuspendableDriver.SUSPENDED ) );
        //                    otherwise ERROR
        this.nodes.get ( EjectableDriver.EJECTED )
            .otherwise ()
            .executes ( this.errorTransition )
            .build ();

        return this;
    }
}
