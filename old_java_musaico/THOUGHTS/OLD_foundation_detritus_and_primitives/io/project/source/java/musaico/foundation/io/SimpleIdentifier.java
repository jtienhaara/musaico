package musaico.foundation.io;

import java.io.Serializable;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Uniquely identifies something within a specific namespace.
 * </p>
 *
 *
 * <p>
 * In Java, every Identifier must be Serializable in order
 * to play nicely across RMI.
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
public class SimpleIdentifier
    implements Identifier
{
    /** The namespace in which this identifier's name is unique. */
    private final Identifier parentNamespace;

    /** The unique name within the namespace. */
    private final Reference name;


    /**
     * <p>
     * Creates a new SimpleIdentifier with the specified parent
     * namespace and unique name within that namespace.
     * </p>
     *
     * @param parent_namespace The namespace in which the specified
     *                         name is unique.  Must not be null.
     *
     * @param name The unique name within the namespace.
     *             Must not be null.  Note that multiple copies of
     *             the same unique identifier may be created; checking
     *             for uniqueness must be done elsewhere (such as
     *             in a lookup of objects by unique identifiers).
     *
     * @throws I18nIllegalArgumentException If the parameters are
     *                                      invalid (see above).
     */
    public SimpleIdentifier (
                             Identifier parent_namespace,
                             Reference name
                             )
    {
        if ( parent_namespace == null
             || name == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleIdentifier with parent namespace [%parent_namespace%] name [%name%]",
                                                     "parent_namespace", parent_namespace,
                                                     "name", name );
        }

        this.parentNamespace = parent_namespace;
        this.name = name;
    }


    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof Identifier ) )
        {
            return false;
        }

        Identifier that = (Identifier) obj;

        Identifier this_parent_namespace = this.parentNamespace ();
        Identifier that_parent_namespace = that.parentNamespace ();

        Reference this_name = this.name ();
        Reference that_name = that.name ();

        if ( this_parent_namespace == null )
        {
            if ( that_parent_namespace == null )
            {
                // null namespace == null namespace
                if ( this_name == null )
                {
                    if ( that_name == null )
                    {
                        // null name == null name
                        return true;
                    }
                    else
                    {
                        // null name != any name
                        return false;
                    }
                }
                else if ( that_name == null )
                {
                    // any name != null
                    return false;
                }

                return this_name.equals ( that_name );
            }
            else
            {
                // null != any namespace
                return false;
            }
        }
        else if ( ! this_parent_namespace.equals ( that_parent_namespace ) )
        {
            return false;
        }

        // namespace == namespace.
        if ( this_name == null )
        {
            if ( that_name == null )
            {
                // null name == null name
                return true;
            }
            else
            {
                // null name != any name
                return false;
            }
        }
        else if ( that_name == null )
        {
            // any name != null name
            return false;
        }

        return this_name.equals ( that_name );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return
            this.parentNamespace ().hashCode () * 31
            + this.name ().hashCode ();
    }


    /**
     * @see musaico.foundation.io.Identifier#name()
     */
    public Reference name ()
    {
        return this.name;
    }


    /**
     * @see musaico.foundation.io.Identifier#parentNamespace()
     */
    public Identifier parentNamespace ()
    {
        return this.parentNamespace;
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        Identifier parent_namespace = this.parentNamespace ();
        if ( ! parent_namespace.equals ( Identifier.NONE ) )
        {
            sbuf.append ( "" + parent_namespace );
        }

        sbuf.append ( "/" );
        sbuf.append ( "" + this.name () );

        return sbuf.toString ();
    }
}
