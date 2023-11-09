// does not compile.  :(
public interface TestGenericAnd4<GEN extends TestGenericAnd4<GEN, VALUE>, VALUE extends Object>
{
    public static interface Sub<SUB extends TestGenericAnd4<SUB, SUB_VALUE>, SUB_VALUE extends Object>
        extends TestGenericAnd4<SUB, SUB_VALUE>
    {
        public abstract SUB doSomething ();
    }

    public abstract <SUB extends TestGenericAnd4.Sub<GEN, VALUE> &  GEN>
        SUB sub ();
}
