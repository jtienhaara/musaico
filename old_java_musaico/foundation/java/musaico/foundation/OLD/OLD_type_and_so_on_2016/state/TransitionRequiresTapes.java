package musaico.foundation.state;

import java.io.Serializable;

import java.util.Collection;
import java.util.Map;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.FilterState;

import musaico.foundation.graph.Arc;
import musaico.foundation.graph.Graph;
import musaico.foundation.graph.NodeMustBeInGraph;
import musaico.foundation.graph.StandardGraph;

import musaico.foundation.machine.AbstractMachine;
import musaico.foundation.machine.Machine;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Just;
import musaico.foundation.value.Maybe;
import musaico.foundation.value.NotOne;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;

import musaico.foundation.value.normal.Normal;


/**
 * <p>
 * A contract requiring TapeMachines corresponding to the Tapes
 * required by a TapeMachine or one of its Transitions.
 * </p>
 *
 * <p>
 * In order to transition through the states of a TapeMachine
 * <code> machine </code>, a TapeMachine for each Tape returnerd by 
 * <code> machine.tapes () </code> must be provided, in order.
 * </p>
 *
 * <p>
 * For example, a TapeMachine might require a Tape.INPUT, to
 * read from, and a Tape.OUTPUT, to write to.  In this case,
 * its <code> tapes () </code> method would return
 * <code> { Tape.INPUT, Tape.OUTPUT } </code>.  Any attempt to change
 * the machine's state would then require two TapeMachines to be
 * passed in, in order:
 * <code> machine.transition ( input_machine, output_machine ) </code>.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must be Serializable in order
 * to play nicely over RMI.
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
public class TransitionRequiresTapes
    implements Contract<TapeMachine[], TransitionRequiresTapes.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( TransitionRequiresTapes.class );


    /** A contract which ensures a a TapeMachine corresponding
     *  to the Tape.INPUT tape is provided to every transition () call. */
    public static final TransitionRequiresTapes INPUT_TAPE_CONTRACT =
        new TransitionRequiresTapes ( Tape.INPUT );

    /** A contract which ensures a a TapeMachine corresponding
     *  to the Tape.OUTPUT tape is provided to every transition () call. */
    public static final TransitionRequiresTapes OUTPUT_TAPE_CONTRACT =
        new TransitionRequiresTapes ( Tape.OUTPUT );


    // The Tape(s) required in order to transition.
    // Every transition attempt requires one TapeMachine corresponding
    // to each Tape.
    private final Tape [] tapes;


    /**
     * <p>
     * Creates a new TransitionRequiresTapes contract.
     * </p>
     *
     * @param tapes The Tape(s) for which corresponding TapeMachines
     *              must be provided, in order, to every transition attempt.
     *              Must not contain any null elements.  Must not be null.
     */
    public TransitionRequiresTapes (
                                    Tape ... tapes
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) tapes );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               tapes );

        this.tapes = new Tape [ tapes.length ];
        System.arraycopy ( tapes, 0,
                           this.tapes, 0, tapes.length );
    }


    /**
     * <p>
     * Creates a new TransitionRequiresTapes contract.
     * </p>
     *
     * @param tapes The Tape(s) for which corresponding TapeMachines
     *              must be provided, in order, to every transition attempt.
     *              Must not contain any null elements.  Must not be null.
     */
    public TransitionRequiresTapes (
                                    Collection<Tape> tapes
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tapes );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               tapes );

        this.tapes = new Tape [ tapes.size () ];
        int t = 0;
        for ( Tape tape : tapes )
        {
            this.tapes [ t ] = tape;
            t ++;
        }
    }


    /**
     * <p>
     * Creates a new TransitionRequiresTapes contract.
     * </p>
     *
     * @param tapes The Tape(s) for which corresponding TapeMachines
     *              must be provided, in order, to every transition attempt.
     *              Must not contain any null elements.  Must not be null.
     */
    public TransitionRequiresTapes (
                                    Countable<Tape> tapes
                                    )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               tapes );

        this.tapes = new Tape [ (int) tapes.length () ];
        int t = 0;
        for ( Tape tape : tapes )
        {
            this.tapes [ t ] = tape;
            t ++;
        }
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        final String tapes_string = this.tapesString ();

        return "Every transition attempt must provide TapeMachines"
            + " corresponding to the Tapes required"
            + " (" + tapes_string + "),"
            + " in order, so that the tape machines can be read from"
            + " and/or written to by the transition process.";
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final TransitionRequiresTapes that =
            (TransitionRequiresTapes) object;

        if ( this.tapes.length != that.tapes.length )
        {
            return false;
        }

        for ( int t = 0; t < this.tapes.length; t ++ )
        {
            if ( ! this.tapes [ t ].equals ( that.tapes [ t ] ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               TapeMachine [] tape_machines
                               )
    {
        if ( tape_machines == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( tape_machines.length != this.tapes.length )
        {
            return FilterState.DISCARDED;
        }

        for ( TapeMachine tape_machine : tape_machines )
        {
            if ( tape_machine == null )
            {
                return FilterState.DISCARDED;
            }

        }

        return FilterState.KEPT;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int tapes_hash_code = 0;
        for ( Tape tape : this.tapes )
        {
            tapes_hash_code += tape.hashCode ();
        }

        return tapes_hash_code
            + 17 * ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @return The Tape(s) required by this contract.  Never null.
     */
    public final Tape [] tapes ()
        throws ReturnNeverNull.Violation
    {
        final Tape [] tapes = new Tape [ this.tapes.length ];
        System.arraycopy ( this.tapes, 0,
                           tapes, 0, this.tapes.length );

        return tapes;
    }


    private final String tapesString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        for ( Tape tape : this.tapes )
        {
            if ( is_first )
            {
                sbuf.append ( " " );
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( tape.name () );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + this.tapesString ();
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TransitionRequiresTapes.Violation violation (
            Object plaintiff,
            TapeMachine [] evidence
            )
    {
        return new TransitionRequiresTapes.Violation (
            this,
            plaintiff,
            evidence );
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, musaico.foundation.value.Value, java.lang.Throwable)
     */
    @Override
    public TransitionRequiresTapes.Violation violation (
            Object plaintiff,
            TapeMachine [] evidence,
            Throwable cause
            )
    {
        final TransitionRequiresTapes.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }

    /**
     * <p>
     * A violation of the TransitionRequiresTapes contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TransitionRequiresTapes.serialVersionUID;

        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          TapeMachine [] evidence
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this ( contract,
                   plaintiff,
                   evidence,
                   null ); // cause
        }

        public Violation (
                          Contract<?, ?> contract,
                          Object plaintiff,
                          TapeMachine [] evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The specified tape machines do not include"
                    + " TapeMachines for all of"
                    + " the required Tapes.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
