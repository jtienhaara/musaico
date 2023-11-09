package musaico.foundation.domains.iterators;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;


/**
 * <p>
 * An Iterator which steps over arrays, and which does not allow elements
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
 * @see musaico.foundation.domains.iterators.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.iterators.MODULE#LICENSE
 */
public class ArrayIterator<VALUE extends Object>
    implements IndexedIterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


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
    // The previous index we stepped to, or -1 if we have not yet
    // started stepping.
    private int lastIndex = -1;


    /**
     * <p>
     * Creates a new ArrayIterator.
     * </p>
     *
     * @param array The array of elements to iterate over.
     *              Can contain null elements.  Must not be null.
     *
     * @throws NullPointerException If the specified array is null.
     */
    @SafeVarargs // VALUE...elements parameter.
    @SuppressWarnings("varargs") // VALUE... passed as array to asList(...).a
    public ArrayIterator (
            VALUE... array
            )
        throws NullPointerException
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
     *              Can contain null elements.  Must not be null.
     *
     * @param start_index The first index to iterate from.  Typically 0.
     *                    Can be greater than end_index, to iterate in reverse.
     *                    Can be -1 only if the array is empty.
     *                    Must be greater than or equal to -1.
     *                    Must be less than array.length.
     *
     * @param end_index The last index to iterate to.
     *                  Typically array.length - 1.
     *                  Can be less than start_index, to iterate in reverse.
     *                  Can be -1 only if the array is empty.
     *                  Must be greater than or equal to -1.
     *                  Must be less than array.length.
     *
     * @throws NullPointerException If the specified array is null.
     *
     * @throws IndexOutOfBoundsException If either or both of the start
     *                                   and/or end indices is/are
     *                                   less than 0 (-1 in the case
     *                                   of an empty array)
     *                                   or greater than
     *                                   or equal to array.length.
     */
    public ArrayIterator (
            VALUE [] array,
            int start_index,
            int end_index
            )
        throws NullPointerException,
               IndexOutOfBoundsException
    {
        if ( array == null )
        {
            throw new NullPointerException ( "ERROR Cannot create "
                                             + ClassName.of ( this.getClass () )
                                             + " with array = null" );
        }
        else if ( array.length == 0 )
        {
            if ( start_index != -1
                 || end_index != -1 )
            {
                throw new IndexOutOfBoundsException ( "ERROR Cannot create "
                                                      + ClassName.of ( this.getClass () )
                                                      + " with array = "
                                                      + StringRepresentation.of (
                                                            array,
                                                            StringRepresentation.DEFAULT_ARRAY_LENGTH )
                                                      + " and start index = "
                                                      + start_index
                                                      + " and end index = "
                                                      + end_index );
            }
        }
        else if ( start_index < 0
                  || start_index >= array.length
                  || end_index < 0
                  || end_index >= array.length )
        {
            throw new IndexOutOfBoundsException ( "ERROR Cannot create "
                                                  + ClassName.of ( this.getClass () )
                                                  + " with array = "
                                                  + StringRepresentation.of (
                                                        array,
                                                        StringRepresentation.DEFAULT_ARRAY_LENGTH )
                                                  + " and start index = "
                                                  + start_index
                                                  + " and end index = "
                                                  + end_index );
        }

        this.array = array;
        this.startIndex = start_index;
        this.endIndex = end_index;

        this.nextIndex = this.startIndex;
        this.lastIndex = -1;
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
     * @see musaico.foundation.domains.iterators.IndexedIterator#index()
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
                                                        this.array,
                                                        StringRepresentation.DEFAULT_ARRAY_LENGTH ) );
        }

        this.lastIndex = this.nextIndex;

        final VALUE element = this.array [ this.nextIndex ];

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

        final String next_index = " [ " + this.nextIndex + " ]";

        return ClassName.of ( this.getClass () )
            + " "
            + array_as_string
            + maybe_index_range
            + next_index;
    }
}
