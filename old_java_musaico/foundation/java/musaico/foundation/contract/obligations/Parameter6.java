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
 * Contracts for parameter # 6 (the 6th parameter,
 * array index # 5) of a constructor or method.
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
public class Parameter6
    extends AbstractParameterN
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton parameter # 6 constant. */
    public static final Parameter6 PARAMETER = new Parameter6 ();


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected Parameter6 ()
    {
    }


    /**
     * @see AbstractParameterN#PARAMETER_BITMAP()
     */
    @Override
    public final long PARAMETER_BITMAP ()
    {
        // Parameter # 6 (index 5):
        // 0000000000100000:
        return 0x0020L;
    }


    /**
     * @see musaico.foundation.contract.obligations.AbstractParameterN.MustBeInDomain
     */
    public static class MustBeInDomain<DOMAIN extends Object>
        extends AbstractParameterN.MustBeInDomain<DOMAIN, Parameter6.MustBeInDomain.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustBeInDomain (
                Filter<DOMAIN> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeInDomain.Violation violation (
                Object plaintiff,
                DOMAIN evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeInDomain.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeInDomain<?> contract,
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
    public static class MustNotBeNull
        extends AbstractParameterN.MustNotBeNull<Parameter6.MustNotBeNull.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustNotBeNull CONTRACT =
            new Parameter6.MustNotBeNull ();

        public MustNotBeNull ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustNotBeNull.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustNotBeNull.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }

        /**
         * <p>
         * Violation of the MustNotBeNull obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustNotBeNull contract,
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
    public static class MustChange<CHANGED extends Object>
        extends AbstractParameterN.MustChange<CHANGED, Parameter6.MustChange.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustChange<Object> CONTRACT =
            new Parameter6.MustChange<Object> ();

        public MustChange ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustChange.Violation violation (
                Object plaintiff,
                BeforeAndAfter<CHANGED> evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustChange.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustChange obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustChange<?> contract,
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
    public static class MustNotChange<UNCHANGED extends Object>
        extends AbstractParameterN.MustNotChange<UNCHANGED, Parameter6.MustNotChange.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustNotChange<Object> CONTRACT =
            new Parameter6.MustNotChange<Object> ();

        public MustNotChange ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustNotChange.Violation violation (
                Object plaintiff,
                BeforeAndAfter<UNCHANGED> evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustNotChange.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustNotChange obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustNotChange<?> contract,
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
    public static class MustEqual<EQUAL extends Object>
        extends AbstractParameterN.MustEqual<EQUAL, Parameter6.MustEqual.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustEqual (
                EQUAL object
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    object );             // object
        }

        public MustEqual (
                EqualTo<EQUAL> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // object
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustEqual.Violation violation (
                Object plaintiff,
                EQUAL evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustEqual.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustEqual obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustEqual<?> contract,
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
    public static class MustNotEqual<UNEQUAL extends Object>
        extends AbstractParameterN.MustNotEqual<UNEQUAL, Parameter6.MustNotEqual.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustNotEqual (
                UNEQUAL object
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    object );             // object
        }

        public MustNotEqual (
                NotEqualTo<UNEQUAL> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustNotEqual.Violation violation (
                Object plaintiff,
                UNEQUAL evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustNotEqual.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustNotEqual obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustNotEqual<?> contract,
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
    public static class MustBeGreaterThanNegativeOne
        extends AbstractParameterN.MustBeGreaterThanNegativeOne<Parameter6.MustBeGreaterThanNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeGreaterThanNegativeOne CONTRACT =
            new Parameter6.MustBeGreaterThanNegativeOne ();

        public MustBeGreaterThanNegativeOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanNegativeOne contract,
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
    public static class MustBeGreaterThanOne
        extends AbstractParameterN.MustBeGreaterThanOne<Parameter6.MustBeGreaterThanOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeGreaterThanOne CONTRACT =
            new Parameter6.MustBeGreaterThanOne ();

        public MustBeGreaterThanOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanOne contract,
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
    public static class MustBeGreaterThanZero
        extends AbstractParameterN.MustBeGreaterThanZero<Parameter6.MustBeGreaterThanZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeGreaterThanZero CONTRACT =
            new Parameter6.MustBeGreaterThanZero ();

        public MustBeGreaterThanZero ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanZero contract,
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
    public static class MustBeGreaterThanOrEqualToNegativeOne
        extends AbstractParameterN.MustBeGreaterThanOrEqualToNegativeOne<Parameter6.MustBeGreaterThanOrEqualToNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeGreaterThanOrEqualToNegativeOne CONTRACT =
            new Parameter6.MustBeGreaterThanOrEqualToNegativeOne ();

        public MustBeGreaterThanOrEqualToNegativeOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanOrEqualToNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanOrEqualToNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanOrEqualToNegativeOne contract,
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
    public static class MustBeGreaterThanOrEqualToOne
        extends AbstractParameterN.MustBeGreaterThanOrEqualToOne<Parameter6.MustBeGreaterThanOrEqualToOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeGreaterThanOrEqualToOne CONTRACT =
            new Parameter6.MustBeGreaterThanOrEqualToOne ();

        public MustBeGreaterThanOrEqualToOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanOrEqualToOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanOrEqualToOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanOrEqualToOne contract,
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
    public static class MustBeGreaterThanOrEqualToZero
        extends AbstractParameterN.MustBeGreaterThanOrEqualToZero<Parameter6.MustBeGreaterThanOrEqualToZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeGreaterThanOrEqualToZero CONTRACT =
            new Parameter6.MustBeGreaterThanOrEqualToZero ();

        public MustBeGreaterThanOrEqualToZero ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanOrEqualToZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanOrEqualToZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanOrEqualToZero contract,
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
    public static class MustBeLessThanNegativeOne
        extends AbstractParameterN.MustBeLessThanNegativeOne<Parameter6.MustBeLessThanNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeLessThanNegativeOne CONTRACT =
            new Parameter6.MustBeLessThanNegativeOne ();

        public MustBeLessThanNegativeOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanNegativeOne contract,
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
    public static class MustBeLessThanOne
        extends AbstractParameterN.MustBeLessThanOne<Parameter6.MustBeLessThanOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeLessThanOne CONTRACT =
            new Parameter6.MustBeLessThanOne ();

        public MustBeLessThanOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanOne contract,
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
    public static class MustBeLessThanZero
        extends AbstractParameterN.MustBeLessThanZero<Parameter6.MustBeLessThanZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeLessThanZero CONTRACT =
            new Parameter6.MustBeLessThanZero ();

        public MustBeLessThanZero ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanZero contract,
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
    public static class MustBeLessThanOrEqualToNegativeOne
        extends AbstractParameterN.MustBeLessThanOrEqualToNegativeOne<Parameter6.MustBeLessThanOrEqualToNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeLessThanOrEqualToNegativeOne CONTRACT =
            new Parameter6.MustBeLessThanOrEqualToNegativeOne ();

        public MustBeLessThanOrEqualToNegativeOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanOrEqualToNegativeOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanOrEqualToNegativeOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanOrEqualToNegativeOne contract,
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
    public static class MustBeLessThanOrEqualToOne
        extends AbstractParameterN.MustBeLessThanOrEqualToOne<Parameter6.MustBeLessThanOrEqualToOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeLessThanOrEqualToOne CONTRACT =
            new Parameter6.MustBeLessThanOrEqualToOne ();

        public MustBeLessThanOrEqualToOne ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanOrEqualToOne.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanOrEqualToOne obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanOrEqualToOne contract,
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
    public static class MustBeLessThanOrEqualToZero
        extends AbstractParameterN.MustBeLessThanOrEqualToZero<Parameter6.MustBeLessThanOrEqualToZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeLessThanOrEqualToZero CONTRACT =
            new Parameter6.MustBeLessThanOrEqualToZero ();

        public MustBeLessThanOrEqualToZero ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanOrEqualToZero.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanOrEqualToZero obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanOrEqualToZero contract,
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
    public static class MustBeGreaterThan<NUMBER extends Number>
        extends AbstractParameterN.MustBeGreaterThan<NUMBER, Parameter6.MustBeGreaterThan.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustBeGreaterThan (
                NUMBER number
                )
        {
            super ( Parameter6.PARAMETER,  // parameter
                    number );              // number
        }

        public MustBeGreaterThan (
                GreaterThanNumber<NUMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThan.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThan.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThan obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThan<?> contract,
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
    public static class MustBeGreaterThanOrEqualTo<NUMBER extends Number>
        extends AbstractParameterN.MustBeGreaterThanOrEqualTo<NUMBER, Parameter6.MustBeGreaterThanOrEqualTo.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustBeGreaterThanOrEqualTo (
                NUMBER number
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    number );             // number
        }

        public MustBeGreaterThanOrEqualTo (
                GreaterThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeGreaterThanOrEqualTo.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeGreaterThanOrEqualTo obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeGreaterThanOrEqualTo<?> contract,
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
    public static class MustBeLessThan<NUMBER extends Number>
        extends AbstractParameterN.MustBeLessThan<NUMBER, Parameter6.MustBeLessThan.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustBeLessThan (
                NUMBER number
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    number );             // number
        }

        public MustBeLessThan (
                LessThanNumber<NUMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThan.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThan.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThan obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThan<?> contract,
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
    public static class MustBeLessThanOrEqualTo<NUMBER extends Number>
        extends AbstractParameterN.MustBeLessThanOrEqualTo<NUMBER, Parameter6.MustBeLessThanOrEqualTo.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustBeLessThanOrEqualTo (
                NUMBER number
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    number );             // number
        }

        public MustBeLessThanOrEqualTo (
                LessThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeLessThanOrEqualTo.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeLessThanOrEqualTo obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeLessThanOrEqualTo<?> contract,
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
    public static class MustBeBetween<NUMBER extends Number>
        extends AbstractParameterN.MustBeBetween<NUMBER, Parameter6.MustBeBetween.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        /** Closed bounds: [ minimum_number, maximum_number ]. */
        public MustBeBetween (
                NUMBER minimum_number,
                NUMBER maximum_number
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    minimum_number,       // minimum_number
                    maximum_number );     // maximum_number
        }

        public MustBeBetween (
                BoundedFilter.EndPoint minimum_end_point,
                NUMBER minimum_number,
                BoundedFilter.EndPoint maximum_end_point,
                NUMBER maximum_number
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    minimum_end_point,    // minimum_end_point
                    minimum_number,       // minimum_number
                    maximum_end_point,    // maximum_end_point
                    maximum_number );     // maximum_number
        }

        public MustBeBetween (
                BoundedFilter<NUMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeBetween.Violation violation (
                Object plaintiff,
                NUMBER evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeBetween.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeBetween obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeBetween<?> contract,
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
    public static class MustBeInBounds<BOUNDED extends Object>
        extends AbstractParameterN.MustBeInBounds<BOUNDED, Parameter6.MustBeInBounds.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        /** Closed bounds: [ min, max ]. */
        public MustBeInBounds (
                Comparator<BOUNDED> comparator,
                BOUNDED min,
                BOUNDED max
                )
        {
            super ( Parameter6.PARAMETER,  // parameter
                    comparator,            // comparator
                    min,                   // min
                    max );                 // max
        }

        public MustBeInBounds (
                Comparator<BOUNDED> comparator,
                BoundedFilter.EndPoint min_end_point,
                BOUNDED min,
                BoundedFilter.EndPoint max_end_point,
                BOUNDED max
                )
        {
            super ( Parameter6.PARAMETER,      // parameter
                    comparator,                // comparator
                    min_end_point,             // min_end_point
                    min,                       // min
                    max_end_point,             // max_end_point
                    max );                     // max
        }

        public MustBeInBounds (
                BoundedFilter<BOUNDED> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeInBounds.Violation violation (
                Object plaintiff,
                BOUNDED evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeInBounds.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeInBounds obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeInBounds<?> contract,
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
    public static class MustBeInstanceOf<INSTANCE extends Object>
        extends AbstractParameterN.MustBeInstanceOf<INSTANCE, Parameter6.MustBeInstanceOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustBeInstanceOf (
                Class<?> domain_class
                )
        {
            super ( Parameter6.PARAMETER,      // parameter
                    domain_class );            // domain_class
        }

        public MustBeInstanceOf (
                InstanceOf<INSTANCE> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeInstanceOf.Violation violation (
                Object plaintiff,
                INSTANCE evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeInstanceOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeInstanceOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeInstanceOf<?> contract,
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
    public static class MustNotBeInstanceOf<INSTANCE extends Object>
        extends AbstractParameterN.MustNotBeInstanceOf<INSTANCE, Parameter6.MustNotBeInstanceOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustNotBeInstanceOf (
                Class<?> verboten_class
                )
        {
            super ( Parameter6.PARAMETER,        // parameter
                    verboten_class );            // verboten_class
        }

        public MustNotBeInstanceOf (
                InstanceOf<INSTANCE> verboten_domain
                )
        {
            super ( Parameter6.PARAMETER,          // parameter
                    verboten_domain );             // verboten_domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustNotBeInstanceOf.Violation violation (
                Object plaintiff,
                INSTANCE evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustNotBeInstanceOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustNotBeInstanceOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustNotBeInstanceOf<?> contract,
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
    public static class MustBeEmptyString
        extends AbstractParameterN.MustBeEmptyString<Parameter6.MustBeEmptyString.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeEmptyString CONTRACT =
            new Parameter6.MustBeEmptyString ();

        public MustBeEmptyString ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeEmptyString.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeEmptyString.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeEmptyString obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeEmptyString contract,
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
    public static class MustNotBeEmptyString
        extends AbstractParameterN.MustNotBeEmptyString<Parameter6.MustNotBeEmptyString.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustNotBeEmptyString CONTRACT =
            new Parameter6.MustNotBeEmptyString ();

        public MustNotBeEmptyString ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustNotBeEmptyString.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustNotBeEmptyString.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustNotBeEmptyString obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustNotBeEmptyString contract,
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
    public static class MustBeStringLength
        extends AbstractParameterN.MustBeStringLength<Parameter6.MustBeStringLength.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;


        public MustBeStringLength (
                int exact_length
                )
        {
            super ( Parameter6.PARAMETER,      // parameter
                    exact_length );            // exact_length
        }

        public MustBeStringLength (
                int minimum_length,
                int maximum_length
                )
        {
            super ( Parameter6.PARAMETER,         // parameter
                    minimum_length,               // minimum_length
                    maximum_length );             // maximum_length
        }

        public MustBeStringLength (
                StringLength domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeStringLength.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeStringLength.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeStringLength obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeStringLength contract,
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
    public static class MustExcludeSpaces
        extends AbstractParameterN.MustExcludeSpaces<Parameter6.MustExcludeSpaces.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustExcludeSpaces CONTRACT =
            new Parameter6.MustExcludeSpaces ();

        public MustExcludeSpaces ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustExcludeSpaces.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustExcludeSpaces.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustExcludeSpaces obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustExcludeSpaces contract,
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
    public static class MustContainNonSpaces
        extends AbstractParameterN.MustContainNonSpaces<Parameter6.MustContainNonSpaces.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainNonSpaces CONTRACT =
            new Parameter6.MustContainNonSpaces ();

        public MustContainNonSpaces ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainNonSpaces.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainNonSpaces.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainNonSpaces obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainNonSpaces contract,
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
    public static class MustContainOnlyNumerics
        extends AbstractParameterN.MustContainOnlyNumerics<Parameter6.MustContainOnlyNumerics.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainOnlyNumerics CONTRACT =
            new Parameter6.MustContainOnlyNumerics ();

        public MustContainOnlyNumerics ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyNumerics.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyNumerics.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainOnlyNumerics obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyNumerics contract,
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
    public static class MustContainOnlyAlpha
        extends AbstractParameterN.MustContainOnlyAlpha<Parameter6.MustContainOnlyAlpha.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainOnlyAlpha CONTRACT =
            new Parameter6.MustContainOnlyAlpha ();

        public MustContainOnlyAlpha ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyAlpha.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyAlpha.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainOnlyAlpha obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyAlpha contract,
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
    public static class MustContainOnlyAlphaNumerics
        extends AbstractParameterN.MustContainOnlyAlphaNumerics<Parameter6.MustContainOnlyAlphaNumerics.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainOnlyAlphaNumerics CONTRACT =
            new Parameter6.MustContainOnlyAlphaNumerics ();

        public MustContainOnlyAlphaNumerics ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyAlphaNumerics.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyAlphaNumerics.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainOnlyAlphaNumerics obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyAlphaNumerics contract,
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
    public static class MustContainOnlyPrintableCharacters
        extends AbstractParameterN.MustContainOnlyPrintableCharacters<Parameter6.MustContainOnlyPrintableCharacters.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainOnlyPrintableCharacters CONTRACT =
            new Parameter6.MustContainOnlyPrintableCharacters ();

        public MustContainOnlyPrintableCharacters ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyPrintableCharacters.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyPrintableCharacters.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainOnlyPrintableCharacters obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyPrintableCharacters contract,
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
    public static class MustBeStringID
        extends AbstractParameterN.MustBeStringID<Parameter6.MustBeStringID.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustBeStringID CONTRACT =
            new Parameter6.MustBeStringID ();

        public MustBeStringID ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeStringID.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeStringID.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeStringID obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeStringID contract,
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
    public static class MustMatchPattern
        extends AbstractParameterN.MustMatchPattern<Parameter6.MustMatchPattern.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;


        public MustMatchPattern (
                Pattern pattern
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    pattern );            // pattern
        }

        public MustMatchPattern (
                StringPattern domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustMatchPattern.Violation violation (
                Object plaintiff,
                String evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustMatchPattern.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustMatchPattern obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustMatchPattern contract,
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
    public static class MustBeMemberOf<MEMBER extends Object>
        extends AbstractParameterN.MustBeMemberOf<MEMBER, Parameter6.MustBeMemberOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public MustBeMemberOf (
                MEMBER ... array
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    array );              // array
        }

        public MustBeMemberOf (
                Collection<MEMBER> collection
                )
        {
            super ( Parameter6.PARAMETER,    // parameter
                    collection );            // collection
        }

        public MustBeMemberOf (
                Iterable<MEMBER> iterable
                )
        {
            super ( Parameter6.PARAMETER,  // parameter
                    iterable );            // iterable
        }

        public MustBeMemberOf (
                MemberOf<MEMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustBeMemberOf.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustBeMemberOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustBeMemberOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustBeMemberOf<?> contract,
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
    public static class MustNotBeMemberOf<MEMBER extends Object>
        extends AbstractParameterN.MustNotBeMemberOf<MEMBER, Parameter6.MustNotBeMemberOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public MustNotBeMemberOf (
                MEMBER ... array
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    array );              // array
        }

        public MustNotBeMemberOf (
                Collection<MEMBER> collection
                )
        {
            super ( Parameter6.PARAMETER,     // parameter
                    collection );             // collection
        }

        public MustNotBeMemberOf (
                Iterable<MEMBER> iterable
                )
        {
            super ( Parameter6.PARAMETER,  // parameter
                    iterable );            // iterable
        }

        public MustNotBeMemberOf (
                MemberOf<MEMBER> membership
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    membership );         // membership
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustNotBeMemberOf.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustNotBeMemberOf.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustNotBeMemberOf obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustNotBeMemberOf<?> contract,
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
    public static class MustContainMembers<MEMBER extends Object>
        extends AbstractParameterN.MustContainMembers<MEMBER, Parameter6.MustContainMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        @SuppressWarnings("unchecked") // Generic varargs.
        public MustContainMembers (
                MEMBER ... members
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    members );            // members
        }

        public MustContainMembers (
                IncludesMembers<MEMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainMembers.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainMembers.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainMembers obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainMembers<?> contract,
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
    public static class MustContainOnlyMembers<MEMBER extends Object>
        extends AbstractParameterN.MustContainOnlyMembers<MEMBER, Parameter6.MustContainOnlyMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        @SuppressWarnings("unchecked") // Generic varargs.
        public MustContainOnlyMembers (
                MEMBER ... members
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    members );            // members
        }

        public MustContainOnlyMembers (
                IncludesOnlyMembers<MEMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyMembers.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyMembers.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainOnlyMembers obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyMembers<?> contract,
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
    public static class MustExcludeMembers<MEMBER extends Object>
        extends AbstractParameterN.MustExcludeMembers<MEMBER, Parameter6.MustExcludeMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        @SuppressWarnings("unchecked") // Possible heap pollution
        //                                generic varargs: MEMBER ...
        public MustExcludeMembers (
                MEMBER ... members
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    members );            // members
        }

        public MustExcludeMembers (
                Collection<MEMBER> members
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    members );            // members
        }

        public MustExcludeMembers (
                Iterable<MEMBER> members
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    members );            // members
        }

        public MustExcludeMembers (
                ExcludesMembers<MEMBER> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustExcludeMembers.Violation violation (
                Object plaintiff,
                Object evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustExcludeMembers.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustExcludeMembers obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustExcludeMembers<?> contract,
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
    public static class MustContainIndices<ELEMENT extends Object>
        extends AbstractParameterN.MustContainIndices<ELEMENT, Parameter6.MustContainIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustContainIndices (
                long ... indices
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    indices );            // indices
        }

        public MustContainIndices (
                int ... indices
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    indices );            // indices
        }

        public MustContainIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( Parameter6.PARAMETER,         // parameter
                    indices_filter );             // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainIndices.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainIndices obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainIndices<?> contract,
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
    public static class MustContainOnlyIndices<ELEMENT extends Object>
        extends AbstractParameterN.MustContainOnlyIndices<ELEMENT, Parameter6.MustContainOnlyIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustContainOnlyIndices (
                long ... indices
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    indices );            // indices
        }

        public MustContainOnlyIndices (
                int ... indices
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    indices );            // indices
        }

        public MustContainOnlyIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( Parameter6.PARAMETER,         // parameter
                    indices_filter );             // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyIndices.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainOnlyIndices obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyIndices<?> contract,
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
    public static class MustExcludeIndices<ELEMENT extends Object>
        extends AbstractParameterN.MustExcludeIndices<ELEMENT, Parameter6.MustExcludeIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustExcludeIndices (
                long ... indices
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    indices );            // indices
        }

        public MustExcludeIndices (
                int ... indices
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    indices );            // indices
        }

        public MustExcludeIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( Parameter6.PARAMETER,         // parameter
                    indices_filter );             // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustExcludeIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustExcludeIndices.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustExcludeIndices obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustExcludeIndices<?> contract,
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
    public static class MustContainNoDuplicates<ELEMENT extends Object>
        extends AbstractParameterN.MustContainNoDuplicates<ELEMENT, Parameter6.MustContainNoDuplicates.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainNoDuplicates<Object> CONTRACT =
            new Parameter6.MustContainNoDuplicates<Object> ();

        public MustContainNoDuplicates ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        public MustContainNoDuplicates (
                NoDuplicates<ELEMENT> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainNoDuplicates.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainNoDuplicates.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainNoDuplicates obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainNoDuplicates<?> contract,
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
        public static class MustBeGreaterThanOne
            extends AbstractParameterN.Length.MustBeGreaterThanOne<Parameter6.Length.MustBeGreaterThanOne.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public static final Parameter6.Length.MustBeGreaterThanOne CONTRACT =
                new Parameter6.Length.MustBeGreaterThanOne ();

            public MustBeGreaterThanOne ()
            {
                super ( Parameter6.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeGreaterThanOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeGreaterThanOne.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeGreaterThanOne obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeGreaterThanOne contract,
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
        public static class MustBeGreaterThanZero
            extends AbstractParameterN.Length.MustBeGreaterThanZero<Parameter6.Length.MustBeGreaterThanZero.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public static final Parameter6.Length.MustBeGreaterThanZero CONTRACT =
                new Parameter6.Length.MustBeGreaterThanZero ();

            public MustBeGreaterThanZero ()
            {
                super ( Parameter6.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeGreaterThanZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeGreaterThanZero.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeGreaterThanZero obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeGreaterThanZero contract,
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
        public static class MustEqual<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustEqual<ELEMENT, Parameter6.Length.MustEqual.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustEqual (
                    int length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustEqual (
                    long length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustEqual (
                    EqualToNumber<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            public MustEqual (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustEqual.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustEqual obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustEqual<?> contract,
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
        public static class MustNotEqual<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustNotEqual<ELEMENT, Parameter6.Length.MustNotEqual.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustNotEqual (
                    int length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustNotEqual (
                    long length
                    )
            {
                super ( Parameter6.PARAMETER,  // parameter
                        length );              // length
            }

            public MustNotEqual (
                    NotEqualToNumber<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER,  // parameter
                        domain );              // domain
            }

            public MustNotEqual (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustNotEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustNotEqual.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustNotEqual obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustNotEqual<?> contract,
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
        public static class MustEqualZero<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustEqualZero<ELEMENT, Parameter6.Length.MustEqualZero.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public static final Parameter6.Length.MustEqualZero<Object> CONTRACT =
                new Parameter6.Length.MustEqualZero<Object> ();

            public MustEqualZero ()
            {
                super ( Parameter6.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustEqualZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustEqualZero.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustEqualZero obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustEqualZero<?> contract,
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
        public static class MustEqualOne<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustEqualOne<ELEMENT, Parameter6.Length.MustEqualOne.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public static final Parameter6.Length.MustEqualOne<Object> CONTRACT =
                new Parameter6.Length.MustEqualOne<Object> ();

            public MustEqualOne ()
            {
                super ( Parameter6.PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustEqualOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustEqualOne.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustEqualOne obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustEqualOne<?> contract,
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
        public static class MustBeGreaterThan<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeGreaterThan<ELEMENT, Parameter6.Length.MustBeGreaterThan.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustBeGreaterThan (
                    int length
                    )
            {
                super ( Parameter6.PARAMETER,  // parameter
                        length );              // length
            }

            public MustBeGreaterThan (
                    long length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeGreaterThan (
                    GreaterThanNumber<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            public MustBeGreaterThan (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeGreaterThan.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeGreaterThan obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeGreaterThan<?> contract,
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
        public static class MustBeGreaterThanOrEqualTo<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeGreaterThanOrEqualTo<ELEMENT, Parameter6.Length.MustBeGreaterThanOrEqualTo.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustBeGreaterThanOrEqualTo (
                    int length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeGreaterThanOrEqualTo (
                    long length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeGreaterThanOrEqualTo (
                    GreaterThanOrEqualToNumber<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            public MustBeGreaterThanOrEqualTo (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeGreaterThanOrEqualTo.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeGreaterThanOrEqualTo obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeGreaterThanOrEqualTo<?> contract,
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
        public static class MustBeLessThan<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeLessThan<ELEMENT, Parameter6.Length.MustBeLessThan.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustBeLessThan (
                    int length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeLessThan (
                    long length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeLessThan (
                    LessThanNumber<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            public MustBeLessThan (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeLessThan.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeLessThan obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeLessThan<?> contract,
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
        public static class MustBeLessThanOrEqualTo<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeLessThanOrEqualTo<ELEMENT, Parameter6.Length.MustBeLessThanOrEqualTo.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustBeLessThanOrEqualTo (
                    int length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeLessThanOrEqualTo (
                    long length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        length );             // length
            }

            public MustBeLessThanOrEqualTo (
                    LessThanOrEqualToNumber<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            public MustBeLessThanOrEqualTo (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeLessThanOrEqualTo.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeLessThanOrEqualTo obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeLessThanOrEqualTo<?> contract,
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
        public static class MustBeBetween<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeBetween<ELEMENT, Parameter6.Length.MustBeBetween.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                    int minimum_length,
                    int maximum_length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        minimum_length,       // minimum_length
                        maximum_length );     // maximum_length
            }

            public MustBeBetween (
                    BoundedFilter.EndPoint minimum_end_point,
                    int minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    int maximum_length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        minimum_end_point,    // minimum_end_point
                        minimum_length,       // minimum_length
                        maximum_end_point,    // maximum_end_point
                        maximum_length );     // maximum_length
            }

            
            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                    long minimum_length,
                    long maximum_length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        minimum_length,       // minimum_length
                        maximum_length );     // maximum_length
            }

            public MustBeBetween (
                    BoundedFilter.EndPoint minimum_end_point,
                    long minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    long maximum_length
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        minimum_end_point,    // minimum_end_point
                        minimum_length,       // minimum_length
                        maximum_end_point,    // maximum_end_point
                        maximum_length );     // maximum_length
            }

            public MustBeBetween (
                    BoundedFilter<Long> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            public MustBeBetween (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeBetween.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeBetween obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeBetween<?> contract,
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
        public static class MustBeInDomain<ELEMENT extends Object>
            extends AbstractParameterN.Length.MustBeInDomain<ELEMENT, Parameter6.Length.MustBeInDomain.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;

            public MustBeInDomain (
                    Filter<Long> length_number_filter
                    )
            {
                super ( Parameter6.PARAMETER,              // parameter
                        length_number_filter );            // length_number_filter
            }

            public MustBeInDomain (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( Parameter6.PARAMETER, // parameter
                        domain );             // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected Parameter6.Length.MustBeInDomain.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ evidence_or_null,
                    String description
                    )
            {
                return new Parameter6.Length.MustBeInDomain.Violation (
                        this,             // contract
                        plaintiff,        // plaintiff
                        evidence_or_null, // evidence_or_null
                        description );    // description
            }


            /**
             * <p>
             * Violation of the Length.MustBeInDomain obligation contract.
             * </p>
             */
            public static class Violation
                extends UncheckedViolation
            {
                private static final long serialVersionUID =
                    Parameter6.serialVersionUID;
                public Violation (
                        Parameter6.Length.MustBeInDomain<?> contract,
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
    public static class MustContainNoNulls<ELEMENT extends Object>
        extends AbstractParameterN.MustContainNoNulls<ELEMENT, Parameter6.MustContainNoNulls.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public static final Parameter6.MustContainNoNulls<Object> CONTRACT =
            new Parameter6.MustContainNoNulls<Object> ();

        public MustContainNoNulls ()
        {
            super ( Parameter6.PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainNoNulls.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainNoNulls.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainNoNulls obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainNoNulls<?> contract,
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
    public static class MustContainOnlyClasses<ELEMENT extends Object>
        extends AbstractParameterN.MustContainOnlyClasses<ELEMENT, Parameter6.MustContainOnlyClasses.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustContainOnlyClasses (
                Class<?> ... required_classes
                )
        {
            super ( Parameter6.PARAMETER,          // parameter
                    required_classes );            // required_classes
        }

        public MustContainOnlyClasses (
                IncludesOnlyClasses<ELEMENT> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustContainOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustContainOnlyClasses.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustContainNoNulls obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustContainOnlyClasses<?> contract,
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
    public static class MustExcludeClasses<ELEMENT extends Object>
        extends AbstractParameterN.MustExcludeClasses<ELEMENT, Parameter6.MustExcludeClasses.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            Parameter6.serialVersionUID;

        public MustExcludeClasses (
                Class<?> ... excluded_classes
                )
        {
            super ( Parameter6.PARAMETER,          // parameter
                    excluded_classes );            // excluded_classes
        }

        public MustExcludeClasses (
                ExcludesClasses<ELEMENT> domain
                )
        {
            super ( Parameter6.PARAMETER, // parameter
                    domain );             // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractParameterN.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected Parameter6.MustExcludeClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ evidence_or_null,
                String description
                )
        {
            return new Parameter6.MustExcludeClasses.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        /**
         * <p>
         * Violation of the MustExcludeClasses obligation contract.
         * </p>
         */
        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                Parameter6.serialVersionUID;
            public Violation (
                    Parameter6.MustExcludeClasses<?> contract,
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
