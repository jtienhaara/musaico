package musaico.foundation.operation.edit;

import java.io.Serializable;

import java.util.Iterator;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.BloomFilter;
import musaico.foundation.domains.ClassName;

import musaico.foundation.operation.AbstractPipe;
import musaico.foundation.operation.Buffer;
import musaico.foundation.operation.Pipe;
import musaico.foundation.operation.Stream;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Finite;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.contracts.TermMustMeetAllContracts;
import musaico.foundation.term.contracts.TermMustMeetAtLeastOneContract;
import musaico.foundation.term.contracts.ValueMustBeCountable;
import musaico.foundation.term.contracts.ValueMustBeOneOrMore;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.infinite.Cyclical;
import musaico.foundation.term.infinite.CyclicalCycleMustMeet;
import musaico.foundation.term.infinite.CyclicalHeaderMustMeet;
import musaico.foundation.term.infinite.TermMustBeCyclical;

import musaico.foundation.term.multiplicities.Infinite;


/**
 * <p>
 * A Pipe which outputs the result of a set operation (such as
 * difference, intersection, union) of the main input stream
 * and another stream.
 * </p>
 *
 * <p>
 * Each stream must be read in its entirety before anything can be output.
 * </p>
 *
 *
 * <p>
 * The first time <code> apply ( ... ) </code> is called,
 * the stateless Pipe creates a stateful instance of itself, and
 * delegates the <code> apply ( ... ) </code> call to that instance.
 * The instance then performs its iterative or recursive step (see below).
 * This Pipe then returns the instance to the caller, who can choose to call
 * it again, if the caller wants the Pipe to write more Term(s)
 * to its stream(s).
 * </p>
 *
 * </p>
 * When <code> apply ( ... ) </code> is invoked on a stateful Pipe instance,
 * it performs one step of its iterative or recursive operation, and
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
 * @see musaico.foundation.operation.edit.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.edit.MODULE#LICENSE
 */
