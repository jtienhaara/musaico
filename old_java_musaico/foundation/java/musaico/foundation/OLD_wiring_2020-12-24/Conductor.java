package musaico.foundation.wiring;

public interface Conductor
    extends Tagged<Conductor>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public static final Conductor NONE = new NoConductor ();


    public abstract Conductivity state ( Board board );

    public abstract Conductor on ( Board board );
    public abstract Conductor off ( Board board );

    public abstract Conductor start ( Board board );
    public abstract Conductor stop ( Board board );

    public abstract Carrier [] pull ( Board board, Selector source );
    public abstract void push ( Board board, Selector source, Carrier ... data );

    public abstract Selector [] selectors ( Board board );
}
