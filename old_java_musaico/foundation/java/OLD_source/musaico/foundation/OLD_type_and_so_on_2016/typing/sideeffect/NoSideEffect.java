package musaico.foundation.typing.sideeffect;

import java.io.Serializable;


import musaico.foundation.domains.ClassName;


/**
 * <p>
 * No side-effect at all.  Not very useful.
 * </p>
 *
 *
 * <p>
 * In Java every SideEffect must be Seriaizable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.typing.sideeffect.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.sideeffect.MODULE#LICENSE
 */
public class NoSideEffect
    implements SideEffect, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            // Any NoSideEffect != null.
            return false;
        }
        else if ( object == this )
        {
            // Any NoSideEffect == itself.
            return true;
        }
        else if ( ! ( object instanceof NoSideEffect ) )
        {
            // Any NoSideEffect != any object of a different class.
            return false;
        }

        // Every NoSideEffect == every other NoSideEffect.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return 0;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
