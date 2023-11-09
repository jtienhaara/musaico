public class DomainViolation
    extends Exception
{
    private DomainContract contract;
    private Domain domain;
    private Object object;

    public DomainViolation ()
    {
    }

    public DomainContract contract ()
    {
        if ( this.contract == null
             || this.domain == null )
        {
            throw new IllegalStateException ( "DomainViolation " + this + " not fully initialized" );
        }

        return this.contract;
    }

    protected void setContract ( DomainContract contract )
    {
        this.contract = contract;
    }

    public Domain domain ()
    {
        if ( this.contract == null
             || this.domain == null )
        {
            throw new IllegalStateException ( "DomainViolation " + this + " not fully initialized" );
        }

        return this.domain;
    }

    protected void setDomain ( Domain domain )
    {
        this.domain = domain;
    }

    public Object object ()
    {
        if ( this.contract == null
             || this.domain == null )
        {
            throw new IllegalStateException ( "DomainViolation " + this + " not fully initialized" );
        }

        return this.object;
    }

    protected void setObject ( Object object )
    {
        this.object = object;
    }

    @Override
    public String getMessage ()
    {
        if ( this.contract == null
             || this.domain == null )
        {
            throw new IllegalStateException ( "DomainViolation " + this + " not fully initialized" );
        }

        return "Contract " + this.contract + " violated with " + this.object;
    }
}
