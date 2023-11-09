public class Generic<GENERIC extends Object, SPECIFIC extends Object>
{
    private final Class<?> genericClass;
    private final Class<?> specificClass;

    public Generic (
            Class<?> generic_class,
            Class<?> specific_class
            )
    {
        this.genericClass = generic_class;
        this.specificClass = specific_class;
    }


    public final Class<?> genericClass ()
    {
        return this.genericClass;
    }


    public final Class<?> specificClass ()
    {
        return this.specificClass;
    }


    public final boolean checkGeneric (
        GENERIC object
        )
    {
        if ( ! this.genericClass.isInstance ( object ) )
        {
            return false;
        }
        else if ( ! this.specificClass.isInstance ( object ) )
        {
            return false;
        }

        return true;
    }

    public final boolean checkSpecific (
        SPECIFIC object
        )
    {
        if ( ! this.genericClass.isInstance ( object ) )
        {
            return false;
        }
        else if ( ! this.specificClass.isInstance ( object ) )
        {
            return false;
        }

        return true;
    }


    @SuppressWarnings("unchecked") // Cast-crazy.
    public final GENERIC genericize (
            SPECIFIC specific
            )
    {
        if ( ! this.checkSpecific ( specific ) )
        {
            throw new IllegalArgumentException ( "" + this + ": Not an instance: " + specific );
        }

        return (GENERIC) specific;
    }


    @SuppressWarnings("unchecked") // Cast-crazy.
    public final SPECIFIC specificize (
            GENERIC generic
            )
    {
        if ( ! this.checkGeneric ( generic ) )
        {
            throw new IllegalArgumentException ( "" + this + ": Not an instance: " + generic );
        }

        return (SPECIFIC) generic;
    }
}
