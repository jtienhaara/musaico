package musaico.foundation.typing.requirement;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.Mutation;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBody1;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.value.Value;


/**
 * <p>
 * !!!
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
 * @see musaico.foundation.typing.requirement.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.requirement.MODULE#LICENSE
 */
public abstract class Requirement<KEY extends Object>
    extends StandardOperation1<Mutation, Mutation>
    implements OperationBody1<Mutation, Mutation>, Contract<SatisfactionAttempt<KEY>, TypingViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    private final Class<KEY> requirementClass;

    public Requirement (
                        String name,
                        Class<KEY> requirement_class
                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,
                Mutation.TYPE,
                Mutation.TYPE,
                null ); // body = this.

        this.requirementClass = requirement_class;
    }

    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Deliberate cast AFTER we ensure safety.
    public Value<Mutation> evaluateBody (
                                         Value<Mutation> input_mutations
                                         )
        throws ReturnNeverNull.Violation
    {
        final TypedValueBuilder<Mutation> output_mutations =
            new TypedValueBuilder<Mutation> ( Mutation.TYPE );
        for ( Mutation input_mutation : input_mutations )
        {
            if ( ! input_mutation.symbol ().hasValue () )
            {
                // Looks like another mutate operation already
                // mutated the symbol out of existence.
                output_mutations.add ( input_mutation );
                continue;
            }

            final Symbol unmutated = input_mutation.symbol ().orNone ();
            if ( ! ( unmutated instanceof Operation ) )
            {
                output_mutations.add ( input_mutation );
                continue;
            }

            final Operation<?> unmutated_operation =
                (Operation<?>) unmutated;

            final Symbol mutated_operation =
                this.mutateOperation ( unmutated_operation );

            final Mutation output_mutation =
                new Mutation ( input_mutation.symbolID (),
                               mutated_operation );
            output_mutations.add ( output_mutation );
        }

        return output_mutations.build ();
    }

    @SuppressWarnings("unchecked") // !!! cast Unsatisfier
    public <OPERATION extends Operation<OUTPUT>, OUTPUT extends Object>
        OPERATION mutateOperation (
                                   OPERATION operation
                                   )
    {
        if ( operation instanceof Satisfier )
        {
            return operation;
        }

        final Unsatisfier<OPERATION, OUTPUT> unsatisfier;
        if ( operation instanceof Unsatisfier )
        {
            unsatisfier = (Unsatisfier<OPERATION, OUTPUT>) operation;
        }
        else
        {
            unsatisfier = new Unsatisfier<OPERATION, OUTPUT> ( operation );
        }

        unsatisfier.requires ( this );

        return (OPERATION) unsatisfier;
    }

    public Class<KEY> requirementClass ()
    {
        return this.requirementClass;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public final TypingViolation violation (
                                            Object plaintiff,
                                            SatisfactionAttempt<KEY> inspectable_data
                                            )
    {
        return new TypingViolation ( this,
                                     Contracts.makeSerializable ( plaintiff ),
                                     Contracts.makeSerializable ( inspectable_data ) );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "Requirement " + this.id ().name ();
    }
}
