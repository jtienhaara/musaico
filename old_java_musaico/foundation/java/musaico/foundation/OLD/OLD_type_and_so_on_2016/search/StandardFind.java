package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;


/**
 * <p>
 * Finds needle(s) in a haystack according to some criterion (criteria)
 * by using a specific search strategy (such as Search.LINEAR
 * or Search.BINARY and so on).
 * </p>
 *
 *
 * <p>
 * In Java, every Find must be Serializable in order
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
public class StandardFind<NEEDLE extends Object, HAYSTACK extends Object>
    implements Find<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardFind.class );


    // The search strategy decides how to step through the haystack,
    // in order to evaluate candidate needles.
    private final Search<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> strategy;

    // Tells us when we've found the needle we're looking for.
    private final Filter<NEEDLE> criterion;

    // The search space in which we want to find needles.
    private final HAYSTACK haystack;


    /**
     * <p>
     * Creates a new StandardFind to find needle(s) matching the
     * specified criterion (criteria) in the specified haystack,
     * using the specified search strategy.
     * </p>
     *
     * @param strategy Steps through candidate found values
     *                 from the haystack, in whatever order
     *                 the search strategy decides.
     *                 Must not be null.
     *
     * @param criterion Used to match needles in the haystack.
     *                  Must not be null.
     *
     * @param haystack The haystack in which to find matching needle(s).
     *                 Must not be null.
     */
    public StandardFind (
                         Search<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> strategy,
                         Filter<NEEDLE> criterion,
                         HAYSTACK haystack
                         )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               strategy, criterion, haystack );

        this.strategy = strategy;
        this.criterion = criterion;
        this.haystack = haystack;
    }


    /**
     * @see musaico.foundation.search.Find#criterion()
     */
    @Override
    public final Filter<NEEDLE> criterion ()
        throws ReturnNeverNull.Violation
    {
        return this.criterion;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
                                 Object obj
                                 )
    {
        if ( obj == this )
        {
            // Any StandardFind == itself.
            return true;
        }
        else if ( obj == null )
        {
            // Any StandardFind != null.
            return false;
        }
        else if ( ! ( obj instanceof StandardFind ) )
        {
            // Any StandardFind != any non-StandardFind.
            return false;
        }

        final StandardFind<?, ?> that = (StandardFind<?, ?>) obj;
        if ( ! this.criterion.equals ( that.criterion ) )
        {
            return false;
        }
        else if ( ! this.strategy.equals ( that.strategy ) )
        {
            return false;
        }
        else if ( ! this.haystack.equals ( that.haystack ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        int hash_code = 0;
        hash_code += this.strategy.hashCode ();
        hash_code += this.haystack.hashCode () * 17;
        hash_code += this.criterion.hashCode () * 31;

        return hash_code;
    }


    /**
     * @see musaico.foundation.search.Find#haystack()
     */
    @Override
    public final HAYSTACK haystack ()
        throws ReturnNeverNull.Violation
    {
        return this.haystack;
    }


    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public FoundIterator<NEEDLE, HAYSTACK> iterator ()
    {
        return new FoundIterator<NEEDLE, HAYSTACK> ( this );
    }


    /**
     * @see musaico.foundation.search.Find#strategy()
     */
    @Override
    public final Search<NEEDLE, HAYSTACK, IndexedIterator<NEEDLE>> strategy ()
        throws ReturnNeverNull.Violation
    {
        return this.strategy;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "Find ( "
            + this.criterion
            + " ) in ( "
            + this.haystack
            + " ) with strategy ( "
            + this.strategy
            + " )";
    }
}
