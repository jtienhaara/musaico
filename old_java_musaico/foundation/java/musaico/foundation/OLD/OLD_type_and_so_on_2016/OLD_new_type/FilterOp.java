package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * Filters each input value, returning only the inputs which were kept
 * by the filter.
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
public class FilterOp<GRAIN extends Object>
    extends StandardOperation1<GRAIN, GRAIN>
    implements OperationBody1<GRAIN, GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( FilterOp.class );


    /** The filter which decides which input values should be KEPT
     *  and which input values should be DISCARDED. */
    private final Filter<GRAIN> filter;


    /**
     * <p>
     * Creates a new FilterOp operation to filter out input values.
     * </p>
     *
     * @param name The name of this FilterOp, which will be used to
     *             construct an OperationID unique in every SymbolTable.
     *             Must not be null.
     *
     * @param type The Type of values filtered by this FilterOp.
     *             Must not be null.
     *
     * @param filter The filter which decides which input values will
     *               be KEPT and which input values will be DISCARDED.
     *               Must not be null.
     */
    public FilterOp (
                     String name,
                     Type<GRAIN> type,
                     Filter<GRAIN> filter
                     )
    {
        super ( name,
                new OperationType1<GRAIN, GRAIN> ( type, type ),
                null ); // body = this.

        FilterOp.classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                        filter );

        this.filter = filter;
    }


    /**
     * @see musaico.foundation.types.OperationBody#evaluateBody(musaico.foundation.types.Value)
     */
    @Override
    public final Value<GRAIN> evaluateBody (
                                            Value<GRAIN> input
                                            )
    {
        if ( ! input.hasValue () )
        {
            // No value.
            return input;
        }

        final ValueBuilder<GRAIN> builder =
            new TypedValueBuilder<GRAIN> ( this.outputType () );
        for ( GRAIN grain : input )
        {
            final FilterState filter_state = this.filter.filter ( grain );
            if ( filter_state.isKept () )
            {
                builder.add ( grain );
            }
        }

        Value<GRAIN> filtered = builder.build ();

        return filtered;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public FilterOp<GRAIN> rename (
                                   String name
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new FilterOp<GRAIN> ( name,
                                     this.input1Type (),
                                     this.filter );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public FilterOp<GRAIN> retype (
                                   String name,
                                   OperationType<? extends Operation<GRAIN>, GRAIN> type
                                   )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new FilterOp<GRAIN> ( name,
                                     type.outputType (),
                                     this.filter );
    }
}
