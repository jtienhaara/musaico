package musaico.foundation.topology;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.AbstractTermType;
import musaico.foundation.typing.Constant;
import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;

import musaico.foundation.value.One;
import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Synchronous;
import musaico.foundation.value.ValueMustNotBeEmpty;


/**
 * <p>
 * A set of zero or more POINTs in a Topology.
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
public class Measure<POINT extends Object, MEASURE extends Object>
    extends Constant<MEASURE>
    implements Iterable<MEASURE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( Region.class );


    /**
     * <p>
     * Creates a new empty Measure with the specified value Topology,
     * and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param topology The Topology of the Measure value to create.
     *                 Must not be null.
     */
    public Measure (
                    Topology<POINT, MEASURE> topology
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology.measureType () );
    }


    /**
     * <p>
     * Creates a new empty Measure with the specified value Type,
     * and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Measure value to create.
     *                   Must not be null.
     */
    public Measure (
                    Type<MEASURE> value_type
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               value_type.none () ); // Create One value even for no Measure.
    }


    /**
     * <p>
     * Creates a new single-element Measure with the specified Topology,
     * the specified single MEASURE, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param topology The Topology of the Measure to create.
     *                 Must not be null.
     *
     * @param measure The single MEASURE.  Must not be null.
     */
    public Measure (
                    Topology<POINT, MEASURE> topology,
                    MEASURE measure
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology.measureType (),
               measure );
    }


    /**
     * <p>
     * Creates a new single-element Measure with the specified value Type,
     * the specified single MEASURE, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Measure value to create.
     *                   Must not be null.
     *
     * @param measure The single MEASURE.  Must not be null.
     */
    public Measure (
                    Type<MEASURE> value_type,
                    MEASURE measure
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               new One<MEASURE> ( value_type.valueClass (),
                                  measure ),
               Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Measure with the specified value Type
     * and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param topology The Topology of the Measure value to create.
     *                 Must not be null.
     *
     * @param value The fixed value of this Measure.  Must not be null.
     */
    public Measure (
                    Topology<POINT, MEASURE> topology,
                    One<MEASURE> value
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology.measureType (),
               value,
               Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Measure with the specified value Type
     * and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Measure value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Measure.  Must not be null.
     */
    public Measure (
                    Type<MEASURE> value_type,
                    One<MEASURE> value
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type, value, Metadata.NONE );
    }


    /**
     * <p>
     * Creates a new Measure with the specified value Type
     * and non-blocking Value.
     * </p>
     *
     * <p>
     * The new Measure will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Measure value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Measure.  Must not be null.
     *
     * @param metadata The Metadata for this Measure, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Measure (
                    Type<MEASURE> value_type,
                    One<MEASURE> value,
                    Metadata metadata
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( Constant.generateName ( value_type, value ),
               value_type,
               value,
               metadata );
    }


    /**
     * <p>
     * Creates a new Measure with the specified name,
     * value Type and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Measure value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Measure.  Must not be null.
     */
    public Measure (
                    String name,
                    Type<MEASURE> value_type,
                    One<MEASURE> value
                    )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, value_type, value, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Measure with the specified value Type
     * and non-blocking Value.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Measure value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Measure.  Must not be null.
     *
     * @param metadata The Metadata for this Measure, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Op<A, B> - Op<VAL, VAL>.
    public Measure (
                    String name,
                    Type<MEASURE> value_type,
                    One<MEASURE> value,
                    Metadata metadata
                    )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name, value_type, value, metadata );
    }


    // A Constant with value V and so on is equal to a Measure with value V.
    // No need to override equalsTerm().


    /**
     * @return The One very first MEASURE, or No MEASURE if these Measure
     *         are empty.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<M> - One<M>.
    public final One<MEASURE> first ()
        throws ReturnNeverNull.Violation
    {
        final One<MEASURE> maybe_measure = (One<MEASURE>)
            this.value ().await ();

        return maybe_measure.first ();
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    public final Iterator<MEASURE> iterator ()
        throws ReturnNeverNull.Violation
    {
        return this.value ().await ().iterator ();
    }


    /**
     * @return The One very last element of this One value,
     *         or No element if this value is empty.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<M> - One<M>.
    public final One<MEASURE> last ()
        throws ReturnNeverNull.Violation
    {
        final One<MEASURE> maybe_measure = (One<MEASURE>)
            this.value ().await ();

        return maybe_measure.last ();
    }


    /**
     * @return The number of elements in this One value.
     *         Always 1.
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<M> - One<M>.
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final One<MEASURE> maybe_measure = (One<MEASURE>)
            this.value ().await ();

        return maybe_measure.length ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast NonBlocking<M> - One<M>.
    public Measure<POINT, MEASURE> rename (
                                           String name
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Measure<POINT, MEASURE> ( name,
                                             this.valueType (),
                                             (One<MEASURE>) this.value ().await (),
                                             this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast NonBlocking<M> - One<M>.
    public Measure<POINT, MEASURE> retype (
                                           String name,
                                           AbstractTermType<? extends Term<MEASURE>, MEASURE> type
                                           )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Measure<POINT, MEASURE> ( name,
                                             type.valueType (),
                                             (One<MEASURE>) this.value ().await (),
                                             this.metadata ().orNone ().renew () );
    }
}
