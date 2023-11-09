package musaico.kernel.objectsystem.onode;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Path;
import musaico.io.ReferenceCount;
import musaico.io.SimpleReferenceCount;

import musaico.buffer.Buffer;
import musaico.buffer.NullBuffer;

import musaico.kernel.memory.Segment;
import musaico.kernel.memory.SegmentIdentifier;

import musaico.kernel.objectsystem.Record;

import musaico.mutex.Mutex;

import musaico.region.Region;
import musaico.region.Size;

import musaico.security.Credentials;
import musaico.security.NoSecurity;
import musaico.security.Security;

import musaico.state.Machine;

import musaico.time.Time;


/**
 * <p>
 * The class for ONode.NONE.
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed, every ONode
 * must be Serializable in order to play nicely over RMI.
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
public class NoONode
    implements ONode, Serializable
{
    /**
     * <p>
     * Creates a new NoONode.
     * </p>
     *
     * <p>
     * You should use ONode.NONE instead.
     * </p>
     *
     * <p>
     * Package-private.
     * </p>
     */
    NoONode ()
    {
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#data()
     */
    @Override
    public Record data ()
    {
        return Record.NONE;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#id()
     */
    @Override
    public ONodeIdentifier id ()
    {
        return ONodeIdentifier.NONE;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#link(musaico.security.Credentials,musaico.kernel.objectsystem.onodes.OEntry)
     */
    @Override
    public long link (
                      Credentials credentials,
                      OEntry entry
                      )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        throw new ONodeOperationException ( "NoONode cannot work for credentials [%credentials%] on entry [%entry%]",
                                            "credentials", credentials,
                                            "entry", entry );
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#lookup(musaico.security.Credentials,musaico.kernel.objectsystem.onodes.OEntry,musaico.io.Path)
     */
    @Override
    public OEntry lookup (
                          Credentials credentials,
                          OEntry entry,
                          Path path
                          )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        throw new ONodeOperationException ( "NoONode cannot work for credentials [%credentials%] on entry [%entry%]",
                                            "credentials", credentials,
                                            "entry", entry );
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#metadata()
     */
    @Override
    public ONodeMetadata metadata ()
    {
        return ONodeMetadata.NONE;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#move(musaico.security.Credentials,musaico.kernel.objectsystem.onodes.OEntry,musaico.kernel.objectsystem.OEntry)
     */
    @Override
    public void move (
                      Credentials credentials,
                      OEntry entry,
                      OEntry new_entry
                      )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        throw new ONodeOperationException ( "NoONode cannot work for credentials [%credentials%] on entry [%entry%]",
                                            "credentials", credentials,
                                            "entry", entry );
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#mutex(musaico.security.Credentials)
     */
    @Override
    public Mutex mutex (
                        Credentials credentials
                        )
    {
        // Nobody is allowed to exclusively lock us,
        // so we return a brand new Mutex every time.
        return new Mutex ( this );
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#references()
     */
    @Override
    public ONodeReferences references ()
    {
        return ONodeReferences.NONE;
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#security()
     */
    @Override
    public Security security ()
    {
        return new NoSecurity ();
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#superBlockRef()
     */
    @Override
    public Identifier superBlockRef ()
    {
        return ONodeIdentifier.NONE.parentNamespace ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "NoONode";
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#truncate(musaico.security.Credentials,musaico.kernel.objectsystem.onodes.OEntry,musaico.io.Region)
     */
    @Override
    public void truncate (
                          Credentials credentials,
                          OEntry entry,
                          Region truncate_all_fields_outside_of_region
                          )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        throw new ONodeOperationException ( "NoONodes cannot work for credentials [%credentials%] on entry [%entry%]",
                                            "credentials", credentials,
                                            "entry", entry );
    }


    /**
     * @see musaico.kernel.objectsystem.ONode#unlink(musaico.security.Credentials,musaico.kernel.objectsystem.onodes.OEntry)
     */
    @Override
    public long unlink (
                        Credentials credentials,
                        OEntry entry
                        )
        throws ONodeOperationException,
               I18nIllegalArgumentException
    {
        throw new ONodeOperationException ( "NoONode cannot work for credentials [%credentials%] on entry [%entry%]",
                                            "credentials", credentials,
                                            "entry", entry );
    }
}
