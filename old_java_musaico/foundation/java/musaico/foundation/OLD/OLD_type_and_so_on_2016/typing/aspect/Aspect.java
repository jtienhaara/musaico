package musaico.foundation.typing.aspect;

import java.io.Serializable;

import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.Origin;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.StandardOrigin;

import musaico.foundation.typing.AbstractTag;
import musaico.foundation.typing.Mutation;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationBody1;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.StandardOperation1;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.TypedValueBuilder;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.value.Value;


/**
 * <p>
 * Mutates Operations to execute the pre and post code for an Aspect.
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
 * @see musaico.foundation.typing.aspect.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.aspect.MODULE#LICENSE
 */
public class Aspect
    extends StandardOperation1<Mutation, Mutation>
    implements OperationBody1<Mutation, Mutation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Instruments each mutated Operation, performing pre and
     * post tasks which might or modify the input/output
     * to/from the Operation, or might simply perform some
     * pre/post task(s) without processing the input/output.
     * </p>
     */
    public static class PrePost
        implements Serializable
    {
        private static final long serialVersionUID = Aspect.serialVersionUID;

        public <OUTPUT extends Object>
            List<Value<Object>> pre (
                                     Operation<OUTPUT> operation,
                                     List<Value<Object>> inputs
                                     )
        {
            this.preNoProcessing ( operation );
            return inputs;
        }


        public <OUTPUT extends Object>
            Value<OUTPUT> post (
                                Operation<OUTPUT> operation,
                                Value<OUTPUT> output
                                )
        {
            this.postNoProcessing ( operation );
            return output;
        }

        protected void preNoProcessing (
                                        Operation<?> operation
                                        )
        {
        }

        protected void postNoProcessing (
                                         Operation<?> operation
                                         )
        {
        }
    }


    // Checks parameters to constructors and static methods for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Aspect.class );


    // The pre and post aspects to each instrumented Operation.
    private final Aspect.PrePost prePost;


    public Aspect (
                   String name,
                   PrePost pre_post
                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name,
                Mutation.TYPE,
                Mutation.TYPE,
                null ); // body = this.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pre_post );

        this.prePost = pre_post;
    }


    /**
     * @see musaico.foundation.typing.OperationBody1#evaluateBody(musaico.foundation.value.Value)
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


    private <OPERATION extends Operation<OUTPUT>, OUTPUT extends Object>
        AspectOperation<OPERATION, OUTPUT> mutateOperation (
                                                            OPERATION operation
                                                            )
    {
        final AspectOperation<OPERATION, OUTPUT> mutated =
            new AspectOperation<OPERATION, OUTPUT> ( operation,
                                                     this );
        return mutated;
    }


    /**
     * @return The pre and post operation aspects which might
     *         modify the input/output to/from the Operation,
     *         or might simply perform some pre/post task(s)
     *         without processing the input/output.  Never null.
     */
    public final Aspect.PrePost prePost ()
    {
        return this.prePost;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Aspect rename (
                          String name
                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name );

        return new Aspect ( name, this.prePost );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Aspect retype (
                          String name,
                          OperationType<? extends Operation<Mutation>, Mutation> type
                          )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Aspect ( name, this.prePost );
    }
}
