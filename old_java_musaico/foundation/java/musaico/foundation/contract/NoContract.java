package musaico.foundation.contract;

import java.io.Serializable;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * No Contract at all.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
public class NoContract
    implements Contract<Object, UncheckedViolation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
            Object obj
            )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "No contract at all.";
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            Object grain
            )
    {
        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 10;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    @Override
    public UncheckedViolation violation (
            Object plaintiff,
            Object evidence
            )
    {
        return new UncheckedViolation ( this,
                                        "An arbitrary violation of a non-existant contract.",
                                        plaintiff,
                                        evidence );
    }


    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object, java.lang.Throwable)
     */
    @Override
    public UncheckedViolation violation (
            Object plaintiff,
            Object evidence,
            Throwable cause
            )
    {
        return new UncheckedViolation ( this,
                                        "An arbitrary violation of a non-existant contract.",
                                        plaintiff,
                                        evidence,
                                        cause );
    }
}
