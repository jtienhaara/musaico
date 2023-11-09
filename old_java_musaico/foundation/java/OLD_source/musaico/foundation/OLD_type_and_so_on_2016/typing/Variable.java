package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.TrackingContracts;

import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Synchronous;
import musaico.foundation.value.Value;


/**
 * <p>
 * A Term whose value is mutable.
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
public class Variable<VALUE extends Object>
    extends AbstractTerm<VariableTermID<VALUE>, Variable<VALUE>, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Variable.class );


    // Synchronize critical sections on this lock:
    private final Serializable lock = new String ();

    // The READ operation, to read the value of this term.
    // Typically a Type provides return-the-parameter operation,
    // but a Tag might mutate the operation to block private values
    // from being returned, or encrypt confidential data, and so on.
    private final Read<VALUE> read;

    // The WRITE operation, to write the value of this term.
    // Typically a Type provides return-the-parameter operation,
    // but a Tag might mutate the operation to return a special
    // Value implementation that tracks historical values, and
    // so on.
    private final Write<VALUE> write;

    // The current Value of this Variable.
    // *** Can change over time. ***
    private NonBlocking<VALUE> value;


    /**
     * <p>
     * Creates a new Variable with the specified value Type and initial Value,
     * and StandardMetadata.
     * </p>
     *
     * <p>
     * The new Variable will have a default, generated TermID.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * <p>
     * The value Type can have a "write" Operation to track value history and
     * so on.
     * </p>
     *
     * @param value_type The Type of Variable value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Can have a "write" Operation to write the mutable
     *                   value (default implementation is
     *                   <code> musaico.foundation.typing.Write </code>,
     *                   but this can be mutated by Tags to track value
     *                   history and so on).  The "write" Operation takes
     *                   the new value as input, and returns the value
     *                   to store inside this Variable as output.
     *                   Must not be null.
     *
     * @param initial_value The initial value of this Variable.
     *                      Can be No<VALUE>.  Must not be null.
     */
    public Variable (
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> initial_value
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               initial_value,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new Variable with the specified value Type and initial Value,
     * and the specified Metadata.
     * </p>
     *
     * <p>
     * The new Variable will have a default, generated TermID.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * <p>
     * The value Type can have a "write" Operation to track value history and
     * so on.
     * </p>
     *
     * @param value_type The Type of Variable value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Can have a "write" Operation to write the mutable
     *                   value (default implementation is
     *                   <code> musaico.foundation.typing.Write </code>,
     *                   but this can be mutated by Tags to track value
     *                   history and so on).  The "write" Operation takes
     *                   the new value as input, and returns the value
     *                   to store inside this Variable as output.
     *                   Must not be null.
     *
     * @param initial_value The initial value of this Variable.
     *                      Can be No<VALUE>.  Must not be null.
     *
     * @param metadata The Metadata for this Variable, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Op<A, B> - Op<VAL, VAL>.
    public Variable (
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> initial_value,
                     Metadata metadata
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Variable.generateName ( value_type ),
               value_type,
               initial_value,
               metadata );
    }


    /**
     * <p>
     * Creates a new Variable with the specified value Type and initial Value,
     * and StandardMetadata.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * <p>
     * The value Type can have a "write" Operation to track value history and
     * so on.
     * </p>
     *
     * @param name The name which will be used to create a TermID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Variable value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Can have a "write" Operation to write the mutable
     *                   value (default implementation is
     *                   <code> musaico.foundation.typing.Write </code>,
     *                   but this can be mutated by Tags to track value
     *                   history and so on).  The "write" Operation takes
     *                   the new value as input, and returns the value
     *                   to store inside this Variable as output.
     *                   Must not be null.
     *
     * @param initial_value The initial value of this Variable.
     *                      Can be No<VALUE>.  Must not be null.
     */
    public Variable (
                     String name,
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> initial_value
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name,
               value_type,
               initial_value,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new Variable with the specified value Type and initial Value,
     * and the specified Metadata.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * <p>
     * The value Type can have a "write" Operation to track value history and
     * so on.
     * </p>
     *
     * @param name The name which will be used to create a TermID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Variable value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Can have a "write" Operation to write the mutable
     *                   value (default implementation is
     *                   <code> musaico.foundation.typing.Write </code>,
     *                   but this can be mutated by Tags to track value
     *                   history and so on).  The "write" Operation takes
     *                   the new value as input, and returns the value
     *                   to store inside this Variable as output.
     *                   Must not be null.
     *
     * @param initial_value The initial value of this Variable.
     *                      Can be No<VALUE>.  Must not be null.
     *
     * @param metadata The Metadata for this Variable, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Op<A, B> - Op<VAL, VAL>.
    public Variable (
                     String name,
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> initial_value,
                     Metadata metadata
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new VariableTermID<VALUE> (
                    new VariableTermType<VALUE> ( value_type ),
                    name ),
                metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_type, initial_value, metadata );

        this.value = initial_value;

        this.read = Variable.getCreateReadOperation ( value_type );

        // The Write operation, by default, returns the value as-is.
        // However it can be mutated by Tags to, for example, protect
        // private data, or encrypt confidential values.
        final OperationID<Operation1<VALUE, VALUE>, VALUE> write_id =
            new OperationID<Operation1<VALUE, VALUE>, VALUE> ( "write",
                new OperationType1<VALUE, VALUE> ( value_type,
                                                   value_type ) );

        Operation1<VALUE, VALUE> possible_write =
            value_type.symbol ( write_id ).orNull ();

        if ( possible_write == null )
        {
            this.write =
                new Write<VALUE> ( write_id, null ); // No pre-processor.
        }
        else if ( possible_write instanceof Write )
        {
            this.write = (Write<VALUE>) possible_write;
        }
        else
        {
            // Make sure the write value is always a NonBlocking.
            this.write = new Write<VALUE> ( possible_write );
        }
    }




    /**
     * <p>
     * Creates an auto-generated name for a Variable with the
     * specified Value.
     * </p>
     */
    protected static final <VARIABLE extends Object>
        String generateName (
                             Type<VARIABLE> value_type
                             )
    {
        final String name =
            "var#"
            + System.nanoTime()
            + "("
            + value_type.id ().name ()
            + ")";

        return name;
    }


    /**
     * <p>
     * Retrieves the Read Operation from the specified Type,
     * or creates a new generic one if the Type does not provide one.
     * </p>
    */
    private static final <READABLE extends Object>
        Read<READABLE> getCreateReadOperation (
                                               Type<READABLE> value_type
                                               )
    {
        // The Read operation, by default, returns the value as-is.
        // However it can be mutated by Tags to, for example, protect
        // private data, or encrypt confidential values.
        final OperationID<Operation1<READABLE, READABLE>, READABLE> read_id =
            new OperationID<Operation1<READABLE, READABLE>, READABLE> ( "read",
                new OperationType1<READABLE, READABLE> ( value_type,
                                                         value_type ) );

        Operation1<READABLE, READABLE> possible_read =
            value_type.symbol ( read_id ).orNull ();

        final Read<READABLE> read;
        if ( possible_read == null )
        {
            read =
                new Read<READABLE> ( read_id, null ); // No pre-processor.
        }
        else if ( possible_read instanceof Read )
        {
            read = (Read<READABLE>) possible_read;
        }
        else
        {
            // Make sure the read value is always a NonBlocking.
            read = new Read<READABLE> ( possible_read );
        }

        return read;
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
    protected boolean equalsTerm (
                                  Variable<VALUE> that
                                  )
    {
        // The Types have already been determined to match by
        // the time we get here.

        final NonBlocking<VALUE> this_value;
        synchronized ( this.lock )
        {
            this_value = this.value;
        }

        final NonBlocking<VALUE> that_value;
        synchronized ( that.lock )
        {
            that_value = that.value;
        }

        if ( ! this_value.equals ( that_value ) )
        {
            // That Variable has a different value.
            return false;
        }
        else if ( ! this.read.equals ( that.read ) )
        {
            // Different read operation.  We cannot return true if
            // the values are the same, but one is unencrypted and the
            // other has an encrypt Read operation (for example).  That
            // would allow a brute force discovery of the encrypted value,
            // making the special Read operation pointless.
            return false;
        }
        else if ( ! this.write.equals ( that.write ) )
        {
            // Different write operation.
            return false;
        }
        else
        {
            // Everything is all matchy-matchy.
            return true;
        }
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Variable<VALUE> rename (
                                   String name
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Variable<VALUE> ( name,
                                     this.valueType (),
                                     this.value,
                                     this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Variable<VALUE> retype (
                                   String name,
                                   AbstractTermType<? extends Term<VALUE>, VALUE> type
                                   )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Variable<VALUE> ( name,
                                     type.valueType (),
                                     this.value,
                                     this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Term#value()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Op<?, ?> - Op<VALUE, VALUE>.
    public final Synchronous<VALUE> value ()
        throws ReturnNeverNull.Violation
    {
        final NonBlocking<VALUE> input_to_read;
        synchronized ( this.lock )
        {
            input_to_read = this.value;
        }

        final NonBlocking<VALUE> value_to_return =
            this.read.evaluate ( input_to_read );

        final Synchronous<VALUE> result =
            new Synchronous<VALUE> ( value_to_return );

        return result;
    }


    /**
     * <p>
     * Overwrites the current value stored by this Variable.
     * </p>
     *
     * @param new_value The new value to set this Variable to.
     *                  Can be a No<VALUE>.  Must not be null.
     *
     * @return The previous value of this Variable.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Op<?, ?> - Op<VALUE, VALUE>.
    public final NonBlocking<VALUE> value (
                                           NonBlocking<VALUE> new_value
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  new_value );

        final NonBlocking<VALUE> old_value;
        if ( this.read == null )
        {
            // Return the value as-is.
            synchronized ( this.lock )
            {
                old_value = this.value;

                // The write is done inside the synchronized block,
                // to ensure that "write" Operations do not get
                // interleaved and mess up value history.
                if ( this.write == null )
                {
                    // Set the value as-is.
                    this.value = new_value;
                }
                else
                {
                    // set the value to the output of the "write" Operation.
                    this.value = this.write.evaluate ( new_value );
                }
            }
        }
        else
        {
            // Return the value return by the "read" Operation.
            final NonBlocking<VALUE> input_to_read;
            synchronized ( this.lock )
            {
                input_to_read = this.value;

                // The write is done inside the synchronized block,
                // to ensure that "write" Operations do not get
                // interleaved and mess up value history.
                if ( this.write == null )
                {
                    // Set the value as-is.
                    this.value = new_value;
                }
                else
                {
                    // set the value to the output of the "write" Operation.
                    this.value = this.write.evaluate ( new_value );
                }
            }

            old_value = this.read.evaluate ( input_to_read );
        }

        return old_value;
    }
}
