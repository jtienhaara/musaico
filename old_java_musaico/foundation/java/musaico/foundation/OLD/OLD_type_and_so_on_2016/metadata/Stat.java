package musaico.foundation.metadata;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A statistic about some object, tracked in its metadata, such as
 * the number of active references to the object, the number of
 * errors or warnings its methods have generated, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Stat must implement:
 * </p>
 *
 * @see java.lang.Object#equals(java.lang.Object)
 * @see java.lang.Object#hashCode()
 * @see java.lang.Object#toString()
 *
 * <p>
 * In Java every Stat must be Serializable in order to play
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class Stat
    implements Comparable<Stat>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on static methods and constructors for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Stat.class );


    /** The number of non-contract errors caused by an object. */
    public static final Stat ERRORS =
        new Stat ( "errors" );

    /** The number of times an object's contract guarantees and
     *  obligations were violated. */
    public static final Stat VIOLATIONS =
        new Stat ( "violations" );

    /** Reference count on an object. */
    public static final Stat REFERENCES =
        new Stat ( "references" );

    /** The number of non-contract warnings caused by an object. */
    public static final Stat WARNINGS =
        new Stat ( "warnings" );


    /** The name of this statistic, such as "references". */
    private final String name;


    /**
     * <p>
     * Creates a new Stat with the specified name.
     * </p>
     *
     * @param name The name of this Stat, such as "references".
     *             Must not be null.
     */
    public Stat (
                 String name
                 )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        this.name = name;
    }


    /**
     * @see java.util.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo (
                          Stat that
                          )
    {
        if ( that == null )
        {
            // Any Stat < null.
            return Integer.MIN_VALUE + 1;
        }
        else if ( this == that )
        {
            // Any Stat == itself.
            return 0;
        }

        // Compare names using ASCii / etc character ordering.
        return this.name.compareTo ( that.name );
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
        else if ( ! ( object instanceof Stat ) )
        {
            return false;
        }

        final Stat that = (Stat) object;

        return this.name.equals ( that.name );
    }


    /**
     * @see java.lang.hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.name.hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.name;
    }
}
