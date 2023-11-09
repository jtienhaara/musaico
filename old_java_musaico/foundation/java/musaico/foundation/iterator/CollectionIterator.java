package musaico.foundation.iterator;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.StringRepresentation;


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
 * @see musaico.foundation.iterator.MODULE#COPYRIGHT
 * @see musaico.foundation.iterator.MODULE#LICENSE
 */
public class CollectionIterator<VALUE extends Object>
    implements IndexedIterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Collection to step through.
    private final Collection<VALUE> collection;

    // The Collection's own Iterator, which we wrap only if we are
    // iterating over the entire Collection:
    private final Iterator<VALUE> iterator;

    // A List representation of the Collection, only if we are iterating
    // over a sub-range of the Collection:
    private final List<VALUE> list;

    // The start index (typically 0).
    private final int startIndex;

    // The end index (typically collection.size () - 1).
    private final int endIndex;

    // MUTABLE:
    // The next index to step to, or -1 if we're out of elements.
    private int nextIndex = Integer.MIN_VALUE;

    // MUTABLE:
    // The previous index we stepped to, or -1 if we have not yet
    // started stepping.
    private int lastIndex = -1;


    /**
     * <p>
     * Creates a new CollectionIterator.
     * </p>
     *
     * @param collection The Collection of elements to iterate over.
     *                   Must not be null.
     *
     * @throws NullPointerException If the specified Collection is null.
     */
    public CollectionIterator (
            Collection<VALUE> collection
            )
        throws NullPointerException
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
     *                    Can be -1 only if the Collection is empty.
     *                    Must be greater than or equal to -1.
     *                    Must be less than collection.size ().
     *
     * @param end_index The last index to iterate to.
     *                  Typically collection.size () - 1.
     *                  Can be less than start_index, to iterate in reverse.
     *                  Can be -1 only if the Collection is empty.
     *                  Must be greater than or equal to -1.
     *                  Must be less than collection.size ().
     *
     * @throws NullPointerException If the specified Collection is null.
     *
     * @throws IndexOutOfBoundsException If either or both of the start
     *                                   and/or end indices is/are
     *                                   less than 0 (-1 in the case
     *                                   of an empty Collection)
     *                                   or greater than
     *                                   or equal to Collection.size ().
     */
    public CollectionIterator (
            Collection<VALUE> collection,
            int start_index,
            int end_index
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( collection == null )
        {
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " with collection = null" );
        }
        else if ( collection.size () == 0 )
        {
            if ( start_index != -1
                 || end_index != -1 )
            {
                throw new IndexOutOfBoundsException ( "ERROR Cannot create "
                                                      + ClassName.of ( this.getClass () )
                                                      + " with collection = "
                                                      + StringRepresentation.of (
                                                            collection,
                                                            StringRepresentation.DEFAULT_ARRAY_LENGTH )
                                                      + " and start index = "
                                                      + start_index
                                                      + " and end index = "
                                                      + end_index );
            }
        }
        else if ( start_index < 0
                  || start_index >= collection.size ()
                  || end_index < 0
                  || end_index >= collection.size () )
        {
            throw new IndexOutOfBoundsException ( "ERROR Cannot create "
                                                  + ClassName.of ( this.getClass () )
                                                  + " with collection = "
                                                  + StringRepresentation.of (
                                                        collection,
                                                        StringRepresentation.DEFAULT_ARRAY_LENGTH )
                                                  + " and start index = "
                                                  + start_index
                                                  + " and end index = "
                                                  + end_index );
        }

        this.startIndex = start_index;
        this.endIndex = end_index;

        this.nextIndex = this.startIndex; // Could be -1.
        this.lastIndex = -1;

        this.collection = collection;

        // Iterate over the whole Collection:
        if ( this.collection.size () == 0 )
        {
            this.iterator = this.collection.iterator ();
            this.list = null;
        }
        else if ( this.startIndex == 0
             && this.endIndex == ( this.collection.size () - 1 ) )
        {
            this.iterator = this.collection.iterator ();
            this.list = null;
        }
        // Iterate over only part of the Collection:
        else if ( collection instanceof List )
        {
            this.iterator = null;
            this.list = (List<VALUE>) collection;
        }
        else
        {
            this.iterator = null;
            this.list = new ArrayList<VALUE> ( collection );
        }
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
     * @see musaico.foundation.iterator.IndexedIterator#index()
     */
    public final long index ()
    {
        return (long) this.lastIndex;
    }



    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public final VALUE next ()
        throws IndexOutOfBoundsException
    {
        if ( this.nextIndex < 0 )
        {
            throw new IndexOutOfBoundsException ( "ERROR no more elements"
                                                  + " to iterate over: "
                                                  + StringRepresentation.of (
                                                        this.collection,
                                                        StringRepresentation.DEFAULT_ARRAY_LENGTH ) );
        }

        this.lastIndex = this.nextIndex;

        // Could be null:
        final VALUE element;
        if ( this.iterator != null )
        {
            element = this.iterator.next ();

            if ( this.iterator.hasNext () )
            {
                this.nextIndex ++;
            }
            else
            {
                this.nextIndex = -1;
            }
        }
        else // this.iterator = null, so this.list != null.  Sub-range.
        {
            element = this.list.get ( this.nextIndex );

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
        else if ( this.iterator != null )
        {
            maybe_index_range = "";
        }
        else
        {
            maybe_index_range = " [ " + this.startIndex + " .. "
                + this.endIndex + " ]";
        }

        final String collection_as_string =
            StringRepresentation.of
 (
                this.collection,
                StringRepresentation.DEFAULT_ARRAY_LENGTH );

        final String next_index = " [ " + this.nextIndex + " ]";

        return ClassName.of ( this.getClass () )
            + " "
            + collection_as_string
            + maybe_index_range
            + next_index;
    }
}
