package musaico.foundation.operation;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;

import musaico.foundation.term.countable.No;


/**
 * <p>
 * A pipe from nothing.  Never outputs any Terms.
 * </p>
 *
 *
 * <p>
 * In Java, every Pipe must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Pipe must be Serializable in order to
 * play nicely across RMI.  However in general it is recommended
 * that only stateless Pipes be passed around over RMI, and even
 * then, do so with caution.  The Terms which feed inputs
 * to Pipes, although Serializable themselves,
 * can contain non-Serializable elements.
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
public class NoPipe<FROM extends Object, TO extends Object>
    implements Pipe<FROM, TO>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( NoPipe.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The Type of Term(s) to read from upstream.
    private final Type<FROM> upstreamType;

    // The Type of Term(s) to write to downstream.
    private final Type<TO> downstreamType;


    /**
     * <p>
     * Creates a new NoPipe.
     * </p>
     *
     * @param upstream_type The Type of Term(s) to read from upstream.
     *                      Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write to downstream. 
     *                        Must not be null.
     */
    public NoPipe (
            Type<FROM> upstream_type,
            Type<TO> downstream_type
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               upstream_type, downstream_type );

        this.upstreamType = upstream_type;
        this.downstreamType = downstream_type;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.operation.Pipe#downstreamReadType()
     */
    @Override
    public final Type<Term<TO>> downstreamReadType ()
        throws ReturnNeverNull.Violation
    {
        final Type<Term<TO>> downstream_read_type =
            this.downstreamType.enclose (
                Term.class,
                (Term<TO>) new No<TO> ( this.downstreamType ) )
            .buildType ();
        return downstream_read_type;
    }


    /**
     * @see musaico.foundation.operation.Pipe#downstreamType()
     */
    @Override
    public Type<TO> downstreamType ()
        throws ReturnNeverNull.Violation
    {
        return this.downstreamType;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final NoPipe<?, ?> that = (NoPipe<?, ?>) object;

        if ( this.upstreamType == null )
        {
            if ( that.upstreamType != null )
            {
                return false;
            }
        }
        else if ( that.upstreamType == null )
        {
            return false;
        }
        else if ( ! this.upstreamType.equals ( that.upstreamType ) )
        {
            return false;
        }

        if ( this.downstreamType == null )
        {
            if ( that.downstreamType != null )
            {
                return false;
            }
        }
        else if ( that.downstreamType == null )
        {
            return false;
        }
        else if ( ! this.downstreamType.equals ( that.downstreamType ) )
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
        return 37 * this.getClass ().getName ().hashCode ()
            + 23 * ( this.upstreamType == null
                         ? 0
                         : this.upstreamType.hashCode () )
            + ( this.downstreamType == null
                    ? 0
                    : this.downstreamType.hashCode () );
    }


    /**
     * @see musaico.foundation.operation.Pipe#inputPipes()
     */
    @Override
    @SuppressWarnings("unchecked") // Check FROM type = TO type then cast
        // Pipe<FROM, TO> to Pipe<FROM, FROM>.
    public final Countable<Pipe<?, FROM>> inputPipes ()
        throws ReturnNeverNull.Violation
    {
        final Pipe<FROM, FROM> no_pipe;
        if ( this.upstreamType.equals ( this.downstreamType ) )
        {
            no_pipe = (Pipe<FROM, FROM>) this; // Yes, circular!
        }
        else
        {
            no_pipe = new NoPipe<FROM, FROM> ( this.upstreamType,
                                               this.upstreamType );
        }

        final Type<Pipe<?, FROM>> input_pipes_type =
            (Type<Pipe<?, FROM>>) ( (Type<?>)
            this.upstreamType.enclose ( Pipe.class, // container_class
                                        no_pipe )   // none
            .buildType ()
                                    );
        final No<Pipe<?, FROM>> input_pipes =
            new No<Pipe<?, FROM>> ( input_pipes_type );
        return input_pipes;
    }


    /**
     * @see musaico.foundation.operation.Pipe#maximumOutputPipes()
     */
    @Override
    public final long maximumOutputPipes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.operation.Pipe#minimumOutputPipes()
     */
    @Override
    public final long minimumOutputPipes ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.operation.Pipe#step(musaico.foundation.operation.Context)
     */
    @Override
    public final boolean step (
            Context context
            )
        throws ParametersMustNotBeNull.Violation
    {
        return false;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();

        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( ": " + this.downstreamType );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.operation.Pipe#upstreamType()
     */
    @Override
    public final Type<FROM> upstreamType ()
        throws ReturnNeverNull.Violation
    {
        return this.upstreamType;
    }
}
