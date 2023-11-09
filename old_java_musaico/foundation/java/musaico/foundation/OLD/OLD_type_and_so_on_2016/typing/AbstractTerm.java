package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.Origin;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.No;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Synchronous;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * Boilerplate Term method implementations.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
public abstract class AbstractTerm<TERM_ID extends AbstractTermID<? extends AbstractTermType<TERM, VALUE>, TERM, VALUE>, TERM extends Term<VALUE>, VALUE extends Object>
    extends AbstractSymbol<TERM_ID, TERM>
    implements Term<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractTerm.class );


    // The Metadata for this Term.  Always has exactly one Metadata,
    // but could  have Metadata.NONE as the single value
    // if Metadata is not supported, and could have an encrypted or
    // blocked Metadata if it is protected by this term's Type.
    private final ZeroOrOne<Metadata> metadata;


    /**
     * <p>
     * Creates a new AbstractTerm with the specified id, value Type
     * and Metadata.
     * </p>
     *
     * @param id The AbstractTermID which uniquely identifies this AbstractTerm
     *           in a SymbolTable.  Must not be null.
     *
     * @param metadata Optional Metdata value.
     *                 Pass a StandardMetadata or similar to use
     *                 metadata for this term, or Metadata.NONE
     *                 to omit metadata for this term.  Must not be null.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Value<Op> / Value<Op<X, Y>>
                                          // Cast Op to Op<Metadata, Metadata>,
                                          // ZeroOrOne<Op> - ZeroOrOne<Op1>.
    public AbstractTerm (
                         TERM_ID term_id,
                         Metadata metadata
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( term_id, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadata );

        final OperationID<Operation1<Metadata, Metadata>, Metadata> read_metadata_id =
            new OperationID<Operation1<Metadata, Metadata>, Metadata> ( "read_metadata",
                new OperationType1<Metadata, Metadata> ( MetadataType.TYPE,
                                                         MetadataType.TYPE ) );
        final ZeroOrOne<Operation1<Metadata, Metadata>> v_read_metadata =
            term_id.valueType ().symbol ( read_metadata_id );
        final ZeroOrOne<Metadata> raw_metadata = MetadataType.TYPE.builder ()
            .add ( metadata )
            .buildZeroOrOne ();
        if ( ! v_read_metadata.hasValue () )
        {
            // No protection on the metadata, return it to anyone who
            // wants it.
            this.metadata = raw_metadata;
        }
        else
        {
            // The Type provides some kind of wrapper for the metadata,
            // possibly to protect it or secure it.
            // Wrap the metadata and store the result.
            // If the wrapping operation does not obey the rules (for
            // example creating a Partial or Many Metadata - must be
            // ZeroOrOne) then we store No Metadata at all.
            final Operation1<Metadata, Metadata> read_metadata =
                v_read_metadata.orNone ();
            final Value<Metadata> zero_one_many_metadata =
                read_metadata.evaluate ( raw_metadata );
            if ( zero_one_many_metadata instanceof ZeroOrOne )
            {
                this.metadata = (ZeroOrOne<Metadata>) zero_one_many_metadata;
            }
            else
            {
                // Can't have Partial or Many Metadata.
                // The Builder will return a No<Metadata> for us.
                this.metadata = MetadataType.TYPE.builder ()
                    .addAll ( zero_one_many_metadata )
                    .buildZeroOrOne ();
            }
        }
    }


    /**
     * @see musaico.foundation.typing.Term#as(musaico.foundation.typing.Type)
     */
    @Override
    public final <CAST extends Object>
        Term<CAST> as (
                       Type<CAST> target_type
                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  target_type );

        final Value<CAST> target_value =
            this.value ( target_type )
                .async ();

        final Term<CAST> target_term = target_type.instance ( target_value );

        return target_term;
    }


    /**
     * @see musaico.foundation.typing.Term#call(musaico.foundation.typing.OperationID)
     */
    @Override
    @SuppressWarnings({"rawtypes","unchecked"}) // Value<Op>-Value<Op<X,Y>>
                                        // Cast Op<?, ?> to Op<VALUE, OUTPUT>,
                                        // ZeroOrOne<Op> - ZeroOrOne<Op1>.
    public final <OUTPUT extends Object>
        Term<OUTPUT> call (
                           OperationID<Operation1<VALUE, OUTPUT>, OUTPUT> operation_id
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  operation_id );

        final ZeroOrOne<Operation1<VALUE, OUTPUT>> v_operation =
            this.valueType ().symbol ( operation_id );
        final Type<OUTPUT> output_type =
            operation_id.operationType ().outputType ();
        Operation1<VALUE, OUTPUT> operation = null;
        for ( Operation1<?, ?> maybe_operation : v_operation )
        {
            if ( this.valueType ().equals ( maybe_operation.input1Type () )
                 && output_type.equals ( maybe_operation.outputType () ) )
            {
                operation = (Operation1<VALUE, OUTPUT>) maybe_operation;
                break;
            }
        }

        if ( operation != null )
        {
            return this.call ( operation );
        }
        else
        {
            final Unregistered unregistered_operation =
                new Unregistered ( operation_id );
            final TypingViolation violation =
                SymbolMustBeRegistered.CONTRACT
                    .violation ( this,
                                 unregistered_operation );
            final NonBlocking<OUTPUT> error_value =
                output_type.errorValue ( violation );
            final Term<OUTPUT> error_term =
                operation.outputType ().instance ( error_value );
            return error_term;
        }
    }


    /**
     * @see musaico.foundation.typing.Term#call(musaico.foundation.typing.Operation1)
     */
    @Override
    public final <OUTPUT extends Object>
        Term<OUTPUT> call (
                           Operation1<VALUE, OUTPUT> operation
                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  operation );

        final List<Term<?>> inputs = new ArrayList<Term<?>> ();
        inputs.add ( this );
        final Expression<OUTPUT> output_term =
            new Expression<OUTPUT> ( operation,
                                     inputs,
                                     this.metadata ().orNone ().renew () );

        return output_term;
    }


    /**
     * @see musaico.foundation.typing.AbstractSymbol#equalsSymbol(musaico.foundation.Symbol)
     */
    @Override
    protected final boolean equalsSymbol (
                                          TERM that
                                          )
    {
        final Type<?> that_value_type = that.valueType ();
        if ( ! this.valueType ().equals ( that_value_type ) )
        {
            // This AbstractTerm's Type != that Term's Type.
            return false;
        }

        // Everything is all matchy-matchy so far.
        final boolean is_matching_term_details =
            this.equalsTerm ( that );

        return is_matching_term_details;
    }


    /**
     * <p>
     * Returns true if this Term equals the specified Term.
     * </p>
     *
     * @param that The Term which might or might not be equal to this one.
     *             Must not be null.
     *
     * @return True if this Term equals that Term,
     *         false if not.
     */
    protected abstract boolean equalsTerm (
                                           TERM that
                                           );


    /**
     * @see musaico.foundation.typing.Term#metadata()
     */
    @Override
    public final ZeroOrOne<Metadata> metadata ()
        throws ReturnNeverNull.Violation
    {
        return this.metadata;
    }


    /**
     * @see musaico.foundation.Term#findMetadatum(java.lang.Class)
     */
    @Override
    public final <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> findMetadatum (
                                            Class<METADATUM> metadatum_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final Metadata metadata = this.metadata ().orNone ();
        if ( metadata.has ( metadatum_class ) )
        {
            return metadata.get ( metadatum_class );
        }

        // Search the Type then up the hierarchy of Namespaces.
        final Type<VALUE> type = this.valueType ();
        return type.findMetadatum ( metadatum_class );
    }


    /** Every AbstractTerm must implement hashCode (). */


    /**
     * @see musaico.foundation.typing.Term#pipe(musaico.foundation.typing.Operation1[])
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<Object>
    public final Term<?> pipe (
                               Value<Operation1<?, ?>> operations
                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  operations );

        if ( ! operations.hasValue () )
        {
            return this;
        }

        final Operation1<?, ?> first_operation = operations.head ().orNone ();
        Operation1<?, ?> previous_operation = first_operation;
        for ( Operation1<?, ?> operation : operations )
        {
            previous_operation = operation;
        }
        final Operation1<?, ?> last_operation = previous_operation;

        final Type<?> input_type = first_operation.type ();
        final Type<?> output_type = last_operation.type ();
        if ( ! this.valueType ().equals ( input_type ) )
        {
            final Unregistered unregistered_operation =
                new Unregistered ( first_operation.id () );
            final TypingViolation violation =
                SymbolMustBeRegistered.CONTRACT
                    .violation ( this,
                                 unregistered_operation );
            final NonBlocking<?> error_value =
                output_type.errorValue ( violation );
            final Term<?> error_term = output_type.instance ( error_value );
            return error_term;
        }

        final Pipe<VALUE, Object> pipe;
        try
        {
            pipe = new Pipe<VALUE, Object> ( "" + this.id ().name ()
                                             + "#anonymous",
                                             this.valueType (),
                                             (Type<Object>) output_type,
                                             operations );
        }
        catch ( TypingViolation violation )
        {
            final NonBlocking<?> error_value =
                output_type.errorValue ( violation );
            final Term<?> error_term = output_type.instance ( error_value );
            return error_term;
        }

        final Term<?> output_term = this.call ( pipe );

        return output_term;
    }


    // Every AbstractTerm implementation must provide <code> value () </code>.


    /**
     * @see musaico.foundation.typing.Term#value(java.lang.Class, musaico.foundation.typing.Tag[])
     */
    @Override
    public final <CAST extends Object>
        Synchronicity<CAST> value (
                                   Class<CAST> target_class,
                                   Tag ... tags
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  target_class, tags );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  tags );

        final Type<CAST> base_type =
            this.valueType ().typeOf ( target_class );
        final Type<CAST> target_type;
        if ( tags.length == 0 )
        {
            target_type = base_type;
        }
        else
        {
            final ZeroOrOne<Type<CAST>> v_target_type = base_type.sub ( tags );

            if ( v_target_type.hasValue () )
            {
                target_type = v_target_type.orNone ();
            }
            else
            {
                final NoType<CAST> no_type = (NoType<CAST>)
                    v_target_type.orNone ();
                final No<CAST> cast_value =
                    new No<CAST> ( target_class,
                                   no_type.violation () );
                // Non-blocking result:
                final Synchronicity<CAST> result =
                    new Synchronous<CAST> ( cast_value );
                return result;
            }
        }

        // Possibly blocking result:
        return this.value ( target_type );
    }


    /**
     * @see musaico.foundation.typing.Term#value(musaico.foundation.typing.Type)
     */
    @Override
    public final <CAST extends Object>
        Synchronicity<CAST> value (
                                   Type<CAST> target_type
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  target_type );

        final Operation1<VALUE, CAST> caster =
            this.valueType ().to ( target_type );

        final List<Term<?>> inputs = new ArrayList<Term<?>> ();
        inputs.add ( this );
        final Expression<CAST> expression =
            new Expression<CAST> ( caster,
                                   inputs,
                                   this.metadata ().orNone ().renew () );
        final Synchronicity<CAST> result = expression.value ();

        return result;
    }


    /**
     * @see musaico.foundation.typing.Term#valueType()
     */
    @Override
    public final Type<VALUE> valueType ()
        throws ReturnNeverNull.Violation
    {
        return this.id ().valueType ();
    }
}
