package musaico.build.classweb;

import java.io.Serializable;

import java.lang.annotation.Annotation;

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
 * Specifies the overall order, filters and individual sort orders
 * for printing out a class hierarchy.
 * </p>
 */
public class Order
    implements MemberListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The default overall order: constructors, methods, fields. */
    public static final Order DEFAULT =
        new Order ( MemberOrder.PUBLIC_CONSTANTS,
                    MemberOrder.PUBLIC_CONSTRUCTORS,
                    MemberOrder.PUBLIC_METHODS,
                    MemberOrder.PUBLIC_ATTRIBUTES );


    /** The member filters, by class of member, in overall order. */
    private final MemberListProcessor [] memberListProcessors;

    /**
     * <p>
     * Creates a new overall order to print Members matching the
     * specified filters, in the specified order of filters.
     * </p>
     *
     * @param member_list_processors The filters, sorters, and so on,
     *                               in order, which will decide
     *                               the Member elements of each class to be
     *                               output, and the order to output them.
     *                               Must not be null.  Must not
     *                               contain any null elements.
     */
    public Order (
                  MemberListProcessor ... member_list_processors
                  )
    {
        this.memberListProcessors =
            new MemberListProcessor [ member_list_processors.length ];
        System.arraycopy ( member_list_processors, 0,
                           this.memberListProcessors, 0, member_list_processors.length );
    }

    /**
     * @see ClassHierarchy.MemberListProcessor#process(java.util.List)
     */
    @Override
    public List<Member> process (
                                 List<Member> input
                                 )
    {
        List<Member> unused_input = new ArrayList<Member> ( input );
        List<Member> output = new ArrayList<Member> ();
        for ( MemberListProcessor member_list_processor
                  : this.memberListProcessors )
        {
            List<Member> used = member_list_processor.process ( unused_input );
            output.addAll ( used );
            unused_input.removeAll ( used );
        }

        return output;
    }

    /**
     * <p>
     * Returns the ordred member filters, each of which deals with
     * a specific type of Member (field, method, constructor, and so on).
     * </p>
     *
     * @return The ordered Member filters.  Never null.  Never contains
     *         any null elements.
     */
    public MemberListProcessor [] memberListProcessors ()
    {
        MemberListProcessor [] member_list_processors =
            new MemberListProcessor [ this.memberListProcessors.length ];
        System.arraycopy ( this.memberListProcessors, 0,
                           member_list_processors, 0, this.memberListProcessors.length );

        return member_list_processors;
    }
}
