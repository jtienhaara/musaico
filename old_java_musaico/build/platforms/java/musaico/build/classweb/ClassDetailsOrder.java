package musaico.build.classweb;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * <p>
 * Specifies the overall order, filters and individual sort orders
 * for printing out the details of one class.
 * </p>
 */
public class ClassDetailsOrder
    implements ClassDetailsListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The default overall order: stereotype, name, space decoration,
     *  package, enumerated values, line divider, inheritance, members. */
    public static final ClassDetailsOrder DEFAULT =
        new ClassDetailsOrder ( new ClassIf (
                                    ClassDetail.STEREOTYPE,
                                    new ClassDecoration ( "\n" ),
                                    null /* else_detail */ ),
                                new ClassDecoration ( "class " ),
                                ClassDetail.NAME,
                                new ClassDecoration ( "  " ),
                                ClassDetail.PACKAGE,
                                new ClassDecoration ( "\n" ),
                                new ClassIf (
                                    ClassDetail.ENUMERATED_VALUES,
                                    new ClassDecoration ( "\n" ),
                                    null /* else_detail */ ),
                                new ClassDecoration ( "--------------------------------------------------\n" ),
                                new ClassIf (
                                    ClassDetail.INHERITANCE,
                                    new ClassDecoration ( "\n" ),
                                    null /* else_detail */ ),
                                new ClassIf (
                                    ClassDetail.MEMBERS,
                                    new ClassDecoration ( "\n" ),
                                    null /* else_detail */ ),
                                new ClassDecoration ( "\n\n" ) );


    /** The class details, in overall order. */
    private final ClassDetail<?> [] types;

    /**
     * <p>
     * Creates a new overall order to print class details matching the
     * types, in the specified order.
     * </p>
     *
     * @param types The class detail types, in order, which will decide
     *              the class details of each class to be
     *              output, and the order to output them.
     *              Must not be null.  Must not
     *              contain any null elements.
     */
    public ClassDetailsOrder (
                              ClassDetail<?> ... types
                              )
    {
        this.types = new ClassDetail<?> [ types.length ];
        System.arraycopy ( types, 0,
                           this.types, 0, types.length );
    }

    /**
     * @see ClassHierarchy.ClassDetailsListProcessor#process(java.util.List)
     */
    @Override
    public List<ClassDetails<?>> process (
                                          List<ClassDetails<?>> input
                                          )
    {
        List<ClassDetails<?>> unused_input =
            new ArrayList<ClassDetails<?>> ( input );
        List<ClassDetails<?>> output = new ArrayList<ClassDetails<?>> ();
        for ( ClassDetail<?> type : this.types )
        {
            if ( type instanceof ClassDecoration )
            {
                for ( ClassDetails<?> decoration
                          : type.fromClass ( Object.class ) )
                {
                    output.add ( decoration );
                }

                continue;
            }

            for ( int cd = 0; cd < unused_input.size (); cd ++ )
            {
                final ClassDetails<?> details = unused_input.get ( cd );
                if ( type.isOrHas ( details.type () ) )
                {
                    unused_input.remove ( cd );

                    final Class<?> class_or_interface =
                        details.classOrInterface ();

                    for ( ClassDetails<?> processed :
                              type.fromClass ( class_or_interface ) )
                    {
                        output.add ( processed );
                    }

                    cd --;
                }
            }
        }

        return output;
    }

    /**
     * <p>
     * Replaces this ClassDetailsOrder and any child or grandchild
     * that contains the specified replaced ClassDetail type,
     * replacing each one with a ClassDetailsOrder containing the
     * specified replacement ClassDetail type instead.  If no changes
     * are made, this ClassDetailsOrder is returned as-is.
     * </p>
     */
    public final ClassDetailsListProcessor replaceAll (
                                                       ClassDetail<?> replaced,
                                                       ClassDetail<?> replacement
                                                       )
    {
        final ClassDetail<?> [] new_types =
            new ClassDetail<?> [ this.types.length ];
        boolean is_replaced = false;

        int t = 0;
        for ( ClassDetail<?> type : this.types )
        {
            new_types [ t ] = type.replaceAll ( replaced, replacement );
            if ( new_types [ t ] != type )
            {
                is_replaced = true;
            }
        }

        if ( ! is_replaced )
        {
            return this;
        }

        final ClassDetailsOrder new_order =
            new ClassDetailsOrder ( new_types );

        return new_order;
    }

    /**
     * <p>
     * Returns the ordred class detail types, each of which deals with
     * a specific type of class detail (name, stereotype, package, members,
     * and so on).
     * </p>
     *
     * @return The ordered class detail types.  Never null.  Never contains
     *         any null elements.
     */
    public ClassDetail<?> [] types ()
    {
        ClassDetail<?> [] types =
            new ClassDetail<?> [ this.types.length ];
        System.arraycopy ( this.types, 0,
                           types, 0, this.types.length );

        return types;
    }
}
