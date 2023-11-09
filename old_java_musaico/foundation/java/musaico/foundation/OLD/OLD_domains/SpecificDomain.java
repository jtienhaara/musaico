package musaico.foundation.domains;

import java.io.Serializable;

import musaico.foundation.filter.AbstractDomain;
import musaico.foundation.filter.Domain;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A domain of specific objects governed by the domain rules of some
 * other domain covering more generic objects.
 * </p>
 *
 * <p>
 * For example, a Domain of Strings governed by a more generic
 * Domain of Objects:
 * </p>
 *
 * <pre>
 *     final Domain<Object> generic = ...;
 *     final Domain<String> specific =
 *         new SpecificDomain<Object, String> ( generic,
 *                                              String.class );
 * </p>
 *
 * <p>
 * Or a Domain of Integers governed by a more generic
 * Domain of Numbers:
 * </p>
 *
 * <pre>
 *     final Domain<Number> generic = ...;
 *     final Domain<Integer> specific =
 *         new SpecificDomain<Number, Integer> ( generic,
 *                                               Integer.class );
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java every Domain must implement equals (), hashCode () and
 * toString ().
 * </p>
 *
 * <p>
 * In Java every Domain must be Serializable in order to play nicely
 * across RMI.
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
public class SpecificDomain<GENERIC_DOMAIN extends Object, SPECIFIC_DOMAIN extends GENERIC_DOMAIN>
    extends AbstractDomain<SPECIFIC_DOMAIN>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The underlying domain covering some other, more generic,
    // class of domain objects, which governs the specific class of
    // objects in this domain.
    private final Domain<GENERIC_DOMAIN> genericDomain;

    // The specific class of objects governed by this domain.
    private final Class<SPECIFIC_DOMAIN> specificClass;


    /**
     * <p>
     * Creates a new SpecificDomain.
     * </p>
     *
     * @param generic_domain The underlying domain covering some other,
     *                       more generic, class of domain objects,
     *                       which governs the specific class of
     *                       objects in this domain.  Must not be null.
     *
     * @param specific_class The specific class of objects governed by
     *                       this domain.  Must not be null.
     */
    public SpecificDomain (
                           Domain<GENERIC_DOMAIN> generic_domain,
                           Class<SPECIFIC_DOMAIN> specific_class
                           )
        throws NullPointerException
    {
        if ( generic_domain == null )
        {
            throw new NullPointerException ( "Cannot create a SpecificDomain with generic_domain = " + generic_domain );
        }
        else if ( specific_class == null )
        {
            throw new NullPointerException ( "Cannot create a SpecificDomain with specific_class = " + specific_class );
        }

        this.genericDomain = generic_domain;
        this.specificClass = specific_class;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == this )
        {
            return true;
        }
        else if ( obj == null )
        {
            return false;
        }
        else if ( this.getClass () != obj.getClass () )
        {
            return false;
        }

        final SpecificDomain<?, ?> that = (SpecificDomain<?, ?>) obj;

        if ( this.genericDomain == null )
        {
            if ( that.genericDomain != null )
            {
                return false;
            }
        }
        else if ( that.genericDomain == null )
        {
            return false;
        }
        else if ( ! this.genericDomain.equals ( that.genericDomain ) )
        {
            return false;
        }

        if ( this.specificClass == null )
        {
            if ( that.specificClass != null )
            {
                return false;
            }
        }
        else if ( that.specificClass == null )
        {
            return false;
        }
        else if ( ! this.specificClass.equals ( that.specificClass ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
                                     SPECIFIC_DOMAIN value
                                     )
    {
        return this.genericDomain.filter ( value );
    }


    /**
     * @return The underlying domain covering some other,
     *         more generic, class of domain objects,
     *         which governs the specific class of
     *         objects in this domain.  Never null.
     */
    public final Domain<GENERIC_DOMAIN> genericDomain ()
    {
        return this.genericDomain;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ( this.genericDomain == null
                     ? 0
                     : this.genericDomain.hashCode () )
            *
            ( this.specificClass == null
                  ? 0
              : this.specificClass.hashCode () );
    }


    /**
     * @return The specific class of objects governed by
     *         this domain.  Never null.
     */
    public final Class<SPECIFIC_DOMAIN> specificClass ()
    {
        return this.specificClass;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "Specific<"
            + ClassName.of ( this.specificClass )
            + "> ( "
            + this.genericDomain
            + " )";
    }
}
