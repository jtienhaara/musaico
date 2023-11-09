package musaico.foundation.term.contracts;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;


/**
 * <p>
 * Stores an Operation and an input to that Operation, and
 * also the most recent output from applying the Operation to the input.
 * </p>
 *
 * <p>
 * Can be used, for example, as the evidence to a Contract which ensures
 * an Operation's outcome, given a specific input, is as expected.
 * </p>
 *
 *
 * <p>
 * In Java every Invocation must be Serializable in order to
 * play nicely across RMI.  However users of the Invocation
 * must be careful, since the Terms stored inside might not be
 * entirely Serializable, containing potentially non-Serializable
 * value elements.
 * </p>
 *
 * <p>
 * In Java every Invocation must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public class Invocation<INPUT extends Object, OUTPUT extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Invocation.class );


    // Lock critical sections on this token:
    private final Serializable lock = new String ( "" );

    // The Operation to invoke.
    private final Operation<INPUT, OUTPUT> operation;

    // The input to pass to the Operation.  Can be null.
    private final Term<INPUT> input;

    // MUTABLE:
    // The (most recent) output from the Operation, if this Invocation
    // has been executed at least once.
    private Term<OUTPUT> output = null;


    /**
     * <p>
     * Creates a new Invocation.
     * objects.
     * </p>
     *
     * @param operation The Operation to invoke.  Must not be null.
     *
     * @param input The input to pass to the Operation.  CAN be null.
     */
    public Invocation (
                       Operation<INPUT, OUTPUT> operation,
                       Term<INPUT> input
                       )
        throws Parameter1.MustNotBeNull.Violation
    {
        this ( operation,
               input,
               null ); // output
    }


    /**
     * <p>
     * Creates a new Invocation.
     * objects.
     * </p>
     *
     * @param operation The Operation to invoke.  Must not be null.
     *
     * @param input The input to pass to the Operation.  CAN be null.
     *
     * @param output The most recent output that was returned by the
     *               Operation, if any.  CAN be null.
     */
    public Invocation (
                       Operation<INPUT, OUTPUT> operation,
                       Term<INPUT> input,
                       Term<OUTPUT> output
                       )
        throws Parameter1.MustNotBeNull.Violation
    {
        classContracts.check ( Parameter1.MustNotBeNull.CONTRACT,
                               operation );

        this.operation = operation;
        this.input = input;
        this.output = output;
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

        final Invocation<?, ?> that = (Invocation<?, ?>) object;

        if ( this.operation == null )
        {
            if ( that.operation != null )
            {
                return false;
            }
        }
        else if ( that.operation == null )
        {
            return false;
        }
        else if ( ! this.operation.equals ( that.operation ) )
        {
            return false;
        }

        if ( this.input == null )
        {
            if ( that.input != null )
            {
                return false;
            }
        }
        else if ( that.input == null )
        {
            return false;
        }
        else if ( ! this.input.equals ( that.input ) )
        {
            return false;
        }

        // The output doesn't matter.

        return true;
    }


    /**
     * <p>
     * Applies the input to the Operation, and returns the output.
     * </p>
     *
     * @return The output from invoking the Operation with the input.
     *         Never null.
     */
    public final Term<OUTPUT> execute ()
        throws ReturnNeverNull.Violation
    {
        final Term<OUTPUT> output = this.operation.apply ( this.input );

        synchronized ( this.lock )
        {
            this.output = output;
        }

        return output;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return
            31 * ( this.input == null
                       ? Integer.MIN_VALUE
                       : this.input.hashCode () )
            + 17 * ( this.operation == null
                       ? Integer.MIN_VALUE
                       : this.operation.hashCode () );
    }


    /**
     * @return The input to pass to the Operation.
     *         WARNING: Can be null.
     */
    public final Term<INPUT> input ()
    {
        return this.input;
    }


    /**
     * @return The Operation to invoke.  Never null.
     */
    public final Operation<INPUT, OUTPUT> operation ()
        throws ReturnNeverNull.Violation
    {
        return this.operation;
    }


    /**
     * @return The output returned by the last execution
     *         of this Invocation, if any.
     *         WARNING: Can be null.
     */
    public final Term<OUTPUT> output ()
    {
        synchronized ( this.lock )
        {
            return this.output;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () ) + ": "
            + this.operation
            + " ( "
            + this.input
            + " )";
    }
}
