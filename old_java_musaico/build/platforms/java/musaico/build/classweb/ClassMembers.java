package musaico.build.classweb;

import java.io.Serializable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * The members of each class, including its constants, constructors,
 * methods, and so on.
 * </p>
 */
public class ClassMembers
    implements ClassDetail<Member>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Filters out members and sorts them.
    private final Order memberOrder;


    /**
     * <p>
     * Creates a new ClassMembers detail with the specified filters
     * and sort order of the members of each class.
     * </p>
     *
     * @param member_order The filters and sort order of the members
     *                     of each class.  Must not be null.
     */
    public ClassMembers (
                         Order member_order
                         )
    {
        this.memberOrder = member_order;
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

        // Equal even if the sort order / filters are different.
        return true;
    }

    /**
     * @see musaico.build.classweb.ClassDetail#fromClass(java.lang.Class)
     */
    @Override
    public final List<ClassDetails<Member>> fromClass (
            Class<?> class_or_interface
            )
    {
        final List<Member> all_members = new ArrayList<Member> ();

        // Constants
        for ( Field field : class_or_interface.getDeclaredFields () )
        {
            all_members.add ( field );
        }
        for ( Constructor<?> constructor : class_or_interface.getConstructors () )
        {
            all_members.add ( constructor );
        }
        for ( Method method : class_or_interface.getDeclaredMethods () )
        {
            all_members.add ( method );
        }

        List<Member> ordered_members =
            this.memberOrder.process ( all_members );

        final ClassDetails<Member> details =
            new ClassDetails<Member> ( class_or_interface,
                                       this,
                                       ordered_members );

        final List<ClassDetails<Member>> sets =
            new ArrayList<ClassDetails<Member>> ();
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
                             ClassDetails<Member> members,
                             StringBuilder out
                             )
    {
        final MemberPrinter member_printer = new MemberPrinter ( out );
        member_printer.process ( members.details () );
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
