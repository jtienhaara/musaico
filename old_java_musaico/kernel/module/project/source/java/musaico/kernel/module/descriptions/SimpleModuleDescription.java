package musaico.kernel.module.descriptions;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.i18n.message.MessageFormat;

import musaico.i18n.text.I18nString;

import musaico.kernel.module.ModuleDescription;
import musaico.kernel.module.ModuleIdentifier;
import musaico.kernel.module.SimpleModuleIdentifier;

import musaico.time.Time;


/**
 * <p>
 * A detailed description of a Module, including authors,
 * licenses and so on.
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
public class SimpleModuleDescription
    implements ModuleDescription, Serializable
{
    /** Parses and formats internationalized text for us,
     *  turning [%module_id%] into our module identifier, and so on. */
    private final MessageFormat format;

    /** Lookup of ModuleDescription parameter value by name.
     *  We don't care about order, we just sort alphabetically
     *  when someone requests the parameter names.
     *  MUST BE KEPT IMMUTABLE AFTER THE CONSTRUCTOR! */
    private final Map<String,Serializable> parameters =
        new HashMap<String,Serializable> ();


    /**
     * <p>
     * Creates a new SimpleModuleDescription.
     * </p>
     *
     * @param format The formatter for internationalized Strings,
     *               including license text.  Should generally be a
     *               musaico.i18n.message.StandardMessageFormat.
     *               Must not be null.
     *
     * @param args The (String parameter name, Serializable value)
     *             pairs describing the module.  Must not be null.
     *             May include null values, but no null parameter
     *             names are allowed.  All parameter names listed in
     *             ModuleDescription.REQUIRED must be present and
     *             have non-null values.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     */
    public SimpleModuleDescription (
                                    MessageFormat format,
                                    Serializable... args
                                    )
        throws I18nIllegalArgumentException
    {
        if ( format == null
             || args == null
             || ( args.length % 2 ) != 0 )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleModuleDescription with format [%message_format%] description parameters [%description_args%]",
                                                     "message_format", format,
                                                     "description_parameters", args );
        }

        this.format = format;

        for ( int a = 0; a < ( args.length - 1 ); a += 2 )
        {
            if ( args [ a ] == null
                 || ! ( args [ a ] instanceof String ) )
            {
                throw new I18nIllegalArgumentException ( "Invalid parameters while constructing SimpleModuleDescription with [%description_args%]",
                                                         "description_args", args );
            }

            String name = (String) args [ a ];
            Serializable value = args [ a + 1 ];

            if ( value != null )
            {
                value = this.parseAndValidateParameter ( name, value );
                this.parameters.put ( name, value );
            }
        }

        this.fillInLicense ();
        this.fillInOptionalParameters ();
        SimpleModuleDescription.validateDescription ( this );
    }


    /**
     * <p>
     * Creates a new SimpleModuleDescription.
     * </p>
     *
     * @param format The formatter for internationalized Strings,
     *               including license text.  Should generally be a
     *               musaico.i18n.message.StandardMessageFormat.
     *               Must not be null.
     *
     * @param properties The Properties describing the module.
     *                   Must not be null.  May include null values.
     *                   All parameter names listed in
     *                   ModuleDescription.REQUIRED must be present
     *                   and have non-null values.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid
     *                                      (see above).
     */
    public SimpleModuleDescription (
                                    MessageFormat format,
                                    Properties properties
                                    )
        throws I18nIllegalArgumentException
    {
        if ( format == null
             || properties == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleModuleDescription with format [%message_format%] description parameters [%properties%]",
                                                     "message_format", format,
                                                     "properties", properties );
        }

        this.format = format;

        Iterator<String> names =
            properties.stringPropertyNames ().iterator ();
        while ( names.hasNext () )
        {
            String name = names.next ();
            String value = properties.getProperty ( name );

            if ( value != null )
            {
                Serializable parsed_value =
                    this.parseAndValidateParameter ( name, value );
                this.parameters.put ( name, parsed_value );
            }
        }

        this.fillInLicense ();
        this.fillInOptionalParameters ();
        SimpleModuleDescription.validateDescription ( this );
    }


    /**
     * <p>
     * Creates the license text, if necessary, from the license
     * type(s).
     * </p>
     *
     * @throws I18nIllegalArgumentException If the license
     *                                      parameter is broken.
     */
    private void fillInLicense ()
        throws I18nIllegalArgumentException
    {
        // Fill in the module license with copyrights etc.
        Serializable unvalidated =
            this.parameter ( ModuleDescription.LICENSE_TEXT );
        if ( unvalidated == null )
        {
            unvalidated =
                this.parameter ( ModuleDescription.LICENSE_TYPES );
            if ( unvalidated == null
                 || ! ( unvalidated instanceof License [] )
                 || ( (License []) unvalidated ).length == 0 )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a SimpleModuleDescription with license types [%license_types%]",
                                                         "license_types", unvalidated );
            }

            License [] license_types = (License []) unvalidated;
            I18nString [] license_texts =
                new I18nString [ license_types.length ];
            for ( int l = 0; l < license_types.length; l ++ )
            {
                license_texts [ l ] = license_types [ l ].fullText ( this );
            }

            if ( license_texts.length == 1 )
            {
                // Easy enough.
                this.parameters.put ( ModuleDescription.LICENSE_TEXT,
                                      license_texts [ 0 ] );
            }
            else
            {
                // Need to concatenate all the license texts.
                StringBuilder full_text = new StringBuilder ();
                Object [] license_args =
                    new Object [ license_texts.length * 2 ];
                int la = 0;
                for ( int l = 0; l < license_texts.length; l ++ )
                {
                    license_args [ la ] = "license_text" + l;
                    license_args [ la + 1 ] = license_texts [ l ];

                    if ( l > 0 )
                    {
                        full_text.append ( "\n\n" );
                    }
                    full_text.append ( "[%" + license_args [ la ] + "%]" );

                    la ++;
                }

                I18nString concatenated_licenses =
                    new I18nString ( this.format,
                                     full_text.toString (),
                                     license_args );
                this.parameters.put ( ModuleDescription.LICENSE_TEXT,
                                      concatenated_licenses );
            }
        }
    }


    /**
     * <p>
     * Make sure we always include the standard parameters, even
     * if they are "".  This makes the output toString() a lot
     * prettier.
     * </p>
     */
    private void fillInOptionalParameters ()
    {
        for ( int op = 0; op < ModuleDescription.OPTIONAL.length; op ++ )
        {
            String name = ModuleDescription.OPTIONAL [ op ];
            if ( ! this.parameters.containsKey ( name ) )
            {
                Serializable value =
                    this.parseAndValidateParameter ( name, "" );
                this.parameters.put ( name, value );
            }
        }
    }


    /**
     * @see musaico.message.Message#format()
     */
    public MessageFormat format()
    {
        return this.format;
    }


    /**
     * @see musaico.message.Message#id()
     */
    public String id ()
    {
        return "" + this.parameters.get ( ModuleDescription.ID );
    }


    /**
     * <p>
     * Checks the specified parameter and the specified value, possibly
     * parsing String values where necessary (for example copyrights
     * and license types can be parsed into objects), and returning
     * the validated / parsed value.
     * </p>
     *
     * @throws I18nIllegalArgumentException If the specified parameter
     *                                      name or value is invalid,
     *                                      such as a wrong value class.
     */
    private Serializable parseAndValidateParameter (
                                                    String name,
                                                    Serializable value
                                                    )
        throws I18nIllegalArgumentException
    {
        if ( name == null
             || value == null )
        {
            // We'll validate all the parameters later.
            // For now there's nothing more we can do
            // for this parameter.
            return value;
        }

        // !!! This is horrid.  I should have put all the classes
        // !!! into a map and then selected on the class in the
        // !!! map.  No time to change right now though...

        // In order to avoid throwing exceptions from a billion
        // different places in these branches, anything that falls
        // out of this if block throws the same exception.
        // If the value is valid, make sure to return from
        // insdie the if block!
        if ( name.equals ( ModuleDescription.AUTHORS )
             || name.equals ( ModuleDescription.MAINTAINERS )
             || name.equals ( ModuleDescription.URLS_SEE_ALSO ) )
        {
            if ( value instanceof String [] )
            {
                // Leave as-is.
                return value;
            }
            else if ( value instanceof String )
            {
                // Parse the string separated by commas.
                // We don't care about trailing commas here.
                String [] split_values = ( (String) value ).split ( "," );
                return split_values;
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.CONFIGURATION )
                  || name.equals ( ModuleDescription.LICENSE_TEXT )
                  || name.equals ( ModuleDescription.MANUAL )
                  || name.equals ( ModuleDescription.OVERVIEW )
                  || name.equals ( ModuleDescription.SYNOPSIS_USAGE ) )
        {
            if ( value instanceof I18nString )
            {
                return value;
            }
            else if ( value instanceof String )
            {
                return new I18nString ( this.format, (String) value );
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.COPYRIGHTS ) )
        {
            if ( value instanceof Copyright [] )
            {
                return value;
            }
            else if ( value instanceof Copyright )
            {
                // Turn it into an array.
                return new Copyright [] { (Copyright) value };
            }
            else if ( value instanceof String )
            {
                return Copyright.parse ( (String) value );
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( DEPENDENCIES ) )
        {
            if ( value instanceof ModuleIdentifier [] )
            {
                return value;
            }
            else if ( value instanceof ModuleIdentifier )
            {
                return new ModuleIdentifier [] { (ModuleIdentifier) value };
            }
            else if ( value instanceof String )
            {
                // We don't care about trailing commas.
                String [] module_id_strings = ( (String) value ).split ( "," );
                ModuleIdentifier[] dependencies =
                    new ModuleIdentifier [ module_id_strings.length ];
                for ( int d = 0; d < module_id_strings.length; d ++ )
                {
                    String trimmed_module_id = module_id_strings [ d ].trim ();
                    if ( trimmed_module_id.equals ( "" ) )
                    {
                        continue;
                    }

                    dependencies [ d ] =
                        SimpleModuleIdentifier.parse ( trimmed_module_id );
                }

                return dependencies;
            }
            else if ( value instanceof String [] )
            {
                String [] module_id_strings = (String []) value;
                ModuleIdentifier[] dependencies =
                    new ModuleIdentifier [ module_id_strings.length ];
                for ( int d = 0; d < module_id_strings.length; d ++ )
                {
                    dependencies [ d ] =
                        SimpleModuleIdentifier.parse ( module_id_strings [ d ] );
                }

                return dependencies;
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.DEPRECATED_SINCE ) )
        {
            if ( value instanceof Time )
            {
                return value;
            }
            // !!! WE SHOULD REALLY PARSE STRING INTO AbsoluteTime.
            else if ( "".equals ( value ) )
            {
                // No deprecated since specified.
                return Time.NEVER;
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.ID )
                  || name.equals ( ModuleDescription.REPLACED_BY_MODULE ) )
        {
            if ( value instanceof ModuleIdentifier )
            {
                return value;
            }
            else if ( "".equals ( value ) )
            {
                return ModuleIdentifier.NONE;
            }
            else if ( value instanceof String )
            {
                return SimpleModuleIdentifier.parse ( (String) value );
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.LICENSE_TYPES ) )
        {
            if ( value instanceof License [] )
            {
                return value;
            }
            else if ( value instanceof License )
            {
                return new License [] { (License) value };
            }
            else if ( value instanceof String )
            {
                // We don't care about trailing commas.
                String [] license_ids = ( (String) value ).split ( "," );
                License [] licenses = new License [ license_ids.length ];
                for ( int l = 0; l < license_ids.length; l ++ )
                {
                    licenses [ l ] = License.lookup ( license_ids [ l ] );
                    if ( licenses [ l ] == null )
                    {
                        throw new I18nIllegalArgumentException ( "Illegal value for ModuleDescription parameter [%parameter_name%]: [%parameter_value%]",
                                                 "parameter_name", name,
                                                 "parameter_value", value );
                    }
                }

                return licenses;
            }
            else if ( value instanceof String [] )
            {
                String [] license_ids = (String []) value;
                License [] licenses = new License [ license_ids.length ];
                for ( int l = 0; l < license_ids.length; l ++ )
                {
                    licenses [ l ] = License.lookup ( license_ids [ l ] );
                    if ( licenses [ l ] == null )
                    {
                        throw new I18nIllegalArgumentException ( "Illegal value for ModuleDescription parameter [%parameter_name%]: [%parameter_value%]",
                                                 "parameter_name", name,
                                                 "parameter_value", value );
                    }
                }

                return licenses;
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.TEST_COVERAGE ) )
        {
            if ( value instanceof Double )
            {
                return ( (Double) value ).doubleValue ();
            }
            else if ( "".equals ( value ) )
            {
                return 0.0D;
            }
            else if ( value instanceof String )
            {
                try
                {
                    return Double.parseDouble ( (String) value );
                }
                catch ( NumberFormatException e )
                {
                    throw new I18nIllegalArgumentException ( "Illegal value for ModuleDescription parameter [%parameter_name%]: [%parameter_value%]",
                                                             "parameter_name", name,
                                                             "parameter_value", value );
                }
            }
            // else we'll throw an exception down below.
        }
        else if ( name.equals ( ModuleDescription.URL_BINARY )
                  || name.equals ( ModuleDescription.URL_BUGS )
                  || name.equals ( ModuleDescription.URL_DESCRIPTION )
                  || name.equals ( ModuleDescription.URL_DEVELOPERS )
                  || name.equals ( ModuleDescription.URL_EXAMPLES )
                  || name.equals ( ModuleDescription.URL_SOURCE )
                  || name.equals ( ModuleDescription.URL_TESTS )
                  || name.equals ( ModuleDescription.URL_TROUBLESHOOTING )
                  || name.equals ( ModuleDescription.URL_USERS ) )
        {
            if ( value instanceof String )
            {
                return value;
            }
            // else we'll throw an exception down below.
        }
        else
        {
            // A custom parameter for some particular breed of
            // Module.  Just pass it through.
            return value;
        }

        throw new I18nIllegalArgumentException ( "Illegal value for ModuleDescription parameter [%parameter_name%]: [%parameter_value%]",
                                                 "parameter_name", name,
                                                 "parameter_value", value );
    }


    /**
     * @see musaico.message.Message#text()
     */
    public String text ()
    {
        return ModuleDescription.FULL_TEXT;
    }


    /**
     * @see musaico.message.Message#parameter(String)
     */
    public Serializable parameter (
                                   String name
                                   )
    {
        return this.parameters.get ( name );
    }


    /**
     * @see musaico.message.Message#parameters()
     */
    public String [] parameters ()
    {
        // We don't need to synchronize here, the parameters
        // map is immutable after construction time.
        List<String> parameter_names_list = new ArrayList<String> ();
        parameter_names_list.addAll ( this.parameters.keySet () );

        // Sort alphabetically.
        Collections.sort ( parameter_names_list );

        String [] template = new String [ parameter_names_list.size () ];
        String [] parameter_names = parameter_names_list.toArray ( template );

        return parameter_names;
    }


    /**
     * @see musaico.message.Message#toString()
     */
    public String toString ()
    {
        // Construct the final message.
        // In a stupid, slow fashion.  :(
        String formatted_message = this.text ();
        String [] parameter_names = this.parameters ();
        for ( int p = 0; p < parameter_names.length; p ++ )
        {
            Serializable value = this.parameter ( parameter_names [ p ] );
            formatted_message =
                this.format ().parameter ( formatted_message,
                                           parameter_names [ p ],
                                           value );
        }

        return formatted_message;
    }


    /**
     * <p>
     * Checks that all the required parameters are present
     * in the specified ModuleDescription, and throws an
     * exception if anything is missing or invalid.
     * </p>
     *
     * @param description The ModuleDescription to validate.
     *                    Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified ModuleDescription
     *                                      is invalid.
     */
    public static void validateDescription (
                                            ModuleDescription description
                                            )
        throws I18nIllegalArgumentException
    {
        List<String> missing_parameters = null;
        for ( int r = 0; r < ModuleDescription.REQUIRED.length; r ++ )
        {
            String parameter = ModuleDescription.REQUIRED [ r ];
            Serializable value = description.parameter ( parameter );
            if ( value == null )
            {
                if ( missing_parameters == null )
                {
                    missing_parameters = new ArrayList<String> ();
                }

                missing_parameters.add ( parameter );
            }
        }

        if ( missing_parameters != null
             && missing_parameters.size () > 0 )
        {
            throw new I18nIllegalArgumentException ( "SimpleModule id [%id%] description is missing: [%missing_parameters%] Whole description: [%description%]",
                                                     "id", description.parameter ( ModuleDescription.ID ),
                                                     "missing_parameters", missing_parameters,
                                                     "description", description );
        }
    }
}
