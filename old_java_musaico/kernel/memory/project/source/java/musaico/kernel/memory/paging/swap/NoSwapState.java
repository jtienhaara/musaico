package musaico.kernel.memory.paging.swap;

import java.io.Serializable;


import musaico.field.Field;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Reference;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.memory.paging.KernelPaging;
import musaico.kernel.memory.paging.Page;
import musaico.kernel.memory.paging.SwapException;
import musaico.kernel.memory.paging.SwapState;

import musaico.kernel.memory.paging.kernelpagings.SimpleKernelPaging;

import musaico.region.Position;
import musaico.region.Size;
import musaico.region.Space;

import musaico.region.array.ArraySpace;

import musaico.security.Credentials;


/**
 * <p>
 * Illegal swap state.  Nothing can be swapped into or
 * out of this state.
 * </p>
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
public class NoSwapState
    implements SwapState, Serializable
{
    /** The unique identifier for NoSwapState. */
    private final Reference id =
        new SimpleSoftReference<String> ( "no_swap_state" );


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
        throw new SwapException ( "NoSwapState cannot create a page with credentials [%credentials%] at position [%start_position%] with swap state configuration [%swap_state_configuration%]",
                                  "credentials", credentials,
                                  "start_position", start_position,
				  "swap_state_configuration", swap_state_configuration );
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
             || ! ( obj instanceof NoSwapState ) )
        {
            return false;
        }
        else
        {
            // no swap state = no swap state.
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
        return new SimpleKernelPaging ();
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#pageSize()
     */
    @Override
    public Size pageSize ()
    {
        return this.space ().none ();
    }


    /**
     * @see musaico.kernel.memory.paging.SwapState#space()
     */
    @Override
    public Space space ()
    {
        return ArraySpace.STANDARD;
    }
}
