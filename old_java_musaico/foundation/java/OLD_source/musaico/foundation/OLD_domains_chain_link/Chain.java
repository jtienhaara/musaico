package musaico.foundation.domains;

import java.io.Serializable;


/**
 * <p>
 * A linked list, possibly with totally different generic elements
 * from link to link.
 * </p>
 *
 * <p>
 * For example, a structure comprising a String, a Number and a Date
 * might look like:
 * </p>
 *
 * <pre>
 *     private static class MyEnd
 *         implements ChainEnd
 *     {
 *         ...;
 *     }
 *
 *     final Link<String, Link<Number, Link<Date, MyEnd, MyEnd>, MyEnd>, MyEnd> structure = ...;
 *     final Link<String, ?, MyEnd> string_link = structure;
 *     final Link<Number, ?, MyEnd> number_link = string_link.next ();
 *     final Link<Date, ?, MyEnd> date_link = number_link.next ();
 * </pre>
 *
 *
 * <p>
 * In Java every Chain must be Serializable in order to
 * play nicely across RMI.  However users of the Chain
 * must be careful, since any values stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.domains.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.MODULE#LICENSE
 */
public interface Chain<END extends Chain<END>>
    extends Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;
}
