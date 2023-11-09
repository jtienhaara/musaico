package musaico.foundation.contract;

import java.io.Serializable;


import musaico.foundation.filter.Filter;


/**
 * <p>
 * A contract between two or more parties, such as an obligation
 * to the caller of a method, or a guarantee made by the provider
 * of a method, or the multiple parties involved in an exchange
 * of money for goods and services, and so on.
 * </p>
 *
 * <p>
 * A Contract governs some kind of evidence.  To determine
 * whether specific evidence lives up to the contract's rules,
 * call <code> Contract.filter ( ... ) </code> with the
 * evidence.  If the evidence is
 * KEPT by the contract, then the data passes inspection.
 * If it is DISCARDED, then it violates the contract.  Evidence
 * which violates a contract can be passed to the contract's
 * <code> violation ( ... ) </code> method, which creates an
 * exception detailing why the evidence is not valid.
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
 * @see musaico.foundation.contract.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.MODULE#LICENSE
 */
public interface Contract<EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
    extends Filter<EVIDENCE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No Contract at all. */
    public static final Contract<Object, UncheckedViolation> NONE =
        new NoContract ();


    /**
     * @return A human-readable description of this Contract.  This
     *         description is for developers and maintainers and so
     *         is not internationalized or localized for users.
     *         It is meant to provide context at runtime when
     *         the system enters an exceptional or error state,
     *         to aid in troubleshooting, tracing and debugging efforts.
     *         Never null.
     */
    public abstract String description ();


    /**
     * <p>
     * Creates a violation of this Contract by the
     * specified evidence.
     * </p>
     *
     * <p>
     * The violation is created whenever a caller asks for one,
     * regardless of whether or not the evidence adheres
     * to this contract.
     * </p>
     *
     * <p>
     * This method never checks the evidence for contract
     * validity.  Use
     * <code> Contract.filter ( evidence ) </code>
     * instead (KEPT is valid data, DISCARDED indicates a violation
     * that should be enforced).  This method always returns a
     * violation, even if the caller is daft enough to pass in
     * valid evidence.
     * </p>
     *
     * @param plaintiff The object under contract.  For
     *                  example, if object x has a method
     *                  obligation "parameters must not be
     *                  null" and someone calls x.method ( p )
     *                  with parameter p = null, then object x
     *                  would be the object under contract.
     *                  Must not be null.
     *
     * @param evidence The evidence, to be checked by
     *                 this contract.  For
     *                 example, if object x has a method
     *                 obligation "parameters must not be
     *                 null" and someone calls x.method ( p )
     *                 with parameter p = null, then parameter p
     *                 would be the evidence.
     *                 Can be null.  Can contain null elements.
     *
     * @return The newly created violation of this Contract
     *         by the specified evidence, enforced for the
     *         specified object under contract.  Even if the
     *         evidence is legal by this contract,
     *         a non-null violation will be returned, citing the
     *         specific evidence as the violator.  Never null.
     */
    public abstract VIOLATION violation (
                                         Object plaintiff,
                                         EVIDENCE evidence
                                         );


    /**
     * <p>
     * Convenience method.  Has exactly the same effect as:
     * </p>
     *
     * <pre>
     *     final XYZViolation violation =
     *         xyz_contract.violation ( plaintiff, evidence );
     *     violation.initCause ( cause );
     * </pre>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     *
     * @see java.lang.Throwable#initCause(java.lang.Throwable)
     *
     * @param plaintiff The object under contract.  For
     *                  example, if object x has a method
     *                  obligation "parameters must not be
     *                  null" and someone calls x.method ( p )
     *                  with parameter p = null, then object x
     *                  would be the object under contract.
     *                  Must not be null.
     *
     * @param evidence The evidence, to be checked by
     *                 this contract.  For
     *                 example, if object x has a method
     *                 obligation "parameters must not be
     *                 null" and someone calls x.method ( p )
     *                 with parameter p = null, then parameter p
     *                 would be the evidence.
     *                 Can be null.  Can contain null elements.
     *
     * @param cause The Exception, contract Violation or other Throwable
     *              which led to this contract's violation, if any.
     *              Can be null.
     *
     * @return The newly created violation of this Contract
     *         by the specified evidence, enforced for the
     *         specified object under contract, and with the
     *         specified cause.  Even if the
     *         evidence is legal by this Contract,
     *         a non-null violation will be returned, citing the
     *         specific evidence as the violator.  Never null.
     */
    public abstract VIOLATION violation (
                                         Object plaintiff,
                                         EVIDENCE evidence,
                                         Throwable cause
                                         );
}
