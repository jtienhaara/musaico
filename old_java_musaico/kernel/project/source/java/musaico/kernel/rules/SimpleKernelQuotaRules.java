package musaico.kernel.rules;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.KernelQuotaRules;
import musaico.kernel.QuotaCategory;


/**
 * <p>
 * Kernel-specific quota rules, such as the categories of credentials
 * which can have quotas applied (users, groups and so on).
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
public class SimpleKernelQuotaRules
    implements KernelQuotaRules
{
    /** The types of credentials which can be quota
     *  limited by the kernel. */
    private final QuotaCategory [] categories;


    /**
     * <p>
     * Creates a new SimpleKernelQuotaRules with the specified
     * quota categories (such as user, group, kernel modules).
     * </p>
     *
     * @param categories The quota categories supported by the
     *                   kernel.  Must not be null.  Must not
     *                   contain any null elements.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SimpleKernelQuotaRules (
				   QuotaCategory... categories
				   )
    {
	boolean is_illegal_categories = false;
	if ( categories == null )
        {
	    is_illegal_categories = true;
	}
	else
        {
	    for ( QuotaCategory category : categories )
	    {
		if ( category == null )
		{
		    is_illegal_categories = true;
		    break;
		}
	    }
	}

	if ( is_illegal_categories )
	{
	    throw new I18nIllegalArgumentException ( "Cannot create a SimpleKernelQuotaRules with categories [%categories%]",
						     "categories", categories );
	}

	// Make a defensive copy so nobody can change the
	// categories behind our back.
	this.categories = new QuotaCategory [ categories.length ];
	System.arraycopy ( categories, 0,
			   this.categories, 0, this.categories.length );
    }

    /**
     * @see musaico.kernel.KernelQuotaRules#categories()
     */
    public QuotaCategory [] categories ()
    {
	// Hopefully the caller doesn't overwrite our array...
	return this.categories;
    }
}
