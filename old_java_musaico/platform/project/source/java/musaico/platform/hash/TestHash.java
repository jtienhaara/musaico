package musaico.platform.hash;

import java.io.InputStream;

import java.lang.reflect.Constructor;

import java.security.Provider;
import java.security.Security;

import java.util.Iterator;
import java.util.Set;


/**
 * <p>
 * Tests a hash class with the set of strings provided on the commandline.
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
 * Copyright (c) 2010, 2013 Johann Tienhaara
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
public class TestHash
{
    @SuppressWarnings({"unchecked", "rawtypes"}) // Stupid Java generics
    public static void main ( String [] args )
        throws Exception
    {
        if ( args.length < 1 )
        {
            System.out.println ( "Usage: java TestHash <algorithm> <string1> ..." );
            System.out.println ( "    where <algorithm> is SHA1, MD5, and so on" );
            System.out.println ( "    and <string1> ... is a set of Strings to hash" );
            System.out.println ( "" );
            System.out.println ( "Or: java TestHash <algorithm> < <filename>" );
            System.out.println ( "" );
            System.out.println ( "N.b. current Java MessageDigest services:" );
            Provider [] providers = Security.getProviders ();
            for ( int p = 0; p < providers.length; p ++ )
            {
                Set<Provider.Service> services = providers [ p ].getServices ();
                Iterator<Provider.Service> it = services.iterator ();
                while ( it.hasNext () )
                {
                    Provider.Service service = it.next ();
                    String service_type = service.getType ();
                    if ( ! "MessageDigest".equals ( service_type ) )
                    {
                        continue;
                    }

                    System.out.println ( "    " + service.getAlgorithm () );
                }
            }
            return;
        }

        String hash_name = args [ 0 ];
        String hash_class_name = "musaico.foundation.hash." + hash_name;
        Class<Hash> hash_class = (Class<Hash>) Class.forName ( hash_class_name );
        Class [] bytes_constructor_params = new Class [] { byte[].class };
        Constructor<Hash> bytes_constructor = hash_class.getConstructor ( bytes_constructor_params );

        for ( int a = 1; a < args.length; a ++ )
        {
            String string_to_hash = args [ a ];
            byte [] bytes_to_hash = string_to_hash.getBytes ( "UTF-8" );
            Hash hash = bytes_constructor.newInstance ( bytes_to_hash );

            System.out.println ( hash_name + " of '" + string_to_hash + "':" );
            System.out.println ( "    " + hash );
            System.out.println ( "" );
        }

        Class [] stream_constructor_params = new Class [] { InputStream.class };
        Constructor<Hash> stream_constructor = hash_class.getConstructor ( stream_constructor_params );

        if ( args.length < 2 )
        {
            // Stdin.
            System.out.println ( "Reading input from standard in." );
            Hash hash = stream_constructor.newInstance ( System.in );

            System.out.println ( hash_name + " of input stream:" );
            System.out.println ( "    " + hash );
            System.out.println ( "" );
        }
    }
}
