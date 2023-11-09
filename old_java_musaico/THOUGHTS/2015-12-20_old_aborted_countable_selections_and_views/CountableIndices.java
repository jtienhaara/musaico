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
 * A selection of the indices which refer to the items in a
 * selection of elements.
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
public class CountableIndices<VALUE extends Object>
    extends AbstractCountableSelection<VALUE, Long>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CountableIndices.class );


    /** Iterates through the indices of a selection of elements. */
    public static class IndexIterator
        implements Iterator<Long>, Serializable
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            CountableIndices.serialVersionUID;

        // Enforces constructor and static method parameter obligations for us.
        private static final ObjectContracts classContracts =
            new ObjectContracts ( CountableIndices.IndexIterator.class );

        // Enforces method parameter obligations and guarantees for us.
        private final ObjectContracts contracts;

        // Lock critical sections on this token:
        private final Serializable lock = new String ();

        // The elements to iterate over.
        private final Iterator<?> elementsIterator;

        // The index to return the next time next () is called, or -1L
        // after we've reached the end.
        private long nextIndex = 0L;


        /**
         * <p>
         * Creates a new CountableIndices.IndexIterator
         * over the specified elements.
         * </p>
         *
         * @param elements_iterator Iterates over the elements while
         *                          this class increments the current
         *                          index number.  Must not be null.
         *                         Must never iterate over a null element.
         */
        public IndexIterator (
                              Iterator<?> elements_iterator
                              )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   elements_iterator );

            this.elementsIterator = elements_iterator;

            this.contracts = new ObjectContracts ( this );
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public final boolean hasNext ()
        {
            synchronized ( this.lock )
            {
                return this.elementsIterator.hasNext ();
            }
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public final Long next ()
            throws ReturnNeverNull.Violation
        {
            final long current_index;
            synchronized ( this.lock )
            {
                if ( ! this.elementsIterator.hasNext () )
                {
                    current_index = -1L;
                    this.nextIndex = -1L;
                }
                else
                {
                    this.elementsIterator.next ();
                    current_index = this.nextIndex;
                    this.nextIndex ++;
                }
            }

            return current_index;
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
    }


    // The selection of elements underlying this selection of indices.
    private final CountableSelection<VALUE, VALUE> elements;


    /**
     * <p>
     * Creates a new CountableIndices, selecting the indices from the
     * specified selection of elements (0L, 1L, 2L, ..., length - 1L).
     * </p>
     *
     * @param elements The elements referenced by this indices in this
     *                 selection.  Must not be null.
     */
    public CountableIndices (
                             CountableSelection<VALUE, VALUE> elements
                             )
        throws Parameter1.MustNotBeNull.Violation
    {
        super ( classContracts.check ( Parameter1.MustNotBeNull.CONTRACT,
                                       elements )
                              .source (),
                CountableView.Items.INDICES );

        this.elements = elements;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#at(long)
     */
    @Override
    public final ZeroOrOne<Long> at (
                                     long unconverted_index
                                     )
        throws ReturnNeverNull.Violation
    {
        final long length = this.length ();
        final long index = CountableView.index ( unconverted_index, length );

        final ValueBuilder<Long> builder =
            new ValueBuilder<Long> ( Long.class );
        final ZeroOrOne<Long> maybe_index;
        if ( index < 0L )
        {
            // Index out of range.
            // !!! create a special violation.
        }
        else
        {
            // Valid index.
            builder.add ( index );
        }

        maybe_index = builder.buildZeroOrOne ();

        return maybe_index;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#elements()
     */
    @Override
    public final CountableView<VALUE, VALUE> elements ()
        throws ReturnNeverNull.Violation
    {
        final CountableView<VALUE, VALUE> view =
            new CountableView<VALUE, VALUE> ( this.elements );
        return view;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#has(long)
     */
    public final boolean has (
                              long unconverted_index
                              )
        throws ReturnNeverNull.Violation
    {
        final long length = this.length ();
        final long index = CountableView.index ( unconverted_index, length );

        if ( index < 0L )
        {
            // Index out of range.
            return false;
        }
        else
        {
            // Valid index.
            return true;
        }
    }


    /**
     * @see java.lang.CountableSelection#iterator()
     */
    @Override
    public final Iterator<Long> iterator ()
    {
        final Iterator<Long> iterator =
            new CountableIndices.IndexIterator ( this.elements.iterator () );
        return iterator;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final long length = this.elements.length ();
        return length;
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#name()
     */
    @Override
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return "indices";
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#parameterStrings()
     */
    @Override
    public final String [] parameterStrings ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new String [] { "" + this.elements };
    }


    /**
     * @see musaico.foundation.value.CountableSelection#range(long, long)
     */
    @Override
    public final IdempotentAndCountable<Long> range (
                                                     long unconverted_start_index,
                                                     long unconverted_end_index
                                                     )
        throws ReturnNeverNull.Violation
    {
        final long length = this.elements.length ();

        final AbstractCountableSelection.RangeIndices range_indices =
            new AbstractCountableSelection.RangeIndices ( unconverted_start_index,
                                                          unconverted_end_index,
                                                          length );

        final ValueBuilder<Long> builder =
            new ValueBuilder<Long> ( Long.class );
        final IdempotentAndCountable<Long> range;
        if ( range_indices.isError )
        {
            // Indices are out of bounds.
            // !!! create a violation.
            range = builder.build ();
            return range;
        }

        final int range_length = (int) range_indices.rangeListLength;
        final List<Long> range_list = new ArrayList<Long> ( range_length );

        for ( long index = range_indices.startIndex;
              index <= range_indices.endListIndex;
              index ++ )
        {
            range_list.add ( index );
        }

        builder.addAll ( range_list );

        if ( range_indices.isClamped
             || ! range_indices.isFitsInList )
        {
            // The range had to be clamped or squeezed down to fit in a list.
            range = builder.buildPartial ();
        }
        else
        {
            // Return all indices in the specified range.
            range = builder.build ();
        }

        return range;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#toList()
     */
    @Override
    public final List<Long> toList ()
        throws ReturnNeverNull.Violation
    {
        final long length = this.elements.length ();
        final int list_length;
        if ( length <= (long) Integer.MAX_VALUE )
        {
            list_length = (int) length;
        }
        else
        {
            list_length = Integer.MAX_VALUE;
        }

        final List<Long> indices = new ArrayList<Long> ( list_length );
        for ( long index = 0L; index < (long) list_length; index ++ )
        {
            indices.add ( index );
        }

        return indices;
    }
}
