package musaico.foundation.term.pipeline;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
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
import musaico.foundation.pipeline.Fork;
import musaico.foundation.pipeline.Join;
import musaico.foundation.pipeline.OrderBy;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Select;
import musaico.foundation.pipeline.Source;
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
 * Standard Transform implementation, to map input Terms of one
 * Type/element class to output Terms of potentially another
 * Type and/or element class.
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
public class TermTransform<FROM extends Object, TO extends Object>
    implements Transform<FROM, TO>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermTransform.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The input Term or TermPipeline.
    private final TermPipeline.TermSink<FROM> input;

    // The Operation which will transform the input.
    private final Operation<FROM, TO> operation;


    /**
     * <p>
     * Creates a new TermTransform.
     * </p>
     *
     * @param input The Term or TermPipeline input into this pipeline.
     *              Must not be null.
     *
     * @param operation The Operation which takes terms of the FROM type
     *                  and transforms them into terms of the TO type.
     *                  Must not be null.
     */
    public TermTransform (
            TermPipeline.TermSink<FROM> input,
            Operation<FROM, TO> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input, operation );

        this.input = input;
        this.operation = operation;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#cast()
     */
    @Override
    public final Transform<TO, TO> cast ()
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final Type<TO> type = this.type ();
        final Operation<TO, TO> pass_through =
            type.to ( type );
        final Transform<TO, TO> cast =
            new TermTransform<TO, TO> ( this,
                                        pass_through );
        return cast;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#check()
     */
    @Override
    public final TermPipeline.TermSink<TO> check ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<TO> ( this )
            .check ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#contains(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final boolean contains (
            Pipeline<?, ?> that
            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that == this )
        {
            return true;
        }
        else if ( this.equals ( that ) )
        {
            return true;
        }

        final TermPipeline.TermSink<?> this_input =
            this.input ();

        final OperationPipeline<?, ?> operation_pipeline;
        if ( ( that instanceof OperationPipeline )
             || ( that instanceof TermPipeline ) )
        {
            if ( this_input == this )
            {
                return false;
            }

            operation_pipeline = (OperationPipeline<?, ?>) that;
            final boolean is_contained_by_input =
                this_input.contains ( that );
            return is_contained_by_input;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.pipeline.Sink#duplicate()
     */
    @Override
    public final Transform<FROM, TO> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new TermTransform<FROM, TO> (
            this.input,       // input
            this.operation ); // operation
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#edit()
     */
    @Override
    public final Edit<TO, TermPipeline.TermSink<TO>, TermPipeline.TermSink<TO>> edit ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<TO> ( this )
            .edit ();
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden (by TermWhen, and so on).
     */
    @Override
    public boolean equals (
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

        final TermTransform<?, ?> that =
            (TermTransform<?, ?>) object;

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

        // It doesn't matter if this pipeline has a parent (as in
        // a TermWhen or a TermFork or a TermJoin),
        // this sub-pipeline is equal to that one.

        return true;
    }


    /**
     * @see musaico.foundation.pipeline.Sink#fork(musaico.foundation.pipeline.Source[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final TermFork<TO, TermPipeline.TermSink<TO>> fork (
            OperationPipeline.OperationSink<TO> ... outputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<TO> ( this )
                       .fork ( outputs );
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can be overridden (by TermWhen, and so on).
     */
    @Override
    public int hashCode ()
    {
        if ( this.operation == null
             || this.input == null )
        {
            return 0;
        }

        return 37 * this.operation.hashCode ()
            + this.input.hashCode ();
    }


    /**
     * @see musaico.foundation.term.Term#indices()
     */
    @Override
    public final Transform<TO, Long> indices ()
        throws ReturnNeverNull.Violation
    {
        final Operation<TO, Long> extract_indices =
            this.type ().indices ();
        final Transform<TO, Long> transform =
            new TermTransform<TO, Long> ( this, extract_indices );
        return transform;
    }


    /**
     * @see musaico.foundation.term.Transform#inputOrigin ()
     */
    @Override
    public final TermPipeline.TermSink<FROM> inputOrigin ()
        throws ReturnNeverNull.Violation
    {
        return this.input;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#input()
     */
    @Override
    public final TermPipeline.TermSink<FROM> input ()
        throws ReturnNeverNull.Violation
    {
        return this.input;
    }


    /**
     * @see musaico.foundation.term.Transform#inputType ()
     */
    @Override
    public final Type<FROM> inputType ()
        throws ReturnNeverNull.Violation
    {
        return this.operation.inputType ();
    }


    /**
     * @see musaico.foundation.pipeline.Sink#join(musaico.foundation.pipeline.Sink[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final TermJoin<TO, TermPipeline.TermSink<TO>> join (
            TermPipeline.TermSink<TO> ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<TO> ( this )
                       .join ( inputs );
    }


    /**
     * @see musaico.foundation.term.Transform#operation ()
     */
    @Override
    public final Operation<FROM, TO> operation ()
        throws ReturnNeverNull.Violation
    {
        return this.operation;
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#operations()
     */
    @Override
    public final OperationPipeline.OperationSink<TO> operations ()
        throws ReturnNeverNull.Violation
    {
        return this.outputType ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final OrderBy<TO, TermPipeline.TermSink<TO>> orderBy (
            Order<TO> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<TO> ( this )
            .orderBy ( orders );
    }


    /**
     * @see musaico.foundation.pipeline.Sink#output()
     */
    @Override
    public final Term<TO> output ()
        throws ReturnNeverNull.Violation
    {
        final Term<FROM> input_term = this.input.output ();
        final Term<TO> built = this.operation.apply ( input_term );
        return built;
    }


    /**
     * @see musaico.foundation.term.Transform#outputType ()
     */
    @Override
    public final Type<TO> outputType ()
        throws ReturnNeverNull.Violation
    {
        return this.operation.outputType ();
    }


    /**
     * @see musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.Operation)
     */
    @Override
    public final TermPipeline.TermSink<TO> pipe (
            Operation<TO, TO> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<TO> ( this )
            .pipe ( operation );
    }


    /**
     * @see musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.OperationPipeline)
     */
    @Override
    public final TermPipeline.TermSink<TO> pipe (
            OperationPipeline.OperationSink<TO> pipeline
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final ToplevelTermPipeline<TO> new_pipeline =
            new ToplevelTermPipeline<TO> ( this, pipeline );
        return new_pipeline;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#select()
     */
    @Override
    public final Select<TO, TermPipeline.TermSink<TO>> select ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<TO> ( this )
            .select ();
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#symbols(musaico.foundation.term.Operation[])
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public final <SYMBOL extends Object>
        Transform<TO, SYMBOL> symbols (
            Operation<TO, SYMBOL> ... symbols
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) symbols );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               symbols );

        if ( symbols.length == 0 )
        {
            // No symbols were specified, so casting should be fine...right??
            symbols = (Operation<TO, SYMBOL> [])
                this.operation.outputType ().symbols ()
                                            .cast ().toArray ();
        }

        final Operation<TO, SYMBOL>to_symbols =
            this.operation.outputType ().symbols ( symbols );
        final TermTransform<TO, SYMBOL> new_symbols_pipeline =
            new TermTransform<TO, SYMBOL> ( this, to_symbols );
        return new_symbols_pipeline;
    }


    /**
     * @see musaico.foundation.term.Transform#term ()
     */
    @Override
    public final Term<TO> term ()
        throws ReturnNeverNull.Violation
    {
        final Term<TO> built = this.output ();
        return built;
    }


    /**
     * @see musaico.foundation.pipeline.Parent#thisPipeline()
     */
    @Override
    public final Transform<FROM, TO> thisPipeline ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.term.Transform#to (
            Type<NEW_TARGET> to_type
            )
     */
    @Override
    public final <NEW_TARGET extends Object>
        Transform<TO, NEW_TARGET> to (
            Type<NEW_TARGET> to_type
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Operation<TO, NEW_TARGET> cast_operation =
            this.type ().to ( to_type );
        final TermTransform<TO, NEW_TARGET> cast =
            new TermTransform<TO, NEW_TARGET> ( this, cast_operation );
        return cast;
    }


    /**
     * @see musaico.foundation.term.Transform#toArray ()
     */
    @Override
    public final TO [] toArray ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Term<TO> built = this.output ();
        final Countable<TO> countable = built.countable ();
        final long length = countable.length ();
        final int num_elements;
        if ( length < (long) Integer.MAX_VALUE )
        {
            num_elements = (int) length;
        }
        else
        {
            // Could be empty, or infinite, or an error, and so on.
            num_elements = 0;
        }

        final Type<TO> array_type = this.outputType ();
        final TO [] to_array = array_type.array ( num_elements );
        int a = 0;
        for ( TO element : countable )
        {
            if ( a >= num_elements )
            {
                // Should never happen.  Error.
                return array_type.array ( 0 );
            }

            to_array [ a ] = element;

            a ++;
        }

        if ( a < num_elements )
        {
            // Should never happen.  Error.
            return array_type.array ( 0 );
        }

        return to_array;
    }


    /**
     * @see musaico.foundation.term.Transform#toList ()
     */
    @Override
    public final List<TO> toList ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Term<TO> built = this.output ();
        final Countable<TO> countable = built.countable ();
        final long length = countable.length ();
        final int num_elements;
        if ( length < (long) Integer.MAX_VALUE )
        {
            num_elements = (int) length;
        }
        else
        {
            // Could be empty, or infinite, or an error, and so on.
            num_elements = 0;
        }

        final List<TO> to_list = new ArrayList<TO> ();
        int l = 0;
        for ( TO element : countable )
        {
            if ( l >= num_elements )
            {
                // Should never happen.  Error.
                to_list.clear ();
                return to_list;
            }

            to_list.add ( element );

            l ++;
        }

        if ( l < num_elements )
        {
            to_list.clear ();
            return to_list;
        }

        return to_list;
    }


    /**
     * @see musaico.foundation.term.Transform#toMap ()
     */
    @Override
    public final LinkedHashMap<FROM, TO> toMap ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Term<FROM> from_term = this.input.output ();
        final Countable<FROM> from_countable = from_term.countable ();

        final LinkedHashMap<FROM, TO> map = new LinkedHashMap<FROM, TO> ();
        final long from_length = from_countable.length ();
        if ( from_length <= 0L
             || from_length > Integer.MAX_VALUE )
        { 
            return map;
        }

        final Term<TO> to_term = this.output ();
        final Countable<TO> to_countable = to_term.countable ();
        if ( to_countable.length () != from_length )
        {
            return map;
        }

        final Iterator<FROM> from_it = from_term.iterator ();
        final Iterator<TO>   to_it   = to_term.iterator ();
        for ( long e = 0L; e < from_length
                  && from_it.hasNext ()
                  && to_it.hasNext (); e ++ )
        {
            final FROM from_element = from_it.next ();
            final TO to_element = to_it.next ();

            final TO existing = map.get ( from_element );
            if ( existing != null
                 && ! existing.equals ( to_element ) )
            {
                return map;
            }

            map.put ( from_element, to_element );
        }

        return map;
    }


    /**
     * @see musaico.foundation.term.Transform#toSet ()
     */
    @Override
    public final LinkedHashSet<TO> toSet ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Term<TO> built = this.output ();
        final Countable<TO> countable = built.countable ();
        final long length = countable.length ();
        final int num_elements;
        if ( length < (long) Integer.MAX_VALUE )
        {
            num_elements = (int) length;
        }
        else
        {
            // Could be empty, or infinite, or an error, and so on.
            num_elements = 0;
        }

        final LinkedHashSet<TO> to_set = new LinkedHashSet<TO> ();
        int s = 0;
        for ( TO element : countable )
        {
            if ( s >= num_elements )
            {
                // Should never happen.  Error.
                to_set.clear ();
                return to_set;
            }

            to_set.add ( element );

            s ++;
        }

        if ( s < num_elements )
        {
            to_set.clear ();
            return to_set;
        }

        return to_set;
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden (by TermWhen, and so on).
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.input + " )"
            + " { " + this.operation + " }";
    }


    /**
     * @see musaico.foundation.term.TermPipeline#transform(musaico.foundation.term.Operation)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast this - TermPipeline.TermSink<OUTPUT>
    public final <OUTPUT extends Object>
        Transform<TO, OUTPUT> transform (
            Operation<TO, OUTPUT> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final TermTransform<TO, OUTPUT> new_transform =
            new TermTransform<TO, OUTPUT> ( this, operation );
        return new_transform;
    }


    /**
     * @see musaico.foundation.term.TermPipeline#type()
     */
    @Override
    public final Type<TO> type ()
        throws ReturnNeverNull.Violation
    {
        return this.outputType ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)
     */
    @Override
    public final TermWhen<TO, TermPipeline.TermSink<TO>> when (
            Filter<?> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.CONTRACT:
        return new ToplevelTermPipeline<TO> ( this )
            .when ( condition );
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final Where<TO, TermPipeline.TermSink<TO>> where (
            Filter<TO> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<TO> ( this )
            .where ( filters );
    }
}
