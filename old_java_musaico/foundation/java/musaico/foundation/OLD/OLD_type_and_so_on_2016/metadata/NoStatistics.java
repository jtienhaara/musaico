package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Tracks references to nothing.
 * </p>
 *
 * <p>
 * None of the methods do anything useful.  This is like a reference
 * counter for null memory space.  It represents the reference
 * count to nowhere.
 * </p>
 *
 *
 * <p>
 * In Java, every metadatum must be Serializable in order
 * to play nicely over RMI.
 * </p>
 *
 * <p>
 * In Java, every metadatum must implement equals(Object), hashCode()
 * and toString().
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class NoStatistics
    implements Statistics, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * @see musaico.foundation.metadata.Statistics#all()
     */
    @Override
    public Stat [] all ()
        throws ReturnNeverNull.Violation
    {
        return new Stat [ 0 ];
    }


    /**
     * @see musaico.foundation.metadata.Statistics#count(musaico.foundation.metadata.Stat)
     */
    @Override
    public long count (
                       Stat statistic
                       )
        throws ParametersMustNotBeNull.Violation
    {
        return 0L;
    }


    /**
     * @see musaico.foundation.metadata.Statistics#decrement(musaico.foundation.metadata.Stat)
     */
    @Override
    public long decrement (
                           Stat statistic
                           )
        throws ParametersMustNotBeNull.Violation
    {
        return 0L;
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

        // Every NoStatistics is identical to every other
        // NoStatistics of the same class.
        return true;
    }


    /**
     * @see java.lang.hashCode()
     */
    @Override
    public int hashCode ()
    {
        return 0;
    }


    /**
     * @see musaico.foundation.metadata.Statistics#increment(musaico.foundation.metadata.Stat)
     */
    @Override
    public long increment (
                           Stat statistic
                           )
        throws ParametersMustNotBeNull.Violation
    {
        return 0L;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
