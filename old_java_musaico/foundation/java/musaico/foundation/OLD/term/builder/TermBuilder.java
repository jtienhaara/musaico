package musaico.foundation.term.builder;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Type;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.countable.Many;
import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;
import musaico.foundation.term.countable.ValueMustNotBeMany;

import musaico.foundation.term.incomplete.Partial;

import musaico.foundation.term.infinite.Cyclical;


/**
 * <p>
 * Builds up a constant value term, one element at a time.  Can create
 * No elements, One element, or Many elements.
 * </p>
 *
 * <p>
 * This TermBuilder is NOT thread-safe.  Do not use it from multiple
 * threads.
 * </p>
 *
 *
 * <p>
 * In Java every TermBuilder must be Serializable in order to play
 * nicely over RMI.  However be aware that the objects stored inside
 * a TermBuilder need not be Serializable.  If non-Serializable
 * objects are used in an RMI environment, be prepared for trouble.
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
 * @see musaico.foundation.term.builder.MODULE#COPYRIGHT
 * @see musaico.foundation.term.builder.MODULE#LICENSE
 */
public class TermBuilder<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( TermBuilder.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The type of the value being built.
    private final Type<VALUE> type;

    // MUTABLE:
    // The elements of the term being built.
    // Changes content over time.
    private final List<VALUE> elements = new ArrayList<VALUE> ();

    // MUTABLE:
    // The header of the Cyclical term being built (if any).
    // Changes content over time.
    private List<VALUE> cyclicalHeader = null;


    /**
     * <p>
     * Creates a new TermBuilder.
     * </p>
     *
     * @param type The Type of the term to be built.  Must not be null.
     */
    public TermBuilder (
            Type<VALUE> type
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( Parameter1.MustNotBeNull.CONTRACT,
                               type );

        this.type = type;

        this.contracts = new Advocate ( this );
    }


    /**
     * <p>
     * Creates a new TermBuilder and adds the specified element.
     * </p>
     *
     * @param type The Type of the term to be built.  Must not be null.
     *
     * @param element The one element to add to this TermBuilder.
     *                Must not be null.
     */
    public TermBuilder (
            Type<VALUE> type,
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               element );

        this.add ( element );
    }


    /**
     * <p>
     * Creates a new TermBuilder and adds the specified
     * Iterable elements.
     * </p>
     *
     * @param type The Type of the term to be built.  Must not be null.
     *
     * @param elements The elements to add to this TermBuilder.
     *                 Must not be null.  Must not contain any null elements.
     */
    public TermBuilder (
            Type<VALUE> type,
            Iterable<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               elements );

        this.addAll ( elements );
    }


    /**
     * <p>
     * Creates a new TermBuilder and adds the specified
     * array of elements.
     * </p>
     *
     * @param type The Type of the term to be built.  Must not be null.
     *
     * @param elements The elements to add to this TermBuilder.
     *                 Must not be null.  Must not contain any null elements.
     */
    public TermBuilder (
            Type<VALUE> type,
            VALUE [] elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               elements );

        this.addAll ( elements );
    }


    /**
     * <p>
     * Adds the specified element to the term being built.
     * </p>
     *
     * <p>
     * NOT thread-safe.
     * </p>
     *
     * @param element The element to add.  Must not be null.
     *
     * @return This TermBuilder.  Never null.
     */
    public TermBuilder<VALUE> add (
            VALUE element
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               element );

        this.elements.add ( element );
        return this;
    }


    /**
     * <p>
     * Adds the specified elements to the term being built.
     * </p>
     *
     * <p>
     * NOT thread-safe.
     * </p>
     *
     * @param elements The elements to add.  Must not be null.
     *
     * @return This TermBuilder.  Never null.
     */
    public TermBuilder<VALUE> addAll (
            Iterable<VALUE> elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );

        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        if ( elements instanceof Collection )
        {
            final Collection<VALUE> collection =
                (Collection<VALUE>) elements;
            this.elements.addAll ( collection );
        }
        else if ( elements instanceof Cyclical )
        {
            final Cyclical<VALUE> cyclical =
                (Cyclical<VALUE>) elements;
            if ( this.cyclicalHeader == null )
            {
                this.cyclicalHeader = new ArrayList<VALUE> ();
            }

            for ( VALUE header : cyclical.header () )
            {
                this.cyclicalHeader.add ( header );
            }
            for ( VALUE cycle : cyclical.cycle () )
            {
                this.elements.add ( cycle );
            }
        }
        else
        {
            for ( VALUE element : elements )
            {
                this.elements.add ( element );
            }
        }

        return this;
    }


    /**
     * <p>
     * Adds the specified elements to the term being built.
     * </p>
     *
     * <p>
     * NOT thread-safe.
     * </p>
     *
     * @param elements The elements to add.  Must not be null.
     *
     * @return This TermBuilder.  Never null.
     */
    public TermBuilder<VALUE> addAll (
            VALUE [] elements
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements );

        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               elements );

        for ( VALUE element : elements )
        {
            this.elements.add ( element );
        }

        return this;
    }


    /**
     * <p>
     * Adds the specified Terms to the term being built.
     * </p>
     *
     * <p>
     * Each element of each term is added to this TermBuilder,
     * as though <code> TermBuilder.addAll ( Term&lt;VALUE&gt; ) </code>
     * had been called on each and every one of the Terms passed in.
     * </p>
     *
     * <p>
     * NOT thread-safe.
     * </p>
     *
     * @param values The values to add.  Must not be null.
     *
     * @return This TermBuilder.  Never null.
     */
    public TermBuilder<VALUE> addTerms (
            Iterable<Term<VALUE>> values
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               values );

        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               values );

        for ( Term<VALUE> value : values )
        {
            // Term<VALUE> is an Iterable<VALUE>, so use the addAll ()
            // method to add the elements from the current Term.
            this.addAll ( value );
        }

        return this;
    }


    /**
     * <p>
     * Adds the specified Terms to the term being built.
     * </p>
     *
     * <p>
     * Each element of each term is added to this TermBuilder,
     * as though <code> TermBuilder.addAll ( Term&lt;VALUE&gt; ) </code>
     * had been called on each and every one of the Terms passed in.
     * </p>
     *
     * <p>
     * NOT thread-safe.
     * </p>
     *
     * @param terms The terms to add.  Must not be null.
     *
     * @return This TermBuilder.  Never null.
     */
    public TermBuilder<VALUE> addTerms (
            Term<VALUE> [] terms
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) terms );

        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               terms );

        for ( Term<VALUE> term : terms )
        {
            // Term<VALUE> is an Iterable<VALUE>, so use the addAll ()
            // method to add the elements from the current Term.
            this.addAll ( term );
        }

        return this;
    }


    /**
     * <p>
     * Builds No, One or Many value(s), depending on how
     * many elements have been added to this builder.
     * </p>
     *
     * @return A newly created Term.  Never null.
     */
    public Countable<VALUE> build ()
        throws ReturnNeverNull.Violation
    {
        final int num_elements;
        final List<VALUE> from_elements;
        if ( this.cyclicalHeader != null
             && this.elements.size () == 0 )
        {
            num_elements = this.cyclicalHeader.size ();
            from_elements = this.cyclicalHeader;
        }
        else if ( this.cyclicalHeader != null )
        {
            // Can't turn a Cyclical term into a Countable one.
            return new No<VALUE> ( this.type );
        }
        else
        {
            num_elements = this.elements.size ();
            from_elements = this.elements;
        }

        if ( num_elements == 0 )
        {
            return new No<VALUE> ( this.type );
        }
        else if ( num_elements == 1 )
        {
            return new One<VALUE> ( this.type,
                                    from_elements.get ( 0 ) );
        }
        else
        {
            final VALUE [] template = this.type.array ( num_elements );
            final VALUE [] elements = from_elements.toArray ( template );

            return new Many<VALUE> ( this.type,
                                     elements );
        }
    }


    /**
     * <p>
     * Builds a Cyclical value with one or more elements in its
     * infinitely repeating cycle, or No value if there are no elements
     * or only one element.
     * </p>
     *
     * @return A newly created Cyclical or No Term.  Never null.
     */
    public Term<VALUE> buildCyclical ()
        throws ReturnNeverNull.Violation
    {
        final int num_elements = this.elements.size ();
        if ( num_elements == 0 )
        {
            return this.build ();
        }
        else
        {
            final VALUE [] template = this.type.array ( num_elements );
            final VALUE [] elements = this.elements.toArray ( template );

            final Cyclical<VALUE> cyclical;
            if ( num_elements == 1 )
            {
                final One<VALUE> one_cycle =
                    new One<VALUE> ( this.type,
                                     this.elements.get ( 0 ) );
                cyclical =
                    new Cyclical<VALUE> (
                                         this.type,
                                         one_cycle );
            }
            else
            {
                final Many<VALUE> one_cycle =
                    new Many<VALUE> ( this.type,
                                      elements );
                cyclical =
                    new Cyclical<VALUE> (
                                         this.type,
                                         one_cycle );
            }

            return cyclical;
        }
    }


    /**
     * <p>
     * Builds a Partial value with one or more elements, or No value
     * if there are no elements.
     * </p>
     *
     * @return A newly created Partial or No Term.  Never null.
     */
    public Term<VALUE> buildPartial ()
        throws ReturnNeverNull.Violation
    {
        final Countable<VALUE> wrapped = this.build ();

        if ( ! wrapped.hasValue () )
        {
            return wrapped;
        }
        else
        {
            return new Partial<VALUE> ( this.type,
                                        wrapped );
        }
    }


    /**
     * <p>
     * Builds an Error due to the specified Violation.
     * </p>
     *
     * @param violation The Violation which caused the Error.
     *                  Must not be null.
     *
     * @return A newly created Error caused by the specified Violation.
     *         Never null.
     */
    public <VIOLATION extends Throwable & Violation>
        Error<VALUE> buildViolation (
                VIOLATION violation
                )
            throws ReturnNeverNull.Violation
    {
        final TermViolation term_violation;
        if ( violation instanceof TermViolation )
        {
            term_violation = (TermViolation) violation;
        }
        else
        {
            term_violation = new TermViolation ( violation );
        }

        final Error<VALUE> error =
            new Error<VALUE> ( this.type,
                               term_violation );
        return error;
    }


    /**
     * <p>
     * Builds No or One value(s), depending on how
     * many elements have been added to this builder.
     * If Many elements have been added to this builder,
     * then No value is returned with a term violation.
     * </p>
     *
     * @return A newly created Term, Maybe One, Maybe No value,
     *         Maybe an Error.  Never null.
     */
    public Maybe<VALUE> buildZeroOrOne ()
        throws ReturnNeverNull.Violation
    {
        if ( this.cyclicalHeader != null )
        {
            if ( this.cyclicalHeader.size () != 1
                 || this.elements.size () != 0 )
            {
                // Either totally empty or Cyclical.
                return new No<VALUE> ( this.type );
            }
            else
            {
                // Started building a Cyclical Term, but only
                // ended up with 1 element in the header,
                // and nothing in the cycle.
                return new One<VALUE> ( this.type,
                                        this.cyclicalHeader.get ( 0 ) );
            }
        }

        final int num_elements = this.elements.size ();
        if ( num_elements == 1 )
        {
            return new One<VALUE> ( this.type,
                                    this.elements.get ( 0 ) );
        }
        else
        {
            return new No<VALUE> ( this.type );
        }
    }


    /**
     * @return The Advocate for this Symbol.
     *         Checks method obligations and guarantees.
     *         Never null.
     */
    protected final Advocate contracts ()
    {
        return this.contracts;
    }


    /**
     * @return The elements being built into a Term by this
     *         TermBuilder.  Can be used by derived implementations
     *         to manipulate the elements.  Use with caution!
     *         Never null.  Never contains any null elements.
     */
    protected final List<VALUE> elements ()
    {
        return this.elements;
    }


    /**
     * @return The elements being built into the header of a Cyclical Term
     *         by this TermBuilder, if any.  Can be used
     *         by derived implementations
     *         to manipulate the elements.  Use with caution!
     *         CAN BE NULL if there is currently no cyclical header.
     */
    protected final List<VALUE> cyclicalHeader ()
    {
        return this.cyclicalHeader;
    }


    /**
     * @return The number of elements added to this builder so far.
     *         If a Cyclical Term is being built, then Long.MAX_VALUE
     *         is returned.  Always 0L or greater.
     */
    public final long length ()
    {
        if ( this.cyclicalHeader != null
             && this.elements.size () > 0 )
        {
            return Long.MAX_VALUE;
        }

        return (long) this.elements.size ();
    }


    /**
     * <p>
     * Removes all elements from this TermBuilder (including
     * cyclical header elements, if any), leaving it blank.
     * </p>
     *
     * @return This TermBuilder.  Never null.
     */
    public final TermBuilder<VALUE> removeAll ()
        throws ReturnNeverNull.Violation
    {
        this.elements.clear ();
        if ( this.cyclicalHeader != null )
        {
            this.cyclicalHeader.clear ();
        }

        return this;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        if ( this.cyclicalHeader != null )
        {
            return this.termBuilderName ()
                + " Cyclical"
                + " [" + this.cyclicalHeader.size () + "],"
                + " [" + this.elements.size () + "...]";
        }
        return this.termBuilderName ()
            + " [" + this.elements.size () + "]";
    }


    /**
     * @return The base name of this TermBuilder.  Used by toString ()
     *         to output the name and size of this builder.
     *         Never null.
     */
    protected String termBuilderName ()
        throws ReturnNeverNull.Violation
    {
        return ClassName.of ( this.getClass () )
            + "<"
            + this.type
            + ">";
    }


    /**
     * @return The type of the term being built.  Never null.
     */
    public final Type<VALUE> type ()
    {
        return this.type;
    }
}
