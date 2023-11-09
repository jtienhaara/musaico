package musaico.foundation.operation;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter4;
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
public class Stream<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Stream.class );


     // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // !!!
    private final Pipe<VALUE> pipe;

    // !!!
    private final Context context;

    // MUTABLE:
    // !!!
    private long index = 0L;

    // MUTABLE contents:
    // !!!
    private final List<Countable<VALUE>> outputFinite;

    // MUTABLE contents:
    // !!!
    private final List<Countable<VALUE>> outputCycleOrNull;

    // MUTABLE contents:
    // !!!
    private final List<Term<VALUE>> outputOther;

    // !!!
    private final long finiteOffset;

    // !!!
    private final long length;

    // MUTABLE:
    private long numElementsWritten = 0;

    // MUTABLE:
    private boolean isComplete = false;


    /**
     * <p>
     * Creates a new Stream.
     * </p>
     *
     * !!!
     */
    public Stream (
            Pipe<VALUE> pipe,
            Context context,
            List<Countable<VALUE>> output_finite,
            List<Countable<VALUE>> output_cycle_or_null,
            List<Term<VALUE>> output_other,
            long finite_offset,
            long minimum_finite_elements
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pipe, context );

        this.pipe = pipe;
        this.context = context;

        this.outputFinite = output_finite;
        this.outputCycleOrNull = output_cycle_or_null;
        this.outputOther = output_other;
        this.finiteOffset = finite_offset;
        this.length = minimum_finite_elements;

        this.contracts = new Advocate ( this );
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

        final Stream<?> that = (Stream<?>) object;
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
        if ( this.index <= Countable.NONE )
        {
            return null;
        }

        final List<Countable<VALUE>> finite =
            new ArrayList<Countable<VALUE>> ();
        final List<Term<VALUE>> other =
            new ArrayList<Term<VALUE>> ();
        final long num_elements_read =
            this.pipe.read ( finite,         // output_finite
                             null,           // output_cycle
                             other,          // output_other
                             this.index,     // finite_offset
                             1L,             // minimum_finite_elements
                             this.context ); // context

        if ( num_elements_read <= 0L )
        {
            this.index = Countable.NONE;
            return null;
        }

        for ( Countable<VALUE> output : finite )
        {
            if ( output.hasValue () )
            {
                final Maybe<VALUE> head_countable = output.head ();
                final VALUE head_element = head_countable.orNull ();
                if ( head_element == null )
                {
                    this.index = Countable.NONE;
                }

                return head_element;
            }
        }

        // It shouldn't ever come to this, but the compiler thinks it could.
        return null;
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
        throws Parameter1.MustBeGreaterThanOrEqualToNegativeOne.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanOrEqualToNegativeOne.CONTRACT,
                               index );

        this.index = index;
    }


    /**
       !!!
    */
    public final boolean isAtEnd ()
    {
        if ( this.index <= Countable.NONE )
        {
            return true;
        }
        else if ( this.head () == null )
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
        this.index = 0L;
    }


    /**
       !!!
    */
    public final long step ()
    {
        if ( this.index <= Countable.NONE )
        {
            return this.index;
        }
        else
        {
            this.index ++;
            return this.index;
        }
    }


    /**
       !!!
    */
    public final Stream<VALUE> tail ()
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


    /**
     * <p>
     * Reads elements from this Stream's Pipe, starting at
     * this Stream's current index.
     * </p>
     *
     * @see musaico.foundation.operation.Pipe#read(java.util.List, java.util.List, java.util.List, long, long, musaico.foundation.operation.Context)
     *
     * @param output_finite The finite component of the output.
     *                      If the output is entirely finite,
     *                      then 0 or more Countable terms
     *                      will be concatenated together in this
     *                      output list.  If the output is Cyclical,
     *                      then the header component will be
     *                      recorded here as a concatenation of
     *                      0 or more Countable terms.  Otherwise,
     *                      no terms will be added to the finite
     *                      outputs and some other Term, such as
     *                      an Error output, will be added to
     *                      the output_other list.  Must not be null.
     *
     * @param output_cycle_or_null The optional infinite cycle component
     *                             of the output, or null.  If the output
     *                             is entirely finite, then no terms will
     *                             be added to this list.  But if the output
     *                             is Cyclical, then the cycle portion
     *                             will be added to this list as 0 or more
     *                             Countable terms.  Can be null.
     *
     * @param output_finite The "error or other output Term" stream.
     *                      If the result of reading this Stream's Pipe is
     *                      neither Countable nor Cyclical, then the
     *                      resulting Term will be added to the end
     *                      of the specified List.  For exampe,
     *                      an Error term would be output here,
     *                      or a Partial result, and so on.
     *                      Must not be null.
     *
     * @param minimum_finite_elements The minimum number of elements
     *                                to read into the finite output list.
     *                                Ignored if output_cycle_or_null
     *                                is not null (in which case the
     *                                entire output is always returned).
     *                                Must be greater than or equal to 0L.
     *
     * @return The number of elements read into the output_finite list,
     *         or -1L if there were no more finite elements to read.
     *         Always -1L or greater.
     */
    public final long read (
            List<Countable<VALUE>> output_finite,
            List<Countable<VALUE>> output_cycle_or_null,
            List<Term<VALUE>> output_other,
            long minimum_finite_elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter4.MustBeGreaterThanOrEqualToZero.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        this.contracts.check ( Parameter4.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               minimum_finite_elements );
        // Let the pipe check for nulls.

        if ( this.index <= Countable.NONE )
        {
            return -1L;
        }

        final long num_elements_read =
            this.pipe.read ( output_finite,
                             output_cycle_or_null,
                             output_other,
                             this.index,
                             minimum_finite_elements,
                             this.context );

        if ( num_elements_read < 0L )
        {
            this.index = Countable.NONE;
        }

        return num_elements_read;
    }


    public long pipe (
            Stream<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        if ( this.isComplete () )
        {
            return -1L;
        }

        final int num_finite_terms_before = this.outputFinite.size ();

        final long num_elements_written =
            input.read ( this.outputFinite,
                         this.outputCycleOrNull,
                         this.outputOther,
                         this.finiteOffset
                             + this.length
                             - this.numElementsWritten );

        return this.add ( num_elements_written,
                          num_finite_terms_before );
    }


    public long pipe (
            Stream<VALUE> input,
            long to_index
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        final long index = input.index ();
        if ( index == Countable.NONE
             || to_index == Countable.NONE )
        {
                // !!! error;
            return -1L;
        }

        final long max_length = to_index - index + 1L;
        if ( max_length >= ( this.finiteOffset
                             + this.length
                             - this.numElementsWritten ) )
        {
            return this.pipe ( input );
        }

        final int num_finite_terms_before = this.outputFinite.size ();

        final long num_elements_written =
            input.read ( this.outputFinite,
                         this.outputCycleOrNull,
                         this.outputOther,
                         max_length );

        return this.add ( num_elements_written,
                          num_finite_terms_before );
    }


    private long add (
            long num_elements_written,
            int num_finite_terms_before
            )
    {
        if ( this.outputCycleOrNull != null
             && this.outputCycleOrNull.size () > 0 )
        {
            for ( int c = this.outputCycleOrNull.size () - 1; c >= 0; c -- )
            {
                final Countable<VALUE> chunk =
                    this.outputCycleOrNull.get ( c );
                if ( chunk.length () <= 0L )
                {
                    this.outputCycleOrNull.remove ( c );
                }
            }

            if ( this.outputCycleOrNull.size () > 0 )
            {
                this.isComplete = true;
                return Long.MAX_VALUE;
            }
        }

        if ( num_elements_written <= 0L )
        {
            return num_elements_written;
        }

        final boolean was_before_offset;
        if ( this.numElementsWritten > 0L
             && this.numElementsWritten <= this.finiteOffset )
        {
            was_before_offset = true;
        }
        else
        {
            was_before_offset = false;
        }

        this.numElementsWritten += num_elements_written;

        if ( was_before_offset )
        {
            // We might need to remove and/or split terms.
            long index = this.numElementsWritten;
            for ( int f = this.outputFinite.size () - 1;
                  f > num_finite_terms_before;
                  f -- )
            {
                final Countable<VALUE> finite = this.outputFinite.get ( f );
                final long finite_length = finite.length ();
                if ( index <= this.finiteOffset )
                {
                    this.outputFinite.remove ( f );
                    index -= finite_length;
                }
                else
                {
                    index -= finite_length;
                    if ( index < this.finiteOffset )
                    {
                        final long break_point = this.finiteOffset - index;
                        final Countable<VALUE> keep =
                            finite.range ( break_point, Countable.LAST );
                        this.outputFinite.set ( f, keep );
                    }
                }
            }
        }

        return num_elements_written;
    }


    /**
     * <p>
     * Writes the specified Term to this Stream.
     * </p>
     *
     * @param term The Term to put into this Stream for reading
     *             at the other end.  Must not be null.
     *
     * @return The number of elements added to this stream.
     *         Always 0L or greater.
     */
    public final long write (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               term );

        final int num_finite_terms_before = this.outputFinite.size ();

        final TermPipe<VALUE> term_pipe = new TermPipe<VALUE> ( term );

        final long num_elements_written =
            term_pipe.read ( this.outputFinite,      // output_finite
                             this.outputCycleOrNull, // output_cycle_or_null
                             this.outputOther,       // output_other
                             0L,                     // finite_offset
                             this.finiteOffset       // minimum_num_elements
                                 + this.length
                                 - this.numElementsWritten,
                             this.context );         // context

        return this.add ( num_elements_written,
                          num_finite_terms_before );
    }


    public final long numElementsWritten ()
    {
        return this.numElementsWritten - this.finiteOffset;
    }


    public final long length ()
    {
        return this.length;
    }


    public final List<Countable<VALUE>> outputFinite ()
    {
        return this.outputFinite;
    }


    public final List<Countable<VALUE>> outputCycleOrNull ()
    {
        return this.outputCycleOrNull;
    }


    public final List<Term<VALUE>> outputOther ()
    {
        return this.outputOther;
    }


    public final boolean isComplete ()
    {
        if ( this.isComplete )
        {
                return true;
        }
        else if ( this.outputCycleOrNull != null
                  && this.outputCycleOrNull.size () > 0 )
        {
            this.isComplete = true;
            return true;
        }
        else if ( this.numElementsWritten
                  >= ( this.finiteOffset + this.length ) )
        {
            this.isComplete = true;
            return true;
        }
        else
        {
            return false;
        }
    }
}

