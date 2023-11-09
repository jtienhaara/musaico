public class Parameter3
{
    public static class MustBeGreaterThanZero
        extends ParameterN.MustBeGreaterThanZero<Parameter3.MustBeGreaterThanZero.Violation>
    {
        public static class Violation
            extends ParameterN.MustBeGreaterThanZero.Violation
        {
            public Violation ( Domain domain, Object obj ) { super ( domain, obj ); }
        }
    }
}
