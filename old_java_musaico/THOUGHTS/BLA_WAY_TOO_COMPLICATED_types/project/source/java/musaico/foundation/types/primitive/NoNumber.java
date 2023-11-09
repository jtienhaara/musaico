package musaico.types.primitive;

import java.io.Serializable;


public final class NoNumber
    extends Number
    implements Serializable
{
    @Override
    public double doubleValue ()
    {
        return Double.NaN;
    }

    @Override
    public boolean equals (
                           Object that
                           )
    {
        if ( that != null
             && ( that instanceof NoNumber ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public float floatValue ()
    {
        return Float.NaN;
    }

    @Override
    public int hashCode ()
    {
        return Integer.MIN_VALUE;
    }

    @Override
    public int intValue ()
    {
        return Integer.MIN_VALUE;
    }

    @Override
    public long longValue ()
    {
        return Long.MIN_VALUE;
    }

    @Override
    public String toString ()
    {
        return "NaN";
    }
}
