package musaico.foundation.wiring.components;

import java.io.Serializable;

import java.lang.reflect.Array;


import musaico.foundation.container.ImmutableContainer;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.structure.StringRepresentation;

import musaico.foundation.wiring.Carrier;
import musaico.foundation.wiring.Tags;

public class StandardCarrier
    extends ImmutableContainer<Object>
    implements Carrier, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    // Immutable (but mutable content):
    private final Tags tags = new Tags ();


    @SuppressWarnings({"unchecked", "varargs"}) // Cast DATA [] to Object [],
                                                // heap pollution generic varargs.
    @SafeVarargs // Possible heap pollution DATA ...
    public <DATA extends Object> StandardCarrier (
            Class<DATA> data_type,
            DATA ... data
            )
    {
        super ( (Class<Object>) ( (Object) data_type ), // element_type
                (Object []) ( (Object) data ) );        // elements
    }

    /**
     * @see musaico.foundation.wiring.Carrier#tags()
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
        sbuf.append ( "carrier [" );

        boolean is_first = true;
        for ( Object datum : this.elements () )
        {
            if ( is_first )
            {
                is_first = false;
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

        if ( is_first )
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
