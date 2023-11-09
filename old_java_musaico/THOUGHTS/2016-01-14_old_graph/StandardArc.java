package musaico.foundation.graph;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * An arc in a StandardGraph, comprising nothing more than a filter
 * function for determining whether a pass allows traversal of the
 * arc, and a destination state.
 * </p>
 *
 *
 * <p>
 * In Java every StandardArc must be Serializable in order to
 * play nicely across RMI.  However users of the StandardArc
 * must be careful, since the destination node might not
 * be Serializable -- leading to exceptions during serialization
 * of the parent StandardArc.
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
public class StandardArc<NODE extends Object, PASS extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardArc.class );

    // The filter which determines whether a pass
    // leads to successful traversal of this arc (kept),
    // or fails to traverse this arc (discarded).
    private final Filter<PASS> filter;

    // The node to which this arc leads.
    private final NODE destination;


    /**
     * <p>
     * Creates a new StandardArc.
     * </p>
     *
     * @param filter The filter which determines whether a pass
     *               leads to successful traversal of this arc (kept),
     *               or fails to traverse this arc (discarded).
     *               Must not be null.
     *
     * @param destination The node to which this arc leads.
     *                    Must not be null.
     */
    public StandardArc (
                        Filter<PASS> filter,
                        NODE destination
                        )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               filter, destination );

        this.filter = filter;
        this.destination = destination;
    }


    /**
     * @return The node to which this arc leads.  Never null.
     */
    public final NODE destination ()
    {
        return this.destination;
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
        if ( this.filter == null )
        {
            if ( that.filter == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.filter == null )
        {
            return false;
        }
        else if ( ! this.filter.equals ( that.filter ) )
        {
            return false;
        }

        if ( this.destination == null )
        {
            if ( that.destination == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.destination == null )
        {
            return false;
        }
        else if ( ! this.destination.equals ( that.destination ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @return The filter which determines whether a pass
     *         leads to successful traversal of this arc (kept),
     *         or fails to traverse this arc (discarded).
     *         Never null.
     */
    public final Filter<PASS> filter ()
    {
        return this.filter;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return
            this.filter.hashCode ()
            + 31 * this.destination.hashCode ();
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "--> " + this.destination;
    }
}
