package musaico.foundation.metadata;

import java.io.Serializable;

import java.util.UUID;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * Records the host, process and thread origin of a reference object.
 * </p>
 *
 * <p>
 * For example, if a number of JVMs are running in a distributed
 * environment, then object A might have been created in JVM 1 thread 1,
 * object B might have been created in JVM 2 thread 1, and object C
 * might have been created in JVM 2 thread 2.
 * </p>
 *
 * <p>
 * This information is sometimes useful in message-passing systems.
 * For example in the distributed environment example above, a method
 * might send a request to JVM 1 to work on object A.  Or if a health
 * monitor determines that object B has been around for too long, then
 * JVM 2 thread 1 might be interrupted / killed.  And so on.
 * </p>
 *
 *
 * <p>
 * In Java, every metadatum must be Serializable in order
 * to play nicely over RMI.
 * </p>
 *
 * <p>
 * In Java, every metadatum must implement equals(Object), hashCode()
 * and toString().
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
public interface Origin
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** This JVM (system and process). */
    public static final String THIS_HOST_AND_PROCESS_ID =
        "" + UUID.randomUUID ();

    /** No Origin at all, a placebo. */
    public static final Origin NONE = new NoOrigin ();


    /** Every Origin must implement:
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Origin must implement:
     *  @see java.lang.Object#hashCode() */


    /**
     * @return The unique String representation of the machine and
     *         process in which the reference object originated.
     *         For example, the system and process might be represented
     *         as a URL in a distributed web environment.  Or it
     *         might simply be a randomly generated UUID, uniquely
     *         identifying the particular machine and process without
     *         providing any clues as to how to communicate with it.
     *         And so on.  Never null.
     */
    public abstract String hostAndProcessID ()
        throws ReturnNeverNull.Violation;


    /**
     * @return A String representation of the stack trace from
     *         which the reference object was created.
     *         Typically if the stack trace is supported then it
     *         will be a String version of <code> Thread.stackTrace () </code>
     *         from where the reference object was constructed.
     *         If not supported then "".  Never null.
     */
    public abstract String stackTrace ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The unique String representation of the thread in which
     *         the reference object originated, such as "1" or "2"
     *         and so on to identify the thread within a process by a
     *         unique number.  Never null.
     */
    public abstract String threadID ()
        throws ReturnNeverNull.Violation;
}
