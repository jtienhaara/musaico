package musaico.foundation.state;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Maybe;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * A guarantee that each Transition is executable in Java (or, to
 * put it more precisely, is not a NonJavaTransition).
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
 * @see musaico.foundation.machine.MODULE#COPYRIGHT
 * @see musaico.foundation.machine.MODULE#LICENSE
 */
public class TransitionMustBeExecutableInJava
    implements Contract<Transition, TransitionMustBeExecutableInJava.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TransitionMustBeExecutableInJava.serialVersionUID;


        /**
         * <p>
         * Creates a new TransitionMustBeExecutableInJava.Violation.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                  violated.  Must not be null.
         *
         * @param evidence The input which violated the contract.
         *                    Must not be null.
         */
        public Violation (
                Contract<Transition, TransitionMustBeExecutableInJava.Violation> contract,
                Object plaintiff,
                Transition evidence
                )
        {
            super ( contract,
                    "The Transition is not executable"
                    + " in Java.", // description
                    plaintiff,
                    evidence );
        }
    }




    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( TransitionMustBeExecutableInJava.class );


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "Each Transition must be executable in Java.";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Transition transition
                               )
    {
        if ( transition == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( transition instanceof NonJavaTransition )
        {
            return FilterState.DISCARDED;
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public TransitionMustBeExecutableInJava.Violation violation (
            Object plaintiff,
            Transition evidence
            )
    {
        return new TransitionMustBeExecutableInJava.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * <p>
     * Helper method.  Always passes this TransitionMustBeExecutableInJava
     * contract as the first parameter to the full method, and sets
     * the specified initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.machine.TransitionMustBeExecutableInJava#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public TransitionMustBeExecutableInJava.Violation violation (
            Object plaintiff,
            Transition evidence,
            Throwable cause
            )
    {
        final TransitionMustBeExecutableInJava.Violation violation =
            this.violation ( plaintiff,
                             evidence );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
