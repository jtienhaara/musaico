package musaico.foundation.wiring;

public abstract interface Board
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Board NONE = new NoBoard ();


    public String id ();

    public abstract Schematic schematic ();
    public abstract int schematicVersion ();

    public abstract Conductor [] conductors ();

    public abstract Board [] motherboards ();
    public abstract Board [] daughterboards ();

    public abstract Wire [] wires ();
    public abstract Wire [] wiresFrom ( Conductor end );
}
