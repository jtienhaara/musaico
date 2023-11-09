package musaico.foundation.types;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.InstanceOfClass;
import musaico.foundation.domains.SpecificDomain;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;

import musaico.foundation.operations.select.StandardSelect;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Just;
import musaico.foundation.term.Select;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;

import musaico.foundation.term.contracts.ElementsMustBelongToDomain;


/**
 * <p>
 * A straightforward Type, with an element Class, a "none" value
 * (such as 0 or "" or an empty array, and so on),
 * a domain of elements (such as <code> InstanceOfClass </code>),
 * and a contract for the whole Term (such as
 * <code>TermMustBeInstanceOfClass</code>).
 * </p>
 *
 * <p>
 * Note that it is not always practical to verify that a Term's
 * elements match the element domain.  For example, Infinite Terms
 * can generally only be checked against the Contract for the overall term.
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
 * @see musaico.foundation.types.MODULE#COPYRIGHT
 * @see musaico.foundation.types.MODULE#LICENSE
 */
public class StandardType<VALUE extends Object>
    implements Type<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( StandardType.class );


    /** A simple BigDecimal type, with BigDecimal.ZERO
     *  as the none object. */
    public static final Type<BigDecimal> BIGDECIMAL =
        new StandardType<BigDecimal> (
            BigDecimal.class,    // element_class
            BigDecimal.ZERO );   // none

    /** A simple BigInteger type, with BigInteger.ZERO
     *  as the none object. */
    public static final Type<BigInteger> BIGINTEGER =
        new StandardType<BigInteger> (
            BigInteger.class,    // element_class
            BigInteger.ZERO );   // none

    /** A simple Boolean type, with false as the none object. */
    public static final Type<Boolean> BOOLEAN =
        new StandardType<Boolean> (
            Boolean.class,       // element_class
            false );             // none

    /** A simple Byte type, with byte 0 as the none object. */
    public static final Type<Byte> BYTE =
        new StandardType<Byte> (
            Byte.class,          // element_class
            (byte) 0 );          // none

    /** A simple byte [] type, with {} as the none object. */
    public static final Type<byte []> BYTES =
        new StandardType<byte []> (
            byte [].class,       // element_class
            new byte [ 0 ] );    // none

    /** A simple Character type, with '\0' as the none object. */
    public static final Type<Character> CHARACTER =
        new StandardType<Character> (
            Character.class,     // element_class
            '\0' );              // none

    /** A simple Date type, with UNIX time 0 as the none object. */
    public static final Type<Date> Date =
        new StandardType<Date> (
            Date.class,        // element_class
            new Date ( 0L ) ); // none

    /** A simple Double type, with 0D as the none object. */
    public static final Type<Double> DOUBLE =
        new StandardType<Double> (
            Double.class,        // element_class
            0D );                // none

    /** A simple Float type, with 0F as the none object. */
    public static final Type<Float> FLOAT =
        new StandardType<Float> (
            Float.class,         // element_class
            0F );                // none

    /** A simple Integer type, with int 0 as the none object. */
    public static final Type<Integer> INTEGER =
        new StandardType<Integer> (
            Integer.class,       // element_class
            0 );                 // none

    /** A simple Long type, with 0L as the none object. */
    public static final Type<Long> LONG =
        new StandardType<Long> (
            Long.class,          // element_class
            0L );               // none

    /** A simple Number type, with int 0 as the none object. */
    public static final Type<Number> NUMBER =
        new StandardType<Number> (
            Number.class,      // element_class
            0 );               // none

    /** A simple Object type, with "" as the none object. */
    public static final Type<Object> OBJECT =
        new StandardType<Object> (
            Object.class,      // element_class
            "" );              // none

    /** A simple Short type, with short 0 as the none object. */
    public static final Type<Short> SHORT =
        new StandardType<Short> (
            Short.class,         // element_class
            (short) 0 );         // none

    /** A simple String type, with "" as the none object. */
    public static final Type<String> STRING =
        new StandardType<String> (
            String.class,      // element_class
            "" );              // none


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // The class of term elements covered by this type.
    private final Class<VALUE> elementClass;

    // The filter which determines whether an element is or isn't a member
    // of this type.
    private final Domain<VALUE> elementDomain;

    // The contract which governs whether or not a whole Term is or isn't
    // a Term of this type.
    private final Contract<Term<?>, ? extends TermViolation> termContract;

    // The constant "none" value.
    private final VALUE none;


    /**
     * <p>
     * Creates a new StandardType.
     * </p>
     *
     * @param element_class The class of term elements covered by
     *                      this type.  Because Java
     *                      generics are erased during the compile,
     *                      we cannot pass genericized classes in
     *                      unless we accept <code> Class&lt;?&gt; </code>.
     *                      For example, if the element_class parameter
     *                      was defined as <code> Class&lt;VALUE&gt; </code>,
     *                      as it really should be, then we would
     *                      not be able to pass <code> List.class </code>
     *                      for a StandardType representing
     *                      <code> List&lt;String&gt; </code> elements.
     *                      Unfortunately this means that if the caller
     *                      passes <code> Integer.class </code>
     *                      or <code> Set.class </code> and so on
     *                      to a StandardType with "none"
     *                      value <code> new ArrayList&lt;String&gt; () </code>
     *                      then the compiler will not catch it; a
     *                      Parameter1.MustBeInstanceOfClass.Violation will.
     *                      Also note that, prior to Java 7, no checking
     *                      was done on the elements of Collections
     *                      passed to a method requiring
     *                      <code> Collection&lt;X&gt; </code> parameter.
     *                      You could, for example, create a
     *                      <code> new ArrayList&lt;Object&gt; () </code>,
     *                      add an Integer to it, then cast the List
     *                      without any problems and pass it to a method
     *                      requiring <code> List&lt;String&gt; </code>.
     *                      Only when the method actually tries to treat
     *                      the Integer element as a String will a
     *                      RuntimeException occur in Java 5 and most
     *                      builds of Java 6.  Use with caution prior to 7.
     *                      Must not be null.
     *
     * @param none The constant "no value" value.  For example, "" for
     *             a String none, or 0 or -1 for an Integer none,
     *             and so on.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Class<?>-Class<VALUE>. See comment.
    public StandardType (
                         Class<?> element_class,
                         VALUE none
                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeInstanceOfClass.Violation
    {
        this ( element_class,
               new SpecificDomain<Object, VALUE> (
                   new InstanceOfClass ( element_class ),
                   (Class<VALUE>) element_class
                   ),
               none );
    }


    /**
     * <p>
     * Creates a new StandardType.
     * </p>
     *
     * @param element_class The class of term elements covered by
     *                      this type.  Because Java
     *                      generics are erased during the compile,
     *                      we cannot pass genericized classes in
     *                      unless we accept <code> Class&lt;?&gt; </code>.
     *                      For example, if the element_class parameter
     *                      was defined as <code> Class&lt;VALUE&gt; </code>,
     *                      as it really should be, then we would
     *                      not be able to pass <code> List.class </code>
     *                      for a StandardType representing
     *                      <code> List&lt;String&gt; </code> elements.
     *                      Unfortunately this means that if the caller
     *                      passes <code> Integer.class </code>
     *                      or <code> Set.class </code> and so on
     *                      to a StandardType with "none"
     *                      value <code> new ArrayList&lt;String&gt; () </code>
     *                      then the compiler will not catch it; a
     *                      Parameter1.MustBeInstanceOfClass.Violation will.
     *                      Also note that, prior to Java 7, no checking
     *                      was done on the elements of Collections
     *                      passed to a method requiring
     *                      <code> Collection&lt;X&gt; </code> parameter.
     *                      You could, for example, create a
     *                      <code> new ArrayList&lt;Object&gt; () </code>,
     *                      add an Integer to it, then cast the List
     *                      without any problems and pass it to a method
     *                      requiring <code> List&lt;String&gt; </code>.
     *                      Only when the method actually tries to treat
     *                      the Integer element as a String will a
     *                      RuntimeException occur in Java 5 and most
     *                      builds of Java 6.  Use with caution prior to 7.
     *                      Must not be null.
     *
     * @param element_domain The filter which determines whether an element
     *                       is or isn't a member of this type.
     *                       For example, an InstanceOfClass domain.
     *                       Must not be null.
     *
     * @param none The constant "no value" value.  For example, "" for
     *             a String none, or 0 or -1 for an Integer none,
     *             and so on.  Must not be null.
     */
    public StandardType (
                         Class<?> element_class,
                         Domain<VALUE> element_domain,
                         VALUE none
                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeInstanceOfClass.Violation
    {
        this ( element_class,
               element_domain,
               element_domain == null
                   ? null
                   : new ElementsMustBelongToDomain<VALUE> ( element_domain ),
               none );
    }


    /**
     * <p>
     * Creates a new StandardType.
     * </p>
     *
     * @param element_class The class of term elements covered by
     *                      this type.  Because Java
     *                      generics are erased during the compile,
     *                      we cannot pass genericized classes in
     *                      unless we accept <code> Class&lt;?&gt; </code>.
     *                      For example, if the element_class parameter
     *                      was defined as <code> Class&lt;VALUE&gt; </code>,
     *                      as it really should be, then we would
     *                      not be able to pass <code> List.class </code>
     *                      for a StandardType representing
     *                      <code> List&lt;String&gt; </code> elements.
     *                      Unfortunately this means that if the caller
     *                      passes <code> Integer.class </code>
     *                      or <code> Set.class </code> and so on
     *                      to a StandardType with "none"
     *                      value <code> new ArrayList&lt;String&gt; () </code>
     *                      then the compiler will not catch it; a
     *                      Parameter1.MustBeInstanceOfClass.Violation will.
     *                      Also note that, prior to Java 7, no checking
     *                      was done on the elements of Collections
     *                      passed to a method requiring
     *                      <code> Collection&lt;X&gt; </code> parameter.
     *                      You could, for example, create a
     *                      <code> new ArrayList&lt;Object&gt; () </code>,
     *                      add an Integer to it, then cast the List
     *                      without any problems and pass it to a method
     *                      requiring <code> List&lt;String&gt; </code>.
     *                      Only when the method actually tries to treat
     *                      the Integer element as a String will a
     *                      RuntimeException occur in Java 5 and most
     *                      builds of Java 6.  Use with caution prior to 7.
     *                      Must not be null.
     *
     * @param element_domain The filter which determines whether an element
     *                       is or isn't a member of this type.
     *                       For example, an InstanceOfClass domain.
     *                       Must not be null.
     *
     * @param term_contract The contract which governs whether
     *                      a whole Term is or isn't a Term of this
     *                      type.  If the element Domain
     *                      DISCARDs an element, then the specified Contract
     *                      must discard any term that contains
     *                      that element, as well.
     *                      However even if all elements are KEPT by
     *                      the element Domain, the Contract can still
     *                      reject the whole term (for example because
     *                      it is of the wrong length, and so on).
     *                      Must not be null.
     *
     * @param none The constant "no value" value.  For example, "" for
     *             a String none, or 0 or -1 for an Integer none,
     *             and so on.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Class<?>-Class<VALUE>. See comment.
    public StandardType (
                         Class<?> element_class,
                         Domain<VALUE> element_domain,
                         Contract<Term<?>, ? extends TermViolation> term_contract,
                         VALUE none
                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustBeInstanceOfClass.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               element_class, element_domain, term_contract, none );

        try
        {
            this.elementClass = (Class<VALUE>) element_class;
        }
        catch ( ClassCastException e )
        {
            throw new Parameter1.MustBeInstanceOfClass ( Class.class )
                .violation (
                            this,
                            element_class
                            );
        }

        this.elementDomain = element_domain;
        this.termContract = term_contract;
        this.none = none;

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Type#array(int)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic array creation
    public final VALUE [] array (
                                 int num_elements
                                 )
        throws Parameter1.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( Parameter1.MustBeGreaterThanOrEqualToZero.CONTRACT,
                               num_elements );

        final VALUE [] array = (VALUE [])
            Array.newInstance ( this.elementClass, num_elements );

        return array;
    }


    /**
     * @see musaico.foundation.term.Type#elementClass()
     */
    @Override
    public final Class<VALUE> elementClass ()
        throws ReturnNeverNull.Violation
    {
        return this.elementClass;
    }


    /**
     * @see musaico.foundation.term.Type#elementDomain()
     */
    @Override
    public final Domain<VALUE> elementDomain ()
        throws ReturnNeverNull.Violation
    {
        return this.elementDomain;
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final StandardType<?> that = (StandardType<?>) object;

        if ( this.elementClass != that.elementClass )
        {
            return false;
        }

        if ( this.none == null )
        {
            if ( that.none != null )
            {
                return false;
            }
        }
        else if ( that.none == null )
        {
            return false;
        }
        else if ( ! this.none.equals ( that.none ) )
        {
            return false;
        }

        if ( this.elementDomain == null )
        {
            if ( that.elementDomain != null )
            {
                return false;
            }
        }
        else if ( that.elementDomain == null )
        {
            return false;
        }
        else if ( ! this.elementDomain.equals ( that.elementDomain ) )
        {
            return false;
        }

        if ( this.termContract == null )
        {
            if ( that.termContract != null )
            {
                return false;
            }
        }
        else if ( that.termContract == null )
        {
            return false;
        }
        else if ( ! this.termContract.equals ( that.termContract ) )
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
        if ( this.elementClass == null )
        {
            return 0;
        }

        return this.elementClass.hashCode ();
    }


    /**
     * @see musaico.foundation.term.Type#none()
     */
    @Override
    public final VALUE none ()
        throws ReturnNeverNull.Violation
    {
        return this.none;
    }


    /**
     * @see musaico.foundation.term.Type#select()
     */
    @Override
    public final Select<VALUE> select ()
        throws ReturnNeverNull.Violation
    {
        return new StandardSelect<VALUE> ( this );
    }


    /**
     * @see musaico.foundation.term.Type#termContract()
     */
    @Override
    public final Contract<Term<?>, ? extends TermViolation> termContract ()
        throws ReturnNeverNull.Violation
    {
        return this.termContract;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final String maybe_term_details;
        if ( this.termContract instanceof ElementsMustBelongToDomain
             && ( (ElementsMustBelongToDomain<?>) this.termContract ).domain ()
                     == this.elementDomain )
        {
            maybe_term_details = "";
        }
        else
        {
            maybe_term_details = " [ "
                + this.termContract
                + " ]";
        }

        final String maybe_element_details;
        if ( this.elementDomain instanceof InstanceOfClass
             && ( (InstanceOfClass) this.elementDomain ).domainClass () == this.elementClass )
        {
            maybe_element_details = "";
        }
        else
        {
            maybe_element_details = " [ "
                + this.elementDomain
                + " ]";
        }

        return ClassName.of ( this.elementClass )
            + maybe_term_details
            + maybe_element_details;
    }
}
