package musaico.foundation.value;

import java.io.Serializable;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;


!!!;
    /**
     * <p>
     * Logically ANDs this Value with that Value.
     * </p>
     *
     * <p>
     * Value truth table:
     * </p>
     *
     * <pre>
     *               |                                          A
     *               |            B                        C    c
     *               |       T    l    W    P              y    y
     *               |       i    o    a    a              c    c
     *               |  E    m    c    r    r              l    l
     *               |  r    e    k    n    t         M    i    i
     *     That:     |  r    o    i    i    i    O    a    c    c
     *               |  o    u    n    n    a    n    n    a    a    N
     *     This      |  r    t    g    g    l    e    y    l    l    o
     *     ----------+---------------------------------------------------
     *     Error     |  Err  Err  Err  Err  Err  Err  Err  Err  Err  Err
     *     Timeout   |  Err  Tim  Tim  Tim  Tim  Tim  Tim  Tim  Tim  Tim
     *     Blocking  |  Err  Tim  Blo  Blo  Blo  Blo  Blo  Blo  Blo  Blo
     *     Warning   |  Err  Tim  Blo  War  Part War  War  War  War  No
     *     Partial   |  Err  Tim  Blo  Part Part Part Part Part Part No
     *     One       |  Err  Tim  Blo  War  Part One  One  One  One  No
     *     Many      |  Err  Tim  Blo  War  Part One  Many Many Many No
     *     Cyclical  |  Err  Tim  Blo  War  Part One  Many Cyc  Cyc  No
     *     Acyclical |  Err  Tim  Blo  War  Part One  Many Cyc  Acyc No
     *     No        |  Err  Tim  Blo  No   No   No   No   No   No   No
     * </pre>
     *
     * @param that The Value to logically AND with this Value.
     *             Can be any Value.
     *             Must not be null.
     *
     * @return The logically ANDed Value.  Never null.
     */
    public abstract Value<VALUE> and (
                                      Value<VALUE> that
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation;
!!!;


/**
 * <p>
 * Logically ANDs together Values.
 * </p>
 *
 * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
 *
 * <p>
 * On the first call to <code> process () </code> with
 * <code> value1 </code>, an AndValues processor returns
 * <code> value1 </code>.
 * </p>
 *
 * <p>
 * On the second call to <code> process () </code> with
 * <code> value2 </code>, an AndValues processor returns
 * <code> value1.and ( value2 ) </code>.
 * </p>
 *
 * <p>
 * On the third call to <code> process () </code> with
 * <code> value3 </code>, an AndValues processor returns
 * <code> value1.and ( value2 ).and ( value3 ) </code>.
 * </p>
 *
 * <p>
 * And so on.
 * </p>
 *
 *
 * <p>
 * In Java every ValueProcessor must be Serializable in order to
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public class AndValues<VALUE extends Object>
    implements ValueProcessor<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Enforces static parameter obligations and so on for us. */
    private static final ObjectContracts classContracts =
        new ObjectContracts ( AndValues.class );


    // Enforces parameter obligations and so on for us.
    private final ObjectContracts contracts;

    // Lock critical sections on this token:
    private final Serializable lock = new String ();

    // MUTABLE:
    // The result Value so far.
    private Value<VALUE> value;


    /**
     * <p>
     * Creates a new AndValues which will logically AND together the Values
     * it processes, keeping the resulting Value state.
     * </p>
     *
     * @param initial_value The initial "zero" state of this AndValues,
     *                      such as No Value.  Must not be null.
     */
    public AndValues (
                      Value<VALUE> initial_value
                      )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               initial_value );

        this.value = initial_value;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * @see musaico.foundation.value.ValueProcessor#process(musaico.foundation.value.Value)
     */
    @Override
    public Value<VALUE> process (
                                 Value<VALUE> term
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               term );

        final Value<VALUE> new_value;
        synchronized ( this.lock )
        {
            this.value = this.value.and ( term );
            new_value = this.value;
        }

        return new_value;
    }


    /**
     * @see musaico.foundation.value.ValueProcessor#process(musaico.foundation.value.Value)
     */
    @Override
    public final void processPartial (
                                      NonBlocking<VALUE> partial_input
                                      )
        throws ParametersMustNotBeNull.Violation
    {
        // Ignore.  We don't care about partial results, such as
        // asynchronous results from a Blocking operation.
        // All we care about are Complete values.
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        return "AndValues ( " + this.value + " && "
            + ClassName.of ( this.value.expectedClass () )
            + " input ) : Value<"
            + ClassName.of ( this.value.expectedClass () )
            + ">";
    }
}






from AbstractMultiple.java:




    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // AbstractMultiple value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // AbstractMultiple value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // AbstractMultiple value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // AbstractMultiple value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // AbstractMultiple value this & Partial value that = that.
            return that;
        }
        else if ( that instanceof One )
        {
            // AbstractMultiple value this & One value that = that.
            return that;
        }
        else if ( that instanceof Countable )
        {
            // AbstractMultiple value this & Countable value that
            //     = shorter of the two.
            final Countable<VALUE> countable_that =
                (Countable<VALUE>) that;
            if ( this.length () <= countable_that.length () )
            {
                // This is shorter.
                return this;
            }
            else
            {
                // That is shorter.
                return that;
            }
        }
        else if ( that instanceof Just )
        {
            // AbstractMultiple value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // AbstractMultiple value this & Unjust value that = that.
            return that;
        }
    }





