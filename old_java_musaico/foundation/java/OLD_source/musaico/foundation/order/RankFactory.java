package musaico.foundation.order;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Creates a Rank out of each rankable object.
 * </p>
 *
 *
 * <p>
 * In Java every RankFactory must be Serializable in order to play
 * nicely across RMI.
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public interface RankFactory<RANKABLE extends Object, INDEX extends Serializable>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates a new Rank from the specified rankable object.
     * </p>
     *
     * @param rankable The object to convert into a Rank.  Must not be null.
     *
     * @return The Rank of the specified rankable object.  Never null.
     */
    public abstract Rank<INDEX> rank (
            RANKABLE rankable
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
