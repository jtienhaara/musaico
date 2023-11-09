package musaico.platform.primitives.operations.string;

import java.io.Serializable;

import java.util.Locale;
import java.util.ResourceBundle;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.types.AbstractOperation;
import musaico.foundation.types.NoValue;
import musaico.foundation.types.Value;
import musaico.foundation.types.ValueBuilder;


/**
 * <p>
 * Localizes Strings to a specific locale, using a ResourceBundle.
 * </p>
 *
 * <p>
 * If there is no localized version of a specific input text, then
 * the input text is used as the "no value" for the failed output.
 * Thus the following code will fall back on the input if there is
 * no localized version of "Hello, world!":
 * </p>
 *
 * <pre>
 *     final ResourceBundle resource_bundle =
 *         ResourceBundle.getBundle ( Locale.CANADA );
 *     final String localized_hello_world =
 *         new Localize ( resource_bundle )
 *             .evaluate ( new Scalar<String> ( "Hello, world!" ) )
 *             .orNone ();
 * </pre>
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
 * Copyright (c) 2013 Johann Tienhaara
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
public class Localize
    extends AbstractOperation<String, String>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130915L;
    private static final String serialVersionHash =
        "0xDA86A9681A17F7FF3D29195DC7E4208CCF1408AE";


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Localize.class );


    /** The resource bundle used for localizing text. */
    private final ResourceBundle resourceBundle;


    /**
     * <p>
     * Creates a new Localize operation which uses the specified
     * ResourceBundle to look up localized versions of internationalized
     * text.
     * </p>
     *
     * @param resource_bundle The resource bundle which will
     *                        be used to lookup localized versions
     *                        of text.  For example,
     *                        <code> ResourceBundle.getBundle ( Locale.CANADA ) </code>.
     *                        Must not be null.
     */
    public Localize (
                     ResourceBundle resource_bundle
                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( "localize["
                + ( resource_bundle == null ? "null"
                        : resource_bundle.getLocale ().toLanguageTag () )
                + "]",
                String.class,
                "" ); // None

        Localize.classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                        resource_bundle );

        this.resourceBundle = resource_bundle;
    }


    /**
     * @see musaico.foundation.types.Operation#evaluate(musaico.foundation.types.Value)
     */
    @Override
    public Value<String> evaluate (
                                   Value<String> input
                                   )
    {
        if ( input instanceof NoValue )
        {
            return input;
        }

        final ValueBuilder<String> builder =
            new ValueBuilder<String> ( String.class, "" );
        for ( String internationalized : input )
        {
            final String localized =
                this.resourceBundle.getString ( internationalized );
            if ( localized == null )
            {
                return input;
            }

            builder.add ( localized );
        }

        return builder.build ();
    }


    /**
     * @return The Locale to which Strings will be localized.
     *         Never null.
     */
    public Locale locale ()
    {
        return this.resourceBundle.getLocale ();
    }


    /**
     * @return The resource bundle used by this localizer.
     *         Never null.
     */
    public ResourceBundle resourceBundle ()
    {
        return this.resourceBundle;
    }
}
