package musaico.kernel.common.swappers.block;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.buffer.Buffer;

import musaico.kernel.common.oentries.SimpleOEntry;

import musaico.kernel.driver.BlockDriver;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.Swapper;
import musaico.kernel.memory.paging.SwapState;

import musaico.kernel.memory.paging.buffer.BufferPage;
import musaico.kernel.memory.paging.buffer.BufferSwapState;

import musaico.kernel.objectsystem.Cursor;

import musaico.kernel.objectsystem.RecordFlag;
import musaico.kernel.objectsystem.RecordOperationException;
import musaico.kernel.objectsystem.RecordPermission;
import musaico.kernel.objectsystem.RecordPermissions;

import musaico.kernel.objectsystem.cursors.SimpleCursor;

import musaico.kernel.objectsystem.onode.OEntry;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Space;

import musaico.security.Credentials;
import musaico.security.Permissions;


/**
 * <p>
 * Reads in and writes out between buffer memory and
 * a block driver.
 * </p>
 *
 * <p>
 * This is the main swapper for most paged areas.  It can
 * be used, for example, to swap out to a database server or
 * file system, and in to buffer / fields memory.
 * </p>
 *
 *
 * <p>
 * In Java every Swapper must be Serializable in order
 * to play nicely over RMI.
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
 * Copyright (c) 2010, 2011, 2012 Johann Tienhaara
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
public class BufferBlockSwapper
    implements Swapper, Serializable
{
    /** The swapped-out state for this swapper.
     *  Always some kind of BlockSwapState, swapped out to a block driver. */
    private final BlockSwapState outSwapState;

    /** The swapped-in state for this swapper.
     *  Always some kind of BufferSwapState, swapped in to fields in memory. */
    private final BufferSwapState inSwapState;


    /**
     * <p>
     * Creates a new Buffer / BlockDriver swapper with
     * the specified "in" and "out" swap states.
     * </p>
     *
     * <p>
     * The "out" swap state is always swapped out to some block
     * driver, while the "in" swap state is swapped in to fields
     * in memory.
     * </p>
     *
     * @param in_swap_state The "in" swap state for this
     *                      buffer / block driver swapper.
     *                      Must not be null.
     *
     * @param out_swap_state The "out" swap state for this
     *                       buffer / block driver swapper.
     *                       Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameters are
     *                                      invalid (see above).
     */
    public BufferBlockSwapper (
                               BlockSwapState out_swap_state,
                               BufferSwapState in_swap_state
                               )
        throws I18nIllegalArgumentException
    {
        if ( out_swap_state == null
             || in_swap_state == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create BufferBlockSwapper with out_swap_state [%out_swap_state%] in_swap_state [%in_swap_state%]",
                                                     "out_swap_state", out_swap_state,
                                                     "in_swap_state", in_swap_state );
        }

        this.outSwapState = out_swap_state;
        this.inSwapState = in_swap_state;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#inPosition(musaico.region.Position)
     */
    @Override
    public Position inPosition (
                                Position out_position
                                )
        throws I18nIllegalArgumentException
    {
        Space in_space = this.inState ().space ();

        Position in_position = in_space.from ( out_position );

        return in_position;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#inState()
     */
    @Override
    public SwapState inState ()
    {
        return this.inSwapState;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#outPosition(musaico.region.Position)
     */
    @Override
    public Position outPosition (
                                 Position in_position
                                 )
        throws I18nIllegalArgumentException
    {
        Space out_space = this.outState ().space ();

        Position out_position = out_space.from ( in_position );

        return out_position;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#outState()
     */
    @Override
    public SwapState outState ()
    {
        return this.outSwapState;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#readIn(musaico.security.Credentials,musaico.kernel.memory.paging.Page,musaico.io.Region,musaico.kernel.memory.paging.Page,musaico.io.Region)
     */
    @Override
    public void readIn (
                        Credentials credentials,
                        Page out_page,
                        Region out_region,
                        Page in_page,
                        Region in_region
                        )
        throws SwapException
    {
        if ( out_page == null
             || ! ( out_page instanceof BlockPage )
             || out_region == null
             || in_page == null
             || ! ( in_page instanceof BufferPage )
             || in_region == null )
        {
            throw new SwapException ( "Swapper [%swapper%] cannot read in from page [%out_page%] region [%out_region%] to page [%in_page%] region [%in_region%]",
                                      "swapper", this,
                                      "out_page", out_page,
                                      "out_region", out_region,
                                      "in_page", in_page,
                                      "in_region", in_region );
        }

        BlockPage block_page = (BlockPage) out_page;
        BufferPage buffer_page = (BufferPage) in_page;

        BlockDriver block_driver = block_page.blockDriver ();
        Buffer buffer = buffer_page.buffer ();

        Permissions<RecordFlag> permissions =
            new RecordPermissions ( credentials,
                                    RecordPermission.READ,
                                    RecordFlag.RANDOM_ACCESS );

        try
        {
            Cursor cursor = new SimpleCursor ( SimpleOEntry.NO_SUCH_OENTRY,
                                               permissions,
                                               block_driver );
            cursor.record ().open ( cursor );
            Position out_start = out_region.start ();
            Position found_start =
                cursor.record ().seek ( cursor, out_start );
            if ( ! cursor.record ().region ( cursor ).contains ( found_start ) )
            {
                throw new SwapException ( "Corrupt block?  Could not seek to position [%position%] in region [%region%] while swapping in from [%out_page%] to [%in_page%]",
                                          "position", out_start,
                                          "region", out_region,
                                          "out_page", out_page,
                                          "in_page", in_page );
            }

            Region fields_read =
                cursor.record ().read ( cursor, buffer, out_region );
            cursor.record ().close ( cursor );
        }
        catch ( RecordOperationException e )
        {
            throw new SwapException ( "BufferBlockSwapper [%swapper%] failed to read in from page [%out_page%] block driver [%block_driver%] to page [%in_page%] buffer [%buffer%]",
                                      "swapper", this,
                                      "out_page", block_page,
                                      "block_driver", block_driver,
                                      "in_page", buffer_page,
                                      "buffer", buffer,
                                      "cause", e );
        }
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#writeOut(musaico.security.Credentials,musaico.kernel.memory.paging.Page,musaico.io.Region,musaico.kernel.memory.paging.Page,musaico.io.Region)
     */
    @Override
    public void writeOut (
                          Credentials credentials,
                          Page in_page,
                          Region in_region,
                          Page out_page,
                          Region out_region
                          )
        throws SwapException
    {
        if ( in_page == null
             || ! ( in_page instanceof BufferPage )
             || in_region == null
             || out_page == null
             || ! ( out_page instanceof BlockPage )
             || out_region == null )
        {
            throw new SwapException ( "Swapper [%swapper%] cannot write out from page [%in_page%] region [%in_region%] to page [%out_page%] [%out_region%]",
                                      "swapper", this,
                                      "in_page", in_page,
                                      "in_region", in_region,
                                      "out_page", out_page,
                                      "out_region", out_region );
        }

        BlockPage block_page = (BlockPage) out_page;
        BufferPage buffer_page = (BufferPage) in_page;

        BlockDriver block_driver = block_page.blockDriver ();
        Buffer buffer = buffer_page.buffer ();

        Permissions<RecordFlag> permissions =
            new RecordPermissions ( credentials,
                                    RecordPermission.READ,
                                    RecordFlag.RANDOM_ACCESS );

        try
        {
            Cursor cursor = new SimpleCursor ( SimpleOEntry.NO_SUCH_OENTRY,
                                               permissions,
                                               block_driver );
            cursor.record ().open ( cursor );
            Position out_start = out_region.start ();
            Position found_start =
                cursor.record ().seek ( cursor, out_start );
            if ( ! cursor.record ().region ( cursor ).contains ( found_start ) )
            {
                throw new SwapException ( "Corrupt block?  Could not seek to position [%position%] in region [%region%] while swapping out from [%in_page%] to [%out_page%]",
                                          "position", out_start,
                                          "region", out_region,
                                          "in_page", in_page,
                                          "out_page", out_page );
            }

            Region fields_written =
                cursor.record ().write ( cursor, buffer, in_region );
            cursor.record ().close ( cursor );
        }
        catch ( RecordOperationException e )
        {
            throw new SwapException ( "BufferBlockSwapper [%swapper%] failed to write out from page [%in_page%] buffer [%buffer%] to page [%out_page%] block driver [%block_driver%]",
                                      "swapper", this,
                                      "out_page", block_page,
                                      "block_driver", block_driver,
                                      "in_page", buffer_page,
                                      "buffer", buffer,
                                      "cause", e );
        }
    }
}
