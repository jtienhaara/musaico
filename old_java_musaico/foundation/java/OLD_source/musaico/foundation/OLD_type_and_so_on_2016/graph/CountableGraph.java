package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.value.Countable;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A graph whose nodes and arcs are countable, and can be indexed
 * by integers (such as int n for a node, and int a for an arc).
 * </p>
 *
 *
 * <p>
 * In Java every Graph must be Serializable in order to
 * play nicely across RMI.  However users of the Graph
 * must be careful, since the nodes and arc inside might not
 * be Serializable -- leading to exceptions during serialization
 * of the parent Graph.
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
public interface CountableGraph<NODE extends Object, ARC extends Object>
    extends Graph<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @see musaico.foundation.graph.Graph#arcs()
     */
    @Override
    public abstract Countable<Arc<NODE, ARC>> arcs ()
        throws ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.graph.Graph#arcs(java.lang.Object)
     */
    @Override
    public abstract Countable<Arc<NODE, ARC>> arcs (
                                                    NODE node
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @see musaico.foundation.graph.Graph#nodes()
     */
    @Override
    public abstract Countable<NODE> nodes ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The number of arcs in this CountableGraph.  Always greater
     *         than or equal to 0L.
     */
    public abstract long numArcs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The number of nodes in this CountableGraph.  Always greater
     *         than or equal to 0L.
     */
    public abstract long numNodes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @return The number of SubGraphs in this CountableGraph.  Always greater
     *         than or equal to 0L.
     */
    public abstract long numSubGraphs ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation;


    /**
     * @see musaico.foundation.graph.Graph#subGraphs()
     */
    @Override
    public abstract Countable<SubGraph<NODE, ARC>> subGraphs ()
        throws ReturnNeverNull.Violation;
}
