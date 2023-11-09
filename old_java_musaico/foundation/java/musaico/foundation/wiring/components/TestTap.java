package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Schematic;
import musaico.foundation.wiring.Selector;
import musaico.foundation.wiring.Tag;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Tap;
import musaico.foundation.wiring.Wire;

public class TestTap
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private static void assertCarriersEquals (
            Carrier [] left,
            Carrier [] right,
            String error_message
            )
        throws AssertionError
    {
        if ( Arrays.equals ( left, right ) )
        {
            return;
        }

        throw new AssertionError ( error_message
                                   + ": left carriers = "
                                   + StringRepresentation.of ( left,
                                                               0,
                                                               0 )
                                   + "; right carriers = "
                                   + StringRepresentation.of ( right,
                                                               0,
                                                               0 ) );
    }


    private static void assertPulseEquals (
            Pulse left,
            Pulse right,
            String error_message
            )
        throws AssertionError
    {
        final boolean is_equal;
        if ( left == null )
        {
            if ( right == null )
            {
                is_equal = true;
            }
            else
            {
                is_equal = false;
            }
        }
        else if ( right == null )
        {
            is_equal = false;
        }
        else
        {
            is_equal = left.equals ( right );
        }

        if ( is_equal )
        {
            return;
        }

        throw new AssertionError ( error_message
                                   + ": left context = "
                                   + left
                                   + "; right context = "
                                   + right );
    }


    public static class Request
        implements Serializable
    {
        private static final long serialVersionUID = TestTap.serialVersionUID;

        private final Pulse context;
        private final Carrier [] contents;
        final int hashCode;

        private final Serializable lock = new String ( "lock" );
        // MUTABLE Syncronize on this.lock:
        private final List<TestTap.Request> nextRequests =
            new ArrayList<TestTap.Request> ();

        public Request (
                Pulse context,
                Carrier ... contents
                )
        {
            this.context = context;
            this.contents = contents;

            int hash_code = 0;
            if ( this.context != null )
            {
                hash_code += 31 * this.context.hashCode ();
            }

            if ( this.contents != null )
            {
                for ( Carrier carrier : this.contents )
                {
                    if ( carrier != null )
                    {
                        hash_code += carrier.hashCode ();
                    }
                }
            }

            this.hashCode = hash_code;
        }

        public static TestTap.Request pull (
                Board board,
                Conductor source_conductor,
                Wire source_wire,
                Carrier ... resulting_contents
                )
        {
            return new TestTap.Request (
                new Pulse (
                    Pulse.Direction.PULL,
                    board,
                    source_conductor,
                    source_wire,
                    new Tags () ),
                resulting_contents );
        }

        public static TestTap.Request push (
                Board board,
                Conductor source_conductor,
                Wire source_wire,
                Carrier ... input_contents
                )
        {
            return new TestTap.Request (
                new Pulse (
                    Pulse.Direction.PUSH,
                    board,
                    source_conductor,
                    source_wire,
                    new Tags () ),
                input_contents );
        }

        public final void checkContents (
                String message,
                Carrier ... actual_contents
                )
            throws AssertionError
        {
            TestTap.assertCarriersEquals ( this.contents,
                                           actual_contents,
                                           message );
        }

        public final void checkContext (
                String message,
                Pulse actual_context
                )
            throws AssertionError
        {
            TestTap.assertPulseEquals ( this.context,
                                        actual_context,
                                        message );
        }

        public final Pulse context ()
        {
            return this.context;
        }

        public final Carrier [] contents ()
        {
            return this.contents;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public final boolean equals (
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
            else if ( object.getClass () != this.getClass () )
            {
                return false;
            }

            final TestTap.Request that = (TestTap.Request) object;

            if ( this.context == null )
            {
                if ( that.context != null )
                {
                    return false;
                }
            }
            else if ( that.context == null )
            {
                return false;
            }
            else if ( ! this.context.equals ( that.context ) )
            {
                return false;
            }

            if ( this.nextRequests == null )
            {
                if ( that.nextRequests != null )
                {
                    return false;
                }
            }
            else if ( that.nextRequests == null )
            {
                return false;
            }
            else if ( ! this.nextRequests.equals ( that.nextRequests ) )
            {
                return false;
            }

            if ( this.contents == null )
            {
                if ( that.contents != null )
                {
                    return false;
                }
            }
            else if ( that.contents == null )
            {
                return false;
            }
            else if ( ! Arrays.equals ( this.contents, that.contents ) )
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
            return this.hashCode;
        }

        public final TestTap.Request next (
                TestTap.Request ... next_requests
                )
        {
            synchronized ( this.lock )
            {
                for ( TestTap.Request next_request : next_requests )
                {
                    this.nextRequests.add ( next_request );
                }
            }

            return this;
        }

        public final TestTap.Request [] nextRequests ()
        {
            final TestTap.Request [] next_requests;
            synchronized ( this.lock )
            {
                final TestTap.Request [] template =
                    new TestTap.Request [ this.nextRequests.size () ];
                next_requests = this.nextRequests.toArray ( template );
            }

            return next_requests;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return "request { " + this.context + " } = "
                + StringRepresentation.of ( this.contents,
                                            0,   // No maximum total length
                                            0 ); // No maximum element length
        }
    }




    public static class ExpectedSequence
        implements Serializable
    {
        private static final long serialVersionUID = TestTap.serialVersionUID;


        private final Serializable lock = new String ( "lock" );

        // MUTABLE synchronize on this.lock:
        private final List<TestTap.Request> expectedRequests =
            new ArrayList<TestTap.Request> ();
        private final List<TestTap.Request> actualRequests =
            new ArrayList<TestTap.Request> ();


        public ExpectedSequence ()
        {
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public final boolean equals (
                Object object
                )
        {
            if ( object == this )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public final int hashCode ()
        {
            return super.hashCode ();
        }

        public final TestTap.ExpectedSequence expect (
                TestTap.Request expected_request
                )
        {
            synchronized ( this.lock )
            {
                this.expectTrees ( expected_request );
            }

            return this;
        }

        // MUST be called inside synchronized ( this.lock ):
        private final void expectTrees (
                TestTap.Request ... trees
                )
        {
            for ( TestTap.Request tree : trees )
            {
                this.expectedRequests.add ( tree );

                final TestTap.Request [] branches =
                    tree.nextRequests ();
                if ( branches.length == 0 )
                {
                    continue;
                }

                this.expectTrees ( branches );
            }
        }

        public final TestTap.Request pull (
                Pulse context
                )
            throws AssertionError
        {
            final int next_request_index;
            final TestTap.Request expected_request;
            final Carrier [] result;
            final TestTap.Request actual_request;
            synchronized ( this.lock )
            {
                next_request_index = this.actualRequests.size ();
                if ( next_request_index >= this.expectedRequests.size () )
                {
                    expected_request = null;
                    result = new Carrier [ 0 ];
                }
                else
                {
                    expected_request = this.expectedRequests.get ( next_request_index );
                    result = expected_request.contents ();
                }

                actual_request = new TestTap.Request ( context, result );
                this.actualRequests.add ( actual_request );
            }

            if ( expected_request == null )
            {
                throw new AssertionError ( "ERROR Did not expect any more requests, but got request # "
                                           + next_request_index + ": " + actual_request );
            }

            return actual_request;
        }

        public final TestTap.Request push ( Pulse context, Carrier ... actual_data )
        {
            final int next_request_index;
            final TestTap.Request expected_request;
            final TestTap.Request actual_request;
            synchronized ( this.lock )
            {
                next_request_index = this.actualRequests.size ();
                if ( next_request_index >= this.expectedRequests.size () )
                {
                    expected_request = null;
                }
                else
                {
                    expected_request = this.expectedRequests.get ( next_request_index );
                }

                actual_request = new TestTap.Request ( context, actual_data );
                this.actualRequests.add ( actual_request );
            }

            if ( expected_request == null )
            {
                throw new AssertionError ( "ERROR Did not expect any more requests, but got request # "
                                           + next_request_index + ": " + actual_request );
            }

            return actual_request;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return "expected_sequence#" + this.hashCode ();
        }
    }




    public static class TestConductor
        implements Conductor, Serializable
    {
        private static final long serialVersionUID = TestTap.serialVersionUID;


        private static final Advocate classContracts =
            new Advocate ( TestTap.TestConductor.class );

        private final String id;
        private final Tags tags = new Tags ();
        private final TestTap.ExpectedSequence expectedSequence;


        public TestConductor (
                String id,
                TestTap.ExpectedSequence expected_sequence
                )
            throws Parameter2.MustNotBeNull.Violation
        {
            classContracts.check ( Parameter2.MustNotBeNull.CONTRACT,
                                   expected_sequence );

            if ( id == null )
            {
                this.id = "test_conductor#" + super.hashCode ();
            }
            else
            {
                this.id = id;
            }

            this.expectedSequence = expected_sequence;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public final boolean equals (
                Object object
                )
        {
            if ( object == this )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public final int hashCode ()
        {
            return this.id.hashCode ();
        }

        /**
         * @see musaico.foundation.wiring.Conductor#id()
         */
        @Override
        public final String id ()
            throws Return.NeverNull.Violation
        {
            return this.id;
        }

        /**
         * @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Pulse)
         */
        @Override
        public final Carrier [] pull ( Pulse context )
            throws AssertionError
        {
            // Throws AssertionError:
            final TestTap.Request actual_request = this.expectedSequence.pull ( context );
            // Throws AsssertionError:
            actual_request.checkContext ( "Unexpected pull",
                                          context );


            final TestTap.Request [] next_requests = actual_request.nextRequests ();
            if ( next_requests.length == 0 )
            {
                final Carrier [] result_contents = actual_request.contents ();
                return result_contents;
            }

            for ( int nr = 0; nr < next_requests.length; nr ++ )
            {
                final TestTap.Request next_request = next_requests [ nr ];

                final Pulse next_context = next_request.context ();
                final Carrier [] next_contents = next_request.contents ();

                final Wire next_wire = next_context.sourceWire ();
                final Pulse.Direction next_direction = next_context.direction ();
                switch ( next_direction )
                {
                case PULL:
                    // Throws AssertionError:
                    final Carrier [] next_pulled = next_wire.pull ( next_context );
                    // Throws AssertionError:
                    next_request.checkContents ( "Unexpected pull results from "
                                                + next_wire
                                                + " while pulling from "
                                                + this
                                                + " (next request # " + nr + ")",
                                                next_pulled );
                    break;

                case PUSH:
                    // Throws AssertionError:
                    next_wire.push ( next_context, next_contents );
                    break;

                default:
                    throw new AssertionError ( "ERROR Unknown direction: should be PUSH or PULL, not "
                                               + next_direction
                                               + " while pulling from "
                                               + this
                                               + " (next request # " + nr + ")" );
                }
            }

            final Carrier [] pulled_carriers = actual_request.contents ();
            return pulled_carriers;
        }

        /**
         * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
         */
        @Override
        public final void push ( Pulse context, Carrier ... actual_data )
        {
            // Throws AssertionError:
            final TestTap.Request actual_request = this.expectedSequence.push ( context, actual_data );
            // Throws AsssertionError:
            actual_request.checkContext ( "Unexpected push",
                                          context );
            // Throws AsssertionError:
            actual_request.checkContents ( "Unexpected push contents",
                                           actual_data );

            final TestTap.Request [] next_requests = actual_request.nextRequests ();
            if ( next_requests.length == 0 )
            {
                return;
            }

            for ( int nr = 0; nr < next_requests.length; nr ++ )
            {
                final TestTap.Request next_request = next_requests [ nr ];

                final Pulse next_context = next_request.context ();
                final Carrier [] next_contents = next_request.contents ();

                final Wire next_wire = next_context.sourceWire ();
                final Pulse.Direction next_direction = next_context.direction ();
                switch ( next_direction )
                {
                case PULL:
                    // Throws AssertionError:
                    final Carrier [] next_pulled = next_wire.pull ( next_context );
                    // Throws AssertionError:
                    next_request.checkContents ( "Unexpected pull results from "
                                                + next_wire
                                                + " while pulling from "
                                                + this
                                                + " (next request # " + nr + ")",
                                                next_pulled );
                    break;

                case PUSH:
                    // Throws AssertionError:
                    next_wire.push ( next_context, next_contents );
                    break;

                default:
                    throw new AssertionError ( "ERROR Unknown direction: should be PUSH or PULL, not "
                                               + next_direction
                                               + " while pulling from "
                                               + this
                                               + " (next request # " + nr + ")" );
                }
            }
        }

        /**
         * @see musaico.foundation.wiring.Conductor#tags()
         */
        @Override
        public final Tags tags ()
            throws Return.NeverNull.Violation
        {
            return this.tags;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return "test_tap [ " + this.id + " ]";
        }
    }




    public void execute ()
    {
        // !!! final Conductor in =
        // !!! new TestTap.TestConductor ( 
        // !!! final Conductor
        // !!! final Wire tapped_wire = new 
        // !!! final Tap under_test = new StandardTap ( 
    }


    public static void main (
            String [] args
            )
    {
        new TestTap ().execute ();
    }
}
