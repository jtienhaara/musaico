package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Returns the enumerated values of an enum class.
 * </p>
 */
public class ClassEnumeratedValues
    implements ClassDetail<Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ClassEnumeratedValues detail.
     * </p>
     */
    public ClassEnumeratedValues ()
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
    public final List<ClassDetails<Object>> fromClass (
            Class<?> class_or_interface
            )
    {
        Object [] enumerated_values = class_or_interface.getEnumConstants ();
        final List<Object> enumerated_values_list =
            new ArrayList<Object> ();
        if ( enumerated_values != null
             && enumerated_values.length > 0 )
        {
            for ( Object enumerated_value : enumerated_values )
            {
                enumerated_values_list.add ( enumerated_value );
            }
        }

        final ClassDetails<Object> details =
            new ClassDetails<Object> ( class_or_interface,
                                       this,
                                       enumerated_values_list );

        final List<ClassDetails<Object>> sets =
            new ArrayList<ClassDetails<Object>> ();
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
                             ClassDetails<Object> enumerated_values,
                             StringBuilder out
                             )
    {
        boolean is_first = true;
        for ( Object enumerated_value : enumerated_values )
        {
            if ( is_first )
            {
                out.append ( " = {" );
                is_first = false;
            }
            else
            {
                out.append ( "," );
            }

            out.append ( " " + enumerated_value );
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
