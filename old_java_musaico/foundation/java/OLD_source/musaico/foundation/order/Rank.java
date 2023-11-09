package musaico.foundation.order;

import java.io.Serializable;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A FilterState which also represents the ranking of the filtered
 * object according to its own natural order or some external order.
 * </p>
 *
 * <p>
 * For example, the natural Rank of a SportsRecord might have a Long index
 * <code> 2L * wins () + 1L * ties () </code>, with an <code> order () </code>
 * that would sort higher total points before lower.  Other Ranks
 * might exist for the same SportsRecord, such as +/-
 * (<code> for () - against () </code>) and so on.
 * </p>
 *
 *
 * <p>
 * In Java every Rank must implement equals (), hashCode ()
 * and toString ().
 * </p>
 *
 * <p>
 * In Java every Rank must be Serializable in order to play nicely
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
public class Rank<INDEX extends Serializable>
    extends FilterState
    implements Comparable<Rank<INDEX>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method parameters for us.
    private static final Advocate classContracts =
        new Advocate ( Rank.class );




    /**
     * <p>
     * Creates a Rank of each of the specified object(s), and returns them
     * all in the same (unsorted) order.
     * </p>
     *
     * @param order The Order of the rankable objects, which will generate
     *              a Rank for each one.  Must not be null.
     *
     * @param rankables The object(s) whose Rank will be returned.
     *                  Must not be null.  Must not contain any null elements.
     *
     * @return The Rank(s) of the specified orderable object(s).
     *         Never null.  Never contains any null elements.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
        // Generic varargs heap pollution,
        // generic array creation Rank [].
    public static final <RANKABLE extends Object, RANK_INDEX extends Serializable>
        Rank<RANK_INDEX> [] ranks (
            RankFactory<RANKABLE, RANK_INDEX> factory,
            RANKABLE ... rankables
            )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               factory, rankables );
        classContracts.check ( Parameter2.MustContainNoNulls.CONTRACT,
                               rankables );

        final Rank<RANK_INDEX> [] unsorted_ranks =
            new Rank [ rankables.length ];
        for ( int r = 0; r < unsorted_ranks.length; r ++ )
        {
            unsorted_ranks [ r ] = factory.rank ( rankables [ r ] );
        }

        return unsorted_ranks;
    }




    // The Order to sort this Rank's index.
    private final Order<INDEX> order;

    // This Rank's index.
    private final INDEX index;


    /**
     * <p>
     * Creates a new Rank.
     * </p>
     *
     * @param order The order of rank indices.  Must not be null.
     *
     * @param index The index for this new Rank.  Must not be null.
     */
    public Rank (
            Order<INDEX> order,
            INDEX index
            )
        throws ParametersMustNotBeNull.Violation
    {
        super ( createName ( order, index ), // name
                true ); // is_kept

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               order, index );

        this.order = order;
        this.index = index;
    }


    private static final <NAMED_INDEX extends Serializable>
            String createName (
                Order<NAMED_INDEX> order,
                NAMED_INDEX index
                )
    {
        return " Rank [ " + index + " ] (" + order + ")";
    }


     /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public final int compareTo (
                                Rank<INDEX> that
                                )
    {
        if ( that == null )
        {
            return Integer.MIN_VALUE + 1;
        }

        final int index_comparison =
            this.order.compare ( this.index, that.index );
        return index_comparison;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked") // Cast Rank<?>-Rank<Ser>, try-cast-catch.
    @Override
    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( ! ( object instanceof Rank ) )
        {
            return false;
        }

        final Rank<Serializable> that = (Rank<Serializable>) object;
        if ( this.index == null )
        {
            if ( that.index != null )
            {
                return false;
            }
        }
        else if ( that.index == null )
        {
            return false;
        }

        try
        {
            if ( this.order.compare ( this.index,
                                      (INDEX) that.index ) == 0
                 && that.order.compare ( this.index, that.index ) == 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch ( Exception e )
        {
            // ClassCastException etc.
            return false;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        return this.index == null
                   ? 0
                   : this.index.hashCode ();
    }


    /**
     * @return The numeric or other index representation of this rank.
     *         Depending on the <code> order () </code>, higher or lower
     *         might be sorted first.  For example, a natural
     *         thread priority Rank might have an order which sorts
     *         lower indices first, whereas a sports record Rank might
     *         have an order which sorts higher indices first.
     *         Can be any number.
     */
    public final INDEX index ()
    {
        return this.index;
    }


    /**
     * @return The natural Order which compares and sorts the indices
     *         of Ranks of this type.  Never null.
     */
    public final Order<INDEX> order ()
    {
        return this.order;
    }
}
