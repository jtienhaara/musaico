package musaico.foundation.topology;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBody1;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * Given two Region inputs, returns the Region comprising only the
 * POINTs common to the two Regions.
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
public class Mid
    extends StandardOperation1<Region, Object>
    implements OperationBody1<Region, Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new Mid operation with the default name.
     * </p>
     */
    public Mid ()
    {
        this ( TopologyTypeClass.RegionClass.MID.name () );
    }


    /**
     * <p>
     * Creates a new Mid operation with the specified name.
     * </p>
     *
     * @param name The name of the new Operation.  Used to create an ID
     *             that is unique among symbols of the same type in any
     *             given SymbolTable.  Must not be null.
     */
    public Mid (
                String name
                )
    {
        // body = this.
        super ( name,
                Region.TYPE,   // Input type 1.
                TopologyTypeClass.POINT_TYPE ); // Output type.

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.types.OperationBody1#evaluateBody(musaico.foundation.types.Value)
     */
    @Override
    public final Value<Object> evaluateBody (
                                             Value<Region> region
                                             )
    {
        if ( ! region.hasValue () )
        {
            // No regions.  Return empty POINTs.
            return TopologyTypeClass.POINT_TYPE.noValue ();
        }

        final TypedValueBuilder<Object> builder =
            TopologyTypeClass.POINT_TYPE.builder ();
        final Iterator<Region> region =
            regions.iterator ();
        while ( region.hasNext () )
        {
            this.mid ( region.next (),
                       builder );
        }

        final Value<Object> midpoints = builder.build ();

        return midpoints;
    }


    /**
     * <p>
     * Adds the midpoint(s) of the specified Region to the specified
     * points builder.
     * </p>
     *
     * @param region The Region whose midpoint(s) will be returned.
     *               Must not be null.
     *
     * @param builder Builds up one or more mid-POINT(s).
     *                Must not be null.
     */
    public void mid (
                     Region region,
                     TypedValueBuilder<Object> builder
                     )
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region, builder );

        // !!!;

        return; // !!!!!!!!!!!!!!!!!!!!!!!!!!!
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Mid rename (
                       String name
                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new Mid ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Mid retype (
                       String name,
                       OperationType<? extends Operation<Region>, Region> type
                       )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Mid ( name );
    }
}
