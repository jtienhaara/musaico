package musaico.kernel.module;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Reference;
import musaico.io.TypedIdentifier;

import musaico.io.references.SimpleSoftReference;
import musaico.io.references.Version;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * A nothing-fancy implementation of the ModuleIdentifier, representing
 * a name and version for a Module.
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
public class SimpleModuleIdentifier
    implements ModuleIdentifier, Serializable
{
    /** The name of the uniquely identified Module. */
    private final String moduleName;

    /** The version of the uniquely identified Module. */
    private final Version version;

    /** The Reference returned by this Identifier.name (),
     *  combining the module name and the version. */
    private final Reference nameReference;

    /** The hash code for this module id. */
    private final int hashCode;


    /**
     * <p>
     * Creates a new SimpleModuleIdentifier with the specified
     * Module name and version number.
     * </p>
     *
     * @param module_name The name of the Module.  Must not be null.
     *
     * @param version The version number of the Module.
     *                Must not be null.
     */
    public SimpleModuleIdentifier (
                                   String module_name,
                                   Version version
                                   )
    {
        if ( module_name == null
             || version == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create SimpleModuleIdentifier with module name [%module_name%] version [%version%]",
                                                     "module_name", module_name,
                                                     "version", version );
        }

        this.moduleName = module_name;
        this.version = version;

        this.nameReference =
            new SimpleSoftReference<String> ( this.moduleName
                                              + "-" + this.version );

        this.hashCode =
            31 * this.moduleName.hashCode ()
            + this.version.hashCode ();
    }


    /**
     * @see musaico.io.Reference#equals(Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( ! ( obj instanceof ModuleIdentifier ) )
        {
            return false;
        }

        String my_module_name = this.moduleName ();
        Version my_version = this.version ();

        if ( my_module_name == null
             || my_version == null )
        {
            return false;
        }

        ModuleIdentifier other = (ModuleIdentifier) obj;
        String other_module_name = other.moduleName ();
        Version other_version = other.version ();

        if ( other_module_name == null
             || other_version == null )
        {
            return false;
        }

        if ( my_module_name.equals ( other_module_name )
             && my_version.equals ( other_version ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.io.Reference#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see musaico.io.TypedIdentifier#identifiedObjectClass()
     */
    @Override
    public Class<Module> identifiedObjectClass ()
    {
        return Module.class;
    }


    /**
     * @see musaico.io.TypedIdentifier#identifiedObjectClass()
     */
    @Override
    public boolean isIdentifiable (
                                   Object object
                                   )
    {
        return Module.class.isInstance ( object );
    }


    /**
     * @see musaico.module.ModuleIdentifier#moduleName()
     */
    @Override
    public final String moduleName ()
    {
        return this.moduleName;
    }


    /**
     * @see musaico.io.Identifier#name()
     */
    @Override
    public Reference name ()
    {
        return this.nameReference;
    }


    /**
     * @see musaico.io.Identifier#parentNamespace()
     */
    @Override
    public Identifier parentNamespace ()
    {
        return KernelNamespaces.MODULES;
    }


    /**
     * <p>
     * Parses the specified String of the format
     * "module_name-version" into a ModuleIdentifier.
     * </p>
     *
     * <p>
     * For example, the string might look like:
     * <code> musaico_xml_driver-1.0.0 </code>, in which case
     * a ModuleIdentifier with name "musaico_xml_driver" and Version 1.0.0
     * is created.
     * </p>
     *
     * @param module_id_string The string to parse.  Must not be null.
     *
     * @return The parsed ModuleIdentifier.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     */
    public static ModuleIdentifier parse (
                                          String module_id_string
                                          )
    {
        try
        {
            int version_index = module_id_string.lastIndexOf ( '-' );
            String module_name =
                module_id_string.substring ( 0, version_index );
            String version_string =
                module_id_string.substring ( version_index + 1 );
            Version version = new Version ( version_string );
            return new SimpleModuleIdentifier ( module_name, version );
        }
        catch ( Exception e )
        {
            throw new I18nIllegalArgumentException ( "Could not parse [%module_id_string%] into a ModuleIdentifier",
                                                     "module_id_string", module_id_string,
                                                     "cause", e );
        }
    }


    /**
     * @see musaico.io.Reference#toString()
     */
    @Override
    public String toString ()
    {
        return this.moduleName + "-" + this.version;
    }


    /**
     * @see musaico.module.ModuleIdentifier#version()
     */
    @Override
    public Version version ()
    {
        return this.version;
    }
}
