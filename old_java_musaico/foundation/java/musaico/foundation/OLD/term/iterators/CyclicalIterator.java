package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An Iterator which steps over a finite cycle of elements, repeating
 * the cycle ad infinitum, and does not allow removing elements.
 * </p>
 *
 *
 * <p>
 * In Java, every CyclicalIterator must be Serializable in order to
 * play nicely over RMI.
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
 * @see musaico.foundation.term.iterators.MODULE#COPYRIGHT
 * @see musaico.foundation.term.iterators.MODULE#LICENSE
 */
public class CyclicalIterator<VALUE extends Object>
    extends UnchangingIterator<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( CyclicalIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The finite cycle which we repeatedly iterate through.
    // Only set once, during the constructor of UnchangingIterator.
    private Iterable<VALUE> cycle = null;

    // The next position in the cycle.  When null, start from the
    // beginning all over again.
    // MUTABLE.  Changes over time.
    private Iterator<VALUE> iterator = null;


    /**
     * <p>
     * Creates a new infinite CyclicalIterator over the specified
     * finite cycle.
     * </p>
     */
    public CyclicalIterator (
                             Iterable<VALUE> cycle
                             )
        throws Parameter1.MustNotChange.Violation
    {
        super ( cycle );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               cycle);

        this.iterator = null;

        this.contracts = new Advocate ( this );
    }

    /**
     * @see musaico.foundation.term.iterators.UnchangingIterator#unchangingElements(java.util.List)
     */
    @Override
    protected void unchangingElements (
                                       List<VALUE> cycle
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               this.cycle );

        this.cycle = cycle;
    }


    /**
     * @see java.util.Iterator#hasNext()
     * @see musaico.foundation.term.iterators.UnchangingIterator#hasNext()
     */
    @Override
    public final boolean hasNext ()
    {
        return true;
    }


    /**
     * @see java.util.Iterator#next()
     * @see musaico.foundation.term.iterators.UnchangingIterator#next()
     */
    @Override
    public final VALUE next ()
        throws ReturnNeverNull.Violation
    {
        final Iterator<VALUE> one_cycle;
        synchronized ( this.lock )
        {
            if ( this.iterator == null
                 || ! this.iterator.hasNext () )
            {
                // Back to the start of the cycle.
                // Create a new Iterator.
                this.iterator = this.cycle.iterator ();
            }

            one_cycle = this.iterator;
        }

        final VALUE element = one_cycle.next ();

        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               element );

        return element;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "Cyclical { " + this.cycle + " }";
    }
}
