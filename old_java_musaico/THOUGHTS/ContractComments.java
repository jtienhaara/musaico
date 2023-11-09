package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * A contract which must hold before, during and after the execution
 * of some code, and which can be statically inspected in order to
 * generate unit tests.
 * </p>
 *
 * <p>
 * A Contract is either an atomic Requirement in itself, or it is
 * made up of groups of Requirements: typically pre-conditions,
 * invariants and post-conditions.
 * </p>
 *
 * <p>
 * Each Contract can be tested at any time.  The Contract 
!!!!!!!!!!!!!!!!! by the system-wide
 * Arbiter.
 * </p>
 *
 * @see musaico.foundation.contract.Evaluator
 *
 * <p>
 * If the Evaluator decides that a Contract has not been met, then
 * the system-wide Arbiter is called upon to decide how to handle
 * the violation.
 * </p>
 *
 * @see musaico.foundation.contract.Arbiter
 *
 * <p>
 * Contracts, the Evaluator and the Arbiter can vary independently.
 * So depending on the system, all Requirements might be evaluated
 * and every violation enforced thoroughly, or
 * all Requirements might be evaluated as noOps for speed with
 * no violations and therefore no enforcement, or some combination
 * of the two.
 * </p>
 *
 * <p>
 * This flexibility allows the system builder to
 * decide whether to strictly evaluate and enforce Contracts at runtime,
 * or to let things blow up on the assumption that testing has
 * covered all possible failure scenarios, and so on.
 * </p>
 *
 * <p>
 * In Java a Contract is often built for each method of an interface,
 * and then checked at runtime by each class implementing the interface.
 * For example, if an interface <code> Account </code> has a method
 * <code> withdraw ( Money amount ): Money </code>, then the Contract
 * for the withdraw method might have pre-conditions (amount is not null,
 * amount is less than the remaining Account.balance ()) as well as
 * a post-condition (the Money returned must equal the amount parameter).
 * These conditions would be checked by the implementation, say
 * ChequingAccount or SavingsAccount, at the start and end of the
 * withdraw method implementation.  An external tool could also
 * inspect the Contracts for the Account interface, and test each
 * implementing class automatically to make sure that it rejects parameters
 * which do not match the caller's obligations and that it honours its
 * guarantees.
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
 * <pre>
 * Copyright (c) 2012 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public interface Contract
    extends Serializable
{
    /**
     * <p>
     * The result of checking a Contract against specific
     * arguments.
     * </p>
     */
    public static enum Result
    {
        OK,
        VIOLATED;
    }


    /**
     * <p>
     * Returns <code> Contract.Result.OK </code> if this Contract holds
     * for the specified arguments, <code> Contract.Result.VIOLATED </code>
     * if it is not met by the specified arguments.
     * </p>
     *
     * @param arguments The arguments to check.  Can contain nulls.
     *                  Must not be null, although the array can be empty.
     *
     * @return <code> Contract.Result.OK </code> if this Contract is met,
     *         <code> Contract.Result.VIOLATED </code> if it is not met
     *         by the specified arguments.
     */
    public abstract Contract.Result check (
                                           Object ... arguments
                                           );


    /**
     * <p>
     * Returns a human-readable identifier for this contract,
     * to aid in debugging.
     * </p>
     *
     * <p>
     * For example, a Contract for a method called "withdraw"
     * with one argument, in an interface called "Account", might
     * return the id: "Account.withdraw ( Money )".  Or a
     * "must not be null" contract might return "!= null".
     * And so on.
     * </p>
     *
     * @return A human-readable identifier for this contract.
     *         Never null.
     */
    public abstract String id ();
}
