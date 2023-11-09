package musaico.foundation.wiring;

import musaico.foundation.filter.Filter;

public interface Tagged<TAGGED extends Tagged<TAGGED>>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public abstract TAGGED clearTags ();

    public abstract TAGGED discardTags (
            String ... names
            );

    public abstract TAGGED discardTags (
            Tag ... tags
            );

    public abstract TAGGED filterTags (
            Filter<Tag> filter
            );

    public abstract TAGGED keepTags (
            String ... names
            );

    public abstract TAGGED keepTags (
            Tag ... tags
            );

    public abstract TAGGED tag (
            Tag ... tags
            );

    public abstract String [] tagNames (
            Filter<Tag> filter
            );

    public abstract String [] tagNames (
            String ... names
            );

    public abstract String [] tagNamesFrom (
            Tag ... exact_tags
            );

    public abstract Tag [] tags (
            Filter<Tag> filter
            );

    public abstract Tag [] tags (
            String ... names
            );

    public abstract Tag [] tagsFrom (
            Tag ... exact_tags
            );
}
