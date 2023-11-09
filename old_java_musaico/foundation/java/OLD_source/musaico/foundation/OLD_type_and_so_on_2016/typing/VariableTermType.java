package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.Origin;
import musaico.foundation.metadata.StandardMetadata;
import musaico.foundation.metadata.StandardOrigin;

import musaico.foundation.value.No;
import musaico.foundation.value.NoneGenerator;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;


/**
 * <p>
 * The Type describing a Variable.
 * </p>
 *
 * <p>
 * A VariableTermType describes the actual Variable, and
 * its <code> valueType () </code> describes the value Type of
 * the Variable, such as a string Type, or an integer Type, and so on.
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
public class VariableTermType<VALUE extends Object>
    extends AbstractTermType<Variable<VALUE>, VALUE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( VariableTermType.class );


    /**
     * <p>
     * Generates the "none" Variable for a Variable,
     * avoiding infinite recursion at constructor time.
     * </p>
     */
    public static class NoneVariableTermGenerator<NONE_VALUE extends Object>
        implements NoneGenerator<Variable<NONE_VALUE>>, Serializable
    {
        private static final long serialVersionUID =
            VariableTermType.serialVersionUID;

        // Lock critical sections on this token:
        private final Serializable lock = new String ();

        // The value Type for Variables of this
        // VariableTermType.
        private final Type<NONE_VALUE> valueType;

        // Generated once, return the same value after that.
        // MUTABLE.
        private Variable<NONE_VALUE> none = null;


        /**
         * <p>
         * Creates a new NoneGenerator for an VariableTermType.
         * </p>
         *
         * @param value_type The value Type of the VariableTermType.
         *                   Used to create the "none" value for the
         *                   "none" Variable.  Must not be null.
         */
        public NoneVariableTermGenerator (
                                          Type<NONE_VALUE> value_type
                                          )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   value_type );

            this.valueType = value_type;
        }

        /**
         * @see musaico.foundation.VALUE.NoneGenerator#none()
         */
        public final Variable<NONE_VALUE> none ()
            throws ReturnNeverNull.Violation
        {
            synchronized ( this.lock )
            {
                if ( this.none == null )
                {
                    final Unregistered unregistered_id =
                        new Unregistered ( SymbolID.NONE );
                    final TypingViolation violation =
                        SymbolMustBeRegistered.CONTRACT.violation ( this,
                                                                    unregistered_id );
                    final No<NONE_VALUE> no_value =
                        this.valueType.noValue ( violation );

                    this.none = new Variable<NONE_VALUE> ( this.valueType,
                                                           no_value );
                }

                return this.none;
            }
        }
    }




    /**
     * <p>
     * Creates a new VariableTermType with the specified value Type,
     * an empty SymbolTable, and StandardMetadata,
     * with an automatically generated name for this new
     * VariableTermType.
     * </p>
     *
     * @param value_type The Type of Value for each Variable
     *                   of this Type.  For example, a string Type,
     *                   or an integer Type, and so on.  Must not be null.
     */
    VariableTermType (
                      Type<VALUE> value_type
                      )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               new SymbolTable () );
    }


    /**
     * <p>
     * Creates a new VariableTermType with the specified value Type,
     * and SymbolTable, and StandardMetadata,
     * with an automatically generated name for this new
     * VariableTermType.
     * </p>
     *
     * @param value_type The Type of Value for each Variable
     *                   of this Type.  For example, a string Type,
     *                   or an integer Type, and so on.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     */
    VariableTermType (
                      Type<VALUE> value_type,
                      SymbolTable symbol_table
                      )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new VariableTermType with the specified value Type,
     * SymbolTable and Metadata,
     * with an automatically generated name for this new
     * VariableTermType.
     * </p>
     *
     * @param value_type The Type of Value for each Variable
     *                   of this Type.  For example, a string Type,
     *                   or an integer Type, and so on.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this VariableTermType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    VariableTermType (
                      Type<VALUE> value_type,
                      SymbolTable symbol_table,
                      Metadata metadata
                      )
        throws ParametersMustNotBeNull.Violation
    {
        this ( "variable(" + value_type.id ().name () + ")",
               value_type,
               symbol_table,
               metadata );
    }


    /**
     * <p>
     * Creates a new VariableTermType with the specified type name,
     * value Type, SymbolTable and Metadata.
     * </p>
     *
     * @param raw_type_name The name of this new Type.  The name will be
     *                      used to create a SymbolID for this Type.
     *                      For example, "expression doSomething(1,2,3)"
     *                      or "constant 5", and so on.
     *                      Must not be null.
     *
     * @param value_type The Type of Value for each Variable
     *                   of this Type.  For example, a string Type,
     *                   or an integer Type, and so on.  Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Type.  The caller may continue to add to the
     *                     SymbolTable after constructing this StandardType,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this type.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this VariableTermType.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public VariableTermType (
                             String raw_type_name,
                             Type<VALUE> value_type,
                             SymbolTable symbol_table,
                             Metadata metadata
                             )
        throws ParametersMustNotBeNull.Violation
    {
        super ( raw_type_name,
                new VariableTermType.NoneVariableTermGenerator<VALUE> ( value_type ),
                value_type,
                symbol_table,
                metadata );
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public final VariableTermType<VALUE> rename (
                                                 String name
                                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.rename ( name, new SymbolTable () );
    }


    /**
     * @see musaico.foundation.typing.Term#rename(java.lang.String, musaico.foundation.typing.SymbolTable)
     */
    @Override
        public VariableTermType<VALUE> rename (
                                               String name,
                                               SymbolTable symbol_table
                                               )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name, symbol_table );

        symbol_table.addAll ( this.symbolTable () );

        return new VariableTermType<VALUE> ( name,
                                             this.valueType (),
                                             symbol_table,
                                             this.metadata ().renew () );
    }


    // Ugly hack for @*%# generics.
    private static class Silly<TERM_CLASS>
    {
        @SuppressWarnings("unchecked")
        public Class<TERM_CLASS> getTermClass ()
        {
            return (Class<TERM_CLASS>) Variable.class;
        }
    }

    /**
     * @see musaico.foundation.typing.Type#valueClass()
     */
    @Override
        public final Class<Variable<VALUE>> valueClass ()
        throws ReturnNeverNull.Violation
    {
        return new Silly<Variable<VALUE>> ().getTermClass ();
    }
}
