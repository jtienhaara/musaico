package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Returns the package parent of each class.
 * </p>
 */
public class ClassPackage
    implements ClassDetail<Package>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ClassPackage detail.
     * </p>
     */
    public ClassPackage ()
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
    public final List<ClassDetails<Package>> fromClass (
            Class<?> class_or_interface
            )
    {
        final List<Package> all_packages = new ArrayList<Package> ();

        Package package_of_class = class_or_interface.getPackage ();
        if ( package_of_class != null )
        {
            all_packages.add ( package_of_class );
        }

        final ClassDetails<Package> details =
            new ClassDetails<Package> ( class_or_interface,
                                        this,
                                        all_packages );

        final List<ClassDetails<Package>> sets =
            new ArrayList<ClassDetails<Package>> ();
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
                             ClassDetails<Package> packages,
                             StringBuilder out
                             )
    {
        boolean is_first = true;
        for ( Package package_of_class : packages )
        {
            if ( is_first )
            {
                out.append ( "[ package " );
                is_first = false;
            }
            else
            {
                out.append ( ", " );
            }

            out.append ( package_of_class.getName () );
        }

        if ( ! is_first )
        {
            out.append ( " ]" );
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
            return (ClassDetail<?>) replacement;
        }
        else
        {
            return this;
        }
    }
}
