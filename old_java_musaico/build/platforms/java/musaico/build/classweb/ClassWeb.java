package musaico.build.classweb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import java.util.regex.Pattern;


/**
 * <p>
 * Outputs the hierarchy of interfaces, classes, and so on
 * inherited by class X, including methods and data for each
 * class.
 * </p>
 */
public class ClassWeb
    implements ClassListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final Pattern REGULAR_EXPRESSION_CLASS_NAME =
        Pattern.compile ( "^[A-Za-z_][A-Za-z_\\.\\$\\-0-9]*$" );


    /**
     * <p>
     * Usage: <code> java musaico.build.classweb.ClassWeb path.to.MyClass </code>
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
            System.out.println ( "Usage: java musaico.build.classweb.ClassWeb path.to.MyClass" );
            System.out.println ( "" );
            return;
        }

        final List<ClassListProcessor> class_list_processors =
            new ArrayList<ClassListProcessor> ();
        final List<ClassDetailsListProcessor> class_details_list_processors =
            new ArrayList<ClassDetailsListProcessor> ();
        final List<MemberListProcessor> member_list_processors =
            new ArrayList<MemberListProcessor> ();

        final List<Class<?>> my_classes = new ArrayList<Class<?>> ();

        final StringBuilder out = new StringBuilder ();

        final List<ClassListProcessorFactory> additional_class_list_processors =
            new ArrayList<ClassListProcessorFactory> ();

        int num_outputters = 0;

        for ( int a = 0; a < args.length; a ++ )
        {
            String arg = args [ a ];

            final Class<?> my_class = ClassWeb.getClass ( arg );
            if ( my_class != null )
            {
                my_classes.add ( my_class );
                continue;
            }

            if ( REGULAR_EXPRESSION_CLASS_NAME.matcher ( arg ).matches () )
            {
                // Looks like a class name.  Fail.
                System.err.println ( "Could not load class '" + arg + "'."
                                     + "  If this is meant to be text"
                                     + " for output, prefix it with the"
                                     + " -decoration switch." );
                System.exit ( 2 );
                return;
            }

            // Treat this argument as a switch or a decoration
            // (below).

            final ClassListProcessor class_list_processor;
            final ClassDetailsListProcessor class_details_list_processor;
            final MemberListProcessor member_list_processor;

            if ( ! args [ a ].startsWith ( "-" ) )
            {
                final String decorative_text =
                    decorativeText ( args [ a ] );
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        new ClassDecoration ( decorative_text ) );
                member_list_processor = null;
            }
            else if ( "-abstractclasses".equals ( arg ) )
            {
                class_list_processor =
                    new ClassListFilter ( ClassFilter.ABSTRACT );
                class_details_list_processor = null;
                member_list_processor = null;
            }
            else if ( "-concreteclasses".equals ( arg ) )
            {
                class_list_processor =
                    new ClassListFilter ( ClassFilter.CONCRETE );
                class_details_list_processor = null;
                member_list_processor = null;
            }
            else if ( "-extract".equals ( arg ) )
            {
                additional_class_list_processors.add ( ClassExtractDetails.FACTORY );
                continue;
            }
            else if ( "-hierarchy".equals ( arg ) )
            {
                class_list_processor = new ClassHierarchy ();
                class_details_list_processor = null;
                member_list_processor = null;
            }
            else if ( "-interfaces".equals ( arg ) )
            {
                class_list_processor =
                    new ClassListFilter ( ClassFilter.INTERFACE );
                class_details_list_processor = null;
                member_list_processor = null;
            }
            else if ( "-uml".equals ( arg ) )
            {
                final OutputStream output_stream;
                if ( args.length > ( a + 1 )
                     && ! args [ a + 1 ].startsWith ( "-" )
                     && ClassWeb.getClass ( args [ a + 1 ] ) == null )
                {
                    // Not a switch ("-xyz") and not a class name
                    // ("thing.vehicle.Car"), so must be a filename
                    // for our UML diagram to be output to.
                    final String filename = args [ a + 1 ];
                    a ++;
                    try
                    {
                        output_stream =
                            new FileOutputStream ( filename );
                    }
                    catch ( IOException e )
                    {
                        System.err.println ( "Cannot output Dia UML diagram"
                                             + " to " + filename
                                             + ": " + e );
                        System.exit ( 1 );
                        return;
                    }
                }
                else
                {
                    output_stream = System.out;
                }

                final DiaUML.Factory dia_uml_factory =
                    new DiaUML.Factory ( output_stream );
                additional_class_list_processors.add ( dia_uml_factory );
                num_outputters ++;
                continue;
            }
            else if ( "-decoration".equals ( arg )
                      && args.length > ( a + 1 ) )
            {
                final String decorative_text =
                    decorativeText ( args [ a + 1 ] );
                a ++;
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        new ClassDecoration ( decorative_text ) );
                member_list_processor = null;
            }
            else if ( "-enum".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        ClassDetail.ENUMERATED_VALUES );
                member_list_processor = null;
            }
            else if ( "-if".equals ( arg ) )
            {
                // !!! not yet implemented.
                // The content of the "if" will come next...
                continue;
            }
            else if ( "-inheritance".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        ClassDetail.INHERITANCE );
                member_list_processor = null;
            }
            else if ( "-members".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        ClassDetail.MEMBERS );
                member_list_processor = null;
            }
            else if ( "-name".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        ClassDetail.NAME );
                member_list_processor = null;
            }
            else if ( "-package".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        ClassDetail.PACKAGE );
                member_list_processor = null;
            }
            else if ( "-print".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsPrinter ( out );
                num_outputters ++;
                member_list_processor = null;
            }
            else if ( "-stereotypes".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor =
                    new ClassDetailsOrder (
                        ClassDetail.STEREOTYPE );
                member_list_processor = null;
            }
            else if ( "-constants".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.NONE,
                                             new AlphaFieldComparator () );
            }
            else if ( "-publicconstants".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.PUBLIC_CONSTANT,
                                             new AlphaFieldComparator () );
            }
            else if ( "-protectedconstants".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.PROTECTED_CONSTANT,
                                             new AlphaFieldComparator () );
            }
            else if ( "-privateconstants".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.PRIVATE_CONSTANT,
                                             new AlphaFieldComparator () );
            }
            else if ( "-constructors".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Constructor> ( Constructor.class,
                                                   MemberFilter.NONE,
                                                   new AlphaConstructorComparator () );
            }
            else if ( "-publicconstructors".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Constructor> ( Constructor.class,
                                                   MemberFilter.PUBLIC,
                                                   new AlphaConstructorComparator () );
            }
            else if ( "-protectedconstructors".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Constructor> ( Constructor.class,
                                                   MemberFilter.PROTECTED,
                                                   new AlphaConstructorComparator () );
            }
            else if ( "-privateconstructors".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Constructor> ( Constructor.class,
                                                   MemberFilter.PRIVATE,
                                                   new AlphaConstructorComparator () );
            }
            else if ( "-methods".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Method> ( Method.class,
                                              MemberFilter.NONE,
                                              new AlphaMethodComparator () );
            }
            else if ( "-publicmethods".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Method> ( Method.class,
                                              MemberFilter.PUBLIC,
                                              new AlphaMethodComparator () );
            }
            else if ( "-protectedmethods".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Method> ( Method.class,
                                              MemberFilter.PROTECTED,
                                              new AlphaMethodComparator () );
            }
            else if ( "-privatemethods".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Method> ( Method.class,
                                              MemberFilter.PRIVATE,
                                              new AlphaMethodComparator () );
            }
            else if ( "-fields".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.NONE,
                                             new AlphaFieldComparator () );
            }
            else if ( "-publicfields".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.PUBLIC,
                                             new AlphaFieldComparator () );
            }
            else if ( "-protectedfields".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.PROTECTED,
                                             new AlphaFieldComparator () );
            }
            else if ( "-privatefields".equals ( arg ) )
            {
                class_list_processor = null;
                class_details_list_processor = null;
                member_list_processor =
                    new MemberOrder<Field> ( Field.class,
                                             MemberFilter.PRIVATE,
                                             new AlphaFieldComparator () );
            }
            else
            {
                System.err.println ( "Unrecognized argument: " + arg );
                return;
            }

            if ( class_list_processor != null )
            {
                class_list_processors.add ( class_list_processor );
            }

            if ( class_details_list_processor != null )
            {
                class_details_list_processors.add ( class_details_list_processor );
            }

            if ( member_list_processor != null )
            {
                member_list_processors.add ( member_list_processor );
            }
        }


        final Order members;
        if ( member_list_processors.size () > 0 )
        {
            MemberListProcessor [] member_list_processors_template =
                new MemberListProcessor [ member_list_processors.size () ];
            members = new Order ( member_list_processors.toArray ( member_list_processors_template ) );
        }
        else
        {
            members = Order.DEFAULT;
        }


        for ( int cd = 0; cd < class_details_list_processors.size (); cd ++ )
        {
            final ClassDetailsListProcessor processor =
                class_details_list_processors.get ( cd );
            if ( processor instanceof ClassDetailsOrder )
            {
                final ClassDetailsOrder order = (ClassDetailsOrder) processor;
                class_details_list_processors.remove ( cd );
                class_details_list_processors.add (
                    cd,
                    order.replaceAll ( ClassDetail.MEMBERS,
                                       new ClassMembers ( members ) ) );
            }
        }


        if ( class_details_list_processors.size () == 0
             && class_list_processors.size () == 0 )
        {
            class_details_list_processors.add ( ClassDetailsOrder.DEFAULT );
        }
        if ( num_outputters == 0 )
        {
            class_details_list_processors.add ( new ClassDetailsPrinter ( out ) );
        }
        ClassDetailsListProcessor [] class_detail_template =
            new ClassDetailsListProcessor [ class_details_list_processors.size () ];
        final ClassDetailsListProcessor [] class_details_list_processors_array =
            class_details_list_processors.toArray ( class_detail_template );


        if ( class_details_list_processors.size () == 0
             && class_list_processors.size () == 0 )
        {
            class_list_processors.add ( new ClassHierarchy () );
            additional_class_list_processors.add ( ClassExtractDetails.FACTORY );
        }
        for ( ClassListProcessorFactory factory
                  : additional_class_list_processors )
        {
            class_list_processors.add (
                factory.create (
                    class_details_list_processors_array ) );
        }
        final ClassListProcessor [] class_list_processors_template =
            new ClassListProcessor [ class_list_processors.size () ];
        final ClassListProcessor [] class_list_processors_array =
            class_list_processors.toArray ( class_list_processors_template );

        ClassWeb web =
            new ClassWeb ( class_list_processors_array );

        web.process ( my_classes );

        final String output = out.toString ();

        System.out.println ( output );
    }


    private static final String decorativeText (
                                                String text
                                                )
    {
        return text
            .replaceAll ( "\\\\n", "\n" )
            .replaceAll ( "\\\\t", "\t" );
    }


    private static final Class<?> getClass (
                                            String class_name
                                            )
    {
        try
        {
            return Class.forName ( class_name );
        }
        catch ( ClassNotFoundException e )
        {
            return null;
        }
        catch ( Exception e )
        {
            return null;
        }
    }




    // Sorts the classes to print, filters out unwanted classes,
    // and so on.
    private final ClassListProcessor [] classListProcessors;

    /**
     * <p>
     * Creates a new ClassWeb with the specified filters and ordering
     * and so on.
     * </p>
     */
    public ClassWeb (
                     ClassListProcessor [] class_list_processors
                     )
    {
        this.classListProcessors =
            new ClassListProcessor [ class_list_processors.length ];
        System.arraycopy ( class_list_processors, 0,
                           this.classListProcessors, 0, class_list_processors.length );
    }

    /**
     * @see musaico.build.classweb.ClassListProcessor#process(java.util.List)
     */
    @Override
    public final List<Class<?>> process (
                                         List<Class<?>> input
                                         )
    {
        List<Class<?>> output = new ArrayList<Class<?>> ( input );

        for ( ClassListProcessor processor : this.classListProcessors )
        {
            output = processor.process ( output );
        }

        return output;
    }
}
