// See what happens when we try to re-cast each Number element of a Number []
// array as:
// 1. elements [ e ] = (Integer) elements [ e ];
// 2. elements [ e ] = (Number) elements [ e ];
// 3. elements [ e ] = (Object) elements [ e ];
// 4. elements [ e ] = (Comparable<?>) elements [ e ];
// 5. elements [ e ] = (Comparable<Number>) elements [ e ];
// 6. elements [ e ] = (Serializable) elements [ e ];
// What happens?  Absosmurfly nothing.
import java.io.Serializable;

public class CastingExperiments2<ELEMENT extends Object>
{
    @SuppressWarnings("unchecked") // we're TRYING to cause ClassCastExceptions
    public CastingExperiments2 (
            ELEMENT [] elements
            )
        throws Exception
    {
        final Object [] objects = (Object []) elements;
        for ( int e = 0; e < elements.length; e ++ )
        {
            System.out.println ( "1. elements [ e ] = (Integer) elements [ e ];" );
            objects [ e ] = (Integer) elements [ e ];

            System.out.println ( "2. elements [ e ] = (Number) elements [ e ];" );
            objects [ e ] = (Number) elements [ e ];

            System.out.println ( "3. elements [ e ] = (Object) elements [ e ];" );
            objects [ e ] = (Object) elements [ e ];

            System.out.println ( "4. elements [ e ] = (Comparable<?>) elements [ e ];" );
            objects [ e ] = (Comparable<?>) elements [ e ];

            System.out.println ( "5. elements [ e ] = (Comparable<Number>) elements [ e ];" );
            objects [ e ] = (Comparable<Number>) elements [ e ];

            System.out.println ( "6. elements [ e ] = (Serializable) elements [ e ];" );
            objects [ e ] = (Serializable) elements [ e ];
        }

        try
        {
            System.out.println ( "1. final Integer [] cast = (Integer []) elements;" );
            final Integer [] cast = (Integer []) elements;
        }
        catch ( Exception e )
        {
            System.err.println ( "    " + e.getMessage () );
        }

        try
        {
            System.out.println ( "2. final Number [] cast = (Number []) elements;" );
            final Number [] cast = (Number []) elements;
        }
        catch ( Exception e )
        {
            System.out.println ( "    " + e.getMessage () );
        }

        try
        {
            System.out.println ( "3. final Object [] cast = (Object []) elements;" );
            final Object [] cast = (Object []) elements;
        }
        catch ( Exception e )
        {
            System.out.println ( "    " + e.getMessage () );
        }

        try
        {
            System.out.println ( "4. final Comparable<?> [] cast = (Comparable<?> []) elements;" );
            final Comparable<?> [] cast = (Comparable<?> []) elements;
        }
        catch ( Exception e )
        {
            System.out.println ( "    " + e.getMessage () );
        }

        try
        {
            System.out.println ( "5. final Comparable<Number> [] cast = (Comparable<Number> []) elements;" );
            final Comparable<Number> [] cast = (Comparable<Number> []) elements;
        }
        catch ( Exception e )
        {
            System.out.println ( "    " + e.getMessage () );
        }

        try
        {
            System.out.println ( "6. final Serializable [] cast = (Serializable []) elements;" );
            final Serializable [] cast = (Serializable []) elements;
        }
        catch ( Exception e )
        {
            System.out.println ( "    " + e.getMessage () );
        }
    }


    public static void main (
            String [] args
            )
        throws Exception
    {
        final Number [] elements = new Number [] {
            1
        };

        new CastingExperiments2<Number> ( elements );
    }
}
