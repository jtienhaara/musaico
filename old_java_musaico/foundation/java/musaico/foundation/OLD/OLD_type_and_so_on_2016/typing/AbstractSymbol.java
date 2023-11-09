package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.TrackingContracts;


/**
 * <p>
 * Implements boilerplate code for all Symbols (Types, Tags, Operations,
 * Constraints, and so on).
 * </p>
 *
 *
 * <p>
 * Every AbstractSymbol must implement rename ().
 * </p>
 *
 * @see musaico.foundation.typing.Symbol#rename(musaico.foundation.typing.SymbolID)
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
public abstract class AbstractSymbol<ID extends SymbolID<SYMBOL>, SYMBOL extends Symbol>
    implements Symbol, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Checks contracts on static methods and constructors for us.
    private final ObjectContracts classContracts =
        new ObjectContracts ( AbstractSymbol.class );


    // Checks method obligations and guarantees, tracks violations.
    private final TrackingContracts contracts;

    // The unique identifier for this symbol.
    private final ID id;


    /**
     * <p>
     * Creates a new AbstractSymbol with the specified identifier,
     * using its Type's Metadata to track contract Violations.
     * </p>
     *
     * @param id The identifier for this new symbol.  Ensures uniqueness
     *           within a SymbolTable.  Must not be null.
     */
    public AbstractSymbol (
                           ID id
                           )
    {
        this ( id,
               id.type ().metadata () );
    }


    /**
     * <p>
     * Creates a new AbstractSymbol with the specified identifier.
     * </p>
     *
     * @param id The identifier for this new symbol.  Ensures uniqueness
     *           within a SymbolTable.  Must not be null.
     *
     * @param metadata The Metadata which will be used to track violations
     *                 of this Symbol's Contracts.  If this Symbol has
     *                 its own Metadata (such as a Type, a Namespace or
     *                 a Tag), then its own Metadata must be passed in.
     *                 If this Symbol does not have its own Metadata
     *                 (such as an Operation or Constraint) then the
     *                 Metadata of its Type must be passed in.
     *                 Must not be null.
     */
    public AbstractSymbol (
                           ID id,
                           Metadata metadata
                           )
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               id, metadata );

        this.id = id;

        // Pass the original, non-wrapped Metadata to our
        // TrackingContracts.  Any time we want to directly read or write
        // the real Metadata for this Term, we can retrieve the
        // real Metadata from our TrackingContracts.
        this.contracts = new TrackingContracts ( this.id.name (),
                                                 metadata );
    }


    /**
     * <p>
     * Checks the specified Type to make sure it is compatible with
     * this Symbol's Type, during a retype () invocation.
     * </p>
     *
     * <p>
     * Retypable Symbols (such as Operations and Terms) can use
     * this method to ensure retype safety before proceeeding
     * with a retype () request.
     * </p>
     *
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     *
     * @param name The name being used for the retype.  Must not be null.
     *
     * @param type The Type to check.  Must not be null.
     *
     *
     * @throws TypesMustHaveSameValueClass.Violation If the specified
     *                                               Type cannot be used
     *                                               to retype this Symbol.
     */
    protected void checkRetype (
                                String name,
                                Type<? extends Symbol> type
                                )
            throws ParametersMustNotBeNull.Violation,
                   TypesMustHaveSameValueClasses.Violation,
                   ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, type );

        final TypesMustHaveSameValueClasses type_contract =
            new TypesMustHaveSameValueClasses ( this.type ().asType () );
        // Throws TypesMustHaveSameValueClass.Violation:
        this.contracts ().check ( type_contract, type );
    }


    /**
     * @return The ObjectContracts for this Symbol.
     *         Checks method obligations and guarantees, tracks violations.
     *         A Symbol with its own Metadata (such as a Type or a Namespace
     *         or a Tag) tracks its own violations.  A Symbol without
     *         Metadata (such as an Operation or a Constraint) uses its
     *         Type's Metadata to track violations.  Never null.
     */
    protected final TrackingContracts contracts ()
    {
        return this.contracts;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     * Final for speed.
     */
    @Override
    @SuppressWarnings("unchecked") // Try...catch is apparently unchecked.
    public final boolean equals (
                                 Object object
                                 )
    {
        if ( object == null )
        {
            // Any Symbol != null.
            return false;
        }
        else if ( object == this )
        {
            // Any Symbol == itself.
            return true;
        }
        else if ( ! ( object instanceof AbstractSymbol ) )
        {
            // Any Symbol != any other class of object.
            return false;
        }

        final AbstractSymbol<ID, SYMBOL> that_abstract_symbol =
            (AbstractSymbol<ID, SYMBOL>) object;

        if ( ! this.id.equals ( that_abstract_symbol.id ) )
        {
            // Symbol with id X != Symbol with id Y
            return false;
        }

        final SYMBOL that;
        try
        {
            that = (SYMBOL) object;
        }
        catch ( ClassCastException e )
        {
            // Symbol that can't be cast to our generic (Tag, Type,
            // Operation, and so on) is not equal to this.
            return false;
        }

        // Perform any symbol-specific extra checks.
        return this.equalsSymbol ( that );
    }


    /**
     * <p>
     * An optional refinement on the standard
     * AbstractSymbol#equals(java.lang.Object).
     * </p>
     *
     * <p>
     * Once <code> equals () </code> has determined that two AbstractSymbols
     * have the same class and SymbolID, this method is invoked to check
     * contents further.
     * </p>
     *
     * <p>
     * By default this method does nothing.  It can be overridden to
     * specify extra equals checks.  For example, a Type might compare
     * Tags, or an Operation might compare input and output Types,
     * and so on.
     * </p>
     *
     * @param symbol The Symbol of exactly the same class as this
     *               AbstractSymbol which will be compared.
     *               Must not be null.
     *
     * @return True if this equals that, false if not.
     */
    protected boolean equalsSymbol (
                                    SYMBOL that
                                    )
    {
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     *
     * Final for speed.
     */
    @Override
    public final int hashCode ()
    {
        return this.id.hashCode ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#id()
     *
     * Final for speed.
     */
    @Override
    public final ID id ()
        throws ReturnNeverNull.Violation
    {
        return this.id;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.id.toString ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#type()
     *
     * Cannot be final, since some Symbols return more specific
     * types than the generic SymbolType (such as Operation and Kind).
     */
    @Override
    public SymbolType type ()
    {
        return this.id.type ();
    }
}
