package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.Arrays;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
 
import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;


/**
 * <p>
 * Boilerplate implementation of a Component in a wiring Board.
 * </p>
 *
 *
 * <p>
 * In Java every Component must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public abstract class AbstractComponent
    implements Component, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;

    // Checks obligations on constructor static method parameters and so on.
    private static final Advocate classAdvocate =
        new Advocate ( AbstractComponent.class );


    // A short, distinguishing name for this AbstractComponent.
    private final String name;

    // The input leads into this Component.
    // Each Class defines the type of data that this AbstractComponent
    // will pull in a given circuit context over a specific wire #,
    // such as String data or Integer data or some FooBar object data
    // or other, and so on.  Each lead has a specific data requirement,
    // or Object.class if any data is acceptable to be pulled
    // as input from the given wire..
    private final Class<?> [] wiresIn;

    // The output leads from this Component.
    // Each Class defines the type of data that this AbstractComponent
    // will push to a given circuit context over a specific wire #,
    // such as String data or Integer data or some FooBar object data
    // or other, and so on.  Each lead has a specific data requirement,
    // or Object.class if any data will be pushed as output
    // on the given wire.
    private final Class<?> [] wiresOut;

    // Hash code of this Component.
    private final int hashCode;


    /**
     * <p>
     * Creates a new AbstractComponent.
     * </p>
     *
     * @param name A short, distinguishing name for this AbstractComponent.
     *             Must not be null.
     *
     * @param wires_in The input leads into this Component.
     *                 Each Class defines the type of data
     *                 that this AbstractComponent
     *                 will pull in a given circuit context
     *                 over a specific wire #, such as String data
     *                 or Integer data or some FooBar object data
     *                 or other, and so on.
     *                 Each lead has a specific data requirement,
     *                 or Object.class if any data is acceptable
     *                 to be pulled as input from the given wire..
     *                 Can be empty, if this Component has
     *                 no inputs.  Must not be null.
     *                 Must not contain any null elements.
     *
     * @param wires_out The output leads from this Component.
     *                  Each Class defines the type of data
     *                  that this AbstractComponent
     *                  will push to a given circuit context
     *                  over a specific wire #, such as String data
     *                  or Integer data or some FooBar object data
     *                  or other, and so on.
     *                  Each lead has a specific data requirement,
     *                  or Object.class if any data will be pushed
     *                  as output on the given wire.
     *                  Can be empty, if this Component has
     *                  no outputs.  Must not be null.
     *                  Must not contain any null elements.
     */
    public AbstractComponent (
            String name,
            Class<?> [] wires_in,
            Class<?> [] wires_out
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              name, wires_in, wires_out );
        classAdvocate.check ( Parameter2.MustContainNoNulls.CONTRACT,
                              wires_in );
        classAdvocate.check ( Parameter3.MustContainNoNulls.CONTRACT,
                              wires_out );

        this.name = name;
        this.wiresIn = wires_in;
        this.wiresOut = wires_out;

        int hash_code = 31 * this.name.hashCode ();
        hash_code += 17 * this.wiresIn.length;
        hash_code += 13 * this.wiresOut.length;
        for ( Class<?> wire_in : this.wiresIn )
        {
            hash_code += wire_in.hashCode ();
        }
        for ( Class<?> wire_out : this.wiresOut )
        {
            hash_code += wire_out.hashCode ();
        }

        this.hashCode = hash_code;
    }


    /**
     * Every Component must implement:
     * @see musaico.foundation.wiring.Component#conduct(musaico.foundation.wiring.Circuit, java.lang.String)
     */


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
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final AbstractComponent that = (AbstractComponent) object;

        final String thatName = that.name ();
        if ( this.name == null )
        {
            if ( thatName != null )
            {
                return false;
            }
        }
        else if ( thatName == null )
        {
            return false;
        }
        else if ( ! this.name.equals ( thatName ) )
        {
            return false;
        }

        if ( this.wiresIn == null )
        {
            if ( that.wiresIn != null )
            {
                return false;
            }
        }
        else if ( that.wiresIn == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.wiresIn, that.wiresIn ) )
        {
            return false;
        }

        if ( this.wiresOut == null )
        {
            if ( that.wiresOut != null )
            {
                return false;
            }
        }
        else if ( that.wiresOut == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.wiresOut, that.wiresOut ) )
        {
            return false;
        }

        if ( ! this.equalsComponent ( that ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @param that The Component with which this might be equal.
     *             Must be of the exact same class as this.
     *             Must not be null.
     * @return True if this Component equals that one, false if not.
     */
    protected abstract boolean equalsComponent (
            AbstractComponent that
            )
        throws Parameter1.MustBeInstanceOf.Violation,
               EveryParameter.MustNotBeNull.Violation;


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see musaico.foundation.wiring.Component#name()
     */
    @Override
    public final String name ()
        throws Return.NeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public final String toString ()
    {
        return this.name;
    }
    /**
     * @see musaico.foundation.wiring.Component#wiresIn()
     */
    @Override
    public final Class<?> [] wiresIn ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.wiresIn;
    }


    /**
     * @see musaico.foundation.wiring.Component#wiresOut()
     */
    @Override
    public final Class<?> [] wiresOut ()
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.wiresOut;
    }
}
