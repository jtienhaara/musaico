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
 * Outputs the hierarchy of interfaces, classes, and so on
 * inherited by class X, including methods and data for each
 * class.
 * </p>
 */
public class ClassHierarchy
{
    /**
     * <p>
     * Usage: <code> java ClassHierarchy path.to.MyClass </code>
     * </p>
     */
    @SuppressWarnings("rawtypes") // No way to declare MemberOrder<Constructor<?>> without raw types.
    public static void main (
                             String [] args
                             )
        throws Exception
    {
        if ( args == null
             || args.length < 1 )
        {
            System.out.println ( "Usage: java ClassHierarchy path.to.MyClass" );
            return;
        }

        String my_class_name = args [ args.length - 1 ];
        Class<?> my_class = Class.forName ( my_class_name );

        final Order order;
        if ( args.length == 1 )
        {
            order = Order.DEFAULT;
        }
        else
        {
            List<MemberOrder<?>> member_orders =
                new ArrayList<MemberOrder<?>> ();
            for ( int a = 0; a < ( args.length - 1 ); a ++ )
            {
                String arg = args [ a ];
                final MemberOrder<?> member_order;
                if ( "-constants".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.NONE,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-publicconstants".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.PUBLIC_CONSTANT,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-protectedconstants".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.PROTECTED_CONSTANT,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-privateconstants".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.PRIVATE_CONSTANT,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-constructors".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Constructor> ( Constructor.class,
                                                       MemberFilter.NONE,
                                                       new AlphaConstructorComparator () );
                }
                else if ( "-publicconstructors".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Constructor> ( Constructor.class,
                                                       MemberFilter.PUBLIC,
                                                       new AlphaConstructorComparator () );
                }
                else if ( "-protectedconstructors".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Constructor> ( Constructor.class,
                                                       MemberFilter.PROTECTED,
                                                       new AlphaConstructorComparator () );
                }
                else if ( "-privateconstructors".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Constructor> ( Constructor.class,
                                                       MemberFilter.PRIVATE,
                                                       new AlphaConstructorComparator () );
                }
                else if ( "-methods".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Method> ( Method.class,
                                                  MemberFilter.NONE,
                                                  new AlphaMethodComparator () );
                }
                else if ( "-publicmethods".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Method> ( Method.class,
                                                  MemberFilter.PUBLIC,
                                                  new AlphaMethodComparator () );
                }
                else if ( "-protectedmethods".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Method> ( Method.class,
                                                  MemberFilter.PROTECTED,
                                                  new AlphaMethodComparator () );
                }
                else if ( "-privatemethods".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Method> ( Method.class,
                                                  MemberFilter.PRIVATE,
                                                  new AlphaMethodComparator () );
                }
                else if ( "-fields".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.NONE,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-publicfields".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.PUBLIC,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-protectedfields".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.PROTECTED,
                                                 new AlphaFieldComparator () );
                }
                else if ( "-privatefields".equals ( arg ) )
                {
                    member_order =
                        new MemberOrder<Field> ( Field.class,
                                                 MemberFilter.PRIVATE,
                                                 new AlphaFieldComparator () );
                }
                else
                {
                    System.err.println ( "Unrecognized argument: " + arg );
                    return;
                }

                member_orders.add ( member_order );
            }

            MemberOrder<?> [] template =
                new MemberOrder<?> [ member_orders.size () ];
            order = new Order ( member_orders.toArray ( template ) );
        }

        ClassHierarchy hierarchy_printer =
            new ClassHierarchy ();

        List<Class<?>> classes =
            hierarchy_printer.getInheritanceTree ( my_class );

