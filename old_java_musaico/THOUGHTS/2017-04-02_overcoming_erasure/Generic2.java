public class Generic2<GENERIC extends Object, SPECIFIC extends Object, PARAMETER1 extends Object, PARAMETER2 extends Object>
    extends Generic<GENERIC, SPECIFIC>
{
    private final Class<?> parameter1Class;
    private final Class<?> parameter2Class;

    public Generic2 (
            Class<?> generic_class,
            Class<?> specific_class,
            Class<?> parameter1_class,
            Class<?> parameter2_class
            )
    {
        super ( generic_class, specific_class );

        this.parameter1Class = parameter1_class;
        this.parameter2Class = parameter2_class;
    }


    public final Class<?> parameter1Class ()
    {
        return this.parameter1Class;
    }


    public final Class<?> parameter2Class ()
    {
        return this.parameter2Class;
    }


    @Override
    public final String toString ()
    {
        final String parameter1;
        final String parameter2;
        if ( this.parameter1Class == Object.class
             && this.parameter2Class == Object.class )
        {
            parameter1 = "?";
            parameter2 = "?";
        }
        else
        {
            parameter1 = this.parameter1Class.getSimpleName ();
            parameter2 = this.parameter2Class.getSimpleName ();
        }

        return this.specificClass ().getSimpleName ()
            + "<"
            + parameter1
            + ", "
            + parameter2
            + ">";
    }
}
