package musaico.foundation.order;

import java.io.Serializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The result of some comparison between objects.
 * </p>
 *
 * <p>
 * Comparison is a specialization of the FilterState result, where
 * <code> Comparison.LEFT_EQUALS_RIGHT.isKept () = true </code> 
 * and all other Comparisons return <code> isKept () = false </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every Comparison must be Serializable in order to play
 * nicely over RMI.
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
public class Comparison
    extends FilterState
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method parameters for us.
    private static final Advocate classContracts =
        new Advocate ( Comparison.class );


    /** The left operand is less than the right operand
     *  (this &lt; that). */
    public static final Comparison LEFT_LESS_THAN_RIGHT =
        new Comparison ( "LEFT_LESS_THAN_RIGHT", -1 );

    /** The left and right operands are equal
     *  (this == that). */
    public static final Comparison LEFT_EQUALS_RIGHT =
        new Comparison ( "LEFT_EQUALS_RIGHT", 0 );

    /** The left operand is greater than the right operand
     *  (this &gt; that). */
    public static final Comparison LEFT_GREATER_THAN_RIGHT =
        new Comparison ( "LEFT_GREATER_THAN_RIGHT", 1 );

    /** The left and right operands are not even comparable
     *  (this != that).  As far as sorting goes, the left
     *  operand (this) is considered incomparable, so it will
     *  be sorted to the end, AFTER right. */
    public static final Comparison INCOMPARABLE_LEFT =
        new Comparison ( "INCOMPARABLE_LEFT", Integer.MAX_VALUE );

    /** The left and right operands are not even comparable
     *  (this != that).  As far as sorting goes, the right
     *  operand (that) is considered incomparable, so it will
     *  be sorted to the end, AFTER left. */
    public static final Comparison INCOMPARABLE_RIGHT =
        new Comparison ( "INCOMPARABLE_RIGHT", Integer.MIN_VALUE + 1 );

    /** The left and right operands are not even comparable
     *  (this != that).  As far as sorting goes, both
     *  operands are considered incomparable, so they are
     *  arbitrarily treated as left coming befor right. */
    public static final Comparison INCOMPARABLE_LEFT_AND_RIGHT =
        new Comparison ( "INCOMPARABLE_LEFT_AND_RIGHT", Integer.MIN_VALUE );


    // Matches "LEFT_LESS_THAN_RIGHT[-1234]" and so on.
    private static final Pattern REGULAR_EXPRESSION_LEFT_LESS_THAN_RIGHT =
        Pattern.compile ( "^LEFT_LESS_THAN_RIGHT\\[(\\-[0-9]+)\\]$" );

    // Matches "LEFT_GREATER_THAN_RIGHT[1234]" and so on.
    private static final Pattern REGULAR_EXPRESSION_LEFT_GREATER_THAN_RIGHT =
        Pattern.compile ( "^LEFT_GREATER_THAN_RIGHT\\[([0-9]+)\\]$" );


    // Checks method parameter obligations and return value guarantees.
    private final Advocate contracts;

    // The java Comparator / Comparable equivalent value.
    private final int difference;


    /**
     * <p>
     * Creates a new Comparison with the specified difference
     * (used by Java comparators).
     * </p>
     *
     * <p>
     * Note that you can (and possibly should) use the enumerated
     * values in Comparison instead:
     * </p>
     *
     * <ul>
     *   <li> <code> Comparison.LEFT_LESS_THAN_RIGHT </code> </li>
     *   <li> <code> Comparison.LEFT_EQUALS_RIGHT </code> </li>
     *   <li> <code> Comparison.LEFT_GREATER_THAN_RIGHT </code> </li>
     *   <li> <code> Comparison.INCOMPARABLE_LEFT </code> </li>
     *   <li> <code> Comparison.INCOMPARABLE_RIGHT </code> </li>
     *   <li> <code> Comparison.INCOMPARABLE_LEFT_AND_RIGHT </code> </li>
     * <ul>
     *
     * @param difference The value to convert this to / from
     *                   for a Java Comparetor.compare () equivalent.
     *                   Can be any number.
     */
    public Comparison (
                       int difference
                       )
    {
        this ( Comparison.customComparisonName ( difference ),
               difference );
    }


    /**
     * <p>
     * Creates a new Comparison with the specified difference
     * (used by Java comparators).
     * </p>
     *
     * <p>
     * Note that you can (and possibly should) use the enumerated
     * values in Comparison instead:
     * </p>
     *
     * <ul>
     *   <li> <code> Comparison.LEFT_LESS_THAN_RIGHT </code> </li>
     *   <li> <code> Comparison.LEFT_EQUALS_RIGHT </code> </li>
     *   <li> <code> Comparison.LEFT_GREATER_THAN_RIGHT </code> </li>
     *   <li> <code> Comparison.INCOMPARABLE_LEFT </code> </li>
     *   <li> <code> Comparison.INCOMPARABLE_RIGHT </code> </li>
     *   <li> <code> Comparison.INCOMPARABLE_LEFT_AND_RIGHT </code> </li>
     * <ul>
     *
     * @param name The name of the Comparison to create, such
     *             as "LEFT_LESS_THAN_RIGHT" or "INCOMPARABLE_RIGHT"
     *             or "LEFT_GREATER_THAN_RIGHT[1234]" and so on.
     *             Must not be null.
     *
     * @param difference The value to convert this to / from
     *                   for a Java Comparetor.compare () equivalent.
     *                   Can be any number.
     */
    @SuppressWarnings("unchecked") // Cast the output of contracts.check().
    public Comparison (
                       String name,
                       int difference
                       )
        throws ParametersMustNotBeNull.Violation
    {
        super ( (String)
                classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                       name ) [ 0 ],
                difference == 0 // is_kept
                    ? true
                    : false );

        this.difference = difference;

        this.contracts = new Advocate ( this );
    }


    /**
     * @return A custom name for the specified difference,
     *         such as "LEFT_LESS_THAN_RIGHT[-1234]",
     *         or "LEFT_GREATER_THAN_RIGHT[1234]",
     *         or, if the specified difference is 0, then always
     *         "LEFT_EQUALS_RIGHT".  Never null.
     */
    private static final String customComparisonName (
                                                      int difference
                                                      )
    {
        if ( difference < 0 )
        {
            return "LEFT_LESS_THAN_RIGHT["+ difference + "]";
        }
        else if ( difference > 0 )
        {
            return "LEFT_GREATER_THAN_RIGHT["+ difference + "]";
        }
        else // difference == 0
        {
            return "LEFT_EQUALS_RIGHT";
        }
    }


    /**
     * @return The Comparable.compareTo () or Comparator.compare ()
     *         equivalent of this Comparison, or a kludge if nothing
     *         is quite equivalent.
     */
    public int difference ()
    {
        return this.difference;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( ! ( object instanceof FilterState ) )
        {
            return false;
        }

        final FilterState that_state = (FilterState) object;
        if ( this.difference == 0
             && that_state.isKept () )
        {
            return true;
        }
        else if ( ! ( that_state instanceof Comparison ) )
        {
            return false;
        }

        final Comparison that = (Comparison) that_state;
        if ( this.difference != that.difference )
        {
            return false;
        }

        // We don't care if the names are different.  If the
        // difference value is the same, the Comparisons are the same.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.difference;
    }


    /**
     * <p>
     * Returns true if this Comparison is any of the ones specified.
     * </p>
     *
     * @param comparisons The values to match this Comparison against.
     *                    Must not be null.  Must not contain any null
     *                    elements.
     */
    public final boolean isOneOf (
                                  Comparison ... comparisons
                                  )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object []) comparisons );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               comparisons );

        for ( Comparison that : comparisons )
        {
            if ( this.equals ( that ) )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * @return True if the left term is equal to the right term,
     *         false if not.
     */
    public final boolean isEqual ()
    {
        if ( this.difference == 0 )
        {
            return true;
        }

        return false;
    }


    /**
     * @return True if the left term is greater than or equal to
     *         the right term, false if not.
     */
    public final boolean isLeftGreaterThanOrEqualToRight ()
    {
        if ( this.isIncomparable () )
        {
            return false;
        }
        else if ( this.difference >= 0 )
        {
            return true;
        }

        return false;
    }


    /**
     * @return True if the left term is greater than the right term,
     *         false if not.
     */
    public final boolean isLeftGreaterThanRight ()
    {
        if ( this.isIncomparable () )
        {
            return false;
        }
        else if ( this.difference > 0 )
        {
            return true;
        }

        return false;
    }


    /**
     * @return True if the left term is less than the right term,
     *         false if not.
     */
    public final boolean isLeftLessThanRight ()
    {
        if ( this.isIncomparable () )
        {
            return false;
        }
        else if ( this.difference < 0 )
        {
            return true;
        }

        return false;
    }


    /**
     * @return True if the left term is less than or equal to
     *         the right term, false if not.
     */
    public final boolean isLeftLessThanOrEqualToRight ()
    {
        if ( this.isIncomparable () )
        {
            return false;
        }
        else if ( this.difference <= 0 )
        {
            return true;
        }

        return false;
    }


    /**
     * @return True if the terms are incomparable,
     *         false if not.
     */
    public final boolean isIncomparable ()
    {
        if ( this == Comparison.INCOMPARABLE_LEFT
             || this == Comparison.INCOMPARABLE_RIGHT
             || this == Comparison.INCOMPARABLE_LEFT_AND_RIGHT )
        {
            return true;
        }

        return false;
    }




    /**
     * <p>
     * Given the specified result from Comparable.compareTo () or
     * Comparator.compare (), returns the best matching Comparison.
     * </p>
     *
     * @param difference The Java compareTo () or compare ()
     *                   result.  Can be any number.  Note that
     *                   the difference values
     *                   <code> Integer.MIN_VALUE </code>,
     *                   <code> Integer.MIN_VALUE + 1 </code>
     *                   and <code> Integer.MAX_VALUE </code>
     *                   return
     *                   <code> Comparison.INCOMPARABLE_LEFT_AND_RIGHT </code>,
     *                   <code> Comparison.INCOMPARABLE_LEFT </code>
     *                   and <code> Comparison.INCOMPARABLE_RIGHT </code>,
     *                   respectively.
     *
     * @return The corresponding Comparison.  Never null.
     */
    public static final Comparison fromDifference (
                                                   int difference
                                                   )
        throws ReturnNeverNull.Violation
    {
        if ( difference == Comparison.LEFT_LESS_THAN_RIGHT.difference () )
        {
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }
        else if ( difference == Comparison.LEFT_GREATER_THAN_RIGHT.difference () )
        {
            return Comparison.LEFT_GREATER_THAN_RIGHT;
        }
        else if ( difference == Comparison.LEFT_EQUALS_RIGHT.difference () )
        {
            return Comparison.LEFT_EQUALS_RIGHT;
        }
        else if ( difference == Comparison.INCOMPARABLE_LEFT.difference () )
        {
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( difference == Comparison.INCOMPARABLE_RIGHT.difference () )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }
        else if ( difference == Comparison.INCOMPARABLE_LEFT_AND_RIGHT.difference () )
        {
            return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
        }
        else
        {
            return new Comparison ( difference );
        }
    }


    /**
     * <p>
     * Returns the Comparison represented by the specified FilterState.
     * </p>
     *
     * <p>
     * If the FilterState is a Comparison, it is cast and returned.
     * Otherwise, if it <code> isKept () <code>, then
     * <code> Comparison.LEFT_EQUALS_RIGHT </code> is returned.
     * Otherwise, <code> Comparison.INCOMPARABLE_LEFT_AND_RIGHT </code>
     * is returned.
     * </p>
     *
     * @param filter_state The FilterState to convert to a Comparison.
     *                     Must not be null.
     *
     * @return The corresponding Comparison.  Never null.
     */
    public static final Comparison fromFilterState (
                                                    FilterState filter_state
                                                    )
        throws ReturnNeverNull.Violation
    {
        if ( filter_state == null )
        {
            return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
        }
        else if ( filter_state instanceof Comparison )
        {
            return (Comparison) filter_state;
        }
        else if ( filter_state.isKept () )
        {
            return Comparison.LEFT_EQUALS_RIGHT;
        }
        else
        {
            return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
        }
    }


    /**
     * <p>
     * Returns the Comparison represented by the specified String,
     * the reverse of <code> Comparison.toString () </code>.
     * </p>
     *
     * @param string The String representation of the Comparison to
     *               return, such as "LEFT_LESS_THAN_RIGHT"
     *               or "INCOMPARABLE_RIGHT" or "LEFT_GREATER_THAN_RIGHT[1234]"
     *               and so on.  Must not be null.
     *
     * @return The corresponding Comparison.  Never null.
     */
    public static final Comparison fromString (
                                               String string
                                               )
        throws ReturnNeverNull.Violation
    {
        if ( string == null )
        {
            return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
        }
        else if ( Comparison.LEFT_LESS_THAN_RIGHT.name ().equals ( string ) )
        {
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }
        else if ( Comparison.LEFT_GREATER_THAN_RIGHT.name ().equals ( string ) )
        {
            return Comparison.LEFT_GREATER_THAN_RIGHT;
        }
        else if ( Comparison.LEFT_EQUALS_RIGHT.name ().equals ( string ) )
        {
            return Comparison.LEFT_EQUALS_RIGHT;
        }
        else if ( Comparison.INCOMPARABLE_LEFT.name ().equals ( string ) )
        {
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( Comparison.INCOMPARABLE_RIGHT.name ().equals ( string ) )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }
        else if ( Comparison.INCOMPARABLE_LEFT_AND_RIGHT.name ().equals ( string ) )
        {
            return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
        }
        else if ( Comparison.REGULAR_EXPRESSION_LEFT_LESS_THAN_RIGHT.matcher ( string ).matches () )
        {
            try
            {
                final Matcher matcher =
                    Comparison.REGULAR_EXPRESSION_LEFT_LESS_THAN_RIGHT.matcher ( string );
                matcher.matches ();
                final String difference_string = matcher.group ( 1 );
                final int difference = Integer.parseInt ( difference_string );
                return new Comparison ( difference );
            }
            catch ( Exception e )
            {
                // Shouldn't happen, but when it inevitably does,
                // we fall back on "garbage in, garbage out".
                return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
            }
        }
        else if ( Comparison.REGULAR_EXPRESSION_LEFT_GREATER_THAN_RIGHT.matcher ( string ).matches () )
        {
            try
            {
                final Matcher matcher =
                    Comparison.REGULAR_EXPRESSION_LEFT_GREATER_THAN_RIGHT.matcher ( string );
                matcher.matches ();
                final String difference_string = matcher.group ( 1 );
                final int difference = Integer.parseInt ( difference_string );
                return new Comparison ( difference );
            }
            catch ( Exception e )
            {
                // Shouldn't happen, but when it inevitably does,
                // we fall back on "garbage in, garbage out".
                return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
            }
        }
        else // Garbage string.
        {
            return Comparison.INCOMPARABLE_LEFT_AND_RIGHT;
        }
    }
}
