package musaico.foundation.contract.obligations;

import java.io.Serializable;

import java.util.Comparator;


import musaico.foundation.contract.Domain;

import musaico.foundation.contract.domains.NaturalOrderComparator;


/**
 * <p>
 * A method which throws Parameter5.Violation
 * expects the specified parameter # to have a value in some domain
 * (such as a number greater than or equal to 1, or a String of
 * exactly 16 characters, and so on).  Violators will be sent
 * to arbitration, possibly inducing the runtime exception.
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
 * <pre>
 * Copyright (c) 2012, 2013 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class Parameter5
    extends ParameterN
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    /** Every time a new Domain is added to AllDomains, add it to this
     *  class, too. */
    private static final long serialVersionUID = 20131015L;
    private static final String serialVersionHash =
        "0x13FF7C9BBAF7D942D71EDB6A07506C4DBECA595F";


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected Parameter5 ()
    {
    }


    /** @see musaico.foundation.contract.Domain */
    public static class MustBe<DOMAIN extends Object>
        extends ParameterN.MustBe<DOMAIN, Parameter5.MustBe.DomainViolation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public MustBe (
                       Domain<DOMAIN> domain
                       )
        {
            super ( domain );
        }

        public static class DomainViolation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.GreaterThanZero */
    public static class MustBeGreaterThanZero
        extends ParameterN.MustBeGreaterThanZero<Parameter5.MustBeGreaterThanZero.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final MustBeGreaterThanZero CONTRACT =
            new MustBeGreaterThanZero ();

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.GreaterThanOrEqualToZero */
    public static class MustBeGreaterThanOrEqualToZero
        extends ParameterN.MustBeGreaterThanOrEqualToZero<Parameter5.MustBeGreaterThanOrEqualToZero.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final MustBeGreaterThanOrEqualToZero CONTRACT =
            new MustBeGreaterThanOrEqualToZero ();

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.LessThanZero */
    public static class MustBeLessThanZero
        extends ParameterN.MustBeLessThanZero<Parameter5.MustBeLessThanZero.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final MustBeLessThanZero CONTRACT =
            new MustBeLessThanZero ();

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.LessThanOrEqualToZero */
    public static class MustBeLessThanOrEqualToZero
        extends ParameterN.MustBeLessThanOrEqualToZero<Parameter5.MustBeLessThanOrEqualToZero.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final MustBeLessThanOrEqualToZero CONTRACT =
            new MustBeLessThanOrEqualToZero ();

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.BoundedDomain */
    public static class MustBeInBounds<BOUNDED extends Object>
        extends ParameterN.MustBeInBounds<BOUNDED, Parameter5.MustBeInBounds.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static <NATURALLY_ORDERED extends Comparable<NATURALLY_ORDERED>>
            MustBeInBounds<NATURALLY_ORDERED> over (
                                                    NATURALLY_ORDERED minimum,
                                                    NATURALLY_ORDERED maximum
                                                    )
            throws IllegalArgumentException
        {
            return new MustBeInBounds<NATURALLY_ORDERED> ( new NaturalOrderComparator<NATURALLY_ORDERED> (),
                                                           minimum, maximum );
        }

        public MustBeInBounds (
                               Comparator<BOUNDED> comparator,
                               BOUNDED minimum,
                               BOUNDED maximum
                               )
        {
            super ( comparator, minimum, maximum );
        }

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.ContainsElements */
    public static class MustContain
        extends ParameterN.MustContain<Parameter5.MustContain.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public MustContain (
                            Serializable ... elements
                            )
        {
            super ( elements );
        }

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.ExcludesElements */
    public static class MustExclude
        extends ParameterN.MustExclude<Parameter5.MustExclude.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public MustExclude (
                            Serializable ... elements
                            )
        {
            super ( elements );
        }

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.NoDuplicates */
    public static class MustContainNoDuplicates
        extends ParameterN.MustContainNoDuplicates<Parameter5.MustContainNoDuplicates.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final MustContainNoDuplicates CONTRACT =
            new MustContainNoDuplicates ();

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.NoNulls */
    public static class MustContainNoNulls
        extends ParameterN.MustContainNoNulls<Parameter5.MustContainNoNulls.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public static final MustContainNoNulls CONTRACT =
            new MustContainNoNulls ();

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }


    /** @see musaico.foundation.contract.domains.ExcludesSpecificClasses */
    public static class MustExcludeClasses
        extends ParameterN.MustExcludeClasses<Parameter5.MustExcludeClasses.Violation>
    {
        private static final long serialVersionUID =
            Parameter5.serialVersionUID;

        public MustExcludeClasses (
                                   Class<?>... classes
                                   )
        {
            super ( classes );
        }

        public static class Violation
            extends ParameterN.Violation
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;
        }
    }




    /** @see musaico.foundation.contract.domains.VariableDomain */
    public static class DependsOn
    {
        /** Parameter5 depends on the parent object, both must be in any one
         *  of a set of Domains. */
        public static class This
            extends ParameterN.DependsOn.This<Parameter5.DependsOn.This.Violation>
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public This (
                         Domain<?> ... domains
                         )
            {
                super ( domains );
            }

            public static class Violation
                extends ParameterN.Violation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
            }
        }

        /** Parameter5 depends on Parameter1, both must be in any one
         *  of a set of Domains. */
        public static class Parameter1
            extends ParameterN.DependsOn.Parameter1<Parameter5.DependsOn.Parameter1.Violation>
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public Parameter1 (
                               Domain<?> ... domains
                               )
            {
                super ( domains );
            }

            public static class Violation
                extends ParameterN.Violation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
            }
        }

        /** Parameter5 depends on Parameter2, both must be in any one
         *  of a set of Domains. */
        public static class Parameter2
            extends ParameterN.DependsOn.Parameter2<Parameter5.DependsOn.Parameter2.Violation>
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public Parameter2 (
                               Domain<?> ... domains
                               )
            {
                super ( domains );
            }

            public static class Violation
                extends ParameterN.Violation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
            }
        }

        /** Parameter5 depends on Parameter3, both must be in any one
         *  of a set of Domains. */
        public static class Parameter3
            extends ParameterN.DependsOn.Parameter3<Parameter5.DependsOn.Parameter3.Violation>
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public Parameter3 (
                               Domain<?> ... domains
                               )
            {
                super ( domains );
            }

            public static class Violation
                extends ParameterN.Violation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
            }
        }

        /** Parameter5 depends on Parameter4, both must be in any one
         *  of a set of Domains. */
        public static class Parameter4
            extends ParameterN.DependsOn.Parameter4<Parameter5.DependsOn.Parameter4.Violation>
        {
            private static final long serialVersionUID =
                Parameter5.serialVersionUID;

            public Parameter4 (
                               Domain<?> ... domains
                               )
            {
                super ( domains );
            }

            public static class Violation
                extends ParameterN.Violation
            {
                private static final long serialVersionUID =
                    Parameter5.serialVersionUID;
            }
        }
    }
}
