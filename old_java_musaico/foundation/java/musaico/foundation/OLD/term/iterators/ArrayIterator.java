package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;


/**
 * <p>
 * An Iterator which steps over arrays, and does not allow elements
 * to be removed.
 * </p>
 *
 * <p>
 * NOT thread-safe.  Do not access an ArrayIterator from multiple
 * threads without providing external synchronization.
 * </p>
 *
 *
 * <p>
 * In Java, every ArrayIterator must be Serializable in order to
 * play nicely over RMI.  However, be warned that there is nothing
 * stopping someone from creating an ArrayIterator over an array
 * of non-Serializable objects.  Trying to serialize such an
 * ArrayIterator will result in exceptions.
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
public class ArrayIterator<VALUE extends Object>
    implements IndexedIterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( ArrayIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Optional protector against infinte loops.
    // Can be null.
    private final InfiniteLoopProtector infiniteLoopProtector;

    // The array to step through.
    private final VALUE [] array;

    // The start index (typically 0).
    private final int startIndex;

    // The end index (typically array.length - 1).
    private final int endIndex;

    // MUTABLE:
    // The next index to step to, or -1 if we're out of elements.
    private int nextIndex = Integer.MIN_VALUE;

    // MUTABLE:
    // The previous index we stepped to, or -1 if we're out of elements
    // or have not yet started stepping.
    private int lastIndex = -1;


    /**
     * <p>
     * Creates a new ArrayIterator.
     * </p>
     *
     * @param array The array of elements to iterate over.
     *              Must not be null.
     */
    @SafeVarargs // VALUE...elements parameter.
    @SuppressWarnings("varargs") // VALUE... passed as array to asList(...).a
    public ArrayIterator (
                          VALUE... array
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( array,               // array
               array == null
               || array.length == 0 // start_index
                   ? -1
                   : 0,
               array == null
               || array.length == 0 // end_index
                   ? -1
                   : array.length - 1 );
    }


    /**
     * <p>
     * Creates a new ArrayIterator.
     * </p>
     *
     * @param array The array of elements to iterate over.
     *              Must not be null.
     *
     * @param start_index The first index to iterate from.  Typically 0.
     *                    Can be greater than end_index, to iterate in reverse.
     *                    Can be -1 only if elements is an empty array.
     *                    Must be greater than or equal to -1.
     *                    Must be less than elements.length.
     *
     * @param end_index The last index to iterate to.
     *                  Typically elements.length - 1.
     *                  Can be less than start_index, to iterate in reverse.
     *                  Can be -1 only if elements is an empty array.
     *                  Must be greater than or equal to 0.
     *                  Must be less than elements.length.
     */
    public ArrayIterator (
                          VALUE [] array,
                          int start_index,
                          int end_index
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter2.MustBeGreaterThanOrEqualToNegativeOne.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeGreaterThanOrEqualToNegativeOne.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter2.MustBeLessThan.Violation,
               Parameter3.MustBeLessThan.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               array );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               array );
        if ( array.length == 0 )
        {
            classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToNegativeOne.CONTRACT,
                                   start_index );
            classContracts.check ( Parameter3.MustBeGreaterThanOrEqualToNegativeOne.CONTRACT,
                                   end_index );
        }
        else
        {
            classContracts.check ( Parameter2.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   start_index );
            classContracts.check ( Parameter3.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   end_index );
        }
        classContracts.check ( new Parameter2.MustBeLessThan ( array.length ),
                               start_index );
        classContracts.check ( new Parameter3.MustBeLessThan ( array.length ),
                               end_index );

        this.array = array;
        this.startIndex = start_index;
        this.endIndex = end_index;
        this.infiniteLoopProtector = null;

        this.nextIndex = this.startIndex;
        this.lastIndex = -1;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new ArrayIterator over an array of elements
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
     * @param array The array of elements to iterate over.
     *              Must not be null.
     */
    @SafeVarargs // VALUE...elements parameter.
    @SuppressWarnings("varargs") // VALUE... passed as array to asList(...).a
    public ArrayIterator (
                          InfiniteLoopProtector infinite_loop_protector,
                          VALUE... array
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( infinite_loop_protector,
               array,               // array
               array == null
               || array.length == 0 // start_index
                   ? -1
                   : 0,
               array == null
               || array.length == 0 // end_index
                   ? -1
                   : array.length - 1 );
    }


    /**
     * <p>
     * Creates a new ArrayIterator over an array of elements
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
     * @param array The array of elements to iterate over.
     *              Must not be null.
     *
     * @param start_index The first index to iterate from.  Typically 0.
     *                    Can be greater than end_index, to iterate in reverse.
     *                    Can be -1 only if elements is an empty array.
     *                    Must be greater than or equal to -1.
     *                    Must be less than elements.length.
     *
     * @param end_index The last index to iterate to.
     *                  Typically elements.length - 1.
     *                  Can be less than start_index, to iterate in reverse.
     *                  Can be -1 only if elements is an empty array.
     *                  Must be greater than or equal to 0.
     *                  Must be less than elements.length.
     */
    public ArrayIterator (
                          InfiniteLoopProtector infinite_loop_protector,
                          VALUE [] array,
                          int start_index,
                          int end_index
                          )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter3.MustBeGreaterThanOrEqualToNegativeOne.Violation,
               Parameter3.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter4.MustBeGreaterThanOrEqualToNegativeOne.Violation,
               Parameter4.MustBeGreaterThanOrEqualToZero.Violation,
               Parameter3.MustBeLessThan.Violation,
               Parameter4.MustBeLessThan.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               array, infinite_loop_protector );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               array );
        if ( array.length == 0 )
        {
            classContracts.check ( Parameter3.MustBeGreaterThanOrEqualToNegativeOne.CONTRACT,
                                   start_index );
            classContracts.check ( Parameter4.MustBeGreaterThanOrEqualToNegativeOne.CONTRACT,
                                   end_index );
        }
        else
        {
            classContracts.check ( Parameter3.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   start_index );
            classContracts.check ( Parameter4.MustBeGreaterThanOrEqualToZero.CONTRACT,
                                   end_index );
        }
        classContracts.check ( new Parameter3.MustBeLessThan ( array.length ),
                               start_index );
        classContracts.check ( new Parameter4.MustBeLessThan ( array.length ),
                               end_index );

        this.array = array;
        this.startIndex = start_index;
        this.endIndex = end_index;
        this.infiniteLoopProtector = infinite_loop_protector;

        this.nextIndex = this.startIndex;
        this.lastIndex = -1;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public final boolean hasNext ()
    {
        if ( this.nextIndex == -1 )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see musaico.foundation.term.iterators.IndexedIterator#index()
     */
    public final long index ()
        throws Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        return (long) this.lastIndex;
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
        if ( this.nextIndex < 0 )
        {
            throw IteratorMustHaveNextObject.CONTRACT.violation ( this,
                                                                  this );
        }

        if ( this.infiniteLoopProtector != null )
        {
            final IteratorMustBeFinite.Violation violation =
                this.infiniteLoopProtector.step ( this );
            if ( violation != null )
            {
                throw violation;
            }
        }

        this.lastIndex = this.nextIndex;

        final VALUE element = this.array [ this.nextIndex ];

        this.contracts.check ( ReturnNeverNull.CONTRACT,
                               element );

        if ( this.endIndex >= this.startIndex )
        {
            this.nextIndex ++;
            if ( this.nextIndex > this.endIndex )
            {
                this.nextIndex = -1;
            }
        }
        else
        {
            this.nextIndex --;
            if ( this.nextIndex < this.endIndex )
            {
                this.nextIndex = -1;
            }
        }

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
        final String maybe_index_range;
        if ( this.array.length == 0 )
        {
            maybe_index_range = "";
        }
        else if ( this.startIndex == 0
                  && this.endIndex == ( this.array.length - 1 ) )
        {
            maybe_index_range = "";
        }
        else
        {
            maybe_index_range = " [ " + this.startIndex + " .. "
                + this.endIndex + " ]";
        }

        final String array_as_string =
            StringRepresentation.of (
                this.array,
                StringRepresentation.DEFAULT_ARRAY_LENGTH );

        return ClassName.of ( this.getClass () )
            + " "
            + array_as_string
            + maybe_index_range;
    }
}
