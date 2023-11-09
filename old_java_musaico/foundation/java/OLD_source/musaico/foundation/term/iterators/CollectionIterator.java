package musaico.foundation.term.iterators;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


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
 * An Iterator which steps over Collections, and does not allow elements
 * to be removed.
 * </p>
 *
 * <p>
 * NOT thread-safe.  Do not access an CollectionIterator from multiple
 * threads without providing external synchronization.
 * </p>
 *
 *
 * <p>
 * In Java, every CollectionIterator must be Serializable in order to
 * play nicely over RMI.  However, be warned that there is nothing
 * stopping someone from creating an CollectionIterator over a Collection
 * of non-Serializable objects.  Trying to serialize such an
 * CollectionIterator will result in exceptions.
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
public class CollectionIterator<VALUE extends Object>
    implements IndexedIterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( CollectionIterator.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Optional protector against infinte loops.
    // Can be null.
    private final InfiniteLoopProtector infiniteLoopProtector;

    // The List representation of the Collection to step through.
    private final List<VALUE> collection;

    // The start index (typically 0).
    private final int startIndex;

    // The end index (typically collection.size () - 1).
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
     * Creates a new CollectionIterator.
     * </p>
     *
     * @param collection The Collection of elements to iterate over.
     *                   Must not be null.
     */
    public CollectionIterator (
                               Collection<VALUE> collection
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( collection,                // collection
               collection == null
               || collection.size () == 0 // start_index
                   ? -1
                   : 0,
               collection == null
               || collection.size () == 0 // end_index
                   ? -1
                   : collection.size () - 1 );
    }


    /**
     * <p>
     * Creates a new CollectionIterator.
     * </p>
     *
     * @param collection The Collection of elements to iterate over.
     *                   Must not be null.
     *
     * @param start_index The first index to iterate from.  Typically 0.
     *                    Can be greater than end_index, to iterate in reverse.
     *                    Can be -1 only if elements is an empty Collection.
     *                    Must be greater than or equal to -1.
     *                    Must be less than elements.size ().
     *
     * @param end_index The last index to iterate to.
     *                  Typically elements.size () - 1.
     *                  Can be less than start_index, to iterate in reverse.
     *                  Can be -1 only if elements is an empty Collection.
     *                  Must be greater than or equal to 0.
     *                  Must be less than elements.size ().
     */
    public CollectionIterator (
                               Collection<VALUE> collection,
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
                               collection );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               collection );
        if ( collection.size () == 0 )
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
        classContracts.check ( new Parameter2.MustBeLessThan ( collection.size () ),
                               start_index );
        classContracts.check ( new Parameter3.MustBeLessThan ( collection.size () ),
                               end_index );

        if ( collection instanceof List )
        {
            this.collection = (List<VALUE>) collection;
        }
        else
        {
            this.collection = new ArrayList<VALUE> ( collection );
        }
        this.startIndex = start_index;
        this.endIndex = end_index;
        this.infiniteLoopProtector = null;

        this.nextIndex = this.startIndex;
        this.lastIndex = -1;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new CollectionIterator over a Collection of elements
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
     * @param collection The Collection of elements to iterate over.
     *                   Must not be null.
     */
    public CollectionIterator (
                               InfiniteLoopProtector infinite_loop_protector,
                               Collection<VALUE> collection
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( infinite_loop_protector,
               collection,                // collection
               collection == null
               || collection.size () == 0 // start_index
                   ? -1
                   : 0,
               collection == null
               || collection.size () == 0 // end_index
                   ? -1
                   : collection.size () - 1 );
    }


    /**
     * <p>
     * Creates a new CollectionIterator over a Collection of elements
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
     * @param collection The Collection of elements to iterate over.
     *                   Must not be null.
     *
     * @param start_index The first index to iterate from.  Typically 0.
     *                    Can be greater than end_index, to iterate in reverse.
     *                    Can be -1 only if elements is an empty Collection.
     *                    Must be greater than or equal to -1.
     *                    Must be less than elements.size ().
     *
     * @param end_index The last index to iterate to.
     *                  Typically elements.size () - 1.
     *                  Can be less than start_index, to iterate in reverse.
     *                  Can be -1 only if elements is an empty Collection.
     *                  Must be greater than or equal to 0.
     *                  Must be less than elements.size ().
     */
    public CollectionIterator (
                               InfiniteLoopProtector infinite_loop_protector,
                               Collection<VALUE> collection,
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
                               collection, infinite_loop_protector );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               collection );
        if ( collection.size () == 0 )
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
        classContracts.check ( new Parameter3.MustBeLessThan ( collection.size () ),
                               start_index );
        classContracts.check ( new Parameter4.MustBeLessThan ( collection.size () ),
                               end_index );

        if ( collection instanceof List )
        {
            this.collection = (List<VALUE>) collection;
        }
        else
        {
            this.collection = new ArrayList<VALUE> ( collection );
        }
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

        final VALUE element = this.collection.get ( this.nextIndex );

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
        if ( this.collection.size () == 0 )
        {
            maybe_index_range = "";
        }
        else if ( this.startIndex == 0
                  && this.endIndex == ( this.collection.size () - 1 ) )
        {
            maybe_index_range = "";
        }
        else
        {
            maybe_index_range = " [ " + this.startIndex + " .. "
                + this.endIndex + " ]";
        }

        final String collection_as_string =
            StringRepresentation.of (
                this.collection,
                StringRepresentation.DEFAULT_ARRAY_LENGTH );

        return ClassName.of ( this.getClass () )
            + " "
            + collection_as_string
            + maybe_index_range;
    }
}
