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
 * An Expression such as "X intersection Y", starting from a region
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
public class RegionExpression<POINT extends Serializable, MEASURE extends Serializable>
    extends Expression<Region>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( RegionExpression.class );


    // Checks method obligations and guarantees, and tracks violations.
    private final TrackingContracts contracts;

    // The topological space for which this expression.
    private final Topology<POINT, MEASURE> topology;

    // The single metadata shared by this expression and all the
    // children it creates via method calls.
    private final Metadata sharedMetadata;


    /**
     * <p>
     * Creates a new RegionExpression from the specified
     * measurement Topology and initial toplevel region(s) Term.
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
     * @param top_term The region(s) input Term to this Expression.
     *                 Can be a Constant or a whole Expression and so on.
     *                 Must not be null.
     */
    public RegionExpression (
                             Topology<POINT, MEASURE> topology,
                             Term<Region> top_term
                             )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology,
               top_term,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new RegionExpression from the specified
     * measurement Topology and initial toplevel region(s) Term.
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
     * @param top_term The region(s) input Term to this Expression
     *                 Can be a Constant or a whole Expression and so on.
     *                 Must not be null.
     *
     * @param shared_metadata The Metadata which will be shared by all Terms
     *                        created by this expression.
     *                        Can be Metadata.NONE.  Must not be null.
     */
    public RegionExpression (
                             Topology<POINT, MEASURE> topology,
                             Term<Region> top_term,
                             Metadata shared_metadata
                             )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new Identity<Region> ( Region.TYPE ),
                shared_metadata,
                top_term );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology );

        this.topology = topology;
        this.sharedMetadata = shared_metadata;

        // Maximum amount of time to wait when evaluating the
        // Expression in calls to region ().
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
     * Creates a new RegionExpression from the specified binary Operation
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
    public <LEFT extends Object, RIGHT extends Object>
        RegionExpression (
                          Topology<POINT, MEASURE> topology,
                          Operation2<LEFT, RIGHT, Region> operation,
                          Term<LEFT> left,
                          Term<RIGHT> right,
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
        // Expression in calls to region ().
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
     * Calculates the intersection region in common to the specified region
     * and this region expression.
     * </p>
     *
     * @param region The region with which to intersect this  expression.
     *                Must not be null.
     *
     * @return A newly created RegionExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public RegionExpression<POINT, MEASURE> intersection (
                                                          Region region
                                                          )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<Region> region_term =
            Region.TYPE.instance ( region );
        return this.intersection ( region_term );
    }


    /**
     * <p>
     * Calculates the intersection region(s) in common to the specified
     * value(s) of measure and this point expression.
     * </p>
     *
     * @param region_value The value(s) to intersect with this
     *                     region expression.  Must not be null.
     *
     * @return A newly created RegionExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    public RegionExpression<POINT, MEASURE> intersection (
                                                          Value<Region> region_value
                                                          )
        throws ParametersMustNotBeNull.Violation
    {
        final Term<Region> region_term =
            Region.TYPE.instance ( region_value );
        return this.intersection ( region_term );
    }


    /**
     * <p>
     * Calculates the intersection region in common between the
     * specified region Term and this point expression.
     * </p>
     *
     * @param region_term The region to intersect with this 
     *                    region expression.  Can be a Constant, a Variable,
     *                    an Expression, and so on.  Must not be null.
     *
     * @return A newly created RegionExpression, with this as the left
     *         input to the binary operation, and the specified term
     *         as the right.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast inputs().get(0) - Term<Region>.
    public RegionExpression<POINT, MEASURE> intersection (
                                                          Term<Region> region_term
                                                          )
        throws ParametersMustNotBeNull.Violation
    {
        final Operation2<Region, Region, Region> intersection =
            this.topology.operations ().intersection ();

        final RegionExpression<POINT, MEASURE> new_expression;
        // If we are just an Identity placeholder, pass the one input
        // to the new Expression constructor, rather than this whole
        // Expression.
        if ( this.operation () instanceof Identity )
        {
            final Term<Region> initial_region_term =
                (Term<Region>) this.inputs ().get ( 0 );
            new_expression =
                new RegionExpression<POINT, MEASURE> (
                    this.topology,
                    intersection,
                    initial_region_term,
                    region_term,
                    this.sharedMetadata );
        }
        else
        {
            new_expression =
                new RegionExpression<POINT, MEASURE> (
                    this.topology,
                    intersection,
                    this,
                    region_term,
                    this.sharedMetadata );
        }

        return new_expression;
    }


    /**
     * <p>
     * Evaluates this region expression, waits for
     * the resulting Region, and then returns it to the caller.
     * </p>
     *
     * <p>
     * THIS METHOD BLOCKS THE CALLER.
     * To retrieve the resulting Region asynchronously, call
     * <code> value ().async () </code> instead.
     * </p>
     *
     * @return The region(s) value calculated from this region Expression.
     *         Never null.
     */
    public final NonBlocking<Region> region ()
    {
        final long maximum_blocking_time_milliseconds =
            this.findMetadatum ( MetaBlockingTimeLimit.class )
                .orNone ()
                .maximumBlockingTimeMilliseconds ();

        final NonBlocking<Region> region =
            this.value ().await ( maximum_blocking_time_milliseconds );

        return region;
    }
}
