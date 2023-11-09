package musaico.foundation.term.elements;

import java.io.Serializable;

import java.lang.reflect.Array;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Immutable elements.
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
 * @see musaico.foundation.term.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.term.elements.MODULE#LICENSE
 */
public class ImmutableElements<ELEMENT>
    implements Elements<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Does not actually lock anything.
     *  Used by ImmutableElements. */
    private static class DummyLock
        implements AutoCloseable, Serializable
    {
        private static final long serialVersionUID =
            ImmutableElements.serialVersionUID;

        /**
         * @see java.lang.AutoCloseable#close()
         */
        @Override
        public final void close ()
        {
            // Do absolutely nothing.
        }
    }


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( ImmutableElements.class );

    // The class of each element.
    private final Class<ELEMENT> elementClass;

    // The immutable elements.
    private final ELEMENT [] elements;

    // The lock which synchronizes access to these elements
    // (unnecessary for ImmutableElements).
    private final AutoCloseable lock = new ImmutableElements.DummyLock ();

    /**
     * <p>
     * Creates a new ImmutableElements.
     * </p>
     *
     * @param element_class The Class of each element.
     *                      Must not be null.
     *
     * @param elements The immutable elements of this container.
     *                 Must not be null.  Must not contain
     *                 any null elements.
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public ImmutableElements (
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
     * @see musaico.foundation.term.elements.Elements#elements()
     */
    @Override
    public final ELEMENT [] array ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.elements;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#clamp(long, long, long)
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
             || index == elements.AFTER_LAST )
        {
            return Elements.NONE;
        }

        final long length = this.length ();
        final long offset =
            Abstractelements.clamp ( index,
                                     start,
                                     end,
                                     length );
        final long clamped;
        if ( offset <= elements.NONE )
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
     * @see musaico.foundation.term.elements.Elements#direction ()
     */
    @Override
    public final long direction ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation,
               Return.AlwaysLessThanOrEqualToZero.Violation
    {
        return 1L;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#duplicate ()
     */
    @Override
    public final ImmutableElements<ELEMENT> duplicate ()
        throws ReturnNeverNull.Violation
    {
        @SuppressWarnings("unchecked") // Generic array creation.
        final ELEMENT [] new_array = (ELEMENT [])
            Array.newInstance ( this.elementClass,
                                this.elements.length );
        System.arraycopy ( this.elements, 0,
                           new_array, 0, this.elements.length );

        final ImmutableElements<ELEMENT> new_elements =
            new ImmutableElements<ELEMENT> ( this.elementClass,
                                             new_array );

        return new_elements;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#elementClass()
     */
    @Override
    public final Class<ELEMENT> elementClass ()
        throws ReturnNeverNull.Violation
    {
        return this.elementClass;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#end ()
     */
    @Override
    public final long end ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation
    {
        return this.length () - 1L;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#isFixedLength ()
     */
    @Override
    public final boolean isFixedLength ()
    {
        return true;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#isImmutable ()
     */
    @Override
    public final boolean isImmutable ()
    {
        return true;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#isLockedBy(java.lang.Thread)
     */
    @Override
    public final boolean isLockedBy (
            Thread thread
            )
    {
        return true;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#isLockedByCurrentThread()
     */
    @Override
    public final boolean isLockedByCurrentThread ()
    {
        return true;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return (long) this.elements.length;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#lock()
     */
    @Override
    public final AutoCloseable lock ()
        throws ReturnNeverNull.Violation
    {
        return this.lock;
    }

    /**
     * @see musaico.foundation.term.elements.Elements#lockOwner()
     */
    @Override
    public final Thread lockOwner ()
    {
        return Thread.currentThread ();
    }

    /**
     * @see musaico.foundation.term.elements.Elements#start()
     */
    @Override
    public final long start ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation,
               Return.AlwaysLessThan.Violation
    {
        return 0L;
    }
}
