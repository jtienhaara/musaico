// Not allowed.

public class Pattern<X> extends X
{
    public boolean matches ( X x )
    {
        return true;
    }

    public static void main ( String [] args )
    {
        new Pattern<String>.matches ();
    }
}
