!!! IncidenceMatrix;
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
public class IncidenceMatrix<NODE extends Object, ARC extends Object>
    implements CountableGraph<NODE, ARC>, ImmutableGraph<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns the arc # <code> arc_num </code> in the incidence matrix.
     * </p>
     *
     * @param arc_num The arc # to return.  Must be greater than or equal
     *                to 0.
     *
     * @return Either the One requested arc, or, if no such arc exists
     *         in this graph, No arc.  Never null.
     */
    public abstract ZeroOrOne<ARC> arc (
                                        int arc_num
                                        )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the incidence matrix of this graph.
     * </p>
     *
     * <p>
     * The incidence matrix contains <code> numNodes () </code> rows
     * and <code> numArcs () </code> columns.
     * </p>
     *
     * <p>
     * Each cell of the matrix <code> [ node_num ] [ arc_num ] </code>
     * is an integer value.  The following values are possible:
     * </p>
     *
     * <ul>
     *   <li> 0: The arc # <code> arc_num </code> is not connected
     *        in any way to node # <code> node_num </code>. </li>
     *   <li> 1: The arc # <code> arc_num </code> leads out of
     *        node # <code> node_num </code>. </li>
     *   <li> -1: The arc # <code> arc_num </code> leads to
     *        node # <code> node_num </code>. </li>
     *   <li> 2: The arc # <code> arc_num </code> connects
     *        node # <code> node_num </code> to itself </li>
     *   <li> Other values: specialized graphs may introduce other
     *        values for the incidence matrix.  An operation on the
     *        incidence matrix which does not know the meanings
     *        of these other values may assume that -2 indicates
     *        some variation of arc # <code> arc_num </code> connecting
     *        node # <code> node_num </code> to itself; all other
     *        positive values (3..Long.MAX_VALUE) may be treated
     *        as variations on arc # <code> arc_num </code> leading
     *        out of node # <code> node_num </code>; Long.MIN_VALUE
     *        may be treated as no connection from
     *        node # <code> node_num </code> to arc #
     *        <code> arc_num </code>; and all other
     *        negative values ((Long.MIN_VALUE+1)..-3) may be treated
     *        as variations on arc # <code> arc_num </code> leading
     *        to node # <code> node_num </code>. </li>
     * </ul>
     *
     * @return The incidence matrix for this CountableGraph.
     *         Never null.
     */
    public abstract long [] [] incidenceMatrix ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the node # <code> node_num </code> in the incidence matrix.
     * </p>
     *
     * @param node_num The node # to return.  Must be greater than or equal
     *                 to 0.
     *
     * @return Either the One requested node, or, if no such node exists
     *         in this graph, No node.  Never null.
     */
    public abstract ZeroOrOne<NODE> node (
                                          int node_num
                                          )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation;
}