// from No.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // No value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // No value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // No value this & Blocking value that = this.
            return this;
        }
        else if ( that instanceof Warning )
        {
            // No value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // No value this & Partial value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // No value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // No value this & Unjust value that = this.
            return this;
        }
    }




// from One.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // One value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // One value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // One value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // One value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // One value this & Partial value that = that.
            return that;
        }
        else if ( that instanceof Just )
        {
            // One value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // One value this & Unjust value that = that.
            return that;
        }
    }




from Acyclical.java:



    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Acyclical value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Acyclical value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Acyclical value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Acyclical value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // Acyclical value this & Partial value that = that.
            return that;
        }
        else if ( that instanceof Acyclical )
        {
            // Acyclical value this & Acyclical value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // Acyclical value this & Just value that = that.
            return that;
        }
        else // Any other Unjust value, including No.
        {
            // Acyclical value this & Unjust value that = that.
            return that;
        }
    }




// from Error.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Error value this & Error value that = this.
            return this;
        }
        else if ( that instanceof Timeout )
        {
            // Error value this & Timeout value that = this.
            return this;
        }
        else if ( that instanceof Blocking )
        {
            // Error value this & Blocking value that = this.
            return this;
        }
        else if ( that instanceof Warning )
        {
            // Error value this & Warning value that = this.
            return this;
        }
        else if ( that instanceof Partial )
        {
            // Error value this & Partial value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // Error value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // Error value this & Unjust value that = this.
            return this;
        }
    }




// from Warning.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Warning value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Warning value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Warning value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Warning value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // Warning value this & Partial value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // Warning value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // Warning value this & Unjust value that = that.
            return that;
        }
    }




// From Partial.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Partial value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Partial value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Partial value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Partial value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // Partial value this & Partial value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // Partial value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // Partial value this & Unjust value that = that.
            return that;
        }
    }




from Cyclical.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Cyclical value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Cyclical value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Cyclical value this & Blocking value that = new Blocking value.
            final Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( this );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Cyclical value this & Warning value that = that.
            return that;
        }
        else if ( that instanceof Partial )
        {
            // Cyclical value this & Partial value that = that.
            return that;
        }
        else if ( that instanceof Infinite )
        {
            // Cyclical value this & Infinite value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // Cyclical value this & Just value that = that.
            return that;
        }
        else // Any other Unjust value, including No.
        {
            // Cyclical value this & Unjust value that = that.
            return that;
        }
    }




// from Blocking.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    @SuppressWarnings("unchecked") // Generic varargs array creation.
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Blocking value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Blocking value this & Timeout value that = that.
            return that;
        }
        else if ( that instanceof Blocking )
        {
            // Blocking value this & Blocking value that = new Blocking value.
            Blocking<VALUE> blocking_that =
                (Blocking<VALUE>) that;
            final No<VALUE> initial_value =
                new No<VALUE> ( this.expectedClass,
                                this.valueViolation );
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( initial_value );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,           // logic
                                         this,            // blocking_values
                                         blocking_that ); // blocking_values
        }
        else if ( that instanceof Warning )
        {
            // Blocking value this & Warning value that = new Blocking value.
            final Warning<VALUE> warning_that =
                (Warning<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( warning_that );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,  // logic
                                         this ); // blocking_values
        }
        else if ( that instanceof Partial )
        {
            // Blocking value this & Partial value that = new Blocking value.
            final Partial<VALUE> partial_that =
                (Partial<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( partial_that );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,  // logic
                                         this ); // blocking_values
        }
        else if ( that instanceof Just )
        {
            // Blocking value this & Just value that = new Blocking value.
            final Just<VALUE> just_so =
                (Just<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( just_so );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,  // logic
                                         this ); // blocking_values
        }
        else if ( that instanceof NonBlocking ) // Unjust NonBlocking value.
        {
            // Blocking value this & NonBlocking value that
            //     = new Blocking value.
            final NonBlocking<VALUE> non_blocking_that =
                (NonBlocking<VALUE>) that;
            final AndValues<VALUE> logic =
                new AndValues<VALUE> ( non_blocking_that );
            return new Blocking<VALUE> ( this.expectedClass, // exp.class
                                         logic,  // logic
                                         this ); // blocking_values
        }
        else // Some other Unjust value that is neither Blocking
             // nor NonBlocking.
        {
            // Blocking value this & Unjust value that = that.
            return that;
        }
    }




// from Timeout.java:


    /**
     * @see musaico.foundation.value.Value#and(musaico.foundation.value.Value)
     */
    @Override
    public final Value<VALUE> and (
                                   Value<VALUE> that
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               that );

        if ( that instanceof Error )
        {
            // Timeout value this & Error value that = that.
            return that;
        }
        else if ( that instanceof Timeout )
        {
            // Timeout value this & Timeout value that = this.
            return this;
        }
        else if ( that instanceof Blocking )
        {
            // Timeout value this & Blocking value that = this.
            return this;
        }
        else if ( that instanceof Warning )
        {
            // Timeout value this & Warning value that = this.
            return this;
        }
        else if ( that instanceof Partial )
        {
            // Timeout value this & Partial value that = this.
            return this;
        }
        else if ( that instanceof Just )
        {
            // Timeout value this & Just value that = this.
            return this;
        }
        else // Any other Unjust value, including No.
        {
            // Timeout value this & Unjust value that = this.
            return this;
        }
    }
