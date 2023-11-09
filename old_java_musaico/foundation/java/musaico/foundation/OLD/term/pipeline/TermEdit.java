package musaico.foundation.term.pipeline;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.EditInsert;
import musaico.foundation.pipeline.EditMove;
import musaico.foundation.pipeline.EditSequence;
import musaico.foundation.pipeline.EditSet;
import musaico.foundation.pipeline.EditVarious;
import musaico.foundation.pipeline.Pipeline;

import musaico.foundation.term.Countable;
import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermParent;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Dumb implementation of Edit for Terms, relying pretty much entirely
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
 * @see musaico.foundation.term.pipeline.MODULE#COPYRIGHT
 * @see musaico.foundation.term.pipeline.MODULE#LICENSE
 */
public class TermEdit<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermSubsidiary<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, VALUE, PARENT, Edit<VALUE, ?, TermPipeline.TermSink<VALUE>>>
    implements Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermEdit.
     * </p>
     *
     * @param parent The parent of this Subsidiary.  Must not be null.
     *
     * @param slave The Edit operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermEdit (
            TermParent<VALUE, PARENT> parent,
            Edit<VALUE, ?, TermPipeline.TermSink<VALUE>> slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        super ( parent,  // parent
                slave ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Edit#append()
     */
    @Override
    public final EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> append ()
        throws ReturnNeverNull.Violation
    {
        final EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_sequence =
            new TermEditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().append ()
                );
        return edit_sequence;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#except()
     */
    @Override
    public final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> except ()
        throws ReturnNeverNull.Violation
    {
        final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_set =
            new TermEditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().except ()
                );
        return edit_set;
    }


    /**
     * @see musaico.foundation.pipeline.Subsidiary#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermEdit<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends Edit<VALUE, SLAVE_PARENT, TermPipeline.TermSink<VALUE>>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermEdit<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        return new TermEdit<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.Edit#insert()
     */
    @SuppressWarnings("unchecked") // Generics hell TermEditSeq<...> -> SEQ.
    public final <SEQUENCE extends EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, SEQUENCE>>
        EditInsert<VALUE, SEQUENCE, ?> insert ()
        throws ReturnNeverNull.Violation
    {
        final EditInsert<VALUE, ?, ?> slave_insert =
            this.slave ().insert ();

        final TermParent<VALUE, SEQUENCE> edit_sequence =
            (TermParent<VALUE, SEQUENCE>)
            (
             (TermEditSequence<VALUE, ?>)
             new TermEditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                 this,
                 (EditSequence<VALUE, ?, TermPipeline.TermSink<VALUE>, ?>) slave_insert.parent ()
                 )
             );
        final EditInsert<VALUE, SEQUENCE, ?> edit_insert =
            new TermEditInsert<VALUE, SEQUENCE> (
                edit_sequence,
                slave_insert
                );
        return edit_insert;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#intersection()
     */
    @Override
    public final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> intersection ()
        throws ReturnNeverNull.Violation
    {
        final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_set =
            new TermEditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().intersection ()
                );
        return edit_set;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#move()
     */
    @Override
    @SuppressWarnings("unchecked") // Generics hell TermEditMove<...> -> MOVE.
    public final <MOVE extends EditMove<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, MOVE>>
        EditVarious<VALUE, MOVE, TermPipeline.TermSink<VALUE>, ?> move ()
        throws ReturnNeverNull.Violation
    {
        final EditVarious<VALUE, ?, TermPipeline.TermSink<VALUE>, ?> slave_various =
            this.slave ().move ();

        final TermParent<VALUE, MOVE> edit_move =
            (TermParent<VALUE, MOVE>)
            (
             (TermEditMove<VALUE, ?>)
             new TermEditMove<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                 this,
                 (EditMove<VALUE, ?, ?>) slave_various.parent ()
                 )
             );
        final EditVarious<VALUE, MOVE, TermPipeline.TermSink<VALUE>, ?> edit_various =
            new TermEditVarious<VALUE, MOVE> (
                edit_move,
                slave_various
                );
        return edit_various;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#pad(long)
     */
    @SuppressWarnings("unchecked") // Generics hell TermEditSeq<...> -> SEQ.
    public final <SEQUENCE extends EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, SEQUENCE>>
        EditInsert<VALUE, SEQUENCE, ?> pad (
            long target_length
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation:
        final EditInsert<VALUE, ?, ?> slave_insert =
            this.slave ().pad ( target_length );

        final TermParent<VALUE, SEQUENCE> edit_sequence =
            (TermParent<VALUE, SEQUENCE>)
            (
             (TermEditSequence<VALUE, ?>)
             new TermEditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                 this,
                 (EditSequence<VALUE, ?, TermPipeline.TermSink<VALUE>, ?>) slave_insert.parent ()
                 )
             );
        final EditInsert<VALUE, SEQUENCE, ?> edit_insert =
            new TermEditInsert<VALUE, SEQUENCE> (
                edit_sequence,
                slave_insert
                );
        return edit_insert;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#prepend()
     */
    @Override
    public final EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> prepend ()
        throws ReturnNeverNull.Violation
    {
        final EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_sequence =
            new TermEditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().prepend ()
                );
        return edit_sequence;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#remove()
     */
    @Override
    public final EditVarious<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> remove ()
        throws ReturnNeverNull.Violation
    {
        final EditVarious<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_various =
            new TermEditVarious<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().remove ()
                );
        return edit_various;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#removeAll()
     */
    @Override
    public final Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>> removeAll ()
        throws ReturnNeverNull.Violation
    {
        this.slave ().removeAll ();
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#repeat(long)
     */
    @Override
    public final Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>> repeat (
            long repetitions
            )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation,
               ReturnNeverNull.Violation
    {
        // Throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation:
        this.slave ().repeat ( repetitions );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#replace()
     */
    @Override
    @SuppressWarnings("unchecked") // Generics hell TermEditSeq<...> -> SEQ.
    public final <SEQUENCE extends EditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, SEQUENCE>>
        EditVarious<VALUE, SEQUENCE, TermPipeline.TermSink<VALUE>, ?> replace ()
        throws ReturnNeverNull.Violation
    {
        final EditVarious<VALUE, ?, TermPipeline.TermSink<VALUE>, ?> slave_various =
            this.slave ().replace ();

        final TermParent<VALUE, SEQUENCE> edit_replace =
            (TermParent<VALUE, SEQUENCE>)
            (
             (TermEditSequence<VALUE, ?>)
             new TermEditSequence<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                 this,
                 (EditSequence<VALUE, ?, TermPipeline.TermSink<VALUE>, ?>) slave_various.parent ()
                 )
             );
        final EditVarious<VALUE, SEQUENCE, TermPipeline.TermSink<VALUE>, ?> edit_various =
            new TermEditVarious<VALUE, SEQUENCE> (
                edit_replace,
                slave_various
                );
        return edit_various;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#swap()
     */
    @Override
    @SuppressWarnings("unchecked") // Generics hell TermEditVar<...> -> SEQ.
    public final <SEQUENCE_OR_SET extends EditVarious<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, SEQUENCE_OR_SET>>
        EditVarious<VALUE, SEQUENCE_OR_SET, TermPipeline.TermSink<VALUE>, ?> swap ()
        throws ReturnNeverNull.Violation
    {
        final EditVarious<VALUE, ?, TermPipeline.TermSink<VALUE>, ?> slave_first =
            this.slave ().swap ();

        final TermParent<VALUE, SEQUENCE_OR_SET> edit_second =
            (TermParent<VALUE, SEQUENCE_OR_SET>)
            (
             (TermEditVarious<VALUE, ?>)
             new TermEditVarious<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                 this,
                 (EditVarious<VALUE, ?, TermPipeline.TermSink<VALUE>, ?>) slave_first.parent ()
                 )
             );
        final EditVarious<VALUE, SEQUENCE_OR_SET, TermPipeline.TermSink<VALUE>, ?> edit_first =
            new TermEditVarious<VALUE, SEQUENCE_OR_SET> (
                edit_second,
                slave_first
                );
        return edit_first;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#union()
     */
    @Override
    public final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> union ()
        throws ReturnNeverNull.Violation
    {
        final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_set =
            new TermEditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().union ()
                );
        return edit_set;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#xor()
     */
    @Override
    public final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> xor ()
        throws ReturnNeverNull.Violation
    {
        final EditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>, TermPipeline.TermSink<VALUE>, ?> edit_set =
            new TermEditSet<VALUE, Edit<VALUE, PARENT, TermPipeline.TermSink<VALUE>>> (
                this,
                this.slave ().xor ()
                );
        return edit_set;
    }
}
