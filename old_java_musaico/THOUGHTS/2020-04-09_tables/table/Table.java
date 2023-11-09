package table;

import java.io.PrintStream;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.regex.Pattern;


public class Table
{
    public static final Table EMPTY =
        new Table ( new TableEntry [ 0 ] );

    public static final Table SELF =
        new Table ( new TableEntry [ 0 ] );


    private final TableEntry [] entries;
    private final int hashCode;

    // MUTABLE, protect critical sections with synchronized ( this.tagLock ):
    private final Map<String, Tag> tags;
    private final Serializable tagLock = new String ( "tagLock" );

    // MUTABLE, protect critical sections with synchronized ( this.cacheLock ):
    private final Map<String, TableEntry []> cache =
        new HashMap<String, TableEntry []> ();
    private final Serializable cacheLock = new String ( "cacheLock" );

    protected Table (
            TableEntry [] entries,
            Tag ... tags
            )
    {
        if ( entries == null )
        {
            this.entries = new TableEntry [ 0 ];
        }
        else
        {
            this.entries = entries;
        }

        this.tags = new HashMap<String, Tag> ();
        if ( tags != null )
        {
            for ( Tag tag : tags )
            {
                final String id = tag.id ();
                this.tags.put ( id, tag );
            }
        }

        int hash_code = 0;
        for ( int e = 0; e < this.entries.length; e ++ )
        {
            hash_code += this.entries [ e ].hashCode ();

            this.entries [ e ] =
                this.entries [ e ].replaceSelfWith ( this );
        }
        this.hashCode = hash_code;
    }

    public final TableEntry [] entries ()
    {
        final TableEntry [] duplicate = new TableEntry [ this.entries.length ];
        System.arraycopy ( this.entries, 0,
                           duplicate, 0, this.entries.length );
        return duplicate;
    }

    public final TableEntry [] entries (
            String regular_expression
            )
    {
        if ( regular_expression == null )
        {
            return TableEntry.EMPTY_ARRAY;
        }

        final TableEntry [] matching_entries;
        synchronized ( this.cacheLock )
        {
            final TableEntry [] cached_entries =
                this.cache.get ( regular_expression );
            if ( cached_entries != null )
            {
                return cached_entries;
            }

            Pattern pattern = null;
            try
            {
                pattern = Pattern.compile ( regular_expression );
            }
            catch ( Exception e )
            {
                pattern = Pattern.compile (
                              Pattern.quote ( regular_expression )
                              );
            }

            final List<TableEntry> matching_entries_list =
                new ArrayList<TableEntry> ();
            for ( TableEntry entry : this.entries )
            {
                final String name = entry.name ();
                if ( pattern.matcher ( name ).find () )
                {
                    matching_entries_list.add ( entry );
                }
            }

            final TableEntry [] template =
                new TableEntry [ matching_entries_list.size () ];
            matching_entries =
                matching_entries_list.toArray ( template );

            this.cache.put ( regular_expression, matching_entries );
        }

        return matching_entries;
    }

    public final Tag tagOrNull ( String id )
    {
        synchronized ( this.tagLock )
        {
            return this.tags.get ( id );
        }
    }

    public final Tag [] tags ()
    {
        final Tag [] tags;
        synchronized ( this.tagLock )
        {
            final Tag [] template = new Tag [ this.tags.size () ];
            tags = this.tags.values ().toArray ( template );
        }

        Arrays.sort ( tags, new TagIDComparator () );

        return tags;
    }

    public final Tag [] tags (
            String regular_expression
            )
    {
        if ( regular_expression == null )
        {
            return Tag.EMPTY_ARRAY;
        }

        Pattern pattern = null;
        try
        {
            pattern = Pattern.compile ( regular_expression );
        }
        catch ( Exception e )
        {
            pattern = Pattern.compile (
                          Pattern.quote ( regular_expression )
                          );
        }

        final List<Tag> matching_tags_list =
            new ArrayList<Tag> ();
        synchronized ( this.tagLock )
        {
            for ( Tag tag : this.tags.values () )
            {
                final String id = tag.id ();
                if ( pattern.matcher ( id ).find () )
                {
                    matching_tags_list.add ( tag );
                }
            }
        }

        final Tag [] template =
            new Tag [ matching_tags_list.size () ];
        final Tag [] matching_tags =
            matching_tags_list.toArray ( template );

        Arrays.sort ( matching_tags, new TagIDComparator () );

        return matching_tags;
    }

    public final boolean equals (
            Object object
            )
    {
        if ( object == null )
        {
            return false;
        }
        else if ( object == this )
        {
            return true;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final Table that = (Table) object;
        if ( this.entries == null )
        {
            if ( that.entries != null )
            {
                return false;
            }
        }
        else if ( that.entries == null )
        {
            return false;
        }
        else if ( ! Arrays.equals ( this.entries, that.entries ) )
        {
            return false;
        }

        return true;
    }

    public final int hashCode ()
    {
        return this.hashCode;
    }

    public final void print (
            PrintStream ps
            )
    {
        final String table_string = this.toString ();
        ps.println ( table_string );
        for ( TableEntry entry : this.entries )
        {
            final String entry_string = entry.toString ();
            ps.print ( "    " );
            ps.println ( entry_string );
        }
    }

    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Table#" + this.hashCode () );

        final Tag [] tags = this.tags ();
        boolean is_first_tag = true;
        for ( Tag tag : tags )
        {
            if ( is_first_tag )
            {
                sbuf.append ( " [ " );
                is_first_tag = false;
            }
            else
            {
                sbuf.append ( "; " );
            }

            sbuf.append ( "" + tag );
        }

        if ( ! is_first_tag )
        {
            sbuf.append ( " ]" );
        }

        return sbuf.toString ();
    }
}
