package musaico.foundation.contract;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;


/**
 * <p>
 * A violation of some contract without any specific detail
 * about what went wrong.
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
 * @see musaico.foundation.contract.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.MODULE#LICENSE
 */
public class CheckedViolation
    extends Exception
    implements Violation, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** How many stack frames will be output, maximum, during
     *  printStackTrace()? */
    public static final int MAXIMUM_STACK_FRAMES = 7;


    // The contract which enforced this CheckedViolation.
    private final Contract<?, ?> contract;

    // A human-readable, non-internationalized explanation of
    // why the contract was violated.  Used by developers, testers
    // and maintainers to troubleshoot and debug exceptions and errors.
    private final String description;

    // The serialized version of the object under contract.
    private final Serializable plaintiff;

    // The serialized version of the evidence which did
    // not comply with the contract.
    private final Serializable evidence;


    /**
     * <p>
     * Creates a new CheckedViolation of the specified contract,
     * on behalf of the specified plaintiff, with the specified
     * violating evidence.
     * </p>
     *
     * @param contract The contract which was violated.
     *                 Must not be null.
     *
     * @param description A human-readable, non-internationalized
     *                    explanation of why the contract was violated.
     *                    Used by developers, testers and maintainers
     *                    to troubleshoot and debug exceptions and errors.
     *                    Must not be null.
     *
     * @param plaintiff The object under contract, such as the object
     *                  whose method obligation was violated, or which
     *                  violated its own method guarantee.  Must not be null.
     *
     * @param evidence The data which violated the contract.
     *                 Can be null.
     */
    public CheckedViolation (
                             Contract<?, ?> contract,
                             String description,
                             Object plaintiff,
                             Object evidence
                             )
    {
        super ( description );

        this.contract = contract;
        this.description = description;
        this.plaintiff =
            Contracts.makeSerializable ( plaintiff );
        this.evidence =
            Contracts.makeSerializable ( evidence );
    }


    /**
     * <p>
     * Creates a new CheckedViolation of the specified contract,
     * on behalf of the specified plaintiff, with the specified
     * violating evidence.
     * </p>
     *
     * @param contract The contract which was violated.
     *                 Must not be null.
     *
     * @param description A human-readable, non-internationalized
     *                    explanation of why the contract was violated.
     *                    Used by developers, testers and maintainers
     *                    to troubleshoot and debug exceptions and errors.
     *                    Must not be null.
     *
     * @param plaintiff The object under contract, such as the object
     *                  whose method obligation was violated, or which
     *                  violated its own method guarantee.  Must not be null.
     *
     * @param evidence The data which violated the contract.
     *                 Can be null.
     *
     * @param cause The Throwable which caused this contract violation.
     *              Can be null.
     */
    public CheckedViolation (
                             Contract<?, ?> contract,
                             String description,
                             Object plaintiff,
                             Object evidence,
                             Throwable cause
                             )
    {
        this ( contract, description, plaintiff, evidence );

        if ( cause != null )
        {
            this.initCause ( cause );
        }
    }


    /**
     * <p>
     * Creates a new CheckedViolation to wrap the specified
     * contract Violation.
     * </p>
     *
     * @param violation The contract Violation to wrap.
     *                  Must not be null.
     */
    public <VIOLATION extends Throwable & Violation>
                              CheckedViolation (
                                                VIOLATION violation
                                                )
    {
        this ( violation == null
                   ? null
                   : violation.contract (),
               violation == null
                   ? null
                   : violation.description (),
               violation == null
                   ? null
                   : violation.plaintiff (),
               violation == null
                   ? null
                   : violation.evidence (),
               violation  ); // cause
    }


    /**
     * @return The stack trace element from before we entered Violation
     *         stack frames (constructor or so on).  Never null.
     */
    public static final String getStackFrame ()
    {
        final StackTraceElement [] stack_frames =
            Thread.currentThread ().getStackTrace ();
        for ( StackTraceElement stack_frame : stack_frames )
        {
            final String summarized_stack_frame =
                summarizeStackFrame ( stack_frame );
            if ( summarized_stack_frame != null )
            {
                return summarized_stack_frame;
            }
        }

        return "?Unknown stack frame?";
    }


    /**
     * @return A brief rendering of the specified stack frame in
     *         a stack trace, or null if it's not even worth
     *         showing it (for example because the stack trace is
     *         from java.*, in which case, 99.999% of the time, the
     *         troubleshooter does not care, and wants to know
     *         only how the java method invocation got placed on
     *         the stack in the first place).  Can be null.
     */
    protected static final String summarizeStackFrame (
            StackTraceElement stack_frame
            )
    {
        if ( stack_frame == null )
        {
            return null;
        }

        final String class_name = stack_frame.getClassName ();
        if ( class_name == null
             || class_name.startsWith ( "java." )
             || class_name.contains ( "contract" )
             || class_name.contains ( "Contract" )
             || class_name.contains ( "Must" )
             || class_name.contains ( "violation" )
             || class_name.contains ( "Violation" ) )
        {
            return null;
        }

        final String simple_class_name = ClassName.from ( class_name );

        final String method_name;
        if ( stack_frame.getMethodName () == null )
        {
            method_name = "";
        }
        else
        {
            method_name = "." + stack_frame.getMethodName () + "(...)";
        }

        final String line_number;
        if ( stack_frame.getLineNumber () <= 0 )
        {
            line_number = "";
        }
        else
        {
            line_number = " line " + stack_frame.getLineNumber ();
        }

        return simple_class_name
            + method_name
            + line_number;
    }


    /**
     * @see musaico.foundation.contract.Violation#contract()
     */
    @Override
    public final Contract<?, ?> contract ()
    {
        return this.contract;
    }


    /**
     * @see musaico.foundation.contract.Violation#description()
     */
    @Override
    public final String description ()
    {
        return this.description;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
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
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final CheckedViolation that = (CheckedViolation) object;

        if ( this.contract == null )
        {
            if ( that.contract != null )
            {
                return false;
            }
        }
        else if ( that.contract == null )
        {
            return false;
        }
        else if ( ! this.contract.equals ( that.contract ) )
        {
            return false;
        }

        if ( this.description == null )
        {
            if ( that.description != null )
            {
                return false;
            }
        }
        else if ( that.description == null )
        {
            return false;
        }
        else if ( ! this.description.equals ( that.description ) )
        {
            return false;
        }

        if ( this.plaintiff == null )
        {
            if ( that.plaintiff != null )
            {
                return false;
            }
        }
        else if ( that.plaintiff == null )
        {
            return false;
        }
        else if ( ! this.plaintiff.equals ( that.plaintiff ) )
        {
            return false;
        }

        if ( this.evidence == null )
        {
            if ( that.evidence != null )
            {
                return false;
            }
        }
        else if ( that.evidence == null )
        {
            return false;
        }
        else if ( ! this.evidence.equals ( that.evidence ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        if ( this.contract != null )
        {
            hash_code += 31 * this.contract.hashCode ();
        }
        if ( this.plaintiff != null )
        {
            hash_code += 17 * this.plaintiff.hashCode ();
        }
        if ( this.evidence != null )
        {
            hash_code += this.evidence.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see musaico.foundation.contract.Violation#evidence()
     */
    @Override
    public final Serializable evidence ()
    {
        return this.evidence;
    }


    /**
     * @see musaico.foundation.contract.Violation#plaintiff()
     */
    @Override
    public final Serializable plaintiff ()
    {
        return this.plaintiff;
    }


    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    @Override
    public final void printStackTrace ()
    {
        this.printStackTrace ( System.err );
        System.err.flush ();
    }


    /**
     * @see java.lang.Throwable#printStackTrace(java.io.StreamWriter)
     */
    @Override
    public final void printStackTrace (
                                       PrintStream out_stream
                                       )
    {
        final PrintWriter out =
            new PrintWriter (
                new OutputStreamWriter (
                    out_stream ) );
        this.printStackTrace ( out );
        out.flush ();
    }


    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    @Override
    public final void printStackTrace (
                                       PrintWriter out
                                       )
    {
        CheckedViolation.printStackTrace ( this, out );
        out.flush ();
    }


    /**
     * <p>
     * Prints detailed information about the specified Contract
     * Violation to the specified PrintWriter.
     * </p>
     *
     * @param violation The Contract Violation to describe.
     *                  Must not be null.
     *
     * @param out The PrintWriter to output to.  Must not be null.
     */
    public static final void printStackTrace (
                                              Violation violation,
                                              PrintWriter out
                                              )
    {
        out.println ( "------------------------------------------------------------------------" );
        out.println ( ClassName.of ( violation.getClass () ) );
        if ( violation instanceof CheckedViolation )
        {
            out.println ( "    (Checked violation)" );
        }
        else if ( violation instanceof UncheckedViolation )
        {
            out.println ( "    (Unchecked violation)" );
        }
        out.println ( "    Contract    = " + violation.contract () );
        out.println ( "    Plaintiff   = "
                      + StringRepresentation.of (
                            violation.plaintiff (),
                            0
                            ) );
        out.println ( "    Evidence    = "
                      + StringRepresentation.of (
                            violation.evidence (),
                            0
                            ) );
        out.println ( "Contract:  " + violation.contract ().description () );
        out.println ( "Violation: " + violation.description () );

        Throwable root_cause = null;

        Throwable cause = null;
        if ( violation instanceof Throwable )
        {
            final Throwable throwable = (Throwable) violation;
            root_cause = throwable;
            cause = throwable.getCause ();
        }
        Contract<?, ?> last_contract = violation.contract ();
        String last_contract_description =
            violation.contract ().description ();
        String last_violation_description = violation.description ();
        int infinite_loop_protector = 0;
        while ( cause != null
                && infinite_loop_protector < 64 )
        {
            root_cause = cause;

            out.println ( "" );
            out.println ( "Caused by:" );
            out.println ( ClassName.of ( cause.getClass () ) );
            if ( cause instanceof CheckedViolation )
            {
                out.println ( "    (Checked violation)" );
            }
            else if ( cause instanceof UncheckedViolation )
            {
                out.println ( "    (Unchecked violation)" );
            }
            else if ( cause instanceof RuntimeException )
            {
                out.println ( "    (Unchecked exception)" );
            }
            else if ( cause instanceof Exception )
            {
                out.println ( "    (Checked exception)" );
            }

            if ( cause instanceof Violation )
            {
                // Violation: output as much detail as possible, but
                // no extra stack trace information beyond whatever
                // is included in the violation's description.
                final Violation cause_violation =
                    (Violation) cause;

                final Contract<?, ?> contract = cause_violation.contract ();
                final String maybe_same;
                if ( contract == last_contract )
                {
                    maybe_same = " (same as above)";
                }
                else
                {
                    maybe_same = "";
                }
                out.println ( "    Contract    = "
                              + contract + maybe_same );
                out.println ( "    Plaintiff   = "
                              + StringRepresentation.of (
                                    cause_violation.plaintiff (),
                                    0
                                    ) );
                out.println ( "    Evidence    = "
                              + StringRepresentation.of (
                                    cause_violation.evidence (),
                                    0
                                    ) );
                // Only output a detailed description of the contract
                // if it's not shared by the consequence Violation which
                // has already been printed.
                final String contract_description = contract.description ();
                if ( ! contract_description.equals ( last_contract_description ) )
                {
                    out.println ( "Contract:  "
                                  + contract_description );
                }
                final String violation_description =
                    cause_violation.description ();
                if ( ! violation_description.equals ( last_violation_description ) )
                {
                    out.println ( "Violation: "
                                  + violation_description );
                }

                last_contract = contract;
                last_contract_description = contract_description;
                last_violation_description = violation_description;
            }
            else // Not a Violation, just a regular Throwable.
            {
                // Throwable: output a truncated stack trace.
                String violation_description =
                    cause.getMessage ();
                if ( violation_description == null )
                {
                    violation_description = "(null)";
                }
                if ( ! violation_description.equals ( last_violation_description ) )
                {
                    out.println ( violation_description );
                }
                out.println ( "    At:" );
                boolean has_ellipsis = false;
                int st = 0;
                for ( StackTraceElement frame : cause.getStackTrace () )
                {
                    final String frame_summary =
                        summarizeStackFrame ( frame );
                    if ( frame_summary == null )
                    {
                        if ( ! has_ellipsis )
                        {
                            out.println ( "        ..." );
                            has_ellipsis = true;
                        }

                        continue;
                    }

                    out.println ( "        " + frame_summary );

                    has_ellipsis = false;
                    st ++;
                    if ( st >= MAXIMUM_STACK_FRAMES )
                    {
                        if ( ! has_ellipsis )
                        {
                            out.println ( "        ..." );
                            has_ellipsis = true;
                        }

                        break;
                    }
                }

                last_contract = null;
                last_contract_description = null;
                last_violation_description = violation_description;
            }

            cause = cause.getCause ();
            infinite_loop_protector ++;
        }

        // Output a truncated stack trace for the root cause of the violation.
        if ( root_cause != null )
        {
            out.println ( "At:" );
            boolean has_ellipsis = false;
            int st = 0;
            for ( StackTraceElement frame : root_cause.getStackTrace () )
            {
                final String frame_summary =
                    summarizeStackFrame ( frame );
                if ( frame_summary == null )
                {
                    if ( ! has_ellipsis )
                    {
                        out.println ( "    ..." );
                        has_ellipsis = true;
                    }

                    continue;
                }

                out.println ( "    " + frame_summary );

                has_ellipsis = false;
                st ++;
                if ( st >= MAXIMUM_STACK_FRAMES )
                {
                    if ( ! has_ellipsis )
                    {
                        out.println ( "    ..." );
                        has_ellipsis = true;
                    }

                    break;
                }
            }
        }

        out.flush ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( " (checked violation) {" );
        sbuf.append ( " contract = " + this.contract () );
        sbuf.append ( ", plaintiff   = "
                      + StringRepresentation.of (
                            this.plaintiff (),
                            StringRepresentation.DEFAULT_OBJECT_LENGTH
                            ) );
        sbuf.append ( ", evidence = "
                      + StringRepresentation.of (
                            this.evidence (),
                            StringRepresentation.DEFAULT_OBJECT_LENGTH
                            ) );
        sbuf.append ( " }" );
        return sbuf.toString ();
    }
}
