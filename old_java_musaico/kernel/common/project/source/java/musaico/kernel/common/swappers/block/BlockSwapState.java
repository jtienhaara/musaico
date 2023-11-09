package musaico.kernel.common.swappers.block;

import java.io.Serializable;


import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.driver.BlockDriver;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapState;

import musaico.kernel.memory.paging.kernelpagings.SimpleKernelPaging;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.security.Credentials;


/**
 * <p>
 * A SwapState class which can be reused for multiple swap states
 * for various block drivers.
 * </p>
 *
 * <p>
 * For example, one BlockSwapState might be a "swapped way out
 * to a remote webserver" swap state:
 * </p>
 *
 * <pre>
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *     BlockDriver web_driver = ...;
 *     SwapState swapped_out_to_the_cloud =
 *         new BlockSwapState ( "swapped_way_out_to_web",
 *                              web_driver.id () );
 * </pre>
 *
 * <p>
 * Another BlockSwapState might be a "swapped out to local filesystem":
 * </p>
 *
 * <pre>
 *     BlockDrier file_system_driver = ...;
 *     SwapState swapped_out_to_file_system =
 *         new BlockSwapState ( "swapped_out_to_file_system",
 *                              file_system_driver.id () );
 * </pre>
 *
 *
 * <p>
 * In Java every SwapState must be Serializable in order to
 * play nicely over RMI.
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
public class BlockSwapState
    implements SwapState, Serializable
{
    /** The unique identifier for this swap state in the kernel,
     *  such as a SimpleSoftReference ( "swapped_in_to_buffers" ). */
    private final Reference id;

    /** The pages use this for maintaining the kernel's LRU list of
     *  pages as well as their clean/dirty states. */
    private final KernelPaging kernelPaging;

    /** The Size of each Page created by this swap state,
     *  such as an ArraySize ( 32L ). */
    private final Size pageSize;


    /**
     * <p>
     * Creates a new BlockSwapState for the specified BlockDriver.
     * </p>
     *
     * @param id The unique identifier for this BlockSwapState,
     *           such as a <code> SimpleSoftReference&lt;String&gt;
     *           ( "swapped_out_to_database" ) </code>.
     *           Must not be null.
     *
     * @param kernel_paging Used by the pages created in this swap state
     *                      to maintain the kernel's LRU and dirty/clean
     *                      states.  Must not be null.
     *
     * @param page_size The Size of each Page created by this swap
     *                  state, such as <code> new ArraySize ( 32L ) </code>
     *                  for 32 Fields per Page.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public BlockSwapState (
                           Reference id,
                           KernelPaging kernel_paging,
                           Size page_size
                           )
        throws I18nIllegalArgumentException
    {
        if ( id == null
             || kernel_paging == null
             || page_size == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a BlockSwapState with id [%id%] kernel paging [%kernel_paging%] page size [%page_size%]",
                                                     "id", id,
                                                     "kernel_paging", kernel_paging,
                                                     "page_size", page_size );
        }

        this.id = id;
        this.kernelPaging = kernel_paging;
        this.pageSize = page_size;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#createPage(musaico.security.Credentials,musaico.region.Position,musaico.field.Field)
     */
    @Override
    public Page createPage (
                            Credentials credentials,
                            Position start_position,
			    Field swap_state_configuration
                            )
        throws I18nIllegalArgumentException,
               SwapException
    {
        if ( credentials == null
             || start_position == null
             || ! start_position.space ().equals ( this.space () ))
        {
            throw new I18nIllegalArgumentException ( "SwapState [%swap_state%] cannot create a page for credentials [%credentials%] at start position [%start_position%]",
                                                     "swap_state", this,
                                                     "credentials", credentials,
                                                     "start_position", start_position );
        }

        Space space = this.space ();
        Size page_size = this.pageSize ();
        Position next_start_position =
            space.expr ( start_position ).add ( page_size ).position ();
        Position end_position =
            space.expr ( next_start_position ).previous ();
        Region page_region = space.region ( start_position, end_position );
        if ( ! page_region.size ().equals ( page_size ) )
        {
            throw new SwapException ( "Swap state [%swap_state%] could not create a page region of size [%expected_page_size%]: region [%actual_page_region%] is size [%actual_page_size%]",
                                      "swap_state", this,
                                      "expected_page_size", page_size,
                                      "actual_page_region", page_region,
                                      "actual_page_size", page_region.size () );
        }

	// The block driver is configured per paged area,
	// so that this swap state can remain generic.
	BlockDriver block_driver =
	    swap_state_configuration.value ( BlockDriver.class );

        // Just throw any exceptions up the chain.
        Page block_page = new SimpleBlockPage ( this.kernelPaging (),
                                                this, // swap_state
                                                block_driver,
                                                page_region );

        return block_page;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null
             || ! ( obj instanceof BlockSwapState ) )
        {
            return false;
        }

        BlockSwapState that = (BlockSwapState) obj;

        if ( ! this.id ().equals ( that.id () )
             || ! this.kernelPaging ().equals ( that.kernelPaging () )
             || ! this.pageSize ().equals ( that.pageSize () )
             || ! this.space ().equals ( that.space () ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        return this.id ().hashCode () + this.pageSize ().hashCode ();
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#id()
     */
    @Override
    public Reference id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#kernelPaging()
     */
    @Override
    public KernelPaging kernelPaging ()
    {
        return this.kernelPaging;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#pageSize(!!!)
     */
    @Override
    public Size pageSize ()
    {
        return this.pageSize;
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#space(!!!)
     */
    @Override
    public Space space ()
    {
        return this.pageSize ().space ();
    }
}
