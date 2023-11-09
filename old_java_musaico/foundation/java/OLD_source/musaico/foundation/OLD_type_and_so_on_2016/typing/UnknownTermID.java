package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A unique identifier for Unknowns within a SymbolTable.
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
public class UnknownTermID<VALUE extends Object>
    extends AbstractTermID<UnknownTermType<VALUE>, Unknown<VALUE>, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new UnknownTermUID which will always be
     * unique within a SymbolTable.
     * </p>
     *
     * @param value_type The Type of value for the Unknown with
     *                   this UnknownTermID, such as a string
     *                   value Type, or an integer value Type, and so on.
     *                   Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "pi" or "x" or "expression#123" and so on.
     *             Must not be null.
     */
    public UnknownTermID (
                          Type<VALUE> value_type,
                          String name
                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               name,
               Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new UnknownTermUID which will always be
     * unique within a SymbolTable.
     * </p>
     *
     * @param value_type The Type of value for the Unknown with
     *                   this UnknownTermID, such as a string
     *                   value Type, or an integer value Type, and so on.
     *                   Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "pi" or "x" or "expression#123" and so on.
     *             Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public UnknownTermID (
                          Type<VALUE> value_type,
                          String name,
                          Visibility visibility
                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( new UnknownTermType<VALUE> ( value_type ),
               name,
               visibility );
    }


    /**
     * <p>
     * Creates a new UnknownTermUID which will always be
     * unique within a SymbolTable.
     * </p>
     *
     * @param term_type The UnknownTermType of the
     *                  Unknown represented by this ID,
     *                  such as an UnknownTermType for a string
     *                  value Type, or an UnknownTermType
     *                  for an integer value Type.
     *                  Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "pi" or "x" or "expression#123" and so on.
     *             Must not be null.
     */
    public UnknownTermID (
                          UnknownTermType<VALUE> term_type,
                          String name
                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( term_type, name, Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new UnknownTermUID which will always be
     * unique within a SymbolTable.
     * </p>
     *
     * @param term_type The UnknownTermType of the
     *                  Unknown represented by this ID,
     *                  such as an UnknownTermType for a string
     *                  value Type, or an UnknownTermType
     *                  for an integer value Type.
     *                  Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "pi" or "x" or "expression#123" and so on.
     *             Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public UnknownTermID (
                          UnknownTermType<VALUE> term_type,
                          String name,
                          Visibility visibility
                          )
        throws ParametersMustNotBeNull.Violation
    {
        super ( term_type, name, visibility );
    }


    /**
     * @see musaico.foundation.typing.SymbolID#rename(java.lang.String, musaico.foundation.typing.Visibility)
     */
    @Override
    public UnknownTermID<VALUE> rename (
                                        String name,
                                        Visibility visibility
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new UnknownTermID<VALUE> ( this.type (),
                                          name,
                                          visibility );
    }
}
