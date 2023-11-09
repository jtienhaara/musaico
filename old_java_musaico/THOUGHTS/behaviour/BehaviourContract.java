package behaviour;

public class BehaviourContract<BEHAVING_OBJECT extends Object>
    implements Contract<BEHAVING_OBJECT, BehaviourViolation>
{
    private final Obligation [] givens;
    private final BehaviouralEvent<BEHAVING_OBJECT> [] events;
    private final Guarantee [] thens;

    public BehaviourContract (
                              Obligation [] givens,
                              BehaviouralEvent<BEHAVING_OBJECT> [] events,
                              Guarantee [] thens
                              )
    {
        this.givens = new Obligation [ givens.length ];
        System.arraycopy ( givens, 0, this.givens, 0, givens.length );

        this.events = new Obligation<BEHAVING_OBJECT> [ events.length ];
        System.arraycopy ( events, 0, this.events, 0, events.length );

        this.thens = new Obligation [ thens.length ];
        System.arraycopy ( thens, 0, this.thens, 0, thens.length );
    }

    @Override
    public void enforce (
                         Object object_under_contract,
                         BEHAVING_OBJECT behaving_object
                         )
        throws BehaviourViolation
    {
        // Check pre-conditions.
        if ( ! isGivensHold ( behaving_object ) )
        {
            // OK.
            return;
        }

        // Execute behavioural events.
        // Throws BehaviourViolation.
        executeEvents ( behaving_object );

        // Check post-conditions.
        if ( ! isThensHold ( behaving_object ) )
        {
            throw new BehaviourViolation ( "" + this + " did not hold"
                                           + " for behaving object "
                                           + behaving_object
                                           + " for object under contract "
                                           + object_under_contract );
        }
    }

    public boolean isGivensHold (
                                 BEHAVING_OBJECT behaving_object
                                 )
    {
        try
        {
            for ( Obligation obligation : this.obligations )
            {
                this.contracts.check ( obligation, behaving_object );
            }
        }
        catch ( ObligationViolation v )
        {
            return false;
        }

        return true;
    }

    public void executeEvents (
                               BEHAVING_OBJECT behaving_object
                               )
        throws BehaviourViolation
    {
        BehaviouralEvent event = null;
        try
        {
            for ( event : this.events )
            {
                event.execute ( behaving_object );
            }
        }
        catch ( Exception e )
        {
            throw new BehaviourViolation ( "" + this
                                           + " failed to execute event "
                                           + event
                                           + " on behaving object "
                                           + behaving_object,
                                           e );
        }
    }

    public boolean isThensHold (
                                BEHAVING_OBJECT behaving_object
                                )
    {
        try
        {
            for ( Guarantee guarantee : this.guaranteess )
            {
                this.contracts.check ( guarantee, behaving_object );
            }
        }
        catch ( GuaranteeViolation v )
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Behaviour {" );

        boolean has_content = false;

        if ( this.givens.length > 0 )
        {
            has_content = true;

            sbuf.append ( " Given" );
            for ( Obligation obligation : this.obligations )
            {
                sbuf.append ( " ( " + obligation + " )" );
            }
        }

        if ( this.events.length > 0 )
        {
            has_content = true;

            sbuf.append ( " Given" );
            for ( BehaviouralEvent event : this.events )
            {
                sbuf.append ( " ( " + event + " )" );
            }
        }

        if ( this.thens.length > 0 )
        {
            has_content = true;

            sbuf.append ( " Given" );
            for ( Guarantee guarantee : this.guarantees )
            {
                sbuf.append ( " ( " + guarantee + " )" );
            }
        }

        if ( has_content )
        {
            sbuf.append ( " " );
        }
        sbuf.append ( "}" );

        return sbuf.toString ();
    }
}
