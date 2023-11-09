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
 * Sorts Constructors more or less alphabetically (ASCii sort).
 * </p>
 */
@SuppressWarnings("rawtypes") // Because of MemberOrder<Constructor> later.
public class AlphaConstructorComparator
    implements Comparator<Constructor>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public int compare ( Constructor constructor1,
                         Constructor constructor2 )
    {
        if ( constructor1 == null )
        {
            if ( constructor2 == null )
            {
                // null == null
                return 0;
            }

            // null > constructor2
            return 1;
        }
        else if ( constructor2 == null )
        {
            // constructor1 < null
            return -1;
        }

        String name1 = constructor1.getName ();
        String name2 = constructor2.getName ();

        return name1.compareTo ( name2 );
    }
}
