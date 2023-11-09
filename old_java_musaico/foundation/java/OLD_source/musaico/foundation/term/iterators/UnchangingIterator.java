package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.HashedObject;

import musaico.foundation.domains.time.BeforeAndAfter;


/**
 * <p>
 * An Iterator which steps over a sequence of elements which is guaranteed
 * to never change in the middle of iterating (even if iteration takes a
 * long time).
 * </p>
 *
 *
 * <p>
 * In Java, every UnchangingIterator must be Serializable in order to
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
public class UnchangingIterator<VALUE extends Object>
    implements Iterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( UnchangingIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The child iterator which does the work for us, stepping through
    // the list of unchanging elements.
    private final Iterator<VALUE> unchangingIterator;


    /**
     * <p>
     * Creates a new UnchangingIterator over the specified
     * sequence of elements.
     * </p>
     */
    public UnchangingIterator (
                               Iterable<VALUE> changeable_elements
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustNotChange.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               changeable_elements );

        final HashedObject before = new HashedObject ( changeable_elements );

        final List<VALUE> unchanging_elements = new ArrayList<VALUE> ();
        try
        {
            for ( VALUE element : changeable_elements )
            {
                unchanging_elements.add ( element );
            }
        }
        catch ( ConcurrentModificationException e )
        {
            final HashedObject after =
                new HashedObject ( changeable_elements );
            final BeforeAndAfter before_and_after =
                new BeforeAndAfter ( before, after );
            throw Parameter1.MustNotChange.CONTRACT.violation ( this,
                                                                before_and_after );
        }

        this.unchangingElements ( unchanging_elements );

        this.unchangingIterator = unchanging_elements.iterator ();

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new UnchangingIterator over the specified
     * sequence of elements.
     * </p>
     */
    @SafeVarargs // VALUE... changeable_elements possible heap pollution.
    @SuppressWarnings("varargs") // Possible heap pollution new HashedObj(...).
    public UnchangingIterator (
                               VALUE... changeable_elements
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustNotChange.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               changeable_elements );

        final HashedObject before = new HashedObject ( changeable_elements );

        final List<VALUE> unchanging_elements = new ArrayList<VALUE> ();
        try
        {
            for ( VALUE element : changeable_elements )
            {
                unchanging_elements.add ( element );
            }
        }
        catch ( ConcurrentModificationException e )
        {
            final HashedObject after =
                new HashedObject ( changeable_elements );
            final BeforeAndAfter before_and_after =
                new BeforeAndAfter ( before, after );
            throw Parameter1.MustNotChange.CONTRACT.violation ( this,
                                                                before_and_after );
        }

        this.unchangingElements ( unchanging_elements );

        this.unchangingIterator = unchanging_elements.iterator ();

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Accepts an unchanging list of elements from the UnchangingIterator
     * constructor.
     * </p>
     *
     * <p>
     * This method must only ever be invoked once, at construction time.
     * </p>
     *
     * @param unchanging_elements The unchanging list of elements
     *                            underpinning this iterator.
     *                            Must not be null.
     */
    protected void unchangingElements (
                                       List<VALUE> unchanging_elements
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        // By default do nothing.
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext ()
    {
        return this.unchangingIterator.hasNext ();
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public VALUE next ()
        throws ReturnNeverNull.Violation
    {
        final VALUE element = this.unchangingIterator.next ();

        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               element );

        return element;
    }


    /**
     * @see java.util.Iterator#remove()
     */
    @Override
    public final void remove ()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException ( ClassName.of ( this.getClass () )
                                                  + ".remove () not supported" );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () )
            + " { " + this.unchangingIterator + " }";
    }
}
