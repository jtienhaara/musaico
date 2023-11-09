package musaico.foundation.wiring;

import java.io.Serializable;

import java.lang.reflect.Array;

import musaico.foundation.structure.StringRepresentation;

public class SimpleCarrier
    extends AbstractTagged<Carrier>
    implements Carrier, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Class<?> dataClass;
    private final Object [] data;

    @SuppressWarnings("unchecked") // Cast DATA [] to Object []
    public <DATA extends Object> SimpleCarrier (
            Class<DATA> data_class,
            DATA [] data,
            Tag ... tags
            )
    {
        super ( tags );

        if ( data_class == null )
        {
            this.dataClass = Object.class;
        }
        else
        {
            this.dataClass = data_class;
        }

        if ( data == null )
        {
            this.data = (Object [])
                Array.newInstance ( this.dataClass, 0 );
        }
        else
        {
            final DATA [] defensive_duplicate = (DATA [])
                Array.newInstance ( this.dataClass,
                                    data.length );
            System.arraycopy ( data, 0,
                               defensive_duplicate, 0, data.length );
            this.data = (Object []) defensive_duplicate;
        }
    }

    @SuppressWarnings("unchecked") // Cast Object to DATA
    public final <DATA extends Object> DATA [] data (
            Class<DATA> data_class,
            DATA [] default_value
            )
    {
        if ( data_class == null
             || ! data_class.isAssignableFrom ( this.dataClass ) )
        {
            return default_value;
        }

        if ( this.data.length == 0 )
        {
            // No point duplicating a 0-length array.
            // SuppressWarnings("unchecked") // Cast Object to DATA
            return (DATA []) this.data;
        }

        final DATA [] defensive_duplicate = (DATA [])
            Array.newInstance ( data_class, this.data.length );

        System.arraycopy ( this.data, 0,
                           defensive_duplicate, 0, this.data.length );

        return defensive_duplicate;
    }
}
