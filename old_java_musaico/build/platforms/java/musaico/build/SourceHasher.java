package musaico.build;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.security.MessageDigest;


/**
 * <p>
 * Reads a source code file then hashes the code using SHA-1.
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
public class SourceHasher
{
    /** The MusaicoModule to which this class belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Reads the specified source file then generates a SHA-1
     * hash of the bytes.
     * </p>
     *
     * @param source_filename The source filename to read in and hash.
     *                        Must not be null.
     *
     * @return The SHA-1 hashed serialized form of the specified source file.
     *         Never null.
     *
     * @throws IOException If reading the source file or hashing fails.
     */
    public byte [] hash (
                         String source_filename
                         )
        throws IOException
    {
        try
        {
            ByteArrayOutputStream source_bytes_out =
                new ByteArrayOutputStream ();
            final FileInputStream source_in =
                new FileInputStream ( source_filename );
            final byte [] buf = new byte [ 32768 ];
            int num_bytes;
            while ( ( num_bytes = source_in.read ( buf ) ) >= 0 )
            {
                source_bytes_out.write ( buf, 0, num_bytes );
            }

            byte [] source_bytes =
                source_bytes_out.toByteArray ();

            MessageDigest md = MessageDigest.getInstance("SHA");

            byte [] hash = md.digest ( source_bytes );

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
