package musaico.foundation.wiring.components;

import java.io.Serializable;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.wiring.Board;
import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Carriers;
import musaico.foundation.wiring.Conductor;
import musaico.foundation.wiring.NoCarrier;
import musaico.foundation.wiring.Pulse;
import musaico.foundation.wiring.Tags;
import musaico.foundation.wiring.Wire;


public class Constant
    implements Conductor, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Immutable:
    private final String id;
    private final Carrier [] carriers;
    private final Tags tags = new Tags ();
    private final int hashCode;

    @SuppressWarnings("varargs") // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution DATA ...
    public <DATA extends Object> Constant (
            String id,
            Class<DATA> data_type,
            DATA ... data
            )
    {
        this ( id,
               ( data_type == null || data == null )
                   ? null
                   : new StandardCarrier ( data_type,
                                           data ) );
    }

    public Constant (
            String id,
            Carrier ... carriers
            )
    {
        int hash_code = 0;
        if ( id == null )
        {
            this.id = "no_id";
        }
        else
        {
            this.id = id;
            hash_code += this.id.hashCode ();
        }

        if ( carriers == null )
        {
            this.carriers = Carrier.NO_CARRIERS;
        }
        else
        {
            this.carriers = new Carrier [ carriers.length ];
            System.arraycopy ( carriers, 0,
                               this.carriers, 0, carriers.length );
            for ( int c = 0; c < this.carriers.length; c ++ )
            {
                if ( this.carriers [ c ] == null )
                {
                    this.carriers [ c ] = Carrier.NONE;
                }
                else
                {
                    hash_code += this.carriers [ c ].hashCode ();
                }
            }
        }

        this.hashCode = hash_code;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#id()
     */
    @Override
    public final String id ()
        throws Return.NeverNull.Violation
    {
        return this.id;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#pull(musaico.foundation.wiring.Pulse)
     */
    @Override
    public final Carrier [] pull ( Pulse context )
    {
        if ( context == null )
        {
            return new Carrier [ 0 ];
        }

        final Carrier [] pulled = new Carrier [ this.carriers.length ];
        System.arraycopy ( this.carriers, 0,
                           pulled, 0, this.carriers.length );

        return pulled;
    }

    /**
     * @see musaico.foundation.wiring.Conductor#push(musaico.foundation.wiring.Pulse, musaico.foundation.Carrier[])
     */
    @Override
    public final void push ( Pulse context, Carrier ... data )
    {
        // Do nothing.
    }

    /**
     * @see musaico.foundation.wiring.Conductor#tags()
     */
    @Override
    public final Tags tags ()
        throws Return.NeverNull.Violation
    {
        return this.tags;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "constant " );
        sbuf.append ( this.id () );
        sbuf.append ( " [" );

        boolean is_first_carrier = true;
        for ( Carrier carrier : this.carriers )
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
