public class AllDomains
{
    public static class GreaterThanZero
        implements Domain
    {
        public static final GreaterThanZero DOMAIN =
            new GreaterThanZero ();

        private GreaterThanZero ()
        {
        }

        @Override
        public boolean contains ( Object object )
        {
            if ( object == null
                 || ! ( object instanceof Integer ) )
            {
                return false;
            }

            Integer i = (Integer) object;
            if ( i.intValue () > 0 )
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        @Override
        public String toString ()
        {
            return "greater than 0";
        }
    }
}
