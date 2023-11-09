package musaico.foundation.contract.guarantees;

import java.io.Serializable;

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
import musaico.foundation.domains.array.ElementsBelongToDomain;
import musaico.foundation.domains.array.ExcludesElements;
import musaico.foundation.domains.array.ExcludesIndices;
import musaico.foundation.domains.array.ExcludesSpecificClasses;
import musaico.foundation.domains.array.IndicesBelongToDomain;
import musaico.foundation.domains.array.Length;
import musaico.foundation.domains.array.NoDuplicates;
import musaico.foundation.domains.array.NoNulls;

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

import musaico.foundation.domains.string.EmptyString;
import musaico.foundation.domains.string.NotEmptyString;
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

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.composite.Not;


/**
 * <p>
 * Contracts for return-value of a constructor or method.
 * </p>
 *
 * <p>
 * A constructor or method, whether abstract or concrete, can
 * declare its contract by stating that it throws a violation
 * of a specific Return contract.  For example an interface
 * may declare that it throws Return.AlwaysLessThanZero.Violation.
 * In this case, classes which implement that interface must
 * check return-value against the Return.AlwaysLessThanZero.CONTRACT
 * whenever the method is invoked, and throw
 * a Return.AlwaysLessThanZero.Violation runtime exception when
 * the return-value is not in the required domain.
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
 * @see musaico.foundation.contract.guarantees.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.guarantees.MODULE#LICENSE
 */
