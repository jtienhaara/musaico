package musaico.foundation.contract.obligations;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.filter.NotNullFilter;

import musaico.foundation.filter.comparability.BoundedFilter;

import musaico.foundation.filter.container.LengthFilter;

import musaico.foundation.filter.elements.AllIndicesFilter;
import musaico.foundation.filter.elements.ExcludesClasses;
import musaico.foundation.filter.elements.ExcludesIndices;
import musaico.foundation.filter.elements.ExcludesMembers;
import musaico.foundation.filter.elements.IncludesIndices;
import musaico.foundation.filter.elements.IncludesMembers;
import musaico.foundation.filter.elements.IncludesOnlyClasses;
import musaico.foundation.filter.elements.IncludesOnlyIndices;
import musaico.foundation.filter.elements.IncludesOnlyMembers;
import musaico.foundation.filter.elements.NoDuplicates;
import musaico.foundation.filter.elements.NoNulls;

import musaico.foundation.filter.equality.EqualTo;
import musaico.foundation.filter.equality.NotEqualTo;

import musaico.foundation.filter.membership.InstanceOf;
import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.filter.number.EqualToNumber;
import musaico.foundation.filter.number.GreaterThanNumber;
import musaico.foundation.filter.number.GreaterThanOrEqualToNegativeOne;
import musaico.foundation.filter.number.GreaterThanOrEqualToNumber;
import musaico.foundation.filter.number.GreaterThanOrEqualToOne;
import musaico.foundation.filter.number.GreaterThanOrEqualToZero;
import musaico.foundation.filter.number.GreaterThanNegativeOne;
import musaico.foundation.filter.number.GreaterThanOne;
import musaico.foundation.filter.number.GreaterThanZero;
import musaico.foundation.filter.number.LessThanNumber;
import musaico.foundation.filter.number.LessThanOrEqualToNegativeOne;
import musaico.foundation.filter.number.LessThanOrEqualToNumber;
import musaico.foundation.filter.number.LessThanOrEqualToOne;
import musaico.foundation.filter.number.LessThanOrEqualToZero;
import musaico.foundation.filter.number.LessThanNegativeOne;
import musaico.foundation.filter.number.LessThanOne;
import musaico.foundation.filter.number.LessThanZero;
import musaico.foundation.filter.number.NotEqualToNumber;

import musaico.foundation.filter.string.NotEmptyString;
import musaico.foundation.filter.string.EmptyString;
import musaico.foundation.filter.string.StringExcludesSpaces;
import musaico.foundation.filter.string.StringContainsNonSpaces;
import musaico.foundation.filter.string.StringContainsOnlyAlpha;
import musaico.foundation.filter.string.StringContainsOnlyAlphaNumerics;
import musaico.foundation.filter.string.StringContainsOnlyNumerics;
import musaico.foundation.filter.string.StringContainsOnlyPrintableCharacters;
import musaico.foundation.filter.string.StringID;
import musaico.foundation.filter.string.StringLength;
import musaico.foundation.filter.string.StringPattern;

import musaico.foundation.filter.time.BeforeAndAfter;
import musaico.foundation.filter.time.Changing;
import musaico.foundation.filter.time.Unchanging;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.composite.Not;

import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.Numbers;
import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Contracts for parameter # N of a constructor or method.
 * </p>
 *
 * <p>
 * A constructor or method, whether abstract or concrete, can
 * declare its contract by stating that it throws a violation
 * of a specific AbstractMultipleParameters contract.  For example an interface
 * may declare that it throws Parameter1.MustBeLessThanZero.Violation.
 * In this case, classes which implement that interface must
 * check parameter 1 against the Parameter1.MustBeLessThanZero.CONTRACT
 * whenever the method is invoked, and throw
 * a Parameter1.MustBeLessThanZero.Violation runtime exception when
 * the parameter is not in the required domain.
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
 * @see musaico.foundation.contract.obligations.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.obligations.MODULE#LICENSE
 */
