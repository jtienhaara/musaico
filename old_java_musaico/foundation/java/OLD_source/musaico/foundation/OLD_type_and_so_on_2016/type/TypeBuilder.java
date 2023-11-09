package musaico.foundation.type;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.string.NotEmptyString;

import musaico.foundation.filter.Domain;

import musaico.foundation.graph.Graph;

import musaico.foundation.state.StateGraphBuilder;
import musaico.foundation.state.Tape;
import musaico.foundation.state.TapeMachine;
import musaico.foundation.state.Transition;

import musaico.foundation.value.Value;
import musaico.foundation.value.ValueClass;

import musaico.foundation.value.classes.StandardValueClass;

import musaico.foundation.value.contracts.ValueMustEqual;

import musaico.foundation.value.finite.One;


/**
 * <p>
 * !!!
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
public class TypeBuilder<VALUE extends Object>
    extends StateGraphBuilder
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final StateGraphBuilder ROOT_NAMESPACE =
        new StateGraphBuilder ( "/" ); // Mutable (can acquire children etc).

    /** Every type has a SYMBOL_TABLE node.  Its children are the
     *  children of the type-as-namespace. */
    public static final Value<?> SYMBOL_TABLE =
            new One<String> (
                StandardValueClass.STRING, // value_class
                "symbol_table" );

    public static final Graph<Value<?>, Transition> NONE_TYPE =
        new TypeBuilder<Graph<Value<?>, Transition>> (
            TapeMachine.GRAPH_VALUE_CLASS,
            "none",
            ROOT_NAMESPACE )
        .build (); // Immutable (no children, and so on).


    // The ValueClass for the type being built, such as String values for
    // a text type, or Number values for a numeric type, and so on.
    private final ValueClass<VALUE> valueClass;

    // The parent namespace of this type builder.
    private final StateGraphBuilder parentNamespace;


    public TypeBuilder (
                        ValueClass<VALUE> value_class,
                        String name,
                        StateGraphBuilder parent_namespace
                        )
    {
        super ( name );

        this.valueClass = value_class;
        this.parentNamespace = parent_namespace;

        // Pass through a Value that meets the type contract, as-is.
        this.fromEntry ()
            .on ( value_class.valueContract () )
            .copy ( Tape.INPUT, Tape.OUTPUT )
            .toExit ();

        // Symbol table contains child elements of this namespace.
        this.fromEntry ()
            .on ( "/" )
            .to ( TypeBuilder.SYMBOL_TABLE );

        // Begin constructing a new sub-type, such as
        // text[uppercase] or number[int][0-9] and so on.
        this.fromEntry ()
            .on ( "[" )
            .to ( "sub_start" );
        this.from ( "sub_start" )
            .on ( NotEmptyString.DOMAIN )
            // !!! .copy Tape.INPUT, !!! )
            .to ( "sub_end" );
        this.from ( "sub_end" )
            .on ( "[" ) // Add another bracketed kind to the sub-type?
            .to ( "sub_start" );
        this.from ( "sub_end" )
            .on ( "]" )
            .toExit ();

        // Reset the starting position so the caller can invoke on ().
        this.fromEntry ();

        // Now add the new type to its parent namespace.
        this.parentNamespace
            .from ( TypeBuilder.SYMBOL_TABLE )
                .on ( name )
                .to ( this );
        this.parentNamespace.fromEntry (); // Reset to default starting point.
    }


    public TypeBuilder<VALUE> add (
                                   Graph<Value<?>, Transition> symbol_type,
                                   Graph<Value<?>, Transition> symbol
                                   )
    {
        final One<Graph<Value<?>, Transition>> v_symbol_type =
            new One<Graph<Value<?>, Transition>> (
                TapeMachine.GRAPH_VALUE_CLASS,
                symbol_type );

        final ValueMustEqual input_symbol_type_name =
            new ValueMustEqual ( symbol_type.entry () );

        this.from ( TypeBuilder.SYMBOL_TABLE )
            .on ( input_symbol_type_name )
            .to ( symbol );

        this.from ( symbol )
            .toExit (); // Automatically to the exit.

        return this;
    }
}
