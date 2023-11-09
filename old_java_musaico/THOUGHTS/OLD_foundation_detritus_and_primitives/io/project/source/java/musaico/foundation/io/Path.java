package musaico.foundation.io;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Pattern;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Represents a path in a file system or some similar system organized
 * hierarchically using slashes.
 * </p>
 *
 * <p>
 * Regardless of the system referred to (a file system in operating
 * system X, or an XPath, and so on), the same set of characters
 * is always used, and slash ("/") is always used as a hierarchical
 * separator.
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
 * <pre>
 * Copyright (c) 2009, 2011 Johann Tienhaara
 * All rights reserved.
 *
 * This file is part of Musaico.
 *
 * Musaico is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Musaico is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Musaico.  If not, see &lt;http://www.gnu.org/licenses/&gt;.
 * </pre>
 */
public class Path
    implements NaturallyOrdered<Path>, Identifier, Serializable
{
    /** Any of these characters will be escaped with a backslash
     *  when converting a Path to a String. */
    public static final Pattern ESCAPED_CHARACTERS =
        Pattern.compile ( "([^a-zA-Z0-9\\-_=\\+,\\.])" );

    /** Any of the escaped characters are replaced with this
     *  pattern. */
    public static final String ESCAPE_REPLACEMENT = "\\\\$1";


    /** The path, broken up into pieces. */
    private final String [] path;

    /** Whether this is an absolute path ("/a/b/c") or a
     *  relative path ("a/b/c"). */
    private final boolean isAbsolute;


    /**
     * <p>
     * Creates a new Path from the specified String.
     * </p>
     *
     * @param path_string The path, using slashes and backslashes
     *                    appropriately.  Must not be null.
     *
     * @throws I18nIllegalArgumentException If the path_string is null
     *                                      or is not formatted properly.
     */
    public Path (
                 String path_string
                 )
        throws I18nIllegalArgumentException
    {
        if ( path_string == null )
        {
            throw new I18nIllegalArgumentException ( "Invalid path [%path_string%]",
                                                     "path_string", path_string );
        }

        List<String> names = new ArrayList<String> ();
        int prev_char = -1;
        StringBuilder current_name = new StringBuilder ();
        boolean is_absolute = false;
        for ( int c = 0; c < path_string.length (); c ++ )
        {
            char curr_char = path_string.charAt ( c );

            switch ( prev_char )
            {
            case '\\':
                current_name.append ( (char) curr_char );
                prev_char = -1;
                break;

            default:
                switch ( curr_char )
                {
                case '\\':
                    prev_char = curr_char;
                    break;

                case '/':
                    if ( c == 0 )
                    {
                        is_absolute = true;
                    }

                    if ( current_name.length () > 0 )
                    {
                        String name = current_name.toString ();
                        names.add ( name );
                        current_name = new StringBuilder ();
                    }

                    prev_char = -1;
                    break;

                default:
                    current_name.append ( (char) curr_char );
                    prev_char = -1;
                }
            }
        }

        if ( current_name.length () > 0 )
        {
            String name = current_name.toString ();
            names.add ( name );
        }

        if ( prev_char != -1 )
        {
            throw new I18nIllegalArgumentException ( "Invalid trailing path character '[%invalid_char%]' in path [%path_string%]",
                                                     "invalid_char", (char) prev_char,
                                                     "path_string", path_string );
        }
        else if ( names.size () == 0 )
        {
            throw new I18nIllegalArgumentException ( "Invalid path [%path_string%]",
                                                     "path_string", path_string );
        }

        String [] template = new String [ names.size () ];
        this.path = names.toArray ( template );
        this.isAbsolute = is_absolute;
    }


    /**
     * <p>
     * Creates a new Path from the specified String [] names.
     * </p>
     *
     * @param path_names The path as an array of names.  Must not be null.
     *                   Must not contain any null elements.
     *
     * @param is_absolute True if the path is absolute ("/a/b/c"),
     *                    false if it is relative ("a/b/c").
     *
     * @throws I18nIllegalArgumentException If the path_string is invalid.
     */
    public Path (
                 String [] path_names,
                 boolean is_absolute
                 )
        throws I18nIllegalArgumentException
    {
        if ( path_names == null )
        {
            throw new I18nIllegalArgumentException ( "Invalid path [%path_names%]",
                                                     "path_names", path_names );
        }

        for ( int p = 0; p < path_names.length; p ++ )
        {
            if ( path_names [ p ] == null )
            {
                throw new I18nIllegalArgumentException ( "Invalid path [%path_names%]",
                                                         "path_names", path_names );
            }
        }

        this.path = path_names;
        this.isAbsolute = is_absolute;
    }


    /**
     * <p>
     * Creates a new path with the specified name(s) appended to the end.
     * </p>
     *
     * <p>
     * For example, to create a Path "/foo/bar" under "/foo",
     * call <code> foo_path.append ( "bar" ) </code>.
     * </p>
     *
     * @param names The name(s) to append.  Must not be null.
     *              Must not contain any nulls.  Must not be 0-length.
     *              Each name is a single level of path, so if "/"
     *              is included, it will be escaped on output.
     *              for example,
     *              <code> foo_path.append ( "male/female" ) </code>
     *              creates the path "/foo/male\/female".
     *
     * @return The newly created Path.  Never null.
     *
     * @throws I18nIllegalArgumentException If the parameters are invalid.
     */
    public Path append (
                        String... names
                        )
        throws I18nIllegalArgumentException
    {
        if ( names == null
             || names.length == 0 )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a path of [%parent_path%] with appended names [%names%]",
                                                     "parent_path", this,
                                                     "names", names );
        }

        for ( int n = 0; n < names.length; n ++ )
        {
            if ( names [ n ] == null )
            {
                throw new I18nIllegalArgumentException ( "Cannot create a path of [%parent_path%] with appended names [%names%]",
                                                         "parent_path", this,
                                                         "names", names );
            }
        }

        String [] all_names =
            new String [ this.path.length + names.length ];
        System.arraycopy ( this.path, 0,
                           all_names, 0, this.path.length );
        System.arraycopy ( names, 0,
                           all_names, this.path.length, names.length );

        Path new_path = new Path ( all_names, this.isAbsolute () );

        return new_path;
    }


    /**
     * <p>
     * Returns the child of this path.  For example, if this path
     * is "/a/b/c" then "b/c" is returned.
     * </p>
     *
     * @return The child Path of this path.  Never null.
     */
    public Path child ()
    {
        if ( this.path.length <= 1 )
        {
            return this;
        }

        final String [] child_names = new String [ this.path.length - 1 ];
        System.arraycopy ( this.path, 1,
                           child_names, 0, this.path.length - 1 );

        return new Path ( child_names, false );
    }


    /**
     * <p>
     * Returns the number of hierarchical names in this path.
     * </p>
     *
     * <p>
     * For example, "/a/b/c" has 3 levels, as does "a/b/c".
     * "/a/b/c/" is 4 deep; "a" is 1 deep; "/a" is 1 deep; and so on.
     * </p>
     *
     * @return This path's depth.  Always 0 or greater.
     */
    public int depth ()
    {
        return this.path.length;
    }


    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null )
        {
            return false;
        }
        else if ( obj instanceof String )
        {
            String my_path_string = this.toString ();
            if ( my_path_string == null )
            {
                return false;
            }

            return my_path_string.equals ( obj );
        }
        else if ( ! ( obj instanceof Path ) )
        {
            return false;
        }

        Path other_path = (Path) obj;
        String other_path_string = other_path.toString ();
        if ( other_path_string == null )
        {
            return false;
        }

        String my_path_string = this.toString ();
        if ( my_path_string == null )
        {
            return false;
        }

        return my_path_string.equals ( other_path_string );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode ()
    {
        String path_string = this.toString ();
        if ( path_string == null )
        {
            return 0;
        }

        return path_string.hashCode ();
    }


    /**
     * <p>
     * Returns true if this is an absolute path ("/a/b/c"),
     * false if it is a relative one ("a/b/c").
     * </p>
     *
     * @return True if this is an absolute path.
     */
    public boolean isAbsolute ()
    {
        return this.isAbsolute;
    }


    /**
     * <p>
     * Returns the last name in this path as a String.
     * </p>
     *
     * @return The last name in the path, or "" if the path is empty
     *         or "/" if the path is "/".  Never null.
     */
    public String leafName ()
    {
        if ( this.path.length == 0 )
        {
            return "";
        }

        int last_index = this.path.length - 1;
        if ( last_index == 0
             && this.path [ last_index ].equals ( "" ) )
        {
            if ( this.isAbsolute () )
            {
                return "/";
            }
            else
            {
                return "";
            }
        }

        return this.path [ last_index ];
    }


    /**
     * @see musaico.foundation.io.Identifier#name()
     */
    public Path name ()
    {
        if ( this.path.length == 0 )
        {
            return this;
        }

        int last_index = this.path.length - 1;
        if ( last_index == 0
             && this.path [ last_index ].equals ( "" ) )
        {
            return this;
        }

        return new Path ( this.path [ last_index ] );
    }


    /**
     * <p>
     * Returns the names of this path as an array of Strings.
     * </p>
     *
     * @return The hierarchical names of this path.  Never null.
     *         Never contains any null elements.
     */
    public String [] names ()
    {
        String [] names = new String [ this.path.length ];
        System.arraycopy ( this.path, 0,
                           names, 0, this.path.length );
        return names;
    }


    /**
     * @see musaico.foundation.io.NaturallyOrdered#orderIndex()
     */
    public Path orderIndex ()
    {
        return this;
    }


    /**
     * @see musaico.foundation.io.NaturallyOrdered#order()
     */
    public Order<Path> order ()
    {
        // Each level of 2 Paths is compared by dictionary order.
        return PathOrder.DEFAULT;
    }


    /**
     * <p>
     * Returns the parent path of this path.
     * For example, the parent of "/a/b/c" is "/a/b",
     * and the parent of "a/b/c" is "a/b", and so on.
     * </p>
     *
     * @return The parent path of this path.  Never null.
     */
    public Path parent ()
    {
        if ( this.isAbsolute () )
        {
            if ( this.path.length == 0 )
            {
                return this;
            }
        }
        else if ( this.path.length <= 1 )
        {
            return this;
        }

        final String [] parent_names = new String [ this.path.length - 1 ];
        System.arraycopy ( this.path, 0,
                           parent_names, 0, this.path.length - 1 );

        return new Path ( parent_names, this.isAbsolute () );
    }


    /**
     * @see musaico.foundation.io.Identifier#parentNamespace()
     */
    public Path parentNamespace ()
    {
        return this.parent ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        for ( int n = 0; n < this.path.length; n ++ )
        {
            if ( ( n == 0
                   && this.isAbsolute )
                 || n > 0 )
            {
                sbuf.append ( "/" );
            }

            String name =
                Path.ESCAPED_CHARACTERS.matcher ( this.path [ n ] )
                .replaceAll ( Path.ESCAPE_REPLACEMENT );
            sbuf.append ( name );
        }

        return sbuf.toString ();
    }
}
