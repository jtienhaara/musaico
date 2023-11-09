package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.UncheckedViolation;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * No SymbolType at all.  Not very useful.
 * </p>
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
public class NoSymbolType
    extends AbstractSymbolType<Symbol>
    implements SymbolType, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NoSymbolType.class );


    /**
     * <p>
     * Creates a new NoSymbolType.
     * </p>
     *
     * <p>
     * Package-private.  For use by SymbolType.NONE only.
     * </p>
     *
     * @param id The SymbolID for this NoSymbolType.  Must not be null.
     */
    NoSymbolType ()
    {
        super ( "nosymbol",
                new SymbolTable (),
                Metadata.NONE );
    }


    /**
     * @see musaico.foundation.typing.Symbol#id()
     *
     * Final for speed.
     */
    @Override
    public final TypeID id ()
        throws ReturnNeverNull.Violation
    {
        return TypeIDs.NO_SYMBOL_TYPE;
    }


    /**
     * @see musaico.foundation.typing.Type#none()
     */
    @Override
    public final Symbol none ()
        throws ReturnNeverNull.Violation
    {
        return Type.NONE;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final NoSymbolType rename (
                                      String name
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Namespace#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
    public NoSymbolType rename (
                                String name,
                                SymbolTable symbol_table
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name, symbol_table );

        // There can only ever be one NoSymbolType per JVM, because
        // of the way this class is constructed to avoid load order issues.
        throw ReturnNeverNull.CONTRACT
            .violation ( this,
                         "Cannot rename NoSymbolType" );
    }


    /**
     * @see java.lang.Object#toString()
     *
     * Final for speed.
     */
    @Override
    public final String toString ()
    {
        return this.id ().name ();
    }


    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
    public final Class<Symbol> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return Symbol.class;
    }


    /**
     * @return The contract Violation which led to this NoSymbolType.
     *         Never null.
     */
    public TypingViolation violation ()
    {
        return
            SymbolMustBeRegistered.CONTRACT.violation (
                NoSymbolType.class,
                new Unregistered ( new SymbolID<Type<?>> ( Kind.ROOT,
                                                           "nosymbol",
                                                           Visibility.NONE ) ) );
    }
}
