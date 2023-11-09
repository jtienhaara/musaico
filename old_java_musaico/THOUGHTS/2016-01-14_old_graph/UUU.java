package musaico.foundation.graph;

import java.util.LinkedHashMap;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


public class TTT
{
    public static void main ( String [] args )
    {
        final Filter<String> any_string =
            new Filter<String> ()
            {
                @Override
                public final FilterState filter (
                                                 String grain
                                                 )
                {
                    if ( grain == null )
                    {
                        return FilterState.DISCARDED;
                    }
                    else
                    {
                        return FilterState.KEPT;
                    }
                }
            };

        StandardArc<String, String> param;

        final LinkedHashMap<String, StandardArc<String, String> []> concatenate_arcs =
            new LinkedHashMap<String, StandardArc<String, String> []> ();

        param = new StandardArc<String, String> ( any_string, "param1" );
        concatenate_arcs.add ( "start", param );

        exit = new StandardArc<String, String> ( any_string, "param2" )
        {
            
        };
        concatenate_arcs.add ( "param1", param );

        final StandardGraph<String, String> concatenate =
            new StandardGraph<String, String> ( String.class,
                                                concatenate_arcs );
    }
}
