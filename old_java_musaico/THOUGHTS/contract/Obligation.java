public interface Obligation
    extends Contract
{
    @Override
    public abstract void enforce ()
        throws ObligationException;
}
