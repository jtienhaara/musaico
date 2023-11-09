package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A foundational implementation for named Symbols, providing the
 * boilerplate code.
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
 * @see musaico.foundation.type.MODULE#COPYRIGHT
 * @see musaico.foundation.type.MODULE#LICENSE
 */
public class BasicSymbol
    implements Symbol, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final Advocate classContracts =
        new Advocate ( BasicSymbol.class );


    // Checks method obligations and guarantees.
    private final Advocate contracts;

    // The name of this BasicSymbol.
    private final String name;


    /**
     * <p>
     * Creates a new BasicSymbol.
     * </p>
     *
     * @param name The name of this BasicSymbol.  Must not be null.
     */
    public BasicSymbol (
                        String name
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this.name = name;

        this.contracts = new Advocate ( this );
    }


    /**
     * @return This BasicSymbol's advocate, for derived classes to
     *         check parameter obligations and return value guarantees.
     *         Never null.
     */
    protected final Advocate contracts ()
    {
        return this.contracts;
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
        else if ( ! ( object instanceof BasicSymbol ) )
        {
            return false;
        }

        final BasicSymbol that = (BasicSymbol) object;

        if ( this.name == null )
        {
            if ( that.name == null )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if ( that.name == null )
        {
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.name.hashCode ();
    }


    /**
     * @see musaico.foundation.type.Symbol#name()
     */
    @Override
    public final String name ()
        throws ReturnNeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.Object.toString()
     */
    @Override
    public String toString ()
    {
        return this.name;
    }
}
