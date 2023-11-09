package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * Steps through any iterable search space, looking for an object
 * matching any old filter.
 * </p>
 *
 *
 * <p>
 * In Java, every Search strategy must be Serializable in order
 * to play nicely over RMI.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class LinearSearch<NEEDLE extends Object>
    implements Search<NEEDLE, Iterable<NEEDLE>, IndexedIterator<NEEDLE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The family of linear search strategies (one strategy per class
     *  of NEEDLE). */
    public static class Family
        implements SearchFamily
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID =
            LinearSearch.serialVersionUID;

        /**
         * @see musaico.foundation.search.SearchFamily#over(java.lang.Class)
         */
        @Override
        public final <MEMBER extends Object>
                                     Search<MEMBER, Iterable<MEMBER>, IndexedIterator<MEMBER>>
                                     // !!!                LinearSearch<MEMBER>
 over (
                                           Class<MEMBER> needle_class
                                           )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            return new LinearSearch<MEMBER> ();
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
            else if ( this.getClass () != object.getClass () )
            {
                return false;
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
            hash_code += this.toString ().hashCode ();
            return hash_code;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return "LinearSearch.Family";
        }
    }


    /** The family of linear search strategies (one strategy per class
     *  of NEEDLE). */
    public static final SearchFamily FAMILY = new LinearSearch.Family ();


    // Enforces method parameter obligations and guarantees and so on for us.
    private final ObjectContracts contracts;


    /**
     * <p>
     * Creates a new LinearSearch.
     * </p>
     */
    public LinearSearch ()
    {
        this.contracts = new ObjectContracts ( this );
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.search.Search#family()
     */
    @Override
    public SearchFamily family ()
        throws ReturnNeverNull.Violation
    {
        return LinearSearch.FAMILY;
    }


    /**
     * @see musaico.foundation.search.Search#find(musaico.foundation.filter.Filter, java.lang.Object)
     */
    @Override
    public Find<NEEDLE, Iterable<NEEDLE>, IndexedIterator<NEEDLE>> find (
            Filter<NEEDLE> criterion,
            Iterable<NEEDLE> haystack
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               criterion, haystack );

        final Find<NEEDLE, Iterable<NEEDLE>, IndexedIterator<NEEDLE>> find =
            new StandardFind<NEEDLE, Iterable<NEEDLE>> (
                this,
                criterion,
                haystack );

        return find;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.getClass ().getName ().hashCode ();
        return hash_code;
    }


    /**
     * @see musaico.foundation.search.Search#start(musaico.foundation.search.Find, java.io.Iterator)
     */
    @Override
    public FilterState start (
            Find<NEEDLE, Iterable<NEEDLE>, IndexedIterator<NEEDLE>> find,
            IndexedIterator<NEEDLE> first_step
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               find, first_step );

        return FilterState.DISCARDED;
    }


    /**
     * @see musaico.foundation.search.Search#step(musaico.foundation.search.Find, java.io.Iterator, musaico.foundation.filter.FilterState)
     */
    @Override
    public IndexedIterator<NEEDLE> step (
            Find<NEEDLE, Iterable<NEEDLE>, IndexedIterator<NEEDLE>> find,
            IndexedIterator<NEEDLE> from_step,
            FilterState filtered
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               find, from_step, filtered );

        // We don't care what the previous filtered state was.
        // We always search left-to-right, and we don't even change
        // up that order, even if the search space is ordered.
        return from_step;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return ClassName.of ( this.getClass () );
    }
}
