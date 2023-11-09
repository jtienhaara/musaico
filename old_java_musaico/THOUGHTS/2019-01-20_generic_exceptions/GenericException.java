public class GenericException<EVIDENCE extends Object>
    extends Exception
{
    public static class GenericThrower<BAD extends Object>
    {
        private final BAD bad;
        public GenericThrower (
                BAD bad
                )
        {
            this.bad = bad;
        }

        public int maybeThrowSomething ()
            throws GenericException<BAD>
        {
            throw new GenericException<BAD> ( this.bad );
        }
    }


    private final EVIDENCE evidence;
    public GenericException (
            EVIDENCE evidence
            )
    {
        this.evidence = evidence;
    }

    public final EVIDENCE evidence ()
    {
        return this.evidence;
    }

    public static void main (
            String [] args
            )
        throws Exception
    {
        final GenericThrower<String> thrower = new GenericThrower<String> ( "evidenceString" );
        try
        {
            thrower.maybeThrowSomething ();
        }
        catch ( GenericException<String> e )
        {
            System.out.println ( "YAY threw generic exception <String> : " + e.evidence () );
        }
    }
}
