package musaico.foundation.contract;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * The contract manager for a specific object.
 * </p>
 *
 * <p>
 * Typically a class will create its own static private final ObjectContracts
 * for enforcing contracts on constructors and static methods, and
 * an object will create its own private final ObjectContracts for enforcing
 * contracts on methods.  Wherever needed, the <code> check () </code>
 * method of the appropriate ObjectContracts is invoked to enforce
 * the appropriate contracts.
 * </p>
 *
 * <p>
 * In Java, the behaviour of ObjectContracts can be controlled by setting
 * system properties.  This control is meant to be similar to the controls
 * used by logging libraries.
 * </p>
 *
 * <p>
 * The properties which can be defined to control the behaviour of the
 * ObjectContracts are as follows:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b> <code> musaico.contracts </code> </b> :
 *       This property sets the <code> Arbiter </code>
 *       to be used by default by all ObjectContracts instances.
 *   </li>
 *   <li>
 *     <b> <code> musaico.contracts:fully.qualified.package </code> <b> :
 *       This property sets the <code> Arbiter </code>
 *       to be used by default by all ObjectContracts instances for
 *       classes of a specific package and its sub-packages as well
 *       as object instances of those classes.  Note that this
 *       setting does NOT affect classes or objects which derive
 *       from classes of the specified package.
 *   </li>
 *   <li>
 *     <b> <code> musaico.contracts:fully.qualified.class </code> </b> :
 *       This property sets the <code> Arbiter </code>
 *       to be used for the ObjectContracts instance for the specified
 *       class, as well as the <code> Arbiter <code> to be used
 *       by default for instances of that class.  Note that this
 *       setting does NOT affect classes or objects which derive
 *       from the specified class.
 *   </li>
 *   <li>
 *     <b> <code> musaico.contracts:fully.qualified.class:toString </code> </b> :
 *       For those who want stupidly specific control, this
 *       property sets the <code> Arbiter </code>
 *       to be used for the ObjectContracts instance for the
 *       specified instance of the specified class.  When the
 *       ObjectContracts is created, it looks at the fully qualified
 *       class of the object whose contracts it is enforcing, and
 *       takes the <code> toString () </code> of that object, and
 *       looks to see if there is a specific setting for the object.
 *   </li>
 * </ul>
 *
 * <p>
 * So each ObjectContracts looks for the class of Arbiter to use
 * according to the order: object - class - packages ... - overall default.
 * </p>
 *
 * <p>
 * The class of Arbiter determines how/whether Contracts will be
 * inspected and enforced.  For example, normally Contracts are
 * inspected and enforced by throwing exceptions on violation, using
 * the <code> musaico.foundation.contract.StandardArbiter </code>
 * class.  However speed nuts can totally disable contract inspection
 * and enforcement by specifying
 * <code> musaico.foundation.contract.NoArbiter </code>.  Logging nuts
 * and aspect-oriented nuts might want to capture logging information
 * every time a Contract is broken, before carrying on with the
 * standard enforcement behaviour.  And so on.
 * </p>
 *
 * <p>
 * As a security precaution, the Arbiter settings are read in only
 * once per process, to prevent malicious or stupid code from overriding
 * settings in the middle of process execution.  The first time any
 * ObjectContracts is created, it will load in the settings.  After that
 * no changes will ever affect its behaviour.
 * </p>
 *
 * @see musaico.foundation.contract.Arbiter
 *
 *
 * <p>
 * In Java every ObjectContracts is Serializable in order to play
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
 * Copyright (c) 2013-2014 Johann Tienhaara
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
public class ObjectContracts
    implements Serializable
{
    /** Run "make serializable" to test and/or generate a new hash. */
    private static final long serialVersionUID = 20140413L;
    private static final String serialVersionHash =
        "0xE7B86ED3FA1CA598CE5038163089E452D9CE17B8";


    /** The lookup of Arbiters to use for different patrons.
     *  Indexed as described in the header of this class.
     *  Created once and then never modified. */
    private static final Map<String, Arbiter> arbiters = createArbiters ();


    /** A serializable representation of the parent object, on whose
     *  behalf which we manage contracts. */
    private final Serializable patron;


    /**
     * <p>
     * Creates a new ObjectContracts for the specified patron, using
     * the Arbiter specified in the system properties.
     * </p>
     *
     * <p>
     * Be careful to only call <code> new ObjectContracts ( patron ) </code>
     * on a patron which is initialized to the point that its
     * <code> toString () </code> method will not throw
     * NullPointerExceptions and so on.  Usually it is best to create
     * the ObjectContracts for a patron as the very last step of
     * the patron's constructor.
     * </p>
     *
     * @param patron The object on whose behalf we will manage contracts.
     *               A Serializable reference to the specified patron
     *               will be stored by calling patron.toString ().
     *               No reference to the actual patron will be kept.
     *               Must not be null.
     */
    public ObjectContracts (
                            Object patron
                            )
    {
        this ( patron, arbiter ( ObjectContracts.arbiters, patron ) );
    }


    /**
     * <p>
     * Creates a new ObjectContracts for the specified patron and with
     * a specific Arbiter, overriding the system properties settings.
     * </p>
     *
     * <p>
     * This constructor can be used, for example, when certain contract
     * enforcement should always be handled the same way, and not
     * be configurable.
     * </p>
     *
     * <p>
     * Be careful to only call <code> new ObjectContracts ( patron ) </code>
     * on a patron which is initialized to the point that its
     * <code> toString () </code> method will not throw
     * NullPointerExceptions and so on.  Usually it is best to create
     * the ObjectContracts for a patron as the very last step of
     * the patron's constructor.
     * </p>
     *
     * @param patron The object on whose behalf we will manage contracts.
     *               A Serializable reference to the specified patron
     *               will be stored by calling patron.toString ().
     *               No reference to the actual patron will be kept.
     *               Must not be null.
     */
    public ObjectContracts (
                            Object patron,
                            Arbiter arbiter
                            )
    {
        this.patron = createSerializable ( patron );
        this.arbiter = arbiter;
    }


    /**
     * <p>
     * Looks up the specified patron in the specified arbiters map,
     * and returns the appropriate Arbiter to use for that patron.
     * </p>
     *
     * <p>
     * If there is no default Arbiter defined in the specified map,
     * then a new StandardArbiter will be created each time this
     * method is invoked.
     * </p>
     *
     * @param arbiters The lookup of Arbiter by default/package/class/object.
     *                 Must not be null.  Must not contain any
     *                 null keys or values.
     *
     * @param patron The object under contract.  Must not be null.
     *
     * @return The Arbiter for the specified patron.  Never null.
     */
    public static final Arbiter arbiter (
                                         Map<String, Arbiter> arbiters,
                                         Object patron
                                         )
    {
        if ( arbiters == null
             || patron == null )
        {
            return new StandardArbiter ();
        }

        String patron_id = patron.toString ();
        final Class<?> patron_class = patron.getClass ();
        final String class_id = patron_class.getName ();
        final String prefix = "musaico.contracts";

        String key = prefix + ":" + class_id + ":" + patron_id;
        Arbiter arbiter = arbiters.get ( key );
        if ( arbiter != null )
        {
            // Object-specific Arbiter.
            return arbiter;
        }

        key = prefix + ":" + class_id;
        arbiter = arbiters.get ( key );
        if ( arbiter != null )
        {
            // Class-specific Arbiter.
            return arbiter;
        }

        int index = class_id.length () + 1;
        while ( ( index = class_id.lastIndexOf ( '.', index - 1 ) ) > 0 )
        {
            final String package_id = class_id.substring ( 0, index );
            key = prefix + ":" + package_id;
            arbiter = arbiters.get ( key );
            if ( arbiter != null )
            {
                // Package-specific Arbiter.
                return arbiter;
            }
        }

        key = prefix;
        arbiter = arbiters.get ( key );
        if ( arbiter != null )
        {
            // Default Arbiter.
            return arbiter;
        }

        // No default Arbiter defined. Create a new standard one
        // each time someone calls us and we end up here.
        return new StandardArbiter ();
    }


    /**
     * <p>
     * Gives the specified contract to the current arbiter to inspect
     * and enforce (though nothing will happen if the arbiter does
     * not do inspections).
     * </p>
     *
     * <p>
     * If the contract has been breached and the current arbiter enforces
     * contracts by throwing exceptions, then a ContractViolation will
     * be thrown.
     * </p>
     *
     * @param contract The contract to inspect.  Must not be null.
     *
     * @param object_under_contract The under contract, such
     *                              as an object whose method enforces
     *                              this contract.  Must not be null.
     *
     * @param inspectable_data The inspectable data, to be checked by
     *                         this contract.  If the inspectable[s]
     *                         do not match the contract requirements,
     *                         then a violation will be thrown.
     *                         If more than one inspectable data is passed
     *                         via varargs, then an Object array is created
     *                         for the contracts to check.
     *                         Must not be null.
     *                         Must not contain any null elements.
     *
     * @throws ContractViolation If the arbiter inspected the contract,
     *                           determined that it was breached,
     *                           and enforced the contract by throwing
     *                           a violation.
     */
    public final
        <INSPECTABLE extends Object, VIOLATION extends Throwable>
        void check (
                    Contract<INSPECTABLE, VIOLATION> contract,
                    INSPECTABLE inspectable_data
                    )
        throws VIOLATION
    {
        // Potentially throws ContractViolations up the stack:
        this.arbiter.inspect ( contract,
                               this.patron,
                               inspectable_data );
    }


    /**
     * <p>
     * Variant on check () which allows for variable # of arguments
     * to be passed in, for checking arrays (such as checking that
     * all parameters are not null).
     * </p>
     *
     * @see musaico.foundation.contract.ObjectContracts#check(musaico.foundation.contract.Contract, java.lang.Object)
     */
    public final
        <VIOLATION extends Throwable>
        void check (
                    Contract<Object [], VIOLATION> contract,
                    Object ... inspectable_array
                    )
        throws VIOLATION
    {
        // Potentially throws ContractViolations up the stack:
        this.arbiter.inspect ( contract,
                               this.patron,
                               inspectable_array );
    }


    /**
     * <p>
     * Creates a lookup of Arbiters by key, as described in the
     * header documentation for ObjectContracts.
     * </p>
     *
     * <p>
     * The ObjectContracts class itself only ever calls this method
     * once, at class load time.  Anyone else wanting to
     * reuse the loading logic is welcome to call it any time.
     * It will not, however, affect the settings used by ObjectContracts,
     * which are read-once, never modify.
     * </p>
     *
     * @return The newly created lookup of Arbiters by default/package/
     *         class/object.  Never null.  Never contains any null
     *         keys or values.
     */
    public static Map<String, Arbiter> createArbiters ()
        throws RuntimeException
    {
        final Map<String, Arbiter> arbiters =
            new HashMap<String, Arbiter> ();

        final Properties properties = System.getProperties ();
        for ( Object property_name : properties.keySet () )
        {
            if ( ! ( property_name instanceof String ) )
            {
                continue;
            }

            final String key = (String) property_name;
            final Object value = properties.getProperty ( key );

            final Arbiter arbiter;
            if ( value instanceof String )
            {
                final String class_name = (String) value;

                // Throws a RuntimeException if the class is not even loaded
                // or if it is not a class of Arbiter.
                try
                {
                    final Class<?> arbiter_class =
                        Class.forName ( class_name );
                    arbiter = (Arbiter) arbiter_class.newInstance ();
                }
                catch ( Exception e )
                {
                    throw new RuntimeException ( "Failed to load Arbiter String '"
                                                 + class_name
                                                 + "' for "
                                                 + key,
                                                 e );
                }
            }
            else if ( value instanceof Class )
            {
                Class<?> arbiter_class = (Class<?>) value;

                // Throws a RuntimeException if the class cannot be
                // instantiated, or is not a class of Arbiter, and so on.
                try
                {
                    arbiter = (Arbiter) arbiter_class.newInstance ();
                }
                catch ( Exception e )
                {
                    throw new RuntimeException ( "Failed to load Arbiter Class "
                                                 + arbiter_class
                                                 + " for "
                                                 + key,
                                                 e );
                }
            }
            else if ( value instanceof Arbiter )
            {
                arbiter = (Arbiter) value;
            }
            else
            {
                throw new RuntimeException ( "Failed to load Arbiter property '"
                                             + value
                                             + "' for "
                                             + key );
            }
        }

        final String prefix = "musaico.contracts";
        if ( ! arbiters.containsKey ( prefix ) )
        {
            // No default has been defined.  Use StandardArbiter as
            // the default.
            arbiters.put ( prefix, new StandardArbiter () );
        }
    }


    /**
     * <p>
     * Creates a Serializable representation of the specified patron.
     * By default <code> patron.toString () </code> is returned.
     * </p>
     *
     * @param patron The object under contract.  Must not be null.
     *
     * @return The Serializable representation of the patron.
     *         Never null.
     */
    protected Serializable createSerializable (
                                               Object patron
                                               )
    {
        return "" + patron;
    }


    /**
     * @see java.lang.Objects#toString()
     */
    @Override
    public String toString ()
    {
        return "ObjectContracts ( " + this.patron + " )"
            + " [ " + this.arbiter + " ]";
    }
}
