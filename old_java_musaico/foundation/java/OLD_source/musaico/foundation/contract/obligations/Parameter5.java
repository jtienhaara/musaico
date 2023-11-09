package musaico.foundation.contract.obligations;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.InstanceOfClass;
import musaico.foundation.domains.NotNull;

import musaico.foundation.domains.array.ArrayObject;
import musaico.foundation.domains.array.CoDependentDomain;
import musaico.foundation.domains.array.ContainsElements;
import musaico.foundation.domains.array.ContainsIndices;
import musaico.foundation.domains.array.ContainsOnlySpecificClasses;
import musaico.foundation.domains.array.ElementOf;
import musaico.foundation.domains.array.ElementsBelongToDomain;
import musaico.foundation.domains.array.ExcludesElements;
import musaico.foundation.domains.array.ExcludesIndices;
import musaico.foundation.domains.array.ExcludesSpecificClasses;
import musaico.foundation.domains.array.IndicesBelongToDomain;
import musaico.foundation.domains.array.Length;
import musaico.foundation.domains.array.NoDuplicates;
import musaico.foundation.domains.array.NoNulls;
import musaico.foundation.domains.array.NotElementOf;

import musaico.foundation.domains.comparability.BoundedDomain;

import musaico.foundation.domains.equality.EqualTo;
import musaico.foundation.domains.equality.NotEqualTo;

import musaico.foundation.domains.number.EqualToNumber;
import musaico.foundation.domains.number.GreaterThanNumber;
import musaico.foundation.domains.number.GreaterThanOrEqualToNegativeOne;
import musaico.foundation.domains.number.GreaterThanOrEqualToNumber;
import musaico.foundation.domains.number.GreaterThanOrEqualToOne;
import musaico.foundation.domains.number.GreaterThanOrEqualToZero;
import musaico.foundation.domains.number.GreaterThanNegativeOne;
import musaico.foundation.domains.number.GreaterThanOne;
import musaico.foundation.domains.number.GreaterThanZero;
import musaico.foundation.domains.number.LessThanNumber;
import musaico.foundation.domains.number.LessThanOrEqualToNegativeOne;
import musaico.foundation.domains.number.LessThanOrEqualToNumber;
import musaico.foundation.domains.number.LessThanOrEqualToOne;
import musaico.foundation.domains.number.LessThanOrEqualToZero;
import musaico.foundation.domains.number.LessThanNegativeOne;
import musaico.foundation.domains.number.LessThanOne;
import musaico.foundation.domains.number.LessThanZero;
import musaico.foundation.domains.number.NotEqualToNumber;

import musaico.foundation.domains.string.NotEmptyString;
import musaico.foundation.domains.string.EmptyString;
import musaico.foundation.domains.string.StringExcludesSpaces;
import musaico.foundation.domains.string.StringContainsNonSpaces;
import musaico.foundation.domains.string.StringContainsOnlyAlpha;
import musaico.foundation.domains.string.StringContainsOnlyAlphaNumerics;
import musaico.foundation.domains.string.StringContainsOnlyNumerics;
import musaico.foundation.domains.string.StringContainsOnlyPrintableCharacters;
import musaico.foundation.domains.string.StringID;
import musaico.foundation.domains.string.StringLength;
import musaico.foundation.domains.string.StringPattern;

import musaico.foundation.domains.time.BeforeAndAfter;
import musaico.foundation.domains.time.Changing;
import musaico.foundation.domains.time.Unchanging;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.composite.Not;


