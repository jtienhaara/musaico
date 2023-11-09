package musaico.kernel.common.swappers.block;


import java.io.Serializable;


import musaico.kernel.driver.BlockDriver;

import musaico.kernel.memory.paging.Page;


/**
 * <p>
 * A page of data to be read from / written to a block driver.
 * </p>
 *
 * <p>
 * The BlockPage is not an in-memory page of data, but can
 * be swapped into / out from an in-memory page of data using
 * a Swapper.
 * </p>
 *
 * <p>
 * The Swapper reads this page's data in from the block driver, and
 * writes data out to the block driver.  It does NOT store
 * anything in memory.
 * </p>
 *
 *
 * <p>
 * In Java, all Pages must be Serializable.  Note, however,
 * that a Page may contain non-Serializable data.
 * (For example, a buffer Page with non-Serializable Fields.)
 * Therefore great care must be taken when passing Pages
 * back and forth across RMI.
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
 * Copyright (c) 2010, 2011 Johann Tienhaara
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
public interface BlockPage
    extends Page, Serializable
{
    /**
     * <p>
     * Returns the BlockDriver to/from which this page
     * writes and reads its data.
     * </p>
     *
     * @return This page's block driver.  Never null.
     */
    public abstract BlockDriver blockDriver ();
}
