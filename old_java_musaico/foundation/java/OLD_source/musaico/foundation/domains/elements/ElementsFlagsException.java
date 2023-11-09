package musaico.foundation.domains.elements;

import java.io.Serializable;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.StringRepresentation;


/**
 * <p>
 * An Exception thrown because Elements cannot be represented
 * as some particular type of container, such as a Collection or an array,
 * because the container is incompatible with a specific set
 * of ElementFlags.
 * </p>
 *
 * <p>
 * For example, an array might be incompatible with
 * Flags that are variable length; or a stack might be incompatible
 * with Flags that allow the individual element values to be mutable.
 * </p>
 *
 *
 * <p>
 * Every ElementsFlagsException must be Serializable in order to play nicely
 * over RMI.
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
 * @see musaico.foundation.domains.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.elements.MODULE#LICENSE
 */
public class ElementsFlagsException
    extends RuntimeException
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // A Serializable representation (such as toString ()) of the
    // of the Elements which are incompatible with specific Flags.
    private final Serializable elements;

    // The incompatible Flags.
    private final ElementFlags flags;


    /**
     * <p>
     * Creates a new ElementsFlagsException.
     * </p>
     *
     * @param elements The Elements which have the wrong Flags.
     *                 Must not be null.
     */
    public ElementsFlagsException (
            Elements<?> elements
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        this ( elements,        // elements
               elements == null // flags
                   ? null
                   : elements.flags () );
    }


    /**
     * <p>
     * Creates a new ElementsFlagsException.
     * </p>
     *
     * @param elements The Elements which have the wrong Flags.
     *                 Must not be null.
     *
     * @param flags The ElementFlags with which the specified
     *              Elements representation are incompatible.
     *              Must not be null.
     *
     * @throws NullPointerException If the specified elements representation
     *                              is null, or if the specified
     *                              ElementFlags are null.
     */
    public ElementsFlagsException (
            Elements<?> elements,
            ElementFlags flags
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        super ( "Invalid flags for "
                + ( elements == null
                        ? ""
                        : ClassName.of ( elements.getClass () ) )
                + " "
                + StringRepresentation.of ( elements, // Serializable
                                            StringRepresentation.DEFAULT_ARRAY_LENGTH )
                + " : " + flags );

        if ( elements == null
             || flags == null )
        {
            throw new NullPointerException ( "ERROR ElementsFlagsException cannot be constructed with elements = "
                                             + elements
                                             + ", flags = "
                                             + flags );
        }

        this.elements =
            StringRepresentation.of ( elements, // Serializable
                                      StringRepresentation.DEFAULT_ARRAY_LENGTH );
        this.flags = flags;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final ElementsFlagsException that =
            (ElementsFlagsException) object;

        if ( this.elements == null )
        {
            if ( that.elements != null )
            {
                return false;
            }
        }
        else if ( that.elements == null )
        {
            return false;
        }
        else if ( ! this.elements.equals ( that.elements ) )
        {
            return false;
        }

        if ( this.flags == null )
        {
            if ( that.flags != null )
            {
                return false;
            }
        }
        else if ( that.flags == null )
        {
            return false;
        }
        else if ( ! this.flags.equals ( that.flags ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.elements.hashCode ();
    }
}
