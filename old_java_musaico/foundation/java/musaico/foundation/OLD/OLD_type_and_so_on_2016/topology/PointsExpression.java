package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.typing.BlockingConstant;
import musaico.foundation.typing.Constant;
import musaico.foundation.typing.Expression;
import musaico.foundation.typing.Identity;
import musaico.foundation.typing.MetaBlockingTimeLimit;
import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;

import musaico.foundation.value.Countable;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;


/**
 * <p>
 * An Expression such as "x + 5", starting from a point
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
public class PointsExpression<POINT extends Serializable, MEASURE extends Serializable>
    extends Expression<POINT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( PointsExpression.class );


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // The topological space for which this expression.
    private final Topology<POINT, MEASURE> topology;

    // The single metadata shared by this expression and all the
    // children it creates via method calls.
    private final Metadata sharedMetadata;


    /**
     * <p>
     * Creates a new PointsExpression from the specified
     * measurement Topology and initial toplevel point(s) Term.
     * </p>
     *
     * <p>
     * This initial toplevel expression comprises an Identity
     * operation on the initial term.
     * </p>
     *
     * @param topology The topological space in which this expression
     *                 resides.  Must not be null.
     *
     * @param top_term The point(s) input Term to this Expression.
     *                 Can be a Constant or a whole Expression and so on.
     *                 Must not be null.
     */
    public PointsExpression (
                             Topology<POINT, MEASURE> topology,
                             Term<POINT> top_term
                             )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology,
               top_term,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new PointsExpression from the specified
     * measurement Topology and initial toplevel point(s) Term.
     * </p>
     *
     * <p>
     * This initial toplevel expression comprises an Identity
     * operation on the initial term.
     * </p>
     *
     * @param topology The topological space in which this expression
     *                 Must not be null.
     *
     * @param top_term The point(s) input Term to this Expression
     *                 Can be a Constant or a whole Expression and so on.
     *                 Must not be null.
     *
     * @param shared_metadata The Metadata which will be shared by all Terms
     *                        created by this expression.
     *                        Can be Metadata.NONE.  Must not be null.
     */
    public PointsExpression (
                             Topology<POINT, MEASURE> topology,
                             Term<POINT> top_term,
                             Metadata shared_metadata
                             )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new Identity<POINT> ( topology == null
                                          ? null
                                          : topology.pointType () ),
                shared_metadata,
                top_term );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology );

        this.topology = topology;
        this.sharedMetadata = shared_metadata;

        // Maximum amount of time to wait when evaluating the
        // Expression in calls to point ().
        if ( ! this.sharedMetadata.has ( MetaBlockingTimeLimit.class ) )
        {
            final MetaBlockingTimeLimit blocking_time =
                this.topology.measureType ()
                    .findMetadatum ( MetaBlockingTimeLimit.class )
                    .orNone ();
            this.sharedMetadata.addOrGet ( MetaBlockingTimeLimit.class,
                                           blocking_time );
        }

        this.contracts = new TrackingContracts ( this, this.sharedMetadata );
    }


    /**
     * <p>
     * Creates a new PointsExpression from the specified binary Operation
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
    public PointsExpression (
                             Topology<POINT, MEASURE> topology,
                             Operation2<POINT, MEASURE, POINT> operation,
                             Term<POINT> left,
                             Term<MEASURE> right,
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
        // Expression in calls to point ().
        if ( ! this.sharedMetadata.has ( MetaBlockingTimeLimit.class ) )
        {
            final MetaBlockingTimeLimit blocking_time =
                this.topology.measureType ()
                    .findMetadatum ( MetaBlockingTimeLimit.class )
                    .orNone ();
            this.sharedMetadata.addOrGet ( MetaBlockingTimeLimit.class,
                                           blocking_time );
        }

        this.contracts = new TrackingContracts ( this, this.sharedMetadata );
    }


    /**
     * <p>
     * Creates a new PointsExpression from the specified unary Operation
     * and one input operand.
     * </p>
     *
     * @param topology The topological space in which this expression
     *                 Must not be null.
     *
     * @param operation The unary Operation which this expression will
     *                  evaluate on the one operand input.
     *                  Must not be null.
     *
     * @param input The input to the binary Operation.
     *              Can be a Constant or a whole Expression and so on.
     *              Must not be null.
     *
     * @param shared_metadata The Metadata which will be shared by all Terms
     *                        created by this expression.
     *                        Can be Metadata.NONE.  Must not be null.
     */
    public PointsExpression (
                             Topology<POINT, MEASURE> topology,
                             Operation1<POINT, POINT> operation,
                             Term<POINT> input,
                             Metadata shared_metadata
                             )
        throws ParametersMustNotBeNull.Violation
    {
        super ( operation,
                shared_metadata,
                input );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology );

        this.topology = topology;
        this.sharedMetadata = shared_metadata;

        // Maximum amount of time to wait when evaluating the
        // Expression in calls to point ().
        if ( ! this.sharedMetadata.has ( MetaBlockingTimeLimit.class ) )
        {
            final MetaBlockingTimeLimit blocking_time =
                this.topology.measureType ()
                    .findMetadatum ( MetaBlockingTimeLimit.class )
                    .orNone ();
            this.sharedMetadata.addOrGet ( MetaBlockingTimeLimit.class,
                                           blocking_time );
        }

        this.contracts = new TrackingContracts ( this, this.sharedMetadata );
    }


    /**
     * <p>
     * Adds the specified relative measure to this point expression.
     * </p>
     *
     * @param measure The amount to add to this point expression.
     *                Must not be null.
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointsExpression<POINT, MEASURE> add (
                                                 MEASURE measure
                                                 )
        throws ParametersMustNotBeNull.Violation
    {
        final Measure<POINT, MEASURE> measure_term =
            new Measure<POINT, MEASURE> ( this.topology,
                                           measure );
        return this.add ( measure_term );
    }


    /**
     * <p>
     * Adds the specified value(s) of measure to this point expression.
     * </p>
     *
     * @param measure_value The value(s) to add to this point expression.
     *                      Must not be null.
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointsExpression<POINT, MEASURE> add (
                                                 Value<MEASURE> measure_value
                                                 )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term;
        if ( measure_value instanceof One )
        {
            measure_term =
                new Measure<POINT, MEASURE> ( this.topology,
                                              (One<MEASURE>) measure_value );
        }
        else
        {
            measure_term =
                this.topology.measureType ().instance ( measure_value );
        }

        return this.add ( measure_term );
    }


    /**
     * <p>
     * Adds the specified relative measure Term to this point expression.
     * </p>
     *
     * @param measure_term The amount to add to this point expression.
     *                     Can be a Constant, a Variable,
     *                     an Expression, and so on.  Must not be null.
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointsExpression<POINT, MEASURE> add (
                                                 Term<MEASURE> measure_term
                                                 )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> add =
            this.topology.operations ().addPointPlusMeasure ();

        final PointsExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    add,
                    initial_points_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointsExpression<POINT, MEASURE> (
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
     * Returns the next Position after this point expression.
     * </p>
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointsExpression<POINT, MEASURE> next ()
        throws ParametersMustNotBeNull.Violation
    {
        final Operation1<POINT, POINT> next =
            this.topology.operations ().nextPoint ();

        final PointsExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    next,
                    initial_points_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    next,
                    this,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Returns the previous Position before this point expression.
     * </p>
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointsExpression<POINT, MEASURE> previous ()
        throws ParametersMustNotBeNull.Violation
    {
        final Operation1<POINT, POINT> previous =
            this.topology.operations ().previousPoint ();

        final PointsExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    previous,
                    initial_points_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    previous,
                    this,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Subtracts the specified relative measure from this point expression.
     * </p>
     *
     * @param measure The amount to subtract from this point expression.
     *                Must not be null.
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointsExpression<POINT, MEASURE> subtractMeasure (
                                                             MEASURE measure
                                                             )
        throws ParametersMustNotBeNull.Violation
    {
        final Measure<POINT, MEASURE> measure_term =
            new Measure<POINT, MEASURE> ( this.topology,
                                           measure );
        return this.subtractMeasure ( measure_term );
    }


    /**
     * <p>
     * Subtracts the specified value(s) of measure from this point expression.
     * </p>
     *
     * @param measure_value The value(s) to subtract from
     *                      this point expression.
     *                      Must not be null.
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointsExpression<POINT, MEASURE> subtractMeasure (
                                                             Value<MEASURE> measure_value
                                                             )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term;
        if ( measure_value instanceof One )
        {
            measure_term =
                new Measure<POINT, MEASURE> ( this.topology,
                                              (One<MEASURE>) measure_value );
        }
        else
        {
            measure_term =
                this.topology.measureType ().instance ( measure_value );
        }

        return this.subtractMeasure ( measure_term );
    }


    /**
     * <p>
     * Subtracts the specified relative measure Term from
     * this point expression.
     * </p>
     *
     * @param measure_term The amount to subtract from this point expression.
     *                     Can be a Constant, a Variable,
     *                     an Expression, and so on.  Must not be null.
     *
     * @return A newly created PointsExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointsExpression<POINT, MEASURE> subtractMeasure (
                                                             Term<MEASURE> measure_term
                                                             )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> subtract_measure =
            this.topology.operations ().subtractPointMinusMeasure ();

        final PointsExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_measure,
                    initial_points_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointsExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_measure,
                    this,
                    measure_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Subtracts the specified absolute point from this point expression.
     * </p>
     *
     * @param point The point to subtract from this point expression.
     *              Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs POINT -> POINT ...
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            POINT point
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Points<POINT, MEASURE> points_term =
            new Points<POINT, MEASURE> ( this.topology,
                                         point );
        return this.subtractPoint ( points_term );
    }


    /**
     * <p>
     * Subtracts the specified value(s) of point from this point expression.
     * </p>
     *
     * @param points_value The value(s) to subtract from this point expression.
     *                     Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            Value<POINT> points_value
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<POINT> points_term;
        if ( points_value instanceof Countable )
        {
            points_term =
                new Points<POINT, MEASURE> ( this.topology,
                                             (Countable<POINT>) points_value );
        }
        else
        {
            points_term =
                this.topology.pointType ().instance ( points_value );
        }

        return this.subtractPoint ( points_term );
    }


    /**
     * <p>
     * Subtracts the specified absolute point Term from this point expression.
     * </p>
     *
     * @param points_term The amount to subtract from this point expression.
     *                    Can be a Constant, a Variable,
     *                    an Expression, and so on.  Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            Term<POINT> points_term
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, POINT, MEASURE> subtract_point =
            this.topology.operations ().subtractPointMinusPoint ();

        final MeasureExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_point,
                    initial_points_term,
                    points_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_point,
                    this,
                    points_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Creates a new Region from this point expression to the specified
     * absolute point.
     * </p>
     *
     * @param point The point to which the new Region will extend from
     *              this point expression.  Must not be null.
     *
     * @return An expression containing a newly created Region, with this
     *         point expression as the start of the Region, and the specified
     *         end point as the end of the Region.  Never null.
     */
    @SuppressWarnings("unchecked") // Generic varargs POINT -> POINT ...
    public RegionExpression<POINT, MEASURE> region (
                                                    POINT point
                                                    )
        throws ParametersMustNotBeNull.Violation
    {
        final Points<POINT, MEASURE> points_term =
            new Points<POINT, MEASURE> ( this.topology,
                                          point );
        return this.region ( points_term );
    }


    /**
     * <p>
     * Creates a new Region from this point expression to the specified
     * value(s) of point.
     * </p>
     *
     * @param points_value The value(s) to which the new Region will
     *                     extend from this point expression.
     *                     Must not be null.
     *
     * @return An expression containing a newly created Region, with this
     *         point expression as the start of the Region, and the specified
     *         end point(s).  Never null.
     */
    public RegionExpression<POINT, MEASURE> region (
                                                    Value<POINT> points_value
                                                    )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<POINT> points_term;
        if ( points_value instanceof Countable )
        {
            points_term =
                new Points<POINT, MEASURE> ( this.topology,
                                             (Countable<POINT>) points_value );
        }
        else
        {
            points_term =
                this.topology.pointType ().instance ( points_value );
        }

        return this.region ( points_term );
    }


    /**
     * <p>
     * Creates a new Region from this point expression to the
     * specified point(s) Term.
     * </p>
     *
     * @param points_term The point(s) to which the new Region will
     *                    extend from this point expression.
     *                    Can be a Constant, a Variable,
     *                    an Expression, and so on.  Must not be null.
     *
     * @return An expression containing a newly created Region, with this
     *         point expression as the start of the Region, and the specified
     *         end point(s).  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public RegionExpression<POINT, MEASURE> region (
                                                    Term<POINT> points_term
                                                    )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, POINT, Region> region =
            this.topology.operations ().region ();

        final RegionExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new RegionExpression<POINT, MEASURE> (
                    this.topology,
                    region,
                    initial_points_term,
                    points_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new RegionExpression<POINT, MEASURE> (
                    this.topology,
                    region,
                    this,
                    points_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Evaluates this point expression, waits for
     * the resulting POINT, and then returns it to the caller.
     * </p>
     *
     * <p>
     * THIS METHOD BLOCKS THE CALLER.
     * To retrieve the resulting POINT asynchronously, call
     * <code> value ().async () </code> instead.
     * </p>
     *
     * @return The point calculated from this point Expression.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public final NonBlocking<POINT> point ()
    {
        final long maximum_blocking_time_milliseconds =
            this.findMetadatum ( MetaBlockingTimeLimit.class )
                .orNone ()
                .maximumBlockingTimeMilliseconds ();

        final NonBlocking<POINT> point;
        if ( this.operation () instanceof Identity )
        {
            // If we are just an Identity placeholder, don't bother
            // evaluating this Expression -- just evaluate the top
            // term.  If it's a Constant, we'll return right away,
            // without bothering to process anything.
            final Term<POINT> initial_points_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            point =
                this.value ().await ( maximum_blocking_time_milliseconds );
        }
        else
        {
            point =
                this.value ().await ( maximum_blocking_time_milliseconds );
        }

        return point;
    }
}
