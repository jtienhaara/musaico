package musaico.foundation.wiring;

import java.io.Serializable;

import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.EveryParameter;


/**
 * <p>
 * A namespace for Tags, each one uniquely identified within
 * the namespace.
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
public class TagNamespace
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /** The root TagNamespace. */
    public static final TagNamespace ROOT = new TagNamespace ( "." );

    /** No TagNamespace at all. */
    public static final TagNamespace NONE = new TagNamespace ();


    private static final Advocate classAdvocate =
        new Advocate ( TagNamespace.class );


    private final TagNamespace namespaceOrNull;
    private final String id;
    private final String path;
    private final int hashCode;

    private TagNamespace (
            String id
            )
    {
        // Root namespace.
        this.namespaceOrNull = null;
        this.id = id;
        this.path = this.id;

        this.hashCode = 1;
    }

    private TagNamespace ()
    {
        // No namespace.
        this.namespaceOrNull = null;
        this.id = "no_namespace";
        this.path = this.id;

        this.hashCode = 0;
    }

    public TagNamespace (
            TagNamespace parent_namespace,
            String id
            )
        throws EveryParameter.MustNotBeNull.Violation
    {
        classAdvocate.check ( EveryParameter.MustNotBeNull.CONTRACT,
                              parent_namespace, id );
        this.namespaceOrNull = parent_namespace;
        this.id = id;
        if ( this.namespaceOrNull == TagNamespace.ROOT )
        {
            this.path = this.id;
        }
        else
        {
            this.path = this.namespaceOrNull.path () + "." + this.id;
        }

        this.hashCode = this.path.hashCode ();
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

        final TagNamespace that = (TagNamespace) object;

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
     * @return The parent TagNamespace, or null if this is the toplevel
     *         TagNamespace.  CAN be null.
     */
    public final TagNamespace namespaceOrNull ()
    {
        return this.namespaceOrNull;
    }

    /**
     * @return The path (of parent TagNamespace[s]) to this
     *         TagNamespace, including this TagNamespace's id.
     *         For example, "musaico.wire" or
     *         "myapplication.http.trace" and so on.
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
        return "tag_namespace [ " + this.path + " ]";
    }
}
