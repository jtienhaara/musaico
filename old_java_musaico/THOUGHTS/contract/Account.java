public interface Account
{
    public abstract Money balance ()
        throws ReturnNeverNull;

    public abstract void deposit ( Money amount )
        throws ParametersMustNotBeNull;

    public abstract Money withdraw ( Money amount )
        throws ParametersMustNotBeNull,
               AmountMustNotExceedBalance,
               ReturnNeverNull,
               WithdrawalEqualsAmount;
}
