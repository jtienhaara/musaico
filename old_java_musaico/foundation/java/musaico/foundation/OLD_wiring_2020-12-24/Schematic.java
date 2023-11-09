package musaico.foundation.wiring;

public abstract interface Schematic
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public abstract Schematic add ( Conductor ... conductors );
    public abstract Board build ();
    public abstract Conductor [] conductors ();
    public abstract Schematic remove ( Conductor ... conductors );
    public abstract Schematic set ( Conductor ... conductors );
    public abstract int version ();
}
