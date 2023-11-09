package musaico.foundation.term.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;

import musaico.foundation.pipeline.Parent;
import musaico.foundation.pipeline.Select;

import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermParent;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Selects a subset of zero or more elements from each Term's value,
 * and produces a new Term as output.
 * </p>
 *
 *
 * <p>
 * In Java every Select must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Select must be Serializable in order to
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
 * @see musaico.foundation.term.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.term.pipeline.MODULE#LICENSE
 */
public class TermSelect<VALUE extends Object, PARENT extends Parent<VALUE, PARENT>>
    implements Select<VALUE, PARENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TermSelect.class );


    // The parent pipeline.
    private final TermParent<VALUE, PARENT> parent;

    // The Operation Pipeline which will do all of our work for us.
    private final Select<VALUE, ?> slave;


    /**
     * <p>
     * Creates a new TermSelect.
     * </p>
     *
     * @param parent The parent pipeline.  Must not be null.
     *
     * @param slave The Select operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermSelect (
            TermParent<VALUE, PARENT> parent,
            Select<VALUE, ?> slave
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent, slave );

        this.parent = parent;
        this.slave = slave;
    }


    /**
     * @see musaico.foundation.pipeline.Select#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast PARENT - TermParent<V, P>.
    public final TermSelect<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( (TermParent<VALUE, PARENT>)
                                    duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends Select<VALUE, SLAVE_PARENT>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermSelect<VALUE, PARENT> duplicateUgly (
            TermParent<VALUE, PARENT> duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermSelect<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.Select#all()
     */
    @Override
    public final PARENT all ()
        throws ReturnNeverNull.Violation
    {
        this.slave.all ();
        return this.parent.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#at(long)
     */
    @Override
    public final PARENT at (
            long index
            )
        throws ReturnNeverNull.Violation
    {
        this.slave.at ( index );
        return this.parent.thisPipeline ();
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        // It doesn't matter whether the parent pipelines are equal,
        // this sub-pipeline is equal to that one.

        return true;
    }


    /**
     * @see musaico.foundation.pipeline.Select#first()
     */
    @Override
    public final PARENT first ()
        throws ReturnNeverNull.Violation
    {
        this.slave.first ();
        return this.parent.thisPipeline ();
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
        this.slave.first ( num_elements );
        return this.parent.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#fromEnd()
     */
    @Override
    public final TermSelect<VALUE, PARENT> fromEnd ()
        throws ReturnNeverNull.Violation
    {
        this.slave.fromEnd ();
        return this;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#last()
     */
    @Override
    public final PARENT last ()
        throws ReturnNeverNull.Violation
    {
        this.slave.last ();
        return this.parent.thisPipeline ();
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
        this.slave.last ( num_elements );
        return this.parent.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#middle()
     */
    @Override
    public final PARENT middle ()
        throws ReturnNeverNull.Violation
    {
        this.slave.middle ();
        return this.parent.thisPipeline ();
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
        this.slave.middle ( num_elements );
        return this.parent.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#neighbourhood()
     */
    @Override
    public final TermSelect<VALUE, PARENT> neighbourhood ()
        throws ReturnNeverNull.Violation
    {
        this.slave.neighbourhood ();
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Select#noElements()
     */
    @Override
    public final PARENT noElements ()
        throws ReturnNeverNull.Violation
    {
        this.slave.noElements ();
        return this.parent.thisPipeline ();
    }


    /**
     * @see musaico.foundation.pipeline.Select#range(long,long)
     */
    @Override
    public final PARENT range (
            long start,
            long end
            )
        throws ReturnNeverNull.Violation
    {
        this.slave.range ( start, end );
        return this.parent.thisPipeline ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
