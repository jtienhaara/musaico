package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.No;
import musaico.foundation.value.Synchronous;


/**
 * <p>
 * An unknown value (free variable) Term.
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
public class Unknown<VALUE extends Object>
    extends AbstractTerm<UnknownTermID<VALUE>, Unknown<VALUE>, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Unknown.class );


    // The No value returned by this Unknown.
    private final Synchronous<VALUE> returnSynchronousValue;


    /**
     * <p>
     * Creates a new Unknown with the specified value Type,
     * and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Unknown will have a default, generated TermID.
     * </p>
     *
     * @param value_type The Type of Unknown value to create.
     *                   Must not be null.
     */
    public Unknown (
                    Type<VALUE> value_type
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Unknown with the specified value Type
     * and metadata.
     * </p>
     *
     * <p>
     * The new Unknown will have a default, generated TermID.
     * </p>
     *
     * @param value_type The Type of Unknown value to create.
     *                   Must not be null.
     *
     * @param metadata The Metadata for this Unknown, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Unknown (
                    Type<VALUE> value_type,
                    Metadata metadata
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Unknown.generateName (),
               value_type,
               metadata );
    }


    /**
     * <p>
     * Creates a new Unknown with the specified name
     * and value Type, and a StandardMetadata.
     * </p>
     *
     * @param name The name which will be used to create a TermID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Unknown value to create.
     *                   Must not be null.
     */
    public Unknown (
                    String name,
                    Type<VALUE> value_type
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, value_type, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Unknown with the specified value Type
     * and metadata.
     * </p>
     *
     * @param name The name which will be used to create a TermID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Unknown value to create.
     *                   Must not be null.
     *
     * @param metadata The Metadata for this Unknown, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Op<A, B> - Op<VAL, VAL>.
    public Unknown (
                    String name,
                    Type<VALUE> value_type,
                    Metadata metadata
                    )
        throws ParametersMustNotBeNull.Violation
    {
        super ( new UnknownTermID<VALUE> (
                    new UnknownTermType<VALUE> ( value_type ),
                    name ),
                metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value_type, metadata );

        final TypingViolation violation =
            TermMustNotBeUnknown.CONTRACT.violation ( value_type, this );
        final No<VALUE> value_to_return = value_type.noValue ( violation );
        this.returnSynchronousValue =
            new Synchronous<VALUE> ( value_to_return );
    }


    /**
     * <p>
     * Creates an auto-generated name for an Unknown.
     * </p>
     */
    protected static final String generateName ()
    {
        return "(Unknown#"
            + System.nanoTime ()
            + ")";
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
                                  Unknown<VALUE> that
                                  )
    {
        // We already know we have the same id
        // (AbstractSymbol.equals ())
        // and value Type (AbstractTerm.equalsSymbol () ).
        // Now we also know that the other term is also
        // an Unknown.  So everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Unknown<VALUE> rename (
                                  String name
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Unknown<VALUE> ( name,
                                    this.valueType (),
                                    this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    public Unknown<VALUE> retype (
                                  String name,
                                  AbstractTermType<? extends Term<VALUE>, VALUE> type
                                  )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Unknown<VALUE> ( name,
                                    type.valueType (),
                                    this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Term#value()
     */
    @Override
    public final Synchronous<VALUE> value ()
        throws ReturnNeverNull.Violation
    {
        // No value to return.
        return this.returnSynchronousValue;
    }
}
