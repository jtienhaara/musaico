package musaico.foundation.term.contracts;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;

import musaico.foundation.domains.array.ArrayObject;
import musaico.foundation.domains.array.ExcludesSpecificClasses;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * Each element of each inspected Term must
 * not be of any of a specific set of Classes, or a TermViolation
 * will be generated.
 * </p>
 *
 * @see musaico.foundation.domains.array.ExcludesSpecificClasses
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
 * @see musaico.foundation.term.contracts.MODULE#COPYRIGHT
 * @see musaico.foundation.term.contracts.MODULE#LICENSE
 */
public class CountableMustExcludeSpecificClasses
    implements Contract<Term<?>, CountableMustExcludeSpecificClasses.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces constructor and static method parameter obligations for us.
    private static final Advocate classContracts =
        new Advocate ( CountableMustExcludeSpecificClasses.class );


    // The ExcludesSpecificClasses Domain to which every Term
    // must belong.
    private final ExcludesSpecificClasses domain;


    /**
     * <p>
     * Creates a new CountableMustExcludeSpecificClasses contract.
     * </p>
     *
     * @param excluded_classes The class(es) of which every element
     *                         in each Term must NOT be
     *                         an instance.  Must not be null.
     *                         Must not contain any nulll elements.
     */
    public CountableMustExcludeSpecificClasses (
                                                Class<?> ... excluded_classes
                                                )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        this (
              new ExcludesSpecificClasses ( excluded_classes ) );
    }


    /**
     * <p>
     * Creates a new CountableMustExcludeSpecificClasses contract.
     * </p>
     *
     * @param domain The ExcludesSpecificClasses Domain to which
     *               every inspected Term elements must belong.
     *               Must not be null.
     */
    public CountableMustExcludeSpecificClasses (
            ExcludesSpecificClasses domain
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               domain );

        this.domain = domain;
    }


    /**
     * @see musaico.foundation.domains.array.ExcludesSpecificClasses#classesToString()
     */
    public final String classesToString ()
    {
        return this.domain.classesToString ();
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "Term must not contain any elements of classes { "
            + this.classesToString ()
            + " }";
    }


    /**
     * @return The Domain to which every Term elements must belong.
     *         Never null.
     */
    public final ExcludesSpecificClasses domain ()
    {
        return this.domain;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final CountableMustExcludeSpecificClasses that =
            (CountableMustExcludeSpecificClasses) obj;

        if ( this.domain == null )
        {
            if ( that.domain != null )
            {
                return false;
            }
        }
        else if ( that.domain == null )
        {
            return false;
        }
        else if ( ! this.domain.equals ( that.domain ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Term<?> (which is an
        // Iterable<?>) to Iterable<Object>.
    public final FilterState filter (
            Term<?> term
            )
    {
        if ( term == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( ! ( term instanceof Countable ) )
        {
            // Can't iterate through the term's elements, so fail.
            return FilterState.DISCARDED;
        }

        return this.domain.filter (
            new ArrayObject<Object> ( Object.class,
                                      (Iterable<Object>) term ) );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 31 * ClassName.of ( this.getClass () ).hashCode ()
            + this.domain.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { "
            + this.classesToString ()
            + " }";
    }


    /**
     * @see musaico.foundation.domains.array.ExcludesSpecificClasses#excludedClasses()
     */
    public final Class<?> [] excludedClasses ()
    {
        return this.domain.excludedClasses ();
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public CountableMustExcludeSpecificClasses.Violation violation (
            Object plaintiff,
            Term<?> evidence
            )
    {
        return new CountableMustExcludeSpecificClasses.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public CountableMustExcludeSpecificClasses.Violation violation (
            Object plaintiff,
            Term<?> evidence,
            Throwable cause
            )
    {
        final CountableMustExcludeSpecificClasses.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the CountableMustExcludeSpecificClasses contract.
     * </p>
     */
    public static class Violation
        extends TermViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            CountableMustExcludeSpecificClasses.serialVersionUID;


        /**
         * <p>
         * Creates a new CountableMustExcludeSpecificClasses.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Term which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          CountableMustExcludeSpecificClasses contract,
                          Object plaintiff,
                          Term<?> evidence
                          )
            throws ParametersMustNotBeNull.Violation
        {
            this ( contract,
                   plaintiff,
                   evidence,
                   null ); // cause
        }


        /**
         * <p>
         * Creates a new CountableMustExcludeSpecificClasses.Violation.
         * </p>
         *
         * @param contract The contract which was violated.
         *                 Must not be null.
         *
         * @param plaintiff The object under contract, such as the object
         *                  whose method obligation was violated, or which
         *                  violated its own method guarantee.
         *                  Must not be null.
         *
         * @param evidence The Term which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        @SuppressWarnings("unchecked") // Cast Term<?> (which is an
            // Iterable<?>) to Iterable<Object>.
        public Violation (
                          CountableMustExcludeSpecificClasses contract,
                          Object plaintiff,
                          Term<?> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The term did not belong to the domain: "
                    + ( contract == null
                        ? evidence
                        : contract.domain ().nonMember (
                              new ArrayObject<Object> (
                                  Object.class,
                                  (Iterable<Object>) evidence ) ) )
                    + ".", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
