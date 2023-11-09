package musaico.foundation.buffer.types;


import java.io.Serializable;


import musaico.foundation.buffer.Buffer;

import musaico.field.Field;

import musaico.types.TypeCastException;
import musaico.types.TypeCaster;
import musaico.types.TypeException;


/**
 * <p>
 * Casts Buffers Strings by casting each constituent Field to
 * a String then appending them all.
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
public class CastBufferToString
    extends AbstractBufferCaster<String,StringBuilder>
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
    protected StringBuilder add (
                                 String element,
                                 StringBuilder cast_under_construction
                                 )
    {
        if ( cast_under_construction == null )
        {
            cast_under_construction = new StringBuilder ();
        }

        cast_under_construction.append ( element );

        return cast_under_construction;
    }


    /**
     * @see musaico.foundation.buffer.types.AbstractBufferCaster#build(java.lang.Object)
     */
    @Override
    protected String build (
                            StringBuilder cast_under_construction
                            )
    {
        return cast_under_construction.toString ();
    }
}
