public class Transaction
{
    public static BehaviourContract<Transaction> [] behaviours ()
    {
        final Domain itemReturned
        final Obligation itemReturned =
            new DomainObligation (
                !!!!!!!
        final BehaviourContract [] behaviours =
            new BehaviourContract []
            {
                new Behaviour ()
                    .given ( itemReturned )
                    .when ( customerAdded )
                    .then ( itemPriceIsNegative )
                    .then ( subTotalIsNegative )
                    .build ()
            };

        return behaviours;
    }
}
