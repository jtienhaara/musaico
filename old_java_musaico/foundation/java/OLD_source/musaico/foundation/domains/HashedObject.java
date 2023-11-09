package musaico.foundation.domains;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * The snapshot of an object at a specific point in time.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public class HashedObject
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** A String representation of the object in this HashedObject. */
    public final String objectAsString;

    /** A hash code representation of the object in this HashedObject. */
    public final int hashCode;


    /**
     * <p>
     * Creates a new HashedObject, capturing the specified
     * object's state right now.
     * </p>
     *
     * <p>
     * An object whose <code> hashCode () </code> changes whenever
     * its internal state changes can safely use this constructor.
     * Any object whose hashCode () does not change even when
     * its internal state changes should use the constructor which
     * passes in an externally generated hash value.
     * </p>
     *
     * @param object The object whose state will be stored.
     *               Can be null.
     */
    public HashedObject (
                         Object object
                         )
    {
        this ( object,
               object == null
                   ? 0
                   : object.hashCode () );
    }

    /**
     * <p>
     * Creates a new HashedObject, capturing the specified
     * object's state right now as summarized by the specified
     * hash code.
     * </p>
     *
     * @param object The object whose state will be stored.
     *               Can be null.
     *
     * @param hash_code A hash of the object in its current
     *                  state.  Can be any integer value.
     */
    public HashedObject (
                         Object object,
                         int hash_code
                         )
    {
        this.objectAsString = "" + object;
            this.hashCode = hash_code;
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final HashedObject that = (HashedObject) object;
        if ( this.hashCode != that.hashCode )
        {
            return false;
        }
        else if ( this.objectAsString != that.objectAsString )
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
        return this.hashCode;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + " "
            + this.objectAsString
            + "["
            + this.hashCode
            + "]";
    }
}
