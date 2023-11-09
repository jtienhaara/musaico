// does not compile.  :(
public class TestGenericAnd3<VALUE extends Object>
    implements TestGenericAnd3.Sub<TestGenericAnd3<VALUE>, VALUE>
{
    public static interface Generic<GEN extends TestGenericAnd3.Generic<GEN, VALUE>, VALUE extends Object>
    {
        public abstract <SUB extends TestGenericAnd3<GEN, VALUE> & TestGenericAnd3.Sub<GEN, VALUE>>
            SUB sub ();
    }


    public static interface Operation<OP extends TestGenericAnd3.Generic<OP, OUTPUT>, OUTPUT extends Object>
        extends TestGenericAnd3<OP, OUTPUT>
    {
        public abstract OUTPUT output ();
    }


    public static interface Select<SEL extends TestGenericAnd3<SEL, SEL_VALUE>, SEL_VALUE extends Object>
        extends TestGenericAnd3<SEL, SEL_VALUE>
!!! bleah
    {
        public abstract SEL addToChain ();
    }


    public static void main ( String [] args )
    {
        final TestGeneric
    }
}
