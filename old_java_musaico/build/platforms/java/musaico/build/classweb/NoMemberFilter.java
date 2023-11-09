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
 * Allows all class Members to pass through, filtering out nothing.
 * </p>
 */
public class NoMemberFilter
    implements MemberFilter, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoMemberFilter to not filter out anything.
     * Use MemberFilter.NONE instead.
     * </p>
     */
    // package private
    NoMemberFilter ()
    {
    }

    /**
     * See ClassHierarchy.MemberFilter#matches(java.lang.reflect.Member)
     */
    @Override
    public final boolean matches (
                                  Member member
                                  )
    {
        return true;
    }
}
