package musaico.kernel.module;


import java.io.Serializable;


import musaico.io.TypedIdentifier;

import musaico.io.references.Version;


/**
 * <p>
 * A unique identifier for Modules comprising the Module's name
 * and version.
 * </p>
 *
 *
 * <p>
 * In Java, every ModuleIdentifier and its contents must be Serializable
 * in order to play nicely across RMI.
 * </p>
 *
 * <p>
 * The <code> equals () </code> and <code> hashCode () </code>
 * methods must be overrided by the ModuleIdentifier
 * implementation, so that ModuleIdentifiers can be safely placed
 * in hash tables.
 * </p>
 *
 * <p>
 * Generally it is a good idea to override the <code> toString () </code>
 * method, too, to aid in debugging.
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
 * Copyright (c) 2010 Johann Tienhaara
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
public interface ModuleIdentifier 
    extends TypedIdentifier<Module>, Serializable
{
    /** Illegal reference to non-existant module. */
    public static final ModuleIdentifier NONE =
        new SimpleModuleIdentifier ( "no_module", new Version ( "0" ) );


    /**
     * <p>
     * Returns the name of the Module.
     * </p>
     *
     * <p>
     * Modules are uniquely identified in a kernel by (name,version).
     * </p>
     *
     * @return The name of the Module.  Never null.
     */
    public abstract String moduleName ();


    /**
     * <p>
     * Returns the version of the Module.
     * </p>
     *
     * <p>
     * Modules are uniquely identified in a kernel by (name,version).
     * </p>
     *
     * @return The version of the module.  Never null.
     */
    public abstract Version version ();
}
