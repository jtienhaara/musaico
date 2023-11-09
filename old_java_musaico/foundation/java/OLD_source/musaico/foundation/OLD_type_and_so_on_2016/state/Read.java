package musaico.foundation.state;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.EqualTo;
import musaico.foundation.domains.NotEqualTo;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.Filter;

import musaico.foundation.filter.composite.Or;

import musaico.foundation.graph.Arc;

import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.contracts.ElementsMustBeInstancesOfClass;
import musaico.foundation.value.contracts.ElementsMustBelongToDomain;
import musaico.foundation.value.contracts.ValueMustEqual;
import musaico.foundation.value.contracts.ValueMustNotEqual;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;


/**
 * <p>
 * A Transition which expects some particular domain of values, consuming
 * every value from a specific tape (such as the input tape)
 * that matches its expectation, and inducing a transition
 * to the next state.
 * </p>
 *
 *
 * <p>
 * In Java every Transition must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class Read
    extends AbstractTransition
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Read.class );

    // The index of the machine to read from.
    private static final int INDEX_READ = 0;

    // The index of the Tape.DEBUG machine.
    private static final int INDEX_DEBUG = 1;


    // A Contract which checks each value, and either induces a transition
    // and consumption of the tape, or returns a Violation of the
    // Contract (which can be used to create No transition ()).
    private final Contract<Value<?>, ?> readContract;


    /**
     * <p>
     * Creates a new Read transition.
     * </p>
     *
     * @param tape The Tape to read from.  For example, Tape.INPUT.
     *             Must not be null.
     *
     * @param enumerated_values One or more constants, any of which
     *                          is accepted as an allowable value when
     *                          the tape is read at runtime.
     *                          Must not be null.  Must not contain
     *                          any null elements.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution DOMAIN... vararg.
    public <DOMAIN extends Object> Read (
            Tape tape,
            DOMAIN ... enumerated_values
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this ( tape,
               createEnumerationContract ( enumerated_values  ) );
    }

    // Turns an array of enumerated values into one Contract.
    // Package-private since other classes in this package use this helper.
    static final <DOMAIN extends Object>
            Contract<Value<?>, ?> createEnumerationContract (
                DOMAIN [] enumerated_values
                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.Length.MustBeGreaterThanZero.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               enumerated_values );
        classContracts.check ( Parameter1.Length.MustBeGreaterThanZero.CONTRACT,
                               enumerated_values );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               enumerated_values );

        if ( enumerated_values.length == 1 )
        {
            final One<DOMAIN> value =
                new One<DOMAIN> (
                    new StandardValueClass<DOMAIN> ( // value_class
                        // WARNING using the first element's getClass ()
                        // could be too specific!  It might be one
                        // specialization of DOMAIN, while the
                        // subsequent values are others.
                        // Simple example: { 0, BigDecimal.ZERO }.
                        enumerated_values [ 0 ].getClass (), // element_class
                        enumerated_values [ 0 ] ),           // none
                    enumerated_values [ 0 ] );
            final ValueMustEqual contract =
                new ValueMustEqual ( value );
            return contract;
        }

        final List<Domain<Object>> enumerated_domains =
            new ArrayList<Domain<Object>> ();
        for ( DOMAIN enumerated_value : enumerated_values )
        {
            final EqualTo domain = new EqualTo ( enumerated_value );
            enumerated_domains.add ( domain );
        }

        final Or<Object> or_enumerated_domains =
            new Or<Object> ( enumerated_domains );

        final ElementsMustBelongToDomain<Object> contract =
            new ElementsMustBelongToDomain<Object> ( or_enumerated_domains );

        return contract;
    }


    /**
     * <p>
     * Creates a new Read transition.
     * </p>
     *
     * @param tape The Tape to read from.  For example, Tape.INPUT.
     *             Must not be null.
     *
     * @param value_domain A Domain which determines whether each
     *                     value induces a transition (kept)
     *                     or No result (discarded).
     *                     Must not be null.
     */
    public <DOMAIN extends Object> Read (
            Tape tape,
            Domain<DOMAIN> value_domain
            )
        throws ParametersMustNotBeNull.Violation
    {
        this ( tape,
               value_domain == null
                   ? null
                   : new ElementsMustBelongToDomain<DOMAIN> (
                         value_domain
                         ) );
    }


    /**
     * <p>
     * Creates a new Read transition.
     * </p>
     *
     * @param tape The Tape to read from.  For example, Tape.INPUT.
     *             Must not be null.
     *
     * @param value_contract A Contract which checks each value, and either
     *                       induces a transition and consumption of
     *                       the value, or returns a Violation of the
     *                       Contract (which can be used to create
     *                       No transition ()).  Must not be null.
     */
    public Read (
                 Tape tape,
                 Contract<Value<?>, ?> value_contract
                 )
        throws ParametersMustNotBeNull.Violation
    {
        super ( tape,
                Tape.DEBUG );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tape, value_contract );

        this.readContract = value_contract;
    }


    /**
     * @see java.lang.AbstractTransition#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( "(" );

        // Don't bother specifying that we're reading from
        // the input tape; only mention any different tape that we
        // read from.
        final Tape read_tape = this.tapes ().orNull (); //Only one, never null.
        if ( read_tape != Tape.INPUT )
        {
            sbuf.append ( "" + read_tape );
            sbuf.append ( ", " );
        }

        // Don't bother outputting all the verbosity of the contract
        // if it's just something like 'Value must equal "X"'.
        if ( this.readContract instanceof ValueMustEqual )
        {
            final ValueMustEqual must_equal =
                (ValueMustEqual) this.readContract;
            final Object equal_object = must_equal.value ();
            if ( equal_object instanceof One )
            {
                final One<?> one = (One<?>) equal_object;
                final Object value = one.orNull ();
                final String value_as_string =
                    StringRepresentation.of  ( value,
                                               0 ); // No max_length
                sbuf.append ( value_as_string );
            }
            else
            {
                sbuf.append ( "" + equal_object );
            }
        }
        // Don't bother outputting all the verbosity of the contract
        // if it's just something like 'Value must NOT equal "X"'.
        else if ( this.readContract instanceof ValueMustNotEqual )
        {
            final ValueMustNotEqual must_not_equal =
                (ValueMustNotEqual) this.readContract;
            final Object not_equal_object = must_not_equal.value ();
            if ( not_equal_object instanceof One )
            {
                final One<?> one = (One<?>) not_equal_object;
                final Object value = one.orNull ();
                final String value_as_string =
                    StringRepresentation.of ( value,
                                              0 ); // No max_length
                sbuf.append ( "! " );
                sbuf.append ( value_as_string );
            }
            else
            {
                sbuf.append ( "! " + not_equal_object );
            }
        }
        // Don't bother outputting all the verbosity of the contract
        // if it's just something like 'Value must be one of (X, Y, Z)',
        // or if it's something like 'value must belong to domain D'.
        else if ( this.readContract instanceof ElementsMustBelongToDomain )
        {
            final ElementsMustBelongToDomain<?> domain_contract =
                (ElementsMustBelongToDomain<?>) this.readContract;
            final Domain<?> domain = domain_contract.domain ();
            boolean is_or_string = false;
            final StringBuilder or_buf = new StringBuilder ();
            if ( domain instanceof Or )
            {
                final Or<?> or_domains = (Or<?>) domain;
                is_or_string = true;
                for ( Filter<?> maybe_domain : or_domains.filters () )
                {
                    if ( maybe_domain instanceof EqualTo )
                    {
                        final EqualTo equal_to =
                            (EqualTo) maybe_domain;
                        final Object equal_object = equal_to.value ();
                        if ( equal_object instanceof One )
                        {
                            final One<?> one = (One<?>) equal_object;
                            final Object value = one.orNull ();
                            final String value_as_string =
                                StringRepresentation.of (
                                    value,
                                    0 ); // No max_length
                            sbuf.append ( value_as_string );
                        }
                        else
                        {
                            sbuf.append ( "" + equal_object );
                        }
                    }
                    else if ( maybe_domain instanceof NotEqualTo )
                    {
                        final NotEqualTo not_equal_to =
                            (NotEqualTo) maybe_domain;
                        final Object not_equal_object = not_equal_to.value ();
                        if ( not_equal_object instanceof One )
                        {
                            final One<?> one = (One<?>) not_equal_object;
                            final Object value = one.orNull ();
                            final String value_as_string =
                                StringRepresentation.of (
                                    value,
                                    0 ); // No max_length
                            sbuf.append ( value_as_string );
                        }
                        else
                        {
                            sbuf.append ( "" + not_equal_object );
                        }
                    }
                    else
                    {
                        is_or_string = false;
                        break;
                    }
                }
            }

            if ( is_or_string )
            {
                sbuf.append ( or_buf );
            }
            else
            {
                sbuf.append ( "" + domain );
            }
        }
        // Don't bother outputting all the verbosity of the contract
        // if it's just something like 'Value must be an instanceof class C'.
        else if ( this.readContract instanceof ElementsMustBeInstancesOfClass )
        {
            final ElementsMustBeInstancesOfClass domain_contract =
                (ElementsMustBeInstancesOfClass) this.readContract;
            sbuf.append ( ClassName.of ( domain_contract.domainClass () ) );
        }
        else
        {
            sbuf.append ( "" + this.readContract );
        }

        sbuf.append ( ")" );

        return sbuf.toString ();
    }


    /**
     * @see musaico.foundation.tape.Transition#transition(musaico.foundation.tape.TapeMachine...)
     */
    @Override
    public final boolean transition (
                                     TapeMachine ... tape_machines
                                     )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  (Object) tape_machines );
        this.contracts ().check ( Parameter1.MustContainNoNulls.CONTRACT,
                                  tape_machines );

        // We have exactly one Tape, so one tape machine.
        final TapeMachine input = tape_machines [ INDEX_READ ];

        if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
        {
            this.debug ( tape_machines [ INDEX_DEBUG ],
                         new Date (),
                         "  " + this + " Begin (" + input + ")" );
        }

        final Arc<Value<?>, Transition> input_arc =
            Read.read ( input,
                        this.readContract,
                        tape_machines [ INDEX_DEBUG ] );

        if ( input_arc == null )
        {
            if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ INDEX_DEBUG ],
                             new Date (),
                             "  " + this + " Fail" );
            }

            // None of the input options worked.
            return false;
        }
        else
        {
            // Successful transition.
            // Found a valid input.
            // Force the input machine into a new state, ignoring
            // its transition arcs since we're just treating it
            // as a data structure.
            input.forceState ( input_arc.to () );

            if ( tape_machines [ INDEX_DEBUG ] != TapeMachine.NONE )
            {
                this.debug ( tape_machines [ INDEX_DEBUG ],
                             new Date (),
                             "  " + this + " Success ("
                             + input_arc.to () + ")" );
            }

            return true;
        }
    }


    /**
     * <p>
     * Reads one value from the specified tape machine.
     *
     * <p>
     * No checking of parmeters or return values is done.
     * </p>
     */
    protected static final Arc<Value<?>, Transition> read (
            TapeMachine input,
            Contract<Value<?>, ?> read_contract,
            TapeMachine debug
            )
    {
        final Value<?> input_state = input.state ();
        final Value<Arc<Value<?>, Transition>> input_options =
            input.graph ().arcs ( input_state );
        final Value<?> input_end_state = input.graph ().exit ();

        if ( debug != TapeMachine.NONE )
        {
            final String maybe_read_contract;
            if ( read_contract == null )
            {
                maybe_read_contract = "";
            }
            else
            {
                maybe_read_contract = " (contract: "
                    + read_contract.description ()
                    + ")";
            }

            AbstractTransition.debugMessage ( debug,
                                              new Date (),
                                              "  Read.read () Begin"
                                              + maybe_read_contract );
        }

        // If the input machine has more than 1 arc out of its
        // current state, then we try the value at the other end
        // of each arc, until we find an input option that works.
        for ( Arc<Value<?>, Transition> input_arc : input_options )
        {
            final Value<?> input_option = input_arc.to ();

            if ( debug != TapeMachine.NONE )
            {
                AbstractTransition.debugMessage ( debug,
                                                  new Date (),
                                                  "  Read.read ()"
                                                  + " input option = "
                                                  + input_option );
            }

            if ( input_option == input_end_state )
            {
                // This option would be the end of the input.
                // But we need one input.
                // So carry on to the next option.
                continue;
            }

            // Does the input match this Transition's requirement?
            if ( read_contract == null
                 || read_contract.filter ( input_option ).isKept () )
            {
                // Successful transition.
                if ( debug != TapeMachine.NONE )
                {
                    AbstractTransition.debugMessage ( debug,
                                                      new Date (),
                                                      "  Read.read ()"
                                                      + " Success" );
                }

                // Found a valid input.
                return input_arc;
            }
        }

        // None of the input options worked.
        if ( debug != TapeMachine.NONE )
        {
            AbstractTransition.debugMessage ( debug,
                                              new Date (),
                                              "  Read.read () Fail" );
        }

        return null;
    }
}
