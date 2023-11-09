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
public class Arc<NODE extends Object, ARC extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Arc.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The source node, from which this arc starts.
    private final NODE from;

    // The arc data along this arc.
    private final ARC arc;

    // The target node, at which this arc ends.
    private final NODE to;


    /**
     * <p>
     * Creates a new Arc.
     * </p>
     *
     * @param from The source node, from which this arc starts.
     *             Must not be null.
     *
     * @param arc The arc data along this arc.  Must not be null.
     *
     * @param to The target node, at which this arc ends.
     *           Must not be null.
     */
    public Arc (
                NODE from,
                ARC arc,
                NODE to
                )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               from, arc, to );

        this.from = from;
        this.arc = arc;
        this.to = to;

        this.contracts = new Advocate ( this );
    }


    /**
     * @return The arc data across this arc.  Never null.
     */
    public final ARC arc ()
    {
        return this.arc;
    }


    /**
     * @return This Arc's contracts checker, for use by
     *         derived classes to ensure method parameter obligations
     *         and return value guarantees and so on are met.  Never null.
     */
    protected final Advocate contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
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

        final Arc<?, ?> that = (Arc<?, ?>) object;
        if ( this.from == null )
        {
            if ( that.from == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.from == null )
        {
            return false;
        }
        else if ( ! this.from.equals ( that.from ) )
        {
            return false;
        }

        if ( this.arc == null )
        {
            if ( that.arc == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.arc == null )
        {
            return false;
        }
        else if ( ! this.arc.equals ( that.arc ) )
        {
            return false;
        }

        if ( this.to == null )
        {
            if ( that.to == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.to == null )
        {
            return false;
        }
        else if ( ! this.to.equals ( that.to ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @return The node from which this arc begins.  Never null.
     */
    public final NODE from ()
    {
        return this.from;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return
            17 * this.from.hashCode ()
            + this.arc.hashCode ()
            + 31 * this.to.hashCode ();
    }


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
    public final boolean isAcross (
                                   Arc<NODE, ARC> arc
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               arc );

        if ( this.arc.equals ( arc ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


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
    public final boolean isFrom (
                                 NODE node
                                 )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        if ( this.from.equals ( node ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


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
    public final boolean isTo (
                               NODE node
                               )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               node );

        if ( this.to.equals ( node ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @return The node to which this arc leads.  Never null.
     */
    public final NODE to ()
    {
        return this.to;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "" + this.from + " --[" + this.arc + "]--> " + this.to;
    }
}
