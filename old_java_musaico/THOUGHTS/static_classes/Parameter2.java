public class Parameter2
{
    public static class MustBeGreaterThanZero
        extends ParameterN.MustBeGreaterThanZero<ParameterN.Violation>
    {
        @Override
        public void throwViolation ( Object obj )
            throws ParameterN.Violation
        {
            throw new ParameterN.Violation ( this, obj );
        }
    }

}
