public class OEntryCapability
    extends Capability
{
    private final String name;

    public OEntryCapability ()
    {
        this.name = this.getClass ().getSimpleName ();
    }

    public String name ()
    {
        return this.name;
    }

    public String toString ()
    {
        return this.name;
    }
}
