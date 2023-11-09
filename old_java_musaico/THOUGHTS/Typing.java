public class Instance<VALUE extends Object>
    implements Serializable
{
    private static final Operation<VALUE, VALUE> READ =
        new Operation<VALUE, VALUE> ()
    {
        @Override
        public final Conditional<VALUE> evaluate ( VALUE input )
        {
            return new Successful<VALUE, Typing.Violation> ( input, !!! );
        }
    }

    private final Type<VALUE> type;
    private final VALUE value;

    public Conditional<VALUE, Typing.Violation> value ()
    {
        Operation<VALUE, VALUE> read = this.type.transform ( Instance.READ );
        return read.evaluate ( this.value );
    }

    public Conditional<VALUE, Typing.Violation> value ( Tag ... tags )
    {
        Conditional<VALUE, Typing.Violation> conditional_value = this.value ();
        if ( ! ( conditional_value instanceof Successful ) )
        {
            return conditional_value;
        }

        Operation<VALUE, VALUE> read = Instance.READ;
        for ( Tag tag : tags )
        {
            read = tag.transform ( read );
        }
        return read.evaluate ( conditional_value.orNone () );
    }

    public <CAST extends Object>
        Conditional<CAST, Typing.Violation> value ( Class<CAST> to_class, Tag ... tags )
    {
        final Conditional<VALUE, Typing.Violation> conditional_value = this.value ();
        if ( ! ( conditional_value instanceof Successful ) )
        {
            return conditional_value;
        }

        final Conditional<Operation<VALUE, CAST>, Typing.Violation> conditional_caster =
            this.type.casterTo ( to_class, tags );
        if ( ! ( caster instanceof Success ) )
        {
            return new Failure ( !!!, conditional_caster.exception (), !!! );
        }
        final Operation<VALUE, CAST> caster = conditional_caster.orNone ();
        final VALUE value = conditional_value.orNone ();
        final Conditional<CAST, Typing.Violation> conditional_cast =
            caster.evaluate ( value );
        return conditional_cast;
    }
}

public interface Operation<INPUT extends Object, OUTPUT extends Object>
    extends Serializable
{
    public abstract Conditional<RESULT, Typing.Violation> evaluate ( INPUT input );
}

public interface Cast<SOURCE extends Object, TARGET Extends Object>
    extends Operation<SOURCE, TARGET>, Serializable
{
}


public interface Tag
    extends Serializable
{
    public abstract String id ();
    public abstract <SOURCE extends Object, TARGET extends Object>
        Operation<SOURCE, TARGET> transform ( SOURCE input, Operation<SOURCE, TARGET> op );
}


public class Type<VALUE extends Object>
    extends Tag, Serializable
{
    private final Tag [] tags;

    @Override
    public <SOURCE extends Object, TARGET extends Object>
        Operation<SOURCE, TARGET> transform ( SOURCE input, Operation<SOURCE, TARGET> op )
    {
        Operation<SOURCE, TARGET> transformed_op = op;
        for ( Tag tag : this.tags )
        {
            transformed_op = tag.transform ( transformed_op );
        }

        return transformed_op;
    }
}
