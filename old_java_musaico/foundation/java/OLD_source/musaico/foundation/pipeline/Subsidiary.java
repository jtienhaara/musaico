package musaico.foundation.pipeline;

import java.io.Serializable;

import java.util.Collection;
import java.util.Set;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A specialized set of methods to add Operations to a parent pipeline,
 * such as Edit or FilterElements or OrderElements and so on.
 * </p>
 *
 * <p>
 * Subsidiarys can be <code> end () </code>ed.  After adding its
 * operations to the parent Pipeline once, the Subsidiary cannot
 * be <code> end () </code>ed a second time; an attempt to do so
 * violates the <code> SubsidiaryMustNotBeEnded.CONTRACT </code>
 * obligation.
 * </p>
 *
 *
 * <p>
 * In Java every Subsidiary must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Subsidiary must be Serializable in order to
 * play nicely across RMI.
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
public interface Subsidiary<STREAM extends Object, SUB extends Subsidiary<STREAM, SUB, PARENT_STREAM, PARENT>, PARENT_STREAM extends Object, PARENT extends Parent<PARENT_STREAM, PARENT>>
    extends Parent<STREAM, SUB>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates a copy of this Subsidiary.
     * </p>
     *
     * <p>
     * The new Subsidiary will have exactly the same operations
     * as this one.
     * </p>
     *
     * <p>
     * No parent or ancestor Pipelines are copied, only this Subsidiary
     * and all of its Subsidiary children (if it as any)
     * that have already been <code> end () </code>ed.
     * </p>
     *
     * <p>
     * Duplication can occur after a Subsidiary has been
     * <code> end () </code>ed.
     * </p>
     *
     * @param parent The parent Pipeline for the new Subsidiary.
     *               Must not be null.
     *
     * @return The new copy of this Subsidiary.  Never null.
     */
    public abstract SUB duplicate (
            PARENT parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The parent Pipeline, with this Subsidiary's operations
     *         all added to it.  Never null.
     */
    public abstract PARENT end ()
        throws SubsidiaryMustNotBeEnded.Violation,
               ReturnNeverNull.Violation;


    // Every Subsidiary must implement java.lang.Object#equals(java.lang.Object)

    // Every Subsidiary must implement java.lang.Object#hashCode()


    /**
     * @return True if this Subsidiary has been <code> end () </code>ed
     *         (and therefore its operations have all been added
     *         to its parent Pipeline) or if it has a parent
     *         Subsidiary which <code> isEnded () </code>;
     *         or false if both this Subsidiary and its parent
     *         are still open for further construction.
     */
    public abstract boolean isEnded ();


    /**
     * @return The parent Pipeline, without any of this Subsidiary's
     *         operations added to it.  Can be invoked any time,
     *         including after <code> end () </code> has sealed
     *         this Subsidiary.  Never null.
     */
    public abstract PARENT parent ();


    // Every Subsidiary must implement java.lang.Object#toString()
}
