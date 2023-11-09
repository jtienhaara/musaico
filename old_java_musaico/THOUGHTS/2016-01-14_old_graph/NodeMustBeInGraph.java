package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
// Cannot be imported due to name conflict with NodeMustBeInGraph.Violation:
// import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A guarantee that each node kept by the filter is in one specific Graph.
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
public class NodeMustBeInGraph<NODE extends Object>
    implements Contract<NODE, NodeMustBeInGraph.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static class Violation
        extends Exception
        implements musaico.foundation.contract.Violation, Serializable
    {
        private static final long serialVersionUID =
            NodeMustBeInGraph.serialVersionUID;

        /** Checks contracts on constructors and static methods for us. */
        private static final ObjectContracts classContracts =
            new ObjectContracts ( NodeMustBeInGraph.Violation.class );


        /** The violated contract. */
        private final Contract<?, NodeMustBeInGraph.Violation> contract;

        /** A Serializable representation of the object
         *  whose contract was violated. */
        private final Serializable plaintiff;

        /** A Serializable representation of the node. */
        private final Serializable inspectableData;


        /**
         * <p>
         * Creates a new NodeMustBeInGraph.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param inspectable_data The data which violated the contract.
         *                         Must not be null.
         */
        public <VIOLATING_NODE extends Object>
            Violation (
                       Contract<VIOLATING_NODE, NodeMustBeInGraph.Violation> contract,
                       Object plaintiff,
                       VIOLATING_NODE inspectable_data
                       )
        {
            super ( "" + contract + ".Violation" );

            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   contract, plaintiff, inspectable_data );

            this.contract = contract;
            this.plaintiff = "" + plaintiff;
            this.inspectableData =
                Contracts.makeSerializable ( inspectable_data );
        }

        /**
         * @see musaico.foundation.contract.Violation#contract()
         */
        @Override
        public Contract<?, ?> contract ()
        {
            return this.contract;
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (
                               Object object
                               )
        {
            if ( object == null )
            {
                // Any NodeMustBeInGraph.Violation != null.
                return false;
            }
            else if ( object == this )
            {
                // Every NodeMustBeInGraph.Violation == itself.
                return true;
            }
            else if ( object.getClass () != this.getClass () )
            {
                // Any NodeMustBeInGraph.Violation of class X
                //    != any NodeMustBeInGraph.Violation of class Y.
                return false;
            }

            final NodeMustBeInGraph.Violation that =
                (NodeMustBeInGraph.Violation) object;

            if ( this.contract == null )
            {
                if ( that.contract == null )
                {
                    // null contract == null contract.
                    return true;
                }
                else
                {
                    // null contract != any contract.
                    return false;
                }
            }
            else if ( that.contract == null )
            {
                // any contract != null contract.
                return false;
            }

            if ( ! this.contract.equals ( that.contract ) )
            {
                // This contract != that contract.
                return false;
            }

            if ( this.plaintiff == null )
            {
                if ( that.plaintiff == null )
                {
                    // null plaintiff == null plaintiff.
                    return true;
                }
                else
                {
                    // null plaintiff != any plaintiff.
                    return false;
                }
            }
            else if ( that.plaintiff == null )
            {
                // any plaintiff != null plaintiff.
                return false;
            }

            if ( ! this.plaintiff.equals ( that.plaintiff ) )
            {
                // This plaintiff != that plaintiff.
                return false;
            }

            if ( this.inspectableData == null )
            {
                if ( that.inspectableData == null )
                {
                    // null inspectableData == null inspectableData.
                    return true;
                }
                else
                {
                    // null inspectableData != any inspectableData.
                    return false;
                }
            }
            else if ( that.inspectableData == null )
            {
                // any inspectableData != null inspectableData.
                return false;
            }

            if ( ! this.inspectableData.equals ( that.inspectableData ) )
            {
                // This inspectableData != that inspectableData.
                return false;
            }

            // Return true since all the details are equal.
            return true;
        }


        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return this.contract.hashCode () * 31
                + ClassName.of ( this.getClass () ).hashCode ();
        }


        /**
         * @see musaico.foundation.contract.Violation#inspectableData()
         */
        @Override
        public Serializable inspectableData ()
        {
            return this.inspectableData;
        }


        /**
         * @see musaico.foundation.contract.Violation#plaintiff()
         */
        @Override
        public Serializable plaintiff ()
        {
            return this.plaintiff;
        }
    }




    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NodeMustBeInGraph.class );


    // The graph to which every node must be a member.
    private final Graph<NODE, ?, ?, ?> graph;


    /**
     * <p>
     * Creates a new NodeMustBeInGraph contract, ensuring that every
     * node belongs to the specified graph.
     * </p>
     *
     * @param graph The Graph to which every node must belong.
     *              Must not be null.
     */
    public NodeMustBeInGraph (
                              Graph<NODE, ?, ?, ?> graph
                              )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph );

        this.graph = graph;
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

        final NodeMustBeInGraph<?> that =
            (NodeMustBeInGraph<?>) obj;
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
                               NODE node
                               )
    {
        if ( node == null )
        {
            return FilterState.DISCARDED;
        }

        for ( NODE node_in_graph : this.graph.nodes () )
        {
            if ( node_in_graph.equals ( node ) )
            {
                return FilterState.KEPT;
            }
        }

        return FilterState.DISCARDED;
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
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public NodeMustBeInGraph.Violation violation (
                                                  Object plaintiff,
                                                  NODE inspectable_data
                                                  )
    {
        return new NodeMustBeInGraph.Violation ( this,
                                                 plaintiff,
                                                 inspectable_data );
    }


    /**
     * <p>
     * Helper method.  Always passes this NodeMustBeInGraph contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.graph.NodeMustBeInGraph#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public NodeMustBeInGraph.Violation violation (
                                                  Object plaintiff,
                                                  NODE inspectable_data,
                                                  Throwable cause
                                                  )
    {
        final NodeMustBeInGraph.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
