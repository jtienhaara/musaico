package musaico.kernel;

import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Path;
import musaico.io.SimpleIdentifier;

import musaico.security.Credentials;


/**
 * <p>
 * A category of quotas, such as user quotas, group
 * quotas, module quotas and so on.
 * </p>
 *
 * <p>
 * Each object system can maintain quotas and
 * current levels for each quota category that the kernel
 * supports.  For example there might be hard limits and
 * soft limits on the number of entities owned by a user,
 * a group, and so on.
 * </p>
 *
 * <p>
 * The particular quota categories supported may vary from
 * kernel to kernel.  Some kernels might have traditional
 * user and group quotas, others might add module quotas
 * to the mix, while others might choose some undreamt-of
 * quota categories to maintain.  Each object system
 * maintains the quotas and current counts, but the kernel
 * decides the categories (who can have quota limits)
 * for all object systems.
 * </p>
 *
 *
 * <p>
 * In Java every QuotaCategory must be Serializable in
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
public class QuotaCategory
    extends SimpleIdentifier
    implements Serializable
{
    /** User quotas.  Each user must keep their current counts
     *  below the quotas in this category. */
    public static final QuotaCategory USER =
	new QuotaCategory ( "user", new Class [ 0 ] /* !!! */ );

    /** Group quotas.  Each group of users must keep their
     *  current counts below the quotas in this category. */
    public static final QuotaCategory GROUP =
	new QuotaCategory ( "group", new Class [ 0 ] /* !!! */ );

    /** Module quotas.  Each kernel module must keep their
     *  current counts below the quotas in this category. */
    public static final QuotaCategory MODULE =
	new QuotaCategory ( "module", new Class [ 0 ] /* !!! */ );


    /** The class(es) of credentials to which quotas in this category are
     *  applied. */
    private final Class<Credentials> [] credentialsClasses;


    /**
     * <p>
     * Creates a new QuotaCategory with the specified name
     * for the specified class of Credentials (users, groups
     * and so on).
     * </p>
     *
     * @param name This category's name, such as "user" or
     *             "group" and so on.  Must not be null.
     *
     * @param credentials_classes The class(es) of Credentials to
     *                            which quotas in this category
     *                            apply.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public QuotaCategory (
			  String name,
			  Class<Credentials>... credentials_classes
			  )
	throws I18nIllegalArgumentException
    {
	super ( KernelNamespaces.QUOTA_CATEGORIES,
		new Path ( name ) );

	boolean is_illegal_args = false;
	if ( name == null
	     || credentials_classes == null )
	{
	    is_illegal_args = true;
	}
	else
	{
	    for ( Class<Credentials> credentials_class : credentials_classes )
	    {
		if ( credentials_class == null )
		{
		    is_illegal_args = true;
		    break;
		}
	    }
	}

	if ( is_illegal_args )
	{
	    throw new I18nIllegalArgumentException ( "Cannot create a QuotaCategory with name [%name%] credentials classes [%credentials_classes%]",
						     "name", name,
						     "credentials_classes", credentials_classes );
	}

	this.credentialsClasses = credentials_classes;
    }
}
