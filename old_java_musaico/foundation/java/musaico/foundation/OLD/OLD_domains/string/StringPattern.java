package musaico.foundation.domains.string;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The domain of all Strings which match a regular expression Pattern
 * exactly.
 * </p>
 *
 * @see java.util.regex.Pattern
 *
 *
 * <p>
 * *** Do not forget to add new domains to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.domains.string.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.string.MODULE#LICENSE
 */
public class StringPattern
    implements Domain<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The Pattern to match exactly. */
    private final Pattern pattern;


    /**
     * <p>
     * Creates a new StringPattern filter with the specified
     * regular expression String.
     * </p>
     *
     * <p>
     * Every String which matches the specified regular expression exactly
     * shall be KEPT, whereas any String which does not match the
     * regular expression exactly shall be DISCARDED.
     * </p>
     *
     * @param regular_expression The regular expression String
     *                           to match against.  Must be a valid,
     *                           compilable regular expression, according
     *                           to the rules specified by the Pattern class.
     *                           Must not be null.
     */
    public StringPattern (
                          String regular_expression
                          )
        throws NullPointerException
    {
        this ( regular_expression == null
               ? null // null Pattern, causes NullPointerException.
               : Pattern.compile ( regular_expression ) );
    }

    /**
     * <p>
     * Creates a new StringPattern filter with the specified
     * regular expression Pattern.
     * </p>
     *
     * <p>
     * Every String which matches the specified Pattern exactly
     * shall be KEPT, whereas any String which does not match the
     * Pattern exactly shall be DISCARDED.
     * </p>
     *
     * @param pattern The regular expression Pattern to match against.
     *                Must not be null.
     */
    public StringPattern (
                          Pattern pattern
                          )
        throws NullPointerException
    {
        this.pattern = pattern;

        this.pattern.hashCode (); // Throws NullPointerException.
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        final StringPattern that = (StringPattern) obj;
        if ( this.pattern == null )
        {
            if ( that.pattern != null )
            {
                return false;
            }
        }
        else if ( that.pattern == null )
        {
            return false;
        }
        else if ( ! this.pattern.equals ( that.pattern ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     String value
                                     )
    {
        if ( value == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( this.pattern.matcher ( value ).matches () )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 17 * this.getClass ().getName ().hashCode ()
            + this.pattern.pattern ().hashCode ();
    }


    /**
     * @see musaico.foundation.filter.Domain#member(java.lang.Object)
     */
    @Override
    public final List<String> member (
                                      String maybe_member
                                      )
    {
        final List<String> members = new ArrayList<String> ();
        if ( this.filter ( maybe_member ).isKept () )
        {
            members.add ( maybe_member );
        }

        return members;
    }


    /**
     * @see musaico.foundation.filter.Domain#nonMember(java.lang.Object)
     */
    @Override
    public final List<String> nonMember (
                                         String maybe_non_member
                                         )
    {
        final List<String> non_members = new ArrayList<String> ();
        // For each possible non-member, return a String
        // that shows the invalid characters.  For example,
        // if the pattern is "[0-9]+" (a numeric String)
        // and a String "a123b456" is passed in, then the non-numeric
        // characters will be shown in the non-member
        // output String: "a...b...".
        final int length = maybe_non_member.length ();
        final Matcher matcher = this.pattern.matcher ( maybe_non_member );
        final StringBuilder sbuf = new StringBuilder ();
        int previous = 0;
        while ( matcher.find ( previous ) )
        {
            final int next = matcher.regionStart ();
            final int end = matcher.regionEnd ();
            if ( next > previous )
            {
                final String disallowed_chunk =
                    maybe_non_member.substring ( previous, next );
                sbuf.append ( disallowed_chunk );
            }

            for ( int dot = next; dot <= end; dot ++ )
            {
                sbuf.append ( '.' );
            }

            previous = end + 1;
        }

        if ( previous == 0 )
        {
            // No pattern matches.
            // This string is a member of the domain.
        }
        else if ( previous >= 0
                  && previous < length )
        {
            final String disallowed_chunk =
                maybe_non_member.substring ( previous, length );
            sbuf.append ( disallowed_chunk );

            final String non_member = sbuf.toString ();
            non_members.add ( non_member );
        }

        return non_members;
    }


    /**
     * @return The regular expression Pattern to match against.
     *         Never null.
     */
    public Pattern pattern ()
    {
        return this.pattern;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this )
            + " /" + this.pattern.pattern () + "/";
    }
}
