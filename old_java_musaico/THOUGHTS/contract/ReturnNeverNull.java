public class ReturnNeverNull
    extends GuaranteeException
{
    private ReturnNeverNull ( CONTRACT contract )
    {
        super ( contract );
    }

    public static final
        <RESULT extends Object>
            RESULT check ( Object object, RESULT result )
    {
        if ( Arbiter.isContractInspectionEnabled () )
        {
            Arbiter.inspect ( new CONTRACT ( object, result ) );
        }

        return result;
    }

    private static class CONTRACT
        implements Guarantee
    {
        private final Object object;
        private final Object result;

        public CONTRACT ( Object object, Object result )
        {
            this.object = object;
            this.result = result;
        }

        @Override
        public Object [] actuals ()
        {
            return new Object [] { this.object, this.result };
        }

        @Override
        public final void enforce ()
            throws ReturnNeverNull
        {
            if ( this.result == null )
            {
                throw new ReturnNeverNull ( this );
            }
        }

        public String toString ()
        {
            return "" + this.object + " will never return null actual result: "
                + this.result;
        }
    }
}
