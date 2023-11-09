package musaico.kernel.objectsystem;

import java.io.Serializable;


import musaico.buffer.Buffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.kernel.memory.Segment;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.security.NoSecurity;
import musaico.security.Security;


/**
 * <p>
 * A Record which does absolutely nothing, no matter how hard
 * you try.  Not very useful!
 * </p>
 *
 *
 * <p>
 * Because object systems can conceivably be distributed and using
 * RMI, every Record must be Serializable in order to play nicely
 * over RMI.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class NoRecord
    implements Record, Serializable
{
    /**
     * @see musaico.kernel.objectsystem.Record#close(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void close (
                       Cursor cursor
                       )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#mmap(musaico.kernel.objectsystem.Cursor,musaico.kernel.memory.Segment)
     */
    @Override
    public void mmap (
                      Cursor cursor,
                      Segment segment
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#open(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public void open (
                      Cursor cursor
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#poll(musaico.kernel.objectsystem.Cursor,musaico.kernel.objectsystem.PollRequest,musaico.kernel.objectsystem.PollResponse)
     */
    @Override
    public void poll (
                      Cursor cursor,
                      PollRequest request,
                      PollResponse response
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#read(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.region.Region)
     */
    @Override
    public Region read (
                        Cursor cursor,
                        Buffer read_fields,
                        Region read_into_region
                        )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#region(musaico.kernel.objectsystem.Cursor)
     */
    @Override
    public Region region (
                          Cursor cursor
                          )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#security()
     */
    @Override
    public Security<RecordFlag> security ()
    {
        return new NoSecurity<RecordFlag> ();
    }


    /**
     * @see musaico.kernel.objectsystem.Record#seek(musaico.kernel.objectsystem.Cursor,musaico.region.Position)
     */
    @Override
    public Position seek (
                          Cursor cursor,
                          Position position
                          )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#space()
     */
    @Override
    public ArraySpace space ()
    {
        return ArraySpace.STANDARD;
    }


    /**
     * @see musaico.kernel.objectsystem.Record#sync(musaico.kernel.objectsystem.Cursor,boolean)
     */
    @Override
    public void sync (
                      Cursor cursor,
                      boolean is_metadata_only
                      )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }


    /**
     * @see musaico.kernel.objectsystem.Record#write(musaico.kernel.objectsystem.Cursor,musaico.buffer.Buffer,musaico.region.Region)
     */
    @Override
    public Region write (
                         Cursor cursor,
                         Buffer write_fields,
                         Region write_from_region
                         )
        throws RecordSecurityException,
               RecordOperationException,
               I18nIllegalArgumentException
    {
        throw new RecordOperationException ( "NoRecord [%no_record%] cannot perform any operations for cursor [%cursor%]",
                                             "no_record", this,
                                             "cursor", cursor );
    }
}
