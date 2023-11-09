package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.No;
import musaico.foundation.value.One;
import musaico.foundation.value.ZeroOrOne;


/**
 * <p>
 * The input to, and output from, a Mutate operation, which transforms
 * Symbols during calls to <code> Type.subType () </code>.
 * </p>
 *
 * <p>
 * A Mutation embodies one Symbol, input to / output from one or more
 * mutate Operations.  Each operation can pass through the input un-changed,
 * or mutate the symbol in some way to output a different result.
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
public class Mutation
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Mutation.class );

    // Duplicate of Kind.ROOT.  Exists for abstruse startup
    // logic reasons (to make sure Namespace gets loaded before Type
    // before we create Mutation.NONE below).
    private static final TypeKind FORCE_LOAD_ORDER = Namespace.ROOT_DUPLICATE;


    /** No Mutation at all. */
    public static final Mutation NONE =
        new Mutation (
            SymbolID.NONE,
            new No<Symbol> (
                Symbol.class,
                new SymbolMustBeInTable ( new SymbolTable () ).violation (
                    Mutation.class, // plaintiff
                    SymbolID.NONE ) ) );

    /** The Type of all Mutations. */
    public static final Type<Mutation> TYPE =
        new StandardType<Mutation> (
            Namespace.ROOT,              // parent_namespace
            Kind.ROOT,                   // kind
            "mutation",                  // raw_type_name
            Mutation.class,              // value_class
            Mutation.NONE,               // none
            new SymbolTable (),          // symbol_table
            new StandardMetadata () );   // metadata

    /** The type of all operations which can be used to mutate the
     *  symbols of a parent Type into the symbols of a sub-Type. */
    public static final OperationType1<Mutation, Mutation> MUTATE_TYPE =
        new OperationType1<Mutation, Mutation> (
            Mutation.TYPE,
            Mutation.TYPE );


    // The symbol ID to mutate.
    private final SymbolID<Symbol> symbolID;

    // The mutated or un-mutated symbol (if any).
    private final ZeroOrOne<Symbol> symbol;


    /**
     * <p>
     * Creates a new Mutation with the specified symbol ID and
     * one corresponding Symbol.
     * </p>
     *
     * @param symbol_id The unique identifier of the Symbol being mutated.
     *                  Must not be null.
     *
     * @param symbol The one Symbol being mutated.  Must not be null.
     */
    public Mutation (
                     SymbolID<Symbol> symbol_id,
                     Symbol symbol
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( symbol_id,
               new One<Symbol> ( Symbol.class,
                                 symbol ) );
    }


    /**
     * <p>
     * Creates a new Mutation with the specified symbol ID and
     * either One corresponding Symbol, or No corresponding Symbol.
     * </p>
     *
     * @param symbol_id The unique identifier of the Symbol being mutated.
     *                  Must not be null.
     *
     * @param symbol Either the One Symbol being mutated,
     *               or No Symbol.  Must not be null.
     */
    public Mutation (
                     SymbolID<Symbol> symbol_id,
                     ZeroOrOne<Symbol> symbol
                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               symbol_id, symbol );

        this.symbolID = symbol_id;
        this.symbol = symbol;
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
            // Any Mutation != null.
            return false;
        }
        else if ( object == this )
        {
            // Any Mutation == itself.
            return true;
        }
        else if ( ! ( object instanceof Mutation ) )
        {
            // Any Mutation != any other class of Object.
            return false;
        }

        final Mutation that = (Mutation) object;
        if ( this.symbolID.equals ( that.symbolID )
             && this.symbol.equals ( that.symbol ) )
        {
            // Any Mutation of id X and symbol Y
            // == any other Mutation of id X and symbol Y.
            return true;
        }
        else
        {
            // Any Mutation of id X and symbol Y
            // != any other Mutation of id P and symbol Q.
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.symbolID.hashCode ();
    }


    /**
     * @return The unique identifier of the Symbol to be mutated.
     *         Never null.
     */
    public final SymbolID<Symbol> symbolID ()
        throws ReturnNeverNull.Violation
    {
        return this.symbolID;
    }


    /**
     * @return Either the One Symbol to be mutated, or No Symbol.
     *         Never null.
     */
    public final ZeroOrOne<Symbol> symbol ()
        throws ReturnNeverNull.Violation
    {
        return this.symbol;
    }


    /**
     * @see java.lang.Object#toString
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Mutation " );
        sbuf.append ( this.symbolID.name () );
        sbuf.append ( " : " );
        sbuf.append ( this.symbol );
        return sbuf.toString ();
    }
}
