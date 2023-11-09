package musaico.foundation.topology;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.Parameter4;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.AbstractTagWithTypeConstraint;
import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.NamespaceID;
import musaico.foundation.typing.NamespaceVisitor;
import musaico.foundation.typing.NamespaceWalker;
import musaico.foundation.typing.Retypable;
import musaico.foundation.typing.SubTypeSubstituteParentType;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.SymbolType;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.TypingViolation;
import musaico.foundation.typing.UnknownType;

import musaico.foundation.typing.typeclass.TypeClass;
import musaico.foundation.typing.typeclass.TypeClassInstance;
import musaico.foundation.typing.typeclass.TypeClassInstanceBuilder;

import musaico.foundation.value.No;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;


/**
 * <p>
 * Creates Topologies, one or more instances of a TopologyTypeClass per Type.
 * </p>
 *
 *
 * <p>
 * In Java every TypeClass must be Serializable in order to play nicely
 * over RMI.  However be warned that instances of any given TypeClassInstance
 * might contain non-Serializable Symbols.
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
public class TopologyBuilder<POINT extends Object, MEASURE extends Object>
    extends TypeClassInstanceBuilder<TopologyTypeClass, Topology<POINT, MEASURE>, Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( TopologyBuilder.class );


    // Ugly hack for @*%# generics.
    private static class Silly<TOPOLOGY_CLASS>
    {
        @SuppressWarnings("unchecked")
        public Class<TOPOLOGY_CLASS> getTopologyClass ()
        {
        System.out.println ( "!!! about to create topology builder" );
            return (Class<TOPOLOGY_CLASS>) Topology.class;
        }
    }


    /**
     * <p>
     * Creates a new TopologyBuilder, to create an instance
     * of the specified TopologyTypeClass, instantiated by the specified Type.
     * </p>
     *
     * @param type_class The TypeClass to be instantiated.
     *                   Must not be null.
     *
     * @param type The Type which will instantiate the TypeClass.
     *             Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<Object>,
        // cast Topology.class - Class<Topology<POINT, MEASURE>>.
    public TopologyBuilder (
                            TopologyTypeClass type_class,
                            Type<?> type
                            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type_class,
                (Type<Object>) type,
                new TopologyBuilder.Silly<Topology<POINT, MEASURE>> ()
                    .getTopologyClass () );
        System.out.println ( "!!! topology builder created" );
    }


    /**
     * <p>
     * Creates a new Topology for the Type,
     * with the specified <code> (required SymbolID --&gt; instance ) </code>
     * mapping.
     * </p>
     *
     * <p>
     * This create method can be overridden by derived TopologyBuilder
     * classes, in order to provide more specialized
     * Topologies.
     * </p>
     *
     * @param child_type_class_instances The <code> child TypeClass --&gt; child Topology </code>
     *                                   mapping for the new Topology.
     *                                   Must not be null.
     *                                   Must not contain any null
     *                                   keys or values.
     *
     * @param symbols_mapping The <code> (required SymbolID --&gt; Symbol instance ) </code>
     *                        mapping for the new Topology.
     *                        Must not be null.  Must not contain any null
     *                        keys or values.
     *
     * @return The new Topology for the Type and the TypeClass.  Never null.
     */
    @Override
    @SuppressWarnings("unchecked") // Cast Type<?> - Type<POINT>,
        // cast Type<?> - Type<MEASURE>.
    protected void addInstanceToBuilder (
            Map<TypeClass, TypeClassInstance> child_type_class_instances,
            Map<SymbolID<?>, Symbol> symbols_mapping
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts ().check ( ParametersMustNotBeNull.CONTRACT,
                                  child_type_class_instances,
                                  symbols_mapping );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  child_type_class_instances.keySet () );
        this.contracts ().check ( Parameter2.MustContainNoNulls.CONTRACT,
                                  child_type_class_instances.values () );
        this.contracts ().check ( Parameter3.MustContainNoNulls.CONTRACT,
                                  symbols_mapping.keySet () );
        this.contracts ().check ( Parameter3.MustContainNoNulls.CONTRACT,
                                  symbols_mapping.values () );

        final TopologyTypeClass topology_type_class =
            this.typeClass ();

        final TypeClass point_type_class =
            topology_type_class.pointTypeClass ();
        Type<POINT> point_type = (Type<POINT>)
            child_type_class_instances.get ( point_type_class )
                                      .instanceType ();

        final TypeClass measure_type_class =
            topology_type_class.measureTypeClass ();
        Type<MEASURE> measure_type = (Type<MEASURE>)
            child_type_class_instances.get ( measure_type_class )
                                      .instanceType ();

        try
        {
            final Topology<POINT, MEASURE> instance =
                new Topology<POINT, MEASURE> ( topology_type_class,
                                               this.type (),
                                               child_type_class_instances,
                                               symbols_mapping,
                                               new SymbolTable (),
                                               point_type,
                                               measure_type );
            this.valueBuilder ().add ( instance );
        }
        catch ( TypingViolation violation )
        {
            // Do not add the instance to the builder, since it
            // for some reason does not meet criteria.
        }
    }


    /**
     * <p>
     * Creates a none Topology  which always fails to return
     * any symbols or evaluate any operations from its TopologyTypeClass.
     * </p>
     *
     * <p>
     * This create method can be overridden by derived TopologyBuilder
     * classes, in order to provide more specialized "none"
     * Topologies.
     * </p>
     *
     * @return The none Topology for the Type and TypeClass.  Never null.
     */
    @Override
    protected Topology<POINT, MEASURE> createNone ()
        throws ReturnNeverNull.Violation
    {
        return new Topology<POINT, MEASURE> ( this.typeClass (),
                                              this.type () );
    }
}
