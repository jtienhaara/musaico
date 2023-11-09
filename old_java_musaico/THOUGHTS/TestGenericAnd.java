public interface TestGenericAnd<GEN extends TestGenericAnd<GEN, VALUE>, VALUE extends Object>
{
    public static interface Sub<SUB extends TestGenericAnd<SUB, SUB_VALUE>, SUB_VALUE extends Object>
        extends TestGenericAnd<SUB, SUB_VALUE>
    {
        public abstract SUB doSomething ();
    }

    public abstract <SUB extends TestGenericAnd<GEN, VALUE> & TestGenericAnd.Sub<GEN, VALUE>>
        SUB sub ();
}
