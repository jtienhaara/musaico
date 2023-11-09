import java.lang.reflect.Constructor;

public class ParameterN
{
    public static class MustBeGreaterThanZero<VIOLATION extends ParameterN.MustBeGreaterThanZero.Violation>
        extends Domain.MustBeGreaterThanZero<VIOLATION>
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
            throws VIOLATION
        {
            final VIOLATION violation;
            try
            {
                String class_name = this.getClass ().getName ();
                class_name += ".Violation";
                Class<VIOLATION> violation_class = (Class<VIOLATION>)
                    Class.forName ( class_name );
                Constructor constructor =
                    violation_class.getConstructor ( Domain.class, Object.class );
               violation = (VIOLATION)
                    constructor.newInstance ( new Object [] { this, obj } );
            }
            catch ( Exception e )
            {
                e.printStackTrace ();
                throw new RuntimeException ( "Screwed up", e );
            }

            throw violation;
        }

        @Override
        public String toString ()
        {
            return getClass ().getName ();
        }
    }

    public static class Violation
        extends Exception
    {
        public Violation ( Domain domain, Object obj )
        {
            super ( "" + domain + " : " + obj );
        }
    }
}
