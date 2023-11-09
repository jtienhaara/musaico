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
 * A member filter and sort order for a specific class of Members.
 * </p>
 */
public class MemberOrder<MEMBER extends Member>
    implements MemberListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Print all public constant fields, in alphanumeric order. */
    public static final MemberOrder<Field> PUBLIC_CONSTANTS =
        new MemberOrder<Field> ( Field.class,
                                 MemberFilter.PUBLIC_CONSTANT,
                                 new AlphaFieldComparator () );

    /** Print all public constructors, in alphanumeric order. */
    @SuppressWarnings("rawtypes") // No way of declaring without raw types.
    public static final MemberOrder<Constructor> PUBLIC_CONSTRUCTORS =
        new MemberOrder<Constructor> ( Constructor.class,
                                       MemberFilter.PUBLIC,
                                       (Comparator<Constructor>) new AlphaConstructorComparator () );

    /** Print all public methods, static methods followed by non-static
     *  ones, each in alphanumeric order. */
    @SuppressWarnings("unchecked") // Stupid generic arrays
    public static final MemberOrder<Method> PUBLIC_METHODS =
        new MemberOrder<Method> (
            Method.class,
            MemberFilter.PUBLIC,
            new CompositeMemberComparator<Method> (
                (Comparator<Method> [] )
                new Comparator<?> [] {
                    new StaticMemberComparator<Method> (),
                    new AlphaMethodComparator ()
                } ) );

    /** Print all public non-constant fields, in alphanumeric order. */
    public static final MemberOrder<Field> PUBLIC_ATTRIBUTES =
        new MemberOrder<Field> ( Field.class,
                                 MemberFilter.PUBLIC,
                                 new AlphaFieldComparator () );


    /** The class of filter to which the member filter applies. */
    private final Class<MEMBER> memberType;

    /** The filter to apply to each member of the specific type. */
    private final MemberFilter memberFilter;

    /** The sort order for members which made it through the filter. */
    private final Comparator<MEMBER> memberOrder;

    /** Creates a new MemberOrder for the specified class of Members,
     *  which will filter Members using the specified member filter,
     *  then sorting by the specified comparator. */
    public MemberOrder (
                        Class<MEMBER> member_type,
                        MemberFilter member_filter,
                        Comparator<MEMBER> member_order
                        )
    {
        this.memberType = member_type;
        this.memberFilter = member_filter;
        this.memberOrder = member_order;
    }

    /**
     * @see ClassHierarchy.MemberListProcessor#process(java.util.List)
     */
    @Override
    public List<Member> process (
                                 List<Member> input
                                 )
    {
        List<MEMBER> ordered_members = new ArrayList<MEMBER> ();
        for ( Member member : input )
        {
            if ( ! this.memberType.isInstance ( member ) )
            {
                continue;
            }

            @SuppressWarnings("unchecked") // instanceof checked above.
            MEMBER filterable_member = (MEMBER) member;

            if ( this.memberFilter.matches ( filterable_member ) )
            {
                ordered_members.add ( filterable_member );
            }
        }

        Collections.sort ( ordered_members, this.memberOrder );

        List<Member> output = new ArrayList<Member> ( ordered_members );

        return output;
    }

    /**
     * @see java.lang.Object.equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( ! ( object instanceof MemberOrder ) )
        {
            return false;
        }

        MemberOrder<?> that = (MemberOrder<?>) object;
        if ( this.memberType.equals ( that.memberType )
             && this.memberFilter.equals ( that.memberFilter )
             && this.memberOrder.getClass ().equals ( that.memberOrder.getClass () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object.hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.memberType.hashCode ();
    }
}
