package musaico.security;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;


/**
 * <p>
 * Permissions (requested by / granted to) a specific requester to
 * perform operations on a specific target.
 * </p>
 *
 *
 * <p>
 * In Java, every Permissions must be Serializable in order to play
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
public class SimplePermissions<PERMISSION extends Reference>
    implements Permissions<PERMISSION>, Serializable
{
    /** The credentials we were constructed with.  Can be
     *  a KernelCredentials (such as a user or module credentials),
     *  or just a reference. */
    private final Credentials credentials;

    /** A reference to the target to which these permissions
     *  apply. */
    private final Reference target;

    /** The permissions we were constructed with. */
    private final Set<PERMISSION> permissions;


    /**
     * <p>
     * Creates a new SimplePermissions (requested or granted,
     * depending on who is creating the permissions!) with the
     * specified credentials, target, and permissions flags.
     * </p>
     *
     * @param credentials Either a reference to a KernelCredentials,
     *                    or the KernelCredentials itself.
     *                    Be very careful about passing around
     *                    KernelCredentials!  Must not be null.
     *
     * @param target The target on which permissions are being
     *               requested/granted to perform certain operations.
     *               Must not be null.
     *
     * @param permissions The permissions to request / grant.
     *                    Must not be null.  Must not contain
     *                    any null elements.  Can be zero-length.
     *
     * @throws I18nIllegalArgumentException If any of the parameters
     *                                      are invalid.
     */
    public SimplePermissions (
                              Credentials credentials,
                              Reference target,
                              PERMISSION... permissions
                              )
        throws I18nIllegalArgumentException
    {
        if ( credentials == null
             || target == null
             || permissions == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimplePermissions with credentials [%credentials%] target [%target%] permissions [%permissions%]",
                                                     "credentials", credentials,
                                                     "target", target,
                                                     "permissions", permissions );
        }

        this.credentials = credentials;
        this.target = target;

        this.permissions = new HashSet<PERMISSION> ();
        for ( int p = 0; p < permissions.length; p ++ )
        {
            if ( permissions [ p ] == null )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a SimplePermissions with credentials [%credentials%] target [%target%] permissions [%permissions%]",
                                                         "credentials", credentials,
                                                         "target", target,
                                                         "permissions", permissions );
            }

            this.permissions.add ( permissions [ p ] );
        }
    }


    /**
     * @see musaico.security.Permissions#allowed()
     */
    public PERMISSION [] allowed ()
    {
        Class cl = this.permissions.iterator ().next ().getClass ();
        PERMISSION [] template = (PERMISSION [])
            Array.newInstance ( cl, this.permissions.size () );
        return this.permissions.toArray ( template );
    }


    /**
     * @see musaico.security.Permissions#credentials()
     */
    public Credentials credentials ()
    {
        return this.credentials;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( ! ( obj instanceof Permissions ) )
        {
            return false;
        }

        Permissions that = (Permissions) obj;
        if ( ! this.credentials ().equals ( that.credentials () ) )
        {
            return false;
        }
        else if ( ! this.target ().equals ( that.target () ) )
        {
            return false;
        }

        Object [] that_permissions = that.allowed ();
        if ( that_permissions == null )
        {
            return false;
        }
        else if ( that_permissions.length != this.permissions.size () )
        {
            return false;
        }

        for ( int p = 0; p < that_permissions.length; p ++ )
        {
            if ( ! this.permissions.contains ( that_permissions [ p ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.credentials ().hashCode ();
        hash_code += this.target ().hashCode ();
        Iterator<PERMISSION> permiterator = this.permissions.iterator ();
        while ( permiterator.hasNext () )
        {
            hash_code += permiterator.next ().hashCode ();
        }

        return hash_code;
    }


    /**
     * @see musaico.security.Permissions#isAllowed(T)
     */
    public boolean isAllowed (
                              PERMISSION permission
                              )
    {
        return this.permissions.contains ( permission );
    }


    /**
     * @see musaico.security.Permissions#isAllowed(musaico.security.Permissions<T>)
     */
    public boolean isAllowed (
                              Permissions<PERMISSION> permissions
                              )
    {
        PERMISSION [] check_permissions = permissions.allowed ();
        for ( int p = 0; p < check_permissions.length; p ++ )
        {
            if ( ! this.isAllowed ( check_permissions [ p ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see musaico.security.Permissions#target()
     */
    public Reference target ()
    {
        return this.target;
    }
}
