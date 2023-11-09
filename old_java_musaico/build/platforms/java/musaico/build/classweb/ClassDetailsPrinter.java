package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Prints out ClassDetails to a StringBuilder.
 * </p>
 */
public class ClassDetailsPrinter
    implements ClassDetailsListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The string builder to output to.
    private final StringBuilder out;

    /**
     * <p>
     * Creates a new ClassDetailsPrinter to output all class details
     * to the specified StringBuilder.
     * </p>
     */
    public ClassDetailsPrinter (
                                StringBuilder out
                                )
    {
        this.out = out;
    }


    public final String output ()
    {
        return this.out.toString ();
    }

    /**
     * @see ClassHierarchy.ClassDetailsListProcessor#process(java.util.List)
     */
    @Override
    public List<ClassDetails<?>> process (
                                          List<ClassDetails<?>> input
                                          )
    {
        for ( ClassDetails<?> detail : input )
        {
            print ( detail );
        }

        return input;
    }


    public <DETAIL extends Object>
        void print (
                    ClassDetails<DETAIL> detail
                    )
    {
        final ClassDetail<DETAIL> type = detail.type ();
        type.print ( detail,
                     this.out );
    }
}
