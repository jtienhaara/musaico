package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;

import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.wiring.Conductor;


public class SpecificConductorsSelector
    extends StandardSelector
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Conductor [] ends;

    public SpecificConductorsSelector (
            Conductor ... ends
            )
    {
        super ( createEndFilters ( ends ) );

        this.ends = SpecificConductorsSelector.fixEnds ( ends );
    }

    private static final Conductor [] fixEnds (
            Conductor ... ends
            )
    {
        if ( ends == null
             || ends.length == 0 )
        {
            return new Conductor [ 0 ];
        }

        final Conductor [] fixed_ends = new Conductor [ ends.length ];
        System.arraycopy ( ends, 0,
                           fixed_ends, 0, ends.length );

        for ( int fe = 0; fe < fixed_ends.length; fe ++ )
        {
            if ( fixed_ends [ fe ] == null )
            {
                fixed_ends [ fe ] = Conductor.NONE;
            }
        }

        return fixed_ends;
    }

    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array Filter [],
                                                 // Cast Filter [] - Filter<Conductor> [],
    private static final Filter<Conductor> [] createEndFilters (
            Conductor ... ends
            )
    {
        final Conductor [] fixed_ends =
            SpecificConductorsSelector.fixEnds ( ends );

        // SuppressWarnings({"rawtypes", "unchecked"}) Generic array Filter [],
        //                                             Cast Filter [] - Filter<Conductor> [].
        final Filter<Conductor> [] end_filters = (Filter<Conductor> [])
            new Filter [ fixed_ends.length ];
        for ( int ef = 0; ef < fixed_ends.length; ef ++ )
        {
            end_filters [ ef ] = new EqualTo<Conductor> ( fixed_ends [ ef ] );
        }

        return end_filters;
    }

    public SpecificConductorsSelector (
            Collection<Conductor> ends
            )
    {
        this ( collectionToArray ( ends ) );
    }

    private static final Conductor [] collectionToArray (
            Collection<Conductor> ends
            )
    {
        if ( ends == null )
        {
            return null;
        }

        final Conductor [] template =
            new Conductor [ ends.size () ];
        final Conductor [] ends_array = ends.toArray ( template );

        return ends_array;
    }
}
