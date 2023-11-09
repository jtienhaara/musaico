package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.One;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * The input to, and output from, a SubType operation, which creates
 * a new Type by adding Tags to a parent Type.  The workbench serves
 * as the container for the details needed to create the sub-Type,
 * including the mutable SymbolTable for the new sub-Type.
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
public class SubTypeWorkBench<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( SubTypeWorkBench.class );

    // Duplicate of Kind.ROOT.  Exists for abstruse startup
    // logic reasons (to make sure Namespace gets loaded before Type
    // before we create SubTypeWorkBench.NONE below).
    private static final TypeKind FORCE_LOAD_ORDER = Namespace.ROOT_DUPLICATE;


    /** No SubTypeWorkBench at all. */
    public static final SubTypeWorkBench<Object> NONE =
        new SubTypeWorkBench<Object> ( Type.NONE,
                                       TypeBuilder.NONE,
                                       new Symbol [] {} );

    /** The Type of all SubTypeWorkBenches. */
    @SuppressWarnings("unchecked") // Class<SubTypeWorkBench<?>> cast
    public static final Type<SubTypeWorkBench<?>> TYPE =
        new StandardType<SubTypeWorkBench<?>> (
            Namespace.ROOT,              // parent_namespace
            Kind.ROOT,                   // kind
            "subtype_workbench",         // raw_type_name
            (Class<SubTypeWorkBench<?>>) SubTypeWorkBench.NONE.getClass (), // value_class
            SubTypeWorkBench.NONE,       // none
            new SymbolTable (),          // symbol_table
            new StandardMetadata () );   // metadata

    /** The type of all operations which can be used to build up a new
     *  sub-type for a parent Type. */
    public static final OperationType1<SubTypeWorkBench<?>, SubTypeWorkBench<?>> SUB_TYPE_TYPE =
        new OperationType1<SubTypeWorkBench<?>, SubTypeWorkBench<?>> (
            SubTypeWorkBench.TYPE,
            SubTypeWorkBench.TYPE );


    // The source parent Type which is being sub-Typed.
    private final Type<VALUE> parentType;

    // The builder being used to create the new sub-type.
    private final TypeBuilder<VALUE> typeBuilder;

    // Extra symbols to add to the new sub-type's symbol table.
    private final Symbol [] symbolsToAdd;

    // The new tag names to append to the sub-type's name,
    // enclosed in "[]".
    private final String tagNames;

    // The One newly built sub-Type, or null if it has
    // not yet been successfully built.
    private ZeroOrOne<Type<VALUE>> subType = null;


    /**
     * <p>
     * Creates a new SubTypeWorkBench for a sub-type operation to create
     * a new sub-type.
     * </p>
     *
     * @param parent_type The source parent Type which is being sub-Typed.
     *                    Must not be null.
     *
     * @param type_builder The builder being used to create the new sub-type.
     *                     Must not be null.
     *
     * @param symbols_to_add Extra symbols to add to the new sub-type's
     *                       symbol table.  Must not be null.
     */
    public SubTypeWorkBench (
                             Type<VALUE> parent_type,
                             TypeBuilder<VALUE> type_builder,
                             Symbol [] symbols_to_add
                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               parent_type, type_builder,
                               symbols_to_add );
        classContracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               symbols_to_add );

        this.parentType = parent_type;
        this.typeBuilder = type_builder;
        this.symbolsToAdd = symbols_to_add;
        this.tagNames = SubTypeWorkBench.generateTagNames ( symbols_to_add );
    }


    /**
     * <p>
     * Generates the tag names to be enclosed in "[]" for the new
     * sub-type (along with the tag names of the parent type).
     * </p>
     *
     * <p>
     * For example, a "number" type might have tags
     * "positive,odd" to create TypeID
     * "number[positive,odd]".  Or a "string" type might
     * have tags "lowercase,length(1,40)" to create
     * TypeID "string[lowercase,length(1,40)]".
     * </p>
     *
     * @param symbols The Symbols being added to the symbols of the
     *                parent type to create the sub-type.
     *                Tag names are added to a comma-separated
     *                String.  Must not be null.  Must not
     *                contain any null elements.
     */
    protected static final String generateTagNames (
                                                    Symbol [] symbols
                                                    )
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first_tag = true;
        for ( Symbol symbol : symbols )
        {
            if ( ! ( symbol instanceof Tag ) )
            {
                continue;
            }

            final Tag tag = (Tag) symbol;
            final TagID tag_id = tag.id ();

            if ( is_first_tag )
            {
                is_first_tag = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            if ( tag_id == null )
            {
                sbuf.append ( "null" );
            }
            else
            {
                sbuf.append ( tag_id.name () );
            }
        }

        final String tag_names = sbuf.toString ();
        return tag_names;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            // Any SubTypeWorkBench != null.
            return false;
        }
        else if ( object == this )
        {
            // Any SubTypeWorkBench == itself.
            return true;
        }
        else if ( ! ( object instanceof SubTypeWorkBench ) )
        {
            // Any SubTypeWorkBench != any other class of Object.
            return false;
        }

        final SubTypeWorkBench<?> that = (SubTypeWorkBench<?>) object;
        if ( ! this.parentType.equals ( that.parentType )
             || ! this.typeBuilder.equals ( that.typeBuilder )
             || this.symbolsToAdd.length != that.symbolsToAdd.length )
        {
            // Any SubTypeWorkBench with x,y,z
            // != any other SubTypeWorkBench of with a,b,c.
            return false;
        }

        // Order of symbols is important.
        for ( int s = 0; s < this.symbolsToAdd.length; s ++ )
        {
            if ( ! this.symbolsToAdd [ s ].equals ( that.symbolsToAdd [ s ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.typeBuilder.hashCode ()
            * this.symbolsToAdd.length;
    }


    /**
     * @return The source parent Type which is being sub-Typed.
     *         Never null.
     */
    public final Type<VALUE> parentType ()
        throws ReturnNeverNull.Violation
    {
        return this.parentType;
    }


    /**
     * <p>
     * Builds the, or returns the previously built, new sub-Type.
     * </p>
     *
     * @return The One new sub-Type, or No sub-Type if building failed.
     *         Never null.
     */
    public ZeroOrOne<Type<VALUE>> subType ()
    {
        final ZeroOrOne<Type<VALUE>> v_sub_type;
        if ( this.subType == null )
        {
            v_sub_type = this.typeBuilder.build ();
            if ( v_sub_type instanceof One )
            {
                this.subType = v_sub_type;
            }
            // Otherwise leave the subType as null, the caller might fix
            // the TypeBuilder's state and try again.
        }
        else
        {
            v_sub_type = this.subType;
        }

        return v_sub_type;
    }


    /**
     * @return The new tag names of the tags being added to the parent
     *         type to create a new sub-type.
     *         For example, a "number" type might have tags
     *         "positive,odd" to create TypeID
     *         "number[positive,odd]".  Or a "string" type might
     *         have tags "lowercase,length(1,40)" to create
     *         TypeID "string[lowercase,length(1,40)]".
     *         Never null.
     */
    public final String tagNames ()
    {
        return this.tagNames;
    }


    /**
     * @return The builder being used to create the new sub-type.
     *         Never null.
     */
    public final TypeBuilder<VALUE> typeBuilder ()
        throws ReturnNeverNull.Violation
    {
        return this.typeBuilder;
    }


    /**
     * @return Extra symbols to add to the new sub-type's symbol table.
     *         Never null.  Never contains any null elements.
     */
    public final Symbol [] symbolsToAdd ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return this.symbolsToAdd;
    }


    /**
     * @see java.lang.Object#toString
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "SubTypeWorkBench (parent type: " );
        sbuf.append ( this.parentType.id ().name () );
        sbuf.append ( " ) {" );
        boolean is_first = true;
        for ( Symbol symbol : this.symbolsToAdd )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " );
            sbuf.append ( symbol.id ().name () );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
