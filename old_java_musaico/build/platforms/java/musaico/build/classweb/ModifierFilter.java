package musaico.build.classweb;

import java.io.Serializable;

import java.lang.annotation.Annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Filters out classes/interfaces and/or class Members which
 * do not have a specific Modifier or set of Modifiers
 * (PUBLIC, PRIVATE, STATIC and so on).
 * </p>
 */
public class ModifierFilter
    implements ClassFilter, MemberFilter, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The modifier(s) which must be present, or the class/interface/Member
     * is filtered out. */
    private final int modifiers;

    /**
     * <p>
     * Creates a new ModifierFilter to discard every class/interface/Member
     * which does not have the specified modifier(s).
     * </p>
     *
     * @param modifier The modifier(s) to look for, logically
     *                 OR'ed together.
     */
    public ModifierFilter (
                           int modifiers
                           )
    {
        this.modifiers = modifiers;
    }

    /**
     * See ClassHierarchy.ClassFilter#matches(java.lang.Class)
     */
    @Override
    public boolean matches (
                            Class<?> class_or_interface
                            )
    {
        int actual_modifiers = class_or_interface.getModifiers ();
        if ( ( actual_modifiers & this.modifiers ) == this.modifiers )
        {
            // Class/interface has all the right modifiers.
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * See ClassHierarchy.MemberFilter#matches(java.lang.reflect.Member)
     */
    @Override
    public boolean matches (
                            Member member
                            )
    {
        int actual_modifiers = member.getModifiers ();
        if ( ( actual_modifiers & this.modifiers ) == this.modifiers )
        {
            // Member has all the right modifiers.
            return true;
        }
        else
        {
            return false;
        }
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

        ModifierFilter that = (ModifierFilter) object;

        if ( this.modifiers == that.modifiers )
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
    public final int hashCode ()
    {
	return 31 * this.getClass ().getName ().hashCode ()
	    + this.modifiers;
    }
}
