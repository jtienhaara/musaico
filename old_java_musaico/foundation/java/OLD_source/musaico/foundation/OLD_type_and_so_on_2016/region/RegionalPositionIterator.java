package musaico.foundation.region;

import java.util.Iterator;
import java.util.NoSuchElementException;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


/**
 * <p>
 * Steps through a Region from start to end and every Position
 * in between.
 * </p>
 *
 * <p>
 * This iterator is NOT thread-safe.  If you call its methods
 * from multiple threads, expect a concurrent modification exception.
 * Instead, you should create one Iterator for each thread.
 * (Multiple iterators stepping over a single Region is fine, since
 * each Region is immutable.)
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
public class RegionalPositionIterator
    implements Iterator<Position>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks parameters to constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( RegionalPositionIterator.class );


    /** The region over which we step. */
    private final Region region;

    /** The next position within the region to return from next (). */
    private Position nextPosition;


    /**
     * <p>
     * Creates a new iterator over the specified Region.
     * </p>
     *
     * @param region The region over which to step.  Must not be null.
     */
    public RegionalPositionIterator (
                                     Region region
                                     )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               region );

        this.region = region;
        this.nextPosition = this.region.start ();
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext ()
    {
        if ( this.nextPosition instanceof NoPosition )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public Position next ()
        throws NoSuchElementException
    {
        Position current_position = this.nextPosition;

        if ( current_position instanceof NoPosition )
        {
            throw new NoSuchElementException ( ""
                                               + ClassName.of ( this.getClass () )
                                               + " "
                                               + this
                                               + " for region "
                                               + region
                                               + " has no more elements" );
        }

        this.nextPosition = this.region.expr ( current_position )
            .next ()
            .orNone ();

        return current_position;
    }


    /**
     * @see java.util.Iterator#remove()
     *
     * <p>
     * Always throws UnsupportedOperationException.  It does
     * not make any sense to remove a Position from an immutable
     * Region.
     * </p>
     */
    @Override
    public void remove ()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException ( "Remove is"
                                                  + " not supported by "
                                                  + ClassName.of ( this.getClass () )
                                                  + " " + this );
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        return "" + ClassName.of ( getClass () )
            + " over " + this.region;
    }
}
