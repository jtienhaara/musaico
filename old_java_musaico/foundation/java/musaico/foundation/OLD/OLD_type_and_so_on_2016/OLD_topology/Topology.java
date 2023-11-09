package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;


/**
 * <p>
 * A somewhat mathematical-ish expression builder for constructing
 * expressions such as "X + Y * Z", given non-numeric objects
 * such as time.
 * </p>
 *
 * <p>
 * A Topology has has one POINT Type, for absolute points in the
 * topological space, one MEASURE Type, for distances or
 * intervals between points, and one Region Type, for sets of points.
 * The POINT and MEASURE Types for a topological space can be
 * the same.  For example, a Topology covering Integer POINTs and
 * Integer MEASUREs can be created.
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
public class Topology<POINT extends Serializable, MEASURE extends Serializable>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Topology.class );


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The signature of the Type representing relative measures
    // in this Topology, providing the add, subtract and so on
    // operations for a Type such as a Long Type, or a TimeInterval Type.
    private final MeasureSignature<POINT, MEASURE> measureSignature;

    // The signature of the Type representing absolute points
    // in this Topology, providing the add, subtract and so on
    // operations for a Type such as a Long Type, or a Time Type.
    private final PointSignature<POINT, MEASURE> pointSignature;

    // The signature of the Type representing sets of points
    // in this Topology, providing the intersection, union and so on
    // operations for a Type such as a Set<Long> Type, or a Time[] Type.
    private final RegionSignature<POINT, MEASURE> regionSignature;


    /**
     * <p>
     * Creates a new Topology for the specified point
     * and measure signatures.
     * </p>
     *
     * @param point_signature The signature of the Type representing
     *                        absolute points in this Topology,
     *                        providing the add, subtract and so on
     *                        operations for a Type such as a Long Type,
     *                        or a Time Type.
     *                        Must not be null.
     *
     * @param measure_type The signature of the Type representing
     *                     relative measures in this Topology,
     *                     providing the add, subtract and so on
     *                     operations for a Type such as a Long Type,
     *                     or a TimeInterval Type.
     *                     Must not be null.
     *
     * @param region_signature The signature of the Type representing
     *                         sets of points in this Topology,
     *                         providing the intersection, union and so on
     *                         operations for a Type such as a Set<Long> Type,
     *                         or a Time[] Type.
     *                         Must not be null.
     */
    public Topology (
                     PointSignature<POINT, MEASURE> point_signature,
                     MeasureSignature<POINT, MEASURE> measure_signature,
                     RegionSignature<POINT, MEASURE> region_signature
                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point_signature,
                               measure_signature,
                               region_signature );

        this.pointSignature = point_signature;
        this.measureSignature = measure_signature;
        this.regionSignature = region_signature;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            // Any Topology != null.
            return false;
        }
        else if ( object == this )
        {
            // Any Topology == itself.
            return true;
        }
        else if ( ! ( object instanceof Topology ) )
        {
            // Any Topology != any object of a different class.
            return false;
        }

        Topology<?, ?, ?> that = (Topology<?, ?, ?>) object;
        if ( ! this.pointSignature.equals ( that.pointSignature () )
             || ! this.measureSignature.equals ( that.measureSignature () )
             || ! this.regionSignature.equals ( that.regionSignature () ) )
        {
            // Topology over P1, M1, R1 != Topology over P2, M2, R2.
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.pointSignature.hashCode ()
            + this.measureSignature.hashCode ()
            + this.regionSignature.hashCode ();
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * measure.
     * </p>
     *
     * @param measure The initial input to the Expression to be built.
     *                Must not be null.
     *
     * @return A newly created MeasureExpression, with the
     *         specified measure as the initial input.
     *         Never null.
     */
    public MeasureExpression<POINT, MEASURE> measure (
                                                      MEASURE measure
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               measure );

        final Term<MEASURE> measure_term = this.measureTerm ( measure );
        return this.measure ( measure_term );
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * measure.
     * </p>
     *
     * @param measure The initial input Value to the Expression to be built.
     *                Must not be null.
     *
     * @return A newly created MeasureExpression, with the
     *         specified measure(s) Value as the initial input.
     *         Never null.
     */
    public MeasureExpression<POINT, MEASURE> measure (
                                                      Value<MEASURE> measure_value
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               measure_value );

        final Term<MEASURE> measure_term = this.measureTerm ( measure_value );
        return this.measure ( measure_term );
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * measure.
     * </p>
     *
     * @param measure_term The initial input Term to the Expression
     *                     to be built.  Must not be null.
     *
     * @return A newly created MeasureExpression, with the
     *         specified measure(s) Term as the initial input.
     *         Never null.
     */
    public MeasureExpression<POINT, MEASURE> measure (
                                                      Term<MEASURE> measure_term
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               measure_term );

        return new MeasureExpression<POINT, MEASURE> ( this,
                                                       measure_term );
    }


    /**
     * @return The signature of measures in this topological space,
     *         including the Type as well as the add, subtract
     *         and so on operations.  Never null.
     */
    public final MeasureSignature<POINT, MEASURE> measureSignature ()
        throws ReturnNeverNull.Violation
    {
        return this.measureSignature;
    }


    /**
     * <p>
     * Creates a new Term out of the specified measure.
     * </p>
     *
     * @param measure The measure to turn into a Term.  Must not be null.
     *
     * @return A newly created Term representing the specified measure.
     *         Never null.
     */
    public final Term<MEASURE> measureTerm (
                                            MEASURE measure
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               measure );

        return this.measureTerm (
                                 this.measureValue ( measure ) );
    }


    /**
     * <p>
     * Creates a new Term out of the specified measure Value.
     * </p>
     *
     * @param measure_value The measure(s) Value to turn into a Term.
     *                      Must not be null.
     *
     * @return A newly created Term representing the specified measure(s).
     *         Never null.
     */
    public final Term<MEASURE> measureTerm (
                                            Value<MEASURE> measure_value
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               measure_value );

        final Term<MEASURE> measure_term =
            this.measureSignature.type ().instance ( measure_value );
        return measure_term;
    }


    /**
     * <p>
     * Creates a new Value out of the specified measure.
     * </p>
     *
     * @param measure The measure to turn into a Value.  Must not be null.
     *
     * @return A newly created Value representing the specified measure.
     *         Never null.
     */
    public final Value<MEASURE> measureValue (
                                              MEASURE measure
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               measure );

        final Value<MEASURE> measure_value =
            new One<MEASURE> ( this.measureSignature.type ().valueClass (),
                               measure );
        return measure_value;
    }


    public abstract RegionExpression<POINT, MEASURE> newRegion (
                                                                POINT start,
                                                                POINT end
                                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    public abstract RegionExpression<POINT, MEASURE> newRegionOverPoints (
                                                                          Iterable<POINT> points
                                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    public abstract RegionExpression<POINT, MEASURE> newRegionOfSize (
                                                                      POINT start,
                                                                      MEASURE size
                                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    public abstract RegionExpression<POINT, MEASURE> newSparseRegion (
                                                                      Iterable<Region<POINT, MEASURE>> sub_regions
                                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new Expression starting from the specified
     * point.
     * </p>
     *
     * @param point The initial input to the Expression to be built.
     *              Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point as the initial input.
     *         Never null.
     */
    public PointExpression<POINT, MEASURE> point (
                                                  POINT point
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point );

        final Term<POINT> point_term = this.pointTerm ( point );
        return this.point ( point_term );
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * point.
     * </p>
     *
     * @param point_value The initial input Value to the Expression
     *                    to be built.  Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point(s) Value as the initial input.
     *         Never null.
     */
    public PointExpression<POINT, MEASURE> point (
                                                  Value<POINT> point_value
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point_value );

        final Term<POINT> point_term = this.pointTerm ( point_value );
        return this.point ( point_term );
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * point.
     * </p>
     *
     * @param point_term The initial input Term of the Expression to be built.
     *                   Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point(s) Term as the initial input.
     *         Never null.
     */
    public PointExpression<POINT, MEASURE> point (
                                                  Term<POINT> point_term
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point_term );

        return new PointExpression<POINT, MEASURE> ( this,
                                                     point_term );
    }


    /**
     * @return The signature of points in this topological space,
     *         including the Type as well as the add, subtract
     *         and so on operations.  Never null.
     */
    public final PointSignature<POINT, MEASURE> pointSignature ()
        throws ReturnNeverNull.Violation
    {
        return this.pointSignature;
    }


    /**
     * <p>
     * Creates a new Term out of the specified point.
     * </p>
     *
     * @param point The point to turn into a Term.  Must not be null.
     *
     * @return A newly created Term representing the specified point.
     *         Never null.
     */
    public final Term<POINT> pointTerm (
                                        POINT point
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point );

        return this.pointTerm ( this.pointValue ( point ) );
    }


    /**
     * <p>
     * Creates a new Term out of the specified point Value.
     * </p>
     *
     * @param point_value The point(s) Value to turn into a Term.
     *                    Must not be null.
     *
     * @return A newly created Term representing the specified point(s).
     *         Never null.
     */
    public final Term<POINT> pointTerm (
                                        Value<POINT> point_value
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point_value );

        final Term<POINT> point_term =
            this.pointSignature.type ().instance ( point_value );
        return point_term;
    }


    /**
     * <p>
     * Creates a new Value out of the specified point.
     * </p>
     *
     * @param point The point to turn into a Value.  Must not be null.
     *
     * @return A newly created Value representing the specified point.
     *         Never null.
     */
    public final Value<POINT> pointValue (
                                          POINT point
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               point );

        final Value<POINT> point_value =
            new One<POINT> ( this.pointSignature.type ().valueClass (),
                             point );
        return point_value;
    }


    /**
     * <p>
     * Creates a new Expression starting from the specified
     * region.
     * </p>
     *
     * @param region The initial input to the Expression to be built.
     *               Must not be null.
     *
     * @return A newly created RegionExpression, with the
     *         specified region as the initial input.
     *         Never null.
     */
    public RegionExpression<POINT, MEASURE> region (
                                                    Region<POINT, MEASURE> region
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        final Term<Region<POINT, MEASURE>> region_term =
            this.regionTerm ( region );
        return this.region ( region_term );
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * region.
     * </p>
     *
     * @param region_value The initial input Value to the Expression
     *                     to be built.  Must not be null.
     *
     * @return A newly created RegionExpression, with the
     *         specified region(s) Value as the initial input.
     *         Never null.
     */
    public RegionExpression<POINT, MEASURE> region (
                                                    Value<Region<POINT, MEASURE>> region_value
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region_value );

        final Term<Region<POINT, MEASURE>> region_term =
            this.regionTerm ( region_value );
        return this.region ( region_term );
    }


    /**
     * <p>
     * Creates a new expression starting from the specified
     * region.
     * </p>
     *
     * @param region_term The initial input Term of the Expression to be built.
     *                    Must not be null.
     *
     * @return A newly created RegiontExpression, with the
     *         specified region(s) Term as the initial input.
     *         Never null.
     */
    public RegionExpression<POINT, MEASURE> region (
                                                    Term<Region<POINT, MEASURE>> region_term
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region_term );

        return new RegionExpression<POINT, MEASURE> ( this,
                                                      region_term );
    }


    /**
     * @return The signature of regions in this topological space,
     *         including the Type as well as the add, subtract
     *         and so on operations.  Never null.
     */
    public final RegionSignature<POINT, MEASURE> regionSignature ()
        throws ReturnNeverNull.Violation
    {
        return this.regionSignature;
    }


    /**
     * <p>
     * Creates a new Term out of the specified region.
     * </p>
     *
     * @param region The region to turn into a Term.  Must not be null.
     *
     * @return A newly created Term representing the specified region.
     *         Never null.
     */
    public final Term<Region<POINT, MEASURE>> regionTerm (
                                                          Region<POINT, MEASURE> region
                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        return this.regionTerm ( this.regionValue ( region ) );
    }


    /**
     * <p>
     * Creates a new Term out of the specified region Value.
     * </p>
     *
     * @param region_value The region(s) Value to turn into a Term.
     *                     Must not be null.
     *
     * @return A newly created Term representing the specified region(s).
     *         Never null.
     */
    public final Term<Region<POINT, MEASURE>> regionTerm (
                                                          Value<Region<POINT, MEASURE>> region_value
                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region_value );

        final Term<Region<POINT, MEASURE>> region_term =
            this.regionSignature.type ().instance ( region_value );
        return region_term;
    }


    /**
     * <p>
     * Creates a new Value out of the specified region.
     * </p>
     *
     * @param region The region to turn into a Value.  Must not be null.
     *
     * @return A newly created Value representing the specified region.
     *         Never null.
     */
    public final Value<Region<POINT, MEASURE>> regionValue (
                                                            Region<POINT, MEASURE> region
                                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        final Value<Region<POINT, MEASURE>> region_value =
            new One<Region<POINT, MEASURE>> ( this.regionSignature.type ().valueClass (),
                                              region );
        return region_value;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.getClass ().getSimpleName ()
            + " over { "
            + this.pointSignature
            + ", "
            + this.measureSignature
            + ", "
            + this.regionSignature
            + " }";
    }
}
