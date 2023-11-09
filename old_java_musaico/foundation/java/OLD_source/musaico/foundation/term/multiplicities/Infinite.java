package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.term.Definite;


/**
 * <p>
 * A value with infinite elements, such as the digits in Pi, or the number of
 * books in a supposed "trilogy", and so on.
 * </p>
 *
 * <p>
 * Unlike One value, an Infinite does not have one single element.
 * So operations which expect a single element can treat an Infinite
 * as a Failed value, which it partially is.  Or, by using special
 * operations specific to the Infinite implementation, the caller
 * can step through some of the Infinite values.
 * </p>
 *
 * The <code> iterator () </code> method does not step over any of
 * the elements of an Infinite value; it steps over a 0-length list,
 * instead, in order to prevent infinite loops.
 * </p>
 *
 * <p>
 * An Infinite cannot be converted to an array, so its toArray ()
 * method always returns the empty array.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.term.multiplicities.MODULE#COPYRIGHT
 * @see musaico.foundation.term.multiplicities.MODULE#LICENSE
 */
public interface Infinite<VALUE extends Object>
    extends Definite<VALUE>, OneToInfinity<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return An iterator which will happily step through the infinite
     *         elements in this Infinite value.
     *         Never null.  Never contains any null elements.
     *
     * @see musaico.foundation.term.Multiplicity#indefiniteIterator(long)
     */
    public abstract Iterator<VALUE> infiniteIterator ();
}
