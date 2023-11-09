package table;


public class TestTable
{
    public void testGraph001 ()
    {
        final Table graph =
            new TableBuilder ()
                .arc ( "input" )
                .tag ( "filter", "x < 5" )
                .to ()
                .tag ( "name", "output" )
                .pop ()
                .build ();
        graph.print ( System.out );
    }


    public static void main (
            String [] args
            )
    {
        new TestTable ().testGraph001 ();
    }
}
