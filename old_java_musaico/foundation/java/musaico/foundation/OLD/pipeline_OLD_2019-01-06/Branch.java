package musaico.foundation.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * A major Subsidiary which provides all the functionality of a
 * Pipeline.
 * </p>
 *
 * <p>
 * A Branch need not handle the same class of STREAM elements as its
 * parent Pipeline.  For example, a Pipeline of Strings might provide
 * a Branch to manipulate individual characters; or a Pipeline of
 * <code> byte [] </code> arrays might provide a Branch to manipulate
 * individual bytes; or a more complex structure might provide
 * a Branch to manipulate components or fields; and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
 * play nicely across RMI.  However users of the Pipeline
 * must be careful, since the values and expected data stored inside
 * (if any) might not be Serializable.
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
public interface Branch<SUB_STREAM extends Object, BRANCH extends Branch<SUB_STREAM, BRANCH, PARENT_STREAM, PARENT>, PARENT_STREAM extends Object, PARENT extends Pipeline<PARENT_STREAM, PARENT>>
    extends Pipeline<SUB_STREAM, BRANCH>, Subsidiary<SUB_STREAM, BRANCH, PARENT_STREAM, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#copy(musaico.foundation.pipeline.Pipeline)

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#edit()

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#end()

    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Pipeline must implement java.lang.Object#hashCode()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])

    // Every Subsidiary must implement
    // musaico.foundation.pipeline.Subsidiary#parent()


    /**
     * @return The previous segment of this Branch, if any;
     *         or null, if this is the first segment in the Branch.
     *         Idempotent: always returns the same result.
     *         Can be called any time, including after <code> end () </code>
     *         has been called to seal this Branch.  CAN BE NULL.
     */
    public abstract BRANCH previousOrNull ();


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()

    // Every Pipeline must implement java.lang.Object#toString()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
