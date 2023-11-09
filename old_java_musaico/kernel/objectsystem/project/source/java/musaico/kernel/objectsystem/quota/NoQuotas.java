package musaico.kernel.objectsystem.quota;

import java.io.Serializable;


import musaico.field.Attribute;

import musaico.kernel.KernelNamespaces;
import musaico.kernel.QuotaCategory;

import musaico.kernel.objectsystem.NoRecord;
import musaico.kernel.objectsystem.Record;

import musaico.region.Size;

import musaico.security.Credentials;

import musaico.time.RelativeTime;


/**
 * <p>
 * No quotas for an ONode.  These quotas cannot be modified, a new
 * Quotas must be created for the ONode instead.
 * </p>
 *
 *
 * <p>
 * In Java, because an object system can conceivably be distributed
 * over RMI, every Quotas must be Serializable in order to
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
public class NoQuotas
    extends NoRecord
    implements Quotas, Serializable
{
    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#categories()
     */
    @Override
    public QuotaCategory [] categories ()
    {
	return new QuotaCategory [ 0 ];
    }


    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#categoriesFor(musaico.security.Credentials)
     */
    @Override
    public QuotaCategory [] categoriesFor (
					   Credentials credentials
					   )
    {
	return new QuotaCategory [ 0 ];
    }


    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#resources()
     */
    @Override
    public QuotaResource [] resources ()
    {
	return new QuotaResource [ 0 ];
    }


    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#quotaHardLimit(musaico.kernel.QuotaCategory,musaico.kernel.objectsystem.quota.QuotaResource)
     */
    @Override
    public Attribute<Size> quotaHardLimit (
					   QuotaCategory category,
					   QuotaResource resource
					   )
    {
        return new Attribute<Size> ( "quota_hard_limit",
				     Size.class,
				     this.space ().position ( 0L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#quotaSoftLimit(musaico.kernel.QuotaCategory,musaico.kernel.objectsystem.quota.QuotaResource)
     */
    @Override
    public Attribute<Size> quotaSoftLimit (
					   QuotaCategory category,
					   QuotaResource resource
					   )
    {
        return new Attribute<Size> ( "quota_soft_limit",
				     Size.class,
				     this.space ().position ( 1L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#quotaGracePeriod(musaico.kernel.QuotaCategory,musaico.kernel.objectsystem.quota.QuotaResource)
     */
    @Override
    public Attribute<RelativeTime> quotaGracePeriod (
						     QuotaCategory category,
						     QuotaResource resource
						     )
    {
        return new Attribute<RelativeTime> ( "quota_grace_period",
					     RelativeTime.class,
					     this.space ().position ( 2L ) );
    }


    /**
     * @see musaico.kernel.objectsystem.quota.Quotas#quotaUsage(musaico.security.Credentials,musaico.kernel.objectsystem.quota.QuotaResource)
     */
    @Override
    public Attribute<Size> quotaUsage (
				       Credentials credentials,
				       QuotaResource resource
				       )
    {
        return new Attribute<Size> ( "quota_usage",
				     Size.class,
				     this.space ().position ( 3L ) );
    }
}