public abstract class AbstractSetOperation<VALUE extends Object>
    extends AbstractPipe<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( AbstractSetOperation.class );

    // The default number of elements to gather up before flushing
    // a Term of output downstream.
    private static final long DEFAULT_BUILDER_SIZE = 64L;


    // The 2nd Pipe of Term(s), other than the input stream, to treat as a set.
    private final Pipe<?, VALUE> other;

    // The Buffer of Term(s) on which to perform the set operation,
    // along with the main input stream, if this is a AbstractSetOperation
    // instance; or null, if this is a template.
    private final Buffer<VALUE> otherBuffer;

    // After generating how many set elements of output should we flush
    // the TermBuilder's content downstream?
    private final long builderSize;


    /**
     * <p>
     * Creates a new stateless AbstractSetOperation template.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param other The other Pipe of Term(s) to treat as a set of elements.
     *              Must not be null.
     */
    public AbstractSetOperation (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Pipe<?, VALUE> other
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( input_pipe,      // input_pipe
               downstream_type, // downstream_type
               null,            // upstream_or_null
               other );         // other
    }


    /**
     * <p>
     * Creates a new AbstractSetOperation.
     * </p>
     *
     * @param input_pipe The upstream Pipe, from which we read Term(s).
     *                   Must not be null.
     *
     * @param upstream_or_null Either the Stream coming from the
     *                         input_pipe from which we read input Term(s),
     *                         or null if this is a stateless
     *                         template AbstractSetOperation.
     *                         Must not be null.
     *
     * @param downstream_type The Type of Term(s) to write downstream.
     *                        Must not be null.
     *
     * @param other The other Pipe of Term(s) to treat as a set of elements.
     *              Must not be null.
     */
    public AbstractSetOperation (
            Pipe<?, VALUE> input_pipe,
            Type<VALUE> downstream_type,
            Buffer<VALUE> upstream_or_null,
            Pipe<?, VALUE> other
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeInstanceOfClass.Violation
    {
        super ( input_pipe,           // input_pipe
                downstream_type,      // downstream_type
                upstream_or_null );   // upstream_or_null

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               other );

        this.other = other;

        if ( this.state () == Pipe.State.STATELESS )
        {
            // Template.
            this.otherBuffer = null;
        }
        else
        {
            // Instance.
            // Create a stream for each parameter.
            this.otherBuffer = // Always a buffer.
                this.createParameterBuffer ( this.other );
        }

        this.builderSize = this.builderSize ();
        classContracts.check ( Return.AlwaysGreaterThanZero.CONTRACT,
                               this.builderSize );
    }


    /**
     * <p>
     * Adds all elements of the specified Term set to the specified
     * BloomFilter and / or adds the difference/intersection/union
     * to the specified set builder (if non-null) and outputs
     * any and all built Term(s) downstream.
     * </p>
     *
     * @param set The term to add.  Must be Countable or Cyclical.
     *            Must not be null.
     *
     * @param add_to_filter_or_null The BloomFilter to add to.
     *                             CAN be null.
     *
     * @param other_filter_or_null The optional other BloomFilter against
     *                             which each element will be checked
     *                             for membership; or null to not check.
     *
     * @param other_terms_or_null The optional other set against which
     *                            each element will be checked
     *                            for membership, if the other BloomFilter
     *                            claims that it is a member;
     *                            or null to not check.
     *
     * @param set_builder_or_null The optional TermBuilder which will build
     *                            up the output set by first checking
     *                            each element for membership in the
     *                            other filter and other terms, then calling
     *                            <code> this.intersecting ( ... ) </code>
     *                            or <code> this.different ( ... ) </code>,
     *                            depending on whether the element is
     *                            or is not a member of the other set;
     *                            or null to not build up the output.
     *                            Once the <code> builderSize () </code>
     *                            number of elements have been added
     *                            to the builder, a Countable term will
     *                            be built and flushed downstream.
     *                            At the end of the operation, the final
     *                            Countable output (possibly No value)
     *                            will be built and flushed downstream.
     *                            If the builder is null, then no output
     *                            will be generated by this operation.
     *                            CAN be null.
     *
     * @param downstream The Stream to which this SetOperation
     *                   will write its output Term(s), including
     *                   any errors.  Must not be null.
     *
     * @return True if there have been no errors and downstream is still
     *         open for input; false if the operation should be aborted.
     *
     * @throws UncheckedTermViolation If the specified set
     *         is neither Countable nor Cyclical.
     */
    protected final boolean add (
            Term<VALUE> set,
            BloomFilter add_to_filter_or_null,
            BloomFilter other_filter_or_null,
            Term<VALUE> [] other_terms_or_null,
            TermBuilder<VALUE> set_builder_or_null,
            Stream<VALUE> downstream
            )
        throws ParametersMustNotBeNull.Violation,
               UncheckedTermViolation
    {
        final Countable<VALUE> finite;
        final Countable<VALUE> cycle;
        if ( set instanceof Countable )
        {
            finite = (Countable<VALUE>) set;
            cycle = null;
        }
        else if ( set instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) set;
            finite = cyclical.header ();
            cycle = cyclical.cycle ();
        }
        else
        {
            final TermViolation violation =
                Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.violation (
                    this,  // plaintiff
                    set ); // evidence
            throw new UncheckedTermViolation ( violation );
        }

        for ( VALUE element : finite )
        {
            if ( add_to_filter_or_null != null )
            {
                add_to_filter_or_null.add ( element );
            }

            if ( set_builder_or_null != null
                 && other_terms_or_null != null )
            {
                final TermViolation violation_or_null;
                if ( this.isMember (
                         element,                    // maybe_member
                         other_filter_or_null,       // bloom_filter_or_null
                         other_terms_or_null ) )     // set
                {
                    violation_or_null =
                        this.intersecting (
                            set_builder_or_null,     // set_builder
                            element );               // intersecting
                }
                else
                {
                    // Definitely not in the other set.
                    violation_or_null =
                        this.different (
                            set_builder_or_null,     // set_builder
                            element );               // different
                }

                if ( violation_or_null != null )
                {
                    // Uh-oh.  Abort!
                    final Error<VALUE> error =
                        new Error<VALUE> ( this.downstreamType (),
                                           violation_or_null );
                    downstream.write ( error );

                    // Ended with an error.
                    // Finished.
                    return false;
                }
                else if ( set_builder_or_null.length ()
                          >= this.builderSize )
                {
                    // Time to build an output Term
                    // and flush it downstream.
                    final Countable<VALUE> output =
                        set_builder_or_null.build ();
                    set_builder_or_null.removeAll ();
                    final Pipe.State downstream_state =
                        downstream.write ( output );
                    if ( downstream_state != Pipe.State.OPEN )
                    {
                        // Downstream is closed, so we are finished.
                        return false;
                    }
                }
            }
        }

        if ( cycle != null )
        {
            for ( VALUE element : cycle )
            {
                if ( add_to_filter_or_null != null )
                {
                    add_to_filter_or_null.add ( element );
                }

                if ( set_builder_or_null != null
                     && other_terms_or_null != null )
                {
                    final TermViolation violation_or_null;
                    if ( this.isMember (
                             element,                // maybe_member
                             other_filter_or_null,   // bloom_filter_or_null
                             other_terms_or_null ) ) // set
                    {
                        violation_or_null =
                            this.intersecting (
                                set_builder_or_null, // set_builder
                                element );           // intersecting
                    }
                    else
                    {
                        // Definitely not in the other set.
                        violation_or_null =
                            this.different (
                                set_builder_or_null, // set_builder
                                element );           // different
                    }

                    if ( violation_or_null != null )
                    {
                        // Uh-oh.  Abort!
                        final Error<VALUE> error =
                            new Error<VALUE> ( this.downstreamType (),
                                               violation_or_null );
                        downstream.write ( error );

                        // Ended with an error.
                        // Finished.
                        return false;
                    }
                    else if ( set_builder_or_null.length ()
                              >= this.builderSize )
                    {
                        // Time to build an output Term
                        // and flush it downstream.
                        final Countable<VALUE> output =
                            set_builder_or_null.build ();
                        set_builder_or_null.removeAll ();
                        final Pipe.State downstream_state =
                            downstream.write ( output );
                        if ( downstream_state != Pipe.State.OPEN )
                        {
                            // Downstream is closed, so we are finished.
                            return false;
                        }
                    }
                }
            }
        }

        if ( set_builder_or_null != null )
        {
            // Time to build the final output Term and flush it downstream.
            final Countable<VALUE> output =
                set_builder_or_null.build ();
            set_builder_or_null.removeAll ();
            downstream.write ( output );
            // We don't care about the downstream state, we're finished
            // either way.
        }

        // Success.
        return true;
    }


    /**
     * <p>
     * Creates a new BloomFilter for the specified Buffer,
     * given the specified input length and other length.
     * </p>
     *
     * @param total_length The total length of the 2 Buffers, in element(s).
     *                     Must be greater than or equal to 0L.
     *
     * @return A new, un-populated BloomFilter for the specified Buffer.
     *         Never null.
     */
    protected BloomFilter bloomFilter (
            long total_length
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation
    {
        final int ratio = 15;
        final int max_num_buckets = 65536;
        final int num_buckets;
        if ( total_length >= ( (long) Integer.MAX_VALUE / (long) ratio ) )
        {
            num_buckets = max_num_buckets;
        }
        else
        {
            final int length_times_ratio =
                ( (int) total_length ) * ratio;
            if ( length_times_ratio > max_num_buckets )
            {
                num_buckets = max_num_buckets;
            }
            else
            {
                num_buckets = length_times_ratio;
            }
        }

        final BloomFilter bloom_filter = new BloomFilter ( num_buckets );

        return bloom_filter;
    }


    /**
     * @return The maximum number of elements to gather up in the
     *         output Term builder before flushing a Term downstream.
     *         Called once, from the constructor of AbstractSetOperation.
     *         Can be Long.MAX_VALUE for no limit.
     *         By default the builder size is set (arbitrarily) to 64L.
     *         Must be greater than 0L.
     */
    protected long builderSize ()
        throws Return.AlwaysGreaterThanZero.Violation
    {
        return AbstractSetOperation.DEFAULT_BUILDER_SIZE;
    }


    /**
     * <p>
     * Checks the specified Term to ensure it is either Countable
     * or Cyclical, and returns an Error if it is not.
     * </p>
     *
     * @param term The Term to check.  Must not be null.
     *
     * @return Either the Error generated because the Term is neither
     *         Countable nor Cyclical; or null, if the Term is either
     *         Countable or Cyclical.  CAN be null.
     */
    protected Error<VALUE> checkTerm (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation // CAN return null.
    {
        if (  Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.filter ( term )
              .isKept () )
        {
            // No error.
            return null;
        }

        // Can't perform set operations on unknown Term type.
        // Fail fast.
        // The Term must be either:
        // 1) Countable; or
        // 2) Cyclical.
        final TermViolation violation =
            Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.violation (
                this, // plaintiff
                term );
        final Error<VALUE> error =
            new Error<VALUE> ( this.downstreamType (),
                               violation );

        return error;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#createInputStream()
     *
     * Always a buffer, so that we can read it multiple times.
     */
    @Override
    protected final Buffer<VALUE> createInputStream ()
        throws ReturnNeverNull.Violation
    {
        final Buffer<VALUE> input_buffer =
            new Buffer<VALUE> ( this.inputPipe () ); // input_pipe
        return input_buffer;
    }


    /**
     * <p>
     * Performs one elemental step of the set operation, for an element
     * which is either in the input (left) stream or in the other
     * (right) stream, but not both.
     * </p>
     *
     * @param set_builder The (current) buider of the set operation's output.
     *                    Can change from one call to the next.
     *                    Must not be null.
     *
     * @param element The element which is in either the left or right stream.
     *                Must not be null.
     *
     * @return Either a TermViolation, if this element should induce
     *         a downstream Error and abort the set operation; or null,
     *         if everything is hunky-dory.  CAN be null.
     */
    protected abstract TermViolation different (
            TermBuilder<VALUE> set_builder,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * @see musaico.foundation.operation.AbstractPipe#step(musaico.foundation.operation.Stream, musaico.foundation.operation.Stream)
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"}) // Cast upstream
        // to Buffer<VALUE>;
        // Create generic Type<VALUE> [] arrays left and right.
    protected final boolean step (
            Stream<VALUE> upstream,
            Stream<VALUE> downstream
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeInstanceOfClass.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  upstream, downstream,
                                  this.otherBuffer );
        final Parameter1.MustBeInstanceOfClass upstream_must_be_buffer =
            new Parameter1.MustBeInstanceOfClass ( Buffer.class );
        this.contracts ().check ( upstream_must_be_buffer,
                                  upstream );

        if ( this.state () == Pipe.State.OPEN )
        {
            final Term<VALUE> input = upstream.read ().orNull ();

            if ( input != null )
            {
                final Error<VALUE> error = this.checkTerm ( input );
                if ( error != null )
                {
                    downstream.write ( error );

                    // Ended with an error.
                    // Finished.
                    return false;
                }

                // Input != null.
                // Keep reading from the input.
                return true;
            }

            // Input = null.
            // The input stream is now done.
            // We seal ourselves off from the input pipe, and
            // carry on reading from the other stream, below.
            this.close (); // Seal off this Pipe instance.
        }

        final Term<VALUE> other =
            this.otherBuffer.read ().orNull ();
        if ( other != null )
        {
            final Error<VALUE> error = this.checkTerm ( other );
            if ( error != null )
            {
                downstream.write ( error );

                // Ended with an error.
                // Finished.
                return false;
            }

            // Other != null.
            // Still reading from the other pipe.
            return true;
        }

        // Other == null.
        // We have already read to the end of the upstream, and now
        // we've also reached the end of the other stream.
        // Now it's time to perform the set operation.
        final Buffer<VALUE> upstream_buffer = (Buffer<VALUE>) upstream;
        upstream_buffer.reset ();
        this.otherBuffer.reset ();

        final Term<VALUE> [] left = (Term<VALUE> [])
            new Term [ upstream_buffer.available () ];
        for ( int l = 0; l < left.length; l ++ )
        {
            final Maybe<Term<VALUE>> maybe_left = upstream_buffer.read ();
            left [ l ] = maybe_left.orNull ();
            if ( left [ l ] == null )
            {
                final ValueMustBeOneOrMore.Violation violation =
                    ValueMustBeOneOrMore.CONTRACT.violation (
                        this, // plaintiff
                        maybe_left );
                final Error<VALUE> error =
                    new Error<VALUE> ( this.downstreamType (),
                                       violation );
                downstream.write ( error );

                // Ended with an error.
                // Finished.
                return false;
            }
        }
        final long left_length = this.length ( left );
        if ( left_length < 0L )
        {
            // Should be impossible.  We checkTerm ()ed while reading
            // in the terms.
            return false;
        }

        final Term<VALUE> [] right = (Term<VALUE> [])
            new Term [ this.otherBuffer.available () ];
        for ( int r = 0; r < right.length; r ++ )
        {
            final Maybe<Term<VALUE>> maybe_right = this.otherBuffer.read ();
            right [ r ] = maybe_right.orNull ();
            if ( right [ r ] == null )
            {
                final ValueMustBeOneOrMore.Violation violation =
                    ValueMustBeOneOrMore.CONTRACT.violation (
                        this, // plaintiff
                        maybe_right );
                final Error<VALUE> error =
                    new Error<VALUE> ( this.downstreamType (),
                                       violation );
                downstream.write ( error );

                // Ended with an error.
                // Finished.
                return false;
            }
        }
        final long right_length = this.length ( right );
        if ( right_length < 0L )
        {
            // Should be impossible.  We checkTerm ()ed while reading
            // in the terms.
            return false;
        }

        this.setOperation ( left,         // left
                            left_length,  // left_length
                            right,        // right
                            right_length, // right_length
                            downstream ); // downstream

        // After the set operation we're done.
        this.close (); // Seal off this Pipe instance.
        return false;
    }


    // Every AbstractPipe must implement
    //musaico.foundation.operation.AbstractPipe#instantiate()


    /**
     * <p>
     * Returns the total length of the specified Term(s).
     * </p>
     *
     * @param terms The Term(s) whose length(s) will be summed.
     *              If any of the Term(s) is neither Countable
     *              nor Infinite (such as an Error), then -1L will be
     *              returned.  Otherwise, if any of the Terms is Infinite
     *              (such as a Cyclical value), then Long.MAX_VALUE
     *              will be returned.  Otherwise, the Countable
     *              lengths will be added together, to a maximum
     *              of Long.MAX_VALUE.  Must not be null.
     *              Must not contain any null elements.
     *
     * @return The sum of the specified Term(s).  Always greater than
     *         or equal to -1L.
     */
    protected long length (
            Term<VALUE> [] terms
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        long length = 0L;
        for ( int t = 0; t < terms.length; t ++ )
        {
            final long term_length;
            if ( terms [ t ] instanceof Countable )
            {
                final Countable<VALUE> countable =
                    (Countable<VALUE>) terms [ t ];
                term_length = countable.length ();
            }
            else if ( terms [ t ] instanceof Cyclical )
            {
                final Cyclical<VALUE> cyclical =
                    (Cyclical<VALUE>) terms [ t ];
                final Countable<VALUE> header =
                    cyclical.header ();
                final Countable<VALUE> cycle =
                    cyclical.cycle ();
                term_length =
                    header.length ()
                    + cycle.length ();
            }
            else if ( terms [ t ] instanceof Infinite )
            {
                term_length = Long.MAX_VALUE;
            }
            else
            {
                term_length = -1L;
            }

            if ( term_length < 0L )
            {
                length = -1L;
                break;
            }
            else if ( term_length < ( Long.MAX_VALUE - length ) )
            {
                length += term_length;
            }
            else
            {
                length = Long.MAX_VALUE;
                // Keep looking, in case an Error or similar
                // comes later in the stream (then the length
                // will change to -1L).
            }
        }

        return length;
    }


    /**
     * <p>
     * Performs one elemental step of the set operation, for an element
     * which is a member of both the input (left) stream and the other
     * (right) stream.
     * </p>
     *
     * @param set_builder The (current) buider of the set operation's output.
     *                    Can change from one call to the next.
     *                    Must not be null.
     *
     * @param element The element which is in both the left and right streams.
     *                Must not be null.
     *
     * @return Either a TermViolation, if this element should induce
     *         a downstream Error and abort the set operation; or null,
     *         if everything is hunky-dory.  CAN be null.
     */
    protected abstract TermViolation intersecting (
            TermBuilder<VALUE> set_builder,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Returns true if the specified maybe-member is a member of
     * the specified Term(s) of elements; or false if not.
     * </p>
     *
     * @param maybe_member The element to test for set membership.
     *                     Must not be null.
     *
     * @param bloom_filter_or_null The optional BloomFilter to
     *                             speed up elimination of elements
     *                             which are definitely not members
     *                             of the specified set.  CAN be null.
     *
     * @param set Zero or more Term(s) comprising the set.  Each Term
     *            in the set must be either Countable or Cyclical.
     *            Must not be null.  Must not contain any null elements.
     *
     * @return True if the specified maybe-member is definitely a member
     *         of the specified set; false if it is definitely not
     *         (either because the BloomFilter said so, or because an
     *         element-by-element comparison was performed).
     */
    protected final boolean  isMember (
            VALUE maybe_member,
            BloomFilter bloom_filter_or_null,
            Term<VALUE> [] set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               UncheckedTermViolation
    {
        if ( bloom_filter_or_null != null )
        {
            if ( ! bloom_filter_or_null.contains ( maybe_member ) )
            {
                // Definitely not in the set.
                return false;
            }

            // Might be in the set.  We have to check the slow way, below.
        }

        // Iterate throught the whole set to find the maybe member.
        for ( Term<VALUE> term : set )
        {
            final Countable<VALUE> finite;
            final Countable<VALUE> cycle;
            if ( term instanceof Countable )
            {
                finite = (Countable<VALUE>) term;
                cycle = null;
            }
            else if ( term instanceof Cyclical )
            {
                final Cyclical<VALUE> cyclical = (Cyclical<VALUE>) term;
                finite = cyclical.header ();
                cycle = cyclical.cycle ();
            }
            else
            {
                final TermViolation violation =
                    Stream.TERM_MUST_BE_COUNTABLE_OR_CYCLICAL.violation (
                        this,  // plaintiff
                        term ); // evidence
                throw new UncheckedTermViolation ( violation );
            }

            for ( VALUE element : finite )
            {
                if ( element.equals ( maybe_member ) )
                {
                    return true;
                }
            }

            for ( VALUE element : cycle )
            {
                if ( element.equals ( maybe_member ) )
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * @return The other Pipe of Term(s) representing a set.  Never null.
     */
    public final Pipe<?, VALUE> other ()
        throws ReturnNeverNull.Violation
    {
        return this.other;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameters(java.util.List)
     *
     * Can be overridden by derived Pipes that add parameters or
     * provide fixed constants to AbstractSetOperation as its parameters.
     */
    @Override
    protected List<Pipe<?, ?>> parameters (
            List<Pipe<?, ?>> parameters_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        parameters_list.add ( this.other );
        return parameters_list;
    }


    /**
     * @see musaico.foundation.operation.AbstractPipe#parameterStreams(java.util.List)
     */
    @Override
    protected List<Stream<?>> parameterStreams (
            List<Stream<?>> parameter_streams_list
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        if ( this.other != null )
        {
            parameter_streams_list.add ( this.otherBuffer );
        }

        return parameter_streams_list;
    }


    /**
     * <p>
     * Performs this set operation on the specified Buffers.
     * </p>
     *
     * @param left One set, comprising 0 or more term(s).  Must not be null.
     *
     * @param left_length The total length of the left set:
     *                    0 or more term(s), each with 0L or more element(s),
     *                    all summed together.  Note that if there is
     *                    a non-Cylical infinite term, Long.MAX_VALUE
     *                    will always be passed; and if there is any
     *                    non-Countable, non-Cyclical term, then -1L
     *                    will always be passed.  Always greater than
     *                    or equal to -1L.
     *
     * @param right The other set, comprising 0 or more term(s).
     *              Must not be null.
     *
     * @param right_length The total length of the right set:
     *                     0 or more term(s), each with 0L or more element(s),
     *                     all summed together.  Note that if there is
     *                     a non-Cylical infinite term, Long.MAX_VALUE
     *                     will always be passed; and if there is any
     *                     non-Countable, non-Cyclical term, then -1L
     *                     will always be passed.  Always greater than
     *                     or equal to -1L.
     *
     * @param downstream The Stream to which this SetOperation
     *                   will write its output Term(s).  Must not be null.
     */
    protected abstract void setOperation (
            Term<VALUE> [] left,
            long left_length,
            Term<VALUE> [] right,
            long right_length,
            Stream<VALUE> downstream
            )
        throws ParametersMustNotBeNull.Violation;
}
