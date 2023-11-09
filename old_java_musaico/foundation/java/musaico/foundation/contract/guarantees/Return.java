package musaico.foundation.contract.guarantees;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.contract.obligations.AbstractParameterN;

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
 * Contracts for parameter # 1 (the 1st parameter,
 * array index # 0) of a constructor or method.
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
public class Return
    extends AbstractParameterN
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton parameter # 1 constant. */
    public static final Return PARAMETER = new Return ();


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected Return ()
    {
    }


    /**
     * @see AbstractParameterN#PARAMETER_BITMAP()
     */
    @Override
    public final long PARAMETER_BITMAP ()
    {
        // Parameter # 1 (index 0):
        // 0000000000000001:
        return 0x0001L;
    }


    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeInDomain
     */
    public static class AlwaysInDomain<DOMAIN extends Object>
        extends AbstractParameterN.MustBeInDomain<DOMAIN, Return.AlwaysInDomain.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysInDomain (
                Filter<DOMAIN> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysInDomain.Violation violation (
                Object plaintiff,
                DOMAIN evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysInDomain.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysInDomain<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustNotBeNull
     */
    public static class NeverNull
        extends AbstractParameterN.MustNotBeNull<Return.NeverNull.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.NeverNull CONTRACT =
            new Return.NeverNull ();

        public NeverNull ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.NeverNull.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Return.NeverNull.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }

        /**
         * <p>
         * Violation of the NeverNull obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.NeverNull contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustChange
     */
    public static class AlwaysChanges<CHANGED extends Object>
        extends AbstractParameterN.MustChange<CHANGED, Return.AlwaysChanges.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysChanges<Object> CONTRACT =
            new Return.AlwaysChanges<Object> ();

        public AlwaysChanges ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysChanges.Violation violation (
                Object plaintiff,
                BeforeAndAfter<CHANGED> evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysChanges.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysChanges obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysChanges<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }





    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustNotChange
     */
    public static class NeverChanges<UNCHANGED extends Object>
        extends AbstractParameterN.MustNotChange<UNCHANGED, Return.NeverChanges.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.NeverChanges<Object> CONTRACT =
            new Return.NeverChanges<Object> ();

        public NeverChanges ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.NeverChanges.Violation violation (
                Object plaintiff,
                BeforeAndAfter<UNCHANGED> evidence_or_null,
                String description
                )
        {
            return new Return.NeverChanges.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the NeverChanges obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.NeverChanges<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustEqual
     */
    public static class AlwaysEquals<EQUAL extends Object>
        extends AbstractParameterN.MustEqual<EQUAL, Return.AlwaysEquals.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysEquals (
                EQUAL object
                )
        {
            super ( Return.PARAMETER, // parameter
                    object );             // object
        }

        public AlwaysEquals (
                EqualTo<EQUAL> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // object
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysEquals.Violation violation (
                Object plaintiff,
                EQUAL evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysEquals.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysEquals obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysEquals<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustNotEqual
     */
    public static class NeverEquals<UNEQUAL extends Object>
        extends AbstractParameterN.MustNotEqual<UNEQUAL, Return.NeverEquals.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public NeverEquals (
                UNEQUAL object
                )
        {
            super ( Return.PARAMETER, // parameter
                    object );             // object
        }

        public NeverEquals (
                NotEqualTo<UNEQUAL> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.NeverEquals.Violation violation (
                Object plaintiff,
                UNEQUAL evidence_or_null,
                String description
                )
        {
            return new Return.NeverEquals.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the NeverEquals obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.NeverEquals<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanNegativeOne
     */
    public static class AlwaysGreaterThanNegativeOne
        extends AbstractParameterN.MustBeGreaterThanNegativeOne<Return.AlwaysGreaterThanNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanNegativeOne CONTRACT =
            new Return.AlwaysGreaterThanNegativeOne ();

        public AlwaysGreaterThanNegativeOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanNegativeOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanOne
     */
    public static class AlwaysGreaterThanOne
        extends AbstractParameterN.MustBeGreaterThanOne<Return.AlwaysGreaterThanOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOne CONTRACT =
            new Return.AlwaysGreaterThanOne ();

        public AlwaysGreaterThanOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanZero
     */
    public static class AlwaysGreaterThanZero
        extends AbstractParameterN.MustBeGreaterThanZero<Return.AlwaysGreaterThanZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanZero CONTRACT =
            new Return.AlwaysGreaterThanZero ();

        public AlwaysGreaterThanZero ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanZero contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanOrEqualToNegativeOne
     */
    public static class AlwaysGreaterThanOrEqualToNegativeOne
        extends AbstractParameterN.MustBeGreaterThanOrEqualToNegativeOne<Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOrEqualToNegativeOne CONTRACT =
            new Return.AlwaysGreaterThanOrEqualToNegativeOne ();

        public AlwaysGreaterThanOrEqualToNegativeOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanOrEqualToNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanOrEqualToNegativeOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanOrEqualToOne
     */
    public static class AlwaysGreaterThanOrEqualToOne
        extends AbstractParameterN.MustBeGreaterThanOrEqualToOne<Return.AlwaysGreaterThanOrEqualToOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOrEqualToOne CONTRACT =
            new Return.AlwaysGreaterThanOrEqualToOne ();

        public AlwaysGreaterThanOrEqualToOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanOrEqualToOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanOrEqualToOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanOrEqualToOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanOrEqualToZero
     */
    public static class AlwaysGreaterThanOrEqualToZero
        extends AbstractParameterN.MustBeGreaterThanOrEqualToZero<Return.AlwaysGreaterThanOrEqualToZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysGreaterThanOrEqualToZero CONTRACT =
            new Return.AlwaysGreaterThanOrEqualToZero ();

        public AlwaysGreaterThanOrEqualToZero ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanOrEqualToZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanOrEqualToZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanOrEqualToZero contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanNegativeOne
     */
    public static class AlwaysLessThanNegativeOne
        extends AbstractParameterN.MustBeLessThanNegativeOne<Return.AlwaysLessThanNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanNegativeOne CONTRACT =
            new Return.AlwaysLessThanNegativeOne ();

        public AlwaysLessThanNegativeOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanNegativeOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanOne
     */
    public static class AlwaysLessThanOne
        extends AbstractParameterN.MustBeLessThanOne<Return.AlwaysLessThanOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOne CONTRACT =
            new Return.AlwaysLessThanOne ();

        public AlwaysLessThanOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanZero
     */
    public static class AlwaysLessThanZero
        extends AbstractParameterN.MustBeLessThanZero<Return.AlwaysLessThanZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanZero CONTRACT =
            new Return.AlwaysLessThanZero ();

        public AlwaysLessThanZero ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanZero contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanOrEqualToNegativeOne
     */
    public static class AlwaysLessThanOrEqualToNegativeOne
        extends AbstractParameterN.MustBeLessThanOrEqualToNegativeOne<Return.AlwaysLessThanOrEqualToNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOrEqualToNegativeOne CONTRACT =
            new Return.AlwaysLessThanOrEqualToNegativeOne ();

        public AlwaysLessThanOrEqualToNegativeOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanOrEqualToNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanOrEqualToNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanOrEqualToNegativeOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanOrEqualToOne
     */
    public static class AlwaysLessThanOrEqualToOne
        extends AbstractParameterN.MustBeLessThanOrEqualToOne<Return.AlwaysLessThanOrEqualToOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOrEqualToOne CONTRACT =
            new Return.AlwaysLessThanOrEqualToOne ();

        public AlwaysLessThanOrEqualToOne ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanOrEqualToOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanOrEqualToOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanOrEqualToOne contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanOrEqualToZero
     */
    public static class AlwaysLessThanOrEqualToZero
        extends AbstractParameterN.MustBeLessThanOrEqualToZero<Return.AlwaysLessThanOrEqualToZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysLessThanOrEqualToZero CONTRACT =
            new Return.AlwaysLessThanOrEqualToZero ();

        public AlwaysLessThanOrEqualToZero ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanOrEqualToZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanOrEqualToZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanOrEqualToZero contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThan
     */
    public static class AlwaysGreaterThan<NUMBER extends Number>
        extends AbstractParameterN.MustBeGreaterThan<NUMBER, Return.AlwaysGreaterThan.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysGreaterThan (
                NUMBER number
                )
        {
            super ( Return.PARAMETER,  // parameter
                    number );              // number
        }

        public AlwaysGreaterThan (
                GreaterThanNumber<NUMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThan.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThan.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThan obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThan<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeGreaterThanOrEqualTo
     */
    public static class AlwaysGreaterThanOrEqualTo<NUMBER extends Number>
        extends AbstractParameterN.MustBeGreaterThanOrEqualTo<NUMBER, Return.AlwaysGreaterThanOrEqualTo.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysGreaterThanOrEqualTo (
                NUMBER number
                )
        {
            super ( Return.PARAMETER, // parameter
                    number );             // number
        }

        public AlwaysGreaterThanOrEqualTo (
                GreaterThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysGreaterThanOrEqualTo.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysGreaterThanOrEqualTo obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysGreaterThanOrEqualTo<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThan
     */
    public static class AlwaysLessThan<NUMBER extends Number>
        extends AbstractParameterN.MustBeLessThan<NUMBER, Return.AlwaysLessThan.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysLessThan (
                NUMBER number
                )
        {
            super ( Return.PARAMETER, // parameter
                    number );             // number
        }

        public AlwaysLessThan (
                LessThanNumber<NUMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThan.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThan.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThan obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThan<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeLessThanOrEqualTo
     */
    public static class AlwaysLessThanOrEqualTo<NUMBER extends Number>
        extends AbstractParameterN.MustBeLessThanOrEqualTo<NUMBER, Return.AlwaysLessThanOrEqualTo.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysLessThanOrEqualTo (
                NUMBER number
                )
        {
            super ( Return.PARAMETER, // parameter
                    number );             // number
        }

        public AlwaysLessThanOrEqualTo (
                LessThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysLessThanOrEqualTo.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysLessThanOrEqualTo obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysLessThanOrEqualTo<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeBetween
     */
    public static class AlwaysBetween<NUMBER extends Number>
        extends AbstractParameterN.MustBeBetween<NUMBER, Return.AlwaysBetween.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        /** Closed bounds: [ minimum_number, maximum_number ]. */
        public AlwaysBetween (
                NUMBER minimum_number,
                NUMBER maximum_number
                )
        {
            super ( Return.PARAMETER, // parameter
                    minimum_number,       // minimum_number
                    maximum_number );     // maximum_number
        }

        public AlwaysBetween (
                BoundedFilter.EndPoint minimum_end_point,
                NUMBER minimum_number,
                BoundedFilter.EndPoint maximum_end_point,
                NUMBER maximum_number
                )
        {
            super ( Return.PARAMETER, // parameter
                    minimum_end_point,    // minimum_end_point
                    minimum_number,       // minimum_number
                    maximum_end_point,    // maximum_end_point
                    maximum_number );     // maximum_number
        }

        public AlwaysBetween (
                BoundedFilter<NUMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysBetween.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysBetween.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysBetween obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysBetween<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeInBounds
     */
    public static class AlwaysInBounds<BOUNDED extends Object>
        extends AbstractParameterN.MustBeInBounds<BOUNDED, Return.AlwaysInBounds.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        /** Closed bounds: [ min, max ]. */
        public AlwaysInBounds (
                Comparator<BOUNDED> comparator,
                BOUNDED min,
                BOUNDED max
                )
        {
            super ( Return.PARAMETER,  // parameter
                    comparator,            // comparator
                    min,                   // min
                    max );                 // max
        }

        public AlwaysInBounds (
                Comparator<BOUNDED> comparator,
                BoundedFilter.EndPoint min_end_point,
                BOUNDED min,
                BoundedFilter.EndPoint max_end_point,
                BOUNDED max
                )
        {
            super ( Return.PARAMETER,      // parameter
                    comparator,                // comparator
                    min_end_point,             // min_end_point
                    min,                       // min
                    max_end_point,             // max_end_point
                    max );                     // max
        }

        public AlwaysInBounds (
                BoundedFilter<BOUNDED> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysInBounds.Violation violation (
                Object plaintiff,
                BOUNDED evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysInBounds.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysInBounds obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysInBounds<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeInstanceOf
     */
    public static class AlwaysInstanceOf<INSTANCE extends Object>
        extends AbstractParameterN.MustBeInstanceOf<INSTANCE, Return.AlwaysInstanceOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysInstanceOf (
                Class<?> domain_class
                )
        {
            super ( Return.PARAMETER,      // parameter
                    domain_class );            // domain_class
        }

        public AlwaysInstanceOf (
                InstanceOf<INSTANCE> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysInstanceOf.Violation violation (
                Object plaintiff,
                INSTANCE evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysInstanceOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysInstanceOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysInstanceOf<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustNotBeInstanceOf
     */
    public static class NeverInstanceOf<INSTANCE extends Object>
        extends AbstractParameterN.MustNotBeInstanceOf<INSTANCE, Return.NeverInstanceOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public NeverInstanceOf (
                Class<?> verboten_class
                )
        {
            super ( Return.PARAMETER,        // parameter
                    verboten_class );            // verboten_class
        }

        public NeverInstanceOf (
                InstanceOf<INSTANCE> verboten_domain
                )
        {
            super ( Return.PARAMETER,          // parameter
                    verboten_domain );             // verboten_domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.NeverInstanceOf.Violation violation (
                Object plaintiff,
                INSTANCE evidence_or_null,
                String description
                )
        {
            return new Return.NeverInstanceOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the NeverInstanceOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.NeverInstanceOf<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeEmptyString
     */
    public static class AlwaysEmptyString
        extends AbstractParameterN.MustBeEmptyString<Return.AlwaysEmptyString.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysEmptyString CONTRACT =
            new Return.AlwaysEmptyString ();

        public AlwaysEmptyString ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysEmptyString.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysEmptyString.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysEmptyString obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysEmptyString contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustNotBeEmptyString
     */
    public static class NeverEmptyString
        extends AbstractParameterN.MustNotBeEmptyString<Return.NeverEmptyString.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.NeverEmptyString CONTRACT =
            new Return.NeverEmptyString ();

        public NeverEmptyString ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.NeverEmptyString.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.NeverEmptyString.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the NeverEmptyString obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.NeverEmptyString contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeStringLength
     */
    public static class AlwaysStringLength
        extends AbstractParameterN.MustBeStringLength<Return.AlwaysStringLength.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;


        public AlwaysStringLength (
                int exact_length
                )
        {
            super ( Return.PARAMETER,      // parameter
                    exact_length );            // exact_length
        }

        public AlwaysStringLength (
                int minimum_length,
                int maximum_length
                )
        {
            super ( Return.PARAMETER,         // parameter
                    minimum_length,               // minimum_length
                    maximum_length );             // maximum_length
        }

        public AlwaysStringLength (
                StringLength domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysStringLength.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysStringLength.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysStringLength obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysStringLength contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustExcludeSpaces
     */
    public static class AlwaysExcludesSpaces
        extends AbstractParameterN.MustExcludeSpaces<Return.AlwaysExcludesSpaces.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysExcludesSpaces CONTRACT =
            new Return.AlwaysExcludesSpaces ();

        public AlwaysExcludesSpaces ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysExcludesSpaces.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysExcludesSpaces.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysExcludesSpaces obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysExcludesSpaces contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainNonSpaces
     */
    public static class AlwaysContainsNonSpaces
        extends AbstractParameterN.MustContainNonSpaces<Return.AlwaysContainsNonSpaces.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsNonSpaces CONTRACT =
            new Return.AlwaysContainsNonSpaces ();

        public AlwaysContainsNonSpaces ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsNonSpaces.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsNonSpaces.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsNonSpaces obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsNonSpaces contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyNumerics
     */
    public static class AlwaysContainsOnlyNumerics
        extends AbstractParameterN.MustContainOnlyNumerics<Return.AlwaysContainsOnlyNumerics.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyNumerics CONTRACT =
            new Return.AlwaysContainsOnlyNumerics ();

        public AlwaysContainsOnlyNumerics ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyNumerics.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyNumerics.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyNumerics obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyNumerics contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyAlpha
     */
    public static class AlwaysContainsOnlyAlpha
        extends AbstractParameterN.MustContainOnlyAlpha<Return.AlwaysContainsOnlyAlpha.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyAlpha CONTRACT =
            new Return.AlwaysContainsOnlyAlpha ();

        public AlwaysContainsOnlyAlpha ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyAlpha.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyAlpha.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyAlpha obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyAlpha contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyAlphaNumerics
     */
    public static class AlwaysContainsOnlyAlphaNumerics
        extends AbstractParameterN.MustContainOnlyAlphaNumerics<Return.AlwaysContainsOnlyAlphaNumerics.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyAlphaNumerics CONTRACT =
            new Return.AlwaysContainsOnlyAlphaNumerics ();

        public AlwaysContainsOnlyAlphaNumerics ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyAlphaNumerics.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyAlphaNumerics.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyAlphaNumerics obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyAlphaNumerics contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyPrintableCharacters
     */
    public static class AlwaysContainsOnlyPrintableCharacters
        extends AbstractParameterN.MustContainOnlyPrintableCharacters<Return.AlwaysContainsOnlyPrintableCharacters.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsOnlyPrintableCharacters CONTRACT =
            new Return.AlwaysContainsOnlyPrintableCharacters ();

        public AlwaysContainsOnlyPrintableCharacters ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyPrintableCharacters.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyPrintableCharacters.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyPrintableCharacters obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyPrintableCharacters contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeStringID
     */
    public static class AlwaysStringID
        extends AbstractParameterN.MustBeStringID<Return.AlwaysStringID.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysStringID CONTRACT =
            new Return.AlwaysStringID ();

        public AlwaysStringID ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysStringID.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysStringID.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysStringID obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysStringID contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustMatchPattern
     */
    public static class AlwaysMatchesPattern
        extends AbstractParameterN.MustMatchPattern<Return.AlwaysMatchesPattern.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;


        public AlwaysMatchesPattern (
                Pattern pattern
                )
        {
            super ( Return.PARAMETER, // parameter
                    pattern );            // pattern
        }

        public AlwaysMatchesPattern (
                StringPattern domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysMatchesPattern.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysMatchesPattern.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysMatchesPattern obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysMatchesPattern contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeMemberOf
     */
    public static class AlwaysMemberOf<MEMBER extends Object>
        extends AbstractParameterN.MustBeMemberOf<MEMBER, Return.AlwaysMemberOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public AlwaysMemberOf (
                MEMBER ... array
                )
        {
            super ( Return.PARAMETER, // parameter
                    array );              // array
        }

        public AlwaysMemberOf (
                Collection<MEMBER> collection
                )
        {
            super ( Return.PARAMETER,    // parameter
                    collection );            // collection
        }

        public AlwaysMemberOf (
                Iterable<MEMBER> iterable
                )
        {
            super ( Return.PARAMETER,  // parameter
                    iterable );            // iterable
        }

        public AlwaysMemberOf (
                MemberOf<MEMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysMemberOf.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysMemberOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysMemberOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysMemberOf<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustNotBeMemberOf
     */
    public static class NeverMemberOf<MEMBER extends Object>
        extends AbstractParameterN.MustNotBeMemberOf<MEMBER, Return.NeverMemberOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public NeverMemberOf (
                MEMBER ... array
                )
        {
            super ( Return.PARAMETER, // parameter
                    array );              // array
        }

        public NeverMemberOf (
                Collection<MEMBER> collection
                )
        {
            super ( Return.PARAMETER,     // parameter
                    collection );             // collection
        }

        public NeverMemberOf (
                Iterable<MEMBER> iterable
                )
        {
            super ( Return.PARAMETER,  // parameter
                    iterable );            // iterable
        }

        public NeverMemberOf (
                MemberOf<MEMBER> membership
                )
        {
            super ( Return.PARAMETER, // parameter
                    membership );         // membership
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.NeverMemberOf.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Return.NeverMemberOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the NeverMemberOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.NeverMemberOf<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }


    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainMembers
     */
    public static class AlwaysContainsMembers<MEMBER extends Object>
        extends AbstractParameterN.MustContainMembers<MEMBER, Return.AlwaysContainsMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        @SuppressWarnings("unchecked") // Generic varargs.
        public AlwaysContainsMembers (
                MEMBER ... members
                )
        {
            super ( Return.PARAMETER, // parameter
                    members );            // members
        }

        public AlwaysContainsMembers (
                IncludesMembers<MEMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsMembers.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsMembers.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsMembers obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsMembers<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }


    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyMembers
     */
    public static class AlwaysContainsOnlyMembers<MEMBER extends Object>
        extends AbstractParameterN.MustContainOnlyMembers<MEMBER, Return.AlwaysContainsOnlyMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        @SuppressWarnings("unchecked") // Generic varargs.
        public AlwaysContainsOnlyMembers (
                MEMBER ... members
                )
        {
            super ( Return.PARAMETER, // parameter
                    members );            // members
        }

        public AlwaysContainsOnlyMembers (
                IncludesOnlyMembers<MEMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyMembers.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyMembers.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyMembers obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyMembers<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustExcludeMembers
     */
    public static class AlwaysExcludesMembers<MEMBER extends Object>
        extends AbstractParameterN.MustExcludeMembers<MEMBER, Return.AlwaysExcludesMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        @SuppressWarnings("unchecked") // Possible heap pollution
        //                                generic varargs: MEMBER ...
        public AlwaysExcludesMembers (
                MEMBER ... members
                )
        {
            super ( Return.PARAMETER, // parameter
                    members );            // members
        }

        public AlwaysExcludesMembers (
                Collection<MEMBER> members
                )
        {
            super ( Return.PARAMETER, // parameter
                    members );            // members
        }

        public AlwaysExcludesMembers (
                Iterable<MEMBER> members
                )
        {
            super ( Return.PARAMETER, // parameter
                    members );            // members
        }

        public AlwaysExcludesMembers (
                ExcludesMembers<MEMBER> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysExcludesMembers.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysExcludesMembers.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysExcludesMembers obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysExcludesMembers<?> contract,
                    Object plaintiff,
                    Object evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainIndices
     */
    public static class AlwaysContainsIndices<ELEMENT extends Object>
        extends AbstractParameterN.MustContainIndices<ELEMENT, Return.AlwaysContainsIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysContainsIndices (
                long ... indices
                )
        {
            super ( Return.PARAMETER, // parameter
                    indices );            // indices
        }

        public AlwaysContainsIndices (
                int ... indices
                )
        {
            super ( Return.PARAMETER, // parameter
                    indices );            // indices
        }

        public AlwaysContainsIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( Return.PARAMETER,         // parameter
                    indices_filter );             // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsIndices.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsIndices obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsIndices<?> contract,
                    Object plaintiff,
                    Object /* array or Collection */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyIndices
     */
    public static class AlwaysContainsOnlyIndices<ELEMENT extends Object>
        extends AbstractParameterN.MustContainOnlyIndices<ELEMENT, Return.AlwaysContainsOnlyIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysContainsOnlyIndices (
                long ... indices
                )
        {
            super ( Return.PARAMETER, // parameter
                    indices );            // indices
        }

        public AlwaysContainsOnlyIndices (
                int ... indices
                )
        {
            super ( Return.PARAMETER, // parameter
                    indices );            // indices
        }

        public AlwaysContainsOnlyIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( Return.PARAMETER,         // parameter
                    indices_filter );             // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyIndices.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyIndices obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyIndices<?> contract,
                    Object plaintiff,
                    Object /* array or Collection */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustExcludeIndices
     */
    public static class AlwaysExcludesIndices<ELEMENT extends Object>
        extends AbstractParameterN.MustExcludeIndices<ELEMENT, Return.AlwaysExcludesIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysExcludesIndices (
                long ... indices
                )
        {
            super ( Return.PARAMETER, // parameter
                    indices );            // indices
        }

        public AlwaysExcludesIndices (
                int ... indices
                )
        {
            super ( Return.PARAMETER, // parameter
                    indices );            // indices
        }

        public AlwaysExcludesIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( Return.PARAMETER,         // parameter
                    indices_filter );             // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysExcludesIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysExcludesIndices.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysExcludesIndices obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysExcludesIndices<?> contract,
                    Object plaintiff,
                    Object /* array or Collection */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainNoDuplicates
     */
    public static class AlwaysContainsNoDuplicates<ELEMENT extends Object>
        extends AbstractParameterN.MustContainNoDuplicates<ELEMENT, Return.AlwaysContainsNoDuplicates.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsNoDuplicates<Object> CONTRACT =
            new Return.AlwaysContainsNoDuplicates<Object> ();

        public AlwaysContainsNoDuplicates ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        public AlwaysContainsNoDuplicates (
                NoDuplicates<ELEMENT> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsNoDuplicates.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsNoDuplicates.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsNoDuplicates obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsNoDuplicates<?> contract,
                    Object plaintiff,
                    Object /* array or Collection */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
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
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeGreaterThanOne
         */
        public static class AlwaysGreaterThanOne
            extends AbstractParameterN.Length.MustBeGreaterThanOne<Return.Length.AlwaysGreaterThanOne.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysGreaterThanOne CONTRACT =
                new Return.Length.AlwaysGreaterThanOne ();

            public AlwaysGreaterThanOne ()
            {
                super ( Return.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysGreaterThanOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysGreaterThanOne.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysGreaterThanOne obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysGreaterThanOne contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeGreaterThanZero
         */
        public static class AlwaysGreaterThanZero
            extends AbstractParameterN.Length.MustBeGreaterThanZero<Return.Length.AlwaysGreaterThanZero.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysGreaterThanZero CONTRACT =
                new Return.Length.AlwaysGreaterThanZero ();

            public AlwaysGreaterThanZero ()
            {
                super ( Return.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysGreaterThanZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysGreaterThanZero.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysGreaterThanZero obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysGreaterThanZero contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustEqual
         */
        public static class AlwaysEquals<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustEqual<ELEMENT, Return.Length.AlwaysEquals.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public AlwaysEquals (
                    int length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysEquals (
                    long length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysEquals (
                    EqualToNumber<Long> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            public AlwaysEquals (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysEquals.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysEquals.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysEquals obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysEquals<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustNotEqual
         */
        public static class NeverEquals<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustNotEqual<ELEMENT, Return.Length.NeverEquals.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public NeverEquals (
                    int length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public NeverEquals (
                    long length
                    )
            {
                super ( Return.PARAMETER,  // parameter
                        length );              // length
            }

            public NeverEquals (
                    NotEqualToNumber<Long> domain
                    )
            {
                super ( Return.PARAMETER,  // parameter
                        domain );              // domain
            }

            public NeverEquals (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.NeverEquals.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.NeverEquals.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.NeverEquals obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.NeverEquals<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustEqualZero
         */
        public static class AlwaysEqualsZero<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustEqualZero<ELEMENT, Return.Length.AlwaysEqualsZero.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysEqualsZero<Object> CONTRACT =
                new Return.Length.AlwaysEqualsZero<Object> ();

            public AlwaysEqualsZero ()
            {
                super ( Return.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysEqualsZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysEqualsZero.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysEqualsZero obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysEqualsZero<?> contract,
                        Object plaintiff,
                        Object evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustEqualOne
         */
        public static class AlwaysEqualsOne<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustEqualOne<ELEMENT, Return.Length.AlwaysEqualsOne.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public static final Return.Length.AlwaysEqualsOne<Object> CONTRACT =
                new Return.Length.AlwaysEqualsOne<Object> ();

            public AlwaysEqualsOne ()
            {
                super ( Return.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysEqualsOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysEqualsOne.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysEqualsOne obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysEqualsOne<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeGreaterThan
         */
        public static class AlwaysGreaterThan<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeGreaterThan<ELEMENT, Return.Length.AlwaysGreaterThan.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public AlwaysGreaterThan (
                    int length
                    )
            {
                super ( Return.PARAMETER,  // parameter
                        length );              // length
            }

            public AlwaysGreaterThan (
                    long length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysGreaterThan (
                    GreaterThanNumber<Long> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            public AlwaysGreaterThan (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysGreaterThan.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysGreaterThan obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysGreaterThan<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeGreaterThanOrEqualTo
         */
        public static class AlwaysGreaterThanOrEqualTo<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeGreaterThanOrEqualTo<ELEMENT, Return.Length.AlwaysGreaterThanOrEqualTo.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public AlwaysGreaterThanOrEqualTo (
                    int length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysGreaterThanOrEqualTo (
                    long length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysGreaterThanOrEqualTo (
                    GreaterThanOrEqualToNumber<Long> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            public AlwaysGreaterThanOrEqualTo (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysGreaterThanOrEqualTo.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysGreaterThanOrEqualTo obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysGreaterThanOrEqualTo<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeLessThan
         */
        public static class AlwaysLessThan<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeLessThan<ELEMENT, Return.Length.AlwaysLessThan.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public AlwaysLessThan (
                    int length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysLessThan (
                    long length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysLessThan (
                    LessThanNumber<Long> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            public AlwaysLessThan (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysLessThan.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysLessThan obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysLessThan<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeLessThanOrEqualTo
         */
        public static class AlwaysLessThanOrEqualTo<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeLessThanOrEqualTo<ELEMENT, Return.Length.AlwaysLessThanOrEqualTo.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public AlwaysLessThanOrEqualTo (
                    int length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysLessThanOrEqualTo (
                    long length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        length );             // length
            }

            public AlwaysLessThanOrEqualTo (
                    LessThanOrEqualToNumber<Long> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            public AlwaysLessThanOrEqualTo (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysLessThanOrEqualTo.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysLessThanOrEqualTo obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysLessThanOrEqualTo<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeBetween
         */
        public static class AlwaysBetween<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeBetween<ELEMENT, Return.Length.AlwaysBetween.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public AlwaysBetween (
                    int minimum_length,
                    int maximum_length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        minimum_length,       // minimum_length
                        maximum_length );     // maximum_length
            }

            public AlwaysBetween (
                    BoundedFilter.EndPoint minimum_end_point,
                    int minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    int maximum_length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        minimum_end_point,    // minimum_end_point
                        minimum_length,       // minimum_length
                        maximum_end_point,    // maximum_end_point
                        maximum_length );     // maximum_length
            }

            
            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public AlwaysBetween (
                    long minimum_length,
                    long maximum_length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        minimum_length,       // minimum_length
                        maximum_length );     // maximum_length
            }

            public AlwaysBetween (
                    BoundedFilter.EndPoint minimum_end_point,
                    long minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    long maximum_length
                    )
            {
                super ( Return.PARAMETER, // parameter
                        minimum_end_point,    // minimum_end_point
                        minimum_length,       // minimum_length
                        maximum_end_point,    // maximum_end_point
                        maximum_length );     // maximum_length
            }

            public AlwaysBetween (
                    BoundedFilter<Long> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            public AlwaysBetween (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysBetween.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysBetween obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysBetween<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }




        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.Length.MustBeInDomain
         */
        public static class AlwaysInDomain<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeInDomain<ELEMENT, Return.Length.AlwaysInDomain.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;

            public AlwaysInDomain (
                    Filter<Long> length_number_filter
                    )
            {
                super ( Return.PARAMETER,              // parameter
                        length_number_filter );            // length_number_filter
            }

            public AlwaysInDomain (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Return.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Return.Length.AlwaysInDomain.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Return.Length.AlwaysInDomain.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.AlwaysInDomain obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Return.serialVersionUID;
                public Violation (
                        Return.Length.AlwaysInDomain<?> contract,
                        Object plaintiff,
                        Object /* array or Collection etc */ evidence_or_null,
                        String description
                        )
                {
                    super ( contract,           // contract
                            description,        // description
                            plaintiff,          // plaintiff
                            evidence_or_null ); // evidence
                }
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainNoNulls
     */
    public static class AlwaysContainsNoNulls<ELEMENT extends Object>
        extends AbstractParameterN.MustContainNoNulls<ELEMENT, Return.AlwaysContainsNoNulls.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public static final Return.AlwaysContainsNoNulls<Object> CONTRACT =
            new Return.AlwaysContainsNoNulls<Object> ();

        public AlwaysContainsNoNulls ()
        {
            super ( Return.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsNoNulls.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsNoNulls.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsNoNulls obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsNoNulls<?> contract,
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustContainOnlyClasses
     */
    public static class AlwaysContainsOnlyClasses<ELEMENT extends Object>
        extends AbstractParameterN.MustContainOnlyClasses<ELEMENT, Return.AlwaysContainsOnlyClasses.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysContainsOnlyClasses (
                Class<?> ... required_classes
                )
        {
            super ( Return.PARAMETER,          // parameter
                    required_classes );            // required_classes
        }

        public AlwaysContainsOnlyClasses (
                IncludesOnlyClasses<ELEMENT> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysContainsOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysContainsOnlyClasses.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysContainsOnlyClassess obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysContainsOnlyClasses<?> contract,
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }




    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustExcludeClasses
     */
    public static class AlwaysExcludesClasses<ELEMENT extends Object>
        extends AbstractParameterN.MustExcludeClasses<ELEMENT, Return.AlwaysExcludesClasses.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Return.serialVersionUID;

        public AlwaysExcludesClasses (
                Class<?> ... excluded_classes
                )
        {
            super ( Return.PARAMETER,          // parameter
                    excluded_classes );            // excluded_classes
        }

        public AlwaysExcludesClasses (
                ExcludesClasses<ELEMENT> domain
                )
        {
            super ( Return.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Return.AlwaysExcludesClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ evidence_or_null,
                String description
                )
        {
            return new Return.AlwaysExcludesClasses.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the AlwaysExcludesClasses obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Return.serialVersionUID;
            public Violation (
                    Return.AlwaysExcludesClasses<?> contract,
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                super ( contract,           // contract
                        description,        // description
                        plaintiff,          // plaintiff
                        evidence_or_null ); // evidence
            }
        }
    }
}
