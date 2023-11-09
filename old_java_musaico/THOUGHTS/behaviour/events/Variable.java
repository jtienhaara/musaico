public class Variable
    implements Value
{
    private final Serializable lock = new String ();
    private final String name;

    private Object value;

    public Variable (
                     String name,
                     Object initial_value
                     )
    {
        this.name = name;
        this.value = initial_value;
    }

    public String name ()
    {
        return this.name;
    }

    @Override
    public Object value ()
    {
        synchronized ( this.lock )
        {
            return this.value;
        }
    }

    public Object value (
                         Object new_value
                         )
    {
        final Object old_value;
        synchronized ( this.lock )
        {
            old_value = this.value;
            this.value = new_value;
        }

        return old_value;
    }

    public String toString ()
    {
        return this.name ();
    }
}
