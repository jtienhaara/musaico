package musaico.foundation.operation;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.Type;


/**
 * <p>
 * !!!
 * </p>
 *
 *
 * <p>
 * The first time <code> apply ( ... ) </code> is called,
 * the stateless Pipe creates a stateful instance of itself, and
 * delegates the <code> apply ( ... ) </code> call to that instance.
 * The instance then performs its induction step (see below).
 * This Pipe then returns the instance to the caller, who can choose to call
 * it again, if the caller wants the Pipe to write more Term(s)
 * to its stream(s).
 * </p>
 *
 * </p>
 * When <code> apply ( ... ) </code> is invoked on a stateful Pipe instance,
 * it performs one step of its inductive operation, and
 * <code> write ( ... ) </code>s 0 or 1 Term(s) to the provided Stream(s).
 * If it has more processing to do (that is, if it might be able to
 * write more Term(s) downstream), it returns One&lt;Pipe&gt;: itself.
 * If it has no further processing to do, it returns No&lt;Pipe&gt;.
 * </p> 
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.  However in general it is recommended
 * that only stateless Pipes be passed around over RMI, and even
 * then, do so with caution.  The Terms which feed parameters
 * and inputs to Pipes, although Serializable themselves,
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
public class Insert<VALUE extends Object>
    implements Pipe<VALUE, VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Insert.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // Synchronize critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    private final TypeSystem typeSystem;
    private final Type<VALUE> inputType;
    private final Type<VALUE> errorType;

    private final Term<Long> insertAt;
    private final Term<VALUE> insert;

    private final Insert<VALUE> noMoreInduction;

    // MUTABLE:
    // !!!
    private Pipe<VALUE, ?> output = null;

    // MUTABLE:
    // !!!  Modify once, then read-only.
    private Iterator<Long> insertIndex = null;

    // MUTABLE:
    // !!!  Modify once, then read-only.
    private long bufferedInputLength = -1L;

    // MUTABLE:
    // !!!
    private Term<VALUE> lastInput = null;

    // MUTABLE:
    // !!!
    private long inputIndex = -1L;


    /**
     * <p>
     * Creates a new Insert pipe.
     * </p>
     *
     * @param type_system The TypeSystem that will be used to create
     *                    or look up the input, output and error Type(s)
     *                    for this pipe.  Must not be null.
     *
     * @param output The Pipe which receives output from this operation.
     *               Must not be null.
     *
     * @param insert_at The index or indices at which to insert elements
     *                  into the incoming stream.  Must not be null.
     *                  @see musaico.foundation.term.Countable#at(long)
     *
     * @param inserts The element(s) to insert into the incoming stream.
     *                Must not be null.
     */
    public Insert (
            TypeSystem type_system,
            Pipe<VALUE, ?> output,
            Term<VALUE> insert_at,
            Term<VALUE> inserts
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               type_system, output,
                               insert_at, inserts );

        this.typeSystem = type_system;

        this.output = output;

        this.insertAt = insert_at;
        this.inserts = insert;

        this.noMoreInduction =
            new No<Insert<VALUE>> ( this.typeSystem.typeOf ( this ) );

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Instantiates this Insert pipe, copying the parameters and output
     * and so on from the specified template.
     * </p>
     *
     * @param template The template pipe to copy.  Must not be null.
     */
    public Insert (
            Insert<VALUE> template
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               template );

        this.typeSystem = template.typeSystem;
        this.output = template.output;

        this.insertAt = template.insertAt;
        this.inserts = template.inserts;

        this.noMoreInduction = template.noMoreInduction;

        this.insertIndex = this.insertAt.iterator ();
        this.bufferedInputLength = -1L;
        this.lastInput = null;
        this.inputIndex = 0L;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Pipe#apply(musaico.foundation.term.Term)
     */
    @Override
    public final Maybe<Insert<VALUE>> apply (
            Term<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            if ( this.insertIndex == null )
            {
                final Insert<VALUE> instance =
                    new Insert<VALUE> ( this );
                return instance.apply ( input );
            }
            else if ( this.output == null )
            {
                // Closed.
                return this.noMoreInduction;
            }
            else if ( ! this.insertIndex.hasNext () )
            {
                // Finished.
                this.output.close ();
                return this.noMoreInduction;
            }

            long insert_index = this.insertIndex.next ()
                                                .longValue ();
            if ( insert_index < 0L )
            {
                // Offset from the end of the input Term.
                if ( this.bufferedInputLength < 0L )
                {
                    // The insert index is offset from the end
                    // of an unknown number of input Term(s).
                    // We have no choice but to read in all of the input,
                    // then determine where to insert the elements.
                    
                    final Buffer<VALUE> input_buffer =
                        new Buffer<VALUE> ( this.typeSystem,
                                            remaining_input,
                                            this );
                    this.bufferedInputLength = 0L;
                    this.lastInput = null;
                    return input_buffer.apply ( input );
                }
                else if ( this.lastInput == null )
                {
                    // First buffered input.
                    if ( input instanceof Countable )
                    {
                    final Countable<VALUE> countable =
                        (Countable<VALUE>) input;

            if ( insert_index <= this.inputIndex )
            {
                // The next insertion comes before the rest of the input.
                this.output = this.output.apply ( this.inserts )
                                         .orNull ();
                if ( this.output == null )
                {
                    // The output Pipe doesn't want anything more
                    // from us.
                    return this.noMoreInduction;
                }
                else if ( ! ( this.inserts instanceof Countable ) )
                {
                    // Maybe we inserted infinite elements,
                    // or maybe we inserted something bizarre like an Error.
                    // Either way, we must not carry on.
                    this.output.close ();
                    return this.noMoreInduction;
                }
                else
                {
                    return this;
                }
            }

            // Some or all of the input comes before the next insertion.

    }


    /**
     * @see musaico.foundation.term.Pipe#close()
     */
    @Override
    public final void close ()
    {
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null );
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

        final Insert<?> that = (Insert<?>) object;
        if ( this.inputType == null )
        {
            if ( that.inputType != null )
            {
                return false;
            }
        }
        else if ( that.inputType == null )
        {
            return false;
        }
        else if ( ! this.inputType.equals ( that.inputType ) )
        {
            return false;
        }

        if ( this.insertAt == null )
        {
            if ( that.insertAt != null )
            {
                return false;
            }
        }
        else if ( that.insertAt == null )
        {
            return false;
        }
        else if ( ! this.insertAt.equals ( that.insertAt ) )
        {
            return false;
        }

        if ( this.insert == null )
        {
            if ( that.insert != null )
            {
                return false;
            }
        }
        else if ( that.insert == null )
        {
            return false;
        }
        else if ( ! this.insert.equals ( that.insert ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.term.Operation#errorType()
     */
    @Override
    public final Type<VALUE> errorType ()
    {
        return this.errorType;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 37 * this.getClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.foundation.term.Operation#inputType()
     */
    @Override
    public final Type<VALUE> inputType ()
    {
        return this.inputType;
    }


    /**
     * @see musaico.foundation.term.Pipe#output()
     */
    @Override
    public final Pipe<VALUE, ?> output ()
        throws ReturnNeverNull.Violation
    {
        return this.output;
    }


    /**
     * @see musaico.foundation.term.Operation#outputType()
     */
    @Override
    public final Type<VALUE> outputType ()
    {
        return this.inputType;
    }


    /**
     * @see musaico.foundation.term.Pipe#parameters()
     */
    @Override
    public final Countable<Term<?>> parameters ()
        throws ReturnNeverNull.Violation
    {
        return new Many<Term<?>> (
                       this.typeSystem.termType (),
                       this.insertAt,
                       this.insert );
    }


    /**
     * @see musaico.foundation.term.Pipe#sideEffects()
     */
    @Override
    public final Countable<Pipe<?, ?>> sideEffects ()
        throws ReturnNeverNull.Violation
    {
        return new No<Pipe<?, ?>> ( this.typeSystem.termType () );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        for ( Term<?> parameter : this.parameters () )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + parameter );
        }

        return ClassName.of ( this.getClass () )
            + " ( " + sbuf.toString () + " )";
    }
}
