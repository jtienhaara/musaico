package musaico.foundation.domains.elements;

import java.io.Serializable;


import musaico.foundation.domains.StringRepresentation;


/**
 * <p>
 * An Exception thrown because Elements cannot be represented
 * as some particular type of container, such as a Collection or an array,
 * because of the length of the Elements.
 * </p>
 *
 * <p>
 * For example, if there are more than <code> Integer.MAX_VALUE </code>
 * Elements, then calling their <code> array () </code> method will
 * generate this exception.
 * </p>
 *
 *
 * <p>
 * Every ElementsLengthException must be Serializable in order to play nicely
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
public class ElementsLengthException
    extends RuntimeException
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // A Serializable representation (such as toString ()) of the
    // of the Elements which are too numerous to turn into some
    // container or other.
    private final Serializable elements;

    // The # of Elements.
    private final long numElements;


    /**
     * <p>
     * Creates a new ElementsLengthException.
     * </p>
     *
     * @param elements The Elements which are too long.
     *                 Must not be null.
     *
     * @throws NullPointerException If the specified elements are null.
     */
    public ElementsLengthException (
            Elements<?> elements
            )
        throws NullPointerException
    {
        this ( elements,        // elements
               elements == null // num_elements
                   ? -1L
                   : elements.length (),
               null );          // cause
    }


    /**
     * <p>
     * Creates a new ElementsLengthException.
     * </p>
     *
     * @param elements The Elements which are too long.
     *                 Must not be null.
     *
     * @param cause The Exception or other Throwable that induced
     *              this ElementsLengthException.  Can be null for no cause.
     *
     * @throws NullPointerException If the specified elements are null.
     */
    public ElementsLengthException (
            Elements<?> elements,
            Throwable cause
            )
        throws NullPointerException
    {
        this ( elements,        // elements
               elements == null // num_elements
                   ? -1L
                   : elements.length (),
               cause );
    }


    /**
     * <p>
     * Creates a new ElementsLengthException.
     * </p>
     *
     * @param elements The Elements which are too long.
     *                 Must not be null.
     *
     * @param num_elements The number of elements.
     *                     Must be greater than or equal to 0L.
     *
     * @throws NullPointerException If the specified elements representation
     *                              is null.
     *
     * @throws IllegalArgumentException If the specified num_elements
     *                                  is less than 0L.
     */
    public ElementsLengthException (
            Elements<?> elements,
            long num_elements
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        this ( elements,     // elements
               num_elements, // num_elements
               null );       // cause
    }


    /**
     * <p>
     * Creates a new ElementsLengthException.
     * </p>
     *
     * @param elements The Elements which are too long.
     *                 Must not be null.
     *
     * @param num_elements The number of elements.
     *                     Must be greater than or equal to 0L.
     *
     * @param cause The Exception or other Throwable that induced
     *              this ElementsLengthException.  Can be null for no cause.
     *
     * @throws NullPointerException If the specified elements representation
     *                              is null.
     *
     * @throws IllegalArgumentException If the specified num_elements
     *                                  is less than 0L.
     */
    public ElementsLengthException (
            Elements<?> elements,
            long num_elements,
            Throwable cause
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        if ( elements == null )
        {
            throw new NullPointerException ( "ERROR ElementsLengthException cannot be constructed with elements = "
                                             + elements
                                             + ", num_elements = "
                                             + num_elements );
        }
        else if ( num_elements < 0L )
        {
            throw new IllegalArgumentException ( "ERROR ElementsLengthException cannot be constructed with elements = "
                                                 + elements
                                                 + ", num_elements = "
                                                 + num_elements );
        }

        this.elements =
            StringRepresentation.of ( elements, // Serializable
                                      StringRepresentation.DEFAULT_ARRAY_LENGTH );
        this.numElements = num_elements;

        if ( cause != null )
        {
            this.initCause ( cause );
        }
    }


    /**
     * <p>
     * Creates a new ElementsLengthException for a non-Elements object.
     * </p>
     *
     * @param representation A String representation of the object.
     *                       Must not be null.
     *
     * @param length The illegal number of elements.
     *               Must be greater than or equal to 0L.
     *
     * @param cause The Exception or other Throwable that induced
     *              this ElementsLengthException.  Can be null for no cause.
     *
     * @throws NullPointerException If the specified object representation
     *                              is null.
     *
     * @throws IllegalArgumentException If the specified length
     *                                  is less than 0L.
     */
    public ElementsLengthException (
            String representation,
            long length
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        this ( representation, // representation
               length,         // length
               null );         // cause
    }


    /**
     * <p>
     * Creates a new ElementsLengthException for a non-Elements object.
     * </p>
     *
     * @param representation A String representation of the object.
     *                       Must not be null.
     *
     * @param length The illegal number of elements.
     *               Must be greater than or equal to 0L.
     *
     * @param cause The Exception or other Throwable that induced
     *              this ElementsLengthException.  Can be null for no cause.
     *
     * @throws NullPointerException If the specified object representation
     *                              is null.
     *
     * @throws IllegalArgumentException If the specified length
     *                                  is less than 0L.
     */
    public ElementsLengthException (
            String representation,
            long length,
            Throwable cause
            )
        throws NullPointerException,
               IllegalArgumentException
    {
        if ( representation == null )
        {
            throw new NullPointerException ( "ERROR ElementsLengthException cannot be constructed with representation = "
                                             + representation
                                             + ", length = "
                                             + length );
        }
        else if ( length < 0L )
        {
            throw new IllegalArgumentException ( "ERROR ElementsLengthException cannot be constructed with representation = "
                                                 + representation
                                                 + ", length = "
                                                 + length );
        }

        this.elements = representation;
        this.numElements = length;

        if ( cause != null )
        {
            this.initCause ( cause );
        }
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

        final ElementsLengthException that =
            (ElementsLengthException) object;

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