/* !!!
            long finite_offset,
            long minimum_finite_elements,
            Context context


        // output_cycle_or_null can be null.
        this.contracts.check ( Parameter4.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               finite_offset );
        this.contracts.check ( Parameter5.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               minimum_finite_elements );



     * @param output_finite The finite component of the output.
     *                      If the output is entirely finite,
     *                      then 0 or more Countable terms
     *                      will be concatenated together in this
     *                      output list.  If the output is Cyclical,
     *                      then the header component will be
     *                      recorded here as a concatenation of
     *                      0 or more Countable terms.  Otherwise,
     *                      no terms will be added to the finite
     *                      outputs and some other Term, such as
     *                      an Error output, will be added to
     *                      the output_other list.  Must not be null.
     *
     * @param output_cycle_or_null The optional infinite cycle component
     *                             of the output, or null.  If the output
     *                             is entirely finite, then no terms will
     *                             be added to this list.  But if the output
     *                             is Cyclical, then the cycle portion
     *                             will be added to this list as 0 or more
     *                             Countable terms.  Can be null.
     *
     * @param output_other The "error or other output Term" stream.
     *                     If the result of reading this Pipe is
     *                     neither Countable nor Cyclical, then the
     *                     resulting Term will be added to the end
     *                     of the specified List.  For exampe,
     *                     an Error term would be output here,
     *                     or a Partial result, and so on.
     *                     Must not be null.
     !!! */
