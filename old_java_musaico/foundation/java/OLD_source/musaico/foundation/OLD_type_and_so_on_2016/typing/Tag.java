package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.StandardMetadata;


/**
 * <p>
 * A Tag specifies one or more aspects of a Type.
 * </p>
 *
 * <p>
 * A Musaico Type can have Tags attached to it to mutate its behaviour.
 * </p>
 *
 * <p>
 * For example, one Type might represent the Integer class in Java,
 * while the sub-Type with a LengthTag might represent arrays of Integers
 * of a specific length, and the sub-Type with a PrivateTag might
 * represent Integers which cannot be read by normal means once
 * they are instantiated.
 * </p>
 *
 * <p>
 * Tags can be added to Types in various combinations, always resulting
 * in distinct Types which must be cast in order to exchange values.
 * For example in Java a single Integer can be cast to a tuple of exactly
 * 3 Integers casting from the Integer Type to the sub-Type with
 * LengthTag [3].  Or a String readable value can be cast to an unreadable
 * String with the Private tag.  And so on.
 * </p>
 *
 * <p>
 * A Tag provides Symbols which are adopted by each tagged Type.
 * For example a Tag might provide one or more
 * Constraints, which restrict the Values allowed for a Type.
 * Or a Tag might provide a mutate method to modify Operations and
 * other Symbols of a Type.  Or a Tag might provide additional
 * Operations to the Type.  And so on.  Typically a Tag has a SymbolTable
 * which manages the Symbols, and simply adds the appropriate
 * behaviour modification Symbols to the SymbolTable.  When a
 * Type is sub-Typed, the Type's Symbols are optionally mutated by
 * the Tag, and then its Symbols are added to the sub-Type's SymbolTable.
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
public interface Tag
    extends Namespace, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The Type of all Tags. */
    public static final TagType TYPE =
        Namespace.ROOT.registerSymbolType (
            new TagType ( "tag",
                          new SymbolTable (),
                          new StandardMetadata () ) );

    /** The "no tag" tag.  Does absolutely nothing. */
    public static final NoTag NONE =
        new NoTag ( Namespace.ROOT, "none" );


    /**
     * @see musaico.foundation.typing.Symbol#id()
     */
    @Override
    public abstract TagID id ()
        throws ReturnNeverNull.Violation;


    /** Every Tag must implement all the methods of Namespace. */

    /** Every Tag must implement hashCode (), equals () and toString (). */


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public abstract Tag rename (
                                String name,
                                SymbolTable symbol_table
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
