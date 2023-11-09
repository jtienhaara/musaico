package musaico.build;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import java.security.MessageDigest;


/**
 * <p>
 * Serializes an object then hashes the serial bytes using SHA-1.
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
public class SerialHasher
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Serializes the specified object then generates a SHA-1
     * hash of the bytes.
     * </p>
     *
     * @param serializable The object to serialize then hash.
     *                     Must not be null.
     *
     * @return The SHA-1 hashed serialized form of the specified object.
     *         Never null.
     *
     * @throws IOException If the serializing or hashing fails.
     */
    public byte [] hash (
                         Serializable serializable
                         )
        throws IOException
    {
        try
        {
            ByteArrayOutputStream bytes_out =
                new ByteArrayOutputStream ();
            new Serializer ().serialize ( serializable, bytes_out );

            byte [] serialized_bytes =
                bytes_out.toByteArray ();

            MessageDigest md = MessageDigest.getInstance("SHA");

            byte [] hash = md.digest ( serialized_bytes );

            return hash;
        }
        catch ( IOException e )
        {
            throw e;
        }
        catch ( Exception e )
        {
            throw new IOException ( e );
        }
    }
}
