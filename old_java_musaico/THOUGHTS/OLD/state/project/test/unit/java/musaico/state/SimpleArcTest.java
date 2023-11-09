package musaico.state;

import junit.framework.JUnit4TestAdapter;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

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


import musaico.io.Reference;

import musaico.state.IntLabel;
import musaico.state.SimpleArc;
import musaico.state.StringLabel;
import musaico.state.Traversal;
import musaico.state.TraversalException;
import musaico.state.Traverser;


/**
 * <p>
 * Unit test cases for SimpleArc.
 * </p>
 *
 * <p>
 * @see musaico.state.SimpleArc
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class SimpleArcTest
{
    /**
     * <p>
     * Tests that a SimpleArc enforces proper construction.
     * </p>
     */
    @Test public void testBuild ()
    {
        // Test cases:
        //     1) Cause NullPointerException by building before
        //        source or target nodes have been set.
        //     2) Cause NullPointerException by building before
        //        source node has been set.
        //     3) Cause NullPointerException by building before
        //        target node has been set.
        //     4) Set source and target nodes then build the Arc.
        SimpleArc arc_under_test;


        // Test case 1: Create a SimpleArc and immediately try to
        //              finalize the building.  Should throw a
        //              NullPointerException.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        try
        {
            arc_under_test.build ();
            fail ( "Expected build () to throw NullPointerException "
                   + "when the source and target have not yet been set" );
        }
        catch ( NullPointerException e )
        {
            // As expected, caused by null source / target nodes.
        }


        // Test case 2: Create a SimpleArc, set the source, then
        //              try to finalize the building.  Should throw
        //              a NullPointerException.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        arc_under_test.from ( new SimpleNode ( "source" ) );
        try
        {
            arc_under_test.build ();
            fail ( "Expected build () to throw NullPointerException "
                   + "when the source node has not yet been set" );
        }
        catch ( NullPointerException e )
        {
            // As expected, caused by null source node.
        }


        // Test case 3: Create a SimpleArc, set the target, then
        //              try to finalize the building.  Should throw
        //              a NullPointerException.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        arc_under_test.to ( new SimpleNode ( "target" ) );
        try
        {
            arc_under_test.build ();
            fail ( "Expected build () to throw NullPointerException "
                   + "when the target node has not yet been set" );
        }
        catch ( NullPointerException e )
        {
            // As expected, caused by null target node.
        }


        // Test case 4: Create a SimpleArc, set the source and target
        //              nodes, then finalize the building.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        arc_under_test.from ( new SimpleNode ( "source" ) );
        arc_under_test.to ( new SimpleNode ( "target" ) );
        assertEquals( arc_under_test, arc_under_test.build () );
    }


    /**
     * <p>
     * Tests to make sure the SimpleArc returns the source node
     * we told it to.
     * </p>
     */
    @Test public void testSource ()
    {
        // Test cases:
        //     1) Don't set the source node, retrieve it (null).
        //     2) Set the source node once to null.  Throws NPE.
        //     3) Set the source node once, retrieve it.
        //     4) Set the source node twice, make sure only the
        //        first one worked.
        SimpleArc arc_under_test;
        Node expected_node;

        // Test case 1: Don't set source node, retrieve it (null).
        // ===========================================================
        arc_under_test = new SimpleArc ();
        assertNull ( arc_under_test.source () );

        // Test case 2: Set source node once, retrieve it.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        try
        {
            arc_under_test.from ( null );
            fail ( "Expected from ( null ) to throw NullPointerException " );
        }
        catch ( NullPointerException e )
        {
            // Good, the SimpleArc did not allow us to set a null
            // source node.
        }

        // Test case 3: Set source node once, retrieve it.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        expected_node = new SimpleNode ( "source" );
        arc_under_test.from ( expected_node );
        assertEquals ( expected_node, arc_under_test.source () );

        // Test case 4: Set source node twice, retrieve it.
        //              Should return the first one set.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        expected_node = new SimpleNode ( "source" );
        Node unexpected_node = new SimpleNode ( "not the source" );
        arc_under_test.from ( expected_node );
        arc_under_test.from ( unexpected_node );
        assertEquals ( expected_node, arc_under_test.source () );
    }


    /**
     * <p>
     * Tests to make sure the SimpleArc returns the target node
     * we told it to.
     * </p>
     */
    @Test public void testTarget ()
    {
        // Test cases:
        //     1) Don't set the target node, retrieve it (null).
        //     2) Set the target node once to null.  Throws NPE.
        //     3) Set the target node once, retrieve it.
        //     4) Set the target node twice, make sure only the
        //        first one worked.
        SimpleArc arc_under_test;
        Node expected_node;

        // Test case 1: Don't set target node, retrieve it (null).
        // ===========================================================
        arc_under_test = new SimpleArc ();
        assertNull ( arc_under_test.target () );

        // Test case 2: Set target node once, retrieve it.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        try
        {
            arc_under_test.to ( null );
            fail ( "Expected to ( null ) to throw NullPointerException " );
        }
        catch ( NullPointerException e )
        {
            // Good, the SimpleArc did not allow us to set a null
            // target node.
        }

        // Test case 3: Set target node once, retrieve it.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        expected_node = new SimpleNode ( "target" );
        arc_under_test.to ( expected_node );
        assertEquals ( expected_node, arc_under_test.target () );

        // Test case 4: Set target node twice, retrieve it.
        //              Should return the first one set.
        // ===========================================================
        arc_under_test = new SimpleArc ();
        expected_node = new SimpleNode ( "target" );
        Node unexpected_node = new SimpleNode ( "not the target" );
        arc_under_test.to ( expected_node );
        arc_under_test.to ( unexpected_node );
        assertEquals ( expected_node, arc_under_test.target () );
    }


    /**
     * <p>
     * Tests that a SimpleArc executes a Traverser correctly while
     * being traversed.
     * </p>
     */
    @Test public void testTraverse ()
        throws TraversalException
    {
        // Test cases:
        //     1) Initiate a Traversal which executes some custom code.
        //     2) Initiate a Traversal which causes an exception.
        final IntLabel TRIGGER_OK = new IntLabel ( 1 );
        final StringLabel TRIGGER_EXCEPTION =
            new StringLabel ( "blow up real good" );

        // JMock provides sequences and counting and so on.
        final Mockery jmock = new JUnit4Mockery ();

        // Traverser to count invocations.
        final Traverser counter = jmock.mock( Traverser.class );

        Traverser logic = new Traverser ()
            {
                /** Custom code for traversing an Arc. */
                public void execute (
                                     Traversal traversal
                                     )
                    throws TraversalException
                {
                    // Count invocations.
                    counter.execute ( traversal );

                    Reference trigger = traversal.label ();
                    if ( trigger == TRIGGER_OK )
                    {
                        // Not much to do.
                    }
                    else
                    {
                        throw new TraversalException ( "Received trigger [%trigger%]",
                                                       "trigger", trigger );
                    }
                }
            };

        SimpleArc arc_under_test = new SimpleArc ();
        arc_under_test.executes ( logic );

        // Test case 1: Make sure the Traverser is invoked when
        //              the arc is traversed.  Make sure the trigger
        //              does not cause an exception to be thrown.
        // ===========================================================
        // Expectations:
        final Traversal traversal_ok = new SimpleTraversal ()
            .context ( null )
            .label ( TRIGGER_OK );
        jmock.checking ( new Expectations ()
            {
                {
                    this.oneOf ( counter ).execute ( traversal_ok );
                }
            }
            );

        // Execute:
        arc_under_test.traverse ( traversal_ok );


        // Test case 2: Make sure the Traverser is invoked when
        //              the arc is traversed.  Make sure the traverser
        //              throws an exception.
        // ===========================================================
        // Expectations:
        final Traversal traversal_fail = new SimpleTraversal ()
            .context ( null )
            .label ( TRIGGER_EXCEPTION );
        jmock.checking ( new Expectations ()
            {
                {
                    this.oneOf ( counter ).execute ( traversal_fail );
                }
            }
            );

        // Execute:
        try
        {
            arc_under_test.traverse ( traversal_fail );
            fail ( "Expected TraversalException to be thrown" );
        }
        catch ( TraversalException e )
        {
            // What we expected.
        }
    }




    /**
     * <p>
     * Provides the suite of tests to run.
     * </p>
     */
    public static junit.framework.Test suite() { 
        return new JUnit4TestAdapter ( SimpleArcTest.class ); 
    }
}
