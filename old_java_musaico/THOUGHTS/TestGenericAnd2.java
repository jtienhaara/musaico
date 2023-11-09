// does not compile.  :(
public interface TestGenericAnd2<GEN extends TestGenericAnd2<GEN, VALUE>, VALUE extends Object>
{
    public static interface Sub<SUB extends TestGenericAnd2<SUB, SUB_VALUE>, SUB_VALUE extends Object>
        extends TestGenericAnd2<SUB, SUB_VALUE>
    {
        public abstract SUB doSomething ();
    }

    public abstract <SUB extends GEN & TestGenericAnd2.Sub<GEN, VALUE>>
        SUB sub ();
}
