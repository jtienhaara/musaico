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

import musaico.foundation.value.Countable;
import musaico.foundation.value.One;
import musaico.foundation.value.Synchronicity;
import musaico.foundation.value.Synchronous;
import musaico.foundation.value.ValueMustNotBeEmpty;
import musaico.foundation.value.ZeroOrOne;


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
public class Points<POINT extends Object, MEASURE extends Object>
    extends Constant<POINT>
    implements Iterable<POINT>, Serializable
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
     * Creates a new Points with the specified Topology,
     * zero or more POINT elements, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param topology The Topology of the Points to create.
     *                 Must not be null.
     *
     * @param point Zero or more POINTs.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution from POINT ...
    public Points (
                   Topology<POINT, MEASURE> topology,
                   POINT ... points
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology.pointType (),
               points );
    }


    /**
     * <p>
     * Creates a new Points with the specified value Type,
     * zero or more POINT elements, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Points value to create.
     *                   Must not be null.
     *
     * @param points Zero or more POINTs.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution from POINT ...
    public Points (
                   Type<POINT> value_type,
                   POINT ... points
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               (Countable<POINT>) value_type.builder ().addAll ( points ).build (),
               Metadata.NONE );
    }


    /**
     * <p>
     * Creates a new Points with the specified Topology,
     * zero or more POINT elements, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param topology The Topology of the Points to create.
     *                 Must not be null.
     *
     * @param point Zero or more POINTs.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution from POINT ...
    public Points (
                   Topology<POINT, MEASURE> topology,
                   Iterable<POINT> points
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology.pointType (),
               points );
    }


    /**
     * <p>
     * Creates a new Points with the specified value Type,
     * zero or more POINT elements, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Points value to create.
     *                   Must not be null.
     *
     * @param points Zero or more POINTs.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Possible heap pollution from POINT ...
    public Points (
                   Type<POINT> value_type,
                   Iterable<POINT> points
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type,
               (Countable<POINT>) value_type.builder ().addAll ( points ).build (),
               Metadata.NONE );
    }


    /**
     * <p>
     * Creates a new Points with the specified Topology
     * and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param topology The Topology of the Points to create.
     *                 Must not be null.
     *
     * @param value The fixed value of this Points.  Must not be null.
     */
    public Points (
                   Topology<POINT, MEASURE> topology,
                   Countable<POINT> value
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( topology.pointType (),
               value );
    }


    /**
     * <p>
     * Creates a new Points with the specified value Type
     * and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Points value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Points.  Must not be null.
     */
    public Points (
                   Type<POINT> value_type,
                   Countable<POINT> value
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( value_type, value, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Points with the specified value Type
     * and non-blocking Value.
     * </p>
     *
     * <p>
     * The new Points will have a default, generated Term ID.
     * </p>
     *
     * @param value_type The Type of Points value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Points.  Must not be null.
     *
     * @param metadata The Metadata for this Points, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    public Points (
                   Type<POINT> value_type,
                   Countable<POINT> value,
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
     * Creates a new Points with the specified name,
     * value Type and non-blocking Value, and a StandardMetadata.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Points value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Points.  Must not be null.
     */
    public Points (
                   String name,
                   Type<POINT> value_type,
                   Countable<POINT> value
                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( name, value_type, value, Metadata.NONE );
    }



    /**
     * <p>
     * Creates a new Points with the specified value Type
     * and non-blocking Value.
     * </p>
     *
     * @param name The name which will be used to create a Term ID,
     *             unique within each SymbolTable.  Must not be null.
     *
     * @param value_type The Type of Points value to create.
     *                   Must not be null.
     *
     * @param value The fixed value of this Points.  Must not be null.
     *
     * @param metadata The Metadata for this Points, including modified
     *                 time, security settings, and so on.
     *                 Can be Metadata.NONE.  Must not be null.
     */
    @SuppressWarnings("unchecked") // Cast Op<A, B> - Op<VAL, VAL>.
    public Points (
                   String name,
                   Type<POINT> value_type,
                   Countable<POINT> value,
                   Metadata metadata
                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( name, value_type, value, metadata );
    }


    // A Constant with value V and so on is equal to a Points with value V.
    // No need to override equalsTerm().


    /**
     * @return The One very first POINT, or No POINT if these Points
     *         are empty.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<P> - Countable<P>.
    public final ZeroOrOne<POINT> first ()
        throws ReturnNeverNull.Violation
    {
        final Countable<POINT> points = (Countable<POINT>)
            this.value ().await ();

        return points.first ();
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    public final Iterator<POINT> iterator ()
        throws ReturnNeverNull.Violation
    {
        return this.value ().await ().iterator ();
    }


    /**
     * @return The One very last element of this Countable value,
     *         or No element if this value is empty.  Never null.
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<P> - Countable<P>.
    public final ZeroOrOne<POINT> last ()
        throws ReturnNeverNull.Violation
    {
        final Countable<POINT> points = (Countable<POINT>)
            this.value ().await ();

        return points.last ();
    }


    /**
     * @return The number of elements in this Countable value.
     *         Always 0 or greater.
     */
    @SuppressWarnings("unchecked") // Cast NonBlocking<P> - Countable<P>.
    public final long length ()
        throws Return.AlwaysGreaterThanOrEqualToZero.Violation
    {
        final Countable<POINT> points = (Countable<POINT>)
            this.value ().await ();

        return points.length ();
    }


    /**
     * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast NonBlocking<P> - Countable<P>.
    public Points<POINT, MEASURE> rename (
                                          String name
                                          )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               name );

        return new Points<POINT, MEASURE> ( name,
                                            this.valueType (),
                                            (Countable<POINT>) this.value ().await (),
                                            this.metadata ().orNone ().renew () );
    }


    /**
     * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast NonBlocking<P> - Countable<P>.
    public Points<POINT, MEASURE> retype (
                                          String name,
                                          AbstractTermType<? extends Term<POINT>, POINT> type
                                          )
        throws ParametersMustNotBeNull.Violation,
               TypesMustHaveSameValueClasses.Violation,
               ReturnNeverNull.Violation
    {
        this.checkRetype ( name, type );

        return new Points<POINT, MEASURE> ( name,
                                            type.valueType (),
                                            (Countable<POINT>) this.value ().await (),
                                            this.metadata ().orNone ().renew () );
    }
}
