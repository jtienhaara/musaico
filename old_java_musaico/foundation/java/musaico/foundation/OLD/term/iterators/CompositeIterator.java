package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;


/**
 * <p>
 * An Iterator which steps over the elements of first one child iterator,
 * then another, then another, and so on, until all of the child
 * iterators have been used up.
 * </p>
 *
 *
 * <p>
 * In Java, every CompositeIterator must be Serializable in order to
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
public class CompositeIterator<VALUE extends Object>
    extends UnchangingIterator<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( CompositeIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // The child iterators to step through.
    private final UnchangingIterator<VALUE> [] childIterators;

    // MUTABLE:
    // The index of the current child iterator.
    private int childIndex;


    /**
     * <p>
     * Creates a new CompositeIterator over the specified
     * child iterators.
     * </p>
     *
     * @param iterables Iterables whose iterators will be stepped through.
     *                  If a given Iterable does not return
     *                  an UnchangingIterator, then one is created for it.
     *                  Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings({"varargs", "rawtypes", "unchecked"}) // Heap pollution
        // generic varargs, generic array creation.
    @SafeVarargs
    public CompositeIterator (
            Iterable<VALUE> ... iterables
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        super (); // The parent doesn't need any elements,
                  // we'll deal with them.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) iterables );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               iterables );

        this.childIterators = (UnchangingIterator<VALUE> [])
            new UnchangingIterator [ iterables.length ];
        for ( int c = 0; c < iterables.length; c ++ )
        {
            final Iterator<VALUE> child_iterator =
                iterables [ c ].iterator ();
            if ( child_iterator instanceof UnchangingIterator )
            {
                this.childIterators [ c ] = (UnchangingIterator<VALUE>)
                    child_iterator;
            }
            else
            {
                this.childIterators [ c ] =
                    new UnchangingIterator<VALUE> ( iterables [ c ] );
            }
        }

        this.childIndex = 0;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new CompositeIterator over the specified
     * child iterators.
     * </p>
     *
     * @param child_iterators The child iterators to step through.
     *                        Each must be unchanging.  Must not be null.
     *                        Must not contain any null elements.
     */
    @SuppressWarnings({"varargs", "rawtypes", "unchecked"}) // Heap pollution
        // generic varargs, generic array creation.
    @SafeVarargs
    public CompositeIterator (
            UnchangingIterator<VALUE> ... child_iterators
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        super (); // The parent doesn't need any elements,
                  // we'll deal with them.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) child_iterators );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               child_iterators );

        this.childIterators = (UnchangingIterator<VALUE> [])
            new UnchangingIterator [ child_iterators.length ];
        System.arraycopy ( child_iterators, 0,
                           this.childIterators, 0, child_iterators.length );

        this.childIndex = 0;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see java.util.Iterator#hasNext()
     * @see musaico.foundation.term.iterators.UnchangingIterator#hasNext()
     */
    @Override
    public final boolean hasNext ()
    {
        synchronized ( this.lock )
        {
            if ( this.childIndex < 0
                 || this.childIndex > this.childIterators.length )
            {
                return false;
            }

            while ( ! this.childIterators [ this.childIndex ].hasNext () )
            {
                this.childIndex ++;
                if ( this.childIndex > this.childIterators.length )
                {
                    return false;
                }
            }

            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     * @see musaico.foundation.term.iterators.UnchangingIterator#next()
     */
    @Override
    public final VALUE next ()
        throws IteratorMustHaveNextObject.Violation,
               ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            if ( ! this.hasNext () )
            {
                throw IteratorMustHaveNextObject.CONTRACT.violation (
                    this,   // plaintiff
                    this ); // evidence
            }

            // After calling this.hasNext (), we know that this.childIndex
            // is set correctly.
            return this.childIterators [ this.childIndex ].next ();
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { "
            + StringRepresentation.of ( this.childIterators,
                                        0 )
            + " }";
    }
}
