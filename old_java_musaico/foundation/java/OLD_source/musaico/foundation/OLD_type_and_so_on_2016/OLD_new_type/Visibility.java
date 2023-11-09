package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * How to treat a SymbolID when performing various operations,
 * such as listing SymbolIDs in a SymbolTable, or looking up a Symbol
 * by ID, or deciding whether to modify a Symbol during a sub-typing
 * procedure, and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Visibility must be Serializable in order to play nicely
 * over RMI.
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
public class Visibility
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Visibility.class );


    /** No Visibility at all.  Not very useful. */
    public static final Visibility NONE =
        new Visibility (
                        "invisible", // name
                        "-",         // visibility_symbol
                        0,           // hash_code
                        false,       // is_contains_queryable
                        false,       // is_copyable
                        false,       // is_printable
                        false,       // is_queryable_by_id
                        false,       // is_queryable_ids_by_type
                        false,       // is_queryable_symbols_by_type
                        false );     // is_removable

    /** No Visibility at all.  Not very useful.
     *  (The same as Visibility.NONE.) */
    public static final Visibility INVISIBLE = Visibility.NONE;

    /** Private Visibility.  Can be manipulated from the outside,
     *  but only by those who already know the ID is there. */
    public static final Visibility PRIVATE =
        new Visibility (
                        "private",   // name
                        "#",         // visibility_symbol
                        1,           // hash_code
                        true,        // is_contains_queryable
                        true,        // is_copyable
                        true,        // is_printable
                        true,        // is_queryable_by_id
                        false,       // is_queryable_ids_by_type
                        false,       // is_queryable_symbols_by_type
                        true );      // is_removable

    /** Public Visibility.  Can be manipulated by anyone. */
    public static final Visibility PUBLIC =
        new Visibility (
                        "public",    // name
                        "+",         // visibility_symbol
                        2,           // hash_code
                        true,        // is_contains_queryable
                        true,        // is_copyable
                        true,        // is_printable
                        true,        // is_queryable_by_id
                        true,        // is_queryable_ids_by_type
                        true,        // is_queryable_symbols_by_type
                        true );      // is_removable


    // The name of this Visibility, such as "private" or "public".
    private final String name;

    // A single character symbol for this Visibility, such as "#" or "+".
    private final String visibilitySymbol;

    // The unique hash code of this Visibility.
    private final int hashCode;

    // Can anyone call SymbolTable.containsSymbol ( id with this visibility )?
    private final boolean isContainsQueryable;

    // Can anyone create a new SymbolTable as a copy of another, including
    // an id with this visibility?
    private final boolean isCopyable;

    // Should we print out a symbol whose id has this visibility
    // during a call to SymbolTable.printSymbolTable?
    private final boolean isPrintable;

    // Should SymbolTable.symbol ( id ) return a symbol uniquely identified
    // by an id with this visibility?
    private final boolean isQueryableByID;

    // Should SymbolTable.symbolIDs ( symbol_type )
    // return any ids with this visibility?
    private final boolean isQueryableIDsByType;

    // Should SymbolTable.symbols ( symbol_type )
    // return any symbols with this visibility?
    private final boolean isQueryableSymbolsByType;

    // Can anyone call SymbolTable.remove ( id with this visibility )?
    private final boolean isRemovable;


    /**
     * <p>
     * Creates a new Visibility with the specified settings.
     * </p>
     *
     * @param name The name of this Visibility, such as "private" or "public".
     *             Must not be null.
     *
     * @param visibility_symbol A single character symbol for
     *                          this new Visibility, such as "#" or "+".
     *                          Must not be null.
     *
     * @param hash_code The unique hash code of this Visibility.
     *
     * @param is_contains_queryable Can anyone call
     *                              <code> SymbolTable.containsSymbol ( id with this visibility ) </code>?
     *
     * @param is_copyable Can anyone create a new SymbolTable as a copy
     *                    of another, including an id with this visibility?
     *
     * @param is_printable Should we print out a symbol whose id
     *                     has this visibility during a call to
     *                     <code> SymbolTable.printSymbolTable </code>?
     *
     * @param is_queryable_by_id Should <code> SymbolTable.symbol ( id ) </code>
     *                           return a symbol uniquely identified
     *                           by an id with this visibility?
     *
     * @param is_queryable_ids_by_type Should <code> SymbolTable.symbolIDs () </code>
     *                                 return any ids with this visibility?
     *
     * @param is_queryable_symbols_by_type Should <code> SymbolTable.symbols ( symbol_type ) </code>
     *                                     return any symbols with
     *                                     this visibility?
     *
     * @param is_removable Can anyone call
     *                     <code> SymbolTable.remove ( id with this visibility ) </code>?
     *
     * <p>
     * Protected.  Each Visibility must be a publicly declared constant.
     * </p>
     */
    protected Visibility (
                          String name,
                          String visibility_symbol,
                          int hash_code,
                          boolean is_contains_queryable,
                          boolean is_copyable,
                          boolean is_printable,
                          boolean is_queryable_by_id,
                          boolean is_queryable_ids_by_type,
                          boolean is_queryable_symbols_by_type,
                          boolean is_removable
                          )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, visibility_symbol );

        this.name = name;
        this.visibilitySymbol = visibility_symbol;
        this.hashCode = hash_code;
        this.isContainsQueryable = is_contains_queryable;
        this.isCopyable = is_copyable;
        this.isPrintable = is_printable;
        this.isQueryableByID = is_queryable_by_id;
        this.isQueryableIDsByType = is_queryable_ids_by_type;
        this.isQueryableSymbolsByType = is_queryable_symbols_by_type;
        this.isRemovable = is_removable;
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
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @return Can anyone call
     *         <code> SymbolTable.containsSymbol ( id with this visibility ) </code>?
     */
    public boolean isContainsQueryable ()
    {
        return this.isContainsQueryable;
    }


    /**
     * @return Can anyone create a new SymbolTable as a copy
     *         of another, including an id with this visibility?
     */
    public boolean isCopyable ()
    {
        return this.isCopyable;
    }


    /**
     * @return Should we print out a symbol whose id
     *         has this visibility during a call to
     *         <code> SymbolTable.printSymbolTable </code>?
     */
    public boolean isPrintable ()
    {
        return this.isPrintable;
    }


    /**
     * @return Should <code> SymbolTable.symbol ( id ) </code>
     *         return a symbol uniquely identified
     *         by an id with this visibility?
     */
    public boolean isQueryableByID ()
    {
        return this.isQueryableByID;
    }


    /**
     * @return Should <code> SymbolTable.symbolIDs () </code>
     *         return any ids with this visibility?
     */
    public boolean isQueryableIDsByType ()
    {
        return this.isQueryableIDsByType;
    }


    /**
     * @return Should <code> SymbolTable.symbols ( symbol_type ) </code>
     *         return any symbols with this visibility?
     */
    public boolean isQueryableSymbolsByType ()
    {
        return this.isQueryableSymbolsByType;
    }


    /**
     * @return Can anyone call
     *         <code> SymbolTable.remove ( id with this visibility ) </code>?
     */
    public boolean isRemovable ()
    {
        return this.isRemovable;
    }


    /**
     * @return The name of this Visibility, such as "private" or "public".
     *         Never null.
     */
    public String name ()
        throws ReturnNeverNull.Violation
    {
        return this.name;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.name;
    }


    /**
     * @return A single character symbol for
     *         this new Visibility, such as "#" or "+".
     *         Never null.
     */
    public String visibilitySymbol ()
        throws ReturnNeverNull.Violation
    {
        return this.visibilitySymbol;
    }
}
