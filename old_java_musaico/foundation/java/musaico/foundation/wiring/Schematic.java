package musaico.foundation.wiring;

public abstract interface Schematic
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Schematic NONE = new NoSchematic ();


    public abstract Conductor [] conductors ();
    public abstract Schematic addConductors ( Conductor ... conductors );
    public abstract Schematic removeConductors ( Conductor ... conductors );
    public abstract Schematic setConductors ( Conductor ... conductors );

    public abstract Selector [] selectors ();
    public abstract Schematic addSelectors ( Selector ... selectors );
    public abstract Schematic removeSelectors ( Selector ... selectors );
    public abstract Schematic setSelectors ( Selector ... selectors );

    public abstract Schematic [] slots ();
    public abstract Schematic addSlots ( Schematic ... slots );
    public abstract Schematic removeSlots ( Schematic ... slots );
    public abstract Schematic setSlots ( Schematic ... slots );

    public abstract Board build (
            Board ... motherboards
            );

    public abstract String id ();
    public abstract int version ();
}
