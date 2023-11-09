public class Parameter1
{
    public static class MustBeGreaterThanZero
        extends ParameterN.MustBeGreaterThanZero<Parameter1.MustBeGreaterThanZero.Violation>
    {
        public static class Violation
            extends ParameterN.Violation
        {
            public Violation ( Domain domain, Object obj )
            {
                super ( domain, obj );
            }
        }

        @Override
        public void throwViolation ( Object obj )
            throws Violation
        {
            throw new Violation ( this, obj );
        }
    }

}
