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
 * The signature of a topological region Type, specifying the intersection,
 * union and so on Operations provided by the Type.
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
public class RegionTypeClassInstance<POINT extends Serializable, MEASURE extends Serializable>
    extends TypeClassInstance<Region<POINT, MEASURE>>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The <code> intersection ( Region, Region ) : Region </code>
    // binary operation.
    private final Operation2<Region<POINT, MEASURE>, Region<POINT, MEASURE>, Region<POINT, MEASURE>> intersection;

    // The hash code of this RegionTypeClassInstance.
    private final int hashCode;


    /**
     * <p>
     * Creates a new RegionTypeClassInstance with the specified operations.
     * </p>
     *
     * @param type The Type of Region values.  Must contain
     *             all of the Operation symbols below.
     *             Must not be null.
     *
     * @param intersection The <code> intersection ( Region, Region ) : Region </code>
     *                     binary operation.  Must not AlwaysFail.
     *                     Must not be null.
     *
     * @throws TypingViolation If any of the operations is not
     *                         found in the Type's symbol table,
     *                         or if any of the operations is an
     *                         AlwaysFail.
     */
    public RegionTypeClassInstance (
                                    Type<Region<POINT, MEASURE>> type,
                                    Operation2<Region<POINT, MEASURE>, Region<POINT, MEASURE>, Region<POINT, MEASURE>> intersection
                                    )
        throws ParametersMustNotBeNull.Violation,
               TypingViolation
    {
        super ( type );

        int hash_code = type.hashCode ();

        // Throws ParametersMustNotBeNull.Violation, TypingViolation:
        this.checkOperation ( intersection );
        this.intersection = intersection;
        hash_code += this.intersection.hashCode ();

        this.hashCode = hash_code;
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
            // Any RegionTypeClassInstance != null.
            return false;
        }
        else if ( object == this )
        {
            // Any RegionTypeClassInstance == itself.
            return true;
        }
        else if ( ! ( object instanceof RegionTypeClassInstance ) )
        {
            // Any RegionTypeClassInstance != any other object.
            return false;
        }
        else if ( this.hashCode () != object.hashCode () )
        {
            // Any RegionTypeClassInstance with hash code H1
            //     != any RegionTypeClassInstance with hash code H2.
            return false;
        }

        final RegionTypeClassInstance<?, ?, ?> that =
            (RegionTypeClassInstance<?, ?, ?>) object;
        if ( ! this.type ().equals ( that.type () ) )
        {
            // Any RegionTypeClassInstance signifying Type X
            // != any RegionTypeClassInstance signifying Type Y.
            return false;
        }
        else if ( ! this.intersection ().id ().equals ( that.intersection ().id () ) )
        {
            // Any RegionTypeClassInstance with operations A, B, C
            // != any RegionTypeClassInstance with operations X, Y, Z.
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
     * @return The <code> intersection ( Region, Region ) : Region </code>
     *         binary operation.  Never an AlwaysFail.
     *         Never null.
     */
    public final Operation2<Region<POINT, MEASURE>, Region<POINT, MEASURE>, Region<POINT, MEASURE>> intersection ()
    {
        return this.intersection;
    }
}
