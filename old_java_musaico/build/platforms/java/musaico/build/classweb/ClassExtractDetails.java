package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Extracts all ClassDetails from each Class, and passes them
 * to a specific ClassDetailsListProcessor for printing or other
 * operations.
 * </p>
 */
public class ClassExtractDetails
    implements ClassListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a ClassExtractDetails which then filters, sorts and so on
     * the class details for each class using specific
     * ClassDetailsListProcessors.
     * </p>
     */
    public static final ClassListProcessorFactory FACTORY =
        new ClassListProcessorFactory ()
        {
            /**
             * @see musaico.build.classweb.ClassListProcessorFactory#create(musaico.build.classweb.ClassDetailsListProcessor[])
             */
            @Override
            public final ClassListProcessor create (
                                                    ClassDetailsListProcessor [] details_processors
                                                    )
            {
                return new ClassExtractDetails ( details_processors );
            }
        };




    // Prints or otherwise operates on the ClassDetails of each class.
    private final ClassDetailsListProcessor [] classDetailsListProcessors;


    /**
     * <p>
     * Creates a new ClassExtractDetails that will take a list of classes,
     * and for each one, extract all details and pass them through to
     * the specified ClassDetailsListProcessors for further processing.
     * </p>
     */
    public ClassExtractDetails (
                                ClassDetailsListProcessor [] class_details_list_processors
                                )
    {
        this.classDetailsListProcessors =
            new ClassDetailsListProcessor [ class_details_list_processors.length ];
        System.arraycopy ( class_details_list_processors, 0,
                           this.classDetailsListProcessors, 0, class_details_list_processors.length );
    }


    /**
     * @see musaico.build.classweb.ClassListProcessor#process(java.util.List)
     */
    @Override
    public List<Class<?>> process (
                                   List<Class<?>> input
                                   )
    {
        for ( Class<?> class_or_interface : input )
        {
            this.processDetails ( class_or_interface );
        }

        return input;
    }


    public final List<ClassDetails<?>> processDetails (
                                                       Class<?> class_or_interface
                                                       )
    {
        List<ClassDetails<?>> output =
            new ArrayList<ClassDetails<?>> ();

        for ( ClassDetail<?> type : ClassDetail.ALL )
        {
            for ( ClassDetails<?> detail
                      : type.fromClass ( class_or_interface ) )
            {
                output.add ( detail );
            }
        }

        for ( ClassDetailsListProcessor details_processor
                  : this.classDetailsListProcessors )
        {
            output = details_processor.process ( output );
        }

        return output;
    }
}
