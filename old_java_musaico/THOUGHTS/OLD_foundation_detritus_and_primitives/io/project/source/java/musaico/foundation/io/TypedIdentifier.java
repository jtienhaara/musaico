package musaico.foundation.io;

import java.io.Serializable;


/**
 * <p>
 * An Identifier which carries the type of object it refers to.
 * </p>
 *
 * <p>
 * For example, the name "Jane Doe" might be a unique TypedIdentifier<String>
 * within the Namespace of all "employees" for ACME Co.
 * </p>
 *
 *
 * <p>
 * In Java, every Identifier must be Serializable in order
 * to play nicely across RMI.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public interface TypedIdentifier<IDENTIFIED_OBJECT>
    extends Identifier
{
    /**
     * <p>
     * Returns the class of object identified by this identifier.
     * </p>
     *
     * @return This identifier's identified object class.  Never null.
     */
    public abstract Class<IDENTIFIED_OBJECT> identifiedObjectClass ();


    /**
     * <p>
     * Inspects the specified object and decides whether or not
     * it can be identified by this TypedIdentifier.
     * </p>
     *
     * @param object The object to inspect.  Must not be null.
     *
     * @return True if this identifier can be used to identify
     *         the specified object; false if it is not of the
     *         right type.
     */
    public abstract boolean isIdentifiable (
                                            Object object
                                            );
}
