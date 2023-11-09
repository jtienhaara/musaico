package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * An object origin in a particular process on a particular host and a
 * particular thread.
 * </p>
 *
 *
 * <p>
 * In Java every metadatum must implement:
 * </p>
 *
 * @see java.lang.Object#equals(java.lang.Object)
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#toString()
 *
 * <p>
 * In Java every metadatum stored by a Metadata must be
 * Serializable in order to play nicely over RMI.
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class StandardOrigin
    implements Origin, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardOrigin.class );


    /** The host and process in which the referenced object originated. */
    private final String hostAndProcessID;

    /** The stack trace from which the referenced object was created. */
    private final String stackTrace;

    /** The thread in which the referenced object originated. */
    private final String threadID;


    /**
     * <p>
     * Creates a new StandardOrigin for this host and process and
     * the current thread.
     * </p>
     */
    public StandardOrigin ()
    {
        this ( Origin.THIS_HOST_AND_PROCESS_ID,
               StandardOrigin.stackTraceToString ( Thread.currentThread ()
                   .getStackTrace () ),
               "" + Thread.currentThread ().getId () );
    }


    /**
     * <p>
     * Creates a new StandardOrigin with the specified host and process
     * and thread ID.
     * </p>
     *
     * @param host_and_process_id The unique String representation
     *                            of the machine and process in
     *                            which the reference object originated.
     *                            For example, the system and process
     *                            might be represented as a URL in a
     *                            distributed web environment.  Or it
     *                            might simply be a randomly generated
     *                            UUID, uniquely identifying the
     *                            particular machine and process without
     *                            providing any clues as to how to
     *                            communicate with it.  Must not be null.
     *
     * @param stack_trace A String representation of the stack trace
     *                    from whence the referenced object was created.
     *                    Typically created with
     *                    <code> StandardOrigin.stackTraceToString ( Thread.currentThread ().getStackTrace () ) </code>
     *                    from the constructor of the referenced object.
     *                    Must not be null.
     *
     * @param thread_id The unique String representation of the thread
     *                  in which the reference object originated, such
     *                  as "1" or "2" and so on to identify the thread
     *                  within a process by a unique number.
     *                  Must not be null.
     */
    public StandardOrigin (
                           String host_and_process_id,
                           String stack_trace,
                           String thread_id
                           )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               host_and_process_id,
                               stack_trace,
                               thread_id );

        this.hostAndProcessID = host_and_process_id;
        this.stackTrace = stack_trace;
        this.threadID = thread_id;
    }


    /**
     * <p>
     * Converts the specified StackTraceElement array into a String.
     * </p>
     *
     * @param stack_trace The array of StackTraceElements to convert.
     *                    Must not be null.  Must not contain any nulls.
     *
     * @return The String representing the specified stack trace.
     *         Never null.
     */
    public static String stackTraceToString (
                                             StackTraceElement [] stack_trace
                                             )
    {
        final StringBuilder sbuf = new StringBuilder ();
        final int stop_index;
        final int start_again_index;
        if ( stack_trace.length > 8 )
        {
            stop_index = 6;
            start_again_index = stack_trace.length - 3;
        }
        else
        {
            stop_index = stack_trace.length;
            start_again_index = stop_index;
        }

        int sf = 0;
        for ( StackTraceElement frame : stack_trace )
        {
            if ( sf >= stop_index
                 && sf < start_again_index )
            {
                if ( sf == stop_index )
                {
                    sbuf.append ( "...\n" );
                }

                sf ++ ;
                continue;
            }

            sbuf.append ( "    " );
            sbuf.append ( frame.getClassName () );
            sbuf.append ( "." );
            sbuf.append ( frame.getMethodName () );
            sbuf.append ( " [" );
            sbuf.append ( frame.getFileName () );
            sbuf.append ( " line " );
            sbuf.append ( frame.getLineNumber () );
            sbuf.append ( "]\n" );

            sf ++;
        }

        return sbuf.toString ();
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
        else if ( ! ( object instanceof Origin ) )
        {
            return false;
        }

        final Origin that = (Origin) object;
        if ( ! this.hostAndProcessID.equals ( that.hostAndProcessID () )
             || ! this.stackTrace.equals ( that.stackTrace () )
             || ! this.threadID.equals ( that.threadID () ) )
        {
            return false;
        }

        // Everything matches.
        return true;
    }


    /**
     * @see java.lang.hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.hostAndProcessID.hashCode () * 31
            + this.stackTrace ().hashCode () * 17
            + this.threadID.hashCode ();
    }


    /**
     * @see musaico.foundation.metadata.Origin#hostAndProcessID()
     */
    @Override
    public String hostAndProcessID ()
        throws ReturnNeverNull.Violation
    {
        return this.hostAndProcessID;
    }


    /**
     * @see musaico.foundation.metadata.Origin#stackTrace()
     */
    @Override
    public String stackTrace ()
        throws ReturnNeverNull.Violation
    {
        return this.stackTrace;
    }


    /**
     * @see musaico.foundation.metadata.Origin#thread()
     */
    @Override
    public String threadID ()
        throws ReturnNeverNull.Violation
    {
        return this.threadID;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " " + this.hostAndProcessID ()
            + " " + this.threadID ()
            + "\n" + this.stackTrace ();
    }
}
