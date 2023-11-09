package musaico.kernel.memory.paging.pagedareas;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.buffer.Buffer;
import musaico.buffer.BufferException;
import musaico.buffer.BufferTools;
import musaico.buffer.NullBuffer;

import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Filter;
import musaico.io.Reference;

import musaico.kernel.memory.MemoryException;

import musaico.kernel.memory.paging.PagedArea;
import musaico.kernel.memory.paging.PagedAreaIdentifier;
import musaico.kernel.memory.paging.PageFault;
import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.PageFault;
import musaico.kernel.memory.paging.PageTable;
import musaico.kernel.memory.paging.PageTableException;
import musaico.kernel.memory.paging.SimplePageFault;
import musaico.kernel.memory.paging.SwapConfiguration;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapOperation;
import musaico.kernel.memory.paging.SwapState;
import musaico.kernel.memory.paging.SwapSystem;

import musaico.kernel.memory.paging.buffer.BufferPage;

import musaico.region.Position;
import musaico.region.Region;
import musaico.region.Size;
import musaico.region.Space;
import musaico.region.SparseRegion;

import musaico.security.Credentials;
import musaico.security.SecurityException;


/**
 * <p>
 * A simple paged area owned by some specific user or module
 * which uses whatever paging and swapping materials are given to it.
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
 * Copyright (c) 2009, 2010, 2011, 2012 Johann Tienhaara
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
public class SimplePagedArea
    implements PagedArea, Serializable
{
    /** Lock for swapping and so on. */
    private final Serializable lock = new String ();

    /** The unique identifier for this SimplePagedArea. */
    private final PagedAreaIdentifier id;

    /** The KernelPaging manager for all paged areas in the kernel. */
    private final KernelPaging kernelPaging;

    /** The list of swap states for this paged area. */
    private final SwapSystem swapSystem;

    /** The configuration data for each swap state, such as which block
     *  driver to use for a swapped-out-to-block-driver swap state.
     *  Each swap state has exactly one field to do whatever it pleases
     *  with (including store Field.NULL). */
    private final SwapConfiguration swapConfiguration;

    /** The lookup of Pages for this paged area.
     *  Each Page might be in a different swap state! */
    private final PageTable pageTable;


    /**
     * <p>
     * Creates a new SimplePagedArea.
     * </p>
     *
     * @param id The unique identifier for the new SimplePagedArea.
     *           Must not be null.  Must be unique throughout
     *           the kernel.
     *
     * @param swap_system The swap states and swappers for
     *                    this paged area.  Can be shared by
     *                    other paged areas.  Must not be null.
     *
     * @param swap_configuration The configuration settings for
     *                           the various swap states defined
     *                           in the swap system (such as which
     *                           block driver to use for a
     *                           swapped-out-to-block-driver swap state).
     *                           Must not be null.
     *
     * @param kernel_paging The LRU and dirty/clean pages list
     *                      for the whole kernel.  Must not be null.
     *
     * @param page_table The lookup of Pages for this PagedArea.
     *                   Must not be null.
     *
     * @throws I18nIllegalArgumentException If any parameter is null
     *                                      or invalid.
     */
    public SimplePagedArea (
                            PagedAreaIdentifier id,
                            SwapSystem swap_system,
			    SwapConfiguration swap_configuration,
                            KernelPaging kernel_paging,
                            PageTable page_table
                            )
        throws I18nIllegalArgumentException
    {
        if ( id == null
             || swap_system == null
	     || swap_configuration == null
             || kernel_paging == null
             || page_table == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create PagedArea [%paged_area_class%] with id [%id%] using swap system [%swap_system%] swap configuration [%swap_configuration%] kernel paging manager [%kernel_paging%] with page table [%page_table%]",
                                                     "paged_area_class", this.getClass (),
                                                     "id", id,
                                                     "swap_system", swap_system,
                                                     "swap_configuration", swap_configuration,
                                                     "kernel_paging", kernel_paging,
                                                     "page_table", page_table );
        }

        this.id = id;
        this.swapSystem = swap_system;
	this.swapConfiguration = swap_configuration;
        this.kernelPaging = kernel_paging;
        this.pageTable = page_table;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#free()
     */
    @Override
    public void free ()
        throws MemoryException
    {
        // If any errors occur while freeing everything,
        // keep going, but then throw the first exception after
        // we've done our best to clean up.
        MemoryException memory_exception = null;
        synchronized ( this.lock )
        {
            // Free all the pages in the page table.
            Region all_pages_region = this.pageTable.region ();
            final Page [] pages;
            try
            {
                pages = this.pageTable.pages ( all_pages_region );
                this.pageTable.remove ( pages );
            }
            catch ( PageTableException e )
            {
                // Couldn't get or remove the pages!
                // Fatal.
                throw e;
            }

            for ( int p = 0; p < pages.length; p ++ )
            {
                try
                {
                    pages [ p ].free ();
                }
                catch ( MemoryException e )
                {
                    // Couldn't free this page!  Keep going but
                    // throw this exception later.
                    if ( memory_exception == null )
                    {
                        memory_exception = e;
                    }
                }
            }
        }

        // If anything failed, throw the first failure.
        if ( memory_exception != null )
        {
            throw memory_exception;
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#id()
     */
    @Override
    public PagedAreaIdentifier id ()
    {
        return this.id;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#pageFault(musaico.security.Credentials,musaico.kernel.memory.paging.PageFault)
     */
    @Override
    public void pageFault (
                           Credentials credentials,
                           PageFault fault
                           )
        throws I18nIllegalArgumentException,
               MemoryException,
               SecurityException
    {
        if ( credentials == null
             || fault == null )
        {
            throw new I18nIllegalArgumentException ( "PagedArea [%paged_area%] cannot handle page fault with credentials [%credentials%] for fault [%page_fault%]",
                                                     "paged_area", this.id (),
                                                     "credentials", credentials,
                                                     "page_fault", fault );
        }

        /*
         * !!! No longer a Segment method, so this permissions check
         * !!! should have already happened by the time we get here.
         *
        // Check permissions first.
        Permissions<SegmentFlag> requested_permissions =
            new SegmentPermissions ( credentials,
                                     this.id (),
                                     SegmentFlag.ALLOW_SWAP );
        Permissions<SegmentFlag> granted_permissions =
            this.security ().request ( requested_permissions );
        if ( ! granted_permissions.isAllowed ( requested_permissions ) )
        {
            throw new SecurityException ( "Access denied: PagedArea [%paged_area%] cannot swap [%page_fault%] for credentials [%credentials%]",
            "paged_area", this,
            "page_fault", fault,
            "credentials", credentials );
            }
        !!! */

        // Now swap the page in.
        Position page_fault_position = fault.pageRef ();
        SwapState target_swap_state = fault.swapStateTarget ();

        // !!! Critical section involves IO!!!  (swapping):
        synchronized ( this.lock )
        {
            try
            {
                Page fault_page = this.swapIn ( credentials,
                                                page_fault_position,
                                                target_swap_state );
                fault.page ( fault_page, 0 );
            }
            catch ( MemoryException e )
            {
                fault.page ( null, PageFault.ERROR );
                throw new MemoryException ( "PagedArea [%paged_area%] could not swap in page at [%page_position%]",
                                            "paged_area", this,
                                            "page_position", page_fault_position,
                                            "cause", e );
            }
        }
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#pageTable()
     */
    @Override
    public PageTable pageTable ()
    {
        return this.pageTable;
    }


    /**
     * <p>
     * Used internally to resolve the underlying physical Buffer
     * for one specific Page.
     * </p>
     */
    private Buffer physicalBuffer (
                                   Credentials credentials,
                                   Page page
                                   )
    {
        if ( page == null )
        {
            return new NullBuffer ();
        }

        if ( ! ( page instanceof BufferPage ) )
        {
            // Need to swap in the Page (or possibly even swap out
            // from fast cache etc).
            final SwapSystem swap_system = this.swapSystem ();
            SwapState swapped_in_to_fields = swap_system.swappedInToFields ();
            PageFault page_fault =
                new SimplePageFault ( page.region ().start (),
                                      swapped_in_to_fields );
            int flags;
            try
            {
                this.pageFault ( credentials, page_fault );
                flags = page_fault.flags ();
            }
            catch ( MemoryException e )
            {
                // !!! do something about it...
                System.err.println ( "!!! unhandled SegmentOperationException in VirtualBuffer" );
                return null;
            }
            catch ( SecurityException e )
            {
                // !!! do something about it...
                System.err.println ( "!!! unhandled SegmentOperationException in VirtualBuffer" );
                return null;
            }

            if ( flags != 0 )
            {
                // Failed to retrieve the page due to out of memory,
                // no such page, and so on.
                // !!! log it
                return null;
            }

            page = page_fault.page ();

            if ( page == null
                 || ! ( page instanceof BufferPage ) )
            {
                // !!! do something about it...
                System.err.println ( "!!! page fault should have given us the page but got back " + page + " for page id "+ page.region ().start () );
                return null;
            }
        }

        // Now we've got the page.  Get the appropriate field out
        // of its buffer.
        BufferPage buffer_page = (BufferPage) page;
        // !!! probably need to do referencing, pinning the page, etc
        Buffer physical_buffer = buffer_page.buffer ();

        return physical_buffer;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#read(musaico.security.Credentials,musaico.region.Position,musaico.buffer.Buffer,musaico.region.Region)
     */
    public Region read (
                        Credentials credentials,
                        Position memory_start,
                        Buffer read_into_buffer,
                        Region read_into_region
                        )
        throws MemoryException
    {
        // !!! Probably need to lock the PageTable before we start
        // !!! working on the Pages!  Either that or set a flag and
        // !!! fail any requests that come in while it is set...

        Space memory_space = this.pageTable ().space ();
        Region all_memory_region = this.pageTable ().region ();
        Size memory_size =
            memory_space.from ( read_into_region.size () );
        Position memory_end = memory_space
            .expr ( memory_start )
            .add ( memory_size )
            .previous ();
        if ( ! all_memory_region.contains ( memory_end ) )
        {
            memory_end = all_memory_region.end ();
        }
        Region memory_region = memory_space.region ( memory_start, memory_end );

        List<BufferPage> in_memory_pages =
            this.swapIn ( credentials, memory_region );

        // Now we have a list of all the Pages we need.
        // Let's step through them and read their contents
        // and read in the Fields to the Buffer provided.
        Position curr_start = memory_start;
        Position curr_read_position =
            read_into_region.space ().outOfBounds ();
        Position read_end = read_into_region.end ();
        boolean is_empty_region = true;
        for ( BufferPage buffer_page : in_memory_pages )
        {
            Position curr_end = memory_end;
            final boolean is_last_page_required;
            if ( buffer_page.region ().contains ( curr_end ) )
            {
                // We can exit the loop after copying this Page.
                is_last_page_required = true;
            }
            else
            {
                // There are still more Pages to copy after this one.
                is_last_page_required = false;
                curr_end = buffer_page.region ().end ();
            }

            try
            {
                for ( Position memory_position = curr_start,
                          buffer_position = curr_read_position;
                      buffer_page.region ().contains ( memory_position )
                          && read_into_region.contains ( buffer_position );
                      memory_position = buffer_page.region ().expr ( memory_position ).next (),
                          buffer_position = read_into_region.expr ( buffer_position ).next () )
                {
                    Field field = buffer_page.buffer ().get ( memory_position );
                    read_into_buffer.set ( buffer_position, field );

                    curr_read_position = buffer_position;
                    is_empty_region = false;
                }
            }
            catch ( BufferException e )
            {
                throw new MemoryException ( "PagedArea [%paged_area%] failed to read starting at [%memory_position%] into buffer [%read_into_buffer%] region [%read_into_region%]",
                                            "paged_area", this,
                                            "memory_position", memory_start,
                                            "read_into_buffer", read_into_buffer,
                                            "read_into_region", read_into_region,
                                            "cause", e );
            }

            curr_start = memory_space.expr ( curr_end ).next ();
        }

        final Region actual_read_into_region;
        if ( is_empty_region )
        {
            actual_read_into_region = read_into_region.space ().empty ();
        }
        else
        {
            Position actual_start = read_into_region.start ();
            Position actual_end = curr_read_position;
            actual_read_into_region =
                read_into_region.space ().region ( actual_start, actual_end );
        }

        return actual_read_into_region;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#readField(musaico.security.Credentials,musaico.region.Position)
     */
    public Field readField (
                            Credentials credentials,
                            Position memory_position
                            )
        throws MemoryException
    {
        // !!! Probably need to lock the PageTable before we start
        // !!! working on the Pages!  Either that or set a flag and
        // !!! fail any requests that come in while it is set...

        Space memory_space = this.pageTable ().space ();
        Region memory_region =
            memory_space.region ( memory_position, memory_position );

        List<BufferPage> in_memory_pages =
            this.swapIn ( credentials, memory_region );

        if ( in_memory_pages.size () != 1 )
        {
            System.out.println ( "!!! uh oh" + in_memory_pages.size () );
            throw new MemoryException ( "PagedArea [%paged_area%] tried to swap in one Field at [%memory_position%] but instead swapped in [%num_in_memory_pages%] pages",
                                        "paged_area", this,
                                        "memory_position", memory_position,
                                        "num_in_memory_pages", in_memory_pages.size () );
        }

        BufferPage buffer_page = in_memory_pages.get ( 0 );
        final Field field = buffer_page.buffer ().get ( memory_position );

        return field;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#resize(musaico.security.Credentials,musaico.region.Region)
     */
    @Override
    public Region resize (
                          Credentials credentials,
                          Region region
                          )
        throws SwapException
    {
        // Create the pages for the memory to be allocated.
        PageTable page_table = this.pageTable ();
        final Region old_region = page_table.region ();
        SwapState swapped_out = this.swapSystem ().swapStates ().first ();
        Size page_size = swapped_out.pageSize ();
        Size region_size = region.size ();
        long num_fields_in_page = (long)
            swapped_out.space ().expr ( page_size )
            .ratio ( swapped_out.space ().one () );
        long num_fields_to_allocate = (long)
            region.space ().expr ( region_size )
            .ratio ( region.space ().one () );
        final Region allocated_region;
        if ( num_fields_to_allocate >= num_fields_in_page )
        {
            long num_missing_fields =
                num_fields_to_allocate % num_fields_in_page;
            if ( num_missing_fields == 0L )
            {
                allocated_region = region;
            }
            else
            {
                Size extra_size =
                    region.space ().expr ( region.space ().one () )
                    .multiply ( (double) num_missing_fields ).size ();
                Size allocated_size = region.space ().expr ( region_size )
                    .add ( extra_size ).size ();
                Position allocated_start = region.start ();
                Position allocated_end =
                    region.space ().expr ( allocated_start )
                    .add ( allocated_size ).position ();
                allocated_region =
                    region.space ().region ( allocated_start,
                                             allocated_end );
                num_fields_to_allocate += num_missing_fields;
            }
        }
        else
        {
            // Grow the allocated region size to at least fit
            // one page.
            double grow_ratio =
                (double) num_fields_in_page
                / (double) num_fields_to_allocate;
            Size allocated_size = region.space ().expr ( region_size )
                .multiply ( grow_ratio ).size ();
            Position allocated_start = region.start ();
            Position allocated_end =
                region.space ().expr ( allocated_start )
                .add ( allocated_size ).position ();
            allocated_region = region.space ().region ( allocated_start,
                                                        allocated_end );
            num_fields_to_allocate = num_fields_in_page;
        }

	Field swap_state_configuration =
	    this.swapConfiguration ().forSwapState ( swapped_out );

        Position page_start = swapped_out.space ().origin ();
        for ( long num_fields_allocated = 0L;
              num_fields_allocated < num_fields_to_allocate;
              num_fields_allocated += num_fields_in_page )
        {
            Page page =
                swapped_out.createPage ( credentials,
                                         page_start,
					 swap_state_configuration );
            page_table.put ( new Page [] { page } );

            page_start = swapped_out.space ().expr ( page_start )
                .add ( page_size ).position ();
        }

        return old_region;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#swapConfiguration()
     */
    @Override
    public SwapConfiguration swapConfiguration ()
    {
	return this.swapConfiguration;
    }


    /**
     * <p>
     * Swaps in (or out) all Pages covering the specified Region
     * of Fields, and returns the contiguous list of BufferPages
     * so that the Fields can be accessed directly.
     * </p>
     */
    private List<BufferPage> swapIn (
                                     Credentials credentials,
                                     Region region
                                     )
        throws MemoryException
    {
        SwapSystem swap_system = this.swapSystem ();
        SwapState swapped_in_to_fields = swap_system.swappedInToFields ();
        PageTable page_table = this.pageTable ();

        Page [] pages = page_table.pages ( region );
        List<BufferPage> in_memory_pages = new ArrayList<BufferPage> ();
        for ( Page page : pages )
        {
            if ( page instanceof BufferPage )
            {
                // Already swapped in to Fields, no need to swap.
                BufferPage buffer_page = (BufferPage) page;
                in_memory_pages.add ( buffer_page );
                continue;
            }

            // This Page needs to be swapped in (or possibly out,
            // if it is swapped in all the way to fast object cache
            // or something similar).
            Region page_region = page.region ();
            SwapOperation swap_operation =
                swap_system.createSwapOperation ( credentials,
                                                  this, // paged_area
                                                  page_region.start (),
                                                  swapped_in_to_fields );

            swap_operation.swap ( credentials );

            // Now get the swapped-in Pages.
            Page [] in_pages = swap_operation.targetPages ();
            this.pageTable ().put ( in_pages );
            for ( Page in_page : in_pages )
            {
                in_page.kernelPaging ().clean ( in_page );
                if ( ! ( in_page instanceof BufferPage ) )
                {
                    // Uh-oh, how can we have a Page swapped
                    // in to fields that is not a BufferPage???
                    throw new MemoryException ( "PagedArea [%paged_area%] has corrupt Pages / SwapSystem: page [%page%] swap state [%actual_swap_state%] should be in swapped-in-to-fields state [%expected_swap_state%] and should be a BufferPage but is a [%page_class%]",
                                                "paged_area", this,
                                                "page", in_page,
                                                "actual_swap_state", in_page.swapState (),
                                                "expected_swap_state", swapped_in_to_fields,
                                                "page_class", in_page.getClass () );
                }

                BufferPage buffer_page = (BufferPage) in_page;
                in_memory_pages.add ( buffer_page );
            }
        }

        return in_memory_pages;
    }


    /**
     * <p>
     * Big messy routine to swap in all out page(s) to
     * the specified in page as well as any more in page(s) needed
     * to catch up to the size of the out page.
     * </p>
     *
     * <p>
     * The PagedArea must be locked during this method call.
     * </p>
     */
    private Page swapIn (
                         Credentials credentials,
                         Position page_position,
                         SwapState target_swap_state
                         )
        throws MemoryException,
               SwapException
    {
        SwapOperation swap_operation =
            this.swapSystem ().createSwapOperation ( credentials,
                                                     this,
                                                     page_position,
                                                     target_swap_state );
        swap_operation.swap ( credentials );

        Page [] in_pages = swap_operation.targetPages ();
        this.pageTable ().put ( in_pages );
        for ( Page in_page : in_pages )
        {
            in_page.kernelPaging ().clean ( in_page );
        }

        Page desired_page = this.pageTable ().page ( page_position );

        return desired_page;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#swapSystem()
     */
    @Override
    public SwapSystem swapSystem ()
    {
        return this.swapSystem;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#synchronize(musaico.security.Credentials,musaico.kernel.memory.paging.Page,musaico.kernel.memory.paging.Page)
     */
    @Override
    public void synchronize (
                             Credentials credentials,
                             Page in_page,
                             Page out_page
                             )
        throws I18nIllegalArgumentException,
               SwapException
    {
        if ( credentials == null
             || in_page == null
             || out_page == null )
        {
            throw new I18nIllegalArgumentException ( "Paged area [%paged_area%] cannot synchronize pages [%in_page%], [%out_page%] with credentials [%credentials%]",
                                                     "paged_area", this,
                                                     "in_page", in_page,
                                                     "out_page", out_page,
                                                     "credentials", credentials );
        }

        final SwapSystem swap_system = this.swapSystem ();

        synchronized ( this.lock )
        {
            final SwapOperation swap_operation;
            if ( this.kernelPaging.isDirty ( in_page ) )
            {
                // Copy out the dirty data.
                swap_operation =
                    swap_system.createSwapOperation ( credentials,
                                                      this,
                                                      in_page.region ().start (),
                                                      out_page.swapState () );
            }
            else
            {
                // Read in data.
                swap_operation =
                    swap_system.createSwapOperation ( credentials,
                                                      this,
                                                      out_page.region ().start (),
                                                      in_page.swapState () );
            }

            swap_operation.swap ( credentials );

            this.kernelPaging.clean ( in_page );
        }

        /* !!! OLD CODE:
        // Break out the swapper.
        // Throws exception if invalid swap states.
        Swapper swapper =
            this.swapSystem ().swapper ( out_page.swapState (),
                                         in_page.swapState () );

        // Find the overlapping positions between the out
        // page and the in page (usually out page is smaller,
        // but that's no guarantee).
        final Region swap_region;
        Region out_page_region = out_page.region ();
        Region in_page_region = in_page.region ();

        // Cast the in page region to an out page region,
        // since the latter is usually smaller (in-memory
        // page size vs. block size).  Find the intersection:
        // this will be the swap region.
        Position in_page_start_out =
            swapper.outPosition ( in_page_region.start () );
        Position in_page_end_out =
            swapper.outPosition ( in_page_region.end () );
        Region in_page_out_region =
            out_page_region.space ().region ( in_page_start_out,
                                              in_page_end_out );
        Region out_swap_region =
            out_page_region.space ().expr ( out_page_region )
            .intersection ( in_page_out_region ).region ();
        Position in_swap_start =
            swapper.inPosition ( out_swap_region.start () );
        Position in_swap_end =
            swapper.inPosition ( out_swap_region.end () );
        Region in_swap_region =
            in_page_region.space ().region ( in_swap_start,
                                             in_swap_end );

        // If the page is dirty, write it out.
        // Otherwise, read in from the out page, overwriting the
        // in page contents.
        synchronized ( this.lock )
        {
            if ( this.kernelPaging.isDirty ( in_page ) )
            {
                swapper.writeOut ( in_page,
                                   in_swap_region,
                                   out_page,
                                   out_swap_region );
            }
            else
            {
                swapper.readIn ( out_page,
                                 in_swap_region,
                                 in_page,
                                 out_swap_region );
            }

            this.kernelPaging.clean ( in_page );
        }
        !!! */
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#write(musaico.security.Credentials,musaico.region.Position,musaico.buffer.Buffer,musaico.region.Region)
     */
    public Region write (
                         Credentials credentials,
                         Position memory_start,
                         Buffer write_from_buffer,
                         Region write_from_region
                         )
        throws MemoryException
    {
        // !!! Probably need to lock the PageTable before we start
        // !!! working on the Pages!  Either that or set a flag and
        // !!! fail any requests that come in while it is set...

        Space memory_space = this.pageTable ().space ();
        Region all_memory_region = this.pageTable ().region ();
        Size memory_size =
            memory_space.from ( write_from_region.size () );
        Position memory_end = memory_space
            .expr ( memory_start )
            .add ( memory_size )
            .previous ();
        if ( ! all_memory_region.contains ( memory_end ) )
        {
            memory_end = all_memory_region.end ();
        }
        Region memory_region = memory_space.region ( memory_start, memory_end );

        List<BufferPage> in_memory_pages =
            this.swapIn ( credentials, memory_region );

        // Now we have a list of all the Pages we need.
        // Let's step through them and write to them from
        // the Fields to the Buffer provided.
        Position curr_start = memory_start;
        Position curr_write_position =
            write_from_region.space ().outOfBounds ();
        Position write_end = write_from_region.end ();
        boolean is_empty_region = true;
        for ( BufferPage buffer_page : in_memory_pages )
        {
            Position curr_end = memory_end;
            final boolean is_last_page_required;
            if ( buffer_page.region ().contains ( curr_end ) )
            {
                // We can exit the loop after copying this Page.
                is_last_page_required = true;
            }
            else
            {
                // There are still more Pages to copy after this one.
                is_last_page_required = false;
                curr_end = buffer_page.region ().end ();
            }

            try
            {
                for ( Position memory_position = curr_start,
                          buffer_position = curr_write_position;
                      buffer_page.region ().contains ( memory_position )
                          && write_from_region.contains ( buffer_position );
                      memory_position = buffer_page.region ().expr ( memory_position ).next (),
                          buffer_position = write_from_region.expr ( buffer_position ).next () )
                {
                    Field field = write_from_buffer.get ( buffer_position );
                    buffer_page.buffer ().set ( memory_position, field );

                    curr_write_position = buffer_position;
                    is_empty_region = false;
                }
            }
            catch ( BufferException e )
            {
                throw new MemoryException ( "PagedArea [%paged_area%] failed to write starting at [%memory_position%] from buffer [%write_from_buffer%] region [%write_from_region%]",
                                            "paged_area", this,
                                            "memory_position", memory_start,
                                            "write_from_buffer", write_from_buffer,
                                            "write_from_region", write_from_region,
                                            "cause", e );
            }

            curr_start = memory_space.expr ( curr_end ).next ();
        }

        final Region actual_written_from_region;
        if ( is_empty_region )
        {
            actual_written_from_region = write_from_region.space ().empty ();
        }
        else
        {
            Position actual_start = write_from_region.start ();
            Position actual_end = curr_write_position;
            actual_written_from_region =
                write_from_region.space ().region ( actual_start, actual_end );
        }

        return actual_written_from_region;
    }


    /**
     * @see musaico.kernel.memory.paging.PagedArea#writeField(musaico.security.Credentials,musaico.region.Position,musaico.field.Field)
     */
    public void writeField (
                            Credentials credentials,
                            Position memory_position,
                            Field write_field
                            )
        throws MemoryException
    {
        // !!! Probably need to lock the PageTable before we start
        // !!! working on the Pages!  Either that or set a flag and
        // !!! fail any requests that come in while it is set...

        Space memory_space = this.pageTable ().space ();
        Region memory_region =
            memory_space.region ( memory_position, memory_position );

        List<BufferPage> in_memory_pages =
            this.swapIn ( credentials, memory_region );

        if ( in_memory_pages.size () != 1 )
        {
            throw new MemoryException ( "PagedArea [%paged_area%] tried to swap in one Field at [%memory_position%] but instead swapped in [%num_in_memory_pages%] pages",
                                        "paged_area", this,
                                        "memory_position", memory_position,
                                        "num_in_memory_pages", in_memory_pages.size () );
        }

        BufferPage buffer_page = in_memory_pages.get ( 0 );

        try
        {
            buffer_page.buffer ().set ( memory_position, write_field );
        }
        catch ( BufferException e )
        {
            throw new MemoryException ( "PagedArea [%paged_area%] failed to write field at [%memory_position%] field [%write_field%]",
                                        "paged_area", this,
                                        "memory_position", memory_position,
                                        "write_field", write_field,
                                        "cause", e );
        }
    }
}
