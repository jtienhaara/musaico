public class Money
    implements Comparable<Money>
{
    private final double amount;

    public Money ( double amount )
    {
        this.amount = amount;
    }

    @Override
    public int compareTo ( Money that )
    {
        return new Double ( this.amount ).compareTo ( that.amount );
    }

    @Override
    public String toString ()
    {
        return "$" + this.amount;
    }

    public Money add ( Money that )
    {
        return new Money ( this.amount + that.amount );
    }

    public Money subtract ( Money that )
    {
        return new Money ( this.amount - that.amount );
    }
}
