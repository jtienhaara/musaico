package musaico.foundation.wiring;

public enum Conductivity
{
    OFF,
    TURNING_OFF,

    TURNING_ON,
    STOPPED,
    STOPPING,

    STARTING,
    RUNNING,

    BROKEN;


    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;
}
