package musaico.foundation.topology;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;


/**
 * <p>
 * Convenience class to access the operations of a Topology's point
 * and measure instances, such as add, subtract, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every TopologyOperations must be Serializable in order
 * to play nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TopologyOperations<POINT extends Object, MEASURE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TopologyOperations.class );


    // The Topology whose operations we provide access to.
    private final Topology<POINT, MEASURE> topology;


    /**
     * <p>
     * Creates a new TopologyOperations to provide convenient access
     * to the operation instances in the specified Topology.
     * </p>
     *
     * @param topology The Topology whose operations will be conveniently
     *                 accessible from this TopologyOperations.
     *                 Must not be null.
     */
    public TopologyOperations (
                               Topology<POINT, MEASURE> topology
                               )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               topology );

        this.topology = topology;
    }


    /**
     * @return The <code> add ( POINT, MEASURE ) : POINT </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<P,M,P>.
    public final Operation2<POINT, MEASURE, POINT> addPointPlusMeasure ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<POINT, MEASURE, POINT>)
            this.topology.requiredSymbol ( TopologyTypeClass.Point.ADD )
                         .orNone ();
    }


    /**
     * @return The <code> add ( MEASURE, MEASURE ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<M,M,M>.
    public final Operation2<MEASURE, MEASURE, MEASURE> addMeasurePlusMeasure ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<MEASURE, MEASURE, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.ADD )
                         .orNone ();
    }


    /**
     * @return The <code> divide ( MEASURE, BigDecimal ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,BD,Obj> - Op2<M,BD,M>.
    public final Operation2<MEASURE, BigDecimal, MEASURE> divide ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<MEASURE, BigDecimal, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.DIVIDE )
                         .orNone ();
    }


    /**
     * @return The <code> intersection ( Region, Region ) : Region </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Reg<O,O>,..>-Op2<Reg<P,M>,...>.
    public final Operation2<Region<POINT, MEASURE>, Region<POINT, MEASURE>, Region<POINT, MEASURE>> intersection ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<Region<POINT, MEASURE>, Region<POINT, MEASURE>, Region<POINT, MEASURE>>)
            ( (Operation2<?, ?, ?>)
              this.topology.requiredSymbol ( TopologyTypeClass.RegionClass.INTERSECTION )
                           .orNone ()
              );
    }


    /**
     * @return The <code> modulo ( MEASURE, MEASURE ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<M,M,M>.
    public final Operation2<MEASURE, MEASURE, MEASURE> modulo ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<MEASURE, MEASURE, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.MODULO )
                         .orNone ();
    }


    /**
     * @return The <code> multiply ( MEASURE, BigDecimal ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<M,BD,M>.
    public final Operation2<MEASURE, BigDecimal, MEASURE> multiply ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<MEASURE, BigDecimal, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.MULTIPLY )
                         .orNone ();
    }


    /**
     * @return The <code> next ( POINT ) : POINT </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op1<Obj,Obj> - Op1<P,P>.
    public final Operation1<POINT, POINT> nextPoint ()
        throws ReturnNeverNull.Violation
    {
        return (Operation1<POINT, POINT>)
            this.topology.requiredSymbol ( TopologyTypeClass.Point.NEXT )
                         .orNone ();
    }


    /**
     * @return The <code> next ( MEASURE ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op1<Obj,Obj> - Op1<M,M>.
    public final Operation1<MEASURE, MEASURE> nextMeasure ()
        throws ReturnNeverNull.Violation
    {
        return (Operation1<MEASURE, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.NEXT )
                         .orNone ();
    }


    /**
     * @return The <code> previous ( POINT ) : POINT </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op1<Obj,Obj> - Op1<P,P>.
    public final Operation1<POINT, POINT> previousPoint ()
        throws ReturnNeverNull.Violation
    {
        return (Operation1<POINT, POINT>)
            this.topology.requiredSymbol ( TopologyTypeClass.Point.PREVIOUS )
                         .orNone ();
    }


    /**
     * @return The <code> previous ( MEASURE ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op1<Obj,Obj> - Op1<M,M>.
    public final Operation1<MEASURE, MEASURE> previousMeasure ()
        throws ReturnNeverNull.Violation
    {
        return (Operation1<MEASURE, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.PREVIOUS )
                         .orNone ();
    }


    /**
     * @return The <code> ratio ( MEASURE, MEASURE ) : BigDecimal </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,BD> - Op2<M,M,BD>.
    public final Operation2<MEASURE, MEASURE, BigDecimal> ratio ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<MEASURE, MEASURE, BigDecimal>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.RATIO )
                         .orNone ();
    }


    /**
     * @return The <code> region ( POINT, POINT ) : Region </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<P,P,R>.
    public final Operation2<POINT, POINT, Region<POINT, MEASURE>> region ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<POINT, POINT, Region<POINT, MEASURE>>)
            ( (Operation2<?, ?, ?>)
              this.topology.requiredSymbol ( TopologyTypeClass.Point.REGION )
                           .orNone ()
              );
    }


    !!! A bunch of missing operations - see TopologyTypeClass.java.;


    /**
     * @return The <code> subtract ( POINT, MEASURE ) : POINT </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<P,M,P>.
    public final Operation2<POINT, MEASURE, POINT> subtractPointMinusMeasure ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<POINT, MEASURE, POINT>)
            this.topology.requiredSymbol ( TopologyTypeClass.Point.SUBTRACT_MEASURE )
                         .orNone ();
    }


    /**
     * @return The <code> subtract ( POINT, POINT ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<P,P,M>.
    public final Operation2<POINT, POINT, MEASURE> subtractPointMinusPoint ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<POINT, POINT, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Point.SUBTRACT_POINT )
                         .orNone ();
    }


    /**
     * @return The <code> subtract ( MEASURE, MEASURE ) : MEASURE </code>
     *         Operation for the topology.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op2<Obj,Obj,Obj> - Op2<M,M,M>.
    public final Operation2<MEASURE, MEASURE, MEASURE> subtractMeasureMinusMeasure ()
        throws ReturnNeverNull.Violation
    {
        return (Operation2<MEASURE, MEASURE, MEASURE>)
            this.topology.requiredSymbol ( TopologyTypeClass.Measure.SUBTRACT )
                         .orNone ();
    }
}
