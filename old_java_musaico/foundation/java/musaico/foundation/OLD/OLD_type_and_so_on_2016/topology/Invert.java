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
public class Invert
    extends StandardOperation1<Region, Region>
    implements OperationBody1<Region, Region>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new Invert operation with the default name.
     * </p>
     */
    public Invert ()
    {
        this ( TopologyTypeClass.RegionClass.INVERT.name () );
    }


    /**
     * <p>
     * Creates a new Invert operation with the specified name.
     * </p>
     *
     * @param name The name of the new Operation.  Used to create an ID
     *             that is unique among symbols of the same type in any
     *             given SymbolTable.  Must not be null.
     */
    public Invert (
                   String name
                   )
    {
        // body = this.
        super ( name,
                Region.TYPE,   // Input type 1.
                Region.TYPE ); // Output type.

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.types.OperationBody1#evaluateBody(musaico.foundation.types.Value)
     */
    @Override
    public final Value<Region> evaluateBody (
                                             Value<Region> inputs1
                                             )
    {
        if ( ! inputs1.hasValue () )
        {
            // No value for inputs1.  Return empty regions as-is.
            return inputs1;
        }

        final TypedValueBuilder<Region> builder =
            Region.TYPE.builder ();
        final Iterator<Region> input1 =
            inputs1.iterator ();
        while ( input1.hasNext () )
        {
            final Region output;
            output = this.invert ( input1.next () );

            builder.add ( output );
        }

        final Value<Region> outputs = builder.build ();

        return outputs;
    }


    /**
     * <p>
     * Returns the invert of two Regions.
     * </p>
     *
     * @param region The Region to invert.
     *               Must not be null.
     *
     * @return The inversion of the specified Region.
     *         Can be empty.  Never null.
     */
    public Region invert (
                          Region region
                          )
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        final Topology<?, ?> topology = region1.topology ();

        // !!!;

        return Region.NONE; // !!!!!!!!!!!!!!!!!!!!!!!!!!!
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Invert rename (
                          String name
                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new Invert ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Invert retype (
                          String name,
                          OperationType<? extends Operation<Region>, Region> type
                          )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Invert ( name );
    }
}
