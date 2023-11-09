package musaico.foundation.term.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.EditSequence;
import musaico.foundation.pipeline.Parent;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.TermParent;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Dumb implementation of EditSequence for Terms, relying pretty much entirely
 * on an OperationPipeline "slave" to do all the heavy lifting.
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
public class TermEditSequence<
                 VALUE extends Object,
                 PARENT extends Parent<
                     VALUE,
                     PARENT
                     >
                 >
    extends AbstractTermSubsidiary<
                VALUE,
                TermEditSequence<
                    VALUE,
                    PARENT
                    >,
                VALUE,
                PARENT,
                EditSequence<
                    VALUE,
                    ?,
                    TermPipeline.TermSink<
                        VALUE
                        >,
                    ?
                    >
                >
    implements EditSequence<
                   VALUE,
                   PARENT,
                   TermPipeline.TermSink<
                       VALUE
                       >,
                   TermEditSequence<
                       VALUE,
                       PARENT
                       >
                   >,
               Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermEditSequence.
     * </p>
     *
     * @param parent The parent of this Subsidiary.  Must not be null.
     *
     * @param slave The EditSequence operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermEditSequence (
            TermParent<VALUE, PARENT> parent,
            EditSequence<VALUE, ?, TermPipeline.TermSink<VALUE>, ?> slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        super ( parent,  // parent
                slave ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast PARENT - TermParent<V, P>.
    public final TermEditSequence<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return this.duplicateUgly ( (TermParent<VALUE, PARENT>)
                                    duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends EditSequence<VALUE, SLAVE_PARENT, TermPipeline.TermSink<VALUE>, SLAVE>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermEditSequence<VALUE, PARENT> duplicateUgly (
            TermParent<VALUE, PARENT> duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new TermEditSequence<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.EditSequence#sequence(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public final PARENT sequence (
            TermPipeline.TermSink<VALUE> sequence
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().sequence ( sequence );
        return this.end ();
    }


    /**
     * @see musaico.foundation.pipeline.EditSequence#sequence(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public final PARENT sequence (
            VALUE... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        this.slave ().sequence ( sequence );
        return this.end ();
    }
}
