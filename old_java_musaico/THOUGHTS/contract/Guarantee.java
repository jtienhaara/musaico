public interface Guarantee
    extends Contract
{
    @Override
    public abstract void enforce ()
        throws GuaranteeException;
}
