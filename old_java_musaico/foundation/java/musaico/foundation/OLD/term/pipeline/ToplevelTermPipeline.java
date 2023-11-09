package musaico.foundation.term.pipeline;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.OrderBy;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Select;
import musaico.foundation.pipeline.When;
import musaico.foundation.pipeline.Where;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Operation;
import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;
import musaico.foundation.term.Transform;
import musaico.foundation.term.Type;


/**
 * <p>
 * A simple TermPipeline which depends entirely on the Type(s)
 * of the input Term to add Operations to the pipeline.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Pipeline must implement equals (), hashCode ()
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
 * @see musaico.foundation.term.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.term.pipeline.MODULE#LICENSE
 */
public class ToplevelTermPipeline<VALUE extends Object>
    extends AbstractTermPipeline<VALUE, TermPipeline.TermSink<VALUE>, OperationPipeline.OperationSink<VALUE>>
    implements TermPipeline.TermSink<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( ToplevelTermPipeline.class );


    // The input Term or TermPipeline.
    private final TermPipeline.TermSink<VALUE> input;


    /**
     * <p>
     * Creates a new ToplevelTermPipeline.
     * </p>
     *
     * @param input The Term or TermPipeline input into this pipeline.
     *              Must not be null.
     */
    public ToplevelTermPipeline (
            TermPipeline.TermSink<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        this ( input,        // input
               input == null // slave
                   ? null
                   : input.type () );
    }


    /**
     * <p>
     * Creates a new ToplevelTermPipeline.
     * </p>
     *
     * @param input The Term or TermPipeline input into this pipeline.
     *              Must not be null.
     *
     * @param slave The OperationPipeline which will do all the work.
     *              Must not be null.
     */
    public ToplevelTermPipeline (
            TermPipeline.TermSink<VALUE> input,
            OperationPipeline.OperationSink<VALUE> slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        // Throws ParametersMustNotBeNull.Violation,
        // ClassCastException:
        super ( slave );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );

        this.input = input;
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#cast()
     */
    @Override
    public final Transform<VALUE, VALUE> cast ()
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final Type<VALUE> type = this.type ();
        final Operation<VALUE, VALUE> pass_through =
            type.to ( type );
        final Transform<VALUE, VALUE> cast =
            new TermTransform<VALUE, VALUE> ( this,
                                              pass_through );
        return cast;
    }


    /**
     * @see musaico.foundation.pipeline.Sink#duplicate()
     */
    @Override
    public final TermPipeline.TermSink<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<VALUE> (
            this.input,                   // input
            this.slave ().duplicate () ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Sink#fork(musaico.foundation.pipeline.Source[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution,
        // Jumping through absurd hoops to make generics realize
        // that ToplevelTermPipeline IS a TermPipeline.TermSink.
    public final TermFork<VALUE, TermPipeline.TermSink<VALUE>> fork (
            OperationPipeline.OperationSink<VALUE> ... outputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        final TermFork<VALUE, TermPipeline.TermSink<VALUE>> fork =
            (TermFork<VALUE, TermPipeline.TermSink<VALUE>>)
            (
             (TermFork<VALUE, ?>)
             new TermFork<VALUE, TermPipeline.TermSink<VALUE>> (
                     this.thisPipeline (),           // parent
                     this.slave ().fork ( outputs ), // slave
                     null )                          // previous
             );

        return fork;
    }


    /**
     * @see musaico.foundation.term.Term#indices()
     */
    @Override
    public final Transform<VALUE, Long> indices ()
        throws ReturnNeverNull.Violation
    {
        final Operation<VALUE, Long> extract_indices =
            this.type ().indices ();
        final Transform<VALUE, Long> transform =
            new TermTransform<VALUE, Long> ( this, extract_indices );
        return transform;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#input()
     */
    @Override
    public final TermPipeline.TermSink<VALUE> input ()
        throws ReturnNeverNull.Violation
    {
        return this.input;
    }


    /**
     * @see musaico.foundation.pipeline.Sink#join(musaico.foundation.pipeline.Sink[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution,
        // Jumping through absurd hoops to make generics realize
        // that ToplevelTermPipeline IS a TermPipeline.TermSink.
    public final TermJoin<VALUE, TermPipeline.TermSink<VALUE>> join (
            TermPipeline.TermSink<VALUE> ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        final TermJoin<VALUE, TermPipeline.TermSink<VALUE>> join =
            (TermJoin<VALUE, TermPipeline.TermSink<VALUE>>)
            (
             (TermJoin<VALUE, ?>)
             new TermJoin<VALUE, TermPipeline.TermSink<VALUE>> (
                     this.thisPipeline (),          // parent
                     this.slave ().join ( inputs ), // slave
                     null )                         // previous
             );

        return join;
    }


    /**
     * @see musaico.foundation.pipeline.Sink#output()
     */
    @Override
    public final Term<VALUE> output ()
        throws ReturnNeverNull.Violation
    {
        final Term<VALUE> output_term;
        final Operation<VALUE, VALUE> operation =
            this.slave ().output ();
        final Term<VALUE> input_term = this.input.output ();
        output_term = operation.apply ( input_term );

        return output_term;
    }


    /**
     * @see musaico.foundation.term.pipeline.AbstractTermPipeline#slave()
     */
    @Override
    protected final OperationPipeline.OperationSink<VALUE> slave ()
    {
        return super.slave ();
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#symbols(musaico.foundation.term.Operation[])
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public final <SYMBOL extends Object>
        Transform<VALUE, SYMBOL> symbols (
            Operation<VALUE, SYMBOL> ... symbols
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        if ( symbols != null
             && symbols.length == 0 )
        {
            // No symbols were specified, so casting should be fine...right??
            symbols = (Operation<VALUE, SYMBOL> [])
                this.type ().symbols ()
                            .cast ().toArray ();
        }

        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        final Operation<VALUE, SYMBOL> transform_operation =
            this.slave ().symbols ( symbols );
        final Transform<VALUE, SYMBOL> transform =
            new TermTransform<VALUE, SYMBOL> (
                this,
                transform_operation );

        return transform;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return
            "{ " + this.slave () + " }"
            + " ( " + this.input + " )";
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#transform(musaico.foundation.term.Operation)
     */
    @Override
    public final <OUTPUT extends Object>
        Transform<VALUE, OUTPUT> transform (
            Operation<VALUE, OUTPUT> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new TermTransform<VALUE, OUTPUT> ( this, operation );
    }
}
