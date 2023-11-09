package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * An Iterator which wraps another Iterator, passing each element
 * returned by the wrapped Iterator through a Filter, and stepping
 * through only those elements which are KEPT by the Filter.
 * </p>
 *
 * <p>
 * NOT thread-safe.  Do not access an FilteredIterator from multiple
 * threads without providing external synchronization.
 * </p>
 *
 *
 * <p>
 * In Java, every FilteredIterator must be Serializable in order to
 * play nicely over RMI.  However, be warned that there is nothing
 * stopping someone from creating an FilteredIterator to wrap
 * a non-Serializable Iterator.  Trying to serialize such an
 * FilteredIterator will result in exceptions.
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
public class FilteredIterator<VALUE extends Object>
    implements Iterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( FilteredIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Iterator whose elements we pass through a Filter.
    private final Iterator<VALUE> source;

    // The Filter which keeps or discards each element
    // from the source Iterator.
    private final Filter<VALUE> filter;

    // MUTABLE:
    // The next queued up element to be returned by next ().
    // When hasNext () is called, we must actually step through
    // one or more elements from the source Iterator to determine
    // if there are any more left.  If there is at least one, then
    // we queue it up at the end of hasNext (), and return it during
    // next () without bothering to re-evaluate it.
    private VALUE next = null;


    /**
     * <p>
     * Creates a new FilteredIterator.
     * </p>
     *
     * @param source The Iterator whose elements we pass through a
     *               Filter.  Must not be null.
     *
     * @param filter The Filter which keeps or discards each element
     *               from the source Iterator.
     *               Must not be null.
     */
    public FilteredIterator (
                             Iterator<VALUE> source,
                             Filter<VALUE> filter
                             )
        throws ParametersMustNotBeNull.Violation
    {
        this ( source,
               filter,
               null );
    }


    /**
     * <p>
     * Creates a new FilteredIterator.
     * </p>
     *
     * @param source The Iterator whose elements we pass through a
     *               Filter.  Must not be null.
     *
     *
     * @param filter The Filter which keeps or discards each element
     *               from the source Iterator.
     *               Must not be null.
     *
     * @param next The optional next queued up element to be returned
     *             by next ().  When hasNext () is called, we must
     *             actually step through one or more elements
     *             from the source Iterator to determine if there
     *             are any more left.  If there is at least one, then
     *             we queue it up at the end of hasNext (),
     *             and return it during next () without bothering
     *             to re-evaluate it.  Can be null.
     */
    @SuppressWarnings("unchecked") // Generic array creation.
    public FilteredIterator (
                             Iterator<VALUE> source,
                             Filter<VALUE> filter,
                             VALUE next
                             )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source, filter ); // next can be null.

        this.source = source;
        this.filter = filter;
        this.next = next;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext ()
    {
        if ( this.next != null )
        {
            return true;
        }
        else if ( ! this.source.hasNext () )
        {
            return false;
        }

        this.next = this.next ();

        if ( this.next == null )
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
    public final VALUE next ()
        throws ReturnNeverNull.Violation,
               IteratorMustBeFinite.Violation,
               IteratorMustHaveNextObject.Violation
    {
        if ( this.next != null )
        {
            final VALUE next = this.next;
            this.next = null;
            return next;
        }

        while ( this.source.hasNext () )
        {
            final VALUE next = this.source.next ();
            if ( this.filter.filter ( next )
                     .isKept () )
            {
                return next;
            }
        }

        // Uh-oh.  No more source elements.
        throw IteratorMustHaveNextObject.CONTRACT.violation ( this,
                                                              this );
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
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.source
            + " | "
            + this.filter;
    }
}
