public class Return
{
    public static interface Checker
    {
        public void check ( Object value )
            throws RuntimeException;
    }


    public static class MustBeGreaterThanZero
        implements Checker
    {
        public static final MustBeGreaterThanZero CONTRACT =
            new MustBeGreaterThanZero ();

        @Override
        public final void check ( Object value )
            throws MustBeGreaterThanZero.Violation
        {
            if ( value == null )
            {
                throw new MustBeGreaterThanZero.Violation ( "" + this + " ( " + value + " )" );
            }
            else if ( ! ( value instanceof Integer ) )
            {
                throw new MustBeGreaterThanZero.Violation ( "" + this + " ( " + value + " )" );
            }
            final Integer ivalue = (Integer) value;
            final int int_value = ivalue.intValue ();
            if ( int_value <= 0 )
            {
                throw new MustBeGreaterThanZero.Violation ( "" + this + " ( " + value + " )" );
            }
        }

        @Override
        public String toString ()
        {
            return "" + this.getClass ().getSimpleName ();
        }

        public static class Violation
            extends RuntimeException
        {
            Violation ( String message )
            {
                super ( message );
            }
        }

        public static class Or
            extends Return
        {
        }
    }




    public static class MustBeLessThanTen
        implements Checker
    {
        public static final MustBeLessThanTen CONTRACT =
            new MustBeLessThanTen ();

        @Override
        public final void check ( Object value )
            throws MustBeLessThanTen.Violation
        {
            if ( value == null )
            {
                throw new MustBeLessThanTen.Violation ( "" + this + " ( " + value + " )" );
            }
            else if ( ! ( value instanceof Integer ) )
            {
                throw new MustBeLessThanTen.Violation ( "" + this + " ( " + value + " )" );
            }
            final Integer ivalue = (Integer) value;
            final int int_value = ivalue.intValue ();
            if ( int_value >= 10 )
            {
                throw new MustBeLessThanTen.Violation ( "" + this + " ( " + value + " )" );
            }
        }

        @Override
        public String toString ()
        {
            return "" + this.getClass ().getSimpleName ();
        }

        public static class Violation
            extends RuntimeException
        {
            Violation ( String message )
            {
                super ( message );
            }
        }
    }




    public static class Either<GUARANTEE1 extends Return.Checker, GUARANTEE2 extends Return.Checker>
                                                                                     extends Return.Checker
    {
        private final GUARANTEE1 guarantee1;
        private final GUARANTEE2 guarantee2;
        public Either ( GUARANTEE1 guarantee1,
                        GUARANTEE2 guarantee2 )
        {
            this.guarantee1 = guarantee1;
            this.guarantee2 = guarantee2;
        }

        public void check ( Object value )
            throws Return.Either.Violation
        {
            try
            {
                guarantee1.check ( value );
            }
            catch ( RuntimeException violation1 )
            {
                try
                {
                    guarantee2.check ( value );
                }
                catch ( RuntimeException violation2 )
                {
throw new Return.Either!!!wont work as planned.




    public static void main ( String [] args )
    {
        final Return.MustBeGreaterThanZero contract1 =
            new Return.MustBeGreaterThanZero ();
        final Return.MustBeGreaterThanZero.Or.MustBeLessThanTen contract2 =
            new Return.MustBeGreaterThanZero.Or.MustBeLessThanTen ();
        System.out.println ( "Contract 1 : " + contract1
                             + " full class = " + contract1.getClass () );
        System.out.println ( "Contract 2 : " + contract2
                             + " full class = " + contract2.getClass () );
    }
}
