public abstract class Domain<VIOLATION extends Exception>
{
    public abstract void check ( Object object )
        throws VIOLATION;

    public abstract void throwViolation ( Object obj )
        throws VIOLATION;

    public static abstract class MustBeGreaterThanZero<VIOLATION extends Exception>
        extends Domain<VIOLATION>
    {
        @Override
        public void check ( Object object )
            throws VIOLATION
        {
            if ( ! ( object instanceof Integer ) )
            {
                throwViolation ( object );
            }

            Integer i = (Integer) object;
            if ( i.intValue () <= 0 )
            {
                throwViolation ( object );
            }
        }
    }
}
