package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;


/**
 * <p>
 * A Tag with a specific contract which must be adhered to by
 * every Type that has the Tag.
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
public abstract class AbstractTagWithTypeConstraint
    extends AbstractTag
    implements Contract<Type<?>, TypingViolation>, TagWithTypeConstraint, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Creates a new AbstractTagWithTypeConstraint with the specified name,
     * to be used for a unique Tag identifier in SymbolTables.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     AbstractTagWithTypeConstraint, but is expected
     *                     to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     */
    public AbstractTagWithTypeConstraint (
                                          Namespace parent_namespace,
                                          String name,
                                          SymbolTable symbol_table
                                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               name,
               symbol_table,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new AbstractTagWithTypeConstraint with the specified name,
     * to be used for a unique Tag identifier in SymbolTables, and metadata.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param name The name for a unique identifier of this Tag.  Every Tag
     *             must have a unique ID within each SymbolTable.
     *             Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     AbstractTagWithTypeConstraint,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this AbstractTagWithTypeConstraint.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public AbstractTagWithTypeConstraint (
                                          Namespace parent_namespace,
                                          String name,
                                          SymbolTable symbol_table,
                                          Metadata metadata
                                          )
        throws ParametersMustNotBeNull.Violation
    {
        this ( parent_namespace,
               new TagID ( name ),
               symbol_table,
               metadata );
    }


    /**
     * <p>
     * Creates a new AbstractTagWithTypeConstraint with the specified
     * unique identifier for the tag's typing environment.
     * </p>
     *
     * @param parent_namespace The Namespace in whose SymbolTable this
     *                         Tag will eventually reside.
     *                         The parent Namespace is used, for example,
     *                         to look up the Type corresponding to a Class.
     *                         Must not be null.
     *
     * @param id The unique identifier of this Tag.  Every Tag
     *           must have a unique ID within each SymbolTable.
     *           Must not be null.
     *
     * @param symbol_table The Operations, Constants and so on for this
     *                     Tag.  The caller may continue to add to the
     *                     SymbolTable after constructing this
     *                     AbstractTagWithTypeConstraint,
     *                     but is expected to cease additions to the
     *                     SymbolTable before anyone begins using this tag.
     *                     Must not be null.
     *
     * @param metadata The Metadata for this AbstractTagWithTypeConstraint.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public AbstractTagWithTypeConstraint (
                                          Namespace parent_namespace,
                                          TagID id,
                                          SymbolTable symbol_table,
                                          Metadata metadata
                                          )
        throws ParametersMustNotBeNull.Violation
    {
        super ( parent_namespace, id, symbol_table, metadata );
    }


    // Every AbstractTagWithTypeConstraint must implement
    // the un-implemented methods
    // of Symbol, Namespace and Tag, such as rename (),
    // as well as Filter.filter ().


    /**
     * @see musaico.foundation.typing.TagWithTypeConstraint#typeConstraint()
     */
    @Override
    public final Contract<Type<?>, TypingViolation> typeConstraint ()
    {
        return this;
    }

    /**
     * <p>
     * Convenience method.  Creates a violation of this contract,
     * then attaches the specified root cause throwable.
     * </p>
     *
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object, java.lang.Object)
     */
    public final TypingViolation violation (
                                            Object plaintiff,
                                            Type<?> inspectable_data,
                                            Throwable cause
                                            )
    {
        final TypingViolation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        violation.initCause ( cause );

        return violation;
    }

    /**
     * @see musaico.foundation.contract.Contract#violation(java.lang.Object,java.lang.Object)
     */
    @Override
    public TypingViolation violation (
                                      Object plaintiff,
                                      Type<?> inspectable_data
                                      )
    {
        return new TypingViolation ( this,
                                     Contracts.makeSerializable ( plaintiff ),
                                     Contracts.makeSerializable ( inspectable_data ) );
    }
}
