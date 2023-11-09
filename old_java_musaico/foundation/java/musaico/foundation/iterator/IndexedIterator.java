package musaico.foundation.iterator;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * Steps through an Iterable sequence of objects, keeping track of
 * the index of each object in the sequence.
 * </p>
 *
 *
 * <p>
 * In Java every IndexedIterator must be Serializable in order
 * to play nicely over RMI.
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
public interface IndexedIterator<VALUE extends Object>
    extends Iterator<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every IndexedIterator must implement hasNext (), next () and remove ().


    /**
     * @return The index of the most recently returned object
     *         in the sequence, if any has been returned;
     *         or <code> Elements.NONE </code> if this iterator
     *         has not yet stepped, or has stepped past the end
     *         of its Iterable sequence.  Always either Elements.NONE,
     *         or 0L or greater.
     */
    public abstract long index ();
}
