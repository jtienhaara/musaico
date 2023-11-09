package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Visits a Namespace.  Typically used together with a NamespaceWalker
 * to step through a typing hierarchy and perform some task at each
 * node in the tree.
 * </p>
 *
 *
 * <p>
 * In Java every NamespaceVisitor must be Serializable in order to
 * play nicely with RMI.
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
public interface NamespaceVisitor
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * The result of visiting a node in the namespace hierarchy.
     * Ether continue walking through the nodes, or stop at this
     * level but carry on in the upper nodes, or abort altogether.
     * </p>
     */
    public static enum VisitStatus
    {
        /** Continue walking through the namespace hierarchy. */
        CONTINUE,

        /** Stop walking through this level of the namespace
         *  hierarchy, but carry on at the next level up. */
        POP,

        /** Immediately stop walking the namespace hierarchy. */
        ABORT;
    }


    /**
     * <p>
     * Visits a single Namespace in the hierarchy.
     * </p>
     *
     * @param namespace The namespace to visit.  Must not be null.
     *
     * @return The visit status: CONTINUE, POP or ABORT.
     *
     * @see musaico.foundation.typing.NamespaceVisitor.VisitStatus
     */
    public abstract NamespaceVisitor.VisitStatus visit (
                                                        Namespace namespace
                                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
