package musaico.platform.primitives.tag.string;

import java.io.Serializable;

import java.util.Locale;
import java.util.ResourceBundle;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.types.AbstractTag;
import musaico.foundation.types.Cast;
import musaico.foundation.types.Operation;
import musaico.foundation.types.PipeCast;
import musaico.foundation.types.Tag;

import musaico.platform.primitives.operations.string.Localize;


/**
 * <p>
 * Tags a String as being specific to a particular locale.
 * </p>
 *
 * <p>
 * For example, to convert an internationalized String into
 * a localized String for Canadian English:
 * </p>
 *
 * <pre>
 *     Type<String> string = ...;
 *     Instance<String> internationalized_text =
 *         string.instance ( "Hello, world!" );
 *     ResourceBundle canadian_english =
 *         ResourceBundle.getBundle ( Locale.CANADA );
 *     Tag locale = new Localized ( canadian_english );
 *     String localized_text =
 *         internationalized_text.value ( String.class, locale );
 * </pre>
 *
 * <p>
 * The above code "casts" the internationalized text "Hello, world!"
 * into the localized text (which of course would be
 * "Hello, world, eh!").
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
public class Localized
    extends AbstractTag
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130831L;
    private static final String serialVersionHash =
        "0x8E73F443732D6607E15518776B3742447F848099";


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Localized.class );


    /** The resource bundle used for localizing text. */
    private final ResourceBundle resourceBundle;


    /**
     * <p>
     * Creates a new Localized tag for the specified localized
     * ResourceBundle.
     * </p>
     *
     * @param resource_bundle The bundle of text for localizing Strings.
     *                        Must not be null.
     */
    public Localized (
                      ResourceBundle resource_bundle
                      )
        throws ParametersMustNotBeNull.Violation
    {
        super ( "localized["
                + resource_bundle.getLocale ().toLanguageTag ()
                + "]" );

        Localized.classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                         resource_bundle );

        this.resourceBundle = resource_bundle;
    }


    /**
     * @return The Locale to which all values of this Tag are localized.
     *         Never null.
     */
    public Locale locale ()
    {
        return this.resourceBundle.getLocale ();
    }


    /**
     * @see musaico.foundation.types.Tag#refine(musaico.foundation.types.Operation)
     */
    @Override
    @SuppressWarnings("unchecked") // We check for String then cast
    public final <INPUT extends Object, OUTPUT extends Object>
        Operation<INPUT, OUTPUT> refine (
                                         Operation<INPUT, OUTPUT> operation
                                         )
    {
        if ( operation instanceof Cast )
        {
            Cast<INPUT, OUTPUT> caster = (Cast<INPUT, OUTPUT>) operation;
            final Class<OUTPUT> target_class = caster.targetClass ();

            if ( target_class == String.class )
            {
                // Once the cast to a String has been finished,
                // localize the String.
                final Class<INPUT> source_class = caster.sourceClass ();
                final Operation<OUTPUT, OUTPUT> localizer =
                    (Operation<OUTPUT, OUTPUT>)
                    new Localize ( this.resourceBundle );
                final Cast<INPUT, OUTPUT> piper =
                    new PipeCast<INPUT, OUTPUT> ( source_class,
                                                  target_class,
                                                  caster.none (),
                                                  caster, // Op # 1
                                                  localizer ); // Op # 2
                return piper;
            }
        }

        // Leave the operation as-is.
        return operation;
    }


    /**
     * @return The resource bundle used by this localized tag.
     *         Never null.
     */
    public ResourceBundle resourceBundle ()
    {
        return this.resourceBundle;
    }
}
