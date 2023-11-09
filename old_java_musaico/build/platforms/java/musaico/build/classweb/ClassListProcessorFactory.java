package musaico.build.classweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Creates a ClassListProcessor with some class details list processors
 * to pass on the extracted data.
 * </p>
 */
public interface ClassListProcessorFactory
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates a new ClassListProcessor with the specified
     * child class details list processors to filter and sort the name,
     * package, stereotype, members and so on of each class.
     * </p>
     */
    public abstract ClassListProcessor create (
                                               ClassDetailsListProcessor [] class_details_list_processors
                                               );
}
