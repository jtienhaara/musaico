package musaico.kernel.operations;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Progress;

import musaico.kernel.KernelOperation;

import musaico.security.Credentials;


/**
 * <p>
 * Straightforward implementation of a request to some kernel
 * object or other, possibly from a user, or possibly from
 * another kernel object.
 * </p>
 *
 *
 * <p>
 * In Java every KernelOperation must be Serializable in order
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
public class SimpleKernelOperation
    implements KernelOperation, Serializable
{
    /** The Credentials who requested the operation in the first place. */
    private final Credentials credentials;

    /** The Progress meter for this kernel operation state. */
    private final Progress progress;


    /**
     * <p>
     * Creates a new SimpleKernelOperation for the specified requesting
     * Credentials with the specified initial settings.
     * </p>
     *
     * @param credentials The User or Module requesting the operation.
     *                    Must not be null.
     *
     * @param progress The progress meter, indicating how far the
     *                 operation has proceeded.  Updated as the
     *                 operation progresses.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleKernelOperation (
                                  Credentials credentials,
                                  Progress progress
                                  )
        throws I18nIllegalArgumentException
    {
        if ( credentials == null
             || progress == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SimpleKernelOperation for credentials [%credentials%] with progress [%progress%]",
                                                     "credentials", credentials,
                                                     "progress", progress );
        }

        this.credentials = credentials;
        this.progress = progress;
    }


    /**
     * @see musaico.kernel.KernelOperation#progress()
     */
    @Override
    public Credentials credentials ()
    {
        return this.credentials;
    }


    /**
     * @see musaico.kernel.KernelOperation#progress()
     */
    @Override
    public Progress progress ()
    {
        return this.progress;
    }
}
