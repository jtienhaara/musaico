public interface FooBar {
    public FooBar contract ( FooBar instance )
    {
        return new FooBarContract ( instance );
    }

    public abstract int doSomething ( Object x );
}


public class FooBarContract implements FooBar, Contract
{
    private final FooBar instance;

    public FooBarContract ( FooBar instance )
    {
        this.instance = instance;
    }

    public int doSomething ( Object x )
        throws ContractViolationException
    {
        if ( x == null
             || x instanceof Nobbin )
        {
            // Might throw ContractViolationException:
            ContractArbiter.get ().handleParameterViolation ( "x", x );
        }
        else
        {
            int result = this.instance.doSomething ( x );

            if ( result < 0
                 || result > 100 )
            {
                // Might throw ContractViolationException:
                ContractArbiter.get ().handleResultViolation ( result );
            }
            else
            {
                return result;
            }
        }

        // Some kind of contract violation.
        return -1; // Or some other non-null "default" result.
    }
}

// The ContractArbiter decides whether to log contract violations,
// notify someone by email, throw an exception, etc.  Also keeps
// track of how many contract violations there have been since the
// last time it was explicitly asked to reset the counts.
// Configurable statistics gathering.


========================================

public enum ContractPartner
{
    PROVIDER,
    CONSUMER,
    THIRD_PARTY;
}

public class ContractViolationException
    extends I18nRuntimeException
{
    public ContractPartner violator ();
}
