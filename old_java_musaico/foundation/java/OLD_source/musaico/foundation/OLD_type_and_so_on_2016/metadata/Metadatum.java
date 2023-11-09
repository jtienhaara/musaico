package musaico.foundation.metadata;

import java.io.Serializable;

import java.util.LinkedHashSet;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * One metadatum describing some aspect of a particular object.
 * </p>
 *
 * <p>
 * For example, a metadatum might describe the SecurityPolicy for
 * the object.  Another metadatum might hold its Statistics,
 * counting the number of times it generates errors and so on.
 * </p>
 *
 *
 * <p>
 * In Java every metadatum must implement:
 * </p>
 *
 * @see java.lang.Object#equals(java.lang.Object)
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#toString()
 *
 *
 * <p>
 * In Java every Metadatum and every metadatum value must be Serializable
 * in order to play nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class Metadatum
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Flags for a metadatum, such as whether or not to carry it over
     * to a newly created Metadata instance during a call to
     * <code> renew () </code>.  For example, a SecurityPolicy
     * should be carried over to a new instance, whereas Statistics should
     * be reset to zeros.
     * </p>
     */
    public static enum Flag
    {
        /** If this flag is set, then re-use the same metadatum
         *  in the newly created Metadata instance.  For example,
         *  SecurityPolicy is typically carried over, but Statistics
         *  are not. */
        METADATUM_CARRY_OVER;
    }


    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardMetadata.class );


    // Checks method obligations and guarantees for us.
    private final ObjectContracts contracts;

    // The class / category of this metadatum.  Not necessarily
    // the same as metadatum.getClass (), which could be specialized.
    private final Class<? extends Serializable> metadatumClass;

    // The actual metadatum object.
    private final Serializable metadatum;

    // Zero or more flags which tell the parent Metadata how to
    // treat this metadatum (for example whether to carry it over
    // during renew (), or create a new, blank version).
    // A LinkedHashSet is used simply to keep a consistent order.
    private final LinkedHashSet<Metadatum.Flag> flags;


    public Metadatum (
                      Class<? extends Serializable> metadatum_class,
                      Serializable metadatum,
                      Metadatum.Flag ... flags
                      )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadatum_class, metadatum, flags );

        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               flags );

        this.metadatumClass = metadatum_class;
        this.metadatum = metadatum;
        this.flags = new LinkedHashSet<Metadatum.Flag> ();
        for ( Metadatum.Flag flag : flags )
        {
            this.flags.add ( flag );
        }

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object object
                                 )
    {
        if  ( object == this )
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

        final Metadatum that = (Metadatum) object;
        if ( this.metadatumClass != that.metadatumClass )
        {
            return false;
        }
        if ( ! this.metadatum.equals ( that.metadatum ) )
        {
            return false;
        }

        // Almost everything is all matchy-matchy.
        // We don't care if the flags are different.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.metadatumClass.hashCode () * 17;
        hash_code += this.metadatum.hashCode () * 31;

        return hash_code;
    }


    public final Class<? extends Serializable> metadatumClass ()
        throws ReturnNeverNull.Violation
    {
        return this.metadatumClass;
    }


    public final Serializable metadatum ()
        throws ReturnNeverNull.Violation
    {
        return this.metadatum;
    }


    public final boolean isSet (
                                Metadatum.Flag flag
                                )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               flag );

        return this.flags.contains ( flag );
    }


    public final Metadatum.Flag [] flags ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        final Metadatum.Flag [] template =
            new Metadatum.Flag [ this.flags.size () ];
        final Metadatum.Flag [] flags = this.flags.toArray ( template );
        return flags;
    }
}
