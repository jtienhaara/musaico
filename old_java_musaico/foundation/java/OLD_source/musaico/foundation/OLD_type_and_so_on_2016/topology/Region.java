package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.order.Comparison;

import musaico.foundation.typing.Constant;
import musaico.foundation.typing.Expression;
import musaico.foundation.typing.Identity;
import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.RootNamespace;
import musaico.foundation.typing.StandardRootNamespace;
import musaico.foundation.typing.SymbolMustBeUnique;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.StandardType;

import musaico.foundation.typing.typeclass.TypeClassInstance;

import musaico.foundation.value.Countable;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A set of points, including a start point, an end point,
 * and zero or more points in between.
 * </p>
 *
 * <p>
 * The Region class is a generic topological construct representing
 * an area or volume of points.  However it was originally designed
 * to be a flexible representation of the
 * regions of different data structures, much like
 * a pointer, an integer offset, and a <code> size_t </code>
 * integer representation of size are collectively used to
 * describe the regions of many different types of data
 * structures in C.  For example, an integer Region
 * might represent the indices of an array; or a
 * 4-dimensional real valued Region might represent the vertices
 * of objects placed in a 4-D Region; or a time Region
 * might be used to represent the second and nanosecond points
 * of digital audio or video; and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Region must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * In Java every Region must be Serializable in order to
 * play nicely over RMI.  However be warned that the points and
 * measures in a Region (including the endpoints) might not
 * be Serializable.
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
public class Region<POINT extends Object, MEASURE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Region.class );


    /** The "none" Region. */
    public static final Region<Object, Object> NONE =
        new Region<Object, Object> (); // No region constructor.

    // The Namespace in which topological types (including Region.TYPE)
    // reside.  Duplicated as TopologyTypeClass.NAMESPACE.
    // Package private.
    static final RootNamespace NAMESPACE =
        new StandardRootNamespace ( Namespace.ROOT,
                                    "topology",
                                    new SymbolTable (),
                                    new StandardMetadata () );

    /** The Type describing all Regions for all possible Topologies. */
    @SuppressWarnings("unchecked") // Class<Region<?, ?>> cast.
    public static final Type<Region<?, ?>> TYPE =
        new StandardType<Region<?, ?>> ( Region.NAMESPACE,
                                         Kind.ROOT,
                                         "region",
                                         (Class<Region<?, ?>>) Region.NONE.getClass (),
                                         Region.NONE,
                                         new SymbolTable (),
                                         new StandardMetadata () );

    static
    {
        try
        {
            Region.NAMESPACE.add ( Region.TYPE );
        }
        catch ( SymbolMustBeUnique.Violation violation )
        {
            // If this is the main thread, then try to kill the
            // application.  The Topology code is broken,
            // and we don't know how badly, so just blow up
            // real good.
            throw new UncheckedViolation ( violation );
        }
    }


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The topology describing the points and measures of this Region
    // and the operations that can be performed on them.
    private final Topology<POINT, MEASURE> topology;

    // The endpoints of this Region: start, end, start, end, ...
    private final Points<POINT, MEASURE> bounds;

    // The size of this Region, the sum of the sizes of the
    // sub-regions defined by the endpoints.
    private final Expression<MEASURE> size;


    /**
     * <p>
     * Creates a new Region in the specified topology, covering
     * the point(s) in the specified bounds.
     * </p>
     *
     * @param topology The Topology describing the points and measures
     *                 used by this Region.  Must not be null.
     *
     * @param bounds Zero or more POINTs defining the start, end
     *               and holes in this Region.  For example,
     *               0 bounds describes an empty Region.  1 bound
     *               describes a Region starting at a specific POINT
     *               and stretching to the maximum POINT for the
     *               Topology.  A Region with a single pair of POINTs
     *               has no holes.  A Region with 2 or more pairs of
     *               POINTs has holes in between the (start, end)
     *               pairs.  For example, an Integer Region with bounds
     *               (0, 2, 10, 12, 20, 22) is made up of contiguous
     *               sub-regions (0, 2), (10, 12), (20, 22), and
     *               holes (3, 9) and (13, 19).  Must not be null.
     */
    public Region (
                   Topology<POINT, MEASURE> topology,
                   Points<POINT, MEASURE> bounds
                   )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology, bounds );

        this.topology = topology;

        final Type<MEASURE> measure_type = topology.measureType ();
        Expression<MEASURE> size_so_far =
            new Expression<MEASURE> ( "size",
                                new Identity<MEASURE> ( "no_size_yet",
                                                        measure_type,
                                                        measure_type ),
                                measure_type.instance (
                                                       measure_type.none ()
                                                       ) );

        final Type<POINT> point_type = topology.pointType ();
        final TypedValueBuilder<POINT> complete_bounds_builder =
            new TypedValueBuilder<POINT> ( point_type );
        POINT start = null;
        for ( POINT endpoint : bounds )
        {
            complete_bounds_builder.add ( endpoint );

            if ( start == null )
            {
                start = endpoint;
                continue;
            }

            start = null;
        }

        if ( start != null )
        {
            // Odd number of bounds.
            // Add the topology's MAX POINT to the end.
            final TypeClassInstance point_instance = topology.point ();
            final Constant<POINT> max = (Constant<POINT>)
                point_instance.requiredSymbol ( TopologyTypeClass.Point.MAX );
            complete_bounds_builder.add ( max );
        }

        final Countable<POINT> complete_bounds = (Countable<POINT>)
            complete_bounds_builder.build ();
        this.bounds = new Points<POINT, MEASURE> ( point_type,
                                                   complete_bounds );

        final Operation2<POINT, POINT, MEASURE> subtract =
            topology.operations ().subtractPointMinusPoint ();
        final Operation2<MEASURE, MEASURE, MEASURE> add =
            topology.operations ().addMeasurePlusMeasure ();

        start = null;
        for ( POINT endpoint : complete_bounds )
        {
            if ( start == null )
            {
                start = endpoint;
                continue;
            }

            final Constant<POINT> endpoint_term =
                new Constant<POINT> (
                    "end",
                    point_type,
                    new One<POINT> ( point_type.valueClass (),
                                     endpoint ) );

            final Constant<POINT> start_term =
                new Constant<POINT> (
                    "start",
                    point_type,
                    new One<POINT> ( point_type.valueClass (),
                                     start ) );

            final Expression<MEASURE> sub_region_size =
                new Expression<MEASURE> ( "sub_region_size",
                                          subtract,
                                          endpoint_term,
                                          start_term );
            size_so_far =
                new Expression<MEASURE> ( "size",
                                          add,
                                          size_so_far,
                                          sub_region_size );

            start = null;
        }

        this.size = size_so_far;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Special constructor for Region.NONE.
     * </p>
     */
    @SuppressWarnings("unchecked") // Cast various plain Object-related
        // value to POINT-related values (Type<POINT>, Term<POINT>).
    protected Region ()
    {
        this.topology = null;
        Namespace.NONE.toString ();
        this.size =
            new Expression<POINT> ( "size",
                                    new Identity<POINT> ( "no_size",
                                                          (Type<POINT>) Type.NONE,
                                                          (Type<POINT>) Type.NONE ),
                                    (Term<POINT>) Type.NONE.instance (
                                                                      Type.NONE.none ()
                                                                      ) );

        this.bounds = null;
        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @return One or more pairs of endpoints defining the boundaries
     *         of this Region with holes in between pairs, or No endpoints
     *         if this Region is empty.  For example, the
     *         endpoints of an integer array index Region
     *         including points { 0, 1 2 and 3 } might { 0, 3 }.
     *         Or the index Region including points { 0, 1, 2, 7, 8, 9 }
     *         might be { 0, 2, 7, 9 } defining the two sub-regions
     *         { 0, 2 } and { 7, 9 } and the hole in between.
     *         Or the empty index Region { } would have endpoints { }.
     *         And so on.  Even if this Region is infinite, its bounds are
     *         finite and Countable, so that the first and last endpoint
     *         and the number of endpoints can be easily retrieved.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<Obj> - Type<POINT>
        // for Region.NONE.
    public final Points<POINT, MEASURE> bounds ()
        throws ReturnNeverNull.Violation
    {
        if ( this.bounds == null )
        {
            // Region.NONE.
            return new Points<POINT, MEASURE> ( (Type<POINT>) Type.NONE );
        }
        else
        {
            return this.bounds;
        }
    }


    /**
     * <p>
     * Returns true if this Region contains the specified POINT.
     * </p>
     *
     * <p>
     * If the specified point is null then false is always returned.
     * </p>
     *
     * @param point The point which might or might not be
     *              contained in this Region.  Must not be null.
     *
     * @return True if the specified point is contained in this Region;
     *         false if the specified point is not contained in
     *         this Region, or if the specified point is null.
     */
    public boolean contains (
                             POINT point
                             )
        throws ParametersMustNotBeNull.Violation
    {
        // Just a stupid linear search for now, out of laziness / rushing.
        POINT start = null;
        for ( POINT endpoint : this.bounds () )
        {
            if ( start == null )
            {
                start = endpoint;
                continue;
            }

            final Comparison start_comparison =
                this.topology ().pointOrder ().compareValues ( point, start );
            if ( start_comparison.isLeftLessThanRight () )
            {
                // point < start.
                return false;
            }
            else if ( start_comparison.isEqual () )
            {
                // point = start.
                return true;
            }

            // start < point.
            final Comparison end_comparison =
                this.topology ().pointOrder ().compareValues ( point,
                                                               endpoint );
            if ( end_comparison.isLeftLessThanOrEqualToRight () )
            {
                // point < end.
                return true;
            }

            // Keep searching.

            start = null;
        }

        // point > last end.
        return false;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( ! ( object instanceof Region ) )
        {
            return false;
        }

        final Region<?, ?> that = (Region<?, ?>) object;
        if ( ! this.topology ().equals ( that.topology () ) )
        {
            return false;
        }
        else if ( ! this.bounds ().equals ( that.bounds () ) )
        {
            return false;
        }
        else if ( ! this.size ().equals ( that.size () ) )
        {
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * <p>
     * Creates a new RegionExpression starting from this Region.
     * </p>
     *
     * @return A new RegionExpression for this region.
     *         Can be an EmptyRegionExpression if this Region
     *         is a NoRegion.  Never null.
     */
    /* !!!
    public abstract RegionExpression<POINT, MEASURE> expression ()
        throws ReturnNeverNull.Violation;
        !!! */


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.topology ().hashCode ()
            + 17 * this.bounds ().hashCode ()
            + 31 * this.size.hashCode ();
    }


    /**
     * <p>
     * Creates a new Expression starting from the specified
     * point, operating only on the points in this Region.
     * </p>
     *
     * @see musaico.foundation.topology.Topology#point(java.lang.Object)
     *
     * @param point The initial input to the Expression to be built.
     *              Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point as the initial input.
     *         Never null.
     */
    /* !!!
    public abstract PointExpression point (
    POINT point
    )
        throws ParametersMustNotBeNull.Violation,
        ReturnNeverNull.Violation;
        !!! */


    /**
     * <p>
     * Creates a new expression starting from the specified
     * point, operating only on the points in this Region.
     * </p>
     *
     * @see musaico.foundation.topology.Topology#point(musaico.foundation.value.Value)
     *
     * @param point_value The initial input Value to the Expression
     *                    to be built.  Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point(s) Value as the initial input.
     *         Never null.
     */
    /* !!!
    public abstract PointExpression point (
    Value<POINT> point_value
    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
               !!! */


    /**
     * <p>
     * Creates a new expression starting from the specified
     * point, operating only on the points in this Region.
     * </p>
     *
     * @see musaico.foundation.topology.Topology#point(musaico.foundation.typing.Term)
     *
     * @param point_term The initial input Term of the Expression to be built.
     *                   Must not be null.
     *
     * @return A newly created PointExpression, with the
     *         specified point(s) Term as the initial input.
     *         Never null.
     */
    /* !!!
    public abstract PointExpression point (
    Term<POINT> point_term
    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
               !!! */


    /**
     * @return All the points of this Region.  Never null.
     */
    public !!!NonBlocking<POINT> points ()
    {
        final int num_endpoints = this.bounds ().length ();
        if ( num_endpoints < 2 )
        {
            // Empty region.
            !!!;
        }
        else
        {
            !!!;
        }
    }


    /**
     * <p>
     * Returns a default Searcher, which can be used to search for
     * point(s) matching the specified criteria within the
     * specified RegionExpression.
     * </p>
     *
     * <p>
     * Many Region implementations will return a LinearSearcher for
     * the general case, but a more specialized searcher for certain
     * criteria.
     * </p>
     *
     * <p>
     * For example, many Region implementations will return the
     * BinarySearcher strategy whenever a single OrderedFilter
     * criterion is passed in.
     * </p>
     *
     * <p>
     * However a Region which is specific to a data source, such
     * as a Region covering rows in a database table, might
     * convert the specified criteria into parts of an SQL query,
     * and return a Searcher which is capable of executing that
     * SQL query against some database or other.
     * </p>
     *
     * <p>
     * Or a Region which covers large, multi-dimensional
     * data might return a more loosey goosey search strategy,
     * such as a stochastic local search implementation.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * This is only the default Searcher algorithm for this Region.
     * Context-dependent search strategies and optimized search
     * strategies should be implemented in RegionExpression.search(),
     * depending on the Region and on the criteria passed in.
     * For example, a Filter might specify "max satisfied
     * criteria", which might induce a change from the regular
     * (say BinarySearcher) strategy to a more loosey goosey
     * strategy (such as a stochastic local search).
     * </p>
     *
     * @return The default search strategy for this Region for the
     *         specified criteria within the specified RegionExpression.
     *         Never null.
     */
    /* !!!
        Part of the Region signature:
        "find";
        Operation2<Region, Filter<?, ?>, ?>
        {
            public Value<?> evaluate ( Value<Region>,
                                       Value<Filter<?, ?>> );
        }
        "findAll";
        Operation2<Region, Filter<?, ?>, Region>
        {
            public Value<Region> evaluate ( Value<Region>,
                                            Value<Filter<?, ?>> );
        }
    @SuppressWarnings("unchecked") // Heap pollution<generic varargs>
    public abstract Searcher<?, ?> searcher (
                                             RegionExpression region_expression,
                                             Filter<?> ... criteria
                                             );
                                                 !!! */


    /**
     * @return The size of this Region.  For example, an integer
     *         array index Region with indices { 0, 1, 2, 3 } might
     *         return an integer size 4.  Never null.
     */
    public final Expression<MEASURE> size ()
        throws ReturnNeverNull.Violation
    {
        return this.size;
    }


    /**
     * @return The topology describing the points and measures of
     *         this Region.  For example, an integer index Topology,
     *         or a String field / column name space, or a date/time
     *         Topology, or a 4-dimensional Topology,
     *         and so on.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Top<Obj> - Top<P,M> for Region.NONE.
    public final Topology<POINT, MEASURE> topology ()
    {
        if ( this.topology == null )
        {
            // Region.NONE.
            return (Topology<POINT, MEASURE>) Topology.NONE;
        }
        else
        {
            return this.topology;
        }
    }
}
