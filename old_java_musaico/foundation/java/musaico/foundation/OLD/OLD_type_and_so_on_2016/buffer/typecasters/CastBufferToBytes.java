package musaico.foundation.buffer.types;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.buffer.Buffer;

import musaico.field.Field;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Buffers byte arrays by casting each constituent Field to
 * a byte array then merging them all.
 * </p>
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.buffer.types.MODULE#COPYRIGHT
 * @see musaico.foundation.buffer.types.MODULE#LICENSE
 */
public class CastBufferToBytes
    extends AbstractBufferCaster<byte[],List<byte[]>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see musaico.foundation.buffer.types.AbstractBufferCaster#add(java.lang.Object,java.lang.Object)
     */
    @Override
    protected List<byte[]> add (
                                byte [] element,
                                List<byte[]> cast_under_construction
                                )
    {
        if ( cast_under_construction == null )
        {
            cast_under_construction = new ArrayList<byte[]> ();
        }

        cast_under_construction.add ( element );

        return cast_under_construction;
    }


    /**
     * @see musaico.foundation.buffer.types.AbstractBufferCaster#build(java.lang.Object)
     */
    @Override
    protected byte [] build (
                             List<byte[]> cast_under_construction
                             )
    {
        int total_length = 0;
        for ( byte [] chunk : cast_under_construction )
        {
            total_length += chunk.length;
        }

        byte [] result = new byte [ total_length ];
        int b = 0;
        for ( byte [] chunk : cast_under_construction )
        {
            System.arraycopy ( chunk, 0, result, b, chunk.length );
        }

        return result;
    }
}
