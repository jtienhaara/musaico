package musaico.platform.hash;

import java.io.Serializable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import musaico.foundation.contract.AbstractContract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Obligation;
import musaico.foundation.contract.ObligationUncheckedViolation;


/**
 * <p>
 * Each Hash class is supported by the underlying Java[tm] libraries.
 * If a specific hashing algorithm is not provided by those libraries,
 * then this contract is violated and the corresponding Hash class
 * cannot be used.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
 * nicely over RMI.
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
public class HashAlgorithmMustBeBuiltIn
    extends AbstractContract<String, HashAlgorithmMustBeBuiltIn.Violation>
    implements Obligation<String, HashAlgorithmMustBeBuiltIn.Violation>, Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20130916;
    private static final String serialVersionHash =
        "0x3F52066F07DC158128E64D367C1880EAC1B0AD91";


    /** Checks static and constructor contracts for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( HashAlgorithmMustBeBuiltIn.class );


    /** The contract requiring hash algorithms to be provided
     *  by the base Java[tm] libraries. */
    public static final HashAlgorithmMustBeBuiltIn CONTRACT =
        new HashAlgorithmMustBeBuiltIn ();


    /**
     * @see musaico.foundation.contract.Contract#enforce(java.lang.Object, java.lang.Object)
     */
    @Override
    public void enforce (
                         Object object_under_contract,
                         String hash_algorithm_name
                         )
        throws HashAlgorithmMustBeBuiltIn.Violation
    {
        try
        {
            MessageDigest hash_algorithm =
                MessageDigest.getInstance ( hash_algorithm_name );
        }
        catch ( NoSuchAlgorithmException e )
        {
            final HashAlgorithmMustBeBuiltIn.Violation violation =
                new HashAlgorithmMustBeBuiltIn.Violation ( this,
                                                           object_under_contract,
                                                           hash_algorithm_name );
            violation.initCause ( e );
            throw violation;
        }
    }


    /**
     * <p>
     * A violation of the HashAlgorithmMustBeBuiltIn contract.
     * </p>
     */
    public static class Violation
        extends ObligationUncheckedViolation
        implements Serializable
    {
        private static final long serialVersionUID =
            HashAlgorithmMustBeBuiltIn.serialVersionUID;

        /**
         * <p>
         * Creates a HashAlgorithmMustBeBuiltIn.Violation.
         * </p>
         *
         * @param hash_algorithm_name The name of the hash algorithm
         *                            which does not exist
         *                            in Java's MessageDigest lookup.
         *                            Must not be null.
         */
        public Violation (
                          HashAlgorithmMustBeBuiltIn obligation,
                          Object object_under_contract,
                          String hash_algorithm_name
                          )
        {
            super ( obligation,
                    Contracts.makeSerializable ( object_under_contract ),
                    Contracts.makeSerializable ( hash_algorithm_name ) );
        }
    }
}
