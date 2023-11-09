package musaico.foundation.filter;

import java.io.Serializable;


/**
 * <p>
 * A Filter is used to keep (match) or discard (filter out) objects.
 * </p>
 *
 *
 * <p>
 * In Java, every Filter must be generic in order to simplify
 * composing multiple Filters.  For example, a developer might
 * want to filter Long integers using a Boolean OR'ed pair of
 * Filters such as:
 * <code> new And&lt;Long&gt; (
 *                new GreaterThanZero&lt;Long&gt; (),
 *                new MemberOf&lt;Long&gt; ( odd_numbers ) ); </code>
 * If the GreaterThanZero filter had been built non-generically
 * as a <code> Filter&lt;Number&gt; </code> with no generic
 * parameter (which, at one point in the past, it was), then
 * this example composition of Filters would be impossible
 * without horrid casts.  Keep usages such as this one in mind
 * as you develop your Filters, and make sure developers can use
 * them with appropriate generic parameter however they see fit.
 * </p>
 *
 * <p>
 * In Java, every Filter must be Serializable in order to play
 * nicely over RMI.
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public interface Filter<GRAIN extends Object>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /**
     * <p>
     * Returns a kept FilterState if the specified grain has
     * been kept by this Filter, or a discarded FilterState
     * if the specified grain has been filtered out.
     * </p>
     *
     * @param grain The object to filter.  Implementors
     *              of the Filter interface must be careful to deal
     *              defensively with null values.  Can be null.
     *
     * @return A kept FilterState if the object is kept;
     *         a discarded FilterState if the object is filtered out.
     */
    public abstract FilterState filter (
            GRAIN grain
            );
}
