package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Symbol is the basic atom of a typing environment.  Tags (including Types)
 * and Operations are Symbols.
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
public interface Symbol
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The unique identifier for this Symbol.  Within a single
     *         SymbolTable, this Symbol's id is unique.  For example,
     *         a Type or Tag has a unique id within a Namespace's
     *         SymbolTable, or an Operation has a unique id within a
     *         Type's SymbolTable, and so on.  Never null.
     */
    public abstract SymbolID<? extends Symbol> id ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates an exact copy of this Symbol, but with the specified
     * ID name.
     * </p>
     *
     * @param name The name to use for the SymbolID of the Symbol
     *             to be created.  Must not be null.
     *
     * @return A newly created duplicate of this Symbol,
     *         with the specified id name.  Never null.
     */
    public abstract Symbol rename (
                                   String name
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The Type of this Symbol.  Convenience method for
     *         <code> id ().type () </code>.
     *         Every type of symbol also implements SymbolType.
     *         To case back to a Type, use asType().
     *         Many Symbol implementations override this method to
     *         provide a more specific return value, such as
     *         Tag.TYPE and so on.  Never null.
     */
    public abstract SymbolType type ()
        throws ReturnNeverNull.Violation;
}