public class Return
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
    protected Return ()
    {
    }


    /**
     * <p>
     * The return-value always belongs to some arbitrary domain.
     * Typically a method declares this when there is no existing
     * declarative obligation to support its return-value contract.
     * For example, if return-value always belongs to domain
     * Has4Wheels, then the method might declare Return.AlwaysInDomain,
     * check return-value against a new
     * Return.AlwaysInDomain ( Has4Wheels.CONTRACT ) at runtime,
     * and explain the nature of the domain in the method documentation.
     * The AlwaysInDomain contract should be a last resort, since
     * the exception thrown by the method reveals nothing about
     * the domain to which the return value is bound.  For example,
     * throwing Has4Wheels.Violation instead of Return.AlwaysInDomain.Violation
     * reveals more to the caller about what the nature of the domain,
     * and provides an assertion filter for unit testing.
     * </p>
     *
     * @see musaico.foundation.domains
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysInDomain<DOMAIN extends Object>
        implements Contract<DOMAIN, Return.AlwaysInDomain.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final Filter<DOMAIN> domain;

        public AlwaysInDomain (
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
            return "The return value is guaranteed to always be "
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public <VIOLATED_DOMAIN extends Object>
                    Violation (
                               AlwaysInDomain<VIOLATED_DOMAIN> contract,
                               Object plaintiff,
                               Object evidence
                               )
            {
                super ( contract,
                        "The return value was not a member of"
                        + " the guaranteed domain: "
                        + nonMember ( contract, evidence )
                        + ".", // description
                        plaintiff, evidence );
            }

            @SuppressWarnings("unchecked") // Cast inside try...catch.
            private static final <VIOLATED_DOMAIN extends Object>
                    Object nonMember (
                                      AlwaysInDomain<VIOLATED_DOMAIN> contract,
                                      Object evidence
                                      )
            {
                try
                {
                    if ( ! ( contract.domain instanceof Domain ) )
                    {
                        return evidence;
                    }

                    final Domain<VIOLATED_DOMAIN> domain =
                        (Domain<VIOLATED_DOMAIN>) contract.domain;
                    return domain.nonMember (
                                             (VIOLATED_DOMAIN) evidence );
                }
                catch ( Exception e )
                {
                    return evidence;
                }
            }
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
                + " } ";
        }

        @Override
        public Return.AlwaysInDomain.Violation violation (
                                                          Object plaintiff,
                                                          DOMAIN evidence
                                                          )
        {
            return new Return.AlwaysInDomain.Violation ( this,
                                                         plaintiff,
                                                         evidence );
        }

        @Override
        public Return.AlwaysInDomain.Violation violation (
                                                          Object plaintiff,
                                                          DOMAIN evidence,
                                                          Throwable cause
                                                          )
        {
            final Return.AlwaysInDomain.Violation violation =
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
     * The return value is never null.  The same as ReturnNeverNull.
     * Typically a method will only
     * ever throw a Violation of this guarantee if one of its dependents
     * broke its contract.  For example, if a method buildCar ()
     * guarantees the Return.NeverNull.CONTRACT, and its implementation
     * simply invokes <code> this.carType.build () </code>, then if the return
     * value from that method is null, and there is no other way of
     * returning a non-null value, then the buildCar () method
     * should throw a violation of Return.NeverNull.CONTRACT.
     * </p>
     *
     * @see musaico.foundation.domains.NotNull
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class NeverNull
        implements Contract<Object, Return.NeverNull.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.NeverNull CONTRACT =
            new Return.NeverNull ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value was null.", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to never be null.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.NeverNull.Violation violation (
                                                     Object plaintiff,
                                                     Object evidence
                                                     )
        {
            return new Return.NeverNull.Violation ( this,
                                                    plaintiff,
                                                    evidence );
        }

        @Override
        public Return.NeverNull.Violation violation (
                                                     Object plaintiff,
                                                     Object evidence,
                                                     Throwable cause
                                                     )
        {
            final Return.NeverNull.Violation violation =
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
     * The return value will change its state over time.
     * For example, a container of Objects whose contents are removed and
     * added over time.
     * </p>
     *
     * @see musaico.foundation.domains.time.Changing
     * @see musaico.foundation.domains.time.BeforeAndAfter
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class ChangesState
        implements Contract<BeforeAndAfter, Return.ChangesState.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        /** The singleton ChangesState contract. */
        public static final ChangesState DOMAIN =
            new ChangesState ();

        protected ChangesState ()
        {
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to change state"
                + " over time.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         BeforeAndAfter grain
                                         )
        {
            return Changing.DOMAIN.filter ( grain );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              BeforeAndAfter evidence
                              )
            {
                super ( contract,
                        "The return value cannot ever change state: "
                        + Changing.DOMAIN.nonMember ( evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        @Override
        public Return.ChangesState.Violation violation (
                                                        Object plaintiff,
                                                        BeforeAndAfter evidence
                                                        )
        {
            return new Return.ChangesState.Violation ( this,
                                                       plaintiff,
                                                       evidence );
        }

        @Override
        public Return.ChangesState.Violation violation (
                                                        Object plaintiff,
                                                        BeforeAndAfter evidence,
                                                        Throwable cause
                                                        )
        {
            final Return.ChangesState.Violation violation =
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
     * The return value will never change its state.
     * For example, a container of Objects whose contents will never change.
     * </p>
     *
     * @see musaico.foundation.domains.time.Unchanging
     * @see musaico.foundation.domains.time.BeforeAndAfter
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class NeverChangesState
        implements Contract<BeforeAndAfter, Return.NeverChangesState.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        /** The singleton NeverChangesState contract. */
        public static final NeverChangesState DOMAIN =
            new NeverChangesState ();

        protected NeverChangesState ()
        {
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to never change state.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         BeforeAndAfter grain
                                         )
        {
            return Unchanging.DOMAIN.filter ( grain );
        }

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              BeforeAndAfter evidence
                              )
            {
                super ( contract,
                        "The return value could not be guaranteed"
                        + " to never change state: "
                        + Unchanging.DOMAIN.nonMember ( evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        @Override
        public Return.NeverChangesState.Violation violation (
                                                             Object plaintiff,
                                                             BeforeAndAfter evidence
                                                             )
        {
            return new Return.NeverChangesState.Violation ( this,
                                                            plaintiff,
                                                            evidence );
        }

        @Override
        public Return.NeverChangesState.Violation violation (
                                                             Object plaintiff,
                                                             BeforeAndAfter evidence,
                                                             Throwable cause
                                                             )
        {
            final Return.NeverChangesState.Violation violation =
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
     * The return value is always equal to some specific value.
     * For example, a method which accepts only int parameters 6 and 9,
     * and returns an int, might guarantee that it will always return 42.
     * </p>
     *
     * @see musaico.foundation.domains.EqualTo
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysEqualsSpecificValue
        implements Contract<Object, Return.AlwaysEqualsSpecificValue.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        /** The filter which ensures the return value is always equal
         *  to some specific value. */
        private final EqualTo domain;

        /**
         * <p>
         * Creates a new Return.AlwaysEqualsSpecificValue contract
         * for the specified value.
         * </p>
         *
         * @param value The object to which the return value is guaranteed
         *              to always be equal.  Must not be null.
         */
        public AlwaysEqualsSpecificValue ( Object value )
        {
            this ( new EqualTo ( value ) );
        }

        /**
         * <p>
         * Creates a new Return.AlwaysEqualsSpecificValue contract
         * using the specified EqualTo filter to check return
         * values.
         * </p>
         *
         * @param domain The EqualTo filter which determines which
         *               return values meet this guarantee (are KEPT)
         *               and which ones fail (are DISCARDED).
         *               Must not be null.
         */
        public AlwaysEqualsSpecificValue ( EqualTo domain )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value will always be equal to "
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysEqualsSpecificValue contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is equal to "
                        + contract.domain.value ()
                        + ".", // description
                        plaintiff, evidence );
            }
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
                + " } ";
        }

        @Override
        public Return.AlwaysEqualsSpecificValue.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Return.AlwaysEqualsSpecificValue.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysEqualsSpecificValue.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Return.AlwaysEqualsSpecificValue.Violation violation =
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
     * The return value is never equal to some specific value.
     * For example, a method which returns an int might guarantee
     * that it will never return 42.
     * </p>
     *
     * @see musaico.foundation.domains.NotEqualTo
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class NeverEqualsSpecificValue
        implements Contract<Object, Return.NeverEqualsSpecificValue.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        /** The filter which ensures the return value is never equal
         *  to some specific value. */
        private final NotEqualTo domain;

        /**
         * <p>
         * Creates a new Return.NeverEqualsSpecificValue contract
         * for the specified value.
         * </p>
         *
         * @param value The object to which the return value is guaranteed
         *              to never be equal.  Must not be null.
         */
        public NeverEqualsSpecificValue ( Object value )
        {
            this ( new NotEqualTo ( value ) );
        }

        /**
         * <p>
         * Creates a new Return.NeverEqualsSpecificValue contract
         * using the specified NotEqualTo filter to check return
         * values.
         * </p>
         *
         * @param domain The NotEqualTo filter which determines which
         *               return values meet this guarantee (are KEPT)
         *               and which ones fail (are DISCARDED).
         *               Must not be null.
         */
        public NeverEqualsSpecificValue ( NotEqualTo domain )
        {
            this.domain = domain;
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value will never be equal to "
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              NeverEqualsSpecificValue contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is equal to "
                        + contract.domain.value ()
                        + ".", // description
                        plaintiff, evidence );
            }
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
                + " } ";
        }

        @Override
        public Return.NeverEqualsSpecificValue.Violation violation (
                                                                    Object plaintiff,
                                                                    Object evidence
                                                                    )
        {
            return new Return.NeverEqualsSpecificValue.Violation ( this,
                                                                   plaintiff,
                                                                   evidence );
        }

        @Override
        public Return.NeverEqualsSpecificValue.Violation violation (
                                                                    Object plaintiff,
                                                                    Object evidence,
                                                                    Throwable cause
                                                                    )
        {
            final Return.NeverEqualsSpecificValue.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than -1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanNegativeOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanNegativeOne
        implements Contract<Number, Return.AlwaysGreaterThanNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanNegativeOne CONTRACT =
            new Return.AlwaysGreaterThanNegativeOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanNegativeOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is <= -1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be > -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysGreaterThanNegativeOne.Violation violation (
                                                                        Object plaintiff,
                                                                        Number evidence
                                                                        )
        {
            return new Return.AlwaysGreaterThanNegativeOne.Violation ( this,
                                                                       plaintiff,
                                                                       evidence );
        }

        @Override
        public Return.AlwaysGreaterThanNegativeOne.Violation violation (
                                                                        Object plaintiff,
                                                                        Number evidence,
                                                                        Throwable cause
                                                                        )
        {
            final Return.AlwaysGreaterThanNegativeOne.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than 1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanOne
        implements Contract<Number, Return.AlwaysGreaterThanOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOne CONTRACT =
            new Return.AlwaysGreaterThanOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is <= 1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be > 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysGreaterThanOne.Violation violation (
                                                                 Object plaintiff,
                                                                 Number evidence
                                                                 )
        {
            return new Return.AlwaysGreaterThanOne.Violation ( this,
                                                                plaintiff,
                                                                evidence );
        }

        @Override
        public Return.AlwaysGreaterThanOne.Violation violation (
                                                                 Object plaintiff,
                                                                 Number evidence,
                                                                 Throwable cause
                                                                 )
        {
            final Return.AlwaysGreaterThanOne.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than 0.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanZero
     *  @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanZero
        implements Contract<Number, Return.AlwaysGreaterThanZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanZero CONTRACT =
            new Return.AlwaysGreaterThanZero ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanZero contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is <= 0: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be > 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysGreaterThanZero.Violation violation (
                                                                 Object plaintiff,
                                                                 Number evidence
                                                                 )
        {
            return new Return.AlwaysGreaterThanZero.Violation ( this,
                                                                plaintiff,
                                                                evidence );
        }

        @Override
        public Return.AlwaysGreaterThanZero.Violation violation (
                                                                 Object plaintiff,
                                                                 Number evidence,
                                                                 Throwable cause
                                                                 )
        {
            final Return.AlwaysGreaterThanZero.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than or equal to -1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualToNegativeOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanOrEqualToNegativeOne
        implements Contract<Number, Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOrEqualToNegativeOne CONTRACT =
            new Return.AlwaysGreaterThanOrEqualToNegativeOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanOrEqualToNegativeOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is < -1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be >= -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than or equal to 1.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualToOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanOrEqualToOne
        implements Contract<Number, Return.AlwaysGreaterThanOrEqualToOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOrEqualToOne CONTRACT =
            new Return.AlwaysGreaterThanOrEqualToOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanOrEqualToOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is < 1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be >= 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysGreaterThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysGreaterThanOrEqualToOne.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysGreaterThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysGreaterThanOrEqualToOne.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than or equal to 0.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualToZero
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanOrEqualToZero
        implements Contract<Number, Return.AlwaysGreaterThanOrEqualToZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOrEqualToZero CONTRACT =
            new Return.AlwaysGreaterThanOrEqualToZero ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanOrEqualToZero contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is < 0: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be >= 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysGreaterThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysGreaterThanOrEqualToZero.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysGreaterThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysGreaterThanOrEqualToZero.Violation violation =
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
     * The return value is a number that is guaranteed to be less than -1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanNegativeOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanNegativeOne
        implements Contract<Number, Return.AlwaysLessThanNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanNegativeOne CONTRACT =
            new Return.AlwaysLessThanNegativeOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanNegativeOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is >= -1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be < -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysLessThanNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanNegativeOne.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThanNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanNegativeOne.Violation violation =
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
     * The return value is a number that is guaranteed to be less than 1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanOne
        implements Contract<Number, Return.AlwaysLessThanOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOne CONTRACT =
            new Return.AlwaysLessThanOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is >= 1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be < 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysLessThanOne.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanOne.Violation ( this,
                                                            plaintiff,
                                                            evidence );
        }

        @Override
        public Return.AlwaysLessThanOne.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanOne.Violation violation =
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
     * The return value is a number that is guaranteed to be less than 0.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanZero
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanZero
        implements Contract<Number, Return.AlwaysLessThanZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanZero CONTRACT =
            new Return.AlwaysLessThanZero ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanZero contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is >= 0: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be < 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysLessThanZero.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanZero.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThanZero.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanZero.Violation violation =
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
     * The return value is a number that is guaranteed to be less
     * than or equal to -1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualToNegativeOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanOrEqualToNegativeOne
        implements Contract<Number, Return.AlwaysLessThanOrEqualToNegativeOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOrEqualToNegativeOne CONTRACT =
            new Return.AlwaysLessThanOrEqualToNegativeOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanOrEqualToNegativeOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is > -1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be <= -1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysLessThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanOrEqualToNegativeOne.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanOrEqualToNegativeOne.Violation violation =
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
     * The return value is a number that is guaranteed to be less
     * than or equal to 1.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualToOne
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanOrEqualToOne
        implements Contract<Number, Return.AlwaysLessThanOrEqualToOne.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOrEqualToOne CONTRACT =
            new Return.AlwaysLessThanOrEqualToOne ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanOrEqualToOne contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is > 1: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be <= 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysLessThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanOrEqualToOne.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanOrEqualToOne.Violation violation =
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
     * The return value is a number that is guaranteed to be less
     * than or equal to 0.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualToZero
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanOrEqualToZero
        implements Contract<Number, Return.AlwaysLessThanOrEqualToZero.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOrEqualToZero CONTRACT =
            new Return.AlwaysLessThanOrEqualToZero ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanOrEqualToZero contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is > 0: "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be <= 0.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysLessThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanOrEqualToZero.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanOrEqualToZero.Violation violation =
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
     * The return value is a number that is guaranteed to be greater
     * than some specific number.
     * </p>
     *
     * @see musaico.foundation.domains.number.GreaterThanNumber
     *  @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThan
        implements Contract<Number, Return.AlwaysGreaterThan.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final GreaterThanNumber domain;

        /**
         * <p>
         * Creates a new domain of return numbers which are
         * greater than a specific value.
         * </p>
         *
         * @param number The number which all return numbers are
         *               greater than.  Must not be null.
         */
        public AlwaysGreaterThan (
                                  Number number
                                  )
        {
            this ( new GreaterThanNumber ( number ) );
        }


        /**
         * <p>
         * Creates a new domain of return numbers which are
         * greater than a specific value.
         * </p>
         *
         * @param domain The domain of numbers greater than
         *               some specific value.  Must not be null.
         */
        public AlwaysGreaterThan (
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
            return "The return value is guaranteed to be "
                + this.domain;
        }

        /**
         * @return The domain of all return values which are greater
         *         than some specific number.  Never null.
         */
        public final GreaterThanNumber domain ()
        {
            return this.domain;
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
            return ClassName.of ( this.getClass ()
                                  + " ( " + this.domain.value () + " )" );
        }

        @Override
        public Return.AlwaysGreaterThan.Violation violation (
                                                             Object plaintiff,
                                                             Number evidence
                                                             )
        {
            return new Return.AlwaysGreaterThan.Violation ( this,
                                                            plaintiff,
                                                            evidence );
        }

        @Override
        public Return.AlwaysGreaterThan.Violation violation (
                                                             Object plaintiff,
                                                             Number evidence,
                                                             Throwable cause
                                                             )
        {
            final Return.AlwaysGreaterThan.Violation violation =
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThan contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract, // contract
                        "The return value is not "
                        + contract.domain ()
                        + ": "
                        + evidence
                        + ".", // description
                        plaintiff, // plaintiff
                        evidence ); // evidence
            }
        }
    }




    /**
     * <p>
     * The return value is a number that is guaranteed to be greater
     * than or equal to some specific number.
     * </p>
     *
     * @see musaico.foundation.domains.GreaterThanOrEqualTo
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysGreaterThanOrEqualTo
        implements Contract<Number, Return.AlwaysGreaterThanOrEqualTo.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final GreaterThanOrEqualToNumber domain;

        /**
         * <p>
         * Creates a new domain of return numbers which are
         * greater than or equal to a specific value.
         * </p>
         *
         * @param number The number which all return numbers are
         *               greater than or equal to.  Must not be null.
         */
        public AlwaysGreaterThanOrEqualTo (
                Number number
                )
        {
            this ( new GreaterThanOrEqualToNumber ( number ) );
        }


        /**
         * <p>
         * Creates a new domain of return numbers which are
         * greater than or equal to a specific value.
         * </p>
         *
         * @param domain The domain of numbers greater than or equal to
         *               some specific number.  Must not be null.
         */
        public AlwaysGreaterThanOrEqualTo (
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
            return "The return value is guaranteed to be "
                + this.domain;
        }

        /**
         * @return The domain of all return values which are greater than
         *         or equal to some specific number.  Never null.
         */
        public final GreaterThanOrEqualToNumber domain ()
        {
            return this.domain;
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
            return ClassName.of ( this.getClass ()
                                  + " ( " + this.domain.value () + " )" );
        }

        @Override
        public Return.AlwaysGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysGreaterThanOrEqualTo.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysGreaterThanOrEqualTo.Violation violation =
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysGreaterThanOrEqualTo contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract, // contract
                        "The return value is not "
                        + contract.domain ()
                        + ": "
                        + evidence
                        + ".", // description
                        plaintiff, // plaintiff
                        evidence ); // evidence
            }
        }
    }




    /**
     * <p>
     * The return value is a number that is guaranteed to be less than some specific number.
     * </p>
     *
     * @see musaico.foundation.domains.LessThan
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThan
        implements Contract<Number, Return.AlwaysLessThan.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final LessThanNumber domain;

        /**
         * <p>
         * Creates a new domain of return numbers which are
         * less than a specific value.
         * </p>
         *
         * @param number The number which all return numbers are
         *               less than.  Must not be null.
         */
        public AlwaysLessThan (
                Number number
                )
        {
            this ( new LessThanNumber ( number ) );
        }


        /**
         * <p>
         * Creates a new domain of return numbers which are
         * less than a specific value.
         * </p>
         *
         * @param domain The domain of numbers less than
         *               some specific value.  Must not be null.
         */
        public AlwaysLessThan (
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
            return "The return value is guaranteed to be "
                + this.domain;
        }


        /**
         * @return The domain of all return values which are less than
         *         some specific number.  Never null.
         */
        public final LessThanNumber domain ()
        {
            return this.domain;
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
            return ClassName.of ( this.getClass ()
                                  + " ( " + this.domain.value () + " )" );
        }

        @Override
        public Return.AlwaysLessThan.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThan.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThan.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThan.Violation violation =
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThan contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract, // contract
                        "The return value is not "
                        + contract.domain ()
                        + ": "
                        + evidence
                        + ".", // description
                        plaintiff, // plaintiff
                        evidence ); // evidence
            }
        }
    }




    /**
     * <p>
     * The return value is a number that is guaranteed to be less
     * than or equal to some specific number.
     * </p>
     *
     * @see musaico.foundation.domains.LessThanOrEqualTo
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysLessThanOrEqualTo
        implements Contract<Number, Return.AlwaysLessThanOrEqualTo.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final LessThanOrEqualToNumber domain;

        /**
         * <p>
         * Creates a new domain of return numbers which are
         * less than or equal to a specific value.
         * </p>
         *
         * @param number The number which all return numbers are
         *               less than or equal to.  Must not be null.
         */
        public AlwaysLessThanOrEqualTo (
                Number number
                )
        {
            this ( new LessThanOrEqualToNumber ( number ) );
        }


        /**
         * <p>
         * Creates a new domain of return numbers which are
         * less than or equal to a specific value.
         * </p>
         *
         * @param domain The domain of numbers less than or equal to
         *               some specific value.  Must not be null.
         */
        public AlwaysLessThanOrEqualTo (
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
            return "The return value is guaranteed to be "
                + this.domain;
        }


        /**
         * @return The domain of all return values which are less than
         *         or equal to some specific number.  Never null.
         */
        public final LessThanOrEqualToNumber domain ()
        {
            return this.domain;
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
            return ClassName.of ( this.getClass ()
                                  + " ( " + this.domain.value () + " )" );
        }

        @Override
        public Return.AlwaysLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence
                )
        {
            return new Return.AlwaysLessThanOrEqualTo.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                Number evidence,
                Throwable cause
                )
        {
            final Return.AlwaysLessThanOrEqualTo.Violation violation =
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysLessThanOrEqualTo contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract, // contract
                        "The return value is not "
                        + contract.domain ()
                        + ": "
                        + evidence
                        + ".", // description
                        plaintiff, // plaintiff
                        evidence ); // evidence
            }
        }
    }




    /**
     * <p>
     * The return value is a comparable value that is guaranteed to
     * within specific (minimum value, maximum value) bounds.
     * For example if a method pH () returns a BigDecimal value, then
     * it might assert the guarantee
     * <code> new Return.AlwaysInBounds ( BigDecimal.ZERO,
     *                                    new BigDecimal ( "14" ) ) </code>.
     * Ideally this guarantee would be a new declarative class
     * which extends Return.AlwaysInBounds, such that the method
     * could declare <code> throws Between0And14.Violation </code>.
     * The declarative approach provides easy documentation
     * and provides an easy to use assertion filter for unit testing.
     * </p>
     *
     * @see musaico.foundation.domains.comparability.BoundedDomain
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysInBounds<BOUNDED extends Object>
        implements Contract<BOUNDED, Return.AlwaysInBounds.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final BoundedDomain<BOUNDED> domain;

        /** Closed bounds: [ min, max ]. */
        public AlwaysInBounds (
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

        public AlwaysInBounds (
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

        public AlwaysInBounds (
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
            return "The return value is guaranteed to be in "
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

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              AlwaysInBounds<?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value is outside the bounds { "
                        + contract.domain.minimum ()
                        + ", "
                        + contract.domain.maximum ()
                        + " } (inclusive): "
                        + evidence
                        + ".", // description
                        plaintiff, evidence );
            }
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
        public Return.AlwaysInBounds.Violation violation (
                Object plaintiff,
                BOUNDED evidence
                )
        {
            return new Return.AlwaysInBounds.Violation ( this,
                                                         plaintiff,
                                                         evidence );
        }

        @Override
        public Return.AlwaysInBounds.Violation violation (
                Object plaintiff,
                BOUNDED evidence,
                Throwable cause
                )
        {
            final Return.AlwaysInBounds.Violation violation =
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
     * The return value is always an instance of a specific class.
     * For example, a method returning a Number
     * might guarantee that the return value will be an instance of
     * Integer.class.
     * </p>
     *
     * @see musaico.foundation.domains.InstanceOfClass
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysInstanceOfClass
        implements Contract<Object, Return.AlwaysInstanceOfClass.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final InstanceOfClass domain;

        public AlwaysInstanceOfClass (
                Class<?> domain_class
                )
        {
            this ( new InstanceOfClass ( domain_class ) );
        }

        public AlwaysInstanceOfClass (
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
            return "The return value is guaranteed to be an "
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
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value was not an instance"
                        + " of the guaranteed class.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Return.AlwaysInstanceOfClass.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Return.AlwaysInstanceOfClass.Violation violation =
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
     * The return value is never an instance of a specific class.
     * For example, a method returning a Number
     * might guarantee that the parameter is never an instance of
     * BigInteger.class.
     * </p>
     *
     * @see musaico.foundation.domains.InstanceOfClass
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class NeverInstanceOfClass
        implements Contract<Object, Return.NeverInstanceOfClass.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final InstanceOfClass verbotenDomain;

        public NeverInstanceOfClass (
                Class<?> verboten_class
                )
        {
            this ( new InstanceOfClass ( verboten_class ) );
        }

        public NeverInstanceOfClass (
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
            return "The return value is never an "
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
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value was an instance"
                        + " of the verboten class.", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.NeverInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence
                )
        {
            return new Return.NeverInstanceOfClass.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.NeverInstanceOfClass.Violation violation (
                Object plaintiff,
                Object evidence,
                Throwable cause
                )
        {
            final Return.NeverInstanceOfClass.Violation violation =
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
     * Every return value is guaranteed to be "", the empty String.
     * </p>
     *
     * @see musaico.foundation.domains.EmptyString
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysEmptyString
        implements Contract<String, Return.AlwaysEmptyString.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysEmptyString CONTRACT =
            new Return.AlwaysEmptyString ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value was not \"\": "
                        + EmptyString.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to be"
                + " the empty String (\"\").";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysEmptyString.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysEmptyString.Violation ( this,
                                                            plaintiff,
                                                            evidence );
        }

        @Override
        public Return.AlwaysEmptyString.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysEmptyString.Violation violation =
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
     * The return value is never "", the empty String.
     * </p>
     *
     * @see musaico.foundation.domains.NotEmptyString
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class NeverEmptyString
        implements Contract<String, Return.NeverEmptyString.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.NeverEmptyString CONTRACT =
            new Return.NeverEmptyString ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value was \"\".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to never be"
                + " the empty String (\"\").";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.NeverEmptyString.Violation violation (
                                                            Object plaintiff,
                                                            String evidence
                                                            )
        {
            return new Return.NeverEmptyString.Violation ( this,
                                                           plaintiff,
                                                           evidence );
        }

        @Override
        public Return.NeverEmptyString.Violation violation (
                                                            Object plaintiff,
                                                            String evidence,
                                                            Throwable cause
                                                            )
        {
            final Return.NeverEmptyString.Violation violation =
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
     * The return value is always a String of an exact length or
     * of a length in some range.  For example, a sha1ToHexString ()
     * method might guarantee a return String exactly 40 characters
     * long.  On the other hand, a surname () method might guarantee
     * a return String at least one character long.  Or a upcECode ()
     * method might guarantee a String between 5 and 6 characters long.
     * And so on.
     * </p>
     *
     * @see musaico.foundation.domains.StringLength
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysStringLength
        implements Contract<String, Return.AlwaysStringLength.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final StringLength domain;

        public AlwaysStringLength (
                                   int exact_length
                                   )
        {
            this ( new StringLength ( exact_length ) );
        }

        public AlwaysStringLength (
                                   int minimum_length,
                                   int maximum_length
                                   )
        {
            this ( new StringLength ( minimum_length, maximum_length ) );
        }

        public AlwaysStringLength (
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
            return "The return String is guaranteed to always be "
                + this.domain
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysStringLength contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value's String length was out of bounds: "
                        + contract.domain.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysStringLength.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysStringLength.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysStringLength.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysStringLength.Violation violation =
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
     * The return value never contains any whitespace characters, such
     * as ' ', '\t', '\n', '\r', and so on.
     * </p>
     *
     * @see musaico.foundation.domains.StringExcludesSpaces
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysExcludesSpaces
        implements Contract<String, Return.AlwaysExcludesSpaces.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysExcludesSpaces CONTRACT =
            new Return.AlwaysExcludesSpaces ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value contained space characters: "
                        + StringExcludesSpaces.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return String is guaranteed to never contain"
                + " any space characters (' ', '\\t', '\\n', '\\r',"
                + " and so on).";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysExcludesSpaces.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysExcludesSpaces.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysExcludesSpaces.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysExcludesSpaces.Violation violation =
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
     * The return value is guaranteed to contain at least one
     * character which is NOT whitespace
     * (' ', '\t', '\n', '\r', and so on).  Each String MAY also
     * contain whitespace.
     * </p>
     *
     * @see musaico.foundation.domains.ContainsNonSpaces
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsNonSpaces
        implements Contract<String, Return.AlwaysContainsNonSpaces.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsNonSpaces CONTRACT =
            new Return.AlwaysContainsNonSpaces ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return String did not contain any non-space"
                        + "characters: "
                        + StringContainsNonSpaces.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return String is guaranteed to contain"
                + " one or more non-space character(s).";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysContainsNonSpaces.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysContainsNonSpaces.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsNonSpaces.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsNonSpaces.Violation violation =
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
     * The return value is guaranteed to contain only digits from
     * any alphabet, such as '1', '2', '3', and so on.
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyNumerics
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyNumerics
        implements Contract<String, Return.AlwaysContainsOnlyNumerics.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyNumerics CONTRACT =
            new Return.AlwaysContainsOnlyNumerics ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return String contained non-numeric"
                        + " character(s): "
                        + StringContainsOnlyNumerics.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to contain"
                + " only numeric characters ('0', '1', '2', ...).";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysContainsOnlyNumerics.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysContainsOnlyNumerics.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyNumerics.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyNumerics.Violation violation =
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
     * The return value is guaranteed to contain only letters
     * from any alphabet, such as 'a', 'A', 'b', and so on.
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyAlpha
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyAlpha
        implements Contract<String, Return.AlwaysContainsOnlyAlpha.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyAlpha CONTRACT =
            new Return.AlwaysContainsOnlyAlpha ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return value contained non-alpha character(s): "
                        + StringContainsOnlyAlpha.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return value is guaranteed to contain"
                + "only alpha characters ('A', 'a', 'B', ...).";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysContainsOnlyAlpha.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysContainsOnlyAlpha.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyAlpha.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyAlpha.Violation violation =
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
     * The return value is guaranteed to contain only letters and/or
     * digits from any alphabet, such as 'a', 'A', 'b', '1', '2', '3',
     * and so on.
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyAlphaNumerics
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyAlphaNumerics
        implements Contract<String, Return.AlwaysContainsOnlyAlphaNumerics.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyAlphaNumerics CONTRACT =
            new Return.AlwaysContainsOnlyAlphaNumerics ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return String contained non-alphanumeric"
                        + " character(s): "
                        + StringContainsOnlyAlphaNumerics.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return String is guaranteed to contain"
                + " only alphanumeric character(s) ('A', 'a', '0', '1', ...).";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysContainsOnlyAlphaNumerics.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysContainsOnlyAlphaNumerics.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyAlphaNumerics.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyAlphaNumerics.Violation violation =
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
     * The return value is guaranteed to contain only printable characters
     * from any alphabet, not control characters.
     * </p>
     *
     * @see musaico.foundation.domains.StringContainsOnlyPrintableCharacters
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyPrintableCharacters
        implements Contract<String, Return.AlwaysContainsOnlyPrintableCharacters.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyPrintableCharacters CONTRACT =
            new Return.AlwaysContainsOnlyPrintableCharacters ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return String contained non-printable"
                        + " character(s) at "
                        + nonPrintableCharacterIndices (
                            evidence == null
                                ? null
                                : "" + evidence )
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

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return String is guaranteed to contain"
                + " only printable character(s), including alphanumerics,"
                + " whitespace, newlines, punctuation, and so on, but"
                + " never any control codes (escape, backspace, and so on).";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysContainsOnlyPrintableCharacters.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysContainsOnlyPrintableCharacters.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyPrintableCharacters.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyPrintableCharacters.Violation violation =
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
     * The return value is guaranteed to meet the requirements of
     * the String ID domain.  Every ID starts with an ASCii letter
     * 'a', 'A', 'b', and so on, or an underscore '_'.  Subsequent
     * characters in the ID may be ASCii letters, underscore,
     * or ASCii digits '1', '2', '3', and so on.  An ID is guaranteed
     * to be at least one character long.  IDs are based on
     * the case-sensitive names of methods, procedures, variables
     * and so on in many programming languages.
     * </p>
     *
     * @see musaico.foundation.domains.StringID
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysStringID
        implements Contract<String, Return.AlwaysStringID.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysStringID CONTRACT =
            new Return.AlwaysStringID ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return String was not a valid ID: "
                        + StringID.DOMAIN.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return String is guaranteed to always be an ID"
                + " (matching the regular expression "
                + StringID.DOMAIN.pattern ().pattern ()
                + ").";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
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

        @Override
        public Return.AlwaysStringID.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysStringID.Violation ( this,
                                                         plaintiff,
                                                         evidence );
        }

        @Override
        public Return.AlwaysStringID.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysStringID.Violation violation =
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
     * The return String value is guaranteed to match some specific
     * regular expression pattern.
     * </p>
     *
     * @see musaico.foundation.domains.StringPattern
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysMatchesPattern
        implements Contract<String, Return.AlwaysMatchesPattern.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final StringPattern domain;

        public AlwaysMatchesPattern (
                                     Pattern pattern
                                     )
        {
            this ( new StringPattern ( pattern ) );
        }

        public AlwaysMatchesPattern (
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
            return "The return String is guaranteed to always match"
                + " the regular expression pattern "
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysMatchesPattern contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return String did not match the pattern: "
                        + contract.domain.nonMember (
                              evidence == null
                                  ? null
                                  : "" + evidence )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysMatchesPattern.Violation violation (
                Object plaintiff,
                String evidence
                )
        {
            return new Return.AlwaysMatchesPattern.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysMatchesPattern.Violation violation (
                Object plaintiff,
                String evidence,
                Throwable cause
                )
        {
            final Return.AlwaysMatchesPattern.Violation violation =
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
     * The return value is an array or Collection or other Iterable
     * containing every element from a certain set of values.
     * The elements may be in any order, but each value is guaranteed
     * to be present at least once.
     * </p>
     *
     * @see musaico.foundation.domains.array.ContainsElements
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsElements
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsElements.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ContainsElements domain;

        public AlwaysContainsElements (
                                       Object ... elements
                                       )
        {
            this ( new ContainsElements ( elements ) );
        }

        public AlwaysContainsElements (
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
            // Strip out the leading "ContainsElements " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String elements =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to always"
                + " contain the element(s) "
                + elements;
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysContainsElements contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values did not contain all of"
                        + " the required element(s): "
                        + contract.domain.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysContainsElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsElements.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsElements.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * guaranteed to exclude all of the values in a specific set.
     * </p>
     *
     * @see musaico.foundation.domains.array.ExcludesElements
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysExcludesElements
        implements Contract<Object /* array or Collection */, Return.AlwaysExcludesElements.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ExcludesElements domain;

        public AlwaysExcludesElements (
                                       Object ... elements
                                       )
        {
            this ( new ExcludesElements ( elements ) );
        }

        public AlwaysExcludesElements (
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
            // Strip out the leading "ExcludesElements " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String elements =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to never"
                + " contain any of the element(s) "
                + elements;
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysExcludesElements contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained one or more verboten"
                        + " element(s): "
                        + contract.domain.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysExcludesElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysExcludesElements.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysExcludesElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysExcludesElements.Violation violation =
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
     * The return value is an array or Collection or other Iterable
     * containing only elements from a specific domain.
     * </p>
     *
     * @see musaico.foundation.domains.array.ElementsBelongToDomain
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyDomainElements
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsOnlyDomainElements.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ElementsBelongToDomain domain;

        public AlwaysContainsOnlyDomainElements (
                                                 Filter<?> element_domain
                                                 )
        {
            this ( new ElementsBelongToDomain ( element_domain ) );
        }

        public AlwaysContainsOnlyDomainElements (
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
            // Strip out the leading "ElementsBelongToDomain " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String domain_elements =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to always"
                + " contain only element(s) from the domain "
                + domain_elements;
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysContainsOnlyDomainElements contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained element(s)"
                        + " from outside the domain: "
                        + contract.domain.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysContainsOnlyDomainElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsOnlyDomainElements.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyDomainElements.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyDomainElements.Violation violation =
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
     * The return value is an array or Collection or other Iterable
     * containing every index from a certain set of numbers.
     * The required indices may be in any order, but each one is guaranteed
     * to be present at least once.
     * </p>
     *
     * @see musaico.foundation.domains.array.ContainsIndices
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsIndices
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsIndices.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ContainsIndices domain;

        public AlwaysContainsIndices (
                                      long ... indices
                                      )
        {
            this ( new ContainsIndices ( indices ) );
        }

        public AlwaysContainsIndices (
                                      int ... indices
                                      )
        {
            this ( new ContainsIndices ( indices ) );
        }

        public AlwaysContainsIndices (
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
            // Strip out the leading "ContainsIndices " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String indices =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to always"
                + " contain the indice(s) "
                + indices;
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysContainsIndices contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values did not contain all of"
                        + " the required indice(s): "
                        + contract.domain.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysContainsIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsIndices.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsIndices.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * guaranteed to exclude all of the indices in a specific set of numbers.
     * </p>
     *
     * @see musaico.foundation.domains.array.ExcludesIndices
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysExcludesIndices
        implements Contract<Object /* array or Collection */, Return.AlwaysExcludesIndices.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ExcludesIndices domain;

        public AlwaysExcludesIndices (
                                      long ... indices
                                      )
        {
            this ( new ExcludesIndices ( indices ) );
        }

        public AlwaysExcludesIndices (
                                      int ... indices
                                      )
        {
            this ( new ExcludesIndices ( indices ) );
        }

        public AlwaysExcludesIndices (
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
            // Strip out the leading "ExcludesIndices " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String indices =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to never"
                + " contain any of the indice(s) "
                + indices;
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysExcludesIndices contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained one or more verboten"
                        + " indice(s): "
                        + contract.domain.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysExcludesIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysExcludesIndices.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysExcludesIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysExcludesIndices.Violation violation =
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
     * The return value is an array or Collection or other Iterable
     * containing only indices from a specific domain of numbers.
     * </p>
     *
     * @see musaico.foundation.domains.array.IndicesBelongToDomain
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyDomainIndices
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsOnlyDomainIndices.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final IndicesBelongToDomain domain;

        public AlwaysContainsOnlyDomainIndices (
                                                 Filter<Number> index_domain
                                                 )
        {
            this ( new IndicesBelongToDomain ( index_domain ) );
        }

        public AlwaysContainsOnlyDomainIndices (
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
            // Strip out the leading "IndicesBelongToDomain " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String domain_indices =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to always"
                + " contain only indice(s) belonging to the domain "
                + domain_indices;
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysContainsOnlyDomainIndices contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained indices from"
                        + " outside the domain: "
                        + contract.domain.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysContainsOnlyDomainIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsOnlyDomainIndices.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyDomainIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyDomainIndices.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * guaranteed to contain no duplicate values.  Every element
     * is guaranteed to be unique.
     * </p>
     *
     * @see musaico.foundation.domains.array.NoDuplicates
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsNoDuplicates
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsNoDuplicates.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsNoDuplicates CONTRACT =
            new Return.AlwaysContainsNoDuplicates ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained duplicate elements: "
                        + NoDuplicates.DOMAIN.nonMember (
                              new ArrayObject<Object> ( Object.class,
                                                        evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return values are guaranteed not to contain"
                + " any duplicate values.  For example, the"
                + " return array { 1, 2, 3 } would be acceptable,"
                + " but { 1, 2, 1 } would not, since it contains"
                + " duplicates of the value 1.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return NoDuplicates.DOMAIN.filter (
                    new ArrayObject<Object> ( Object.class,
                                              array_or_collection ) );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        @Override
        public Return.AlwaysContainsNoDuplicates.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsNoDuplicates.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsNoDuplicates.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsNoDuplicates.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * whose length always falls within some domain.
     * </p>
     */
    public static class Length
    {
        /**
         * <p>
         * The result is an array, Collection or other Iterable
         * which always has a Length greater than 1.
         * </p>
         *
         * @see musaico.foundation.domains.array.Length
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysGreaterThanOne
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysGreaterThanOne.Violation>,
                       Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysGreaterThanOne CONTRACT =
                new Return.Length.AlwaysGreaterThanOne ();

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return values contained too few elements: "
                            + musaico.foundation.domains.array.Length.GREATER_THAN_ONE_DOMAIN.nonMember (
                                  new ArrayObject<Object> ( Object.class,
                                                            evidence ) )
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "The return values are guaranteed to contain"
                    + " more than 1 element";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object array_or_collection
                                             )
            {
                return musaico.foundation.domains.array.Length.GREATER_THAN_ONE_DOMAIN.filter (
                    new ArrayObject<Object> ( Object.class,
                                              array_or_collection ) );
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () );
            }

            @Override
            public Return.Length.AlwaysGreaterThanOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysGreaterThanOne.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysGreaterThanOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysGreaterThanOne.Violation violation =
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
         * The result is an array, Collection or other Iterable
         * which always has a Length greater than 0.
         * </p>
         *
         * @see musaico.foundation.domains.array.Length
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysGreaterThanZero
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysGreaterThanZero.Violation>,
                       Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysGreaterThanZero CONTRACT =
                new Return.Length.AlwaysGreaterThanZero ();

            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return values contained too few elements: "
                            + musaico.foundation.domains.array.Length.GREATER_THAN_ZERO_DOMAIN.nonMember (
                                  new ArrayObject<Object> ( Object.class,
                                                            evidence ) )
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "The return values are guaranteed to contain"
                    + " at least 1 element";
            }

            /**
             * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
             */
            @Override
            public final FilterState filter (
                                             Object array_or_collection
                                             )
            {
                return musaico.foundation.domains.array.Length.GREATER_THAN_ZERO_DOMAIN.filter (
                    new ArrayObject<Object> ( Object.class,
                                              array_or_collection ) );
            }

            /**
             * @see java.lang.Object#toString()
             */
            @Override
            public final String toString ()
            {
                return ClassName.of ( this.getClass () );
            }

            @Override
            public Return.Length.AlwaysGreaterThanZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysGreaterThanZero.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysGreaterThanZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysGreaterThanZero.Violation violation =
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
         * The array or collection return value's length is always exactly
         * a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.EqualToNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysEqualTo
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysEqualTo.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;


            private final musaico.foundation.domains.array.Length domain;

            public AlwaysEqualTo (
                                  int length
                                  )
            {
                this ( new EqualToNumber ( length ) );
            }

            public AlwaysEqualTo (
                                  EqualToNumber domain
                                  )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public AlwaysEqualTo (
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
                return "Return value is always of "
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysEqualTo.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysEqualTo.Violation violation =
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
         * The array or Collection return value's length is not ever
         * equal to a specific value.
         * </p>
         *
         * @see musaico.foundation.domains.number.NotEqualToNumber
         * @see musaico.foundation.contract.obligations.Return
         */
        public static class NeverEqualTo
            implements Contract<Object /* array or Collection */, Return.Length.NeverEqualTo.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public NeverEqualTo (
                                 int length
                                 )
            {
                this ( new NotEqualToNumber ( length ) );
            }

            public NeverEqualTo (
                                 NotEqualToNumber domain
                                 )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public NeverEqualTo (
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
                return "Return value is of "
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.NeverEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.NeverEqualTo.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.NeverEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.NeverEqualTo.Violation violation =
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
         * The array or collection return value's length is always
         * exactly 0.
         * </p>
         *
         * @see musaico.foundation.domains.number.EqualToNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysEqualsZero
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysEqualsZero.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysEqualsZero CONTRACT =
                new Return.Length.AlwaysEqualsZero ();

            private final musaico.foundation.domains.array.Length domain;

            private AlwaysEqualsZero ()
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
                return "Return value is always of length 0.";
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
            public Return.Length.AlwaysEqualsZero.Violation violation (
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
            public Return.Length.AlwaysEqualsZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysEqualsZero.Violation violation =
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
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
         * The array or collection return value's length is always
         * exactly 1.
         * </p>
         *
         * @see musaico.foundation.domains.number.EqualToNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysEqualsOne
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysEqualsOne.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysEqualsOne CONTRACT =
                new Return.Length.AlwaysEqualsOne ();

            private final musaico.foundation.domains.array.Length domain;

            private AlwaysEqualsOne ()
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
                return "Return value is always of length 1.";
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
            public Return.Length.AlwaysEqualsOne.Violation violation (
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
            public Return.Length.AlwaysEqualsOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysEqualsOne.Violation violation =
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
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
         * The array or collection return value's length is always
         * greater than a specific number.
         * </p>
         *
         * @see musaico.foundation.domains.number.GreaterThanNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysGreaterThan
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysGreaterThan.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;


            private final musaico.foundation.domains.array.Length domain;

            public AlwaysGreaterThan (
                                      int length
                                      )
            {
                this ( new GreaterThanNumber ( length ) );
            }

            public AlwaysGreaterThan (
                                      GreaterThanNumber domain
                                      )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public AlwaysGreaterThan (
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
                return "Return value is always of "
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysGreaterThan.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysGreaterThan.Violation violation =
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
         * The array or collection return value's length is always
         * greater than or equal to a specific number.
         * </p>
         *
         * @see musaico.foundation.domains.number.GreaterThanOrEqualToNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysGreaterThanOrEqualTo
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysGreaterThanOrEqualTo.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;


            private final musaico.foundation.domains.array.Length domain;

            public AlwaysGreaterThanOrEqualTo (
                                               int length
                                               )
            {
                this ( new GreaterThanOrEqualToNumber ( length ) );
            }

            public AlwaysGreaterThanOrEqualTo (
                                               GreaterThanOrEqualToNumber domain
                                               )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public AlwaysGreaterThanOrEqualTo (
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
                return "Return value is always of "
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysGreaterThanOrEqualTo.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysGreaterThanOrEqualTo.Violation violation =
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
         * The array or collection return value's length is always
         * greater than a specific number.
         * </p>
         *
         * @see musaico.foundation.domains.number.LessThanNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysLessThan
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysLessThan.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;


            private final musaico.foundation.domains.array.Length domain;

            public AlwaysLessThan (
                                   int length
                                   )
            {
                this ( new LessThanNumber ( length ) );
            }

            public AlwaysLessThan (
                                   LessThanNumber domain
                                   )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public AlwaysLessThan (
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
                return "Return value is always of "
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysLessThan.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysLessThan.Violation violation =
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
         * The array or collection return value's length is always
         * greater than or equal to a specific number.
         * </p>
         *
         * @see musaico.foundation.domains.number.LessThanOrEqualToNumber
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysLessThanOrEqualTo
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysLessThanOrEqualTo.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;


            private final musaico.foundation.domains.array.Length domain;

            public AlwaysLessThanOrEqualTo (
                                            int length
                                            )
            {
                this ( new LessThanOrEqualToNumber ( length ) );
            }

            public AlwaysLessThanOrEqualTo (
                                            LessThanOrEqualToNumber domain
                                            )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public AlwaysLessThanOrEqualTo (
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
                return "Return value is always of "
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysLessThanOrEqualTo.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysLessThanOrEqualTo.Violation violation =
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
         * The array or collection return value's length is always
         * between some minimum length and some maximum length (inclusive).
         * </p>
         *
         * @see musaico.foundation.domains.comparability.BoundedDomain
         * @see musaico.foundation.contract.guarantees.Return
         */
        public static class AlwaysBetween
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysBetween.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;


            private final musaico.foundation.domains.array.Length domain;

            /** Closed domain: [ minimum_length, maximum_length ]. */
            public AlwaysBetween (
                                  int minimum_length,
                                  int maximum_length
                                  )
            {
                this ( BoundedDomain.EndPoint.CLOSED,
                       minimum_length,
                       BoundedDomain.EndPoint.CLOSED,
                       maximum_length );
            }

            public AlwaysBetween (
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

            public AlwaysBetween (
                                  BoundedDomain<Number> domain
                                  )
            {
                this (
                      new musaico.foundation.domains.array.Length ( domain ) );
            }

            public AlwaysBetween (
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
                return "Return value is always of "
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
                    Return.serialVersionUID;

                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object /* array or Collection */ evidence
                                  )
                {
                    super ( contract,
                            "Return value's length is out of range: "
                            + new ArrayObject<Object> (
                                      Object.class,
                                      evidence )
                                  .length ()
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysBetween.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysBetween.Violation violation =
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
         * The return value's array length always belongs to some arbitrary
         * domain of numbers.
         * Typically a method declares this when there is no existing
         * declarative guarantee to support its return value contract.
         * For example, if the return value's length always belongs to domain
         * Between5And7Elements, then the method might declare
         * "throws Return.Length.AlwaysInDomain.Violation",
         * check the return value against a new
         * Return.Length.AlwaysInDomain ( Between5And7Elements.CONTRACT )
         * at runtime, and explain the nature of the domain in the
         * method documentation.
         * The AlwaysInDomain contract should be a last resort, since
         * the "throws" declaration reveals nothing about
         * the domain to which the return value's length is bound.
         * For example, throwing Between5And7Elements.Violation instead of
         * Return.Length.AlwaysInDomain.Violation reveals more to
         * the caller about what the nature of the domain, and provides
         * an assertion filter for unit testing.
         * </p>
         *
         * @see musaico.foundation.domains.array.Length
         * @see musaico.foundation.contract.guarantees.Return.Length
         */
        public static class AlwaysInDomain
            implements Contract<Object /* array or Collection */, Return.Length.AlwaysInDomain.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            private final musaico.foundation.domains.array.Length domain;

            public AlwaysInDomain (
                                   Filter<Number> domain
                                   )
            {
                this.domain = new musaico.foundation.domains.array.Length ( domain );
            }

            /**
             * @see musaico.foundation.contract.Contract#description()
             */
            @Override
            public final String description ()
            {
                return "The return values are guaranteed to contain"
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
                    Return.serialVersionUID;
                public Violation (
                                  AlwaysInDomain contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return values did not contain a valid"
                            + " number of elements: "
                            + contract.domain.nonMember (
                                  new ArrayObject<Object> ( Object.class,
                                                            evidence ) )
                            + ".", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.Length.AlwaysInDomain.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence
                    )
            {
                return new Return.Length.AlwaysInDomain.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.Length.AlwaysInDomain.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection */ evidence,
                    Throwable cause
                    )
            {
                final Return.Length.AlwaysInDomain.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * guaranteed to contain no null elements.
     * </p>
     *
     * @see musaico.foundation.domains.array.NoNulls
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsNoNulls
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsNoNulls.Violation>,
                   Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsNoNulls CONTRACT =
            new Return.AlwaysContainsNoNulls ();

        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                              Contract<?, ?> contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained one or more"
                        + " null element(s): "
                        + NoNulls.DOMAIN.nonMember (
                                  new ArrayObject<Object> ( Object.class,
                                                            evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        /**
         * @see musaico.foundation.contract.Contract#description()
         */
        @Override
        public final String description ()
        {
            return "The return values are guaranteed to never contain"
                + " any null elements.";
        }

        /**
         * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
         */
        @Override
        public final FilterState filter (
                                         Object array_or_collection
                                         )
        {
            return NoNulls.DOMAIN.filter (
                    new ArrayObject<Object> ( Object.class,
                                              array_or_collection ) );
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () );
        }

        @Override
        public Return.AlwaysContainsNoNulls.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsNoNulls.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsNoNulls.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsNoNulls.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * guaranteed to contain only instances of a specific set of
     * classes.
     * </p>
     *
     * @see musaico.foundation.domains.array.ContainsOnlySpecificClasses
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysContainsOnlyClasses
        implements Contract<Object /* array or Collection */, Return.AlwaysContainsOnlyClasses.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ContainsOnlySpecificClasses domain;

        public AlwaysContainsOnlyClasses (
                                          Class<?> ... required_classes
                                          )
        {
            this ( new ContainsOnlySpecificClasses ( required_classes ) );
        }

        public AlwaysContainsOnlyClasses (
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
            // Strip out the leading "ContainsOnlySpecificClasses " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String classes =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to never contain"
                + " any elements of the class(es) "
                + classes
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysContainsOnlyClasses contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained one or more"
                        + " instance(s) of the verboten classes: "
                        + contract.domain.nonMember (
                                  new ArrayObject<Object> ( Object.class,
                                                            evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysContainsOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysContainsOnlyClasses.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysContainsOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysContainsOnlyClasses.Violation violation =
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
     * The return value is an array, Collection or other Iterable
     * guaranteed to contain no instances of a specific set of
     * classes.
     * </p>
     *
     * @see musaico.foundation.domains.array.ExcludesSpecificClasses
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class AlwaysExcludesClasses
        implements Contract<Object /* array or Collection */, Return.AlwaysExcludesClasses.Violation>, Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        private final ExcludesSpecificClasses domain;

        public AlwaysExcludesClasses (
                                      Class<?> ... excluded_classes
                                      )
        {
            this ( new ExcludesSpecificClasses ( excluded_classes ) );
        }

        public AlwaysExcludesClasses (
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
            // Strip out the leading "ExcludesSpecificClasses " section
            // from this.domain.toString ():
            final String domain_string = "" + this.domain;
            final String classes =
                domain_string.replaceAll ( "^"
                                           + ClassName.of ( this.domain.getClass () )
                                           + " ",
                                           "" );

            return "The return values are guaranteed to never contain"
                + " any elements of the class(es) "
                + classes
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
                Return.serialVersionUID;
            public Violation (
                              AlwaysExcludesClasses contract,
                              Object plaintiff,
                              Object evidence
                              )
            {
                super ( contract,
                        "The return values contained one or more"
                        + " instance(s) of the verboten classes: "
                        + contract.domain.nonMember (
                                  new ArrayObject<Object> ( Object.class,
                                                            evidence ) )
                        + ".", // description
                        plaintiff, evidence );
            }
        }

        @Override
        public Return.AlwaysExcludesClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence
                )
        {
            return new Return.AlwaysExcludesClasses.Violation (
                this,
                plaintiff,
                evidence );
        }

        @Override
        public Return.AlwaysExcludesClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence,
                Throwable cause
                )
        {
            final Return.AlwaysExcludesClasses.Violation violation =
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
     * The return value is guaranteed to belong to the same domain
     * as some other object, such as the "this" object asserting
     * the guarantee, or one of the parameters to the method.
     * </p>
     *
     * @see musaico.foundation.domains.array.CoDependentDomain
     * @see musaico.foundation.contract.guarantees.Return
     */
    public static class DependsOn
    {

        /** Return depends on the parent object, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class This
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.This.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and the object returning the value ("
                    + this.thisObject
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.thisObject,
                                                  array_or_collection
                                              } ) );
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as the object returning"
                            + " the value.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.This.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.This.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.This.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.This.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter1, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter1
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter1.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 1 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 1.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter1.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter1.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter1.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter1.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter2, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter2
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter2.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 2 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 2.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter2.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter2.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter2.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter2.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter3, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter3
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter3.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 3 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 3.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter3.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter3.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter3.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter3.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter4, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter4
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter4.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 4 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 4.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter4.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter4.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter4.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter4.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter5, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter5
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter5.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            private final Object parameter;

            private final CoDependentDomain domain;

            public Parameter5 (
                               Object parameter,
                               Filter<?> ... domains_to_choose_from
                               )
            {
                this ( parameter,
                       new CoDependentDomain ( domains_to_choose_from ) );
            }

            public Parameter5 (
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
                return "The return value and parameter 5 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 5.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter5.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter5.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter5.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter5.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter6, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter6
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter6.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 6 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 6.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter6.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter6.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter6.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter6.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter7, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter7
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter7.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 7 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 7.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter7.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter7.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter7.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter7.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter8, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter8
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter8.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 8 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 8.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter8.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter8.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter8.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter8.Violation violation =
                    this.violation ( plaintiff, evidence );
                if ( cause != null )
                {
                    violation.initCause ( cause );
                }

                return violation;
            }
        }




        /** Return depends on parameter9, both are always in any one
         *  of a set of Domains.
         *  @see musaico.foundation.contract.guarantees.Return */
        public static class Parameter9
            implements Contract<Object /* array, Collection, single object */, Return.DependsOn.Parameter9.Violation>, Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

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
                return "The return value and parameter 9 ("
                    + this.parameter
                    + ") are guaranteed to always belong to the same domain: "
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
                                              new Object [] {
                                                  this.parameter,
                                                  array_or_collection
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
                    Return.serialVersionUID;
                public Violation (
                                  Contract<?, ?> contract,
                                  Object plaintiff,
                                  Object evidence
                                  )
                {
                    super ( contract,
                            "The return value did not belong to the"
                            + " same domain as"
                            + " parameter 9.", // description
                            plaintiff, evidence );
                }
            }

            @Override
            public Return.DependsOn.Parameter9.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence
                    )
            {
                return new Return.DependsOn.Parameter9.Violation (
                    this,
                    plaintiff,
                    evidence );
            }

            @Override
            public Return.DependsOn.Parameter9.Violation violation (
                    Object plaintiff,
                    Object /* array, Collection or single Object */ evidence,
                    Throwable cause
                    )
            {
                final Return.DependsOn.Parameter9.Violation violation =
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
