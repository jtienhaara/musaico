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
 * A selection of all the elements from a source Countable value.
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
public class CountableSelectionSource<VALUE extends Object>
    extends AbstractCountableSelection<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CountableSelectionSource.class );


    /**
     * <p>
     * Creates a new CountableSelectionSource, selecting all of the
     * elements from the specified Countable value.
     * </p>
     *
     * @param source The Countable value which has been selected.
     *               Must not be null.
     */
    public CountableSelectionSource (
                                     Countable<VALUE> source
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( source,  // Throws ParametersMustNotBeNull.Violation.
                CountableView.Items.ELEMENTS );
    }


    /**
     * <p>
     * Creates a new CountableSelectionSource, selecting all of the
     * elements from the specified Countable value.
     * </p>
     *
     * @param source The IdempotentAndCountable value which has been selected.
     *               Must not be null.
     */
    public CountableSelectionSource (
                                     IdempotentAndCountable<VALUE> source
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( source,  // Throws ParametersMustNotBeNull.Violation.
                CountableView.Items.ELEMENTS );
    }


    /**
     * @see musaico.foundation.value.CountableSelection#all()
     */
    @Override
    public final IdempotentAndCountable<VALUE> all ()
        throws ReturnNeverNull.Violation
    {
        return this.source ();
    }


    /**
     * @see musaico.foundation.value.CountableSelection#at(long)
     */
    @Override
    public final ZeroOrOne<VALUE> at (
                                      long unconverted_index
                                      )
        throws ReturnNeverNull.Violation
    {
        final IdempotentAndCountable<VALUE> source = this.source ();
        final long length = source.length ();
        final long index = CountableView.index ( unconverted_index, length );

        if ( index < 0L )
        {
            // Index out of range.
            if ( source instanceof Empty )
            {
                // The index is out of range, and we've already got
                // zero or one.
                // Return it as-is.
                final Empty<VALUE> empty = (Empty<VALUE>) source;
                return empty;
            }

            // !!! create a special violation.
            final Class<VALUE> item_class = this.itemClass ();
            final ValueBuilder<VALUE> builder =
                new ValueBuilder<VALUE> ( item_class );
            return builder.buildZeroOrOne ();
        }

        if ( source instanceof ZeroOrOne )
        {
            // The index is within range, and we've already got zero or one.
            // Return it as-is.
            final ZeroOrOne<VALUE> zero_or_one = (ZeroOrOne<VALUE>) source;
            return zero_or_one;
        }

        final Class<VALUE> item_class = this.itemClass ();
        final ValueBuilder<VALUE> builder =
            new ValueBuilder<VALUE> ( item_class );

        if ( source instanceof AbstractMultiple )
        {
            final AbstractMultiple<VALUE> has_list = (AbstractMultiple<VALUE>)
                source;
            final List<VALUE> elements = has_list.internalElements ();

            final int list_index = (int) index;
            if ( list_index >= 0
                 && list_index < elements.size () )
            {
                final VALUE element = elements.get ( list_index );
                builder.add ( element );
            }
        }
        else
        {
            long current_index = 0;
            for ( VALUE element : source )
            {
                if ( current_index == index )
                {
                    // Found it.
                    builder.add ( element );
                    break;
                }

                current_index ++;
            }
        }

        final ZeroOrOne<VALUE> element_at_index = builder.buildZeroOrOne ();

        return element_at_index;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#has(long)
     */
    public final boolean has (
                              long unconverted_index
                              )
        throws ReturnNeverNull.Violation
    {
        final IdempotentAndCountable<VALUE> source = this.source ();
        final long length = source.length ();
        final long index = CountableView.index ( unconverted_index, length );

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
    public final Iterator<VALUE> iterator ()
    {
        final IdempotentAndCountable<VALUE> source = this.source ();
        final Iterator<VALUE> iterator = source.iterator ();
        return iterator;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final IdempotentAndCountable<VALUE> source = this.source ();
        final long length = source.length ();
        return length;
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#name()
     */
    @Override
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return "select";
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#parameterStrings()
     */
    @Override
    public final String [] parameterStrings ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new String [ 0 ];
    }


    /**
     * @see musaico.foundation.value.CountableSelection#range(long, long)
     */
    @Override
    public final IdempotentAndCountable<VALUE> range (
                                                      long unconverted_start_index,
                                                      long unconverted_end_index
                                                      )
        throws ReturnNeverNull.Violation
    {
        final IdempotentAndCountable<VALUE> source =
            this.source ();
        final long length = source.length ();

        final AbstractCountableSelection.RangeIndices indices =
            new AbstractCountableSelection.RangeIndices ( unconverted_start_index,
                                                          unconverted_end_index,
                                                          length );

        if ( indices.startIndex == 0L
             && indices.endIndex == ( length - 1L ) )
        {
            // The whole range.  Return the whole source countable value.
            return source;
        }

        final Class<VALUE> item_class = this.itemClass ();
        final ValueBuilder<VALUE> builder =
            new ValueBuilder<VALUE> ( item_class );
        final IdempotentAndCountable<VALUE> range;
        if ( indices.isError )
        {
            // Indices are out of bounds.
            // !!! create a violation.
            range = builder.build ();
            return range;
        }

        final List<VALUE> elements;
        if ( source instanceof AbstractMultiple )
        {
            final AbstractMultiple<VALUE> has_list = (AbstractMultiple<VALUE>)
                source;
            final List<VALUE> all_elements =
                new ArrayList<VALUE> ( has_list.internalElements () );
            elements = all_elements.subList ( (int) indices.startIndex,
                                              (int) indices.endIndex );
        }
        else
        {
            final int range_length = (int) indices.rangeListLength;
            elements = new ArrayList<VALUE> ( range_length );

            long index = 0L;
            for ( VALUE element : source )
            {
                if ( index >= indices.startIndex
                     && index <= indices.endListIndex )
                {
                    elements.add ( element );
                }
                else if ( index > indices.endListIndex )
                {
                    break;
                }

                index ++;
            }
        }

        builder.addAll ( elements );

        if ( indices.isClamped
             || ! indices.isFitsInList )
        {
            // The range had to be clamped or squeezed down to fit in a list.
            range = builder.buildPartial ();
        }
        else
        {
            // Return all elements in the specified range.
            range = builder.build ();
        }

        return range;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#toList()
     */
    @Override
    public final List<VALUE> toList ()
        throws ReturnNeverNull.Violation
    {
        final IdempotentAndCountable<VALUE> source = this.source ();

        final long length = source.length ();

        final List<VALUE> elements;
        if ( source instanceof AbstractMultiple )
        {
            final AbstractMultiple<VALUE> has_list = (AbstractMultiple<VALUE>)
                source;
            elements = new ArrayList<VALUE> ( has_list.internalElements () );
        }
        else
        {
            if ( length <= Integer.MAX_VALUE )
            {
                elements = new ArrayList<VALUE> ( (int) length );
            }
            else
            {
                elements = new ArrayList<VALUE> ();
            }

            long e = 0L;
            for ( VALUE element : source )
            {
                if ( e > (long) Integer.MAX_VALUE )
                {
                    break;
                }

                elements.add ( element );

                e ++;
            }
        }

        return elements;
    }
}
