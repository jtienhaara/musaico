package musaico.foundation.region;

import java.io.Serializable;


/**
 * <p>
 * No Position at all.  Use Position.NONE instead.
 * </p>
 *
 *
 * <p>
 * In Java every position must implement <code> equals () </code>
 * and <code> hashCode () </code>, in order to play well with
 * hash sets and hash maps.
 * </p>
 *
 * <p>
 * In Java every Position must be Serializable in order to
 * play nicely over RMI.
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
/* Package private: */
class NoPosition
    implements Position, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals ( Object object )
    {
        if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.region.Position#expr()
     */
    @Override
    public PositionExpression expr ()
    {
        return Space.NONE.expr ( this );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see musaico.foundation.region.SpatialElement#space()
     */
    @Override
    public Space space ()
    {
        return Space.NONE;
    }
}
