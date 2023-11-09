package musaico.foundation.contract.obligations;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.UncheckedViolation;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A method which throws ParametersMustNotBeNull.Violation
 * expects all parameters to be non-null.  Violators will be sent
 * to arbitration, possibly inducing the runtime exception.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.contract.obligations.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.obligations.MODULE#LICENSE
 */
public class ParametersMustNotBeNull
    implements Contract<Object [], ParametersMustNotBeNull.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The parameters-must-not-be-null obligation singleton. */
    public static final ParametersMustNotBeNull CONTRACT =
        new ParametersMustNotBeNull ();


    /**
     * <p>
     * Only the singleton or derived classes can access the constructor
     * directly.  Use ParametersMustNotBeNull.CONTRACT instead.
     * </p>
     */
    protected ParametersMustNotBeNull ()
    {
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final String description ()
    {
        return "Parameters must not be null.";
    }

    @Override
    public final FilterState filter (
                                     Object [] evidence
                                     )
    {
        if ( evidence == null )
        {
            return FilterState.DISCARDED;
        }

        for ( int p = 0; p < evidence.length; p ++ )
        {
            Object parameter = evidence [ p ];
            if ( parameter == null )
            {
                return FilterState.DISCARDED;
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    public final ParametersMustNotBeNull.Violation violation (
                                                              Object plaintiff,
                                                              Object [] evidence
                                                              )
    {
        String pre_method_name = "";
        String post_method_name = "";
        for ( StackTraceElement frame
                  : Thread.currentThread ().getStackTrace () )
        {
            final String class_name = frame.getClassName ();
            if ( class_name.startsWith ( "java.lang.Thread" )
                 || class_name.startsWith ( "musaico.foundation.contract." )
                 || class_name.endsWith ( "Contract" )
                 || class_name.endsWith ( "Contracts" ) )
            {
                continue;
            }

            final String simple_class_name =
                class_name.replaceAll ( "^.*\\.([^\\.]+)$", "$1" );
            pre_method_name = simple_class_name
                + "." + frame.getMethodName () + " ( ";
            post_method_name = " )";
            break;
        }

        final StringBuilder parameters = new StringBuilder ();
        final StringBuilder null_parameters = new StringBuilder ();
        for ( int p = 0; p < evidence.length; p ++ )
        {
            Object parameter = evidence [ p ];
            if ( p > 0 )
            {
                parameters.append ( ", " );
            }

            if ( parameter instanceof String )
            {
                parameters.append ( "\"" + parameter + "\"" );
            }
            else if ( parameter instanceof Character )
            {
                parameters.append ( "'" + parameter + "'" );
            }
            else
            {
                parameters.append ( "" + parameter );
            }

            if ( parameter == null )
            {
                if ( null_parameters.length () > 0 )
                {
                    null_parameters.append ( ", " );
                }

                null_parameters.append ( "# " + ( p + 1 ) );
            }
        }

        final String evidence_string =
            pre_method_name
            + parameters.toString ()
            + post_method_name
            + "  Null parameter(s): "
            + null_parameters.toString ();

        final Object[] text_array = new Object [] { evidence_string };

        return new ParametersMustNotBeNull.Violation ( this,
                                                       plaintiff,
                                                       text_array );
    }

    @Override
    public final ParametersMustNotBeNull.Violation violation (
                                                              Object plaintiff,
                                                              Object [] evidence,
                                                              Throwable cause
                                                              )
    {
        final ParametersMustNotBeNull.Violation violation =
            this.violation ( plaintiff, evidence );
        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }


    /**
     * <p>
     * A violation of the parameters-must-not-be-null contract.
     * </p>
     */
    public static class Violation
        extends UncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            ParametersMustNotBeNull.serialVersionUID;

        /**
         * <p>
         * Creates a ParametersMustNotBeNull.Violation.
         * </p>
         */
        public Violation (
                          ParametersMustNotBeNull obligation,
                          Object plaintiff,
                          Object [] evidence
                          )
        {
            super ( obligation,
                    createDescription ( evidence ), // description
                    Contracts.makeSerializable ( plaintiff ),
                    Contracts.makeSerializable ( evidence ) );
        }

        private static final String createDescription (
            Object [] evidence
            )
        {
            final StringBuilder sbuf = new StringBuilder ();
            int count = 0;
            for ( int p = 0; p < evidence.length; p ++ )
            {
                final Object parameter = evidence [ p ];
                if ( parameter == null )
                {
                    if ( count > 0 )
                    {
                        sbuf.append ( ", " );
                    }

                    sbuf.append ( "" + ( p + 1 ) );

                    count ++;
                }
            }

            if ( count == 0 )
            {
                return "Parameters are null.";
            }
            else if ( count == 1 )
            {
                return "Parameter # " + sbuf.toString () + " is null.";
            }
            else
            {
                return "Parameters # " + sbuf.toString () + " are null.";
            }
        }
    }
}
