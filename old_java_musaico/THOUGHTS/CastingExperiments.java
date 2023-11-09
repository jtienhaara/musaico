public class CastingExperiments
{
    public static interface Op<IN extends Object, OUT extends Object>
    {
        public abstract OUT eval ( IN input );
    }

    public static class Times2
        implements Op<Number, Number>
    {
        public Number eval ( Number input )
        {
            return input.doubleValue () * 2D;
        }

        @Override
        public String toString ()
        {
            return "* 2";
        }
    }

    public static Op<?, ?> createOp ()
    {
        return new Times2 ();
    }

    public static Object createInput ()
    {
        return new Integer ( 1 );
    }

    public static void main ( String [] args )
    {
        final Op<?, ?> op = createOp ();
        final Object input = createInput ();

        final Op<Object, Object> cast_op = (Op<Object, Object>) op;

        final Object output = cast_op.eval ( input );

        System.out.println ( "" + input + " " + op + " --> " + output );
    }
}
