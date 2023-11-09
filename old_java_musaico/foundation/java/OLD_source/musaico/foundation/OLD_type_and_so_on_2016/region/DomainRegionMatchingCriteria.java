package musaico.foundation.region;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.Domain;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * The domain of all Regions which contain at least one Position
 * matching specific Filter criteria.
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
 * @see musaico.foundation.region.MODULE#COPYRIGHT
 * @see musaico.foundation.region.MODULE#LICENSE
 */
public class DomainRegionMatchingCriteria
    implements Domain<Region>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( DomainRegionMatchingCriteria.class );


    /** The filters which every Region must match. */
    private final Filter<Position> [] criteria;


    /**
     * <p>
     * Creates a new domain in which every Region has at least one
     * Position matching all of the specified criteria.
     * </p>
     *
     * @param criteria The Filter criteria which match at least one
     *                 Position from each Region in this domain.
     *                 Must not be null.  Must not contain any null elements.
     */
    @SuppressWarnings({"unchecked", // Heap pollution<generic varargs>
                "rawtypes"}) // Generic array creation
    public DomainRegionMatchingCriteria (
                                         Filter<Position> ... criteria
                                         )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object[]) criteria );
        classContracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               criteria );

        this.criteria = new Filter [ criteria.length ];
        System.arraycopy ( criteria, 0,
                           this.criteria, 0, criteria.length );
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final DomainRegionMatchingCriteria that =
            (DomainRegionMatchingCriteria) object;
        if ( this.criteria.length != that.criteria.length )
        {
            return false;
        }

        for ( int c = 0; c < this.criteria.length; c ++ )
        {
            if ( ! this.criteria [ c ].equals ( that.criteria [ c ] ) )
            {
                return false;
            }
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        for ( Filter<Position> criterion : this.criteria )
        {
            hash_code += criterion.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see musaico.foundation.contract.Domain#isValid(java.lang.Object)
     */
    @Override
    public boolean isValid (
                            Region region
                            )
    {
        if ( region == null )
        {
            return false;
        }

        for ( Position position : region )
        {
            boolean is_matching = true;
            for ( Filter<Position> criterion : this.criteria )
            {
                if ( criterion.filter ( position ) != Filter. State.KEPT )
                {
                    is_matching = false;
                    break;
                }
            }

            if ( is_matching )
            {
                return true;
            }
        }

        return false;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        for ( Filter<Position> criterion : this.criteria )
        {
            if ( is_first )
            {
                sbuf.append ( " " );
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + criterion );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return "regions matching criteria " + sbuf.toString ();
    }
}
