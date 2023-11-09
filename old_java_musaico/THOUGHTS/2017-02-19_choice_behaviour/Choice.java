package musaico.foundation.term.countable;

import java.io.Serializable;


import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.term.Behaviour;
import musaico.foundation.term.BehaviourControl;


/**
 * <p>
 * A Behaviour which provides control over which element(s)
 * of a Multiplicity are kept, and which are discarded.
 * </p>
 *
 * <p>
 * For example, a <code> Choice&lt;Color&gt; </code> might be provided
 * as a template which can then be specified at instantiation time:
 * </p>
 *
 * <pre>
 *     final One<Type<Color>> colour_type = ...;
 *     final Countable<Behaviour<?>> behaviours = ..., Choice.BEHAVIOUR, ...;
 *     final Many<Color> colours =
 *         new Many<Color> ( colour_type, behaviours );
 *     ...
 *     final Term<Color> favourite_colour =
 *         colours.on ( Choice.BEHAVIOUR ).choose ( Color.BLUE )
 *                                        .build ();
 * </pre>
 *
 * <p>
 * A practical use is in duck-typing Terms.  For example, a value
 * which can be either a Date or a String might be represented as follows:
 * </p>
 *
 * <pre>
 *     final Type<Date> date_type = ...;
 *     final Type<String> formatted_date_type = ...;
 *     final Countable<Type<?>> duck_type =
 *         ... date_type, formatted_date_type, ...;
 *     ...
 *     final Term<?> date_or_string =
 *          ...result of some operation, such as retrieving a value
 *             from a database, constructed with types = duck_type;
 *             The Term's value calls on ( Choice.BEHAVIOUR )
 *             .chooseMatching ( element_class_filter )...;
 *     if ( date_or_string.types ().orNone ().elementClass () == Date.class )
 *     {
 *         final Term<Date> quacks_like_a_date =
 *             date_or_string.cast ().to ( date_type ).term ();
 *     }
 *     else
 *     {
 *         final Term<Sring> quacks_like_a_string =
 *             date_or_string.cast ().to ( formatted_date_type ).term ();
 *     }
 * </pre>
 *
 *
 * <p>
 * In Java every Behaviour must be Serializable in order to
 * play nicely across RMI.
 * </p>
 *
 * <p>
 * In Java every Behaviour must implement equals (), hashCode ()
 * and toString ().
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
 * @see musaico.foundation.term.countable.MODULE#COPYRIGHT
 * @see musaico.foundation.term.countable.MODULE#LICENSE
 */
public interface ElementsBehaviour<CONTROL extends BehaviourControl>
    extends Behaviour<CONTROL>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Given input elements, returns an Elements
     *  object: either the input Elements, or a modified one.
     *  Only really useful by AbstractCountable's constructor.
     *  The Behaviour's control will provide functionality interesting
     *  to the end user (such as converting a Term with a mutable
     *  value to a Term with an immutable value, and vice-versa). */
    public abstract <ELEMENT extends Object>
        Elements<ELEMENT> build (
            Elements<ELEMENT> elements,
            boolean is_fixed_length
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation;
}
