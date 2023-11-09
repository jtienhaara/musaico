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
 * Sorts Methods more or less alphabetically (ASCii sort).
 * </p>
 */
public class AlphaMethodComparator
    implements Comparator<Method>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public int compare ( Method method1, Method method2 )
    {
        if ( method1 == null )
        {
            if ( method2 == null )
            {
                // null == null
                return 0;
            }

            // null > method2
            return 1;
        }
        else if ( method2 == null )
        {
            // method1 < null
            return -1;
        }

        String name1 = method1.getName ();
        String name2 = method2.getName ();

        return name1.compareTo ( name2 );
    }
}
