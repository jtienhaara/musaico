public class Parameter1
{
    public static class MustBeGreaterThanZero
        extends ParameterN.MustBeGreaterThanZero<Parameter1.MustBeGreaterThanZero.Violation>
    {
        public static final MustBeGreaterThanZero CONTRACT =
            new MustBeGreaterThanZero ();

        public static class Violation
            extends DomainViolation
        {
        }
    }
}
