package musaico.foundation.term;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Transforms input Terms into output Terms.
 * </p>
 *
 * <p>
 * Code relying on an Operation might invoke the
 * <code> Term.pipe ( ... ) </code> method to convert the Term
 * into something else.  For example, to convert Integer human years
 * into Integer dog years, something along the lines of the following
 * might be applied:
 * </p>
 *
 * <pre>
 *     Term<Integer> age = ...;
 *     Operation<Integer, Integer> convert_to_dog_years =
 *         ...; // Divides human age by 7, or creates an Error
 *              // if the input human age is negative.
 *     int age_in_dog_years =
 *         age.pipe ( convert_to_dog_years )
 *            .orDefault ( -1 );
 * </pre>
 *
 * <p>
 * Or to convert a <code> { red, green, blue } </code> tuple of floating
 * point numbers into a single <code> RRGGBB </code> hexadecimal String,
 * something like the following might be applied:
 * </p>
 *
 * <pre>
 *     Term<float[]> red_green_blue_tuple = ...;
 *     Operation<float[], String> convert_tuple_to_hex_string =
 *         ...; // Transforms { 1.0F, 0.5F, 0.0F } into "FF8000".
 *     String rgb_hex =
 *         red_green_blue_tuple.pipe ( convert_tuple_to_hex_string )
 *            .orDefault ( "000000" );
 * </pre>
 *
 * <p>
 * Operations must not be stateful.  However they may change the state
 * of the world, such as printing a message to the display, or logging
 * some information, and so on.
 * </p>
 *
 *
 * <p>
 * In Java, every Operation must implement equals (), hashCode ()
 * and toString().
 * </p>
 *
 * <p>
 * In Java every Operation must be Serializable in order to
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
 * @see musaico.foundation.term.MODULE#COPYRIGHT
 * @see musaico.foundation.term.MODULE#LICENSE
 */
public interface Operation<INPUT extends Object, OUTPUT extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Processes a conditional Term, possibly filtering or
     * changing the conditional result, and returns either the
     * specified Term or a newly created one.
     * </p>
     *
     * @param input The input conditional Term.  Must not be null.
     *
     * @return The newly transformed conditional Term, possibly the
     *         same input conditional Term, or possibly a completely
     *         different one.  Never null.
     */
    public abstract Term<OUTPUT> apply (
            Term<INPUT> input
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    // Every Operation must implement java.lang.Object#equals(java.lang.Object)


    /**
     * <p>
     * Returns the Type of Terms output by this Operation whenever there
     * is an error, such as an invalid input Term, or even
     * an invalid parameter.
     * </p>
     *
     * <p>
     * For example, an <code> Operation&lt;String, Number&gt; </code>
     * might have error Type
     * <code>IntegerType.refine ().allowTerms ( Error.class ).buildTye ()</code>.
     * </p>
     *
     * <p>
     * Or an <code> Operation&lt;Number, Date&gt; </code> might have
     * error Type
     * <code>DateType.refine ().allowTerms ( Error.class ).buildTye ()</code>.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * Every Term which can ever be output by this Operation as an erropr
     * is valid according to the error type.  For example, if this Operation
     * outputs an Error term whenever the input does not meet the
     * input type's constraints, then the error Type must consider
     * Error Terms to be valid members of its domain.
     * </p>
     *
     * @return The error Type guaranteed by this Operation given
     *         any input Term which is invalid (does not meet
     *         any or all of the constraints of this Operation's
     *         <code> inputType () </code>).  Never null.
     */
    public abstract Type<OUTPUT> errorType ()
        throws ReturnNeverNull.Violation;


    // Every Operation must implement java.lang.Object#hashCode()


    /**
     * <p>
     * Returns the Type of input Terms required by this Operation.
     * </p>
     *
     * <p>
     * For example, an <code> Operation&lt;String, Number&gt; </code>
     * might have input Type StringType.
     * </p>
     *
     * <p>
     * Or an <code> Operation&lt;Number, Date&gt; </code> might have
     * input Type IntegerType.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * When this Operation is applied to an input Term which is not valid
     * according to the input Type, a Term of the <code> errorType () </code>
     * will be output.
     * </p>
     *
     * @return The input Type required by to this Operation.
     *         Never null.
     */
    public abstract Type<INPUT> inputType ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns the Type of Terms output by this Operation.
     * </p>
     *
     * <p>
     * For example, an <code> Operation&lt;String, Number&gt; </code>
     * might have output Type IntegerType.
     * </p>
     *
     * <p>
     * Or an <code> Operation&lt;Number, Date&gt; </code> might have
     * output Type DateType.
     * </p>
     *
     * <p>
     * And so on.
     * </p>
     *
     * <p>
     * Every Term which can ever be output by this Operation
     * given a valid input must be valid according to the output type.
     * However if an input Term does not meet this Operation's
     * <code> inputType () </code>, then the <code> errorType () </code>
     * is guaranteed, not this <code> outputType () </code>.
     * </p>
     *
     * @see musaico.foundation.term.Operation#errorType()
     *
     * @return The output Type guaranteed by this Operation given
     *         any input Term which is valid (meets all of the constraints
     *         of this Operation's <code> inputType () </code>).
     *         Never null.
     */
    public abstract Type<OUTPUT> outputType ()
        throws ReturnNeverNull.Violation;


    // Every Operation must implement java.lang.Object#toString()
}
