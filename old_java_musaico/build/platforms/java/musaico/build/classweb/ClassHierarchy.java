package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Expands the input list to include all inherited interfaces and
 * classes.
 * </p>
 */
public class ClassHierarchy
    implements ClassListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see musaico.build.classweb.ClassListProcessor#process(java.util.List)
     */
    @Override
    public final List<Class<?>> process (
                                         List<Class<?>> input
                                         )
    {
        List<Class<?>> unplumbed = new ArrayList<Class<?>> ();
        unplumbed.addAll ( input );

        List<Class<?>> plumbed = new ArrayList<Class<?>> ();

        while ( unplumbed.size () > 0 )
        {
            Class<?> curr_class = unplumbed.remove ( 0 );

            Class<?> [] declared_classes = curr_class.getDeclaredClasses ();
            for ( Class<?> declared_class : declared_classes )
            {
                if ( ! plumbed.contains ( declared_class )
                     && ! unplumbed.contains ( declared_class ) )
                {
                    unplumbed.add ( declared_class );
                }
            }

            Class<?> [] interfaces = curr_class.getInterfaces ();
            for ( int in = 0; in < interfaces.length; in ++ )
            {
                if ( ! plumbed.contains ( interfaces [ in ] )
                     && ! unplumbed.contains ( interfaces [ in ] ) )
                {
                    unplumbed.add ( interfaces [ in ] );
                }
            }

            Class<?> superclass = curr_class.getSuperclass ();
            if ( superclass != null
                 && ! plumbed.contains ( superclass )
                 && ! unplumbed.contains ( superclass ) )
            {
                unplumbed.add ( superclass );
            }

            plumbed.add ( curr_class );
        }

        return plumbed;
    }
}
