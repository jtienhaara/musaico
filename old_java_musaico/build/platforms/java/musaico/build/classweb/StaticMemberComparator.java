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
 * Sorts static members before other members.
 * </p>
 */
public class StaticMemberComparator<MEMBER extends Member>
    implements Comparator<MEMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public int compare ( MEMBER member1, MEMBER member2 )
    {
        if ( member1 == null )
        {
            if ( member2 == null )
            {
                // null == null
                return 0;
            }

            // null > member2
            return 1;
        }
        else if ( member2 == null )
        {
            // member1 < null
            return -1;
        }

        int modifiers1 = member1.getModifiers ();
        int modifiers2 = member2.getModifiers ();

        if ( ( modifiers1 & Modifier.STATIC ) != 0 )
        {
            if ( ( modifiers2 & Modifier.STATIC ) != 0 )
            {
                // static member == static member
                return 0;
            }
            else
            {
                // static member < non-static member
                return -1;
            }
        }
        else if ( ( modifiers2 & Modifier.STATIC ) != 0 )
        {
            // non-static member > static member
            return 1;
        }

        // non-static member == non-static member
        return 0;
    }
}
