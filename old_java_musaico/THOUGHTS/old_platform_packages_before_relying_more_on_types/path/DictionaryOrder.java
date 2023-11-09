package musaico.foundation.io;


import java.io.Serializable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * Compares Strings by dictionary value (considering only whitespace
 * and alphanumerics, ignoring case, and parsing embedded numbers to
 * compare numerically).
 * </p>
 *
 *
 * <p>
 * In Java, every Order is Serializable in order to play nicely
 * over RMI.
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
 * <pre>
 * Copyright (c) 2011 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class DictionaryOrder
    extends AbstractOrder<String>
    implements Serializable
{
    /** Strips everything but whitespace and alphanumerics out of
     *  the strings. */
    private static final Pattern STRIP_NON_DICTIONARY_CHARACTERS =
        Pattern.compile ( "[^\\p{Alnum}\\p{Space}]" );

    /** Replaces all space and tab and so on characters with one space. */
    private static final Pattern REPLACE_SPACE_CHARACTERS =
        Pattern.compile ( "[\\p{Space}]+" );

    /** Splits a String into groups of alpha/space vs. numeric characters,
     *  so that we can convert the numerics to numbers and compare them
     *  with the NumericOrder. */
    private static final Pattern SPLIT_NUMERIC_CHARACTERS =
        Pattern.compile ( "([\\p{Digit}]+)" );


    /** Each number component of each String is compared as a number
     *  rather than as text. */
    private final Order<Number> numericOrder =
	new NumericOrder ();


    /**
     * <p>
     * Creates a new DictionaryOrder.
     * </p>
     */
    public DictionaryOrder ()
    {
        super ( "Dictionary order" );
    }


    /**
     * @see musaico.foundation.io.Order#compareValues(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           String left,
                                           String right
                                           )
    {
        if ( left == null )
        {
            if ( right == null )
            {
                // null == null.
                return Comparison.LEFT_EQUALS_RIGHT;
            }

            // null > any String.
            return Comparison.INCOMPARABLE_LEFT;
        }
        else if ( right == null )
        {
            // any String < null.
            return Comparison.INCOMPARABLE_RIGHT;
        }

        // Convert the strings to dictionary strings.
        final String stripped_left =
            DictionaryOrder.STRIP_NON_DICTIONARY_CHARACTERS
            .matcher ( left )
            .replaceAll ( "" );
        final String simplified_left =
            DictionaryOrder.REPLACE_SPACE_CHARACTERS
            .matcher ( stripped_left )
            .replaceAll ( " " );
        final String stripped_right =
            DictionaryOrder.STRIP_NON_DICTIONARY_CHARACTERS
            .matcher ( right )
            .replaceAll ( "" );
        final String simplified_right =
            DictionaryOrder.REPLACE_SPACE_CHARACTERS
            .matcher ( stripped_right )
            .replaceAll ( " " );

	final Matcher number_matcher_left =
	    DictionaryOrder.SPLIT_NUMERIC_CHARACTERS.matcher ( simplified_left );
	final Matcher number_matcher_right =
	    DictionaryOrder.SPLIT_NUMERIC_CHARACTERS.matcher ( simplified_right );
	String remaining_left = simplified_left;
	String remaining_right = simplified_right;
	int end_token_left = -1;
	int end_token_right = -1;
	while ( number_matcher_left.find ()
		&& number_matcher_right.find () )
	{
	    final String text_before_number_left =
		simplified_left.substring ( end_token_left + 1,
					    number_matcher_left.start () );
	    final String text_before_number_right =
		simplified_right.substring ( end_token_right + 1,
					     number_matcher_right.start () );

	    // First compare the text before the number (if any).
	    // any String < / == / > any String.
	    int comparator_value =
		text_before_number_left.compareToIgnoreCase ( text_before_number_right );
	    if ( comparator_value != 0 )
	    {
		Comparison comparison =
		    Comparison.fromComparatorValue ( comparator_value );
		return comparison;
	    }

	    // The text before the numbers was the same.
	    // So now compare the numbers. 
	    final String string_number_left = number_matcher_left.group ();
	    final String string_number_right = number_matcher_right.group ();
	    try
	    {
		Long number_left =
		    new Long ( Long.parseLong ( string_number_left ) );
		Long number_right =
		    new Long ( Long.parseLong ( string_number_right ) );

		if ( number_left < number_right )
		{
		    return Comparison.LEFT_LESS_THAN_RIGHT;
		}
		else if ( number_left > number_right )
		{
		    return Comparison.LEFT_GREATER_THAN_RIGHT;
		}
		// Else carry on comparing the rest of the strings.
	    }
	    catch ( NumberFormatException e )
	    {
		// Maybe the numbers are too big.  Just do string
		// compare on the numeric text.
		comparator_value =
		    string_number_left.compareToIgnoreCase ( string_number_right );
		if ( comparator_value != 0 )
		{
		    Comparison comparison =
			Comparison.fromComparatorValue ( comparator_value );
		    return comparison;
		}
	    }

	    end_token_left = number_matcher_left.end ();
	    end_token_right = number_matcher_right.end ();

	    if ( end_token_left == ( simplified_left.length () - 1 ) )
	    {
		remaining_left = "";
	    }
	    else
	    {
		remaining_left =
		    simplified_left.substring ( end_token_left + 1 );
	    }

	    if ( end_token_right == ( simplified_right.length () - 1 ) )
	    {
		remaining_right = "";
	    }
	    else
	    {
		remaining_right =
		    simplified_right.substring ( end_token_right + 1 );
	    }
	}

	// One or both of the Strings has no more numbers.
	// Just do a straight text compare of the rest.
	int comparator_value =
	    remaining_left.compareToIgnoreCase ( remaining_right );
	if ( comparator_value != 0 )
	{
	    Comparison comparison =
		Comparison.fromComparatorValue ( comparator_value );
	    return comparison;
	}

        return Comparison.LEFT_EQUALS_RIGHT;
    }




    /**
     * <p>
     * Quickie test.  Pass in 2 strings and they will be dictionary compared.
     * </p>
     */
    public static void main ( String [] args )
    {
	if ( args == null
	     || args.length != 2 )
	{
	    System.err.println ( "Usage: java musaico.foundation.io.DictionaryOrder string1 string2" );
	    return;
	}

	DictionaryOrder order = new DictionaryOrder ();
	Comparison comparison =
	    order.compareValues ( args [ 0 ], args [ 1 ] );

	System.out.println ( "compareValues ( \"" + args [ 0 ] + "\", \"" + args [ 1 ] + "\" ) = " + comparison );
    }
}
