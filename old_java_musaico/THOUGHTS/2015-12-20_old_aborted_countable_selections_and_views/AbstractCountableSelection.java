package musaico.foundation.value;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;


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
 * Partial implementation of the CountableSelect interface, providing
 * implementations of the boilerplate methods.
 * </p>
 *
 *
 * <p>
 * In Java every CountableSelect must be Serializable in order to
 * play nicely across RMI.  However users of the CountableSelection
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
public abstract class AbstractCountableSelection<VALUE extends Object, SELECTED_ITEM extends Object>
    implements CountableSelection<VALUE, SELECTED_ITEM>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractCountableSelection.class );


    /**
     * <p>
     * A conversion of range indices to fit into the specified length,
     * if possible.
     * </p>
     *
     * <p>
     * Each index can be non-negative (offset from the start,
     * Countable.FROM_START) or negative (offset from the end,
     * Countable.FROM_END), and offset by 0L or more.
     * </p>
     *
     * <p>
     * The converted indices are always either non-negative (if within
     * range and/or clamped) or -1L (if the parameters are invalid
     * and caused an error).
     * </p>
     */
    public static class RangeIndices
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractCountableSelection.serialVersionUID;

        /** The original start index.  Could be negative, meaning offset
         *  from the end (see Countable.FROM_END). */
        public final long unconvertedStartIndex;

        /** The original end index.  Could be negative, meaning offset
         *  from the end (see Countable.FROM_END). */
        public final long unconvertedEndIndex;

        /** The length of the full selection of items over which this
         *  range of indices was created. */
        public final long length;

        /** The start index.  Always 0L or greater unless the start_index
         *  was invalid or clamped past the end of the underlying selection,
         *  in both of which cases it will be -1L and the various flags
         *  below will be set accordingly. */
        public final long startIndex;

        /** The end index.  Always 0L or greater unless the start_index
         *  or end_index or both were invalid, or the end index was
         *  clamped past the end of the underlying selection,
         *  in all of which cases it will be -1L and the various flags
         *  below will be set accordingly. */
        public final long endIndex;

        /** The length of the converted range, in terms of number of items
         *  (elements or indices). */
        public final long rangeLength;

        /** The end index, squeezed to fit into a Java List.
         *  Note that if the start index is greater than 0L then the
         *  list end index may be greater than Integer.MAX_VALUE, as long
         *  as the number of items included in the range is less than
         *  or equal to Integer.MAX_VALUE, and so can fit into a List. */
        public final long endListIndex;

        /** The length of the range, possibly squeezed down from the
         *  rangeLength to fit into a Java List. */
        public final long rangeListLength;

        /** True if either or both of the indices was invalid.
         *  For example, end index less than start index. */
        public final boolean isError;

        /** True if the indices went beyond the underlying selection
         *  of items, and so had to be clamped to fit within the
         *  selection's bounds. */
        public final boolean isClamped;

        /** True if the entire range will fit into a Java List
         *  (rangeListLength is less than or equal to Integer.MAX_VALUE).
         *  Typically if a Countable value of the range of items is being
         *  created, and this flag is false, then a Partial result
         *  is returned, since the whole range cannot fit into a Value
         *  backed by a List. */
        public final boolean isFitsInList;

        /**
         * <p>
         * Converts the specified start and end indices into a valid range,
         * if possible, and provides some helpful information, such as
         * whether the specified range will fit into a Java List.
         * </p>
         *
         */
        public RangeIndices (
                             long unconverted_start_index,
                             long unconverted_end_index,
                             long length
                             )
        {
            this.unconvertedStartIndex = unconverted_start_index;
            this.unconvertedEndIndex = unconverted_end_index;
            this.length = length;

            boolean is_error = false;
            boolean is_clamped = false;

            final long maybe_out_of_bounds_start_index =
                CountableView.index ( unconverted_start_index,
                                      Long.MAX_VALUE );
            if ( maybe_out_of_bounds_start_index < 0L )
            {
                this.startIndex = -1L;
                is_error = true;
            }
            else if ( maybe_out_of_bounds_start_index < length )
            {
                this.startIndex = maybe_out_of_bounds_start_index;
            }
            else
            {
                this.startIndex = -1L;
                is_clamped = true;
            }

            final long maybe_out_of_bounds_end_index =
                CountableView.index ( unconverted_end_index,
                                      Long.MAX_VALUE );
            if ( maybe_out_of_bounds_end_index < this.startIndex )
            {
                this.endIndex = -1L;
                is_error = true;
            }
            else if ( maybe_out_of_bounds_end_index < 0L )
            {
                this.endIndex = -1L;
                is_error = true;
            }
            else if ( this.startIndex < 0L )
            {
                // No point in figuring out a valid end index, since
                // the start index is either past the end or completely
                // illegal.
                this.endIndex = this.startIndex;
            }
            else if ( maybe_out_of_bounds_end_index < length )
            {
                this.endIndex = maybe_out_of_bounds_end_index;
            }
            else
            {
                this.endIndex = length - 1;
                is_clamped = true;
            }

            boolean is_fits_in_list = true;
            this.rangeLength = this.endIndex - this.startIndex + 1L;
            if ( this.rangeLength <= (long) Integer.MAX_VALUE )
            {
                this.endListIndex = this.endIndex;
            }
            else
            {
                this.endListIndex =
                    this.startIndex - 1L + (long) Integer.MAX_VALUE;
                is_fits_in_list = false;
            }

            this.rangeListLength = this.endListIndex - this.startIndex + 1L;

            this.isError = is_error;
            this.isClamped = is_clamped;
            this.isFitsInList = is_fits_in_list;
        }
    }


    // Enforces parameter obligations and so on for us.
    private final ObjectContracts contracts;

    // The value from which this selection was created.
    private final IdempotentAndCountable<VALUE> source;

    // The type of selected items: ELEMENTS or INDICES and so on.
    private final CountableView.Items type;


    /**
     * <p>
     * Creates a new AbstractCountableSelection which is filtered, modified
     * or somehow or other related to the specified countable selection
     * of items.
     * </p>
     *
     * @param source_selection The selection from which this newly created
     *                         AbstractCountableSelection derives its
     *                         items content.  Must not be null.
     */
    public AbstractCountableSelection (
                                       CountableSelection<VALUE, SELECTED_ITEM> selection_source
                                       )
        throws Parameter1.MustNotBeNull.Violation
    {
        this ( classContracts.check ( Parameter1.MustNotBeNull.CONTRACT,
                                      selection_source )
                             .source (),
               selection_source.type () );
    }


    /**
     * <p>
     * Creates a new AbstractCountableSelection of selected items
     * (elements or indices) from the specified original Countable
     * value, with the specified type of selected items (ELEMENTS
     * or INDICES).
     * </p>
     *
     * @param source The original Countable value from which the
     *               selected items in this selection were originally
     *               derived.  Must not be null.
     *
     * @param type The type of selected items: ELEMENTS or INDICES
     *             and so on.  Must not be null.
     */
    public AbstractCountableSelection (
                                       Countable<VALUE> source,
                                       CountableView.Items type
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        // Make sure we either have a snapshot of the specified
        // source right now, or a source that is itself Idempotent.
        this ( source == null ? null : source.idempotent (),
               type );
    }


    /**
     * <p>
     * Creates a new AbstractCountableSelection of selected items
     * (elements or indices) from the specified original Countable
     * value, with the specified type of selected items (ELEMENTS
     * or INDICES).
     * </p>
     *
     * @param source The IdempotentAndCountable value from which the
     *               selected items in this selection were originally
     *               derived.  Must not be null.
     *
     * @param type The type of selected items: ELEMENTS or INDICES
     *             and so on.  Must not be null.
     */
    public AbstractCountableSelection (
                                       IdempotentAndCountable<VALUE> source,
                                       CountableView.Items type
                                       )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               source, type );

        this.source = source;
        this.type = type;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.value.CountableSelection#all()
     */
    @Override
    public IdempotentAndCountable<SELECTED_ITEM> all ()
        throws ReturnNeverNull.Violation
    {
        final IdempotentAndCountable<SELECTED_ITEM> all =
            this.range ( Countable.FROM_START, Countable.FROM_END );
        return all;
    }


    // Every AbstractCountableSelection must implement
    // <code> at ( long ). </code>


    /**
     * @return The ObjectContracts covering this selection.  Never null.
     */
    protected final ObjectContracts contracts ()
        throws ReturnNeverNull.Violation
    {
        return this.contracts;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#elements()
     */
    @Override
    public CountableView<VALUE, VALUE> elements ()
        throws ReturnNeverNull.Violation
    {
        final CountableView<VALUE, VALUE> elements =
            this.type ().viewElements ( this );

        return elements;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast to <?, SELECTED_ITEM> after check.
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        final AbstractCountableSelection<?, ?> that =
            (AbstractCountableSelection<?, ?>) obj;

        if ( that.itemClass () != this.itemClass () )
        {
            return false;
        }

        final long this_length = this.length ();
        final long that_length = that.length ();
        if ( this_length != that_length )
        {
            // This # selected items != that # selected items.
            return false;
        }

        final Iterator<SELECTED_ITEM> this_iterator = this.iterator ();
        final Iterator<?>     that_iterator = that.iterator ();
        while ( this_iterator.hasNext ()
                && that_iterator.hasNext () )
        {
            final SELECTED_ITEM this_item = this_iterator.next ();
            final Object that_item = that_iterator.next ();
            if ( this_item == null )
            {
                if ( that_item != null )
                {
                    // Null != any selected item.
                    return false;
                }
            }
            else if ( that_item == null )
            {
                // Any selected item != null.
                return false;
            }
            else if ( ! this_item.equals ( that_item ) )
            {
                // This selected item != that selected item.
                return false;
            }
        }

        if ( this_iterator.hasNext () )
        {
            // This is longer than that.
            return false;
        }
        else if ( that_iterator.hasNext() )
        {
            // This is shorter than that.
            return false;
        }

        // Everything matched.
        return true;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#first()
     */
    @Override
    public final ZeroOrOne<SELECTED_ITEM> first ()
        throws ReturnNeverNull.Violation
    {
        return this.at ( 0L );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * this.type.hashCode ()
            + 17 * this.source.hashCode ();
    }


    /**
     * @see musaico.foundation.value.CountableSelection#indices()
     */
    @Override
    public CountableView<VALUE, Long> indices ()
        throws ReturnNeverNull.Violation
    {
        final CountableView<VALUE, Long> indices =
            this.type ().viewIndices ( this );

        return indices;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#itemClass()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast value class to Class<SELECTED_ITEM>.
    public final Class<SELECTED_ITEM> itemClass ()
        throws ReturnNeverNull.Violation
    {
        final Class<SELECTED_ITEM> item_class = (Class<SELECTED_ITEM>)
            this.type ().itemClass ( this.source );
        return item_class;
    }


    // Every AbstractCountableSelection implementation must implement
    // <code> java.lang.Iterable.iterator () </code>.


    /**
     * @see musaico.foundation.value.CountableSelection#last()
     */
    @Override
    public final ZeroOrOne<SELECTED_ITEM> last ()
        throws ReturnNeverNull.Violation
    {
        return this.at ( Long.MAX_VALUE );
    }


    // Every AbstractCountableSelection must implement
    // <code> length () </code>.


    /**
     * @see musaico.foundation.value.CountableSelection#middle()
     */
    @Override
    public final IdempotentAndCountable<SELECTED_ITEM> middle ()
        throws ReturnNeverNull.Violation
    {
        final long length = this.length ();
        final long middle_index = length / 2;
        final long remainder = length % 2;

        final IdempotentAndCountable<SELECTED_ITEM> middle;
        if ( length == 0L )
        {
            // No middle selected items.
            middle = this.at ( 0L );
        }
        else if ( remainder != 0L )
        {
            // One middle selected item.
            middle = this.at ( middle_index );
        }
        else
        {
            // Two middle selected items.
            middle = this.range ( middle_index,
                                  middle_index + 1L );
        }

        return middle;
    }


    /**
     * @return The human-readable name of this class of selection.
     *         Never null.
     */
    public abstract String name ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates No selected item with the specified value violation.
     * </p>
     *
     * <p>
     * Convenience method.
     * </p>
     *
     * @param violation The contract violation which led to No value.
     *                  Must not be null.
     *
     * @return A newly created No value.  Never null.
     */
    protected No<SELECTED_ITEM> none (
                                      ValueViolation violation
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               violation );

        final Class<SELECTED_ITEM> item_class =
            this.itemClass ();
        final No<SELECTED_ITEM> none =
            new No<SELECTED_ITEM> ( item_class, violation );

        return none;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#one()
     */
    @Override
    public final ZeroOrOne<SELECTED_ITEM> one ()
        throws ReturnNeverNull.Violation
    {
        final ZeroOrOne<SELECTED_ITEM> maybe_one = this.at ( 0L );
        if ( ! ( maybe_one instanceof Just ) )
        {
            // Not even one.
            return maybe_one;
        }

        if ( this.has ( 1L ) )
        {
            // More than one.
            final ValueViolation violation =
                ValueMustBeOne.CONTRACT.violation ( this,
                                                    this );
            return this.none ( violation );
        }

        // One.
        return maybe_one;
    }


    /**
     * @return String representation(s) of all the parameters which
     *         affect the output of this selection.  Never null.
     *         Never contains any null elements.
     */
    public abstract String [] parameterStrings ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    // Every implementation of AbstractCountableSelection must implement
    // <code> range ( long, long )
    //            : IdempotentAndCountable<SELECTED_ITEM> </code>.


    /**
     * @see musaico.foundation.value.CountableSelection#source()
     */
    @Override
    public final IdempotentAndCountable<VALUE> source ()
        throws ReturnNeverNull.Violation
    {
        return this.source;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#toArray(java.lang.Object[])
     */
    @Override
    public final SELECTED_ITEM [] toArray (
                                           SELECTED_ITEM [] template
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final List<SELECTED_ITEM> as_list = this.toList ();
        final SELECTED_ITEM [] as_array = as_list.toArray ( template );

        return as_array;
    }


    // Every implementation of AbstractCountableSelection must implement
    // <code> toList () : List<SELECTED_ITEM> </code>.


    /**
     * @see musaico.foundation.value.CountableSelection#toMap(java.lang.Object)
     */
    @Override
    public final <MAP_TO_VALUE extends Object>
        LinkedHashMap<SELECTED_ITEM, MAP_TO_VALUE> toMap (
                                                          MAP_TO_VALUE default_value
                                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final LinkedHashSet<SELECTED_ITEM> as_set = this.toSet ();
        final LinkedHashMap<SELECTED_ITEM, MAP_TO_VALUE> as_map =
            new LinkedHashMap<SELECTED_ITEM, MAP_TO_VALUE> ();
        for ( SELECTED_ITEM selected_item : as_set )
        {
            as_map.put ( selected_item, default_value );
        }

        return as_map;
    }

    /**
     * @see musaico.foundation.value.CountableSelection#toSet()
     */
    @Override
    public LinkedHashSet<SELECTED_ITEM> toSet ()
        throws ReturnNeverNull.Violation
    {
        final List<SELECTED_ITEM> as_list = this.toList ();
        final LinkedHashSet<SELECTED_ITEM> as_set =
            new LinkedHashSet<SELECTED_ITEM> ();
        for ( SELECTED_ITEM selected_item : as_list )
        {
            if ( ! as_set.contains ( selected_item ) )
            {
                as_set.add ( selected_item );
            }
        }

        return as_set;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        final String source = "" + this.source ();
        sbuf.append ( source );
        final String name = this.name ();
        sbuf.append ( "." );
        sbuf.append ( name );
        sbuf.append ( "(" );
        final String [] parameters = this.parameterStrings ();
        boolean is_first = true;
        for ( String parameter : parameters )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( parameter );
        }

        sbuf.append ( ")" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.value.CountableSelection#type()
     */
    @Override
    public final CountableView.Items type ()
        throws ReturnNeverNull.Violation
    {
        return this.type;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#unique()
     */
    @Override
    public final IdempotentAndCountable<SELECTED_ITEM> unique ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        final LinkedHashSet<SELECTED_ITEM> unique_items = this.toSet ();
        final ValueBuilder<SELECTED_ITEM> builder =
            new ValueBuilder<SELECTED_ITEM> ( this.itemClass (),
                                              unique_items );
        final IdempotentAndCountable<SELECTED_ITEM> unique = builder.build ();
    }
}
