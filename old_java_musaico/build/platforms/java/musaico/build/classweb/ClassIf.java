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
 * Whenever a condition returns objects, the conditional detail also
 * returns objects; otherwise the "else" detail returns objects.
 * </p>
 */
public class ClassIf
    implements ClassDetail<Object>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The "if" detail.  If this one returns objects, so does the
    // "then" detail.  Otherwise the "else" detail returns objects.
    private final ClassDetail<?> ifDetail;

    // The "then" detail.  If the "if" detail returns objects, so does
    // this one.
    private final ClassDetail<?> thenDetail;

    // The "else" detail.  If the "if" detail does not return
    // any objects, then this one does.
    private final ClassDetail<?> elseDetail;


    /**
     * <p>
     * Creates a new ClassIf detail with the specified
     * "if", "then" and "else" details.
     * </p>
     *
     * @param if_detail The "if" detail.  If this one returns objects,
     *                  so does the "then" detail.  Otherwise
     *                  the "else" detail returns objects.
     *                  Must not be null.
     *
     * @param then_detail The "then" detail.  If the "if" detail returns
     *                    objects, so does this one.  Can be null.
     *
     * @param else_detail The "else" detail.  If the "if" detail does
     *                    not return any objects, then this one does.
     *                    Can be null.
     */
    public ClassIf (
                    ClassDetail<?> if_detail,
                    ClassDetail<?> then_detail,
                    ClassDetail<?> else_detail
                    )
    {
        this.ifDetail = if_detail;
        this.thenDetail = then_detail;
        this.elseDetail = else_detail;
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

        final ClassIf that = (ClassIf) object;

        if ( this.ifDetail == null )
        {
            if ( that.ifDetail != null )
            {
                return false;
            }
        }
        else if ( that.ifDetail == null )
        {
            return false;
        }
        else if ( ! this.ifDetail.equals ( that.ifDetail ) )
        {
            return false;
        }

        if ( this.thenDetail == null )
        {
            if ( that.thenDetail != null )
            {
                return false;
            }
        }
        else if ( that.thenDetail == null )
        {
            return false;
        }
        else if ( ! this.thenDetail.equals ( that.thenDetail ) )
        {
            return false;
        }

        if ( this.elseDetail == null )
        {
            if ( that.elseDetail != null )
            {
                return false;
            }
        }
        else if ( that.elseDetail == null )
        {
            return false;
        }
        else if ( ! this.elseDetail.equals ( that.elseDetail ) )
        {
            return false;
        }

        return true;
    }

    /**
     * @see musaico.build.classweb.ClassDetail#fromClass(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast CD<?> to CD<Object>.
    public final List<ClassDetails<Object>> fromClass (
            Class<?> class_or_interface
            )
    {

        final List<ClassDetails<Object>> sets =
            new ArrayList<ClassDetails<Object>> ();

        final List<Object> if_objects = new ArrayList<Object> ();
        for ( ClassDetails<?> if_details : this.ifDetail.fromClass ( class_or_interface ) )
        {
            for ( Object object : if_details )
            {
                if_objects.add ( object );
            }
        }
        sets.add ( new ClassDetails<Object> (
                       class_or_interface,
                       (ClassDetail<Object>) this.ifDetail,
                       if_objects ) );

        if ( if_objects.size () > 0 )
        {
            if ( this.thenDetail != null )
            {
                final List<Object> then_objects = new ArrayList<Object> ();
                for ( ClassDetails<?> then_details :
                          this.thenDetail.fromClass ( class_or_interface ) )
                {
                    for ( Object object : then_details )
                    {
                        then_objects.add ( object );
                    }
                }

                sets.add ( new ClassDetails<Object> (
                               class_or_interface,
                               (ClassDetail<Object>) this.thenDetail,
                               then_objects ) );
            }
        }
        else
        {
            if ( this.elseDetail != null )
            {
                final List<Object> else_objects = new ArrayList<Object> ();
                for ( ClassDetails<?> else_details :
                          this.elseDetail.fromClass ( class_or_interface ) )
                {
                    for ( Object object : else_details )
                    {
                        else_objects.add ( object );
                    }
                }

                sets.add ( new ClassDetails<Object> (
                               class_or_interface,
                               (ClassDetail<Object>) this.elseDetail,
                               else_objects ) );
            }
        }

        return sets;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public final int hashCode ()
    {
        return
            31 * this.ifDetail.hashCode ()
            + 17 * ( this.thenDetail == null
                         ? 0 
                         : this.thenDetail.hashCode () )
            + ( this.elseDetail == null
                    ? 0
                    : this.elseDetail.hashCode () );
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
        else if ( this.ifDetail.isOrHas ( that ) )
        {
            return true;
        }
        else if ( this.thenDetail != null
                  && this.thenDetail.isOrHas ( that ) )
        {
            return true;
        }
        else if ( this.elseDetail != null
                  && this.elseDetail.isOrHas ( that ) )
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
    @SuppressWarnings("unchecked") // Cast if/then/else to ClassDetail<Obj>.
    public final void print (
                             ClassDetails<Object> details,
                             StringBuilder out
                             )
    {
        if ( details.type () == this.ifDetail )
        {
            ( (ClassDetail<Object>) this.ifDetail ).print ( details, out );
        }
        else if ( this.thenDetail != null
                  && details.type () == this.thenDetail )
        {
            ( (ClassDetail<Object>) this.thenDetail ).print ( details, out );
        }
        else if ( this.elseDetail != null
                  && details.type () == this.elseDetail )
        {
            ( (ClassDetail<Object>) this.elseDetail ).print ( details, out );
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
        final ClassDetail<?> new_if_detail =
            this.ifDetail.replaceAll ( replaced, replacement );

        final ClassDetail<?> new_then_detail;
        if ( this.thenDetail == null )
        {
            new_then_detail = null;
        }
        else
        {
            new_then_detail =
                this.thenDetail.replaceAll ( replaced, replacement );
        }

        final ClassDetail<?> new_else_detail;
        if ( this.elseDetail == null )
        {
            new_else_detail = null;
        }
        else
        {
            new_else_detail =
                this.elseDetail.replaceAll ( replaced, replacement );
        }

        if ( new_if_detail == this.ifDetail
             && new_then_detail == this.thenDetail
             && new_else_detail == this.elseDetail )
        {
            return this;
        }

        final ClassIf new_if = new ClassIf ( new_if_detail,
                                             new_then_detail,
                                             new_else_detail );

        return new_if;
    }
}
