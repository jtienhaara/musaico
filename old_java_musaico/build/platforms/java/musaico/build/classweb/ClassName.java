package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Returns the name of each class.
 * </p>
 */
public class ClassName
    implements ClassDetail<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ClassName detail.
     * </p>
     */
    public ClassName ()
    {
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( ! this.getClass ().equals ( object.getClass () ) )
        {
            return false;
        }

        return true;
    }

    /**
     * @see musaico.build.classweb.ClassDetail#fromClass(java.lang.Class)
     */
    @Override
    public final List<ClassDetails<String>> fromClass (
            Class<?> class_or_interface
            )
    {
        final List<String> all_names = new ArrayList<String> ();
        String class_name = class_or_interface.getSimpleName ();
        final String name;
        Class<?> parent_class = class_or_interface.getDeclaringClass ();
        if ( parent_class == null )
        {
            name = class_name;
        }
        else
        {
            name = parent_class.getSimpleName () + "$" + class_name;
        }
        all_names.add ( name );

        final ClassDetails<String> details =
            new ClassDetails<String> ( class_or_interface,
                                       this,
                                       all_names );

        final List<ClassDetails<String>> sets =
            new ArrayList<ClassDetails<String>> ();
        sets.add ( details );

        return sets;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }

    /**
     * @see musaico.build.classweb.ClassDetail#isOrHas(musaico.build.classweb.ClassDetail)
     */
    public final boolean isOrHas (
                                  ClassDetail<?> that
                                  )
    {
        if ( this.equals ( that ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see musaico.build.classweb.ClassDetailPrinter#print(java.lang.Class,musaico.build.classweb.ClassDetail,java.util.List,java.lang.StringBuilder)
     */
    @Override
    public final void print (
                             ClassDetails<String> names,
                             StringBuilder out
                             )
    {
        boolean is_first = true;
        for ( String name : names )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                out.append ( ", " );
            }

            out.append ( name );
        }
    }

    /**
     * @see musaico.build.classweb.ClassDetail#replaceAll(musaico.build.classweb.ClassDetail,musaico.build.classweb.ClassDetail)
     */
    @Override
    public final ClassDetail<?> replaceAll (
                                            ClassDetail<?> replaced,
                                            ClassDetail<?> replacement
                                            )
    {
        if ( this.equals ( replaced ) )
        {
            return replacement;
        }
        else
        {
            return this;
        }
    }
}
