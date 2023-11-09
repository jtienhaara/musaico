public class ThrowsGenericExceptions
{
    public void doSomething ()
        throws GenericException<String>,
               GenericException<Integer>
    {
        System.out.println ( "No exception this time" );
    }
}
