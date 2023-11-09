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
 * Allows all class details to pass through, filtering out nothing.
 * </p>
 */
public class NoClassDetailsFilter
    implements ClassDetailsFilter, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoClassDetailsFilter to not filter out anything.
     * Use ClassDetailsFilter.NONE instead.
     * </p>
     */
    // package private
    NoClassDetailsFilter ()
    {
    }

    /**
     * See ClassHierarchy.ClassDetailsFilter#matches(musaico.build.classweb.ClassDetail,java.lang.Object)
     */
    @Override
    public final boolean matches (
                                  List<ClassDetails<?>> class_details
                                  )
    {
        return true;
    }
}
