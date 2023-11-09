package musaico.foundation.typing;

import java.io.Serializable;

import java.util.Map;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts; 

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.metadata.Origin;


/**
 * <p>
 * A Contract on Terms which does not permit Unknowns.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TermMustNotBeUnknown
    implements Contract<Term<?>, TermMustNotBeUnknown.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton TermMustNotBeUnknown contract. */
    public static final TermMustNotBeUnknown CONTRACT =
        new TermMustNotBeUnknown ();


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TermMustNotBeUnknown.class );


    public static class Violation
        extends TypingViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TermMustNotBeUnknown.serialVersionUID;

        /**
         * <p>
         * Creates a new TermMustNotBeUnknown.Violation for some typing contract
         * that was breached.
         * </p>
         */
        public Violation (
                          Contract<?, ?> contract,
                          Serializable plaintiff,
                          Object value
                          )
        {
            this ( contract, plaintiff, value, null );
        }

        /**
         * <p>
         * Creates a new Violation for some typing contract
         * that was breached.
         * </p>
         */
        public Violation (
                          Contract<?, ?> contract,
                          Serializable plaintiff,
                          Object value,
                          Throwable cause
                          )
        {
            super ( contract,
                    plaintiff,
                    Contracts.makeSerializable ( value ) );

            if ( cause != null )
            {
                this.initCause ( cause );
            }
        }
    }


    /**
     * <p>
     * Only constructed by TermMustNotBeUnknown.CONTRACT.
     * </p>
     */
    private TermMustNotBeUnknown ()
    {
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
            // Any TermMustNotBeUnknown != null.
            return false;
        }
        else if ( object == this )
        {
            // Any TermMustNotBeUnknown == itself.
            return true;
        }
        else if ( ! ( object instanceof TermMustNotBeUnknown ) )
        {
            // Any TermMustNotBeUnknown != any other object.
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Term<?> term
                               )
    {
        if ( term == null )
        {
            // Filter out nulls.
            return FilterState.DISCARDED;
        }
        else if ( term instanceof Unknown )
        {
            // Filter out Unknowns.
            return FilterState.DISCARDED;
        }
        else
        {
            // Keep all other Terms.
            return FilterState.KEPT;
        }
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
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    public final TermMustNotBeUnknown.Violation violation (
                                                           Object plaintiff,
                                                           Term<?> inspectable_data,
                                                           Throwable cause
                                                           )
    {
        final TermMustNotBeUnknown.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    @Override
    public TermMustNotBeUnknown.Violation violation (
                                                     Object plaintiff,
                                                     Term<?> inspectable_data
                                                     )
    {
        final Serializable serialized =
            Contracts.makeSerializable ( inspectable_data );

        return new TermMustNotBeUnknown.Violation ( this,
                                                    Contracts.makeSerializable ( plaintiff ),
                                                    serialized );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( this.getClass () );
    }
}
