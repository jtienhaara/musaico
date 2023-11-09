package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.DiscardAll;

import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.filter.membership.MemberOf;


public class FixedSelector
    extends StandardSelector
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Conductor [] fixation;

    public FixedSelector (
            Conductor ... fixation
            )
    {
        super ( createFixedFilter ( fixation ) );

        this.fixation = FixedSelector.fixFixation ( fixation );
    }

    private static final Conductor [] fixFixation (
            Conductor ... fixation
            )
    {
        if ( fixation == null
             || fixation.length == 0 )
        {
            return new Conductor [ 0 ];
        }

        int num_none_conductors = 0;
        final Conductor [] fixed_fixation = new Conductor [ fixation.length ];
        System.arraycopy ( fixation, 0,
                           fixed_fixation, 0, fixation.length );

        for ( int f = 0; f < fixed_fixation.length; f ++ )
        {
            if ( fixed_fixation [ f ] == null )
            {
                fixed_fixation [ f ] = Conductor.NONE;
                num_none_conductors ++;
            }
        }

        if ( num_none_conductors == fixed_fixation.length )
        {
            return new Conductor [ 0 ];
        }

        return fixed_fixation;
    }

    private static final Filter<Conductor> createFixedFilter (
            Conductor ... fixation
            )
    {
        final Conductor [] fixed_fixation =
            FixedSelector.fixFixation ( fixation );

        if ( fixed_fixation.length == 0 )
        {
            return new DiscardAll<Conductor> ();
        }
        else if ( fixed_fixation.length == 1 )
        {
            return new EqualTo<Conductor> ( fixed_fixation [ 0 ] );
        }
        else
        {
            return new MemberOf<Conductor> ( fixed_fixation );
        }
    }

    public FixedSelector (
            Collection<Conductor> fixation
            )
    {
        this ( collectionToArray ( fixation ) );
    }

    private static final Conductor [] collectionToArray (
            Collection<Conductor> fixation
            )
    {
        if ( fixation == null )
        {
            return null;
        }

        final Conductor [] template =
            new Conductor [ fixation.size () ];
        final Conductor [] fixation_array = fixation.toArray ( template );

        return fixation_array;
    }
}
