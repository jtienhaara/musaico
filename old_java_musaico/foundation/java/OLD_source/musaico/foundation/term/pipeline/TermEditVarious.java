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

import musaico.foundation.pipeline.EditVarious;
import musaico.foundation.pipeline.Parent;
import musaico.foundation.pipeline.Where;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.TermParent;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Dumb implementation of EditVarious for Terms, relying pretty much entirely
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
public class TermEditVarious<
                 VALUE extends Object,
                 PARENT extends Parent<
                     VALUE,
                     PARENT
                     >
                 >
    extends AbstractTermSubsidiary<
                VALUE,
                TermEditVarious<
                    VALUE,
                    PARENT
                    >,
                VALUE,
                PARENT,
                EditVarious<
                    VALUE,
                    ?,
                    TermPipeline.TermSink<
                        VALUE
                        >,
                    ?
                    >
                >
    implements EditVarious<
                   VALUE,
                   PARENT,
                   TermPipeline.TermSink<
                       VALUE
                       >,
                   TermEditVarious<
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
     * Creates a new TermEditVarious.
     * </p>
     *
     * @param parent The parent of this Subsidiary.  Must not be null.
     *
     * @param slave The EditVarious operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermEditVarious (
            TermParent<VALUE, PARENT> parent,
            EditVarious<VALUE, ?, TermPipeline.TermSink<VALUE>, ?> slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        super ( parent, // parent
                slave ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Select#all()
     */
    @Override
    public final PARENT all ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().all ();
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#at(long)
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
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Parent)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast PARENT - TermParent<V, P>.
    public final TermEditVarious<VALUE, PARENT> duplicate (
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
    private final <SLAVE extends EditVarious<VALUE, SLAVE_PARENT, TermPipeline.TermSink<VALUE>, SLAVE>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermEditVarious<VALUE, PARENT> duplicateUgly (
            TermParent<VALUE, PARENT> duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new TermEditVarious<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }

    /**
     * @see musaico.foundation.pipeline.Select#first()
     */
    @Override
    public final PARENT first ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().first ();
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#first(long)
     */
    @Override
    public final PARENT first (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation:
        this.slave ().first ( num_elements );
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#fromEnd()
     */
    @Override
    public final TermEditVarious<VALUE, PARENT> fromEnd ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().fromEnd ();
        return this;
    }

    /**
     * @see musaico.foundation.pipeline.Select#last()
     */
    @Override
    public final PARENT last ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().last ();
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#last(long)
     */
    @Override
    public final PARENT last (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation:
        this.slave ().last ( num_elements );
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#middle()
     */
    @Override
    public final PARENT middle ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().middle ();
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#middle(long)
     */
    @Override
    public final PARENT middle (
            long num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation:
        this.slave ().middle ( num_elements );
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#neighbourhood()
     */
    @Override
    public final TermEditVarious<VALUE, PARENT> neighbourhood ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().neighbourhood ();
        return this;
    }

    /**
     * @see musaico.foundation.pipeline.Select#noElements()
     */
    @Override
    public final PARENT noElements ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().noElements ();
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.Select#range(long, long)
     */
    @Override
    public final PARENT range (
            long start,
            long end
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               Parameter2.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
        //        Parameter2.MustBeGreaterThanOrEqualToOne.Violation:
        this.slave ().range ( start, end );
        return this.end ();
    }

    /**
     * @see musaico.foundation.pipeline.EditSequence#sequence(java.lang.Object)
     */
    @Override
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
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs,
    public final PARENT sequence (
            VALUE ... sequence
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

    /**
     * @see musaico.foundation.pipeline.EditSet#set(java.lang.Object)
     */
    @Override
    public final PARENT set (
            TermPipeline.TermSink<VALUE> set
            )
        throws ParametersMustNotBeNull.Violation,
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
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public final PARENT set (
            VALUE ... set
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


    /**
     * @see musaico.foundation.pipeline.EditSequence#where(musaico.foundation.pipeline.EditVarious#where(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs possible heap pollution,
        // cast PARENT - TermParent<V, P> since AbstractTermSubsidiary
        //     constructor already checked it.
    public final Where<VALUE, PARENT> where (
            Filter<VALUE> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation:
        return new TermWhere<VALUE, PARENT> (
            (TermParent<VALUE, PARENT>) this.parent (), // parent
            this.slave ().where ( filters ) );          // slave
    }
}
