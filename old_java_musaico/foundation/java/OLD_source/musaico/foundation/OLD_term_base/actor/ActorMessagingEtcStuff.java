package musaico.foundation.value.blocking;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.value.Maybe;
import musaico.foundation.value.NonBlocking;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Operation;
import musaico.foundation.value.ProgressiveOperation;
import musaico.foundation.value.Type;
import musaico.foundation.value.ValueViolation;

import musaico.foundation.value.abnormal.Error;

import musaico.foundation.value.expression.Expression;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * An asynchronous, delayed result which wakes up the parent Blocked
 * object after waiting, successfully or unsuccessfully, for the
 * asynchronous conditional Value.
 * </p>
 *
 *
 * <p>
 * In Java, every Result must be Serializable in order to play nicely
 * over RMI.  WARNING: Results, and the Value parameters which are passed
 * to their methods, are Serializable but, for all intents and purposes,
 * are useless once serialized.  The whole purpose of a Result is to
 * capture data when it was ready, and notify interested parties
 * accordingly.  Once a Result has been serialized, it is divorced
 * from its original, source Result, and so will not find out when
 * the final data is ready, or what that data is.  An Expression added
 * to the list of recipients for notification when a remote Result is
 * ready will also be serialized, divorced from its source Expression,
 * and so breaking the connection.  If Results are to be shared across
 * RMI, then some additional infrastructure is required.  a framework
 * something like the following would work:
 * </p>
 *
 * <pre>
 *     Host 1                             Host 2
 *     ================================   ================================
 *     Blocking Value creates a             |
 *       | UnicastRemoteObject Result.      |
 *       |                                  |
 *     Host 1 creates a UnicastRemoteObject |
 *       | Blocking Value and shares   -- Host 2 stores a reference to
 *       | it with host 2.                  | the remote Blocking
 *       |                                  | Value, which in turn
 *     Host 1 works toward the final        | maintains a reference to the
 *       | data for the Result.             | remote Result.
 *       |                                  |
 *       |                                Host 2 creates a UnicastRemoteObject
 *       |                                  | Expression, which will be
 *     Host 1 adds the remote Expression -- | notified when the Result
 *       | to the list to be notified.      + is complete.
 *       |
 *      ...
 *       |
 *     Host 1's Result completes its
 *       | work.  Subscribers can now  -- Host 2's Expression receives
 *       + be notified.                     | notification that the
 *                                          | remote Result is complete.
 * </pre>
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
 * @see musaico.foundation.value.blocking.MODULE#COPYRIGHT
 * @see musaico.foundation.value.blocking.MODULE#LICENSE
 */
