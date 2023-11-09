package musaico.foundation.types;

import java.io.Serializable;


import musaico.foundation.operations.select.StandardSelect;

import musaico.foundation.term.Countable;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.NotOne;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;

import musaico.foundation.term.finite.Many;
import musaico.foundation.term.finite.No;
import musaico.foundation.term.finite.One;


public class TestSelect
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    private final <TEST_INPUT extends Object, TEST_OUTPUT extends Object>
        void testOperation (
                            String title,
                            String description,
                            Operation<TEST_INPUT, TEST_OUTPUT> under_test,
                            Term<TEST_INPUT> input,
                            Term<TEST_OUTPUT> expected_result
                            )
        throws Exception
    {
        System.out.println ( "===================================="
                             + "====================================" );
        System.out.println ( title + " Start" );
        System.out.println ( "  " + description );


        final Term<TEST_OUTPUT> actual_result = under_test.apply ( input );

        System.out.println ( "  Expected result: " + expected_result );
        System.out.println ( "  Actual result:   " + actual_result );

        System.out.println ( title + " End" );

        if ( ! expected_result.equals ( actual_result ) )
        {
            throw new Exception ( "FAILURE: expected "
                                  + expected_result
                                  + " but received "
                                  + actual_result );
        }
        else
        {
            System.out.println ( "SUCCESS" );
        }

        System.out.println ( "------------------------------------"
                             + "------------------------------------" );
    }




    public void testAt1 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new One<String> ( StandardType.STRING,
                              "a" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.FIRST );

        this.testOperation ( "At Test # 1",
                             "At ( 0 ), the first element of 6, 'a'.",
                             under_test,
                             input,
                             expected_result );
    }


    public void testAt2 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new One<String> ( StandardType.STRING,
                              "d" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( 3L );

        testOperation ( "At Test # 2",
                        "At ( 3 ), the 4th element of 6, 'd'.",
                        under_test,
                        input,
                        expected_result );
    }


    public void testAt3 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new One<String> ( StandardType.STRING,
                              "f" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( 5L );

        testOperation ( "At Test # 3",
                        "At ( 5 ), the last element of 6, 'f'.",
                        under_test,
                        input,
                        expected_result );
    }


    public void testAt4 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new One<String> ( StandardType.STRING,
                              "f" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.LAST );

        testOperation ( "At Test # 4",
                        "At ( COUNTABLE.LAST ), the last element of 6, 'f'.",
                        under_test,
                        input,
                        expected_result );
    }


    public void testAt5 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new One<String> ( StandardType.STRING,
                              "c" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.BACKWARD + 3L );

        testOperation ( "At Test # 5",
                        "At ( end-3 ), the 3rd element of 6, 'c'.",
                        under_test,
                        input,
                        expected_result );

        final Term<String> actual_result = under_test.apply ( input );
    }


    public void testAt6 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new One<String> ( StandardType.STRING,
                              "a" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.BACKWARD + 5L );

        testOperation ( "At Test # 6",
                        "At ( end-5 ), the first element of 6, 'a'.",
                        under_test,
                        input,
                        expected_result );

        final Term<String> actual_result = under_test.apply ( input );
    }


    public void testAt7 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new No<String> ( StandardType.STRING );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.NONE );

        testOperation ( "At Test # 7",
                        "At ( Countable.NONE ), No such element.",
                        under_test,
                        input,
                        expected_result );

        final Term<String> actual_result = under_test.apply ( input );
    }


    public void testAt8 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new No<String> ( StandardType.STRING );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.AFTER_LAST );

        testOperation ( "At Test # 8",
                        "At ( Countable.AFTER_LAST ), No such element.",
                        under_test,
                        input,
                        expected_result );

        final Term<String> actual_result = under_test.apply ( input );
    }


    public void testAt9 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new No<String> ( StandardType.STRING );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( 6L );

        testOperation ( "At Test # 9",
                        "At ( 6 ), No such element.",
                        under_test,
                        input,
                        expected_result );

        final Term<String> actual_result = under_test.apply ( input );
    }


    public void testAt10 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        final Term<String> expected_result =
            new No<String> ( StandardType.STRING );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .at ( Countable.BACKWARD + 6L );

        testOperation ( "At Test # 10",
                        "At ( end-6 ), No such element.",
                        under_test,
                        input,
                        expected_result );

        final Term<String> actual_result = under_test.apply ( input );
    }




    public void testRange1 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        // The actual result will be a term.finite.Range object,
        // not a term.finite.Many object, but it will still
        // return equals ( expected_result ) == true.
        final Term<String> expected_result = input;

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .range ( Countable.FIRST, Countable.LAST );

        this.testOperation ( "Range Test # 1",
                             "Range ( 0, end ), all elements 'a' - 'f'.",
                             under_test,
                             input,
                             expected_result );
    }


    public void testRange2 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        // The actual result will be a term.finite.Range object,
        // not a term.finite.Many object, but it will still
        // return equals ( expected_result ) == true.
        final Term<String> expected_result =
            new Many<String> ( StandardType.STRING,
                               "b",
                               "c",
                               "d",
                               "e" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .range ( Countable.FORWARD + 1L, Countable.BACKWARD + 1L );

        this.testOperation ( "Range Test # 2",
                             "Range ( 1, end-1 ), elements 'b' - 'e'.",
                             under_test,
                             input,
                             expected_result );
    }


    public void testRange3 ()
        throws Exception
    {
        final Term<String> input =
            new Many<String> ( StandardType.STRING,
                               "a",
                               "b",
                               "c",
                               "d",
                               "e",
                               "f" );
        // The actual result will be a term.finite.Range object,
        // not a term.finite.Many object, but it will still
        // return equals ( expected_result ) == true.
        final Term<String> expected_result =
            new Many<String> ( StandardType.STRING,
                               "f",
                               "e",
                               "d",
                               "c",
                               "b",
                               "a" );

        final Operation<String, String> under_test =
            input.type ()
                 .select ()
                 .range ( Countable.LAST, Countable.FIRST );

        this.testOperation ( "Range Test # 3",
                             "Range ( end, 0 ), reverse order 'f' - 'a'.",
                             under_test,
                             input,
                             expected_result );
    }




    public static void main (
                             String [] args
                             )
        throws Exception
    {
        new TestSelect ().testAt1 ();
        new TestSelect ().testAt2 ();
        new TestSelect ().testAt3 ();
        new TestSelect ().testAt4 ();
        new TestSelect ().testAt5 ();
        new TestSelect ().testAt6 ();
        new TestSelect ().testAt7 ();
        new TestSelect ().testAt8 ();
        new TestSelect ().testAt9 ();
        new TestSelect ().testAt10 ();

        new TestSelect ().testRange1 ();
        new TestSelect ().testRange2 ();
        new TestSelect ().testRange3 ();
    }
}
