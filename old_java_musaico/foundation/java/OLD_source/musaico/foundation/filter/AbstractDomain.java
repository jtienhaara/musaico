package musaico.foundation.filter;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Provides the boilerplate code for most domains, allowing clients
 * to filter out members and non-members from sets of objects.
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
 * @see musaico.foundation.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.MODULE#LICENSE
 */
public abstract class AbstractDomain<MEMBER extends Object>
    implements Domain<MEMBER>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    // Every Domain must implement
    // musaico.foundation.filter.Filter#filter(java.lang.Object)


    /**
     * @see musaico.foundation.filter.Domain#member(java.lang.Object)
     */
    @Override
    public final List<MEMBER> member (
                                      MEMBER maybe_member
                                      )
    {
        final List<MEMBER> members = new ArrayList<MEMBER> ();
        if ( this.filter ( maybe_member ).isKept () )
        {
            members.add ( maybe_member );
        }

        return members;
    }


    /**
     * @see musaico.foundation.filter.Domain#nonMember(java.lang.Object)
     */
    @Override
    public final List<MEMBER> nonMember (
                                         MEMBER maybe_non_member
                                         )
    {
        final List<MEMBER> non_members = new ArrayList<MEMBER> ();
        if ( this.filter ( maybe_non_member ).isKept () )
        {
            non_members.add ( maybe_non_member );
        }

        return non_members;
    }
}
