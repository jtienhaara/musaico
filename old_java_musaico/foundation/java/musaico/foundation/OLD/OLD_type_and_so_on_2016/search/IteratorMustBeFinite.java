package musaico.foundation.search;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;
// Used but can't be imported: import musaico.foundation.contract.Violation;

import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;


/**
 * <p>
 * A guarantee that Iterator.next () will return a non-null value.
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
 * @see musaico.foundation.search.MODULE#COPYRIGHT
 * @see musaico.foundation.search.MODULE#LICENSE
 */
public class IteratorMustBeFinite
    implements Contract<Iterator<?>, IteratorMustBeFinite.Violation>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Checks contracts on constructors and static methods for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( IteratorMustBeFinite.class );


    /** Prevents iterating forever.  Int.MAX VALUE is a reasonable
     *  limit before we assume infinite length. */
    public static final long DEFAULT_MAX_INDEX = (long) Integer.MAX_VALUE;

    /** The default IteratorMustBeFinite contract.
     *  After stepping past index number
     *  <code> IteratorMustBeFinite.MAX_INDEX </code>,
     *  an Iterator is assumed to go on infinitely. */
    public static final IteratorMustBeFinite CONTRACT =
        new IteratorMustBeFinite ( IteratorMustBeFinite.DEFAULT_MAX_INDEX );


    public static class Violation
        extends RuntimeException
        implements musaico.foundation.contract.Violation, Serializable
    {
        private static final long serialVersionUID =
            IteratorMustBeFinite.serialVersionUID;

        /** Checks contracts on constructors and static methods for us. */
        private static final ObjectContracts classContracts =
            new ObjectContracts ( IteratorMustBeFinite.Violation.class );


        /** The violated contract. */
        private final Contract<Iterator<?>, IteratorMustBeFinite.Violation> contract;

        /** A Serializable representation of the object
         *  whose contract was violated. */
        private final Serializable plaintiff;

        /** A Serializable representation of the iterator. */
        private final Serializable inspectableData;


        /**
         * <p>
         * Creates a new IteratorMustBeFinite.Violation
         * with the specified details.
         * </p>
         *
         * @param contract The violated contract.  Must not be null.
         *
         * @param plaintiff The object whose contract was
         *                              violated.  Must not be null.
         *
         * @param inspectable_data The data which violated the contract.
         *                         Must not be null.
         */
        public Violation (
                          Contract<Iterator<?>, IteratorMustBeFinite.Violation> contract,
                          Object plaintiff,
                          Iterator<?> inspectable_data
                          )
        {
            super ( "" + contract + ".Violation" );

            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   contract, plaintiff, inspectable_data );

            this.contract = contract;
            this.plaintiff = "" + plaintiff;
            this.inspectableData =
                Contracts.makeSerializable ( inspectable_data );
        }

        /**
         * @see musaico.foundation.contract.Violation#contract()
         */
        @Override
        public Contract<?, ?> contract ()
        {
            return this.contract;
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
                // Any IteratorMustBeFinite.Violation != null.
                return false;
            }
            else if ( object == this )
            {
                // Every IteratorMustBeFinite.Violation == itself.
                return true;
            }
            else if ( object.getClass () != this.getClass () )
            {
                // Any IteratorMustBeFinite.Violation of class X
                //    != any IteratorMustBeFinite.Violation of class Y.
                return false;
            }

            final IteratorMustBeFinite.Violation that =
                (IteratorMustBeFinite.Violation) object;

            if ( this.contract == null )
            {
                if ( that.contract == null )
                {
                    // null contract == null contract.
                    return true;
                }
                else
                {
                    // null contract != any contract.
                    return false;
                }
            }
            else if ( that.contract == null )
            {
                // any contract != null contract.
                return false;
            }

            if ( ! this.contract.equals ( that.contract ) )
            {
                // This contract != that contract.
                return false;
            }

            if ( this.plaintiff == null )
            {
                if ( that.plaintiff == null )
                {
                    // null plaintiff == null plaintiff.
                    return true;
                }
                else
                {
                    // null plaintiff != any plaintiff.
                    return false;
                }
            }
            else if ( that.plaintiff == null )
            {
                // any plaintiff != null plaintiff.
                return false;
            }

            if ( ! this.plaintiff.equals ( that.plaintiff ) )
            {
                // This plaintiff != that plaintiff.
                return false;
            }

            if ( this.inspectableData == null )
            {
                if ( that.inspectableData == null )
                {
                    // null inspectableData == null inspectableData.
                    return true;
                }
                else
                {
                    // null inspectableData != any inspectableData.
                    return false;
                }
            }
            else if ( that.inspectableData == null )
            {
                // any inspectableData != null inspectableData.
                return false;
            }

            if ( ! this.inspectableData.equals ( that.inspectableData ) )
            {
                // This inspectableData != that inspectableData.
                return false;
            }

            // Return true since all the details are equal.
            return true;
        }


        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode ()
        {
            return this.contract.hashCode () * 31
                + ClassName.of ( this.getClass () ).hashCode ();
        }


        /**
         * @see musaico.foundation.contract.Violation#inspectableData()
         */
        @Override
        public Serializable inspectableData ()
        {
            return this.inspectableData;
        }


        /**
         * @see musaico.foundation.contract.Violation#plaintiff()
         */
        @Override
        public Serializable plaintiff ()
        {
            return this.plaintiff;
        }
    }




    // The index at which we give up and decide the iterator is going
    // to go on infinitely.
    private final long maxIndex;


    /**
     * <p>
     * Creates a new IteratorMustBeFinite which will give up and
     * assume an Iterator is infinite once it has passed the specified
     * maximum index number.
     * </p>
     *
     * @param max_index The maximum index number an Iterator can
     *                  step over before it is assumed to be infinite.
     *                  Must be greater than or equal to 1L.
     */
    public IteratorMustBeFinite (
                                 long max_index
                                 )
        throws Parameter1.MustBeGreaterThanOrEqualToOne.Violation
    {
        classContracts.check ( Parameter1.MustBeGreaterThanOrEqualToOne.CONTRACT,
                               max_index );

        this.maxIndex = max_index;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj.getClass () != this.getClass () )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               Iterator<?> iterator
                               )
    {
        if ( iterator == null )
        {
            return FilterState.DISCARDED;
        }

        for ( long index = 0L; index < this.maxIndex; index ++ )
        {
            if ( ! iterator.hasNext () )
            {
                // Finite end.
                return FilterState.KEPT;
            }
        }

        // No end in sight.
        return FilterState.DISCARDED;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return ClassName.of ( this.getClass () ).hashCode ();
    }


    /**
     * @return The maximum index beyond which an Iterator is assumed to
     *         go on to infinity.  Always greater than or equal to 1L.
     */
    public final long maxIndex ()
        throws Return.AlwaysGreaterThanOrEqualToOne.Violation
    {
        return this.maxIndex;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () );
    }


    /**
     * @see musaico.contract.Contract#violation(musaico.contract.Contract, java.lang.Object, java.lang.Object)
     */
    @Override
    public IteratorMustBeFinite.Violation violation (
        Object plaintiff,
        Iterator<?> inspectable_data
        )
    {
        return new IteratorMustBeFinite.Violation (
                this,
                plaintiff,
                inspectable_data );
    }


    /**
     * <p>
     * Helper method.  Always passes this IteratorMustBeFinite contract
     * as the first parameter to the full method, and sets the specified
     * initial cause (if any).
     * </p>
     *
     * @see musaico.foundation.search.IteratorMustBeFinite#violation(musaico.foundation.contract.Contract, java.lang.Object, java.lang.Object)
     */
    public IteratorMustBeFinite.Violation violation (
            Object plaintiff,
            Iterator<?> inspectable_data,
            Throwable cause
            )
    {
        final IteratorMustBeFinite.Violation violation =
            this.violation ( plaintiff,
                             inspectable_data );

        if ( cause != null )
        {
            violation.initCause ( cause );
        }

        return violation;
    }
}
