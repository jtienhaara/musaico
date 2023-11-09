package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import musaico.foundation.filter.Filter;

import musaico.foundation.structure.ClassName;


public abstract class AbstractTagged<TAGGED extends Tagged<TAGGED>>
    implements Tagged<TAGGED>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    private final Serializable lock = new String ( "lock" );
    private Tag [] tags;

    public AbstractTagged ()
    {
        this ( new Tag [ 0 ] );
    }

    public AbstractTagged (
            Tag ... tags
            )
    {
        if ( tags == null )
        {
            this.tags = new Tag [ 0 ];
        }
        else
        {
            this.tags = new Tag [ tags.length ];
            System.arraycopy ( tags, 0,
                               this.tags, 0, tags.length );
            for ( int t = 0; t < this.tags.length; t ++ )
            {
                if ( this.tags [ t ] == null )
                {
                    this.tags [ t ] = Tag.NONE;
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED clearTags ()
    {
        synchronized ( this.lock )
        {
            this.tags = new Tag [ 0 ];
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED discardTags (
            String ... names
            )
    {
        if ( names == null
             || names.length == 0 )
        {
            // SuppressWarnings("unchecked"): Cast this - TAGGED
            return (TAGGED) this;
        }

        final Set<String> names_set =
            new HashSet<String> ();
        for ( String name : names )
        {
            names_set.add ( name );
        }

        final List<Tag> kept_tags =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            for ( Tag tag : this.tags )
            {
                if ( ! names_set.contains ( tag.name () ) )
                {
                    kept_tags.add ( tag );
                }
            }

            if ( kept_tags.size () != this.tags.length )
            {
                final Tag [] template = new Tag [ kept_tags.size () ];
                this.tags = kept_tags.toArray ( template );

                this.updateTags ();
            }
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED discardTags (
            Tag ... tags
            )
    {
        if ( tags == null
             || tags.length == 0 )
        {
            // SuppressWarnings("unchecked"): Cast this - TAGGED
            return (TAGGED) this;
        }

        final Set<Tag> tags_set =
            new HashSet<Tag> ();
        for ( Tag tag : tags )
        {
            tags_set.add ( tag );
        }

        final List<Tag> kept_tags =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            for ( Tag tag : this.tags )
            {
                if ( ! tags_set.contains ( tag ) )
                {
                    kept_tags.add ( tag );
                }
            }

            if ( kept_tags.size () != this.tags.length )
            {
                final Tag [] template = new Tag [ kept_tags.size () ];
                this.tags = kept_tags.toArray ( template );

                this.updateTags ();
            }
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED filterTags (
            Filter<Tag> filter
            )
    {
        if ( filter == null )
        {
            // SuppressWarnings("unchecked"): Cast this - TAGGED
            return (TAGGED) this;
        }

        final List<Tag> kept_tags =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            for ( Tag tag : this.tags )
            {
                if ( filter.filter ( tag ).isKept () )
                {
                    kept_tags.add ( tag );
                }
            }

            if ( kept_tags.size () != this.tags.length )
            {
                final Tag [] template = new Tag [ kept_tags.size () ];
                this.tags = kept_tags.toArray ( template );

                this.updateTags ();
            }
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED keepTags (
            String ... names
            )
    {
        if ( names == null
             || names.length == 0 )
        {
            // SuppressWarnings("unchecked"): Cast this - TAGGED
            return (TAGGED) this;
        }

        final Set<String> names_set =
            new HashSet<String> ();
        for ( String name : names )
        {
            names_set.add ( name );
        }

        final List<Tag> kept_tags =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            for ( Tag tag : this.tags )
            {
                if ( names_set.contains ( tag.name () ) )
                {
                    kept_tags.add ( tag );
                }
            }

            if ( kept_tags.size () != this.tags.length )
            {
                final Tag [] template = new Tag [ kept_tags.size () ];
                this.tags = kept_tags.toArray ( template );

                this.updateTags ();
            }
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED keepTags (
            Tag ... tags
            )
    {
        if ( tags == null
             || tags.length == 0 )
        {
            // SuppressWarnings("unchecked"): Cast this - TAGGED
            return (TAGGED) this;
        }

        final Set<Tag> tags_set =
            new HashSet<Tag> ();
        for ( Tag tag : tags )
        {
            tags_set.add ( tag );
        }

        final List<Tag> kept_tags =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            for ( Tag tag : this.tags )
            {
                if ( tags_set.contains ( tag ) )
                {
                    kept_tags.add ( tag );
                }
            }

            if ( kept_tags.size () != this.tags.length )
            {
                final Tag [] template = new Tag [ kept_tags.size () ];
                this.tags = kept_tags.toArray ( template );

                this.updateTags ();
            }
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    @SuppressWarnings("unchecked") // Cast this - TAGGED
    public final TAGGED tag (
            Tag ... tags
            )
    {
        if ( tags == null
             || tags.length == 0 )
        {
            // SuppressWarnings("unchecked"): Cast this - TAGGED
            return (TAGGED) this;
        }

        synchronized ( this.lock )
        {
            final Tag [] old_tags = this.tags;
            this.tags = new Tag [ old_tags.length + tags.length ];
            System.arraycopy ( old_tags, 0,
                               this.tags, 0, old_tags.length );
            System.arraycopy ( tags, 0,
                               this.tags, old_tags.length, tags.length );

            this.updateTags ();
        }

        // SuppressWarnings("unchecked"): Cast this - TAGGED
        return (TAGGED) this;
    }

    @Override
    public final String [] tagNames (
            Filter<Tag> filter
            )
    {
        final List<String> filtered_names_list =
            new ArrayList<String> ();
        synchronized ( this.lock )
        {
            if ( filter == null )
            {
                final String [] unfiltered_names =
                    new String [ this.tags.length ];
                for ( int t = 0; t < this.tags.length; t ++ )
                {
                    unfiltered_names [ t ] = this.tags [ t ].name ();
                }
                return unfiltered_names;
            }

            for ( Tag tag : this.tags )
            {
                if ( filter.filter ( tag ).isKept () )
                {
                    filtered_names_list.add ( tag.name () );
                }
            }
        }

        final String [] template = new String [ filtered_names_list.size () ];
        final String [] filtered_names =
            filtered_names_list.toArray ( template );

        return filtered_names;
    }

    @Override
    public final String [] tagNames (
            String ... names
            )
    {
        final Set<String> names_set =
            new HashSet<String> ();
        if ( names != null )
        {
            for ( String name : names )
            {
                names_set.add ( name );
            }
        }

        final List<String> filtered_names_list =
            new ArrayList<String> ();
        synchronized ( this.lock )
        {
            if ( names == null
                 || names.length == 0 )
            {
                final String [] unfiltered_names =
                    new String [ this.tags.length ];
                for ( int t = 0; t < this.tags.length; t ++ )
                {
                    unfiltered_names [ t ] = this.tags [ t ].name ();
                }
                return unfiltered_names;
            }

            for ( Tag tag : this.tags )
            {
                if ( names_set.contains ( tag.name () ) )
                {
                    filtered_names_list.add ( tag.name () );
                }
            }
        }

        final String [] template = new String [ filtered_names_list.size () ];
        final String [] filtered_names =
            filtered_names_list.toArray ( template );

        return filtered_names;
    }

    @Override
    public final String [] tagNamesFrom (
            Tag ... exact_tags
            )
    {
        final Set<Tag> tags_set =
            new HashSet<Tag> ();
        if ( exact_tags != null )
        {
            for ( Tag tag : exact_tags )
            {
                tags_set.add ( tag );
            }
        }

        final List<String> filtered_names_list =
            new ArrayList<String> ();
        synchronized ( this.lock )
        {
            if ( tags == null
                 || tags.length == 0 )
            {
                final String [] unfiltered_names =
                    new String [ this.tags.length ];
                for ( int t = 0; t < this.tags.length; t ++ )
                {
                    unfiltered_names [ t ] = this.tags [ t ].name ();
                }
                return unfiltered_names;
            }

            for ( Tag tag : this.tags )
            {
                if ( tags_set.contains ( tag ) )
                {
                    filtered_names_list.add ( tag.name () );
                }
            }
        }

        final String [] template = new String [ filtered_names_list.size () ];
        final String [] filtered_names =
            filtered_names_list.toArray ( template );

        return filtered_names;
    }


    @Override
    public final Tag [] tags (
            Filter<Tag> filter
            )
    {
        final List<Tag> filtered_tags_list =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            if ( filter == null )
            {
                final Tag [] unfiltered_tags =
                    new Tag [ this.tags.length ];
                System.arraycopy ( this.tags, 0,
                                   unfiltered_tags, 0, this.tags.length );
                return unfiltered_tags;
            }

            for ( Tag tag : this.tags )
            {
                if ( filter.filter ( tag ).isKept () )
                {
                    filtered_tags_list.add ( tag );
                }
            }
        }

        final Tag [] template = new Tag [ filtered_tags_list.size () ];
        final Tag [] filtered_tags =
            filtered_tags_list.toArray ( template );

        return filtered_tags;
    }

    @Override
    public final Tag [] tags (
            String ... names
            )
    {
        final Set<String> names_set =
            new HashSet<String> ();
        if ( names != null )
        {
            for ( String name : names )
            {
                names_set.add ( name );
            }
        }

        final List<Tag> filtered_tags_list =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            if ( names == null
                 || names.length == 0 )
            {
                final Tag [] unfiltered_tags =
                    new Tag [ this.tags.length ];
                System.arraycopy ( this.tags, 0,
                                   unfiltered_tags, 0, this.tags.length );
                return unfiltered_tags;
            }

            for ( Tag tag : this.tags )
            {
                if ( names_set.contains ( tag.name () ) )
                {
                    filtered_tags_list.add ( tag );
                }
            }
        }

        final Tag [] template = new Tag [ filtered_tags_list.size () ];
        final Tag [] filtered_tags =
            filtered_tags_list.toArray ( template );

        return filtered_tags;
    }

    @Override
    public final Tag [] tagsFrom (
            Tag ... exact_tags
            )
    {
        final Set<Tag> tags_set =
            new HashSet<Tag> ();
        if ( exact_tags != null )
        {
            for ( Tag tag : exact_tags )
            {
                tags_set.add ( tag );
            }
        }

        final List<Tag> filtered_tags_list =
            new ArrayList<Tag> ();
        synchronized ( this.lock )
        {
            if ( exact_tags == null
                 || exact_tags.length == 0 )
            {
                final Tag [] unfiltered_tags =
                    new Tag [ this.tags.length ];
                System.arraycopy ( this.tags, 0,
                                   unfiltered_tags, 0, this.tags.length );
                return unfiltered_tags;
            }

            for ( Tag tag : this.tags )
            {
                if ( tags_set.contains ( tag ) )
                {
                    filtered_tags_list.add ( tag );
                }
            }
        }

        final Tag [] template = new Tag [ filtered_tags_list.size () ];
        final Tag [] filtered_tags =
            filtered_tags_list.toArray ( template );

        return filtered_tags;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();

        final Tag [] maybe_name_tag = this.tags ( Tag.NAME_TAG_NAME );
        if ( maybe_name_tag != null
             && maybe_name_tag.length == 1
             && maybe_name_tag [ 0 ] != null )
        {
            sbuf.append ( "" + maybe_name_tag [ 0 ].value () );
            return sbuf.toString ();
        }

        final Tag [] maybe_id_tag = this.tags ( Tag.ID_TAG_NAME );
        if ( maybe_id_tag != null
             && maybe_id_tag.length == 1
             && maybe_id_tag [ 0 ] != null )
        {
            sbuf.append ( "" + maybe_id_tag [ 0 ].value () );
            return sbuf.toString ();
        }

        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( "#" + this.hashCode () );

        return sbuf.toString ();
    }

    private void updateTags ()
    {
        // The tags have been modified.
        // Make sure we set Tag.TAGGED
        // and anything else important.
        // !!!
    }
}
