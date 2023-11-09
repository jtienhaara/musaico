package musaico.kernel.module;


import java.util.Comparator;


import musaico.io.ComparableReference;


/**
 * <p>
 * Compares two ModuleIdentifiers (the unique identifiers of Modules),
 * typically in order to sort them.
 * </p>
 *
 * <p>
 * ModuleIdentifiers are comparable:
 * </p>
 *
 * <ol>
 *   <li> First by their names (straight character value comparison is fine);
 *        and </li>
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
public class ModuleIdentifierComparator
    implements Comparator<ModuleIdentifier>
{
    /**
     * @see java.lang.Comparator#compare(T,T)
     *
     * Final for speed.
     */
    public final int compare (
                              ModuleIdentifier id1,
                              ModuleIdentifier id2
                              )
    {
        // <= -1 id1 < id2
        //     0 id1 == id2
        // >=  1 id1 > id2
        if ( id1 == null
             && id2 == null )
        {
            return 0;
        }
        else if ( id1 == null )
        {
            // null > id2
            return 1;
        }
        else if ( id2 == null )
        {
            // id1 < null
            return -1;
        }

        // Compare 1st by module name.
        String id1_module_name = id1.moduleName ();
        String id2_module_name = id2.moduleName ();

        if ( id1_module_name == null
             && id2_module_name == null )
        {
            // null module name == null module name.
            return 0;
        }
        else if ( id1_module_name == null )
        {
            // null module name > id2_module_name.
            return 1;
        }
        else if ( id2_module_name == null )
        {
            // id1_module_name < null module name.
            return -1;
        }

        int module_name_comparison =
            id1_module_name.compareTo ( id2_module_name );
        if ( module_name_comparison != 0 )
        {
            return module_name_comparison;
        }

        // Now compare by version.
        ComparableReference id1_version = id1.version ();
        ComparableReference id2_version = id2.version ();

        if ( id1_version == null
             && id2_version == null )
        {
            // null version == null version.
            return 0;
        }
        else if ( id1_version == null )
        {
            // null version > id2_version.
            return 1;
        }
        else if ( id2_version == null )
        {
            // id1_version < null version.
            return -1;
        }

        return id1_version.compareTo ( id2_version );
    }
}
