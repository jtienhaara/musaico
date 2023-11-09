package musaico.foundation.buffer;


import java.io.Serializable;


import musaico.foundation.condition.Conditional;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.region.Position;
import musaico.foundation.region.Region;

import musaico.foundation.typing.Instance;
import musaico.foundation.typing.Typing;

import musaico.foundation.value.Value;


/**
 * <p>
 * Represents an array or data structure of Instance values.
 * </p>
 *
 * <p>
 * A Buffer is akin to a pointer to a data structure in C.
 * The Region of the Buffer defines its size and indices,
 * and each element is a typed Instance (which might in turn
 * also be a Buffer / pointer to another memory region).
 * </p>
 *
 * <p>
 * A Buffer can be used as a data structure, by getting and setting
 * fields with <code> get () </code> and <code> set () </code> methods,
 * passing in the appropriate Position from the data structure Region.
 * </p>
 *
 * <p>
 * A Buffer can also be used as a linear array, to step over the
 * Instances and operate on each one, the same way a region
 * of memory can be operated upon in C without any understanding
 * of the structure stored there.
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
 * @see musaico.foundation.buffer.MODULE#COPYRIGHT
 * @see musaico.foundation.buffer.MODULE#LICENSE
 */
public interface Buffer
    extends Value<Instance<?>, Typing.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No Buffer at all, always empty.  A bit like a NULL pointer. */
    public static final Buffer NONE =
        new NoBuffer ();


    /**
     * <p>
     * Returns the specified field from this buffer as an Instance of
     * some Type.
     * </p>
     *
     * @param position The position within this Buffer to retrieve.
     *                 Must be a position with this Buffer's Region.
     *                 Must not be null.
     *
     * @return The Successful Instance at the specified Position, which
     *         contains the type of field and the value(s) of the field,
     *         or a Failed Instance if the specified Position is invalid.
     *         Never null.
     */
    public abstract Conditional<Instance<?>, BufferViolation> get (
                                                                   Position position
                                                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a description of this Buffer's size and structure,
     * but NOT its content.
     * </p>
     *
     * A Buffer's Region may be passed around over RMI safely without
     * inducing serialization of the whole Buffer.
     * </p>
     *
     * <p>
     * The Region defines the start and end positions of this Buffer,
     * how to step through the Positions of its fields, how to search
     * and so on.
     * </p>
     *
     * <p>
     * The Region defined by the Buffer can be used to step through all
     * the fields of the Buffer.  For example:
     * </p>
     *
     * <pre>
     *     Buffer buffer = ... ;
     *     for ( Position position : buffer.region () )
     *     {
     *         Instance<?> field = buffer.get ( position );
     *         ...
     *     }
     * </pre>
     *
     * <p>
     * Or, to step through the fields in reverse order:
     * </p>
     *
     * <pre>
     *     Buffer buffer = ... ;
     *     Region buffer_region = buffer.region ();
     *     for ( Position position = buffer_region.end ();
     *           ! ( position instanceof NoPosition );
     *           position = buffer_region.expr ( position ).previous () )
     *     {
     *         Instance<?> field = buffer.get ( position );
     *         ...
     *     }
     * </pre>
     *
     * @return The Region describing this Buffer's size and
     *         structure (but NOT its content).  Never null.
     */
    public abstract Region region ();


    /**
     * <p>
     * Expands this Buffer by either inserting NoInstance's and / or
     * deleting fields at specific Positions, to match the specified
     * target Region.
     * </p>
     *
     * <p>
     * Positions from the specified Region which are not currently
     * in the Buffer will be added to the Buffer's region, and Positions
     * which are in the Buffer's current region but not listed in
     * the specified Region will be removed from this Buffer.
     * </p>
     *
     * <p>
     * Not all Buffers allow resizing.  Be sure to check the result,
     * as it could be a NoBuffer to indicate failure.
     * </p>
     *
     * @param region The new region for the Buffer.  Must not be null.
     *
     * @return The resulting Buffer, on success, or a NoBuffer if the
     *         specified Region is impossible.  Never null.
     */
    public abstract Buffer resize (
                                   Region region
                                   )
        throws ParametersMustNotBeNull.Violation,
        ReturnNeverNull.Violation;


    /**
     * <p>
     * Overwrites the nth field in this buffer.
     * </p>
     *
     * <p>
     * Typically this method is used to overwrite the contents of
     * a specific field or write a new field to a specific empty position
     * in the Buffer.
     * </p>
     *
     * <p>
     * When a field is set to Instance.NONE at the beginning or middle of
     * the Buffer, a hole is left in the Buffer.  This hole can be
     * removed by calling
     * <code> new Compact ().evaluate ( buffer ) </code>, if desired.
     * </p>
     *
     * @param position The position within this buffer.
     *                 Must not be null.  Must not be a NoPosition.
     *                 Must be a valid position within this Buffer.
     *
     * @param field The field to store at the specified position.
     *              Can be Instance.NONE.  Must not be null.
     *
     * @return This Successful Buffer, or a Failed.  Never null.
     */
    public abstract Buffer set (
                                Position position,
                                Instance<?> instance
                                )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the value(s) of the specified field from this buffer.
     * </p>
     *
     * @param position The position within this Buffer to retrieve.
     *                 Must be a position with this Buffer's Region.
     *                 Must not be null.
     *
     * @return The Value of the field at the specified Position,
     *         or a NoValue if the specified Position is invalid
     *         or if the value(s) of the field may not be retrieved.
     *         Never null.
     */
    public abstract Value<?, Typing.Violation> value (
                                                      Position position
                                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
}
