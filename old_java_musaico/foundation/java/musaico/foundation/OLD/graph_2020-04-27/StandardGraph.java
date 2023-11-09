package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * A straightforward implementation of Graph, nodes connected
 * by arcs.
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
public class StandardGraph<NODE extends Object, ARC extends Object>
    implements Graph<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks obligations on constructor static method parameters and so on.
    private static final Advocate classAdvocate =
        new Advocate ( StandardGraph.class );

    // Indices into the nodeArcs array:
    //     [ node index ] [ FROM / TO ] [ Nth arc from / to the node ]
    private static final int FROM = 0;
    private static final int TO = 1;


    // The Class of nodes in this graph.
    private final Class<NODE> nodeClass;

    // The nodes in this graph, by index #.
    private final NODE [] nodes;

    // The Class of arcs in this graph.
    private final Class<ARC> arcClass;

    // The arcs in this graph, in order, by index #.
    private final ARC [] arcs;

    public static class ArcDetails
    {
        public final int arcIndex;
        public final int fromNode;
        public final int toNode;
    }

    !!!!!!!!!!!!!!!! suck
    public static class NodeEntry
    {
        public final int nodeIndex;
        public final int [] fromArcs;
        public final int [] toArcs;
        public NodeEntry (
                int node_index,
                int [] from_arcs,
                int [] to_arcs
                )
        {
            this.nodeIndex = node_index;
            this.fromArcs = from_arcs;
            this.toArcs = to_arcs;
        }
    }

    private final NodeEntry [] nodeEntries;

    // [ from node index ] [ to node index ] [ from node arc # ] = arc index
    private final int [] [] [] arcsFrom;


    /**
     * <p>
     * Creates a new StandardGraph.
     * </p>
     *
     * @param node_class The class of nodes in this StandardGraph.
     *                   Must not be null.
     *
     * @param nodes The nodes in this StandardGraph.
     *              Must not be null.  Must not contain any null elements.
     *
     * @param arc_class The class of arcs in this StandardGraph.
     *                  Must not be null.
     *
     * @param arcs The arcs in this StandardGraph.
     *              Must not be null.  Must not contain any null elements.
     *
!!!!!!!!!!!! not going to work, too weird:
     * @param arcs_from Table of arcs by nodes:
     *                  [ from node index ] [ to node index ] [ from node arc # ] = arc index
     *                  For example, if node x has an arc a to node y
     *                  and 2 arcs, b and c, to node z, then the table might
     *                  look like:
     *                      <code> arcs_from [ x ] [ y ] [ 0 ] = a; </code>
     *                      <code> arcs_from [ x ] [ z ] [ 1 ] = b; </code>
     *                      <code> arcs_from [ x ] [ z ] [ 2 ] = c; </code>
     *                  The first column in the table is the "from" node index.
     *                  The second column in the table is the "to" node index.
     *                  The third column in the table is number of the
     *                  arc, in order, in the "from" node (0 = first arc
     *                  out of x, 1 = second arc out of x, 2 = third
     *                  arc out of x).  The fourth column, the value of
     *                  each array element, is the index into the arcs
     *                  array describing the arc.
     *                  The array may contain nulls such as:
     *                      <code> arcs_from [ x ] = null; </code>
     *                  The above would indicate that node x has no arcs
     *                  out of it.
     *                  However the array may NOT contain nulls such as:
     *                      <code> arcs_from [ x ] [ y ] = null; </code>
     *                      <code> arcs_from [ x ] [ y ] [ 0 ] = null; </code>
     *                  Must not be null.  See above for a description
     *                  of which elements must not be null.
     */
    public StandardGraph (
            Class<NODE> node_class,
            NODE [] nodes,
            Class<ARC> arc_class,
            ARC [] arcs,
            int [] [] [] arcs_from
            )
        throws EveryParameter.MustNotBeNull.Violation,
               EveryParameter.MustContainNoNulls.Violation
    {
        
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              node_class, nodes, arc_class, arcs,
                              incidenceMatrix );
        classAdvocate.check ( EveryParameter.MustContainNoNulls.CONTRACT,
                              nodes, arcs, incidence_matrix );

        this.nodeClass = node_class;
        this.nodes = (NODE [])
            Array.newInstance ( this.nodeClass, nodes.length );
        this.arcClass = arc_class;
        this.arcs = (ARC [])
            Array.newInstance ( this.arcClass, arcs.length );
        this.arcsFrom = mew int [ this.nodes.length ] [] [];

        for (int fn = 0; fn < this.nodes.length; fn ++ )
        {
            if ( fn >= arcs_from.length )
            {
                continue;
            }
            else if ( arcs_from [ fn ] == null
                      || arcs_from [ fn ].length == 0 )
            {
                continue;
            }

            this.arcsFrom [ fn ] =
                new int [ this.nodes.length ];

            for ( int tn = 0; tn < this.nodes.length; tn ++ )
            {
                if ( tn >= arcs_from [ fn ].length )
                {
                    continue;
                }
                else if ( arcs_from [ fn ] [ tn ] == null
                          || arcs_from [ fn ] [ tn ].length == 0 )
                {
                    continue;
                }

                
                if ( arcs_from == null
                     || arcs_from [ fn ] [ tn ] == null )
                {
                    // No arcs from node index fn to node index tn.
                    continue;
                }

                this.incidenceMatrix [ !!! have to order the arcs, they can't be ordered by "to" node.
    }


    /**
     * <p>
     * Returns all arcs to and from the nodes in this Graph.
     * </p>
     *
     * @return All arcs in this Graph.  Can be empty.
     *         Never null.  Never contains any null elements.
     */
    public abstract ARC [] arcs ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the arc(s) leading out of the specified node.
     * </p>
     *
     * @param node The node out of which all arc(s) will be returned.
     *             Must not be null.
     *
     * @return The arcs that leave the specified node, in the order
     *         of evaluation they are stepped through during a traversal
     *         of this Graph.  Can be 0 arcs.
     *         Never null.  Never contains any null elements.
     */
    public abstract ARC [] arcsFrom (
            NODE node
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the node(s) from which the specified arc leads.
     * Typically (but not necessarily) one source node per arc.
     * </p>
     *
     * @param arc The arc whose source node will be returned.
     *            Must not be null.
     *
     * @return The source node(s) for the specified arc.
     *         Can be empty or more than 1, although these
     *         are atypical cases.  Usually an arc has
     *         exactly 1 source node, and exactly 1 target node.
     *         Never null.  Never contains any null elements.
     */
    public abstract NODE [] arcSources (
            ARC arc
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the node(s) to which the specified arc leads.
     * Typically (but not necessarily) one target node per arc.
     * </p>
     *
     * @param arc The arc whose target node will be returned.
     *            Must not be null.
     *
     * @return The target node(s) for the specified arc.
     *         Can be empty or more than 1, although these
     *         are atypical cases.  Usually an arc has
     *         exactly 1 source node, and exactly 1 target node.
     *         Never null.  Never contains any null elements.
     */
    public abstract NODE [] arcTargets (
            ARC arc
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * @return The entry node, or source of this Graph; the starting point
     *         for traversing the arcs and nodes in this graph.
     *         Can be arbitrary, if this Graph is not oriented.
     *         Never null.
     */
    public abstract NODE entry ()
        throws Return.NeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#equals(java.lang.Object)
     */


    /**
     * @return The exit node, or sink, from this Graph; the terminal node.
     *         Can be arbitrary, if this Graph is not oriented.
     *         Never null.
     */
    public abstract NODE exit ()
        throws Return.NeverNull.Violation;


    /**
     * Every Graph must override:
     * @see java.lang.Object#hashCode()
     */


    /**
     * @return All of the nodes in this Graph.  Never null.
     *         Never contains any null elements.
     */
    public abstract NODE [] nodes ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * Every Graph must override toString () with a short description:
     * @see java.lang.toString()
     */


    /**
     * @return A detailed description of this Graph, in human-readable
     *         form.  Can contain newlines and so on.  Never null.
     */
    public abstract String toStringDetails ();
}