/**
 * <p>
 * Contracts for parameter5 of a constructor or method.
 * </p>
 *
 * <p>
 * A constructor or method, whether abstract or concrete, can
 * declare its contract by stating that it throws a violation
 * of a specific Parameter5 contract.  For example an interface
 * may declare that it throws Parameter5.MustBeLessThanZero.Violation.
 * In this case, classes which implement that interface must
 * check parameter5 against the Parameter5.MustBeLessThanZero.CONTRACT
 * whenever the method is invoked, and throw
 * a Parameter5.MustBeLessThanZero.Violation runtime exception when
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
public class Parameter5
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected Parameter5 ()
    {
    }


    /**
     * <p>
     * The parameter must belong to some arbitrary domain.
     * Typically a method declares this when there is no existing
     * declarative obligation to support its parameter contract.
     * For example, if parameter1 must belong to domain
     * Has4Wheels, then the method might declare Parameter1.MustBeInDomain,
     * check parameter1 against a new
     * Parameter1.MustBeInDomain ( Has4Wheels.CONTRACT ) at runtime,
     * and explain the nature of the domain in the method documentation.
     * The MustBeInDomain contract should be a last resort, since
     * the exception thrown by the method reveals nothing about
     * the domain to which the parameter is bound.  For example,
     * throwing Has4Wheels.Violation instead of
     * Parameter1.AlwaysInDomain.Violation reveals more to the caller
     * about what the nature of the domain, and provides an assertion
     * filter for unit testing.
     * </p>
     *
     * @see musaico.foundation.domains
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeInDomain<DOMAIN extends Object>
        implements Contract<DOMAIN, Parameter5.MustBeInDomain.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final Filter<DOMAIN> domain;

        public MustBeInDomain (
                               Filter<DOMAIN> domain
                               )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must belong to the domain "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         DOMAIN grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 does not belong to the domain.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeInDomain.Violation violation (
                                                              Object plaintiff,
                                                              DOMAIN evidence
                                                              )
        {
            return new Parameter5.MustBeInDomain.Violation ( this,
                                                             plaintiff,
                                                             evidence );
        }

        @Override
        public Parameter5.MustBeInDomain.Violation violation (
                                                              Object plaintiff,
                                                              DOMAIN evidence,
                                                              Throwable cause
                                                              )
        {
            final Parameter5.MustBeInDomain.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must not be null.
     * </p>
     *
     * @see musaico.foundation.domains.NotNull
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustNotBeNull
        implements Contract<Object, Parameter5.MustNotBeNull.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustNotBeNull CONTRACT =
            new Parameter5.MustNotBeNull ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not be null.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Object grain
                                   )
        {
            return NotNull.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is null.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustNotBeNull.Violation violation (
                                                             Object plaintiff,
                                                             Object evidence
                                                             )
        {
            return new Parameter5.MustNotBeNull.Violation ( this,
                                                            plaintiff,
                                                            evidence );
        }

        @Override
        public Parameter5.MustNotBeNull.Violation violation (
                                                             Object plaintiff,
                                                             Object evidence,
                                                             Throwable cause
                                                             )
        {
            final Parameter5.MustNotBeNull.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.time.Changing
     * @see musaico.foundation.domains.time.BeforeAndAfter
     * @see musaico.foundation.domains.HashedObject
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustChange
        implements Contract<BeforeAndAfter, Parameter5.MustChange.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustChange CONTRACT =
            new Parameter5.MustChange ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must change over time.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   BeforeAndAfter grain
                                   )
        {
            return Changing.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              BeforeAndAfter evidence
                              )
            {
                super ( contract,
                        "Parameter 5 did not change over time.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustChange.Violation violation (
                                                          Object plaintiff,
                                                          BeforeAndAfter evidence
                                                          )
        {
            return new Parameter5.MustChange.Violation ( this,
                                                         plaintiff,
                                                         evidence );
        }

        @Override
        public Parameter5.MustChange.Violation violation (
                                                          Object plaintiff,
                                                          BeforeAndAfter evidence,
                                                          Throwable cause
                                                          )
        {
            final Parameter5.MustChange.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.time.Unchanging
     * @see musaico.foundation.domains.time.BeforeAndAfter
     * @see musaico.foundation.domains.HashedObject
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustNotChange
        implements Contract<BeforeAndAfter, Parameter5.MustNotChange.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustNotChange CONTRACT =
            new Parameter5.MustNotChange ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not change over time.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   BeforeAndAfter grain
                                   )
        {
            return Unchanging.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              BeforeAndAfter evidence
                              )
            {
                super ( contract,
                        "Parameter 5 changed.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustNotChange.Violation violation (
                                                             Object plaintiff,
                                                             BeforeAndAfter evidence
                                                             )
        {
            return new Parameter5.MustNotChange.Violation ( this,
                                                            plaintiff,
                                                            evidence );
        }

        @Override
        public Parameter5.MustNotChange.Violation violation (
                                                             Object plaintiff,
                                                             BeforeAndAfter evidence,
                                                             Throwable cause
                                                             )
        {
            final Parameter5.MustNotChange.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must equal a specific value.  For example,
     * a method accepting an int parameter might require that the
     * input be any value except 42.
     * </p>
     *
     * @see musaico.foundation.domains.equality.EqualTo
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustEqual
        implements Contract<Object, Parameter5.MustEqual.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final EqualTo domain;

        public MustEqual (
                          Object object
                          )
        {
            this ( new EqualTo ( object ) );
        }

        public MustEqual (
                          EqualTo domain
                          )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must equal "
                + this.domain.value ()
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not equal to the required value.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustEqual.Violation violation (
                                                         Object plaintiff,
                                                         Object evidence
                                                         )
        {
            return new Parameter5.MustEqual.Violation ( this,
                                                        plaintiff,
                                                        evidence );
        }

        @Override
        public Parameter5.MustEqual.Violation violation (
                                                         Object plaintiff,
                                                         Object evidence,
                                                         Throwable cause
                                                         )
        {
            final Parameter5.MustEqual.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must not equal a specific value.  For example,
     * a method accepting an int parameter might require that the
     * input be any value except 42.
     * </p>
     *
     * @see musaico.foundation.domains.equality.NotEqualTo
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustNotEqual
        implements Contract<Object, Parameter5.MustNotEqual.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final NotEqualTo domain;

        public MustNotEqual (
                             Object object
                             )
        {
            this ( new NotEqualTo ( object ) );
        }

        public MustNotEqual (
                             NotEqualTo domain
                             )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not equal "
                + this.domain.value ()
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is equal to the specified value.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustNotEqual.Violation violation (
                                                            Object plaintiff,
                                                            Object evidence
                                                            )
        {
            return new Parameter5.MustNotEqual.Violation ( this,
                                                           plaintiff,
                                                           evidence );
        }

        @Override
        public Parameter5.MustNotEqual.Violation violation (
                                                            Object plaintiff,
                                                            Object evidence,
                                                            Throwable cause
                                                            )
        {
            final Parameter5.MustNotEqual.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than 1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanNegativeOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanNegativeOne
        implements Contract<Number, Parameter5.MustBeGreaterThanNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeGreaterThanNegativeOne CONTRACT =
            new Parameter5.MustBeGreaterThanNegativeOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be greater than -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return GreaterThanNegativeOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not greater than -1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanNegativeOne.Violation violation (
                                                                            Object plaintiff,
                                                                            Number evidence
                                                                            )
        {
            return new Parameter5.MustBeGreaterThanNegativeOne.Violation ( this,
                                                                           plaintiff,
                                                                           evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanNegativeOne.Violation violation (
                                                                            Object plaintiff,
                                                                            Number evidence,
                                                                            Throwable cause
                                                                            )
        {
            final Parameter5.MustBeGreaterThanNegativeOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than 1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanOne
        implements Contract<Number, Parameter5.MustBeGreaterThanOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeGreaterThanOne CONTRACT =
            new Parameter5.MustBeGreaterThanOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be greater than 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return GreaterThanOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not greater than 1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanOne.Violation violation (
                                                                    Object plaintiff,
                                                                    Number evidence
                                                                    )
        {
            return new Parameter5.MustBeGreaterThanOne.Violation ( this,
                                                                   plaintiff,
                                                                   evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanOne.Violation violation (
                                                                    Object plaintiff,
                                                                    Number evidence,
                                                                    Throwable cause
                                                                    )
        {
            final Parameter5.MustBeGreaterThanOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than 0.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanZero
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanZero
        implements Contract<Number, Parameter5.MustBeGreaterThanZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeGreaterThanZero CONTRACT =
            new Parameter5.MustBeGreaterThanZero ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be greater than 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return GreaterThanZero.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not greater than 0.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanZero.Violation violation (
                                                                     Object plaintiff,
                                                                     Number evidence
                                                                     )
        {
            return new Parameter5.MustBeGreaterThanZero.Violation ( this,
                                                                    plaintiff,
                                                                    evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanZero.Violation violation (
                                                                     Object plaintiff,
                                                                     Number evidence,
                                                                     Throwable cause
                                                                     )
        {
            final Parameter5.MustBeGreaterThanZero.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than or
     * equal to -1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualToNegativeOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanOrEqualToNegativeOne
        implements Contract<Number, Parameter5.MustBeGreaterThanOrEqualToNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeGreaterThanOrEqualToNegativeOne CONTRACT =
            new Parameter5.MustBeGreaterThanOrEqualToNegativeOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be >= -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return GreaterThanOrEqualToNegativeOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not >= -1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualToNegativeOne.Violation violation (
                                                                                     Object plaintiff,
                                                                                     Number evidence
                                                                                     )
        {
            return new Parameter5.MustBeGreaterThanOrEqualToNegativeOne.Violation ( this,
                                                                                    plaintiff,
                                                                                    evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualToNegativeOne.Violation violation (
                                                                                     Object plaintiff,
                                                                                     Number evidence,
                                                                                     Throwable cause
                                                                                     )
        {
            final Parameter5.MustBeGreaterThanOrEqualToNegativeOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than or
     * equal to 1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualToOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanOrEqualToOne
        implements Contract<Number, Parameter5.MustBeGreaterThanOrEqualToOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeGreaterThanOrEqualToOne CONTRACT =
            new Parameter5.MustBeGreaterThanOrEqualToOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be >= 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return GreaterThanOrEqualToOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not >= 1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualToOne.Violation violation (
                                                                             Object plaintiff,
                                                                             Number evidence
                                                                             )
        {
            return new Parameter5.MustBeGreaterThanOrEqualToOne.Violation ( this,
                                                                            plaintiff,
                                                                            evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualToOne.Violation violation (
                                                                             Object plaintiff,
                                                                             Number evidence,
                                                                             Throwable cause
                                                                             )
        {
            final Parameter5.MustBeGreaterThanOrEqualToOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be greater than or
     * equal to 0.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualToZero
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanOrEqualToZero
        implements Contract<Number, Parameter5.MustBeGreaterThanOrEqualToZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeGreaterThanOrEqualToZero CONTRACT =
            new Parameter5.MustBeGreaterThanOrEqualToZero ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be >= 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return GreaterThanOrEqualToZero.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not >= 0.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualToZero.Violation violation (
                                                                              Object plaintiff,
                                                                              Number evidence
                                                                              )
        {
            return new Parameter5.MustBeGreaterThanOrEqualToZero.Violation ( this,
                                                                             plaintiff,
                                                                             evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualToZero.Violation violation (
                                                                              Object plaintiff,
                                                                              Number evidence,
                                                                              Throwable cause
                                                                              )
        {
            final Parameter5.MustBeGreaterThanOrEqualToZero.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than -1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanNegativeOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanNegativeOne
        implements Contract<Number, Parameter5.MustBeLessThanNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeLessThanNegativeOne CONTRACT =
            new Parameter5.MustBeLessThanNegativeOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be < -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return LessThanNegativeOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not < -1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanNegativeOne.Violation violation (
                                                                         Object plaintiff,
                                                                         Number evidence
                                                                         )
        {
            return new Parameter5.MustBeLessThanNegativeOne.Violation ( this,
                                                                        plaintiff,
                                                                        evidence );
        }

        @Override
        public Parameter5.MustBeLessThanNegativeOne.Violation violation (
                                                                         Object plaintiff,
                                                                         Number evidence,
                                                                         Throwable cause
                                                                         )
        {
            final Parameter5.MustBeLessThanNegativeOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than 1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanOne
        implements Contract<Number, Parameter5.MustBeLessThanOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeLessThanOne CONTRACT =
            new Parameter5.MustBeLessThanOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be < 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return LessThanOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not < 1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanOne.Violation violation (
                                                                 Object plaintiff,
                                                                 Number evidence
                                                                 )
        {
            return new Parameter5.MustBeLessThanOne.Violation ( this,
                                                                plaintiff,
                                                                evidence );
        }

        @Override
        public Parameter5.MustBeLessThanOne.Violation violation (
                                                                 Object plaintiff,
                                                                 Number evidence,
                                                                 Throwable cause
                                                                 )
        {
            final Parameter5.MustBeLessThanOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than 0.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanZero
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanZero
        implements Contract<Number, Parameter5.MustBeLessThanZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeLessThanZero CONTRACT =
            new Parameter5.MustBeLessThanZero ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be < 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return LessThanZero.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not < 0.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanZero.Violation violation (
                                                                  Object plaintiff,
                                                                  Number evidence
                                                                  )
        {
            return new Parameter5.MustBeLessThanZero.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
        }

        @Override
        public Parameter5.MustBeLessThanZero.Violation violation (
                                                                  Object plaintiff,
                                                                  Number evidence,
                                                                  Throwable cause
                                                                  )
        {
            final Parameter5.MustBeLessThanZero.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than or equal
     * to -1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualToNegativeOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanOrEqualToNegativeOne
        implements Contract<Number, Parameter5.MustBeLessThanOrEqualToNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeLessThanOrEqualToNegativeOne CONTRACT =
            new Parameter5.MustBeLessThanOrEqualToNegativeOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be <= -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return LessThanOrEqualToNegativeOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not <= -1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualToNegativeOne.Violation violation (
                                                                                  Object plaintiff,
                                                                                  Number evidence
                                                                                  )
        {
            return new Parameter5.MustBeLessThanOrEqualToNegativeOne.Violation ( this,
                                                                                 plaintiff,
                                                                                 evidence );
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualToNegativeOne.Violation violation (
                                                                                  Object plaintiff,
                                                                                  Number evidence,
                                                                                  Throwable cause
                                                                                  )
        {
            final Parameter5.MustBeLessThanOrEqualToNegativeOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than or equal
     * to 1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualToOne
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanOrEqualToOne
        implements Contract<Number, Parameter5.MustBeLessThanOrEqualToOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeLessThanOrEqualToOne CONTRACT =
            new Parameter5.MustBeLessThanOrEqualToOne ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be less than or equal to 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return LessThanOrEqualToOne.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not <= 1.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualToOne.Violation violation (
                                                                          Object plaintiff,
                                                                          Number evidence
                                                                          )
        {
            return new Parameter5.MustBeLessThanOrEqualToOne.Violation ( this,
                                                                         plaintiff,
                                                                         evidence );
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualToOne.Violation violation (
                                                                          Object plaintiff,
                                                                          Number evidence,
                                                                          Throwable cause
                                                                          )
        {
            final Parameter5.MustBeLessThanOrEqualToOne.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a number which must be less than or equal
     * to 0.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualToZero
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanOrEqualToZero
        implements Contract<Number, Parameter5.MustBeLessThanOrEqualToZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeLessThanOrEqualToZero CONTRACT =
            new Parameter5.MustBeLessThanOrEqualToZero ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be <= 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Number grain
                                   )
        {
            return LessThanOrEqualToZero.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not <= 0.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualToZero.Violation violation (
                                                                           Object plaintiff,
                                                                           Number evidence
                                                                           )
        {
            return new Parameter5.MustBeLessThanOrEqualToZero.Violation ( this,
                                                                          plaintiff,
                                                                          evidence );
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualToZero.Violation violation (
                                                                           Object plaintiff,
                                                                           Number evidence,
                                                                           Throwable cause
                                                                           )
        {
            final Parameter5.MustBeLessThanOrEqualToZero.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must be greater than a specific number.
     * </p>
     *
     * @see musaico.foundation.domains.number.GreaterThanNumber
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThan
        implements Contract<Number, Parameter5.MustBeGreaterThan.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final GreaterThanNumber domain;

        public MustBeGreaterThan (
                                  Number number
                                  )
        {
            this ( new GreaterThanNumber ( number ) );
        }

        public MustBeGreaterThan (
                                  GreaterThanNumber domain
                                  )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Number grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
Number evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is out of range: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThan.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Parameter5.MustBeGreaterThan.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThan.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeGreaterThan.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must be greater than or equal to a specific number.
     * </p>
     *
     * @see musaico.foundation.domains.number.GreaterThanOrEqualToNumber
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeGreaterThanOrEqualTo
        implements Contract<Number, Parameter5.MustBeGreaterThanOrEqualTo.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final GreaterThanOrEqualToNumber domain;

        public MustBeGreaterThanOrEqualTo (
                                           Number number
                                           )
        {
            this ( new GreaterThanOrEqualToNumber ( number ) );
        }

        public MustBeGreaterThanOrEqualTo (
                                           GreaterThanOrEqualToNumber domain
                                           )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Number grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
Number evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is out of range: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Parameter5.MustBeGreaterThanOrEqualTo.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeGreaterThanOrEqualTo.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must be less than a specific number.
     * </p>
     *
     * @see musaico.foundation.domains.number.LessThanNumber
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThan
        implements Contract<Number, Parameter5.MustBeLessThan.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final LessThanNumber domain;

        public MustBeLessThan (
                               Number number
                               )
        {
            this ( new LessThanNumber ( number ) );
        }

        public MustBeLessThan (
                               LessThanNumber domain
                               )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Number grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
Number evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is out of range: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThan.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Parameter5.MustBeLessThan.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeLessThan.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeLessThan.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must be less than or equal to a specific number.
     * </p>
     *
     * @see musaico.foundation.domains.number.LessThanOrEqualToNumber
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeLessThanOrEqualTo
        implements Contract<Number, Parameter5.MustBeLessThanOrEqualTo.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final LessThanOrEqualToNumber domain;

        public MustBeLessThanOrEqualTo (
                                        Number number
                                        )
        {
            this ( new LessThanOrEqualToNumber ( number ) );
        }

        public MustBeLessThanOrEqualTo (
                                        LessThanOrEqualToNumber domain
                                        )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Number grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
Number evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is out of range: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Parameter5.MustBeLessThanOrEqualTo.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeLessThanOrEqualTo.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must be between some minimum number and some
     * maximum number (inclusive or exclusive, depending on the
     * BoundedDomain.EndPoints used).
     * </p>
     *
     * @see musaico.foundation.domains.comparability.BoundedDomain
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeBetween
        implements Contract<Number, Parameter5.MustBeBetween.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final BoundedDomain<Number> domain;

        /** Closed bounds: [ minimum_number, maximum_number ]. */
        public MustBeBetween (
                              Number minimum_number,
                              Number maximum_number
                              )
        {
            this ( BoundedDomain.EndPoint.CLOSED,
                   minimum_number,
                   BoundedDomain.EndPoint.CLOSED,
                   maximum_number );
        }

        public MustBeBetween (
                              BoundedDomain.EndPoint minimum_end_point,
                              Number minimum_number,
                              BoundedDomain.EndPoint maximum_end_point,
                              Number maximum_number
                              )
        {
            this ( BoundedDomain.overNumbers (
                       minimum_end_point,
                       minimum_number,
                       maximum_end_point,
                       maximum_number ) );
        }

        public MustBeBetween (
                              BoundedDomain<Number> domain
                              )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be of "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Number grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Number evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is out of range: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeBetween.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Parameter5.MustBeBetween.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeBetween.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeBetween.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.comparability.BoundedDomain
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeInBounds<BOUNDED extends Object>
        implements Contract<BOUNDED, Parameter5.MustBeInBounds.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final BoundedDomain<BOUNDED> domain;

        /** Closed bounds: [ min, max ]. */
        public MustBeInBounds (
                               Comparator<BOUNDED> comparator,
                               BOUNDED min,
                               BOUNDED max
                               )
        {
            this ( comparator,
                   BoundedDomain.EndPoint.CLOSED,
                   min,
                   BoundedDomain.EndPoint.CLOSED,
                   max );
        }

        public MustBeInBounds (
                               Comparator<BOUNDED> comparator,
                               BoundedDomain.EndPoint min_end_point,
                               BOUNDED min,
                               BoundedDomain.EndPoint max_end_point,
                               BOUNDED max
                               )
        {
            this ( new BoundedDomain<BOUNDED> (
                           comparator,
                           min_end_point,
                           min,
                           max_end_point,
                           max ) );
        }

        public MustBeInBounds (
                               BoundedDomain<BOUNDED> domain
                               )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be in "
                + this.domain;
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         BOUNDED grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is out of bounds.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeInBounds.Violation violation (
                                                              Object plaintiff,
                                                              BOUNDED evidence
                                                              )
        {
            return new Parameter5.MustBeInBounds.Violation ( this,
                                                             plaintiff,
                                                             evidence );
        }

        @Override
        public Parameter5.MustBeInBounds.Violation violation (
                                                              Object plaintiff,
                                                              BOUNDED evidence,
                                                              Throwable cause
                                                              )
        {
            final Parameter5.MustBeInBounds.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.InstanceOfClass
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeInstanceOfClass
        implements Contract<Object, Parameter5.MustBeInstanceOfClass.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final InstanceOfClass domain;

        public MustBeInstanceOfClass (
                                      Class<?> domain_class
                                      )
        {
            this ( new InstanceOfClass ( domain_class ) );
        }

        public MustBeInstanceOfClass (
                                      InstanceOfClass domain
                                      )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be an "
                + this.domain
                + ".";
        }

        /**
         * @return The required domain Class.  Never null.
         *
         * @see musaico.foundation.domains.InstanceOfClass#domainClass()
         */
        public final Class<?> domainClass ()
        {
            return this.domain.domainClass ();
        }


        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not an instance of the required class.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Parameter5.MustBeInstanceOfClass.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeInstanceOfClass.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.InstanceOfClass
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustNotBeInstanceOfClass
        implements Contract<Object, Parameter5.MustNotBeInstanceOfClass.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final InstanceOfClass verbotenDomain;

        public MustNotBeInstanceOfClass (
                                         Class<?> verboten_class
                                         )
        {
            this ( new InstanceOfClass ( verboten_class ) );
        }

        public MustNotBeInstanceOfClass (
                                         InstanceOfClass verboten_domain
                                         )
        {
            this.verbotenDomain = verboten_domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not be an "
                + this.verbotenDomain
                + ".";
        }

        /**
         * @return The verboten Class.  Never null.
         *
         * @see musaico.foundation.domains.InstanceOfClass#domainClass()
         */
        public final Class<?> verbotenClass ()
        {
            return this.verbotenDomain.domainClass ();
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object grain
                                         )
        {
            return this.verbotenDomain.filter ( grain )
                .opposite ();
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is an instance of the verboten class.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustNotBeInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Parameter5.MustNotBeInstanceOfClass.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustNotBeInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Parameter5.MustNotBeInstanceOfClass.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String value which must always be the
     * empty String ("").
     * </p>
     *
     * @see musaico.foundation.domains.EmptyString
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeEmptyString
        implements Contract<String, Parameter5.MustBeEmptyString.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeEmptyString CONTRACT =
            new Parameter5.MustBeEmptyString ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be the empty String \"\".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return EmptyString.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not an empty String.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeEmptyString.Violation violation (
                                                                 Object plaintiff,
                                                                 String evidence
                                                                 )
        {
            return new Parameter5.MustBeEmptyString.Violation ( this,
                                                                plaintiff,
                                                                evidence );
        }

        @Override
        public Parameter5.MustBeEmptyString.Violation violation (
                                                                 Object plaintiff,
                                                                 String evidence,
                                                                 Throwable cause
                                                                 )
        {
            final Parameter5.MustBeEmptyString.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must not be the empty
     * String ("").
     * </p>
     *
     * @see musaico.foundation.domains.NotEmptyString
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustNotBeEmptyString
        implements Contract<String, Parameter5.MustNotBeEmptyString.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustNotBeEmptyString CONTRACT =
            new Parameter5.MustNotBeEmptyString ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not be empty.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return NotEmptyString.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is an empty String.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustNotBeEmptyString.Violation violation (
                                                                    Object plaintiff,
                                                                    String evidence
                                                                    )
        {
            return new Parameter5.MustNotBeEmptyString.Violation ( this,
                                                                   plaintiff,
                                                                   evidence );
        }

        @Override
        public Parameter5.MustNotBeEmptyString.Violation violation (
                                                                    Object plaintiff,
                                                                    String evidence,
                                                                    Throwable cause
                                                                    )
        {
            final Parameter5.MustNotBeEmptyString.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.StringLength
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeStringLength
        implements Contract<String, Parameter5.MustBeStringLength.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final StringLength domain;

        public MustBeStringLength (
                                   int exact_length
                                   )
        {
            this ( new StringLength ( exact_length ) );
        }

        public MustBeStringLength (
                                   int minimum_length,
                                   int maximum_length
                                   )
        {
            this ( new StringLength ( minimum_length, maximum_length ) );
        }

        public MustBeStringLength (
                                   StringLength domain
                                   )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must be between "
                + this.domain.minimumLength ()
                + " and "
                + this.domain.maximumLength ()
                + " characters long.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         String grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is the wrong length ("
                        + ( evidence == null
                                ? -1
                                : ("" + evidence).length () )
                        + " characters).", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeStringLength.Violation violation (
                                                                  Object plaintiff,
                                                                  String evidence
                                                                  )
        {
            return new Parameter5.MustBeStringLength.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
        }

        @Override
        public Parameter5.MustBeStringLength.Violation violation (
                                                                  Object plaintiff,
                                                                  String evidence,
                                                                  Throwable cause
                                                                  )
        {
            final Parameter5.MustBeStringLength.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must not contain any whitespace
     * characters in any alphabet, such as ' ', '\t', '\n', '\r'
     * and so on.
     * </p>
     *
     * @see musaico.foundation.domains.StringExcludesSpaces
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustExcludeSpaces
        implements Contract<String, Parameter5.MustExcludeSpaces.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustExcludeSpaces CONTRACT =
            new Parameter5.MustExcludeSpaces ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not contain any whitespace characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringExcludesSpaces.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains "
                        + ( evidence == null // description
                                ? -1
                                : ( "" + evidence )
                                    .replaceAll ( "[^\\s]+", "" )
                                    .length () ) // # whitespace chars
                        + " whitespace characters.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustExcludeSpaces.Violation violation (
                                                                 Object plaintiff,
                                                                 String evidence
                                                                 )
        {
            return new Parameter5.MustExcludeSpaces.Violation ( this,
                                                                plaintiff,
                                                                evidence );
        }

        @Override
        public Parameter5.MustExcludeSpaces.Violation violation (
                                                                 Object plaintiff,
                                                                 String evidence,
                                                                 Throwable cause
                                                                 )
        {
            final Parameter5.MustExcludeSpaces.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain at least one
     * non-whitespace character in any alphabet (at least one character
     * other than ' ', '\t', '\n', '\r' and so on).
     * </p>
     *
     * @see musaico.foundation.domains.ContainsNonSpaces
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainNonSpaces
        implements Contract<String, Parameter5.MustContainNonSpaces.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainNonSpaces CONTRACT =
            new Parameter5.MustContainNonSpaces ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain at least one non-whitespace"
                + " character.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringContainsNonSpaces.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 does not contain any non-whitespace"
                        + " characters.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainNonSpaces.Violation violation (
                                                                    Object plaintiff,
                                                                    String evidence
                                                                    )
        {
            return new Parameter5.MustContainNonSpaces.Violation ( this,
                                                                   plaintiff,
                                                                   evidence );
        }

        @Override
        public Parameter5.MustContainNonSpaces.Violation violation (
                                                                    Object plaintiff,
                                                                    String evidence,
                                                                    Throwable cause
                                                                    )
        {
            final Parameter5.MustContainNonSpaces.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only numeric
     * digits in any alphabet ('1', '2', '3' and so on).
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyNumerics
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyNumerics
        implements Contract<String, Parameter5.MustContainOnlyNumerics.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainOnlyNumerics CONTRACT =
            new Parameter5.MustContainOnlyNumerics ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only numeric digits.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringContainsOnlyNumerics.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains characters other than"
                        + " numerals: "
                        + ( evidence == null // description
                            ? "<null>"
                            : ( "" + evidence )
                                .replaceAll ( "[^\\d]+", "" ) ),
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainOnlyNumerics.Violation violation (
                                                                       Object plaintiff,
                                                                       String evidence
                                                                       )
        {
            return new Parameter5.MustContainOnlyNumerics.Violation ( this,
                                                                      plaintiff,
                                                                      evidence );
        }

        @Override
        public Parameter5.MustContainOnlyNumerics.Violation violation (
                                                                       Object plaintiff,
                                                                       String evidence,
                                                                       Throwable cause
                                                                       )
        {
            final Parameter5.MustContainOnlyNumerics.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only letters
     * from any alphabet ('a', 'A', 'b' and so on).
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyAlpha
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyAlpha
        implements Contract<String, Parameter5.MustContainOnlyAlpha.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainOnlyAlpha CONTRACT =
            new Parameter5.MustContainOnlyAlpha ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only alphabetical characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringContainsOnlyAlpha.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains non-alphabetical characters: "
                        + ( evidence == null // description
                            ? "<null>"
                            : ( "" + evidence )
                                .replaceAll ( "[\\p{Alpha}]+", "" ) ),
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainOnlyAlpha.Violation violation (
                                                                    Object plaintiff,
                                                                    String evidence
                                                                    )
        {
            return new Parameter5.MustContainOnlyAlpha.Violation ( this,
                                                                   plaintiff,
                                                                   evidence );
        }

        @Override
        public Parameter5.MustContainOnlyAlpha.Violation violation (
                                                                    Object plaintiff,
                                                                    String evidence,
                                                                    Throwable cause
                                                                    )
        {
            final Parameter5.MustContainOnlyAlpha.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only letters
     * from any alphabet ('a', 'A', 'b' and so on) and/or
     * numeric digits from any alphabet ('1', '2', '3' and so on).
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyAlphaNumerics
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyAlphaNumerics
        implements Contract<String, Parameter5.MustContainOnlyAlphaNumerics.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainOnlyAlphaNumerics CONTRACT =
            new Parameter5.MustContainOnlyAlphaNumerics ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only alphabetical and/or numeric"
                + " characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringContainsOnlyAlphaNumerics.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains non-alphanumeric characters: "
                        + ( evidence == null // description
                            ? "<null>"
                            : ( "" + evidence )
                            .replaceAll ( "[\\p{Alnum}]+", "" ) ),
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainOnlyAlphaNumerics.Violation violation (
                                                                            Object plaintiff,
                                                                            String evidence
                                                                            )
        {
            return new Parameter5.MustContainOnlyAlphaNumerics.Violation ( this,
                                                                           plaintiff,
                                                                           evidence );
        }

        @Override
        public Parameter5.MustContainOnlyAlphaNumerics.Violation violation (
                                                                            Object plaintiff,
                                                                            String evidence,
                                                                            Throwable cause
                                                                            )
        {
            final Parameter5.MustContainOnlyAlphaNumerics.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must contain only printable
     * characters in any alphabet, excluding control characters.
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyPrintableCharacters
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyPrintableCharacters
        implements Contract<String, Parameter5.MustContainOnlyPrintableCharacters.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainOnlyPrintableCharacters CONTRACT =
            new Parameter5.MustContainOnlyPrintableCharacters ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only printable characters.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringContainsOnlyPrintableCharacters.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains non-printable character(s) at "
                        + nonPrintableCharacterIndices ( "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
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

        @Override
        public Parameter5.MustContainOnlyPrintableCharacters.Violation violation (
                                                                                  Object plaintiff,
                                                                                  String evidence
                                                                                  )
        {
            return new Parameter5.MustContainOnlyPrintableCharacters.Violation ( this,
                                                                                 plaintiff,
                                                                                 evidence );
        }

        @Override
        public Parameter5.MustContainOnlyPrintableCharacters.Violation violation (
                                                                                  Object plaintiff,
                                                                                  String evidence,
                                                                                  Throwable cause
                                                                                  )
        {
            final Parameter5.MustContainOnlyPrintableCharacters.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
     * @see musaico.foundation.domains.StringID
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeStringID
        implements Contract<String, Parameter5.MustBeStringID.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustBeStringID CONTRACT =
            new Parameter5.MustBeStringID ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only alphanumerics and/or"
                + " underscores (_), and must not start with a numeric.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   String grain
                                   )
        {
            return StringID.DOMAIN.filter ( grain );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is not an ID.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeStringID.Violation violation (
                                                              Object plaintiff,
                                                              String evidence
                                                              )
        {
            return new Parameter5.MustBeStringID.Violation ( this,
                                                             plaintiff,
                                                             evidence );
        }

        @Override
        public Parameter5.MustBeStringID.Violation violation (
                                                              Object plaintiff,
                                                              String evidence,
                                                              Throwable cause
                                                              )
        {
            final Parameter5.MustBeStringID.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is a String which must match some regular
     * expression pattern.
     * </p>
     *
     * @see musaico.foundation.domains.StringPattern
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustMatchPattern
        implements Contract<String, Parameter5.MustMatchPattern.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final StringPattern domain;

        public MustMatchPattern (
                                 Pattern pattern
                                 )
        {
            this ( new StringPattern ( pattern ) );
        }

        public MustMatchPattern (
                                 StringPattern domain
                                 )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must match the regular expression "
                + this.domain.pattern ().pattern ()
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         String grain
                                         )
        {
            return this.domain.filter ( grain );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 does not match the regular expression.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustMatchPattern.Violation violation (
                                                                Object plaintiff,
                                                                String evidence
                                                                )
        {
            return new Parameter5.MustMatchPattern.Violation ( this,
                                                               plaintiff,
                                                               evidence );
        }

        @Override
        public Parameter5.MustMatchPattern.Violation violation (
                                                                Object plaintiff,
                                                                String evidence,
                                                                Throwable cause
                                                                )
        {
            final Parameter5.MustMatchPattern.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an element of a specific array or Collection
     * or other Iterable.  The element may be at any position,
     * and may occur more than once, but must be present at least once.
     * </p>
     *
     * @see musaico.foundation.domains.ElementOf
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustBeElementOf
        implements Contract<Object, Parameter5.MustBeElementOf.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ElementOf domain;

        @SafeVarargs
        @SuppressWarnings("varargs") // ELEMENT ...
        public <ELEMENT extends Object> MustBeElementOf (
                Class<ELEMENT> element_class,
                ELEMENT ... array
                )
        {
            this ( new ElementOf ( element_class, array ) );
        }

        public <ELEMENT extends Object> MustBeElementOf (
                Class<ELEMENT> element_class,
                Collection<ELEMENT> collection
                )
        {
            this ( new ElementOf ( element_class, collection ) );
        }

        public <ELEMENT extends Object> MustBeElementOf (
                Class<ELEMENT> element_class,
                Iterable<ELEMENT> iterable
                )
        {
            this ( new ElementOf ( element_class, iterable ) );
        }

        public <ELEMENT extends Object> MustBeElementOf (
                ArrayObject<ELEMENT> array
                )
        {
            this ( new ElementOf ( array ) );
        }

        public MustBeElementOf (
                ElementOf domain
                )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 1 must be an element of "
                + this.domain.array ()
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                    Parameter5.MustBeElementOf contract,
                    Object plaintiff,
                    Object evidence
                    )
            {
                super ( contract,
                        "Parameter 5 is not an element"
                        + " of the required array or Collection or Iterable.",
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustBeElementOf.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Parameter5.MustBeElementOf.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustBeElementOf.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Parameter5.MustBeElementOf.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is not an element of a specific array or Collection
     * or other Iterable.
     * </p>
     *
     * @see musaico.foundation.domains.NotElementOf
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustNotBeElementOf
        implements Contract<Object, Parameter5.MustNotBeElementOf.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final NotElementOf domain;

        @SafeVarargs
        @SuppressWarnings("varargs") // ELEMENT ...
        public <ELEMENT extends Object> MustNotBeElementOf (
                Class<ELEMENT> element_class,
                ELEMENT ... array
                )
        {
            this ( new NotElementOf ( element_class, array ) );
        }

        public <ELEMENT extends Object> MustNotBeElementOf (
                Class<ELEMENT> element_class,
                Collection<ELEMENT> collection
                )
        {
            this ( new NotElementOf ( element_class, collection ) );
        }

        public <ELEMENT extends Object> MustNotBeElementOf (
                Class<ELEMENT> element_class,
                Iterable<ELEMENT> iterable
                )
        {
            this ( new NotElementOf ( element_class, iterable ) );
        }

        public <ELEMENT extends Object> MustNotBeElementOf (
                ArrayObject<ELEMENT> array
                )
        {
            this ( new NotElementOf ( array ) );
        }

        public MustNotBeElementOf (
                NotElementOf domain
                )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 1 must not be an element of "
                + this.domain.array ()
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                    Parameter5.MustNotBeElementOf contract,
                    Object plaintiff,
                    Object evidence
                    )
            {
                super ( contract,
                        "Parameter 5 is an element of the verboten"
                        + " array or Collection or Iterable",
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustNotBeElementOf.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Parameter5.MustNotBeElementOf.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustNotBeElementOf.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Parameter5.MustNotBeElementOf.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }


    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * containing every element from a certain set of values.
     * The elements may be in any order, but each value must
     * to be present at least once.
     * </p>
     *
     * @see musaico.foundation.domains.ContainsElements
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainElements
        implements Contract<Object /* array or Collection */, Parameter5.MustContainElements.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ContainsElements domain;

        public MustContainElements (
                                    Object ... elements
                                    )
        {
            this ( new ContainsElements ( elements ) );
        }

        public MustContainElements (
                                    ContainsElements domain
                                    )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain the elements "
                + Arrays.toString ( this.domain.elements () )
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustContainElements contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is missing required elements: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainElements.Violation violation (
                                                                   Object plaintiff,
                                                                   Object /* array or Collection */ evidence
                                                                   )
        {
            return new Parameter5.MustContainElements.Violation ( this,
                                                                  plaintiff,
                                                                  evidence );
        }

        @Override
        public Parameter5.MustContainElements.Violation violation (
                                                                   Object plaintiff,
                                                                   Object /* array or Collection */ evidence,
                                                                   Throwable cause
                                                                   )
        {
            final Parameter5.MustContainElements.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * which must exclude every element from a certain set of values.
     * </p>
     *
     * @see musaico.foundation.domains.ExcludesElements
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustExcludeElements
        implements Contract<Object /* array or Collection */, Parameter5.MustExcludeElements.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ExcludesElements domain;

        public MustExcludeElements (
                                    Object ... elements
                                    )
        {
            this ( new ExcludesElements ( elements ) );
        }

        public MustExcludeElements (
                                    ExcludesElements domain
                                    )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must exclude the elements "
                + Arrays.toString ( this.domain.elements () )
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustExcludeElements contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 must contains verboten elements: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustExcludeElements.Violation violation (
                                                                   Object plaintiff,
                                                                   Object /* array or Collection */ evidence
                                                                   )
        {
            return new Parameter5.MustExcludeElements.Violation ( this,
                                                                  plaintiff,
                                                                  evidence );
        }

        @Override
        public Parameter5.MustExcludeElements.Violation violation (
                                                                   Object plaintiff,
                                                                   Object /* array or Collection */ evidence,
                                                                   Throwable cause
                                                                   )
        {
            final Parameter5.MustExcludeElements.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * containing only elements which belong to a specific domain.
     * </p>
     *
     * @see musaico.foundation.domains.ElementsBelongToDomain
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyDomainElements
        implements Contract<Object /* array or Collection */, Parameter5.MustContainOnlyDomainElements.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ElementsBelongToDomain domain;

        public MustContainOnlyDomainElements (
                                              Filter<?> element_domain
                                              )
        {
            this ( new ElementsBelongToDomain ( element_domain ) );
        }

        public MustContainOnlyDomainElements (
                                              ElementsBelongToDomain domain
                                              )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only elements in the domain "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustContainOnlyDomainElements contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains elements from outside the domain: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainOnlyDomainElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Parameter5.MustContainOnlyDomainElements.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustContainOnlyDomainElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Parameter5.MustContainOnlyDomainElements.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }



    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * containing every index from a certain set of numbers.
     * The indices may be in any order, but each one must
     * to be present.
     * </p>
     *
     * @see musaico.foundation.domains.ContainsIndices
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainIndices
        implements Contract<Object /* array or Collection */, Parameter5.MustContainIndices.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ContainsIndices domain;

        public MustContainIndices (
                                   long ... indices
                                   )
        {
            this ( new ContainsIndices ( indices ) );
        }

        public MustContainIndices (
                                   int ... indices
                                   )
        {
            this ( new ContainsIndices ( indices ) );
        }

        public MustContainIndices (
                                   ContainsIndices domain
                                   )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain the indices "
                + Arrays.toString ( this.domain.indices () )
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustContainIndices contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 is missing required indices: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainIndices.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence
                                                                  )
        {
            return new Parameter5.MustContainIndices.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
        }

        @Override
        public Parameter5.MustContainIndices.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence,
                                                                  Throwable cause
                                                                  )
        {
            final Parameter5.MustContainIndices.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * which must exclude every index from a certain set of numbers.
     * </p>
     *
     * @see musaico.foundation.domains.ExcludesIndices
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustExcludeIndices
        implements Contract<Object /* array or Collection */, Parameter5.MustExcludeIndices.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ExcludesIndices domain;

        public MustExcludeIndices (
                                   long ... indices
                                   )
        {
            this ( new ExcludesIndices ( indices ) );
        }

        public MustExcludeIndices (
                                   int ... indices
                                   )
        {
            this ( new ExcludesIndices ( indices ) );
        }

        public MustExcludeIndices (
                                   ExcludesIndices domain
                                   )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must exclude the indices "
                + Arrays.toString ( this.domain.indices () )
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustExcludeIndices contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 must contains verboten indices: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustExcludeIndices.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence
                                                                  )
        {
            return new Parameter5.MustExcludeIndices.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
        }

        @Override
        public Parameter5.MustExcludeIndices.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence,
                                                                  Throwable cause
                                                                  )
        {
            final Parameter5.MustExcludeIndices.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array or Collection or other Iterable
     * containing only indices which belong to a specific domain.
     * </p>
     *
     * @see musaico.foundation.domains.IndicesBelongToDomain
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyDomainIndices
        implements Contract<Object /* array or Collection */, Parameter5.MustContainOnlyDomainIndices.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final IndicesBelongToDomain domain;

        public MustContainOnlyDomainIndices (
                                             Filter<Number> index_domain
                                             )
        {
            this ( new IndicesBelongToDomain ( index_domain ) );
        }

        public MustContainOnlyDomainIndices (
                                             IndicesBelongToDomain domain
                                             )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must contain only indices in the domain "
                + this.domain
                + ".";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustContainOnlyDomainIndices contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains indices not in the domain: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainOnlyDomainIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Parameter5.MustContainOnlyDomainIndices.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Parameter5.MustContainOnlyDomainIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Parameter5.MustContainOnlyDomainIndices.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must not contain any duplicate values.  Every element
     * must be unique.
     * </p>
     *
     * @see musaico.foundation.domains.NoDuplicates
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainNoDuplicates
        implements Contract<Object /* array or Collection */, Parameter5.MustContainNoDuplicates.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainNoDuplicates CONTRACT =
            new Parameter5.MustContainNoDuplicates ();

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not contain any duplicate elements.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public FilterState filter (
                                   Object /* array or Collection */ grain
                                   )
        {
            return NoDuplicates.DOMAIN.filter (
                new ArrayObject<Object> ( Object.class,
                                          grain ) );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustContainNoDuplicates contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains duplicate elements: "
                        + NoDuplicates.DOMAIN.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainNoDuplicates.Violation violation (
                                                                       Object plaintiff,
                                                                       Object /* array or Collection */ evidence
                                                                       )
        {
            return new Parameter5.MustContainNoDuplicates.Violation ( this,
                                                                      plaintiff,
                                                                      evidence );
        }

        @Override
        public Parameter5.MustContainNoDuplicates.Violation violation (
                                                                       Object plaintiff,
                                                                       Object /* array or Collection */ evidence,
                                                                       Throwable cause
                                                                       )
        {
            final Parameter5.MustContainNoDuplicates.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
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
         * <p>
         * The parameter is an array, Collection or other Iterable
         * which must have a Length greater than 1.
         * </p>
         *
         * @see musaico.foundation.domains.array.Length
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeGreaterThanOne
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeGreaterThanOne.Violation>,
                       Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public static final Parameter5.Length.MustBeGreaterThanOne CONTRACT =
                new Parameter5.Length.MustBeGreaterThanOne ();

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must have > 1 elements.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public FilterState filter (
                                       Object /* array or Collection */ grain
                                       )
            {
                return musaico.foundation.domains.array.Length.GREATER_THAN_ONE_DOMAIN.filter (
                    new ArrayObject<Object> ( Object.class,
                                              grain ) );
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () );
            }

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 contains "
                            + new ArrayObject<Object> ( Object.class,
                                                        evidence )
                                .length ()
                            + " elements.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeGreaterThanOne.Violation violation (
                                                                               Object plaintiff,
                                                                               Object /* array or Collection */ evidence
                                                                               )
            {
                return new Parameter5.Length.MustBeGreaterThanOne.Violation ( this,
                                                                              plaintiff,
                                                                              evidence );
            }

            @Override
            public Parameter5.Length.MustBeGreaterThanOne.Violation violation (
                                                                               Object plaintiff,
                                                                               Object /* array or Collection */ evidence,
                                                                               Throwable cause
                                                                               )
            {
                final Parameter5.Length.MustBeGreaterThanOne.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The parameter is an array, Collection or other Iterable
         * which must have a Length greater than 0.
         * </p>
         *
         * @see musaico.foundation.domains.array.Length
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeGreaterThanZero
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeGreaterThanZero.Violation>,
                       Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public static final Parameter5.Length.MustBeGreaterThanZero CONTRACT =
                new Parameter5.Length.MustBeGreaterThanZero ();

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must have > 0 elements.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public FilterState filter (
                                       Object /* array or Collection */ grain
                                       )
            {
                return musaico.foundation.domains.array.Length.GREATER_THAN_ZERO_DOMAIN.filter (
                     new ArrayObject<Object> ( Object.class,
                                               grain ) );
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () );
            }

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 contains "
                            + new ArrayObject<Object> ( Object.class,
                                                        evidence )
                                .length ()
                            + " elements.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeGreaterThanZero.Violation violation (
                                                                                Object plaintiff,
                                                                                Object /* array or Collection */ evidence
                                                                                )
            {
                return new Parameter5.Length.MustBeGreaterThanZero.Violation ( this,
                                                                               plaintiff,
                                                                               evidence );
            }

            @Override
            public Parameter5.Length.MustBeGreaterThanZero.Violation violation (
                                                                                Object plaintiff,
                                                                                Object /* array or Collection */ evidence,
                                                                                Throwable cause
                                                                                )
            {
                final Parameter5.Length.MustBeGreaterThanZero.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be exactly
         * a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.EqualToNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustEqual
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustEqual.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public MustEqual (
                              int length
                              )
            {
                this ( new EqualToNumber ( length ) );
            }

            public MustEqual (
                              EqualToNumber domain
                              )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustEqual (
                              musaico.foundation.domains.array.Length domain
                              )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be of exactly "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustEqual.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustEqual.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or Collection parameter's length must not be
         * equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.NotEqualToNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustNotEqual
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustNotEqual.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public MustNotEqual (
                                 int length
                                 )
            {
                this ( new NotEqualToNumber ( length ) );
            }

            public MustNotEqual (
                                 NotEqualToNumber domain
                                 )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustNotEqual (
                              musaico.foundation.domains.array.Length domain
                              )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be of "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustNotEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustNotEqual.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustNotEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustNotEqual.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * exactly 0.
         * </p>
         *
         * @see musaico.foundation.domains.number.EqualToNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustEqualZero
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustEqualZero.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public static final Parameter5.Length.MustEqualZero CONTRACT =
                new Parameter5.Length.MustEqualZero ();

            private MustEqualZero ()
            {
                this.domain =
                    new musaico.foundation.domains.array.Length (
                        new EqualToNumber ( 0 ) );
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be of exactly length 0.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            @Override
            public final Parameter5.Length.MustEqualZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return this.violation (
                    plaintiff,
                    evidence,
                    null ); // cause
            }

            @Override
            public final Parameter5.Length.MustEqualZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustEqualZero.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * exactly 1.
         * </p>
         *
         * @see musaico.foundation.domains.number.EqualToNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustEqualOne
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustEqualOne.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public static final Parameter5.Length.MustEqualOne CONTRACT =
                new Parameter5.Length.MustEqualOne ();

            private MustEqualOne ()
            {
                this.domain =
                    new musaico.foundation.domains.array.Length (
                        new EqualToNumber ( 1 ) );
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be of exactly length 1.";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            @Override
            public final Parameter5.Length.MustEqualOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return this.violation (
                    plaintiff,
                    evidence,
                    null ); // cause
            }

            @Override
            public final Parameter5.Length.MustEqualOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustEqualOne.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * greater than a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.GreaterThanNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeGreaterThan
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeGreaterThan.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public MustBeGreaterThan (
                                      int length
                                      )
            {
                this ( new GreaterThanNumber ( length ) );
            }

            public MustBeGreaterThan (
                                      GreaterThanNumber domain
                                      )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustBeGreaterThan (
                                      musaico.foundation.domains.array.Length domain
                                      )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustBeGreaterThan.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustBeGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustBeGreaterThan.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * greater than or equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.GreaterThanOrEqualToNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeGreaterThanOrEqualTo
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeGreaterThanOrEqualTo.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public MustBeGreaterThanOrEqualTo (
                                               int length
                                               )
            {
                this ( new GreaterThanOrEqualToNumber ( length ) );
            }

            public MustBeGreaterThanOrEqualTo (
                                               GreaterThanOrEqualToNumber domain
                                               )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustBeGreaterThanOrEqualTo (
                                               musaico.foundation.domains.array.Length domain
                                               )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustBeGreaterThanOrEqualTo.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustBeGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustBeGreaterThanOrEqualTo.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * less than a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.LessThanNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeLessThan
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeLessThan.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public MustBeLessThan (
                                   int length
                                   )
            {
                this ( new LessThanNumber ( length ) );
            }

            public MustBeLessThan (
                                   LessThanNumber domain
                                   )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustBeLessThan (
                                   musaico.foundation.domains.array.Length domain
                                   )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustBeLessThan.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustBeLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustBeLessThan.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be
         * less than or equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.LessThanOrEqualToNumber
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeLessThanOrEqualTo
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeLessThanOrEqualTo.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public MustBeLessThanOrEqualTo (
                                            int length
                                            )
            {
                this ( new LessThanOrEqualToNumber ( length ) );
            }

            public MustBeLessThanOrEqualTo (
                                            LessThanOrEqualToNumber domain
                                            )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustBeLessThanOrEqualTo (
                                            musaico.foundation.domains.array.Length domain
                                            )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustBeLessThanOrEqualTo.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustBeLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustBeLessThanOrEqualTo.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The array or collection parameter's length must be between
         * some minimum value and some maximum value (inclusive
         * or exclusive, depending on the BoundedDomain.EndPoints used).
         * </p>
         *
         * @see musaico.foundation.domains.comparability.BoundedDomain
         * @see musaico.foundation.contract.obligations.Parameter5
         */
        public static class MustBeBetween
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeBetween.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                                  int minimum_length,
                                  int maximum_length
                                  )
            {
                this ( BoundedDomain.EndPoint.CLOSED,
                       minimum_length,
                       BoundedDomain.EndPoint.CLOSED,
                       maximum_length );
            }

            public MustBeBetween (
                                  BoundedDomain.EndPoint minimum_end_point,
                                  int minimum_length,
                                  BoundedDomain.EndPoint maximum_end_point,
                                  int maximum_length
                                  )
            {
                this ( BoundedDomain.overNumbers (
                           minimum_end_point,
                           minimum_length,
                           maximum_end_point,
                           maximum_length ) );
            }

            public MustBeBetween (
                                  BoundedDomain<Number> domain
                                  )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public MustBeBetween (
                                  musaico.foundation.domains.array.Length domain
                                  )
            {
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must be of "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                    Object /* array or Collection */ array_or_collection
                    )
            {
                return this.domain.filter (
                           new ArrayObject<Object> ( Object.class,
                                                     array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Parameter5.Length.MustBeBetween.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Parameter5.Length.MustBeBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Parameter5.Length.MustBeBetween.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /**
         * <p>
         * The parameter's array length must belong to some arbitrary
         * domain of numbers.
         * Typically a method declares this when there is no existing
         * declarative obligation to support its parameter contract.
         * For example, if parameter5's length must belong to domain
         * Between5And7Elements, then the method might declare
         * "throws Parameter5.Length.MustBeInDomain.Violation",
         * check parameter5 against a new
         * Parameter5.Length.MustBeInDomain ( Between5And7Elements.CONTRACT )
         * at runtime, and explain the nature of the domain in the
         * method documentation.
         * The MustBeInDomain contract should be a last resort, since
         * the "throws" declaration reveals nothing about
         * the domain to which the parameter's length is bound.  For example,
         * throwing Between5And7Elements.Violation instead of
         * Parameter5.Length.AlwaysInDomain.Violation reveals more to
         * the caller about what the nature of the domain, and provides
         * an assertion filter for unit testing.
         * </p>
         *
         * @see musaico.foundation.domains
         * @see musaico.foundation.contract.obligations.Parameter5.Length
         */
        public static class MustBeInDomain
            implements Contract<Object /* array or Collection */, Parameter5.Length.MustBeInDomain.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Filter<ArrayObject<?>> domain;

            public MustBeInDomain (
                                   Filter<Number> domain
                                   )
            {
                this.domain =
                    new musaico.foundation.domains.array.Length ( domain );
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 must have elements of length "
                    + this.domain
                    + ".";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object array_or_collection
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              array_or_collection ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 contains "
                            + new ArrayObject<Object> ( Object.class,
                                                        evidence )
                                .length ()
                            + " elements.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.Length.MustBeInDomain.Violation violation (
                                                                         Object plaintiff,
                                                                         Object /* array or Collection */ evidence
                                                                         )
            {
                return new Parameter5.Length.MustBeInDomain.Violation ( this,
                                                                        plaintiff,
                                                                        evidence );
            }

            @Override
            public Parameter5.Length.MustBeInDomain.Violation violation (
                                                                         Object plaintiff,
                                                                         Object /* array or Collection */ evidence,
                                                                         Throwable cause
                                                                         )
            {
                final Parameter5.Length.MustBeInDomain.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must not contain any null elements.
     * </p>
     *
     * @see musaico.foundation.domains.NoNulls
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainNoNulls
        implements Contract<Object /* array or Collection */, Parameter5.MustContainNoNulls.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final Parameter5.MustContainNoNulls CONTRACT =
            new Parameter5.MustContainNoNulls ();

        @Override
        public final String description ()
        {
            return "Parameter 5 must not contain any null elements.";
        }

        @Override
        public FilterState filter (
                                   Object /* array or Collection */ grain
                                   )
        {
            return NoNulls.DOMAIN.filter (
                new ArrayObject<Object> ( Object.class,
                                          grain ) );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 5 contains null element(s).", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainNoNulls.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence
                                                                  )
        {
            return new Parameter5.MustContainNoNulls.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
        }

        @Override
        public Parameter5.MustContainNoNulls.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence,
                                                                  Throwable cause
                                                                  )
        {
            final Parameter5.MustContainNoNulls.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must contain only instances of a specific set of
     * classes.
     * </p>
     *
     * @see musaico.foundation.domains.ContainsOnlySpecificClasses
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustContainOnlyClasses
        implements Contract<Object /* array or Collection */, Parameter5.MustContainOnlyClasses.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ContainsOnlySpecificClasses domain;

        public MustContainOnlyClasses (
                                       Class<?> ... required_classes
                                       )
        {
            this ( new ContainsOnlySpecificClasses ( required_classes ) );
        }

        public MustContainOnlyClasses (
                                       ContainsOnlySpecificClasses domain
                                       )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not contain any elements of the classes "
                + this.domain;
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustContainOnlyClasses contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 1 contains instance(s) of"
                        + " verboten classes: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustContainOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Parameter5.MustContainOnlyClasses.Violation (
                    this,
                    plaintiff,
                    evidence );
        }

        @Override
        public Parameter5.MustContainOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Parameter5.MustContainOnlyClasses.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter is an array, Collection or other Iterable
     * which must not contain any instances of a specific set of
     * classes.
     * </p>
     *
     * @see musaico.foundation.domains.ExcludesSpecificClasses
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class MustExcludeClasses
        implements Contract<Object /* array or Collection */, Parameter5.MustExcludeClasses.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        private final ExcludesSpecificClasses domain;

        public MustExcludeClasses (
                                   Class<?> ... excluded_classes
                                   )
        {
            this ( new ExcludesSpecificClasses ( excluded_classes ) );
        }

        public MustExcludeClasses (
                                   ExcludesSpecificClasses domain
                                   )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "Parameter 5 must not contain any elements of the classes "
                + this.domain;
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return this.domain.filter (
                new ArrayObject<Object> ( Object.class,
                                          array_or_collection ) );
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
            public Violation (
                              Parameter5.MustExcludeClasses contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "Parameter 1 contains instance(s) of"
                        + " verboten classes: "
                        + contract.domain.nonMember (
                            new ArrayObject<Object> ( Object.class,
                                                      evidence ) ), // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Parameter5.MustExcludeClasses.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence
                                                                  )
        {
            return new Parameter5.MustExcludeClasses.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
        }

        @Override
        public Parameter5.MustExcludeClasses.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array or Collection */ evidence,
                                                                  Throwable cause
                                                                  )
        {
            final Parameter5.MustExcludeClasses.Violation violation =
                this.violation ( plaintiff, evidence );
            if ( cause != null )
            {
                violation.initCause ( cause );
            }

            return violation;
        }
    }




    /**
     * <p>
     * The parameter must belong to the same domain
     * as some other object, such as the "this" object asserting
     * the guarantee, or one of the other parameters to the method.
     * </p>
     *
     * @see musaico.foundation.domains.VariableDomain
     * @see musaico.foundation.contract.obligations.Parameter5
     */
    public static class DependsOn
    {


        /** Parameter5 depends on the parent object, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class This
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.This.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object thisObject;

            private final CoDependentDomain domain;

            public This (
                         Object this_object,
                         Filter<?> ... domains_to_choose_from
                         )
            {
                this ( this_object,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public This (
                         Object this_object,
                         CoDependentDomain domain
                         )
            {
                this.thisObject = this_object;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and the object receiving"
                    + " the parameter ("
                    + this.thisObject
                    + ") must belong to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.thisObject,
                                                  grain
                                              } ) );
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () )
                    + " { "
                    + this.thisObject
                    + ", "
                    + this.domain
                    + " }";
            }

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and the object receiving"
                            + " the parameter belong to different"
                            + " domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.This.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array, Collection or single Object */ evidence
                                                                  )
            {
                return new Parameter5.DependsOn.This.Violation ( this,
                                                                 plaintiff,
                                                                 evidence );
            }

            @Override
            public Parameter5.DependsOn.This.Violation violation (
                                                                  Object plaintiff,
                                                                  Object /* array, Collection or single Object */ evidence,
                                                                  Throwable cause
                                                                  )
            {
                final Parameter5.DependsOn.This.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter1, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter1
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter1.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter1 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter1 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 1 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 1 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter1.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter1.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter1.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter1.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter2, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter2
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter2.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter2 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter2 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 2 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 2 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter2.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter2.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter2.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter2.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter3, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter3
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter3.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter3 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter3 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 3 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 3 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter3.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter3.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter3.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter3.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter4, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter4
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter4.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter4 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter4 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 4 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 4 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter4.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter4.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter4.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter4.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter6, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter6
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter6.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter6 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter6 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 6 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 6 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter6.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter6.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter6.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter6.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter7, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter7
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter7.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter7 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter7 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 7 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 7 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter7.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter7.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter7.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter7.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter8, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter8
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter8.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter8 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter8 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 8 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 8 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter8.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter8.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter8.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter8.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Parameter5 depends on parameter9, both must be in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.obligations.Parameter5 */
        public static class Parameter9
            implements Contract<Object /* array, Collection, single object */, Parameter5.DependsOn.Parameter9.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter9 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter9 (
                               Object parameter,
                               CoDependentDomain domain
                               )
            {
                this.parameter = parameter;
                this.domain = domain;
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "Parameter 5 and parameter 9 must belong"
                    + " to the same domain";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object grain
                                             )
            {
                return this.domain.filter (
                    new ArrayObject<Object> ( Object.class,
                                              new Object [] {
                                                  this.parameter,
                                                  grain
                                              } ) );
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

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "Parameter 5 and parameter 9 belong to"
                            + " different domains.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Parameter5.DependsOn.Parameter9.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence
                                                                        )
            {
                return new Parameter5.DependsOn.Parameter9.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
            }

            @Override
            public Parameter5.DependsOn.Parameter9.Violation violation (
                                                                        Object plaintiff,
                                                                        Object /* array, Collection or single Object */ evidence,
                                                                        Throwable cause
                                                                        )
            {
                final Parameter5.DependsOn.Parameter9.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }
    }
}
