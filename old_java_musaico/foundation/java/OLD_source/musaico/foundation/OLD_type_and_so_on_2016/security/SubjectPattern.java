package musaico.foundation.security;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Matches a security context based only on the subject (Credentials)
 * matching some pattern, such as the subject matches "the user who
 * owns file F" and so on.
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
public class SubjectPattern
    implements SecurityContextPattern, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts for static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SubjectPattern.class );


    /** Checks contracts for methods for us. */
    private final ObjectContracts contracts;

    /** The pattern matcher for each SecurityContext's subject. */
    private final CredentialsPattern credentialsPattern;


    /**
     * <p>
     * Creates a new SubjectPattern which will match the subject of
     * each SecurityContext against the specified CredentialsPattern.
     * </p>
     *
     * @param subject_pattern The pattern to match each SecurityContext's
     *                        subject against.  Must not be null.
     */
    public SubjectPattern (
                           CredentialsPattern subject_pattern
                           )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               subject_pattern );

        this.credentialsPattern = subject_pattern;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.security.SecurityContextPattern#matches(musaico.foundation.security.SecurityContext)
     */
    @Override
    public boolean matches (
                            SecurityContext context
                            )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               context );

        boolean is_subject_matched =
            this.credentialsPattern.matches ( context.subject () );

        return is_subject_matched;
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
            // Any SubjectPattern != null.
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            // SubjectPatternA != Foo or SubjectPatternB.
            return false;
        }

        SubjectPattern that = (SubjectPattern) object;
        if ( this.credentialsPattern == null )
        {
            if ( that.credentialsPattern == null )
            {
                // null == null.
                return true;
            }
            else
            {
                // null != Any CredentialsPattern.
                return false;
            }
        }
        else if ( that.credentialsPattern == null )
        {
            // Any CredentialsPattern != null.
            return false;
        }

        return this.credentialsPattern.equals ( that.credentialsPattern );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.credentialsPattern.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "subject " + this.credentialsPattern;
    }
}
