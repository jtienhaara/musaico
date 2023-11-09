package musaico.foundation.typing;

import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.NoneGuesser;
import musaico.foundation.value.Value;


/**
 * <p>
 * A toplevel Namespace for a set of Types and child Namespaces,
 * as well as Terms.
 * </p>
 *
 * <p>
 * A RootNamespace is a generic Namespace in which Types can be registered
 * by class, so that a Term can look up the Type corresponding to a given
 * class during a cast.  The registered Types can change over time, as
 * new Types are registered, and as new child Namespaces are added to
 * the hierarchy.  However once a Type is registered by class, it cannot
 * be un-registered or overwritten in that RootNamespace.
 * </p>
 *
 *
 * <p>
 * In Java every RootNamespace must be Serializable in order to
 * play nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public interface RootNamespace
    extends Namespace, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Adds a child Namespace to this RootNamespace.
     * </p>
     *
     * @param namespace The child namespace to add.  Could be a
     *                  Type, or a StandardNamespace, or even some other
     *                  Namespace which is useful to register, such as
     *                  perhaps a Tag.  Must not be null.
     *
     * @return This RootNamespace.  Never null.
     *
     * @throws SymbolMustBeUnique.Violation If a Namespace with the same id
     *                                      already exists in this table,
     *                                      or if the specified Namespace
     *                                      is a Type whose value class has
     *                                      already been registered in
     *                                      this RootNamespace to point to
     *                                      a Type.
     */
    public abstract RootNamespace add (
                                       Namespace namespace
                                       )
        throws ParametersMustNotBeNull.Violation,
               SymbolMustBeUnique.Violation;
}
