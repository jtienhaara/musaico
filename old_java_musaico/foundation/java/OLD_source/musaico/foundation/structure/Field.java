package musaico.foundation.structure;

import java.io.Serializable;


/**
 * <p>
 * A singleton Structure, with only a single simple value.
 * </p>
 *
 *
 * <p>
 * In Java every Structure must implement equals (), hashCode ()
 * and toString ().
 * </p>
 *
 * <p>
 * In Java every Structure must be Serializable in order
 * to play nicely over RMI.  However users of Structures must
 * be careful: the contents of a Structure might not be Serializable,
 * in which case exceptions will be generated when trying to
 * pass Structures over RMI.
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public class Field<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( Field.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The value of this Field.
    private final VALUE value;


    /**
     * <p>
     * Creates a new Field.
     * </p>
     *
     * @param value The single simple value of this Field, such as a String,
     *              or an Integer, or a Term of elements, and so on.
     *              Must not be null.
     */
    public Field (
            VALUE value
            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               value );

        this.value = value;

        this.contracts = new Advocate ( this );
    }


    !!!;
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can be overridden.
     */
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final AbstractPlumber<?, ?, ?> that = (AbstractPlumber<?, ?, ?>) object;

        if ( this.inputPipes == null )
        {
            if ( that.inputPipes != null )
            {
                return false;
            }
        }
        else if ( that.inputPipes == null )
        {
            return false;
        }
        else if ( ! this.inputPipes.equals ( that.inputPipes ) )
        {
            return false;
        }

        if ( this.downstreamType == null )
        {
            if ( that.downstreamType != null )
            {
                return false;
            }
        }
        else if ( that.downstreamType == null )
        {
            return false;
        }
        else if ( ! this.downstreamType.equals ( that.downstreamType ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.getClass ().getName ().hashCode ();
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Can be overridden
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
