package musaico.foundation.io;

import java.io.Serializable;

import java.util.Iterator;
import java.util.NoSuchElementException;


import musaico.foundation.i18n.exceptions.I18nIllegalArgumentException;


/**
 * <p>
 * Iterates over the elements in a Sequence.
 * </p>
 *
 *
 * <p>
 * In Java a SequenceIterator must be Serializable in order to play
 * nicely over RMI.  However only a Sequence with Serializable
 * elements will actually play nicely over RMI.  Sequences can be
 * comprised of non-Serializable objects if the sequence builder chooses.
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
 * Copyright (c) 2012 Johann Tienhaara
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
public class SequenceIterator<ELEMENT extends Object>
    implements Iterator<ELEMENT>, Serializable
{
    /** The sequence to iterate over. */
    private final Sequence<ELEMENT> sequence;

    /** The next element from the sequence to return.
     *  Changes over time. */
    private ELEMENT nextElement;


    /**
     * <p>
     * Creates a new SequenceIterator for the specified Sequence.
     * </p>
     *
     * @param sequence The sequence to step through.
     *                 Must not be null.
     *
     * @throws I18nIllegalArgumentException If any of the parameter(s)
     *                                      specified is invalid
     *                                      (see above).
     */
    public SequenceIterator (
                             Sequence<ELEMENT> sequence
                             )
        throws I18nIllegalArgumentException
    {
        if ( sequence == null )
        {
            throw new I18nIllegalArgumentException ( "Cannot create a SequenceIterator for sequence [%sequence%]",
                                                     "sequence", sequence );
        }

        this.sequence = sequence;
        this.nextElement = this.sequence.first ();
    }


    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext ()
    {
        if ( this.sequence.noSuchElement ().equals ( this.nextElement ) )
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    /**
     * @see java.util.Iterator#next()
     */
    @Override
    public ELEMENT next ()
        throws NoSuchElementException
    {
        ELEMENT current_element = this.nextElement;
        if ( this.sequence.noSuchElement ().equals ( current_element ) )
        {
            throw new NoSuchElementException ( "SequenceIterator "
                                               + this
                                               + " for sequence "
                                               + sequence
                                               + " has no more elements" );
        }

        this.nextElement = this.sequence.after ( current_element );

        return current_element;
    }


    /**
     * @see java.util.Iterator#remove()
     *
     * <p>
     * Always throws UnsupportedOperationException.  It does
     * not make any sense to remove an element from an immutable
     * Sequence.
     * </p>
     */
    @Override
    public void remove ()
        throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException ( "Remove is not supported by " + this.getClass () + " " + this );
    }
}
