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
 * A selection of items (elements or indices and so on), each of which
 * is in one or the other of two sets, but not in both sets.
 * </p>
 *
 * <p>
 * For example, the insert of { 1, 2, 3, 4 } and { 3, 4, 5 }
 * is { 1, 2, 5 }.
 * </p>
 *
 *
 * <p>
 * In Java every CountableSelection must be Serializable in order to
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
public class CountableInsert<VALUE extends Object, SELECTED_ITEM extends Object>
    extends AbstractCountableSelection<VALUE, SELECTED_ITEM>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The CountableSelection being operated upon.
    private final CountableSelection<VALUE, SELECTED_ITEM> selection;

    // The Countable value to insert into the selection.
    private final IdempotentAndCountable<SELECTED_ITEM> subValue;

    // The index at which to insert the subValue.
    private final long index;


    /**
     * <p>
     * Creates a new CountableInsert, placing the specified sub-value
     * at the specified index in the selected items (possibly after the
     * end of the selected items).
     * </p>
     *
     * @param selection The selected items (elements or indices and
     *                  so on) into which the sub-value will be inserted.
     *                  Must not be null.
     *
     * @param sub_value The item(s) (elements or indices and so on)
     *                  to insert into the selection.  Must not be null.
     *
     * @param index The index at which to insert the sub-value, such
     *              as <code> 0L </code> (start)
     *              or <code> 4L </code> (the 5th item)
     *              or <code> Countable.AFTER_END </code> (after the
     *              end of the selected items) or
     *              <code> Countable.FROM_END + 4L </code> (the 5th
     *              last item), and so on.  Can be positive or negative.
     */
    public CountableInsert (
                            CountableSelection<VALUE, SELECTED_ITEM> selection,
                            IdempotentAndCountable<SELECTED_ITEM> sub_value,
                            long index
                            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( selection ); // Throws ParametersMustNotBeNull.Violation.

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               selection, sub_value );

        this.selection = selection;
        this.subValue = sub_value;
        this.index = index;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#at(long)
     */
    @Override
    public final ZeroOrOne<SELECTED_ITEM> at (
                                              long unconverted_index
                                              )
        throws ReturnNeverNull.Violation
    {
        final long parent_length = this.selection.length ();
        final long sub_value_length = this.subValue.length ();
        final long inserted_length = this.index + sub_value_length;
        final long total_length = parent_length + sub_value_length;

        final long at_index = CountableView.index ( unconverted_index,
                                                    total_length );

        final ZeroOrOne<SELECTED_ITEM> selected_item;
        if ( at_index < 0L )
        {
            final Class<SELECTED_ITEM> item_class = this.itemClass ();
            // !!! Create specific violation.
            final ValueBuilder<SELECTED_ITEM> builder =
                new ValueBuilder<SELECTED_ITEM> ( item_class );
            selected_item = builder.buildZeroOrOne ();
        }
        else if ( at_index < this.index )
        {
            selected_item = this.selection.at ( at_index );
        }
        else if ( at_index < inserted_length )
        {
            final long sub_value_at_index = at_index - parent_length;
            selected_item = this.subValue.select ().at ( sub_value_at_index );
        }
        else
        {
            final long after_sub_value_index = at_index - sub_value_length;
            selected_item = this.selection.at ( after_sub_value_index );
        }

        return selected_item;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#has(long)
     */
    public final boolean has (
                              long unconverted_index
                              )
        throws ReturnNeverNull.Violation
    {
        final long parent_length = this.selection.length ();
        final long sub_value_length = this.subValue.length ();
        final long total_length = parent_length + sub_value_length;

        final long at_index = CountableView.index ( unconverted_index,
                                                    total_length );

        final boolean has_item;
        if ( at_index < 0L )
        {
            has_item = false;
        }
        else
        {
            has_item = true;
        }

        return has_item;
    }


    /**
     * @see java.lang.CountableSelection#iterator()
     */
    @Override
    public final Iterator<SELECTED_ITEM> iterator ()
    {
        final long parent_length = this.selection.length ();
        final long sub_value_length = this.subValue.length ();
        final long inserted_length = this.index + sub_value_length;
        final long total_length = parent_length + sub_value_length;

        final Iterator<SELECTED_ITEM> iterator =
            new Iterator<SELECTED_ITEM> ()
        {
            private final Iterator<SELECTED_ITEM> parentIterator =
                selection.iterator ();
            private final Iterator<SELECTED_ITEM> subValueIterator =
                subValue.iterator ();

            private long nextIndex = 0L;

            /**
             * @see java.util.Iterator#hasNext()
             */
            @Override
            public final boolean hasNext ()
            {
                if ( nextIndex >= 0L )
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }

            /**
             * @see java.util.Iterator#next()
             */
            @Override
            public final SELECTED_ITEM next ()
                throws ReturnNeverNull.Violation
            {
                final SELECTED_ITEM item;
                if ( this.nextIndex < 0L )
                {
                    item = null;
                }
                else if ( nextIndex < index )
                {
                    item = parentIterator.next ();
                }
                else if ( nextIndex < inserted_length )
                {
                    item = subValueIterator.next ();
                }
                else
                {
                    item = parentIterator.next ();
                }

                nextIndex ++;
                if ( nextIndex >= total_length )
                {
                    nextIndex = -1L;
                }

                this.contracts.check ( ReturnNeverNull.CONTRACT,
                                       item );

                return item;
            }

            /**
             * @see java.util.Iterator#remove()
             */
            @Override
            public final void remove ()
                throws UnsupportedOperationException
            {
                throw new UnsupportedOperationException ( this.getClass ().getSimpleName ()
                                                          + ".remove () not supported" );
            }
        };

        return iterator;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final long parent_length = this.selection.length ();
        final long sub_value_length = this.subValue.length ();
        final long total_length = parent_length + sub_value_length;

        return total_length;
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#name()
     */
    @Override
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return "insert";
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#parameterStrings()
     */
    @Override
    public final String [] parameterStrings ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new String [] { "" + this.subValue, 
                               "" + this.index };
    }


    /**
     * @see musaico.foundation.value.CountableSelection#range(long, long)
     */
    @Override
    public final IdempotentAndCountable<SELECTED_ITEM> range (
                                                              long unconverted_start_index,
                                                              long unconverted_end_index
                                                              )
        throws ReturnNeverNull.Violation
    {
        !!!;
        final LinkedHashSet<SELECTED_ITEM> insert = this.toSet ();
        final long length = (long) insert.size ();

        final AbstractCountableSelection.RangeIndices indices =
            new AbstractCountableSelection.RangeIndices ( unconverted_start_index,
                                                          unconverted_end_index,
                                                          length );

        final Class<SELECTED_ITEM> item_class = this.itemClass ();
        final ValueBuilder<SELECTED_ITEM> builder =
            new ValueBuilder<SELECTED_ITEM> ( item_class );
        if ( indices.startIndex == 0L
             && indices.endIndex == ( length - 1L ) )
        {
            // The whole insert.
            builder.addAll ( insert );
        }
        else if ( ! indices.isError
                  && indices.startIndex >= 0L
                  && indices.endIndex < length )
        {
            // Not the whole insert, but some valid sub-range.
            final List<SELECTED_ITEM> insert_list =
                new ArrayList<SELECTED_ITEM> ( insert );
            final List<SELECTED_ITEM> range_list =
                insert_list.subList ( (int) indices.startIndex,
                                          (int) indices.endIndex );
            builder.addAll ( range_list );
        }
        else
        {
            // Error.
            // Leave the value builder empty.
            // !!! create a violation.
        }

        final IdempotentAndCountable<SELECTED_ITEM> range =
            builder.build ();

        return range;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#toList()
     */
    @Override
    public final List<SELECTED_ITEM> toList ()
        throws ReturnNeverNull.Violation
    {
        final LinkedHashSet<SELECTED_ITEM> insert_set =
            this.toSet ();

        final List<SELECTED_ITEM> insert =
            new ArrayList<SELECTED_ITEM> ( insert_set );

        return insert;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#toSet()
     */
    @Override
    public final LinkedHashSet<SELECTED_ITEM> toSet ()
        throws ReturnNeverNull.Violation
    {
        synchronized ( this.lock )
        {
            if ( this.cache != null )
            {
                return this.cache;
            }
            else if ( this.right instanceof AbstractMultiple )
            {
                final AbstractMultiple<SELECTED_ITEM> right_multiple =
                    (AbstractMultiple<SELECTED_ITEM>) this.right;
                final List<SELECTED_ITEM> right_elements_list =
                    right_multiple.internalElements ();
                this.cache =
                    new LinkedHashSet<SELECTED_ITEM> ( right_elements_list );

                for ( SELECTED_ITEM item : this.left )
                {
                    if ( this.cache.contains ( item ) )
                    {
                        this.cache.remove ( item );
                    }
                    else
                    {
                        this.cache.add ( item );
                    }
                }

                return this.cache;
            }

            final Iterator<SELECTED_ITEM> left_iterator =
                this.left.iterator ();
            final Iterator<SELECTED_ITEM> right_iterator =
                this.right.iterator ();

            this.cache =
                new LinkedHashSet<SELECTED_ITEM> ();

            while ( left_iterator.hasNext ()
                    || right_iterator.hasNext () )
            {
                if ( left_iterator.hasNext () )
                {
                    final SELECTED_ITEM selected_item = left_iterator.next ();
                    if ( this.cache.contains ( selected_item ) )
                    {
                        this.cache.remove ( selected_item );
                    }
                    else
                    {
                        this.cache.add ( selected_item );
                    }
                }

                if ( right_iterator.hasNext () )
                {
                    final SELECTED_ITEM selected_item = right_iterator.next ();
                    if ( this.cache.contains ( selected_item ) )
                    {
                        this.cache.remove ( selected_item );
                    }
                    else
                    {
                        this.cache.add ( selected_item );
                    }
                }
            }

            return this.cache;
        }
    }
}
