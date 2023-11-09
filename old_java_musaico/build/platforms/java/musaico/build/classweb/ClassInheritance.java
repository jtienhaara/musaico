package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Returns the classes / interfaces from which each class/interface derives.
 * </p>
 */
public class ClassInheritance
    implements ClassDetail<Class<?>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ClassInheritance detail.
     * </p>
     */
    public ClassInheritance ()
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
        public final List<ClassDetails<Class<?>>> fromClass (
                Class<?> class_or_interface
                )
    {
        final List<Class<?>> all_inherits = new ArrayList<Class<?>> ();

        Class<?> superclass = class_or_interface.getSuperclass ();
        if ( superclass != null )
        {
            all_inherits.add ( superclass );
        }
        Class<?> [] interfaces = class_or_interface.getInterfaces ();

        for ( Class<?> in : interfaces )
        {
            all_inherits.add ( in );
        }

        final ClassDetails<Class<?>> details =
            new ClassDetails<Class<?>> ( class_or_interface,
                                         this,
                                         all_inherits );

        final List<ClassDetails<Class<?>>> sets =
            new ArrayList<ClassDetails<Class<?>>> ();
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
                             ClassDetails<Class<?>> inherits,
                             StringBuilder out
                             )
    {
        boolean is_first_interface = true;
        for ( Class<?> inherit : inherits )
        {
            if ( is_first_interface )
            {
                if ( ! inherit.isInterface () )
                {
                    out.append ( "    extends " );
                    out.append ( inherit.getName () );
                    out.append ( "\n" );
                    continue;
                }
                else
                {
                    out.append ( "    implements " );
                    is_first_interface = false;
                }
            }
            else
            {
                out.append ( ", " );
            }

            out.append ( inherit.getSimpleName () );
        }
    }

    // Ugly hack for @*%# generics.
    private static class SillyClass<CLASS_CLASS> // Class<?>
    {
        @SuppressWarnings("unchecked")
        public Class<CLASS_CLASS> getClassClass ()
        {
            return (Class<CLASS_CLASS>) Class.class;
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
