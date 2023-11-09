package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Value;


/**
 * <p>
 * A Contract governing Values, such as a requirement that
 * every value be a String of 1 or more characters, or that
 * the value be a Multiple with exactly 3 elements, and so on.
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
public interface Constraint
    extends Contract<Value<?>, TypingViolation>, Symbol, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Type describing all Constraints. */
    public static final ConstraintType TYPE =
        Namespace.ROOT.registerSymbolType (
            new ConstraintType ( "constraint",
                                 new SymbolTable (),
                                 new StandardMetadata () ) );

    /** No Constraint.  Does nothing.
     * Do not register NONE objects in any namespace. */
    public static final Constraint NONE =
        new NoConstraint ( "none" );


    /** Every Constraint must implement all the methods of Contract. */

    /** Every Constraint must implement all the methods of Symbol. */


    /**
     * @see musaico.foundation.typing.Symbol#id()
     */
    @Override
    public abstract ConstraintID id ()
        throws ReturnNeverNull.Violation;
}
