package musaico.foundation.wiring;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.io.Serializable;


import musaico.foundation.structure.StringRepresentation;


public class Carriers
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Serializable lock = new String ( "lock" );
    private final List<Carrier []> carriers = new ArrayList<Carrier []> ();
    private int numCarriers = 0;
    private int numData = 0;

    public Carriers (
            Carrier ... carriers
            )
    {
        if ( carriers == null )
        {
            this.carriers.add ( new Carrier [ 0 ] );
        }
        else
        {
            for ( int c = 0; c < carriers.length; c ++ )
            {
                if ( carriers [ c ] == null )
                {
                    carriers [ c ] = Carrier.NONE;
                }

                this.numData += carriers [ c ].numElements ();
            }

            this.carriers.add ( carriers );
            this.numCarriers += carriers.length;
        }
    }

    public final Carriers add (
            Carrier ... carriers
            )
    {
        if ( carriers == null
             || carriers.length == 0 )
        {
            return this;
        }

        int num_data = 0;
        for ( int c = 0; c < carriers.length; c ++ )
        {
            if ( carriers [ c ] == null )
            {
                carriers [ c ] = Carrier.NONE;
            }

            num_data += carriers [ c ].numElements ();
        }

        synchronized ( this.lock )
        {
            this.carriers.add ( carriers );
            this.numCarriers += carriers.length;
            this.numData += num_data;
        }

        return this;
    }

    public final Carriers remove (
            Carrier ... carriers
            )
    {
        if ( carriers == null
             || carriers.length == 0 )
        {
            return this;
        }

        final Set<Carrier> carriers_to_remove =
            new HashSet<Carrier> ();
        for ( int c = 0; c < carriers.length; c ++ )
        {
            if ( carriers [ c ] == null )
            {
                carriers [ c ] = Carrier.NONE;
            }

            carriers_to_remove.add ( carriers [ c ] );
        }

        synchronized ( this.lock )
        {
            int num_carriers_removed = 0;
            int num_data_removed = 0;
            for ( int cc = 0; cc < this.carriers.size (); cc ++ )
            {
                final Carrier [] chunk = this.carriers.get ( cc );
                final List<Integer> indices_to_remove =
                    new ArrayList<Integer> ();
                for ( int c = 0; c < chunk.length; c ++ )
                {
                    final Carrier carrier = chunk [ c ];
                    if ( carriers_to_remove.contains ( carrier ) )
                    {
                        indices_to_remove.add ( c );
                        num_carriers_removed ++;
                        num_data_removed += carrier.numElements ();
                    }
                }

                final Carrier [] replacement_chunk;
                if ( indices_to_remove.size () == 0 )
                {
                    continue;
                }
                else if ( indices_to_remove.size () >= chunk.length )
                {
                    replacement_chunk = new Carrier [ 0 ];
                }
                else
                {
                    replacement_chunk =
                        new Carrier [ chunk.length - indices_to_remove.size () ];
                    int copy_from_index = 0;
                    int offset = 0;
                    for ( int itr = 0; itr <= indices_to_remove.size (); itr ++ )
                    {
                        final int copy_to_index;
                        if ( itr < indices_to_remove.size () )
                        {
                            copy_to_index = indices_to_remove.get ( itr ) - 1;
                        }
                        else
                        {
                            copy_to_index = chunk.length - 1;
                        }

                        if ( copy_to_index <= copy_from_index )
                        {
                            copy_from_index = copy_to_index + 1;
                            continue;
                        }

                        System.arraycopy ( chunk, copy_from_index,
                                           replacement_chunk, offset, copy_to_index - copy_from_index + 1 );

                        offset += copy_to_index - copy_from_index + 1;

                        copy_from_index = copy_to_index + 1;
                    }
                }

                this.carriers.set ( cc, replacement_chunk );
            }
        }

        return this;
    }

    public final Carrier [] toArray ()
    {
        final Carrier [] carriers;
        synchronized ( this.lock )
        {
            if ( this.carriers.size () == 0 )
            {
                carriers = new Carrier [ 0 ];
            }
            else if ( this.carriers.size () == 1 )
            {
                carriers = this.carriers.get ( 0 );
            }
            else
            {
                carriers = new Carrier [ this.numCarriers ];
                int offset = 0;
                for ( Carrier [] one_to_add : this.carriers )
                {
                    System.arraycopy ( one_to_add, 0,
                                       carriers, offset, one_to_add.length );
                    offset += one_to_add.length;
                }
            }
        }

        for ( int c = 0; c < carriers.length; c ++ )
        {
            if ( carriers [ c ] == null )
            {
                carriers [ c ] = Carrier.NONE;
            }
        }

        return carriers;
    }

    public final Object [] toData ()
    {
        final Object [] data;
        synchronized ( this.lock )
        {
            data = new Object [ this.numData ];
            int offset = 0;
            for ( Carrier [] one_carriers_chunk : this.carriers )
            {
                for ( Carrier one_carrier : one_carriers_chunk )
                {
                    final Object [] one_data = one_carrier.elements ();
                    if ( one_data == null )
                    {
                        continue;
                    }

                    System.arraycopy ( one_data, 0,
                                       data, offset, one_data.length );
                    offset += one_data.length;
                }
            }
        }

        // Data can include nulls.
        return data;
    }

    @SuppressWarnings("unchecked") // Cast Array.newInstance () : Object -> DATA []
    public final <DATA extends Object> DATA [] toData (
            Class<DATA> data_type,
            DATA [] default_value_per_carrier
            )
    {
        if ( data_type == null
             || default_value_per_carrier == null )
        {
            return default_value_per_carrier;
        }

        final DATA [] data;
        synchronized ( this.lock )
        {
            // SuppressWarnings("unchecked") Cast Array.newInstance () : Object -> DATA []
            data = (DATA [])
                Array.newInstance ( data_type, this.numData );
            int offset = 0;
            for ( Carrier [] one_carriers_chunk : this.carriers )
            {
                for ( Carrier one_carrier : one_carriers_chunk )
                {
                    final DATA [] one_data =
                        one_carrier.elements ( data_type, default_value_per_carrier );
                    if ( one_data == null )
                    {
                        continue;
                    }

                    System.arraycopy ( one_data, 0,
                                       data, offset, one_data.length );
                    offset += one_data.length;
                }
            }
        }

        // Data can include nulls.
        return data;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "carriers [" );

        final Carrier [] carriers = this.toArray ();
        boolean is_first_carrier = true;
        for ( Carrier carrier : carriers )
        {
            if ( is_first_carrier )
            {
                is_first_carrier = false;
                sbuf.append ( " [" );
            }
            else
            {
                sbuf.append ( ", [" );
            }

            boolean is_first_datum = true;
            for ( Object datum : carrier.elements () )
            {
                if ( is_first_datum )
                {
                    is_first_datum = false;
                    sbuf.append ( " " );
                }
                else
                {
                    sbuf.append ( ", " );
                }

                final String datum_string =
                    StringRepresentation.of ( datum,
                                              StringRepresentation.DEFAULT_OBJECT_LENGTH );
                sbuf.append ( datum_string );
            }

            if ( is_first_carrier )
            {
                sbuf.append ( "]" );
            }
            else
            {
                sbuf.append ( " ]" );
            }
        }

        if ( is_first_carrier )
        {
            sbuf.append ( "]" );
        }
        else
        {
            sbuf.append ( " ]" );
        }

        final String as_string = sbuf.toString ();
        return as_string;
    }
}
