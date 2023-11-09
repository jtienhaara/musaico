package musaico.foundation.term.countable;

import java.io.Serializable;

import java.lang.reflect.Array;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Mutable elements, fixed length.
 * </p>
 *
 *
 * <p>
 * In Java every Elements must be Serializable in order to
 * play nicely across RMI.  However users of the Elements
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.term.countable.MODULE#COPYRIGHT
 * @see musaico.foundation.term.countable.MODULE#LICENSE
 */
public class MutableElements<ELEMENT>
    implements Elements<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    @SuppressWarnings("try") // No matter what I do I can't prevent the
        // warning: "auto-closeable resource Lock has a member method
        // close() that could throw InterruptedException".
        // Must find out why on stackoverflow tomorrow...
    private static class Lock
        implements AutoCloseable, Serializable
    {
        private static final long serialVersionUID =
            MutableElements.serialVersionUID;

        private Serializable syncLock = new String ( "lock" );
        private Thread lockedByThread = null;

        /**
         * @see java.lang.AutoCloseable#close()
         */
        @Override
        public final void close ()
            throws Exception
        {
            synchronized ( this.syncLock )
            {
                if ( this.lockedByThread != Thread.currentThread () )
                {
                    final String thread_identifier;
                    if ( this.lockedByThread == null )
                    {
                        thread_identifier = "(NOT LOCKED)";
                    }
                    else
                    {
                        thread_identifier =
                            "" + this.lockedByThread.getId ()
                            + " " + this.lockedByThread.getName ();
                    }

                    throw new IllegalStateException (
                        "ERROR Tried to close MutableElements.Lock"
                        + " when the lock is held by: "
                        + thread_identifier );
                }

                this.lockedByThread = null;
                this.syncLock.notify (); // Wake up one thread.
            }
        }

        /**
         * @return True if the specified Thread holds this lock,
         *         false if it is not locked or if another thread owns it.
         */
        public final boolean isLockedBy (
                Thread thread
                )
        {
            synchronized ( this.syncLock )
            {
                if ( this.lockedByThread == thread )
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        /** Kind of backwards, opening a lock in order to lock it,
         *  but oh well...  It fits the naming of AutoCloseable. */
        protected void open ()
            throws IllegalStateException
        {
            final Thread my_thread = Thread.currentThread ();
            int infinite_loop_protector = 0;
            boolean is_infinite_loop = false;
            InterruptedException interrupted = null;
            String illegal_state = null;
            synchronized ( this.syncLock )
            {
                while ( this.lockedByThread != my_thread )
                {
                    infinite_loop_protector ++;
                    if ( infinite_loop_protector >= 64 )
                    {
                        is_infinite_loop = true;
                        break;
                    }

                    try
                    {
                        this.syncLock.wait ( 1000L ); // Wait up to 1 second.
                    }
                    catch ( InterruptedException e )
                    {
                        // Give up.
                        interrupted = e;
                        break;
                    }
                }

                if ( ! is_infinite_loop
                     && interrupted == null )
                {
                    if ( this.lockedByThread != null )
                    {
                        // Still can't lock it!  What's going on???
                        // Something is badly borken...
                        illegal_state =
                            "Lock is still held by thread "
                            + this.lockedByThread.getId ()
                            + " " + this.lockedByThread.getName ();
                    }
                    else
                    {
                        // Locked.
                        this.lockedByThread = my_thread;
                        return;
                    }
                }
            }

            if ( illegal_state != null )
            {
                throw new IllegalStateException ( "ERROR Could not lock access"
                                                  + " to elements: "
                                                  + illegal_state );
            }

            throw new IllegalStateException ( "ERROR Could not lock access"
                                              + " to elements after "
                                              + infinite_loop_protector
                                              + " seconds.Giving up.",
                                              interrupted ); // cause
        }

        /**
         * @return The Thread which currently owns this lock, or null
         *         if nobody has locked it.  Can be null.
         */
        public final Thread owner ()
        {
            synchronized ( this.syncLock )
            {
                return this.lockedByThread;
            }
        }
    }




    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( MutableElements.class );

    // The class of each element.
    private final Class<ELEMENT> elementClass;

    // The mutable elements.
    private final ELEMENT [] elements;

    // The lock which synchronizes access to these elements.
    private final MutableElements.Lock lock =
        new MutableElements.Lock ();

    /**
     * <p>
     * Creates a new MutableElements.
     * </p>
     *
     * @param element_class The Class of each element.
     *                      Must not be null.
     *
     * @param elements The mutable elements of this container.
     *                 Can change content over time, but not length.
     *                 Must not be null.  Must not contain
     *                 any null elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public MutableElements (
            Class<ELEMENT> element_class,
            ELEMENT ... elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               element_class, elements );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               elements );

        this.elementClass = element_class;
        this.elements = elements;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#elements()
     */
    @Override
    @SuppressWarnings({"unchecked", // Generic array creation.
                "try"}) // Lock.close() somehow throws InterruptedEx.
    public final ELEMENT [] array ()
        throws ElementsMustBeLocked.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        if ( ! this.isLockedByCurrentThread () )
        {
            throw ElementsMustBeLocked.CONTRACT.violation (
                Thread.currentThread (),
                this );
        }

        return this.elements;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#clamp(long, long, long)
     */
    @Override
    public final long clamp (
            long index,
            long start,
            long end
            )
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        if ( start < 0L
             || end < 0L
             || index == Countable.AFTER_LAST )
        {
            return Countable.NONE;
        }

        final long length = this.length ();
        final long offset =
            AbstractCountable.clamp ( index,
                                      start,
                                      end,
                                      length );
        final long clamped;
        if ( offset <= Countable.NONE )
        {
            clamped = offset;
        }
        else
        {
            final long absolute_start = this.start ();
            if ( this.direction () > 0L )
            {
                clamped = absolute_start + offset;
            }
            else
            {
                clamped = absolute_start - offset;
            }
        }

        return clamped;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#direction ()
     */
    @Override
    public final long direction ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation,
               Return.AlwaysLessThanOrEqualToZero.Violation
    {
        return 1L;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#duplicate ()
     */
    @Override
    public final MutableElements<ELEMENT> duplicate ()
        throws ReturnNeverNull.Violation
    {
        if ( ! this.isLockedByCurrentThread () )
        {
            throw ElementsMustBeLocked.CONTRACT.violation (
                Thread.currentThread (),
                this );
        }

        @SuppressWarnings("unchecked") // Generic array creation.
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( this.elementClass,
                                this.elements.length );
        System.arraycopy ( this.elements, 0,
                           new_array, 0, this.elements.length );

        final MutableElements<ELEMENT> new_elements =
            new MutableElements<ELEMENT> ( this.elementClass,
                                           new_array );

        return new_elements;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#elementClass()
     */
    @Override
    public final Class<ELEMENT> elementClass ()
        throws ReturnNeverNull.Violation
    {
        return this.elementClass;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#end ()
     */
    @Override
    public final long end ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation
    {
        return this.length () - 1L;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#isFixedLength ()
     */
    @Override
    public final boolean isFixedLength ()
    {
        return true;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#isImmutable ()
     */
    @Override
    public final boolean isImmutable ()
    {
        return false;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#isLockedBy(java.lang.Thread)
     */
    @Override
    public final boolean isLockedBy (
            Thread thread
            )
    {
        return this.lock.isLockedBy ( thread );
    }

    /**
     * @see musaico.foundation.term.countable.Elements#isLockedByCurrentThread()
     */
    @Override
    public final boolean isLockedByCurrentThread ()
    {
        return this.isLockedBy ( Thread.currentThread () );
    }

    /**
     * @see musaico.foundation.term.countable.Elements#length()
     */
    @Override
    @SuppressWarnings("try") // Lock.close() somehow throws InterruptedEx.
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        try
        {
            try ( final AutoCloseable lock = this.lock () )
            {
                return (long) this.elements.length;
            }
        }
        catch ( Exception e )
        {
            throw Return.AlwaysGreaterThanOrEqualToZero.CONTRACT.violation (
                this, // plaintiff
                -1L,  // evidence
                e );  // cause
        }
    }

    /**
     * @see musaico.foundation.term.countable.Elements#lock()
     */
    @Override
    public final AutoCloseable lock ()
        throws ReturnNeverNull.Violation
    {
        try
        {
            this.lock.open ();
        }
        catch ( IllegalStateException e )
        {
            final ReturnNeverNull.Violation violation =
                ReturnNeverNull.CONTRACT.violation (
                    this,              // plaintiff
                    this.elements );   // Evidence
            violation.initCause ( e );
            throw violation;
        }

        return this.lock;
    }

    /**
     * @see musaico.foundation.term.countable.Elements#lockOwner()
     */
    @Override
    public final Thread lockOwner ()
    {
        return this.lock.owner ();
    }

    /**
     * @see musaico.foundation.term.countable.Elements#start()
     */
    @Override
    public final long start ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation
    {
        return 0L;
    }
}
