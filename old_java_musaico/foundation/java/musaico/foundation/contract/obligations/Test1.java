package musaico.foundation.contract.obligations;

import java.util.Arrays;

import musaico.foundation.contract.Advocate;

public class Test1
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    private final Advocate contracts = new Advocate ( this );

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

    public void multipleParameters (
            String a,
            int x,
            int y,
            String b
            )
    {
        this.contracts.check ( EveryParameter.MustNotBeNull.CONTRACT,
                               a, x, y, b );
    }

    public static void main ( String [] args )
    {
        Test1 x = new Test1 ();
        int i;

        System.out.println ( "Must be greater than zero:" );
        System.out.println ( "==========================" );
        i = 0;
        for ( int value : new int [] { 1, 0, -1, 2 } )
            {
                final boolean should_pass;
                boolean did_pass = true;
                if ( value > 0 )
                {
                    should_pass = true;
                }
                else
                {
                    should_pass = false;
                }

                System.out.println ( "Testing [ " + i + " ] " + value
                                     + " (should pass? " + should_pass + ")" );

                try
                    {
                        x.foo ( value );
                        System.out.println ( "    PASSED" );

                        if ( should_pass )
                        {
                            System.out.println ("    (correct)" );
                        }
                    }
                catch ( Exception e )
                    {
                        did_pass = false;
                        System.out.println ( "    FAILED with " + e );

                        if ( ! should_pass )
                        {
                            System.out.println ("    (correct)" );
                        }
                        else
                        {
                            throw new IllegalStateException ( "ERROR INCORRECT should have passed value # " + i + " = " + value );
                        }
                    }

                if ( did_pass
                     && ! should_pass )
                {
                    throw new IllegalStateException ( "ERROR INCORRECT should have failed value # " + i + " = " + value );
                }

                i ++;
            }


        System.out.println ( "Must contain no nulls:" );
        System.out.println ( "======================" );
        i = 0;
        for ( Object[] array : new Object[] [] {
                new Object [] { 1, 2, 3 },
                new Object [] { 1, 2, null },
                new Object [] { 1, null, 3 },
                new Object [] { null, 2, 3 },
                new Object [] { },
                new Object [] { null, null, null } } )
            {
                boolean should_pass = true;
                boolean did_pass = true;
                for ( Object element : array )
                {
                    if ( element == null )
                    {
                        should_pass = false;
                        break;
                    }
                }

                System.out.println ( "Testing [ " + i + " ] "
                                     + Arrays.toString ( array )
                                     + " (should pass? " + should_pass + ")" );

                try
                    {
                        x.bar ( array );
                        System.out.println ( "    OK" );

                        if ( should_pass )
                        {
                            System.out.println ("    (correct)" );
                        }
                    }
                catch ( Exception e )
                    {
                        did_pass = false;
                        System.out.println ( "    FAILED with " + e );

                        if ( ! should_pass )
                        {
                            System.out.println ("    (correct)" );
                        }
                        else
                        {
                            throw new IllegalStateException ( "ERROR INCORRECT should have passed array # " + i + " = " + Arrays.toString ( array ) );
                        }
                    }

                if ( did_pass
                     && ! should_pass )
                {
                    throw new IllegalStateException ( "ERROR INCORRECT should have failed array # " + i + " = " + Arrays.toString ( array ) );
                }

                i ++;
            }

        System.out.println ( "" );
        System.out.println ( "Testing EveryParameter.MustNotBeNull." );
        i = 0;
        for ( String aa : new String [] { "a1", null, "a2" } )
        {
            for ( Integer xx : new Integer [] { 0, 1, null } )
            {
                for ( Integer yy : new Integer [] { 10, 20, null } )
                {
                    for ( String bb : new String [] { "b1", null, "b2" } )
                    {
                        final String parameters_string =
                            ( aa == null ? "" : "\"" )
                            + aa
                            + ( aa == null ? ", " : "\", " )
                            + xx
                            + ", "
                            + yy
                            + ( bb == null ? ", " : ", \"" )
                            + bb
                            + ( bb == null ? "" : "\"" );

                        final boolean should_pass;
                        boolean did_pass = true;
                        if ( aa != null
                             && xx != null
                             && yy != null
                             && bb != null )
                        {
                            System.out.println ( "    Should pass: "
                                                 + parameters_string );
                            should_pass = true;
                        }
                        else
                        {
                            System.out.println ( "    Should fail: "
                                                 + parameters_string );
                            should_pass = false;
                        }

                        try
                        {
                            x.multipleParameters ( aa, xx, yy, bb );
                            System.out.println ( "    OK" );

                            if ( should_pass )
                            {
                                System.out.println ("    (correct)" );
                            }
                        }
                        catch ( Exception e )
                        {
                            did_pass = false;
                            System.out.println ( "    FAILED with " + e );

                            if ( ! should_pass )
                            {
                                System.out.println ("    (correct)" );
                            }
                            else
                            {
                                throw new IllegalStateException ( "ERROR INCORRECT should have passed parameters # " + i + " = " + parameters_string );
                            }
                        }

                        if ( did_pass
                             && ! should_pass )
                        {
                            throw new IllegalStateException ( "ERROR INCORRECT should have failed parameters # " + i + " = " + parameters_string );
                        }

                        i ++;
                    }
                }
            }
        }
    }
}
