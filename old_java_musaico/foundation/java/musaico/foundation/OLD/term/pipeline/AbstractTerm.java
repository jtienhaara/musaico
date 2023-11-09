package musaico.foundation.term.pipeline;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

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
import musaico.foundation.term.Multiplicity;
import musaico.foundation.term.Operation;
import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;
import musaico.foundation.term.Transform;
import musaico.foundation.term.Type;
import musaico.foundation.term.TypePipeline;


/**
 * <p>
 * The basis for most Multiplicities, providing boilerplate method
 * implementations.
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
public abstract class AbstractTerm<VALUE extends Object>
    implements Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( AbstractTerm.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The Type of this Term.
    private final Type<VALUE> type;


    /**
     * <p>
     * Creates a new AbstractTerm.
     * </p>
     *
     * @param type_pipeline The TypePipeline.TypeSink describing
     *                      this Term, such as a Type&lt;String&gt;.refine ()
     *                      for a Term of Strings, or a pipeline which
     *                      has already been weakened to allow
     *                      specific elements, such as
     *                      Type&lt;Number&gt;.refine().allowElements ( 1, 2 ),
     *                      and so on.  Must not be null.
     */
    public AbstractTerm (
            TypePipeline.TypeSink<VALUE> type_pipeline
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type_pipeline );

        // Make sure we have a Type of which we are a valid instance.
        this.type = type_pipeline
            .allowTerms ( this.getClass () )
            .buildType ();

        this.contracts = new Advocate ( this );
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
        final Transform<VALUE, VALUE> cast =
            this.type ().cast ( this );
        return cast;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#check()
     */
    @Override
    public final TermPipeline.TermSink<VALUE> check ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<VALUE> ( this )
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

        final OperationPipeline<?, ?> operation_pipeline;
        if ( that instanceof OperationPipeline )
        {
            final boolean is_contained_by_type =
                this.type.contains ( that );
            return is_contained_by_type;
        }
        else if ( that instanceof Term )
        {
            final boolean is_equal = this.equals ( that );
            return is_equal;
        }
        else
        {
            return false;
        }
    }


    /**
     * @return The Advocate which checks contracts for this AbstractTerm.
     *         Never null.
     */
    protected final Advocate contracts ()
    {
        return this.contracts;
    }


    // Every Multiplicity must implement
    // musaico.foundation.term.Multiplicity#countable()


    /**
     * @see musaico.foundation.term.Term#declares(musaico.foundation.term.Type[])
     */
    @Override
    public final boolean declares (
            Type<?>... types
            )
        throws ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) types );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               types );

        for ( Type<?> type : types )
        {
            if ( ! this.type.contains ( type ) )
            {
                return false;
            }
        }

        return true;
    }


    // Every TermPipeline.TermSink must implement
    // musaico.foundation.term.TermPipeline.TermSink#duplicate()


    /**
     * @see musaico.foundation.pipeline.Pipeline#edit()
     */
    @Override
    public final Edit<VALUE, TermPipeline.TermSink<VALUE>, TermPipeline.TermSink<VALUE>> edit ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<VALUE> ( this )
            .edit ();
    }


    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)


    // Every Multiplicity must implement
    // musaico.foundation.term.Multiplicity#filterState()


    /**
     * @see musaico.foundation.pipeline.Sink#fork(musaico.foundation.pipeline.Source[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final TermFork<VALUE, TermPipeline.TermSink<VALUE>> fork (
            OperationPipeline.OperationSink<VALUE> ... outputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<VALUE> ( this )
                       .fork ( outputs );
    }


    // Every Pipeline must implement java.lang.Object#hashCode()


    /**
     * @see musaico.foundation.term.Multiplicity#hasValue()
     */
    @Override
    public final boolean hasValue ()
    {
        return this.filterState ()
            .isKept ();
    }


    // Every Multiplicity must implement
    // musaico.foundation.term.Multiplicity#head()


    // Every Multiplicity must implement
    // musaico.foundation.term.Multiplicity#head(long)


    // Every Multiplicity must implement
    // musaico.foundation.term.Multiplicity#indefiniteIterator(long)


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
    public final Term<VALUE> input ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    // Every Multiplicity must implement
    // musaico.foundation.term.Multiplicity#iterator()


    /**
     * @see musaico.foundation.pipeline.Sink#join(musaico.foundation.pipeline.Sink[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final TermJoin<VALUE, TermPipeline.TermSink<VALUE>> join (
            TermPipeline.TermSink<VALUE> ... inputs
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new ToplevelTermPipeline<VALUE> ( this )
                       .join ( inputs );
    }


    /**
     * @see musaico.foundation.term.TermParent#operations()
     */
    @Override
    public final OperationPipeline.OperationSink<VALUE> operations ()
        throws ReturnNeverNull.Violation
    {
        return this.type ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final OrderBy<VALUE, TermPipeline.TermSink<VALUE>> orderBy (
            Order<VALUE> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<VALUE> ( this )
            .orderBy ( orders );
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orDefault(java.lang.Object)
     */
    @Override
    public final VALUE orDefault (
                                  VALUE default_value
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               default_value );

        final VALUE element = this.orNull ();
        if ( element == null )
        {
            return default_value;
        }
        else
        {
            return element;
        }
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orNone()
     */
    @Override
    public final VALUE orNone ()
        throws ReturnNeverNull.Violation
    {
        final VALUE element = this.orNull ();
        if ( element == null )
        {
            return this.type.none ();
        }
        else
        {
            return element;
        }
    }


    // Every AbstractMultiplicity must implement:
    // musaico.foundation.term.Multiplicity#orNull()


    /**
     * @see musaico.foundation.term.Multiplicity#orThrow(java.lang.Class)
     */
    @Override
    public final <EXCEPTION extends Exception>
        VALUE orThrow (
                       Class<EXCEPTION> exception_class
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception_class );

        final VALUE element = this.orNull ();
        if ( element != null )
        {
            return element;
        }

        Exception cause_exception = null;
        EXCEPTION exception = null;
        try
        {
            // First try single arg MyException ( String message ).
            final Constructor<EXCEPTION> constructor =
                exception_class.getConstructor ( String.class );
            exception =
                constructor.newInstance ( "" + this );
        }
        catch ( Exception e )
        {
            cause_exception = e;
            try
            {
                // Now try 0 args constructor.
                final Constructor<EXCEPTION> constructor =
                    exception_class.getConstructor ();
                exception = constructor.newInstance ();
            }
            catch ( Exception e2 )
            {
                exception = null;
            }
        }

        if ( exception == null )
        {
            final ReturnNeverNull.Violation violation =
                ReturnNeverNull.CONTRACT.violation ( this,
                                                     "Could not instantiate exception class " + exception_class.getName () );
            if ( cause_exception != null )
            {
                violation.initCause ( cause_exception );
            }

            throw violation;
        }

        throw exception;
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orThrow(java.lang.Exception)
     */
    @Override
    public final <EXCEPTION extends Exception>
        VALUE orThrow (
                       EXCEPTION exception
                       )
        throws EXCEPTION,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               exception );

        final VALUE element = this.orNull ();
        if ( element == null )
        {
            throw exception;
        }
        else
        {
            return element;
        }
    }


    // Every AbstractTerm must implement
    // musaico.foundation.term.Multiplicity#orThrowChecked()
    // Typically a violation is created by calling
    // musaico.foundation.term.contracts.ValueMustBeJust.CONTRACT.violation(..)

    // Every AbstractTerm must implement
    // musaico.foundation.term.Multiplicity#orThrowUnchecked()
    // Typically a violation is created by calling
    // musaico.foundation.term.contracts.ValueMustBeJust.CONTRACT.violation(..)


    /**
     * @see musaico.foundation.term.Multiplicity#orViolation(musaico.foundation.contract.Contract, java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast checked by try...catch.
    public final <TERM extends Multiplicity<?>, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<TERM, VIOLATION> contract,
                           Object plaintiff
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION
    {
        TERM evidence = null;
        try
        {
            evidence = (TERM) this;
        }
        catch ( ClassCastException e )
        {
            evidence = null;
        }

        return this.orViolation ( contract,
                                  plaintiff,
                                  evidence );
    }


    /**
     * @see musaico.foundation.term.Multiplicity#orViolation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    @Override
    public final <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
        VALUE orViolation (
                           Contract<EVIDENCE, VIOLATION> contract,
                           Object plaintiff,
                           EVIDENCE evidence
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               VIOLATION
    {
        // The evidence can be null.
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               contract, plaintiff );

        final VALUE element = this.orNull ();
        if ( element == null )
        {
            final VIOLATION violation =
                contract.violation ( plaintiff,
                                     evidence );
            throw violation;
        }
        else
        {
            return element;
        }
    }


    /**
     * @see musaico.foundation.pipeline.Sink#output()
     *
     * Can be overridden (for example to return a more specific Term class).
     */
    @Override
    public Term<VALUE> output ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#pipe(musaico.foundation.term.Operation)
     */
    @Override
    public final TermPipeline.TermSink<VALUE> pipe (
            Operation<VALUE, VALUE> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new ToplevelTermPipeline<VALUE> ( this )
            .pipe ( operation );
    }


    /**
     * @see musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.OperationPipeline)
     */
    @Override
    public final TermPipeline.TermSink<VALUE> pipe (
            OperationPipeline.OperationSink<VALUE> pipeline
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new ToplevelTermPipeline<VALUE> ( this, pipeline );
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#select()
     */
    @Override
    public final Select<VALUE, TermPipeline.TermSink<VALUE>> select ()
        throws ReturnNeverNull.Violation
    {
        return new ToplevelTermPipeline<VALUE> ( this )
            .select ();
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
                this.type.symbols ()
                         .cast ().toArray ();
        }

        // Throws ParametersMustNotBeNull.Violation,
        // Parameter1.MustContainNoNulls.Violation:
        final Operation<VALUE, SYMBOL> transform_operation =
            this.type.symbols ( symbols );
        final Transform<VALUE, SYMBOL> transform =
            new TermTransform<VALUE, SYMBOL> (
                this,
                transform_operation );

        return transform;
    }


    /**
     * @see musaico.foundation.pipeline.Parent#thisPipeline()
     */
    @Override
    public final Term<VALUE> thisPipeline ()
        throws ReturnNeverNull.Violation
    {
        return this;
    }


    // Every Pipeline must implement java.lang.Object#toString()


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
        // Throws ParametersMustNotBeNull.Violation:
        return new TermTransform<VALUE, OUTPUT> ( this, operation );
    }


    /**
     * @see musaico.foundation.term.Term#type()
     */
    @Override
    public final Type<VALUE> type ()
        throws ReturnNeverNull.Violation
    {
        return this.type;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)
     */
    @Override
    public final TermWhen<VALUE, TermPipeline.TermSink<VALUE>> when (
            Filter<?> condition
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new ToplevelTermPipeline<VALUE> ( this )
            .when ( condition );
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
        public final Where<VALUE, TermPipeline.TermSink<VALUE>> where (
            Filter<VALUE> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new ToplevelTermPipeline<VALUE> ( this )
            .where ( filters );
    }
}
