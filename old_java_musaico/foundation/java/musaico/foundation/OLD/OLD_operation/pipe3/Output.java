package musaico.foundation.operation;

import java.io.Serializable;

import java.util.Iterator;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;
import musaico.foundation.contract.guarantees.Return;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.term.Countable;
import musaico.foundation.term.Maybe;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;
import musaico.foundation.term.UncheckedTermViolation;

import musaico.foundation.term.countable.No;

import musaico.foundation.term.pipeline.AbstractTerm;


/**
 * <p>
 * !!!
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
 * @see musaico.foundation.operation.MODULE#COPYRIGHT
 * @see musaico.foundation.operation.MODULE#LICENSE
 */
public class Output<VALUE extends Object>
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( Output.class );


    private final VALUE [] array;
    private int offset;
    private final int length;

    // MUTABLE:
    private int numElementsRead = 0;

    public Output (
            VALUE [] array,
            int offset,
            int length
            )
    {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }


    public int read (
            Cursor<VALUE> input
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        if ( this.isComplete () )
        {
            return -1;
        }

        final Pipe<VALUE> pipe = input.pipe ();
        final long index = input.index ();
        final Context context = input.context ();
        final int num_elements_read =
            pipe.read ( this.array,
                        this.offset + this.numElementsRead,
                        this.length - this.numElementsRead,
                        index,
                        pipe.stream ( context ),
                        context );
        if ( num_elements_read <= 0 )
        {
            return num_elements_read;
        }

        input.index ( index + (long) num_elements_read );
        this.numElementsRead += num_elements_read;

        return num_elements_read;
    }


    public int read (
            Cursor<VALUE> input,
            long to_index
            )
        throws ParametersMustNotBeNull.Violation,
               Return.AlwaysGreaterThanOrEqualToNegativeOne.Violation
    {
        final Pipe<VALUE> pipe = input.pipe ();
        final Context context = input.context ();
        final long index = input.index ();
        final int length;
        if ( index == Countable.NONE
             || to_index == Countable.NONE )
        {
                // !!! error;
            return -1;
        }
        else if ( ( index < 0L
                    && to_index < 0L )
                  || ( index >= 0L
                       && to_index >= 0L ) )
        {
            final long max_length = to_index - index + 1L;
            if ( max_length >= (long) ( this.length - this.numElementsRead ) )
            {
                return this.read ( input );
            }
            else
            {
                length = (int) max_length;
            }
        }
        else if ( pipe.stream ( context ) == Pipe.Stream.CYCLICAL )
        {
            return this.read ( input );
        }
        else
        {
            // !!! error;
            return -1;
        }

        final int num_elements_read =
            pipe.read ( this.array,
                        this.offset + this.numElementsRead,
                        length,
                        index,
                        pipe.stream ( context ),
                        context );
        if ( num_elements_read <= 0 )
        {
            return num_elements_read;
        }

        input.index ( index + (long) num_elements_read );
        this.numElementsRead += num_elements_read;

        return num_elements_read;
    }


    public final int numElementsRead ()
    {
        return this.numElementsRead;
    }


    public final int offset ()
    {
        return this.offset;
    }


    public final int length ()
    {
        return this.length;
    }


    public final VALUE [] array ()
    {
        return this.array;
    }


    public final boolean isComplete ()
    {
        if ( this.numElementsRead >= this.length )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
