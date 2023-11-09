package musaico.foundation.term.reducibility;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A Reducible objects which must all be reduced before a
 * final result can be generated.
 * </p>
 *
 * </p>
 * For example, an Invocation is a CompositeReducible: it must first
 * reduce the 
 *
 *
 * <p>
 * In Java every Reducibility must be Serializable in order to
 * play nicely across RMI.  However users of the Reducibility
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
 * </p>
 *
 * <p>
 * In Java every Reducibility must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.term.reducibility.MODULE#COPYRIGHT
 * @see musaico.foundation.term.reducibility.MODULE#LICENSE
 */
public class CompositeReducible<VALUE extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * @see musaico.foundation.term.Reducibility#child()
     */
    @Override
    public final Reducibility<VALUE> child ()
        throws ReturnNeverNull.Violation
    {
        !!!;
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

        final CompositeReducible<?> that = (CompositeReducible<?>) object;

        !!!;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return
            !!!;
    }


    /**
     * @see musaico.foundation.term.Reducibility#reduce()
     */
    @Override
    public final Reducibility<VALUE> reduce ()
        throws ReturnNeverNull.Violation
    {
        !!!;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + ": "
            !!!;
    }


    /**
     * @see musaico.foundation.term.Reducibility#value()
     */
    @Override
    public final Multiplicity<VALUE> value ()
        throws ReturnNeverNull.Violation
    {
        !!!;
    }
}
