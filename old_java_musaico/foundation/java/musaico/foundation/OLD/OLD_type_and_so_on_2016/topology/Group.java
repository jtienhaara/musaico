package musaico.foundation.topology;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;

import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBody2;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperation2;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * Divides each single Region up into groups, each Region
 * containing only POINTs that are equal, given a specific Order
 * to compare by.
 * </p>
 *
 * <p>
 * For example, with an Order which
 * considers all integers less than 5 to be equal, all integers
 * 5-10 to be equal, and all integers greater than 5 to be equal,
 * then grouping the Region { 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 }
 * would result in three output Regions: { 3, 4 }, { 5, 6, 7, 8, 9, 10 },
 * and { 11, 12 }.
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
public class Group
    extends StandardOperation2<Order<Object>, Region, Region>
    implements OperationBody2<Order<Object>, Region, Region>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new Group operation with the default name.
     * </p>
     */
    public Group ()
    {
        this ( TopologyTypeClass.RegionClass.GROUP.name () );
    }


    /**
     * <p>
     * Creates a new Group operation with the specified name.
     * </p>
     *
     * @param name The name of the new Operation.  Used to create an ID
     *             that is unique among symbols of the same type in any
     *             given SymbolTable.  Must not be null.
     */
    public Group (
                  String name
                  )
    {
        // body = this.
        super ( name,
                TopologyTypeClass.ORDER_TYPE,   // Input type 1.
                Region.TYPE,                    // Input type 2.
                Region.TYPE );                  // Output type.

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.types.OperationBody2#evaluateBody(musaico.foundation.types.Value, musaico.foundation.types.Value)
     */
    @Override
    public final Value<Region> evaluateBody (
                                             Value<Order<Object>> orders,
                                             Value<Region> regions
                                             )
    {
        if ( ! regions.hasValue () )
        {
            // No regions.  Return as-is.
            return regions;
        }
        else if ( ! orders.hasValue () )
        {
            // No orders.  Return as-is.
            return regions;
        }

        final TypedValueBuilder<Region> builder =
            Region.TYPE.builder ();
        final Iterator<Region> region =
            regions.iterator ();
        final Iterator<Order<Object>> order =
            orders.iterator ();
        while ( regions.hasNext () )
        {
            if ( ! order.hasNext () )
            {
                builder.add ( region.next () );
            }
            else
            {
                this.group ( order.next (),
                             region.next (),
                             builder );
            }
        }

        final Value<Region> outputs = builder.build ();

        return outputs;
    }


    /**
     * <p>
     * Groups the specified Region according to the specified Order, and
     * adds each new Region to the builder.
     * </p>
     *
     * @param region The Region to group.
     *               Must not be null.
     *
     * @param order The order by which to group the Region.
     *              Equal POINTs will be grouped into the same Region.
     *              Must not be null.
     *
     * @param builder Builds up the output Region(s).
     *                Must not be null. 
     */
    public void group (
                       Region region,
                       Order<Object> order,
                       TypedValueBuilder<Region> builder
                       )
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region, order, builder );

        // !!!;

        return; // !!!!!!!!!!!!!!!!!!!!!!!!!!!
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Group rename (
                         String name
                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new Group ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Group retype (
                         String name,
                         OperationType<? extends Operation<Region>, Region> type
                         )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Group ( name );
    }
}
