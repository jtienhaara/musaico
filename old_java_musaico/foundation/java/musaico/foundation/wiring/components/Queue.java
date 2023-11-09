package musaico.foundation.wiring.components;

import java.io.Serializable;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.NoCarrier;
import musaico.foundation.wiring.Wire;


public class Queue
    extends AbstractInOut
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final String ID = "queue";


    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution DATA ...
    public <DATA extends Object> Queue (
            Class<DATA> data_type,
            DATA ... data
            )
    {
        this ( Queue.ID,
               data_type,
               data );
    }

    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution DATA ...
    public <DATA extends Object> Queue (
            String id,
            Class<DATA> data_type,
            DATA ... data
            )
    {
        super ( id,
                data_type,
                data );
    }

    public Queue (
            Carrier ... carriers
            )
    {
        this ( Queue.ID,
               carriers );
    }

    public Queue (
            String id,
            Carrier ... carriers
            )
    {
        super ( id,
                carriers );
    }

    protected final Carrier [] out (
            Carrier [] in_carriers
            )
    {
        if ( in_carriers == null
             || in_carriers.length < 1 )
        {
            return new Carrier [ 0 ];
        }

        final int index = in_carriers.length - 1;
        final Carrier [] out = new Carrier [] { in_carriers [ index ] };

        return out;
    }
}
