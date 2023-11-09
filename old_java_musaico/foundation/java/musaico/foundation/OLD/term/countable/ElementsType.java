package musaico.foundation.term.countable;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Type;


/**
 * <p>
 * A Type which modifies the mutability / immutability of elements
 * for a value.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.term.countable.MODULE#COPYRIGHT
 * @see musaico.foundation.term.countable.MODULE#LICENSE
 */
public interface ElementsType<VALUE extends Object>
    extends Type<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Given input elements, returns an Elements
     *  object: either the input Elements, or a modified one.
     *  Only really useful by AbstractCountable's constructor.
     *  The Type-specific operation pipelines will provide
     *  functionality interesting to the end user (such as
     *  converting a Term with a mutable value to a Term
     *  with an immutable value, and vice-versa). */
    public abstract <ELEMENT extends Object>
        Elements<ELEMENT> buildElements (
            Elements<ELEMENT> elements,
            boolean is_fixed_length
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
