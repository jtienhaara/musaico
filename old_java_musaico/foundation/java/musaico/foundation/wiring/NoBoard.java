package musaico.foundation.wiring;

import java.io.Serializable;


public class NoBoard
    implements Board, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Protected constructor.
    // Use Board.NONE instead.
    protected NoBoard ()
    {
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 0;
    }

    /**
     * @see musaico.foundation.wiring.Board#id()
     */
    public final String id ()
    {
        return "NO_ID";
    }

    /**
     * @see musaico.foundation.wiring.Board#schematic()
     */
    @Override
    public final Schematic schematic ()
    {
        return Schematic.NONE;
    }

    /**
     * @see musaico.foundation.wiring.Board#schematicVersion()
     */
    public final int schematicVersion ()
    {
        return 0;
    }

    /**
     * @see musaico.foundation.wiring.Board#conductors()
     */
    @Override
    public final Conductor [] conductors ()
    {
        return new Conductor [ 0 ];
    }


    /**
     * @see musaico.foundation.wiring.Board#motherboards()
     */
    @Override
    public final Board [] motherboards ()
    {
        return new Board [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Board#daughterboards()
     */
    @Override
    public final Board [] daughterboards ()
    {
        return new Board [ 0 ];
    }


    /**
     * @see musaico.foundation.wiring.Board#wires()
     */
    @Override
    public final Wire [] wires ()
    {
        return new Wire [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Board#wiresFrom(musaico.foundation.wiring.Conductor)
     */
    @Override
    public final Wire [] wiresFrom ( Conductor end )
    {
        return new Wire [ 0 ];
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "board.none";
    }
}
