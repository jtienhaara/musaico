package musaico.types.primitive.types;

import java.util.Calendar;
import java.util.Date;

import musaico.time.Time;

import musaico.types.Instance;
import musaico.types.TypingEnvironment;
import musaico.types.SimpleTypingEnvironment;
import musaico.types.TypeSystem;
import musaico.types.Type;

import musaico.types.primitive.PrimitiveTypeSystem;

import musaico.types.primitive.types.IntType;
import musaico.types.primitive.types.RealType;
import musaico.types.primitive.types.StringType;
import musaico.types.primitive.types.TimeType;

public class TTT
{
    public static void main ( String [] args ) throws Exception
    {
        TypingEnvironment env = new SimpleTypingEnvironment ();

        PrimitiveTypeSystem.standard ( env );


        Instance instance1 = env.create ( 42 );

        System.out.println ( "Instance1:         "
                             + instance1 );
        System.out.println ( "Type:             "
                             + instance1.type () );
        System.out.println ( "Value:            "
                             + instance1.value () );
        System.out.println ( "Storage class:    "
                             + instance1.type ().storageClass () );
        System.out.println ( "As storage value: "
                             + instance1.value ().getClass ()
                             + " "
                             + instance1.value () );
        System.out.println ( "As Double:        "
                             + instance1.value ( Double.class ).getClass ()
                             + " "
                             + instance1.value ( Double.class ) );
        System.out.println ( "As Float:         "
                             + instance1.value ( Float.class ).getClass ()
                             + " "
                             + instance1.value ( Float.class ) );
        System.out.println ( "As Long:          "
                             + instance1.value ( Long.class ).getClass ()
                             + " "
                             + instance1.value ( Long.class ) );
        System.out.println ( "As Integer:       "
                             + instance1.value ( Integer.class ).getClass ()
                             + " "
                             + instance1.value ( Integer.class ) );
        System.out.println ( "As String:        "
                             + instance1.value ( String.class ).getClass ()
                             + " "
                             + instance1.value ( String.class ) );
        System.out.println ( "As StringType:    "
                             + instance1.value ( env.typeOf ( String.class ) ).getClass ()
                             + " "
                             + instance1.value ( env.typeOf ( String.class ) ) );
        System.out.println ( "" );


        Instance instance2 = env.create ( "0x2A" );

        System.out.println ( "Instance2:         "
                             + instance2 );
        System.out.println ( "Type:             "
                             + instance2.type () );
        System.out.println ( "Value:            "
                             + instance2.value () );
        System.out.println ( "Storage class:    "
                             + instance2.type ().storageClass () );
        System.out.println ( "As storage value: "
                             + instance2.value ().getClass ()
                             + " "
                             + instance2.value () );
        System.out.println ( "As Long:          "
                             + instance2.value ( Long.class ).getClass ()
                             + " "
                             + instance2.value ( Long.class ) );
        System.out.println ( "As Integer:       "
                             + instance2.value ( Integer.class ).getClass ()
                             + " "
                             + instance2.value ( Integer.class ) );
        System.out.println ( "As String:        "
                             + instance2.value ( String.class ).getClass ()
                             + " "
                             + instance2.value ( String.class ) );
        System.out.println ( "As StringType:    "
                             + instance2.value ( env.typeOf ( String.class ) ).getClass ()
                             + " "
                             + instance2.value ( env.typeOf ( String.class ) ) );
        System.out.println ( "" );


        Instance instance3 = env.create ( new Date ( 1234567890987L ) );

        System.out.println ( "Instance3:         "
                             + instance3 );
        System.out.println ( "Type:             "
                             + instance3.type () );
        System.out.println ( "Value:            "
                             + instance3.value () );
        System.out.println ( "Storage class:    "
                             + instance3.type ().storageClass () );
        System.out.println ( "As storage value: "
                             + instance3.value ().getClass ()
                             + " "
                             + instance3.value () );
        System.out.println ( "As Calendar:      "
                             + instance3.value ( Calendar.class ).getClass ()
                             + " "
                             + instance3.value ( Calendar.class ) );
        System.out.println ( "As Date:          "
                             + instance3.value ( Date.class ).getClass ()
                             + " "
                             + instance3.value ( Date.class ) );
        System.out.println ( "As String:        "
                             + instance3.value ( String.class ).getClass ()
                             + " "
                             + instance3.value ( String.class ) );
        System.out.println ( "As StringType:    "
                             + instance3.value ( env.typeOf ( String.class ) ).getClass ()
                             + " "
                             + instance3.value ( env.typeOf ( String.class ) ) );
        System.out.println ( "As Time:          "
                             + instance3.value ( Time.class ).getClass ()
                             + " "
                             + instance3.value ( Time.class ) );
        System.out.println ( "" );


        Instance instance4 =
            env.create ( "1234567890.987654321 seconds UTC" );

        System.out.println ( "Instance4:         "
                             + instance4 );
        System.out.println ( "Type:             "
                             + instance4.type () );
        System.out.println ( "Value:            "
                             + instance4.value () );
        System.out.println ( "Storage class:    "
                             + instance4.type ().storageClass () );
        System.out.println ( "As storage value: "
                             + instance4.value ().getClass ()
                             + " "
                             + instance4.value () );
        System.out.println ( "As Calendar:      "
                             + instance4.value ( Calendar.class ).getClass ()
                             + " "
                             + instance4.value ( Calendar.class ) );
        System.out.println ( "As Date:          "
                             + instance4.value ( Date.class ).getClass ()
                             + " "
                             + instance4.value ( Date.class ) );
        System.out.println ( "As String:        "
                             + instance4.value ( String.class ).getClass ()
                             + " "
                             + instance4.value ( String.class ) );
        System.out.println ( "As StringType:    "
                             + instance4.value ( env.typeOf ( String.class ) ).getClass ()
                             + " "
                             + instance4.value ( env.typeOf ( String.class ) ) );
        System.out.println ( "As Time:          "
                             + instance4.value ( Time.class ).getClass ()
                             + " "
                             + instance4.value ( Time.class ) );
        System.out.println ( "" );
    }
}
