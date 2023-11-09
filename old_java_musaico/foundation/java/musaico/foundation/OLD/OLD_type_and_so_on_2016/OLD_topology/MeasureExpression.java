package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.typing.Constant;
import musaico.foundation.typing.Expression;
import musaico.foundation.typing.Identity;
import musaico.foundation.typing.MetaBlockingTimeLimit;
import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;

import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Value;


/**
 * <p>
 * An Expression such as "x + 5", starting from a measure (distance or size)
 * in some topological space.
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
 * @see musaico.foundation.topology.MODULE#COPYRIGHT
 * @see musaico.foundation.topology.MODULE#LICENSE
 */
public class MeasureExpression<POINT extends Serializable, MEASURE extends Serializable>
    extends Expression<MEASURE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( MeasureExpression.class );


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The topological space in which this expression resides.
    private final Topology<POINT, MEASURE> topology;

    // The single metadata shared by all Terms created by this expression.
    private final Metadata sharedMetadata;


    /**
     * <p>
     * Creates a new MeasureExpression from the specified
     * measurement Topology and initial toplevel measure(s) Term.
     * </p>
     *
     * <p>
     * This initial toplevel expression comprises an Identity
     * operation on the initial term.
     * </p>
     *
     * @param topology The topological space in which this expression resides.
     *                 Must not be null.
     *
     * @param top_term The measure(s) input Term to this Expression.
     *                 Can be a Constant or a whole
     *                 Expression and so on.  Must not be null.
     */
    public MeasureExpression (
                              Topology<POINT, MEASURE> topology,
                              Term<MEASURE> top_term
                              )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology,
               top_term,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new MeasureExpression from the specified
     * measurement Topology and initial toplevel measure(s) Term.
     * </p>
     *
     * <p>
     * This initial toplevel expression comprises an Identity
     * operation on the initial term.
     * </p>
     *
     * @param topology The topological space in which this expression resides.
     *                 Must not be null.
     *
     * @param top_term The measure(s) input Term to this Expression.
     *                 Can be a Constant or a whole
     *                 Expression and so on.  Must not be null.
     *
     * @param shared_metadata The Metadata which will be shared by all Terms
     *                        created by this expression. 
     *                        Can be Metadata.NONE.  Must not be null.
     */
    public MeasureExpression (
                              Topology<POINT, MEASURE> topology,
                              Term<MEASURE> top_term,
                              Metadata shared_metadata
                              )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new Identity<MEASURE> ( topology == null
                                            ? null
                                            : topology.measureSignature ().type () ),
                shared_metadata,
                top_term );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology );

        this.topology = topology;
        this.sharedMetadata = shared_metadata;

        // Maximum amount of time to wait when evaluating the
        // Expression in calls to measure ().
        if ( ! this.sharedMetadata.has ( MetaBlockingTimeLimit.class ) )
        {
            final MetaBlockingTimeLimit blocking_time =
                this.topology.measureSignature ().type ()
                    .findMetadatum ( MetaBlockingTimeLimit.class )
                    .orNone ();
            this.sharedMetadata.addOrGet ( MetaBlockingTimeLimit.class,
                                           blocking_time );
        }

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Creates a new MeasureExpression from the specified binary Operation
     * and two input operands.
     * </p>
     *
     * @param topology The topological space in which this expression
     *                 Must not be null.
     *
     * @param operation The binary Operation which this expression will
     *                  evaluate on the two operand inputs.
     *                  Must not be null.
     *
     * @param left The 1st input to the binary Operation.
     *             Can be a Constant or a whole Expression and so on.
     *             Must not be null.
     *
     * @param right The 2nd input to the binary Operation.
     *              Can be a Constant or a whole Expression and so on.
     *              Must not be null.
     *
     * @param shared_metadata The Metadata which will be shared by all Terms
     *                        created by this expression.
     *                        Can be Metadata.NONE.  Must not be null.
     */
    public MeasureExpression (
                              Topology<POINT, MEASURE> topology,
                              Operation2<?, ?, MEASURE> operation,
                              Term<?> left,
                              Term<?> right,
                              Metadata shared_metadata
                              )
        throws ParametersMustNotBeNull.Violation
    {
        super ( operation,
                shared_metadata,
                left,
                right );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology );

        this.topology = topology;
        this.sharedMetadata = shared_metadata;

        // Maximum amount of time to wait when evaluating the
        // Expression in calls to measure ().
        if ( ! this.sharedMetadata.has ( MetaBlockingTimeLimit.class ) )
        {
            final MetaBlockingTimeLimit blocking_time =
                this.topology.measureSignature ().type ()
                    .findMetadatum ( MetaBlockingTimeLimit.class )
                    .orNone ();
            this.sharedMetadata.addOrGet ( MetaBlockingTimeLimit.class,
                                           blocking_time );
        }

        this.contracts = new TrackingContracts ( this, this.sharedMetadata );
    }


    /**
     * <p>
     * Adds the specified relative measure to this measure expression.
     * </p>
     *
     * @param measure The amount to add to this measure expression.
     *                Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> add (
                                                  MEASURE measure
                                                  )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term =
            this.topology.measureTerm ( measure );
        return this.add ( measure_term );
    }


    /**
     * <p>
     * Adds the specified value(s) of measure to this measure expression.
     * </p>
     *
     * @param measure_value The value(s) to add to this measure expression.
     *                      Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> add (
                                                  Value<MEASURE> measure_value
                                                  )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term =
            this.topology.measureTerm ( measure_value );
        return this.add ( measure_term );
    }


    /**
     * <p>
     * Adds the specified relative measure Term to this measure expression.
     * </p>
     *
     * @param measure_term The amount to add to this measure expression.
     *                     Can be a Constant, a Variable,
     *                     an Expression, and so on.  Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<MEASURE>.
    public MeasureExpression<POINT, MEASURE> add (
                                                  Term<MEASURE> measure_term
                                                  )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<MEASURE, MEASURE, MEASURE> add =
            this.topology.measureSignature ().add ();

        final MeasureExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<MEASURE> initial_measure_term =
                (Term<MEASURE>) this.inputs ().get ( 0 );
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    add,
                    initial_measure_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    add,
                    this,
                    measure_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Evaluates this measure expression, waits for
     * the resulting MEASURE, and then returns it to the caller.
     * </p>
     *
     * <p>
     * THIS METHOD BLOCKS THE CALLER.
     * To retrieve the resulting MEASURE asynchronously, call
     * <code> value ().async () </code> instead.
     * </p>
     *
     * @return The measure calculated from this measure Expression.
     *         Never null.
     */
    public final NonBlocking<MEASURE> measure ()
    {
        final long maximum_blocking_time_milliseconds =
            this.findMetadatum ( MetaBlockingTimeLimit.class )
                .orNone ()
                .maximumBlockingTimeMilliseconds ();

        final NonBlocking<MEASURE> measure =
            this.value ().await ( maximum_blocking_time_milliseconds );

        return measure;
    }


    public abstract MEASURE unit ()
        throws ReturnNeverNull.Violation;
}
