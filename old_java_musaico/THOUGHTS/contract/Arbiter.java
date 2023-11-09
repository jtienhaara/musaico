public class Arbiter
{
    public static boolean isContractInspectionEnabled ()
    {
        return true;
    }

    public static void inspect ( Contract contract )
        throws ContractException
    {
        if ( isContractInspectionEnabled () )
        {
            contract.enforce (); // Throws exception up the stack.
        }
    }
}
