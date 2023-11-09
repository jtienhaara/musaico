package musaico.foundation.wiring;

import java.io.Serializable;


public class NoSchematic
    implements Schematic, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Protected constructor.
    // Use Schematic.NONE instead.
    protected NoSchematic ()
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
     * @see musaico.foundation.wiring.Schematic#conductors()
     */
    @Override
    public final Conductor [] conductors ()
    {
        return new Conductor [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Schematic#addConductors(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Schematic addConductors ( Conductor ... conductors )
    {
        // Do nothing.
        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#removeConductors(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Schematic removeConductors ( Conductor ... conductors )
    {
        // Do nothing.
        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#setConductors(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Schematic setConductors ( Conductor ... conductors )
    {
        // Do nothing.
        return this;
    }

    
    /**
     * @see musaico.foundation.wiring.Schematic#selectors()
     */
    @Override
    public final Selector [] selectors ()
    {
        return new Selector [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Schematic#addSelectors(musaico.foundation.wiring.Selector[])
     */
    @Override
    public final Schematic addSelectors ( Selector ... selectors )
    {
        // Do nothing.
        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#removeSelectors(musaico.foundation.wiring.Selector[])
     */
    @Override
    public final Schematic removeSelectors ( Selector ... selectors )
    {
        // Do nothing.
        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#setSelectors(musaico.foundation.wiring.Selector[])
     */
    @Override
    public final Schematic setSelectors ( Selector ... selectors )
    {
        // Do nothing.
        return this;
    }


    /**
     * @see musaico.foundation.wiring.Schematic#slots()
     */
    @Override
    public final Schematic [] slots ()
    {
        return new Schematic [ 0 ];
    }

    /**
     * @see musaico.foundation.wiring.Schematic#addSlots(musaico.foundation.wiring.Schematic[])
     */
    @Override
    public final Schematic addSlots ( Schematic ... slots )
    {
        // Do nothing.
        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#removeSlots(musaico.foundation.wiring.Schematic[])
     */
    @Override
    public final Schematic removeSlots ( Schematic ... slots )
    {
        // Do nothing.
        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#setSlots(musaico.foundation.wiring.Schematic[])
     */
    @Override
    public final Schematic setSlots ( Schematic ... slots )
    {
        // Do nothing.
        return this;
    }


    /**
     * @see musaico.foundation.wiring.Schematic#build(musaico.foundation.wiring.Board[])
     */
    @Override
    public final Board build (
            Board ... motherboards
            )
    {
        return Board.NONE;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "schematic.none";
    }


    /**
     * @see musaico.foundation.wiring.Schematic#id()
     */
    public final String id ()
    {
        return "NO_ID";
    }

    /**
     * @see musaico.foundation.wiring.Schematic#version()
     */
    public final int version ()
    {
        return 0;
    }
}
