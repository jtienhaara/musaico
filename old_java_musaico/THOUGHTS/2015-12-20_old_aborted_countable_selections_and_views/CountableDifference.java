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
 * For example, the difference of { 1, 2, 3, 4 } and { 3, 4, 5 }
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
public class CountableDifference<VALUE extends Object, SELECTED_ITEM extends Object>
    extends AbstractCountableSelection<VALUE, SELECTED_ITEM>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Synchronize cache accesses on this lock:
    private final Serializable lock = new String ();

    // The CountableSelection being operated upon.
    private final CountableSelection<VALUE, SELECTED_ITEM> left;

    // The Countable value to differentiate from the left operand.
    private final IdempotentAndCountable<SELECTED_ITEM> right;

    // MUTABLE:
    // Cached LinkedHashSet of differences between left and right.
    // Synchronized on this.lock.
    private LinkedHashSet<SELECTED_ITEM> cache = null;


    /**
     * <p>
     * Creates a new CountableDifference, selecting all of the
     * elements from the specified Countable value.
     * </p>
     *
     * @param left The selection of items to take the difference of, with
     *             respect to the right operand.  Must not be null.
     *
     * @param right The Countable value to take the difference of, with
     *              respect to the left operand.  Must not be null.
     */
    public CountableDifference (
                                CountableSelection<VALUE, SELECTED_ITEM> left,
                                IdempotentAndCountable<SELECTED_ITEM> right
                                )
        throws ParametersMustNotBeNull.Violation
    {
        super ( left ); // Throws ParametersMustNotBeNull.Violation.

        this.left = left;
        this.right = right;
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
        final List<SELECTED_ITEM> difference_list = this.toList ();
        final long length = (long) difference_list.size ();

        final long index =
            CountableView.index ( unconverted_index, length );

        final Class<SELECTED_ITEM> item_class = this.itemClass ();
        final ValueBuilder<SELECTED_ITEM> builder =
            new ValueBuilder<SELECTED_ITEM> ( item_class );
        if ( index < 0L )
        {
            // Index out of range.
            // !!! create a special violation.
            return builder.buildZeroOrOne ();
        }

        final SELECTED_ITEM item_at_index =
            difference_list.get ( (int) index );
        builder.add ( item_at_index );

        final ZeroOrOne<SELECTED_ITEM> item =
            builder.buildZeroOrOne ();

        return item;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#has(long)
     */
    public final boolean has (
                              long unconverted_index
                              )
        throws ReturnNeverNull.Violation
    {
        final LinkedHashSet<SELECTED_ITEM> difference = this.toSet ();
        final long length = (long) difference.size ();

        final long index =
            CountableView.index ( unconverted_index, length );

        if ( index < 0L )
        {
            // Index out of range.
            return false;
        }
        else
        {
            // The index is in range.
            return true;
        }
    }


    /**
     * @see java.lang.CountableSelection#iterator()
     */
    @Override
    public final Iterator<SELECTED_ITEM> iterator ()
    {
        final LinkedHashSet<SELECTED_ITEM> difference = this.toSet ();
        final Iterator<SELECTED_ITEM> iterator = difference.iterator ();
        return iterator;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final LinkedHashSet<SELECTED_ITEM> difference = this.toSet ();
        final long length = (long) difference.size ();
        return length;
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#name()
     */
    @Override
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return "difference";
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#parameterStrings()
     */
    @Override
    public final String [] parameterStrings ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new String [] { "" + this.right };
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
        final LinkedHashSet<SELECTED_ITEM> difference = this.toSet ();
        final long length = (long) difference.size ();

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
            // The whole difference.
            builder.addAll ( difference );
        }
        else if ( ! indices.isError
                  && indices.startIndex >= 0L
                  && indices.endIndex < length )
        {
            // Not the whole difference, but some valid sub-range.
            final List<SELECTED_ITEM> difference_list =
                new ArrayList<SELECTED_ITEM> ( difference );
            final List<SELECTED_ITEM> range_list =
                difference_list.subList ( (int) indices.startIndex,
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
        final LinkedHashSet<SELECTED_ITEM> difference_set =
            this.toSet ();

        final List<SELECTED_ITEM> difference =
            new ArrayList<SELECTED_ITEM> ( difference_set );

        return difference;
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
