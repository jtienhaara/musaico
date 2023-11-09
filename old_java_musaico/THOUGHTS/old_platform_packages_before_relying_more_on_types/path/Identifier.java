package musaico.foundation.io;

import java.io.Serializable;


/**
 * <p>
 * An Identifier is a Reference which is unique within some
 * Namespace.
 * </p>
 *
 * <p>
 * For example, the name "Jane Doe" might be a unique Identifier
 * within the Namespace of all "employees" for ACME Co.
 * </p>
 *
 *
 * <p>
 * In Java, every Identifier must be Serializable in order
 * to play nicely across RMI.
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
 * Copyright (c) 2011 Johann Tienhaara
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
public interface Identifier
    extends Reference, Serializable
{
    /** No identifier.  The parent namespace of any toplevel / root
     *  namespace is always NONE. */
    public static final Identifier NONE = new NoIdentifier ();


    /**
     * @see java.lang.Object#equals(Object)
     */
    public abstract boolean equals (
                                    Object obj
                                    );


    /**
     * @see java.lang.Object#hashCode()
     */
    public abstract int hashCode ();


    /**
     * <p>
     * Returns the name of this Identifier, unique within its
     * parent namespace.
     * </p>
     *
     * @return This Identifier's name, unique within the parent
     *         namespace.  Never null.
     */
    public abstract Reference name ();


    /**
     * <p>
     * Returns this Identifier's parent namespace.
     * </p>
     *
     * @return This Identifier's parent Namespace.  Never null.
     *         (Note that <code> Identifier.NONE </code> returns
     *         itself when this method is called.  All toplevel /
     *         root namespaces will return Identifier.NONE when
     *         this method is called.)
     */
    public abstract Identifier parentNamespace ();


    /**
     * @see java.lang.Object#toString()
     */
    public abstract String toString ();


    /**
     * <p>
     * Class describing the NONE identifier.  Use Identifier.NONE
     * instead of accessing this class.
     * </p>
     */
    public static class NoIdentifier
        implements Identifier, Serializable
    {
        /** The name of this NoIdentifier. */
        private final Reference name =
            new Path ( "/no_identifier;" );


        /**
         * <p>
         * Use Identifier.NONE instead.
         * </p>
         */
        private NoIdentifier ()
        {
        }


        /**
         * @see java.lang.Object#equals(Object)
         */
        public boolean equals (
                               Object obj
                               )
        {
            if ( obj.getClass () == this.getClass () )
            {
                // A NoIdentifier is always equal to a NoIdentifier.
                return true;
            }
            else
            {
                return false;
            }
        }


        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode ()
        {
            return 0;
        }


        /**
         * @see musaico.foundation.io.Identifier#name()
         */
        public Reference name ()
        {
            return this.name;
        }


        /**
         * @see musaico.foundation.io.Identifier#parentNamespace()
         */
        public Identifier parentNamespace ()
        {
            return this;
        }


        /**
         * @see java.lang.Object#toString()
         */
        public String toString ()
        {
            return "No Identifier";
        }
    }
}
