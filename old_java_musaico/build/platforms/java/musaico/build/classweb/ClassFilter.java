package musaico.build.classweb;

import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Filters out specific classes/interfaces from the output.
 * </p>
 */
public interface ClassFilter
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Does not filter anything out. */
    public static final ClassFilter NONE =
        new NoClassFilter ();

    /** Filters out everything but abstract classes/interfaces. */
    public static final ClassFilter ABSTRACT =
        new ModifierFilter ( Modifier.ABSTRACT );

    /** Filters out everything but concrete (not abstract) classes. */
    public static final ClassFilter CONCRETE =
        new NotFilter ( new ModifierFilter ( Modifier.ABSTRACT
                                             | Modifier.INTERFACE ),
                        null ); // modifier_filter

    /** Filters out everything but interfaces. */
    public static final ClassFilter INTERFACE =
        new ModifierFilter ( Modifier.INTERFACE );

    /**
     * <p>
     * Filters the specified class / interface.
     * </p>
     *
     * @param class_or_interface The class / interface to filter.
     *                           Must not be null.
     *
     * @return True if the specified class / interface
     *         passed through this filter;
     *         false if the specified class was filtered out.
     */
    public abstract boolean matches (
                                     Class<?> class_or_interface
                                     );
}