public interface Result<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    --> ResultFactory:;
    public abstract Maybe<ResultServer<VALUE>> create (
            Type<VALUE> type
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
    public abstract Maybe<ResultServer<VALUE>> destroy (
            Type<VALUE> type
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    StandardResultFactory;
    RMISafeResultFactory;

    Or:;
    public interface Server<CLIENT, SERVER>
    {
        public abstract SERVER server ()
            throws ReturnNeverNull.Violation;
        ...
    }


    Or *after* the value package have message package:;
    public interface Message;
    public interface Client
    {
    }
    public interface Server
    {
    }
    or actor package:;
    public interface MessageSection
    {
        Value<Object> content ();
    }
    public interface MessageHeader extends MessageSection
    {
        Serializable from ();
        Serializable to ();
        BigDecimal timeoutInSeconds ();
    }
    public interface MessageBody extends MessageSection
    {
        Capability capability ();
    }
    public interface MessageFooter extends MessageSection
    {
    }
    public interface Message
    {
        MessageHeader header ();
        MessageBody body ();
        MessageFooter footer ();
    }
    public class StandardMessageType implements Type<Message>...;
    public interface Actor
    {
        public Serializable id ();
        public BigDecimal defaultSendTimeout ();
        public Type<Message> messageType ( Capability capability );
        public abstract Value<Message> send ( Value<Message> messages );
        public abstract Value<Message> receive ( BigDecimal timeout_in_seconds );
        public abstract Value<Capability> capabilities ();
        public abstract ZeroOrOne<Capability> capability ( Capability capability, Serializable subject_id ); // subject for checking security.
        public abstract Value<Serializable> peers ();
        public abstract Value<Serializable> peer ( Serializable id ); // Use .orXYZ() if expecting only one under that ID.
        public abstract Message message ( Serializable to_id,
                                          BigDecimal timeout_in_seconds,
                                          Value<Object> header,
                                          Capability capability,
                                          Value<Object> body,
                                          Value<Object> footer );
    }


    public class LocalActor implements Actor
    {
        private static final Object classLock = new Object (); // Deliberately not serializable.
        private static final Map<Long, LocalActor> localActors =
            new HashMap<Long, LocalActor> ();
        private static long nextID = 0L;
        public static final LocalActor NONE = new NoLocalActor ();
        private final Object lock = new Object (); // Deliberately not serializable.
        private final long id;
        private final Value<Capability> capabilities;
        private final List<Message> received = new ArrayList<Message> ();
        public LocalActor ( Value<Capability> capabilities ) { ...register in localActors etc... }
        public final Long id () { ... }
        public final Value<Message> send ( Value<Message> requests )
        {
            for ( Message request ... )
            {
                final Long to_id = request.header ().to ();
                final LocalActor to = LocalActor.localActors.get ( to_id );
                ...error if null / no such local actor...;
                ...Also check to make sure from is me...;
                final Capability capability = request.header ().capability ();
                if ( to_actor.capability ( capability, this.id ) == null ) { ...errr... }
                synchronized ( to_actor.lock )
                {
                    to_actor.received.add ( response );
                    to_actor.received.notify ();
                }
            }

            return requests; // unless error, of course; then include a partial cause if applicable.
        }
        public final Value<Message> receive ( BigDecimal timeout_in_seconds )
        {
            final long timeout_in_milliseconds = ...;
            synchronized ( this.lock )
            {
                if ( this.received.isEmpty ()
                     && timeout_in_milliseconds > 0L )
                {
                    try
                    {
                        this.lock.wait ( timeout_in_milliseconds );
                    }
                    catch ( InterruptedException e )
                    {
                        ...return error...;
                    }
                }

                if ( this.received.isEmpty () )
                {
                    ...return timeout...;
                }

                final One<Message> message = this.received.get ( 0 );
                this.received.remove ( 0 );
                return message;
            }
        }
        public Value<Capability> capabilities ()
        {
            ...!!!...;
        }
        public Value<Serializable> peers ()
        {
            ...return LocalActor.localActors.keySet() -> Value<Serializable>...;
        }
        public Value<Serializable> peer ( Serializable id ) // Use .orXYZ() if expecting only one under that ID.
        {
            ...return the One id if it is in LocalActor.localActors.keySet (),
               otherwise return No id...;
        }
    }


    public interface Result<VALUE> // (in value package)
    {
        public static final Capability SET_FINAL_RESULT = ...;
        public static final Capability UPDATE_PARTIAL_RESULT = ...;
        public abstract NonBlocking<VALUE> partialValue ()
            throws ReturnNeverNull.Violation;

        public abstract NonBlocking<VALUE> finalValue ()
            throws ReturnNeverNull.Violation;

        public abstract Expression<VALUE, OUTPUT> async (
                Operation<VALUE, OUTPUT> callback
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        public abstract NonBlocking<VALUE> await (
                BigDecimal timeout_in_seconds
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        // Start/stop/pause/resume whatever is generating this
        // Result, e.g. a ResultServer etc.
        // Maybe these should be in an interface Operable or something...
        public abstract Maybe<OperatingState> start ();
        public abstract Maybe<OperatingState> pause ();
        public abstract Maybe<OperatingState> resume ();
        public abstract Maybe<OperatingState> stop ();
    }

    public class AbstractMessenger
        implements Runnable // maybe Future etc too...?
    {
        private final Serializable lock = new String ( "lock" );
        private final Actor actor;
        private final BigDecimal timeoutInSeconds;
        private final Clock clock;

        private OperatingState state = OperatingState.NONE;
        private final List<Message> messageQueue =
            new ArrayList<Message> ();

        public ResultServer (
                Actor actor,
                BigDecimal timeout_in_seconds,
                Clock clock
                )
        {
            this.actor = actor;
            this.timeoutInSeconds = timeout_in_seconds;
            this.clock = clock;
        }

        public void start ()
        {
            ...send ourselves a message with the START capability...;
        }

        public void pause ()
        {
            ...send ourselves a message with the PAUSE capability...;
        }

        public void resume ()
        {
            ...send ourselves a message with the RESUME capability...;
        }

        public void stop ()
        {
            ...send ourselves a message with the STOP capability...;
        }

        public final OperatingState state ()
        {
            synchronized ( this.lock )
            {
                return this.state;
            }
        }

        private final OperatingState changeState (
                OperatingState new_state
                )
        {
            final Maybe<OperatingState> ok_new_state;
            synchronized ( this.lock )
            {
                ok_new_state = this.handleStateChange ( new_state );
                if ( ok_new_state.orNull () != null )
                {
                    this.state = ok_new_state;
                }
            }

            return ok_new_state;
        }

        public void run ()
        {
            this.receiveRepeatedly ( this.timeoutInSeconds,
                                     true ); // is_stop_on_timeout
        }

        protected void receiveRepeatedly (
                BigDecimal timeout_in_seconds,
                boolean is_stop_on_timeout
                )
        {
            final BigDecimal start_time =
                this.clock.currentTimeInSeconds ();
            final BigDecimal end_time =
                start_time.add ( timeout_in_seconds );
            // Someone has to send a STOP message to get us to stop.
            while ( this.state () != OperatingState.STOPPED )
            {
                final BigDecimal remaining_timeout_in_seconds;
                if ( this.timeoutInSeconds.compareTo ( BigDecimal.ZERO ) <= 0 )
                {
                    remaining_timeout_in_seconds = BigDecimal.ZERO;
                }
                else
                {
                    final BigDecimal current_time =
                        this.clock.currentTimeInSeconds ();
                    remaining_timeout_in_seconds = end_time.subtract ( current_time );
                    if ( is_stop_on_timeout
                         && remaining_timeout_in_seconds.compareTo ( BigDecimal.ZERO ) <= 0 )
                    {
                        this.changeState ( OperatingState.STOPPED );
                        break;
                    }
                }

                this.receive ( remaining_timeout_in_seconds );
            }
        }

        protected void receive (
                                BigDecimal timeout_in_seconds
                                )
        {
            for ( Message message : this.actor.receive ( remaining_timeout_in_seconds ) )
            {
                final Capability capability =
                    message.body ().capability ();
                if ( capability instanceof OperatingCapability )
                {
                    final OperatingCapability operating_capability =
                        (OperatingCapability) capability;
                    final OperatingState new_state =
                        operating_capability.operatingState ();
                    final OperatingState old_state;
                    synchronized ( this.lock )
                    {
                        old_state = this.state;
                        this.state = this.changeState ( new_state );
                    }

                    if ( new_state == OperatingState.STARTED )
                    {
                        final List<Message> queue;
                        synchronized ( this.lock )
                        {
                            queue = new ArrayList<Message> ( this.messageQueue );
                            this.messageQueue.clear ();
                        }

                        for ( Message queued_message : queue )
                        {
                            this.handleMessage ( queued_message );
                        }
                    }
                }
                else
                {
                    synchronized ( this.lock )
                    {
                        if ( this.state != OperatingState.STARTED )
                        {
                            // Paused or not even started yet.
                            this.messageQueue.add ( message );
                            continue;
                        }
                    }

                    this.handleMessage ( message );
                }
            }
        }

        protected abstract void handleMessage ( Message message );
        protected abstract Maybe<OperatingState> handleStateChange ( OperatingState new_state );

        protected final Actor actor () { return this.actor; }
        protected final BigDecimal timeoutInSeconds () { return ...;  }
        protected final Clock clock () { return ...;  }
    }


    public class ResultServer<VALUE>
        extends AbstractMessenger
    {
        public static final Capability ADD_CLIENT = ...;
        public static final Capability REMOVE_CLIENT = ...;
        private final Serializable lock = new String ( "lock" );
        private final Type<VALUE> resultType;

        private final LinkedHashSet<Serializable> clientIDs = ...;
        private NonBlocking<VALUE> partialValue = null;
        private NonBlocking<VALUE> finalValue = null;

        public ResultServer (
                Actor actor,
                BigDecimal timeout_in_seconds,
                Clock clock,
                Type<VALUE> result_type
                )
        {
            this ( actor,
                   timeout_in_seconds,
                   clock,
                   result_type,
                   new No<VALUE> ( ... ) );
        }

        public ResultServer (
                Actor actor,
                BigDecimal timeout_in_seconds,
                Clock clock,
                Type<VALUE> result_type,
                NonBlocking<VALUE> initial_partial_value
                )
        {
            super ( actor, timeout_in_seconds, clock );

            this.resultType = result_type;
            this.partialValue = initial_partial_value;
            this.finalValue = null;
        }

        private ResultServer<VALUE> addClient (
                Serializable client_id
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            final NonBlocking<VALUE> final_value;
            final NonBlocking<VALUE> partial_value;
            synchronized ( this.lock )
            {
                this.clientIDs.add ( client_id );

                final_value = this.finalValue;
                partial_value = this.partialValue;
            }

            if ( final_value == null )
            {
                this.sendValue ( Result.UPDATE_PARTIAL_VALUE, partial_value );
            }
            else
            {
                this.sendValue ( Result.SET_FINAL_VALUE, final_value );
            }
        }

        private ResultServer<VALUE> removeClient (
                Serializable client_id
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            synchronized ( this.lock )
            {
                this.clientIDs.remove ( client_id );
            }
        }

        private ResultServer<VALUE> sendValue (
                Capability capability,
                NonBlocking<VALUE> value
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            final BigDecimal timeout_in_seconds =
                this.actor ().defaultTiimeoutInSeconds ();
            final Value<?> header = this.header ( capability,
                                                  final_value );
            final Value<?> footer = this.footer ( capability,
                                                  final_value );
            final LinkedHashSet<Serializable> client_ids;
            synchronized ( this.lock )
            {
                client_ids = new LinkedHashSet<Serializable> ( this.clientIDs );
            }
            final ValueBuilder<Message> messages_builder =
                new ValueBuilder<Message> ( this.actor ().messageType ( capability ) );
            for ( Serializable client_id : client_iDs )
            {
                final Message message =
                    this.actor ().message ( client_id,
                                            timeout_in_seconds,
                                            header,
                                            capability,
                                            value,
                                            footer );
                messages_builder.add ( message );
                ...do some checks, return error if e.g. client id unknown...;
            }

            final Value<Message> send_result = this.actor ().send ( messages );

            if ( capability == Result.SET_FINAL_VALUE )
            {
                synchronized ( this.lock )
                {
                    // Even if the send failed, remove the client anyway.
                    // The Actor is responsible for trying again, if desired.
                    this.clientIDs.removeAll ( client_ids );
                }
            }

            ...return error if necessary otherwise just return value...;
        }

        /** Final value can only be set locally, NOT via actor messaging. */
        public ResultServer<VALUE> setFinalValue (
                NonBlocking<VALUE> final_value
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            ...error if already set...;

            synchronized ( this.lock )
            {
                this.finalValue = final_value;
            }

            final ResultServer<VALUE> result =
                this.sendValue ( Result.SET_FINAL_VALUE, final_value );

            // Now stop ourselves.
            this.stop ();

            return result;
        }

        /** Partial value can only be updated locally, NOT via actor messaging. */
        public ResultServer<VALUE> setPartialValue (
                NonBlocking<VALUE> partial_value
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            synchronized ( this.lock )
            {
                this.partialValue = partial_value;
            }

            return this.sendValue ( Result.UPDATE_PARTIAL_VALUE, partial_value );
        }

        // Can be overridden:
        private Value<?> header ( Capability capability, Value<VALUE> value )
        {
            return new No<...> ( ... );
        }

        // Can be overridden:
        private Value<?> footer ( Capability capability, Value<VALUE> value )
        {
            return new No<...> ( ... );
        }

        public boolean isComplete ()
        {
            synchronized ( this.lock )
            {
                if ( this.finalValue == null )
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        }

        @Override
        protected void handleMessage ( Message message )
        {
            ... request final result partial result add client remove client...;
        }

        @Override
        protected Maybe<OperatingState> handleStateChange ( OperatingState new_state )
        {
            return new One<OperatingState> ( ... new_state ... );
        }
    }


    public class ResultClient<VALUE>
        extends AbstractMessenger
        implements Result<VALUE>
    {
        private final Serializable lock = new String ( "lock" );
        private final Serializable serverID;
        private final Type<VALUE> resultType;

        private final LinkedHashSet<Expression<VALUE, ?>> expressions =
            new LinkedHashSet<Expression<VALUE, ?>> ();

        private NonBlocking<VALUE> partialValue = null;
        private NonBlocking<VALUE> finalValue = null;

        public ResultClient (
                Actor actor,
                BigDecimal timeout_in_seconds,
                Clock clock,
                Serializable server_id,
                Type<VALUE> result_type
                )
        {
            this ( actor,
                   timeout_in_seconds,
                   clock,
                   server_id,
                   result_type,
                   new No<VALUE> ( ... ) );
        }

        public ResultClient (
                Actor actor,
                BigDecimal timeout_in_seconds,
                Clock clock,
                Serializable server_id,
                Type<VALUE> result_type,
                NonBlocking<VALUE> initial_partial_value
                )
        {
            super ( actor,
                    timeout_in_seconds,
                    clock );

            this.serverID = server_id;
            this.resultType = result_type;
            this.partialValue = initial_partial_value;
            this.finalValue = null;
        }

        public final NonBlocking<VALUE> partialValue ()
            throws ReturnNeverNull.Violation
        {
            // First handle any queued up messages.
            this.receive ( BigDecimal.ZERO );
            synchronized ( this.lock )
            {
                return this.partialValue;
            }
        }

        public final NonBlocking<VALUE> finalValue ()
            throws ReturnNeverNull.Violation
        {
            // First handle any queued up messages.
            this.receive ( BigDecimal.ZERO );
            synchronized ( this.lock )
            {
                if ( this.finalValue == null )
                {
                    return ...either original Blocking or No value or...;
                }
                else
                {
                    return this.finalValue;
                }
            }
        }

        // Can be overridden:
        private Value<?> header ( Capability capability )
        {
            return new No<...> ( ... );
        }

        // Can be overridden:
        private Value<?> body ( Capability capability )
        {
            return new No<...> ( ... );
        }

        // Can be overridden:
        private Value<?> footer ( Capability capability )
        {
            return new No<...> ( ... );
        }

        public final Value<OUTPUT> async (
                Operation<VALUE, OUTPUT> callback
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            final Value<VALUE> final_value;
            final Expression<VALUE, OUTPUT> expression;
            synchronized ( this.lock )
            {
                final_value = this.finalValue;
                if ( final_value == null )
                {
                    // Still waiting.
                    expression =
                        new Expression<VALUE, OUTPUT> (
                            !!!blockingvalue or something???!!!, // input
                            operation.outputType (),
                            operation );
                    this.expressions.add ( expression );
                }
                else
                {
                    expression = null;
                }
            }

            if ( final_value == null )
            {
                return expression;
            }

            final Value<OUTPUT> output = operation.apply ( final_value );
            return output;
        }

        public final NonBlocking<VALUE> await (
                BigDecimal timeout_in_seconds
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            // Blocks until timeout or final result:
            this.receiveRepeatedly ( timeout_in_seconds,
                                     false );
            return this.finalValue ();
        }

        public final Maybe<OperatingState> start ()
        {
            final Message to_server =
                this.actor ().message ( this.serverID,
                                        this.timeoutInSeconds (),
                                        this.header ( OperatingCapability.START ),
                                        OperatingCapability.START,
                                        this.body ( OperatingCapability.START ),
                                        this.footer ( OperatingCapability.START ) );
            final Message to_me =
                this.actor ().message ( this.actor.id (),
                                        this.timeoutInSeconds (),
                                        this.header ( OperatingCapability.START ),
                                        OperatingCapability.START,
                                        this.body ( OperatingCapability.START ),
                                        this.footer ( OperatingCapability.START ) );
            final Value<Message> messages =
                new ValueBuilder<Message> ( to_server, to_me ).build ();
            final Value<Message> sent = this.actor ().send ( messages );
            ...return either success or failure...;
        }

        public final Maybe<OperatingState> pause ()
        {
            ...send server and us messages with the PAUSE capability...;
        }

        public final Maybe<OperatingState> resume ()
        {
            ...send server and us messages with the RESUME capability...;
        }

        public final Maybe<OperatingState> stop ()
        {
            ...send server and us messages with the STOP capability...;
        }

        @Override
        protected void handleMessage ( Message message )
        {
            final Capability capability = message.body ().capability ();
            final Value<?> value = message.body ().content ();
            if ( capability == Result.SET_FINAL_VALUE )
            {
                boolean is_complete = false;
                try
                {
                    synchronized ( this.lock )
                    {
                        if ( this.finalValue != null )
                        {
                            ...error...;
                        }
                        else
                        {
                            this.finalValue = (Value<VALUE>) value;
                            is_complete = true;
                        }
                    }
                }
                catch { ...handle class cast exception...; }

                if ( is_complete )
                {
                    ...send STOP to self only, not to server...;
                }
            }
            else if ( capability == Result.UPDATE_PARTIAL_VALUE )
            {
                ...same kind of thing, but simpler thsn finalValue...;
            }
            else error...;
        }

        @Override
        protected Maybe<OperatingState> handleStateChange ( OperatingState new_state )
        {
            return new One<OperatingState> ( ... new_state ... );
        }
    }






    !!!!!!!!!!!;
        {
                final Operation<?, ?> implementation =
                    local_actor.implementations.get ( capability );
                if ( implementation == null ) { ... error ... }
                final Value<?> input = request.body ().content ();
                ...casts and guards against ? mismatches...;
            final Value<?> output = implementation.apply ( input );
            final Message response =
                to_actor.message ( actor.id (),
                                   request.header ().timeoutInSeconds (),
                                   request.header ().content (),
                                   request.body ().capability (),
                                   request.body ().content (),
                                   request.footer ().content () );
        }
    }

    ResultServer extends Result, AutoCloseable;
    --> ResultServer:;
    public abstract ResultServer<VALUE> setFinalValue (
            NonBlocking<VALUE> final_value
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
    --> ResultServer:;
    public abstract ResultServer<VALUE> setPartialValue (
            NonBlocking<VALUE> partial_value
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
    --> ResultServer:;
    public abstract ResultClient<VALUE> connect ()
        throws IllegalStateException,
               IOException,
               RemoteException,
               ReturnNeverNull.Violation;
    --> ResultServer:;
    public abstract void disconnect (
            ResultClient<VALUE> result_client
            )
        throws IllegalArgumentException,
               IllegalStateException,
               IOException,
               RemoteException,
               ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;



    --> Result:;
    public abstract NonBlocking<VALUE> partialValue ()
        throws ReturnNeverNull.Violation;

    public abstract NonBlocking<VALUE> finalValue ()
        throws ReturnNeverNull.Violation;

    public abstract OperatingState state ()
        throws ReturnNeverNull.Violation;

    public abstract Maybe<OperatingState> changeState (
            OperatingState operating_state
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    ResultClient extends Result;
    --> ResultClient:;
    public abstract ResultClient<VALUE> async (
                ResultServer<VALUE> callback
                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;

    !!!;

    /**
     * <p>
     * Subscribes the specified Expression to this Result, so that its
     * <code> completed () </code> method will be kicked when this Result
     * is finished.
     * </p>
     *
     * <p>
     * If the result has already been asynchronously set, then
     * the specified Expression's <code> completed () </code> method
     * will be invoked immediately, in this thread.
     * </p>
     *
     * @see musaico.foundation.value.incomplete.CompletionPossible#completed()
     *
     * @param expression The Expression whose <code> completed () </code>
     *                   method will be invoked when this Result has a
     *                   final answer.  Must not be null.
     */
    public abstract <OUTPUT extends Object>
        void async (
                    Expression<VALUE, OUTPUT> expression
                    )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Blocks the caller until either the asynchronous result is
     * retrieved and returned, or until the asynchronous result times
     * out, whichever happens first.
     * </p>
     *
     * <p>
     * The timeout occurs after <code> min ( this.maxTimeoutInSeconds,
     * timeut_in_seconds ) </code>.  So even if the parameter timeout
     * is specified as, say, 2 minutes, this Result's
     * maximum timeout might be only, say, 10 seconds.  In such a case,
     * the timeout would occur after 10 seconds.
     * </p>
     *
     * <p>
     * If this blocking call times out, then a Timeout failed result
     * is returned.
     * </p>
     *
     * <p>
     * If the current Thread is interrupted, then a Cancelled
     * result is returned, but the final result is NOT set.
     * </p>
     *
     * <p>
     * If the asynchronous result is received before timing out, then
     * the result could be anything -- a Successful result, a
     * FailedResult, a PartialResult, and so on.
     * </p>
     *
     * @param blocking_value The Blocking Value which is waiting on this
     *                       asynchronous result.  Must not be null.
     *
     * @param timeout_in_seconds The maximum amount of time the caller
     *                           is willing to wait for the blocking
     *                           call, in seconds.  0L can be passed
     *                           to not block at all, just immediately
     *                           return the final result or No result
     *                           if incomplete.  Must not be null.
     *                           Must be greater than or equal to
     *                           BigDecimal.ZERO.
     */
    public abstract NonBlocking<VALUE> await (
            Blocking<VALUE> blocking_value,
            BigDecimal timeout_in_seconds
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation;


    /**
     * @return True if a partial result is available, even if the
     *         final result is not; false if no partial value has been
     *         set.
     */
    public abstract boolean hasPartialResult ();


    /**
     * @return True if this Result has finished blocking
     *         and has a result; false if it is still blocking.
     */
    public abstract boolean isFinishedBlocking ();


    /**
     * @return The maximum amount of time, in seconds, that this
     *         Result will take, before giving up await ().
     *         If this Result is already complete, then
     *         0L will be returned.  Otherwise the default maximum time.
     *         This default maximum time is specified by the creator of
     *         the Result to be the maximum amount of time
     *         the process will take to return a result, regardless
     *         of whether the await () caller is willing to block
     *         for more or less time.  For instance, an Result
     *         waiting on a read from a disk might guarantee a
     *         result within 10 seconds, whereas an Result
     *         waiting on a result from the network might guarantee
     *         a result within 2 minutes.  The caller to the await ()
     *         method might be willing to wait 30 seconds, but in the
     *         first case the overall maximum 10 seconds will be the
     *         limiting factor before a failed result is returned,
     *         and in the second case the Blocking Value is not
     *         guaranteed to be complete by the implementer after
     *         the 30 second timeout, but after 30 seconds the caller
     *         will be given a failed result anyway.  Never null.
     *         Always greater than BigDecimal.ZERO seconds.
     */
    public abstract BigDecimal maxTimeoutInSeconds ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * If either the final result or a partial result is available,
     * returns that value.  Otherwise returns No value.
     * </p>
     *
     * @param violation The ValueViolation to use if no partial result
     *                  is available.  Must not be null.
     *
     * @return The final result, if it is available.  Or a partial
     *         result, if the asynchronous worker has provided one.
     *         Otherwise No value.  Never null.
     */
    public abstract NonBlocking<VALUE> partialResult (
            ValueViolation violation
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Removes the specified Expression from those which will operate on
     * this Result when it is eventually set.
     * </p>
     *
     * @param expression The Expression which will no longer operate on this
     *                   asynchronous result when the time comes.
     *                   Must not be null.
     */
    public abstract <OUTPUT extends Object>
        void removeExpression (
                               Expression<VALUE, OUTPUT> expression
                               );


    /**
     * <p>
     * Sets the final asynchronous result.
     * </p>
     *
     * <p>
     * The first time this method is called, the result is stored,
     * any expressions are notified, and the specified final
     * result is returned.
     * </p>
     *
     * <p>
     * Upon any subsequent call, nothing happens, and the original
     * final result is returned.
     * </p>
     *
     * @param final_resut The final asynchronous result.  Must not be null.
     *
     * @return The final asynchronous result.  The specified final result
     *         is returned unless the final result has already been
     *         set previously.  Never null.
     */
    public abstract NonBlocking<VALUE> setFinalResult (
            NonBlocking<VALUE> final_result
            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Sets the current partial asynchronous result.
     * </p>
     *
     * <p>
     * The partial result can be updated zero or more times before the
     * final result is set.
     * </p>
     *
     * <p>
     * Each time a partial result is set,
     * <code> progress () </code> is invoked on each expression
     * waiting on this Result.
     * </p>
     *
     * <p>
     * If the final result is already set, then no processing occurs, and
     * the final result is returned.
     * </p>
     *
     * <p>
     * THIS IS A BLOCKING CALL.  The Expressions could conceivably
     * block indefinitely.  Use with caution!
     * </p>
     *
     * @param partial_resut The current partial asynchronous result.
     *                      Must not be null.
     *
     * @return The partial asynchronous result, unless the final result
     *         has already been set, in which case it is returned.
     *         Never null.
     */
    public abstract NonBlocking<VALUE> setPartialResult (
            NonBlocking<VALUE> partial_result
            )
        throws ParametersMustNotBeNull.Violation;


    // Every Result must implement toString ().


    /**
     * @return The type of this asynchronous result.
     *         Never null.
     */
    public abstract Type<VALUE> type ();
}
