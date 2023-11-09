package musaico.foundation.topology;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.typing.Operation1;
import musaico.foundation.typing.Operation2;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;

import musaico.foundation.typing.typeclass.TypeClassInstance;


/**
 * <p>
 * The signature of a topological point Type, specifying the add,
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
public class PointTypeClassInstance<POINT extends Serializable, MEASURE extends Serializable>
    extends TypeClassInstance<POINT>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The maximum point possible.
    private final POINT max;

    // The minimum point possible.
    private final POINT min;

    // The sorting and comparison Order of points.
    private final Order<POINT> order;

    // The centre or origin point, from which most regions start.
    private final POINT origin;


    // The <code> + ( POINT, MEASURE ) : POINT </code>
    // binary operation.
    private final Operation2<POINT, MEASURE, POINT> add;

    // The <code> % ( POINT, MEASURE ) : MEASURE </code>
    // binary operation.
    private final Operation2<POINT, MEASURE, MEASURE> modulo;

    // The <code> ++ ( POINT ) : POINT </code>
    // unary operation.
    private final Operation1<POINT, POINT> next;

    // The <code> -- ( POINT ) : POINT </code>
    // unary operation.
    private final Operation1<POINT, POINT> previous;

    // The <code> - ( POINT, MEASURE ) : POINT </code>
    // binary operation.
    private final Operation2<POINT, MEASURE, POINT> subtractMeasure;

    // The <code> - ( POINT, POINT ) : MEASURE </code>
    // binary operation.
    private final Operation2<POINT, POINT, MEASURE> subtractPoint;

    // The <code> .. ( POINT, POINT ) : Region </code>
    // binary operation.
    private final Operation2<POINT, POINT, Region<POINT, MEASURE>> to;

    // The hash code of this PointTypeClassInstance.
    private final int hashCode;


    /**
     * <p>
     * Creates a new PointTypeClassInstance with the specified operations.
     * </p>
     *
     * @param type The Type of POINT values.  Must contain
     *             all of the Operation symbols below.
     *             Must not be null.
     *
     * @param min The minimum point possible.  Must not be null.
     *
     * @param max The maximum point possible.  Must not be null.
     *
     * @param order The sorting and comparison Order of points.
     *              Must not be null.
     *
     * @param origin The centre or origin point, from which
     *               most regions start.  For example, point 0 in an array,
     *               or point 1 in a 1-indexed list, or [0, 0, 0, 0] in a
     *               vector universe, and so on.  Must not be null.
     *
     * @param add The <code> + ( POINT, MEASURE ) : POINT </code>
     *            binary operation.  Must not AlwaysFail.
     *            Must not be null.
     *
     * @param modulo The <code> % ( POINT, MEASURE ) : MEASURE </code>
     *               binary operation.  Must not AlwaysFail.
     *               Must not be null.
     *
     * @param next The <code> ++ ( POINT ) : POINT </code>
     *             unary operation.  Must not AlwaysFail.
     *             Must not be null.
     *
     * @param previous The <code> -- ( POINT ) : POINT </code>
     *                 unary operation.  Must not AlwaysFail.
     *                 Must not be null.
     *
     * @param subtract_measure The <code> - ( POINT, MEASURE ) : POINT </code>
     *                         binary operation.  Must not AlwaysFail.
     *                         Must not be null.
     *
     * @param subtract_point The <code> - ( POINT, POINT ) : MEASURE </code>
     *                       binary operation.  Must not AlwaysFail.
     *                       Must not be null.
     *
     * @param to The <code> .. ( POINT, POINT ) : Region </code>
     *           binary operation.  Must not AlwaysFail.
     *           Must not be null.
     *
     * @throws TypingViolation If any of the operations is not
     *                         found in the Type's symbol table,
     *                         or if any of the operations is an
     *                         AlwaysFail.
     */
    public PointTypeClassInstance (
                                   Type<POINT> type,
                                   POINT min,
                                   POINT max,
                                   Order<POINT> order,
                                   POINT origin,
                                   Operation2<POINT, MEASURE, POINT> add,
                                   Operation2<POINT, MEASURE, MEASURE> modulo,
                                   Operation1<POINT, POINT> next,
                                   Operation1<POINT, POINT> previous,
                                   Operation2<POINT, MEASURE, POINT> subtract_measure,
                                   Operation2<POINT, POINT, MEASURE> subtract_point,
                                   Operation2<POINT, POINT, Region<POINT, MEASURE>> to
                                   )
        throws ParametersMustNotBeNull.Violation,
               TypingViolation
    {
        super ( type );

        int hash_code = type.hashCode ();

        this.min = min;
        hash_code += this.min.hashcode ();

        this.max = max;
        hash_code += this.max.hashcode ();

        this.order = order;
        hash_code += this.order.hashcode ();

        this.origin = origin;
        hash_code += this.origin.hashcode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( add );
        this.add = add;
        hash_code += this.add.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( modulo );
        this.modulo = modulo;
        hash_code += this.modulo.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( next );
        this.next = next;
        hash_code += this.next.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( previous );
        this.previous = previous;
        hash_code += this.previous.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( subtract_measure );
        this.subtractMeasure = subtract_measure;
        hash_code += this.subtractMeasure.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( subtract_point );
        this.subtractPoint = subtract_point;
        hash_code += this.subtractPoint.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( to );
        this.to = to;
        hash_code += this.to.hashCode ();

        this.hashCode = hash_code;
    }


    /**
     * @return The <code> + ( POINT, MEASURE ) : POINT </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<POINT, MEASURE, POINT> add ()
    {
        return this.add;
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
            // Any PointTypeClassInstance != null.
            return false;
        }
        else if ( object == this )
        {
            // Any PointTypeClassInstance == itself.
            return true;
        }
        else if ( ! ( object instanceof PointTypeClassInstance ) )
        {
            // Any PointTypeClassInstance != any other object.
            return false;
        }
        else if ( this.hashCode () != object.hashCode () )
        {
            // Any PointTypeClassInstance with hash code H1
            //     != any PointTypeClassInstance with hash code H2.
            return false;
        }

        final PointTypeClassInstance<?, ?, ?> that =
            (PointTypeClassInstance<?, ?, ?>) object;
        if ( ! this.type ().equals ( that.type () ) )
        {
            // Any PointTypeClassInstance signifying Type X
            // != any PointTypeClassInstance signifying Type Y.
            return false;
        }
        // Do comparisons instead of checking max.equals() or min.equals(),
        // because certain classes (e.g. BigDecimal) return eqyals by
        // comparison but false for equals ().
        // E.g. BigDecimal.ZERO.equals ( new BigDecimal("0.0") ) returns false.
        else if ( ! this.order.equals ( that.order () )
                  || ! this.order.compareValues ( this.max, that.max () ).isEqual ()
                  || ! this.order.compareValues ( this.min, that.min () ).isEqual ()
                  || ! this.order.compareValues ( this.origin, that.origin () ).isEqual () )
        {
            // Any PointTypeClassInstance with order, max, min, origin a, b, c, d
            // != any PointTypeClassInstance with order, max, min, origin w, x, y, z.
            return false;
        }
        else if ( ! this.add ().id ().equals ( that.add ().id () )
                  || ! this.modulo ().id ().equals ( that.modulo ().id () )
                  || ! this.next ().id ().equals ( that.next ().id () )
                  || ! this.previous ().id ().equals ( that.previous ().id () )
                  || ! this.subtractMeasure ().id ().equals ( that.subtractMeasure ().id () )
                  || ! this.subtractPoint ().id ().equals ( that.subtractPoint ().id () )
                  || ! this.to ().id ().equals ( that.to ().id () ) )
        {
            // Any PointTypeClassInstance with operations A, B, C
            // != any PointTypeClassInstance with operations X, Y, Z.
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
     * @return The maximum point possible.  Never null.
     */
    public final POINT max ()
        throws ReturnNeverNull.Violation
    {
        return this.max;
    }


    /**
     * @return The minimum point possible.  Never null.
     */
    public POINT min ()
        throws ReturnNeverNull.Violation
    {
        return this.min;
    }


    /**
     * @return The <code> % ( POINT, MEASURE ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<POINT, MEASURE, MEASURE> modulo ()
    {
        return this.modulo;
    }


    /**
     * @return The <code> ++ ( POINT ) : POINT </code>
     *         unary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation1<POINT, POINT> next ()
    {
        return this.next;
    }


    /**
     * @return The "no point" point.  For example, -1 in an array universe,
     *         or [-Inf, -Inf, -Inf, 0] in a vector universe, and so on.
     *         Convenience method for <code> type ().none () <code>.
     */
    public final POINT none ()
    {
        return this.type ().none ();
    }


    /**
     * @return The sorting and comparison Order of points.  Never null.
     */
    public final Order<POINT> order ()
        throws ReturnNeverNull.Violation
    {
        return this.order;
    }


    /**
     * @return The centre or origin point, from which most regions start.
     *         For example, point 0 in an array, or point 1 in a 1-indexed
     *         list, or [0, 0, 0, 0] in a vector universe, and so on.
     *         Never null.
     */
    public final POINT origin ()
        throws ReturnNeverNull.Violation
    {
        return this.origin;
    }


    /**
     * @return The <code> -- ( POINT ) : POINT </code>
     *         unary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation1<POINT, POINT> previous ()
    {
        return this.previous;
    }


    /**
     * @return The <code> - ( POINT, MEASURE ) : POINT </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<POINT, MEASURE, POINT> subtractMeasure ()
    {
        return this.subtractMeasure;
    }


    /**
     * @return The <code> - ( POINT, POINT ) : MEASURE </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<POINT, POINT, MEASURE> subtractPoint ()
    {
        return this.subtractPoint;
    }


    /**
     * @return The <code> .. ( POINT, POINT ) : Region </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<POINT, POINT, Region<POINT, MEASURE>> to ()
    {
        return this.to;
    }
}
