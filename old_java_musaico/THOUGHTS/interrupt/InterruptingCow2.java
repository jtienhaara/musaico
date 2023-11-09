public class InterruptingCow2
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
        long count = 0L;
        try
        {
            for ( count = 0L; count < 10000000L; count ++ )
            {
                System.out.print ( "" );
                Thread.sleep ( 0L );
            }

            System.out.println ( "Successfully finished counting" );
        }
        catch ( InterruptedException e )
        {
            System.out.println ( "Interrupted counting at " + count );
        }
    }

    public static void main ( String [] args )
    {
        InterruptingCow2 cow = new InterruptingCow2 ();
        Thread thread = new Thread ( cow );
        thread.start ();
        thread.interrupt ();

        System.out.println ( "Gave up trying to interrupt" );
    }
}
