package musaico.foundation.topology;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

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
public class SplitInto
    extends StandardOperation2<Region, Region, Region>
    implements OperationBody2<Region, Region, Region>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new SplitInto operation with the default name.
     * </p>
     */
    public SplitInto ()
    {
        this ( TopologyTypeClass.RegionClass.SPLIT_INTO.name () );
    }


    /**
     * <p>
     * Creates a new SplitInto operation with the specified name.
     * </p>
     *
     * @param name The name of the new Operation.  Used to create an ID
     *             that is unique among symbols of the same type in any
     *             given SymbolTable.  Must not be null.
     */
    public SplitInto (
                      String name
                      )
    {
        // body = this.
        super ( name,
                Region.TYPE,   // Input type 1.
                Region.TYPE,   // Input type 2.
                Region.TYPE ); // Output type.

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.types.OperationBody2#evaluateBody(musaico.foundation.types.Value, musaico.foundation.types.Value)
     */
    @Override
    public final Value<Region> evaluateBody (
                                             Value<Region> inputs1,
                                             Value<Region> inputs2
                                             )
    {
        if ( ! inputs2.hasValue () )
        {
            // No value for inputs2.  Return inputs1.
            return inputs1;
        }
        else if ( ! inputs1.hasValue () )
        {
            // No value for inputs1.  Return inputs2.
            return inputs2;
        }

        final TypedValueBuilder<Region> builder =
            Region.TYPE.builder ();
        final Iterator<Region> input1 =
            inputs1.iterator ();
        final Iterator<Region> input2 =
            inputs2.iterator ();
        while ( input1.hasNext ()
                || input2.hasNext () )
        {
            final Region output;
            if ( ! input2.hasNext () )
            {
                output = input1.next ();
            }
            else if ( ! input1.hasNext () )
            {
                output = input2.next ();
            }
            else
            {
                output = this.splitInto ( input1.next (),
                                          input2.next () );
            }

            builder.add ( output );
        }

        final Value<Region> outputs = builder.build ();

        return outputs;
    }


    /**
     * <p>
     * Returns the split into of two Regions.
     * </p>
     *
     * @param region1 The 1st Region to split into.
     *                Must not be null.
     *
     * @param region2 The 2nd Region to split into.
     *                Must not be null.
     *
     * @return The split into of the two specified Regions.
     *         Can be empty.  Never null.
     */
    public Region splitInto (
                             Region region1,
                             Region region2
                             )
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region1, region2 );

        final Topology<?, ?> topology = region1.topology ();

        // !!!;

        return Region.NONE; // !!!!!!!!!!!!!!!!!!!!!!!!!!!
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public SplitInto rename (
                             String name
                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new SplitInto ( name );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public SplitInto retype (
                             String name,
                             OperationType<? extends Operation<Region>, Region> type
                             )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new SplitInto ( name );
    }
}
