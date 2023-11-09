package musaico.types;


import java.io.Serializable;


/**
 * <p>
 * Represents a system of types, such as low-level, immutable,
 * idempotent primitives, or user-defined types, and so on.
 * </p>
 *
 * <p>
 * Each TypeSystem has a default exception handler, which
 * decides whether to throw a RuntimeTypeException, or log
 * errors, or pop up GUI dialogs, and so on, whenever
 * typecasts and so on fail.
 * </p>
 *
 * <p>
 * See the package Overview for notes on exception handlers.
 * </p>
 *
 * @link{overview.html}
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
 * Copyright (c) 2009 Johann Tienhaara
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
public interface TypeSystem
    extends Serializable
{
    /** No type system. */
    public static final TypeSystem NONE = new NoTypeSystem ();


    /**
     * <p>
     * Returns True if this type system contains the specified type,
     * false otherwise.
     * </p>
     *
     * @param type The Type in question.  Must not be null.
     *
     * @return True if this type system contains the specified
     *         Type, false if not.
     */
    public abstract boolean contains (
                                      Type<?> type
                                      );


    /**
     * <p>
     * Returns the parent TypingEnvironment, which contains
     * this type system as well as the registry of all types
     * across all type systems.
     * </p>
     *
     * @return The parent TypingEnvironment.  Never null.
     */
    public abstract TypingEnvironment environment ();


    /**
     * <p>
     * Returns the type-system-wide tags, which
     * apply to all Instances of Types in this system.
     * </p>
     *
     * <p>
     * Note that each Type may also implement type-specific
     * Tags, and each Instance may also implement instance-specific
     * Tags.
     * </p>
     *
     * @return The Tags applicable to this type system.
     *         Never null.  Never contains any null elements.
     */
    public abstract Tag []tags ();


    /**
     * <p>
     * Returns all Type's in this TypeSystem.
     * </p>
     *
     * @return The array of all Type's in this TypeSystem.
     *         Zero-length if no Type's are in this TypeSystem.
     *         Never null.
     */
    public abstract Type<Object> [] types ();


    /**
     * <p>
     * Returns this TypeSystem's parent (typically the root
     * type system, or null if this is the root type system).
     * </p>
     *
     * @return The parent type System.  Only null if this is
     *         the root TypeSystem.
     */
    public abstract TypeSystem typeSystemParent ();
}
