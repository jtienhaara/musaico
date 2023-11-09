package musaico.foundation.term.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.EditInsert;
import musaico.foundation.pipeline.OrderBy;
import musaico.foundation.pipeline.Parent;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.TermParent;


/**
 * <p>
 * Dumb implementation of Editnsert for Terms, relying pretty much entirely
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
public class TermEditInsert<
                 VALUE extends Object,
                 PARENT extends Parent<
                     VALUE,
                     PARENT
                     >
                 >
    extends AbstractTermSubsidiary<
                VALUE,
                TermEditInsert<
                    VALUE,
                    PARENT
                    >,
                VALUE,
                PARENT,
                EditInsert<
                    VALUE,
                    ?,
                    ?
                    >
                >
    implements EditInsert<
                   VALUE,
                   PARENT,
                   TermEditInsert<
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


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermEditInsert.class );


    // The parent pipeline.
    private final TermParent<VALUE, PARENT> parent;


    /**
     * <p>
     * Creates a new TermEditInsert.
     * </p>
     *
     * @param parent The parent of this Subsidiary.  Must not be null.
     *
     * @param slave The EditInsert operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermEditInsert (
            TermParent<VALUE, PARENT> parent,
            EditInsert<VALUE, ?, ?> slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        super ( parent,  // parent
                slave ); // slave

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent );
        this.parent = parent;
    }


    /**
     * @see musaico.foundation.pipeline.EditInsert#at(long)
     */
    @Override
    public final PARENT at (
            long index
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().at ( index );
        return this.end ();
    }


    /**
     * @see musaico.foundation.pipeline.EditInsert#atOffsetFromEnd(long)
     */
    @Override
    public final PARENT atOffsetFromEnd (
            long offset
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().atOffsetFromEnd ( offset );
        return this.end ();
    }


    /**
     * @see musaico.foundation.pipeline.EditInsert#atOffsetFromMiddle(long)
     */
    @Override
    public final PARENT atOffsetFromMiddle (
            long offset
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation:
        this.slave ().atOffsetFromMiddle ( offset );
        return this.end ();
    }


    /**
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast PARENT - TermParent<V, P>.
    public final TermEditInsert<VALUE, PARENT> duplicate (
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
    private final <SLAVE extends EditInsert<VALUE, SLAVE_PARENT, SLAVE>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermEditInsert<VALUE, PARENT> duplicateUgly (
            TermParent<VALUE, PARENT> duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new TermEditInsert<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.EditInsert#orderBy(musaico.foundation.order.Order[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final OrderBy<VALUE, PARENT> orderBy (
            Order<VALUE> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new TermOrderBy<VALUE, PARENT> (
                       this.parent,                        // parent
                       this.slave ().orderBy ( orders ) ); // slave
    }
}
