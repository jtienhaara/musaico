package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A unique Tag identifier within a SymbolTable.
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
public class TagID
    extends SymbolID<Tag>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new TagID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param name The String name of this identifier,
     *             such as "doSomething" or "number"
     *             or "PrivateTag" and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public TagID (
                  String name
                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new TagID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param name The String name of this identifier,
     *             such as "doSomething" or "number"
     *             or "PrivateTag" and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public TagID (
                  String name,
                  Visibility visibility
                  )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Tag.TYPE, name, visibility );
    }


    /**
     * <p>
     * Creates a new TagID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param tag_type The Type of Tag this TagID references,
     *                 such as Tag.TYPE.  Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "doSomething" or "number"
     *             or "PrivateTag" and so on.  Must not be null.
     */
    public TagID (
                  TagType tag_type,
                  String name
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( tag_type, name, Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new TagID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param tag_type The Type of Tag this TagID references,
     *                 such as Tag.TYPE.  Must not be null.
     *
     * @param name The String name of this identifier,
     *             such as "doSomething" or "number"
     *             or "PrivateTag" and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public TagID (
                  TagType tag_type,
                  String name,
                  Visibility visibility
                  )
        throws ParametersMustNotBeNull.Violation
    {
        super ( tag_type, name, visibility );
    }


    /**
     * @see musaico.foundation.typing.SymbolID#rename(java.lang.String, musaico.foundation.typing.Visibility)
     */
    @Override
    public TagID rename (
                         String name,
                         Visibility visibility
                         )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new TagID ( this.type (),
                           name,
                           visibility );
    }


    /**
     * @see musaico.foundation.typing.SymbolID#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Type is always a TagType.
    public TagType type ()
    {
        return (TagType) super.type ();
    }
}
