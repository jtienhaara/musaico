package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Prints out specified details of each class, such as its package,
 * whether it is a class or interface, the enumerated values
 * if it is an enum, and so on.
 * </p>
 */
public interface ClassDetailPrinter<DETAIL extends Object>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Prints the specified details from the specified class to
     * the specified string builder.
     * </p>
     */
    public abstract void print (
                                ClassDetails<DETAIL> class_details,
                                StringBuilder out
                                );
}
