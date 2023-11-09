package musaico.foundation.domain;

import java.io.Serializable;


/**
 * <p>
 * A domain of allowable values.  Typically used to specify obligations
 * involving parameter values, or guarantees involving return values.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must implement equals (), hashCode () and
 * toString ().
 * </p>
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
 * Copyright (c) 2013-2014 Johann Tienhaara
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
public interface Domain<TYPE extends Object>
    extends Serializable
{
    /** No domain at all, allows every value. */
    public static final Domain<Object> NONE = new NoDomain ();


    /**
     * <p>
     * Checks the specified value against this domain,
     * and returns true if it is a valid value, or false if not.
     * </p>
     *
     * @param value The value to check against this Domain.
     *              Because a domain might include the value null,
     *              Domain implementors must be very careful to
     *              guard against null values.
     *              Can be null.
     *
     * @return True if the specified value is a valid member of
     *         this Domain, false if it is not.
     */
    public abstract boolean isValid (
                                     TYPE value
                                     );
}