public abstract class AbstractMultipleParameters
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // String representation of this AbstractMultipleParameters, such as
    // "Parameter 1" or "Parameter 7" or "Every parameter"
    // or "Every parameter except 3" and so on.
    private final String parameterAsString;


    /**
     * <p>
     * Creates a new AbstractMultipleParameters.
     * </p>
     */
    protected AbstractMultipleParameters ()
    {
        this.parameterAsString = "" + this.parameterAsString ();
    }


    /*
     * @return This AbstractMultipleParameters into a human-readable string.
     *         Can be overridden, though it is not recommended.
     *         Never null.
     */
    protected String parameterAsString ()
    {
        final long parameter_bitmap = this.PARAMETER_BITMAP ();

        if ( parameter_bitmap == 0x0001 )
        {
            return "Parameter 1";
        }
        else if ( parameter_bitmap == 0x0002 )
        {
            return "Parameter 2";
        }
        else if ( parameter_bitmap == 0x0004 )
        {
            return "Parameter 2";
        }
        else if ( parameter_bitmap == 0x0008 )
        {
            return "Parameter 3";
        }
        else if ( parameter_bitmap == 0x0010 )
        {
            return "Parameter 4";
        }
        else if ( parameter_bitmap == 0x0020 )
        {
            return "Parameter 5";
        }
        else if ( parameter_bitmap == 0x0040 )
        {
            return "Parameter 6";
        }
        else if ( parameter_bitmap == 0x0080 )
        {
            return "Parameter 8";
        }
        else if ( parameter_bitmap == 0x0100 )
        {
            return "Parameter 9";
        }
        else if ( parameter_bitmap == AbstractMultipleParameters.EVERY_PARAMETER )
        {
            return "Every parameter";
        }

        // Some higher parameter number, or multiple parameters.
        final long [] bits = new long [ 63 ];
        int num_includes = 0;
        int num_excludes = 0;
        long remaining_bits = parameter_bitmap;
        int one_include = -1;
        int one_exclude = -1;
        for ( int b = 0; b < bits.length; b ++ )
        {
            bits [ b ] = remaining_bits & 1L;
            if ( bits [ b ] == 1L )
            {
                num_includes ++;
                one_include = b + 1;
            }
            else
            {
                num_excludes ++;
                one_exclude = b + 1;
            }

            remaining_bits >>= 1;
        }

        final StringBuilder sbuf = new StringBuilder ();
        if ( num_includes == 1 )
        {
            sbuf.append ( "Parameter " + one_include );
        }
        else if ( num_excludes == 1 )
        {
            sbuf.append ( "Every parameter except " + one_exclude );
        }
        else
        {
            if (num_includes <= num_excludes )
            {
                sbuf.append ( "Every parameter in ( " );
            }
            else
            {
                sbuf.append ( "Every parameter except ( " );
            }

            boolean is_first = true;
            for ( int b = 0; b < bits.length; b ++ )
            {
                final boolean is_mentionable;
                if ( num_includes <= num_excludes
                     && bits [ b ] == 1L )
                {
                    is_mentionable = true;
                }
                else if ( num_excludes < num_includes
                          && bits [ b ] == 0L )
                {
                    is_mentionable = true;
                }
                else
                {
                    is_mentionable = false;
                }

                if ( is_mentionable )
                {
                    if ( is_first )
                    {
                        is_first = false;
                    }
                    else
                    {
                        sbuf.append ( ", " );
                    }

                    final int parameter_num = b + 1;
                    sbuf.append ( "" + parameter_num );
                }
            }

            sbuf.append ( " )" );
        }

        final String parameter_as_string = sbuf.toString ();
        return parameter_as_string;
    }


    /** The parameter # representing every parameter
     *  (1 and 2 and 3 and ... and so on).
     * The maximum bitmap in Java is: 0x7FFFFFFFFFFFFFFF.
     * This translates to parameter #s 1, 2, 3, 4, 5, 6, ...,
     * 60, 61, 62, 63.  If you have more than 63 parameters
     * then you are lost in the weeds, and probably need a snorkel
     * more than a contract. */
    public static final long EVERY_PARAMETER = Long.MAX_VALUE;

    /**
     * @return Bitmap of the parameter(s) represented
     *         by this AbstractMultipleParameters.
     *         The first parameter (parameter index 0) is represented
     *         by 0x0001.  The second parameter (parameter index 1)
     *         is represented by 0x0002.  The third parameter (parameter
     *         index 2) is represented by 0x0004.  Fourth is 0x0008,
     *         Fifth is 0x0010, Sixth is 0x0020, and so on.
     *         Can be AbstractMultipleParameters.EVERY_PARAMETER to represent
     *         every parameter / all parameters.  Can be 0L to
     *         reprresent no parameters, though that approach should
     *         be used with caution.  Can be a bitmap of multiple
     *         parameters, such as 0x00FF to represent the first 8
     *         parameters,
     *         or ( AbstractMultipleParameters.EVERY_PARAMETER - 0x0002 )
     *         to represent every parameter except the second.
     *         Always 0L or greater.
     */
    public abstract long PARAMETER_BITMAP ();


    /**
     * <p>
     * Filters the appropriate parameters from the specified
     * array according to the specified per-parameter Filter,
     * keeping all parameters if the one(s) affected
     * by this AbstractMultipleParameters are all kept by the specified Filter,
     * or discarding them all if one or more parameter(s) affected
     * by this AbstractMultipleParameters are not kept by the specified Filter.
     * </p>
     *
     * @param filter The filter to apply to the parameter(s)
     *               affected by this AbstractMultipleParameters.
     *               If null then the parameters are KEPT.
     *
     * @param parameters All parameters.  Only the parameter(s)
     *                   affected by this AbstractMultipleParameters
     *                   will have the Filter applied; all other
     *                   parameters will be kept.
     *                   Can be null.  Can be empty.  Can contain nulls.
     *
     * @return The FilterState from filtering the parameter(s)
     *         under this AbstractMultipleParameters.  Never null.
     */
    public final FilterState filterParameters (
            Filter<?> filter,
            Object ... parameters
            )
    {
        if ( filter == null )
        {
            // No filter.
            // Keep.
            return FilterState.KEPT;
        }

        final long parameter_bitmap = this.PARAMETER_BITMAP ();
        if ( parameter_bitmap == 0L )
        {
            // This inverts the logic.  Any parameter that passes
            // the filter causes the lot to be DISCARDED.
            if ( parameters == null
                 || parameters.length == 0 )
            {
                // No parameters, and no parameters must be kept
                // by the filter, so
                // Keep.
                return FilterState.KEPT;
            }

            for ( Object parameter : parameters )
            {
                final FilterState filter_state =
                    this.filterOneParameter ( filter, parameter );
                if ( filter_state.isKept () )
                {
                    // None of the parameters is supposed to match
                    // the filter, but at least one did.
                    // Discard.
                    return filter_state.opposite ();
                }
            }

            // None of the parameters matched the filter, which is desired.
            // Keep.
            return FilterState.KEPT;
        }
        else if ( parameters == null )
        {
            // No parameters, but at least one must be kept by the filter.
            // Discard.
            return FilterState.DISCARDED;
        }

        long remaining_bits = parameter_bitmap;
        for ( int p = 0; p < 63; p ++ )
        {
            final long bit = remaining_bits & 1L;

            if ( bit == 1L )
            {
                if ( p > parameters.length )
                {
                    // Not enough parameters.
                    // Discard.
                    return FilterState.DISCARDED;
                }

                final Object parameter = parameters [ p ];
                final FilterState filter_state =
                    this.filterOneParameter ( filter, parameter );

                if ( ! filter_state.isKept () )
                {
                    // An affected parameter did not pass the filter.
                    // Discard.
                    return filter_state;
                }
            }

            remaining_bits >>= 1;
            if ( remaining_bits == 0L )
            {
                // No more affected parameters.
                // Keep.
                break;
            }
        }

        // All affected parameter(s) were kept by the filter.
        // Keep.
        return FilterState.KEPT;
    }


    // Filters one parameter, being careful not to let exceptions
    // unravel the stack because AbstractMultipleParameters filtering is fundamental
    // to Musaico's contract safety.
    @SuppressWarnings("unchecked") // Cast Object to PARAMETER in try...catch
    private final <PARAMETER extends Object>
        FilterState filterOneParameter (
            Filter<PARAMETER> filter,
            Object parameter
            )
    {
        if ( filter == null )
        {
            return FilterState.DISCARDED;
        }

        try
        {
            final FilterState filter_state =
                filter.filter ( (PARAMETER) parameter );
            if ( filter_state == null )
            {
                return FilterState.DISCARDED;
            }

            return filter_state;
        }
        catch ( Throwable t )
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.parameterAsString;
    }


    /**
     * <p>
     * Boilerplate code for obligations to callers of methods
     * and constructors.
     * </p>
     *
     * <p>
     * This class exposes usable Contract methods while simplifying
     * the implementation requirements for the derived obligation,
     * and providing some helper functionality for users of
     * parameter obligations.
     * </p>
     *
     * <p>
     * The <code> parameter () </code>
     * and <code> filterParameters ( ... ) </code>
     * methods of AbstractObligation do make it easier to check
     * parameters against a contract:
     * </p>
     *
     * <pre>
     *     Object [] parameters = ...;
     *     AbstractObligation<...> obligation = ...;
     *     if ( ! obligation.filterParameters ( parameters ).isKept () )
     *     {
     *         throw obligation.violation ( ... );
     *     }
     * </pre>
     */
    public static abstract class AbstractObligation<CONTRACT extends AbstractObligation<CONTRACT, EVIDENCE, VIOLATION>, EVIDENCE extends Object, VIOLATION extends UncheckedViolation>
        implements Contract<EVIDENCE, VIOLATION>, Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        // The parameter that is under obligation.  Perhaps Parameter1
        // must meet certain criteria, or Parameter 3, or
        // every parameter (Parameters), and so on.
        private final AbstractMultipleParameters parameter;

        /**
         * <p>
         * Creates a new AbstractObligation for a parameter.
         * </p>
         *
         * @param parameter Either a specific parameter (Parameter1,
         *                  Parameter2, and so on), or every
         *                  parameter (AbstractMultipleParameters), or some more
         *                  complex combination.  Must not be null.
         *
         * @throws NullPointerException If the specified parameter is null.
         *                              AbstractMultipleParameters.AbstractObligation
         *                              is critical to the functioning
         *                              of Musaico, so allowing nulls
         *                              to creep in here would be
         *                              disastrous.
         */
        public AbstractObligation (
            AbstractMultipleParameters parameter
            )
            throws NullPointerException
        {
            if ( parameter == null )
            {
                throw new NullPointerException ( "AbstractMultipleParameters.AbstractObligation must not be created with a null AbstractMultipleParameters: "
                    + parameter );
            }

            this.parameter = parameter;
        }

        /**
         * <p>
         * Filters the specified parameters according to this
         * obligation Contract, keeping them if they all pass the
         * obligation, or discarding them if one or more parameter(s)
         * do not pass the obligation.
         * </p>
         *
         * @param parameters All parameters.  Only the parameter(s)
         *                   affected by this obligation will have the
         *                   contractual filter applied; all other
         *                   parameters will be ignored.
         *                   Can be null.  Can be empty.  Can contain nulls.
         *
         * @return The FilterState from filtering the parameter(s)
         *         under obligation.  Never null.
         */
        public final FilterState filterParameters (
                Object ... parameters
                )
        {
            return this.parameter.filterParameters ( this, parameters );
        }

        /**
         * @return The parameter under contract, such as
         *         Parameter1 or Parameter2 or every parameter.
         *         Never null.
         */
        public final AbstractMultipleParameters parameter ()
        {
            return this.parameter;
        }

        @Override
        public final VIOLATION violation (
                Object plaintiff,
                EVIDENCE evidence_or_null
                )
        {
            String message_with_evidence =
                this.violationMessageWithEvidence (
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    null );           // cause_or_null

            final VIOLATION violation =
                this.violation (
                    plaintiff,               // plaintiff
                    evidence_or_null,        // evidence_or_null
                    message_with_evidence ); // message_with_evidence

            // No cause to initialize.

            return violation;
        }

        @Override
        public final VIOLATION violation (
                Object plaintiff,
                EVIDENCE evidence_or_null,
                Throwable cause_or_null
                )
        {
            String message_with_evidence =
                this.violationMessageWithEvidence (
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    cause_or_null );  // cause_or_null

            final VIOLATION violation =
                this.violation (
                    plaintiff,               // plaintiff
                    evidence_or_null,        // evidence_or_null
                    message_with_evidence ); // message_with_evidence

            if ( cause_or_null != null )
            {
                violation.initCause ( cause_or_null );
            }

            return violation;
        }


        /**
         * <p>
         * Creates a new Violation of this Contract.
         * </p>
         *
         * @param plaintiff The object under contract.  For
         *                  example, if object x has a method
         *                  obligation "parameters must not be
         *                  null" and someone calls x.method ( p )
         *                  with parameter p = null, then object x
         *                  would be the object under contract.
         *                  Must not be null.
         *
         * @param evidence_or_null The evidence that violated
         *                         this contract.  For
         *                         example, if object x has a method
         *                         obligation "parameters must not be
         *                         null" and someone calls x.method ( p )
         *                         with parameter p = null, then parameter p
         *                         would be the evidence.
         *                         Can be null.  Can contain null elements.
         *
         * @param message The exact message of the violation to be
         *                constructed.  Must not be null.
         *
         * @throws NullPointerException If any of the non-nullable
         *                              parameters are null.
         *
         * @return The newly created violation of this Contract.
         *         Never null.
         */
        protected abstract VIOLATION violation (
                Object plaintiff,
                EVIDENCE evidence_or_null,
                String message
                );

        /**
         * <p>
         * Creates the error message for a violation of this contract.
         * </p>
         *
         * @param plaintiff The object under contract.  For
         *                  example, if object x has a method
         *                  obligation "parameters must not be
         *                  null" and someone calls x.method ( p )
         *                  with parameter p = null, then object x
         *                  would be the object under contract.
         *                  Must not be null.
         *
         * @param evidence_or_null The evidence that violated
         *                         this contract.  For
         *                         example, if object x has a method
         *                         obligation "parameters must not be
         *                         null" and someone calls x.method ( p )
         *                         with parameter p = null, then parameter p
         *                         would be the evidence.
         *                         Can be null.  Can contain null elements.
         *
         * @param cause_or_null The Exception, contract Violation
         *                      or other Throwable which led to
         *                      this contract's violation, if any.
         *                      Can be null.
         *
         * @throws NullPointerException If any of the non-nullable
         *                              parameters are null.
         *
         * @return The exception message used to construct a violation
         *         of this Contract for the specified plaintiff,
         *         evidence and optional cause.  Never null.
         */
        protected abstract String violationMessage (
                Object plaintiff,
                EVIDENCE evidence_or_null,
                Throwable cause_or_null
                );


        protected String violationMessageWithEvidence (
                Object plaintiff,
                EVIDENCE evidence_or_null,
                Throwable cause_or_null
                )
        {
            final String message =
                this.violationMessage ( plaintiff,        // plaintiff
                                        evidence_or_null, // evidence_or_null
                                        null );           // cause_or_null
            final String evidence_string =
                StringRepresentation.of ( evidence_or_null, // object
                                          StringRepresentation.DEFAULT_ARRAY_LENGTH ); // max_length
            final String maybe_evidence_class_name;
            if ( evidence_or_null == null )
            {
                maybe_evidence_class_name = "";
            }
            else
            {
                maybe_evidence_class_name = " [" + ClassName.of ( evidence_or_null ) + "]";
            }

            final StringBuilder sbuf = new StringBuilder ();
            sbuf.append ( message );
            sbuf.append ( ": " );
            sbuf.append ( evidence_string );
            sbuf.append ( maybe_evidence_class_name );
            final String message_with_evidence = sbuf.toString ();
            return message_with_evidence;
        }
    }


    /**
     * <p>
     * The parameter must belong to some arbitrary domain.
     * Typically a method declares this when there is no existing
     * declarative obligation to support its parameter contract.
     * For example, if parameter1 must belong to domain
     * Has4Wheels, then the method might declare
     * AbstractMultipleParameters.MustBeInDomain,
     * check parameter1 against a new
     * AbstractMultipleParameters.MustBeInDomain ( Has4Wheels.CONTRACT )
     * at runtime, and explain the nature of the domain
     * in the method documentation.
     * The MustBeInDomain contract should be a last resort, since
     * the exception thrown by the method reveals nothing about
     * the domain to which the parameter is bound.  For example,
     * throwing Has4Wheels.Violation instead of
     * AbstractMultipleParameters.AlwaysInDomain.Violation
     * reveals more to the caller about the nature of the domain,
     * and provides an assertion filter for unit testing.
     * </p>
     *
     * @see musaico.foundation.filter
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeInDomain<DOMAIN extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeInDomain<DOMAIN, VIOLATION>, DOMAIN[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final Filter<DOMAIN> domain;

        public MustBeInDomain (
                AbstractMultipleParameters parameter,
                Filter<DOMAIN> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must belong to the domain "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                DOMAIN [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( DOMAIN grain : grains )
            {
                final FilterState element_filter_state =
                     this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                DOMAIN [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " does not belong to the domain";
        }
    }




    /**
     * <p>
     * The parameter must not be null.
     * </p>
     *
     * @see musaico.foundation.filter.NotNullFilter
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustNotBeNull<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustNotBeNull<VIOLATION>, Object[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustNotBeNull (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not be null.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Object [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object grain : grains )
            {
                final FilterState element_filter_state =
                    NotNullFilter.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is null";
        }
    }




    /**
     * <p>
     * The parameter must change from the start of some operation
     * to the end.
     * </p>
     *
     * <p>
     * The <code> { before, after } </code> pair of values
     * is checked.  For example, if before is 1, and after is 2, then
     * this contract is violated.  Objects which do not change
     * external references, but which have internal
     * mutable state (such as Lists, Maps, arrays, and so on),
     * capture the state at a particular time when wrapped in
     * a HashedObject.
     * </p>
     *
     * @see musaico.foundation.filter.time.Changing
     * @see musaico.foundation.filter.time.BeforeAndAfter
     * @see musaico.foundation.filter.HashedObject
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustChange<CHANGED extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustChange<CHANGED, VIOLATION>, BeforeAndAfter<CHANGED>[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final Changing<CHANGED> changing;

        public MustChange (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );

            this.changing = new Changing<CHANGED> ();
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must change over time.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                BeforeAndAfter<CHANGED> [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( BeforeAndAfter<CHANGED> grain : grains )
            {
                final FilterState element_filter_state =
                    this.changing.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                BeforeAndAfter<CHANGED> [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " did not change over time";
        }
    }





    /**
     * <p>
     * The parameter must not change from the start of some operation
     * to the end.
     * </p>
     *
     * <p>
     * The <code> { before, after } </code> pair of values
     * is checked.  For example, if before is 1, and after is 2, then
     * this contract is violated.  Objects which do not change
     * external references, but which have internal
     * mutable state (such as Lists, Maps, arrays, and so on),
     * capture the state at a particular time when wrapped in
     * a HashedObject.
     * </p>
     *
     * @see musaico.foundation.filter.time.Unchanging
     * @see musaico.foundation.filter.time.BeforeAndAfter
     * @see musaico.foundation.filter.HashedObject
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustNotChange<UNCHANGED extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustNotChange<UNCHANGED, VIOLATION>, BeforeAndAfter<UNCHANGED>[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final Unchanging<UNCHANGED> unchanging;

        public MustNotChange (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );

            this.unchanging = new Unchanging<UNCHANGED> ();
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not change over time.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                BeforeAndAfter<UNCHANGED> [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( BeforeAndAfter<UNCHANGED> grain : grains )
            {
                final FilterState element_filter_state =
                    this.unchanging.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                BeforeAndAfter<UNCHANGED> [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " changed";
        }
    }




    /**
     * <p>
     * The parameter must equal a specific value.  For example,
     * a method accepting an int parameter might require that the
     * input be any value except 42.
     * </p>
     *
     * @see musaico.foundation.filter.equality.EqualTo
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustEqual<EQUAL extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustEqual<EQUAL, VIOLATION>, EQUAL[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final EqualTo<EQUAL> domain;

        public MustEqual (
                AbstractMultipleParameters parameter,
                EQUAL object
                )
        {
            this ( parameter,
                   new EqualTo<EQUAL> ( object ) );
        }

        public MustEqual (
                AbstractMultipleParameters parameter,
                EqualTo<EQUAL> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must equal "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                EQUAL [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( EQUAL grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                EQUAL [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not equal to the required value";
        }
    }




    /**
     * <p>
     * The parameter must not equal a specific value.  For example,
     * a method accepting an int parameter might require that the
     * input be any value except 42.
     * </p>
     *
     * @see musaico.foundation.filter.equality.NotEqualTo
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustNotEqual<UNEQUAL extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustNotEqual<UNEQUAL, VIOLATION>, UNEQUAL[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final NotEqualTo<UNEQUAL> domain;

        public MustNotEqual (
                AbstractMultipleParameters parameter,
                UNEQUAL object
                )
        {
            this ( parameter,
                   new NotEqualTo<UNEQUAL> ( object ) );
        }

        public MustNotEqual (
                AbstractMultipleParameters parameter,
                NotEqualTo<UNEQUAL> domain
                )
        {
            super ( parameter );
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not equal "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                UNEQUAL [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( UNEQUAL grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                UNEQUAL [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is equal to the verboten value";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than 1.
     * </p>
     *
     * @see musaico.foundation.filter.GreaterThanNegativeOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanNegativeOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanNegativeOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeGreaterThanNegativeOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be greater than -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    GreaterThanNegativeOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not greater than -1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than 1.
     * </p>
     *
     * @see musaico.foundation.filter.GreaterThanOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeGreaterThanOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be greater than 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    GreaterThanOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not greater than 1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than 0.
     * </p>
     *
     * @see musaico.foundation.filter.GreaterThanZero
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanZero<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanZero<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeGreaterThanZero (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be greater than 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    GreaterThanZero.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not greater than 0";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than or
     * equal to -1.
     * </p>
     *
     * @see musaico.foundation.filter.GreaterThanOrEqualToNegativeOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanOrEqualToNegativeOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanOrEqualToNegativeOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeGreaterThanOrEqualToNegativeOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be >= -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    GreaterThanOrEqualToNegativeOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not >= -1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than or
     * equal to 1.
     * </p>
     *
     * @see musaico.foundation.filter.GreaterThanOrEqualToOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanOrEqualToOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanOrEqualToOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeGreaterThanOrEqualToOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be >= 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    GreaterThanOrEqualToOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not >= 1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than or
     * equal to 0.
     * </p>
     *
     * @see musaico.foundation.filter.GreaterThanOrEqualToZero
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanOrEqualToZero<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanOrEqualToZero<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeGreaterThanOrEqualToZero (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be >= 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    GreaterThanOrEqualToZero.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not >= 0";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than -1.
     * </p>
     *
     * @see musaico.foundation.filter.LessThanNegativeOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanNegativeOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanNegativeOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeLessThanNegativeOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be < -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    LessThanNegativeOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not < -1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than 1.
     * </p>
     *
     * @see musaico.foundation.filter.LessThanOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeLessThanOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be < 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    LessThanOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not < 1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than 0.
     * </p>
     *
     * @see musaico.foundation.filter.LessThanZero
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanZero<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanZero<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeLessThanZero (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be < 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    LessThanZero.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not < 0";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than or equal
     * to -1.
     * </p>
     *
     * @see musaico.foundation.filter.LessThanOrEqualToNegativeOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanOrEqualToNegativeOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanOrEqualToNegativeOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeLessThanOrEqualToNegativeOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be <= -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    LessThanOrEqualToNegativeOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not <= -1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than or equal
     * to 1.
     * </p>
     *
     * @see musaico.foundation.filter.LessThanOrEqualToOne
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanOrEqualToOne<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanOrEqualToOne<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeLessThanOrEqualToOne (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be less than or equal to 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    LessThanOrEqualToOne.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not <= 1";
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than or equal
     * to 0.
     * </p>
     *
     * @see musaico.foundation.filter.LessThanOrEqualToZero
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanOrEqualToZero<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanOrEqualToZero<VIOLATION>, Number[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeLessThanOrEqualToZero (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }


        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be <= 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Number [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Number grain : grains )
            {
                final FilterState element_filter_state =
                    LessThanOrEqualToZero.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Number [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not <= 0";
        }
    }




    /**
     * <p>
     * The parameter must be greater than a specific number.
     * </p>
     *
     * @see musaico.foundation.filter.number.GreaterThanNumber
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThan<NUMBER extends Number, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThan<NUMBER, VIOLATION>, NUMBER [], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final GreaterThanNumber<NUMBER> domain;

        public MustBeGreaterThan (
                AbstractMultipleParameters parameter,
                NUMBER number
                )
        {
            this ( parameter,
                   new GreaterThanNumber<NUMBER> ( number ) );
        }

        public MustBeGreaterThan (
                AbstractMultipleParameters parameter,
                GreaterThanNumber<NUMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                NUMBER [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( NUMBER grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is out of range";
        }
    }




    /**
     * <p>
     * The parameter must be greater than or equal to a specific number.
     * </p>
     *
     * @see musaico.foundation.filter.number.GreaterThanOrEqualToNumber
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeGreaterThanOrEqualTo<NUMBER extends Number, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeGreaterThanOrEqualTo<NUMBER, VIOLATION>, NUMBER [], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final GreaterThanOrEqualToNumber<NUMBER> domain;

        public MustBeGreaterThanOrEqualTo (
                AbstractMultipleParameters parameter,
                NUMBER number
                )
        {
            this ( parameter,
                   new GreaterThanOrEqualToNumber<NUMBER> ( number ) );
        }

        public MustBeGreaterThanOrEqualTo (
                AbstractMultipleParameters parameter,
                GreaterThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                NUMBER [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( NUMBER grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is out of range";
        }
    }




    /**
     * <p>
     * The parameter must be less than a specific number.
     * </p>
     *
     * @see musaico.foundation.filter.number.LessThanNumber
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThan<NUMBER extends Number, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThan<NUMBER, VIOLATION>, NUMBER [], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final LessThanNumber<NUMBER> domain;

        public MustBeLessThan (
                AbstractMultipleParameters parameter,
                NUMBER number
                )
        {
            this ( parameter,
                   new LessThanNumber<NUMBER> ( number ) );
        }

        public MustBeLessThan (
                AbstractMultipleParameters parameter,
                LessThanNumber<NUMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                NUMBER [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( NUMBER grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is out of range";
        }
    }




    /**
     * <p>
     * The parameter must be less than or equal to a specific number.
     * </p>
     *
     * @see musaico.foundation.filter.number.LessThanOrEqualToNumber
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeLessThanOrEqualTo<NUMBER extends Number, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeLessThanOrEqualTo<NUMBER, VIOLATION>, NUMBER [], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final LessThanOrEqualToNumber<NUMBER> domain;

        public MustBeLessThanOrEqualTo (
                AbstractMultipleParameters parameter,
                NUMBER number
                )
        {
            this ( parameter,
                   new LessThanOrEqualToNumber<NUMBER> ( number ) );
        }

        public MustBeLessThanOrEqualTo (
                AbstractMultipleParameters parameter,
                LessThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                NUMBER [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( NUMBER grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is out of range";
        }
    }




    /**
     * <p>
     * The parameter must be between some minimum number and some
     * maximum number (inclusive or exclusive, depending on the
     * BoundedFilter.EndPoints used).
     * </p>
     *
     * @see musaico.foundation.filter.comparability.BoundedFilter
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeBetween<NUMBER extends Number, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeBetween<NUMBER, VIOLATION>, NUMBER [], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final BoundedFilter<NUMBER> domain;

        /** Closed bounds: [ minimum_number, maximum_number ]. */
        public MustBeBetween (
                AbstractMultipleParameters parameter,
                NUMBER minimum_number,
                NUMBER maximum_number
                )
        {
            this ( parameter,
                   BoundedFilter.EndPoint.CLOSED,
                   minimum_number,
                   BoundedFilter.EndPoint.CLOSED,
                   maximum_number );
        }

        public MustBeBetween (
                AbstractMultipleParameters parameter,
                BoundedFilter.EndPoint minimum_end_point,
                NUMBER minimum_number,
                BoundedFilter.EndPoint maximum_end_point,
                NUMBER maximum_number
                )
        {
            this ( parameter,
                   BoundedFilter.overNumbers (
                    minimum_end_point,
                    minimum_number,
                    maximum_end_point,
                    maximum_number                    ) );
        }

        public MustBeBetween (
                AbstractMultipleParameters parameter,
                BoundedFilter<NUMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be of "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                NUMBER [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( NUMBER grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is out of range";
        }
    }




    /**
     * <p>
     * The parameter is a comparable value which must be
     * within specific (minimum value, maximum value) bounds.
     * For example if a method pH ( value ) accepts a BigDecimal value, then
     * it might enforce the obligation
     * <code> new ParameterX.MustBeInBounds ( BigDecimal.ZERO,
     *                                        new BigDecimal ( "14" ) ) </code>.
     * Ideally this obligation would be a new declarative class
     * which extends ParameterX.MustBeInBounds, such that the method
     * could declare <code> throws Between0And14.Violation </code>.
     * The declarative approach provides easy documentation
     * and provides an easy to use assertion filter for unit testing.
     * </p>
     *
     * @see musaico.foundation.filter.comparability.BoundedFilter
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeInBounds<BOUNDED extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeInBounds<BOUNDED, VIOLATION>, BOUNDED[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final BoundedFilter<BOUNDED> domain;

        /** Closed bounds: [ min, max ]. */
        public MustBeInBounds (
                AbstractMultipleParameters parameter,
                Comparator<BOUNDED> comparator,
                BOUNDED min,
                BOUNDED max
                )
        {
            this ( parameter,
                   comparator,
                   BoundedFilter.EndPoint.CLOSED,
                   min,
                   BoundedFilter.EndPoint.CLOSED,
                   max );
        }

        public MustBeInBounds (
                AbstractMultipleParameters parameter,
                Comparator<BOUNDED> comparator,
                BoundedFilter.EndPoint min_end_point,
                BOUNDED min,
                BoundedFilter.EndPoint max_end_point,
                BOUNDED max
                )
        {
            this ( parameter,
                   new BoundedFilter<BOUNDED> (
                    comparator,
                    min_end_point,
                    min,
                    max_end_point,
                    max                    ) );
        }

        public MustBeInBounds (
                AbstractMultipleParameters parameter,
                BoundedFilter<BOUNDED> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be in "
                + this.domain;
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                BOUNDED [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( BOUNDED grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " "
                + this.domain;
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                BOUNDED [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is out of bounds";
        }
    }




    /**
     * <p>
     * The parameter must be an instance of a specific class.
     * For example, a method accepting a Number parameter
     * might require that the parameter be an instance of
     * Integer.class.
     * </p>
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeInstanceOf<INSTANCE extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeInstanceOf<INSTANCE, VIOLATION>, INSTANCE[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final InstanceOf<INSTANCE> domain;

        public MustBeInstanceOf (
                AbstractMultipleParameters parameter,
                Class<?> domain_class
                )
        {
            this ( parameter,
                   new InstanceOf<INSTANCE> ( domain_class ) );
        }

        public MustBeInstanceOf (
                AbstractMultipleParameters parameter,
                InstanceOf<INSTANCE> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @return The Class(es) which are allowed.  The parameter must be
         *         an instance (or instances) of one of these classes.
         *         Never null.  Never contains any null elements.
         *
         * @see musaico.foundation.filter.membership.InstanceOf#classesToKeep()
         */
        public final Class<?> [] allowedClasses ()
        {
            return this.domain.classesToKeep ();
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be an "
                + this.domain
                + ".";
        }


        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                INSTANCE [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( INSTANCE grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                INSTANCE [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not an instance of the required class(es)";
        }
    }




    /**
     * <p>
     * The parameter must not be an instance of a specific class.
     * For example, a method accepting a Number parameter
     * might require that the parameter is not an instance of
     * BigInteger.class.
     * </p>
     *
     * @see musaico.foundation.filter.membership.InstanceOf
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustNotBeInstanceOf<INSTANCE extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustNotBeInstanceOf<INSTANCE, VIOLATION>, INSTANCE[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final InstanceOf<INSTANCE> verbotenDomain;

        public MustNotBeInstanceOf (
                AbstractMultipleParameters parameter,
                Class<?> verboten_class
                )
        {
            this ( parameter,
                   new InstanceOf<INSTANCE> ( verboten_class ) );
        }

        public MustNotBeInstanceOf (
                AbstractMultipleParameters parameter,
                InstanceOf<INSTANCE> verboten_domain
                )
        {
            super ( parameter );

            this.verbotenDomain = verboten_domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not be an "
                + this.verbotenDomain
                + ".";
        }

        /**
         * @return The verboten Class(es).  The parameter must not be
         *         an instance (or instances) of any of these classes.
         *         Never null.  Never contains any null elements.
         *
         * @see musaico.foundation.filter.membership.InstanceOf#classesToKeep()
         */
        public final Class<?> [] disallowedClasses ()
        {
            return this.verbotenDomain.classesToKeep ();
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                INSTANCE [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( INSTANCE grain : grains )
            {
                final FilterState element_filter_state =
                    this.verbotenDomain.filter ( grain )
                                       .opposite ();
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.verbotenDomain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                INSTANCE [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is an instance of the verboten class(es)";
        }
    }




    /**
     * <p>
     * The parameter is a String value which must always be the
     * empty String ("").
     * </p>
     *
     * @see musaico.foundation.filter.EmptyString
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeEmptyString<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeEmptyString<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeEmptyString (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be the empty String \"\".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    EmptyString.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not an empty String";
        }
    }




    /**
     * <p>
     * The parameter is a String which must not be the empty
     * String ("").
     * </p>
     *
     * @see musaico.foundation.filter.NotEmptyString
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustNotBeEmptyString<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustNotBeEmptyString<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustNotBeEmptyString (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not be empty.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    NotEmptyString.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is an empty String";
        }
    }




    /**
     * <p>
     * The parameter is a String which must be either an exact length or
     * a String length in some range.  For example, a
     * hexStringToSHA1 ( parameter ) method might require a String
     * parameter exactly 40 characters long.  On the other hand,
     * a surname ( parameter ) method might require a String parameter
     * at least one character long.  Or a upcECode ( parameter )
     * method might require a String parameter between 5 and 6
     * characters long.  And so on.
     * </p>
     *
     * @see musaico.foundation.filter.StringLength
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeStringLength<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeStringLength<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final StringLength domain;

        public MustBeStringLength (
                AbstractMultipleParameters parameter,
                int exact_length
                )
        {
            this ( parameter,
                   new StringLength ( exact_length ) );
        }

        public MustBeStringLength (
                AbstractMultipleParameters parameter,
                int minimum_length,
                int maximum_length
                )
        {
            this ( parameter,
                   new StringLength ( minimum_length, maximum_length ) );
        }

        public MustBeStringLength (
                AbstractMultipleParameters parameter,
                StringLength domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be between "
                + this.domain
                + " characters long.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " [ "
                + this.domain
                + " ]";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is the wrong length ("
                + ( evidence_or_null == null
                        ? -1
                        : ("" + evidence_or_null).length () )
                + " characters)";
        }
    }




    /**
     * <p>
     * The parameter is a String which must not contain any whitespace
     * characters in any alphabet, such as ' ', '\t', '\n', '\r'
     * and so on.
     * </p>
     *
     * @see musaico.foundation.filter.StringExcludesSpaces
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustExcludeSpaces<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustExcludeSpaces<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustExcludeSpaces (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not contain any whitespace characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringExcludesSpaces.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains "
                + ( evidence_or_null == null
                        ? -1
                        : ( "" + evidence_or_null )
                              .replaceAll ( "[^\\s]+", "" )
                              .length () ) // # whitespace chars
                + " whitespace characters";
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain at least one
     * non-whitespace character in any alphabet (at least one character
     * other than ' ', '\t', '\n', '\r' and so on).
     * </p>
     *
     * @see musaico.foundation.filter.ContainsNonSpaces
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainNonSpaces<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainNonSpaces<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustContainNonSpaces (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must contain at least one non-whitespace"
                + " character.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringContainsNonSpaces.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " does not contain any non-whitespace"
                + " characters";
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only numeric
     * digits in any alphabet ('1', '2', '3' and so on).
     * </p>
     *
     * @see musaico.foundation.filter.StringContainsOnlyNumerics
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyNumerics<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyNumerics<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustContainOnlyNumerics (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must contain only numeric digits.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringContainsOnlyNumerics.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains characters other than"
                + " numerals: "
                + ( evidence_or_null == null // description
                        ? "<null>"
                        : ( "" + evidence_or_null )
                              .replaceAll ( "[^\\d]+", "" ) );
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only letters
     * from any alphabet ('a', 'A', 'b' and so on).
     * </p>
     *
     * @see musaico.foundation.filter.StringContainsOnlyAlpha
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyAlpha<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyAlpha<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustContainOnlyAlpha (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must contain only alphabetical characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringContainsOnlyAlpha.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains non-alphabetical characters: "
                + ( evidence_or_null == null // description
                        ? "<null>"
                        : ( "" + evidence_or_null )
                              .replaceAll ( "[\\p{Alpha}]+", "" ) );
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only letters
     * from any alphabet ('a', 'A', 'b' and so on) and/or
     * numeric digits from any alphabet ('1', '2', '3' and so on).
     * </p>
     *
     * @see musaico.foundation.filter.StringContainsOnlyAlphaNumerics
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyAlphaNumerics<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyAlphaNumerics<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustContainOnlyAlphaNumerics (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must contain only alphabetical and/or numeric"
                + " characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringContainsOnlyAlphaNumerics.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains non-alphanumeric characters: "
                + ( evidence_or_null == null // description
                        ? "<null>"
                        : ( "" + evidence_or_null )
                              .replaceAll ( "[\\p{Alnum}]+", "" ) );
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only printable
     * characters in any alphabet, excluding control characters.
     * </p>
     *
     * @see musaico.foundation.filter.StringContainsOnlyPrintableCharacters
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyPrintableCharacters<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyPrintableCharacters<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustContainOnlyPrintableCharacters (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must contain only printable characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringContainsOnlyPrintableCharacters.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains non-printable character(s) at "
                + nonPrintableCharacterIndices ( "" + evidence_or_null );
        }

        private static final String nonPrintableCharacterIndices (
                String text
                )
        {
            if ( text == null )
            {
                return "<null>";
            }

            final StringBuilder indices = new StringBuilder ();

            final Pattern nonPrintable =
                Pattern.compile ( "[^\\p{Print}]" );
            final Matcher matcher = nonPrintable.matcher ( text );
            boolean is_first = true;
            for ( int c = matcher.start ();
                  matcher.find ( c );
                  c ++ )
            {
                if ( is_first )
                {
                    is_first = false;
                }
                else
                {
                    indices.append ( ", " );
                }

                indices.append ( "" + c );
            }

            return indices.toString ();
        }
    }




    /**
     * <p>
     * The parameter must meet the requirements of
     * the String ID domain.  Every ID starts with an ASCii letter
     * 'a', 'A', 'b', and so on, or an underscore '_'.  Subsequent
     * characters in the ID may be ASCii letters, underscore,
     * or ASCii digits '1', '2', '3', and so on.  An ID must
     * be at least one character long.  IDs are based on
     * the case-sensitive names of methods, procedures, variables
     * and so on in many programming languages.
     * </p>
     *
     * @see musaico.foundation.filter.StringID
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeStringID<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeStringID<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        public MustBeStringID (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must contain only alphanumerics and/or"
                + " underscores (_), and must not start with a numeric.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    StringID.FILTER.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not an ID";
        }
    }




    /**
     * <p>
     * The parameter is a String which must match some regular
     * expression pattern.
     * </p>
     *
     * @see musaico.foundation.filter.StringPattern
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustMatchPattern<VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustMatchPattern<VIOLATION>, String[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final StringPattern domain;

        public MustMatchPattern (
                AbstractMultipleParameters parameter,
                Pattern pattern
                )
        {
            this ( parameter,
                   new StringPattern ( pattern ) );
        }

        public MustMatchPattern (
                AbstractMultipleParameters parameter,
                StringPattern domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must match the regular expression "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                String [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( String grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                String [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " does not match the regular expression";
        }
    }




    /**
     * <p>
     * The parameter is an element of a specific array or Collection
     * or other Iterable.  The element may be at any position,
     * and may occur more than once, but must be present at least once.
     * </p>
     *
     * @see musaico.foundation.filter.membership.MemberOf
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustBeMemberOf<MEMBER extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustBeMemberOf<MEMBER, VIOLATION>, Object[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final MemberOf<MEMBER> domain;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public MustBeMemberOf (
                AbstractMultipleParameters parameter,
                MEMBER ... array
                )
        {
            this ( parameter,
                   new MemberOf<MEMBER> ( array ) );
        }

        public MustBeMemberOf (
                AbstractMultipleParameters parameter,
                Collection<MEMBER> collection
                )
        {
            this ( parameter,
                   new MemberOf<MEMBER> ( collection ) );
        }

        public MustBeMemberOf (
                AbstractMultipleParameters parameter,
                Iterable<MEMBER> iterable
                )
        {
            this ( parameter,
                   new MemberOf<MEMBER> ( iterable ) );
        }

        public MustBeMemberOf (
                AbstractMultipleParameters parameter,
                MemberOf<MEMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must be an element of "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is not an element"
                + " of the required array or Collection or Iterable";
        }
    }




    /**
     * <p>
     * The parameter is not an element of a specific array or Collection
     * or other Iterable.
     * </p>
     *
     * @see musaico.foundation.filter.membership.MemberOf
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustNotBeMemberOf<MEMBER extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustNotBeMemberOf<MEMBER, VIOLATION>, MEMBER[], VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final Not<MEMBER> domain;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public MustNotBeMemberOf (
                AbstractMultipleParameters parameter,
                MEMBER ... array
                )
        {
            this ( parameter,
                   new MemberOf<MEMBER> ( array ) );
        }

        public MustNotBeMemberOf (
                AbstractMultipleParameters parameter,
                Collection<MEMBER> collection
                )
        {
            this ( parameter,
                   new MemberOf<MEMBER> ( collection ) );
        }

        public MustNotBeMemberOf (
                AbstractMultipleParameters parameter,
                Iterable<MEMBER> iterable
                )
        {
            this ( parameter,
                   new MemberOf<MEMBER> ( iterable ) );
        }

        public MustNotBeMemberOf (
                AbstractMultipleParameters parameter,
                MemberOf<MEMBER> membership
                )
        {
            super ( parameter );

            this.domain = new Not<MEMBER> ( membership );
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not be an element of "
                + this.domain.negatedFilter ()
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                MEMBER [] grains
                )
        {
            if ( grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( MEMBER grain : grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain.negatedFilter ()
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " is a member of the verboten set";
        }
    }


    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * including every member of a certain set of values.
     * The members may appear in the container in any order,
     * but each value must be present at least once.
     * </p>
     *
     * @see musaico.foundation.filter.IncludesMembers
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainMembers<MEMBER extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainMembers<MEMBER, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final IncludesMembers<MEMBER> domain;

        @SuppressWarnings("unchecked") // Generic varargs.
        public MustContainMembers (
                AbstractMultipleParameters parameter,
                MEMBER ... members
                )
        {
            this ( parameter,
                   new IncludesMembers<MEMBER> ( members ) );
        }

        public MustContainMembers (
                AbstractMultipleParameters parameter,
                IncludesMembers<MEMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must include members of "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " does not contain any of the required members: "
                + this.domain.discards ( evidence_or_null, new ArrayList<MEMBER> () );
        }
    }


    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * including every member of a certain set of values,
     * and no elements outside that set.
     * The members may appear in the container in any order,
     * but each value must to be present at least once.
     * </p>
     *
     * @see musaico.foundation.filter.IncludesOnlyMembers
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyMembers<MEMBER extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyMembers<MEMBER, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final IncludesOnlyMembers<MEMBER> domain;

        @SuppressWarnings("unchecked") // Generic varargs.
        public MustContainOnlyMembers (
                AbstractMultipleParameters parameter,
                MEMBER ... members
                )
        {
            this ( parameter,
                   new IncludesOnlyMembers<MEMBER> ( members ) );
        }

        public MustContainOnlyMembers (
                AbstractMultipleParameters parameter,
                IncludesOnlyMembers<MEMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must include only members of "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains element(s) outside the required set: "
                + this.domain.discards ( evidence_or_null, new ArrayList<MEMBER> () );
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * which must exclude every member of a certain set of values.
     * </p>
     *
     * @see musaico.foundation.filter.ExcludesMembers
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustExcludeMembers<MEMBER extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustExcludeMembers<MEMBER, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final ExcludesMembers<MEMBER> domain;

        @SuppressWarnings("unchecked") // Possible heap pollution
        //                                generic varargs: MEMBER ...
        public MustExcludeMembers (
                AbstractMultipleParameters parameter,
                MEMBER ... members
                )
        {
            this ( parameter,
                   new ExcludesMembers<MEMBER> ( members ) );
        }

        public MustExcludeMembers (
                AbstractMultipleParameters parameter,
                Collection<MEMBER> members
                )
        {
            this ( parameter,
                   new ExcludesMembers<MEMBER> ( members ) );
        }

        public MustExcludeMembers (
                AbstractMultipleParameters parameter,
                Iterable<MEMBER> members
                )
        {
            this ( parameter,
                   new ExcludesMembers<MEMBER> ( members ) );
        }

        public MustExcludeMembers (
                AbstractMultipleParameters parameter,
                ExcludesMembers<MEMBER> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must exclude all members of "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains element(s) that must be excluded: "
                + this.domain.discards ( evidence_or_null, new ArrayList<MEMBER> () );
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * containing every index from a certain set of numbers.
     * The indices may be in any order, but each one must
     * be present, and each indexed element must not be null.
     * </p>
     *
     * @see musaico.foundation.filter.elements.IncludesIndices
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainIndices<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainIndices<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final AllIndicesFilter<ELEMENT> indicesFilter;

        public MustContainIndices (
                AbstractMultipleParameters parameter,
                long ... indices
                )
        {
            this ( parameter,
                   new IncludesIndices<ELEMENT> ( indices ) ); // All of the specified indices must be present.
        }

        public MustContainIndices (
                AbstractMultipleParameters parameter,
                int ... indices
                )
        {
            this ( parameter,
                   new IncludesIndices<ELEMENT> ( indices ) ); // All of the specified indices must be present.
        }

        public MustContainIndices (
                AbstractMultipleParameters parameter,
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( parameter );

            this.indicesFilter = indices_filter;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            final String indices_filter_string;
            if ( this.indicesFilter instanceof IncludesIndices )
            {
                final IncludesIndices<ELEMENT> includes_indices =
                    (IncludesIndices<ELEMENT>) this.indicesFilter;
                indices_filter_string = Arrays.toString ( includes_indices.indices () );
            }
            else
            {
                indices_filter_string = "matching "
                    + this.indicesFilter;
            }

            return "" + this.parameter () + " must include indices "
                + indices_filter_string
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.indicesFilter.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.indicesFilter
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " does not contain any required indices: "
                + this.indicesFilter.discards ( evidence_or_null, new ArrayList<ELEMENT> () );
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * containing every index from a certain set of numbers,
     * but not any indices from outside that set.
     * The indices may be in any order, but each one must
     * be present, and each indexed element must not be null.
     * </p>
     *
     * @see musaico.foundation.filter.elements.IncludesOnlyIndices
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyIndices<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyIndices<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final AllIndicesFilter<ELEMENT> indicesFilter;

        public MustContainOnlyIndices (
                AbstractMultipleParameters parameter,
                long ... indices
                )
        {
            this ( parameter,
                   new IncludesOnlyIndices<ELEMENT> ( indices ) ); // All of the specified indices must be present.
        }

        public MustContainOnlyIndices (
                AbstractMultipleParameters parameter,
                int ... indices
                )
        {
            this ( parameter,
                   new IncludesOnlyIndices<ELEMENT> ( indices ) ); // All of the specified indices must be present.
        }

        public MustContainOnlyIndices (
                AbstractMultipleParameters parameter,
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( parameter );

            this.indicesFilter = indices_filter;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            final String indices_filter_string;
            if ( this.indicesFilter instanceof IncludesOnlyIndices )
            {
                final IncludesOnlyIndices<ELEMENT> includes_only_indices =
                    (IncludesOnlyIndices<ELEMENT>) this.indicesFilter;
                indices_filter_string = Arrays.toString ( includes_only_indices.indices () );
            }
            else
            {
                indices_filter_string = "matching "
                    + this.indicesFilter;
            }

            return "" + this.parameter () + " must include only indices "
                + indices_filter_string
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.indicesFilter.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.indicesFilter
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains indices outside the required set: "
                + this.indicesFilter.discards ( evidence_or_null, new ArrayList<ELEMENT> () );
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * that does not contain any index from a certain set of numbers.
     * The indices may be in any order, but each one must either
     * not be present at all, or if it is present, it must index
     * a null element.
     * </p>
     *
     * @see musaico.foundation.filter.elements.ExcludesIndices
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustExcludeIndices<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustExcludeIndices<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final AllIndicesFilter<ELEMENT> indicesFilter;

        public MustExcludeIndices (
                AbstractMultipleParameters parameter,
                long ... indices
                )
        {
            this ( parameter,
                   new ExcludesIndices<ELEMENT> ( indices ) ); // All of the specified indices must be present.
        }

        public MustExcludeIndices (
                AbstractMultipleParameters parameter,
                int ... indices
                )
        {
            this ( parameter,
                   new ExcludesIndices<ELEMENT> ( indices ) ); // All of the specified indices must be present.
        }

        public MustExcludeIndices (
                AbstractMultipleParameters parameter,
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( parameter );

            this.indicesFilter = indices_filter;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            final String indices_filter_string;
            if ( this.indicesFilter instanceof ExcludesIndices )
            {
                final ExcludesIndices<ELEMENT> excludes_indices =
                    (ExcludesIndices<ELEMENT>) this.indicesFilter;
                indices_filter_string = Arrays.toString ( excludes_indices.indices () );
            }
            else
            {
                indices_filter_string = "matching "
                    + this.indicesFilter;
            }

            return "" + this.parameter () + " must exclude indices "
                + indices_filter_string
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.indicesFilter.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.indicesFilter
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains excluded indices: "
                + this.indicesFilter.discards ( evidence_or_null, new ArrayList<ELEMENT> () );
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must not contain any duplicate values.  Every element
     * must be unique.
     * </p>
     *
     * @see musaico.foundation.filter.NoDuplicates
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainNoDuplicates<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainNoDuplicates<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final NoDuplicates<ELEMENT> domain;

        public MustContainNoDuplicates (
                AbstractMultipleParameters parameter
                )
        {
            this ( parameter,                      // parameter
                   new NoDuplicates<ELEMENT> () ); // domain
        }

        public MustContainNoDuplicates (
                AbstractMultipleParameters parameter,
                NoDuplicates<ELEMENT> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not contain any duplicate elements.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                Object [] /* array or Collection */ grains
                )
        {
            if ( /* array or Collection */ grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object /* array or Collection */ grain : /* array or Collection */ grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains duplicate elements: "
                + this.domain.discards ( evidence_or_null, new ArrayList<ELEMENT> () );
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * whose length must fall within some domain.
     * </p>
     */
    public static class Length
    {
        /**
         * @return The length of the specified array, Collection,
         *         Iterable or singleton object.  Always 0L or
         *         greater if the length count succeeded.
         *         Less than 0L if an internal error occurred,
         *         such as a class cast exception, or an iterator
         *         blew up.
         *
         * @see musaico.foundation.filter.container.LengthFilter#lengthOf(java.lang.Object)
         */
        public static final long of (
                Object container
                )
        {
            return LengthFilter.lengthOf ( container );
        }


        /**
         * <p>
         * The parameter is an array, Collection or other Iterable
         * which must have a Length greater than 1.
         * </p>
         *
         * @see musaico.foundation.filter.container.LengthFilter
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeGreaterThanOne<VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeGreaterThanOne<VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            public MustBeGreaterThanOne (
                    AbstractMultipleParameters parameter
                    )
            {
                super ( parameter );
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must have > 1 elements.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public FilterState filter (
                    Object [] /* array or Collection */ grains
                    )
            {
                if ( /* array or Collection */ grains == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ grain : /* array or Collection */ grains )
                {
                    final FilterState element_filter_state =
                        LengthFilter.GREATER_THAN_ONE.filter ( grain );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () );
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + " contains "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null )
                    + " elements";
            }
        }




        /**
         * <p>
         * The parameter is an array, Collection or other Iterable
         * which must have a Length greater than 0.
         * </p>
         *
         * @see musaico.foundation.filter.container.LengthFilter
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeGreaterThanZero<VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeGreaterThanZero<VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            public MustBeGreaterThanZero (
                    AbstractMultipleParameters parameter
                    )
            {
                super ( parameter );
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must have > 0 elements.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public FilterState filter (
                    Object [] /* array or Collection */ grains
                    )
            {
                if ( /* array or Collection */ grains == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ grain : /* array or Collection */ grains )
                {
                    final FilterState element_filter_state =
                        LengthFilter.GREATER_THAN_ZERO.filter ( grain );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () );
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + " contains "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null )
                    + " elements";
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be exactly
         * a specific value.
         * </p>
         *
         * @see musaico.foundation.filter.number.EqualToNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustEqual<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustEqual<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustEqual (
                    AbstractMultipleParameters parameter,
                    int length
                    )
            {
                this ( parameter,
                       (long) length );
            }

            public MustEqual (
                    AbstractMultipleParameters parameter,
                    long length
                    )
            {
                this ( parameter,
                       new EqualToNumber<Long> ( new Long ( length ) ) );
            }

            public MustEqual (
                    AbstractMultipleParameters parameter,
                    EqualToNumber<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustEqual (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be of exactly "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or Collection parameter's length must not be
         * equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.filter.number.NotEqualToNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustNotEqual<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustNotEqual<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustNotEqual (
                    AbstractMultipleParameters parameter,
                    int length
                    )
            {
                this ( parameter,
                       new NotEqualToNumber<Long> ( (long) length ) );
            }

            public MustNotEqual (
                    AbstractMultipleParameters parameter,
                    long length
                    )
            {
                this ( parameter,
                       new NotEqualToNumber<Long> ( length ) );
            }

            public MustNotEqual (
                    AbstractMultipleParameters parameter,
                    NotEqualToNumber<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustNotEqual (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be of "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * exactly 0.
         * </p>
         *
         * @see musaico.foundation.filter.number.EqualToNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustEqualZero<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustEqualZero<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustEqualZero (
                    AbstractMultipleParameters parameter
                    )
            {
                super ( parameter );

                this.domain =
                    new LengthFilter<ELEMENT> (
                            new EqualToNumber<Long> ( 0L ) );
            }

            /**
             * @see musaico.foundation.contract.Contract#description( )
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be of exactly length 0.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * exactly 1.
         * </p>
         *
         * @see musaico.foundation.filter.number.EqualToNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustEqualOne<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustEqualOne<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustEqualOne (
                    AbstractMultipleParameters parameter
                    )
            {
                super ( parameter );

                this.domain =
                    new LengthFilter<ELEMENT> (
                            new EqualToNumber<Long> ( 1L ) );
            }

            /**
             * @see musaico.foundation.contract.Contract#description( )
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be of exactly length 1.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * greater than a specific value.
         * </p>
         *
         * @see musaico.foundation.filter.number.GreaterThanNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeGreaterThan<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeGreaterThan<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustBeGreaterThan (
                    AbstractMultipleParameters parameter,
                    int length
                    )
            {
                this ( parameter,
                       new GreaterThanNumber<Long> ( (long) length ) );
            }

            public MustBeGreaterThan (
                    AbstractMultipleParameters parameter,
                    long length
                    )
            {
                this ( parameter,
                       new GreaterThanNumber<Long> ( length ) );
            }

            public MustBeGreaterThan (
                    AbstractMultipleParameters parameter,
                    GreaterThanNumber<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustBeGreaterThan (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * greater than or equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.filter.number.GreaterThanOrEqualToNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeGreaterThanOrEqualTo<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeGreaterThanOrEqualTo<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustBeGreaterThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    int length
                    )
            {
                this ( parameter,
                       new GreaterThanOrEqualToNumber<Long> ( (long) length ) );
            }

            public MustBeGreaterThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    long length
                    )
            {
                this ( parameter,
                       new GreaterThanOrEqualToNumber<Long> ( length ) );
            }

            public MustBeGreaterThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    GreaterThanOrEqualToNumber<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustBeGreaterThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * less than a specific value.
         * </p>
         *
         * @see musaico.foundation.filter.number.LessThanNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeLessThan<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeLessThan<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustBeLessThan (
                    AbstractMultipleParameters parameter,
                    int length
                    )
            {
                this ( parameter,
                       new LessThanNumber<Long> ( (long) length ) );
            }

            public MustBeLessThan (
                    AbstractMultipleParameters parameter,
                    long length
                    )
            {
                this ( parameter,
                       new LessThanNumber<Long> ( length ) );
            }

            public MustBeLessThan (
                    AbstractMultipleParameters parameter,
                    LessThanNumber<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustBeLessThan (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * less than or equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.filter.number.LessThanOrEqualToNumber
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeLessThanOrEqualTo<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeLessThanOrEqualTo<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustBeLessThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    int length
                    )
            {
                this ( parameter,
                       new LessThanOrEqualToNumber<Long> ( (long) length ) );
            }

            public MustBeLessThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    long length
                    )
            {
                this ( parameter,
                       new LessThanOrEqualToNumber<Long> ( length ) );
            }

            public MustBeLessThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    LessThanOrEqualToNumber<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustBeLessThanOrEqualTo (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be between
         * some minimum value and some maximum value (inclusive
         * or exclusive, depending on the BoundedFilter.EndPoints used).
         * </p>
         *
         * @see musaico.foundation.filter.comparability.BoundedFilter
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
         */
        public static abstract class MustBeBetween<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeBetween<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                    AbstractMultipleParameters parameter,
                    int minimum_length,
                    int maximum_length
                    )
            {
                this ( parameter,
                       BoundedFilter.EndPoint.CLOSED,
                       minimum_length,
                       BoundedFilter.EndPoint.CLOSED,
                       maximum_length );
            }

            public MustBeBetween (
                    AbstractMultipleParameters parameter,
                    BoundedFilter.EndPoint minimum_end_point,
                    int minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    int maximum_length
                    )
            {
                this ( parameter,
                       BoundedFilter.overNumbers (
                           minimum_end_point,
                           (long) minimum_length,
                           maximum_end_point,
                           (long) maximum_length ) );
            }

            
            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                    AbstractMultipleParameters parameter,
                    long minimum_length,
                    long maximum_length
                    )
            {
                this ( parameter,
                       BoundedFilter.EndPoint.CLOSED,
                       minimum_length,
                       BoundedFilter.EndPoint.CLOSED,
                       maximum_length );
            }

            public MustBeBetween (
                    AbstractMultipleParameters parameter,
                    BoundedFilter.EndPoint minimum_end_point,
                    long minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    long maximum_length
                    )
            {
                this ( parameter,
                       BoundedFilter.overNumbers (
                           minimum_end_point,
                           minimum_length,
                           maximum_end_point,
                           maximum_length ) );
            }

            public MustBeBetween (
                    AbstractMultipleParameters parameter,
                    BoundedFilter<Long> domain
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( domain ) );
            }

            public MustBeBetween (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must be of "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] /* array or Collection */ arrays_or_collections
                    )
            {
                if ( /* array or Collection */ arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object /* array or Collection */ array_or_collection : /* array or Collection */ arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + "'s length is out of range: "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null );
            }
        }




        /**
         * <p>
         * The parameter's array length must belong to some arbitrary
         * domain of numbers.
         * Typically a method declares this when there is no existing
         * declarative obligation to support its parameter contract.
         * For example, if parameter1's length must belong to domain
         * Between5And7Elements, then the method might declare
         * "throws AbstractMultipleParameters.Length.MustBeInDomain.Violation",
         * check parameter1 against a new
         * AbstractMultipleParameters.Length.MustBeInDomain ( Between5And7Elements.CONTRACT )
         * at runtime, and explain the nature of the domain in the
         * method documentation.
         * The MustBeInDomain contract should be a last resort, since
         * the "throws" declaration reveals nothing about
         * the domain to which the parameter's length is bound.  For example,
         * throwing Between5And7Elements.Violation instead of
         * AbstractMultipleParameters.Length.AlwaysInDomain.Violation reveals more to
         * the caller about what the nature of the domain, and provides
         * an assertion filter for unit testing.
         * </p>
         *
         * @see musaico.foundation.filter
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length
         */
        public static abstract class MustBeInDomain<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
            extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.Length.MustBeInDomain<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
            implements Serializable
        {
            private static final long serialVersionUID =
                AbstractMultipleParameters.serialVersionUID;

            private final LengthFilter<ELEMENT> domain;

            public MustBeInDomain (
                    AbstractMultipleParameters parameter,
                    Filter<Long> length_number_filter
                    )
            {
                this ( parameter,
                       new LengthFilter<ELEMENT> ( length_number_filter ) );
            }

            public MustBeInDomain (
                    AbstractMultipleParameters parameter,
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( parameter );

                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "" + this.parameter () + " must have elements of length "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object [] arrays_or_collections
                    )
            {
                if ( arrays_or_collections == null )
                {
                    return FilterState.DISCARDED;
                }

                FilterState filter_state = FilterState.KEPT;
                for ( Object array_or_collection : arrays_or_collections )
                {
                    final FilterState element_filter_state =
                        this.domain.filter ( array_or_collection );
                    filter_state = filter_state.and ( element_filter_state );
                    if ( ! filter_state.isKept () )
                    {
                        return filter_state;
                    }
                }

                return filter_state;
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.domain
                    + " }";
            }

            // Every AbstractMultipleParameters.AbstractObligation must implement
            // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
             */
            @Override
            protected String violationMessage (
                    Object plaintiff,
                    Object [] /* array or Collection */ evidence_or_null,
                    Throwable cause_or_null
                    )
            {
                return "" + this.parameter () + " contains "
                    + AbstractMultipleParameters.Length.of ( evidence_or_null )
                    + " elements";
            }
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must not contain any null elements.
     * </p>
     *
     * @see musaico.foundation.filter.NoNulls
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainNoNulls<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainNoNulls<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final NoNulls<ELEMENT> domain;

        public MustContainNoNulls (
                AbstractMultipleParameters parameter
                )
        {
            super ( parameter );

            domain = new NoNulls<ELEMENT> ();
        }

        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not contain any null elements.";
        }

        @Override
        public FilterState filter (
                Object [] /* array or Collection */ grains
                )
        {
            if ( /* array or Collection */ grains == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object /* array or Collection */ grain : /* array or Collection */ grains )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( grain );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains null element(s)";
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must contain only instances of a specific set of
     * classes.
     * </p>
     *
     * @see musaico.foundation.filter.IncludesOnlyClasses
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustContainOnlyClasses<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustContainOnlyClasses<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final IncludesOnlyClasses<ELEMENT> domain;

        public MustContainOnlyClasses (
                AbstractMultipleParameters parameter,
                Class<?> ... required_classes
                )
        {
            this ( parameter,
                   new IncludesOnlyClasses<ELEMENT> ( required_classes ) );
        }

        public MustContainOnlyClasses (
                AbstractMultipleParameters parameter,
                IncludesOnlyClasses<ELEMENT> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not contain any elements of the classes "
                + this.domain;
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains instance(s) of"
                + " verboten classes: "
                + this.domain.discards ( evidence_or_null, new ArrayList<ELEMENT> () );
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must not contain any instances of a specific set of
     * classes.
     * </p>
     *
     * @see musaico.foundation.filter.ExcludesClasses
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters
     */
    public static abstract class MustExcludeClasses<ELEMENT extends Object, VIOLATION extends UncheckedViolation>
        extends AbstractMultipleParameters.AbstractObligation<AbstractMultipleParameters.MustExcludeClasses<ELEMENT, VIOLATION>, Object[] /* array[] or Collection[] */, VIOLATION>
        implements Serializable
    {
        private static final long serialVersionUID =
            AbstractMultipleParameters.serialVersionUID;

        private final ExcludesClasses<ELEMENT> domain;

        public MustExcludeClasses (
                AbstractMultipleParameters parameter,
                Class<?> ... excluded_classes
                )
        {
            this ( parameter,
                   new ExcludesClasses<ELEMENT> ( excluded_classes ) );
        }

        public MustExcludeClasses (
                AbstractMultipleParameters parameter,
                ExcludesClasses<ELEMENT> domain
                )
        {
            super ( parameter );

            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "" + this.parameter () + " must not contain any elements of the classes "
                + this.domain;
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                Object [] arrays_or_collections
                )
        {
            if ( arrays_or_collections == null )
            {
                return FilterState.DISCARDED;
            }

            FilterState filter_state = FilterState.KEPT;
            for ( Object array_or_collection : arrays_or_collections )
            {
                final FilterState element_filter_state =
                    this.domain.filter ( array_or_collection );
                filter_state = filter_state.and ( element_filter_state );
                if ( ! filter_state.isKept () )
                {
                    return filter_state;
                }
            }

            return filter_state;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " { "
                + this.domain
                + " }";
        }

        // Every AbstractMultipleParameters.AbstractObligation must implement
        // musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violationMessage(java.lang.Object, java.lang.Object, java.lang.Throwable)
         */
        @Override
        protected String violationMessage (
                Object plaintiff,
                Object [] /* array or Collection */ evidence_or_null,
                Throwable cause_or_null
                )
        {
            return "" + this.parameter () + " contains instance(s) of"
                + " verboten classes: "
                + this.domain.discards ( evidence_or_null, new ArrayList<ELEMENT> () );
        }
    }
}
