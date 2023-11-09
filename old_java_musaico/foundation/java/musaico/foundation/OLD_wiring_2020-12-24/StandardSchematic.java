package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


public class StandardSchematic
    implements Schematic, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Serializable lock = new String ( "lock" );
    private final List<Conductor> conductors = new ArrayList<Conductor> ();
    private int version = 0;

    @Override
    public final Schematic add ( Conductor ... conductors )
    {
        if ( conductors == null
             || conductors.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Conductor conductor : conductors )
            {
                this.conductors.add ( conductor );
            }

            this.version ++;
        }

        return this;
    }

    @Override
    public Board build ()
    {
        final StandardBoard board =
            new StandardBoard ( this,
                                this.lock );
        return board;
    }

    @Override
    public final Conductor [] conductors ()
    {
        final Conductor [] template = new Conductor [ this.conductors.size () ];
        final Conductor [] conductors = this.conductors.toArray ( template );
        return conductors;
    }

    @Override
    public final Schematic remove ( Conductor ... conductors )
    {
        if ( conductors == null
             || conductors.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            boolean is_changed = false;
            for ( Conductor conductor : conductors )
            {
                is_changed |= this.conductors.remove ( conductor );
            }

            if ( is_changed )
            {
                this.version ++;
            }
        }

        return this;
    }

    @Override
    public final Schematic set ( Conductor ... conductors )
    {
        if ( conductors == null
             || conductors.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            this.conductors.clear ();
            for ( Conductor conductor : conductors )
            {
                this.conductors.add ( conductor );
            }

            this.version ++;
        }

        return this;
    }

    @Override
    public final int version ()
    {
        synchronized ( this.lock )
        {
            return this.version;
        }
    }
}
