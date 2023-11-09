// compiles but unlike 2 and 4 is nothing like what we want; just
// a test of syntax.  :(
import java.io.Serializable;

public interface TestGenericAnd5<GEN extends TestGenericAnd5<GEN, VALUE>, VALUE extends Object>
{
    public static interface Sub<SUB extends TestGenericAnd5<SUB, SUB_VALUE>, SUB_VALUE extends Object>
        extends TestGenericAnd5<SUB, SUB_VALUE>
    {
        public abstract SUB doSomething ();
    }

    public abstract <SUB extends TestGenericAnd5.Sub<GEN, VALUE> &  Serializable>
        SUB sub ();
}
