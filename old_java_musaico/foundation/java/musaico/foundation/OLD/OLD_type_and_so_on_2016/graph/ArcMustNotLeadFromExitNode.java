package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A guarantee that no arc ever leads back from the exit node of a Graph.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
public class ArcMustNotLeadFromExitNode<NODE extends Object, ARC extends Object>
    implements Contract<Arc<NODE, ARC>, ArcMustNotLeadFromExitNode.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ArcMustNotLeadFromExitNode.serialVersionUID;

        /**
         * <p>
         * Creates a new ArcMustNotLeadFromExitNode.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param evidence The Arc which violated the contract.
         *                    Must not be null.
         */
        public <VIOLATING_NODE extends Object, VIOLATING_ARC extends Object>
            Violation (
                       Contract<Arc<VIOLATING_NODE, VIOLATING_ARC>, ArcMustNotLeadFromExitNode.Violation> contract,
                       Object plaintiff,
                       Arc<VIOLATING_NODE, VIOLATING_ARC> evidence
                       )
        {
            super ( contract,
                    "The arc " + evidence
                    + " leads from the exit node of the graph.", // description
                    plaintiff,
                    evidence );
        }
    }




    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( ArcMustNotLeadFromExitNode.class );


    // The graph to which every arc must be a member.
    private final Graph<NODE, ARC> graph;


    /**
     * <p>
     * Creates a new ArcMustNotLeadFromExitNode contract.
     * </p>
     *
     * @param graph The Graph from whose exit node no Arc may lead.
     *              Must not be null.
     */
    public ArcMustNotLeadFromExitNode (
                                      Graph<NODE, ARC> graph
                                      )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        this.graph = graph;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "No Arc may ever lead from the exit node "
            + this.graph.exit ()
            + " of graph "
            + this.graph
            + ".";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        final ArcMustNotLeadFromExitNode<?, ?> that =
            (ArcMustNotLeadFromExitNode<?, ?>) obj;
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
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Arc<NODE, ARC> arc
                               )
    {
        if ( arc == null )
        {
            return FilterState.DISCARDED;
        }

        final NODE exit = this.graph.exit ();
        if ( exit.equals ( arc.from () ) )
        {
            // Arc leads from the exit node.
            return FilterState.DISCARDED;
        }
        else
        {
            // Arc does not lead from the exit node.
            return FilterState.KEPT;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { " + this.graph + " }";
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public ArcMustNotLeadFromExitNode.Violation violation (
            Object plaintiff,
            Arc<NODE, ARC> evidence
            )
    {
        return new ArcMustNotLeadFromExitNode.Violation ( this,
                                                         plaintiff,
                                                         evidence );
    }


    /**
     * <p>
     * Helper method.  Always passes this ArcMustNotLeadFromExitNode contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.graph.ArcMustNotLeadFromExitNode#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public ArcMustNotLeadFromExitNode.Violation violation (
            Object plaintiff,
            Arc<NODE, ARC> evidence,
            Throwable cause
            )
    {
        final ArcMustNotLeadFromExitNode.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
