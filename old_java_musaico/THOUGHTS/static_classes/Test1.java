public class Test1
{
    public void foo ( int a )
        throws Parameter3.MustBeGreaterThanZero.Violation
    {
        Parameter3.MustBeGreaterThanZero contract =
            new Parameter3.MustBeGreaterThanZero ();
        contract.check ( a );
    }


    public static void main ( String [] args )
        throws Exception
    {
        /* !!!
        Domain contract;

        System.out.println ( "Parameter1:" );
        System.out.println ( "===========" );
        contract = new Parameter1.MustBeGreaterThanZero ();

        for ( Integer value : new Integer [] { 1, 0, null } )
        {
            System.out.println ( "Checking " + value + ":" );
            try
            {
                contract.check ( value );

                System.out.println ( "    OK" );
            }
            catch ( Exception e )
            {
                System.out.println ( "    Failed with: " + e.getMessage () );
            }
        }


        System.out.println ( "Parameter2:" );
        System.out.println ( "===========" );
        contract = new Parameter2.MustBeGreaterThanZero ();

        for ( Integer value : new Integer [] { 1, 0, null } )
        {
            System.out.println ( "Checking " + value + ":" );
            try
            {
                contract.check ( value );

                System.out.println ( "    OK" );
            }
            catch ( Exception e )
            {
                System.out.println ( "    Failed with: " + e.getMessage () );
            }
        }


        System.out.println ( "Parameter3:" );
        System.out.println ( "===========" );
        contract = new Parameter3.MustBeGreaterThanZero ();

        for ( Integer value : new Integer [] { 1, 0, null } )
        {
            System.out.println ( "Checking " + value + ":" );
            try
            {
                contract.check ( value );

                System.out.println ( "    OK" );
            }
            catch ( Exception e )
            {
                System.out.println ( "    Failed with: " + e.getMessage () );
            }
        }
        !!! */


        System.out.println ( "Parameter3:" );
        System.out.println ( "===========" );

        Test1 test1 = new Test1 ();

        for ( Integer value : new Integer [] { 1, 0, null } )
        {
            System.out.println ( "Checking " + value + ":" );
            try
            {
                test1.foo ( value );

                System.out.println ( "    OK" );
            }
            catch ( Exception e )
            {
                System.out.println ( "    Failed with " + e.getClass ().getSimpleName () + " : " + e.getMessage () );
            }
        }
    }
}
