import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;

public class GenericArray<FOO extends Serializable>
{
    public FOO [] foobar(FOO[] originals, List<FOO> a_list)
    {
        Class cl = originals[ 0 ].getClass ();
        FOO [] template = (FOO[]) Array.newInstance( cl, 0 );
        FOO [] b = a_list.toArray ( template );

        return b;
    }


    public static void main ( String[] args )
    {
        GenericArray<String> abc = new GenericArray<String> ();
        List<String> abc_list = new ArrayList<String> ();
        abc_list.add ( "a" );
        abc_list.add ( "b" );
        abc_list.add ( "c" );
        String[] originals = new String[] { "x", "y", "z" };
        String [] abc_array = abc.foobar( originals, abc_list );
        System.out.println( "!!! ABC ARRAY = " + abc_array + " {" );
        System.out.println( "!!!     " + abc_array [ 0 ] );
        System.out.println( "!!!     " + abc_array [ 1 ] );
        System.out.println( "!!!     " + abc_array [ 2 ] );
        System.out.println( "!!! }" );
    }
}
