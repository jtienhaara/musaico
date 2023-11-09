package musaico.foundation.term.multiplicities;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Term;


/**
 * <p>
 * A sequence of elements of unknown length, possibly finite,
 * possibly infinite, possibly even blocking.
 * </p>
 *
 * <p>
 * An Indefinite does not know whether it will have one single element.
 * So operations which expect a single element will fail.
 * The <code> iterator () </code> method also iterates over 0
 * Indefinite elements, in order to prevent infinite loops.
 * The <code> indefiniteIterator () </code> can be used for
 * operations which do expect multiple values to iterate over
 * some of the elements of the Indefinite (CAUTION: infinite loops!).
 * Or the first One element of the Indefinite value can be returned
 * by calling head ().
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
public interface Indefinite<VALUE extends Object>
    extends Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
