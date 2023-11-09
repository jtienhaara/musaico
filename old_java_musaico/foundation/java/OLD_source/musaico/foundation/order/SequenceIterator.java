package musaico.foundation.order;

import java.io.Serializable;

import java.util.Iterator;
import java.util.NoSuchElementException;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Iterates over the elements in a Sequence.
 * </p>
 *
 *
 * <p>
 * In Java a SequenceIterator must be Serializable in order to play
 * nicely over RMI.  However only a Sequence with Serializable
 * elements will actually play nicely over RMI.  Sequences can be
 * comprised of non-Serializable objects if the sequence builder chooses.
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public class SequenceIterator<ELEMENT extends Serializable>
    implements Iterator<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method parameters etc. */
    private static final Advocate classContracts =
        new Advocate ( SequenceIterator.class );


    /** The sequence to iterate over. */
    private final Sequence<ELEMENT> sequence;

    /** The next element from the sequence to return.
     *  Changes over time. */
    private ELEMENT nextElement;


    /**
     * <p>
     * Creates a new SequenceIterator for the specified Sequence.
     * </p>
     *
     * @param sequence The sequence to step through.
     *                 Must not be null.
     */
    public SequenceIterator (
                             Sequence<ELEMENT> sequence
                             )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sequence );

        this.sequence = sequence;
        this.nextElement = this.sequence.first ();
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext ()
    {
        if ( this.sequence.noSuchElement ().equals ( this.nextElement ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public ELEMENT next ()
        throws NoSuchElementException
    {
        ELEMENT current_element = this.nextElement;
        if ( this.sequence.noSuchElement ().equals ( current_element ) )
        {
            throw new NoSuchElementException ( "SequenceIterator "
                                               + this
                                               + " for sequence "
                                               + sequence
                                               + " has no more elements" );
        }

        this.nextElement = this.sequence.after ( current_element );

        return current_element;
    }


    /**
     * @see java.util.Iterator#remove()
     *
     * <p>
     * Always throws UnsupportedOperationException.  It does
     * not make any sense to remove an element from an immutable
     * Sequence.
     * </p>
     */
    @Override
    public void remove ()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException ( "Remove is not supported by " + this.getClass () + " " + this );
    }
}
