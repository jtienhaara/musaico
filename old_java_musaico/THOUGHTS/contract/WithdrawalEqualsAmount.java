public class WithdrawalEqualsAmount
    extends GuaranteeException
{
    private WithdrawalEqualsAmount ( CONTRACT contract )
    {
        super ( contract );
    }

    public static final Money check ( Account account, Money amount,
                                      Money withdrawal )
    {
        if ( Arbiter.isContractInspectionEnabled () )
        {
            Arbiter.inspect ( new CONTRACT ( account, amount, withdrawal ) );
        }

        return withdrawal;
    }

    private static class CONTRACT
        implements Guarantee
    {
        private final Account account;
        private final Money amount;
        private final Money withdrawal;

        public CONTRACT ( Account account, Money amount, Money withdrawal )
        {
            this.account = account;
            this.amount = amount;
            this.withdrawal = withdrawal;
        }

        @Override
        public Object [] actuals ()
        {
            return new Object [] { this.account, this.amount, this.withdrawal };
        }

        @Override
        public final void enforce ()
            throws WithdrawalEqualsAmount
        {
            if ( this.withdrawal.compareTo ( this.amount ) != 0 )
            {
                throw new WithdrawalEqualsAmount ( this );
            }
        }

        public String toString ()
        {
            return "" + this.account + " will always return the amount "
                + this.amount + " withdrawn: " + this.withdrawal;
        }
    }
}
