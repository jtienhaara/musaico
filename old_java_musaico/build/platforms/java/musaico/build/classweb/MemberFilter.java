package musaico.build.classweb;

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
 * Filters out specific Members from the output.
 * </p>
 */
public interface MemberFilter
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Does not filter anything out. */
    public static final MemberFilter NONE =
        new NoMemberFilter ();

    /** Filters out everything but public members. */
    public static final MemberFilter PUBLIC =
        new ModifierFilter ( Modifier.PUBLIC );

    /** Filters out everything but protected members. */
    public static final MemberFilter PROTECTED =
        new ModifierFilter ( Modifier.PROTECTED );

    /** Filters out everything but private members. */
    public static final MemberFilter PRIVATE =
        new ModifierFilter ( Modifier.PRIVATE );

    /** Filters out everything except public static final members. */
    public static final MemberFilter PUBLIC_CONSTANT =
        new ModifierFilter ( Modifier.FINAL
                             | Modifier.PUBLIC
                             | Modifier.STATIC );

    /** Filters out everything except protected static final members. */
    public static final MemberFilter PROTECTED_CONSTANT =
        new ModifierFilter ( Modifier.FINAL
                             | Modifier.PROTECTED
                             | Modifier.STATIC );

    /** Filters out everything except private static final members. */
    public static final MemberFilter PRIVATE_CONSTANT =
        new ModifierFilter ( Modifier.FINAL
                             | Modifier.PRIVATE
                             | Modifier.STATIC );

    /**
     * <p>
     * Filters the specified class Member.
     * </p>
     *
     * @param member The member of the class to filter.
     *               Must not be null.
     *
     * @return True if the specified member passed through this filter;
     *         false if the specified member was filtered out.
     */
    public abstract boolean matches (
                                     Member member
                                     );
}
