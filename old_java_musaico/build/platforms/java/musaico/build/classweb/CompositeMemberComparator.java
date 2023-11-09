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
 * Composite orders: first use one comparator, if it returns 2 members
 *  are equal then use the next comparator, and so on.
 * </p>
 */
public class CompositeMemberComparator<MEMBER extends Member>
    implements Comparator<MEMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The comparators to use, in order. */
    private final Comparator<MEMBER> [] comparators;

    /**
     * <p>
     * Creates a new composite comparator from the specified ones.
     * Each one will be applied in order until one returns a
     * non-equal (non-zero) result when comparing 2 members.
     * </p>
     */
    public CompositeMemberComparator (
                                      Comparator<MEMBER> [] comparators
                                      )
    {
        this.comparators = comparators;
    }

    public int compare ( MEMBER member1, MEMBER member2 )
    {
        for ( Comparator<MEMBER> comparator : this.comparators )
        {
            int comparison = comparator.compare ( member1, member2 );
            if ( comparison != 0 )
            {
                return comparison;
            }
        }

        // No comparator spotted any differences.  The members are
        // comparatively equivalent.
        return 0;
    }
}
