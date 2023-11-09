package musaico.foundation.filter.stream;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.filter.FilterState;

import musaico.foundation.filter.membership.MemberOf;

import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Discards each container unless it contains all of the members of
 * a specific set of Objects.
 * </p>
 *
 * <p>
 * An empty container is kept.
 * </p>
 *
 *
 * <p>
 * In Java, every FilterStream must be Serializable in order to play
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
 * @see musaico.foundation.filter.stream.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.stream.MODULE#LICENSE
 */
public class KeptAllMembers<MEMBER extends Object>
    implements FilterStream<MEMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Lock critical sections on this object:
    private final Serializable lock = new String ( "lock" );

    // MUTABLE:
    // The lookup of members, all of which must be kept when filtering
    // a container in order for the container to be kept.
    private final LinkedHashMap<MEMBER, Boolean> keptMembers;


    /**
     * <p>
     * Creates a new KeptAllMembers.
     * </p>
     *
     * @param member_of_filter The MemberOfFilter whose members are to be kept.
     *                         The filter itself is not used.  Instead, its
     *                         <code> setToKeep () </code> is extracted.
     *                         If null, then an empty set of members will be used.
     *                         DO NOT PASS NULL, DO NOT COLLECT 200 DOLLARS!
     */
    public KeptAllMembers (
            MemberOf<MEMBER> member_of_filter
            )
    {
        this ( member_of_filter == null // members_to_keep
                   ? null
                   : member_of_filter.setToKeep () );
    }


    /**
     * <p>
     * Creates a new KeptAllMembers.
     * </p>
     *
     * @param members The members to be kept.
     *                If null, then an empty set of members will be used.
     *                DO NOT PASS NULL!
     */
    public KeptAllMembers (
            Collection<MEMBER> members
            )
    {
        this.keptMembers = new LinkedHashMap<MEMBER, Boolean> ();
        if ( members != null )
        {
            for ( MEMBER member : members )
            {
                this.keptMembers.put ( member, Boolean.FALSE );
            }
        }
    }


    /**
     * <p>
     * Creates a new KeptAllMembers.
     * </p>
     *
     * @param members The members to be kept.
     *                If null, then an empty set of members will be used.
     *                DO NOT PASS NULL!
     */
    @SuppressWarnings("unchecked") // Generic varargs.
    public KeptAllMembers (
            MEMBER ... members
            )
    {
        this.keptMembers = new LinkedHashMap<MEMBER, Boolean> ();
        if ( members != null )
        {
            for ( MEMBER member : members )
            {
                this.keptMembers.put ( member, Boolean.FALSE );
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

        final KeptAllMembers<?> that = (KeptAllMembers<?>) object;
        final LinkedHashMap<MEMBER, Boolean> this_kept_members;
        synchronized ( this.lock )
        {
            if ( this.keptMembers == null )
            {
                this_kept_members = null;
            }
            else
            {
                this_kept_members = new LinkedHashMap<MEMBER, Boolean> ( this.keptMembers );
            }
        }
        final LinkedHashMap<?, Boolean> that_kept_members;
        synchronized ( that.lock )
        {
            if ( that.keptMembers == null )
            {
                that_kept_members = null;
            }
            else
            {
                that_kept_members = new LinkedHashMap<Object, Boolean> ( that.keptMembers );
            }
        }

        if ( this_kept_members == null )
        {
            if ( that_kept_members != null )
            {
                return false;
            }
        }
        else if ( that_kept_members == null )
        {
            return false;
        }
        else if ( ! this_kept_members.equals ( that_kept_members ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filtered(java.lang.Object, musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filtered (
            MEMBER element,
            FilterState element_filter_state
            )
        throws NullPointerException
    {
        if ( element_filter_state == null
             || element_filter_state == FilterStream.CONTINUE
             || element_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else if ( element_filter_state.isKept () )
        {
            synchronized ( this.lock )
            {
                this.keptMembers.put ( element, Boolean.TRUE );
            }
        }

        return FilterStream.CONTINUE;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterEnd(musaico.foundation.filter.FilterState)
     */
    @Override
    public final FilterState filterEnd (
            FilterState container_filter_state
            )
        throws NullPointerException
    {
        if ( container_filter_state == null
             || container_filter_state == FilterStream.CONTINUE
             || container_filter_state == FilterStream.END )
        {
            return FilterState.DISCARDED;
        }
        else if ( ! container_filter_state.isKept () )
        {
            return container_filter_state;
        }

        synchronized ( this.lock )
        {
            for ( MEMBER member : this.keptMembers.keySet () )
            {
                final boolean is_kept = this.keptMembers.get ( member ).booleanValue ();
                if ( ! is_kept )
                {
                    return FilterState.DISCARDED;
                }
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see musaico.foundation.filter.stream.FilterStream#filterStart()
     */
    @Override
    public final FilterState filterStart ()
    {
        synchronized ( this.lock )
        {
            for ( MEMBER member : this.keptMembers.keySet () )
            {
                this.keptMembers.put ( member, Boolean.FALSE );
            }
        }

        return FilterState.KEPT;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        final int members_hashcode;
        synchronized ( this.lock )
        {
            members_hashcode = this.keptMembers.hashCode ();
        }

        return ClassName.of ( this.getClass () ).hashCode ()
            * members_hashcode;
    }


    
    /**
     * @return The lookup of members that were kept (true) / not kept (false)
     *         during the most recent filtering.  Never null.
     */
    public final LinkedHashMap<MEMBER, Boolean> keptMembers ()
    {
        synchronized ( this.lock )
        {
            return new LinkedHashMap<MEMBER, Boolean> ( this.keptMembers );
        }
    }


    /**
     * @return The members to track.  Can be empty.  Can contain null elements.
     *         Never null.
     */
    public final LinkedHashSet<MEMBER> members ()
    {
        synchronized ( this.lock )
        {
            return new LinkedHashSet<MEMBER> ( this.keptMembers.keySet () );
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final List<MEMBER> members;
        synchronized ( this.lock )
        {
            members = new ArrayList<MEMBER> ( this.keptMembers.keySet () );
        }

        final String members_string =
            StringRepresentation.of ( members,
                                      StringRepresentation.DEFAULT_ARRAY_LENGTH );

        return ClassName.of ( this.getClass () + " " + members_string );
    }
}
