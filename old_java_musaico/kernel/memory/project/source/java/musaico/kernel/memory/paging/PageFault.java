package musaico.kernel.memory.paging;


import java.io.Serializable;


import musaico.region.Position;


/**
 * <p>
 * Represents a page fault in the virtual memory layer.
 * </p>
 *
 *
 * <p>
 * In Java, every PageFault must be Serializable in order to
 * place nicely over RMI.
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
public interface PageFault
    extends Serializable
{
    /** Fault: out of memory. */
    public static final int           OUT_OF_MEMORY = 0x0001;

    /** Fault: !!! no idea what SIGBUS means, except that it's bad. */
    public static final int           SIGBUS        = 0x0002;

    /** Fault: !!! no idea what MAJOR means. */
    public static final int           MAJOR         = 0x0004;

    /** Fault: Special case for getUserPages (). */
    public static final int           WRITE         = 0x0008;


    /** Fault: No page. */
    public static final int           NO_PAGE       = 0x0100;

    /** Fault: Locked page. */
    public static final int           LOCKED        = 0x0200;


    /** Fault: Any error. */
    public static final int           ERROR         =
        PageFault.OUT_OF_MEMORY
        | PageFault.SIGBUS
        | PageFault.NO_PAGE;


    /** Fault flag: fault was a write access. */
    public static final int           FLAG_WRITE    = 0x0001;


    /**
     * <p>
     * Returns the flags for the fault (such as whether or not
     * the fault was caused by a write access).
     * </p>
     *
     * @return The flags for the fault.  Never negative.
     */
    public abstract int flags ();


    /**
     * <p>
     * Returns a reference to the Page which caused the fault.
     * </p>
     *
     * @return A reference to the Page which caused the fault.
     */
    public abstract Position pageRef ();


    /**
     * <p>
     * Page which caused the fault.  Filled in by the fault handler.
     * </p>
     *
     * @return The page which caused the fault.  Never null after
     *         return from the fault handler <i>unless</i> the
     *         fault handler returned a <code>PageFault.NO_PAGE</code>
     *         flag.
     */
    public abstract Page page ();


    /**
     * <p>
     * Sets the Page which caused the fault, and any flags.
     * Filled in by the fault handler.
     * </p>
     *
     * <p>
     * Write-once, read-only.
     * </p>
     *
     * @param page The page which induced the fault (possibly
     *             after swapping it in).  Can be null if the fault
     *             was unsuccessful (for example due to out of memory).
     *
     * @param flags The logically OR'ed-together flags for
     *              this SimplePageFault.  Must be 0 or more.
     */
    public abstract void page (
                               Page page,
                               int flags
                               );


    /**
     * <p>
     * Returns the desired state of the page to swap in.
     * </p>
     *
     * <p>
     * Typically this method returns
     * <code> SwapStates.SWAPPED_IN_TO_FIELDS </code>.
     * However if, for example, fast cache is needed, then
     * <code> SwapStates.SWAPPED_IN_TO_FAST_CACHE </code> might
     * be returned.  And so on.
     * </p>
     *
     * @return The desired end state of the Page to swap in.
     *         Must not be null.
     */
    public abstract SwapState swapStateTarget ();
}
