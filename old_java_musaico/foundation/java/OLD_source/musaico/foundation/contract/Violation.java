package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * A breach of contract, typically implemented either as a checked
 * Exception or as an unchecked RuntimeException.
 * </p>
 *
 *
 * <p>
 * In Java every Violation must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.contract.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.MODULE#LICENSE
 */
public interface Violation
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return The contract which was breached.  Never null.
     */
    public abstract Contract<?, ?> contract ();


    /**
     * @return A description of this particular Violation of a Contract,
     *         detailing WHY the Contract was violated.  For example,
     *         if a Contract stipulates that an array must contain
     *         no nulls, but a particular instance of an array contains
     *         a null value at the 13th and 17th elements of the array,
     *         then the Violation of that Contract might provide
     *         a description something like "Null values # 13 and # 17",
     *         or something similar.  The purpose of this description is
     *         to provide clues to developers, testers and maintainers
     *         during troubleshooting, tracing and debugging efforts.
     *         This description is not intended for users, so it is
     *         not internationalized and localized.  It is generally
     *         helpful to include some information about the stack
     *         trace in this description.
     *         Never null.
     */
    public abstract String description ();


    /**
     * <p>
     * Returns the object which was under contract (or a
     * Serializable representation of it, in case it was not
     * Serializable itself).
     * </p>
     *
     * @return The object under contract, or its Serializable representation.
     *         Never null.
     */
    public abstract Serializable plaintiff ();


    /**
     * <p>
     * Returns the evidence which caused the contract to be
     * breached, possibly including Serializable representations of
     * evidence which was not Serializable.
     * </p>
     *
     * <p>
     * If the contract took one evidence, then an array of
     * size one is returned.  If it took an array, or varargs, then
     * the appropriate length array is returned.
     * </p>
     *
     * @return The evidence which violated the contract,
     *         or Serializable representation(s) of that data.
     *         Never null.  Never contains any null elements.
     */
    public abstract Serializable evidence ();
}
