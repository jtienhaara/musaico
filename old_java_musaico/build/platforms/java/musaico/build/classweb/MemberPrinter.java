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
 * Prints out details about the specified list of class Members.
 * </p>
 */
public class MemberPrinter
    implements MemberListProcessor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


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
