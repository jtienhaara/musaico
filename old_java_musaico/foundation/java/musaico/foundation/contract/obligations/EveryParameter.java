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
 * Contracts for all parameters of a constructor or method.
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
public class EveryParameter
    extends AbstractMultipleParameters
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The singleton constant covering all parameters. */
    public static final EveryParameter EVERY_PARAMETER = new EveryParameter ();


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected EveryParameter ()
    {
    }


    /**
     * @see AbstractMultipleParameters#PARAMETER_BITMAP()
     */
    @Override
    public final long PARAMETER_BITMAP ()
    {
        // All parameters (up to 63 max).
        return AbstractMultipleParameters.EVERY_PARAMETER;
    }


    /**
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeInDomain
     */
    public static class MustBeInDomain<DOMAIN extends Object>
        extends AbstractMultipleParameters.MustBeInDomain<DOMAIN, EveryParameter.MustBeInDomain.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustBeInDomain (
                Filter<DOMAIN> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeInDomain.Violation violation (
                Object plaintiff,
                DOMAIN [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeInDomain.Violation (
                    this,             // contract
                    plaintiff,        // plaintiff
                    evidence_or_null, // evidence_or_null
                    description );    // description
        }


        public static class Violation
            extends UncheckedViolation
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeInDomain<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustNotBeNull
     */
    public static class MustNotBeNull
        extends AbstractMultipleParameters.MustNotBeNull<EveryParameter.MustNotBeNull.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustNotBeNull CONTRACT =
            new MustNotBeNull ();

        public MustNotBeNull ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustNotBeNull.Violation violation (
                Object plaintiff,
                Object [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustNotBeNull.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustNotBeNull contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustChange
     */
    public static class MustChange<CHANGED extends Object>
        extends AbstractMultipleParameters.MustChange<CHANGED, EveryParameter.MustChange.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustChange<Object> CONTRACT =
            new EveryParameter.MustChange<Object> ();

        public MustChange ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustChange.Violation violation (
                Object plaintiff,
                BeforeAndAfter<CHANGED> [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustChange.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustChange<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustNotChange
     */
    public static class MustNotChange<UNCHANGED extends Object>
        extends AbstractMultipleParameters.MustNotChange<UNCHANGED, EveryParameter.MustNotChange.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustNotChange<Object> CONTRACT =
            new EveryParameter.MustNotChange<Object> ();

        public MustNotChange ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustNotChange.Violation violation (
                Object plaintiff,
                BeforeAndAfter<UNCHANGED> [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustNotChange.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustNotChange<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustEqual
     */
    public static class MustEqual<EQUAL extends Object>
        extends AbstractMultipleParameters.MustEqual<EQUAL, EveryParameter.MustEqual.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustEqual (
                EQUAL object
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    object );                       // object
        }

        public MustEqual (
                EqualTo<EQUAL> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // object
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustEqual.Violation violation (
                Object plaintiff,
                EQUAL [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustEqual.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustEqual<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustNotEqual
     */
    public static class MustNotEqual<UNEQUAL extends Object>
        extends AbstractMultipleParameters.MustNotEqual<UNEQUAL, EveryParameter.MustNotEqual.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustNotEqual (
                UNEQUAL object
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    object );                       // object
        }

        public MustNotEqual (
                NotEqualTo<UNEQUAL> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustNotEqual.Violation violation (
                Object plaintiff,
                UNEQUAL [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustNotEqual.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustNotEqual<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanNegativeOne
     */
    public static class MustBeGreaterThanNegativeOne
        extends AbstractMultipleParameters.MustBeGreaterThanNegativeOne<EveryParameter.MustBeGreaterThanNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeGreaterThanNegativeOne CONTRACT =
            new EveryParameter.MustBeGreaterThanNegativeOne ();

        public MustBeGreaterThanNegativeOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanNegativeOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanNegativeOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanNegativeOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanOne
     */
    public static class MustBeGreaterThanOne
        extends AbstractMultipleParameters.MustBeGreaterThanOne<EveryParameter.MustBeGreaterThanOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeGreaterThanOne CONTRACT =
            new EveryParameter.MustBeGreaterThanOne ();

        public MustBeGreaterThanOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanZero
     */
    public static class MustBeGreaterThanZero
        extends AbstractMultipleParameters.MustBeGreaterThanZero<EveryParameter.MustBeGreaterThanZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeGreaterThanZero CONTRACT =
            new EveryParameter.MustBeGreaterThanZero ();

        public MustBeGreaterThanZero ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanZero.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanZero.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanZero contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanOrEqualToNegativeOne
     */
    public static class MustBeGreaterThanOrEqualToNegativeOne
        extends AbstractMultipleParameters.MustBeGreaterThanOrEqualToNegativeOne<EveryParameter.MustBeGreaterThanOrEqualToNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeGreaterThanOrEqualToNegativeOne CONTRACT =
            new EveryParameter.MustBeGreaterThanOrEqualToNegativeOne ();

        public MustBeGreaterThanOrEqualToNegativeOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanOrEqualToNegativeOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanOrEqualToNegativeOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanOrEqualToOne
     */
    public static class MustBeGreaterThanOrEqualToOne
        extends AbstractMultipleParameters.MustBeGreaterThanOrEqualToOne<EveryParameter.MustBeGreaterThanOrEqualToOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeGreaterThanOrEqualToOne CONTRACT =
            new EveryParameter.MustBeGreaterThanOrEqualToOne ();

        public MustBeGreaterThanOrEqualToOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanOrEqualToOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanOrEqualToOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanOrEqualToZero
     */
    public static class MustBeGreaterThanOrEqualToZero
        extends AbstractMultipleParameters.MustBeGreaterThanOrEqualToZero<EveryParameter.MustBeGreaterThanOrEqualToZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeGreaterThanOrEqualToZero CONTRACT =
            new EveryParameter.MustBeGreaterThanOrEqualToZero ();

        public MustBeGreaterThanOrEqualToZero ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanOrEqualToZero.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanOrEqualToZero contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanNegativeOne
     */
    public static class MustBeLessThanNegativeOne
        extends AbstractMultipleParameters.MustBeLessThanNegativeOne<EveryParameter.MustBeLessThanNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeLessThanNegativeOne CONTRACT =
            new EveryParameter.MustBeLessThanNegativeOne ();

        public MustBeLessThanNegativeOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanNegativeOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanNegativeOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanNegativeOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanOne
     */
    public static class MustBeLessThanOne
        extends AbstractMultipleParameters.MustBeLessThanOne<EveryParameter.MustBeLessThanOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeLessThanOne CONTRACT =
            new EveryParameter.MustBeLessThanOne ();

        public MustBeLessThanOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanZero
     */
    public static class MustBeLessThanZero
        extends AbstractMultipleParameters.MustBeLessThanZero<EveryParameter.MustBeLessThanZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeLessThanZero CONTRACT =
            new EveryParameter.MustBeLessThanZero ();

        public MustBeLessThanZero ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanZero.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanZero.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanZero contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanOrEqualToNegativeOne
     */
    public static class MustBeLessThanOrEqualToNegativeOne
        extends AbstractMultipleParameters.MustBeLessThanOrEqualToNegativeOne<EveryParameter.MustBeLessThanOrEqualToNegativeOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeLessThanOrEqualToNegativeOne CONTRACT =
            new EveryParameter.MustBeLessThanOrEqualToNegativeOne ();

        public MustBeLessThanOrEqualToNegativeOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanOrEqualToNegativeOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanOrEqualToNegativeOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanOrEqualToNegativeOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanOrEqualToOne
     */
    public static class MustBeLessThanOrEqualToOne
        extends AbstractMultipleParameters.MustBeLessThanOrEqualToOne<EveryParameter.MustBeLessThanOrEqualToOne.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeLessThanOrEqualToOne CONTRACT =
            new EveryParameter.MustBeLessThanOrEqualToOne ();

        public MustBeLessThanOrEqualToOne ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanOrEqualToOne.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanOrEqualToOne.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanOrEqualToOne contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanOrEqualToZero
     */
    public static class MustBeLessThanOrEqualToZero
        extends AbstractMultipleParameters.MustBeLessThanOrEqualToZero<EveryParameter.MustBeLessThanOrEqualToZero.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeLessThanOrEqualToZero CONTRACT =
            new EveryParameter.MustBeLessThanOrEqualToZero ();

        public MustBeLessThanOrEqualToZero ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanOrEqualToZero.Violation violation (
                Object plaintiff,
                Number [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanOrEqualToZero.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanOrEqualToZero contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThan
     */
    public static class MustBeGreaterThan<NUMBER extends Number>
        extends AbstractMultipleParameters.MustBeGreaterThan<NUMBER, EveryParameter.MustBeGreaterThan.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustBeGreaterThan (
                NUMBER number
                )
        {
            super ( EveryParameter.EVERY_PARAMETER,  // parameter
                    number );                        // number
        }

        public MustBeGreaterThan (
                GreaterThanNumber<NUMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThan.Violation violation (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThan.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThan<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeGreaterThanOrEqualTo
     */
    public static class MustBeGreaterThanOrEqualTo<NUMBER extends Number>
        extends AbstractMultipleParameters.MustBeGreaterThanOrEqualTo<NUMBER, EveryParameter.MustBeGreaterThanOrEqualTo.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustBeGreaterThanOrEqualTo (
                NUMBER number
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    number );                       // number
        }

        public MustBeGreaterThanOrEqualTo (
                GreaterThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeGreaterThanOrEqualTo.Violation violation (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeGreaterThanOrEqualTo.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeGreaterThanOrEqualTo<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThan
     */
    public static class MustBeLessThan<NUMBER extends Number>
        extends AbstractMultipleParameters.MustBeLessThan<NUMBER, EveryParameter.MustBeLessThan.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustBeLessThan (
                NUMBER number
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    number );                       // number
        }

        public MustBeLessThan (
                LessThanNumber<NUMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThan.Violation violation (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThan.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThan<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeLessThanOrEqualTo
     */
    public static class MustBeLessThanOrEqualTo<NUMBER extends Number>
        extends AbstractMultipleParameters.MustBeLessThanOrEqualTo<NUMBER, EveryParameter.MustBeLessThanOrEqualTo.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustBeLessThanOrEqualTo (
                NUMBER number
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    number );                       // number
        }

        public MustBeLessThanOrEqualTo (
                LessThanOrEqualToNumber<NUMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeLessThanOrEqualTo.Violation violation (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeLessThanOrEqualTo.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeLessThanOrEqualTo<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeBetween
     */
    public static class MustBeBetween<NUMBER extends Number>
        extends AbstractMultipleParameters.MustBeBetween<NUMBER, EveryParameter.MustBeBetween.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        /** Closed bounds: [ minimum_number, maximum_number ]. */
        public MustBeBetween (
                NUMBER minimum_number,
                NUMBER maximum_number
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    minimum_number,                 // minimum_number
                    maximum_number );               // maximum_number
        }

        public MustBeBetween (
                BoundedFilter.EndPoint minimum_end_point,
                NUMBER minimum_number,
                BoundedFilter.EndPoint maximum_end_point,
                NUMBER maximum_number
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    minimum_end_point,              // minimum_end_point
                    minimum_number,                 // minimum_number
                    maximum_end_point,              // maximum_end_point
                    maximum_number );               // maximum_number
        }

        public MustBeBetween (
                BoundedFilter<NUMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeBetween.Violation violation (
                Object plaintiff,
                NUMBER [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeBetween.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeBetween<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeInBounds
     */
    public static class MustBeInBounds<BOUNDED extends Object>
        extends AbstractMultipleParameters.MustBeInBounds<BOUNDED, EveryParameter.MustBeInBounds.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        /** Closed bounds: [ min, max ]. */
        public MustBeInBounds (
                Comparator<BOUNDED> comparator,
                BOUNDED min,
                BOUNDED max
                )
        {
            super ( EveryParameter.EVERY_PARAMETER,  // parameter
                    comparator,                      // comparator
                    min,                             // min
                    max );                           // max
        }

        public MustBeInBounds (
                Comparator<BOUNDED> comparator,
                BoundedFilter.EndPoint min_end_point,
                BOUNDED min,
                BoundedFilter.EndPoint max_end_point,
                BOUNDED max
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    comparator,                     // comparator
                    min_end_point,                  // min_end_point
                    min,                            // min
                    max_end_point,                  // max_end_point
                    max );                          // max
        }

        public MustBeInBounds (
                BoundedFilter<BOUNDED> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeInBounds.Violation violation (
                Object plaintiff,
                BOUNDED [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeInBounds.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeInBounds<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeInstanceOf
     */
    public static class MustBeInstanceOf<INSTANCE extends Object>
        extends AbstractMultipleParameters.MustBeInstanceOf<INSTANCE, EveryParameter.MustBeInstanceOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustBeInstanceOf (
                Class<?> domain_class
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain_class );                 // domain_class
        }

        public MustBeInstanceOf (
                InstanceOf<INSTANCE> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeInstanceOf.Violation violation (
                Object plaintiff,
                INSTANCE [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeInstanceOf.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeInstanceOf<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustNotBeInstanceOf
     */
    public static class MustNotBeInstanceOf<INSTANCE extends Object>
        extends AbstractMultipleParameters.MustNotBeInstanceOf<INSTANCE, EveryParameter.MustNotBeInstanceOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustNotBeInstanceOf (
                Class<?> verboten_class
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    verboten_class );               // verboten_class
        }

        public MustNotBeInstanceOf (
                InstanceOf<INSTANCE> verboten_domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    verboten_domain );              // verboten_domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustNotBeInstanceOf.Violation violation (
                Object plaintiff,
                INSTANCE [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustNotBeInstanceOf.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustNotBeInstanceOf<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeEmptyString
     */
    public static class MustBeEmptyString
        extends AbstractMultipleParameters.MustBeEmptyString<EveryParameter.MustBeEmptyString.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeEmptyString CONTRACT =
            new EveryParameter.MustBeEmptyString ();

        public MustBeEmptyString ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeEmptyString.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeEmptyString.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeEmptyString contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustNotBeEmptyString
     */
    public static class MustNotBeEmptyString
        extends AbstractMultipleParameters.MustNotBeEmptyString<EveryParameter.MustNotBeEmptyString.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustNotBeEmptyString CONTRACT =
            new EveryParameter.MustNotBeEmptyString ();

        public MustNotBeEmptyString ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustNotBeEmptyString.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustNotBeEmptyString.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustNotBeEmptyString contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeStringLength
     */
    public static class MustBeStringLength
        extends AbstractMultipleParameters.MustBeStringLength<EveryParameter.MustBeStringLength.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;


        public MustBeStringLength (
                int exact_length
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    exact_length );                 // exact_length
        }

        public MustBeStringLength (
                int minimum_length,
                int maximum_length
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    minimum_length,                 // minimum_length
                    maximum_length );               // maximum_length
        }

        public MustBeStringLength (
                StringLength domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeStringLength.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeStringLength.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeStringLength contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustExcludeSpaces
     */
    public static class MustExcludeSpaces
        extends AbstractMultipleParameters.MustExcludeSpaces<EveryParameter.MustExcludeSpaces.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustExcludeSpaces CONTRACT =
            new EveryParameter.MustExcludeSpaces ();

        public MustExcludeSpaces ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustExcludeSpaces.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustExcludeSpaces.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustExcludeSpaces contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainNonSpaces
     */
    public static class MustContainNonSpaces
        extends AbstractMultipleParameters.MustContainNonSpaces<EveryParameter.MustContainNonSpaces.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainNonSpaces CONTRACT =
            new EveryParameter.MustContainNonSpaces ();

        public MustContainNonSpaces ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainNonSpaces.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainNonSpaces.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainNonSpaces contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyNumerics
     */
    public static class MustContainOnlyNumerics
        extends AbstractMultipleParameters.MustContainOnlyNumerics<EveryParameter.MustContainOnlyNumerics.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainOnlyNumerics CONTRACT =
            new EveryParameter.MustContainOnlyNumerics ();

        public MustContainOnlyNumerics ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyNumerics.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyNumerics.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyNumerics contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyAlpha
     */
    public static class MustContainOnlyAlpha
        extends AbstractMultipleParameters.MustContainOnlyAlpha<EveryParameter.MustContainOnlyAlpha.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainOnlyAlpha CONTRACT =
            new EveryParameter.MustContainOnlyAlpha ();

        public MustContainOnlyAlpha ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyAlpha.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyAlpha.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyAlpha contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyAlphaNumerics
     */
    public static class MustContainOnlyAlphaNumerics
        extends AbstractMultipleParameters.MustContainOnlyAlphaNumerics<EveryParameter.MustContainOnlyAlphaNumerics.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainOnlyAlphaNumerics CONTRACT =
            new EveryParameter.MustContainOnlyAlphaNumerics ();

        public MustContainOnlyAlphaNumerics ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyAlphaNumerics.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyAlphaNumerics.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyAlphaNumerics contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyPrintableCharacters
     */
    public static class MustContainOnlyPrintableCharacters
        extends AbstractMultipleParameters.MustContainOnlyPrintableCharacters<EveryParameter.MustContainOnlyPrintableCharacters.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainOnlyPrintableCharacters CONTRACT =
            new EveryParameter.MustContainOnlyPrintableCharacters ();

        public MustContainOnlyPrintableCharacters ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyPrintableCharacters.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyPrintableCharacters.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyPrintableCharacters contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeStringID
     */
    public static class MustBeStringID
        extends AbstractMultipleParameters.MustBeStringID<EveryParameter.MustBeStringID.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustBeStringID CONTRACT =
            new EveryParameter.MustBeStringID ();

        public MustBeStringID ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeStringID.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeStringID.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeStringID contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustMatchPattern
     */
    public static class MustMatchPattern
        extends AbstractMultipleParameters.MustMatchPattern<EveryParameter.MustMatchPattern.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;


        public MustMatchPattern (
                Pattern pattern
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    pattern );                      // pattern
        }

        public MustMatchPattern (
                StringPattern domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustMatchPattern.Violation violation (
                Object plaintiff,
                String [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustMatchPattern.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustMatchPattern contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustBeMemberOf
     */
    public static class MustBeMemberOf<MEMBER extends Object>
        extends AbstractMultipleParameters.MustBeMemberOf<MEMBER, EveryParameter.MustBeMemberOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public MustBeMemberOf (
                MEMBER ... array
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    array );                        // array
        }

        public MustBeMemberOf (
                Collection<MEMBER> collection
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    collection );                   // collection
        }

        public MustBeMemberOf (
                Iterable<MEMBER> iterable
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    iterable );                     // iterable
        }

        public MustBeMemberOf (
                MemberOf<MEMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustBeMemberOf.Violation violation (
                Object plaintiff,
                Object [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustBeMemberOf.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustBeMemberOf<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustNotBeMemberOf
     */
    public static class MustNotBeMemberOf<MEMBER extends Object>
        extends AbstractMultipleParameters.MustNotBeMemberOf<MEMBER, EveryParameter.MustNotBeMemberOf.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        @SafeVarargs
        @SuppressWarnings("varargs") // MEMBER ...
        public MustNotBeMemberOf (
                MEMBER ... array
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    array );                        // array
        }

        public MustNotBeMemberOf (
                Collection<MEMBER> collection
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    collection );                   // collection
        }

        public MustNotBeMemberOf (
                Iterable<MEMBER> iterable
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    iterable );                     // iterable
        }

        public MustNotBeMemberOf (
                MemberOf<MEMBER> membership
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    membership );                   // membership
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustNotBeMemberOf.Violation violation (
                Object plaintiff,
                Object [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustNotBeMemberOf.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustNotBeMemberOf<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainMembers
     */
    public static class MustContainMembers<MEMBER extends Object>
        extends AbstractMultipleParameters.MustContainMembers<MEMBER, EveryParameter.MustContainMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        @SuppressWarnings("unchecked") // Generic varargs.
        public MustContainMembers (
                MEMBER ... members
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    members );                      // members
        }

        public MustContainMembers (
                IncludesMembers<MEMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainMembers.Violation violation (
                Object plaintiff,
                Object [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainMembers.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainMembers<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyMembers
     */
    public static class MustContainOnlyMembers<MEMBER extends Object>
        extends AbstractMultipleParameters.MustContainOnlyMembers<MEMBER, EveryParameter.MustContainOnlyMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        @SuppressWarnings("unchecked") // Generic varargs.
        public MustContainOnlyMembers (
                MEMBER ... members
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    members );                      // members
        }

        public MustContainOnlyMembers (
                IncludesOnlyMembers<MEMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyMembers.Violation violation (
                Object plaintiff,
                Object [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyMembers.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyMembers<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustExcludeMembers
     */
    public static class MustExcludeMembers<MEMBER extends Object>
        extends AbstractMultipleParameters.MustExcludeMembers<MEMBER, EveryParameter.MustExcludeMembers.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        @SuppressWarnings("unchecked") // Possible heap pollution
        //                                generic varargs: MEMBER ...
        public MustExcludeMembers (
                MEMBER ... members
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    members );                      // members
        }

        public MustExcludeMembers (
                Collection<MEMBER> members
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    members );                      // members
        }

        public MustExcludeMembers (
                Iterable<MEMBER> members
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    members );                      // members
        }

        public MustExcludeMembers (
                ExcludesMembers<MEMBER> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustExcludeMembers.Violation violation (
                Object plaintiff,
                Object [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustExcludeMembers.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustExcludeMembers<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainIndices
     */
    public static class MustContainIndices<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustContainIndices<ELEMENT, EveryParameter.MustContainIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustContainIndices (
                long ... indices
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices );                      // indices
        }

        public MustContainIndices (
                int ... indices
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices );                      // indices
        }

        public MustContainIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices_filter );               // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainIndices.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainIndices<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyIndices
     */
    public static class MustContainOnlyIndices<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustContainOnlyIndices<ELEMENT, EveryParameter.MustContainOnlyIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustContainOnlyIndices (
                long ... indices
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices );                      // indices
        }

        public MustContainOnlyIndices (
                int ... indices
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices );                      // indices
        }

        public MustContainOnlyIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices_filter );               // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyIndices.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyIndices<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustExcludeIndices
     */
    public static class MustExcludeIndices<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustExcludeIndices<ELEMENT, EveryParameter.MustExcludeIndices.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustExcludeIndices (
                long ... indices
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices );                      // indices
        }

        public MustExcludeIndices (
                int ... indices
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices );                      // indices
        }

        public MustExcludeIndices (
                AllIndicesFilter<ELEMENT> indices_filter
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    indices_filter );               // indices_filter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustExcludeIndices.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustExcludeIndices.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustExcludeIndices<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainNoDuplicates
     */
    public static class MustContainNoDuplicates<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustContainNoDuplicates<ELEMENT, EveryParameter.MustContainNoDuplicates.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainNoDuplicates<Object> CONTRACT =
            new EveryParameter.MustContainNoDuplicates<Object> ();

        public MustContainNoDuplicates ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        public MustContainNoDuplicates (
                NoDuplicates<ELEMENT> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainNoDuplicates.Violation violation (
                Object plaintiff,
                Object /* array or Collection */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainNoDuplicates.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainNoDuplicates<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeGreaterThanOne
         */
        public static class MustBeGreaterThanOne
            extends AbstractMultipleParameters.Length.MustBeGreaterThanOne<EveryParameter.Length.MustBeGreaterThanOne.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public static final EveryParameter.Length.MustBeGreaterThanOne CONTRACT =
                new EveryParameter.Length.MustBeGreaterThanOne ();

            public MustBeGreaterThanOne ()
            {
                super ( EveryParameter.EVERY_PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeGreaterThanOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeGreaterThanOne.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeGreaterThanOne contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeGreaterThanZero
         */
        public static class MustBeGreaterThanZero
            extends AbstractMultipleParameters.Length.MustBeGreaterThanZero<EveryParameter.Length.MustBeGreaterThanZero.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public static final EveryParameter.Length.MustBeGreaterThanZero CONTRACT =
                new EveryParameter.Length.MustBeGreaterThanZero ();

            public MustBeGreaterThanZero ()
            {
                super ( EveryParameter.EVERY_PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeGreaterThanZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeGreaterThanZero.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeGreaterThanZero contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustEqual
         */
        public static class MustEqual<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustEqual<ELEMENT, EveryParameter.Length.MustEqual.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustEqual (
                    int length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustEqual (
                    long length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustEqual (
                    EqualToNumber<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustEqual (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustEqual.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustEqual<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustNotEqual
         */
        public static class MustNotEqual<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustNotEqual<ELEMENT, EveryParameter.Length.MustNotEqual.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustNotEqual (
                    int length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustNotEqual (
                    long length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustNotEqual (
                    NotEqualToNumber<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustNotEqual (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustNotEqual.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustNotEqual.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustNotEqual<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustEqualZero
         */
        public static class MustEqualZero<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustEqualZero<ELEMENT, EveryParameter.Length.MustEqualZero.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public static final EveryParameter.Length.MustEqualZero<Object> CONTRACT =
                new EveryParameter.Length.MustEqualZero<Object> ();

            public MustEqualZero ()
            {
                super ( EveryParameter.EVERY_PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustEqualZero.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustEqualZero.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustEqualZero<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustEqualOne
         */
        public static class MustEqualOne<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustEqualOne<ELEMENT, EveryParameter.Length.MustEqualOne.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public static final EveryParameter.Length.MustEqualOne<Object> CONTRACT =
                new EveryParameter.Length.MustEqualOne<Object> ();

            public MustEqualOne ()
            {
                super ( EveryParameter.EVERY_PARAMETER ); // parameter
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustEqualOne.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustEqualOne.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustEqualOne<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeGreaterThan
         */
        public static class MustBeGreaterThan<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustBeGreaterThan<ELEMENT, EveryParameter.Length.MustBeGreaterThan.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustBeGreaterThan (
                    int length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeGreaterThan (
                    long length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeGreaterThan (
                    GreaterThanNumber<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustBeGreaterThan (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeGreaterThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeGreaterThan.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeGreaterThan<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeGreaterThanOrEqualTo
         */
        public static class MustBeGreaterThanOrEqualTo<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustBeGreaterThanOrEqualTo<ELEMENT, EveryParameter.Length.MustBeGreaterThanOrEqualTo.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustBeGreaterThanOrEqualTo (
                    int length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeGreaterThanOrEqualTo (
                    long length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeGreaterThanOrEqualTo (
                    GreaterThanOrEqualToNumber<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustBeGreaterThanOrEqualTo (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeGreaterThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeGreaterThanOrEqualTo.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeGreaterThanOrEqualTo<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeLessThan
         */
        public static class MustBeLessThan<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustBeLessThan<ELEMENT, EveryParameter.Length.MustBeLessThan.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustBeLessThan (
                    int length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeLessThan (
                    long length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeLessThan (
                    LessThanNumber<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustBeLessThan (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeLessThan.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeLessThan.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeLessThan<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeLessThanOrEqualTo
         */
        public static class MustBeLessThanOrEqualTo<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustBeLessThanOrEqualTo<ELEMENT, EveryParameter.Length.MustBeLessThanOrEqualTo.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustBeLessThanOrEqualTo (
                    int length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeLessThanOrEqualTo (
                    long length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length );                       // length
            }

            public MustBeLessThanOrEqualTo (
                    LessThanOrEqualToNumber<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustBeLessThanOrEqualTo (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeLessThanOrEqualTo.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeLessThanOrEqualTo.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeLessThanOrEqualTo<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeBetween
         */
        public static class MustBeBetween<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustBeBetween<ELEMENT, EveryParameter.Length.MustBeBetween.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                    int minimum_length,
                    int maximum_length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        minimum_length,                 // minimum_length
                        maximum_length );               // maximum_length
            }

            public MustBeBetween (
                    BoundedFilter.EndPoint minimum_end_point,
                    int minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    int maximum_length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        minimum_end_point,              // minimum_end_point
                        minimum_length,                 // minimum_length
                        maximum_end_point,              // maximum_end_point
                        maximum_length );               // maximum_length
            }

            
            /** Closed bounds: [ minimum_length, maximum_length ]. */
            public MustBeBetween (
                    long minimum_length,
                    long maximum_length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        minimum_length,                 // minimum_length
                        maximum_length );               // maximum_length
            }

            public MustBeBetween (
                    BoundedFilter.EndPoint minimum_end_point,
                    long minimum_length,
                    BoundedFilter.EndPoint maximum_end_point,
                    long maximum_length
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        minimum_end_point,              // minimum_end_point
                        minimum_length,                 // minimum_length
                        maximum_end_point,              // maximum_end_point
                        maximum_length );               // maximum_length
            }

            public MustBeBetween (
                    BoundedFilter<Long> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            public MustBeBetween (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeBetween.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeBetween.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeBetween<?> contract,
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
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.Length.MustBeInDomain
         */
        public static class MustBeInDomain<ELEMENT extends Object>
            extends AbstractMultipleParameters.Length.MustBeInDomain<ELEMENT, EveryParameter.Length.MustBeInDomain.Violation>
            implements Serializable
        {
            private static final long serialVersionUID =
                EveryParameter.serialVersionUID;

            public MustBeInDomain (
                    Filter<Long> length_number_filter
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        length_number_filter );         // length_number_filter
            }

            public MustBeInDomain (
                    LengthFilter<ELEMENT> domain
                    )
            {
                super ( EveryParameter.EVERY_PARAMETER, // parameter
                        domain );                       // domain
            }

            /**
             * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
             */
            @Override
            protected EveryParameter.Length.MustBeInDomain.Violation violation (
                    Object plaintiff,
                    Object /* array or Collection etc */ [] evidence_or_null,
                    String description
                    )
            {
                return new EveryParameter.Length.MustBeInDomain.Violation (
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
                    EveryParameter.serialVersionUID;
                public Violation (
                        EveryParameter.Length.MustBeInDomain<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainNoNulls
     */
    public static class MustContainNoNulls<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustContainNoNulls<ELEMENT, EveryParameter.MustContainNoNulls.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public static final EveryParameter.MustContainNoNulls<Object> CONTRACT =
            new EveryParameter.MustContainNoNulls<Object> ();

        public MustContainNoNulls ()
        {
            super ( EveryParameter.EVERY_PARAMETER ); // parameter
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainNoNulls.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainNoNulls.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainNoNulls<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustContainOnlyClasses
     */
    public static class MustContainOnlyClasses<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustContainOnlyClasses<ELEMENT, EveryParameter.MustContainOnlyClasses.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustContainOnlyClasses (
                Class<?> ... required_classes
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    required_classes );             // required_classes
        }

        public MustContainOnlyClasses (
                IncludesOnlyClasses<ELEMENT> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustContainOnlyClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustContainOnlyClasses.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustContainOnlyClasses<?> contract,
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
     * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.MustExcludeClasses
     */
    public static class MustExcludeClasses<ELEMENT extends Object>
        extends AbstractMultipleParameters.MustExcludeClasses<ELEMENT, EveryParameter.MustExcludeClasses.Violation>
        implements Serializable
    {
        private static final long serialVersionUID =
            EveryParameter.serialVersionUID;

        public MustExcludeClasses (
                Class<?> ... excluded_classes
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    excluded_classes );             // excluded_classes
        }

        public MustExcludeClasses (
                ExcludesClasses<ELEMENT> domain
                )
        {
            super ( EveryParameter.EVERY_PARAMETER, // parameter
                    domain );                       // domain
        }

        /**
         * @see musaico.foundation.contract.obligations.AbstractMultipleParameters.AbstractObligation#violation(java.lang.Object, java.lang.Object, java.lang.String)
         */
        @Override
        protected EveryParameter.MustExcludeClasses.Violation violation (
                Object plaintiff,
                Object /* array or Collection etc */ [] evidence_or_null,
                String description
                )
        {
            return new EveryParameter.MustExcludeClasses.Violation (
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
                EveryParameter.serialVersionUID;
            public Violation (
                    EveryParameter.MustExcludeClasses<?> contract,
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
