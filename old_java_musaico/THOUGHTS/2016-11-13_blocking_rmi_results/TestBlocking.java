package musaico.foundation.value.blocking;

import java.io.Serializable;


import musaico.foundation.value.Countable;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.finite.Many;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.incomplete.Partial;

import musaico.foundation.value.types.StandardType;


public class TestBlocking
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public void testBlocking1 ()
        throws Exception
    {
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( "Test Blocking 1 start" );
        System.out.println ( "  Blocking while one worker Thread generates"
                             + " a String Value, one String added" );
        System.out.println ( "  per second, for 3 seconds.  Wait for the"
                             + " thread to complete then" );
        System.out.println ( "  output the completed result." );

        final long max_timeout_in_nanoseconds =
            10L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;
        final long blocking_nanoseconds =
            20L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;

        final NonBlocking<String> expected_result =
            new Many<String> ( StandardType.STRING,
                               "Hello",
                               "there,",
                               "world!" );

        final AsynchronousResult<String> background_result =
            new AsynchronousResult<String> (
                                            StandardType.STRING,
                                            max_timeout_in_nanoseconds
                                            );
        final Thread worker_thread = new Thread ()
            {
                @Override
                public void run ()
                {
                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Start" );

                    NonBlocking<String> partial_result;

                    try
                    {
                        Thread.sleep ( 1000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new One<String> ( StandardType.STRING,
                                          "Hello" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 1" );

                    try
                    {
                        Thread.sleep ( 1000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new Many<String> ( StandardType.STRING,
                                           "Hello",
                                           "there" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 2" );

                    try
                    {
                        Thread.sleep ( 1000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    background_result.setFinalResult ( expected_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        worker_thread.setName ( "Worker1" );

        final Blocking<String> blocking =
            new Blocking<String> ( worker_thread,
                                   background_result );

        worker_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_nanoseconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.valueViolation ().printStackTrace ();
            System.out.println ( "Blocking:   ...Violation details." );
        }

        // Give the thread(s) some time to clean up.
        try
        {
            Thread.sleep ( 1000L );
        }
        catch ( InterruptedException e )
        {
            // Ignore.
        }

        System.out.println ( "Blocking: Interrupting worker thread..." );
        worker_thread.interrupt ();
        System.out.println ( "Blocking: Interrupted worker thread." );

        System.out.println ( "Test Blocking 1 End" );

        if ( ! expected_result.equals ( actual_result ) )
        {
            throw new Exception ( "FAILURE: expected "
                                  + expected_result
                                  + " but received "
                                  + actual_result );
        }
        else
        {
            System.out.println ( "SUCCESS" );
        }

        System.out.println ( "------------------------------------"
                             + "------------------------------------" );
    }


    public void testBlocking2 ()
        throws Exception
    {
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( "Test Blocking 2 start" );
        System.out.println ( "  Blocking while one worker Thread generates"
                             + " a String Value, one String added" );
        System.out.println ( "  per 10 seconds, for 30 seconds.  Wait for"
                             + " only 2 seconds then" );
        System.out.println ( "  output the timeout result." );

        final long max_timeout_in_nanoseconds =
            10L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;
        final long blocking_nanoseconds =
            2L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;

        final NonBlocking<String> final_result =
            new Many<String> ( StandardType.STRING,
                               "Hello",
                               "there,",
                               "world!" );

        final AsynchronousResult<String> background_result =
            new AsynchronousResult<String> (
                                            StandardType.STRING,
                                            max_timeout_in_nanoseconds
                                            );
        final Thread worker_thread = new Thread ()
            {
                @Override
                public void run ()
                {
                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Start" );

                    NonBlocking<String> partial_result;

                    try
                    {
                        Thread.sleep ( 10000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new One<String> ( StandardType.STRING,
                                          "Hello" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 1" );

                    try
                    {
                        Thread.sleep ( 10000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new Many<String> ( StandardType.STRING,
                                           "Hello",
                                           "there" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 2" );

                    try
                    {
                        Thread.sleep ( 10000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    background_result.setFinalResult ( final_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        worker_thread.setName ( "Worker1" );

        final Blocking<String> blocking =
            new Blocking<String> ( worker_thread,
                                   background_result );

        final ValueViolation value_violation =
            BlockingMustComplete.violation (
                0L, // Doesn't matter for equals ()
                new BlockingMustComplete.ElapsedTime<String> (
                    blocking,
                    0L ) ); // Doesn't matter for equals ()
        final NonBlocking<String> expected_result =
            new Timeout<String> (
                StandardType.STRING,
                blocking, // cause
                value_violation,
                new Partial<String> (
                    StandardType.STRING,
                    "Hello" ) );

        worker_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_nanoseconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.valueViolation ().printStackTrace ();
            System.out.println ( "Blocking:   ...Violation details." );
        }

        // Give the thread(s) some time to clean up.
        try
        {
            Thread.sleep ( 1000L );
        }
        catch ( InterruptedException e )
        {
            // Ignore.
        }

        System.out.println ( "Blocking: Interrupting worker thread..." );
        worker_thread.interrupt ();
        System.out.println ( "Blocking: Interrupted worker thread." );

        System.out.println ( "Test Blocking 2 End" );

        if ( ! expected_result.equals ( actual_result ) )
        {
            throw new Exception ( "FAILURE: expected "
                                  + expected_result
                                  + " but received "
                                  + actual_result );
        }
        else
        {
            System.out.println ( "SUCCESS" );
        }

        System.out.println ( "------------------------------------"
                             + "------------------------------------" );
    }


    public void testBlocking3 ()
        throws Exception
    {
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( "Test Blocking 3 start" );
        System.out.println ( "  Blocking while one worker Thread generates"
                             + " a String Value, one String added" );
        System.out.println ( "  per 10 seconds, for 30 seconds.  Wait for"
                             + " only 2 seconds then" );
        System.out.println ( "  cancel and output the cancelled result." );

        final long max_timeout_in_nanoseconds =
            10L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;
        final long blocking_nanoseconds =
            20L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;

        final long cancel_after_milliseconds = 2000L; // 2 seconds.

        final NonBlocking<String> final_result =
            new Many<String> ( StandardType.STRING,
                               "Hello",
                               "there,",
                               "world!" );

        final AsynchronousResult<String> background_result =
            new AsynchronousResult<String> (
                                            StandardType.STRING,
                                            max_timeout_in_nanoseconds
                                            );
        final Thread worker_thread = new Thread ()
            {
                @Override
                public void run ()
                {
                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Start" );

                    NonBlocking<String> partial_result;

                    try
                    {
                        Thread.sleep ( 10000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new One<String> ( StandardType.STRING,
                                          "Hello" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 1" );

                    try
                    {
                        Thread.sleep ( 10000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new Many<String> ( StandardType.STRING,
                                           "Hello",
                                           "there" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 2" );

                    try
                    {
                        Thread.sleep ( 10000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    background_result.setFinalResult ( final_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        worker_thread.setName ( "Worker1" );

        final Blocking<String> blocking =
            new Blocking<String> ( worker_thread,
                                   background_result );

        final ValueViolation value_violation =
            BlockingMustComplete.violation (
                0L, // Doesn't matter for equals ()
                new BlockingMustComplete.CancelledTime<String> (
                    blocking,
                    0L ) ); // Doesn't matter for equals ()
        final NonBlocking<String> expected_result =
            new Cancelled<String> (
                StandardType.STRING,
                blocking, // cause
                value_violation,
                new Partial<String> (
                    StandardType.STRING,
                    "Hello" ) );

        final Thread cancel_thread = new Thread ()
            {
                @Override
                public void run ()
                {
                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Start" );

                    try
                    {
                        Thread.sleep ( cancel_after_milliseconds );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": About to cancel" );

                    blocking.cancel (
                        new ValueViolation (
                            CancellableMustNotBeCancelled.CONTRACT
                                .violation ( this, // plaintiff
                                             Cancellable.NONE ) ) ); //evidence

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Cancelled" );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        cancel_thread.setName ( "Cancel" );

        worker_thread.start ();
        cancel_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_nanoseconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.valueViolation ().printStackTrace ();
            System.out.println ( "Blocking:   ...Violation details." );
        }

        // Give the thread(s) some time to clean up.
        try
        {
            Thread.sleep ( 1000L );
        }
        catch ( InterruptedException e )
        {
            // Ignore.
        }

        System.out.println ( "Blocking: Interrupting worker thread..." );
        worker_thread.interrupt ();
        System.out.println ( "Blocking: Interrupted worker thread." );

        System.out.println ( "Blocking: Interrupting cancel thread..." );
        cancel_thread.interrupt ();
        System.out.println ( "Blocking: Interrupted cancel thread." );

        System.out.println ( "Test Blocking 3 End" );

        if ( ! expected_result.equals ( actual_result ) )
        {
            throw new Exception ( "FAILURE: expected "
                                  + expected_result
                                  + " but received "
                                  + actual_result );
        }
        else
        {
            System.out.println ( "SUCCESS" );
        }

        System.out.println ( "------------------------------------"
                             + "------------------------------------" );
    }


    public void testBlocking4 ()
        throws Exception
    {
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( "Test Blocking 4 start" );
        System.out.println ( "  Blocking while one worker Thread generates"
                             + " a String Value, one String added" );
        System.out.println ( "  per second, thread blows up after 2 seconds."
                             + " Wait for 20 seconds then" );
        System.out.println ( "  output the error result." );

        final long max_timeout_in_nanoseconds =
            30L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;
        final long blocking_nanoseconds =
            20L // seconds
            * 1000L // milliseconds
            * 1000000000L; // nanoseconds;

        final NonBlocking<String> final_result =
            new Many<String> ( StandardType.STRING,
                               "Hello",
                               "there,",
                               "world!" );

        final AsynchronousResult<String> background_result =
            new AsynchronousResult<String> (
                                            StandardType.STRING,
                                            max_timeout_in_nanoseconds
                                            );
        final Thread worker_thread = new Thread ()
            {
                @Override
                public void run ()
                {
                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Start" );

                    NonBlocking<String> partial_result;

                    try
                    {
                        Thread.sleep ( 1000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    partial_result =
                        new One<String> ( StandardType.STRING,
                                          "Hello" );
                    background_result.setPartialResult ( partial_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": Partial result 1" );

                    try
                    {
                        Thread.sleep ( 1000L );
                    }
                    catch ( InterruptedException e )
                    {
                        System.out.println ( "Thread \""
                                             + Thread.currentThread ().getName ()
                                             + "\": Interrupted" );
                        return;
                    }

                    // Now blow up.
                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": About to blow up" );
                    throw new RuntimeException ( "UH OH THREAD "
                                                 + Thread.currentThread ().getName ()
                                                 + " BLEW UP REAL GOOD!" );
                }
            };

        worker_thread.setName ( "Worker1" );
        worker_thread.setUncaughtExceptionHandler (
            new Thread.UncaughtExceptionHandler ()
            {
                @Override
                public void uncaughtException (
                                               Thread thread,
                                               Throwable throwable
                                               )
                {
                    System.out.println ( "Thread \""
                                         + thread.getName ()
                                         + "\": Blew up real good" );
                }
            });

        final Blocking<String> blocking =
            new Blocking<String> ( worker_thread,
                                   background_result );

        final ValueViolation value_violation =
            BlockingMustComplete.violation (
                0L, // Doesn't matter for equals ()
                new BlockingMustComplete.ErrorTime<String> (
                    blocking,
                    0L ) ); // Doesn't matter for equals ()
        final NonBlocking<String> expected_result =
            new Error<String> (
                StandardType.STRING,
                value_violation );

        worker_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_nanoseconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.valueViolation ().printStackTrace ();
            System.out.println ( "Blocking:   ...Violation details." );
        }

        // Give the thread(s) some time to clean up.
        try
        {
            Thread.sleep ( 1000L );
        }
        catch ( InterruptedException e )
        {
            // Ignore.
        }

        System.out.println ( "Blocking: Interrupting worker thread..." );
        worker_thread.interrupt ();
        System.out.println ( "Blocking: Interrupted worker thread." );

        System.out.println ( "Test Blocking 4 End" );

        if ( ! expected_result.equals ( actual_result ) )
        {
            throw new Exception ( "FAILURE: expected "
                                  + expected_result
                                  + " but received "
                                  + actual_result );
        }
        else
        {
            System.out.println ( "SUCCESS" );
        }

        System.out.println ( "------------------------------------"
                             + "------------------------------------" );
    }


    public static void main (
                             String [] args
                             )
        throws Exception
    {
        new TestBlocking ().testBlocking1 ();
        new TestBlocking ().testBlocking2 ();
        new TestBlocking ().testBlocking3 ();
        new TestBlocking ().testBlocking4 ();

        System.exit ( 0 );
    }
}
