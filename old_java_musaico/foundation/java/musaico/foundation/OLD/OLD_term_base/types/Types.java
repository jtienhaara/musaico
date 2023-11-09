package musaico.foundation.term;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;

import musaico.foundation.pipeline.Behaviour;
import musaico.foundation.pipeline.BehaviourControl;
import musaico.foundation.pipeline.BehaviourPipeline;
import musaico.foundation.pipeline.Edit;
import musaico.foundation.pipeline.Group;
import musaico.foundation.pipeline.OrderBy;
import musaico.foundation.pipeline.Pipeline;
import musaico.foundation.pipeline.Select;
import musaico.foundation.pipeline.When;
import musaico.foundation.pipeline.Where;


/**
 * <p>
 * One or more compatible Type(s).
 * </p>
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
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
public class Types<VALUE extends Object>
    implements Type<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Types.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The child Type(s) of this Types.  Just and Countable.
    private final Countable<Type<VALUE>> children;

    // The child Tyle(s) of this Types, as one OperationPipeline.
    private final OperationPipeline<VALUE> pipeline;

    // The most specific elementClass ().
    private final Class<VALUE> elementClass;

    // The common none () value.
    private final VALUE none;

    // The union of Behaviours.
    private final Countable<Behaviour<Term<?>, ? extends BehaviourControl>> behaviours;

    // The union of constraint Contracts.
    private final Countable<Contract<Term<?>, ? extends TermViolation>> constraints;

    // The union of element constraint Contracts.
    private final Countable<Contract<VALUE, ?>> elementConstraints;

    // The union of Transforms to fields.
    private final Countable<Transform<VALUE, ?>> fields;


    /**
     * <p>
     * Creates a new Types from the specified child Type(s).
     * </p>
     *
     * @param children One or more child Type(s) which are compatible
     *                 with one another.  Must not be null.
     *
     * @throws TypesMustBeCompatible.Violation If the specified children
     *                                         are incompatible with
     *                                         each other.
     */
    @SuppressWarnings("unchecked") // Cast Term<Type<VALUE>> - Term<Type<?>.
    public <CHILDREN extends Just<Type<VALUE>> & Countable<Type<VALUE>>>
        Types (
            CHILDREN children
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               children );
        classContracts.check ( TypesMustBeCompatible.CONTRACT,
                               (Term<Type<?>>) ( (Object) children ) );

        this.children = children;

        // Throws TypesMustBeCompatible.Violation:
        this.elementClass =
            TypesMustBeCompatible.elementClass ( this.children );

        // Throws TypesMustBeCompatible.Violation:
        this.none =
            TypesMustBeCompatible.none ( this.children );

        OperationPipeline<VALUE> pipeline =
            null;
        Countable<Behaviour<Term<?>, ? extends BehaviourControl>> behaviours =
            null;
        Countable<Contract<Term<?>, ? extends TermViolation>> constraints =
            null;
        Countable<Contract<VALUE, ?>> element_constraints =
            null;
        Countable<Transform<VALUE, ?>> fields =
            null;

        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                pipeline = child;
                behaviours = child.behaviours ();
                constraints = child.constraints ();
                element_constraints = child.elementConstraints ();
                fields = child.fields ();

                is_first = false;
            }
            else
            {
                pipeline = pipeline.pipe ( child );
                behaviours =
                    behaviours.edit ()
                              .union ( child.behaviours () )
                              .end ()
                              .build ()
                              .countable ();
                constraints =
                    constraints.edit ()
                               .union ( child.constraints () )
                               .end ()
                               .build ()
                               .countable ();
                element_constraints =
                    element_constraints.edit ()
                                       .union ( child.elementConstraints () )
                                       .end ()
                                       .build ()
                                       .countable ();
                fields =
                    fields.edit ()
                          .union ( child.fields () )
                          .end ()
                          .build ()
                          .countable ();
            }
        }

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               pipeline, behaviours, constraints, element_constraints, fields );

        this.pipeline = pipeline;
        this.behaviours = behaviours;
        this.constraints = constraints;
        this.elementConstraints = element_constraints;
        this.fields = fields;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Duplicates the specified Types.
     * </p>
     *
     * @param types The Types to duplicate.  Must not be null.
     */
    public Types (
            Types<VALUE> that
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        this.children = that.children;
        this.elementClass = that.elementClass;
        this.none = that.none;

        this.pipeline = that.pipeline.duplicate ();
        this.behaviours = that.behaviours;
        this.constraints = that.constraints;
        this.elementConstraints = that.elementConstraints;
        this.fields = that.fields;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#applyLater(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> applyLater (
            Term<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        // Nothing to apply.
        return input;
    }


    /**
     * @see musaico.foundation.term.Type#array(int)
     */
    @Override
    public final VALUE [] array (
            int num_elements
            )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               num_elements );

        @SuppressWarnings("unchecked") // Cast Array.newInstance(X.class)-X[].
        final VALUE [] array = (VALUE [])
            Array.newInstance ( this.elementClass, num_elements );

        return array;
    }


    /**
     * @see musaico.foundation.term.Type#behaviours()
     */
    @Override
    public final Countable<Behaviour<Term<?>, ? extends BehaviourControl>> behaviours ()
        throws ReturnNeverNull.Violation
    {
        return this.behaviours;
    }


    /**
     * @see musaico.foundation.term.Pipeline#build()
     */
    @Override
    public final Operation<VALUE, VALUE> build ()
        throws ReturnNeverNull.Violation
    {
        final Operation<VALUE, VALUE> operation =
            this.pipeline.build ();

        return operation;
    }


    /**
     * @see musaico.foundation.term.Type#cast(musaico.foundation.term.Term)
     */
    @Override
    public final Transform<VALUE, VALUE> cast (
            Term<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Transform<VALUE, VALUE> transform = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                transform = child.cast ( input );
                is_first = false;
            }
            else
            {
                transform = child.from ( transform );
            }
        }

        return transform;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#check()
     */
    @Override
    public final OperationPipeline<VALUE> check ()
        throws ReturnNeverNull.Violation
    {
        OperationPipeline<VALUE> pipeline = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                pipeline = child.check ();
                is_first = false;
            }
            else
            {
                pipeline = pipeline.pipe ( child.check () );
            }
        }

        return pipeline;
    }


    /**
     * @return The 1 or more child Types.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast Countable<> to Just<> & Countable<>
        // because we checked at constructor time.
    public final <CHILDREN extends Just<Type<VALUE>> & Countable<Type<VALUE>>>
        CHILDREN children ()
        throws ReturnNeverNull.Violation
    {
        return (CHILDREN) this.children;
    }


    /**
     * @see musaico.foundation.term.Type#constraints()
     */
    @Override
    public final Countable<Contract<Term<?>, ? extends TermViolation>> constraints ()
        throws ReturnNeverNull.Violation
    {
        return this.constraints;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#duplicate()
     */
    @Override
    public Types<VALUE> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new Types<VALUE> ( this );
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#edit()
     */
    @Override
    public final Edit<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>, Term<Term<VALUE>>> edit ()
        throws ReturnNeverNull.Violation
    {
        final Edit<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>, Term<Term<VALUE>>> edit =
            this.pipeline.duplicate ().edit ();

        return edit;
    }


    /**
     * @see musaico.foundation.term.Type#elementClass()
     */
    @Override
    public final Class<VALUE> elementClass ()
        throws ReturnNeverNull.Violation
    {
        return this.elementClass;
    }


    /**
     * @see musaico.foundation.term.Type#elementConstraints()
     */
    @Override
    public final Countable<Contract<VALUE, ?>> elementConstraints ()
        throws ReturnNeverNull.Violation
    {
        return this.elementConstraints;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
            java.lang.Object object
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final Types<?> that = (Types<?>) object;

        if ( this.elementClass == null )
        {
            if ( that.elementClass != null )
            {
                return false;
            }
        }
        else if ( that.elementClass == null )
        {
            return false;
        }
        else if ( this.elementClass != that.elementClass )
        {
            return false;
        }

        if ( this.children == null )
        {
            if ( that.children != null )
            {
                return false;
            }
        }
        else if ( that.children == null )
        {
            return false;
        }
        else if ( ! this.children.equals ( that.children ) )
        {
            return false;
        }

        // All of the other fields are guaranteed to match,
        // since the children are exactly equal.

        return true;
    }


    /**
     * @see musaico.foundation.term.Type#fields()
     */
    @Override
    public final Countable<Transform<VALUE, ?>> fields ()
        throws ReturnNeverNull.Violation
    {
        return this.fields;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#fields(musaico.foundation.term.Transform)
     */
    @Override
    @SuppressWarnings("unchecked") // Possible heap pollution generic varargs.
    public final <FIELD extends Object>
        OperationPipeline<FIELD> fields (
            Transform<VALUE, FIELD> ... fields
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final OperationPipeline<FIELD> fields_pipeline =
            this.pipeline.fields ( fields );

        return fields_pipeline;
    }


    /**
     * @see musaico.foundation.term.Type#from(java.lang.Object)
     */
    @Override
    public final Term<VALUE> from (
            VALUE source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Term<VALUE> term = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                term = child.from ( source );
                is_first = false;
            }
            else
            {
                term = child.from ( term );
            }
        }

        return term;
    }


    /**
     * @see musaico.foundation.term.Type#from(java.lang.Object[])
     */
    @Override
    public final Term<VALUE> from (
            VALUE [] source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Term<VALUE> term = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                term = child.from ( source );
                is_first = false;
            }
            else
            {
                term = child.from ( term );
            }
        }

        return term;
    }


    /**
     * @see musaico.foundation.term.Type#from(java.util.Collection)
     */
    @Override
    public final Term<VALUE> from (
            Collection<VALUE> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Term<VALUE> term = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                term = child.from ( source );
                is_first = false;
            }
            else
            {
                term = child.from ( term );
            }
        }

        return term;
    }


    /**
     * @see musaico.foundation.term.Type#from(java.util.Map)
     */
    @Override
    public final <TARGET extends Object>
        Transform<VALUE, TARGET> from (
            Map<VALUE, TARGET> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Transform<VALUE, TARGET> transform = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                transform = child.from ( source );
                is_first = false;
            }
            else
            {
                transform = child.from ( transform );
            }
        }

        return transform;
    }


    /**
     * @see musaico.foundation.term.Type#from(java.util.Set)
     */
    @Override
    public final Term<VALUE> from (
            Set<VALUE> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Term<VALUE> term = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                term = child.from ( source );
                is_first = false;
            }
            else
            {
                term = child.from ( term );
            }
        }

        return term;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#from(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> from (
            Term<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Term<VALUE> output = null;
        for ( Type<VALUE> child : this.children )
        {
            output = child.from ( input );
        }

        return output;
    }


    /**
     * @see musaico.foundation.term.Type#from(musaico.foundation.term.Transform)
     */
    @Override
    public final <TARGET extends Object>
        Transform<VALUE, TARGET> from (
            Transform<VALUE, TARGET> source
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        Transform<VALUE, TARGET> transform = null;
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                transform = child.from ( source );
                is_first = false;
            }
            else
            {
                transform = child.from ( transform );
            }
        }

        return transform;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        if ( this.children == null )
        {
            return 0;
        }

        int hash_code = 0;
        for ( Type<VALUE> child : this.children )
        {
            hash_code = hash_code * 31 + child.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#in(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> in (
        Term<VALUE> input
        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.from ( input );
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#into(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> into (
        Term<VALUE> input
        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.from ( input );
    }


    /**
     * @return The kind (Type) governing these Types.  Never null.
     */
    public final Type<Type<VALUE>> kind ()
        throws ReturnNeverNull.Violation
    {
        return this.children.type ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#make(musaico.foundation.pipeline.Behaviour)
     */
    @Override
    public final <CONTROL extends BehaviourControl>
        BehaviourPipeline<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>, ? extends CONTROL> make (
            Behaviour<Term<VALUE>, CONTROL> behaviour
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final BehaviourPipeline<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>, ? extends CONTROL> behaviour_pipeline =
            this.pipeline.duplicate ().make ( behaviour );

        return behaviour_pipeline;
    }


    /**
     * @see musaico.foundation.term.Type#none()
     */
    @Override
    public final VALUE none ()
        throws ReturnNeverNull.Violation
    {
        return this.none;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#on(musaico.foundation.pipeline.Behaviour)
     */
    @Override
    public final <CONTROL extends BehaviourControl>
        BehaviourPipeline<Term<VALUE>, Operation<VALUE, VALUE>, When<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>>, ? extends CONTROL> on (
            Behaviour<Term<VALUE>, CONTROL> behaviour
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final BehaviourPipeline<Term<VALUE>, Operation<VALUE, VALUE>, When<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>>, ? extends CONTROL> behaviour_pipeline =
            this.pipeline.duplicate ().on ( behaviour );

        return behaviour_pipeline;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#onto(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> onto (
        Term<VALUE> input
        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.from ( input );
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final OrderBy<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> orderBy (
            Order<Term<VALUE>> ... orders
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final OrderBy<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> order_by =
            this.pipeline.duplicate ().orderBy ( orders );

        return order_by;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#pipe(musaico.foundation.term.Operation)
     */
    @Override
    public final <OUTPUT extends Object>
        OperationPipeline<OUTPUT> pipe (
            Operation<VALUE, OUTPUT> operation
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final OperationPipeline<OUTPUT> pipeline =
            this.pipeline.duplicate ().pipe ( operation );

        return pipeline;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#pipe(musaico.foundation.term.OperationPipeline)
     */
    @Override
    public final OperationPipeline<VALUE> pipe (
            OperationPipeline<VALUE> pipeline
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final OperationPipeline<VALUE> new_pipeline =
            this.pipeline.duplicate ().pipe ( pipeline );

        return pipeline;
    }


    /**
     * @see musaico.foundation.term.Type#refine
     */
    @Override
    public final TermPipeline<Type<VALUE>> refine ()
        throws ReturnNeverNull.Violation
    {
        final Type<VALUE> first_child = this.children.head ().orNull ();
        TermPipeline<Type<VALUE>> refined = first_child.refine ();

        final Term<Type<VALUE>> subsequent_children =
            this.children.select ().range ( 1L, Countable.LAST ).build ();
        refined = refined.edit ().append ( subsequent_children ).end ();

        return refined;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#select()
     */
    @Override
    public final Select<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> select ()
        throws ReturnNeverNull.Violation
    {
        final Select<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> select =
            this.pipeline.duplicate ().select ();

        return select;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#to(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> to (
        Term<VALUE> input
        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.from ( input );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        if ( this.getClass () != Types.class )
        {
            sbuf.append ( ClassName.of ( this.getClass () ) );
            sbuf.append ( " " );
        }

        if ( this.children == null )
        {
            sbuf.append ( "{ ??? under construction ??? }" );
            return sbuf.toString ();
        }

        sbuf.append ( "{" );
        boolean is_first = true;
        for ( Type<VALUE> child : this.children )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " );
            sbuf.append ( "" + child );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)
     */
    @Override
    public final When<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> when (
            Filter<?> condition
            )
    {
        final When<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> conditional_pipeline =
            this.pipeline.duplicate ().when ( condition );

        return conditional_pipeline;
    }


    /**
     * @see musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array heap pollution.
    public final Where<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> where (
            Filter<Term<VALUE>> ... filters
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final Where<Term<VALUE>, Operation<VALUE, VALUE>, OperationPipeline<VALUE>> where =
            this.pipeline.duplicate ().where ( filters );

        return where;
    }


    /**
     * @see musaico.foundation.term.OperationPipeline#with(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> with (
        Term<VALUE> input
        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.from ( input );
    }
}
