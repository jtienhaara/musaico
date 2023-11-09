package musaico.build.classweb;

import java.io.Serializable;

import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Returns plain Strings to be output as decoration,
 * such as spacing, line dividers, and so on.
 * </p>
 */
public class ClassDecoration
    implements ClassDetail<String>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The decorative text to return for every class.
    private final String text;


    /**
     * <p>
     * Creates a new ClassDecoration detail with the specified
     * decorative text.
     * </p>
     */
    public ClassDecoration (
                            String text
                            )
    {
        this.text = text;
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
        else
        {
            return false;
        }
    }

    /**
     * @see musaico.build.classweb.ClassDetail#fromClass(java.lang.Class)
     */
    @Override
    public final List<ClassDetails<String>> fromClass (
            Class<?> class_or_interface
            )
    {
        final List<String> all_decorations = new ArrayList<String> ();
        all_decorations.add ( this.text );

        final ClassDetails<String> details =
            new ClassDetails<String> ( class_or_interface,
                                       this,
                                       all_decorations );

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
        return this.text.hashCode ();
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
                             ClassDetails<String> decorations,
                             StringBuilder out
                             )
    {
        for ( String decoration : decorations )
        {
            out.append ( decoration );
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
