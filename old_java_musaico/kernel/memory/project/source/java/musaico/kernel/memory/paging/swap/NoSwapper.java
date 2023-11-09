package musaico.kernel.memory.paging.swap;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.region.Position;
import musaico.region.Region;

import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.Swapper;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapState;

import musaico.security.Credentials;


/**
 * <p>
 * Only one level of memory, no swapping at all.
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
 * Copyright (c) 2009, 2010, 2012 Johann Tienhaara
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
public class NoSwapper
    implements Swapper, Serializable
{
    /**
     * @see musaico.kernel.memory.paging.Swapper#inPosition(musaico.region.Position)
     */
    @Override
    public Position inPosition (
                                Position out_position
                                )
        throws I18nIllegalArgumentException
    {
        return out_position;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#inState()
     */
    public SwapState inState ()
    {
        return new NoSwapState ();
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
        return in_position;
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#outState()
     */
    public SwapState outState ()
    {
        return new NoSwapState ();
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#readIn(musaico.security.Credentials,musaico.kernel.memory.paging.Page,musaico.region.Region,musaico.kernel.memory.paging.Page,musaico.region.Region)
     */
    public void readIn (
                        Credentials credentials,
                        Page out_page,
                        Region out_region,
                        Page in_page,
                        Region in_region
                        )
        throws SwapException
    {
        if ( out_page != in_page )
        {
            throw new SwapException ( "Swapper [%swapper%] cannot swap in from page [%out_page%] to page [%in_page%] with credentials [%credentials%]",
                                      "swapper", this,
                                      "out_page", out_page,
                                      "in_page", in_page,
                                      "credentials", credentials );
        }
    }


    /**
     * @see musaico.kernel.memory.paging.Swapper#writeOut(musaico.security.Credentials,musaico.kernel.memory.paging.Page,musaico.region.Region,musaico.kernel.memory.paging.Page,musaico.region.Region)
     */
    public void writeOut (
                          Credentials credentials,
                          Page in_page,
                          Region in_region,
                          Page out_page,
                          Region out_region
                          )
        throws SwapException
    {
        if ( in_page != out_page )
        {
            throw new SwapException ( "Swapper [%swapper%] cannot swap out from page [%in_page%] to the page [%out_page%] with credentials [%credentials%]",
                                      "swapper", this,
                                      "in_page", in_page,
                                      "out_page", out_page,
                                      "credentials", credentials );
        }
    }
}
