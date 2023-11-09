package musaico.build;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;


/**
 * <p>
 * Given an input object and an output srteam, serializes the object
 * and writes the serial bytes to the specified output stream.
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
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.build.MODULE#COPYRIGHT
 * @see musaico.build.MODULE#LICENSE
 */
public class Serializer
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Serializes the specified object to the specified output stream.
     * </p>
     *
     * @param serializable The serializable object to write out to the stream.
     *                     Must not be null.
     *
     * @param out The output stream to write to.  Must not be null.
     */
    public void serialize (
                           Serializable serializable,
                           OutputStream out
                           )
        throws IOException
    {
        ObjectOutputStream obj = new ObjectOutputStream ( out );
        obj.writeObject ( serializable );
    }
}
