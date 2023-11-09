package musaico.foundation.term.blocking;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.Clock;
import musaico.foundation.domains.InstanceOfClass;
import musaico.foundation.domains.SpecificDomain;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Edit;
import musaico.foundation.term.FilterElements;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.NotOne;
import musaico.foundation.term.OrderElements;
import musaico.foundation.term.Select;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.contracts.ElementsMustBeInstancesOfClass;

import musaico.foundation.term.finite.Many;
import musaico.foundation.term.finite.One;

import musaico.foundation.term.incomplete.Partial;


public class TestBlocking
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public Type<String> createStringType ()
    {
        return new Type<String> ()
        {
            private static final long serialVersionUID =
                TestBlocking.serialVersionUID;

            @Override
            public final String [] array (
                    int num_elements
                    )
                throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
                       ReturnNeverNull.Violation
            {
                return new String [ num_elements ];
            }
            @Override
            public final Edit<String> edit ()
                throws ReturnNeverNull.Violation
            {
                return null; // !!!
            }
            @Override
            public final Class<String> elementClass ()
                throws ReturnNeverNull.Violation
            {
                return String.class;
            }
            @Override
            public final Domain<String> elementDomain ()
                throws ReturnNeverNull.Violation
            {
                return new SpecificDomain<Object, String> (
                    new InstanceOfClass ( String.class ),
                    String.class
                    );
            }
            @Override
            @SuppressWarnings("unchecked") // Generic array heap pollution.
            public final FilterElements<String> filter (
                    Filter<String> ... filters
                    )
            throws ParametersMustNotBeNull.Violation,
                   Parameter1.MustContainNoNulls.Violation,
                   ReturnNeverNull.Violation
            {
                return null; // !!!
            }
            @Override
            public final String none ()
                throws ReturnNeverNull.Violation
            {
                return "";
            }
            @Override
            @SuppressWarnings("unchecked") // Generic array heap pollution.
            public final OrderElements<String> order (
                    Order<String> ... orders
                    )
            throws ParametersMustNotBeNull.Violation,
                   Parameter1.MustContainNoNulls.Violation,
                   ReturnNeverNull.Violation
            {
                return null; // !!!
            }
            @Override
            public final Select<String> select ()
                throws ReturnNeverNull.Violation
            {
                return null; // !!!
            }
            @Override
            public final Contract<Term<?>, ? extends TermViolation> termContract ()
                throws ReturnNeverNull.Violation
            {
                return new ElementsMustBeInstancesOfClass ( String.class );
            }
        };
    }


    public void testBlocking1 ()
        throws Exception
    {
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( "Test Blocking 1 start" );
        System.out.println ( "  Blocking while one worker Thread generates"
                             + " a String Term, one String added" );
        System.out.println ( "  per second, for 3 seconds.  Wait for the"
                             + " thread to complete then" );
        System.out.println ( "  output the completed result." );

        final BigDecimal max_timeout_in_seconds = BigDecimal.TEN;
        final BigDecimal blocking_seconds = new BigDecimal ( "20" );

        final Type<String> string_type = this.createStringType ();

        final NonBlocking<String> expected_result =
            new Many<String> ( string_type,
                               "Hello",
                               "there,",
                               "world!" );

        final LocalResult<String> background_result =
            new LocalResult<String> (
                                        string_type,
                                        Clock.STANDARD,
                                        max_timeout_in_seconds
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
                        new One<String> ( string_type,
                                          "Hello" );
                    background_result.setPartialTerm ( partial_result );

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
                        new Many<String> ( string_type,
                                           "Hello",
                                           "there" );
                    background_result.setPartialTerm ( partial_result );

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

                    background_result.setFinalTerm ( expected_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        worker_thread.setName ( "Worker1" );

        final Blocking<String> blocking =
            new Blocking<String> ( background_result );

        worker_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_seconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.termViolation ().printStackTrace ();
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
                             + " a String Term, one String added" );
        System.out.println ( "  per 10 seconds, for 30 seconds.  Wait for"
                             + " only 2 seconds then" );
        System.out.println ( "  output the timeout result." );

        final BigDecimal max_timeout_in_seconds = BigDecimal.TEN;
        final BigDecimal blocking_seconds = new BigDecimal ( "2" );

        final Type<String> string_type = this.createStringType ();

        final NonBlocking<String> final_result =
            new Many<String> ( string_type,
                               "Hello",
                               "there,",
                               "world!" );

        final LocalResult<String> background_result =
            new LocalResult<String> (
                                     string_type,
                                     Clock.STANDARD,
                                     max_timeout_in_seconds
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
                        new One<String> ( string_type,
                                          "Hello" );
                    background_result.setPartialTerm ( partial_result );

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
                        new Many<String> ( string_type,
                                           "Hello",
                                           "there" );
                    background_result.setPartialTerm ( partial_result );

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

                    background_result.setFinalTerm ( final_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        worker_thread.setName ( "Worker1" );

        final Blocking<String> blocking =
            new Blocking<String> ( background_result );

        final TermViolation term_violation =
            BlockingMustComplete.violation (
                this, // plaintiff
                BigDecimal.ZERO, // Doesn't matter for equals ()
                new BlockingMustComplete.ElapsedTime<String> (
                    background_result,
                    BigDecimal.ZERO ) ); // Doesn't matter for equals ()
        final NonBlocking<String> expected_result =
            new Timeout<String> (
                string_type,
                blocking, // cause
                term_violation,
                new Partial<String> (
                    string_type,
                    "Hello" ) );

        worker_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_seconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.termViolation ().printStackTrace ();
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
                             + " a String Term, one String added" );
        System.out.println ( "  per 10 seconds, for 30 seconds.  Wait for"
                             + " only 2 seconds then" );
        System.out.println ( "  cancel and output the cancelled result." );

        final BigDecimal max_timeout_in_seconds = BigDecimal.TEN;
        final BigDecimal blocking_seconds = new BigDecimal ( "20" );

        final long cancel_after_milliseconds = 2000L; // 2 seconds.

        final Type<String> string_type = this.createStringType ();

        final NonBlocking<String> final_result =
            new Many<String> ( string_type,
                               "Hello",
                               "there,",
                               "world!" );

        final LocalResult<String> background_result =
            new LocalResult<String> (
                                     string_type,
                                     Clock.STANDARD,
                                     max_timeout_in_seconds
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
                        new One<String> ( string_type,
                                          "Hello" );
                    background_result.setPartialTerm ( partial_result );

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
                        new Many<String> ( string_type,
                                           "Hello",
                                           "there" );
                    background_result.setPartialTerm ( partial_result );

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

                    background_result.setFinalTerm ( final_result );

                    System.out.println ( "Thread \""
                                         + Thread.currentThread ().getName ()
                                         + "\": End" );
                }
            };

        worker_thread.setName ( "Worker1" );

        final Blocking<String> blocking =
            new Blocking<String> ( background_result );

        final TermViolation term_violation =
            BlockingMustComplete.violation (
                this, // plaintiff
                BigDecimal.ZERO, // Doesn't matter for equals ()
                new BlockingMustComplete.CancelledTime<String> (
                    background_result,
                    BigDecimal.ZERO ) ); // Doesn't matter for equals ()
        final NonBlocking<String> expected_result =
            new Cancelled<String> (
                string_type,
                blocking, // cause
                term_violation,
                new Partial<String> (
                    string_type,
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
                        new TermViolation (
                            BlockingMustNotBeCancelled.CONTRACT
                                .violation ( this, // plaintiff
                                             blocking ) ) ); //evidence

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
            blocking.await ( blocking_seconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.termViolation ().printStackTrace ();
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
        // This test is a bit pointless, now that we no longer
        // store worker threads inside Blocking terms.  When we
        // did so, we could set the uncaught exception handler of
        // the worker thread, and automagically set an Error result
        // for the Blocking term if the worker thread were to blow up.
        // But now we have to explicitly set the Error result,
        // which is kind of pointless as a test of the Blocking class.
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( "Test Blocking 4 start" );
        System.out.println ( "  Blocking while one worker Thread generates"
                             + " a String Term, one String added" );
        System.out.println ( "  per second, thread blows up after 2 seconds."
                             + " Wait for 20 seconds then" );
        System.out.println ( "  output the error result." );

        final BigDecimal max_timeout_in_seconds = new BigDecimal ( "30" );
        final BigDecimal blocking_seconds = new BigDecimal ( "20" );

        final Type<String> string_type = this.createStringType ();

        final NonBlocking<String> final_result =
            new Many<String> ( string_type,
                               "Hello",
                               "there,",
                               "world!" );

        final LocalResult<String> background_result =
            new LocalResult<String> (
                                     string_type,
                                     Clock.STANDARD,
                                     max_timeout_in_seconds
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
                        new One<String> ( string_type,
                                          "Hello" );
                    background_result.setPartialTerm ( partial_result );

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

                    final TermViolation term_violation =
                        BlockingMustComplete.violation (
                            this, // plaintiff
                            max_timeout_in_seconds, // timeout_in_seconds
                            new BlockingMustComplete.ErrorTime<String> (
                                background_result, // blocking_result
                                new BigDecimal ( "2" ) ) ); // working_seconds

                    final Error<String> error =
                        new Error<String> ( string_type,
                                            term_violation );

                    background_result.setFinalTerm ( error );
                }
            });

        final Blocking<String> blocking =
            new Blocking<String> ( background_result );

        final TermViolation term_violation =
            BlockingMustComplete.violation (
                this, // plaintiff
                max_timeout_in_seconds, // Doesn't matter for equals ()
                new BlockingMustComplete.ErrorTime<String> (
                    background_result,
                    new BigDecimal ( "2" ) ) ); // Doesn't matter for equals ()

        final NonBlocking<String> expected_result =
            new Error<String> (
                string_type,
                term_violation );

        worker_thread.start ();

        System.out.println ( "Blocking: About to start blocking..." );
        final NonBlocking<String> actual_result =
            blocking.await ( blocking_seconds );
        System.out.println ( "Blocking: Result = " + actual_result );
        if ( ! ( actual_result instanceof Countable )
             && ( actual_result instanceof NotOne ) )
        {
            final NotOne<String> failed = (NotOne<String>) actual_result;
            System.out.println ( "Blocking:   Violation details..." );
            failed.termViolation ().printStackTrace ();
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
