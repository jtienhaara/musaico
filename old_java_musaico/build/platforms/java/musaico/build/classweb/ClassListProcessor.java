package musaico.build.classweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Takes a List of classes and interfaces as input, maybe does
 * some side-effects, then returns a (possibly modified)
 * List of classes and interfaces as output.
 * </p>
 */
public interface ClassListProcessor
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Takes a List of classes and interfaces as input, maybe does
     * some side-effects, then returns a (possibly modified)
     * List of classes and interfaces as output.
     * </p>
     */
    public abstract List<Class<?>> process (
                                            List<Class<?>> input
                                            );
}
