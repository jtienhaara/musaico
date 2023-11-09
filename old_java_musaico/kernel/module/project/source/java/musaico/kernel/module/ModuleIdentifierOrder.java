package musaico.kernel.module;

import java.io.Serializable;


import musaico.io.AbstractOrder;
import musaico.io.Comparison;
import musaico.io.Order;

import musaico.io.references.Version;
import musaico.io.references.VersionOrder;


/**
 * <p>
 * Compares two ModuleIdentifiers (the unique identifiers of Modules),
 * typically in order to sort them.
 * </p>
 *
 * <p>
 * All ModuleIdentifiers are comparable:
 * </p>
 *
 * <ol>
 *   <li> First by their names (dictionary ordering); and </li>
 *   <li> Second by their versions. </li>
 * </ol>
 *
 * <p>
 * Thus, module "disorgandizer v1.0" might be considered "less than" module
 * "organdizer v1.0".  Module "organdizer v1.0" should definitely compare
 * as less than "organdizer v2.0".
 * </p>
 *
 *
 * <p>
 * In Java every Order must be Serializable in order to play nicely
 * over RMI.
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
 * Copyright (c) 2010, 2012 Johann Tienhaara
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
public class ModuleIdentifierOrder
    extends AbstractOrder<ModuleIdentifier>
    implements Serializable
{
    /** The standard module identifier order (by dictionary name
     *  then by version #). */
    public static final ModuleIdentifierOrder STANDARD =
        new ModuleIdentifierOrder ( "Module identifier order" );


    /**
     * <p>
     * Creates a new ModuleIdentifierOrder with the specified
     * internationalized description text.
     * </p>
     *
     * <p>
     * Make sure you put internationalized_description_text
     * into your Messages.properties file, and translate it
     * for other locales!
     * </p>
     *
     * @param internationalized_description_text The international
     *                                           identifier for the
     *                                           description message
     *                                           for this order.
     *                                           This text will be
     *                                           internationalized, and
     *                                           then the Messages.properties
     *                                           files will be searched any
     *                                           time the description of
     *                                           this Order is localized
     *                                           (for example, by a client
     *                                           application).
     *                                           Must not be null.
     */
    protected ModuleIdentifierOrder (
                                     String internationalized_description_text
                                     )
    {
        super ( internationalized_description_text );
    }


    /**
     * @see musaico.io.Order#compareValues(java.lang.Object,java.lang.Object)
     *
     * Final for speed.
     */
    public final Comparison compareValues (
                                           ModuleIdentifier id1,
                                           ModuleIdentifier id2
                                           )
    {
        if ( id1 == null
             && id2 == null )
        {
            return Comparison.LEFT_EQUALS_RIGHT;
        }
        else if ( id1 == null )
        {
            // null > id2
            return Comparison.LEFT_GREATER_THAN_RIGHT;
        }
        else if ( id2 == null )
        {
            // id1 < null
            return Comparison.LEFT_LESS_THAN_RIGHT;
        }

        // Compare 1st by module name.
        String id1_module_name = id1.moduleName ();
        String id2_module_name = id2.moduleName ();

        final Comparison name_comparison =
            Order.DICTIONARY.compareValues ( id1_module_name,
                                             id2_module_name );
        if ( ! name_comparison.equals ( Comparison.LEFT_EQUALS_RIGHT ) )
        {
            // id1.name <> id2.name.
            return name_comparison;
        }

        // Now compare by version.
        Version id1_version = id1.version ();
        Version id2_version = id2.version ();

        final Comparison version_comparison =
            VersionOrder.DEFAULT.compareValues ( id1_version,
                                                 id2_version );

        return version_comparison;
    }
}
