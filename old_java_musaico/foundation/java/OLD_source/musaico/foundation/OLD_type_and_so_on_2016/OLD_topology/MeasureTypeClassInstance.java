package musaico.foundation.topology;

import java.io.Serializable;

import java.math.BigDecimal;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.typing.typeclass.TypeClassInstance;


/**
 * <p>
 * The signature of a topological measure Type, specifying the add,
 * subtract and so on Operations provided by the Type.
 * </p>
 *
 *
 * <p>
 * In Java every TypeClassInstance must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.topology.MODULE#COPYRIGHT
 * @see musaico.foundation.topology.MODULE#LICENSE
 */
public class MeasureTypeClassInstance<POINT extends Serializable, MEASURE extends Serializable>
    extends TypeClassInstance<MEASURE>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The discrete unit size for this measure.
    private final MEASURE unit;

    // The <code> + ( MEASURE, MEASURE ) : MEASURE </code>
    // binary operation.
    private final Operation2<MEASURE, MEASURE, MEASURE> add;

    // The <code> -- ( MEASURE ) : MEASURE </code>
    // unary operation.
    private final Operation1<MEASURE, MEASURE> decrement;

    // The <code> / ( MEASURE, BigDecimal ) : MEASURE </code>
    // binary operation.
    private final Operation2<MEASURE, BigDecimal, MEASURE> divide;

    // The <code> ++ ( MEASURE ) : MEASURE </code>
    // unary operation.
    private final Operation1<MEASURE, MEASURE> increment;

    // The <code> % ( MEASURE, MEASURE ) : MEASURE </code>
    // binary operation.
    private final Operation2<MEASURE, MEASURE, MEASURE> modulo;

    // The <code> * ( MEASURE, BigDecimal ) : MEASURE </code>
    // binary operation.
    private final Operation2<MEASURE, BigDecimal, MEASURE> multiply;

    // The <code> / ( MEASURE, MEASURE ) : BigDecimal </code>
    // binary operation.
    private final Operation2<MEASURE, MEASURE, BigDecimal> ratio;

    // The <code> - ( MEASURE, MEASURE ) : MEASURE </code>
    // binary operation.
    private final Operation2<MEASURE, MEASURE, MEASURE> subtract;

    // The hash code of this MeasureTypeClassInstance.
    private final int hashCode;


    /**
     * <p>
     * Creates a new MeasureTypeClassInstance with the specified Type
     * providing the Symbols required by the MeasureTypeClass.
     * </p>
     *
     * @param type The Type of MEASURE values.  Must contain
     *             a Symbol for each SymbolID required by
     *             the MeasureTypeClass.  Must not be null.
     *
     * @throws TypingViolation If any of the SymbolIDs required by
     *                         the TypeClass does not exist in the
     *                         specified Type.
     */
    public MeasureTypeClassInstance (
                                     Type<MEASURE> type
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        this ( MeasureTypeClass.TYPE_CLASS, type );
    }


    /**
     * <p>
     * Creates a new MeasureTypeClassInstance with the specified Type
     * providing the Symbols required by the specified MeasureTypeClass.
     * </p>
     *
     * @param measure_type_class The TypeClass representing all Types of
     *                           MEASURE values.  Must not be null.
     *
     * @param type The Type of MEASURE values.  Must contain
     *             a Symbol for each SymbolID required by
     *             the MeasureTypeClass.  Must not be null.
     *
     * @throws TypingViolation If any of the SymbolIDs required by
     *                         the TypeClass does not exist in the
     *                         specified Type.
     */
    public MeasureTypeClassInstance (
                                     MeasureTypeClass measure_type_class,
                                     Type<MEASURE> type
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        super ( measure_type_class, type );

        int hash_code = type.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.add = this.symbol ( MeasureTypeClass.ADD );
        hash_code += this.add.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.decrement = this.symbol ( MeasureTypeClass.DECREMENT );
        hash_code += this.decrement.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.divide = this.symbol ( MeasureTypeClass.DIVIDE );
        hash_code += this.divide.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.increment = this.symbol ( MeasureTypeClass.INCREMENT );
        hash_code += this.increment.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.modulo = this.symbol ( MeasureTypeClass.MODULO );
        hash_code += this.modulo.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.multiply = this.symbol ( MeasureTypeClass.MULTIPLY );
        hash_code += this.multiply.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.ratio = this.symbol ( MeasureTypeClass.RATIO );
        hash_code += this.ratio.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.subtract = this.symbol ( MeasureTypeClass.SUBTRACT );
        hash_code += this.subtract.hashCode ();

        this.hashCode = hash_code;
    }


    /**
     * <p>
     * Creates a new "none" MeasureTypeClassInstance, the specified
     * Type does not implement the MeasureTypeClass.
     * </p>
     */
    protected MeasureTypeClassInstance (
                                        Type<MEASURE> type,
                                        String ignore_me
                                        )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type );
    }


    /**
     * @return The <code> + ( MEASURE, MEASURE ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<MEASURE, MEASURE, MEASURE> add ()
    {
        return this.add;
    }


    /**
     * @return The <code> -- ( MEASURE ) : MEASURE </code>
     *         unary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation1<MEASURE, MEASURE> decrement ()
    {
        return this.decrement;
    }


    /**
     * @return The <code> / ( MEASURE, BigDecimal ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<MEASURE, BigDecimal, MEASURE> divide ()
    {
        return this.divide;
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
            // Any MeasureTypeClassInstance != null.
            return false;
        }
        else if ( object == this )
        {
            // Any MeasureTypeClassInstance == itself.
            return true;
        }
        else if ( ! ( object instanceof MeasureTypeClassInstance ) )
        {
            // Any MeasureTypeClassInstance != any other object.
            return false;
        }
        else if ( this.hashCode () != object.hashCode () )
        {
            // Any MeasureTypeClassInstance with hash code H1
            //     != any MeasureTypeClassInstance with hash code H2.
            return false;
        }

        final MeasureTypeClassInstance<?, ?, ?> that =
            (MeasureTypeClassInstance<?, ?, ?>) object;
        if ( ! this.type ().equals ( that.type () ) )
        {
            // Any MeasureTypeClassInstance signifying Type X
            // != any MeasureTypeClassInstance signifying Type Y.
            return false;
        }
        else if ( ! this.add ().id ().equals ( that.add ().id () )
                  || ! this.decrement ().id ().equals ( that.decrement ().id () )
                  || ! this.increment ().id ().equals ( that.increment ().id () )
                  || ! this.modulo ().id ().equals ( that.modulo ().id () )
                  || ! this.subtract ().id ().equals ( that.subtract ().id () ) )
        {
            // Any MeasureTypeClassInstance with operations A, B, C
            // != any MeasureTypeClassInstance with operations X, Y, Z.
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @return The <code> ++ ( MEASURE ) : MEASURE </code>
     *         unary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation1<MEASURE, MEASURE> increment ()
    {
        return this.increment;
    }


    /**
     * @return The <code> % ( MEASURE, MEASURE ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<MEASURE, MEASURE, MEASURE> modulo ()
    {
        return this.modulo;
    }


    /**
     * @return The <code> * ( MEASURE, BigDecimal ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<MEASURE, BigDecimal, MEASURE> multiply ()
    {
        return this.multiply;
    }


    /**
     * @return The "no measure" measure.  For example, 0 in an array universe,
     *         or [0, 0, 0, 0] in a vector universe, and so on.
     *         Convenience method for <code> type ().none () <code>.
     */
    public final MEASURE none ()
    {
        return this.type ().none ();
    }


    /**
     * @return The <code> / ( MEASURE, MEASURE ) : BigDecimal </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<MEASURE, MEASURE, BigDecimal> ratio ()
    {
        return this.ratio;
    }


    /**
     * @return The <code> - ( MEASURE, MEASURE ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<MEASURE, MEASURE, MEASURE> subtract ()
    {
        return this.subtract;
    }


    /**
     * @return The discrete unit size for this measure, such as
     *         1 in an integral measure space, or 1/44100 in a
     *         digital audio measure space, and so on.
     *         Never null.
     */
    public final MEASURE unit ()
    {
        return this.unit;
    }
}
