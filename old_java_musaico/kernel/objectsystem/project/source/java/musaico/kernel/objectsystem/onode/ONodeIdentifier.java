package musaico.kernel.objectsystem.onode;

import java.io.Serializable;

import java.lang.reflect.Field;


import musaico.i18n.exceptions.I18nIllegalArgumentException;

import musaico.io.Identifier;
import musaico.io.Reference;
import musaico.io.SimpleTypedIdentifier;

import musaico.io.references.SimpleSoftReference;

import musaico.kernel.KernelNamespaces;


/**
 * <p>
 * Uniquely identifies an ONode within a particular SuperBlock's
 * namespace.
 * </p>
 *
 *
 * <p>
 * In Java, every Identifier must be Serializable in order
 * to play nicely across RMI.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public class ONodeIdentifier
    extends SimpleTypedIdentifier<ONode>
    implements Serializable
{
    /** An ONodeIdentifier pointing to no ONode.
     *  Useful for stepping through an index of ONodeIdentifiers. */
    public static final ONodeIdentifier NONE =
        new ONodeIdentifier ( ONodeIdentifier.lookupNoSuperBlockIdentifier (),
                              new SimpleSoftReference<String> ( "no_onode" ) );


    /**
     * <p>
     * Creates a new ONodeIdentifier with the specified
     * ONode number.
     * </p>
     *
     * @param super_block_ref The SuperBlockIdentifier of the object system
     *                        to which the ONode belongs.
     *                        Must be a SuperBlockIdentifier.
     *                        Must not be null.
     *
     * @param onode_num The name of the ONode.  Must be 0 or greater.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    public ONodeIdentifier (
                            Identifier super_block_ref,
                            long onode_num
                            )
        throws I18nIllegalArgumentException
    {
        this ( super_block_ref,
               new SimpleSoftReference<Long> ( onode_num ) );
    }


    /**
     * <p>
     * Creates a new ONodeIdentifier with the specified name.
     * </p>
     *
     * <p>
     * This method is provided for sub-classing ONodeIdentifier
     * with a different (non-long) type of name.  Use it wisely.
     * </p>
     *
     * @param super_block_ref The identifier of the object system
     *                        to which the ONode belongs.
     *                        Must not be null.
     *
     * @param onode_name The unique name of the ONode.
     *                   Must not be null.
     *
     * @throws I18nIllegalArgumentException If the specified parameters
     *                                      are invalid (see above).
     */
    protected ONodeIdentifier (
                               Identifier super_block_ref,
                               Reference onode_name
                               )
    {
        super ( super_block_ref, onode_name, ONode.class );

        if ( ! super_block_ref.getClass ().getSimpleName ()
             .equals ( "SuperBlockIdentifier" ) )
        {
            throw new I18nIllegalArgumentException ( "Cannot create ONodeIdentifier [%onode_name%] under identifier [%super_block_ref%]",
                                                     "onode_name", onode_name,
                                                     "super_block_ref", super_block_ref );
        }
    }




    /**
     * <p>
     * Searches for the "no super block identifier" by reflection.
     * </p>
     */
    private static Identifier lookupNoSuperBlockIdentifier ()
    {
        try
        {
            // Allow exceptions to be thrown up the stack if
            // the system is not properly linked at runtime.
            Class<?> super_block_identifier_class =
                Class.forName ( "musaico.kernel.objectsystem.superblock.SuperBlockIdentifier" );
            Field none_field = super_block_identifier_class.getField ( "NONE" );
            Identifier no_super_block_identifier = (Identifier)
                none_field.get ( null );

            return no_super_block_identifier;
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException ( e.getMessage (), e );
        }
        catch ( NoSuchFieldException e )
        {
            throw new RuntimeException ( e.getMessage (), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException ( e.getMessage (), e );
        }
    }
}
