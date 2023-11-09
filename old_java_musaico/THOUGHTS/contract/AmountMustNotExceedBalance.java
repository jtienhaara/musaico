public class AmountMustNotExceedBalance
    extends ObligationException
{
    private AmountMustNotExceedBalance ( CONTRACT contract )
    {
        super ( contract );
    }

    public Account account ()
    {
        return ( (CONTRACT) this.contract () ).account;
    }

    public Money amount ()
    {
        return ( (CONTRACT) this.contract () ).amount;
    }

    public static final void check ( Account account, Money amount )
    {
        if ( Arbiter.isContractInspectionEnabled () )
        {
            Arbiter.inspect ( new CONTRACT ( account, amount ) );
        }
    }

    private static class CONTRACT
        implements Obligation
    {
        private final Account account;
        private final Money amount;

        public CONTRACT ( Account account, Money amount )
        {
            this.account = account;
            this.amount = amount;
        }

        @Override
        public final Object [] actuals ()
        {
            return new Object [] { this.account, this.amount };
        }

        @Override
        public final void enforce ()
            throws AmountMustNotExceedBalance
        {
            Money balance = this.account.balance ();
            if ( balance != null )
            {
                if ( this.amount.compareTo ( balance ) <= 0 )
                {
                    // Contract OK.
                    return;
                }
            }

            // Some kind of breach.  The arbiter might throw the exception
            // up the chain, or she might catch it and do something else
            // (log it, exit, etc).
            throw new AmountMustNotExceedBalance ( this );
        }

        @Override
        public String toString ()
        {
            return "Amount " + this.amount
                + " must not exceed the balance in account " + this.account;
        }
    }
}
