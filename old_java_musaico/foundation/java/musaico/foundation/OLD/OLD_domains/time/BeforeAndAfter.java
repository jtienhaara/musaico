package musaico.foundation.domains.time;

import java.io.Serializable;


import musaico.foundation.domains.ClassName;
import musaico.foundation.domains.HashedObject;

import musaico.foundation.domains.comparability.LeftAndRight;


/**
 * <p>
 * Stores a "before" state and an "after" state, comparing the two
 * to detect changes.
 * </p>
 *
 * <p>
 * The "before" and "after" states are compared using some sort of
 * hash function (the simplest being <code> Object.hashCode () </code>).
 * If the "before" hash does not match the "after" hash, then the state
 * has changed.
 * </p>
 *
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
 * @see musaico.foundation.domains.time.MODULE#COPYRIGHT
 * @see musaico.foundation.domains.time.MODULE#LICENSE
 */
public class BeforeAndAfter
    extends LeftAndRight<HashedObject, HashedObject>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /**
     * <p>
     * Creates a new BeforeAndAfter with the specified "before" and "after"
     * HashedObjects representing some object which might or might
     * not be Unchanging (in the domain or not).
     * </p>
     *
     * @param before The state of the object before some operations
     *               took place.  Must not be null.
     *
     * @param after The state of the object after the operations
     *               took place.  Must not be null.
     */
    public BeforeAndAfter (
                           HashedObject before,
                           HashedObject after
                           )
        throws NullPointerException
    {
        // We want to call after.equals ( before ), so after is left,
        // before is right.
        super ( after, before );

        if ( before == null
             || after == null )
        {
            throw new NullPointerException ( "BeforeAndAfter: invalid HashedObjects"
                                             + " before = " + before
                                             + " and after = " + after );
        }
    }


    /**
     * @return The "before" state.  Never null.
     */
    public final HashedObject before ()
    {
        // We want to call after.equals ( before ), so after is left,
        // before is right.
        return this.right ();
    }


    /**
     * @return The "after" state.  Never null.
     */
    public final HashedObject after ()
    {
        // We want to call after.equals ( before ), so after is left,
        // before is right.
        return this.left ();
    }


    /**
     * @return True if the "after" state matches the "before" state.
     *         False if the before and after states differ.
     */
    public final boolean isUnchanged ()
    {
        return this.isEqual ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () ) + ": "
            + "before = " + this.before ()
            + ", "
            + "after = " + this.after ();
    }
}
