package musaico.foundation.io.references;


import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.NaturallyOrdered;
import musaico.foundation.io.Range;
import musaico.foundation.io.SoftReference;


/**
 * <p>
 * Represents a range of versions, such as "1-3" or "2.0-any" and so on.
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
public class VersionRange
    extends Range<Version>
    implements Serializable
{
    /**
     * <p>
     * Creates a new range of versions.
     * </p>
     *
     * @param lower The lower bound of the versions range.
     *              Must not be null.
     *
     * @param upper The upper bound of thr range of versions.
     *              Must not be null.
     */
    public VersionRange (
                         Version from,
                         Version to
                         )
    {
	super ( from, to, VersionOrder.DEFAULT );
    }
}
