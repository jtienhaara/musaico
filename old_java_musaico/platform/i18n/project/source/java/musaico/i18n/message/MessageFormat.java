package musaico.i18n.message;

import java.io.Serializable;


/**
 * <p>
 * A text format of messages in the resource bundles.
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
public interface MessageFormat
    extends Serializable
{
    /**
     * <p>
     * Replaces all instances of the specified parameter in the
     * specified text with the specified value.
     * </p>
     *
     * @param text The text in which to replace all instances
     *             of the specified parameter.  Must not be null.
     * @param parameter_name The parameter to replace.  Can be null.
     * @param parameter_value The value to replace each instance
     *                        of the formatted parameter name.
     *                        Must not be null.
     *
     * @return The input text with all instances of the formatted
     *         parameter name replaced by the specified value.
     *         Never null.
     */
    public abstract String parameter (
                                      String text,
                                      String parameter_name,
                                      Serializable parameter_value
                                      );


    /**
     * <p>
     * Returns the parameter name, wrapped in whatever formatting
     * will make it parseable for this formatter.
     * </p>
     *
     * <p>
     * For example, if the format expects to find "${foo}" for
     * parameter "foo", then calling parameterName ( "foo" )
     * will return "${foo}".
     * </p>
     *
     * @param parameter_name The name to format as a parameter.
     *                       Must not be null.
     *
     * @return The wrapped parameter.  Never null.
     */
    public abstract String parameterName (
                                          String parameter_name
                                          );


    /**
     * <p>
     * Returns a regular expression for matching the
     * specified parameter name in a locale text.
     * </p>
     *
     * <p>
     * This implementation looks for instances of [%param%].
     * </p>
     *
     * @param parameter_name The name of the parameter to look for.
     *                       Must not be null.
     * @return The regular expression to match parameter names in
     *         a localized text bundle.  Never null.
     */
    public abstract String parameterNameRegex (
                                               String parameter_name
                                               );


    /**
     * <p>
     * Returns a regular expression for replacing the
     * specified parameter value in localized text.
     * </p>
     *
     * @param parameter_value The value of the parameter to put in
     *                        place of parameter name instances.
     *                        Must not be null.
     * @return The regular expression to replace parameter value in
     *         localized text.  Never null.
     */
    public abstract String parameterValueRegex (
                                                Serializable parameter_value
                                                );
}
