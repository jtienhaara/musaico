package musaico.types.opaque;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.types.SimpleTypeSystemBuilder;
import musaico.types.Type;
import musaico.types.TypeSystem;
import musaico.types.TypingEnvironment;


/**
 * <p>
 * Builds a TypeSystem of arbitrary types, about which the typing system
 * knows nothing, not even the raw class.
 * </p>
 *
 * <p>
 * Any two OpaqueTypes might not be interchangeable or even comparable.
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
 * Copyright (c) 2009, 2012 Johann Tienhaara
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
public class OpaqueTypeSystemBuilder
    extends SimpleTypeSystemBuilder
    implements Serializable
{
    /**
     * <p>
     * Creates a new OpaqueTypeSystemBuilder which will create a new opaque
     * type system beneath the specified type system.
     * </p>
     *
     * @param parent_type_system The parent type system.  This builder will
     *                           construct a child type system.
     *                           For example, the root type
     *                           system of the specified environment,
     *                           or perhaps another type system which
     *                           this builder will extend with a child
     *                           type system.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public OpaqueTypeSystemBuilder (
                                    TypingEnvironment environment,
                                    TypeSystem parent_type_system
                                    )
        throws I18nIllegalArgumentException
    {
        // Throws I18nIllegalArgumentException:
        super ( environment, parent_type_system );

        // Create the universal "anything goes" opaque type.
        Type<Object> opaque_type = this.prepare ( "object",         // name
                                                  Object.class,     // class
                                                  new Object () )   // none
            // No default typecasters to / from opaque objects.
            .build ();
        this.add ( opaque_type );
    }
}
