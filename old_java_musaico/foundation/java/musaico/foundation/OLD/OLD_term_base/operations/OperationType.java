package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;


/**
 * <p>
 * A Type defining Operations, possibly defining a contract on each Term,
 * but also providing the Type of each Operation's required
 * input and the Type of its provided output.
 * </p>
 *
 *
 * <p>
 * In Java, every Type must implement equals (), hashCode () and toString().
 * </p>
 *
 * <p>
 * In Java, every Type must be Serializable in order to
 * play nicely over RMI.
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface OperationType<INPUT extends Object, OUTPUT extends Object>
    extends Type<Operation<INPUT, OUTPUT>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Every Type must implement equals ().
     *  @see java.lang.Object#equals(java.lang.Object) */

    /** Every Type must implement hashCode ().
     *  @see java.lang.Object#hashCode() */


    /**
     * <p>
     * Returns the Type of the input Term required by Operations of
     * this OperationType.
     * </p>
     *
     * <p>
     * For example, a <code> Type&lt;String, Integer&gt; </code>
     * might have an input Type "StringType".
     * </p>
     *
     * <p>
     * Or a <code> OperationType&lt;Number, Date&gt; </code> might have
     * an input Type "NumberType".
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @return The required input Type to Operations of this OperationType.
     *         Never null.
     */
    public abstract Type<INPUT> operationInputType ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Type of the output Term provided by Operations
     * of this OperationType.
     * </p>
     *
     * <p>
     * For example, a <code> OperationType&lt;String, Integer&gt; </code>
     * might have an output Type "IntegerType".
     * </p>
     *
     * <p>
     * Or a <code> OperationType&lt;Number, Date&gt; </code> might have
     * an output Type "DateType".
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * @return The output Type provided by Operations of this OperationType.
     *         Never null.
     */
    public abstract Type<OUTPUT> operationOutputType ()
        throws ReturnNeverNull.Violation;


    /** Every Type must implement toString ().
     *  @see java.lang.Object#toString() */
}
