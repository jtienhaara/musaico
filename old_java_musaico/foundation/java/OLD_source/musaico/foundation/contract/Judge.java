package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * Decides when to inspect contracts for possible violations,
 * and how to enforce them (such as by throwing runtime exceptions,
 * stopping the entire system, logging an error then carrying on
 * and letting things blow up, and so on).
 * </p>
 *
 *
 * <p>
 * In Java, every Judge must be Serializable in order to play
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
public interface Judge
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The default Judge, for systems that do no configured
     *  specific Judges. */
    public static final StandardJudge DEFAULT =
        new StandardJudge ();

    /** No Judge at all.  Does nothing whenever asked to check evidence. */
    public static final NoJudge NONE =
        new NoJudge ();


    /**
     * <p>
     * Inspects and then enforces the specified contract for the specified
     * object and evidence.
     * </p>
     *
     * <p>
     * If the contract has been breached and this arbiter enforces
     * contracts by throwing exceptions, then a violation will
     * be thrown.
     * </p>
     *
     * @param contract The contract to inspect.  Must not be null.
     *
     * @param plaintiff The object which is under contract.
     *                  Must not be null.
     *
     * @param evidence The data which might breach the contract.
     *                 Must not be null.
     *
     * @throws ContractViolation If the arbiter has inspected the contract,
     *                           determined it was breached,
     *                           and enforced the contract by throwing
     *                           a runtime exception.
     */
    public abstract
        <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
            void inspect (
                          Contract<EVIDENCE, VIOLATION> contract,
                          Object plaintiff,
                          EVIDENCE evidence
                          )
        throws VIOLATION;
}
