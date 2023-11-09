package !!!;


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
 * @see musaico.foundation.!!!.MODULE#COPYRIGHT
 * @see musaico.foundation.!!!.MODULE#LICENSE
 */
public !!!
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public Error<VALUE> read (
            List<Countable<VALUE>> output_header,
            List<Countable<VALUE>> output_cycle,
            Context context
            )
    {
        final List<Countable<VALUE>> input_header =
            new ArrayList<Countable<VALUE>> ();
        final List<Countable<VALUE>> input_cycle =
            new ArrayList<Countable<VALUE>> ();
        final Error<VALUE> input_error =
            this.input.read ( input_header,
                              input_cycle,
                              context );
        if ( input_error != null )
        {
            context.term ( this, input_error );
            return input_error;
        }

        final List<Countable<Long>> insert_at_header =
            new ArrayList<Countable<Long>> ();
        final List<Countable<Long>> insert_at_cycle =
            new ArrayList<Countable<Long>> ();
        final Error<VALUE> insert_at_error =
            this.insert_at.read ( insert_at_header,
                                  insert_at_cycle,
                                  context );
        if ( insert_at_error != null )
        {
            final TermViolation violation = insert_at_error.violation ();
            final Error<VALUE> error =
                new Error<VALUE> ( this.type ( context ),
                                   violation );
            context.term ( this, error );
            return error;
        }

        final List<Countable<VALUE>> elements_header =
            new ArrayList<Countable<VALUE>> ();
        final List<Countable<VALUE>> elements_cycle =
            new ArrayList<Countable<VALUE>> ();
        final Error<VALUE> elements_error =
            this.elements.read ( elements_header,
                                 elements_cycle,
                                 context );
        if ( elements_error != null )
        {
            context.term ( this, elements_error );
            return elements_error;
        }


        int input_index = 0;
        long total_input_length = 0L;
        for ( Countable<Long> insert_ats : insert_at_header )
        {
            for ( Long insert_at_unclamped : insert_ats )
            {
                boolean is_inserted = false;
                for ( input_index = 0;
                      input_index < input_header.size ();
                      input_index ++ )
                {
                    final Countable<VALUE> input =
                        input_header.get ( input_index );
                    final long insert_at = input.clamp ( insert_at_unclamped );
                    if ( total_input_length == insert_at )
                    {
                        output_header.addAll ( elements_header );
                        if ( elements_cycle.size () > 0 )
                        {
                            output_cycle.addAll ( elements_cycle );
                            return null; // No error.
                        }

                        is_inserted = true;
                    }
                    else if ( total_input_length > insert_at )
                    {
                        return new Error<VALUE> ( !!! indexes went backward );
                    }

                    final long input_length = input.length ();
                    final long new_total_length =
                        total_input_length + input_length;
                    if ( new_total_length < insert_at )
                    {
                        output_header.add ( input );
                    }
                    else
                    {
                        final Countable<VALUE> before =
                            input.head ( insert_at - total_input_length );
                        final Countable<VALUE> after =
                            input.range ( insert_at - total_input_length,
                                          Countable.LAST );
                        input_header.remove ( input_index );
                        input_header.add ( input_index, before );
                        input_header.add ( input_index + 1, after );

                        output_header.add ( before );
                    }

                    total_length = new_total_length;
                }

                if ( ! is_inserted )
                {
                    output_header.addAll ( elements_header );
                    if ( elements_cycle.size () > 0 )
                    {
                        output_cycle.addAll ( elements_cycle );
                        return null; // No error.
                    }
                }
            }
        }

        for ( Countable<Long> insert_ats : insert_at_cycle )
        {
            for ( Long insert_at_unclamped : insert_ats )
            {
                long total_input_length = 0L;
                for ( ; input_index < input_header.size (); input_index ++ )
                {
                    final Countable<VALUE> input = input_header.get ( i );
                    final long insert_at = input.clamp ( insert_at_unclamped );
    }
}

