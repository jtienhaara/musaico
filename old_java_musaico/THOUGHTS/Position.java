public interface Position
    extends Constant<Serializable>
{
    public abstract Space space ();
    public abstract PositionExpression expr ();
}

public interface PositionExpression
    extends Expression<SpatialElement>
{
    add () etc -> e.g. implementation:
    public static final Curry2<SpatialElement, SpatialElement, SpatialElement> ADD = ...;
    public XYZPositionExpression add ( Size size )
    {
        return new XYZPositionExpression ( ADD, this );
    }
}

public interface Expression<INPUT, OUTPUT>
    extends Term<OUTPUT>
{
    public Operation<INPUT, OUTPUT> operation ();
    --> Probably should be either Value<Operation<INPUT, OUTPUT>>
            or Term<Operation<INPUT, OUTPUT>> (which is hard
            because Term.type () = a Type<Operation> no INPUT, OUTPUT)
    public Term<INPUT> input ();
}

public class StandardExpression<INPUT, OUTPUT>
    implements Expression<INPUT, OUTPUT>
{
    public Value<OUTPUT> value ()
    {
        final int STACK_OVERFLOW = 16384;
        Operation<
        for ( 
}
        !!!!!!!!!!!!!;

public interface ExpressionBuilder<INPUT, OUTPUT>
{
    public abstract Expression<INPUT, OUTPUT> build ();
    public abstract ExpressionBuilder<INPUT, OUTPUT> op ( Operation<?, ?> op );
    public abstract Value<Operation<?, ?>> operations ();
    public abstract Term<INPUT> input ();
}

public interface Expression<INPUT, OUTPUT>
    extends Term<OUTPUT>
{
    public Term<INPUT> input ();
    public abstract Value<Operation<?, ?>> operations ();
    // value () evaluates the expression and returns a Value<OUTPUT>.
}

for example,
Position1 + ( Size1 / Size2 ) - Position2 = Size3
!!!
Time1 + ( Interval1 / Interval2 ) - Time2 = Interval3

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


    position1.expr ( Position.add ( size1.expr ( Size.divide ( size2 ) ) ) )

ugh


    size3 = position1.add ( size1.divide ( size2 ) ).subtract ( position2 )
    .value () or .none () or etc;


public class Add<Integer>
    implements Operation<Integer, Integer>
{
    body ( Value<Integer> input )
    {
        int sum = 0;
        for ( Integer element : input )
        {
            sum += element.intValue ();
        }

        return new One<Integer> ( Integer.class, sum );
    }
}
Concatenate<VALUE extends Object>
extends Curry2<VALUE>
{
    body ( VALUE input1, VALUE input2 )
    {
        ValueBuilder<VALUE> builder =
            new ValueBuilder<VALUE> ( this.outputType ().valueClass (),
                                      this.outputType ().none () );

        builder.addAll ( input1 );
        builder.addAll ( input2 );

        final Value<VALUE> output = builder.build ();
        return output;
    }
}

Add.evaluate ( Concatenate.evaluate ( term1.value (), term2.value () ) );



public class Read<VALUE extends Object>
{
    public static class Builder<BUILD_VALUE extends Object>
    {
        public Value<Read<BUILD_VALUE>> build () { ... }
        public Builder<BUILD_VALUE> type ( Type<BUILD_VALUE> type ) { ... }
    }

    ...;
}

