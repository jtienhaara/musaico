package musaico.foundation.term.countable;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.array.ArrayObject;

import musaico.foundation.term.Countable;


/**
 * <p>
 * Allows Countable terms to be treated as ArrayObjects.
 * </p>
 *
 * <p>
 * Typically used with
 * <code> musaico.foundation.domains.array </code> domains,
 * including in
 * <code> musaico.foundation.contract.obligations </code>
 * and <code> musaico.foundation.contract.guarantees </code>.
 * </p>
 *
 * <p>
 * Attempting to return an <code> array () </code> from a Countable
 * term with more than <code> Integer.MAX_VALUE </code> elements
 * will, regrettably, cause an exception to be thrown.
 * </p>
 *
 *
 * <p>
 * Every ArrayObject must be Serializable in order to play
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class CountableArrayObject<VALUE extends Object>
    extends ArrayObject<VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    private final Countable<VALUE> countable;


    /**
     * <p>
     * Creates a new CountableArrayObject from the specified
     * Countable term.
     * </p>
     *
     * @param countable The term to wrap as a CountableArrayObject.
     *                  Must not be null.
     */
    public CountableArrayObject (
            Countable<VALUE> countable
            )
    {
        super ( countable == null // element_class
                    ? null
                    : countable.type () == null
                        ? null
                        : countable.type ().elementClass (),
                null );           // null elements = 0 length.

        this.countable = countable;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#array()
     */
    @Override
    public final String [] array ()
    {
        final String string = this.string ();
        final String [] array = new String [ string.length () ];
        for ( int c = 0; c < array.length; c ++ )
        {
            array [ c ] = string.substring ( c, c + 1 );
        }

        return array;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#element(long)
     */
    @Override
    public final String element (
                                 long index
                                 )
        throws IllegalArgumentException
    {
        if ( index < 0L )
        {
            throw new IllegalArgumentException ( "No such index " + index
                                                 + " in "
                                                 + this );
        }

        final String string = this.string ();

        if ( index >= (long) string.length () )
        {
            throw new IllegalArgumentException ( "No such index " + index
                                                 + " in "
                                                 + this );
        }

        final String element = string.substring ( (int) index,
                                                  (int) index + 1 );

        return element;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#iterator()
     */
    @Override
    public final Iterator<String> iterator ()
    {
        return this.countable.iterator ();
    }


    /**
     * @see musaico.foundation.array.ArrayObject#length()
     */
    @Override
    public final long length ()
    {
        return (long) this.string ().length ();
    }


    /**
     * @return The String underlying this CountableArrayObject.  Never null,
     *         even if this was created with a null String.
     */
    public final String string ()
    {
        return super.array () [ 0 ];
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final String class_name = ClassName.of ( this.getClass () );
        final String string = this.string ();
        return class_name + " \"" + string + "\"";
    }
}
