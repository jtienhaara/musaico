package musaico.foundation.operations.filter;

import java.io.Serializable;

import java.util.Iterator;
import java.util.LinkedHashMap;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.filter.Filter;

import musaico.foundation.operations.AbstractOperation;

import musaico.foundation.order.Order;

import musaico.foundation.term.Countable;
import musaico.foundation.term.ElementalOperation;
import musaico.foundation.term.Edit;
import musaico.foundation.term.FilterElements;
import musaico.foundation.term.NonBlocking;
import musaico.foundation.term.Operation;
import musaico.foundation.term.OrderElements;
import musaico.foundation.term.Pipeline;
import musaico.foundation.term.Select;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermViolation;
import musaico.foundation.term.Type;


/**
 * <p>
 * Sorts or shuffles input Terms.
 * </p>
 *
 *
 * <p>
 * In Java every Operation must be Serializable in order to
 * play nicely across RMI.
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
 * @see musaico.foundation.operations.filter.MODULE#COPYRIGHT
 * @see musaico.foundation.operations.filter.MODULE#LICENSE
 */
public class StandardFilterElements<VALUE extends Object>
    extends AbstractOperation<VALUE, VALUE>
    implements FilterElements<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final Advocate classContracts =
        new Advocate ( StandardFilterElements.class );


    // Enforces parameter obligations and so on for us.
    private final Advocate contracts;

    // Synchronize critical sections on this token:
    private final Serializable lock = new String ( "lock" );

    // MUTABLE:
    // The ElementalOperation(s) which take in stream data, and how big a
    // partial Term they want (or just the whole result).
    private final LinkedHashMap<ElementalOperation<VALUE, VALUE>, Pipeline.BlockSize> operations;

    // MUTABLE:
    // The current hash code of this StandardFilterElements operation,
    // generated from all the child operations.
    private int hashCode;


    /**
     * <p>
     * Creates a new StandardFilterElements operation as a copy of another.
     * </p>
     */
    public StandardFilterElements (
                                   StandardFilterElements<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        this ( that == null
                   ? null
                   : that.inputType () );

        this.operations.putAll ( that.operations );
        this.hashCode = that.hashCode;
    }


    /**
     * <p>
     * Creates a new StandardFilterElements operation.
     * </p>
     *
     * @param type The Type of Terms accepted as inputs to, and returned
     *             as outputs from, Operations created by this Pipeline,
     *             such as a <code> Type<Integer> </code> or
     *             a Type<String> and so on.  Must not be null.
     */
    public StandardFilterElements (
                                   Type<VALUE> type
                                   )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type,
                type );

        this.operations =
            new LinkedHashMap<ElementalOperation<VALUE, VALUE>, Pipeline.BlockSize> ();

        this.contracts = new Advocate ( this );
    }


    /**
     * @see musaico.foundation.term.Operation#apply(musaico.foundation.term.Term)
     */
    @Override
    public final Term<VALUE> apply (
                                    Term<VALUE> input
                                    )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.apply ( input,
                            Long.MAX_VALUE ); // Generate all output elems.
    }


    /**
     * @see musaico.foundation.term.ElementalOperation#apply(musaico.foundation.term.Term, long)
     */
    @Override
    public final Term<VALUE> apply (
                                    Term<VALUE> input,
                                    long num_elements
                                    )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustBeGreaterThanOrEqualToZero.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               input );

        Term<VALUE> output = input;

        final Iterator<ElementalOperation<VALUE, VALUE>> iterator =
            this.operations.keySet ().iterator ();

        ElementalOperation<VALUE, VALUE> next_operation;
        if ( iterator.hasNext () )
        {
            next_operation = iterator.next ();
        }
        else
        {
            next_operation = null;
        }

        while ( next_operation != null )
        {
            final ElementalOperation<VALUE, VALUE> operation =
                next_operation;
            final Pipeline.BlockSize block_size;
            if ( iterator.hasNext () )
            {
                // Not the last operation yet.
                next_operation = iterator.next ();
                block_size = this.operations.get ( next_operation );
            }
            else if ( num_elements == Long.MAX_VALUE )
            {
                // Last operation, generate all output elements.
                next_operation = null;
                block_size = Pipeline.ALL;
            }
            else
            {
                // Last operation, only generate a subset of output elements.
                next_operation = null;
                block_size = new Pipeline.BlockSize ( num_elements );
            }

            if ( block_size == Pipeline.ALL )
            {
                // Do the complete processing.
                output = operation.apply ( input );
            }
            else
            {
                // We only need a few elements.
                output = operation.apply ( input,
                                           block_size.numElements () );
            }

            this.contracts.check ( ReturnNeverNull.CONTRACT,
                                   output );

            input = output;
        }

        return output;
    }


    /**
     * @see musaico.foundation.term.Pipeline#edit()
     */
    @Override
    public final Edit<VALUE> edit ()
        throws ReturnNeverNull.Violation
    {
        final Edit<VALUE> edit;
        if ( this.operations.size () == 0 )
        {
            edit = this.inputType ().edit ();
        }
        else
        {
            // Since the caller could continue to build this FilterElements,
            // make a frozen-in-time snapshot, and pipe that to the Edit
            // pipeline.
            final StandardFilterElements<VALUE> snapshot =
                new StandardFilterElements<VALUE> ( this );
            edit = this.inputType ().edit ()
                                    .pipe ( snapshot,
                                            Pipeline.ALL );
        }

        return edit;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast checked with try...catch
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

        final StandardFilterElements<VALUE> that;
        try
        {
            that = (StandardFilterElements<VALUE>) object;
        }
        catch ( Exception e ) // E.g. ClassCastException.
        {
            return false;
        }

        if ( this.operations.size () != that.operations.size () )
        {
            return false;
        }

        final Iterator<ElementalOperation<VALUE, VALUE>> this_iterator =
            this.operations.keySet ().iterator ();
        final Iterator<ElementalOperation<VALUE, VALUE>> that_iterator =
            that.operations.keySet ().iterator ();
        int infinite_loop_protector = 0;
        while ( this_iterator.hasNext ()
                && that_iterator.hasNext () )
        {
            infinite_loop_protector ++;
            if ( infinite_loop_protector >= ( Integer.MAX_VALUE - 1000 ) )
            {
                // Whoah, way too big.
                return false;
            }

            final ElementalOperation<VALUE, VALUE> this_operation =
                this_iterator.next ();
            final ElementalOperation<VALUE, VALUE> that_operation =
                that_iterator.next ();
            if ( this_operation == null )
            {
                if ( that_operation != null )
                {
                    return false;
                }
            }
            else if ( that_operation == null )
            {
                return false;
            }
            else if ( ! this_operation.equals ( that_operation ) )
            {
                return false;
            }

            final Pipeline.BlockSize this_block_size =
                this.operations.get ( this_operation );
            final Pipeline.BlockSize that_block_size =
                that.operations.get ( that_operation );

            if ( ! this_block_size.equals ( that_block_size ) )
            {
                return false;
            }
        }

        if ( this_iterator.hasNext ()
             || that_iterator.hasNext () )
        {
            // Somehow they lied about their sizes, and one is actually
            // longer than the other.
            // (Impossible...)
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see musaico.foundation.term.Pipeline#filter(musaico.foundation.filter.Filter[])
     */
    @Override
    @SuppressWarnings("unchecked") // generic varargs heap pollution.
    public final FilterElements<VALUE> filter (
                                               Filter<VALUE> ... filters
                                               )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               (Object) filters );
        this.contracts.check ( Parameter1.MustContainNoNulls.CONTRACT,
                               filters );

        if ( filters.length == 0 )
        {
            return this;
        }

        return this.pipe ( new FilterBy<VALUE> ( this.inputType (),
                                                 filters ),
                           Pipeline.ALL );
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode ()
    {
        synchronized ( this.lock )
        {
            return this.hashCode;
        }
    }


    /**
     * @see musaico.foundation.term.Select#operations()
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"}) // ElementalOperation<V,V>[] array
    public final ElementalOperation<VALUE, VALUE> [] operations ()
    {
        final ElementalOperation<VALUE, VALUE> [] operations;
        synchronized ( this.lock )
        {
            final ElementalOperation<VALUE, VALUE> [] template =
                (ElementalOperation<VALUE, VALUE> [])
                new ElementalOperation [ this.operations.size () ];
            operations = this.operations.keySet ().toArray ( template );
        }

        return operations;
    }


    /**
     * @see musaico.foundation.term.Pipeline#order(musaico.foundation.order.Order[])
     */
    @Override
    @SuppressWarnings("unchecked") // generic varargs heap pollution.
    public final OrderElements<VALUE> order (
                                             Order<VALUE> ... orders
                                             )
        throws ParametersMustNotBeNull.Violation,
               Parameter1.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        final OrderElements<VALUE> order;
        if ( this.operations.size () == 0 )
        {
            order = this.inputType ()
                        .order ( orders );
        }
        else
        {
            // Since the caller could continue to build this FilterElements,
            // make a frozen-in-time snapshot, and pipe that to the
            // OrderElements pipeline.
            final StandardFilterElements<VALUE> snapshot =
                new StandardFilterElements<VALUE> ( this );
            order = this.inputType ()
                        .order ()
                        .pipe ( snapshot,
                                Pipeline.ALL )
                        .order ( orders );
        }

        return order;
    }


    /**
     * @see musaico.foundation.term.Pipeline#pipe(musaico.foundation.term.ElementalOperation, musaico.foundation.term.Pipeline.BlockSize)
     */
    @Override
    public final StandardFilterElements<VALUE> pipe (
            ElementalOperation<VALUE, VALUE> operation,
            Pipeline.BlockSize block_size
            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               operation, block_size );

        synchronized ( this.lock )
        {
            this.operations.put ( operation, block_size );

            this.hashCode += operation.hashCode ();
        }

        return this;
    }


    /**
     * @see musaico.foundation.term.OrderElements#reverse()
     */
    @Override
    public final StandardOrderElements<VALUE> reverse ()
        throws ReturnNeverNull.Violation
    {
        return this.pipe ( new Reverse<VALUE> ( this.inputType () ),
                           Pipeline.ALL );
    }


    /**
     * @see musaico.foundation.term.Pipeline#select()
     */
    @Override
    public final Select<VALUE> select ()
        throws ReturnNeverNull.Violation
    {
        final Select<VALUE> select;
        if ( this.operations.size () == 0 )
        {
            select = this.inputType ().select ();
        }
        else
        {
            // Since the caller could continue to build this FilterElements,
            // make a frozen-in-time snapshot, and pipe that to the Select
            // pipeline.
            final StandardFilterElements<VALUE> snapshot =
                new StandardFilterElements<VALUE> ( this );
            select = this.inputType ().select ()
                                      .pipe ( snapshot,
                                              Pipeline.ALL );
        }

        return select;
    }


    /**
     * @see musaico.foundation.term.OrderElements#shuffle(musaico.foundation.term.Term)
     */
    @Override
    public <RANK extends Comparable<? super RANK>>
        StandardOrderElements<VALUE> shuffle (
                                              Term<RANK> ranker
                                              )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        return this.pipe ( new Shuffle<VALUE> ( this.inputType (),
                                                ranker ),
                           Pipeline.ALL );
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        boolean is_first = true;
        sbuf.append ( "{" );
        for ( ElementalOperation<VALUE, VALUE> operation
                  : this.operations.keySet () )
        {
            if ( is_first )
            {
                is_first = false;
            }
            else
            {
                sbuf.append ( "," );
            }

            sbuf.append ( " " + operation );
        }

        if ( ! is_first )
        {
            sbuf.append ( " " );
        }

        sbuf.append ( "}" );

        return ClassName.of ( this.getClass () )
            + sbuf.toString ();
    }
}
