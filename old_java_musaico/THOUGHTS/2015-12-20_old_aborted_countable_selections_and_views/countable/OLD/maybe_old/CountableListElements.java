package musaico.foundation.value;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.BoundedDomain;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * An offshoot from a Countable value which can be used to filter
 * the elements, sort them, replace them, and so on, and build new
 * Countable values.
 * </p>
 *
 * <p>
 * The CountableListElements is mutable.  It potentially changes with
 * every operation.  You can create a defensive copy of
 * a CountableListElements with <code> duplicate () </code>, if you
 * are worried about someone else messing with your elements.
 * </p>
 *
 *
 * <p>
 * In Java every CountableElements must be Serializable in order to
 * play nicely across RMI.  However users of the CountableElements
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public class CountableListElements<VALUE extends Object>
    extends AbstractCountableListView<VALUE, VALUE, CountableElements<VALUE>>
    implements CountableElements<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CountableListElements.class );


    /**
     * <p>
     * Creates a new CountableListElements which will work on the specified
     * Countable and elements.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable elements.  Must not be null.
     *
     * @param elements The 0 or more elements of this countable elements.
     *                 Must not be null.  Must not contain any null elements.
     */
    public CountableListElements (
                                  Countable<VALUE> countable,
                                  List<VALUE> elements
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( countable,
                countable == null
                    ? null
                    : countable.expectedClass (),
                elements );
    }


    /**
     * <p>
     * Creates a new CountableListElements which will work on the specified
     * Countable and single element.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable elements.  Must not be null.
     *
     * @param element The one element of this countable elements.
     *                Must not be null.
     */
    public CountableListElements (
                                  Countable<VALUE> countable,
                                  VALUE element
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( countable,
                countable == null
                    ? null
                    : countable.expectedClass (),
                element );
    }


    /**
     * <p>
     * Creates a new CountableListElements which will work on the specified
     * Countable and elements.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable elements.  Must not be null.
     *
     * @param elements The 0 or more elements of this countable elements.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param none_violation The initial "no value" violation.
     *                       Can be null for default.
     */
    public CountableListElements (
                                  Countable<VALUE> countable,
                                  List<VALUE> elements,
                                  ValueViolation none_violation
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( countable,
                countable == null
                    ? null
                    : countable.expectedClass (),
                elements,
                none_violation );
    }




    /**
     * <p>
     * Returns a newly created countable elements constructed from
     * an Error due to the specified contract being violated with an
     * abnormal input.
     * </p>
     *
     * @param violated_contract The Contract which was violated by an
     *                          abnormal input.  For example, if
     *                          <code> element () </code> receives an
     *                          abnormal input, or operates on an
     *                          abnormal original value, then the
     *                          <code> CountableElements.Element </code>
     *                          contract has been violated.
     *                          Must not be null.
     *
     * @param abnormal_value The input which violates the contract.
     *                       Must not be null.
     */
    protected CountableElements<VALUE> createAbnormalCountableElements (
                                                                        Contract<Iterable<?>, ValueMustNotBeAbnormal.Violation> violated_contract,
                                                                        Abnormal<?> abnormal_value
                                                                        )
    {
        final ValueViolation violation =
            violated_contract.enforcer ().violation ( violated_contract,
                                                      this,
                                                      abnormal_value );
        violation.initCause ( abnormal_value.valueViolation () );
        final Error<VALUE> original_value =
            new Error<VALUE> ( this.originalValue.expectedClass (),
                               violation );
        final CountableListElements<VALUE> abnormal =
            new CountableListElements<VALUE> ( original_value,
                                               new ArrayList<VALUE> () );
        return abnormal;
    }


    /**
     * <p>
     * Returns a newly created countable elements constructed from
     * an Error due to the specified contract being violated with an
     * input that is out of range.
     * </p>
     *
     * @param violated_contract The Contract which was violated by an
     *                          abnormal input.  For example, if
     *                          <code> pad () </code> receives an
     *                          invalid target length input, then a
     *                          <code> MustBeInBounds </code>
     *                          contract has been violated.
     *                          Must not be null.
     *
     * @param input The input which violates the contract.
     *              Must not be null.
     */
    protected <VIOLATION extends Throwable & Violation>
        CountableElements<VALUE> createAbnormalCountableElementsInvalidInput (
                                                                              VIOLATION violation
                                                                              )
    {
        final Error<VALUE> original_value =
            new Error<VALUE> ( this.originalValue.expectedClass (),
                               violation );
        final CountableElements<VALUE> abnormal =
            new CountableListElements<VALUE> ( original_value,
                                               new ArrayList<VALUE> () );
        return abnormal;
    }


    /**
     * <p>
     * Determines whether this countable elements contains all elements of
     * the specified sub-value, in the same order, either with or without
     * preceding and/or proceeding other elements.
     * </p>
     *
     * <p>
     * No value is always considered to be contained inside any
     * Countable elements (including an empty countable elements) of the
     * same value class.  Neither Warnings no Errors are ever
     * contained within any Countable elements.  And no Multiple is ever
     * contained by any empty or single-item countable elements.
     * </p>
     *
     * @param sub_value The countable value to search for.  Must not be null.
     *
     * @return True if the specified sub-value is nested anywhere inside
     *         the elements of this countable elements; false if it does
     *         not appear anywhere in this value.
     *         Always false if this Countable elements was constructed
     *         from an Error or Warning.  Always false if the specified
     *         sub-value is an Error or Warning.
     */
    public boolean contains (
                             Countable<VALUE> sub_value
                             )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value );

        if ( this.originalValue instanceof Abnormal )
        {
            return false;
        }
        else if ( sub_value instanceof Abnormal )
        {
            return false;
        }

        final Iterator<VALUE> sub_iterator = sub_value.iterator ();
        if ( ! sub_iterator.hasNext () )
        {
            if ( sub_value instanceof No )
            {
                // Every Countable contains No as a sub-value.
                return true;
            }
            else
            {
                // No Countable ever contains any Unjust values other
                // than No, and we have dealt with Error and Warning
                // above.  This must be some new abstruse cutting
                // edge Unjust value the young whippersnappers are
                // on about.
                return false;
            }
        }

        int last_num_found = 0;
        int last_index = -1;
        final VALUE first_element = sub_iterator.next ();
        synchronized ( this.lock )
        {
            int start_index = 0;
            while ( start_index >= 0 )
            {
                start_index =
                    this.indexOf ( first_element, start_index );

                if ( start_index < 0 )
                {
                    return false;
                }

                int num_found = 1;
                int end_index = start_index;
                for ( int seq = 0; seq < last_num_found; seq ++ )
                {
                    end_index ++;
                    if ( this.elements.get ( last_index + seq )
                         .equals ( this.elements.get ( end_index ) ) )
                    {
                        num_found ++;
                    }
                    else
                    {
                        break;
                    }
                }

                if ( num_found >= last_num_found )
                {
                    last_index = start_index;
                    boolean is_found_all_remaining_elements = true;
                    while ( sub_iterator.hasNext () )
                    {
                        final VALUE sub_element = sub_iterator.next ();
                        final int index = this.indexOf ( sub_element,
                                                         end_index + 1 );
                        if ( index < 0 )
                        {
                            return false;
                        }
                        else if ( index > ( end_index + 1 ) )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }

                        end_index = index;
                        num_found ++;
                    }

                    if ( is_found_all_remaining_elements )
                    {
                        return true;
                    }

                    last_num_found = num_found;
                }
            }
        }

        // Couldn't find all matching sub-elements.
        return false;
    }


    /**
     * @see musaico.foundation.value.CountableView#duplicate()
     */
    @Override
    public CountableElements<VALUE> duplicate ()
    {
        final CountableElements<VALUE> new_elements;
        synchronized ( this.lock )
        {
            new_elements =
                new CountableListElements<VALUE> ( this.originalValue,
                                                   this.elements,
                                                   this.noneViolation );
        }

        return new_elements;
    }


    /**
     * <p>
     * Removes the elements of the specified countable value that are
     * contained in this countable elements, and adds the elements
     * which are not contained in this countable elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element differences will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing only the
     *         elements that are different between the input and
     *         that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> difference (
                                                Countable<VALUE> that
                                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( that instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Difference.CONTRACT,
                                                          (Abnormal<?>) that );
        }

        synchronized ( this.lock )
        {
            if ( this.elements.size () == 0 )
            {
                // Empt set difference with any set S = S.
                for ( VALUE element : that )
                {
                    this.elements.add ( element );
                }
            }
            else if ( that instanceof Unjust )
            {
                // Any set S difference with the empty set = S.
                // Do nothing.
            }
            else
            {
                int num_my_elements = this.elements.size ();
                for ( VALUE element : that )
                {
                    final int index = this.elements.indexOf ( element );
                    if ( index < 0
                         || index >= num_my_elements )
                    {
                        this.elements.add ( element );
                    }
                    else
                    {
                        for ( int remove_index = index;
                              remove_index >= 0
                                  && remove_index < num_my_elements;
                              remove_index =
                                  this.indexOf ( element, remove_index + 1 ) )
                        {
                            this.elements.remove ( remove_index );
                            num_my_elements --;
                        }
                    }
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeDifferent.CONTRACT.violation ( this,
                                                                           new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Returns the element at the specified index of this Countable
     * value (if any), or returns No element if the specified index
     * is not in range.
     * </p>
     *
     * <p>
     * Any index which is less than 0L or greater than or equal to
     * the <code> length () </code> of this Countable vaue shall be
     * ignored.  Values greater than <code> Integer.MAX_VALUE </code>
     * are counted backward from <code> Countable.LAST </code>,
     * and again are ignored if out of range.
     * </p>
     *
     * @param index The index of the element to return, 0L being the first
     *              index, followed by 1L, and so on.  If the index specified
     *              is less than 0L or greater than the
     *              <code> length () </code> of this Countable elements
     *              then no element shall be returned.  If the index specified
     *              is gretaer than <code> Integer.MAX_VALUE </code> then
     *              the element counted backward from index
     *              <code> Countable.LAST </code> (if any) shall be returned.
     *
     * @return The One element at the specified index, if any, or No element
     *         if the specified index is out of range.
     *         If this Countable elements was constructed from
     *         an Error or Warning then No value is returned.
     *         Never null.
     */
    public ZeroOrOne<VALUE> element (
                                     long index
                                     )
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableElements.MustHaveIndex.CONTRACT,
                                     index );
        }
        else if ( index < 0L )
        {
            return this.createNone ( CountableElements.MustHaveIndex.CONTRACT,
                                     index );
        }

        final VALUE element;
        synchronized ( this.lock )
        {
            final int array_index = this.clampIndex ( index );
            if ( array_index >= 0 )
            {
                element = this.elements.get ( array_index );
            }
            else
            {
                element = null;
            }
        }

        final ZeroOrOne<VALUE> maybe_element;
        if ( element == null )
        {
            maybe_element = this.createNone ( CountableElements.MustHaveIndex.CONTRACT,
                                              index );
        }
        else
        {
            maybe_element =
                new One<VALUE> ( this.originalValue.expectedClass (),
                                 element );
        }

        return maybe_element;
    }


    /**
     * <p>
     * Returns the element(s) at the specified index(ices).
     * </p>
     *
     * <p>
     * The elements are returned in the same order they were
     * indexed in, except for out-of-range indices, which do
     * not result in any elements.
     * </p>
     *
     * @param indices Zero or more indices of element(s) to retrieve.
     *                Must not be null.
     *
     * @return A new Countable value containing only the indexed elements
     *         from this countable elements, in the order specified.
     *         If this Countable elements was constructed from
     *         an Error or Warning then a new such will be returned.
     *         If the specified value is an Error or Warning,
     *         then it will be returned as-is.
     *         Never null.
     */
    public Countable<VALUE> elements (
                                      Countable<Long> indices
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.originalValue;
        }
        else if ( indices instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Elements.CONTRACT,
                                                         (Abnormal<?>) indices ).value ();
        }

        final List<VALUE> elements = new ArrayList<VALUE> ();
        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            if ( num_elements > 0 )
            {
                for ( long index : indices )
                {
                    final int array_index = this.clampIndex ( index );
                    if ( array_index >= 0 )
                    {
                        final VALUE element =
                            this.elements.get ( array_index );
                        elements.add ( element );
                    }
                }
            }
        }

        final Countable<VALUE> countable =
            this.createCountable ( elements,
                                   CountableElements.MustHaveIndices.CONTRACT,
                                   indices );

        return countable;
    }


    /**
     * <p>
     * Keeps only the element(s) from this Countable elements which
     * match the specified filter, removing all other element(s).
     * </p>
     *
     * @param filter The Filter which will keep or discard element(s)
     *               of this Countable elements.
     *               Must not be null.
     *
     * @return This countable elements, containing only
     *         the element(s) that were KEPT by the specified filter.
     *         Can result in an empty countable elements, if
     *         no element(s) were kept by the filter.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> filter (
                                            Filter<VALUE> filter
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               filter );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            for ( int index = 0; index < num_elements; index ++ )
            {
                final VALUE element = this.elements.get ( index );
                if ( ! filter.filter ( element ).isKept () )
                {
                    this.elements.remove ( index );
                    index --;
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustMatchFilter.CONTRACT.violation ( this,
                                                                           new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * @return The One very first element of this Countable elements,
     *         or No element if this countable elements is empty.
     *         Never null.
     */
    public ZeroOrOne<VALUE> first ()
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableElements.First.CONTRACT,
                                     this.originalValue );
        }

        final List<VALUE> elements =
            new ArrayList<VALUE> ();
        synchronized ( this.lock )
        {
            if ( this.elements.size () > 0 )
            {
                elements.add ( this.elements.get ( 0 ) );
            }
        }

        final ZeroOrOne<VALUE> first_element =
            this.createZeroOrOne ( elements,
                                   CountableElements.MustHaveFirst.CONTRACT,
                                   0L );

        return first_element;
    }


    /**
     * <p>
     * Adds the specified countable value(s) to (each of) the specified
     * location(s) in the value being built.
     * </p>
     *
     * <p>
     * For example, if the value being built is <code> 1, 2, 3, 4, 5 </code>
     * and the sub-value <code> A, B </code> is inserted at
     * index <code> { 2 } </code> then the resulting value being built
     * will be <code> { 1, 2, A, B, 3, 4, 5 } </code>.
     * </p>
     *
     * <p>
     * Or if the same sub-value is inserted into the same value being
     * built at indices <code> { 0, 1, 2, 3, 4, 5 } </code> then
     * the resulting value being built will be
     * <code> { A, B, 1, A, B, 2, A, B, 3, A, B, 4, A, B, 5, A, B } <code>.
     * </p>
     *
     * @param sub_value The element(s) to insert into the value being
     *                  built.  Must not be null.
     *
     * @param insert_at_indices The location(s) in the value being built
     *                          at which to insert the specified sub-value.
     *                          If an index is less than or equal to 0L
     *                          or greater than or equal to the length
     *                          of the countable value being built
     *                          then it is ignored.  If an index is greater
     *                          than <code> Integer.MAX_VALUE </code>
     *                          then the sub-value is appended AFTER the
     *                          element counted backward from
     *                          <code> Countable.LAST </code>.
     *                          If an index appears more
     *                          than once then the sub-values will be
     *                          inserted multiple times at the same
     *                          location.  Must not be null.
     *
     * @return This countable elements, with the sub-value(s) inserted
     *         at the specified index/indices.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> insert (
                                            Countable<VALUE> sub_value,
                                            Countable<Long> insert_at_indices
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value, insert_at_indices );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( sub_value instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Insert.CONTRACT,
                                                         (Abnormal<?>) sub_value );
        }
        else if ( insert_at_indices instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Insert.CONTRACT,
                                                         (Abnormal<?>) insert_at_indices );
        }
        else if ( sub_value instanceof Unjust )
        {
            // Nothing to insert.
            return this;
        }
        else if ( insert_at_indices instanceof Unjust )
        {
            // Nowhere to insert at.
            return this;
        }

        final List<List<VALUE>> inserts = new ArrayList<List<VALUE>> ();
        final List<VALUE> appends = new ArrayList<VALUE> ();
        final List<VALUE> no_add_elements = new ArrayList<VALUE> ();
        for ( long insert_at_index : insert_at_indices )
        {
            final List<VALUE> add_elements;
            if ( insert_at_index < 0L )
            {
                continue;
            }
            else if ( insert_at_index > (long) Integer.MAX_VALUE )
            {
                add_elements = appends;
            }
            else
            {
                for ( int add_index = inserts.size ();
                      add_index <= (int) insert_at_index;
                      add_index ++ )
                {
                    inserts.add ( (int) insert_at_index, no_add_elements );
                }

                List<VALUE> maybe_add_elements =
                    inserts.get ( (int) insert_at_index );
                if ( maybe_add_elements == no_add_elements )
                {
                    maybe_add_elements = new ArrayList<VALUE> ();
                    inserts.set ( (int) insert_at_index, maybe_add_elements );
                }

                add_elements = maybe_add_elements;
            }

            for ( VALUE element : sub_value )
            {
                add_elements.add ( element );
            }
        }

        synchronized ( this.lock )
        {
            final int original_size = this.elements.size ();
            if ( appends.size () > 0 )
            {
                this.elements.addAll ( appends );
            }

            for ( int index = original_size - 1;
                  index >= 0;
                  index -- )
            {
                final List<VALUE> add_elements = inserts.get ( index );
                if ( add_elements == no_add_elements )
                {
                    continue;
                }

                this.elements.addAll ( index, add_elements );
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveInserted.CONTRACT.violation ( this,
                                                                            new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Removes the elements of the specified countable value that are
     * not contained in both this countable elements, leaving
     * behind only the intersecting or overlapping elements.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element intersections will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing only the
     *         elements that are contained in both the input and
     *         that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> intersection (
                                                  Countable<VALUE> that
                                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( that instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Intersection.CONTRACT,
                                                         (Abnormal<?>) that );
        }

        synchronized ( this.lock )
        {
            if ( this.elements.size () == 0 )
            {
                // Empt set intersection with any set S = empty.
                // Do nothing.
            }
            else if ( that instanceof Unjust )
            {
                // Any set S intersection with the empty set = empty.
                this.elements.clear ();
            }
            else
            {
                final int original_size = this.elements.size ();
                for ( VALUE element : that )
                {
                    final int index = this.elements.indexOf ( element );
                    if ( index >=  0
                         && index < original_size )
                    {
                        this.elements.add ( element );

                        for ( int next_index = index + 1;
                              next_index < original_size;
                              next_index ++ )
                        {
                            final int another_index =
                                this.indexOf ( element, next_index );
                            if ( another_index < 0
                                 || another_index < original_size )
                            {
                                break;
                            }

                            this.elements.add ( element );

                            next_index = another_index + 1;
                        }
                    }
                }

                for ( int remove_index = original_size - 1;
                      remove_index >= 0;
                      remove_index -- )
                {
                    this.elements.remove ( remove_index );
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveIntersection.CONTRACT.violation ( this,
                                                                               new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Keeps all instance(s) of the specific element(s) in the
     * countable value being built, and removes all other elements.
     * </p>
     *
     * <p>
     * For example, starting from the countable elements
     * <code> { A, B, C, B, A } </code>, keeping the elements
     * <code> { C, A } </code> will keep the one <code> C </code> and the
     * two <code> A </code> elements, removing the two <code> B </code>
     * elements, resulting in <code> { A, C, A } </code>.
     * </p>
     *
     * @param elements_to_keep The element(s) to keep.
     *                         If any element is found more than once,
     *                         then all instances are kept.
     *                         Must not be null.
     *
     * @return This countable elements, with the specified element(s)
     *         kept and all others removed.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> keep (
                                          Countable<VALUE> elements_to_keep
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements_to_keep );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( elements_to_keep instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Keep.CONTRACT,
                                                         (Abnormal<?>) elements_to_keep );
        }

        final Set<VALUE> keep_lookup = elements_to_keep.elements ().toSet ();

        synchronized ( this.lock )
        {
            for ( int index = this.elements.size () - 1;
                  index >= 0;
                  index -- )
            {
                final VALUE element = this.elements.get ( index );
                if ( ! keep_lookup.contains ( element ) )
                {
                    this.elements.remove ( index );
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveKept.CONTRACT.violation ( this,
                                                                        new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Removes the tail of the countable value being built, leaving only
     * the first N elements (if any).
     * </p>
     *
     * @param num_elements_to_keep The number of elements to keep from the
     *                             start of the value being built.
     *                             If less than or equal to 0L then
     *                             the value is emptied out.
     *
     * @return This CountableElements, with only the specified number
     *         of elements (if any) kept from the start.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> keepFirst (
                                               long num_elements_to_keep
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            if ( num_elements_to_keep <= 0L )
            {
                this.elements.clear ();
            }
            else if ( num_elements_to_keep < (long) num_elements )
            {
                final int keep_index = (int) num_elements_to_keep - 1;
                for ( int e = this.elements.size () - 1;
                      e > keep_index;
                      e -- )
                {
                    this.elements.remove ( e );
                }
            }
            // Else do nothing, since we are supposed to keep at least
            // as many elements as we have in the first place.

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveKeptFirst.CONTRACT.violation ( this,
                                                                             new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Keeps the element(s) at the specified index(ices),
     * removing all others from the countable value being built.
     * </p>
     *
     * <p>
     * For example, starting from the countable elements
     * <code> { A, B, C, D, E } </code>, keeping the elements at indices
     * <code> { 0, 4 } </code> will keep the <code> A </code> and the
     * <code> E </code> elements, removing the others,
     * resulting in <code> { A, E } </code>.
     * </p>
     *
     * @param indices_to_keep The position(s) of the element(s) to keepe.
     *                        If any index is below 0L
     *                        or greater than or equal to the length
     *                        of the countable value being built, then
     *                        no elements will be kept.
     *                        If an index greater than
     *                        <code> Integer.MAX_VALUE </code> is specified
     *                        then the element counting backward from
     *                        <code> Countable.LAST </code> will be kept.
     *                        If the same index is specified more than once,
     *                        the element at that index will only
     *                        be kept once.  Must not be null.
     *
     * @return This countable elements, with the element(s) at
     *         the specified indices kept, and all others removed.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> keepIndices (
                                                 Countable<Long> indices_to_keep
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices_to_keep );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( indices_to_keep instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.KeepIndices.CONTRACT,
                                                         (Abnormal<?>) indices_to_keep );
        }

        final List<Long> sorted_indices =
            this.sortRemoveSubZeroIndices ( indices_to_keep );

        synchronized ( this.lock )
        {
            final int original_size = this.elements.size ();
            int num_removed = 0;
            int start_removal = 0;
            for ( long index_to_keep : sorted_indices )
            {
                final int kept_index;
                if ( index_to_keep < (long) Integer.MAX_VALUE )
                {
                    kept_index =
                        this.clampIndex ( index_to_keep - (long) num_removed );
                }
                else
                {
                    kept_index = this.clampIndex ( index_to_keep );
                }

                if ( kept_index < 0 )
                {
                    continue;
                }

                for ( int removed_index = kept_index - 1;
                      removed_index >= start_removal;
                      removed_index -- )
                {
                    this.elements.remove ( removed_index );
                }

                final int num_elements_removed = kept_index - start_removal;
                num_removed += num_elements_removed;
                start_removal = kept_index - num_removed + 1;
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveKeptIndices.CONTRACT.violation ( this,
                                                                              new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Removes the head of the countable value being built, leaving only
     * the last N elements (if any).
     * </p>
     *
     * @param num_elements_to_keep The number of elements to keep from the
     *                             end of the value being built.
     *                             If less than or equal to 0L then
     *                             the value is emptied out.
     *
     * @return This CountableElements, with only the specified number
     *         of elements (if any) kept from the end.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> keepLast (
                                              long num_elements_to_keep
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            if ( num_elements_to_keep <= 0L )
            {
                this.elements.clear ();
            }
            else if ( num_elements_to_keep < num_elements )
            {
                final int keep_index =
                    this.elements.size () - (int) num_elements_to_keep;
                for ( int e = keep_index - 1;
                      e >= 0;
                      e -- )
                {
                    this.elements.remove ( e );
                }
            }
            // Else do nothing, since we are supposed to keep at least
            // as many elements as we have in the first place.

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveKeptLast.CONTRACT.violation ( this,
                                                                            new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * @return The One very last element of this Countable elements,
     *         or No element if this value is empty.  Never null.
     */
    public ZeroOrOne<VALUE> last ()
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableElements.Last.CONTRACT,
                                     this.originalValue );
        }

        final List<VALUE> elements = new ArrayList<VALUE> ();
        synchronized ( this.lock )
        {
            if ( this.elements.size () > 0 )
            {
                final VALUE last_element =
                    this.elements.get ( this.elements.size () - 1 );
                elements.add ( last_element );
            }
        }

        final ZeroOrOne<VALUE> last_element =
            this.createZeroOrOne ( elements,
                                   CountableElements.MustHaveLast.CONTRACT,
                                   0L );

        return last_element;
    }


    /**
     * <p>
     * Inserts padding elements to make the value being built
     * a specific length.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { 1, 2, 3 } </code>
     * is padded with element <code> { 0 } </code>
     * at index <code> { 0 } </code> to reach target length <code> 6 </code>,
     * the result will be <code> { 0, 0, 0, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If there are multiple padding elements then each will be added
     * in sequence, followed by a repeat of the sequence, and so on,
     * until one of the elements is added to reach the final target length.
     * For example, starting from countable elements
     * <code> { 1, 2, 3 } </code>, padding the start index <code> { 0 } </code>
     * with <code> { A, B, C } </code> to reach target length <code> 7 </code>
     * would result in <code> { A, B, C, A, 1, 2, 3 } </code>.
     * </p>
     *
     * <p>
     * If the padding is to be inserted at multiple locations, then
     * one sequence of padding is inserted at the first index, then
     * one sequence at the seond index, and so on, until one sequence
     * has been inserted at each of the specified indices; then another
     * sequence is inserted at each index; and so on, until one of the
     * values inserted at one of the elements causes the countable elements
     * to reach the target length.  For example, with countable
     * countable elements <code> { 1, 2, 3 } </code>, padding both the
     * start and the end indices <code> { 0, 3 } </code> with
     * padding <code> { A, B, C } </code> to reach target length
     * <code> 11 </code> would result in
     * <code> { A, B, C, A, B, 1, 2, 3, A, B, C } </code>.
     * </p>
     *
     * @param padding The element(s) to insert at specific indices
     *                in this countable elements.  Must not be null.
     *
     * @param pad_at_indices Where to insert the padding in the
     *                       value being built.  Indices less than 0L
     *                       or greater than or equal to the length
     *                       of the countable value being built are
     *                       ignored.  An index greater than
     *                       <code> Integer.MAX_VALUE </code>
     *                       causes the the padding to be appended
     *                       AFTER the index counting backward from
     *                       <code> Countable.LAST </code>.
     *                       Must not be null.
     *
     * @param target_length How many elements this countable elements
     *                      should have after the padding has
     *                      been inserted.  If less than or equal to
     *                      the length of the value being built
     *                      (for example, if the target length is 0 or
     *                      or -1 or length - 1) then no padding
     *                      will be added.
     *
     * @return This CountableElements, with the specified padding added.
     *         However if this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> pad (
                                         Countable<VALUE> padding,
                                         Countable<Long> pad_at_indices,
                                         long target_length
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               padding, pad_at_indices );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( padding instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Pad.CONTRACT,
                                                         (Abnormal<?>) padding );
        }
        else if ( pad_at_indices instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Pad.CONTRACT,
                                                         (Abnormal<?>) pad_at_indices );
        }
        else if ( padding instanceof Unjust )
        {
            // Nothing to pad with.
            return this;
        }
        else if ( pad_at_indices instanceof Unjust )
        {
            // Nowhere to pad at.
            return this;
        }
        else if ( target_length < 0L
                  || target_length > (long) Integer.MAX_VALUE )
        {
            // Invalid target length.
            final BoundedDomain<Long> bounds =
                BoundedDomain.over ( 0L,
                                     (long) Integer.MAX_VALUE );
            final Contract<Long, Parameter3.MustBeInBounds.Violation> contract =
                new Parameter3.MustBeInBounds<Long> ( bounds );
            return this.createAbnormalCountableElementsInvalidInput ( contract.enforcer ().violation ( contract,
                                                                                                      this,
                                                                                                      new Long ( target_length ) ) );
        }

        final int num_indices = (int) pad_at_indices.length ();

        synchronized ( this.lock )
        {
            int length = this.elements.size ();
            int overall_offset = 0;
            while ( length < (int) target_length )
            {
                for ( long pad_at_index : pad_at_indices )
                {
                    if ( length >= (int) target_length )
                    {
                        break;
                    }

                    final int pad_base_index =
                        this.clampIndex ( pad_at_index );
                    if ( pad_base_index < 0 )
                    {
                        continue;
                    }

                    int current_index_offset = 0;
                    for ( VALUE padding_element : padding )
                    {
                        if ( length >= (int) target_length )
                        {
                            break;
                        }

                        int index = pad_base_index
                            + overall_offset + current_index_offset;

                        this.elements.add ( index, padding_element );

                        length ++;
                        current_index_offset ++;
                    }
                }

                overall_offset += num_indices;
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHavePadding.CONTRACT.violation ( this,
 new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Returns all elements in the given range(s).
     * </p>
     *
     * <p>
     * Each range is a pair of indices.
     * </p>
     *
     * <p>
     * For example, a Countable elements with elements
     * { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 }, when the ranges
     * { 0, 2, 7, 9 } (0-2, 7-9) are requested, would result in:
     * { 1, 2, 3, 8, 9, 10 }.
     * </p>
     *
     * <p>
     * If the indices range is empty, then the result is also empty.
     * </p>
     *
     * <p>
     * If the last range has only one index (i.e. the length of the
     * indices parameter % 2 is 1) then the final range is treated
     * as ( last_specified_index .. last_index_in_elements ).
     * </p>
     *
     * @param range_pairs The range(s) of elements to return.
     *                    Must not be null.
     *
     * @return This Countable elements, containing only
     *         the elements in the specified index ranges.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> range (
                                           Countable<Long> range_pairs
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               range_pairs );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( range_pairs instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Range.CONTRACT,
                                                         (Abnormal<?>) range_pairs );
        }

        final Iterator<Long> range_iterator =
            range_pairs.iterator ();

        synchronized ( this.lock )
        {
            if ( this.elements.size () > 0 )
            {
                final int original_size = this.elements.size ();
                int insert_index = original_size;
                while ( range_iterator.hasNext () )
                {
                    final long start = range_iterator.next ();
                    final long end;
                    if ( ! range_iterator.hasNext () )
                    {
                        end = (long) this.elements.size () - 1L;
                    }
                    else
                    {
                        end = range_iterator.next ();
                    }

                    final int start_clamped =
                        this.clampIndex ( start );
                    if ( start < 0 )
                    {
                        continue;
                    }

                    final int end_clamped =
                        this.clampIndex ( end );
                    if ( end < 0 )
                    {
                        continue;
                    }

                    final int direction;
                    if ( end < start )
                    {
                        direction = -1;
                    }
                    else
                    {
                        direction = 1;
                    }

                    for ( int index = start_clamped;
                          ( direction == 1 && index <= end_clamped )
                              || ( direction == -1 && index >= end_clamped );
                          index += direction )
                    {
                        final VALUE element = this.elements.get ( index );
                        this.elements.add ( insert_index,
                                            element );

                        insert_index ++;
                    }
                }

                this.removeFirst ( original_size );
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeInRange.CONTRACT.violation ( this,
                                                                         new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Removes all instance(s) of the specific element(s) from the
     * countable value being built.
     * </p>
     *
     * <p>
     * For example, starting from the countable elements
     * <code> { A, B, C, B, A } </code>, removing the elements
     * <code> { C, A } </code> will remove the one <code> C </code> and the
     * two <code> A </code> elements, resulting in <code> { B, B } </code>.
     * </p>
     *
     * @param elements_to_remove The element(s) to remove.
     *                           If any element is found more than once,
     *                           then all instances are removed.
     *                           Must not be null.
     *
     * @return This countable elements, with the specified element(s)
     *         removed.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> remove (
                                            Countable<VALUE> elements_to_remove
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               elements_to_remove );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( elements_to_remove instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Remove.CONTRACT,
                                                         (Abnormal<?>) elements_to_remove );
        }
        else if ( elements_to_remove instanceof Unjust )
        {
            // Nothing to remove.
            return this;
        }

        synchronized ( this.lock )
        {
            for ( VALUE element : elements_to_remove )
            {
                this.elements.remove ( element );
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustNotBeEmptyAfterRemove.CONTRACT.violation ( this,
                                                                                                       new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Removes the element(s) at the specified indices from the
     * countable value being built.
     * </p>
     *
     * <p>
     * For example, starting from the countable elements
     * <code> { A, B, C, D, E } </code>, removing the elements at indices
     * <code> { 0, 4 } </code> will remove the <code> A </code> and the
     * <code> E </code> elements, resulting in <code> { B, C, D } </code>.
     * </p>
     *
     * @param indices_to_remove The position(s) of the element(s) to remove.
     *                          If any index is below 0L
     *                          or greater than or equal to the length
     *                          of the countable value being built, then
     *                          no elements will be removed.
     *                          If an index greater than
     *                          <code> Integer.MAX_VALUE </code> is specified
     *                          then the element counting backward from
     *                          <code> Countable.LAST </code> will be removed.
     *                          If the same index is specified more than once,
     *                          the element at that index will only
     *                          be removed once.  Must not be null.
     *
     * @return This countable elements, with the element(s) at
     *         the specified indices removed.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> removeIndices (
                                                   Countable<Long> indices_to_remove
                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices_to_remove );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( indices_to_remove instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.RemoveIndices.CONTRACT,
                                                         (Abnormal<?>) indices_to_remove );
        }
        else if ( indices_to_remove instanceof Unjust )
        {
            // Nothing to remove.
            return this;
        }

        final List<Long> sorted_indices =
            this.sortRemoveSubZeroIndices ( indices_to_remove );

        synchronized ( this.lock )
        {
            final int original_size = this.elements.size ();
            long num_removed = 0L;
            for ( long index_to_remove : sorted_indices )
            {
                final int array_index;
                if ( index_to_remove <= (long) Integer.MAX_VALUE )
                {
                    array_index =
                        this.clampIndex ( index_to_remove - num_removed );
                }
                else
                {
                    array_index = this.clampIndex ( index_to_remove );
                }

                if ( array_index < 0 )
                {
                    continue;
                }

                this.elements.remove ( array_index );

                num_removed ++;
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustNotBeEmptyAfterRemoveIndices.CONTRACT.violation ( this,
                                                                                                      new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Removes the specified number of elements from the beginning of this
     * Countable elements.
     * </p>
     *
     * @param num_elements_to_remove The number of elements to remove.
     *                               If 0L or less then nothing is done.
     *                               If greater than the number of elements
     *                               in this Countable elements then
     *                               all elements are cleared.
     *
     * @return This Countable elements.  Never null.
     */
    public CountableElements<VALUE> removeFirst (
                                                 long num_elements_to_remove
                                                 )
                      throws ReturnNeverNull.Violation
    {
        if ( num_elements_to_remove <= 0L )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            if ( num_elements > 0 )
            {
                final int last = (int) ( num_elements_to_remove - 1L );
                if ( last >= this.elements.size () )
                {
                    this.elements.clear ();
                }
                else
                {
                    for ( int index = last;
                          index >= 0;
                          index -- )
                    {
                        this.elements.remove ( index );
                    }
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustNotBeEmptyAfterRemoveFirst.CONTRACT.violation ( this,
                                                                                                            new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Remove the specified number of elements from the end of this
     * Countable elements.
     * </p>
     *
     * @param num_elements_to_remove The number of elements to remove.
     *                               If 0L or less then nothing is done.
     *                               If greater than the number of elements
     *                               in this Countable elements then
     *                               all elements are cleared.
     *
     * @return This Countable elements.  Never null.
     */
    public CountableElements<VALUE> removeLast (
                                                long num_elements_to_remove
                                                )
                      throws ReturnNeverNull.Violation
    {
        if ( num_elements_to_remove <= 0L )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            if ( num_elements > 0 )
            {
                final int first = num_elements - (int) num_elements_to_remove;
                if ( first <= 0 )
                {
                    this.elements.clear ();
                }
                else
                {
                    for ( int index = this.elements.size () - 1;
                          index >= first;
                          index -- )
                    {
                        this.elements.remove ( index );
                    }
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustNotBeEmptyAfterRemoveLast.CONTRACT.violation ( this,
                                                                                                           new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Creates a new Countable elements which contains the same
     * element(s) as this value, repeated the specified number
     * of times.
     * </p>
     *
     * <p>
     * Specifying 1 repeat leaves the elements as-is.
     * </p>
     *
     * <p>
     * For example, if this is a CountableElements<Integer> whose
     * elements are <code> { A, B } </code>, then calling
     * <code> repeat ( 3 ) </code> would result in a new Countable
     * with elements <code> { A, B, A, B, A, B } </code>.
     * </p>
     *
     * @param repetitions The number of times to repeat the pattern
     *                    of elements.  If the number is less than
     *                    or equal to 1L then it is ignored and no
     *                    repetitions are performed.
     *
     * @return This countable elements, with its elements
     *         repeated the specified number of times.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> repeat (
                                            long repetitions
                                            )
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( repetitions <= 1L )
        {
            // No extra repetitions.
            return this;
        }

        synchronized ( this.lock )
        {
            if ( this.elements.size () > 0 )
            {
                final List<VALUE> elements_to_repeat =
                    new ArrayList<VALUE> ( this.elements );

                for ( long repeat = 0; repeat < repetitions; repeat ++ )
                {
                    this.elements.addAll ( elements_to_repeat );
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeRepeated.CONTRACT.violation ( this,
                                                                          new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Replaces any and all instances of the specified sub-value
     * with the specified replacement sub-value.
     * </p>
     *
     * <p>
     * For example, replacing all instances of <code> { B, C, D } </code>
     * with replacement <code> { X } </code> inside the countable elements
     * <code> { A, B, C, D, A, B, C, D, E } </code> would result
     * in <code> { A, X, A, X, E } </code>.
     * </p>
     *
     * @param sub_value The elements to find and replace in this
     *                  countable elements.  Must not be null.
     *
     * @param replacement The elements to insert in place of each matching
     *                    sequence.  Can be empty.  Must not be null.
     *
     * @return This countable elements, with each and every instance
     *         of the specified sub value replaced by the specified
     *         replacement sequence.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> replace (
                                             Countable<VALUE> sub_value,
                                             Countable<VALUE> replacement
                                             )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value, replacement );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( sub_value instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Replace.CONTRACT,
                                                         (Abnormal<?>) sub_value );
        }
        else if ( replacement instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Replace.CONTRACT,
                                                         (Abnormal<?>) replacement );
        }
        else if ( sub_value instanceof Unjust )
        {
            // Nothing to replace.
            return this;
        }

        final Iterator<VALUE> sub_iterator = sub_value.iterator ();
        if ( ! sub_iterator.hasNext () )
        {
            // Nothing to replace.
            return this;
        }

        int last_num_found = 0;
        int last_index = -1;
        final VALUE first_element = sub_iterator.next ();
        synchronized ( this.lock )
        {
            int start_index = 0;
            while ( start_index >= 0 )
            {
                start_index =
                    this.indexOf ( first_element, start_index );
                if ( start_index < 0 )
                {
                    break;
                }

                int num_found = 1;
                int end_index = start_index;
                for ( int seq = 0; seq < last_num_found; seq ++ )
                {
                    end_index ++;
                    if ( this.elements.get ( last_index + seq )
                         .equals ( this.elements.get ( end_index ) ) )
                    {
                        num_found ++;
                    }
                    else
                    {
                        break;
                    }
                }

                if ( num_found >= last_num_found )
                {
                    last_index = start_index;
                    boolean is_found_all_remaining_elements = true;
                    while ( sub_iterator.hasNext () )
                    {
                        final VALUE sub_element = sub_iterator.next ();
                        final int index = this.indexOf ( sub_element,
                                                         end_index + 1 );
                        if ( index < 0 )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }
                        else if ( index > ( end_index + 1 ) )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }

                        end_index = index;
                        num_found ++;
                    }

                    if ( is_found_all_remaining_elements )
                    {
                        for ( int remove_index = end_index;
                              remove_index >= start_index;
                              remove_index -- )
                        {
                            this.elements.remove ( remove_index );
                        }

                        int index = start_index;

                        for ( VALUE replacement_element : replacement )
                        {
                            this.elements.add ( index, replacement_element );
                            index ++;
                        }

                        start_index = index;
                    }

                    last_num_found = num_found;
                }

                start_index ++;
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveReplacement.CONTRACT.violation ( this,
                                                                               new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Replaces one or more element(s) at the specified indices with the
     * specified replacement sub-value.
     * </p>
     *
     * <p>
     * For example, 1 element at each index  <code> { 0, 2, LAST } </code>
     * with replacement <code> { X, Y } </code> inside the
     * <code> { A, B, C, D, E } </code> would result
     * in <code> { X, Y, B, X, Y, D, X, Y } </code>.
     * </p>
     *
     * @param num_elements_to_replace The number of elements to replace
     *                                at each index.  If less than
     *                                or equal to 0 then this method
     *                                does nothing.
     *
     * @param indices The indices of the elements to replace in this
     *                countable elements.  Must not be null.
     *
     * @param replacement The elements to insert in place of the element
     *                    at each specified index.  Can be empty.
     *                    Must not be null.
     *
     * @return This countable elements, with the element(s) at
     *         each index  replaced by the specified replacement sequence.
     *         If num_elements_to_replace is less than or equal to 0 then
     *         this countable elements is returned as-is.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If either of the specified values is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> replaceIndices (
                                                    int num_elements_to_replace,
                                                    Countable<Long> indices,
                                                    Countable<VALUE> replacement
                                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices, replacement );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( indices instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.ReplaceIndices.CONTRACT,
                                                         (Abnormal<?>) indices );
        }
        else if ( replacement instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.ReplaceIndices.CONTRACT,
                                                         (Abnormal<?>) replacement );
        }
        else if ( num_elements_to_replace <= 0 )
        {
            // No elements to replace, and we refuse to do inserts.
            return this;
        }
        else if ( indices instanceof Unjust )
        {
            // Nothing to replace.
            return this;
        }

        final List<Long> sorted_indices =
            this.sortRemoveSubZeroIndices ( indices );

        synchronized ( this.lock )
        {
            final int original_num_elements = this.elements.size ();
            int total_offset = 0;
            for ( long replace_index : sorted_indices )
            {
                final int array_index =
                    this.clampIndex ( replace_index
                                      + (long) total_offset );
                if ( array_index < 0 )
                {
                    continue;
                }

                for ( int remove_index =
                          array_index + num_elements_to_replace - 1;
                      remove_index >= array_index;
                      remove_index -- )
                {
                    this.elements.remove ( remove_index );
                }

                int add_index = array_index;
                for ( VALUE replacement_element : replacement )
                {
                    this.elements.add ( add_index, replacement_element );
                    add_index ++;
                }

                final int num_elements_added = add_index - array_index;
                total_offset = num_elements_added - num_elements_to_replace;
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveReplacedIndices.CONTRACT.violation (
                        this,
                        new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Reverses the order of the element(s) of this Countable elements.
     * </p>
     *
     * @return This countable elements, with its elements
     *         in reverse order.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> reverse ()
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            // We swap half the elements with their counterparts.
            // For example in { A, B, C, D } we swap A and D,
            // and B and C, to result in { D, C, B, A }.
            // If there are an odd number of elements we do nothing
            // with the middle one.
            final int length = this.elements.size ();
            final int half_length = length / 2;
            for ( int index = 0; index < half_length; index ++ )
            {
                final int swap_index = length - 1 - index;
                final VALUE element_a = this.elements.get ( index );
                final VALUE element_b = this.elements.get ( swap_index );
                this.elements.set ( index, element_b );
                this.elements.set ( swap_index, element_a );
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeReversed.CONTRACT.violation ( this,
                                                                          new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Re-orders the element(s) of this Countable elements using
     * the specified rank generator to assign a rank to each indexed
     * element of this Countable elements.
     * </p>
     *
     * @param ranker Generates the rank for each index'ed element of
     *               this Countable elements.  For example, a
     *               PseudoRandom number generator could be used.
     *               The lower the ranking number generated, the earlier
     *               in the sequence the corresponding indexed element
     *               will be placed.  Ties will result in the tied
     *               indexed elements maintaining their existing relative
     *               ordering.  If the ranker runs out of ranks, then
     *               subsequent indexed elements are placed at the end.
     *               Must not be null.
     *
     * @return This Countable elements, re-ordered so that the
     *         each indexed element is ordered according to the rank
     *         generated for it by the specified ranker.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public <RANK extends Comparable<? super RANK>>
        CountableElements<VALUE> shuffle (
                                         Value<RANK> ranker
                                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               ranker );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( ranker instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Shuffle.CONTRACT,
                                                         (Abnormal<?>) ranker );
        }

        final Iterator<RANK> rank_sequence;
        if ( ranker instanceof Infinite )
        {
            final Infinite<RANK> infinite_ranker = (Infinite<RANK>) ranker;
            rank_sequence = infinite_ranker.infiniteIterator ();
        }
        else
        {
            rank_sequence = ranker.iterator ();
        }

        synchronized ( this.lock )
        {
            // Look up of all indices that fall into each rank.
            final Map<RANK, List<Long>> ranks =
                new HashMap<RANK, List<Long>> ();

            // Indices which have not been ranked.
            final List<Long> unranked = new ArrayList<Long> ();

            for ( long index = 0L; index < (long) this.elements.size ();
                  index ++ )
            {
                if ( ! rank_sequence.hasNext () )
                {
                    unranked.add ( index );
                    continue;
                }

                final RANK rank = rank_sequence.next ();
                List<Long> sub_sequence = ranks.get ( rank );
                if ( sub_sequence == null )
                {
                    sub_sequence = new ArrayList<Long> ();
                    ranks.put ( rank, sub_sequence );
                }

                sub_sequence.add ( index );
            }

            int original_size = this.elements.size ();

            final List<RANK> ordered_ranks =
                new ArrayList<RANK> ( ranks.keySet () );
            Collections.sort ( ordered_ranks );
            for ( RANK rank : ordered_ranks )
            {
                final List<Long> sub_sequence = ranks.get ( rank );
                for ( long index : sub_sequence )
                {
                    final VALUE element = this.elements.get ( (int) index );
                    this.elements.add ( element );
                }
            }

            for ( long index : unranked )
            {
                final VALUE element = this.elements.get ( (int) index );
                this.elements.add ( element );
            }

            this.removeFirst ( original_size );

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeShuffled.CONTRACT.violation ( this,
                                                                          new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Sorts the element(s) of this Countable elements according to the
     * specified Order.
     * </p>
     *
     * @param order The Order in which to sort the element(s) of this
     *              Countable elements.  Must not be null.
     *
     * @return This countable elements, with its elements sorted
     *         according to the specified order.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> sort (
                                          Order<VALUE> order
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               order );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            Collections.sort ( this.elements, order );

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeSorted.CONTRACT.violation ( this,
                                                                        new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * <p>
     * Splits the countable value being built at the specified indices,
     * and returns a list of Countables, each one containing
     * one split segment.
     * </p>
     *
     * <p>
     * If a split is requested at index 0L, then the left side of the split
     * at index 0 will always contain No value.  If a split is requested
     * at an index greater than <code> Integer.MAX_VALUE </code>, then
     * the split will occur AFTER the element counting backward from
     * <code> Countable.LAST </code> (resulting in No value to the right
     * if the split is at <code> Countable.LAST </code>).
     * </p>
     *
     * <p>
     * Indices less than 0L or greater than the length of the
     * countable value being built (except for <code> Countable.Last </code> )
     * will be ignored.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { A, B, C } </code>
     * is split at indices <code> { 0, 2, LAST } </code> then the
     * four resulting Countables will be <code> {} </code> (empty),
     * <code> A, B </code>, <code> C </code> and <code> {} </code> (empty).
     * </p>
     *
     * @param split_at_indices The index or indices at which to split the
     *                         countable value being built.  Must not be null.
     *
     * @param split_index_goes_to Where to place the index around which the
     *                            split is happening: either at the start
     *                            of the subsequent list (Countable.FIRST),
     *                            at the end of the previous list
     *                            (Countable.LAST), or removed (any other
     *                            index).
     *
     * @return 0 or more split Countable values.
     *         If this Countable elements was constructed from
     *         an Error or Warning then an empty list will be returned.
     *         If the specified indices countable value is an Error or Warning
     *         then an empty list will be returned.
     *         Never null.  Never contains any null elements.
     */
    public List<Countable<VALUE>> splitAt (
                                           Countable<Long> split_at_indices,
                                           long split_index_goes_to
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               split_at_indices );

        if ( this.originalValue instanceof Abnormal )
        {
            return new ArrayList<Countable<VALUE>> ();
        }
        else if ( split_at_indices instanceof Abnormal )
        {
            return new ArrayList<Countable<VALUE>> ();
        }

        final int end_offset;
        final int start_offset;
        if ( split_index_goes_to == Countable.FIRST )
        {
            end_offset = -1;
            start_offset = 0;
        }
        else if ( split_index_goes_to == Countable.LAST )
        {
            end_offset = 0;
            start_offset = 1;
        }
        else
        {
            end_offset = -1;
            start_offset = 1;
        }

        final List<Long> sorted_indices =
            this.sortRemoveSubZeroIndices ( split_at_indices );

        final List<Countable<VALUE>> split_lists =
            new ArrayList<Countable<VALUE>> ();

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            int previous_index = 0;
            for ( long split_at_index : sorted_indices )
            {
                final int array_index = this.clampIndex ( split_at_index );
                if ( array_index < 0 )
                {
                    break;
                }

                final int split_index = array_index + end_offset;
                final int split_list_length = array_index - previous_index + 1;
                final List<VALUE> split_list;
                if ( split_list_length == 0 ) // {}, split, {...}
                {
                    split_list = new ArrayList<VALUE> ();
                }
                else
                {
                    split_list = this.elements.subList ( previous_index,
                                                         split_list_length );
                }

                final Countable<VALUE> split_countable =
                    this.createCountable ( split_list,
                                           ValueMustBeOne.CONTRACT,
                                           split_list );
                split_lists.add ( split_countable );

                previous_index = array_index + start_offset;
            }
        }

        return split_lists;
    }


    /**
     * <p>
     * Splits the countable value being built by the specified element(s)
     * pattern, and returns a list of Countables, each one containing
     * one split segment.
     * </p>
     *
     * <p>
     * For example, if the countable elements <code> { A, B, C } </code>
     * is split by pattern <code> { A, B } </code> then the
     * two resulting Countables will be <code> {} </code> (empty)
     * and <code> { C } </code>.
     * </p>
     *
     * @param split_by_sequence The pattern of element(s) which will
     *                          divide the elements of this
     *                          countable elements into a list of
     *                          countable values.  Must not be null.
     *
     * @return 0 or more split Countables.
     *         If this Countable elements was constructed from
     *         an Error or Warning then an empty list will be returned.
     *         If the specified sequence is an Error or Warning
     *         then an empty list will be returned.
     *         Never null.  Never contains any null elements.
     */
    public List<Countable<VALUE>> splitBy (
                                           Countable<VALUE> split_by_sequence
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               split_by_sequence );

        if ( this.originalValue instanceof Abnormal )
        {
            return new ArrayList<Countable<VALUE>> ();
        }
        else if ( split_by_sequence instanceof Abnormal )
        {
            return new ArrayList<Countable<VALUE>> ();
        }

        final Iterator<VALUE> split_by_iterator =
            split_by_sequence.iterator ();
        if ( ! split_by_iterator.hasNext () )
        {
            return new ArrayList<Countable<VALUE>> ();
        }

        final List<Countable<VALUE>> split_lists =
            new ArrayList<Countable<VALUE>> ();

        int last_num_found = 0;
        int last_index = -1;
        int split_list_start_index = 0;
        final VALUE first_element = split_by_iterator.next ();
        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            int start_index = 0;
            while ( start_index >= 0 )
            {
                start_index =
                    this.indexOf ( first_element, start_index );
                if ( start_index < 0 )
                {
                    final List<VALUE> split_list =
                        this.elements.subList ( split_list_start_index,
                                                num_elements - split_list_start_index );
                    final Countable<VALUE> split_countable =
                        this.createCountable ( split_list,
                                               ValueMustBeOne.CONTRACT,
                                               split_list );
                    split_lists.add ( split_countable );
                    break;
                }

                int num_found = 1;
                int end_index = start_index;
                for ( int seq = 0; seq < last_num_found; seq ++ )
                {
                    end_index ++;
                    if ( this.elements.get ( last_index + seq )
                         .equals ( this.elements.get ( end_index ) ) )
                    {
                        num_found ++;
                    }
                    else
                    {
                        break;
                    }
                }

                if ( num_found >= last_num_found )
                {
                    last_index = start_index;
                    boolean is_found_all_remaining_elements = true;
                    while ( split_by_iterator.hasNext () )
                    {
                        final VALUE sub_element = split_by_iterator.next ();
                        final int index = this.indexOf ( sub_element,
                                                         end_index + 1 );
                        if ( index < 0 )
                        {
                            start_index = num_elements;
                            break;
                        }
                        else if ( index > ( end_index + 1 ) )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }

                        end_index = index;
                        num_found ++;
                    }

                    if ( is_found_all_remaining_elements )
                    {
                        if ( start_index > split_list_start_index )
                        {
                            final int num_split_list_elements =
                                start_index - split_list_start_index;
                            final List<VALUE> split_list =
                                this.elements.subList ( split_list_start_index,
                                                        num_split_list_elements );
                            final Countable<VALUE> split_countable =
                                this.createCountable ( split_list,
                                                       ValueMustBeOne.CONTRACT,
                                                       split_list );
                            split_lists.add ( split_countable );
                        }

                        start_index = end_index;
                        split_list_start_index = end_index + 1;
                    }

                    last_num_found = num_found;
                }

                start_index ++;
                if ( start_index >= num_elements )
                {
                    if ( split_list_start_index < num_elements )
                    {
                        final List<VALUE> split_list =
                            this.elements.subList ( split_list_start_index,
                                                    num_elements - split_list_start_index );
                        final Countable<VALUE> split_countable =
                            this.createCountable ( split_list,
                                                   ValueMustBeOne.CONTRACT,
                                                   split_list );
                        split_lists.add ( split_countable );
                    }

                    break;
                }
            }
        }

        return split_lists;
    }


    /**
     * <p>
     * Adds all elements from that specified value which are not already
     * contained in this countable value being built, leaving the union
     * of this and that.
     * </p>
     *
     * <p>
     * Multiple copies of the same element will be kept, even though
     * this is a set operation at heart.
     * </p>
     *
     * @param that The counbtable value whose element unions will
     *             be stored in this countable elements.
     *             Must not be null.
     *
     * @return This countable elements, containing all the
     *         elements from the input and that specified value.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         If the specified value is an Error or Warning,
     *         then a new CountableElements constructed from one such,
     *         with the same violation, is returned.
     *         Never null.
     */
    public CountableElements<VALUE> union (
                                           Countable<VALUE> that
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }
        else if ( that instanceof Abnormal )
        {
            return this.createAbnormalCountableElements ( CountableElements.Union.CONTRACT,
                                                         (Abnormal<?>) that );
        }

        synchronized ( this.lock )
        {
            if ( this.elements.size () == 0 )
            {
                // Empt set union with any set S = S.
                for ( VALUE element : that )
                {
                    this.elements.add ( element );
                }
            }
            else if ( that instanceof Unjust )
            {
                // Any set S union with the empty set = S.
                // Do nothing.
            }
            else
            {
                final int original_size = this.elements.size ();
                for ( VALUE element : that )
                {
                    final int index = this.elements.indexOf ( element );
                    if ( index < 0
                         || index >= original_size )
                    {
                        this.elements.add ( element );
                    }
                }
            }

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustHaveUnion.CONTRACT.violation ( this,
                                                                        new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * @return This countable elements, containing only unique
     *         instances of elements, with all duplicates removed.
     *         If this Countable elements was constructed from
     *         an Error or Warning then it is returned as-is.
     *         Never null.
     */
    public CountableElements<VALUE> unique ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this;
        }

        synchronized ( this.lock )
        {
            final LinkedHashSet<VALUE> unique_elements =
                new LinkedHashSet<VALUE> ( this.elements );
            this.elements.clear ();
            this.elements.addAll ( unique_elements );

            if ( this.noneViolation == null
                 && this.elements.size () == 0 )
            {
                this.noneViolation =
                    CountableElements.MustBeUnique.CONTRACT.violation ( this,
                                                                        new ArrayList<VALUE> () );
            }
            else if ( this.noneViolation != null
                      && this.elements.size () > 0 )
            {
                this.noneViolation = null;
            }
        }

        return this;
    }


    /**
     * @see musaico.foundation.value.AbstractCountableListView#value()
     */
    @Override
    public Countable<VALUE> value ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.originalValue;
        }

        return super.value ();
    }


    /**
     * @see musaico.foundation.value.AbstractCountableListView#viewItems()
     */
    @Override
    protected List<VALUE> viewItems ()
    {
        synchronized ( this.lock )
        {
            return this.elements;
        }
    }
}
