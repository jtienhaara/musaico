package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
// Cannot be imported due to name conflict with ArcsMustBeTraversable.Violation:
// import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A guarantee that each pass leads to the successful traversal
 * of at least one arc out of a specific node in a specific graph.
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
public class ArcsMustBeTraversable<NODE extends Object, ARC extends Object, PASS extends Object, STATE extends Object>
    implements Contract<PASS, ArcsMustBeTraversable.Violation>, Serializable
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
            ArcsMustBeTraversable.serialVersionUID;

        /** Checks contracts on constructors and static methods for us. */
        private static final ObjectContracts classContracts =
            new ObjectContracts ( ArcsMustBeTraversable.Violation.class );


        /** The violated contract. */
        private final Contract<?, ArcsMustBeTraversable.Violation> contract;

        /** A Serializable representation of the object
         *  whose contract was violated. */
        private final Serializable plaintiff;

        /** A Serializable representation of the node. */
        private final Serializable inspectableData;


        /**
         * <p>
         * Creates a new ArcsMustBeTraversable.Violation with the specified
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
        public <VIOLATING_PASS extends Object>
            Violation (
                       Contract<VIOLATING_PASS, ArcsMustBeTraversable.Violation> contract,
                       Object plaintiff,
                       VIOLATING_PASS inspectable_data
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
                // Any ArcsMustBeTraversable.Violation != null.
                return false;
            }
            else if ( object == this )
            {
                // Every ArcsMustBeTraversable.Violation == itself.
                return true;
            }
            else if ( object.getClass () != this.getClass () )
            {
                // Any ArcsMustBeTraversable.Violation of class X
                //    != any ArcsMustBeTraversable.Violation of class Y.
                return false;
            }

            final ArcsMustBeTraversable.Violation that =
                (ArcsMustBeTraversable.Violation) object;

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
        new ObjectContracts ( ArcsMustBeTraversable.class );


    // The graph to which every node must be a member.
    private final Graph<NODE, ARC, PASS, STATE> graph;

    // The initial state from which there must be a valid arc to traverse.
    private final ZeroOrOne<STATE> initialState;


    /**
     * <p>
     * Creates a new ArcsMustBeTraversable contract, ensuring that every
     * node belongs to the specified graph.
     * </p>
     *
     * @param graph The Graph to which every node must belong.
     *              Must not be null.
     */
    public ArcsMustBeTraversable (
                                  Graph<NODE, ARC, PASS, STATE> graph,
                                  ZeroOrOne<STATE> initial_state
                                  )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               graph, initial_state );

        this.graph = graph;
        this.initialState = initial_state;
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

        final ArcsMustBeTraversable<?, ?, ?, ?> that =
            (ArcsMustBeTraversable<?, ?, ?, ?>) obj;
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

        if ( this.initialState == null )
        {
            if ( that.initialState == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.initialState == null )
        {
            return false;
        }
        else if ( ! this.initialState.equals ( that.initialState ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array creation PASS [].
    public FilterState filter (
                               PASS pass
                               )
    {
        if ( pass == null )
        {
            return FilterState.DISCARDED;
        }

        final Maybe<STATE> traversal =
            this.graph.traverse ( this.initialState, pass );
        if ( traversal.orNull () == null )
        {
            return FilterState.DISCARDED;
        }
        else
        {
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
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public ArcsMustBeTraversable.Violation violation (
                                                      Object plaintiff,
                                                      PASS inspectable_data
                                                      )
    {
        return new ArcsMustBeTraversable.Violation ( this,
                                                     plaintiff,
                                                     inspectable_data );
    }


    /**
     * <p>
     * Helper method.  Always passes this ArcsMustBeTraversable contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.graph.ArcsMustBeTraversable#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public ArcsMustBeTraversable.Violation violation (
                                                      Object plaintiff,
                                                      PASS inspectable_data,
                                                      Throwable cause
                                                      )
    {
        final ArcsMustBeTraversable.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
