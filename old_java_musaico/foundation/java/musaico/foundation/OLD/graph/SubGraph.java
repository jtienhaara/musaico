package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An Arc which is also an entire Graph, beginning at the
 * entry node of the Graph, and ending at its exit node.
 * </p>
 *
 * <p>
 * A machine can make use of a SubGraph by pushing its graph onto
 * a stack of graphs, continuing from its entry point, and when the
 * exit node is reached, popping the graph and returning to the
 * SubGraph of the parent Graph.
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
public class SubGraph<NODE extends Object, ARC extends Object>
    extends StandardArc<NODE, ARC>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( SubGraph.class );


    // The Graph contained in this SubGraph.
    private final Graph<NODE, ARC> graph;


    /**
     * <p>
     * Creates a new SubGraph.
     * </p>
     *
     * @param graph The Graph contained in this SubGraph.
     *              Its entry node will be the "from" node of this
     *              Arc, and its exit node will be the "to" node.
     *              Can be a MutableGraph (changing over time),
     *              even if the parent is an ImmutableGraph.
     *              Must not be null.
     *
     * @param arc The arc data along this arc.  Must not be null.
     */
    public SubGraph (
                     Graph<NODE, ARC> graph,
                     ARC arc
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( graph == null // from
                    ? null
                    : graph.entry (),
                arc,
                graph == null // to
                    ? null
                    : graph.exit () );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        this.graph = graph;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }
        else if ( ! super.equals ( object ) )
        {
            return false;
        }

        // From, arc and to are all equal.  Now check the graph.

        final SubGraph<?, ?> that = (SubGraph<?, ?>) object;
        if ( this.graph == null )
        {
            if ( that.graph == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.graph == null )
        {
            return false;
        }
        else if ( ! this.graph.equals ( that.graph ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @return The Graph to which this SubGraph arc points.  Never null.
     */
    public final Graph<NODE, ARC> graph ()
    {
        return this.graph;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return
            17 * this.graph.hashCode ()
            + this.arc ().hashCode ();
    }


    /**
     * <p>
     * Returns true if this SubGraph leads to and from the specified Graph.
     * </p>
     *
     * @param graph The graph to compare to this SubGraph's child graph.
     *              Must not be null.
     *
     * @return True if this SubGraph leads to and from the specified Graph;
     *         false if it leads to a different Graph.
     */
    public final boolean isGraph (
            Graph<NODE, ARC> graph
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  graph );

        if ( this.graph.equals ( graph ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        final String sub_graph_string =
            ( "    " + this.graph )
            .replaceAll ( "\n", "\n    " )
            .replaceAll ( "[\\s]+$", "" );

        return "" + this.from () + " --[" + this.arc () + "]--\n"
            + "  {\n"
            + sub_graph_string
            + "\n  }--> " + this.to ();
    }
}
