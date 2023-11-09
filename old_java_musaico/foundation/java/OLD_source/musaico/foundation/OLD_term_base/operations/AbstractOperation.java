package musaico.foundation.operations;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;


/**
 * <p>
 * Provides boilerplate functionality for implementations of
 * <code> Operation<INPUT, OUTPUT> </code>.
 * </p>
 *
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.operations.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.MODULE#LICENSE
 */
public abstract class AbstractOperation<INPUT extends Object, OUTPUT extends Object>
    implements Operation<INPUT, OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractOperation.class );


    // The Type of terms accepted as inputs to this Operation.
    private final Type<INPUT> inputType;

    // The Type of terms returned as outputs from this Operation.
    private final Type<OUTPUT> outputType;


    /**
     * <p>
     * Creates a new AbstractOperation.
     * </p>
     *
     * @param input_type The Type of terms accepted as inputs to
     *                   this Operations.  Must not be null.
     *
     * @param output_type The Type of terms returned as outputs from
     *                    this Operation.  Must not be null.
     */
    public AbstractOperation (
                              Type<INPUT> input_type,
                              Type<OUTPUT> output_type
                              )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input_type, output_type );

        this.inputType = input_type;
        this.outputType = output_type;
    }


    // Every AbstractOperation must implement:
    // musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
    //
    // If it is also an ElementalOperation, it must also implement:
    // musaico.foundation.term.ElementalOperation#apply(musaico.foundation.term.Term, long)
    //
    // If it is also a ProgressiveOperation, it must also implement:
    // musaico.foundation.term.ProgressiveOperation#progress(musaico.foundation.term.NonBlocking)


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden.
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
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final AbstractOperation<?, ?> that =
            (AbstractOperation<?, ?>) object;

        if ( this.inputType == null )
        {
            if ( that.outputType != null )
            {
                return false;
            }
        }
        else if ( that.outputType == null )
        {
            return false;
        }
        else if ( ! this.inputType.equals ( that.inputType ) )
        {
            return false;
        }

        if ( this.outputType == null )
        {
            if ( that.outputType != null )
            {
                return false;
            }
        }
        else if ( that.outputType == null )
        {
            return false;
        }
        else if ( ! this.outputType.equals ( that.outputType ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can be overridden.
     */
    @Override
    public int hashCode ()
    {
        return
            ( this.inputType == null
                  ? -1
                  : this.inputType.hashCode () )
            +  37 * ( this.outputType == null
                          ? 0
                          : this.outputType.hashCode () );
    }


    /**
     * @see musaico.foundation.term.Operation#inputType()
     */
    @Override
    public final Type<INPUT> inputType ()
        throws ReturnNeverNull.Violation
    {
        return this.inputType;
    }


    /**
     * @see musaico.foundation.term.Operation#outputType()
     */
    @Override
    public final Type<OUTPUT> outputType ()
        throws ReturnNeverNull.Violation
    {
        return this.outputType;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden.
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + "( "
            + this.inputType
            + " ) --> "
            + this.outputType;
    }
}
