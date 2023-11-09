package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.DefaultValuesRegistry;
import musaico.foundation.value.Value;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * A Symbol which contains a SymbolTable of other symbols, such as
 * a generic Namespace, or a more specialized one, such as a Type or a Tag.
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
public interface Namespace
    extends Symbol, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** The root generic Namespace, parent of all other Namespaces. */
    public static final ToplevelNamespace ROOT =
        DefaultValuesRegistry.set ( new ToplevelNamespace () );

    /** The no generic Namespace.
     * Do not register NONE objects in any namespace. */
    public static final NoNamespace NONE = new NoNamespace ();

    /** The Type of all generic Namespaces (not RootNamespaces or Types
     *  and so on, though). */
    public static final NamespaceType TYPE =
        new NamespaceType ( "namespace",
                            new SymbolTable (),
                            new StandardMetadata () );

    /** Duplicate of Namespace.TYPE.  Exists for abstruse startup
     * logic reasons. */
    // Now that Namespace.TYPE exists, register it.
    public static final NamespaceType TYPE_DUPLICATE =
        Namespace.ROOT.registerSymbolType ( Namespace.TYPE.finishConstructing () );

    /** Duplicate of Kind.ROOT.  Exists for abstruse startup
     * logic reasons. */
    // Now that Type.NONE exists, register Kind.ROOT.
    public static final TypeKind ROOT_DUPLICATE =
        Namespace.ROOT.registerSymbolType ( Kind.ROOT );

    /** Duplicate of Type.NONE.  Exists for abstruse startup
     * logic reasons.
     * Do not register NONE objects in any namespace. */
    // Now that Kind.ROOT exists, register the no generic Type.
    public static final NoType<Object> NO_TYPE_DUPLICATE =
        Type.NONE.finishConstructing ();

    /** Duplicate of Kind.NONE.  Exists for abstruse startup
     * logic reasons.
     * Do not register NONE objects in any namespace. */
    // Now that Kind.ROOT exists, register the no generic Kind.
    public static final NoType<Type<?>> NO_KIND_DUPLICATE =
        Kind.NONE.finishConstructing ();


    /**
     * <p>
     * Returns true if this Namespace has a Symbol by the
     * specified identifier, false if not.
     * </p>
     *
     * @param id The SymbolID to look up.  Must not be null.
     *
     * @return True if this Namespace contains a Symbol identified
     *         by the specified identifier, false if not.
     */
    public abstract boolean containsSymbol (
                                            SymbolID<?> id
                                            )
        throws ParametersMustNotBeNull.Violation;


    /**
     * <p>
     * Looks up the specified class of metadatum, starting in
     * this Namespace's <code> metadata () </code>, then progressing
     * upwards through the metadata of the parent Namespaces.
     * </p>
     *
     * @param metadatum_class The class of metadatum to look up.
     *                        Must have a zero args "none"
     *                        constructor.  Must not be null.
     *
     * @return Either the One requested metadatum, or No metadatum
     *         if it does not exist in any of the searched Metadata.
     *         Never null.
     */
    public abstract <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> findMetadatum (
                                            Class<METADATUM> metadatum_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * @return The optional Metadata for this Namespace.
     *         Can be Metadata.NONE.  Never null.
     */
    public abstract Metadata metadata ()
        throws ReturnNeverNull.Violation;


    /**
     * @return All the contents of this Namespace's SymbolTable
     *         as one big String.  Never null.
     *
     * @see musaico.foundation.typing.SymbolTable#printSymbolTable()
     */
    public abstract String printSymbolTable ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Creates an exact copy of this Namespace, but with the specified
     * ID name and SymbolTable.
     * </p>
     *
     * @param name The name to use for the SymbolID of the Namespace
     *             to be created.  Must not be null.
     *
     * @param symbol_table The (blank or partially populated) SymbolTable
     *                     to use for the Namespace to be created.
     *                     Must not be null.
     *
     * @return A newly created duplicate of this Namespace,
     *         with the specified id name and SymbolTable.  Never null.
     */
    public abstract Namespace rename (
                                      String name,
                                      SymbolTable symbol_table
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Looks up the specified Symbol in the SymbolTable.
     * </p>
     *
     * @param id The unique identifier of the Symbol to look up,
     *           such as Operation "doSomething", or Tag "PrivateTag",
     *           or NamespaceID.PARENT, or TypeIDs.ROOT_TYPE, and so on.
     *           Must not be null.
     *
     * @return The requested Symbol, or No Symbol if it does
     *         not exist in thie SymbolTable.  Never null.
     */
    public abstract <SYMBOL extends Symbol>
        ZeroOrOne<SYMBOL> symbol (
                                  SymbolID<SYMBOL> id
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns all SymbolIDs of a specific Symbol Type from
     * the SymbolTable.
     * For example, to retrieve all Tag symbols IDs, call
     * <code> symbolIDs ( Tag.TYPE ) </code>.
     * </p>
     *
     * <p>
     * If no Symbol IDs of the requested Symbol Type are in
     * the SymbolTable then No SymbolID will be returned.
     * </p>
     *
     * @param symbol_type The Type of Symbols to return, such as
     *                    Type.KIND, Tag.TYPE,
     *                    an OperationType, Constraint.TYPE,
     *                    Cast.TYPE, and so on.
                          Must not be null.
     *
     * @return All Symbol IDs of the requested Symbol Type from the
     *         SymbolTable.  Can be empty.  Never null.  Never contains
     *         any null elements.
     */
    public abstract <SYMBOL extends Symbol>
        Value<SymbolID<SYMBOL>> symbolIDs (
                                           Type<SYMBOL> symbol_type
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns all Symbols of a specific Symbol Type from
     * the SymbolTable.
     * For example, to retrieve all Tags, call
     * <code> symbols ( Tag.TYPE ) </code>.
     * </p>
     *
     * <p>
     * No "private" Symbols are ever returned (those indexed by
     * an identifier beginning with "#").
     * </p>
     *
     * <p>
     * If no Symbols of the requested Symbol Type are in
     * the SymbolTable then No Symbol will be returned.
     * </p>
     *
     * @param symbol_type The Type of Symbols to return, such as
     *                    Type.KIND, Tag.TYPE,
     *                    an OperationType, Constraint.TYPE,
     *                    and so on.  Must not be null.
     *
     * @return All Symbols of the requested Symbol Type from the
     *         SymbolTable.  Can be empty.  Never null.  Never contains
     *         any null elements.
     */
    public abstract <SYMBOL extends Symbol>
        Value<SYMBOL> symbols (
                               Type<SYMBOL> symbol_type
                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns all Symbol Types of the Symbols stored
     * in the SymbolTable.
     * </p>
     *
     * @return All Symbol Types of the Symbols from the
     *         SymbolTable.  Can be empty.  Never null.
     *         Never contains any null elements.
     */
    public abstract Value<SymbolType> symbolTypes ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Returns the Type representing the specified value Class of objects.
     * </p>
     *
     * <p>
     * For example, <code> Namespace.ROOT.typeOf ( String.class ) </code>
     * will return a <code> Type<String> </code>.
     * </p>
     *
     * <p>
     * Typically only a RootNamespace will actually maintain a lookup
     * of Types by Classes.  Every other Namespace will forward the
     * request to its parent Namespace (NamespaceID.PARENT), possibly
     * all the way up the hierarchy of Namespaces to Namespace.ROOT.
     * </p>
     *
     * <p>
     * If a given RootNamespace does not have a Type registered for
     * the specified Class, it will forward the request to its parent
     * Namespace (if any).  Only from the top of the Namespace hierarchy
     * will a <code> NoType </code> be returned.
     * </p>
     *
     * <p>
     * In order to be useful, Types must actually be registered in a
     * RootNamespace (such as Namespace.ROOT).  It is the responsibility
     * of a Type provider to register it in a RootNamespace somewhere in
     * the hierarchy (typically, but not necessarily, Namespace.ROOT).
     * </p>
     *
     * @param value_class The value Class of the Type to return, such
     *                    as String.class or Integer.class or MyClass.class.
     *                    Must not be null.
     *
     * @return The Type representing the specified value Class of objects.
     *         Can be a NoType, if no Type has been registered in this
     *         RootNamespace for the specified Class.  Never null.
     */
    public abstract <VALUE extends Object>
        Type<VALUE> typeOf (
                            Class<VALUE> value_class
                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
