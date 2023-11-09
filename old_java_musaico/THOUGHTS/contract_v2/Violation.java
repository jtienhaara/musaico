package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * A breach of contract, typically implemented either as a checked
 * Exception or as an unchecked RuntimeException.
 * </p>
 *
 *
 * <p>
 * In Java, every Violation must be Serializable in order to play
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
 * <pre>
 * Copyright (c) 2012, 2013 Johann Tienhaara
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
public interface Violation
    extends Throwable, Serializable
{
    /**
     * <p>
     * Returns the contract which was breached.
     * </p>
     *
     * @return The contract which was breached.  Never null.
     */
    public abstract Contract<?, ?> contract ();


    /**
     * <p>
     * Returns the object which was under contract (or a
     * Serializable representation of it, in case it was not
     * Serializable itself).
     * </p>
     *
     * @return The object under contract, or its Serializable representation.
     *         Never null.
     */
    public abstract Serializable objectUnderContract ();


    /**
     * <p>
     * Returns the inspectable data which caused the contract to be
     * breached, possibly including Serializable representations of
     * inspectable data which was not Serializable.
     * </p>
     *
     * <p>
     * If the contract took one inspectable datum, then an array of
     * size one is returned.  If it took an array, or varargs, then
     * the appropriate length array is returned.
     * </p>
     *
     * @return The inspectable data which violated the contract,
     *         or Serializable representation(s) of that data.
     *         Never null.  Never contains any null elements.
     */
    public abstract Serializable [] inspectableData ();
}
