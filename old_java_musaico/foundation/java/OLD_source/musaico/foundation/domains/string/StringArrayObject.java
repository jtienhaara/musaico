package musaico.foundation.domains.string;

import java.io.Serializable;

import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;


import musaico.foundation.domains.ClassName;

import musaico.foundation.domains.array.ArrayObject;


/**
 * <p>
 * Allows Strings to be treated as ArrayObjects, for use with
 * <code> musaico.foundation.domains.array </code> domains.
 * </p>
 *
 * <p>
 * Each element of a StringArrayObject is a one-character sub-String.
 * </p>
 *
 *
 * <p>
 * Every ArrayObject must be Serializable in order to play
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
public class StringArrayObject
    extends ArrayObject<String>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;




    /**
     * <p>
     * An iterator which steps through the characters of a StringArrayObject.
     * </p>
     *
     * <p>
     * This iterator is NOT thread-safe.  Do not use a CharactersIterator
     * from concurrent threads.
     * </p>
     */
    public static class CharactersIterator
        implements Iterator<String>, Serializable
    {
        private static final long serialVersionUID =
            StringArrayObject.serialVersionUID;

        // The StringArrayObject whose characters we'll step through.
        private final StringArrayObject string;

        // MUTABLE:
        // The index of the next character in the array.
        private int nextIndex = Integer.MIN_VALUE;

        /**
         * <p>
         * Creates a new CharactersIterator to step through the
         * specified StringArrayObject's character sub-Strings.
         * </p>
         *
         * @param string The StringArrayObject whose characters
         *               we will step through.  Must not be null.
         */
        public CharactersIterator (
                                   StringArrayObject string
                                   )
        {
            this.string = string;
            if ( this.string.string ().length () > 0 )
            {
                this.nextIndex = 0;
            }
            else
            {
                this.nextIndex = -1;
            }
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public final boolean hasNext ()
        {
            if ( nextIndex >= 0
                 && nextIndex < this.string.string ().length () )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public final String next ()
            throws IllegalStateException
        {
            if ( this.nextIndex >= 0
                 && this.nextIndex < this.string.string ().length () )
            {
                final String next =
                    this.string.string ().substring ( this.nextIndex,
                                                      this.nextIndex + 1 );
                this.nextIndex ++;
                if ( this.nextIndex >= this.string.string ().length () )
                {
                    this.nextIndex = -1;
                }

                return next;
            }

            throw new IllegalStateException ( "Iterator " + this
                                              + " has no next character" );
        }

        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public final void remove ()
            throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException ( ClassName.of ( this.getClass () )
                                                      + ".remove () not supported" );
        }


        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public final String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " over "
                + this.string;
        }
    }




    /**
     * <p>
     * Creates a new StringArrayObject from the specified String.
     * </p>
     *
     * @param string The String.  Can be any length.  Can be null.
     */
    public StringArrayObject (
                              String string
                              )
    {
        // super.array () -> string.
        super ( String.class,
                ( string == null
                      ? ""
                      : string ) );
    }


    /**
     * @see musaico.foundation.array.ArrayObject#array()
     */
    @Override
    public final String [] array ()
    {
        final String string = this.string ();
        final String [] array = new String [ string.length () ];
        for ( int c = 0; c < array.length; c ++ )
        {
            array [ c ] = string.substring ( c, c + 1 );
        }

        return array;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#element(long)
     */
    @Override
    public final String element (
                                 long index
                                 )
        throws IllegalArgumentException
    {
        if ( index < 0L )
        {
            throw new IllegalArgumentException ( "No such index " + index
                                                 + " in "
                                                 + this );
        }

        final String string = this.string ();

        if ( index >= (long) string.length () )
        {
            throw new IllegalArgumentException ( "No such index " + index
                                                 + " in "
                                                 + this );
        }

        final String element = string.substring ( (int) index,
                                                  (int) index + 1 );

        return element;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#equals(java.lang.Object)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals (
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final StringArrayObject that = (StringArrayObject) object;
        final String this_string = this.string ();
        final String that_string = that.string ();
        if ( this_string == null )
        {
            if ( that_string != null )
            {
                return false;
            }
        }
        else if ( that_string == null )
        {
            return false;
        }

        else if ( ! this_string.equals ( that_string ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#hashCode()
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        final String string = this.string ();
        if ( string == null )
        {
            return 0;
        }
        else
        {
            return string.hashCode ();
        }
    }


    /**
     * @see musaico.foundation.domains.array.ArrayObject#indexOf(java.lang.Object, int)
     */
    @Override
    public final long indexOf (
            String sub_string,
            long from_index
            )
    {
        if ( from_index < 0L
             || from_index > this.length () )
        {
            return -1L;
        }

        final String string = this.string ();
        final int index =
            string.indexOf ( sub_string, (int) from_index );
        return (long) index;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#iterator()
     */
    @Override
    public final Iterator<String> iterator ()
    {
        return new StringArrayObject.CharactersIterator ( this );
    }


    /**
     * @see musaico.foundation.domains.array.ArrayObject#lastIndexOf(java.lang.Object, int)
     */
    @Override
    public long lastIndexOf (
            String sub_string,
            long from_index
            )
    {
        if ( from_index < 0L
             || from_index > this.length () )
        {
            return -1L;
        }

        final String string = this.string ();
        final int last_index =
            string.lastIndexOf ( sub_string, (int) from_index );
        return (long) last_index;
    }


    /**
     * @see musaico.foundation.array.ArrayObject#length()
     */
    @Override
    public final long length ()
    {
        return (long) this.string ().length ();
    }


    /**
     * @return The String underlying this StringArrayObject.  Never null,
     *         even if this was created with a null String.
     */
    public final String string ()
    {
        // super () was constructed with 1 string.
        // So here we return that 1 string, the 0'th element of super.array ().
        return super.array () [ 0 ];
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        final String class_name = ClassName.of ( this.getClass () );
        final String string = this.string ();
        return class_name + " \"" + string + "\"";
    }
}
