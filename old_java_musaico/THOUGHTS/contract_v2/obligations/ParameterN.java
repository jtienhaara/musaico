package musaico.foundation.contract.obligations;

import java.io.Serializable;

import java.lang.reflect.Constructor;

import java.util.Comparator;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.Domain;
import musaico.foundation.contract.DomainObligation;
import musaico.foundation.contract.DomainObligationUncheckedViolation;
import musaico.foundation.contract.Obligation;
import musaico.foundation.contract.ObligationUncheckedViolation;

import musaico.foundation.contract.domains.AllDomains;


/**
 * <p>
 * A method which throws ParameterN.Violation
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
public class ParameterN
    extends AllDomains
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    /** Every time a new Domain is added to AllDomains, add it to this
     *  class, too. */
    private static final long serialVersionUID = 20131015L;
    private static final String serialVersionHash =
        "0xD787E4536138B1E56039BBA7515CEC6B64978957";


    /**
     * <p>
     * For serial version hashing only.
     * </p>
     */
    protected ParameterN ()
    {
    }


    /** The base contract providing common boilerplate methods for
     *  the various domain obligation classes and their violation
     *  exception classes. */
    private static class Obligation<INSPECTABLE extends Object, VIOLATION extends ParameterN.Violation>
        extends DomainObligation<INSPECTABLE, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        public Obligation (
                           Domain<INSPECTABLE> domain
                           )
        {
            super ( domain );
        }

        @Override
        public VIOLATION createViolation (
                                          Serializable object_under_contract,
                                          Serializable inspectable_data
                                          )
        {
            String class_name = this.getClass ().getName ();
            class_name += "$Violation";

            final VIOLATION violation;
            try
            {
                // we know what we're doing, really!
                @SuppressWarnings("unchecked")
                Class<VIOLATION> violation_class = (Class<VIOLATION>)
                    Class.forName ( class_name );

                currentDomainObligation.set ( this );
                currentObjectUnderContract.set ( object_under_contract );
                currentInspectableData.set ( inspectable_data );

                violation = violation_class.newInstance ();
            }
            catch ( Exception e )
            {
                throw new IllegalStateException ( "Could not load contract violation exception class " + class_name, e );
            }

            return violation;
        }
    }


    /** set these before constructing the parameter # - specific violation,
     *  so that the ParameterN.Violation super constructor can pass the
     *  info along to its super constructor. */
    private static final ThreadLocal<DomainObligation<?, ?>> currentDomainObligation =
        new ThreadLocal<DomainObligation<?, ?>> ();
    private static final ThreadLocal<Serializable> currentObjectUnderContract =
        new ThreadLocal<Serializable> ();
    private static final ThreadLocal<Serializable> currentInspectableData =
        new ThreadLocal<Serializable> ();


    /** Base class for Parameter1.MustBeGreaterThanZero.Violation and so on. */
    public static class Violation
        extends DomainObligationUncheckedViolation
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected Violation ()
        {
            super ( currentDomainObligation.get (),
                    currentObjectUnderContract.get (),
                    currentInspectableData.get () );
        }
    }




    /** @see musaico.foundation.contract.Domain */
    public static class MustBe<DOMAIN extends Object, VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<DOMAIN, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustBe (
                          Domain<DOMAIN> domain
                          )
        {
            super ( domain );
        }
    }




    /** @see musaico.foundation.contract.domains.GreaterThanZero */
    public static class MustBeGreaterThanZero<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Number, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustBeGreaterThanZero ()
        {
            super ( AllDomains.GreaterThanZero.DOMAIN );
        }
    }


    /** @see musaico.foundation.contract.domains.GreaterThanOrEqualToZero */
    public static class MustBeGreaterThanOrEqualToZero<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Number, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustBeGreaterThanOrEqualToZero ()
        {
            super ( AllDomains.GreaterThanOrEqualToZero.DOMAIN );
        }
    }


    /** @see musaico.foundation.contract.domains.LessThanZero */
    public static class MustBeLessThanZero<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Number, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustBeLessThanZero ()
        {
            super ( AllDomains.LessThanZero.DOMAIN );
        }
    }


    /** @see musaico.foundation.contract.domains.LessThanOrEqualToZero */
    public static class MustBeLessThanOrEqualToZero<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Number, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustBeLessThanOrEqualToZero ()
        {
            super ( AllDomains.LessThanOrEqualToZero.DOMAIN );
        }
    }


    /** @see musaico.foundation.contract.domains.BoundedDomain */
    public static class MustBeInBounds<BOUNDED extends Object, VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<BOUNDED, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustBeInBounds (
                                  Comparator<BOUNDED> comparator,
                                  BOUNDED minimum,
                                  BOUNDED maximum
                                  )
        {
            super ( new AllDomains.BoundedDomain<BOUNDED> ( comparator,
                                                            minimum, maximum ) );
        }
    }




    /** @see musaico.foundation.contract.domains.ContainsElements */
    public static class MustContain<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        public MustContain (
                            Serializable ... elements
                            )
        {
            super ( new AllDomains.ContainsElements ( elements ) );
        }
    }


    /** @see musaico.foundation.contract.domains.ExcludesElements */
    public static class MustExclude<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        public MustExclude (
                            Serializable ... elements
                            )
        {
            super ( new AllDomains.ExcludesElements ( elements ) );
        }
    }


    /** @see musaico.foundation.contract.domains.NoDuplicates */
    public static class MustContainNoDuplicates<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustContainNoDuplicates ()
        {
            super ( AllDomains.NoDuplicates.DOMAIN );
        }
    }


    /** @see musaico.foundation.contract.domains.NoNulls */
    public static class MustContainNoNulls<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        protected MustContainNoNulls ()
        {
            super ( AllDomains.NoNulls.DOMAIN );
        }
    }


    /** @see musaico.foundation.contract.domains.ExcludesSpecificClasses */
    public static class MustExcludeClasses<VIOLATION extends ParameterN.Violation>
        extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
    {
        private static final long serialVersionUID =
            ParameterN.serialVersionUID;

        public MustExcludeClasses (
                                   Class<?>... classes
                                   )
        {
            super ( new AllDomains.ExcludesSpecificClasses ( classes ) );
        }
    }




    /** @see musaico.foundation.contract.domains.CoDependentDomain */
    public static class DependsOn
    {
        /** ParameterN depends on parent object, both must be in any one
         *  of a set of Domains. */
        public static class This<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public This (
                         Domain<?> ... domains
                         )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter1, both must be in any one
         *  of a set of Domains. */
        public static class Parameter1<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter1 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter2, both must be in any one
         *  of a set of Domains. */
        public static class Parameter2<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter2 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter3, both must be in any one
         *  of a set of Domains. */
        public static class Parameter3<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter3 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter4, both must be in any one
         *  of a set of Domains. */
        public static class Parameter4<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter4 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter5, both must be in any one
         *  of a set of Domains. */
        public static class Parameter5<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter5 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter6, both must be in any one
         *  of a set of Domains. */
        public static class Parameter6<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter6 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter7, both must be in any one
         *  of a set of Domains. */
        public static class Parameter7<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter7 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter8, both must be in any one
         *  of a set of Domains. */
        public static class Parameter8<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter8 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }

        /** ParameterN depends on Parameter9, both must be in any one
         *  of a set of Domains. */
        public static class Parameter9<VIOLATION extends ParameterN.Violation>
            extends ParameterN.Obligation<Object /* array or Collection */, VIOLATION>
        {
            private static final long serialVersionUID =
                ParameterN.serialVersionUID;

            public Parameter9 (
                               Domain<?> ... domains
                               )
            {
                super ( new AllDomains.CoDependentDomain ( domains ) );
            }
        }
    }
}
