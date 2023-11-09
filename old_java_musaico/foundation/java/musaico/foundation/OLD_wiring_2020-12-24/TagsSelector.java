package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.DiscardAll;

import musaico.foundation.filter.equality.EqualTo;

import musaico.foundation.filter.membership.MemberOf;


public class TagsSelector
    extends StandardSelector
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Filter<Tag> tagsFilter;

    public TagsSelector (
            String tag_name,
            Object tag_value
            )
    {
        this ( tag_name == null
                   ? "NULL TAG NAME"
                   : tag_name,
               tag_value == null
                   ? new DiscardAll<Object> ()
                   : new EqualTo<Object> ( tag_value ) );
    }

    public TagsSelector (
            String tag_name,
            Filter<Object> tag_value_filter
            )
    {
        this ( new TagFilter (
                   tag_name == null
                       ? "NULL TAG NAME"
                       : tag_name,
                   tag_value_filter == null
                       ? new DiscardAll<Object> ()
                       : tag_value_filter ) );
    }

    public TagsSelector (
            Tag ... tags
            )
    {
        this ( tags == null
                   ? new DiscardAll<Tag> ()
                   : new MemberOf<Tag> ( tags ) );
    }

    public TagsSelector (
            Collection<Tag> tags
            )
    {
        this ( tags == null
                   ? new DiscardAll<Tag> ()
                   : new MemberOf<Tag> ( tags ) );
    }

    public TagsSelector (
        Filter<Tag> tags_filter
        )
    {
        super ( tags_filter == null
                    ? new DiscardAll<Conductor> ()
                    : new TaggedFilter<Conductor> ( tags_filter ) );

        if ( tags_filter == null )
        {
            this.tagsFilter = new DiscardAll<Tag> ();
        }
        else
        {
            this.tagsFilter = tags_filter;
        }
    }

    public final Filter<Tag> tagsFilter ()
    {
        return this.tagsFilter;
    }
}
