package musaico.state;

import java.util.HashMap;
import java.util.Map;

public class TestQuickie
{
    /** Empty request command, always does nothing but does not fail. */
    public static final int NOOP                          =  0;
    /** Invalid request command, always generates a Failure.INVALID_COMMAND. */
    public static final int FAIL                          =  1;

    /** Request command: Configure the Driver.  Once before initializing,
     *                   and optionally afterwards to re-configure.
     *                   Overrides any previously configured settings. */
    public static final int CONFIGURE                     =  2;
    /** Request command: Auto-configure the Driver.  Once before initializing,
     *                   and optionally afterwards to re-configure.
     *                   Overrides any previously configured settings. */
    public static final int AUTO_CONFIGURE                =  3;

    /** Request command: Load the Driver's resources into memory. */
    public static final int INITIALIZE                    =  4;
    /** Request command: Shutdown, cleaning up along the way. */
    public static final int SHUTDOWN                      =  5;

    /** Request command: A read operation request. */
    public static final int READ                          =  6;
    /** Request command: A write operation request. */
    public static final int WRITE                         =  7;

    /** Request command: Suspend requests until further notice. */
    public static final int SUSPEND                       =  8;
    /** Request command: Resume requests. */
    public static final int RESUME                        =  9;

    /** Request command: Eject the Driver's media (CD, floppy disk, ...). */
    public static final int EJECT                         = 10;
    public static final int INSERT                        = 11;
    /** Request command: Driver-defined request command. */
    public static final int SPECIAL                       = 12;



    public static void main ( String [] args )
        throws Exception
    {
        SimpleGraph driver_graph = new SimpleGraph ( "driver" );

        SimpleState uninitialized = (SimpleState)
            driver_graph.state ( "uninitialized" );
        SimpleState configured = (SimpleState)
            driver_graph.state ( "configured" );
        SimpleState autoconfigured = (SimpleState)
            driver_graph.state ( "autoconfigured" );
        SimpleState initialized = (SimpleState)
            driver_graph.state ( "initialized" );
        SimpleState suspended = (SimpleState)
            driver_graph.state ( "suspended" );
        SimpleState ejected = (SimpleState)
            driver_graph.state ( "ejected" );

        Transitioner error = new Transitioner ()
            {
                public void transition (
                                        Machine machine,
                                        State from_state,
                                        State to_state,
                                        Transition transition
                                        )
                {
                    System.out.println ( "--> Error." );
                }
            };

        Transitioner read = new Transitioner ()
            {
                public void transition (
                                        Machine machine,
                                        State from_state,
                                        State to_state,
                                        Transition transition
                                        )
                {
                    System.out.println ( "--> Read." );
                }
            };

        Transitioner write = new Transitioner ()
            {
                public void run ()
                {
                    System.out.println ( "--> Write." );
                }
            };

        driver_graph.onEnter ().go ( uninitialized );

        uninitialized.on ( TestQuickie.CONFIGURE ).go ( configured );
        uninitialized.on ( TestQuickie.AUTO_CONFIGURE ).go ( autoconfigured );
        uninitialized.otherwise ().execute ( error );

        configured.on ( TestQuickie.INITIALIZE ).go ( initialized );
        configured.on ( TestQuickie.AUTO_CONFIGURE ).go ( autoconfigured );
        configured.otherwise ().execute ( error );

        autoconfigured.on ( TestQuickie.INITIALIZE ).go ( initialized );
        autoconfigured.on ( TestQuickie.CONFIGURE ).go ( configured );
        autoconfigured.otherwise ().execute ( error );

        initialized.on ( TestQuickie.SHUTDOWN ).go ( configured );
        initialized.on ( TestQuickie.SUSPEND ).go ( suspended );
        initialized.on ( TestQuickie.EJECT ).go ( ejected );
        initialized.on ( TestQuickie.READ ).execute ( read );
        initialized.on ( TestQuickie.WRITE ).execute ( write );
        initialized.otherwise ().execute ( error );

        suspended.on ( TestQuickie.RESUME ).go ( initialized );
        suspended.on ( TestQuickie.SHUTDOWN ).go ( configured );
        suspended.otherwise ().execute ( error );

        ejected.on ( TestQuickie.INSERT ).go ( initialized );
        ejected.on ( TestQuickie.SHUTDOWN ).go ( configured );
        ejected.on ( TestQuickie.SUSPEND ).go ( suspended );
        ejected.otherwise ().execute ( error );


        Map<String,Integer> commands = new HashMap<String,Integer> ();
        commands.put ( "noop", TestQuickie.NOOP );
        commands.put ( "fail", TestQuickie.FAIL );
        commands.put ( "configure", TestQuickie.CONFIGURE );
        commands.put ( "auto_configure", TestQuickie.AUTO_CONFIGURE );
        commands.put ( "initialize", TestQuickie.INITIALIZE );
        commands.put ( "shutdown", TestQuickie.SHUTDOWN );
        commands.put ( "read", TestQuickie.READ );
        commands.put ( "write", TestQuickie.WRITE );
        commands.put ( "suspend", TestQuickie.SUSPEND );
        commands.put ( "resume", TestQuickie.RESUME );
        commands.put ( "eject", TestQuickie.EJECT );
        commands.put ( "insert", TestQuickie.INSERT );
        commands.put ( "special", TestQuickie.SPECIAL );


        Machine driver_machine = new SimpleMachine ( driver_graph );
        driver_machine.transition ();

        for ( int a = 0; a < args.length; a ++ )
        {
            State state = driver_machine.state ();
            String driver_state = "" + state;

            System.out.println ( "State:   " + driver_state );

            String command_str = args [ a ];
            int command = commands.get ( command_str );

            System.out.println ( "Command: " + command_str );

            driver_machine.transition ( command );
            driver_machine.transition ();
        }

        State state = driver_machine.state ();
        String driver_state = "" + state;

        System.out.println ( "State:   " + driver_state );
    }
}
