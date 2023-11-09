package musaico.foundation.contract.obligations;

import musaico.foundation.contract.ObjectContracts;

public class Test1
{
    private final ObjectContracts contracts = new ObjectContracts ( this );

    public void foo ( int a )
        throws Parameter1.MustBeGreaterThanZero.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanZero.CONTRACT,
                               a );
    }

    public void bar ( Object [] x )
        throws Parameter1.MustContainNoNulls.Violation
    {
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               x );
    }

    public static void main ( String [] args )
    {
        Test1 x = new Test1 ();

        System.out.println ( "Must be greater than zero:" );
        System.out.println ( "==========================" );
        for ( int value : new int [] { 1, 0 } )
            {
                System.out.println ( "Testing " + value );
                try
                    {
                        x.foo ( value );
                        System.out.println ( "    OK" );
                    }
                catch ( Exception e )
                    {
                        System.out.println ( "    FAILED with " + e );
                    }
            }


        System.out.println ( "Must contain no nulls:" );
        System.out.println ( "======================" );
        for ( Object[] array : new Object[] [] {
                new Object [] { 1, 2, 3 },
                new Object [] { 1, 2, null },
                new Object [] { 1, null, 3 },
                new Object [] { null, 2, 3 },
                new Object [] { },
                new Object [] { null, null, null } } )
            {
                System.out.println ( "Testing " + array );
                try
                    {
                        x.bar ( array );
                        System.out.println ( "    OK" );
                    }
                catch ( Exception e )
                    {
                        System.out.println ( "    FAILED with " + e );
                    }
            }
    }
}
