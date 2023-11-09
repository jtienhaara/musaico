public class Constant
    implements Value
{
    private Object value;

    public Constant (
                     Object value
                     )
    {
        this.value = value;
    }

    @Override
    public Object value ()
    {
        return this.value;
    }

    public String toString ()
    {
        if ( this.value == null )
        {
            return "null";
        }
        else if ( this.value instanceof String )
        {
            return "\"" + this.value + "\"";
        }
        else
        {
            return "" + this.value;
        }
    }
}
