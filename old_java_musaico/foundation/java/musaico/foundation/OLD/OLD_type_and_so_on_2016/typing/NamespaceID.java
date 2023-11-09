package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A unique Namespace identifier for generic, non-behavioural Namespaces
 * (that is, not Tags and not Types) within a SymbolTable.
 * </p>
 *
 *
 * <p>
 * In Java every SymbolID must be Serializable in order to
 * play nicely with RMI.
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
public class NamespaceID
    extends SymbolID<Namespace>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The ID of the toplevel Namespace. */
    public static final NamespaceID ROOT = new NamespaceID ( "root" );

    /** The ID of the "none" Namespace. */
    public static final NamespaceID NONE = new NamespaceID ( "none" );

    /** The ID of a Namespace's parent Namespace. */
    public static final NamespaceID PARENT = new NamespaceID ( "#parent" );


    /**
     * <p>
     * Creates a new NamespaceID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param name The String name of this identifier,
     *             such as "primitives" and so on.  Must not be null.
     */
    public NamespaceID (
                        String name
                        )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new NamespaceID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param name The String name of this identifier,
     *             such as "primitives" and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public NamespaceID (
                        String name,
                        Visibility visibility
                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( Namespace.TYPE, name, visibility );
    }


    /**
     * @see musaico.foundation.typing.SymbolID#rename(java.lang.String, musaico.foundation.typing.Visibility)
     */
    @Override
    public NamespaceID rename (
                               String name,
                               Visibility visibility
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new NamespaceID ( name,
                                 visibility );
    }
}
