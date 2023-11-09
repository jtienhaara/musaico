package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * A Type describes a class of objects, possibly with other modifier
 * Tags attached (such as an ArrayTag or a PrivateTag and so on).
 * </p>
 *
 * <p>
 * Type forms the under-the-hood basis of a typing environment,
 * providing type casters and other operations to both simplify
 * application development and enforce stringent rules.
 * </p>
 *
 * <p>
 * For example, an Integer[] array might have a Type which enforces
 * length 3.  Then any instance of that Type is always guaranteed to
 * have length 3, providing the possibility of compile-time type safety,
 * or at least early runtime detection of mis-matches between data.
 * An array of Integers of length 2 would not be compatible with such
 * a Type.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
 * @see musaico.foundation.type.MODULE#COPYRIGHT
 * @see musaico.foundation.type.MODULE#LICENSE
 */
public interface Type
    extends Namespace, Contract<Value<?>, ValueViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
