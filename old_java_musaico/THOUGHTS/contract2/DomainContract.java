public abstract class DomainContract<VIOLATION extends DomainViolation>
{
    private final Domain domain;

    public DomainContract ( Domain domain )
    {
        this.domain = domain;
    }

    public void check ( Object object )
        throws VIOLATION
    {
        if ( ! this.domain.contains ( object ) )
        {
            VIOLATION violation = this.createViolation ();
            violation.setContract ( this );
            violation.setDomain ( this.domain );
            violation.setObject ( object );

            throw violation;
        }
    }

    protected abstract VIOLATION createViolation ();


    @Override
    public String toString ()
    {
        return "Must be " + this.domain;
    }
}
