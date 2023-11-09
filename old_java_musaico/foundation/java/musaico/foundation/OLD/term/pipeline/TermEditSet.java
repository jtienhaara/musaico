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

import musaico.foundation.pipeline.EditSet;
import musaico.foundation.pipeline.Parent;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.TermParent;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Dumb implementation of EditSet for Terms, relying pretty much entirely
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
public class TermEditSet<
                 VALUE extends Object,
                 PARENT extends Parent<
                     VALUE, PARENT
                     >
                 >
    extends AbstractTermSubsidiary<
                VALUE,
                TermEditSet<
                    VALUE,
                    PARENT
                    >,
                VALUE,
                PARENT,
                EditSet<
                    VALUE,
                    ?,
                    TermPipeline.TermSink<
                        VALUE
                        >,
                    ?>
                >
    implements EditSet<
                   VALUE,
                   PARENT,
                   TermPipeline.TermSink<
                       VALUE
                   >,
                   TermEditSet<
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
     * Creates a new TermEditSet.
     * </p>
     *
     * @param parent The parent of this Subsidiary.  Must not be null.
     *
     * @param slave The EditSet operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermEditSet (
            TermParent<VALUE, PARENT> parent,
            EditSet<VALUE, ?, TermPipeline.TermSink<VALUE>, ?> slave
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
    public final TermEditSet<VALUE, PARENT> duplicate (
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
    private final <SLAVE extends EditSet<VALUE, SLAVE_PARENT, TermPipeline.TermSink<VALUE>, SLAVE>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermEditSet<VALUE, PARENT> duplicateUgly (
            TermParent<VALUE, PARENT> duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new TermEditSet<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.EditSet#set(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public final PARENT set (
            TermPipeline.TermSink<VALUE> set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().set ( set );
        return this.end ();
    }


    /**
     * @see musaico.foundation.pipeline.EditSet#set(java.lang.Object[])
     */
    @Override
    @SuppressWarnings("unchecked") // Heap pollution varargs.
    public final PARENT set (
            VALUE... set
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        this.slave ().set ( set );
        return this.end ();
    }
}
