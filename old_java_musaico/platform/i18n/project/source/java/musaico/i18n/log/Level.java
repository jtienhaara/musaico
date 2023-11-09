package musaico.i18n.log;


import java.io.Serializable;


import musaico.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Represents a logging level.
 * </p>
 *
 * <p>
 * A number of standard Levels are provided by the class, and
 * custom Levels can be created as well.
 * </p>
 *
 *
 * <p>
 * Every Level must be Serializable in order to play nicely across
 * RMI (although loggers themselves do not need to be serializable).
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
 * Copyright (c) 2010 Johann Tienhaara
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
public class Level
    implements Comparable<Level>, Serializable
{
    /** Fatal log level.  Something went really *really* wrong. */
    public static final Level FATAL = new Level ( "FATAL" );

    /** Error log level.  Something went really wrong. */
    public static final Level ERROR = new Level ( "ERROR" );

    /** Warning log level.  Something went sort of maybe wrong. */
    public static final Level WARNING = new Level ( "WARNING" );

    /** Info log level.  Something worth recording. */
    public static final Level INFO = new Level ( "INFO" );

    /** Debug log level.  Noise that only geeks care about. */
    public static final Level DEBUG = new Level ( "DEBUG" );

    /** Trace log level.  Methods being entered and exited and so on. */
    public static final Level TRACE = new Level ( "TRACE" );


    /** The string representation of this logging Level. */
    private final String name;


    /**
     * <p>
     * Creates a new logging Level with the specified String representation.
     * </p>
     *
     * @param name The string representation of the logging level.
     *             Will be returned by toString ().  Must not be null.
     */
    public Level (
                  String name
                  )
    {
        if ( name == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a logging Level with name [%level_name%]",
                                                     "level_name", name );
        }

        this.name = name;
    }


    /**
     * @see java.lang.Comparable#compareTo(T)
     */
    public int compareTo (
                          Level that_level
                          )
    {
        if ( that_level == null )
        {
            // this < that_level
            return -1;
        }

        return this.name.compareTo ( that_level.toString () );
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
        else if ( ! ( obj instanceof Level ) )
        {
            return false;
        }

        Level that_level = (Level) obj;

        if ( this.name.equals ( that_level.toString () ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * @see java.lang.Object#toString()
     */
    public String toString ()
    {
        return this.name;
    }
}
