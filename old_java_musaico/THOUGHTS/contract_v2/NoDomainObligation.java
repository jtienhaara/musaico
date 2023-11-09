package musaico.foundation.contract;

import java.io.Serializable;


/**
 * <p>
 * No domain obligation at all.  Any value is fine.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract and every ContractViolation must be
 * Serializable in order to play nicely over RMI.
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
 * Copyright (c) 2012, 2013 Johann Tienhaara
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
public class NoDomainObligation<INSPECTABLE extends Object>
    extends DomainObligation<INSPECTABLE, DomainObligationCheckedViolation>
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130902L;
    private static final String serialVersionHash =
        "0xB7EBCB3CD48004D9A7BA8ADE6A1B1CD19BAAD34E";


    /**
     * <p>
     * Creates a new NoDomainObligation for the specified domain.
     * </p>
     *
     * @param domain The domain to which all inspectable data must belong.
     *               For example, Domain.NONE.  Must not be null.
     */
    public NoDomainObligation (
                               Domain<INSPECTABLE> domain
                               )
    {
        super ( domain );
    }


    /**
     * @see musaico.foundation.contract.DomainObligation#createViolation(java.io.Serializable, java.io.Serializable)
     */
    @Override
    public final DomainObligationCheckedViolation createViolation (
                                                                   Serializable object_under_contract,
                                                                   Serializable inspectable_data
                                                                   )
    {
        return new DomainObligationCheckedViolation ( this,
                                                      object_under_contract,
                                                      inspectable_data );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "NoDomainObligation";
    }
}