        for ( Class<?> curr_class : classes )
        {
            StringBuilder printed_class = new StringBuilder ();
            hierarchy_printer.printClass ( curr_class, printed_class, order );
            System.out.println ( printed_class.toString () + "\n\n" );
        }
    }

    public List<Class<?>> getInheritanceTree (
                                              Class<?> top_class
                                              )
    {
        List<Class<?>> unplumbed = new ArrayList<Class<?>> ();
        unplumbed.add ( top_class );

        List<Class<?>> plumbed = new ArrayList<Class<?>> ();

        while ( unplumbed.size () > 0 )
        {
            Class<?> curr_class = unplumbed.remove ( 0 );

            Class<?> [] declared_classes = curr_class.getDeclaredClasses ();
            for ( Class<?> declared_class : declared_classes )
            {
                if ( ! plumbed.contains ( declared_class )
                     && ! unplumbed.contains ( declared_class ) )
                {
                    unplumbed.add ( declared_class );
                }
            }

            Class<?> [] interfaces = curr_class.getInterfaces ();
            for ( int in = 0; in < interfaces.length; in ++ )
            {
                if ( ! plumbed.contains ( interfaces [ in ] )
                     && ! unplumbed.contains ( interfaces [ in ] ) )
                {
                    unplumbed.add ( interfaces [ in ] );
                }
            }

            Class<?> superclass = curr_class.getSuperclass ();
            if ( superclass != null
                 && ! plumbed.contains ( superclass )
                 && ! unplumbed.contains ( superclass ) )
            {
                unplumbed.add ( superclass );
            }

            plumbed.add ( curr_class );
        }

        return plumbed;
    }


    public static class AlphaFieldComparator
        implements Comparator<Field>
    {
        public int compare ( Field field1, Field field2 )
        {
            if ( field1 == null )
            {
                if ( field2 == null )
                {
                    // null == null
                    return 0;
                }

                // null > field2
                return 1;
            }
            else if ( field2 == null )
            {
                // field1 < null
                return -1;
            }

            String name1 = field1.getName ();
            String name2 = field2.getName ();

            return name1.compareTo ( name2 );
        }
    }


    @SuppressWarnings("rawtypes") // Because of MemberOrder<Constructor> later.
    public static class AlphaConstructorComparator
        implements Comparator<Constructor>
    {
        public int compare ( Constructor constructor1,
                             Constructor constructor2 )
        {
            if ( constructor1 == null )
            {
                if ( constructor2 == null )
                {
                    // null == null
                    return 0;
                }

                // null > constructor2
                return 1;
            }
            else if ( constructor2 == null )
            {
                // constructor1 < null
                return -1;
            }

            String name1 = constructor1.getName ();
            String name2 = constructor2.getName ();

            return name1.compareTo ( name2 );
        }
    }


    public static class AlphaMethodComparator
        implements Comparator<Method>
    {
        public int compare ( Method method1, Method method2 )
        {
            if ( method1 == null )
            {
                if ( method2 == null )
                {
                    // null == null
                    return 0;
                }

                // null > method2
                return 1;
            }
            else if ( method2 == null )
            {
                // method1 < null
                return -1;
            }

            String name1 = method1.getName ();
            String name2 = method2.getName ();

            return name1.compareTo ( name2 );
        }
    }


    /** Static members before other members. */
    public static class StaticMemberComparator<MEMBER extends Member>
        implements Comparator<MEMBER>
    {
        public int compare ( MEMBER member1, MEMBER member2 )
        {
            if ( member1 == null )
            {
                if ( member2 == null )
                {
                    // null == null
                    return 0;
                }

                // null > member2
                return 1;
            }
            else if ( member2 == null )
            {
                // member1 < null
                return -1;
            }

            int modifiers1 = member1.getModifiers ();
            int modifiers2 = member2.getModifiers ();

            if ( ( modifiers1 & Modifier.STATIC ) != 0 )
            {
                if ( ( modifiers2 & Modifier.STATIC ) != 0 )
                {
                    // static member == static member
                    return 0;
                }
                else
                {
                    // static member < non-static member
                    return -1;
                }
            }
            else if ( ( modifiers2 & Modifier.STATIC ) != 0 )
            {
                // non-static member > static member
                return 1;
            }

            // non-static member == non-static member
            return 0;
        }
    }


    /** Composite orders: first use one comparator, if it returns 2 members
     *  are equal then use the next comparator, and so on. */
    public static class CompositeMemberComparator<MEMBER extends Member>
        implements Comparator<MEMBER>
    {
        /** The comparators to use, in order. */
        private final Comparator<MEMBER> [] comparators;

        /**
         * <p>
         * Creates a new composite comparator from the specified ones.
         * Each one will be applied in order until one returns a
         * non-equal (non-zero) result when comparing 2 members.
         * </p>
         */
        public CompositeMemberComparator (
                                          Comparator<MEMBER> [] comparators
                                          )
        {
            this.comparators = comparators;
        }

        public int compare ( MEMBER member1, MEMBER member2 )
        {
            for ( Comparator<MEMBER> comparator : this.comparators )
            {
                int comparison = comparator.compare ( member1, member2 );
                if ( comparison != 0 )
                {
                    return comparison;
                }
            }

            // No comparator spotted any differences.  The members are
            // comparatively equivalent.
            return 0;
        }
    }


    /**
     * <p>
     * Filters out specific Members from the output.
     * </p>
     */
    public static interface MemberFilter
    {
        /** Does not filter anything out. */
        public static final MemberFilter NONE =
            new NoMemberFilter ();

        /** Filters out everything but public members. */
        public static final MemberFilter PUBLIC =
            new ModifierFilter ( Modifier.PUBLIC );

        /** Filters out everything but protected members. */
        public static final MemberFilter PROTECTED =
            new ModifierFilter ( Modifier.PROTECTED );

        /** Filters out everything but private members. */
        public static final MemberFilter PRIVATE =
            new ModifierFilter ( Modifier.PRIVATE );

        /** Filters out everything except public static final members. */
        public static final MemberFilter PUBLIC_CONSTANT =
            new ModifierFilter ( Modifier.FINAL
                                 | Modifier.PUBLIC
                                 | Modifier.STATIC );

        /** Filters out everything except protected static final members. */
        public static final MemberFilter PROTECTED_CONSTANT =
            new ModifierFilter ( Modifier.FINAL
                                 | Modifier.PROTECTED
                                 | Modifier.STATIC );

        /** Filters out everything except private static final members. */
        public static final MemberFilter PRIVATE_CONSTANT =
            new ModifierFilter ( Modifier.FINAL
                                 | Modifier.PRIVATE
                                 | Modifier.STATIC );

        /**
         * <p>
         * Filters the specified class Member.
         * </p>
         *
         * @param member The member of the class to filter.
         *               Must not be null.
         *
         * @return True if the specified member should be printed;
         *         false if the specified member should be filtered out
         *         (not printed).
         */
        public abstract boolean matches (
                                         Member member
                                         );
    }


    /**
     * <p>
     * Allows all class Members to pass through, filtering out nothing.
     * </p>
     */
    public static class NoMemberFilter
        implements MemberFilter
    {
        /**
         * <p>
         * Creates a new NoMemberFilter to not filter out anything.
         * Use MemberFilter.NONE instead.
         * </p>
         */
        // package private
        NoMemberFilter ()
        {
        }

        /**
         * See ClassHierarchy.MemberFilter#matches(java.lang.reflect.Member)
         */
        @Override
        public final boolean matches (
                                      Member member
                                      )
        {
            return true;
        }
    }


    /**
     * <p>
     * Filters out class Members which do not have a specific Modifier
     * or set of Modifiers (PUBLIC, PRIVATE, STATIC and so on).
     * </p>
     */
    public static class ModifierFilter
        implements MemberFilter
    {
        /** The modifier(s) which must be present, or the Member
         * is filtered out. */
        private final int modifiers;

        /**
         * <p>
         * Creates a new ModifierFilter to discard every Member which
         * does not have the specified modifier(s).
         * </p>
         *
         * @param modifier The modifier(s) to look for, logically
         *                 OR'ed together.
         */
        public ModifierFilter (
                               int modifiers
                               )
        {
            this.modifiers = modifiers;
        }

        /**
         * See ClassHierarchy.MemberFilter#matches(java.lang.reflect.Member)
         */
        @Override
        public boolean matches (
                                Member member
                                )
        {
            int actual_modifiers = member.getModifiers ();
            if ( ( actual_modifiers & this.modifiers ) == this.modifiers )
            {
                // Member has all the right modifiers.
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.lang.Object.equals(java.lang.Object)
         */
        public final boolean equals (
                                     Object object
                                     )
        {
            if ( object == null )
            {
                return false;
            }
            else if ( this.getClass ().equals ( object.getClass () ) )
            {
                return false;
            }

            ModifierFilter that = (ModifierFilter) object;

            if ( this.modifiers == that.modifiers )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    /**
     * <p>
     * Takes a List of class Members as input, maybe does some side-effects,
     * then returns a new (possibly modified) List of class Members as output.
     * </p>
     */
    public static interface MemberListProcessor
    {
        /**
         * <p>
         * Takes a List of Members as input, maybe does some side-effects,
         * then returns a new List of Members as output.
         * </p>
         */
        public abstract List<Member> process (
                                              List<Member> input
                                              );
    }


    /**
     * <p>
     * A member filter and sort order for a specific class of Members.
     * </p>
     */
    public static class MemberOrder<MEMBER extends Member>
        implements MemberListProcessor
    {
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
            new MemberOrder<Method> ( Method.class,
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


    /**
     * <p>
     * Specifies the overall order, filters and individual sort orders
     * for printing out a class hierarchy.
     * </p>
     */
    public static class Order
        implements MemberListProcessor
    {
        /** The default overall order: constructors, methods, fields. */
        public static final Order DEFAULT =
            new Order ( MemberOrder.PUBLIC_CONSTANTS,
                        MemberOrder.PUBLIC_CONSTRUCTORS,
                        MemberOrder.PUBLIC_METHODS,
                        MemberOrder.PUBLIC_ATTRIBUTES );


        /** The member filters, by class of member, in overall order. */
        private final MemberOrder<?> [] memberOrders;

        /**
         * <p>
         * Creates a new overall order to print Members matching the
         * specified filters, in the specified order of filters.
         * </p>
         *
         * @param filters The filters, in order, which will decide
         *                the Member elements of each class to be
         *                output, and the order to output them.
         *                Must not be null.  Must not
         *                contain any null elements.
         */
        public Order (
                      MemberOrder<?> ... member_orders
                      )
        {
            this.memberOrders = new MemberOrder<?> [ member_orders.length ];
            System.arraycopy ( member_orders, 0,
                               this.memberOrders, 0, member_orders.length );
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
            for ( MemberOrder<?> member_order : this.memberOrders )
            {
                List<Member> used = member_order.process ( unused_input );
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
        public MemberOrder<?> [] memberOrders ()
        {
            MemberOrder<?> [] member_orders =
                new MemberOrder<?> [ this.memberOrders.length ];
            System.arraycopy ( this.memberOrders, 0,
                               member_orders, 0, this.memberOrders.length );

            return member_orders;
        }
    }


    /**
     * <p>
     * Prints out details about the specified list of class Members.
     * </p>
     */
    public static class MemberPrinter
        implements MemberListProcessor
    {
        /** The stringbuilder to print to. */
        private final StringBuilder sbuf;

        /**
         * <p>
         * Creates a new MemberPrinter which will print every Member to
         * the specified string builder.
         * </p>
         */
        public MemberPrinter (
                              StringBuilder sbuf
                              )
        {
            this.sbuf = sbuf;
        }

        /**
         * @see ClassHierarchy.MemberListProcessor#process(java.util.List)
         */
        @Override
        public List<Member> process (
                                     List<Member> input
                                     )
        {
            Class<Member> last_type = null;

            for ( Member member : input )
            {
                @SuppressWarnings("unchecked") // Member.getClass () -> Class<Member> always.  Sigh.
                Class<Member> this_type = (Class<Member>) member.getClass ();
                if ( last_type == null
                     || ! this_type.equals ( last_type ) )
                {
                    this.sbuf.append ( "\n" );
                }

                if ( member instanceof Constructor )
                {
                    Constructor<?> constructor = (Constructor<?>) member;
                    this.printConstructor ( constructor );
                }
                else if ( member instanceof Method )
                {
                    Method method = (Method) member;
                    this.printMethod ( method );
                }
                else if ( member instanceof Field )
                {
                    Field field = (Field) member;
                    this.printField ( field );
                }
                else
                {
                    sbuf.append ( "" + member );
                }

                last_type = this_type;
            }

            return new ArrayList<Member> ( input );
        }

        /**
         * <p>
         * Prints the specified Constructor to the internal StringBuilder.
         * </p>
         */
        public void printConstructor (
                                      Constructor<?> constructor
                                      )
        {
            this.sbuf.append ( "    " );

            final String prefixes = this.getPrefixes ( constructor );
            this.sbuf.append ( prefixes );

            final String name =
                constructor.getDeclaringClass ().getSimpleName ();
            this.sbuf.append ( name );

            this.sbuf.append ( " (" );
            boolean is_first_argument = true;
            for ( Class<?> argument : constructor.getParameterTypes () )
            {
                if ( is_first_argument )
                {
                    is_first_argument = false;
                }
                else
                {
                    this.sbuf.append ( "," );
                }

                this.sbuf.append ( " " + argument.getSimpleName () );
            }

            if ( ! is_first_argument )
            {
                this.sbuf.append ( " " );

                if ( constructor.isVarArgs () )
                {
                    this.sbuf.append ( "... " );
                }
            }

            this.sbuf.append ( ")" );

            final String postfixes = this.getPostfixes ( constructor );
            this.sbuf.append ( postfixes );

            boolean is_first_exception = true;
            for ( Class<?> exception : constructor.getExceptionTypes () )
            {
                if ( is_first_exception )
                {
                    this.sbuf.append ( "\n        throws " );
                    is_first_exception = false;
                }
                else
                {
                    this.sbuf.append ( ",\n               " );
                }

                this.sbuf.append ( exception.getSimpleName () );
            }

            this.sbuf.append ( "\n" );
        }

        /**
         * <p>
         * Prints the specified Method to the internal StringBuilder.
         * </p>
         */
        public void printMethod (
                                 Method method
                                 )
        {
            this.sbuf.append ( "    " );

            final String prefixes = this.getPrefixes ( method );
            this.sbuf.append ( prefixes );

            final String name = method.getName ();
            this.sbuf.append ( name );

            this.sbuf.append ( " (" );
            boolean is_first_argument = true;
            for ( Class<?> argument : method.getParameterTypes () )
            {
                if ( is_first_argument )
                {
                    is_first_argument = false;
                }
                else
                {
                    this.sbuf.append ( "," );
                }

                this.sbuf.append ( " " + argument.getSimpleName () );
            }

            if ( ! is_first_argument )
            {
                this.sbuf.append ( " " );

                if ( method.isVarArgs () )
                {
                    this.sbuf.append ( "... " );
                }
            }

            this.sbuf.append ( ")" );

            Class<?> return_type = method.getReturnType ();
            this.sbuf.append ( " : " + return_type.getSimpleName () );

            final String postfixes = this.getPostfixes ( method );
            this.sbuf.append ( postfixes );

            boolean is_first_exception = true;
            for ( Class<?> exception : method.getExceptionTypes () )
            {
                if ( is_first_exception )
                {
                    this.sbuf.append ( "\n        throws " );
                    is_first_exception = false;
                }
                else
                {
                    this.sbuf.append ( ",\n               " );
                }

                this.sbuf.append ( exception.getSimpleName () );
            }

            this.sbuf.append ( "\n" );
        }

        /**
         * <p>
         * Prints the specified Field to the internal StringBuilder.
         * </p>
         */
        public void printField (
                                Field field
                                )
        {
            this.sbuf.append ( "    " );

            final String prefixes = this.getPrefixes ( field );
            this.sbuf.append ( prefixes );

            final String name = field.getName ();
            this.sbuf.append ( name );

            Class<?> field_type = field.getType ();
            this.sbuf.append ( " : " + field_type.getSimpleName () );

            final String postfixes = this.getPostfixes ( field );
            this.sbuf.append ( postfixes );

            this.sbuf.append ( "\n" );
        }

        /**
         * <p>
         * Returns prefixes to methods, constructors and so on,
         * such as a visibility symbol, "static", and so on.
         * </p>
         */
        public String getPrefixes (
                                   Member member
                                   )
        {
            int modifiers = member.getModifiers ();
            StringBuilder sbuf = new StringBuilder ();

            sbuf.append ( this.getVisibility ( modifiers ) );
            sbuf.append ( " " );

            return sbuf.toString ();
        }

        /**
         * <p>
         * Returns symbols for visibility: "+" for public, "-" for
         * private and so on.
         * </p>
         */
        public String getVisibility (
                                     int modifiers
                                     )
        {
            final String visibility;
            if ( ( modifiers & Modifier.PUBLIC ) != 0 )
            {
                visibility = "+";
            }
            else if ( ( modifiers & Modifier.PROTECTED ) != 0 )
            {
                visibility = "#";
            }
            else
            {
                visibility = "-";
            }

            return visibility;
        }

        /**
         * <p>
         * Returns postfixes to methods, constructors and so on.
         * </p>
         */
        public String getPostfixes (
                                    Member member
                                    )
        {
            int modifiers = member.getModifiers ();
            StringBuilder sbuf = new StringBuilder ();

            if ( member instanceof AnnotatedElement )
            {
                AnnotatedElement annotated = (AnnotatedElement) member;
                for ( Annotation annotation : annotated.getDeclaredAnnotations () )
                {
                    sbuf.append ( "  @" );
                    sbuf.append ( annotation.annotationType ().getSimpleName () );
                    sbuf.append ( " " );
                }
            }

            if ( ( modifiers & Modifier.STATIC ) != 0 )
            {
                sbuf.append ( "  <<static>>" );
            }
            if ( ( modifiers & Modifier.FINAL ) != 0 )
            {
                sbuf.append ( "  <<final>>" );
            }
            if ( ( modifiers & Modifier.NATIVE ) != 0 )
            {
                sbuf.append ( "  <<native>>" );
            }
            if ( ( modifiers & Modifier.STRICT ) != 0 )
            {
                sbuf.append ( "  <<strict>>" );
            }
            if ( ( modifiers & Modifier.SYNCHRONIZED ) != 0 )
            {
                sbuf.append ( "  <<synchronized>>" );
            }
            if ( ( modifiers & Modifier.TRANSIENT ) != 0 )
            {
                sbuf.append ( "  <<transient>>" );
            }
            if ( ( modifiers & Modifier.VOLATILE ) != 0 )
            {
                sbuf.append ( "  <<volatile>>" );
            }

            return sbuf.toString ();
        }
    }


    /**
     * <p>
     * Prints out details about the specified class to the specified
     * StringBuilder, including methods, constants and data.
     * </p>
     *
     * @param class_to_print The class whose details will be printed.
     *                       Must not be null.
     *
     * @param out The StringBuilder to which output will be printed.
     *            Must not be null.
     *
     * @param order The sort orders for constructors, methods, fields
     *              (such as alphanumeric), as well as which elements
     *              to include overall, and the overall sort order
     *              (such as constructors first, methods 2nd, fields last).
     *              Must not be null.
     */
    public void printClass (
                            Class<?> class_to_print,
                            StringBuilder out,
                            Order order
                            )
    {
        List<Member> all_members = new ArrayList<Member> ();

        // Constants
        for ( Field field : class_to_print.getDeclaredFields () )
        {
            all_members.add ( field );
        }
        for ( Constructor<?> constructor : class_to_print.getConstructors () )
        {
            all_members.add ( constructor );
        }
        for ( Method method : class_to_print.getDeclaredMethods () )
        {
            all_members.add ( method );
        }

        List<Member> ordered_members = order.process ( all_members );

        this.printClassAndMembers ( class_to_print, out, ordered_members );
    }

    public void printClassAndMembers (
                                      Class<?> class_to_print,
                                      StringBuilder out,
                                      List<Member> ordered_members
                                      )
    {
        if ( class_to_print.isInterface () )
        {
            out.append ( "<<interface>>" );
            out.append ( "\n" );
        }
        else if ( ( class_to_print.getModifiers () & Modifier.ABSTRACT ) != 0 )
        {
            out.append ( "<<abstract>>" );
            out.append ( "\n" );
        }

        out.append ( "class " );

        String class_name = class_to_print.getSimpleName ();
        final String name;
        Class<?> parent_class = class_to_print.getDeclaringClass ();
        if ( parent_class == null )
        {
            name = class_name;
        }
        else
        {
            name = parent_class.getSimpleName () + "$" + class_name;
        }
        out.append ( name );

        Package package_of_class = class_to_print.getPackage ();
        if ( package_of_class != null )
        {
            out.append ( "  [ package "
                         + package_of_class.getName ()
                         + " ]" );
        }

        Object [] enumerated_values = class_to_print.getEnumConstants ();
        if ( enumerated_values != null
             && enumerated_values.length > 0 )
        {
            boolean is_first_enumerated_value = true;
            for ( Object enumerated_value : enumerated_values )
            {
                if ( is_first_enumerated_value )
                {
                    out.append ( " = {" );
                    is_first_enumerated_value = false;
                }
                else
                {
                    out.append ( "," );
                }

                out.append ( " " + enumerated_value );
            }
        }

        out.append ( "\n" );
        out.append ( "--------------------------------------------------" );
        out.append ( "\n" );

        Class<?> superclass = class_to_print.getSuperclass ();
        if ( superclass != null )
        {
            out.append ( "    extends " + superclass );
            out.append ( "\n" );
        }
        Class<?> [] interfaces = class_to_print.getInterfaces ();
        if ( interfaces.length > 0 )
        {
            out.append ( "    implements " );
        }
        for ( int in = 0; in < interfaces.length; in ++ )
        {
            if ( in > 0 )
            {
                out.append ( ", " );
            }

            out.append ( interfaces [ in ].getSimpleName () );
        }
        if ( interfaces.length > 0 )
        {
            out.append ( "\n" );
        }

        MemberPrinter member_printer = new MemberPrinter ( out );
        member_printer.process ( ordered_members );
    }
}
