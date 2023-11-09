package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Allows all classes and interfaces to pass through, filtering out nothing.
 * </p>
 */
public class NoClassFilter
    implements ClassFilter, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new NoClassFilter to not filter out anything.
     * Use ClassFilter.NONE instead.
     * </p>
     */
    // package private
    NoClassFilter ()
    {
    }

    /**
     * See ClassHierarchy.ClassFilter#matches(java.lang.Class)
     */
    @Override
    public final boolean matches (
                                  Class<?> class_or_interface
                                  )
    {
        return true;
    }
}
