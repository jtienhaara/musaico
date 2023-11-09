public class BankAccount
    implements Account
{
    private Money currentBalance = new Money ( 0D );

    @Override
    public Money balance ()
        throws ReturnNeverNull
    {
        return ReturnNeverNull.check ( this, this.currentBalance );
    }

    @Override
    public void deposit ( Money amount )
        throws ParametersMustNotBeNull
    {
        ParametersMustNotBeNull.check ( this, amount );

        this.currentBalance = this.currentBalance.add ( amount );
    }


    @Override
    public Money withdraw ( Money amount )
        throws ParametersMustNotBeNull,
               AmountMustNotExceedBalance,
               ReturnNeverNull,
               WithdrawalEqualsAmount
    {
        ParametersMustNotBeNull.check ( this, amount );
        AmountMustNotExceedBalance.check ( this, amount );

        this.currentBalance = this.currentBalance.subtract ( amount );

        return WithdrawalEqualsAmount.check ( this, amount,
                                              ReturnNeverNull.check ( this,
                                                                      amount ) );
    }

    @Override
    public String toString ()
    {
        return "BankAccount with balance: " + this.currentBalance;
    }

    public static void main (
                             String [] args
                             )
        throws RuntimeException
    {
        Account account = new BankAccount ();

        System.out.println ( "Balance = " + account.balance () );

        Money deposit = new Money ( 10.00D );
        account.deposit ( deposit );
        System.out.println ( "After depositing " + deposit
                             + " = " + account.balance () );

        Money withdrawal = account.withdraw ( new Money ( 5.00D ) );
        System.out.println ( "After withdrawing " + withdrawal
                             + " = " + account.balance () );

        Money amount = new Money ( 8.00D );
        System.out.println ( "Now withdrawing " + amount
                             + " should blow up:" );
        account.withdraw ( amount );
    }
}
