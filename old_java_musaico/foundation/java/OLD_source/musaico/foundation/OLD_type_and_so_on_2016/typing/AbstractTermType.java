package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.No;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * The Type describing a Term, such as a Constant, an Expression,
 * a Variable, and so on.
 * </p>
 *
 * <p>
 * An AbstractTermType describes the actual Term, and
 * its <code> valueType () </code> describes the value Type of
 * the Term, such as a string Type, or an integer Type, and so on.
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
public abstract class AbstractTermType<TERM extends Term<VALUE>, VALUE extends Object>
    extends AbstractSymbolType<TERM>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AbstractTermType.class );


    // The unique identifier for this AbstractTermType.
    private final TypeID id;

    // Generates the none Term returned by AbstractTermType.none ().
    private final NoneGenerator<TERM> noneGenerator;


    /**
     * <p>
     * Creates a new AbstractTermType with the specified type name,
     * value Type, SymbolTable and Metadata.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "expression doSomething(1,2,3)"
     *                      or "constant 5", and so on.
     *                      Must not be null.
     *
     * @param none_generator When necessary, generates a "no Term" Term of
     *                       the right type, such as a Constant or an
     *                       Expression.  Used whenever
     *                       <code> AbstractTermType.none () </code>
     *                       is invoked.  Must not be null.
     *
     * @param value_type The Type of Value for each Term of this Type.
     *                   For example, a string Type, or an integer Type,
     *                   and so on.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this AbstractTermType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public AbstractTermType (
                             String raw_type_name,
                             NoneGenerator<TERM> none_generator,
                             Type<VALUE> value_type,
                             SymbolTable symbol_table,
                             Metadata metadata
                             )
        throws ParametersMustNotBeNull.Violation
    {
        super ( raw_type_name, symbol_table, metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               raw_type_name,
                               none_generator,
                               value_type,
                               symbol_table,
                               metadata );

        this.id = new TypeID ( raw_type_name,       // raw type name
                               "",                  // tag_names
                               Visibility.PUBLIC ); // visibility

        this.noneGenerator = none_generator;

        final TypeID value_type_id = new TypeID ( "#value" );
        this.symbolTable ().set ( value_type_id, value_type );

        this.symbolTable ().set ( NamespaceID.PARENT, Namespace.NONE );
    }


    /**
     * @see musaico.foundation.typing.Symbol#id()
     */
    @Override
    public final TypeID id ()
        throws ReturnNeverNull.Violation
    {
        return this.id;
    }


    /**
     * @see musaico.foundation.typing.Type#none()
     */
    @Override
    public final TERM none ()
        throws ReturnNeverNull.Violation
    {
        return this.noneGenerator.none ();
    }


    // Every AbstractTermType must implement rename ( String ).

    // Every AbstractTermType must implement rename ( String, SymbolTable ).


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return this.id ().name ();
    }


    // Every AbstractTermType must implement valueClass ().


    /**
     * @return The Type of values for Terms of this type, such as
     *         a string Type, or an integer Type, and so on.
     *         Never null.
     */
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<VALUE>
    public final Type<VALUE> valueType ()
    {
        final TypeID value_type_id = new TypeID ( "#value" );
        final Type<?> value_type =
            this.symbolTable ().symbol ( value_type_id )
                .orNone ();

        return (Type<VALUE>) value_type;
    }
}
