package musaico.foundation.wiring;

public abstract interface Board
    extends Tagged<Board>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public abstract Schematic schematic ();
    public abstract void reconcile ();
    public abstract int version ();

    public abstract Conductivity state ();

    public abstract void on ();
    public abstract void off ();

    public abstract void start ();
    public abstract void stop ();

    public abstract Conductor [] conductors ();

    public abstract Conductor [] select ( Selector selector );
    public abstract Selector [] selectedBy ( Conductor selection );
    public abstract Conductor [] selectorOwners ( Selector selector );

    public abstract void updateCarrierTags ( Direction direction, Carrier ... carriers );
    public abstract void sortCarriers ( Carrier ... carriers );
}
