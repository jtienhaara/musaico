package musaico.foundation.contract;

import java.io.Serializable;


import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Ignores all contracts, never bothers to enforce any of them.
 * </p>
 *
 *
 * <p>
 * In Java, every Judge must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.contract.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.MODULE#LICENSE
 */
public final class NoJudge
    implements Judge
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see musaico.foundation.contract.Judge#inspect(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    @Override
    public final
        <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
            void inspect (
                          Contract<EVIDENCE, VIOLATION> contract,
                          Object plaintiff,
                          EVIDENCE evidence
                          )
        throws VIOLATION
    {
        // Do nothing.
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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

        final NoJudge that = (NoJudge) object;

        return true;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }
}
