package musaico.foundation.typing;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.value.Asynchronous;
import musaico.foundation.value.AsynchronousResult;
import musaico.foundation.value.Blocking;
import musaico.foundation.value.NoneConstant;
import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Value;


/**
 * <p>
 * A Term whose Value is evaluated by passing the Value of particular
 * input Term to an Operation.
 * An Expression's value Type is the output Type of the Operation.
 * An Expression will not necessarily return the same Value each time
 * it is evaluated, depending on the Operation / Cast / and so on
 * which underpins it.
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
public class Expression<OUTPUT extends Object>
    extends AbstractTerm<ExpressionTermID<OUTPUT>, Expression<OUTPUT>, 
OUTPUT>
    implements Term<OUTPUT>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Expression.class );


    // The Operation to evaluate whenever this Expression's value ()
    // is requested.
    private final Operation<OUTPUT> operation;

    // The inputs to the operation to be evaluated.
    private final List<Term<?>> inputs;


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with StandardMetadata.
     * </p>
     *
     * <p>
     * The new Expression will have a default, generated Term ID.
     * </p>
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     */
    public Expression (
                       Operation<OUTPUT> operation,
                       Term<?> ... inputs
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( operation,
               OperationType.createList ( inputs ),
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with StandardMetadata.
     * </p>
     *
     * <p>
     * The new Expression will have a default, generated Term ID.
     * </p>
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     *
     * @param metadata The Metadata which will track this Expression.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Expression (
                       Operation<OUTPUT> operation,
                       Metadata metadata,
                       Term<?> ... inputs
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( operation,
               OperationType.createList ( inputs ),
               metadata );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with StandardMetadata.
     * </p>
     *
     * <p>
     * The new Expression will have a default, generated Term ID.
     * </p>
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The list of inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     */
    public Expression (
                       Operation<OUTPUT> operation,
                       List<Term<?>> inputs
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( operation,
               inputs,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with the specified Metadata.
     * </p>
     *
     * <p>
     * The new Expression will have a default, generated Term ID.
     * </p>
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The list of inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     *
     * @param metadata The Metadata which will track this Expression.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Expression (
                       Operation<OUTPUT> operation,
                       List<Term<?>> inputs,
                       Metadata metadata
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( Expression.generateName ( operation, inputs ),
               operation,
               inputs,
               metadata );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with StandardMetadata.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     */
    public Expression (
                       String name,
                       Operation<OUTPUT> operation,
                       Term<?> ... inputs
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( name,
               operation,
               OperationType.createList ( inputs ),
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with StandardMetadata.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     *
     * @param metadata The Metadata which will track this Expression.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Expression (
                       String name,
                       Operation<OUTPUT> operation,
                       Metadata metadata,
                       Term<?> ... inputs
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( name,
               operation,
               OperationType.createList ( inputs ),
               metadata );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with StandardMetadata.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The list of inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     */
    public Expression (
                       String name,
                       Operation<OUTPUT> operation,
                       List<Term<?>> inputs
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        this ( name,
               operation,
               inputs,
               new StandardMetadata () );
    }


    /**
     * <p>
     * Creates a new Expression, an invocation of the
     * specified Operation, with the specified Metadata.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param operation The Operation to evaluate whenever this
     *                  Expression's value () method is called.
     *                  Must not be null.
     *
     * @param inputs The list of inputs to the Operation.
     *               Must not contain any null elements.  Must not be null.
     *
     * @param metadata The Metadata which will track this Expression.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Expression (
                       String name,
                       Operation<OUTPUT> operation,
                       List<Term<?>> inputs,
                       Metadata metadata
                       )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( new ExpressionTermID<OUTPUT> (
                    new ExpressionTermType<OUTPUT> (
                        operation == null
                            ? null
                            : operation.type () ),
                    name ),
                metadata );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation, inputs );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               inputs );

        this.operation = operation;
        this.inputs = new ArrayList<Term<?>> ( inputs );
    }




    /**
     * <p>
     * Creates an auto-generated name for an Expression with the
     * specified Operation and input Terms.
     * </p>
     */
    static protected final String generateName (
                                                Operation<?> operation,
                                                List<Term<?>> inputs
                                                )
    {
        if ( operation == null
             || inputs == null )
        {
            return "NULL Expression";
        }

        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( operation.id ().name () );
        sbuf.append ( " ( " );
        boolean is_first = true;
        for ( Term<?> input : inputs )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + input );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( ") : " );

        sbuf.append ( operation.outputType ().id ().name () );

        return sbuf.toString ();
    }




    /**
     * @see musaico.foundation.typing.Term#equalsTerm(musaico.foundation.typing.Term)
     */
    @Override
    protected final boolean equalsTerm (
                                        Expression<OUTPUT> that
                                        )
    {
        if ( this.operation.equals ( that.operation ) )
        {
            if ( this.inputs.size ()
                 != that.inputs.size () )
            {
                // Different # of inputs, even though the same
                // Operation is called in that Expression.
                return false;
            }

            int i = 0;
            for ( Term<?> this_input : this.inputs )
            {
                final Term<?> that_input = that.inputs.get ( i );
                if ( ! this_input.equals ( that_input ) )
                {
                    // One of the inputs to the Operation is different.
                    return false;
                }

                i ++;
            }

            // Don't care if that Expression has
            // different Metadata.
            // It's the same.
            return true;
        }
        else
        {
            // Another Expression with a different Operation.
            return false;
        }
    }


    /**
     * @return A copy of the inputs which will be passed to the Operation to be
     *         evaluated when this Expression's value () is requested.
     *         Never contains any null elements.  Never null.
     */
    public final List<Term<?>> inputs ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        return new ArrayList<Term<?>> ( this.inputs );
    }


    /**
     * @return The Operation which will be evaluated when this
     *         Expression's value () is requested.  Never null.
     */
    public final Operation<OUTPUT> operation ()
        throws ReturnNeverNull.Violation
    {
        return this.operation;
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    public Expression<OUTPUT> rename (
                                      String name
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Expression<OUTPUT> ( name,
                                        this.operation,
                                        this.inputs,
                                        this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast AbstractTermType - ExprTermType,
                                   // Term<?> - Term<OUTPUT>.
    public Expression<OUTPUT> retype (
                                      String name,
                                      AbstractTermType<? extends Term<OUTPUT>, OUTPUT> type
                                      )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        // We don't rely on AbstractSymbol.checkRetype () because we
        // need to do an extra step: check that tne specified Type
        // is itself the right class (ExpressionTermType).
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  name, type );

        final ExpressionTermType<OUTPUT> this_term_type =
            this.id ().type ();

        final TypesMustHaveSameValueClasses term_type_contract =
            new TypesMustHaveSameValueClasses ( this_term_type );
        this.contracts ().check ( term_type_contract,
                                  type );
        if ( ! ( type instanceof ExpressionTermType ) )
        {
            // No can do, the new type has to be an expression type.
            throw term_type_contract.violation ( this, type );
        }

        final ExpressionTermType<OUTPUT> that_expression_type =
            (ExpressionTermType<OUTPUT>) type;

        final Type<OUTPUT> value_type = this.valueType ();
        final Type<OUTPUT> retyped_value_type =
            that_expression_type.valueType ();

        final OperationType<?, OUTPUT> operation_type =
            this.type ().operationType ();
        final OperationType<?, OUTPUT> retyped_operation_type =
            that_expression_type.operationType ();
        final Operation<OUTPUT> retyped_operation =
            this.operation.retype ( this.operation.id ().name (),
                                    retyped_operation_type );

        final List<Term<?>> retyped_inputs =
            new ArrayList<Term<?>> ();
        for ( Term<?> input : this.inputs )
        {
            final Term<?> retyped_input;
            if ( this_term_type.equals ( input.id ().type () ) )
            {
                final Term<OUTPUT> input_to_retype =
                    (Term<OUTPUT>) input;
                retyped_input =
                    input_to_retype.retype ( name,
                                             that_expression_type );
            }
            else
            {
                retyped_input = input;
            }

            retyped_inputs.add ( retyped_input );
        }

        return new Expression<OUTPUT> ( name,
                                        retyped_operation,
                                        retyped_inputs,
                                        this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Symbol#type()
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<Symbol> - ExpressionTermType<V>
    public ExpressionTermType<OUTPUT> type ()
    {
        return this.id ().type ();
    }


    /**
     * @see musaico.foundation.typing.Term#value()
     */
    @Override
    public final Asynchronous<OUTPUT> value ()
        throws ReturnNeverNull.Violation
    {
        final Type<OUTPUT> output_type = this.valueType ();
        final Class<OUTPUT> output_class = output_type.valueClass ();
        final AsynchronousResult<OUTPUT> asynchronous_result =
            new AsynchronousResult<OUTPUT> ( output_class,
                                             Long.MAX_VALUE ); // Wait forever.
        final Blocking<OUTPUT> blocking_result =
            new Blocking<OUTPUT> ( asynchronous_result );

        final Asynchronous<OUTPUT> output =
            new Asynchronous<OUTPUT> ( blocking_result );

        // Now choose the "CPU" to execute this expression.
        final Processor processor =
            Processor.TOPLEVEL.delegateProcessingFor ( this );

        // Now start the Processor evaluating this Expression,
        // possibly in the foreground, possibly in the background.
        processor.process ( this, asynchronous_result );

        return output;
    }
}
