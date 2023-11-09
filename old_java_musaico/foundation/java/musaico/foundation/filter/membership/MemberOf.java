package musaico.foundation.filter.membership;

import java.io.Serializable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Keeps elements of a specific set, discarding all other objects.
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add useful new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Filter must be generic in order to
 * enable composability.
 * </p>
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
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
 * @see musaico.foundation.filter.membership.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.membership.MODULE#LICENSE
 */
public class MemberOf<MEMBER extends Object>
    implements Filter<MEMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The Set whose member elements will be KEPT.
    private final LinkedHashSet<MEMBER> setToKeep;


    /**
     * <p>
     * Creates a new MemberOf filter.
     * </p>
     *
     * @param members The array whose members will be kept.
     *                Must not contain any nulls.
     *                If null, then the empty set will be
     *                used by default, so nothing will be kept
     *                by this filter.  (DO NOT PASS NULL.)
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public MemberOf (
            MEMBER ... members
            )
    {
        if ( members == null )
        {
            // Empty set.
            this.setToKeep = new LinkedHashSet<MEMBER> ();
        }
        else
        {
            // Defensive copy.
            this.setToKeep = new LinkedHashSet<MEMBER> ();
            Collections.addAll ( this.setToKeep, members );
        }
    }


    /**
     * <p>
     * Creates a new MemberOf filter.
     * </p>
     *
     * @param members The Collection whose members will be kept.
     *                Must not contain any nulls.
     *                If null, then the empty set will be
     *                used by default, so nothing will be kept
     *                by this filter.  (DO NOT PASS NULL.)
     */
    public MemberOf (
            Collection<MEMBER> members
            )
    {
        if ( members == null )
        {
            // Empty set.
            this.setToKeep = new LinkedHashSet<MEMBER> ();
        }
        else
        {
            // Defensive copy.
            this.setToKeep = new LinkedHashSet<MEMBER> ( members );
        }
    }


    /**
     * <p>
     * Creates a new MemberOf filter.
     * </p>
     *
     * @param members The Iterable whose members will be kept.
     *                Must not contain any nulls.
     *                If null, then the empty set will be
     *                used by default, so nothing will be kept
     *                by this filter.  (DO NOT PASS NULL.)
     */
    public MemberOf (
            Iterable<MEMBER> members
            )
    {
        if ( members == null )
        {
            // Empty set.
            this.setToKeep = new LinkedHashSet<MEMBER> ();
        }
        else
        {
            // Defensive copy.
            this.setToKeep = new LinkedHashSet<MEMBER> ();
            for ( MEMBER member : members )
            {
                this.setToKeep.add ( member );
            }
        }
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
            Object object
            )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final MemberOf<?> that = (MemberOf<?>) object;
        final LinkedHashSet<MEMBER> this_set = this.setToKeep;
        final LinkedHashSet<?> that_set = that.setToKeep;
        if ( ! this_set.equals ( that_set ) )
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
            Object grain
            )
    {
        if ( grain == null )
        {
            return FilterState.DISCARDED;
        }
        else if ( this.setToKeep.contains ( grain ) )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.setToKeep.hashCode ();
    }


    /**
     * @return The Set whose members will be kept by this filter.
     *         A defensive copy is returned, so the caller can
     *         manipulate it to their heart's content.
     *         Never null.
     */
    public final LinkedHashSet<MEMBER> setToKeep ()
    {
        // Defensive copy.
        return new LinkedHashSet<MEMBER> ( this.setToKeep );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " "
            + StringRepresentation.of ( this.setToKeep,
                                        StringRepresentation.DEFAULT_ARRAY_LENGTH );
    }
}
