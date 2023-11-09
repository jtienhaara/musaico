package musaico.foundation.io;

import java.io.Serializable;


/**
 * <p>
 * Represents a marker in data, such as a label marker, a record start
 * marker, a record end marker, and so on.
 * </p>
 *
 * <p>
 * Typically Markers are stored in data (either explicitly or, sometimes,
 * implicitly).  For example, an XML file might have markers representing
 * the beginning and end of each tag (record start and end markers).
 * Or an audio file might have label markers added by a user through
 * some audio editor.  And so on.
 * </p>
 *
 * <p>
 * Markers can only compare themselves to other markers (such
 * as alphabetical string representations), rather than trying
 * to compare their exact positions within unknown data structures
 * to other unknown positions.
 * </p>
 *
 *
 * <p>
 * In Java every Marker must be Serializable in order to play
 * nicely over RMI.
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
 * Copyright (c) 2009, 2011 Johann Tienhaara
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
public interface Marker
    extends Serializable
{
    /**
     * <p>
     * Returns the label of this Marker.
     * </p>
     *
     * <p>
     * A label might be user-entered text, or a marker identifier
     * unique within the data set, and so on.
     * </p>
     *
     * @return The label of this Marker.  Never null.
     */
    public abstract Reference label ();


    /** Each Marker must implement equals (Object), hashCode ()
     *  and toString (). */
}
