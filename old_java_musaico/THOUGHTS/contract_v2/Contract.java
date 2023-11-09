package musaico.foundation.contract;

import java.io.Serializable;


import musaico.foundation.domain.Domain;


/**
 * <p>
 * A contract between two or more parties, such as an obligation
 * to the caller of a method, or a guarantee made by the provider
 * of a method.
 * </p>
 *
 * <p>
 * A Contract can be implemented as a composite object which "has"
 * a Domain and an Enforcer, or it can be implemented as a standalone
 * object which "is" a Domain and an Enforcer, and returns itself from
 * the <code> domain () </code> and <code> enforcer () </code> methods.
 * Sometimes it is convenient to reuse existing Domains and Enforcers,
 * whereas other times it's better to avoid polluting a package with
 * three new classes that only serve one purpose: inspecting and
 * enforcing contract rules.  Every Contract which implements Domain
 * and/or Enforcer must follow the rules specified there (such as
 * implementing equals() and hashCode() and so on).
 * </p>
 *
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
 * <pre>
 * Copyright (c) 2012-2014 Johann Tienhaara
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
public interface Contract<INSPECTABLE extends Object, VIOLATION extends Violation>
    extends Serializable
{
    /**
     * @return The Domain of inspectable objects which comply with
     *         this Contract.  Never null.
     */
    public abstract Domain<INSPECTABLE> domain ();


    /**
     * @return The Enforcer for this Contract, which creates a violation
     *         for inspectable data.  Never null.
     */
    public abstract Enforcer<INSPECTABLE, VIOLATION> enforcer ();
}
