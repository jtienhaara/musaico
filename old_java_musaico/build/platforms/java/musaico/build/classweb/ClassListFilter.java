package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Removes all classes from each list that do not match a specified
 * class filter.
 * </p>
 */
public class ClassListFilter
    implements ClassListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The filter(s) which are ORed together.
    private final ClassFilter [] filters;


    /**
     * <p>
     * Creates a new ClassListFilter that will filter out any and all
     * classes which do not match any of the specified filters.
     * </p>
     */
    public ClassListFilter (
                            ClassFilter ... filters
                            )
    {
        this.filters = new ClassFilter [ filters.length ];
        System.arraycopy ( filters, 0,
                           this.filters, 0, filters.length );
    }


    /**
     * @see musaico.build.classweb.ClassListProcessor#process(java.util.List)
     */
    @Override
    public final List<Class<?>> process (
                                         List<Class<?>> input
                                         )
    {
        final List<Class<?>> output = new ArrayList<Class<?>> ();
        for ( Class<?> class_or_interface : input )
        {
            boolean is_matched = false;
            for ( ClassFilter filter : this.filters )
            {
                if ( filter.matches ( class_or_interface ) )
                {
                    is_matched = true;
                    break;
                }
            }

            if ( is_matched )
            {
                output.add ( class_or_interface );
            }
        }

        return output;
    }
}
