public class InterruptingCow
    implements Runnable
{
    private boolean isDoneExecuting = false;

    public boolean isDoneExecuting ()
    {
        return this.isDoneExecuting;
    }

    @Override
    public void run ()
    {
        this.doSomethingOverAndOver ();

        this.isDoneExecuting = true;
    }

    public void doSomethingOverAndOver ()
    {
        try
        {
            if ( false )
            {
                throw new InterruptedException ( "foo" );
            }

            for ( long count = 0L; count < 10000000L; count ++ )
            {
                System.out.print ( "" );
            }

            System.out.println ( "Successfully finished counting" );
        }
        catch ( InterruptedException e )
        {
            System.out.println ( "Interrupted counting" );
        }
    }

    public static void main ( String [] args )
    {
        InterruptingCow cow = new InterruptingCow ();
        Thread thread = new Thread ( cow );
        thread.start ();

        for ( long num = 0L; num < 10000000L && ! cow.isDoneExecuting ();
              num ++ )
        {
            thread.interrupt ();
        }

        System.out.println ( "Gave up trying to interrupt" );
    }
}
