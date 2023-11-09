package musaico.foundation.io.references;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;

import musaico.foundation.io.HardReference;
import musaico.foundation.io.ReferenceCount;
import musaico.foundation.io.ReferenceException;


/**
 * <p>
 * Represents a hard reference to some local object inside some
 * namespace or other.
 * </p>
 *
 * <p>
 * A HardReference is resolve-able into the object referred to.
 * </p>
 *
 * <p>
 * If a reference to the object is required across RMI, a
 * UnicastHardReference can be created locally and then distributed
 * to other JVMs as needed.
 * </p>
 *
 *
 * <p>
 * In Java, all References are Serializable in order to play nicely
 * with RMI.
 * </p>
 *
 * <p>
 * Also every Reference must implement equals() and hashCode() in Java,
 * in order to ensure easy use in hash tables.
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
public class UnicastHardReference<REF extends Serializable>
    extends UnicastRemoteObject
    implements HardReference<REF>
{
    /** The object referred to by this hard reference. */
    private final REF object;

    /** The reference count for the referred-to object. */
    private final ReferenceCount references;

    /** The hash code for this reference. */
    private final int hashCode;


    /**
     * <p>
     * Creates a new hard reference to the specified object.
     * </p>
     *
     * <p>
     * The specified References object is incremented and decremented
     * as necessary.
     * </p>
     *
     * @param object The object to reference.  Must not be null.
     *
     * @param references The reference count to increment / decrement
     *                   as this hard reference is constructed or finalized.
     *                   Must not be null.
     *
     * @throws RemoteException Because for some bizarre reason the
     *                         UnicastRemoteObject constructor might
     *                         throw a remote exception!
     */
    public UnicastHardReference (
                                 REF object,
                                 ReferenceCount references
                                 )
        throws RemoteException
    {
        super ();

        if ( object == null
             || references == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a UnicastHardReference to object [%object%] with references [%references%]",
                                                     "object", object,
                                                     "references", references );
        }

        this.object = this.storageObject ( object );
        this.references = references;

        this.hashCode = this.object.hashCode ();

        this.references.increment ();
    }


    /**
     * @see musaico.foundation.io.Reference#equals(Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj == this )
        {
            return true;
        }
        else if ( ! ( obj instanceof HardReference ) )
        {
            return false;
        }

        try
        {
            // If it's a hard reference that refers to the same
            // object as ours, then we're effectively equal.
            REF my_referred_to_object = this.resolve ();
            if ( my_referred_to_object == null )
            {
                return false;
            }

            HardReference<?> other_ref = (HardReference<?>) obj;
            Object other_referred_to_object = (Object) other_ref.resolve ();
            if ( other_referred_to_object == null )
            {
                return false;
            }

            if ( my_referred_to_object == other_referred_to_object )
            {
                return true;
            }

            if ( my_referred_to_object.equals ( other_referred_to_object ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( ReferenceException e )
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#finalize()
     */
    public void finalize ()
        throws Throwable
    {
        // Decrement the reference count to the referred-to object.
        this.references.decrement ();

        super.finalize ();
    }


    /**
     * @see musaico.foundation.io.Reference#hashCode()
     */
    public int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see musaico.foundation.io.HardReference#resolve()
     */
    public REF resolve ()
        throws ReferenceException
    {
        return this.object;
    }


    /**
     * @see musaico.foundation.io.Reference#toString()
     */
    public String toString ()
    {
        try
        {
            return "" + this.resolve ();
        }
        catch ( ReferenceException e )
        {
            return "[FAILED TO RESOLVE OBJECT] " + super.toString ();
        }
    }




    /**
     * <p>
     * Returns the specified object, which will then be stored
     * for future calls to resolve ().
     * </p>
     *
     * <p>
     * Derived classes can override this method to store a Java weak
     * reference and so on, rather than a strong reference.
     * <p>
     */
    protected REF storageObject (
                                 REF object
                                 )
    {
        return object;
    }
}
