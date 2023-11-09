package musaico.foundation.order;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * An arbitrarily ordered sequence of objects.
 * </p>
 *
 * <p>
 * For example, to create an ordered sequence of the Strings
 * "one", "two", "three" and "four", as well as a "no such
 * element" value of "NONE":
 * </p>
 *
 * <pre>
 *     Sequence<String> numbers_sequence =
 *         new Sequence<String> ( "NONE", "one", "two", "three", "four" );
 * </p>
 *
 * <p>
 * Then the Sequence may be inspected directly:
 * </p>
 *
 * <pre>
 *     String three = numbers_sequence.after ( "two" );
 *     String one   = numbers_sequence.before ( "two" );
 * </pre>
 *
 * <p>
 * The Sequence may also be used to sort other data structures:
 * </p>
 *
 * <pre>
 *     List<String> numbers_list = new ArrayList<String> ();
 *     numbers_list.add ( "four" );
 *     numbers_list.add ( "two" );
 *     numbers_list.add ( "three" );
 *     numbers_list.add ( "one" );
 *
 *     Collections.sort ( numbers_list, numbers_sequence );
 * </pre>
 *
 * <p>
 * The first parameter to the constructor ("NONE" in the example
 * above) is always a "no-such-element" element.  The following
 * calls, for example, will return the no-such-element "NONE":
 * </p>
 *
 * <pre>
 *     numbers_sequence.before ( "one" );
 *     numbers_sequence.after ( "four" );
 * </pre>
 *
 *
 * <p>
 * In Java a Sequence must be Serializable in order to play
 * nicely over RMI.  However only a Sequence with Serializable
 * elements will actually play nicely over RMI.  The caller can
 * choose to make a Sequence of non-Serializable objects if
 * she chooses.
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public class Sequence<ELEMENT extends Serializable>
    extends AbstractOrder<ELEMENT>
    implements Iterable<ELEMENT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks constructor and static method parameters etc. */
    private static final Advocate classContracts =
        new Advocate ( Sequence.class );


    /** The ordered array of elements. */
    private final ELEMENT [] elements;

    /** Lookup of index by element. */
    private final Map<ELEMENT,Integer> indices =
        new HashMap<ELEMENT,Integer> ();

    /** The no-such-element, returned by before ( first_element )
     *  and after ( last_element ) and so on. */
    private final ELEMENT noSuchElement;


    /**
     * <p>
     * Creates a new sequence from the specified ordered elements.
     * </p>
     *
     * @param no_such_element The no-such-element, returned by
     *                        before ( first_element ),
     *                        after ( last_element ) and so on.
     *                        Must not be null.
     *
     * @param elements The ordered elements.  Must not be null.
     *                 Must not contain any nulls.  Can be
     *                 zero-length.  Must not contain any
     *                 duplicates.  Must not contain the
     *                 no_such_element.
     */
    @SuppressWarnings("unchecked") // Potential heap pollution generic varargs
    public Sequence (
                     ELEMENT no_such_element,
                     ELEMENT... elements
                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoDuplicates.Violation,
               Parameter2.MustExcludeElements.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               no_such_element, elements );
        classContracts.check ( Parameter2.MustContainNoDuplicates.CONTRACT,
                               elements );
        final Parameter2.MustExcludeElements elements_must_exclude_no_such_element =
            new Parameter2.MustExcludeElements ( no_such_element );
        classContracts.check ( elements_must_exclude_no_such_element,
                               elements );

        this.noSuchElement = no_such_element;
        this.elements = (ELEMENT [])
            Array.newInstance ( elements.getClass ().getComponentType (),
                                elements.length );
        System.arraycopy ( elements, 0, this.elements, 0, elements.length );
    }


    /**
     * <p>
     * Returns the next ordered element after the specified elemeent.
     * </p>
     *
     * @param element The element whose next neighbour will be
     *                returned.  Must not be null.
     *
     * @return The next element, or the no-such-element if there
     *         is no next element.  Never null.
     */
    public ELEMENT after (
                          ELEMENT element
                          )
    {
        if ( this.noSuchElement.equals ( element ) )
        {
            return this.noSuchElement;
        }

        int index = this.indices.get ( element );
        if ( index < 0
             || index >= ( this.elements.length - 1 ) )
        {
            return this.noSuchElement;
        }

        return this.elements [ index + 1 ];
    }


    /**
     * <p>
     * Returns the previous ordered element before the specified elemeent.
     * </p>
     *
     * @param element The element whose previous neighbour will be
     *                returned.  Must not be null.
     *
     * @return The previous element, or the no-such-element if there
     *         is no previous element.  Never null.
     */
    public ELEMENT before (
                           ELEMENT element
                           )
    {
        if ( this.noSuchElement.equals ( element ) )
        {
            return this.noSuchElement;
        }

        int index = this.indices.get ( element );
        if ( index <= 0
             || index >= this.elements.length )
        {
            return this.noSuchElement;
        }

        return this.elements [ index - 1 ];
    }


    /**
     * @see musaico.foundation.io.Order#compareValues(java.lang.Object,java.lang.Object)
     */
    @Override
    public Comparison compareValues (
                                     ELEMENT left,
                                     ELEMENT right
                                     )
    {
        int left_index = this.indices.get ( left );
        int right_index = this.indices.get ( right );

        if ( left_index < 0 )
        {
            if ( right_index < 0 )
            {
                return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
            }
            else
            {
                return Comparison.INCOMPARABLE_LEFT;
            }
        }
        else if ( right_index < 0 )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }
        else if ( left_index < right_index )
        {
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }
        else if ( left_index > right_index )
        {
            return Comparison.LEFT_GREATER_THAN_RIGHT;
        }
        else
        {
            return Comparison.LEFT_EQUALS_RIGHT;
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
                  || ! ( obj instanceof Sequence ) )
        {
            return false;
        }

        Sequence<?> that = (Sequence<?>) obj;
        if ( ! this.noSuchElement ().equals ( that.noSuchElement () ) )
        {
            return false;
        }

        if ( ! this.first ().equals ( that.first () )
             || ! this.last ().equals ( that.last () ) )
        {
            return false;
        }

        int e = 0;
        ELEMENT this_element = this.first ();
        for ( Object that_element : that )
        {
            if ( ! this_element.equals ( that_element ) )
            {
                return false;
            }

            this_element = this.after ( this_element );
            e ++;
        }

        if ( e != this.elements.length )
        {
            return false;
        }

        return true;
    }


    /**
     * <p>
     * Returns the first element of this Sequence.
     * </p>
     *
     * @return The first element of this sequence.
     *         Never null.  Never the no-such-element.
     */
    public ELEMENT first ()
    {
        return this.elements [ 0 ];
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.noSuchElement ().hashCode () + this.elements.length;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ELEMENT> iterator ()
    {
        return new SequenceIterator<ELEMENT> ( this );
    }


    /**
     * <p>
     * Returns the last element of this Sequence.
     * </p>
     *
     * @return The last element of this sequence.
     *         Never null.  Never the no-such-element.
     */
    public ELEMENT last ()
    {
        return this.elements [ this.elements.length - 1 ];
    }


    /**
     * <p>
     * Returns the no-such-element.
     * </p>
     *
     * @return The no-such-element.  Never null.
     */
    public ELEMENT noSuchElement ()
    {
        return this.noSuchElement;
    }


    /**
     * <p>
     * Returns a new sequence, sorted by the specified Order.
     * </p>
     *
     * @param order The order by which to sort this sequence.
     *              Must not be null.
     *
     * @return The newly created, sorted sequence.  Never null.
     */
    @SuppressWarnings("unchecked") // Array.newInstance() cast
    public Sequence<ELEMENT> sort (
                                   Order<ELEMENT> order
                                   )
    {
        ELEMENT[] sorted_elements = (ELEMENT [])
            Array.newInstance ( this.elements.getClass ().getComponentType (),
                                this.elements.length );
        System.arraycopy ( this.elements, 0,
                           sorted_elements, 0, sorted_elements.length );

        Arrays.sort ( sorted_elements, order );

        Sequence<ELEMENT> sorted_sequence =
            new Sequence<ELEMENT> ( this.noSuchElement,
                                    sorted_elements );

        return sorted_sequence;
    }
}
