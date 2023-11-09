package musaico.foundation.domains.elements;

import java.io.Serializable;


/**
 * <p>
 * The flags describing a sequence of Elements, describing
 * whether or not the Element values can be overwritten, whether
 * their length is fixed, and so on.
 * </p>
 *
 * <p>
 * In Java every ElementFlags must be serializable in order
 * to play nicely over RMI.
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
 * @see musaico.foundation.domains.elements.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.elements.MODULE#LICENSE
 */
public class ElementFlags
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Fixed values, fixed length Elements. */
    public static final ElementFlags IMMUTABLE =
        new ElementFlags ( false, false ); // isOverwritable, isVariableLength

    /** Fixed values, variable length Elements, such as a stack. */
    public static final ElementFlags VARIABLE_LENGTH =
        new ElementFlags ( false, true ); // isOverwritable, isVariableLength

    /** Overwritable, fixed length Elements, such as an array. */
    public static final ElementFlags OVERWRITABLE =
        new ElementFlags ( true, false ); // isOverwritable, isVariableLength

    /** Overwritable, variable length Elements, such as a dynamic list. */
    public static final ElementFlags MUTABLE =
        new ElementFlags ( true, true ); // isOverwritable, isVariableLength


    /** Array of the ElementFlags available by default (IMMUTABLE,
     *  VARIABLE_LENGTH, OVERWRITABLE, MUTABLE). */
    public static final ElementFlags [] ALL =
        new ElementFlags [] { IMMUTABLE,
                              VARIABLE_LENGTH,
                              OVERWRITABLE,
                              MUTABLE };


    private final boolean isOverwritable;
    private final boolean isVariableLength;

    /**
     * <p>
     * Creates new ElementFlags.
     * </p>
     *
     * <p>
     * The base combinations of ElementFlags (isOverwritable?
     * isVariableLength?) are constants, so this constructor is protected
     * (for access from derived ElementFlags only).
     * Use the ElementFlags constants instead.
     * </p>
     *
     * @see musaico.foundation.domains.elements.ElementFlags#IMMUTABLE
     * @see musaico.foundation.domains.elements.ElementFlags#VARIABLE_LENGTH
     * @see musaico.foundation.domains.elements.ElementFlags#OVERWRITABLE
     * @see musaico.foundation.domains.elements.ElementFlags#MUTABLE
     */
    protected ElementFlags (
            boolean is_overwritable,
            boolean is_variable_length
            )
    {
        this.isOverwritable = is_overwritable;
        this.isVariableLength = is_variable_length;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Can (and should) be overridden by derived classes.
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

        final ElementFlags that = (ElementFlags) object;

        if ( this.isOverwritable () != that.isOverwritable () )
        {
            return false;
        }
        else if ( this.isVariableLength () != that.isVariableLength () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Can (and should) be overridden by derived classes.
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        int add = 1;
        if ( this.isOverwritable () )
        {
            hash_code += add;
        }
        add *= 2;
        if ( this.isVariableLength () )
        {
            hash_code += add;
        }

        return hash_code;
    }


    /**
     * @return True if these Elements' values can change over time;
     *         false if the elements cannot change values.
     *         Independent of <code> isVarableLength () </code>; the
     *         element values might be overwritable while the length is
     *         fixed (such as in an array), or the length might
     *         be variable while all existing element values are
     *         fixed values (as in a stack).
     */
    public final boolean isOverwritable ()
    {
        return this.isOverwritable;
    }


    /**
     * @return True if the length of these Elements can change over time;
     *         false if they are fixed length.
     *         Independent of <code> isOverwritable () </code>; the
     *         element values might be overwritable while the length is
     *         fixed (such as in an array), or the length might
     *         be variable while all existing element values are
     *         fixed values (as in a stack).
     */
    public final boolean isVariableLength ()
    {
        return this.isVariableLength;
    }


    /**
     * @return ElementFlags which are the same as these, with the
     *         isVariableLength () flag set to false.
     *         Never null.
     *
     * Can (and should) be overridden by derived ElementFlags implementations.
     */
    public ElementFlags makeFixedLength ()
    {
        if ( ! this.isVariableLength )
        {
            // Already   ?, false.
            return this;
        }
        else if ( this.isOverwritable )
        {
            // Currently true, true.
            // Return    true, false.
            return ElementFlags.OVERWRITABLE;
        }
        else
        {
            // Currently false, true.
            // Return    false, false.
            return ElementFlags.IMMUTABLE;
        }
    }


    /**
     * @return ElementFlags which are the same as these, with the
     *         isOverwritable () flag set to false.
     *         Never null.
     *
     * Can (and should) be overridden by derived ElementFlags implementations.
     */
    public ElementFlags makeFixedValues ()
    {
        if ( ! this.isOverwritable )
        {
            // Already   false, ?.
            return this;
        }
        else if ( this.isVariableLength )
        {
            // Currently true, true.
            // Return    false, true.
            return ElementFlags.VARIABLE_LENGTH;
        }
        else
        {
            // Currently true, false.
            // Return    false, false.
            return ElementFlags.IMMUTABLE;
        }
    }


    /**
     * @return ElementFlags which are the same as these, with the
     *         isOverwritable () flag set to true.
     *         Never null.
     *
     * Can (and should) be overridden by derived ElementFlags implementations.
     */
    public ElementFlags makeOverwritable ()
    {
        if ( this.isOverwritable )
        {
            // Already   true, ?.
            return this;
        }
        else if ( this.isVariableLength )
        {
            // Currently false, true.
            // Return    true,  true.
            return ElementFlags.MUTABLE;
        }
        else
        {
            // Currently false, false.
            // Return    true,  false.
            return ElementFlags.OVERWRITABLE;
        }
    }


    /**
     * @return ElementFlags which are the same as these, with the
     *         isVariableLength () flag set to false.
     *         Never null.
     *
     * Can (and should) be overridden by derived ElementFlags implementations.
     */
    public ElementFlags makeVariableLength ()
    {
        if ( this.isVariableLength )
        {
            // Already   ?, true.
            return this;
        }
        else if ( this.isOverwritable )
        {
            // Currently true, false.
            // Return    true,  true.
            return ElementFlags.MUTABLE;
        }
        else
        {
            // Currently false, false.
            // Return    false, true.
            return ElementFlags.VARIABLE_LENGTH;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        if ( this == ElementFlags.IMMUTABLE )
        {
            return "IMMUTABLE";
        }
        else if ( this == ElementFlags.VARIABLE_LENGTH )
        {
            return "VARIABLE_LENGTH";
        }
        else if ( this == ElementFlags.OVERWRITABLE )
        {
            return "OVERWRITABLE";
        }
        else if ( this == ElementFlags.MUTABLE )
        {
            return "MUTABLE";
        }

        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "{ " );
        this.toStringFlags ( sbuf );
        sbuf.append ( " }" );

        return sbuf.toString ();
    }


    /**
     * <p>
     * Adds a String representation of these ElementFlags to the specified
     * StringBuilder.
     * </p>
     *
     * <p>
     * Derived classes should override this, calling
     * super.toStringFlags ( ... ), then appending ", "
     * and continuing with all derived class flags.
     * </p>
     */
    protected void toStringFlags (
            StringBuilder sbuf
            )
    {
        if ( this.isOverwritable () )
        {
            sbuf.append ( "overwritable" );
        }
        else
        {
            sbuf.append ( "fixed_values" );
        }

        if ( this.isVariableLength () )
        {
            sbuf.append ( ", variable_length" );
        }
        else
        {
            sbuf.append ( ", fixed_length" );
        }
    }
}
