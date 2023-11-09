package musaico.foundation.filter.composite;

import junit.framework.JUnit4TestAdapter;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.rules.ExpectedException;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Test cases for And'ed Filters.
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
 * Copyright (c) 2016-2018 Johann Tienhaara
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
public class AndTests
{
    private final Filter<String> DISCARD_ALL =
        new Filter<String> ()
    {
        private static final long serialVersionUID = 1L;

        /**
         * @see musaico.foundation.io.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         String ignored_filterable_string
                                         )
        throws NullPointerException
        {
            return FilterState.DISCARDED;
        }
    };


    private final Filter<String> KEEP_ALL =
        new Filter<String> ()
    {
        private static final long serialVersionUID = 1L;
        /**
         * @see musaico.foundation.io.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         String ignored_filterable_string
                                         )
        throws NullPointerException
        {
            return FilterState.KEPT;
        }
    };


    /**
     * <p>
     * Tests passing nulls to the constructor, which ignores them.
     * </p>
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"}) // Filter<String> []
    public void testAndConstructorNullArray ()
    {
        And<String> under_test;
        Filter<String> [] expected_filters;
        Filter<String> [] actual_filters;

        // 1. And ( (Filter<String>[]) null )
        //    = And ()
        under_test = new And<String> ( (Filter[]) null );
        expected_filters = (Filter<String> [])
            new Filter []
        {
        };
        actual_filters = under_test.filters ();
        assertArrayEquals ( "Incorrect component filters",
                            expected_filters,
                            actual_filters );
    }


    /**
     * <p>
     * Tests passing nulls to the constructor, which ignores them.
     * </p>
     */
    @Test
    @SuppressWarnings({"unchecked", "rawtypes"}) // Filter<String> []
    public void testAndConstructorNullElements ()
    {
        And<String> under_test;
        Filter<String> [] expected_filters;
        Filter<String> [] actual_filters;

        // 1. And ( f1, null, f2, null )
        //    = And ( f1, f2 )
        under_test = new And<String> ( KEEP_ALL,
                                       null,
                                       DISCARD_ALL,
                                       null,
                                       KEEP_ALL );
        expected_filters = (Filter<String> [])
            new Filter []
        {
            KEEP_ALL,
            DISCARD_ALL,
            KEEP_ALL
        };
        actual_filters = under_test.filters ();
        assertArrayEquals ( "Incorrect component filters",
                            expected_filters,
                            actual_filters );
    }


    /**
     * <p>
     * Tests the following scenario(s):
     * </p>
     *
     * <pre>
     *     Filter1 AND Filter2 AND Filter3     Expected result
     *     ----------+-----------+-----------+-----------------
     *     ---       | ---       | ---       | KEPT
     *
     * </pre>
     */
    @Test
    @SuppressWarnings("unchecked") // Generic array creation.
    public void testAnd0 ()
    {
        And<String> under_test;
        FilterState expected_result;
        FilterState actual_result;

        // 1. And ()
        //    = KEPT
        under_test = new And<String> ();
        expected_result = FilterState.KEPT;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );
    }


    /**
     * <p>
     * Tests the following scenario(s):
     * </p>
     *
     * <pre>
     *     Filter1 AND Filter2 AND Filter3     Expected result
     *     ----------+-----------+-----------+-----------------
     *     DISCARDED | ---       | ---       | DISCARDED
     *     KEPT      | ---       | ---       | KEPT
     *
     * </pre>
     */
    @Test
    @SuppressWarnings("unchecked") // Generic array creation.
    public void testAnd1 ()
    {
        And<String> under_test;
        FilterState expected_result;
        FilterState actual_result;

        // 1. And ( DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 2. And ( KEPT )
        //    = KEPT
        under_test = new And<String> (
                                      KEEP_ALL
                                      );
        expected_result = FilterState.KEPT;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );
    }


    /**
     * <p>
     * Tests the following scenario(s):
     * </p>
     *
     * <pre>
     *     Filter1 AND Filter2 AND Filter3     Expected result
     *     ----------+-----------+-----------+-----------------
     *     DISCARDED | DISCARDED | ---       | DISCARDED
     *     DISCARDED | KEPT      | ---       | DISCARDED
     *     KEPT      | DISCARDED | ---       | DISCARDED
     *     KEPT      | KEPT      | ---       | KEPT
     *
     * </pre>
     */
    @Test
    @SuppressWarnings("unchecked") // Generic array creation.
    public void testAnd2 ()
    {
        And<String> under_test;
        FilterState expected_result;
        FilterState actual_result;

        // 1. And ( DISCARDED, DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL,
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 2. And ( DISCARDED, KEPT )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL,
                                      KEEP_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 3. And ( KEPT, DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      KEEP_ALL,
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 4. And ( KEPT, KEPT )
        //    = KEPT
        under_test = new And<String> (
                                      KEEP_ALL,
                                      KEEP_ALL
                                      );
        expected_result = FilterState.KEPT;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );
    }


    /**
     * <p>
     * Tests the following scenario(s):
     * </p>
     *
     * <pre>
     *     Filter1 AND Filter2 AND Filter3     Expected result
     *     ----------+-----------+-----------+-----------------
     *     DISCARDED | DISCARDED | DISCARDED | DISCARDED
     *     DISCARDED | DISCARDED | KEPT      | DISCARDED
     *     DISCARDED | KEPT      | DISCARDED | DISCARDED
     *     DISCARDED | KEPT      | KEPT      | DISCARDED
     *     KEPT      | DISCARDED | DISCARDED | DISCARDED
     *     KEPT      | DISCARDED | KEPT      | DISCARDED
     *     KEPT      | KEPT      | DISCARDED | DISCARDED
     *     KEPT      | KEPT      | KEPT      | KEPT
     *
     * </pre>
     */
    @Test
    @SuppressWarnings("unchecked") // Generic array creation.
    public void testAnd3 ()
    {
        And<String> under_test;
        FilterState expected_result;
        FilterState actual_result;

        // 1. And ( DISCARDED, DISCARDED, DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL,
                                      DISCARD_ALL,
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 1. And ( DISCARDED, DISCARDED, KEPT )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL,
                                      DISCARD_ALL,
                                      KEEP_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 1. And ( DISCARDED, KEPT, DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL,
                                      KEEP_ALL,
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 1. And ( DISCARDED, KEPT, KEPT )
        //    = DISCARDED
        under_test = new And<String> (
                                      DISCARD_ALL,
                                      KEEP_ALL,
                                      KEEP_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 1. And ( KEPT, DISCARDED, DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      KEEP_ALL,
                                      DISCARD_ALL,
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 1. And ( KEPT, DISCARDED, KEPT )
        //    = DISCARDED
        under_test = new And<String> (
                                      KEEP_ALL,
                                      DISCARD_ALL,
                                      KEEP_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 1. And ( KEPT, KEPT, DISCARDED )
        //    = DISCARDED
        under_test = new And<String> (
                                      KEEP_ALL,
                                      KEEP_ALL,
                                      DISCARD_ALL
                                      );
        expected_result = FilterState.DISCARDED;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );

        // 8. And ( KEPT, KEPT, KEPT )
        //    = KEPT
        under_test = new And<String> (
                                      KEEP_ALL,
                                      KEEP_ALL,
                                      KEEP_ALL
                                      );
        expected_result = FilterState.KEPT;
        actual_result = under_test.filter ( "Ignored filterable String" );
        assertEquals ( "Incorrect output from " + under_test,
                       expected_result,
                       actual_result );
    }
}
