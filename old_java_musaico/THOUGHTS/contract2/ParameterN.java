public class ParameterN
{
    public static class MustBeGreaterThanZero<VIOLATION extends DomainViolation>
        extends DomainContract<VIOLATION>
    {
        public MustBeGreaterThanZero ()
        {
            super ( AllDomains.GreaterThanZero.DOMAIN );
        }

        @Override
        public VIOLATION createViolation ()
        {
            String class_name = this.getClass ().getName ();
            final VIOLATION violation;
            try
            {
                Class<VIOLATION> violation_class = (Class<VIOLATION>)
                    Class.forName ( class_name + "$Violation" );
                violation = violation_class.newInstance ();
            }
            catch ( Exception e )
            {
                throw new IllegalStateException ( "Uh-oh!", e );
            }

            return violation;
        }
    }
}
