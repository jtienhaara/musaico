package musaico.foundation.filter;

import java.io.Serializable;

import musaico.foundation.structure.ClassName;


/**
 * <p>
 * Indicates whether a filter operation KEPT or DISCARDED an object,
 * or possibly resulted in some other state specific to the filter.
 * </p>
 *
 *
 * <p>
 * In Java every FilterState must be Serializable in order to play
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class FilterState
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Filter state: kept the object (not filtered out). */
    public static final FilterState KEPT = new FilterState ( "KEPT",
                                                             true );

    /** Filter state: filtered out the object. */
    public static final FilterState DISCARDED = new FilterState ( "DISCARDED",
                                                                  false );


    // The name of this filter state, such as "KEPT" or "DISCARDED"
    // or "Discarded with a search hint", and so on.
    private final String name;

    // True if the filter passed the object through;
    // false if the object was filtered out.
    private final boolean isKept;


    /**
     * <p>
     * Creates a new FilterState.
     * </p>
     *
     * @param name The name of this filter state.  Must not be null.
     *
     * @param is_kept True if the filter passed the object through;
     *                false if the object was filtered out.
     *
     * @throws NullPointerException If any of the non-nullable parameters
     *                              is null.
     */
    public FilterState (
            String name,
            boolean is_kept
            )
        throws NullPointerException
    {
        if ( name == null )
        {
            throw new NullPointerException ( "Cannot create a "
                                             + ClassName.of ( this.getClass () )
                                             + " with name = " + name
                                             + " is_kept = " + is_kept );
        }

        this.name = name;
        this.isKept = is_kept;
    }


    /**
     * <p>
     * Logically ANDs this FilterState together with the specified one,
     * returning a kept result only if both states indicated kept.
     * </p>
     *
     * @param that The FilterState to logically AND with this one.
     *             If null, then FilterState.DISCARDED is returned.
     *             DO NOT PASS NULL.
     *
     * @return The logical AND of this FilterState and the specified one.
     *         Never null.
     */
    public FilterState and (
            FilterState that
            )
    {
        if ( that == null )
        {
            return FilterState.DISCARDED;
        }

        final boolean new_is_kept = this.isKept && that.isKept;
        if ( new_is_kept )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
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
        else if ( ! ( object instanceof FilterState ) )
        {
            return false;
        }

        final FilterState that = (FilterState) object;

        if ( this.isKept != that.isKept )
        {
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
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
        if ( this.isKept )
        {
            hash_code += 1;
        }
        else
        {
            hash_code -= 1;
        }

        hash_code += 31 * this.name.hashCode ();

        return hash_code;
    }


    /**
     * @return True if the filter passed the object through;
     *         false if the object was filtered out.
     */
    public final boolean isKept ()
    {
        return this.isKept;
    }


    /**
     * @return True if the object was filtered out;
     *         false if the filter passed the object through.
     */
    public final boolean isDiscarded ()
    {
        return ! this.isKept;
    }


    /**
     * @return The name of this filter state, such as "KEPT" or "DISCARDED"
     *         and so on.  Never null.
     */
    public final String name ()
    {
        return this.name;
    }


    /**
     * @see musaico.foundation.filter.FilterState#opposite()
     */
    public final FilterState negate ()
    {
        return this.opposite ();
    }


    /**
     * @return The "negation" of this FilterState.  For example, if this
     *         is <code> FilterState.KEPT </code>, then
     *         <code> opposite () </code> will return
     *         <code> FilterState.DISCARDED </code>.  And so on.
     *         Never null.
     */
    public FilterState opposite ()
    {
        if ( this.equals ( FilterState.KEPT ) )
        {
            return FilterState.DISCARDED;
        }
        else if ( this.equals ( FilterState.DISCARDED ) )
        {
            return FilterState.KEPT;
        }
        else if ( this.name.startsWith ( "OPPOSITE ( " )
                  && this.name.endsWith ( " )" ) )
        {
            final String original_name =
                this.name.substring ( "OPPOSITE ( ".length (),
                                      this.name.length () - " )".length () );
            final FilterState original = new FilterState ( original_name,
                                                           ! this.isKept );
            return original;
        }
        else
        {
            final String opposite_name = "OPPOSITE ( " + this.name + " )";
            final FilterState opposite = new FilterState ( opposite_name,
                                                           ! this.isKept );
            return opposite;
        }
    }


    /**
     * <p>
     * Logically ORs this FilterState together with the specified one,
     * returning a kept result only if both states indicated kept.
     * </p>
     *
     * @param that The FilterState to logically OR with this one.
     *             If null, then FilterState.DISCARDED is returned.
     *             DO NOT PASS NULL.
     *
     * @return The logical OR of this FilterState and the specified one.
     *         Never null.
     */
    public FilterState or (
            FilterState that
            )
    {
        if ( that == null )
        {
            return FilterState.DISCARDED;
        }

        final boolean new_is_kept = this.isKept || that.isKept;
        if ( new_is_kept )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return this.name;
    }


    /**
     * <p>
     * Logically EXCLUSIVE ORs this FilterState together with the specified one,
     * returning a kept result only if both states indicated kept.
     * </p>
     *
     * @param that The FilterState to logically EXCLUSIVE OR with this one.
     *             If null, then FilterState.DISCARDED is returned.
     *             DO NOT PASS NULL.
     *
     * @return The logical EXCLUSIVE OR of this FilterState
     *         and the specified one.
     *         Never null.
     */
    public FilterState xor (
            FilterState that
            )
    {
        if ( that == null )
        {
            return FilterState.DISCARDED;
        }

        final boolean new_is_kept =
            ( this.isKept && ! that.isKept )
            || ( ! this.isKept && that.isKept );
        if ( new_is_kept )
        {
            return FilterState.KEPT;
        }
        else
        {
            return FilterState.DISCARDED;
        }
    }
}
