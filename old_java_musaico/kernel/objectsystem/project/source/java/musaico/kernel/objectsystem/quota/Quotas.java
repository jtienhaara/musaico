package musaico.kernel.objectsystem.quota;

import java.io.Serializable;


import musaico.field.Attribute;

import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.kernel.QuotaCategory;

import musaico.kernel.objectsystem.Record;

import musaico.region.Size;

import musaico.security.Credentials;

import musaico.time.RelativeTime;


/**
 * <p>
 * A Record for keeping track of an object system's quotas.
 * </p>
 *
 * <p>
 * A UNIX-like kernel and object system might have quotas for
 * owner, group and other, restricting the number of fields
 * and objects for each category.  However more complex quota setups
 * can be designed, depending on the needs of the object
 * system.
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
public interface Quotas
    extends Record, Serializable
{
    /** No quotas for the object system.  Quota limits cannot be
     *  set for the object system. */
    public static final Quotas NONE = new NoQuotas ();


    /**
     * <p>
     * Returns the quota categories supported by this Quotas.
     * </p>
     *
     * <p>
     * For example, if this kernel supports user, group and
     * module quotas, then each Quotas will store separate
     * quotas for { QuotaCategories.USER, QuotaCategories.GROUP
     * and QuotaCategories.MODULE }, as well as the current counts
     * for each individual user, group and module.
     * </p>
     *
     * @return The quota categories stored in this Quotas.
     *         Never null.  Never contains any null elements.
     */
    public abstract QuotaCategory [] categories ();


    /**
     * <p>
     * Returns the QuotaCategory(ies) in which the specified
     * Credentials must observe quotas.
     * </p>
     *
     * <p>
     * For example, a user Credentials might be required by a
     * kernel to maintain the current number of ONodes for
     * their user credentials, as well as for each of their
     * groups.
     * </p>
     *
     * <p>
     * This is a helper method.  The implementation shall
     * delegate to the kernel's quotas layer.
     * </p>
     *
     * @param credentials The credentials whose quota
     *                    categories will be returned.
     *                    Must not be null.
     *
     * @return The quota category(ies) which the specified
     *         credentials must observe.  Can be empty
     *         (for example if a kernel does not support
     *         quotas at all, or if a module credentials is
     *         passed in but kernel modules do not have
     *         to observe quotas).  Never null.
     *         Never contains any null elements.
     */
    public abstract QuotaCategory [] categoriesFor (
						    Credentials credentials
						    );


    /**
     * <p>
     * Returns the resources whose quotas are maintained for this
     * Quotas.
     * </p>
     *
     * <p>
     * For example, { QuotaResource.ONODES, QuotaResource.FIELDS }
     * might be tracked by an Quotas which limits the number
     * of ONodes owned by each Credentials and the number of Fields
     * stored by each Credentials.
     * </p>
     *
     * @return The quota resources which might be capped by this
     *         Quotas.  Never null.  Never contains any
     *         null elements.
     */
    public abstract QuotaResource [] resources ();


    /**
     * <p>
     * The quota attribute defining the hard maximum size
     * for the specified quota category (such as users or
     * groups) creating or owning the specified resource
     * (such as Fields or ONodes).
     * </p>
     *
     * <p>
     * The hard limit is strictly enforced.
     * No Credentials can use more than the hard limit
     * number of resources within the object system
     * enforcing this limit.  Once the hard limit is reached, attempts
     * by the same Credentials to add new resources of the
     * same type will fail.
     * </p>
     *
     * @param category The quota category (users, groups, modules, ...).
     *                 Must be in the set returned by categories().
     *                 Must not be null.
     *
     * @param resource The quota resource (ONodes, fields, ... ).
     *                 Must be in the set returned by resources().
     *                 Must not be null.
     *
     * @return The hard limit quota attribute for the specified
     *         category (users, groups, ...) and resource type
     *         (fields, ONodes, ...).  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Attribute<Size> quotaHardLimit (
						    QuotaCategory category,
						    QuotaResource resource
						    )
	throws I18nIllegalArgumentException;


    /**
     * <p>
     * The quota attribute defining the soft maximum size
     * for the specified quota category (such as users or
     * groups) creating or owning the specified resource
     * (such as Fields or ONodes).
     * </p>
     *
     * <p>
     * The soft limit is not initially strictly enforced.
     * However after some grace period spent in violation
     * of the limit, the soft quota becomes a hard limit
     * and is strictly enforced.
     * </p>
     *
     * @param category The quota category (users, groups, modules, ...).
     *                 Must be in the set returned by categories().
     *                 Must not be null.
     *
     * @param resource The quota resource (ONodes, fields, ... ).
     *                 Must be in the set returned by resources().
     *                 Must not be null.
     *
     * @return The soft limit quota attribute for the specified
     *         category (users, groups, ...) and resource type
     *         (fields, ONodes, ...).  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Attribute<Size> quotaSoftLimit (
						    QuotaCategory category,
						    QuotaResource resource
						    )
	throws I18nIllegalArgumentException;


    /**
     * <p>
     * The quota attribute defining how long a user/group/
     * and so on can remain in violation of a soft limit
     * quota before it turns into a hard limit.
     * </p>
     *
     * @param category The quota category (users, groups, modules, ...).
     *                 Must be in the set returned by categories().
     *                 Must not be null.
     *
     * @param resource The quota resource (ONodes, fields, ... ).
     *                 Must be in the set returned by resources().
     *                 Must not be null.
     *
     * @return The soft limit grace period for the specified
     *         category (users, groups, ...) and resource type
     *         (fields, ONodes, ...).  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Attribute<RelativeTime>
	quotaGracePeriod (
			  QuotaCategory category,
			  QuotaResource resource
			  )
	throws I18nIllegalArgumentException;


    /**
     * <p>
     * The quota attribute defining the current usage by
     * the specified user/group/and so on of the specified
     * type of resource (such as Fields or ONodes).
     * </p>
     *
     * @param credentials The user/group/and so on whose
     *                    current usage will be returned.
     *
     * @param resource The quota resource (ONodes, fields, ... ).
     *                 Must be in the set returned by resources().
     *                 Must not be null.
     *
     * @return The current usage quota attribute for the specified
     *         credentials (user, group, ...) and resource type
     *         (fields, ONodes, ...).  Never null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public abstract Attribute<Size> quotaUsage (
						Credentials credentials,
						QuotaResource resource
						)
	throws I18nIllegalArgumentException;
}

/**
   !!!
     * / Operations working with dquots /
     * struct dquot_operations {
     *         int (*write_dquot) (struct dquot *);            / Ordinary dquot write /
     *         struct dquot *(*alloc_dquot)(struct super_block *, int);        / Allocate memory for new dquot /
     *         void (*destroy_dquot)(struct dquot *);          / Free memory for dquot /
     *         int (*acquire_dquot) (struct dquot *);          / Quota is going to be created on disk /
     *         int (*release_dquot) (struct dquot *);          / Quota is going to be deleted from disk /
     *         int (*mark_dirty) (struct dquot *);             / Dquot is marked dirty /
     *         int (*write_info) (struct super_block *, int);  / Write of quota "superblock" /
     *         / get reserved quota for delayed alloc, value returned is managed by
     *           quota code only /
     *         qsize_t *(*get_reserved_space) (struct inode *);
     * };
     */
