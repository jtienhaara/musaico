public class Transaction2
{
    public static BehaviourContract<Transaction2> [] behaviours ()
    {
        return new BehaviourContract<Transaction2> []
            {
                new Behaviour<Transaction2> ()
                    .given ( new TransactionHasReturnedItemFilter () )
                    .when ( new AddCustomerToTransactionEvent () )
                    .then ( new ReturnedItemHasNegativePrice () )
                    .then ( new TransactionHasNegativeSubTotal () )
                    .build ();
            };
    }
}

But Behaviour is just a restricted state machine.  :(  ) ;

new Behaviour<Transaction2> ();


---- ;



new Behaviour ()
    .given ( Transaction.class )
    .with ( new State<Transaction> () { public boolean matches () { return this.object ().hasReturnedItems (); } } )
    .when ( 