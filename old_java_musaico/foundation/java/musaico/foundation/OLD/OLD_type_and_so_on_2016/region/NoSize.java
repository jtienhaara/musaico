package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.order.Order;

import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * No Size at all.  Use Size.NONE instead.
 * </p>
 *
 * <p>
 * Every Size has a natural order, such as "size 1 is less than size 2"
 * or "a volume of 3 cubic metres is less than a volume of 10 cubic
 * metres", and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Size must implement hashCode () and equals () in
 * order to play nicely with HashMaps.
 * </p>
 *
 * <p>
 * in Java every Size must be Serializable in order to
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
class NoSize
    implements Size, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The default ordering to use for NoSize vs. other Sizes. */
    private static final Order<Size> ORDER = new SizeStringOrder ();


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals ( Object object )
    {
        // Every 2 NoSizes are always equal, even if their causes
        // are different contract violations.
        if ( object instanceof NoSize )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see musaico.foundation.region.Size#expr()
     */
    @Override
    public SizeExpression expr ()
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
    public Order<Size> order ()
        throws ReturnNeverNull.Violation
    {
        return NoSize.ORDER;
    }


    /**
     * @see musaico.foundation.region.Size#space()
     */
    @Override
    public Space space ()
        throws ReturnNeverNull.Violation
    {
        return Space.NONE;
    }
}
