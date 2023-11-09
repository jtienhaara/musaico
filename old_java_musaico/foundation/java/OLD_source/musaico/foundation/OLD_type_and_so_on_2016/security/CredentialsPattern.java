package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A pattern to match the Credentials in a SecurityContext, such as
 * "only match the user who owns file F".
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
public interface CredentialsPattern
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Matches any and all Credentials. */
    public static final CredentialsPattern.Any ANY = new Any ();

    /** Never matches any Credentials at all. */
    public static final CredentialsPattern.None NONE = new None ();


    /**
     * <p>
     * Returns true if the specified Credentials match this pattern,
     * false if not.
     * </p>
     *
     * <p>
     * For example, if this is a "match the user who owns file F"
     * pattern and the specified Credentials is that same user, then
     * true will be returned.  If the specified Credentials is another
     * user, or another type of Credentials entirely, then false
     * is returned.
     * </p>
     *
     * @param credentials The credentials to match.  Must not be null.
     *
     * @return True if the specified credentials match, false if not.
     */
    public abstract boolean matches (
                                     Credentials credentials
                                     )
        throws ParametersMustNotBeNull.Violation;




    /** Matches any credentials execpt ones matching a specific filter
     *  (logical NOT). */
    public static class Not
        implements CredentialsPattern, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;


        /** Checks contracts for constructors and static methods. */
        private static final ObjectContracts classContracts =
            new ObjectContracts ( CredentialsPattern.Not.class );

        /** The pattern to logically NOT. */
        private final CredentialsPattern pattern;

        /** Creates a new logical Not of the specified CredentialsPattern. */
        public Not (
                    CredentialsPattern pattern
                    )
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   pattern );

            this.pattern = pattern;
        }

        /**
         * @see musaico.foundation.security.CredentialsPattern#matches(musaico.foundation.security.Credentials)
         */
        @Override
        public boolean matches (
                                Credentials credentials
                                )
        {
            if ( this.pattern.matches ( credentials ) )
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


    /** Matches any credentials. */
    public static class Any
        implements CredentialsPattern, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;


        /** Creates a new Any.  Use Credentials.ANY instead. */
        private Any ()
        {
        }

        /**
         * @see musaico.foundation.security.CredentialsPattern#matches(musaico.foundation.security.Credentials)
         */
        @Override
        public boolean matches (
                                Credentials credentials
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


    /** Matches no credentials. */
    public static class None
        implements CredentialsPattern, Serializable
    {
        /** The MusaicoModule to which this class or interface belongs. */
        public static final MODULE PARENT_MODULE = MODULE.MODULE;

        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;


        /** Creates a new None.  Use Credentials.NONE instead. */
        private None ()
        {
        }

        /**
         * @see musaico.foundation.security.CredentialsPattern#matches(musaico.foundation.security.Credentials)
         */
        @Override
        public boolean matches (
                                Credentials credentials
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
