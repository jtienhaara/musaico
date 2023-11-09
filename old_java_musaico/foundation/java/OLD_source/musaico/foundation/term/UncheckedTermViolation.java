package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.UncheckedViolation;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * A violation of the contract(s) specified by some Term provider.
 * </p>
 *
 * <p>
 * The UncheckedTermViolation is an unchecked (runtime) exception
 * which can be induced by the consumer of a Term.  For example,
 * in case the consumer might receive an Error term, they may
 * choose to call <code> orThrowUnchecked () </code> in order
 * to fail early.
 * </p>
 *
 *
 * <p>
 * In Java every Violation must be Serializable in order to play
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public class UncheckedTermViolation
    extends UncheckedViolation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new UncheckedTermViolation.
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
    public UncheckedTermViolation (
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
     * Creates a new UncheckedTermViolation.
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
    public UncheckedTermViolation (
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
     * Creates a new UncheckedTermViolation from the specified unchecked one.
     * </p>
     *
     * @param checked The checked TermViolation to wrap.
     *                Must not be null.
     */
    public UncheckedTermViolation (
            TermViolation checked
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( checked == null
                    ? null
                    : checked.contract (),
                checked == null
                    ? null
                    : checked.description (),
                checked == null
                    ? null
                    : checked.plaintiff (),
                checked == null
                    ? null
                    : checked.evidence (),
                checked ); // cause
    }
}
