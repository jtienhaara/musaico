public class ParametersMustNotBeNull
    extends ObligationException
{
    private ParametersMustNotBeNull ( CONTRACT contract )
    {
        super ( contract );
    }

    public static final void check ( Object object, Object ... parameters )
    {
        if ( Arbiter.isContractInspectionEnabled () )
        {
            Arbiter.inspect ( new CONTRACT ( object, parameters ) );
        }
    }

    private static class CONTRACT
        implements Obligation
    {
        private final Object object;
        private final Object [] parameters;

        public CONTRACT ( Object object, Object ... parameters )
        {
            this.object = object;
            this.parameters = new Object [ parameters.length ];
            System.arraycopy ( parameters, 0, this.parameters, 0, parameters.length );
        }

        @Override
        public Object [] actuals ()
        {
            Object [] actuals = new Object [ this.parameters.length + 1 ];
            actuals [ 0 ] = this.object;
            System.arraycopy ( this.parameters, 0,
                               actuals, 1, this.parameters.length );
            return actuals;
        }

        @Override
        public final void enforce ()
            throws ParametersMustNotBeNull
        {
            for ( Object parameter : this.parameters )
            {
                if ( parameter == null )
                {
                    throw new ParametersMustNotBeNull ( this );
                }
            }
        }

        @Override
        public String toString ()
        {
            return "Object " + this.object
                + " must not receive null parameters "
                + this.argumentsToString ( this.parameters );
        }

        private String argumentsToString ( Object ... arguments )
        {
            StringBuilder sbuf = new StringBuilder ();
            for ( int a = 0; a < arguments.length; a ++ )
            {
                if ( a > 0 )
                {
                    sbuf.append ( ", " );
                }

                sbuf.append ( "" + arguments [ a ] );
            }

            return sbuf.toString ();
        }
    }
}
