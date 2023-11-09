import java.io.Serializable;

public interface TestGenericAnd6<GEN extends TestGenericAnd6<GEN, VALUE>, VALUE extends Object>
{
    public static interface Sub<SUB extends TestGenericAnd6<SUB, SUB_VALUE>, SUB_VALUE extends Object>
        extends TestGenericAnd6<SUB, SUB_VALUE>
    {
        public abstract SUB doSomething ();
    }

    public abstract <SUB extends TestGenericAnd6.Sub<GEN, VALUE> &  Serializable>
        SUB sub ();
}
