package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * Creates a Violation of some Contract.
 * </p>
 *
 *
 * <p>
 * In Java every ViolationConstructor must be Serializable in order to play
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
 * @see musaico.foundation.contract.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.MODULE#LICENSE
 */
public interface ViolationConstructor<CONTRACT extends Contract<EVIDENCE, VIOLATION>, EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates a new Violation, without any root cause.
     * </p>
     *
     * @param contract The Contract that has been violated.
     *                 Must not be null.
     *
     * @param plaintiff The object under contract.  For
     *                  example, if object x has a method
     *                  obligation "parameters must not be
     *                  null" and someone calls x.method ( p )
     *                  with parameter p = null, then object x
     *                  would be the object under contract.
     *                  Must not be null.
     *
     * @param evidence_or_null The evidence, to be checked by
     *                         this contract.  For
     *                         example, if object x has a method
     *                         obligation "parameters must not be
     *                         null" and someone calls x.method ( p )
     *                         with parameter p = null, then parameter p
     *                         would be the evidence.
     *                         Can be null.  Can contain null elements.
     *
     * @param message The exact message of the violation to be
     *                constructed.  Must not be null.
     *
     * @throws NullPointerException If any of the non-nullable
     *                              parameters are null.
     */
    public abstract VIOLATION construct (
            CONTRACT contract,
            Object plaintiff,
            EVIDENCE evidence_or_null,
            String message
            );

    /**
     * <p>
     * Creates a new Violation, with the specified cause.
     * </p>
     *
     * @see java.lang.Throwable#initCause(java.lang.Throwable)
     *
     * @param contract The Contract that has been violated.
     *                 Must not be null.
     *
     * @param plaintiff The object under contract.  For
     *                  example, if object x has a method
     *                  obligation "parameters must not be
     *                  null" and someone calls x.method ( p )
     *                  with parameter p = null, then object x
     *                  would be the object under contract.
     *                  Must not be null.
     *
     * @param evidence_or_null The evidence, to be checked by
     *                         this contract.  For
     *                         example, if object x has a method
     *                         obligation "parameters must not be
     *                         null" and someone calls x.method ( p )
     *                         with parameter p = null, then parameter p
     *                         would be the evidence.
     *                         Can be null.  Can contain null elements.
     *
     * @param message The exact message of the violation to be
     *                constructed.  Must not be null.
     *
     * @param cause_or_null The Exception, contract Violation
     *                      or other Throwable which led to
     *                      this contract's violation, if any.
     *                      Can be null.
     *
     * @throws NullPointerException If any of the non-nullable
     *                              parameters are null.
     */
    public abstract VIOLATION construct (
            CONTRACT contract,
            Object plaintiff,
            EVIDENCE evidence_or_null,
            String message,
            Throwable cause_or_null
            );
}
