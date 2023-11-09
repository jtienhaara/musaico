public class ONodeData
{
    public static class Read extends OEntryCapability {}
    public static class Write extends OEntryCapability {}
    public static class Execute extends OEntryCapability {}

    @Capabilities(Read.class)
    public String read ( int count )
    {
        return "" + count;
    }

    @Capabilities({ Read.class, Execute.class })
    public String write ( int count )
    {
        return "" + count;
    }
}
