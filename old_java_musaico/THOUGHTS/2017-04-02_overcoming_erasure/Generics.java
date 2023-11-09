import java.util.HashMap;
import java.util.Map;



public class Generics
{
    @SuppressWarnings("unchecked" )
    public static void main ( String [] args )
    {
        final HashMap<String, Integer> specific =
            new HashMap<String, Integer> ();
        specific.put ( "Test1", 1 );

        final Generic2<Map<?, ?>, HashMap<String, Integer>, String, Integer> magic =
            new Generic2<Map<?, ?>, HashMap<String, Integer>, String, Integer>(
                Map.class,
                HashMap.class,
                String.class,
                Integer.class );

        System.out.println ( "Magic: " + magic );

        System.out.println ( "Original specific: " );
        for ( String key : specific.keySet () )
        {
            final Integer value = specific.get ( key );
            System.out.println ( "  " + key + " -> " + value );
        }

        final Map<?, ?> generic =
            magic.genericize ( specific );

        System.out.println ( "Generic: " );
        for ( Object key : generic.keySet () )
        {
            final Object value = generic.get ( key );
            System.out.println ( "  " + key + " -> " + value );
        }

        try
        {
            // Generates warning:
            ( (Map<Object, Object>) generic ).put ( 5, "Reversed" );
            System.err.println ( "  Did not throw exception WTF!" );
        }
        catch ( ClassCastException e )
        {
            System.out.println ( "  Could not insert bad key + value into generic.  Good." );
        }

        final HashMap<String, Integer> specific2 =
            magic.specificize ( generic );

        System.out.println ( "Final specific: " );
        try
        {
            for ( String key : specific2.keySet () )
            {
                final Integer value = specific2.get ( key );
                System.out.println ( "  " + key + " -> " + value );
            }
        }
        catch ( ClassCastException e )
        {
            System.out.println ( "  Boo.  threw exception after converting to specific." );
        }

        System.out.println ( "End." );
    }
}
