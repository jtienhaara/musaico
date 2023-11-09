package musaico.build.classweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Filters out specific class details, such as the members,
 * the stereotype(s), the name, the package, and so on.
 * </p>
 */
public interface ClassDetailsFilter
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Does not filter anything out. */
    public static final ClassDetailsFilter NONE =
        new NoClassDetailsFilter ();

    /**
     * <p>
     * Filters the specified class detail.
     * </p>
     *
     * @param details The list of class details to filter.  Must not be null.
     *                Must not contain any null elements.
     *
     * @return True if the specified class detail
     *         passed through this filter;
     *         false if the specified class detail was filtered out.
     */
    public abstract boolean matches (
                                     List<ClassDetails<?>> class_details
                                     );
}
