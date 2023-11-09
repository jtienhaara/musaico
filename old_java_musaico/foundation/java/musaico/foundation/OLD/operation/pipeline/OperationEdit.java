package musaico.foundation.term.multiplicities;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.Pipeline;

import musaico.foundation.term.Countable;
import musaico.foundation.term.OperationPipeline;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;


/**
 * <p>
 * Adds elements to and/or removes elements from Term values.
 * </p>
 *
 * <p>
 * For example, to concatenate all elements of two Terms' values, and then
 * add another Term's value to the end, the following code might be used:
 * </p>
 * <pre>
 *     final Term<String> middle = ... "brave", "new", ...;
 *     final Term<String> concatenation =
 *         middle.edit ()
 *                 .prepend ( "Hello," )
 *                 .append ( "world!" )
 *               .end ()
 *               .output ();
 * </pre>
 *
 * <p>
 * The output of the <code> output () </code> method would be
 * a Term&lt;String&gt; along the lines of { "Hello,", "brave",
 * "new", "world!".
 * </p>
 *
 * <p>
 * The output of the <code> build () </code> method would be
 * the concatenation of the three Terms left, middle and right.
 * </p>
 *
 *
 * <p>
 * In Java every SubPipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every SubPipeline must be Serializable in order to
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
 * @see musaico.foundation.term.multiplicities.MODULE#COPYRIGHT
 * @see musaico.foundation.term.multiplicities.MODULE#LICENSE
 */
public class TermEdit<VALUE extends Object, PARENT extends TermPipeline<VALUE, PARENT>>
    extends AbstractTermSubPipeline<VALUE, PARENT, Edit<VALUE, PARENT, Term<VALUE>>, Edit<VALUE, ?, Term<VALUE>>>
    implements Edit<VALUE, PARENT, Term<VALUE>>, Serializable
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
     * @param parent The parent of this SubPipeline.  Must not be null.
     *
     * @param slave The Edit operation pipeline that will perform
     *              the heavy lifting.  Must not be null.
     */
    public TermEdit (
            PARENT parent,
            Edit<VALUE, ?, Term<VALUE>> slave
            )
        throws ParametersMustNotBeNull.Violation,
               ClassCastException
    {
        super ( parent,  // parent
                slave ); // slave
    }


    /**
     * @see musaico.foundation.pipeline.Edit#append(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> append (
            VALUE... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().append ( sequence );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#difference(java.lang.Object)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> difference (
            Term<VALUE> set
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().difference ( set );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.SubPipeline#duplicate(musaico.foundation.pipeline.Pipeline)
     */
    @Override
    public final TermEdit<VALUE, PARENT> duplicate (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return this.duplicateUgly ( duplicate_parent );
    }

    // Yes this is atrocious.  Sorry.
    @SuppressWarnings("unchecked")
    private final <SLAVE extends Edit<VALUE, SLAVE_PARENT, Term<VALUE>>, SLAVE_PARENT extends OperationPipeline<VALUE, SLAVE_PARENT>>
        TermEdit<VALUE, PARENT> duplicateUgly (
            PARENT duplicate_parent
            )
        throws ReturnNeverNull.Violation
    {
        return new TermEdit<VALUE, PARENT> (
            duplicate_parent,
            ( (SLAVE) this.slave () ).duplicate (
                (SLAVE_PARENT)
                    duplicate_parent.operations () )
          );
    }


    /**
     * @see musaico.foundation.pipeline.Edit#insert(long, java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> insert (
            long index,
            VALUE... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().insert ( index, sequence );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#intersection(java.lang.Object)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> intersection (
            Term<VALUE> set
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().intersection ( set );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#pad(long,long,java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> pad (
            long pad_at_index,
            long target_length,
            VALUE... padding
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().pad ( pad_at_index, target_length, padding );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#prepend(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> prepend (
            VALUE... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().prepend ( sequence );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#remove(long,long)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> remove (
            long start_index,
            long end_index
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().remove ( start_index, end_index );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#remove(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> remove (
            VALUE... sequence
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().remove ( sequence );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#removeAll(java.lang.Object)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> removeAll (
            Term<VALUE> set
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().removeAll ( set );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#repeat(long)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> repeat (
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
     * @see musaico.foundation.pipeline.Edit#replace(java.lang.Object, java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> replace (
            VALUE replaced,
            VALUE... replacement
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().replace ( replaced, replacement );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#replace(long,long,java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution varargs.
    @SafeVarargs
    public final Edit<VALUE, PARENT, Term<VALUE>> replace (
            long start_index,
            long end_index,
            VALUE... replacement
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().replace ( start_index, end_index, replacement );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#replaceWith(java.lang.Object)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> replaceWith (
            Term<VALUE> replacement
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().replaceWith ( replacement );
        return this;
    }


    /**
     * @see musaico.foundation.pipeline.Edit#toElementsFrom(java.lang.Object)
     */
    @Override
    public final VALUE [] toElementsFrom (
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               Parameter1.MustBeInDomain.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustContainNoNulls.Violation,
        //        Parameter1.MustBeInDomain.Violation:
        return this.slave ().toElementsFrom ( term );
    }


    /**
     * @see musaico.foundation.pipeline.Edit#toPoolFrom(java.lang.Object[])
     */
    @Override
    @SuppressWarnings({"unchecked", "varargs"}) // Heap pollution generic varargs.
    @SafeVarargs
    public final Term<VALUE> toPoolFrom (
            VALUE ... elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeInDomain.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        // Throws ParametersMustNotBeNull.Violation,
        //        Parameter1.MustBeInDomain.Violation:
        return this.slave ().toPoolFrom ( elements );
    }


    /**
     * @see musaico.foundation.pipeline.Edit#union(java.lang.Object)
     */
    @Override
    public final Edit<VALUE, PARENT, Term<VALUE>> union (
            Term<VALUE> set
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Throws ParametersMustNotBeNull.Violation:
        this.slave ().union ( set );
        return this;
    }
}
