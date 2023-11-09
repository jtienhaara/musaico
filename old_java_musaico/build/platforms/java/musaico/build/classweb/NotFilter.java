package musaico.build.classweb;

import java.io.Serializable;

import java.lang.reflect.Member;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Filters out classes/interfaces and/or class Members which
 * do match another filter.
 * </p>
 */
public class NotFilter
    implements ClassFilter, MemberFilter, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The class filter to negate (if any - can be null
    // if member filter is set).
    private final ClassFilter classFilter;

    // The member filter to negate (if any - can be null
    // if class filter is set).
    private final MemberFilter memberFilter;

    /**
     * <p>
     * Creates a new NotFilter to filter out classes matching the specified
     * ClassFilter.
     * </p>
     *
     * @param class_filter The class filter to negate.  Must not be null.
     */
    public NotFilter (
                      ClassFilter class_filter
                      )
    {
        this ( class_filter,
               null ); // member_filter
    }

    /**
     * <p>
     * Creates a new NotFilter to filter out members matching the specified
     * MemberFilter.
     * </p>
     *
     * @param member_filter The member filter to negate.  Must not be null.
     */
    public NotFilter (
                      MemberFilter member_filter
                      )
    {
        this ( null, // class_filter,
               member_filter );
    }

    /**
     * <p>
     * Creates a new NotFilter to filter out classes matching the
     * optional specified ClassFilter and / or members matching
     * the optional specified MemberFilter.
     * </p>
     *
     * @param class_filter The class filter to negate.
     *                     Can be null as long as member_filter is not null.
     *
     * @param member_filter The member filter to negate.
     *                     Can be null as long as class_filter is not null.
     */
    public NotFilter (
                      ClassFilter class_filter,
                      MemberFilter member_filter
                      )
    {
        this.classFilter = class_filter;
        this.memberFilter = member_filter;

        if ( this.classFilter == null
             && this.memberFilter == null )
        {
            throw new NullPointerException ( "Cannot create a NotFilter with null class filter and null member filter" );
        }
    }

    /**
     * See ClassHierarchy.ClassFilter#matches(java.lang.Class)
     */
    @Override
    public boolean matches (
                            Class<?> class_or_interface
                            )
    {
        return ! this.classFilter.matches ( class_or_interface );
    }

    /**
     * See ClassHierarchy.MemberFilter#matches(java.lang.reflect.Member)
     */
    @Override
    public boolean matches (
                            Member member
                            )
    {
        return ! this.memberFilter.matches ( member );
    }

    /**
     * @see java.lang.Object.equals(java.lang.Object)
     */
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( this.getClass ().equals ( object.getClass () ) )
        {
            return false;
        }

        NotFilter that = (NotFilter) object;

        if ( this.classFilter == null )
        {
            if ( that.classFilter != null )
            {
                return false;
            }
        }
        else if ( that.classFilter == null )
        {
            return false;
        }

        if ( this.memberFilter == null )
        {
            if ( that.memberFilter != null )
            {
                return false;
            }
        }
        else if ( that.memberFilter == null )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
	return 31 * this.getClass ().getName ().hashCode ()
	    + 17 * ( this.classFilter == null ? 0 : this.classFilter.hashCode () )
	    + ( this.memberFilter == null ? 0 : this.memberFilter.hashCode () );
    }
}
