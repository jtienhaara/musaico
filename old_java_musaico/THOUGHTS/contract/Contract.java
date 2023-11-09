public interface Contract
{
    public abstract Object [] actuals ();

    public abstract void enforce ()
        throws ContractException;
}
