package musaico.foundation.wiring;

import java.io.Serializable;

import java.util.UUID;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.KeepAll;

import musaico.foundation.structure.StringRepresentation;

public class Tag
    implements Comparable<Tag>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    // The version of the parent module, YYYYMMDD format.
    private static final long serialVersionUID = MODULE.VERSION;


    /** The name of the Tag that represents unique ID of a tagged object. */
    public static final String ID_TAG_NAME = "musaico.id";

    /** The name of the Tag that represents the name of a tagged object. */
    public static final String NAME_TAG_NAME = "musaico.name";

    /** No flags for this Tag. */
    public static int FLAG_NONE = 0x0000;

    /** Tag.toString () just outputs [ value ] instead of [ name = value ]. */
    public static int FLAG_SHORT = 0x0001;

    /** This Tag sticks to Carriers that pass through Tagged conductors. */
    public static int FLAG_STICKY = 0x0002;


    public static final Tag NONE =
        new Tag ( "musaico.none",   // name
                  0,                // version
                  Boolean.FALSE,    // value
                  Tag.FLAG_SHORT ); // flags


    private final String name;
    private final int version;

    private final Object value;

    private final int flags;

    public Tag (
            String name,
            int version,
            Object value,
            int ... flags
            )
    {
        if ( name == null )
        {
            this.name = Tag.NONE.name;
        }
        else
        {
            this.name = name;
        }
        this.version = version;
        this.value = value; // Can be null.

        if ( flags == null )
        {
            this.flags = 0;
        }
        else
        {
            int current_flags = 0;
            for ( int flag : flags )
            {
                if ( flag > 0 )
                {
                    current_flags |= flag;
                }
            }
            this.flags = current_flags;
        }
    }

    public Tag (
            String name
            )
    {
        this ( name,            // name
               0,               // version
               Boolean.TRUE,    // value
               Tag.FLAG_NONE ); // flags
    }

    @Override
    @SuppressWarnings("unchecked") // Cast Comparable<X> to Comparable<Object>.
    public final int compareTo (
            Tag that
            )
    {
        if ( that == this )
        {
            return 0;
        }
        else if ( that == null )
        {
            return Integer.MIN_VALUE + 1;
        }
        else if ( this.name == null )
        {
            if ( that.name == null )
            {
                return 0;
            }
            else
            {
                return Integer.MAX_VALUE;
            }
        }
        else if ( that.name == null )
        {
            return Integer.MIN_VALUE + 1;
        }

        final int name_comparison =
            this.name.compareTo ( that.name );
        if ( name_comparison != 0 )
        {
            return name_comparison;
        }

        if ( this.version != that.version )
        {
            return this.version - that.version;
        }

        if ( this.value == null )
        {
            if ( that.value == null )
            {
                return 0;
            }
            else
            {
                return Integer.MAX_VALUE;
            }
        }
        else if ( that.value == null )
        {
            return Integer.MIN_VALUE + 1;
        }
        else if ( this.value.getClass () != that.value.getClass () )
        {
            return this.value.getClass ().getName ().compareTo ( that.value.getClass ().getName () );
        }

        if ( this.value instanceof Comparable )
        {
            // SuppressWarnings("unchecked"): Cast Comparable<X> to Comparable<Object>.
            final Comparable<Object> this_comparable =
                (Comparable<Object>) this.value;
            // SuppressWarnings("unchecked"): Cast Comparable<X> to Comparable<Object>.
            final Comparable<Object> that_comparable =
                (Comparable<Object>) that.value;
            return this_comparable.compareTo ( that_comparable );
        }

        if ( this.value.equals ( that.value ) )
        {
            return 0;
        }

        final String this_string = "" + this.value;
        final String that_string = "" + that.value;
        final int string_comparison = this_string.compareTo ( that_string );
        if ( string_comparison != 0 )
        {
            return string_comparison;
        }

        final int this_hash = this.hashCode ();
        final int that_hash = that.hashCode ();
        if ( this_hash != that_hash )
        {
            return this_hash - that_hash;
        }

        // Not much else we can do, unfortunately.
        return Integer.MIN_VALUE;
    }

    @Override
    public final int hashCode ()
    {
        return this.name.hashCode ();
    }

    @Override
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final Tag that = (Tag) object;
        if ( this.version != that.version )
        {
            return false;
        }

        if ( this.name == null )
        {
            if ( that.name != null )
            {
                return false;
            }
        }
        else if ( that.name == null )
        {
            return false;
        }
        else if ( ! this.name.equals ( that.name ) )
        {
            return false;
        }

        if ( this.value == null )
        {
            if ( that.value != null )
            {
                return false;
            }
        }
        else if ( that.value == null )
        {
            return false;
        }
        else if ( this.value.getClass () != that.value.getClass () )
        {
            return false;
        }
        else if ( ! this.value.equals ( that.value ) )
        {
            return false;
        }

        return true;
    }

    public final String name ()
    {
        return this.name;
    }

    public final Object value ()
    {
        return this.value;
    }

    public final int version ()
    {
        return this.version;
    }

    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "[ " );

        if ( ( this.flags & Tag.FLAG_SHORT ) == Tag.FLAG_SHORT )
        {
            // E.g. "[ 7 ]" for length 7L,
            // or "[ String ]" for type "String".
            final String value_string =
                StringRepresentation.of (
                    this.value,
                    StringRepresentation.DEFAULT_OBJECT_LENGTH );
            sbuf.append ( value_string );
        }
        else
        {
            sbuf.append ( this.name );
            if ( this.version != 0 )
            {
                sbuf.append ( "#" + this.version );
            }

            if ( this.value != Boolean.TRUE )
            {
                final String value_string =
                    StringRepresentation.of (
                        this.value,
                        StringRepresentation.DEFAULT_OBJECT_LENGTH );
                sbuf.append ( " = " );
                sbuf.append ( value_string );
            }
        }

        sbuf.append ( " ]" );
        return sbuf.toString ();
    }
}
