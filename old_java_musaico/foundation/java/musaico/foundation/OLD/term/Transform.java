package musaico.foundation.term;

import java.io.InputStream;
import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Maps Terms to other Types, or extracts their values as other data
 * structures (such as arrays and Lists).
 * </p>
 *
 * <p>
 * For example, to cast a String type term to a Number type term:
 * </p>
 *
 * <pre>
 *     final Term<String> text = ...;
 *     final Type<Number> number_type = ...;
 *     final Term<Number> as_number =
 *         text.cast ()
 *             .to ( number_type )
 *             .asTerm ();
 * </pre>
 *
 * <p>
 * Or to convert a term of String elements to an array of Strings:
 * </p>
 *
 * <pre>
 *     final Term<String> text = ...;
 *     final String [] as_array =
 *         text.cast ()
 *             .asArray ();
 * </pre>
 *
 * <p>
 * Every Transform can also serve as the head of a Pipeline of Operations
 * on the transformed output Term.
 * </p>
 *
 * <p>
 * For example, to cast a term of String elements to Numbers,
 * sort the Numbers, eliminate duplicates, then convert the numeric Term
 * to an array of Numbers:
 * </p>
 *
 * <pre>
 *     final Term<String> text = ...;
 *     final Type<Number> number_type = ...;
 *     final Order<Number> numeric_order = ...;
 *     final Number [] as_sorted_array =
 *         text.cast ()
 *             .to ( number_type )
 *             .where ()
 *                 .unique ()
 *               .end ()
 *             .orderBy ( numeric_order )
 *               .end ()
 *             .asArray ();
 * </pre>
 *
 *
 * <p>
 * In Java every Pipeline must implement <code> equals () </code>,
 * <code> hashCode () </code> and <code> toString () </code>.
 * </p>
 *
 * <p>
 * In Java every Pipeline must be Serializable in order to
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
public interface Transform<FROM extends Object, TO extends Object>
    extends TermPipeline.TermSink<TO>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#check()

    // Every Pipeline must implement
    // musaico.foundation.term.TermPipeline#duplicate()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#edit()

    // Every Pipeline must implement java.lang.Object#equals(java.lang.Object)

    // Every Transform must implement java.lang.Object#hashCode()


    /**
     * @return A mapping of the input Term to its indices (0L, 1L, 2L,
     *         and so on).  Never null.
     */
    public abstract Transform<TO, Long> indices ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The original input Term or pipeline.  Never null.
     */
    public abstract TermPipeline.TermSink<FROM> inputOrigin ()
        throws ReturnNeverNull.Violation;


    /**
     * @return The Type of Terms which this Transform takes as inputs.
     *         Not necessarily the same Type as the original input Term.
     *         Never null.
     */
    public abstract Type<FROM> inputType ()
        throws ReturnNeverNull.Violation;


    /**
     * @return An Operation which can take terms of the FROM type and
     *         transform them into terms of the TO type.  Never null.
     */
    public abstract Operation<FROM, TO> operation ()
        throws ReturnNeverNull.Violation;


    // Every TermPipeline.TermSink must implement
    // musaico.foundation.term.TermPipeline.TermSink#operations()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#orderBy(musaico.foundation.order.Order[])

    // Every Sink must implement
    // musaico.foundation.pipeline.Sink#output()


    /**
     * @return The target Type of this Transform.  Never null.
     */
    public abstract Type<TO> outputType ()
        throws ReturnNeverNull.Violation;


    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.Operation)

    // Every TermPipeline must implement
    // musaico.foundation.term.TermPipeline#pipe(musaico.foundation.term.OperationPipeline)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#select()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Parent#thisPipeline()


    /**
     * <p>
     * Maps the input term to the output type and returns it as a Term.
     * </p>
     *
     * <p>
     * If the cast cannot be performed (for example because there is
     * no known way to cast from the source type to the target type)
     * then an Error is returned, even if the source Term
     * was non-empty.
     * </p>
     *
     * @return The input term cast to the target output type.  Never null.
     */
    public abstract Term<TO> term ()
        throws ReturnNeverNull.Violation;


    /**
     * <p>
     * Returns a new Transform to another Type.
     * </p>
     *
     * @param to_type The new target Type.
     *                Must not be null.
     *
     * @return A new Transform.  Never null.
     */
    public abstract <NEW_TARGET extends Object>
        Transform<TO, NEW_TARGET> to (
            Type<NEW_TARGET> to_type
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;


    /**
     * <p>
     * Maps the input term to the output type and returns it as
     * an array.
     * </p>
     *
     * <p>
     * If the cast cannot be performed (for example because there is
     * no known way to cast from the source type to the target type)
     * then an empty array is returned, even if the source Term
     * was non-empty.
     * </p>
     *
     * @return The input term cast to the target output type and converted
     *         to an array.  Never null.  Never contains any null elements.
     */
    public abstract TO [] toArray ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Maps the input term to the output type and returns it as
     * a List.
     * </p>
     *
     * <p>
     * If the cast cannot be performed (for example because there is
     * no known way to cast from the source type to the target type)
     * then an empty List is returned, even if the source Term
     * was non-empty.
     * </p>
     *
     * @return The input term cast to the target output type and converted
     *         to a List.  Never null.  Never contains any null elements.
     */
    public abstract List<TO> toList ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Maps the input term to the output type and returns a Map of
     * the elements present in the original input term to their
     * corresponding output elements.
     * </p>
     *
     * <p>
     * If the cast cannot be performed (for example because there is
     * no known way to cast from the source type to the target type)
     * then an empty Map is returned, even if the source Term
     * was non-empty.
     * </p>
     *
     * <p>
     * If the input elements do not match the output elements in length,
     * or if the input is not Countable, or if the input is Countable
     * but greater in length than Integer.MAX_VALUE, then an empty Map
     * will always be returned.
     * </p>
     *
     * <p>
     * If any given input element maps to 2 or more different output elements,
     * then an empty map will always be returned.
     * </p>
     *
     * @return The input term cast to the target output type, converted
     *         to a Map with the input elements as keys to the
     *         output elements as values.  Never null.
     *         Never contains any null keys or values.
     */
    public abstract LinkedHashMap<FROM, TO> toMap ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    /**
     * <p>
     * Maps the input term to the output type and returns it as
     * a LinkedHashSet.
     * </p>
     *
     * <p>
     * If the cast cannot be performed (for example because there is
     * no known way to cast from the source type to the target type)
     * then an empty LinkedHashSet is returned, even if the source Term
     * was non-empty.
     * </p>
     *
     * @return The input term cast to the target output type and converted
     *         to a LinkedHashSet.  Never null.
     *         Never contains any null elements.
     */
    public abstract LinkedHashSet<TO> toSet ()
        throws ReturnNeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation;


    // Every Pipeline must implement java.lang.Object#toString()

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#when(musaico.foundation.filter.Filter)

    // Every Pipeline must implement
    // musaico.foundation.pipeline.Pipeline#where(musaico.foundation.filter.Filter[])
}
