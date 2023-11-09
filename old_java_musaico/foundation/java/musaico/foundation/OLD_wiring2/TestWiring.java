package musaico.foundation.wiring;

import java.io.PrintWriter;
import java.io.Reader;


/**
 * <p>
 * !!!
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
public class TestWiring
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static class Capacitor
        implements Wiring.Circuit
    {
        @Override
        public final Wiring.Schematic schematic (
                Wiring wiring
                )
        {
            final Wiring.Type none_type =
                Wiring.Type.none ( wiring );
            final Wiring.Type any_type =
                Wiring.Type.any ( wiring );

            // Capacitor:
            final Wiring.Metadata schematic_metadata =
                new Wiring.Metadata (
                        wiring,        // wiring
                        -1L,           // id
                        wiring.namespaceID, // namespace_id
                        "capacitor"    // name
                        );             // no tags for now
            final Wiring.Schematic schematic =
                new Wiring.Schematic (
                        wiring,        // wiring
                        -1L,           // id
                        schematic_metadata, // metadata
                        this,          // circuit
                        none_type,     // configuration_data_type
                        any_type );    // data_type
            // Namespace for the capacitor's terminals:
            final Wiring.Namespace schematic_namespace =
                new Wiring.Namespace (
                        wiring,            // wiring
                        -1L,               // id
                        schematic_metadata // metadata
                        );

            // Terminal to store new data in the capacitor:
            final Wiring.Metadata store_metadata =
                new Wiring.Metadata (
                        wiring,        // wiring
                        -1L,           // id
                        schematic_namespace, // namespace
                        "store"        // name
                        );             // no tags for now
            final Wiring.Terminal store_terminal =
                new Wiring.Terminal (
                        wiring,        // wiring
                        -1L,           // id
                        store_metadata, // metadata
                        schematic,     // schematic
                        any_type );    // type

            // Terminal to drain data out of the capacitor:
            final Wiring.Metadata drain_metadata =
                new Wiring.Metadata (
                        wiring,        // wiring
                        -1L,           // id
                        schematic_namespace, // namespace
                        "drain"        // name
                        );             // no tags for now
            final Wiring.Terminal drain_terminal =
                new Wiring.Terminal (
                        wiring,        // wiring
                        -1L,           // id
                        drain_metadata, // metadata
                        schematic,     // schematic
                        any_type );    // type

            // Terminal to get a copy of the stored data from the capacitor:
            final Wiring.Metadata out_metadata =
                new Wiring.Metadata (
                        wiring,        // wiring
                        -1L,           // id
                        schematic_namespace, // namespace
                        "out"          // name
                        );             // no tags for now
            final Wiring.Terminal out_terminal =
                new Wiring.Terminal (
                        wiring,        // wiring
                        -1L,           // id
                        out_metadata,  // metadata
                        schematic,     // schematic
                        any_type );    // type

            // Ground terminal, for discarding bad inputs etc:
            final Wiring.Metadata ground_metadata =
                new Wiring.Metadata (
                        wiring,        // wiring
                        -1L,           // id
                        schematic_namespace, // namespace
                        "ground"       // name
                        );             // no tags for now
            final Wiring.Terminal ground_terminal =
                new Wiring.Terminal (
                        wiring,        // wiring
                        -1L,           // id
                        ground_metadata, // metadata
                        schematic,     // schematic
                        any_type );    // type

            wiring.metadataTable.add ( store_metadata );
            wiring.metadataTable.add ( drain_metadata );
            wiring.metadataTable.add ( out_metadata );
            wiring.metadataTable.add ( ground_metadata );
            wiring.metadataTable.add ( schematic_metadata );
            wiring.namespaceTable.add ( schematic_namespace );
            wiring.terminalTable.add ( store_terminal );
            wiring.terminalTable.add ( drain_terminal );
            wiring.terminalTable.add ( out_terminal );
            wiring.terminalTable.add ( ground_terminal );
            wiring.schematicTable.add ( schematic );

            return schematic;
        }

        @Override
        public final Wiring.Circuit start (
                Wiring.Chip chip
                )
        {
            return this;
        }

        @Override
        public final Wiring.Circuit stop (
                Wiring.Chip chip
                )
        {
            return this;
        }

        @Override
        public final Wiring.Carrier [] pull (
                Wiring.Chip chip,
                Wiring.Wire wire
                )
        {
            final Wiring.Leg [] drains = chip.legs ( "drain" );
            final Wiring.Leg [] grounds = chip.legs ( "ground" );

            final Wiring.Carrier old_stored = chip.dataValue ( Wiring.Carrier.class );
            boolean is_drained = false;
            for ( Wiring.Leg drain : drains )
            {
                if ( ! drain.isWired () )
                {
                    continue;
                }

                is_drained = true;
                drain.push ( old_stored );
            }

            // Pull new input Carrier, if any.
            Wiring.Carrier [] last_new_stores = null;
            for ( Wiring.Leg leg : chip.legs ( "store" ) )
            {
                final Wiring.Carrier [] new_stores = leg.pull ();
                last_new_stores = pushNewStores ( new_stores,
                                                  is_drained,
                                                  drains,
                                                  grounds,
                                                  last_new_stores );
            }

            final Wiring.Carrier stored =
                store ( chip,
                        last_new_stores,
                        is_drained,
                        drains,
                        grounds,
                        old_stored );

            if ( stored == null )
            {
                // Nothing more we can do without a stored charge.
                return new Wiring.Carrier [ 0 ];
            }

            final String wire_name = wire.leg ().terminalName ();
            if ( ! "out".equals ( wire_name ) )
            {
                // Nothting more that we can do -- we've already
                // pushed to all the wires we can.
                return new Wiring.Carrier [ 0 ];
            }

            // Push the stored charge out.
            final Wiring.Carrier [] out = new Wiring.Carrier [] { stored };
            return out;
        }

        @Override
        public final Wiring.Circuit push (
                Wiring.Chip chip,
                Wiring.Wire wire,
                Wiring.Carrier [] carriers
                )
        {
            final Wiring.Leg [] drains = chip.legs ( "drain" );
            final Wiring.Leg [] grounds = chip.legs ( "ground" );

            final Wiring.Carrier old_stored = chip.dataValue ( Wiring.Carrier.class );
            boolean is_drained = false;
            for ( Wiring.Leg drain : drains )
            {
                if ( ! drain.isWired () )
                {
                    continue;
                }

                is_drained = true;
                drain.push ( old_stored );
            }

            final String wire_name = wire.leg ().terminalName ();
            if ( ! "store".equals ( wire_name ) )
            {
                // Just ground the pushed data.
                for ( Wiring.Leg ground : grounds )
                {
                    ground.push ( carriers );
                }

                return this;
            }

            // Store the pushed data.
            final Wiring.Carrier [] new_stores =
                pushNewStores ( carriers,
                                is_drained,
                                drains,
                                grounds,
                                null ); // last_new_stores

            final Wiring.Carrier stored =
                store ( chip,
                        new_stores,
                        is_drained,
                        drains,
                        grounds,
                        old_stored );

            // Push the stored charge out.
            final Wiring.Carrier [] out = new Wiring.Carrier [] { stored };
            for ( Wiring.Leg out_leg : chip.legs ( "out" ) )
            {
                out_leg.push ( out );
            }

            return this;
        }

        private final Wiring.Carrier [] pushNewStores (
                Wiring.Carrier [] new_stores,
                boolean is_drained,
                Wiring.Leg [] drains,
                Wiring.Leg [] grounds,
                Wiring.Carrier [] last_new_stores
                )
        {
            if ( new_stores.length == 0 )
            {
                return last_new_stores;
            }

            if ( is_drained )
            {
                for ( Wiring.Leg drain : drains )
                {
                    if ( ! drain.isWired () )
                    {
                        continue;
                    }

                    drain.push ( new_stores );
                }
            }
            else if ( last_new_stores != null )
            {
                for ( Wiring.Leg ground : grounds )
                {
                    ground.push ( last_new_stores );
                }
            }

            return new_stores;
        }

        private final Wiring.Carrier store (
                Wiring.Chip chip,
                Wiring.Carrier [] new_stores,
                boolean is_drained,
                Wiring.Leg [] drains,
                Wiring.Leg [] grounds,
                Wiring.Carrier old_stored
                )
        {
            final Wiring.Carrier stored;
            if ( is_drained )
            {
                chip.dataUpdate ( null );
                stored = null;
            }
            else if ( new_stores == null )
            {
                stored = old_stored;
            }
            else
            {
                if ( old_stored != null )
                {
                    final Wiring.Carrier [] throwaway =
                        new Wiring.Carrier [] { old_stored };
                    for ( Wiring.Leg ground : grounds )
                    {
                        ground.push ( throwaway );
                    }
                }

                stored = new_stores [ new_stores.length - 1 ];
                chip.dataUpdate ( stored );

                if ( new_stores.length > 1
                     && grounds.length > 0 )
                {
                    final Wiring.Carrier [] throwaways =
                        new Wiring.Carrier [ new_stores.length - 1 ];
                    System.arraycopy ( new_stores, 0,
                                       throwaways, 0, new_stores.length - 1 );
                    for ( Wiring.Leg ground : grounds )
                    {
                        ground.push ( throwaways );
                    }
                }
            }

            return stored;
        }
    }


    public static void main (
            String [] args
            )
    {
        final Wiring wiring =
            new Wiring ( new Wiring.NoControlPlane (), // control_plane
                         -1L );                        // parent_namespace_id

        final Wiring.Chip stdin =
            wiring.chip ( new ReadLine (), // circuit
                          null,            // configuration_data_value - default stdin
                          null );          // data_value - default to empty StringBuilder
        final Wiring.Chip capacitor =
            wiring.chip ( new TestWiring.Capacitor (), // circuit
                          null,            // configuration_data_value - not allowed
                          null );          // data_value - no initial store
        final Wiring.Chip stdout =
            wiring.chip ( new WriteLine (), // circuit
                          null,             // configuration_data_value - default stdout
                          null );           // data_value - not allowed

        final Wiring.WireBundle stdin_to_capacitor =
            wiring.wire ( stdin.legs ( "out" ),         // leg0
                          capacitor.legs ( "store" ) ); // leg1
        final Wiring.WireBundle capacitor_to_stdout =
            wiring.wire ( capacitor.legs ( "out" ),     // leg0
                          stdout.legs ( "in" ) );       // leg1

        stdin.start ();
        capacitor.start ();
        stdout.start ();

        System.out.println ( "TestWiring run start..." );
        for ( Wiring.Leg leg : capacitor.legs ( "out" ) )
        {
            leg.pull ();
        }
        System.out.println ( "TestWiring run end" );
    }
}
