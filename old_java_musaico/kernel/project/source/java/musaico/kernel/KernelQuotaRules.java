package musaico.kernel;


/**
 * <p>
 * Quota rules for all object systems in the kernel.
 * </p>
 *
 * <p>
 * The kernel dictates which types of credentials (such as
 * users, groups, kernel modules) can have quota limits
 * applied to their object system usage with its
 * <code> categories () </code>.
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
public interface KernelQuotaRules
{
    /**
     * <p>
     * Returns the categories of credentials which can have
     * quota limits applied in this kernel, such as users,
     * groups and kernel modules.
     * </p>
     *
     * @return This kernel's quota categories.  Never null.
     */
    public abstract QuotaCategory [] categories ();
}
