package musaico.build.classweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * <p>
 * Given a lookup of DiaChunks, repositions everything to make a nice,
 * pretty diagram.
 * </p>
 *
 * <p>
 * Repositioning should occur AFTER connecting classes for
 * generalizations, associations, and so on.
 * </p>
 */
public interface DiaRepositioner
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The default repositioner. */
    public static final DiaRepositioner STANDARD =
        new DiaStandardRepositioner ();


    /**
     * <p>
     * Repositions the objects in the specified lookup (each object
     * represented by a List of DiaChunks which are to be repositioned).
     * </p>
     *
     * @param dia_chunks The lookup of DiaChunks by object, which
     *                   must be repositioned to make a pretty diagram.
     *                   Must not be null.
     *                   Must not contain any null elements.
     */
    public abstract void reposition (
                                     LinkedHashMap<Object, List<DiaChunk>> dia_chunks
                                     );
}
