package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * A rule which resricts the values allowed for Instances.
 * so on.
 * </p>
 *
 * <p>
 * For example, an integer Type might be constrained in terms
 * of values (minimum - ( 2 * 2^32 ) and maximum ( 2 * 2 ^ 32 - 1 ) ),
 * or a credit card number Type might require certain formatting,
 * and so on.
 * </p>
 *
 *
 * <p>
 * In Java, all Constraints must be Serializable in order
 * to play nicely over RMI.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public interface Constraint
    extends Tag, Serializable
{
    /**
     * <p>
     * Applies the Constraint rule to the specified instance,
     * and throws a ConstraintException if the constraint is not met.
     * </p>
     *
     * @param instance The Instance to apply the rule to.
     *                 (Note that since Constraints are independent
     *                 of Type's and TypeSystem's, no check is
     *                 done to ensure this Constraint actually
     *                 applies to the Instance through its
     *                 Type or TypeSystem constraints.)
     *                 Must not be null.
     *
     * @return The Instance.  Never null.
     *
     * @throws ConstraintException If the Constraint rule is not met
     *                             by the specified Instance.
     *
     * @override musaico.types.Tag#check(Instance)
     */
    public abstract void check (
                                Instance instance
                                )
        throws ConstraintException;
}
