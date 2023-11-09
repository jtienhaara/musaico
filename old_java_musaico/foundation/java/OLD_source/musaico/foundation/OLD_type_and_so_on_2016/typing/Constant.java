package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Synchronous;


/**
 * <p>
 * An immutable Term, with a Type and a non-blocking Value.
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
public class Constant<VALUE extends Object>
    extends AbstractTerm<ConstantTermID<VALUE>, Constant<VALUE>, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Constant.class );


    // The Value of this Constant.
    private final NonBlocking<VALUE> value;

    // The READ operation, to read the value of this term.
    // Typically a Type provides return-the-parameter operation,
    // but a Tag might mutate the operation to block private values
    // from being returned, or encrypt confidential data, and so on.
    private final Read<VALUE> read;


    /**
     * <p>
     * Creates a new Constant with the specified value Type
     * and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Constant will have a default, generated Term ID.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * @param value_type The Type of Constant value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Must not be null.
     *
     * @param value The fixed value of this Constant.  Must not be null.
     */
    public Constant (
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> value
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type, value, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Constant with the specified value Type
     * and non-blocking Value.
     * </p>
     *
     * <p>
     * The new Constant will have a default, generated Term ID.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * @param value_type The Type of Constant value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Must not be null.
     *
     * @param value The fixed value of this Constant.  Must not be null.
     *
     * @param metadata The Metadata for this Constant, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Constant (
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> value,
                     Metadata metadata
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Constant.generateName ( value_type, value ),
               value_type,
               value,
               metadata );
    }


    /**
     * <p>
     * Creates a new Constant with the specified name,
     * value Type and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Constant value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Must not be null.
     *
     * @param value The fixed value of this Constant.  Must not be null.
     */
    public Constant (
                     String name,
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> value
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, value_type, value, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Constant with the specified value Type
     * and non-blocking Value.
     * </p>
     *
     * <p>
     * The value Type can have a "read" Operation and a "read_metadata"
     * operation to secure or protect the value and metadata, respectively.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Constant value to create.
     *                   Can have a "read" Operation to read
     *                   the fixed value (default implementation is
     *                   <code> musaico.foundation.typing.Read </code>,
     *                   but this can be mutated by Tags to protect
     *                   private data, or to encrypt confidential values,
     *                   and so on).  The "read" Operation takes the fixed
     *                   value as input, and outputs the value to return
     *                   to callers of <code> Term.value () </code>.
     *                   Must not be null.
     *
     * @param value The fixed value of this Constant.  Must not be null.
     *
     * @param metadata The Metadata for this Constant, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Op<A, B> - Op<VAL, VAL>.
    public Constant (
                     String name,
                     Type<VALUE> value_type,
                     NonBlocking<VALUE> value,
                     Metadata metadata
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new ConstantTermID<VALUE> (
                    new ConstantTermType<VALUE> ( value_type ),
                    name ),
                metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_type, value, metadata );

        this.value = value;
        this.read = Constant.getCreateReadOperation ( value_type );
    }


    /**
     * <p>
     * Creates an auto-generated name for a Constant with the
     * specified Value.
     * </p>
     */
    protected static final <CONSTANT extends Object>
        String generateName (
                             Type<CONSTANT> value_type,
                             NonBlocking<CONSTANT> original_value
                             )
    {
        // Read in the value and convert it to a String.
        final Read<CONSTANT> read =
            Constant.getCreateReadOperation ( value_type );
        final NonBlocking<CONSTANT> value = read.evaluate ( original_value );

        return "" + value;
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
            new OperationID<Operation1<READABLE, READABLE>, READABLE> (
                "read",
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
                                  Constant<VALUE> that
                                  )
    {
        if ( ! this.value.equals ( that.value ) )
        {
            // That Constant has a different value.
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
    public Constant<VALUE> rename (
                                   String name
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Constant<VALUE> ( name,
                                     this.valueType (),
                                     this.value,
                                     this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Constant<VALUE> retype (
                                   String name,
                                   AbstractTermType<? extends Term<VALUE>, VALUE> type
                                   )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Constant<VALUE> ( name,
                                     type.valueType (),
                                     this.value,
                                     this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Term#value()
     */
    @Override
    public final Synchronous<VALUE> value ()
        throws ReturnNeverNull.Violation
    {
        // Return the value return by the "read" Operation.
        final NonBlocking<VALUE> value_to_return =
            this.read.evaluate ( this.value );

        final Synchronous<VALUE> result =
            new Synchronous<VALUE> ( value_to_return );

        return result;
    }
}
