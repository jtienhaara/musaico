package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.order.Order;


/**
 * <p>
 * The space in which a Region, point, measure, and so on resides.
 * </p>
 *
 * <p>
 * For example, Regions, points and measures describing arrays
 * might be described with non-negative integers, whereas the Regions,
 * points and measures describing a 3-dimensional volume might
 * be described with 3-tuples doube[] points in space.
 * </p>
 *
 *
 * <p>
 * In Java, every Space must be Serializable in order to play nicely
 * over RMI.
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
public interface Space<POINT extends Object, MEASURE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No space at all.  There are no points in this Space, so
     *  every Region is empty. */
    public static final Space NONE = new NoSpace ();


    public abstract Region<POINT, MEASURE> all ()
        throws ReturnNeverNull.Violation;

    public abstract Region<POINT, MEASURE> empty ()
        throws ReturnNeverNull.Violation;

    // Every Space must implement
    // java.lang.Object#equals(java.lang.Object).

    public abstract PointExpression<POINT, MEASURE> expr (
                                                          POINT point
                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract RegionExpression<POINT, MEASURE> expr (
                                                           Region<POINT, MEASURE> region
                                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract MeasureExpression<POINT, MEASURE> expr (
                                                            MEASURE measure
                                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract PointExpression<POINT, MEASURE> fromIndex (
                                                               long index
                                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    public abstract MeasureExpression<POINT, MEASURE> fromLength (
                                                                  long length
                                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Space must implement java.lang.Object#hashCode().


    public abstract long toIndex (
                                  POINT point
                                  );

    public abstract long toLength (
                                   MEASURE measure
                                   );

    public abstract MEASURE unit ()
        throws ReturnNeverNull.Violation;
}
