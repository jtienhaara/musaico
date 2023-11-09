package musaico.foundation.io.references;


import java.io.Serializable;

import java.util.UUID;

import musaico.foundation.io.SoftReference;


/**
 * <p>
 * References data by a
 * <a href="http://www.ietf.org/rfc/rfc4122.txt">UUID</a>.
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
 * Copyright (c) 2009 Johann Tienhaara
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
public class UUIDReference
    extends SimpleSoftReference<String>
    implements Serializable
{
    /** The UUID identifier for this Reference.  Can be used
     *  as the key in a hash table lookup and so on. */
    private final UUID uuid;


    /**
     * <p>
     * Creates a new Reference to data by UUID identifier.
     * </p>
     *
     * <p>
     * Creates a new UUID for the reference.
     * </p>
     */
    public UUIDReference ()
    {
        this ( UUID.randomUUID () );
    }


    /**
     * <p>
     * Creates a new Reference to data by UUID identifier.
     * </p>
     */
    public UUIDReference (
                          UUID uuid
                          )
    {
        super ( "" + uuid );

        this.uuid = uuid;
    }


    /**
     * <p>
     * Creates a new Reference to data by UUID identifier.
     * </p>
     */
    public UUIDReference (
                          String uuid
                          )
        throws IllegalArgumentException
    {
        super ( uuid );

        this.uuid = UUID.fromString ( uuid );
    }


    /**
     * @see java.lang.Object#equals( Object )
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof SoftReference ) )
        {
            return false;
        }

        UUID my_uuid = this.uuid ();
        UUID other_uuid;
        if ( ! ( obj instanceof UUIDReference ) )
        {
            // Try converting the other Reference to
            // a UUID, and comparing it.
            try
            {
                SoftReference<String> string_ref = (SoftReference<String>) obj;
                String other_string = string_ref.id ();
                other_uuid = UUID.fromString ( other_string );
            }
            catch ( IllegalArgumentException e )
            {
                // Not a valid UUID.
                return false;
            }
        }
        else
        {
            // The other object is, indeed, a UUIDReference.
            UUIDReference other = (UUIDReference) obj;
            other_uuid = other.uuid ();
        }


        if ( my_uuid == null )
        {
            // Should be impossible.  This is a boundary condition.
            if ( other_uuid == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( other_uuid == null )
        {
            // Should be impossible.  This is a boundary condition.
            return false;
        }

        // Test equality of the identifiers.
        return my_uuid.equals ( other_uuid );
    }


    /**
     * <p>
     * Returns a hash code for this reference.
     * </p>
     */
    public int hashCode ()
    {
        return this.uuid ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return "" + this.uuid ();
    }


    /**
     * <p>
     * Returns this Reference's UUID as a Java UUID object.
     * </p>
     */
    public UUID uuid ()
    {
        return this.uuid;
    }
}
