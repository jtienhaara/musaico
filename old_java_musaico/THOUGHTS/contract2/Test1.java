public class Test1
{
    public void foo ( int bar )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        Parameter1.MustBeGreaterThanZero.CONTRACT.check ( bar );
    }

    @Override
    public String toString ()
    {
        return "Domain contract tester";
    }

    public static void main ( String [] args )
        throws Exception
    {
        Test1 tester = new Test1 ();
        for ( int test : new int [] { 1, 0 } )
        {
            System.out.println ( "Testing " + tester + " with " + test );
            try
            {
                tester.foo ( test );
                System.out.println ( "    OK" );
            }
            catch ( Exception e )
            {
                System.out.println ( "    FAILED with " + e );
            }
        }
    }
}
