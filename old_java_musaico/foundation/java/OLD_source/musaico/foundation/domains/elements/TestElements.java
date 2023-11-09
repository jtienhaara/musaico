package musaico.foundation.domains.elements;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.Generator;
import musaico.foundation.domains.Seconds;
import musaico.foundation.domains.StringRepresentation;


public class TestElements
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /* ============================================================ */
    public int testMethodArray (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        String test_name;
        String [] expected_array;
        String [] actual_array;
        Elements<String> under_test;

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.array () ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_array = new String [] { "1", "two", "III" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     expected_array );
        actual_array = under_test.array ();
        if ( under_test.flags ().isOverwritable () )
        {
            if ( under_test.container ().getClass ()
                 == expected_array.getClass ()
                 && actual_array != expected_array )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.array () should just return the original array." );
            }
        }
        else
        {
            if ( actual_array == expected_array )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.array () should not return the original array." );
            }
        }

        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.array () should return the same orginal elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        return test_num;
    }


    /* ============================================================ */
    public int testMethodAt (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        final String [] contents = new String [] { "1", "two", "III" };

        String test_name;
        String [] expected_at;
        String [] actual_at;
        Elements<String> under_test;

        under_test = template.from ( String.class,
                                     template.flags (),
                                     contents );

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.at ( Elements.NONE ) = empty ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_at = new String [] {};
        actual_at = under_test.at ( Elements.NONE );
        if ( ! Arrays.deepEquals ( expected_at, actual_at ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.at ( ... ).  Expected " + Arrays.toString ( expected_at ) + " but found " + Arrays.toString ( actual_at ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.at ( Elements.FIRST ) = "
            + contents [ 0 ]
            + " ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_at = new String [] { contents [ 0 ] };
        actual_at = under_test.at ( Elements.FIRST );
        if ( ! Arrays.deepEquals ( expected_at, actual_at ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.at ( ... ).  Expected " + Arrays.toString ( expected_at ) + " but found " + Arrays.toString ( actual_at ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.at ( Elements.LAST ) = "
            + contents [ contents.length - 1 ]
            + " ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_at = new String [] { contents [ contents.length - 1 ] };
        actual_at = under_test.at ( Elements.LAST );
        if ( ! Arrays.deepEquals ( expected_at, actual_at ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.at ( ... ).  Expected " + Arrays.toString ( expected_at ) + " but found " + Arrays.toString ( actual_at ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.at ( Elements.FIRST + 1L ) = "
            + contents [ 1 ]
            + " ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_at = new String [] { contents [ 1 ] };
        actual_at = under_test.at ( Elements.FIRST + 1L );
        if ( ! Arrays.deepEquals ( expected_at, actual_at ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.at ( ... ).  Expected " + Arrays.toString ( expected_at ) + " but found " + Arrays.toString ( actual_at ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.at ( Elements.LAST + 1L ) = "
            + contents [ contents.length - 2 ]
            + " ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_at = new String [] { contents [ contents.length - 2 ] };
        actual_at = under_test.at ( Elements.LAST + 1L );
        if ( ! Arrays.deepEquals ( expected_at, actual_at ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.at ( ... ).  Expected " + Arrays.toString ( expected_at ) + " but found " + Arrays.toString ( actual_at ) );
        }

        return test_num;
    }


    /* ============================================================ */
    public int testMethodFrom (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        final String [] contents_strings =
            new String [] { "1", "two", "III" };
        final Integer [] contents_integers =
            new Integer [] { 1, 2, 3, 4, 5 };

        String test_name;
        String [] expected_strings;
        String [] actual_strings;
        Integer [] expected_integers;
        Integer [] actual_integers;

        Elements<String> under_test_strings;
        Elements<Integer> under_test_integers;

        under_test_strings =
            new ArrayElements<String> ( String.class,
                                        ElementFlags.IMMUTABLE,
                                        contents_strings );
        under_test_integers =
            new ArrayElements<Integer> ( Integer.class,
                                         ElementFlags.IMMUTABLE,
                                         contents_integers );

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.from ( Integers ) = Integers ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_integers = contents_integers;
        actual_integers =
            under_test_strings.from ( ElementFlags.IMMUTABLE,
                                      under_test_integers )
            .array ();
        if ( ! Arrays.deepEquals ( expected_integers, actual_integers ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.from ( Integers ).  Expected " + Arrays.toString ( expected_integers ) + " but found " + Arrays.toString ( actual_integers ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.from ( Strings ) = Strings ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_strings = contents_strings;
        actual_strings =
            under_test_integers.from ( ElementFlags.IMMUTABLE,
                                       under_test_strings )
            .array ();
        if ( ! Arrays.deepEquals ( expected_strings, actual_strings ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.from ( Strings ).  Expected " + Arrays.toString ( expected_strings ) + " but found " + Arrays.toString ( actual_strings ) );
        }

        return test_num;
    }


    /* ============================================================ */
    public int testMethodIterator (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        String test_name;
        List<String> expected_iterated_elements;
        List<String> actual_iterated_elements;
        Elements<String> under_test;
        int index;
        List<Integer> differences;
        long ns_start;
        long ns_end;
        long ns_difference;
        BigDecimal seconds;
        String [] as_array;
        List<String> as_list;
        int do_something_useless;


        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.iterator () ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        expected_iterated_elements = new ArrayList<String> ();
        expected_iterated_elements.add ( "1" );
        expected_iterated_elements.add ( "two" );
        expected_iterated_elements.add ( "III" );
        expected_iterated_elements.add ( "4" );
        expected_iterated_elements.add ( "five" );
        under_test =
            new ArrayElements<String> ( String.class,
                                        ElementFlags.IMMUTABLE,
                                        new String [] { "1", "two",
                                                        "III", "4", "five" } );
        actual_iterated_elements = new ArrayList<String> ();

        index = 0;
        differences = new ArrayList<Integer> ();
        for ( String actual_element : under_test )
        {
            actual_iterated_elements.add ( actual_element );
            final String expected_element =
                expected_iterated_elements.get ( index );
            if ( index >= expected_iterated_elements.size ()
                 || ! expected_element.equals ( actual_element ) )
            {
                differences.add ( index );
            }
            index ++;
        }

        if ( index < ( expected_iterated_elements.size () - 1 ) )
        {
            for ( int i = index + 1;
                  i < expected_iterated_elements.size ();
                  i ++ )
            {
                differences.add ( i );
            }
        }

        if ( ! differences.isEmpty () )
        {
            throw new IllegalStateException ( "TEST FAILED: Expected to"
                                              + " iterate over elements "
                                              + expected_iterated_elements
                                              + " but actually iterated over "
                                              + actual_iterated_elements );
        }


        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.iterator () metrics ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        as_array = new String [ 1048576 ]; // 2 ^ 20 elements.
        for ( int e = 0; e < as_array.length; e ++ )
        {
            as_array [ e ] = "Element " + e;
        }
        under_test = template.from ( String.class,
                                     template.flags (),
                                     as_array );

        do_something_useless = 0;
        ns_start = System.nanoTime ();
        for ( String element : under_test )
        {
            do_something_useless ++;
        }
        ns_end = System.nanoTime ();
        ns_difference = ns_end - ns_start;
        seconds = new BigDecimal ( ns_difference )
            .multiply ( Seconds.PER_NANOSECOND );
        System.out.println ( "    Elements [ " + under_test.length () + " ]"
                             + " took " + seconds + " seconds." );

        as_array = under_test.array ();
        do_something_useless = 0;
        ns_start = System.nanoTime ();
        for ( String element : as_array )
        {
            do_something_useless ++;
        }
        ns_end = System.nanoTime ();
        ns_difference = ns_end - ns_start;
        seconds = new BigDecimal ( ns_difference )
            .multiply ( Seconds.PER_NANOSECOND );
        System.out.println ( "       Array [ " + as_array.length + " ]"
                             + " took " + seconds + " seconds." );

        as_list = under_test.list ();
        do_something_useless = 0;
        ns_start = System.nanoTime ();
        for ( String element : as_list )
        {
            do_something_useless ++;
        }
        ns_end = System.nanoTime ();
        ns_difference = ns_end - ns_start;
        seconds = new BigDecimal ( ns_difference )
            .multiply ( Seconds.PER_NANOSECOND );
        System.out.println ( "        List [ " + as_list.size () + " ]"
                             + " took " + seconds + " seconds." );

        return test_num;
    }


    /* ============================================================ */
    public int testMethodInsert (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        String test_name;
        String [] starting_array;
        String [] insert_array;
        Object original_container;
        Object actual_container;
        String [] expected_array;
        Elements<String> actual_elements;
        String [] actual_array;
        Elements<String> under_test;

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.insert ( Elements.AFTER_LAST,"
            + " ... ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        insert_array = new String [] { "4", "five" };
        expected_array = new String [] { "1", "two", "III", "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        original_container = under_test.container ();
        actual_elements =
            under_test.insert ( Elements.AFTER_LAST, insert_array );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.insert ( ... ) should not return null." );
        }

        actual_container = actual_elements.container ();
        if ( under_test.flags ().isVariableLength () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Variable length Elements.insert ( ... ) should return the original Elements under test." );
            }
            else if ( actual_container != original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Variable length Elements.insert ( ... ).container () should return the original container." );
            }
        }
        else
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Fixed length Elements.insert ( ... ) should not return the original Elements under test." );
            }
            else if ( actual_container == original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Fixed length Elements.insert ( ... ).container () should not return the original container." );
            }
        }

        actual_array = actual_elements.array ();
        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should return the fully inserted elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.insert ( Elements.LAST, ... ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        insert_array = new String [] { "4", "five" };
        expected_array = new String [] { "1", "two", "4", "five", "III" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        actual_elements = under_test.insert ( Elements.LAST, insert_array );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.insert ( ... ) should not return null." );
        }

        if ( under_test.flags ().isVariableLength () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Variable length Elements.insert ( ... ) should return the original Elements under test." );
            }
        }
        else
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Fixed length Elements.insert ( ... ) should not return the original Elements under test." );
            }
        }

        actual_array = actual_elements.array ();
        if ( actual_array == expected_array )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should not return the original array." );
        }

        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should return the fully inserted elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.insert ( length (), ... ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        insert_array = new String [] { "4", "five" };
        expected_array = new String [] { "1", "two", "III", "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        actual_elements = under_test.insert ( under_test.length (),
                                              insert_array );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.insert ( ... ) should not return null." );
        }

        if ( under_test.flags ().isVariableLength () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Variable length Elements.insert ( ... ) should return the original Elements under test." );
            }
        }
        else
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Fixed length Elements.insert ( ... ) should not return the original Elements under test." );
            }
        }

        actual_array = actual_elements.array ();
        if ( actual_array == expected_array )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should not return the original array." );
        }

        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should return the fully inserted elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.insert ( length () + 1L, ... )"
            + " should throw Exception ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        insert_array = new String [] { "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        try
        {
            actual_elements = under_test.insert ( under_test.length () + 1L,
                                                  insert_array );
            throw new IllegalStateException ( "ERROR elements.insert"
                                              +" ( elements.length () + 1L"
                                              + ", ... ) should fail"
                                              + " but returned: "
                                              + actual_elements );
        }
        catch ( IndexOutOfBoundsException e )
        {
            // All is as it should be.
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.insert ( Elements.FROM_END"
            + " + 1L, ... ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        insert_array = new String [] { "4", "five" };
        expected_array = new String [] { "1", "4", "five", "two", "III" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        actual_elements = under_test.insert ( Elements.FROM_END + 1L,
                                              insert_array );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.insert ( ... ) should not return null." );
        }

        if ( under_test.flags ().isVariableLength () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Variable length Elements.insert ( ... ) should return the original Elements under test." );
            }
        }
        else
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Fixed length Elements.insert ( ... ) should not return the original Elements under test." );
            }
        }

        actual_array = actual_elements.array ();
        if ( actual_array == expected_array )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should not return the original array." );
        }

        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.insert ( ... ).array () should return the fully inserted elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        return test_num;
    }


    /* ============================================================ */
    public int testMethodOverwrite (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        String test_name;
        String [] starting_array;
        String [] overwrite_array;
        Object original_container;
        Object actual_container;
        String [] expected_array;
        Elements<String> actual_elements;
        String [] actual_array;
        Elements<String> under_test;

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
        + ": Elements.overwrite ("
        + " Elements.AFTER_LAST, ... )"
        + " should throw Exception ["
        + template.flags ()
        + " "
        + ClassName.of ( template.getClass () )
        + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        overwrite_array = new String [] { "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        try
        {
            actual_elements = under_test.overwrite ( Elements.AFTER_LAST,
                                                     overwrite_array );
            throw new IllegalStateException ( "ERROR elements.overwrite"
                                              +" ( Elements.AFTER_LAST"
                                              + ", ... ) should fail"
                                              + " but returned: "
                                              + actual_elements );
        }
        catch ( IndexOutOfBoundsException e )
        {
            // All is as it should be.
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.overwrite ("
            + " Elements.FIRST, ... ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        overwrite_array = new String [] { "4", "five" };
        expected_array = new String [] { "4", "five", "III" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        original_container = under_test.container ();
        actual_elements = under_test.overwrite ( Elements.FIRST,
                                                 overwrite_array );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.overwrite ( ... ) should not return null." );
        }

        actual_container = actual_elements.container ();
        if ( under_test.flags ().isOverwritable () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.overwrite ( ... ) should return the original Elements under test." );
            }
            else if ( actual_container != original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.overwrite ( ... ).container () should return the original container." );
            }
        }
        else // not overwritable.
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ) should not return the original Elements under test." );
            }
            else if ( actual_container == original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ).container () should not return the original container." );
            }

            actual_array = actual_elements.array ();
            if ( actual_array == expected_array )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ).array () should not return the original array." );
            }
        }

        actual_array = actual_elements.array ();
        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.overwrite ( ... ).array () should return the fully overwritten elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.overwrite ("
            + " length (), ... ) should throw Exception ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        overwrite_array = new String [] { "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        try
        {
            actual_elements = under_test.overwrite ( under_test.length (),
                                                     overwrite_array );
            throw new IllegalStateException ( "ERROR elements.overwrite"
                                              +" ( elements.length ()"
                                              + ", ... ) should fail"
                                              + " but returned: "
                                              + actual_elements );
        }
        catch ( IndexOutOfBoundsException e )
        {
            // All is as it should be.
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.overwrite ("
            + " length () + 1L, ... )"
            + " should throw Exception ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        overwrite_array = new String [] { "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        try
        {
            actual_elements = under_test.overwrite ( under_test.length () + 1L,
                                                     overwrite_array );
            throw new IllegalStateException ( "ERROR elements.overwrite"
                                              +" ( elements.length () + 1L"
                                              + ", ... ) should fail"
                                              + " but returned: "
                                              + actual_elements );
        }
        catch ( IndexOutOfBoundsException e )
        {
            // All is as it should be.
        }

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.overwrite ("
            + " Elements.FROM_END + 1L, ... ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III" };
        overwrite_array = new String [] { "4", "five" };
        expected_array = new String [] { "1", "4", "five" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        original_container = under_test.container ();
        actual_elements = under_test.overwrite ( Elements.FROM_END + 1L,
                                                 overwrite_array );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.overwrite ( ... ) should not return null." );
        }

        actual_container = actual_elements.container ();
        if ( under_test.flags ().isOverwritable () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.overwrite ( ... ) should return the original Elements under test." );
            }
            else if ( actual_container != original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.overwrite ( ... ).container () should return the original container." );
            }
        }
        else // not overwritable.
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ) should not return the original Elements under test." );
            }
            else if ( actual_container == original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ).container () should not return the original container." );
            }

            actual_array = actual_elements.array ();
            if ( actual_array == expected_array )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ).array () should not return the original array." );
            }
        }

        actual_array = actual_elements.array ();
        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.overwrite ( ... ).array () should return the fully overwritten elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        return test_num;
    }


    /* ============================================================ */
    public int testMethodRemove (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        String test_name;
        String [] starting_array;
        long [] offsets_to_remove;
        String [] expected_array;
        Elements<String> actual_elements;
        String [] actual_array;
        Elements<String> under_test;

        // -------------------------------------------------
        test_num ++;
        test_name = "Test " + test_num
            + ": Elements.remove ( 0L, 1L, 2L, Elements.LAST ) ["
            + template.flags ()
            + " "
            + ClassName.of ( template.getClass () )
            + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III", "4", "five" };
        offsets_to_remove = new long [] { 0L, 1L, Elements.BACKWARD + 2L,
                                          3L, Elements.LAST };
        expected_array = new String [] {};
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        actual_elements = under_test.remove ( offsets_to_remove );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.remove ( ... ) should not return null." );
        }

        if ( under_test.flags ().isVariableLength () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Variable length Elements.remove ( ... ) should return the original Elements under test." );
            }
        }
        else
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Fixed length Elements.remove ( ... ) should not return the original Elements under test." );
            }
        }

        actual_array = actual_elements.array ();
        if ( actual_array == expected_array )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.remove ( ... ).array () should not return the original array." );
        }

        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.remove ( ... ).array () should return the fully removed elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        return test_num;
    }


    /* ============================================================ */
    private static class ASCiiOrder
        implements Comparator<String>, Serializable
    {
        private static final long serialVersionUID =
            TestElements.serialVersionUID;

        @Override
        public final int compare (
                String left,
                String right
                )
        {
            if ( left == null )
            {
                if ( right == null )
                {
                    return 0;
                }
                else
                {
                    return Integer.MAX_VALUE;
                }
            }
            else if ( right == null )
            {
                return Integer.MIN_VALUE + 1;
            }

            return left.compareTo ( right );
        }

        @Override
        public final String toString ()
        {
            return "ASCii order";
        }
    }

    public int testMethodSort (
            int test_num,
            Elements<String> template
            )
        throws Exception
    {
        String test_name;
        String [] starting_array;
        Comparator<String> order;
        Object original_container;
        Object actual_container;
        String [] expected_array;
        String [] actual_array;
        Elements<String> actual_elements;
        Elements<String> under_test;

        // -------------------------------------------------
        order = new TestElements.ASCiiOrder ();
        test_num ++;
        test_name = "Test " + test_num
        + ": Elements.sort ( " + order + " ) ["
        + template.flags ()
        + " "
        + ClassName.of ( template.getClass () )
        + "]";
        System.out.println ( test_name );
        starting_array = new String [] { "1", "two", "III", "4", "five" };
        expected_array = new String [] { "1", "4", "III", "five", "two" };
        under_test = template.from ( String.class,
                                     template.flags (),
                                     starting_array );
        original_container = under_test.container ();
        actual_elements = under_test.sort ( order );
        if ( actual_elements == null )
        {
            throw new NullPointerException ( "TEST FAILED: Elements.sort ( ... ) should not return null." );
        }

        actual_container = actual_elements.container ();
        if ( under_test.flags ().isOverwritable () )
        {
            if ( actual_elements != under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.sort ( ... ) should return the original Elements under test." );
            }
            else if ( actual_container != original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Overwritable Elements.sort ( ... ).container () should return the original container." );
            }
        }
        else // not overwritable.
        {
            if ( actual_elements == under_test )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.sort ( ... ) should not return the original Elements under test." );
            }
            else if ( actual_container == original_container )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.overwrite ( ... ).container () should not return the original container." );
            }

            actual_array = actual_elements.array ();
            if ( actual_array == expected_array )
            {
                throw new IllegalStateException ( "TEST FAILED: Immutable Elements.sort ( ... ).array () should not return the original array." );
            }
        }

        actual_array = actual_elements.array ();
        if ( ! Arrays.deepEquals ( expected_array, actual_array ) )
        {
            throw new IllegalStateException ( "TEST FAILED: Elements.sort ( ... ).array () should return the fully sorted elements.  Expected " + Arrays.toString ( expected_array ) + " but found " + Arrays.toString ( actual_array ) );
        }

        return test_num;
    }




    // Generator for testing GeneratedElements:
    public static class TestGenerator
        implements Generator<String>, Serializable
    {
        private static final long serialVersionUID =
            TestElements.serialVersionUID;

        final String [] strings;
        final long length;


        public TestGenerator (
                String ... strings
                )
        {
            this.strings = strings;
            this.length = (long) this.strings.length;
        }


        /**
         * @see musaico.foundation.domains.elements.Generator#at(long)
         */
        @Override
        public final String [] at (
                long index
                )
        {
            if ( index < 0L
                 || index >= this.length )
            {
                return new String [ 0 ];
            }
            else
            {
                return new String [] { this.strings [ (int) index ] };
            }
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public final boolean equals (
                                     Object object
                                     )
        {
            if ( object == null )
            {
                return false;
            }
            else if ( object == this )
            {
                return true;
            }
            else if ( object.getClass () != this.getClass () )
            {
                return false;
            }

            final TestElements.TestGenerator that =
                (TestElements.TestGenerator) object;

            if ( that.length != this.length )
            {
                return false;
            }
            else if ( ! Arrays.equals ( this.strings, that.strings ) )
            {
                return false;
            }

            return true;
        }


        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public final int hashCode ()
        {
            return (int) this.length;
        }


        /**
         * @see musaico.foundation.domains.elements.Generator#length()
         */
        @Override
        public final long length ()
        {
            return this.length;
        }


        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + StringRepresentation.of (
                      this.strings,
                      StringRepresentation.DEFAULT_ARRAY_LENGTH )
                + " ]";
        }
    }



    /* ============================================================ */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Generic array creation.
    public static void main (
            String [] args
            )
        throws Exception
    {
        final TestElements tests = new TestElements ();

        final Generator<String> generator =
            new TestElements.TestGenerator (
                new String [] { "a", "b", "c", "d" }
                );
        final Elements<String> [] base_string_templates = (Elements<String> [])
            new Elements []
            {
                new ArrayElements<String> ( String.class,
                                            ElementFlags.IMMUTABLE ),
                new GeneratedElements<String> ( String.class,
                                                ElementFlags.IMMUTABLE,
                                                generator ),
                new ListElements<String> ( String.class,
                                           ElementFlags.IMMUTABLE )
            };

        for ( Elements<String> base_template : base_string_templates )
        {
            System.out.println ( "=========================================" );
            System.out.println ( ClassName.of ( base_template.getClass () ) );
            System.out.println ( "-----------------------------------------" );

            int test_num = 0;
            for ( ElementFlags flags : ElementFlags.ALL )
            {
                // Skip over flags that are unsupported,
                // such as variable length flags for ArrayElements.
                if ( base_template.supportedFlags ( flags ).equals ( flags ) )
                {
                    final Elements<String> string_template =
                        base_template.duplicate ( flags );

                    test_num =
                        tests.testMethodArray ( test_num, string_template );
                    test_num =
                        tests.testMethodAt ( test_num, string_template );

                    // collection
                    // container
                    // duplicate
                    // elementClass
                    // equals
                    // filter
                    // filter
                    // find
                    // flags

                    test_num =
                        tests.testMethodFrom ( test_num, string_template );

                    // has
                    // hashCode
                    // hasOffset

                    test_num =
                        tests.testMethodInsert ( test_num, string_template );

                    // insertAll
                    // isArrayLength
                    // isEmpty
                    // isSingleton

                    test_num =
                        tests.testMethodIterator ( test_num, string_template );

                    // length
                    // list
                    // none
                    // offsets
                    // order

                    test_num =
                        tests.testMethodOverwrite ( test_num, string_template );

                    // overwriteAll
                    // range
                    // range

                    test_num =
                        tests.testMethodRemove ( test_num, string_template );

                    // removeAll
                    // set
                    // singleton

                    test_num =
                        tests.testMethodSort ( test_num, string_template );

                    // toString
                }
            }
        }

        System.out.println ( "-------------------------------------------------" );
        System.out.println ( "Tests all succeeded." );
    }
}
