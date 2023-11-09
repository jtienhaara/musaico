package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A unique identifier within a SymbolTable.
 * </p>
 *
 *
 * <p>
 * In Java every SymbolID must be Serializable in order to
 * play nicely with RMI.
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
public class SymbolID<SYMBOL extends Symbol>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SymbolID.class );


    public static final SymbolID<Symbol> NONE =
        new SymbolID<Symbol> ( SymbolType.NONE, "none", Visibility.NONE );


    // The Type of Symbol represented by this id, such as an
    // OperationType or Type.TYPE or Tag.TYPE and so on.
    // Always also a SymbolType, even though we can't represent
    // that here in the object variable.
    private final Type<SYMBOL> type;

    // The String name of this unique identifier.
    private final String name;

    // The Visibility of this unique identifier (PUBLIC, PRIVATE and so on).
    private final Visibility visibility;


    /**
     * <p>
     * Creates a new SymbolID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param symbol_type The Type of Symbol represented, such as
     *                    an OperationType or Type.TYPE or Tag.TYPE
     *                    and so on.  Must also be a SymbolType.
     *                    Must not be null.
     *
     * @param name The String name of this identifier, such as "doSomething" or
     *             "number" or "PrivateTag" and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public <SYMBOL_TYPE extends Type<SYMBOL> & SymbolType>
        SymbolID (
                  SYMBOL_TYPE symbol_type,
                  String name,
                  Visibility visibility
                  )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_type, name, visibility );

        this.type = symbol_type;
        this.name = name;
        this.visibility = visibility;
    }


    /**
     * <p>
     * Copy constructor.
     * </p>
     *
     * @param symbol_id The SymbolID whose Type will be copied.
     *                    Must not be null.
     *
     * @param name The String name of this identifier, such as "doSomething" or
     *             "number" or "PrivateTag" and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Type<S> - SYMBOL_TYPE.
    private <SYMBOL_TYPE extends Type<SYMBOL> & SymbolType>
        SymbolID (
                  SymbolID<SYMBOL> symbol_id,
                  String name,
                  Visibility visibility
                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( symbol_id == null
                   ? null
                   : (SYMBOL_TYPE) symbol_id.type (),
               name,
               visibility );
    }


    /**
     * @return The String name of this SymbolID.  Never null.
     *
     * Final for speed.
     */
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Final for speed.
     */
    @Override
    public final int hashCode ()
    {
         return this.type.hashCode ()
             + this.name.hashCode ()
             + this.visibility.hashCode ();
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
       if ( object == null )
        {
            // Any SymbolID != null.
            return false;
        }
        else if ( ! ( object instanceof SymbolID ) )
        {
            // Any SymbolID != any other class of object.
            return false;
        }

        SymbolID<?> that = (SymbolID) object;
        if ( ! this.type.equals ( that.type ) )
        {
            // SymbolID<Operation> != SymbolID<Type> and so on.
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
        {
            // SymbolID "foo" != SymbolID "bar" and so on.
            return false;
        }
        else if ( ! this.visibility.equals ( that.visibility ) )
        {
            // SymbolID with visibility PUBLIC
            //     != SymbolID with visibility PRIVATE and so on.
            return false;
        }

        return true;
    }


    /**
     * <p>
     * Creates an exact copy of this SymbolID, but with the specified
     * name and Visibility.
     * </p>
     *
     * @param name The name to use for the SymbolID to be created.
     *             Must not be null.
     *
     * @param visibility The Visibility for the SymbolID to be created,
     *                   such as Visibility.PUBLIC and so on.
     *                   Must not be null.
     *
     * @return A newly created duplicate of this SymbolID,
     *         with the specified name.  Never null.
     */
    public SymbolID<SYMBOL> rename (
                                    String name,
                                    Visibility visibility
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new SymbolID<SYMBOL> ( this,
                                      name,
                                      visibility );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.type.id ().name () + " \"" + this.name () + "\"";
    }


    /**
     * @return The Type of Symbol represented by this identifier.
     *         Every type also implements SymbolType.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Type is always also a SymbolType.
    public <SYMBOL_TYPE extends Type<SYMBOL> & SymbolType>
        SYMBOL_TYPE type ()
        throws ReturnNeverNull.Violation
    {
        return (SYMBOL_TYPE) this.type;
    }


    /**
     * @return The Visibility of this SymbolID (PUBLIC, PRIVATE, and so on).
     *         Never null.
     */
    public final Visibility visibility ()
        throws ReturnNeverNull.Violation
    {
        return this.visibility;
    }
}
