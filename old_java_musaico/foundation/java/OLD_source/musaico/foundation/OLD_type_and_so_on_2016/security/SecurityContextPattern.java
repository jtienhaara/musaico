package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Matches a security context by some criterion or criteria, such
 * as "the current process is being run by the user who owns file F"
 * and so on.
 * </p>
 *
 *
 * <p>
 * In Java every SecurityContextPattern must be Serializable in order
 * to play nicely over RMI.
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
 * @see musaico.foundation.security.MODULE#COPYRIGHT
 * @see musaico.foundation.security.MODULE#LICENSE
 */
public interface SecurityContextPattern
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Matches any and all SecurityContext. */
    public static final SecurityContextPattern.Any ANY = new Any ();

    /** Never matches any SecurityContext at all. */
    public static final SecurityContextPattern.None NONE = new None ();


    /**
     * <p>
     * Returns true if the specified SecurityContext matches this
     * pattern, false if not.
     * </p>
     *
     * <p>
     * For example, if this pattern matches every context in which
     * a process is being run by the user who owns file F, then if
     * the specified context has a currently-running-process being
     * run by that user, true will be returned.  If the
     * currently-running-process is run by another user, or if the
     * context does not even contain a currently-running process,
     * then false is returned.
     * </p>
     *
     * @param context The security context to match.  Must not be null.
     *
     * @return True if the specified security context matches, false if not.
     */
    public abstract boolean matches (
                                     SecurityContext context
                                     )
        throws ParametersMustNotBeNull.Violation;




    /** Matches any security context execpt ones matching a specific filter
     *  (logical NOT). */
    public static class Not
        implements SecurityContextPattern, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;


        /** Checks contracts for constructors and static methods. */
        private static final ObjectContracts classContracts =
            new ObjectContracts ( SecurityContextPattern.Not.class );

        /** The pattern to logically NOT. */
        private final SecurityContextPattern pattern;

        /** Creates a new logical Not of the specified SecurityContextPattern. */
        public Not (
                    SecurityContextPattern pattern
                    )
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   pattern );

            this.pattern = pattern;
        }

        /**
         * @see musaico.foundation.security.SecurityContextPattern#matches(musaico.foundation.security.SecurityContext)
         */
        @Override
        public boolean matches (
                                SecurityContext context
                                )
        {
            if ( this.pattern.matches ( context ) )
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (
                               Object object
                               )
        {
            if ( object == null )
            {
                return false;
            }
            else if ( this.getClass () != object.getClass () )
            {
                return false;
            }
            Not that = (Not) object;
            if ( this.pattern.equals ( that.pattern ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return this.pattern.hashCode ();
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return "not " + this.pattern;
        }
    }


    /** Matches any security context. */
    public static class Any
        implements SecurityContextPattern, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;


        /** Creates a new Any.  Use SecurityContext.ANY instead. */
        private Any ()
        {
        }

        /**
         * @see musaico.foundation.security.SecurityContextPattern#matches(musaico.foundation.security.SecurityContext)
         */
        @Override
        public boolean matches (
                                SecurityContext context
                                )
        {
            return true;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (
                               Object object
                               )
        {
            if ( object == this )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return 1;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return "any";
        }
    }


    /** Matches no security context. */
    public static class None
        implements SecurityContextPattern, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;


        /** Creates a new None.  Use SecurityContext.NONE instead. */
        private None ()
        {
        }

        /**
         * @see musaico.foundation.security.SecurityContextPattern#matches(musaico.foundation.security.SecurityContext)
         */
        @Override
        public boolean matches (
                                SecurityContext context
                                )
        {
            return false;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (
                               Object object
                               )
        {
            if ( object == this )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return 0;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return "none";
        }
    }
}
