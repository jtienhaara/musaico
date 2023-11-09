package musaico.foundation.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Minimalist interface providing the intersection between
 * a full Pipeline and a Subsidiary (which doesn't amount to much
 * of an interface), since either of those varieties can be the parent
 * of a Subsidiary.
 * </p>
 *
 *
 * <p>
 * In Java every Parent must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
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
 * @see musaico.foundation.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.pipeline.MODULE#LICENSE
 */
public interface Parent<STREAM extends Object, SUB_OR_PIPELINE extends Parent<STREAM, SUB_OR_PIPELINE>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Parent must implement java.lang.Object#equals(java.lang.Object)

    // Every Parent must implement java.lang.Object#hashCode()


    /**
     * @return This Subsidiary or Pipeline as the SUB_OR_PIPELINE
     *         it claims to implement.  Never null.
     */
    public abstract SUB_OR_PIPELINE thisPipeline ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement java.lang.Object#toString()
}
