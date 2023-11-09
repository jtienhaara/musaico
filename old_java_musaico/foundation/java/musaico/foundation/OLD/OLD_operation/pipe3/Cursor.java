package musaico.foundation.operation;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * !!!
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public class Cursor<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Cursor.class );


    // !!!
    private final Pipe<VALUE> pipe;

    // !!!
    private final Context context;

    // MUTABLE:
    // !!!
    private long index = 0L;


    /**
     * <p>
     * Creates a new Cursor.
     * </p>
     *
     * !!!
     */
    public Cursor (
            Pipe<VALUE> pipe,
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pipe, context );

        this.pipe = pipe;
        this.context = context;
    }


    /**
       !!!
    */
    public final Context context ()
    {
        return this.context;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final Cursor<?> that = (Cursor<?>) object;
        if ( this.pipe == null )
        {
            if ( that.pipe != null )
            {
                return false;
            }
        }
        else if ( that.pipe == null )
        {
            return false;
        }
        else if ( ! this.pipe.equals ( that.pipe ) )
        {
            return false;
        }

        if ( this.context == null )
        {
            if ( that.context != null )
            {
                return false;
            }
        }
        else if ( that.context == null )
        {
            return false;
        }
        else if ( ! this.context.equals ( that.context ) )
        {
            return false;
        }

        return true;
    }



    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.pipe.hashCode ()
            + 7 * this.getClass ().getName ().hashCode ()
            + this.context.hashCode ();
    }


    /**
       !!!
     */
    public final VALUE head ()
        throws ReturnNeverNull.Violation
    {
        final VALUE [] head = this.pipe.type ( this.context ).array ( 1 );
        final int num_elements_read =
            this.pipe.read ( head,                         // array
                             0,                            // offset
                             1,                            // length
                             this.index,                   // start
                             this.pipe.stream ( context ), // stream
                             this.context );               // context

        if ( num_elements_read < 0 )
        {
            return null;
        }
        else
        {
            return head [ 0 ];
        }
    }


    /**
       !!!
    */
    public final long index ()
    {
        return this.index;
    }


    /**
       !!!
    */
    public final void index (
            long index
            )
    {
        this.index = index;
    }


    /**
       !!!
    */
    public final boolean isEmpty ()
    {
        if ( this.head () == null )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
       !!!
    */
    public final Pipe<VALUE> pipe ()
    {
        return this.pipe;
    }


    /**
       !!!
    */
    public final void resetIndex ()
    {
        if ( this.index == Countable.NONE )
        {
            return;
        }
        else if ( this.index < 0L )
        {
            this.index = Countable.LAST;
        }
        else
        {
            this.index = 0L;
        }
    }


    /**
       !!!
    */
    public final long step ()
    {
        if ( this.index == Countable.NONE )
        {
            return this.index;
        }
        else if ( this.index < 0L )
        {
            this.index --;
        }
        else
        {
            this.index ++;
        }

        return this.index;
    }


    /**
       !!!
    */
    public final Cursor<VALUE> tail ()
    {
        this.step ();
        return this;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden by derived classes.
     */
    @Override
    public String toString ()
    {
        return "" + this.pipe + " given " + this.context;
    }
}
