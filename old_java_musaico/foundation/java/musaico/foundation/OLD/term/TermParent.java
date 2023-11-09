package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.pipeline.Parent;


/**
 * <p>
 * Either a TermPipeline or Subsidiary, both of which can be
 * the parent of another Term Subsidiary.
 * </p>
 *
 *
 * <p>
 * In Java every Parent must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Parent must be Serializable in order to
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface TermParent<ELEMENT extends Object, PARENT extends Parent<ELEMENT, PARENT>>
    extends Parent<ELEMENT, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @return An OperationPipeline representing the sequence
     *         of operations in this pipeline or subsidiary
     *         which will be applied to the input Term.
     *         The internal operations (if any) are duplicated,
     *         including the operations of all Subsidiaries
     *         of this parent that have already been
     *         <code> end () </code>ed (if any), but excluding
     *         operations of any Subsidiaries that have
     *         not yet been <code> end () </code>ed (if any),
     *         and excluding any parent or ancestor operations.
     *         To build the whole tree, call <code> operations () </code>
     *         on the toplevel TermParent, after all Subsidiaries,
     *         such as Edits, Forks, Joins, OrderBys, Selects,
     *         Whens and Wheres, have been <code> end () </code>ed.
     *         Note that the OperationPipeline
     *         returned by certain TermParents will be empty
     *         of any operations: for example, a Term returns
     *         an empty OperationPipeline,  Typically such
     *         TermParents will simply return the <code> type () </code>
     *         of the TermPipeline, since Type is itself
     *         an empty OperationPipeline.  Never null.
     */
    public abstract Parent<ELEMENT, ?> operations ()
        throws ReturnNeverNull.Violation;


    // Every Parent must implement java.lang.Object#equals(java.lang.Object)

    // Every Parent must implement java.lang.Object#hashCode()

    // Every Parent must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()

    // Every Parent must implement java.lang.Object#toString()
}
