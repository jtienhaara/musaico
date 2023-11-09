package musaico.platform.mutex;

import java.rmi.RemoteException;


import musaico.foundation.contract.guarantees.TimeoutGuarantee;

import musaico.foundation.security.SecurityContext;
import musaico.foundation.security.SecurityPolicy;
import musaico.foundation.security.SecurityViolation;


public class Test
    implements Runnable
{
    private static final Mutex [] mutexes;

    static
    {
        try
        {
            mutexes =
                new Mutex []
                {
                    new Mutex ( "A", SecurityPolicy.NONE ),
                    new Mutex ( "B", SecurityPolicy.NONE ),
                    new Mutex ( "C", SecurityPolicy.NONE )
                };
        }
        catch ( RemoteException e ) // Ugh RMI ugliness
        {
            // Try to make the whole JVM fail.
            throw new RuntimeException ( e );
        }
    }

    private static int numTimedOutThreads = 0;


    private final boolean isNestedMutexThread;
    private final boolean isBlowupThread;


    public Test (
                 boolean is_nested_mutex_thread,
                 boolean is_blowup_thread
                 )
    {
        this.isNestedMutexThread = is_nested_mutex_thread;
        this.isBlowupThread = is_blowup_thread;
    }


    @Override
    public void run ()
    {
        final MutexLock mutex_lock;
        try
        {
            if ( ! this.isNestedMutexThread )
            {
                mutex_lock = new MutexLock ( SecurityContext.NONE,
                                             SecurityPolicy.NONE,
                                             Test.mutexes [ 0 ],
                                             Test.mutexes [ 1 ],
                                             Test.mutexes [ 2 ] );
            }
            else
            {
                // Grab "A" and "B" first then add "C" later.
                mutex_lock = new MutexLock ( SecurityContext.NONE,
                                             SecurityPolicy.NONE,
                                             Test.mutexes [ 0 ],
                                             Test.mutexes [ 1 ] );
            }
        }
        catch ( SecurityViolation v )
        {
            System.err.println ( "*** SECURITY VIOLATION ***" );
            v.printStackTrace ();
            throw new RuntimeException ( v );
        }

        try
        {
            mutex_lock.lock ( SecurityContext.NONE, 15000L );
        }
        catch ( TimeoutGuarantee.Violation v )
        {
            System.out.println ( "Thread " + Thread.currentThread ().getName () + " timed out: " + v );

            Test.numTimedOutThreads ++;
            return;
        }
        catch ( SecurityViolation v )
        {
            System.err.println ( "*** SECURITY VIOLATION ***" );
            v.printStackTrace ();
            throw new RuntimeException ( v );
        }

        try
        {
            System.out.println ( "Yay got the lock for thread "
                                 + Thread.currentThread ().getName ()
                                 + " at " + System.currentTimeMillis () );
            Thread.sleep ( 1000L );

            if ( this.isNestedMutexThread )
            {
                // Grab some more, make sure we don't block ourselves.
                System.out.println ( "    " + Thread.currentThread ().getName ()
                                     + " ---> Grabbling an extra resource" );
                MutexLock nested_mutex_lock =
                    new MutexLock ( SecurityContext.NONE,
                                    SecurityPolicy.NONE,
                                    Test.mutexes [ 0 ],
                                    Test.mutexes [ 1 ],
                                    Test.mutexes [ 2 ] );
                nested_mutex_lock.lock ( SecurityContext.NONE, 1000L );
                try
                {
                    System.out.println ( "    " + Thread.currentThread ().getName ()
                                         + " ---> Used an extra resource" );
                }
                finally
                {
                    nested_mutex_lock.unlock ( SecurityContext.NONE );
                }
            }

            if ( this.isBlowupThread )
            {
                throw new RuntimeException ( "Thread " + Thread.currentThread ().getName () + " blew up real good after acquiring the mutex lock!" );
            }
        }
        catch ( InterruptedException e )
        {
            // Oh well, give it up.
        }
        catch ( SecurityViolation v )
        {
            System.err.println ( "*** SECURITY VIOLATION ***" );
            v.printStackTrace ();
            throw new RuntimeException ( v );
        }
        finally
        {
            System.out.println ( "Discarding the lock for thread "
                                 + Thread.currentThread ().getName ()
                                 + " at " + System.currentTimeMillis () );

            try
            {
                mutex_lock.unlock ( SecurityContext.NONE );
            }
            catch ( SecurityViolation v )
            {
                System.err.println ( "*** SECURITY VIOLATION ***" );
                v.printStackTrace ();
                throw new RuntimeException ( v );
            }
        }
    }


    public static void main ( String [] args )
        throws Exception
    {
        Test [] runners = new Test []
        {
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( true, false ),
            new Test ( false, true ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false ),
            new Test ( false, false )
        };

        Thread [] threads = new Thread [ runners.length ];
        for ( int t = 0; t < runners.length; t ++ )
        {
            threads [ t ] = new Thread ( runners [ t ], "" + t );
            threads [ t ].start ();
        }

        for ( int t = 0; t < threads.length; t ++ )
        {
            threads [ t ].join ();
        }

        final int expect_timeouts = 5;
        if ( Test.numTimedOutThreads == expect_timeouts )
        {
            System.out.println ( "Test passed" );
        }
        else
        {
            System.out.println ( "***************** TEST FAILED: should have had " + expect_timeouts + " timed out threads but had " + Test.numTimedOutThreads );
        }
    }
}
