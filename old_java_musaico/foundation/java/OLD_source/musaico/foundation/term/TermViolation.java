package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A checked exception thrown by a Term to indicate something went
 * wrong (including, possibly, that the Term does not meet
 * one of its Type's constraints).
 * </p>
 *
 *
 * <p>
 * In Java, every TermViolation must be Serializable
 * in order to play nicely across RMI.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public class TermViolation
    extends CheckedViolation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TermViolation.
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
     * @param evidence The Term which violated the contract.
     *                 Can be null.
     */
    public TermViolation (
                           Contract<?, ?> contract,
                           String description,
                           Object plaintiff,
                           Term<?> evidence
                           )
        throws ParametersMustNotBeNull.Violation
    {
        super ( contract,
                description,
                plaintiff,
                evidence );
    }


    /**
     * <p>
     * Creates a new TermViolation.
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
     * @param evidence The Term which violated the contract.
     *                 Can be null.
     *
     * @param cause The Throwable which caused this contract violation.
     *              Can be null.
     */
    public TermViolation (
                           Contract<?, ?> contract,
                           String description,
                           Object plaintiff,
                           Term<?> evidence,
                           Throwable cause
                           )
        throws ParametersMustNotBeNull.Violation
    {
        super ( contract,
                description,
                plaintiff,
                evidence,
                cause );
    }


    /**
     * <p>
     * Creates a new TermViolation from the specified violation+exception.
     * </p>
     *
     * @param violation The root cause of this TermViolation.
     *                  Must not be null.
     */
    public <VIOLATION extends Throwable & Violation>
                              TermViolation (
                                              VIOLATION violation
                                              )
        throws ParametersMustNotBeNull.Violation
    {
        super ( violation == null
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
                violation ); // cause
    }
}
