package musaico.build.classweb;

import java.lang.annotation.Annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Takes a List of class details, such as name, package, members,
 * and so on, maybe does some side-effects, then returns a
 * (possibly modified) List of class details as output.
 * </p>
 */
public interface ClassDetailsListProcessor
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Takes a List of class details, such as name, package, members,
     * and so on, maybe does some side-effects, then returns a
     * (possibly modified) List of class details as output.
     * </p>
     */
    public abstract List<ClassDetails<?>> process (
                                                   List<ClassDetails<?>> details
                                                   );
}
