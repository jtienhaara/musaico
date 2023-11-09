package musaico.foundation.operation.edit;


/**
 * <p>
 * The variety of Terms passed as parameters to an edit Pipe:
 * a sequence of elements, a set of any matching elements,
 * a selection of elements, elements that have passed through a Filter,
 * and so on.
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
 * @see musaico.foundation.operation.edit.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.edit.MODULE#LICENSE
 */
public enum EditTerm
{
    /** Zero or more element(s) passed through some Filter(s). */
    FILTER,

    /** A selection or range of zero or more element(s) by indices. */
    SELECTION,

    /** Zero or more element(s) in a row. */
    SEQUENCE,

    /** Zero or more element(s) with which to perform set operations. */
    SET;
}
