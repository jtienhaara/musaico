package musaico.foundation.term.contracts;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.InstanceOfClass;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;


/**
 * <p>
 * A general-purpose Contract for Countable values: each inspected Term
 * must have elements which are instances of a specific class,
 * or a TermViolation will be generated.
 * </p>
 *
 *
 * <p>
 * In Java, every Contract must implement equals () and hashCode ().
 * </p>
 *
 * <p>
 * In Java, every Contract must be Serializable in order to play
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
 * @see musaico.foundation.term.contracts.MODULE#COPYRIGHT
 * @see musaico.foundation.term.contracts.MODULE#LICENSE
 */
public class ElementsMustBeInstancesOfClass
    extends ElementsMustBelongToDomain<Object>
    implements Contract<Term<?>, ElementsMustBelongToDomain.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new ElementsMustBeInstancesOfClass contract.
     * </p>
     *
     * @param required_class The class of which every element of every
     *                       Term's Countable value must be an instance.
     *                       Must not be null.
     */
    public ElementsMustBeInstancesOfClass (
                                           Class<?> required_class
                                           )
        throws ParametersMustNotBeNull.Violation
    {
        super (
               new InstanceOfClass ( required_class )
               );
    }


    /**
     * @see musaico.foundation.contract.Contract#description()
     */
    @Override
    public final String description ()
    {
        return "The term must be a "
            + this.domain ()
            + ".";
    }


    /**
     * @see musaico.foundation.domains.InstanceOfClass#domainClass()
     */
    @SuppressWarnings("unchecked") // Cast Domain<?> to InstanceOfClass.
    public final Class<?> domainClass ()
    {
        final InstanceOfClass domain = (InstanceOfClass) this.domain ();
        return domain.domainClass ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " { "
            + this.domainClass ().getName ()
            + " }";
    }
}
