package musaico.foundation.domains;

import java.io.Serializable;


/**
 * <p>
 * Generates the element at each specific index.
 * </p>
 *
 * <p>
 * Every Generator is immutable and idempotent.
 * </p>
 *
 * <p>
 * A Generator always generates the same element at any given index.
 * As long as the element's class implements sane equals ()
 * and hashCode () methods, calling
 * <code> generator.at ( x ).equals ( generator.at ( x ) ) </code>
 * will always result in true, and calling
 * <code> generator.at ( x ).hashCode () == generator.at ( x ).hashCode () </code>
 * will always result in true.
 * </p>
 *
 *
 * <p>
 * In Java every Generator must implement <code> equals ( ... ) </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Generator must be Serializable in order to
 * play nicely across RMI.  However users of the Generator
 * must be careful, since the elements and any other data or metadata
 * stored inside might not be Serializable.
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
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public interface Generator<ELEMENT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Retrieves the index'th element.
     * </p>
     *
     * @param index The index of the element to retrieve.
     *              Must be greater than or equal to 0L and less than
     *              <code> length () </code>.
     *
     * @return An array containing either the one element at
     *         the specified index, or no elements, if the specified
     *         index is less than 0L or greater than or equal to
     *         <code> length () </code>.  Never null.
     *         Can contain one null element, if this Generator
     *         generates null elements.
     */
    public abstract ELEMENT [] at (
            long index
            );


    // Every Generator must implement
    // java.lang.Object#equals(java.lang.Object)

    // Every Generator must implement
    // java.lang.Object#hashCode()


    /**
     * @return The number of elements that can be generated
     *         by this Generator.  Always greater than or equal to 0L.
     */
    public abstract long length ();


    // Every Generator must implement
    // java.lang.Object#toString()
}
