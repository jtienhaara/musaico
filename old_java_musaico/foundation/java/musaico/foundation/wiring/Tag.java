package musaico.foundation.wiring;

import java.io.Serializable;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;
import musaico.foundation.contract.obligations.Parameter2;

import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Metadata attached to a Wire, Conductor, Carrier, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Tag must be Serializable in order to play
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public class Tag
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /** No Tag at all. */
    public static final Tag NONE = new Tag ();

    private static final Advocate classAdvocate =
        new Advocate ( Tag.class );


    private final TagNamespace namespace;
    private final String id;
    private final String path;
    private final int hashCode;
    private Class<? extends Serializable> dataType;
    private Serializable data;

    private Tag ()
    {
        // No tag.
        this.namespace = TagNamespace.NONE;
        this.id = "no_tag";
        this.path = this.namespace.path () + "." + this.id;
        this.dataType = Serializable.class;
        this.data = "no-tag";

        this.hashCode = 0;
    }

    public <DATA extends Serializable> Tag (
            TagNamespace parent_namespace,
            String id,
            Class<DATA> data_type,
            DATA data
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Parameter2.MustBeStringID.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              parent_namespace, id, data_type, data );
        classAdvocate.check ( Parameter2.MustBeStringID.CONTRACT,
                              id );

        this.namespace = parent_namespace;
        this.id = id;
        if ( this.namespace == TagNamespace.ROOT )
        {
            this.path = this.id;
        }
        else
        {
            this.path = this.namespace.path () + "." + this.id;
        }
        this.dataType = data_type;
        this.data = data;

        this.hashCode = this.path.hashCode ();
    }

    /**
     * @return This Tag's data.  Never null.
     */
    public final Serializable data ()
        throws Return.NeverNull.Violation
    {
        return this.data;
    }

    /**
     * <p>
     * Returns this Tag's data, cast to the specified class.
     * </p>
     *
     * @param as_type The class of data to return.
     *                Must not be null.
     *
     * @param default_value The default value, in case this
     *                      Tag's data is not an instance of
     *                      the specified data class.
     *                      CAN be null.  (Don't pass null!)
     *
     * @return This Tag's data, cast to the specified
     *         class; or, if this Tag's data is not a
     *         instance of the specified data class, then
     *         the specified default value is returned.
     *         Can be null if and only if the specified
     *         data class cannot be assigned and the
     *         specified default value is null.
     */
    @SuppressWarnings("unchecked") // Cast Object - AS_DATA.
    public final <AS_DATA extends Serializable> AS_DATA data (
            Class<AS_DATA> as_type,
            AS_DATA default_value
            )
        throws Return.NeverNull.Violation
    {
        if ( as_type == null
             || this.data == null // Should be impossible...
             || ! as_type.isInstance ( this.data ) )
        {
            return default_value;
        }

        // SuppressWarnings("unchecked") Cast Object - AS_DATA.
        return (AS_DATA) this.data;
    }

    /**
     * @return The class of this Tag's data.  The data type
     *         can be generalized from the actual data's
     *         class.  For example, this Tag might have
     *         a data type Number.class, and carry data
     *         Integer.MIN_VALUE or BigDecimal.ONE.
     *         Never null.
     */
    public final Class<? extends Serializable> dataType ()
    {
        return this.dataType;
    }

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

        final Tag that = (Tag) object;

        if ( this.path == null )
        {
            if ( that.path != null )
            {
                return false;
            }
        }
        else if ( that.path == null )
        {
            return false;
        }
        else if ( ! this.path.equals ( that.path ) )
        {
            return false;
        }

        // The data is not considered for equals () -- we want
        // to make sure 2 Tags with the same namespace and id are
        // considered equal in a Set or Map, even if their data differ.

        return true;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.hashCode;
    }

    /**
     * @return The unique identifier of this TagNamespace within its
     *         parent TagNamespace (if any).  Never null.
     */
    public final String id ()
        throws Return.NeverNull.Violation
    {
        return this.id;
    }

    /**
     * @return The parent TagNamespace.  Never null.
     */
    public final TagNamespace namespace ()
    {
        return this.namespace;
    }

    /**
     * @return The path (of TagNamespace[s]) to this Tag, including
     *         this Tag's id.  For example, "musaico.wire.type"
     *         or "myapplication.http.trace.123" and so on.
     *         Never null.
     */
    public final String path ()
        throws Return.NeverNull.Violation
    {
        return this.path;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final String as_string = "tag [ " + this.path () + " ]"
            + " = " + StringRepresentation.of ( this.data,
                                                StringRepresentation.DEFAULT_OBJECT_LENGTH );
        return as_string;
    }
}
