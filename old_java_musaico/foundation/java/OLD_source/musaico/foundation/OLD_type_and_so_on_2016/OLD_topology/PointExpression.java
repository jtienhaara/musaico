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
public class PointExpression<POINT extends Serializable, MEASURE extends Serializable>
    extends Expression<POINT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( PointExpression.class );


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // The topological space for which this expression.
    private final Topology<POINT, MEASURE> topology;

    // The single metadata shared by this expression and all the
    // children it creates via method calls.
    private final Metadata sharedMetadata;


    /**
     * <p>
     * Creates a new PointExpression from the specified
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
    public PointExpression (
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
     * Creates a new PointExpression from the specified
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
    public PointExpression (
                            Topology<POINT, MEASURE> topology,
                            Term<POINT> top_term,
                            Metadata shared_metadata
                            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new Identity<POINT> ( topology == null
                                          ? null
                                          : topology.pointSignature ().type () ),
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
     * Creates a new PointExpression from the specified binary Operation
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
    public PointExpression (
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
     * Adds the specified relative measure to this point expression.
     * </p>
     *
     * @param measure The amount to add to this point expression.
     *                Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> add (
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
     * Adds the specified value(s) of measure to this point expression.
     * </p>
     *
     * @param measure_value The value(s) to add to this point expression.
     *                      Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> add (
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
     * Adds the specified relative measure Term to this point expression.
     * </p>
     *
     * @param measure_term The amount to add to this point expression.
     *                     Can be a Constant, a Variable,
     *                     an Expression, and so on.  Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointExpression<POINT, MEASURE> add (
                                                Term<MEASURE> measure_term
                                                )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> add =
            this.topology.pointSignature ().add ();

        final PointExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    add,
                    initial_point_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointExpression<POINT, MEASURE> (
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
     * Takes this point expression's distance from
     * the origin, modulus divided by the specified relative measure.
     * </p>
     *
     * @param measure The amount to modulo divide this point expression by.
     *                Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> modulo (
                                                     MEASURE measure
                                                     )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term =
            this.topology.measureTerm ( measure );
        return this.modulo ( measure_term );
    }


    /**
     * <p>
     * Takes this point expression's distance from the origin,
     * modulus divided by the specified value(s) of measure.
     * </p>
     *
     * @param measure_value The value(s) by which to modulo divide
     *                      this point expression.
     *                      Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> modulo (
                                                   Value<MEASURE> measure_value
                                                   )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term =
            this.topology.measureTerm ( measure_value );
        return this.modulo ( measure_term );
    }


    /**
     * <p>
     * Takes this point expression's distance from the origin,
     * modulus divided by the specified relative measure Term.
     * </p>
     *
     * @param measure_term The amount by which to modulo divide
     *                     this point expression.
     *                     Can be a Constant, a Variable,
     *                     an Expression, and so on.  Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointExpression<POINT, MEASURE> modulo (
                                                   Term<MEASURE> measure_term
                                                   )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> modulo =
            this.topology.pointSignature ().modulo ();

        final PointExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    modulo,
                    initial_point_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    modulo,
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
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> next ()
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> next =
            this.topology.pointSignature ().next ();

        final PointExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    next,
                    initial_point_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    next,
                    this,
                    measure_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Returns the previous Position before this point expression.
     * </p>
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> previous ()
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> previous =
            this.topology.pointSignature ().previous ();

        final PointExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    previous,
                    initial_point_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    previous,
                    this,
                    measure_term,
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
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> subtractMeasure (
                                                            MEASURE measure
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term =
            this.topology.measureTerm ( measure );
        return this.subtract ( measure_term );
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
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public PointExpression<POINT, MEASURE> subtractMeasure (
                                                            Value<MEASURE> measure_value
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<MEASURE> measure_term =
            this.topology.measureTerm ( measure_value );
        return this.subtract ( measure_term );
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
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public PointExpression<POINT, MEASURE> subtractMeasure (
                                                            Term<MEASURE> measure_term
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, MEASURE, POINT> subtract_measure =
            this.topology.pointSignature ().subtractMeasure ();

        final PointExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new PointExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_measure,
                    initial_point_term,
                    measure_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new PointExpression<POINT, MEASURE> (
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
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            POINT point
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<POINT> point_term =
            this.topology.pointTerm ( point );
        return this.subtractPoint ( point_term );
    }


    /**
     * <p>
     * Subtracts the specified value(s) of point from this point expression.
     * </p>
     *
     * @param point_value The value(s) to subtract from this point expression.
     *                    Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            Value<POINT> point_value
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<POINT> point_term =
            this.topology.pointTerm ( point_value );
        return this.subtractPoint ( point_term );
    }


    /**
     * <p>
     * Subtracts the specified absolute point Term from this point expression.
     * </p>
     *
     * @param point_term The amount to subtract from this point expression.
     *                   Can be a Constant, a Variable,
     *                   an Expression, and so on.  Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            Term<POINT> point_term
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, POINT, MEASURE> subtract_point =
            this.topology.pointSignature ().subtractPoint ();

        final MeasureExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_point,
                    initial_point_term,
                    point_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_point,
                    this,
                    point_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    !!!!!!!!!!!!!!!!!!!!!!!!!1to()
A Region = se se se se ...
Pairs of (start, end) points
In the case of se se se s (last point to max topological point),
    just set the last pair's e to be the max topological point.
se se se se s
x se se se se s y --> se se se sy
x se se se se y --> se se se
x se se se y s --> se se se
x se se se sye --> se se se sy
x se se se y se --> se se se
sxe se se se s y --> xe se se se sy
se x se se se s y --> se se se sy

x' = filter ( xse => s, sxe => x, sex => none )
y' = filter ( yse => none, sye => y, sey => e )
all points' = merge ( x', y', all points )
              filter out all before x'
              filter out all after y' )
    /**
     * <p>
     * Creates a new Region from this point expression to the specified
     * absolute point.
     * </p>
     *
     * @param point The point to subtract from this point expression.
     *              Must not be null.
     *
     * @return A newly created MeasureExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            POINT point
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<POINT> point_term =
            this.topology.pointTerm ( point );
        return this.subtractPoint ( point_term );
    }


    /**
     * <p>
     * Subtracts the specified value(s) of point from this point expression.
     * </p>
     *
     * @param point_value The value(s) to subtract from this point expression.
     *                    Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            Value<POINT> point_value
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<POINT> point_term =
            this.topology.pointTerm ( point_value );
        return this.subtractPoint ( point_term );
    }


    /**
     * <p>
     * Subtracts the specified absolute point Term from this point expression.
     * </p>
     *
     * @param point_term The amount to subtract from this point expression.
     *                   Can be a Constant, a Variable,
     *                   an Expression, and so on.  Must not be null.
     *
     * @return A newly created PointExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<POINT>.
    public MeasureExpression<POINT, MEASURE> subtractPoint (
                                                            Term<POINT> point_term
                                                            )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<POINT, POINT, MEASURE> subtract_point =
            this.topology.pointSignature ().subtractPoint ();

        final MeasureExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<POINT> initial_point_term =
                (Term<POINT>) this.inputs ().get ( 0 );
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_point,
                    initial_point_term,
                    point_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new MeasureExpression<POINT, MEASURE> (
                    this.topology,
                    subtract_point,
                    this,
                    point_term,
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

        final NonBlocking<POINT> point =
        if ( this.operation () instanceof Identity )
        {
            // If we are just an Identity placeholder, don't bother
            // evaluating this Expression -- just evaluate the top
            // term.  If it's a Constant, we'll return right away,
            // without bothering to process anything.
            final Term<POINT> initial_point_term =
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
