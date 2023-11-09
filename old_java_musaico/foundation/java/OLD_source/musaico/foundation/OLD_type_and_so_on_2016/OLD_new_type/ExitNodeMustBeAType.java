package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.graph.Graph;

import musaico.foundation.value.Value;


/**
 * <p>
 * A guarantee that each graph's exit node is a Type.
 * </p>
 *
 * <p>
 * This is useful for composite symbols which must end in Types,
 * such as an Operation, which must end in an output Type.
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
 * @see musaico.foundation.type.MODULE#COPYRIGHT
 * @see musaico.foundation.type.MODULE#LICENSE
 */
public class ExitNodeMustBeAType
    implements Contract<Graph<?, ?>, ExitNodeMustBeAType.Violation>, Serializable
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
            ExitNodeMustBeAType.serialVersionUID;


        /**
         * <p>
         * Creates a new ExitNodeMustBeAType.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param inspectable The graph which violated the contract.
         *                    Must not be null.
         */
        public Violation (
                          Contract<Graph<?, ?>, ExitNodeMustBeAType.Violation> contract,
                          Object plaintiff,
                          Graph<?, ?> inspectable
                          )
        {
            this ( contract,
                   plaintiff,
                   inspectable,
                   null ); // cause
        }

        /**
         * <p>
         * Creates a new ExitNodeMustBeAType.Violation with the specified
         * details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param inspectable The graph which violated the contract.
         *                    Must not be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        public Violation (
                          Contract<Graph<?, ?>, ExitNodeMustBeAType.Violation> contract,
                          Object plaintiff,
                          Graph<?, ?> inspectable,
                          Throwable cause
                          )
        {
            super ( contract,
                    "The exit node ("
                    + ( inspectable == null
                        ? null
                        : inspectable.exit () )
                    + ") is not a Type"
                    + " for graph "
                    + inspectable, // description
                    plaintiff,
                    inspectable,
                    cause );
        }
    }




    /** The exit-node-must-be-a-type obligation singleton. */
    public static final ExitNodeMustBeAType CONTRACT =
        new ExitNodeMustBeAType ();


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( ExitNodeMustBeAType.class );


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use ExitNodeMustBeAType.CONTRACT instead.
     * </p>
     */
    protected ExitNodeMustBeAType ()
    {
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each graph's exit node must be a Type, not just any value.";
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

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Graph<?, ?> graph
                               )
    {
        if ( graph == null )
        {
            return FilterState.DISCARDED;
        }

        final Value<?> exit_node = graph.exit ();
        if ( exit_node == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( exit_node.orNull () instanceof Type )
        {
            return FilterState.KEPT;
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
    public ExitNodeMustBeAType.Violation violation (
                                                    Object plaintiff,
                                                    Graph<?, ?> inspectable_data
                                                    )
    {
        return new ExitNodeMustBeAType.Violation ( this,
                                                   plaintiff,
                                                   inspectable_data );
    }


    /**
     * <p>
     * Helper method.  Always passes this ExitNodeMustBeAType contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.type.ExitNodeMustBeAType#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public ExitNodeMustBeAType.Violation violation (
                                                    Object plaintiff,
                                                    Graph<?, ?> inspectable_data,
                                                    Throwable cause
                                                    )
    {
        final ExitNodeMustBeAType.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
