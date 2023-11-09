package table;

import java.util.Arrays;


public class Tag
{
    public static final Tag [] EMPTY_ARRAY =
        new Tag [ 0 ];


    private final String id;
    private final Object [] values;
    private final String [] valueStrings;

    public Tag (
            String id,
            Object [] values,
            String [] value_strings
            )
    {
        this.id = id;
        this.values = values;
        this.valueStrings = value_strings;
    }

    public Tag (
            String id,
            Object value,
            String value_string
            )
    {
        this.id = id;
        this.values = new Object [] { value };
        this.valueStrings = new String [] { value_string };
    }

    public final String id ()
    {
        return this.id;
    }

    public final Object [] values ()
    {
        return this.values;
    }

    public final String [] valueStrings ()
    {
        return this.valueStrings;
    }

    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final Tag that = (Tag) object;
        if ( this.id == null )
        {
            if ( that.id != null )
            {
                return false;
            }
        }
        else if ( that.id == null )
        {
            return false;
        }
        else if ( ! this.id.equals ( that.id ) )
        {
            return false;
        }

        if ( this.values == null )
        {
            if ( that.values != null )
            {
                return false;
            }
        }
        else if ( that.values == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.values, that.values ) )
        {
            return false;
        }

        return true;
    }

    public final int hashCode ()
    {
        return this.id.hashCode ();
    }

    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( this.id () );
        sbuf.append ( "=" );
        boolean is_first_value = true;
        for ( String value_string : this.valueStrings )
        {
            if ( is_first_value )
            {
                is_first_value = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "\"" );
            sbuf.append ( value_string );
            sbuf.append ( "\"" );
        }

        return sbuf.toString ();
    }
}
