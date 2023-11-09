package musaico.kernel.objectsystem.quota;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.SimpleIdentifier;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * A system resource which can have quotas placed on it,
 * such as ONodes, Fields and so on.
 * </p>
 *
 * <p>
 * Each object system maintains quotas and current levels
 * for each quota resource that it supports.  For example
 * there might be hard limits and soft limits on the number
 * of ONodes owned by one Credentials, or the number of
 * Fields stored by one Credentials, and so on.
 * </p>
 *
 * <p>
 * The resource types which can have quotas imposed
 * depend on the object system.  Often ONodes and Fields
 * are supported, but any arbitrary resource type of use
 * to a particular object system can also be maintained.
 * (By contrast, quota categories -- to whom quota limits
 * can be applied -- are determined by the kernel and apply
 * to all object systems.)
 * </p>
 *
 *
 * <p>
 * In Java every QuotaResource must be Serializable in
 * order to play nicely over RMI.
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
public class QuotaResource
    extends SimpleIdentifier
    implements Serializable
{
    /** Number of ONodes owned.  Credentials must keep their
     *  current counts below the quotas for this resource. */
    public static final QuotaResource ONODES = new QuotaResource ( "onodes" );

    /** Number of Fields stored.  Credentials must keep their
     *  current counts below the quotas for this resource. */
    public static final QuotaResource FIELDS = new QuotaResource ( "fields" );


    /**
     * <p>
     * Creates a new QuotaResource with the specified name
     * for the specified class of Credentials (users, groups
     * and so on).
     * </p>
     *
     * @param name This resource's name, such as "onodes" or
     *             "fields" and so on.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public QuotaResource (
			  String name
			  )
	throws I18nIllegalArgumentException
    {
	super ( KernelNamespaces.QUOTA_RESOURCES,
		new Path ( name ) );

	boolean is_illegal_args = false;
	if ( name == null )
	{
	    is_illegal_args = true;
	}

	if ( is_illegal_args )
	{
	    throw new I18nIllegalArgumentException ( "Cannot create a QuotaResource with name [%name%] credentials classes [%credentials_classes%]",
						     "name", name );
	}
    }
}
