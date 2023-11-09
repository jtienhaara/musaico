!!! NEIGHBOURHOOD!!!;
package musaico.foundation.topology;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.order.NoOrder;
import musaico.foundation.order.Order;

import musaico.foundation.typing.ConstantTermID;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.Operation4;
import musaico.foundation.typing.OperationID;
import musaico.foundation.typing.OperationType1;
import musaico.foundation.typing.OperationType2;
import musaico.foundation.typing.OperationType4;
import musaico.foundation.typing.RootNamespace;
import musaico.foundation.typing.StandardRootNamespace;
import musaico.foundation.typing.StandardType;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypeID;
import musaico.foundation.typing.UnknownType;
import musaico.foundation.typing.Visibility;

import musaico.foundation.typing.typeclass.TypeClass;

import musaico.foundation.value.One;
import musaico.foundation.value.Value;


/**
 * <p>
 * A TypeClass describing Types which are somewhat mathematical-ish,
 * providing "add" and "subtract" and so on.
 * </p>
 *
 * <p>
 * TopologyTypeClass is instantiated by a Type with one POINT Type,
 * for absolute points in the topological space, and one MEASURE Type,
 * for distances or intervals between points.  The POINT and MEASURE
 * Types for a topological space can be the same.  For example,
 * a TopologyTypeClass covering Integer POINTs and Integer MEASUREs
 * can be created.
 * </p>
 *
 * <p>
 * In order to instantiate TopologyTypeClass, a Type must provide:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b> <code> TopologyTypeClass.BIG_DECIMAL_TYPE_ID </code> : </b>
 *     A BigDecimal Type is required for certain operations on MEASUREs.
 *     The default can be used
 *     (<code> TopologyTypeClass.BIG_DECIMAL_TYPE </code>), or a Type
 *     from some Namespace library or other.
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.LONG_TYPE_ID </code> : </b>
 *     A Long Type is required for certain operations on Regions.
 *     The default can be used
 *     (<code> TopologyTypeClass.LONG_TYPE </code>), or a Type
 *     from some Namespace library or other.
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.ADD </code> </b> : </li>
 *     Given a point in the topological space, adds some amount to it
 *     to arrive at a new point.
 *     <br> </br>
 *     POINT + MEASURE = POINT.
 *     <br> </br>
 *     <code> add ( POINT, MEASURE ) : POINT </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.ADD, </code> </b> : </li>
 *     Given some measure in the topological space (distance or size),
 *     adds another measure to it to arrive at a larger measure.
 *     <br> </br>
 *     MEASURE + MEASURE = MEASURE
 *     <br> </br>
 *     <code> add ( MEASURE, MEASURE ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.DIFFERENCE </code> </b> : </li>
 *     Given two Regions, returns the Region containing only the
 *     points which are in either the first region or the second region,
 *     but not both.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region xor Region = Region
 *     <br> </br>
 *     <code> difference ( Region, Region ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.DIVIDE </code> </b> : </li>
 *     Given a measure (distance or size) in the topological
 *     space, scales it up or down by a BigDecimal divisor.
 *     <br> </br>
 *     MEASURE / BigDecimal = MEASURE
 *     <br> </br>
 *     <code> divide ( MEASURE, BigDecimal ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.EXCLUDE </code> </b> : </li>
 *     Given two Regions, returns the Region comprising all points
 *     from the first Region which are not contained in the second Region.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region - Region = Region
 *     <br> </br>
 *     <code> exclude ( Region, Region ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.GROUP </code> </b> : </li>
 *     Divides each single Region up into groups, each Region
 *     containing only POINTs that are equal, given a specific Order
 *     to compare by.  For example, with an Order which
 *     considers all integers less than 5 to be equal, all integers
 *     5-10 to be equal, and all integers greater than 5 to be equal,
 *     then grouping the Region { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 }
 *     would result in three output Regions: { 3, 4 }, { 5, 6, 7, 8, 9, 10 },
 *     and { 11, 12 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     group Region by Order = Region []
 *     <br> </br>
 *     <code> group ( Order, Region ) : Region [] </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.INTERSECTION </code> </b> : </li>
 *     Given two Regions, returns the Region which contains only
 *     the POINTs common to the two Regions.  A default implementation
 *     is provided, so a given Topology can fall back on the default.
 *     <br> </br>
 *     Region &amp; Region = Region
 *     <br> </br>
 *     <code> intersection ( Region, Region ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.INVERT </code> </b> : </li>
 *     Returns the Region covering all of the topological space
 *     that is NOT occupied by the specified Region.
 *     For example, if the specified Region is integer POINTs { 1, 2, 3 }
 *     and its Topology covers the integers from min POINT 0 to max POINT 100,
 *     then the inverted Region would be: { 0, 4, 5, ..., 99, 100 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     ! Region = Region
 *     <br> </br>
 *     <code> invert ( Region, Region ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.MID </code> </b> : </li>
 *     Returns one or more POINTs that comprise the middle point(s)
 *     of the Region.  For example, the mid-point of integer Region
 *     { 1, 2, 3 } is the point 2; and the mid-points of integer
 *     Region { 1, 2, 3, 4 } are 2 and 3; and the mid-points of
 *     integer region { 1, 1, 1, 2, 2, 3 } are 1 and 2.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region mid = POINT
 *     <br> </br>
 *     <code> mid ( Region ) : POINT </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.MODULO </code> </b> : </li>
 *     Given a measure (distance or size) in the topological space,
 *     modulo divides it by another measure to arrive at a new
 *     measure which is always less than or equal to the first
 *     parameter, and less than the second parameter.
 *     <br> </br>
 *     MEASURE % MEASURE = MEASURE
 *     <br> </br>
 *     <code> modulo ( MEASURE, MEASURE ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.MULTIPLY </code> </b> : </li>
 *     Given a measure (distance or size) in the topological space,
 *     scales it up or down by a BigDecimal multiplier.
 *     <br> </br>
 *     MEASURE * BigDecimal = MEASURE
 *     <br> </br>
 *     <code> multiply ( MEASURE, BigDecimal ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.NEXT </code> </b> : </li>
 *     Given a point in the topological space, returns its greater
 *     neighbour.  Equivalent to adding the unit measure to the point.
 *     <br> </br>
 *     POINT ++
 *     <br> </br>
 *     <code> next ( POINT ) : POINT </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.NEXT </code> </b> : </li>
 *     Given a measure (distance or size) in the topological space,
 *     returns the next larger measure.  Equivalent to adding the
 *     unit measure to the parameter.
 *     <br> </br>
 *     MEASURE ++
 *     <br> </br>
 *     <code> next ( MEASURE ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.PREVIOUS </code> </b> : </li>
 *     Given a point in the topological space, returns its lesser
 *     neighbour.  Equivalent to subtracting the unit measure
 *     from the point.
 *     <br> </br>
 *     POINT --
 *     <br> </br>
 *     <code> previous ( POINT ) : POINT </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.PREVIOUS </code> </b> : </li>
 *     Given a measure (distance or size) in the topological space,
 *     returns the next smaller measure.  Equivalent to subtracting the
 *     unit measure from the parameter.
 *     <br> </br>
 *     MEASURE --
 *     <br> </br>
 *     <code> previous ( MEASURE ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.RATIO </code> </b> : </li>
 *     Given two measures (distances or sizes) in the topological space,
 *     returns the BigDecimal ratio between them.
 *     <br> </br>
 *     MEASURE / MEASURE = BigDecimal
 *     <br> </br>
 *     <code> ratio ( MEASURE, MEASURE ) : BigDecimal </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.REGION </code> </b> : </li>
 *     Given two points in the topological space, returns a region
 *     comprising them as endpoints, and all the points in between.
 *     <br> </br>
 *     POINT .. POINT = REGION
 *     <br> </br>
 *     <code> region ( POINT, POINT ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.REVERSE </code> </b> : </li>
 *     Returns the points sorted in reverse order.
 *     For example, the reverse of integer points { 1, 2, 3 } would
 *     be points { 3, 2, 1 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     POINTs reverse = Blocking POINTS
 *     <br> </br>
 *     <code> reverse ( POINTs ) : Blocking POINTs </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.SCALE </code> </b> : </li>
 *     Eliminates points from a Region to make it fit a specific
 *     MEASURE size.  For example, an integer region of size 3 with
 *     points { 0, 1, 2 } might be scaled up to size 5, resulting in
 *     a new region { 0, 1, 2, 3, 4 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region * MEASURE = Region
 *     <br> </br>
 *     <code> scale ( Region, MEASURE ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.SORT </code> </b> : </li>
 *     Returns the points sorted according to
 *     a specific Order.  For example, the integer points
 *     { 0, 1, 2, 3, 4 } might be sorted into the points ordered by
 *     descending even numbers first, followed by ascending odd numbers,
 *     resulting in { 4, 2, 0, 1, 3 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     POINTs sort by Order = Blocking POINTs
 *     <br> </br>
 *     <code> sort ( Order, POINTs ) : Blocking POINTs </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.SPLIT_AT </code> </b> : </li>
 *     Divides each region into sub-regions, each one ending at
 *     a specified POINT.  For example, the integer region { 0, 1, 2, 3, 4 }
 *     might be split at points 1 and 3, resulting in three regions:
 *     { 0, 1 }, { 2, 3 } and { 4 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region @ POINT = Region []
 *     <br> </br>
 *     <code> splitat ( Region, POINT ) : Region [] </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.SPLIT_BY </code> </b> : </li>
 *     Divides each region into sub-regions of a specific size.
 *     For example, the integer region { 0, 1, 2, 3, 4 } might be
 *     divided into regions of length 2, resulting in three regions:
 *     { 0, 1 }, { 2, 3 } and { 4 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region % MEASURE = Region []
 *     <br> </br>
 *     <code> splitby ( Region, MEASURE ) : Region [] </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.SPLIT_INTO </code> </b> : </li>
 *     Divides each region up into N equal sub-regions.  For example,
 *     the integer region { 0, 1, 2, 3, 4 } might be divided into
 *     2 regions, resulting in new regions { 0, 1, 2 } and { 3, 4 }.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region / long = Region []
 *     <br> </br>
 *     <code> splitinto ( Region, long ) : Region [] </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.SUB_REGION </code> </b> : </li>
 *     For each region, extracts the sub-region starting at a specific
 *     POINT (or the region start, if No point is provided), covering
 *     N POINTS, or spanning the POINTs up to and including the
 *     ( end + N )th point if N is less than or equal to 0, or
 *     spanning the POINTs up to and including the specified end point
 *     if No N is specified, or spanning the remaining POINTs if No N
 *     is specified and No end point is specified.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     subregion ( Region, No start, No N, No end ) = whole region as-is
 *     <br> </br>
 *     subregion ( Region, No start, +/0 N, No end ) = first N points
 *     <br> </br>
 *     subregion ( Region, No start, - N, No end ) = last N points
 *     <br> </br>
 *     subregion ( Region, start, No N, No end ) = start..regionEnd
 *     <br> </br>
 *     subregion ( Region, start, +/0 N, No end ) = start..start+N
 *     <br> </br>
 *     subregion ( Region, start, - N, No end ) = start..regionEnd-N
 *     <br> </br>
 *     subregion ( Region, start, No N, end ) = start..end
 *     <br> </br>
 *     subregion ( Region, start, +/0 N, end ) = start+N..end
 *     <br> </br>
 *     subregion ( Region, start, - N, end ) = start..end-N
 *     <br> </br>
 *     subregion ( Region, No start, No N, end ) = regionStart..end
 *     <br> </br>
 *     subregion ( Region, No start, +/0 N, end ) = regionStart+N..end
 *     <br> </br>
 *     subregion ( Region, No start, - N, end ) = regionStart..end-N
 *     <br> </br>
 *     Region sub POINT Long POINT = Region
 *     <br> </br>
 *     <code> subregion ( Region, POINT, Long, POINT ) : Region </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.SUBTRACT_MEASURE </code> </b> : </li>
 *     Given a point in the topological space, subtracts some amount
 *     from it to arrive at a new point.
 *     <br> </br>
 *     POINT - MEASURE = POINT
 *     <br> </br>
 *     <code> subtract ( POINT, MEASURE ) : POINT </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Point.SUBTRACT_POINT </code> </b> : </li>
 *     Given a point in the topological space, returns the distance
 *     from another point.
 *     <br> </br>
 *     POINT - POINT = MEASURE
 *     <br> </br>
 *     <code> subtract ( POINT, POINT ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.Measure.SUBTRACT </code> </b> : </li>
 *     Given a measure (distance or size) in the topological space,
 *     subtracts another measure to arrive at some new, smaller measure.
 *     <br> </br>
 *     MEASURE - MEASURE = MEASURE
 *     <br> </br>
 *     <code> subtract ( MEASURE, MEASURE ) : MEASURE </code>
 *   </li>
 *
 *   <li>
 *     <b> <code> TopologyTypeClass.RegionClass.UNION </code> </b> : </li>
 *     Given two Regions, returns the Region comprising all points
 *     contained by either or both of the input Regions.
 *     A default implementation is provided, so a given Topology
 *     can fall back on the default.
 *     <br> </br>
 *     Region | Region = Region
 *     <br> </br>
 *     <code> union ( Region, Region ) : Region </code>
 *   </li>
 * </ul>
 *
 *
 * <p>
 * In Java every TypeClass must be Serializable in order to play nicely
 * over RMI.  However be warned that instances of any given TypeClass
 * might contain non-Serializable Symbols.
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
public class TopologyTypeClass
    extends TypeClass
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;




    /** The Namespace in which topological types (such as Region.TYPE)
     *  reside. */
     // We have to define it in Region.java first, in order
     // to prevent NullPointerExceptions at startup as we create
     // the various singletons. However TopologyTypeClass.NAMESPACE
     // is the constant pretty much everyone should refer to. */
    public static final RootNamespace NAMESPACE = Region.NAMESPACE;

    /** The SymbolID for the Type describing POINTs
     *  in a Topology. */
    public static final TypeID POINT_TYPE_ID =
        new TypeID ( "point", Visibility.PUBLIC );

    /** The SymbolID for the Type describing MEASUREs
     *  in a Topology. */
    public static final TypeID MEASURE_TYPE_ID =
        new TypeID ( "measure", Visibility.PUBLIC );

    /** The UnknownType placeholder for a POINT Type.
     *  Used as a placeholder in Operation templates which take
     *  POINT inputs and/or return a POINT output. */
    public static final UnknownType POINT_TYPE =
        new UnknownType ( Namespace.NONE,
                          TopologyTypeClass.POINT_TYPE_ID.name () );

    /** The UnknownType placeholder for a MEASURE Type.
     *  Used as a placeholder in Operation templates which take
     *  MEASURE inputs and/or return a MEASURE output. */
    public static final UnknownType MEASURE_TYPE =
        new UnknownType ( Namespace.NONE,
                          TopologyTypeClass.MEASURE_TYPE_ID.name () );


    /** A BigDecimal Type (either a custom one, or the default
     *  TopologyTypeClass.BIG_DECIMAL_TYPE) is required in order
     *  to implement certain MEASURE operations. */
    public static final TypeID BIG_DECIMAL_TYPE_ID =
        new TypeID ( "BigDecimal" );

    /** BigDecimals are required in some topology operations.
     *  This is a bare-bones Type, nothing special.  A Type with
     *  the same ID is required to create a TopologyTypeClass
     *  instance, but if none is provided, this is used by default. */
    public static final Type<BigDecimal> BIG_DECIMAL_TYPE =
        new StandardType<BigDecimal> ( Namespace.ROOT,   // parent_namespace
                                       BIG_DECIMAL_TYPE_ID.name (), // raw name
                                       BigDecimal.class, // value_class
                                       BigDecimal.ZERO,  // none
                                       new SymbolTable () ); // symbol_table

    /** A long Type (either a custom one, or the default
     *  TopologyTypeClass.LONG_TYPE) is required in order
     *  to implement certain Region operations. */
    public static final TypeID LONG_TYPE_ID =
        new TypeID ( "long" );

    /** Longs are required in some topology operations.
     *  This is a bare-bones Type, nothing special.  A Type with
     *  the same ID is required to create a TopologyTypeClass
     *  instance, but if none is provided, this is used by default. */
    public static final Type<Long> LONG_TYPE =
        new StandardType<Long> ( Namespace.ROOT,   // parent_namespace
                                 LONG_TYPE_ID.name (), // raw name
                                 Long.class, // value_class
                                 0L,  // none
                                 new SymbolTable () ); // symbol_table


    // Ugly hack for @*%# generics.
    private static class Silly<ORDER>
    {
        @SuppressWarnings("unchecked")
        public Class<ORDER> getOrderClass ()
        {
            return (Class<ORDER>) Order.class;
        }
    }

    /** An Order<POINT> is required for every Topology. */
    public static final Type<Order<Object>> ORDER_TYPE =
        new StandardType<Order<Object>> ( Namespace.ROOT,   // parent_namespace
                                          "order", // raw name
                                          new Silly<Order<Object>> ().getOrderClass (), // value_class
                                          new NoOrder<Object> (),  // none
                                          new SymbolTable () ); // symbol_table

    /** No TopologyTypeClass at all. */
    public static final TopologyTypeClass NONE =
        new TopologyTypeClass ( TopologyTypeClass.NAMESPACE,
                                "no_topology_type_class",
                                new SymbolTable (),
                                new StandardMetadata () );




    /**
     * <p>
     * Constants and Operations relating to POINTs in a TopologyTypeClass.
     * </p>
     */
    public static class Point
        implements Serializable
    {
        private static final long serialVersionUID =
            TopologyTypeClass.serialVersionUID;

        /** The ID of the TypeClass describing the required SymbolIDs
         *  for a POINT Type. */
        public static final TagID TYPE_CLASS_ID =
            new TagID ( "point" );


        /** The "max" constant is the maximum allowed POINT
         *  in this topology. */
        public static final ConstantTermID<Object> MAX =
            new ConstantTermID<Object> ( POINT_TYPE, "max" );

        /** The "min" constant is the minimum allowed POINT
         *  for this topology. */
        public static final ConstantTermID<Object> MIN =
            new ConstantTermID<Object> ( POINT_TYPE, "min" );

        /** The "order" constant defines how POINTs should be compared and
         *  sorted in this topology. */
        public static final ConstantTermID<Order<Object>> ORDER =
            new ConstantTermID<Order<Object>> ( ORDER_TYPE, "order" );

        /** The "origin" constant defines the starting point of new Regions
         *  in this topology, such as the centre POINT, or
         *  the minimum POINT, and so on. */
        public static final ConstantTermID<Object> ORIGIN =
            new ConstantTermID<Object> ( POINT_TYPE, "origin" );


        // OperationType: ( point, measure ): measure.
        private static final OperationType2<Object, Object, Object> OP_PMM =
            new OperationType2<Object, Object, Object> ( POINT_TYPE,
                                                         MEASURE_TYPE,
                                                         MEASURE_TYPE );

        // OperationType: ( point, measure ): point.
        private static final OperationType2<Object, Object, Object> OP_PMP =
            new OperationType2<Object, Object, Object> ( POINT_TYPE,
                                                         MEASURE_TYPE,
                                                         POINT_TYPE );

        // OperationType: ( point, point ): measure.
        private static final OperationType2<Object, Object, Object> OP_PPM =
            new OperationType2<Object, Object, Object> ( POINT_TYPE,
                                                         POINT_TYPE,
                                                         MEASURE_TYPE );

        // OperationType: ( point, point ): region.
        private static final OperationType2<Object, Object, Region<Object, Object>> OP_PPR =
            new OperationType2<Object, Object, Region<Object, Object>> (
                POINT_TYPE,
                POINT_TYPE,
                Region.TYPE );

        // OperationType: ( point ): point.
        private static final OperationType1<Object, Object> OP_PP =
            new OperationType1<Object, Object> ( POINT_TYPE,
                                                 POINT_TYPE );


        /** The <code> + ( POINT, MEASURE ) : POINT </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Object>, Object> ADD =
            new OperationID<Operation2<Object, Object, Object>, Object> (
                "add",
                OP_PMP );

        /** The <code> ++ ( POINT ) : POINT </code>
         *  unary operation. */
        public static final OperationID<Operation1<Object, Object>, Object> NEXT =
            new OperationID<Operation1<Object, Object>, Object> (
                "next",
                OP_PP );

        /** The <code> -- ( POINT ) : POINT </code>
         *  unary operation. */
        public static final OperationID<Operation1<Object, Object>, Object> PREVIOUS =
            new OperationID<Operation1<Object, Object>, Object> (
                "previous",
                OP_PP );

        /** The <code> .. ( POINT, POINT ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Region<Object, Object>>, Region<Object, Object>> REGION =
            new OperationID<Operation2<Object, Object, Region<Object, Object>>, Region<Object, Object>> (
                "region",
                OP_PPR );

        /** The <code> - ( POINT, MEASURE ) : POINT </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Object>, Object> SUBTRACT_MEASURE =
            new OperationID<Operation2<Object, Object, Object>, Object> (
                "subtract",
                OP_PMP );

        /** The <code> - ( POINT, POINT ) : MEASURE </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Object>, Object> SUBTRACT_POINT =
            new OperationID<Operation2<Object, Object, Object>, Object> (
                "subtract",
                OP_PPM );
    }




    /**
     * <p>
     * Constants and Operations relating to MEASUREs in a TopologyTypeClass.
     * </p>
     */
    public static class Measure
        implements Serializable
    {
        private static final long serialVersionUID =
            TopologyTypeClass.serialVersionUID;

        /** The ID of the TypeClass describing the required SymbolIDs
         *  for a MEASURE Type. */
        public static final TagID TYPE_CLASS_ID =
            new TagID ( "measure" );


        /** The "unit" constant, the smallest unit of MEASURE
         *  in this topology. */
        public static final ConstantTermID<Object> UNIT =
            new ConstantTermID<Object> ( MEASURE_TYPE, "unit" );


        // OperationType: ( measure, measure ): measure.
        private static final OperationType2<Object, Object, Object> OP_MMM =
            new OperationType2<Object, Object, Object> ( MEASURE_TYPE,
                                                         MEASURE_TYPE,
                                                         MEASURE_TYPE );

        // OperationType: ( measure, BigDecimal ): measure.
        private static final OperationType2<Object, BigDecimal, Object> OP_MBM =
            new OperationType2<Object, BigDecimal, Object> ( MEASURE_TYPE,
                                                             BIG_DECIMAL_TYPE,
                                                             MEASURE_TYPE );

        // OperationType: ( measure, measure ): BigDecimal.
        private static final OperationType2<Object, Object, BigDecimal> OP_MMB =
            new OperationType2<Object, Object, BigDecimal> ( MEASURE_TYPE,
                                                             MEASURE_TYPE,
                                                             BIG_DECIMAL_TYPE );

        // OperationType: ( measure ): measure.
        private static final OperationType1<Object, Object> OP_MM =
            new OperationType1<Object, Object> ( MEASURE_TYPE,
                                                 MEASURE_TYPE );


        /** The <code> + ( MEASURE, MEASURE ) : MEASURE </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Object>, Object> ADD =
            new OperationID<Operation2<Object, Object, Object>, Object> (
                "add",
                OP_MMM );

        /** The <code> / ( MEASURE, MEASURE ) : MEASURE </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, BigDecimal, Object>, Object> DIVIDE =
            new OperationID<Operation2<Object, BigDecimal, Object>, Object> (
                "divide",
                OP_MBM );

        /** The <code> % ( MEASURE, MEASURE ) : MEASURE </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Object>, Object> MODULO =
            new OperationID<Operation2<Object, Object, Object>, Object> (
                "modulo",
                OP_MMM );

        /** The <code> * ( MEASURE, BigDecimal ) : MEASURE </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, BigDecimal, Object>, Object> MULTIPLY =
            new OperationID<Operation2<Object, BigDecimal, Object>, Object> (
                "multiply",
                OP_MBM );

        /** The <code> ++ ( MEASURE ) : MEASURE </code>
         *  unary operation. */
        public static final OperationID<Operation1<Object, Object>, Object> NEXT =
            new OperationID<Operation1<Object, Object>, Object> (
                "next",
                OP_MM );

        /** The <code> -- ( MEASURE ) : MEASURE </code>
         *  unary operation. */
        public static final OperationID<Operation1<Object, Object>, Object> PREVIOUS =
            new OperationID<Operation1<Object, Object>, Object> (
                "previous",
                OP_MM );

        /** The <code> / ( MEASURE, MEASURE ) : BigDecimal </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, BigDecimal>, BigDecimal> RATIO =
            new OperationID<Operation2<Object, Object, BigDecimal>, BigDecimal> (
                "ratio",
                OP_MMB );

        /** The <code> - ( MEASURE, MEASURE ) : MEASURE </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Object, Object>, Object> SUBTRACT =
            new OperationID<Operation2<Object, Object, Object>, Object> (
                "subtract",
                OP_MMM );
    }




    /**
     * <p>
     * Constants and Operations relating to Regions in a TopologyTypeClass.
     * </p>
     */
    public static class RegionClass
        implements Serializable
    {
        private static final long serialVersionUID =
            TopologyTypeClass.serialVersionUID;

        // OperationType: ( region, POINT, long, POINT ): region.
        private static final OperationType4<Object, Long, Object, Region<Object, Object>, Region<Object, Object>> OP_PLPRR =
            new OperationType4<Object, Long, Object, Region<Object, Object>, Region<Object, Object>> (
                POINT_TYPE,
                LONG_TYPE,
                POINT_TYPE,
                Region.TYPE,
                Region.TYPE );

        // OperationType: ( long, region ): region.
        private static final OperationType2<Long, Region<Object, Object>, Region<Object, Object>> OP_LRR =
            new OperationType2<Long, Region<Object, Object>, Region<Object, Object>> (
                LONG_TYPE,
                Region.TYPE,
                Region.TYPE );

        // OperationType: ( MEASURE, region ): region.
        private static final OperationType2<Object, Region<Object, Object>, Region<Object, Object>> OP_MRR =
            new OperationType2<Object, Region<Object, Object>, Region<Object, Object>> (
                MEASURE_TYPE,
                Region.TYPE,
                Region.TYPE );

        // OperationType: ( order, region ): region.
        private static final OperationType2<Order<Object>, Region<Object, Object>, Region<Object, Object>> OP_ORR =
            new OperationType2<Order<Object>, Region<Object, Object>, Region<Object, Object>> (
                ORDER_TYPE,
                Region.TYPE,
                Region.TYPE );

        // OperationType: ( POINT, region ): region.
        private static final OperationType2<Object, Region<Object, Object>, Region<Object, Object>> OP_PRR =
            new OperationType2<Object, Region<Object, Object>, Region<Object, Object>> (
                POINT_TYPE,
                Region.TYPE,
                Region.TYPE );

        // OperationType: ( region, region ): region.
        private static final OperationType2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>> OP_RRR =
            new OperationType2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>> (
                Region.TYPE,
                Region.TYPE,
                Region.TYPE );

        // OperationType: ( region ): POINT.
        private static final OperationType1<Region<Object, Object>, Object> OP_RP =
            new OperationType1<Region<Object, Object>, Object> (
                Region.TYPE,
                POINT_TYPE );

        // OperationType: ( region ): region.
        private static final OperationType1<Region<Object, Object>, Region<Object, Object>> OP_RR =
            new OperationType1<Region<Object, Object>, Region<Object, Object>> (
                Region.TYPE,
                Region.TYPE );


        /** The <code> xor ( Region, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> DIFFERENCE =
            new OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "difference",
                OP_RRR );

        /** The <code> - ( Region, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> EXCLUDE =
            new OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "exclude",
                OP_RRR );

        /** The <code> group ( Order, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Order<Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> GROUP =
            new OperationID<Operation2<Order<Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "group",
                OP_ORR );

        /** The <code> &amp; ( Region, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> INTERSECTION =
            new OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "intersection",
                OP_RRR );

        /** The <code> invert ( Region ) : Region </code>
         *  unary operation. */
        public static final OperationID<Operation1<Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> INVERT =
            new OperationID<Operation1<Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "invert",
                OP_RR );

        /** The <code> mid ( Region ) : POINT </code>
         * unary operation. */
        public static final OperationID<Operation1<Region<Object, Object>, Object>, Object> MID =
            new OperationID<Operation1<Region<Object, Object>, Object>, Object> (
                "mid",
                OP_RP );

        /** The <code> scale ( MEASURE, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> SCALE =
            new OperationID<Operation2<Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "scale",
                OP_MRR );

        /** The <code> splitat ( POINT, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> SPLIT_AT =
            new OperationID<Operation2<Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "splitat",
                OP_PRR );

        /** The <code> splitby ( MEASURE, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> SPLIT_BY =
            new OperationID<Operation2<Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "splitby",
                OP_MRR );

        /** The <code> splitinto ( long, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Long, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> SPLIT_INTO =
            new OperationID<Operation2<Long, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "splitinto",
                OP_LRR );

        /** The <code> subregion ( POINT, long, POINT, Region ) : Region </code>
         *  operation. */
        public static final OperationID<Operation4<Object, Long, Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> SUB_REGION =
            new OperationID<Operation4<Object, Long, Object, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "subregion",
                OP_PLPRR );

        /** The <code> | ( Region, Region ) : Region </code>
         *  binary operation. */
        public static final OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> UNION =
            new OperationID<Operation2<Region<Object, Object>, Region<Object, Object>, Region<Object, Object>>, Region<Object, Object>> (
                "union",
                OP_RRR );
    }




    /**
     * <p>
     * Creates a new TopologyTypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     */
    public TopologyTypeClass (
                              Namespace parent_namespace,
                              String name
                              )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               new SymbolTable () );
    }


    /**
     * <p>
     * Creates a new TopologyTypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     TopologyTypeClass,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     */
    public TopologyTypeClass (
                              Namespace parent_namespace,
                              String name,
                              SymbolTable symbol_table
                              )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        this ( parent_namespace,
               name,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new TopologyTypeClass with the specified name, to be used
     * for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     TopologyTypeClass,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this TopologyTypeClass.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public TopologyTypeClass (
                              Namespace parent_namespace,
                              String name,
                              SymbolTable symbol_table,
                              Metadata metadata
                              )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        super ( parent_namespace, name, // name
                new SymbolID<?> []      // required_symbol_ids
                {
                    // A BigDecimal type is required.  The default
                    // can be used (TopologyTypeClass.BIG_DECIMAL_TYPE).
                    TopologyTypeClass.BIG_DECIMAL_TYPE_ID,

                    // Operations taking the POINT type as the 1st argument:
                    TopologyTypeClass.Point.ADD,
                    TopologyTypeClass.Point.NEXT,
                    TopologyTypeClass.Point.PREVIOUS,
                    TopologyTypeClass.Point.REGION,
                    TopologyTypeClass.Point.SUBTRACT_MEASURE,
                    TopologyTypeClass.Point.SUBTRACT_POINT,

                    // Operations taking the MEASURE type as the 1st argument:
                    TopologyTypeClass.Measure.ADD,
                    TopologyTypeClass.Measure.DIVIDE,
                    TopologyTypeClass.Measure.MODULO,
                    TopologyTypeClass.Measure.MULTIPLY,
                    TopologyTypeClass.Measure.NEXT,
                    TopologyTypeClass.Measure.PREVIOUS,
                    TopologyTypeClass.Measure.RATIO,
                    TopologyTypeClass.Measure.SUBTRACT,

                    // Operations taking Region.TYPE as the 1st argument
                    // (can use the default implementations):
                    TopologyTypeClass.RegionClass.DIFFERENCE,
                    TopologyTypeClass.RegionClass.EXCLUDE,
                    TopologyTypeClass.RegionClass.GROUP,
                    TopologyTypeClass.RegionClass.INTERSECTION,
                    TopologyTypeClass.RegionClass.INVERT,
                    TopologyTypeClass.RegionClass.MID,
                    TopologyTypeClass.RegionClass.SCALE,
                    TopologyTypeClass.RegionClass.SPLIT_AT,
                    TopologyTypeClass.RegionClass.SPLIT_BY,
                    TopologyTypeClass.RegionClass.SPLIT_INTO,
                    TopologyTypeClass.RegionClass.SUB_REGION,
                    TopologyTypeClass.RegionClass.UNION
                },
                symbol_table,
                metadata );

        // The default BigDecimal Type:
        symbol_table.set ( TopologyTypeClass.BIG_DECIMAL_TYPE_ID,
                           TopologyTypeClass.BIG_DECIMAL_TYPE );

        // The default difference ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.DIFFERENCE,
                           new Difference ( TopologyTypeClass.RegionClass.DIFFERENCE.name () ) );

        // The default exclude ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.EXCLUDE,
                           new Exclude ( TopologyTypeClass.RegionClass.EXCLUDE.name () ) );

        // The default group ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.GROUP,
                           new Group ( TopologyTypeClass.RegionClass.GROUP.name () ) );

        // The default intersection ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.INTERSECTION,
                           new Intersection ( TopologyTypeClass.RegionClass.INTERSECTION.name () ) );

        // The default invert ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.INVERT,
                           new Invert ( TopologyTypeClass.RegionClass.INVERT.name () ) );

        // The default mid ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.MID,
                           new Mid ( TopologyTypeClass.RegionClass.MID.name () ) );

        // The default scale ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.SCALE,
                           new Scale ( TopologyTypeClass.RegionClass.SCALE.name () ) );

        // The default splitat ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.SPLIT_AT,
                           new SplitAt ( TopologyTypeClass.RegionClass.SPLIT_AT.name () ) );

        // The default splitby ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.SPLIT_BY,
                           new SplitBy ( TopologyTypeClass.RegionClass.SPLIT_BY.name () ) );

        // The default splitinto ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.SPLIT_INTO,
                           new SplitInto ( TopologyTypeClass.RegionClass.SPLIT_INTO.name () ) );

        // The default subregion ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.SUB_REGION,
                           new SubRegion ( TopologyTypeClass.RegionClass.SUB_REGION.name () ) );

        // The default union ( Region, Region ): Region operation.
        symbol_table.set ( TopologyTypeClass.RegionClass.UNION,
                           new Union ( TopologyTypeClass.RegionClass.UNION.name () ) );

        // POINT child TypeClass:
        final TagID point_type_class_id =
            TopologyTypeClass.Point.TYPE_CLASS_ID;

        final TypeClass point_type_class =
            this.createPointTypeClass ( point_type_class_id.name (), // name
                                        metadata ); // Shared metadata

        symbol_table.set ( point_type_class_id,
                           point_type_class );

        // MEASURE child TypeClass:
        final TagID measure_type_class_id =
            TopologyTypeClass.Measure.TYPE_CLASS_ID;

        final TypeClass measure_type_class =
            this.createMeasureTypeClass ( measure_type_class_id.name (),// name
                                          metadata ); // Shared metadata

        symbol_table.set ( measure_type_class_id,
                           measure_type_class );
    }


    /**
     * <p>
     * Creates the TypeClass describing all POINT Types.
     * </p>
     *
     * @param name The name of the POINT TypeClass to be created.
     *             Must not be null.
     *
     * @param metadata The Metadata to store with the POINT TypeClass.
     *                 Tracks statistics and so on for the TypeClass.
     *                 The same Metadata can be shared between the
     *                 topology TypeClass, its POINT TypeClass,
     *                 and the MEASURE TypeClass.  Must not be null.
     */
    protected TypeClass createPointTypeClass (
                                              String name,
                                              Metadata metadata
                                              )
    {
        return new TypeClass ( this, // parent_namespace
                               name, // name
                               new SymbolID<?> [] // required_symbol_ids
                               {
                                   // POINT constants:
                                   TopologyTypeClass.Point.MAX,
                                   TopologyTypeClass.Point.MIN,
                                   TopologyTypeClass.Point.ORDER,
                                   TopologyTypeClass.Point.ORIGIN
                               },
                               new SymbolTable (), // symbol_table
                               metadata ); // metadata
    }


    /**
     * <p>
     * Creates the TypeClass describing all MEASURE Types.
     * </p>
     *
     * @param name The name of the MEASURE TypeClass to be created.
     *             Must not be null.
     *
     * @param metadata The Metadata to store with the MEASURE TypeClass.
     *                 Tracks statistics and so on for the TypeClass.
     *                 The same Metadata can be shared between the
     *                 topology TypeClass, its MEASURE TypeClass,
     *                 and the MEASURE TypeClass.  Must not be null.
     */
    protected TypeClass createMeasureTypeClass (
                                                String name,
                                                Metadata metadata
                                                )
    {
        return new TypeClass ( this, // parent_namespace
                               name, // name
                               new SymbolID<?> [] // required_symbol_ids
                               {
                                   // MEASURE constants:
                                   TopologyTypeClass.Measure.UNIT
                               },
                               new SymbolTable (), // symbol_table
                               metadata ); // metadata
    }


    /**
     * @return The child TypeClass describing what MEASURE types
     *         must provide.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Tag - TypeClass.
    public TypeClass measureTypeClass ()
        throws ReturnNeverNull.Violation
    {
        final TypeClass measure_type_class = (TypeClass)
            this.symbolTable ().symbol ( TopologyTypeClass.Measure.TYPE_CLASS_ID )
                               .orNull ();
        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  measure_type_class );

        return measure_type_class;
    }


    /**
     * @return The child TypeClass describing what POINT types
     *         must provide.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Tag - TypeClass.
    public TypeClass pointTypeClass ()
        throws ReturnNeverNull.Violation
    {
        final TypeClass point_type_class = (TypeClass)
            this.symbolTable ().symbol ( TopologyTypeClass.Point.TYPE_CLASS_ID )
                               .orNull ();
        this.contracts ().check ( ReturnNeverNull.CONTRACT,
                                  point_type_class );

        return point_type_class;
    }
}
