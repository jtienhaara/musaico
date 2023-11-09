package musaico.foundation.filter.string;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Filters Strings which match a regular expression Pattern
 * exactly.
 * </p>
 *
 * @see java.util.regex.Pattern
 *
 *
 * <p>
 * *** Do not forget to add new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java String is the end of the road for class inheritance,
 * so there is no point in making String Filters generic.
 * </p>
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
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
 * @see musaico.foundation.filter.string.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.string.MODULE#LICENSE
 */
public class StringPattern
    implements Filter<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Pattern to match exactly.
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
               ? null // null Pattern
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
     *                Should not be null, but if it is, then
     *                everything will be matched with the pattern
     *                ".*".
     */
    public StringPattern (
            Pattern pattern
            )
    {
        if ( pattern == null )
        {
            this.pattern = Pattern.compile ( ".*" );
        }
        else
        {
            this.pattern = pattern;
        }
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
        else if ( this.pattern.matcher ( value ).find () )
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
        return 17 * ClassName.of ( this.getClass () ).hashCode ()
            + this.pattern.pattern ().hashCode ();
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
