public abstract class ContractException
    extends RuntimeException
{
    private final Contract contract;

    public ContractException ( Contract contract )
    {
        super ( "Contract violation: " + contract );

        this.contract = contract;
    }

    public Contract contract ()
    {
        return this.contract;
    }
}
