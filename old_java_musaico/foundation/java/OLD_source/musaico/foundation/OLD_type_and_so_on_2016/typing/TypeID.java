package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A unique Type identifier within a SymbolTable.
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
public class TypeID
    extends SymbolID<Type<?>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TypeID.class );


    // The un-decorated Type name, before the Tag decorations have been
    // added on.
    private final String rawTypeName;

    // The Tag decorations to add on to the raw type name to form this TypeID.
    private final String tagNames;


    /**
     * <p>
     * Creates a new TypeID which will always be unique within a
     * SymbolTable, with no tag names ("").
     * </p>
     *
     * <p>
     * This constructor uses the Kind.ROOT of type.
     * Use another constructor to specify a different kind.
     * </p>
     *
     * @param raw_type_name The un-decorated name of this identifier,
     *                      before any tags have been appended to
     *                      decorate the name.  The raw type name is
     *                      used during sub-typing, as the common
     *                      root type name shared by all sub-types
     *                      of a root type.  For example, "number",
     *                      or "string", and so on.  Must not be null.
     */
    public TypeID (
                   String raw_type_name
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( raw_type_name,
               Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new TypeID which will always be unique within a
     * SymbolTable, with no tag names ("").
     * </p>
     *
     * <p>
     * This constructor uses the Kind.ROOT of type.
     * Use another constructor to specify a different kind.
     * </p>
     *
     * @param raw_type_name The un-decorated name of this identifier,
     *                      before any tags have been appended to
     *                      decorate the name.  The raw type name is
     *                      used during sub-typing, as the common
     *                      root type name shared by all sub-types
     *                      of a root type.  For example, "number",
     *                      or "string", and so on.  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public TypeID (
                   String raw_type_name,
                   Visibility visibility
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( raw_type_name,
               "",
               visibility );
    }


    /**
     * <p>
     * Creates a new TypeID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * <p>
     * This constructor uses the Kind.ROOT of type.
     * Use the other constructor to specify a different kind.
     * </p>
     *
     * @param raw_type_name The un-decorated name of this identifier,
     *                      before any tags have been appended to
     *                      decorate the name.  The raw type name is
     *                      used during sub-typing, as the common
     *                      root type name shared by all sub-types
     *                      of a root type.  For example, "number",
     *                      or "string", and so on.  Must not be null.
     *
     * @param tag_names The names of tags to append to the full name.
     *                  For example, a "number" type might have tags
     *                  "positive,odd" to create TypeID
     *                  "number[positive,odd]".  Or a "string" type might
     *                  have tags "lowercase,length(1,40)" to create
     *                  TypeID "string[lowercase,length(1,40)]".
     *                  Must not be null.
     */
    public TypeID (
                   String raw_type_name,
                   String tag_names
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( raw_type_name, tag_names, Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new TypeID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * <p>
     * This constructor uses the Kind.ROOT of type.
     * Use the other constructor to specify a different kind.
     * </p>
     *
     * @param raw_type_name The un-decorated name of this identifier,
     *                      before any tags have been appended to
     *                      decorate the name.  The raw type name is
     *                      used during sub-typing, as the common
     *                      root type name shared by all sub-types
     *                      of a root type.  For example, "number",
     *                      or "string", and so on.  Must not be null.
     *
     * @param tag_names The names of tags to append to the full name.
     *                  For example, a "number" type might have tags
     *                  "positive,odd" to create TypeID
     *                  "number[positive,odd]".  Or a "string" type might
     *                  have tags "lowercase,length(1,40)" to create
     *                  TypeID "string[lowercase,length(1,40)]".
     *                  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public TypeID (
                   String raw_type_name,
                   String tag_names,
                   Visibility visibility
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Kind.ROOT, raw_type_name, tag_names, visibility );
    }


    /**
     * <p>
     * Creates a new TypeID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param kind The Kind of Type, such as Kind.ROOT, or
     *             a UML meta-class, and so on.  Must not be null.
     *
     * @param raw_type_name The un-decorated name of this identifier,
     *                      before any tags have been appended to
     *                      decorate the name.  The raw type name is
     *                      used during sub-typing, as the common
     *                      root type name shared by all sub-types
     *                      of a root type.  For example, "number",
     *                      or "string", and so on.  Must not be null.
     *
     * @param tag_names The names of tags to append to the full name.
     *                  For example, a "number" type might have tags
     *                  "positive,odd" to create TypeID
     *                  "number[positive,odd]".  Or a "string" type might
     *                  have tags "lowercase,length(1,40)" to create
     *                  TypeID "string[lowercase,length(1,40)]".
     *                  Must not be null.
     */
    public TypeID (
                   Kind kind,
                   String raw_type_name,
                   String tag_names
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( kind, raw_type_name, tag_names, Visibility.PUBLIC );
    }


    /**
     * <p>
     * Creates a new TypeID which will always be unique within a
     * SymbolTable.
     * </p>
     *
     * @param kind The Kind of Type, such as Kind.ROOT, or
     *             a UML meta-class, and so on.  Must not be null.
     *
     * @param raw_type_name The un-decorated name of this identifier,
     *                      before any tags have been appended to
     *                      decorate the name.  The raw type name is
     *                      used during sub-typing, as the common
     *                      root type name shared by all sub-types
     *                      of a root type.  For example, "number",
     *                      or "string", and so on.  Must not be null.
     *
     * @param tag_names The names of tags to append to the full name.
     *                  For example, a "number" type might have tags
     *                  "positive,odd" to create TypeID
     *                  "number[positive,odd]".  Or a "string" type might
     *                  have tags "lowercase,length(1,40)" to create
     *                  TypeID "string[lowercase,length(1,40)]".
     *                  Must not be null.
     *
     * @param visibility The Visibility of this identifier, such as PUBLIC
     *                   or PRIVATE and so on.  Must not be null.
     */
    public TypeID (
                   Kind kind,
                   String raw_type_name,
                   String tag_names,
                   Visibility visibility
                   )
        throws ParametersMustNotBeNull.Violation
    {
        // Throws ParametersMustNotBeNull for raw_type_name and tag_names:
        super ( kind,
                TypeID.name ( raw_type_name, tag_names ),
                visibility );

        this.rawTypeName = raw_type_name;
        this.tagNames = tag_names;
    }


    /**
     * <p>
     * Creates a new TypeID name from the specified raw type name
     * and tag names.  For example, "number" or "number[positive,odd]"
     * or "string" or "string[lowercase,length(1,40)]" and so on.
     * </p>
     */
    public static final String name (
                                     String raw_type_name,
                                     String tag_names
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name, tag_names );

        if ( tag_names.equals ( "" ) )
        {
            return raw_type_name;
        }
        else
        {
            return raw_type_name + "[" + tag_names + "]";
        }
    }


    /**
     * @return The un-decorated name of this identifier,
     *         before any tags have been appended to
     *         decorate the name.  The raw type name is
     *         used during sub-typing, as the common
     *         root type name shared by all sub-types
     *         of a root type.  For example, whether the
     *         fully tagged name is "number" or
     *         "number[positive,odd]", the un-tagged
     *         raw type name is "number".  Or whether the
     *         full tagged name is "string" or
     *         "string[lowercase,length(1,40)]", the
     *         common raw type name is "string".
     *         Never null.
     */
    public final String rawTypeName ()
    {
        return this.rawTypeName;
    }


    /**
     * @see musaico.foundation.typing.SymbolID#rename(java.lang.String, musaico.foundation.typing.Visibility)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast this.type () - Kind.
    public TypeID rename (
                          String name,
                          Visibility visibility
                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return new TypeID ( this.type (),  // kind
                            name,          // raw_type_name
                            "",            // tag_names
                            visibility );  // visibility
    }


    /**
     * @return The tag names which decorate this TypeID.
     *         For example, a "number" type might have tags
     *         "positive,odd" for TypeID "number[positive,odd]".
     *         Or a "string" type might have tags "lowercase,length(1,40)"
     *         for TypeID "string[lowercase,length(1,40)]".
     *         Never null.
     */
    public final String tagNames ()
    {
        return this.tagNames;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        // Do not prefix the type name with its type (kind)
        // the way other symbols do.
        return this.name ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Force Type<Type<?>> -> Kind.
    public final Kind type ()
        throws ReturnNeverNull.Violation
    {
        return (Kind) super.type ();
    }
}
