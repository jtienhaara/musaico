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
 * A mutable offshoot from a Countable value, which can be used to filter,
 * sort, replace, and so on, and build new Countable values.
 * </p>
 *
 * <p>
 * The AbstractCountableListView is mutable.  It potentially changes
 * with every operation.  You can create a defensive copy of an
 * AbstractCountableListView with <code> duplicate () </code>, if you
 * are worried about someone else messing with your
 * elements / indices / and so on.
 * </p>
 *
 *
 * <p>
 * In Java every CountableView must be Serializable in order to
 * play nicely across RMI.  However users of the CountableView
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
public abstract class AbstractCountableListView<VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
    implements CountableView<VALUE, VIEW_ITEM, VIEW>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractCountableListView.class );


    // Enforces parameter obligations and so on for us.
    protected final ObjectContracts contracts;

    // Synchronize critical sections on this token:
    protected final Serializable lock = new String ();

    // The original Countable value, withOUT any operations
    // applied to its data.
    protected final Countable<VALUE> originalValue;

    // What type of view is this?  VALUE class for the elements
    // view, Long.class for the indices view, and so on.
    protected final Class<VIEW_ITEM> viewItemClass;

    // The current List of elements from the original Countable value,
    // after any processing has been performed on them.
    // MUTABLE.  Changes as methods are invoked on this
    //           countable value view.
    //           Must only be modified in critical sections.
    protected final List<VALUE> elements;

    // The current contract violation which led to no elements
    // being in the list.  Whenever we have multiple elements we
    // do NOT create a "not one" violation, we rely on the default
    // MustBeOne contract violation.  But when we filter out elements
    // and end up with nothing, we keep track of the reason for
    // losing all the elements, so that the caller can figure
    // out what (if anything) went wrong.
    // MUTABLE.  Changes as methods are invoked on this
    //           countable value view.
    //           Must only be modified in critical sections.
    protected ValueViolation noneViolation = null;


    /**
     * <p>
     * Creates a new AbstractCountableListView which will work on the specified
     * Countable and elements.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable value view.  Must not be null.
     *
     * @param view_item_class What type of view is this?  VALUE class
     *                        for the elements view, Long.class
     *                        for the indices view, and so on.
     *                        Must not be null.
     *
     * @param elements The 0 or more elements of this countable view.
     *                 Must not be null.  Must not contain any null elements.
     */
    public AbstractCountableListView (
                                      Countable<VALUE> countable,
                                      Class<VIEW_ITEM> view_item_class,
                                      List<VALUE> elements
                                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( countable,
               view_item_class,
               elements,
               null );
    }


    /**
     * <p>
     * Creates a new AbstractCountableListView which will work on the specified
     * Countable and single element.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable value view.  Must not be null.
     *
     * @param view_item_class What type of view is this?  VALUE class
     *                        for the elements view, Long.class
     *                        for the indices view, and so on.
     *                        Must not be null.
     *
     * @param element The one element of this countable view.
     *                Must not be null.
     */
    public AbstractCountableListView (
                                      Countable<VALUE> countable,
                                      Class<VIEW_ITEM> view_item_class,
                                      VALUE element
                                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( countable,
               view_item_class,
               AbstractCountableListView.createList ( element ),
               null );
    }


    /*
     * <p>
     * Creates a list from the specified element(s).  Final for speed.
     * </p>
     */
    private static final <ELEMENT extends Object>
        List<ELEMENT> createList (
                                  ELEMENT element
                                  )
    {
        final List<ELEMENT> list = new ArrayList<ELEMENT> ();
        list.add ( element );

        return list;
    }


    /**
     * <p>
     * Creates a new AbstractCountableListView which will work on the specified
     * Countable and elements.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable value view.  Must not be null.
     *
     * @param view_item_class What type of view is this?  VALUE class
     *                        for the elements view, Long.class
     *                        for the indices view, and so on.
     *                        Must not be null.
     *
     * @param elements The 0 or more elements of this countable view.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param none_violation The initial "no value" violation.
     *                       Can be null for default.
     */
    protected AbstractCountableListView (
                                         Countable<VALUE> countable,
                                         Class<VIEW_ITEM> view_item_class,
                                         List<VALUE> elements,
                                         ValueViolation none_violation
                                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               countable, elements );

        this.originalValue = countable;
        this.viewItemClass = view_item_class;
        this.elements = new ArrayList<VALUE> ( elements );

        if ( none_violation != null )
        {
            this.noneViolation = none_violation;
        }
        else if ( countable instanceof Unjust )
        {
            Unjust<VALUE> unjust = (Unjust<VALUE>) countable;
            this.noneViolation = unjust.valueViolation ();
        }
        else if ( this.elements.size () == 0 )
        {
            this.noneViolation =
                ValueMustNotBeEmpty.CONTRACT.violation ( this,
                                                         new ArrayList<VALUE> () );
        }
        else
        {
            this.noneViolation = null;
        }

        this.contracts = new ObjectContracts ( this );
    }




    /**
     * <p>
     * Returns the specified index, clamped to an index within range
     * of this view's element indices.
     * </p>
     *
     * <p>
     * For example, if 0L is passed in but this view has no elements, -1
     * will be returned.  Or if <code> Countable.LAST </code> is passed
     * in, and this view has 17 elements, then 16 will be returned.
     * Or if <code> Couuntable.LAST - 16 </code> is passed in, and
     * this view has 17 elements, then 0 will be returned.  And so on.
     * </p>
     *
     * <p>
     * If you are going to do anything with the result, you must
     * synchronize on <code> AbstractCountableListView.lock </code>.
     * Otherwise the clamped index might no longer be in bounds
     * by the time you try to do something with it.
     * </p>
     *
     * @param index The long index.  Can be any value, including
     *              <code> Countable.LAST </code> and so on.
     *
     * @return The int index within the bounds
     *         <code> (0, size()-1) </code> inclusive; or -1 if
     *         the specified index is out of bounds.
     */
    protected final int clampIndex (
                                    long index
                                    )
    {
        if ( index < 0L )
        {
            return -1;
        }

        final int clamped_index;
        synchronized ( this.lock )
        {
            final int size = this.elements.size ();
            if ( index <= (long) Integer.MAX_VALUE )
            {
                // Count forward from 0L.
                if ( index >= (long) size )
                {
                    clamped_index = -1;
                }
                else
                {
                    clamped_index = (int) index;
                }
            }
            else
            {
                // Count backward from Countable.LAST.
                final long subtract = Long.MAX_VALUE - index;
                if ( subtract >= (long) size )
                {
                    clamped_index = -1;
                }
                else
                {
                    clamped_index = size - (int) subtract - 1;
                }

            }
        }

        return clamped_index;
    }


    /**
     * <p>
     * Returns a newly created view items (elements / indices / and so on)
     * Error due to the specified contract being violated with an
     * abnormal input.
     * </p>
     *
     * @param violated_contract The Contract which was violated by an
     *                          abnormal input.  For example, if
     *                          <code> findAll () </code> receives an
     *                          abnormal input, or operates on an
     *                          abnormal original value, then the
     *                          <code> CountableBuilder.FindAll </code>
     *                          contract has been violated.
     *                          Must not be null.
     *
     * @param abnormal_value The input which violates the contract.
     *                       Must not be null.
     */
    protected Error<VIEW_ITEM> createAbnormal (
            Contract<Iterable<?>, ValueMustNotBeAbnormal.Violation> violated_contract,
            Abnormal<?> abnormal_value
            )
    {
        final ValueViolation violation =
            violated_contract.enforcer ().violation ( violated_contract,
                                                      this,
                                                      abnormal_value );
        violation.initCause ( abnormal_value.valueViolation () );
        final Error<VIEW_ITEM> abnormal =
            new Error<VIEW_ITEM> ( this.viewItemClass,
                                   violation );
        return abnormal;
    }


    /**
     * <p>
     * Returns a newly created ValueViolation due to the specified
     * "must have XYZ" contract being violated.
     * </p>
     *
     * @param violated_contract The Contract which was violated.
     *                          For example, if
     *                          <code> element () </code> does not have
     *                          an element at the requested index, then the
     *                          <code> CountableElements.MustHaveElement </code>
     *                          contract has been violated.
     *                          Must not be null.
     *
     * @param input The input object or elements/indices array which led to the
     *              contract being violated.
     *              Could be a Countable value, a single index,
     *              a Filter, and so on, depending on the function.
     *              Must not be null.
     */
    protected <VIOLATION extends ValueViolation>
        VIOLATION createViolation (
                                   Contract<Iterable<?>, VIOLATION> violated_contract,
                                   Object input
                                   )
    {
        final Iterable<?> inspectables;
        if ( input instanceof Iterable )
        {
            inspectables = (Iterable<?>) input;
        }
        else
        {
            final List<Object> list = new ArrayList<Object> ();
            list.add ( input );
            inspectables = list;
        }

        final VIOLATION violation =
            violated_contract.enforcer ().violation ( violated_contract,
                                                      this,
                                                      inspectables );

        return violation;
    }


    /**
     * <p>
     * Creates a new Countable value out of the specified list
     * of view items (elements / indices / and so on),
     * possibly relying on the original countable value to build
     * the result.
     * </p>
     *
     * @param contents The list of contents for the new countable value.
     *                 Must not be null.  Must not contain any null
     *                 elements.
     *
     * @param contract_for_no_contents The contract which will be violated
     *                                 if the specified contents list
     *                                 is empty.  Must not be null.
     *
     * @param input The input to some function which led to the
     *              specified contents being turned into a Countable
     *              value.  Could be a Countable value, a single index,
     *              a Filter, and so on, depending on the function.
     *              Must not be null.
     *
     * @return A newly created Countable value containing the specified
     *         contents.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast ...<VALUE> to ...<VIEW_ITEM> only
                                   // if VALUE.class == VIEW_ITEM.class.
    protected <VIOLATION extends ValueViolation>
        Countable<VIEW_ITEM> createCountable (
                                              List<VIEW_ITEM> contents,
                                              Contract<Iterable<?>, VIOLATION> violated_contract,
                                              Object input
                                              )
    {
        final Countable<VIEW_ITEM> countable;
        if ( this.viewItemClass == this.originalValue.expectedClass ()
             && ( this.originalValue instanceof AbstractMultiple ) )
        {
            // Multiple original value (such as Many).
            // Let the abstract multiple value decide what kind of
            // Countable value to create.
            final ValueViolation violation =
                this.createViolation ( violated_contract,
                                       input );
            final AbstractMultiple<VIEW_ITEM> multiple =
                (AbstractMultiple<VIEW_ITEM>) this.originalValue;
            countable = multiple.createNewCountable ( contents,
                                                      violation );
        }
        else if ( contents.size () == 0 )
        {
            // Empty value.
            // Use the specified contract violation.
            final ValueViolation violation =
                this.createViolation ( violated_contract,
                                       input );
            countable = new No<VIEW_ITEM> ( this.viewItemClass,
                                            violation );
        }
        else
        {
            // One or more contents.
            // Just use a standard ValueBuilder.
            final ValueBuilder<VIEW_ITEM> contents_builder =
                new ValueBuilder<VIEW_ITEM> ( this.viewItemClass );
            countable = contents_builder.build ();
        }

        return countable;
    }


    /**
     * <p>
     * Creates a new No or One value out of the specified list of
     * view items (elements or indices and so on).
     * </p>
     *
     * @param contents The list of contents for the new ZeroOrOne value.
     *                 Must not be null.  Must not contain any null
     *                 elements.
     *
     * @param contract_for_no_contents The contract which will be violated
     *                                 if the specified contents list
     *                                 is empty.  Must not be null.
     *
     * @param input The input to some function which led to the
     *              specified contents being turned into a ZeroOrOne
     *              value.  Could be a Countable value, a single index,
     *              a Filter, and so on, depending on the function.
     *              Must not be null.
     *
     * @return A newly created ZeroOrOne value containing the specified
     *         contents.  Never null.
     */
    protected <VIOLATION extends ValueViolation>
        ZeroOrOne<VIEW_ITEM> createZeroOrOne (
                                              List<VIEW_ITEM> contents,
                                              Contract<Iterable<?>, VIOLATION> violated_contract,
                                              Object input
                                              )
    {
        final ValueViolation violation =
            this.createViolation ( violated_contract,
                                   input );

        final ZeroOrOne<VIEW_ITEM> zero_or_one;
        if ( contents.size () == 0 )
        {
            // Empty value.
            // Use the specified contract violation.
            zero_or_one = new No<VIEW_ITEM> ( this.viewItemClass,
                                              violation );
        }
        else
        {
            // One or more view item (element / index / and so on) contents.
            // Just use a standard ValueBuilder.
            final ValueBuilder<VIEW_ITEM> contents_builder =
                new ValueBuilder<VIEW_ITEM> ( this.viewItemClass );
            zero_or_one = contents_builder.buildZeroOrOne ();
        }

        return zero_or_one;
    }


    /**
     * <p>
     * Returns a newly created No view items (elements / indices / and
     * so on) value due to the specified "must have XYZ" contract
     * being violated.
     * </p>
     *
     * @param violated_contract The Contract which was violated.
     *                          For example, if
     *                          <code> elements () </code> does not have
     *                          an element at the requested index, then the
     *                          <code> CountableElements.MustHaveElement </code>
     *                          contract has been violated.
     *                          Must not be null.
     *
     * @param input The input object or elements or indices array or Filter
     *              and so on which led to the contract being violated.
     *              Must not be null.
     */
    protected <VIOLATION extends ValueViolation>
        No<VIEW_ITEM> createNone (
                                  Contract<Iterable<?>, VIOLATION> violated_contract,
                                  Object input
                                  )
    {
        final ValueViolation violation =
            this.createViolation ( violated_contract,
                                   input );
        final No<VIEW_ITEM> no_contents =
            new No<VIEW_ITEM> ( this.viewItemClass,
                                violation );
        return no_contents;
    }


    /**
     * @see musaico.foundation.value.CountableView#duplicate()
     */
    public abstract VIEW duplicate ()
        throws ReturnNeverNull.Violation;


    /*
     * @return The first index of the specified element on or after
     *         the specified starting index.
     *
     * Final for speed, since this method is used internally a fair bit.
     */
    protected final int indexOf (
                                 VALUE element,
                                 int from_index
                                 )
    {
        final int num_elements;
        final List<VALUE> sub_list;
        synchronized ( this.lock )
        {
            if ( from_index == 0 )
            {
                return this.elements.indexOf ( element );
            }

            num_elements = this.elements.size ();
            sub_list = this.elements.subList ( from_index,
                                               num_elements - from_index );
        }

        final int sub_list_index = sub_list.indexOf ( element );
        if ( sub_list_index < 0 )
        {
            return sub_list_index;
        }
        else
        {
            final int index = sub_list_index + from_index;
            return index;
        }
    }


    /*
     * @return The last index of the specified element on or before
     *         the specified starting index.
     *
     * Final for speed, since this method is used internally.
     */
    protected final int lastIndexOf (
                                     VALUE element,
                                     int from_index
                                     )
    {
        final List<VALUE> sub_list;
        synchronized ( this.lock )
        {
            final int size = this.elements.size ();
            if ( from_index == ( size - 1 ) )
            {
                return this.elements.lastIndexOf ( element );
            }

            sub_list = this.elements.subList ( 0,
                                               from_index + 1 );
        }

        final int index = sub_list.lastIndexOf ( element );
        return index;
    }


    /**
     * @see musaico.foundation.value.CountableView#length()
     */
    @Override
    public One<Long> length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        long length;
        synchronized ( this.lock )
        {
            length = (long) this.elements.size ();
        }

        final One<Long> one_length = new One<Long> ( Long.class, length );

        return one_length;
    }


    /*
     * <p>
     * Removes all indices less than 0L, removes duplicates,
     * sorts the indices, then returns the sorted list.
     * </p>
     *
     * <p>
     * Final for speed, since this method is used internally.
     * </p>
     */
    protected final List<Long> sortRemoveSubZeroIndices (
                                                         Countable<Long> indices
                                                         )
    {
        final Set<Long> unique_indices = new HashSet<Long> ();
        for ( long index : indices )
        {
            if ( index >= 0L )
            {
                unique_indices.add ( index );
            }
        }

        final List<Long> sorted_indices =
            new ArrayList<Long> ( unique_indices );
        Collections.sort ( sorted_indices );

        return sorted_indices;
    }


    /**
     * @see musaico.foundation.value.CountableView#toArray(java.lang.Object[])
     */
    @Override
    public VIEW_ITEM [] toArray (
                                 VIEW_ITEM [] template
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               template );

        if ( this.originalValue instanceof Abnormal )
        {
            return new ArrayList<VIEW_ITEM> ().toArray ( template );
        }

        final VIEW_ITEM [] contents_array;
        synchronized ( this.lock )
        {
            contents_array = this.viewItems ().toArray ( template );
        }

        return contents_array;
    }


    /**
     * @see musaico.foundation.value.CountableView#toList()
     */
    @Override
    public List<VIEW_ITEM> toList ()
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return new ArrayList<VIEW_ITEM> ();
        }

        final List<VIEW_ITEM> contents_list;
        synchronized ( this.lock )
        {
            contents_list = new ArrayList<VIEW_ITEM> ( this.viewItems () );
        }

        return contents_list;
    }


    /**
     * @see musaico.foundation.value.CountableView#toMap()
     */
    @Override
    public <MAP_TO_VALUE extends Object>
        LinkedHashMap<VIEW_ITEM, MAP_TO_VALUE> toMap (
                                                      MAP_TO_VALUE default_value
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               default_value );


        if ( this.originalValue instanceof Abnormal )
        {
            return new LinkedHashMap<VIEW_ITEM, MAP_TO_VALUE> ();
        }

        final LinkedHashMap<VIEW_ITEM, MAP_TO_VALUE> contents_map =
            new LinkedHashMap<VIEW_ITEM, MAP_TO_VALUE> ();
        synchronized ( this.lock )
        {
            for ( VIEW_ITEM view_item : this.viewItems () )
            {
                contents_map.put ( view_item, default_value );
            }
        }

        return contents_map;
    }


    /**
     * @see musaico.foundation.value.CountableView#toSet()
     */
    @Override
    public LinkedHashSet<VIEW_ITEM> toSet ()
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return new LinkedHashSet<VIEW_ITEM> ();
        }

        final LinkedHashSet<VIEW_ITEM> contents_set;
        synchronized ( this.lock )
        {
            contents_set = new LinkedHashSet<VIEW_ITEM> ( this.viewItems () );
        }

        return contents_set;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        synchronized ( this.lock )
        {
            for ( VIEW_ITEM view_item : this.viewItems () )
            {
                if ( is_first )
                {
                    is_first = false;
                }
                else
                {
                    sbuf.append ( "," );
                }

                final String view_item_as_string =
                    One.convertActualValueToString ( view_item );

                sbuf.append ( " " + view_item_as_string );
            }
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.value.CountableView#value()
     */
    @Override
    public Countable<VIEW_ITEM> value ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( ValueMustNotBeAbnormal.CONTRACT,
                                         (Abnormal<VALUE>) this.originalValue );
        }

        final ValueBuilder<VIEW_ITEM> builder;
        synchronized ( this.lock )
        {
            // All the elements may have been filtered out or
            // removed.  If so, use the violation that was set
            // at that time to explain why there are now no elements.
            builder = new ValueBuilder<VIEW_ITEM> ( this.viewItemClass,
                                                    this.noneViolation );

            builder.addAll ( this.viewItems () );
        }

        final Countable<VIEW_ITEM> value = builder.build ();

        return value;
    }


    /**
     * @return The raw, mutable view items (elements / indices / and so on,
     *         depending on this class) of this countable value view.
     *         Do not expose this list to the outside world, as it could
     *         be carefully protected by AbstractAbstractCountableListView.lock
     *         (such as with the elements view, which returns the
     *         list of elements being operated on directly).
     *         The implementor need not synchronize.  The caller must
     *         always synchronize on AbstractAbstractCountableListView.lock while
     *         invoking this method.  The result can be empty.
     *         Never null.  Never contains any null elements.
     */
    protected abstract List<VIEW_ITEM> viewItems ()
        throws ReturnNeverNull.Violation;
}
