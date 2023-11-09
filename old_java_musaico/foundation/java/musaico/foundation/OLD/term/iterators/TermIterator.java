package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * An Iterator which steps over 0, 1 or many items, but does
 * not allow removing elements.
 * </p>
 *
 * <p>
 * Depending on the underlying iterator which is used by the TermIterator
 * to do most of its work, the TermIterator is typically
 * NOT thread-safe.  Do not access a TermIterator from multiple
 * threads without providing external synchronization.
 * </p>
 *
 *
 * <p>
 * In Java, every TermIterator must be Serializable in order to
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
public class TermIterator<VALUE extends Object>
    implements Iterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The underlying iterator implementation for this TermIterator.
    private final Iterator<VALUE> iterator;

    // Optional protector against infinte loops.
    // Can be null.
    private final InfiniteLoopProtector infiniteLoopProtector;


    /**
     * <p>
     * Creates a new TermIterator over no elements.
     * </p>
     */
    public TermIterator ()
    {
        this.iterator = new ArrayList<VALUE> ().iterator ();
        this.infiniteLoopProtector = null;

        this.contracts = new Advocate ( this );
    }




    /**
     * <p>
     * Creates a new TermIterator over no elements,
     * and the specified protector against infinite loops.
     * </p>
     *
     * @param infinite_loop_protector Protects against iterating infinitely,
     *                                typically by counting the number of
     *                                iterations and causing a contract
     *                                violation once the iterator hits a
     *                                certain threshold count.
     *                                Must not be null.
     */
    public TermIterator (
                          InfiniteLoopProtector infinite_loop_protector
                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               infinite_loop_protector );

        this.iterator = new ArrayList<VALUE> ().iterator ();
        this.infiniteLoopProtector = infinite_loop_protector;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator over one element.
     * </p>
     */
    public TermIterator (
                         VALUE element
                         )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               element );

        final List<VALUE> elements = new ArrayList<VALUE> ();
        elements.add ( element );
        this.iterator = elements.iterator ();
        this.infiniteLoopProtector = null;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator over one element,
     * and the specified protector against infinite loops.
     * </p>
     *
     * @param element The single element to iterate over.
     *                Must not be null.
     *
     * @param infinite_loop_protector Protects against iterating infinitely,
     *                                typically by counting the number of
     *                                iterations and causing a contract
     *                                violation once the iterator hits a
     *                                certain threshold count.
     *                                Must not be null.
     */
    public TermIterator (
                          VALUE element,
                          InfiniteLoopProtector infinite_loop_protector
                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               element, infinite_loop_protector );

        final List<VALUE> elements = new ArrayList<VALUE> ();
        elements.add ( element );
        this.iterator = elements.iterator ();
        this.infiniteLoopProtector = infinite_loop_protector;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator over a vector (many elements).
     * </p>
     *
     * @param iterable_elements The elements to iterate over, such as
     *                          a List or other Collection.
     *                          Must not be null.
     */
    public TermIterator (
                          Iterable<VALUE> iterable_elements
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               iterable_elements );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               iterable_elements );

        this.iterator = iterable_elements.iterator ();
        this.infiniteLoopProtector = null;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator over a vector (many elements),
     * and the specified protector against infinite loops.
     * </p>
     *
     * @param iterable_elements The elements to iterate over, such as
     *                          a List or other Collection.
     *                          Must not be null.
     *
     * @param infinite_loop_protector Protects against iterating infinitely,
     *                                typically by counting the number of
     *                                iterations and causing a contract
     *                                violation once the iterator hits a
     *                                certain threshold count.
     *                                Must not be null.
     */
    public TermIterator (
                          Iterable<VALUE> iterable_elements,
                          InfiniteLoopProtector infinite_loop_protector
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               iterable_elements, infinite_loop_protector );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               iterable_elements );

        this.iterator = iterable_elements.iterator ();
        this.infiniteLoopProtector = infinite_loop_protector;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator over a vector (many elements).
     * </p>
     *
     * @param elements The array of elements to iterate over.
     *                 Must not be null.
     */
    @SafeVarargs // VALUE...elements parameter.
    @SuppressWarnings("varargs") // VALUE... passed as array to asList(...).a
    public TermIterator (
                         VALUE... elements
                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        final List<VALUE> as_list = Arrays.asList ( elements );
        this.iterator = as_list.iterator ();
        this.infiniteLoopProtector = null;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator over a vector (many elements),
     * and the specified protector against infinite loops.
     * </p>
     *
     * @param infinite_loop_protector Protects against iterating infinitely,
     *                                typically by counting the number of
     *                                iterations and causing a contract
     *                                violation once the iterator hits a
     *                                certain threshold count.
     *                                Must not be null.
     *
     * @param elements The array of elements to iterate over.
     *                 Must not be null.
     */
    @SafeVarargs // VALUE...elements parameter.
    @SuppressWarnings("varargs") // VALUE... passed as array to asList(...).a
    public TermIterator (
                         InfiniteLoopProtector infinite_loop_protector,
                         VALUE... elements
                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        final List<VALUE> as_list = Arrays.asList ( elements );
        this.iterator = as_list.iterator ();
        this.infiniteLoopProtector = infinite_loop_protector;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator to wrap another Iterator.
     * </p>
     *
     * @param iterator The elements iterator to wrap.  Must not be null.
     */
    public TermIterator (
                          Iterator<VALUE> iterator
                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               iterator );

        this.iterator = iterator;
        this.infiniteLoopProtector = null;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermIterator to wrap another Iterator,
     * and the specified protector against infinite loops.
     * </p>
     *
     * @param iterator The elements iterator to wrap.  Must not be null.
     *
     * @param infinite_loop_protector Protects against iterating infinitely,
     *                                typically by counting the number of
     *                                iterations and causing a contract
     *                                violation once the iterator hits a
     *                                certain threshold count.
     *                                Must not be null.
     */
    public TermIterator (
                          Iterator<VALUE> iterator,
                          InfiniteLoopProtector infinite_loop_protector
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               iterator );

        this.iterator = iterator;
        this.infiniteLoopProtector = infinite_loop_protector;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext ()
    {
        return this.iterator.hasNext ();
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public final VALUE next ()
        throws ReturnNeverNull.Violation,
               IteratorMustBeFinite.Violation
    {
        if ( this.infiniteLoopProtector != null )
        {
            final IteratorMustBeFinite.Violation violation =
                this.infiniteLoopProtector.step ( this );
            if ( violation != null )
            {
                throw violation;
            }
        }

        final VALUE element = this.iterator.next ();

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
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { " + this.iterator + " }";
    }
}
