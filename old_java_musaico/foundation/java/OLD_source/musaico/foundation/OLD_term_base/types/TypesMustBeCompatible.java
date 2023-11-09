package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Type;


/**
 * <p>
 * Guarantees each Term of Types contains only Types which are
 * compatible with each other.
 * </p>
 *
 * <p>
 * Every Term of Type(s) must meet all of the following
 * conditions:
 * </p>
 *
 * <ul>
 *   <li> At least one of the Type(s) must have
 *        an <code> elementClass () </code> which can be cast to
 *        every other element class.  For example, if a Term of two Types
 *        have element classes Number and Integer, respectively, then
 *        the common element class is Integer. </li>
 *
 *   <li> At least one of the Type(s) must have a <code> none () </code>
 *        value which meets the <code> elementConstraints () </code>
 *        of all the Type(s).  For example, if one Type&lt;String&gt;
 *        has a none value of "" and no element constraints, while
 *        a second Type&lt;String&gt; has a none value of "none"
 *        and one element constraint String must not be empty,
 *        then the common none object is the String "none". </li>
 *
 *   <li> The first Type must be able to generate a Term
 *        <code> from ( ... ) </code> the none value common to
 *        all the Type(s).  The subsequent Type(s) must be able to
 *        generate a Term <code> from ( ... ) </code> the preceding
 *        Type's output Term.  The final output Term must have
 *        exactly one element: the none value common to all the Type(s). </li>
 *
 *   <li> A Term generated <code> from ( ... ) </code> the Type(s)
 *        must pass through the <code> check () </code> method of each
 *        Type.  The final output Term must be the same as the original
 *        input Term. </li>
 * </ul>
 *
 * <p>
 * A Term of Type(s) which meet this contract can be used as follows:
 * </p>
 *
 * <ul>
 *   <li> The output(s) of each Type's parameterless method
 *        returning a Term of objects is union'ed together
 *        from all of the Type(s).
 *        For example, if a Term has 3 Types, then its
 *        <code> behaviours () </code> comprise the set containing
 *        all unique Behaviours from all 3 Types.  The same goes for 
 *        its <code> constraints () </code>,
 *        <code> elementConstraints () <code>, <code> fields () <code>,
 *        and so on. </li>
 *
 *   <li> In order to generate a "none" value, the first Type
 *        whose <code> none () </code> produces an element which
 *        meets all <code> constraints () </code> of all the Types
 *        is used to generate the result.  For example,
 *        if a Term&lt;String&gt; is of types <code> StringType </code>,
 *        <code> HexadecimalType </code>
 *        and <code> GreaterThan8Type </code>;
 *        and if the <code> StringType </code> returns none = "",
 *        and <code> HexadecimalType </code> returns none = "0x0",
 *        and <code> GreaterThan8Type </code> returns none = "0x9";
 *        and if StringType has no <code> elementConstraints () </code>,
 *        whereas <code> HexadecimalType </code> insists on every
 *        element being any formatted hexidecimal number,
 *        but <code> GreaterThan8Type </code> requires each element
 *        to be 9 or more, then only the last none value ("0x9")
 *        will satisfy the <code> elementConstraints () </code>
 *        of all 3 Types, and so it is used as the none value. </li>
 *
 *   <li> In order to import an object of some sort or another into
 *        a Term of Types, call
 *        <code> from ( ... ) </code> on the first Type with the input
 *        object, then pass each output Term to the subsequent Type's
 *        <code> from ( Term ) </code> method.  For example,
 *        if a Term&lt;String&gt; is of types <code> StringType </code>,
 *        <code> HexadecimalType </code>
 *        and <code> GreaterThan8Type </code>,
 *        then a String might be imported with code such as:
 *        <code> greater_type.from (
 *                   hexadeximal_type.from (
 *                       string_type.from ( "0xA4" ) ) ); </code> </li>
 *
 *   <li> All other Type methods are guaranteed to produce the
 *        same outcomes.  For example, if a Term&lt;String&gt;
 *        is of types <code> StringType </code>,
 *        <code> HexadecimalType </code>
 *        and <code> GreaterThan8Type </code>, then calling
 *        <code> check () <code> on any of the Types will produce
 *        the same outcome, or calling <code> filter ( ... ) </code>
 *        on any of the Types will produce the same outcome, and so on.
 *        </li>
 * </ul>
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public class TypesMustBeCompatible
    implements Contract<Term<Type<?>>, TypesMustBeCompatible.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( TypesMustBeCompatible.class );

    /** The singleton TypesMustBeCompatible contract. */
    public static final TypesMustBeCompatible CONTRACT =
        new TypesMustBeCompatible ();


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;


    /**
     * <p>
     * Creates TypesMustBeCompatible.CONTRACT.
     * Can be extended by more specific Contracts.
     * </p>
     */
    protected TypesMustBeCompatible ()
    {
        this.contracts = new Advocate ( this );
    }

    /**
     * <p>
     * Performs all validating of the specified Types.
     * </p>
     *
     * @see musaico.foundation.term.TypesMustBeCompatible#filter(musaico.foundation.Term)
     *
     * @throws TypesMustBeCompatible.Violation If the specified Types
     *                                         are incompatible
     *                                         with each other.
     */
    public final void allChecks (
            Term<Type<?>> types
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               TypesMustBeCompatible.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types );

        @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
        final Term<Type<Object>> object_types =
            (Term<Type<Object>>) ( (Object) types );

        // Throws TypesMustBeCompatible.Violation:
        TypesMustBeCompatible.elementClass ( object_types );

        // Throws TypesMustBeCompatible.Violation:
        final Object none =
            TypesMustBeCompatible.none ( object_types );

        // Throws TypesMustBeCompatible.Violation:
        final Term<Object> term =
            TypesMustBeCompatible.term ( object_types, none );

        // Throws TypesMustBeCompatible.Violation:
        TypesMustBeCompatible.check ( object_types, term );

        // Throws TypesMustBeCompatible.Violation:
        this.specificChecks ( types );
    }


    /**
     * <p>
     * Ensures the specified Type(s) all return the same output Term from
     * <code> check ( ... ) </code>ing the specified Term.
     * </p>
     *
     * @param types The Type(s) which will check the Term individually.
     *              Must not be null.
     *
     * @param term The Term to check.  Must not be null.
     *
     * @throws TypesMustBeCompatible.Violation If any of the Type(s)
     *                                         produces an output different
     *                                         from any of the others.
     */
    public static final <VALUE extends Object>
        Term<VALUE> check (
            Term<Type<VALUE>> types,
            Term<VALUE> term
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               TypesMustBeCompatible.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types, term );

        Term<VALUE> checked;
        try
        {
            final Type<VALUE> first_type = types.head ().orNull ();
            checked = first_type.check ().build ().apply ( term );
            for ( Type<VALUE> type : types )
            {
                if ( type == first_type )
                {
                    continue;
                }

                final Term<VALUE> type_checked =
                    type.check ().build ().apply ( checked );
                if ( ! type_checked.equals ( checked ) )
                {
                    @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
                    final TypesMustBeCompatible.Violation violation =
                        TypesMustBeCompatible.CONTRACT.violation (
                            TypesMustBeCompatible.class,          // plaintiff
                            (Term<Type<?>>) ( (Object) types ) ); // evidence
                    throw violation;
                }

                checked = type_checked;
            }
        }
        catch ( TypesMustBeCompatible.Violation violation )
        {
            throw violation;
        }
        catch ( Exception e )
        {
            @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
            final TypesMustBeCompatible.Violation violation =
                TypesMustBeCompatible.CONTRACT.violation (
                    TypesMustBeCompatible.class,        // plaintiff
                    (Term<Type<?>>) ( (Object) types ), // evidence
                    e );
            throw violation;
        }

        return checked;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public String description ()
    {
        return "The Types must be compatible with each other.";
    }


    /**
     * <p>
     * Returns the element class common to the specified Term of Type(s).
     * </p>
     *
     * <p>
     * The most specific elementClass () returned by the specified Type(s)
     * is returned.
     * </p>
     *
     * @param types The Type(s) whose common element class will be returned.
     *              Must not be null.
     *
     * @return The common element class value from the specified Type(s).
     *
     * @throws TypesMustBeCompatible.Violation If a common element class
     *                                         does not exist.
     */
    public static final <VALUE extends Object>
        Class<VALUE> elementClass (
            Term<Type<VALUE>> types
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               TypesMustBeCompatible.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types );

        Class<VALUE> element_class = null;
        for ( Type<VALUE> type : types )
        {
            final Class<VALUE> maybe_element_class = type.elementClass ();
            boolean is_ok = true;
            for ( Type<VALUE> check_type : types )
            {
                final Class<VALUE> that_element_class =
                    check_type.elementClass ();
                if ( ! that_element_class.isAssignableFrom ( maybe_element_class ) )
                {
                    is_ok = false;
                    break;
                }
            }

            if ( is_ok )
            {
                element_class = maybe_element_class;
                break;
            }
        }

        if ( element_class == null )
        {
            @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
            final TypesMustBeCompatible.Violation violation =
                TypesMustBeCompatible.CONTRACT.violation (
                    TypesMustBeCompatible.class,          // plaintiff
                    (Term<Type<?>>) ( (Object) types ) ); // evidence
            throw violation;
        }

        return element_class;
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
    public final FilterState filter (
            Term<Type<?>> types
            )
    {
        if ( types == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            this.allChecks ( types );
        }
        catch ( TypesMustBeCompatible.Violation violation )
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
        return 17 * ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * <p>
     * Returns the "none" object compatible with all of the specified Type(s).
     * </p>
     *
     * <p>
     * In order to generate a "none" value, the first Type
     * whose <code> none () </code> produces an element which
     * meets all <code> constraints () </code> of all the Types
     * is used to generate the result.  For example,
     * if a Term&lt;String&gt; is of types <code> StringType </code>,
     * <code> HexadecimalType </code>
     * and <code> GreaterThan8Type </code>;
     * and if the <code> StringType </code> returns none = "",
     * and <code> HexadecimalType </code> returns none = "0x0",
     * and <code> GreaterThan8Type </code> returns none = "0x9";
     * and if StringType has no <code> elementConstraints () </code>,
     * whereas <code> HexadecimalType </code> insists on every
     * element being any formatted hexidecimal number,
     * but <code> GreaterThan8Type </code> requires each element
     * to be 9 or more, then only the last none value ("0x9")
     * will satisfy the <code> elementConstraints () </code>
     * of all 3 Types, and so it is used as the none value.
     * </p>
     *
     * @param types The Type(s) whose common "none" value will be returned.
     *              Must not be null.
     *
     * @return The common "none" value from the specified Type(s).
     *
     * @throws TypesMustBeCompatible.Violation If a common "none" value
     *                                         does not exist.
     */
    public static final <VALUE extends Object>
        VALUE none (
            Term<Type<VALUE>> types
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               TypesMustBeCompatible.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types );

        VALUE none = null;
        for ( Type<VALUE> type : types )
        {
            final VALUE maybe_none = type.none ();
            boolean is_ok = true;
            for ( Type<VALUE> check_type : types )
            {
                for ( Contract<VALUE, ?> element_constraint : check_type.elementConstraints () )
                {
                    try
                    {
                        if ( ! element_constraint.filter ( maybe_none )
                             .isKept () )
                        {
                            is_ok = false;
                            break;
                        }

                    }
                    catch ( ClassCastException e )
                    {
                        is_ok = false;
                    }
                }

                if ( ! is_ok )
                {
                    break;
                }
            }

            if ( is_ok )
            {
                none = maybe_none;
                break;
            }
        }

        if ( none == null )
        {
            @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
            final TypesMustBeCompatible.Violation violation =
                TypesMustBeCompatible.CONTRACT.violation (
                    TypesMustBeCompatible.class,          // plaintiff
                    (Term<Type<?>>) ( (Object) types ) ); // evidence
            throw violation;
        }

        return none;
    }


    /**
     * <p>
     * Performs more specific validating, if this is a derived class.
     * </p>
     *
     * <p>
     * Only ever called by <code> allChecks ( types ) </code> at the end,
     * after all general checks have passed.
     * </p>
     *
     * @see musaico.foundation.term.TypesMustBeCompatible#allChecks(musaico.foundation.Term)
     *
     * @throws TypesMustBeCompatible.Violation If the specified Types
     *                                         are incompatible, by this
     *                                         derived contract's specific
     *                                         standards.
     */
    public void specificChecks (
            Term<Type<?>> types
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               TypesMustBeCompatible.Violation
    {
        // By default, no additional checks.
    }


    /**
     * <p>
     * Ensures the specified Type(s) can work together to create a Term
     * from the specified single value, and that the Term's only element
     * is exactly that value.
     * </p>
     *
     * @param types The Type(s) which will create the Term.
     *              Must not be null.
     *
     * @param value The input object from which a Term will be created.
     *              Must not be null.
     *
     * @throws TypesMustBeCompatible.Violation If a common "none" value
     *                                         does not exist.
     */
    public static final <VALUE extends Object>
        Term<VALUE> term (
            Term<Type<VALUE>> types,
            VALUE value
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               TypesMustBeCompatible.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               types, value );

        Term<VALUE> term;
        try
        {
            final Type<VALUE> first_type = types.head ().orNull ();
            term = first_type.from ( value );
            for ( Type<VALUE> type : types )
            {
                if ( type == first_type )
                {
                    continue;
                }

                term = type.from ( term );
            }
        }
        catch ( Exception e )
        {
            @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
            final TypesMustBeCompatible.Violation violation =
                TypesMustBeCompatible.CONTRACT.violation (
                    TypesMustBeCompatible.class,        // plaintiff
                    (Term<Type<?>>) ( (Object) types ), // evidence
                    e );
            throw violation;
        }

        if ( ! value.equals ( term.orNull () ) )
        {
            @SuppressWarnings("unchecked") // Cast Term<Type<?>>-Term<Type<Object>>
            final TypesMustBeCompatible.Violation violation =
                TypesMustBeCompatible.CONTRACT.violation (
                    value,                                // plaintiff
                    (Term<Type<?>>) ( (Object) types ) ); // evidence
            throw violation;
        }

        return term;
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
    public TypesMustBeCompatible.Violation violation (
            Object plaintiff,
            Term<Type<?>> evidence
            )
    {
        return new TypesMustBeCompatible.Violation (
            this,
            plaintiff,
            evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public TypesMustBeCompatible.Violation violation (
            Object plaintiff,
            Term<Type<?>> evidence,
            Throwable cause
            )
    {
        final TypesMustBeCompatible.Violation violation =
            this.violation ( plaintiff,
                             evidence,
                             cause );

        return violation;
    }




    /**
     * <p>
     * A violation of the TypesMustBeCompatible contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            TypesMustBeCompatible.serialVersionUID;


        /**
         * <p>
         * Creates a new TypesMustBeCompatible.Violation.
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
         * @param evidence The Types which violated the contract.
         *                 Can be null.
         */
        public Violation (
                          TypesMustBeCompatible contract,
                          Object plaintiff,
                          Term<Type<?>> evidence
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
         * Creates a new TypesMustBeCompatible.Violation.
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
         * @param evidence The Types which violated the contract.
         *                 Can be null.
         *
         * @param cause The Throwable which caused this contract violation.
         *              Can be null.
         */
        @SuppressWarnings("unchecked") // Cast Contract<?,?> - Contract<Obj,?>.
        public Violation (
                          TypesMustBeCompatible contract,
                          Object plaintiff,
                          Term<Type<?>> evidence,
                          Throwable cause
                          )
            throws ParametersMustNotBeNull.Violation
        {
            super ( contract,
                    "The Types were incompatible.", // description
                    plaintiff,
                    evidence,
                    cause );
        }
    }
}
