package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A basic implementation of Arc (and probably the only one ever needed,
 * though who can say for sure in these times of uncertainty and
 * bad music).
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
public class StandardArc<NODE extends Object, ARC extends Object>
    implements Arc<NODE, ARC>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( StandardArc.class );


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
     * Creates a new StandardArc.
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
    public StandardArc (
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
     * @see musaico.foundation.graph.Arc#arc()
     */
    @Override
    public final ARC arc ()
        throws ReturnNeverNull.Violation
    {
        return this.arc;
    }


    /**
     * @return This StandardArc's contracts checker, for use by
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

        final StandardArc<?, ?> that = (StandardArc<?, ?>) object;
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
     * @see musaico.foundation.graph.Arc#from()
     */
    @Override
    public final NODE from ()
        throws ReturnNeverNull.Violation
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
     * @see musaico.foundation.graph.Arc#isAcross(java.lang.Object)
     */
    @Override
    public final boolean isAcross (
            ARC arc
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
     * @see musaico.foundation.graph.Arc#isFrom(java.lang.Object)
     */
    @Override
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
     * @see musaico.foundation.graph.Arc#isTo(java.lang.Object)
     */
    @Override
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
     * @see musaico.foundation.graph.Arc#to()
     */
    @Override
    public final NODE to ()
        throws ReturnNeverNull.Violation
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
