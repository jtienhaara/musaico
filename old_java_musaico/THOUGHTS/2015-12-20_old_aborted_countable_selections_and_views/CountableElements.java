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
 * A selection of elements picked from the elements underlying a selection
 * of indices.
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
public class CountableElements<VALUE extends Object>
    extends AbstractCountableSelection<VALUE, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CountableElements.class );


    /** Iterates through the elements of a selection of indices. */
    public static class ElementIterator<ELEMENT extends Object>
        implements Iterator<ELEMENT>, Serializable
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            CountableElements.serialVersionUID;

        // Enforces constructor and static method parameter obligations for us.
        private static final ObjectContracts classContracts =
            new ObjectContracts ( CountableElements.ElementIterator.class );

        // Enforces method parameter obligations and guarantees for us.
        private final ObjectContracts contracts;

        // Lock critical sections on this token:
        private final Serializable lock = new String ();

        // The elements to iterate over.
        private final CountableSelection<ELEMENT, ELEMENT> elements;

        // The indices of the elements to iterate over.
        private final Iterator<Long> indicesIterator;


        /**
         * <p>
         * Creates a new CountableIndices.ElementIterator
         * over the specified indices of the specified elements.
         * </p>
         *
         * @param elements The selection of elements to iterate over.
         *                 Must not be null.
         *
         * @param indices_iterator Iterates over the indices while
         *                         this class returns the element at
         *                         each index.  Must not be null.
         *                         Must never iterate over a null index.
         */
        public ElementIterator (
                                CountableSelection<ELEMENT, ELEMENT> elements,
                                Iterator<Long> indices_iterator
                                )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   elements, indices_iterator );

            this.elements = elements;
            this.indicesIterator = indices_iterator;

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
                return this.indicesIterator.hasNext ();
            }
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public final ELEMENT next ()
            throws ReturnNeverNull.Violation
        {
            final long index;
            synchronized ( this.lock )
            {
                if ( ! this.indicesIterator.hasNext () )
                {
                    index = -1L;
                }
                else
                {
                    index = this.indicesIterator.next ();
                }
            }

            final ELEMENT element;
            if ( index < 0L )
            {
                element = null;
            }
            else
            {
                final ZeroOrOne<ELEMENT> maybe_element =
                    this.elements.at ( index );
                element = maybe_element.orNull ();
            }

            this.contracts.check ( ReturnNeverNull.CONTRACT,
                                   element );

            return element;
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


    // The selection of indices underlying this selection of elements.
    private final CountableSelection<VALUE, Long> indices;

    // The selection of elements referenced by the selection of indices
    // underlying this selection of elements.
    private final CountableSelection<VALUE, VALUE> elements;


    /**
     * <p>
     * Creates a new CountableElements, selecting the elements addressed
     * by the specified selection of indices.
     * </p>
     *
     * @param indices The indices referencing the elements in this selection
     *                of elements.  Must not be null.
     */
    public CountableElements (
                              CountableSelection<VALUE, Long> indices
                              )
        throws Parameter1.MustNotBeNull.Violation
    {
        super ( classContracts.check ( Parameter1.MustNotBeNull.CONTRACT,
                                       indices )
                              .source (),
                CountableView.Items.ELEMENTS );

        this.indices = indices;
        this.elements = indices.elements ().select ();
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
        final long indices_length = this.indices.length ();
        final long index_of_index =
            CountableView.index ( unconverted_index, indices_length );

        final Class<VALUE> item_class = this.itemClass ();
        final ValueBuilder<VALUE> builder =
            new ValueBuilder<VALUE> ( item_class );
        if ( index_of_index < 0L )
        {
            // Index out of range of the indices.
            // !!! create a special violation.
            return builder.buildZeroOrOne ();
        }

        final Long index =
            this.indices.at ( index_of_index )
                        .orNull ();
        if ( index == null )
        {
            // No such index at that index_of_index.
            // !!! create a special violation.
            return builder.buildZeroOrOne ();
        }

        final ZeroOrOne<VALUE> maybe_element =
            this.elements.at ( index.longValue () );

        return maybe_element;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#has(long)
     */
    public final boolean has (
                              long unconverted_index
                              )
        throws ReturnNeverNull.Violation
    {
        final long indices_length = this.indices.length ();
        final long index_of_index =
            CountableView.index ( unconverted_index, indices_length );

        if ( index_of_index < 0L )
        {
            return false;
        }

        final Long index =
            this.indices.at ( index_of_index )
                        .orNull ();
        if ( index == null )
        {
            // No such index at that index_of_index.
            return false;
        }

        final boolean has_index = this.elements.has ( index.longValue () );
    }


    /**
     * @see musaico.foundation.value.CountableSelection#indices()
     */
    @Override
    public final CountableView<VALUE, Long> indices ()
        throws ReturnNeverNull.Violation
    {
        final CountableView<VALUE, Long> view =
            new CountableView<VALUE, Long> ( this.indices );
        return view;
    }


    /**
     * @see java.lang.CountableSelection#iterator()
     */
    @Override
    public final Iterator<VALUE> iterator ()
    {
        final Iterator<Long> index_iterator = this.indices.iterator ();
        final Iterator<VALUE> iterator =
            new CountableElements.ElementIterator<VALUE> ( this.elements,
                                                           index_iterator );
        return iterator;
    }


    /**
     * @see musaico.foundation.value.CountableSelection#length()
     */
    @Override
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        return this.indices.length ();
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#name()
     */
    @Override
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return "elements";
    }


    /**
     * @see musaico.foundation.value.AbstractCountableSelection#parameterStrings()
     */
    @Override
    public final String [] parameterStrings ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new String [] { "" + this.indices };
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
        final IdempotentAndCountable<Long> range_indices =
            this.indices.range ( unconverted_start_index,
                                 unconverted_end_index );

        final Class<VALUE> item_class = this.itemClass ();
        final ValueBuilder<VALUE> builder;
        final IdempotentAndCountable<VALUE> range;
        if ( range_indices instanceof Unjust )
        {
            final Unjust<Long> unjust_indices = (Unjust<Long>) range_indices;
            final ValueViolation violation = unjust_indices.valueViolation ();
            builder = new ValueBuilder<VALUE> ( item_class, violation );
            range = builder.build ();
            return range;
        }

        long e = 0L;
        ValueViolation missing_element_violation = null;
        boolean is_too_big_for_list = false;
        final List<VALUE> elements_list = new ArrayList<VALUE> ();
        for ( long index : range_indices )
        {
            final ZeroOrOne<VALUE> maybe_element =
                this.elements.at ( index );

            final VALUE element = maybe_element.orNull ();

            if ( element == null )
            {
                if ( missing_element_violation == null )
                {
                    if ( maybe_element instanceof NotOne )
                    {
                        final NotOne<VALUE> no_such_element = (NotOne<VALUE>)
                            maybe_element;
                        missing_element_violation =
                            no_such_element.valueViolation ();
                    }
                    else
                    {
                        // !!! create invalid index violation.
                        missing_element_violation =
                            ValueMustBeOne.CONTRACT.violation ( this,
                                                                maybe_element );
                    }
                }
            }
            else
            {
                elements_list.add ( element );
            }

            e ++;
            if ( e >= Integer.MAX_VALUE )
            {
                is_too_big_for_list = true;
                break;
            }
        }

        if ( missing_element_violation != null )
        {
            builder = new ValueBuilder<VALUE> ( item_class,
                                                missing_element_violation );
        }
        else
        {
            builder = new ValueBuilder<VALUE> ( item_class );
        }

        builder.addAll ( elements_list );

        if ( elements_list.size () == 0 )
        {
            range = builder.build ();
        }
        else if ( missing_element_violation != null
             || is_too_big_for_list )
        {
            range = builder.buildPartial ();
        }
        else
        {
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
        final List<VALUE> elements_list = new ArrayList<VALUE> ();
        long e = 0L;
        for ( long index : this.indices )
        {
            final ZeroOrOne<VALUE> maybe_element =
                this.elements.at ( index );

            final VALUE element = maybe_element.orNull ();

            if ( element != null )
            {
                elements_list.add ( element );
            }

            e ++;
            if ( e >= Integer.MAX_VALUE )
            {
                break;
            }
        }

        return elements_list;
    }
}
