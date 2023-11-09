package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.container.Container;
import musaico.foundation.container.ErrorContainer;
import musaico.foundation.container.MutableContainer;

import musaico.foundation.contract.guarantees.Return;


/**
 * <p>
 * Standard Tags used by various wiring classes.
 * </p>
 *
 *
 * <p>
 * In Java, every Tag must be Serializable in order to play
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
 * @see musaico.foundation.wiring.MODULE#COPYRIGHT
 * @see musaico.foundation.wiring.MODULE#LICENSE
 */
public class Tags
    implements Iterable<Tag>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    public static final Tags NONE =
        new Tags ();


    private final MutableContainer<Tag> container;

    @SuppressWarnings("unchecked") // Cast empty sorted Container<Tag> - MutableContainer<Tag>.
    public Tags ()
    {
        // SuppressWarnings("unchecked") Cast empty sorted Container<Tag> - MutableContainer<Tag>.
        this.container = (MutableContainer<Tag>)
            new MutableContainer<Tag> ( Tag.class ) // No elements.
                .sort ( TagPathOrder.ORDER );
    }

    @SuppressWarnings("unchecked") // Cast empty sorted Container<Tag> - MutableContainer<Tag>.
    public Tags (
            Tags source_tags
            )
    {
        if ( source_tags == null )
        {
            // SuppressWarnings("unchecked") Cast empty sorted Container<Tag> - MutableContainer<Tag>.
            this.container = (MutableContainer<Tag>)
                new MutableContainer<Tag> ( Tag.class ) // No elements.
                    .sort ( TagPathOrder.ORDER );
        }
        else
        {
            // TODO !!! This should really be copy-on-write.
            // TODO !!! But I'm too lazy to add a 3rd tyoe
            // TODO !!! of Container right now.
            // TODO !!! Plus, dinner's almost ready.  And
            // TODO !!! Hazelnut is looking at me with doleful
            // TODO !!! Plott Hound eyes, because she knows
            // TODO !!! what time it is, too.
            this.container = source_tags.container.duplicateMutable ();
        }
    }

    /**
     * @return The Container underlying these Tags, in case you want to
     *         do things not available from the Tags interface itself.
     *         Be sure to keep the Container in <code> TagPathOrder.ORDER </code>,
     *         otherwise you'll bork it horribly.  Never null.
     */
    public final MutableContainer<Tag> container ()
        throws Return.NeverNull.Violation
    {
        return this.container;
    }

    /**
     * <p>
     * Retrieves the specified Tag from these Tags, by its id (ignoring
     * its data), and returns the corresponding Tag from this collection.
     * </p>
     *
     * <p>
     * For example, to find the "musaico.tap.(id)" Tag for a specific Tap
     * id (), one would cal:
     * </p>
     *
     * <pre>
     *     final Tap tap = ...;
     *     final Pulse context = ...;
     *     final Tags tags = context.tags ();
     *     final Tag lookup = TagLibrary.musaico.tap.tag ( tap, Pulse.NONE );
     *     final Tag [] tap_tag = tags.find ( lookup );
     *     if ( tap_tag.length == 1 )
     *     {
     *         ...Found it...
     *     }
     *     else
     *     {
     *         ...No such Tag...
     *     }
     *     // Uuuuuuuuuuuuuuuggggggggggglllllllllllyyyyyyyyyyy...  :(
     * </pre>
     *
     * @param tag The Tag to lookup in this collection.  Must not be null.
     *
     * @return Either the one specified Tag, or an empty array, if the
     *         specified Tag does not exist.  Never null.  Never contains
     *         any null elements.
     */
    public final Tag [] find (
            Tag tag
            )
        throws Return.NeverNull.Violation,
               Return.AlwaysContainsNoNulls.Violation
    {
        if ( tag == null )
        {
            return new Tag [ 0 ];
        }

        final Container<Tag> maybe_found =
            this.container.find ( TagPathOrder.ORDER,
                                  tag );
        if ( maybe_found == null
             || maybe_found instanceof ErrorContainer )
        {
            return new Tag [ 0 ];
        }

        final Tag [] maybe_tag = maybe_found.elements ();

        if ( maybe_tag == null )
        {
            return new Tag [ 0 ];
        }
        else if ( maybe_tag.length > 1 )
        {
            // Uh-oh!
            return new Tag [ 0 ];
        }

        return maybe_tag;
    }

    /**
     * <p>
     * Inserts the specified Tag(s).
     * </p>
     *
     * @param tags The Tag(s) to add or replace.  Each will be inserted
     *             in order of Tag ID.  If null, nothing will happen.
     *             If any null elements, they will be replaced with
     *             <code> Tag.NONE </code>.
     *
     * @return These Tags.  Never null.
     */
    public final Tags insert (
            Tag ... tags
            )
        throws Return.NeverNull.Violation
    {
        if ( tags == null )
        {
            return this;
        }

        for ( int t = 0; t < tags.length; t ++ )
        {
            if ( tags [ t ] == null )
            {
                tags [ t ] = Tag.NONE;
            }
        }

        this.container.insert ( Container.InsertMode.OVERWRITE,
                                TagPathOrder.ORDER,
                                tags )
            .orThrow ();

        return this;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<Tag> iterator ()
    {
        return this.container.iterator ();
    }

    /**
     * <p>
     * Inserts the specified Tag(s).
     * </p>
     *
     * @param tags The Tag(s) to remove.  If null, nothing will happen.
     *             If any null elements, then <code> Tag.NONE </code>
     *             will be removed instead.
     *
     * @return This Tags.  Never null.
     */
    public final Tags remove (
            Tag ... tags
            )
        throws Return.NeverNull.Violation
    {
        if ( tags == null )
        {
            return this;
        }

        for ( int t = 0; t < tags.length; t ++ )
        {
            if ( tags [ t ] == null )
            {
                tags [ t ] = Tag.NONE;
            }
        }

        this.container.remove ( tags )
            .orThrow ();

        return this;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "tags " + this.container.toString ();
    }
}
