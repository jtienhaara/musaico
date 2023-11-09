package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A unique identifier for Terms (Constants, Expressions, Variables,
 * and so on) within a SymbolTable.
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
public abstract class AbstractTermID<TERM_TYPE extends AbstractTermType<TERM, VALUE>, TERM extends Term<VALUE>, VALUE extends Object>
    extends SymbolID<TERM>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks parameters to constructors and static methods for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractTermID.class );


    // The Type of Term represented by this ID (which includes its value Type).
    private final TERM_TYPE termType;


    /**
     * <p>
     * Creates a new AbstractTermID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param term_type The Type of the Term represented
     *                  by this ID, such as a ConstantType for a string
     *                  value Type, or an ExpressionType for a sequence of
     *                  Operations which outputs an integer value Type.
     *                  Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "pi" or "x" or "expression#123" and so on.
     *             Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public AbstractTermID (
                           TERM_TYPE term_type,
                           String name,
                           Visibility visibility
                           )
        throws ParametersMustNotBeNull.Violation
    {
        super ( term_type, name, visibility );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               term_type );

        this.termType = term_type;
    }


    // Every AbstractTermID must implement rename ( String ).


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        // Don't output the Type for a Term, the way the standard
        // SymbolID does.
        // SymbolID: type "name"
        // TermID;   name
        return this.name ();
    }


    /**
     * @see musaico.foundation.typing.SymbolID#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Implicit TERM_TYPE-SYMBOL_TYPE cast.
    public TERM_TYPE type ()
    {
        return this.termType;
    }


    /**
     * @return The type of Value which a Term with this ID evaluates to,
     *         such as a string Type, or an integer Type, and so on.
     *         Never null.
     */
    public final Type<VALUE> valueType ()
    {
        return this.termType.valueType ();
    }
}
