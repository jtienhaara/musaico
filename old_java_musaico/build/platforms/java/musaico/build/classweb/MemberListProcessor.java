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
 * Takes a List of class Members as input, maybe does some side-effects,
 * then returns a (possibly modified) List of class Members as output.
 * </p>
 */
public interface MemberListProcessor
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Takes a List of class Members as input, maybe does some side-effects,
     * then returns a (possibly modified) List of class Members as output.
     * </p>
     */
    public abstract List<Member> process (
                                          List<Member> input
                                          );
}
