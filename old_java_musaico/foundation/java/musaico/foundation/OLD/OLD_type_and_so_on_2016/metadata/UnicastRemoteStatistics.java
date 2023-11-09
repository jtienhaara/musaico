package musaico.foundation.metadata;

import java.io.Serializable;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * A thread-safe Statistics which can be shared over RMI.
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
public class UnicastRemoteStatistics
    extends UnicastRemoteObject
    implements Statistics, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( UnicastRemoteStatistics.class );


    /** Synchronize all critical sections on this lock: */
    private final Serializable lock = new String ();

    /** Lookup of count by Stat for the object: */
    private final Map<Stat, Long> counts =
        new HashMap<Stat, Long> ();


    /**
     * <p>
     * Creates a new UnicastRemoteStatistics.
     * </p>
     *
     * @throws RemoteException If the UnicastRemoteObject constructor fails
     *                         for some abstruse reason.
     */
    public UnicastRemoteStatistics ()
        throws RemoteException
    {
        super (); // Throws RemoteException
    }


    /**
     * @see musaico.foundation.metadata.Statistics#all()
     */
    @Override
    public Stat [] all ()
        throws ReturnNeverNull.Violation
    {
        final Stat [] statistics;
        synchronized ( this.lock )
        {
            final Stat [] template =
                new Stat [ this.counts.size () ];
            statistics = this.counts.keySet ().toArray ( template );
        }

        Arrays.sort ( statistics );

        return statistics;
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
        final Long count;
        synchronized ( this.lock )
        {
            count = this.counts.get ( statistic );
        }

        if ( count == null )
        {
            return 0L;
        }
        else
        {
            return count.longValue ();
        }
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
        final long new_count;
        synchronized ( this.lock )
        {
            final Long undecremented = this.counts.get ( statistic );
            if ( undecremented == null )
            {
                new_count = 0L;
            }
            else
            {
                final long old_count = undecremented.longValue ();
                if ( old_count <= 0L )
                {
                    new_count = 0L;
                }
                else
                {
                    new_count = old_count - 1L;
                }

                this.counts.put ( statistic, new_count );
            }
        }

        return new_count;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        // We implement this method just to satisfy the requirement
        // that every metadatum implements equals ().  However each
        // Statistics really is unique, so we rely on the
        // standard Object.equals () method.
        return super.equals ( object );
    }


    /**
     * @see java.lang.hashCode()
     */
    @Override
    public int hashCode ()
    {
        // We implement this method just to satisfy the requirement
        // that every metadatum implements hashCode ().  However each
        // Statistics really is unique, so we rely on the
        // standard Object.hashCode () method.
        return super.hashCode ();
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
        final long new_count;
        synchronized ( this.lock )
        {
            final Long unincremented = this.counts.get ( statistic );
            if ( unincremented == null )
            {
                new_count = 1L;
            }
            else
            {
                new_count = unincremented.longValue () + 1L;

                this.counts.put ( statistic, new_count );
            }
        }

        return new_count;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + "#" + this.hashCode ();
    }
}
