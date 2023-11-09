package behaviour;

public class Behaviour<BEHAVING_OBJECT extends Object>
{
    !!!;
    private final List<Obligation<BEHAVING_OBJECT, ?>> givens =
        new ArrayList<Obligation<BEHAVING_OBJECT, ?>> ();
    private final List<BehaviouralEvent> events =
        new ArrayList<BehaviouralEvent> ();
    private final List<Guarantee> thens =
        new ArrayList<Guarantee> ();

    public Behaviour<BEHAVING_OBJECT> given ( Obligation given )
    {
        this.givens.add ( given );
        return this;
    }

    public Behaviour<BEHAVING_OBJECT> given ( Domain<BEHAVING_OBJECT> given_domain )
    {
        Obligation<BEHAVING_OBJECT, DomainObligationCheckedViolation> obligation =
            new DomainObligation<BEHAVING_OBJECT, DomainObligationCheckedViolation> ( given_domain );
        this.givens.add ( obligation );
        return this;
    }

    public Behaviour<BEHAVING_OBJECT> when ( BehaviouralEvent event )
    {
        this.events.add ( event );
        return this;
    }

    public Behaviour<BEHAVING_OBJECT> then ( Guarantee then )
    {
        this.thens.add ( then );
        return this;
    }

    public Behaviour<BEHAVING_OBJECT> then ( Domain<BEHAVING_OBJECT> then_domain )
    {
        Guarantee guarantee =
            new DomainGuarantee<BEHAVING_OBJECT, DomainGuaranteeCheckedViolation> ( then_domain );
        this.thens.add ( guarantee );
        return this;
    }

    public BehaviourContract<BEHAVING_OBJECT> build ();
}
