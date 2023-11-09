package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * A Type of Types.
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
public interface Kind
    extends Type<Type<?>>, SymbolType, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The root of all other Kinds. */
    public static final TypeKind ROOT =
        new TypeKind ( "kind" );

    /** No Kind at all in no typing environment. */
    public static final NoType<Type<?>> NONE =
        new NoType<Type<?>> ( "nokind" ); // Never put in Namespace.ROOT!


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public abstract Kind rename (
                                 String name,
                                 SymbolTable symbol_table
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates a new TypeBuilder to build a new Type.
     * </p>
     *
     * <p>
     * The TypeBuilder checks the constraints made by this Kind whenever
     * the caller executes its <code> build () </code> method.
     * </p>
     *
     * <p>
     * The TypeBuilder is NOT thread-safe.
     * </p>
     *
     * <p>
     * For example, to build One new Type&lt;String&gt;:
     * </p>
     *
     * <pre>
     *     RootNamespace parent_namespace = Namespace.ROOT;
     *     final SymbolTable symbol_table = new SymbolTable ();
     *     Type<String> string_type =
     *         kind.typeBuilder ( String.class, symbol_table )
     *             .name ( "string" )
     *             .namespace ( parent_namespace )
     *             .none ( "" )
     *             .build ()
     *             .orNone ();
     *     symbol_table.add ( new Operation1<String, Integer> ( "cast" ) ... );
     * </pre>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @param value_class The class of values represented by the Type
     *                    to build.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for the
     *                     Type to build.  The caller may continue
     *                     to add to the SymbolTable after constructing
     *                     this StandardType, but is expected to cease
     *                     additions to the SymbolTable before anyone
     *                     begins using this type.
     *                     Must not be null.
     *
     * @return A new TypeBuilder for a Type instance of this Kind.
     *         Never null.
     */
    public abstract <NEW_TYPE_VALUE extends Object>
        TypeBuilder<NEW_TYPE_VALUE> typeBuilder (
                                                 Class<NEW_TYPE_VALUE> value_class,
                                                 SymbolTable symbol_table
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
