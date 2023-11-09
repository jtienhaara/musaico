package musaico.foundation.wiring;


import java.io.Serializable;


import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.EveryParameter;

import musaico.foundation.order.AbstractOrder;
import musaico.foundation.order.Comparison;


/**
 * <p>
 * A specific order for comparing objects of a specific type,
 * such as a dictionary order or a character encoding (ASCII etc)
 * order for comparing Strings.
 * </p>
 *
 *
 * <p>
 * In Java, every Order can be used as a Java Comparator.
 * </p>
 *
 * <p>
 * In Java, every Order is Serializable in order to play nicely
 * over RMI.
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
 * @see musaico.foundation.order.MODULE#COPYRIGHT
 * @see musaico.foundation.order.MODULE#LICENSE
 */
public class TagPathOrder
    extends AbstractOrder<Tag>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * Compares Tags by their <code> path () </code>s.
     */
    public static final TagPathOrder ORDER =
        new TagPathOrder ();

    // Private constructor.
    // Use TagPathOrder.ORDER instead.
    private TagPathOrder ()
    {
    }

    /**
     * @see musaico.foundation.order.Order#compareValues(java.lang.Object, java.lang.Object)
     */
    @Override
    public final Comparison compareValues (
            Tag left,
            Tag right
            )
        throws EveryParameter.MustNotBeNull.Violation,
               Return.NeverNull.Violation
    {
        if ( left == null )
        {
            if ( right != null )
            {
                return Comparison.INCOMPARABLE_LEFT;
            }
        }
        else if ( right == null )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }

        final String left_path = left.path ();
        final String right_path = right.path ();
        if ( left_path == null )
        {
            if ( right_path != null )
            {
                return Comparison.INCOMPARABLE_LEFT;
            }
        }
        else if ( right == null )
        {
            return Comparison.INCOMPARABLE_RIGHT;
        }

        final int difference = left_path.compareTo ( right_path );
        final Comparison comparison =
            new Comparison ( difference );

        return comparison;
    }
}
