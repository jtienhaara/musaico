package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.LinkedHashSet;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.Selector;


public class StandardSchematic
    implements Schematic, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final String id;

    private final Serializable lock = new String ( "lock" );
    private int version = 1;
    private int lastBuiltVersion = 0;
    private final LinkedHashSet<Conductor> conductors =
        new LinkedHashSet<Conductor> ();
    private final LinkedHashSet<Selector> selectors =
        new LinkedHashSet<Selector> ();
    private final LinkedHashSet<Schematic> slots =
        new LinkedHashSet<Schematic> ();



    public StandardSchematic (
            String id
            )
    {
        if ( id == null )
        {
            this.id = "NO_ID";
        }
        else
        {
            this.id = id;
        }
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
        return super.hashCode ();
    }

    /**
     * @see musaico.foundation.wiring.Schematic#conductors()
     */
    @Override
    public final Conductor [] conductors ()
    {
        final Conductor [] conductors;
        synchronized ( this.lock )
        {
            final Conductor [] template = new Conductor [ this.conductors.size () ];
            conductors = this.conductors.toArray ( template );
        }

        return conductors;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#addConductors(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Schematic addConductors ( Conductor ... conductors )
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
                if ( conductor == null )
                {
                    this.conductors.add ( Conductor.NONE );
                }
                else
                {
                    this.conductors.add ( conductor );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#removeConductors(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Schematic removeConductors ( Conductor ... conductors )
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
                if ( conductor == null )
                {
                    this.conductors.remove ( Conductor.NONE );
                }
                else
                {
                    this.conductors.remove ( conductor );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#setConductors(musaico.foundation.wiring.Conductor[])
     */
    @Override
    public final Schematic setConductors ( Conductor ... conductors )
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
                if ( conductor == null )
                {
                    this.conductors.add ( Conductor.NONE );
                }
                else
                {
                    this.conductors.add ( conductor );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    
    /**
     * @see musaico.foundation.wiring.Schematic#selectors()
     */
    @Override
    public final Selector [] selectors ()
    {
        final Selector [] selectors;
        synchronized ( this.lock )
        {
            final Selector [] template = new Selector [ this.selectors.size () ];
            selectors = this.selectors.toArray ( template );
        }

        return selectors;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#addSelectors(musaico.foundation.wiring.Selector[])
     */
    @Override
    public final Schematic addSelectors ( Selector ... selectors )
    {
        if ( selectors == null
             || selectors.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Selector selector : selectors )
            {
                if ( selector == null )
                {
                    this.selectors.add ( Selector.NONE );
                }
                else
                {
                    this.selectors.add ( selector );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#removeSelectors(musaico.foundation.wiring.Selector[])
     */
    @Override
    public final Schematic removeSelectors ( Selector ... selectors )
    {
        if ( selectors == null
             || selectors.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Selector selector : selectors )
            {
                if ( selector == null )
                {
                    this.selectors.add ( Selector.NONE );
                }
                else
                {
                    this.selectors.add ( selector );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#setSelectors(musaico.foundation.wiring.Selector[])
     */
    @Override
    public final Schematic setSelectors ( Selector ... selectors )
    {
        if ( selectors == null
             || selectors.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Selector selector : selectors )
            {
                if ( selector == null )
                {
                    this.selectors.add ( Selector.NONE );
                }
                else
                {
                    this.selectors.add ( selector );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.wiring.Schematic#slots()
     */
    @Override
    public final Schematic [] slots ()
    {
        final Schematic [] slots;
        synchronized ( this.lock )
        {
            final Schematic [] template = new Schematic [ this.slots.size () ];
            slots = this.slots.toArray ( template );
        }

        return slots;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#addSlots(musaico.foundation.wiring.Schematic[])
     */
    @Override
    public final Schematic addSlots ( Schematic ... slots )
    {
        if ( slots == null
             || slots.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Schematic slot : slots )
            {
                if ( slot == null )
                {
                    this.slots.add ( Schematic.NONE );
                }
                else
                {
                    this.slots.add ( slot );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#removeSlots(musaico.foundation.wiring.Schematic[])
     */
    @Override
    public final Schematic removeSlots ( Schematic ... slots )
    {
        if ( slots == null
             || slots.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Schematic slot : slots )
            {
                if ( slot == null )
                {
                    this.slots.add ( Schematic.NONE );
                }
                else
                {
                    this.slots.add ( slot );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

        return this;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#setSlots(musaico.foundation.wiring.Schematic[])
     */
    @Override
    public final Schematic setSlots ( Schematic ... slots )
    {
        if ( slots == null
             || slots.length == 0 )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            for ( Schematic slot : slots )
            {
                if ( slot == null )
                {
                    this.slots.add ( Schematic.NONE );
                }
                else
                {
                    this.slots.add ( slot );
                }
            }

            if ( this.version == this.lastBuiltVersion )
            {
                this.version ++;
            }
        }

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
        final int version;
        final Conductor [] conductors;
        final Selector [] selectors;
        final Schematic [] slots;
        synchronized ( this.lock )
        {
            version = this.version;

            final Conductor [] conductors_template =
                new Conductor [ this.conductors.size () ];
            conductors = this.conductors.toArray ( conductors_template );
            final Selector [] selectors_template =
                new Selector [ this.selectors.size () ];
            selectors = this.selectors.toArray ( selectors_template );
            final Schematic [] slots_template =
                new Schematic [ this.slots.size () ];
            slots = this.slots.toArray ( slots_template );

            this.lastBuiltVersion = this.version;
        }

        final Board board =
            new StandardBoard (
                this.id,             // id
                this,                // schematic
                version,             // schematic_version
                conductors,          // conductors
                selectors,           // selectors
                slots,               // slots
                motherboards );      // motherboards

        return board;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "schematic [ " + this.id + " ]";
    }


    /**
     * @see musaico.foundation.wiring.Schematic#id()
     */
    public final String id ()
    {
        return this.id;
    }

    /**
     * @see musaico.foundation.wiring.Schematic#version()
     */
    public final int version ()
    {
        final int version;
        synchronized ( this.lock )
        {
            version = this.version;
        }

        return this.version;
    }
}
