public interface Operation<IN extends Object, OUT extends Object>
    extends Serializable
{
    public Term<OUT> apply (
                            Term<IN> input
                            );
}

public class First<VALUE extends Object>
    implements Operation<VALUE, VALUE>
{
    private final firstNElements;

    @Override
    public Term<VALUE> apply (
                              Term<VALUE> input
                              )
    {
        for ( VALUE element : input.value () )
        {
            this.output.add
    }
}




public interface Invocation<IN extends Object, OUT extends Object>
    extends Serializable
{
    public abstract Term<IN> input ();
    public abstract Term<OUT
        }

public interface Invocation<IN extends Object, OUT extends Object>
    extends Serializable
{
    public abstract Invocation<IN, OUT> minOutputs ( long min_outputs );
    public abstract Invocation<IN, OUT> maxOutputs ( long max_outputs );
    public abstract Invocation<IN, OUT> outputOffset ( long offset );
    public abstract Term<OUT> read ();
    public abstract Invocation<IN, OUT> write ( Term<IN> );
    public abstract Invocation<IN, OUT> close ();
}



Maybe just stick with simple Operations for now...
