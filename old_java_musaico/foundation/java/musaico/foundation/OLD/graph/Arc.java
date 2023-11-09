package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * The Graph-internal representation of a directed edge in a Graph,
 * from one node to another across some Graph-specific arc data.
 * </p>
 *
 *
 * <p>
 * In Java every Arc must be Serializable in order to
 * play nicely across RMI.  However users of the Arc
 * must be careful, since the source or target nodes or the arc data
 * might not be Serializable -- leading to exceptions during
 * serialization of the parent Arc.
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
 * @see musaico.foundation.graph.MODULE#COPYRIGHT
 * @see musaico.foundation.graph.MODULE#LICENSE
 */
public interface Arc<NODE extends Object, ARC extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The arc data across this arc.  Never null.
     */
    public abstract ARC arc ()
        throws ReturnNeverNull.Violation;


    // Every Arc must implement java.lang.Object#equals(java.lang.Object)


    /**
     * @return The node from which this arc begins.  Never null.
     */
    public abstract NODE from ()
        throws ReturnNeverNull.Violation;


    // Every Arc must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Returns true if this Arc's label is equal to the speciied value.
     * </p>
     *
     * @param arc The value to compare to this Arc's label.
     *            Must not be null.
     *
     * @return True if this Arc's label is equal to the specified value;
     *         false if it is different.
     */
    public abstract boolean isAcross (
            ARC arc
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns true if this Arc leads from the specified node.
     * </p>
     *
     * @param node The node to compare to this Arc's from node.
     *             Must not be null.
     *
     * @return True if this Arc leads from the specified node;
     *         false if it leads from a different node.
     */
    public abstract boolean isFrom (
            NODE node
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns true if this Arc leads to the specified node.
     * </p>
     *
     * @param node The node to compare to this Arc's to node.
     *             Must not be null.
     *
     * @return True if this Arc leads to the specified node;
     *         false if it leads to a different node.
     */
    public abstract boolean isTo (
            NODE node
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @return The node to which this arc leads.  Never null.
     */
    public abstract NODE to ()
        throws ReturnNeverNull.Violation;


    // Every Arc must implement java.lang.toString()
}
