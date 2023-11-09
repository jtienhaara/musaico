import java.util.HashMap;
import java.util.Map;

public class Gen
{
    public static interface Type<X extends Object>
    {
        public abstract String getString ();
    }

    public static class Unknown
        implements Type<Object>
    {
        public String getString ()
        {
            return "Unknown";
        }
    }

    public static class IntegerType
        implements Type<Integer>
    {
        public String getString ()
        {
            return "Integer";
        }
    }

    public static class OperationType2<X extends Object, Y extends Object, OUT extends Object>
    {
        private final Map<String, Type<?>> symbolTable;
        public OperationType2 (
                               Type<X> in1,
                               Type<Y> in2,
                               Type<OUT> out
                               )
        {
            this.symbolTable = new HashMap<String, Type<?>> ();

            this.symbolTable.put ( "#input1", in1 );
            this.symbolTable.put ( "#input2", in2 );
            this.symbolTable.put ( "#output", out );
        }

        public Map<String, Type<?>> symbolTable ()
        {
            return this.symbolTable;
        }

        public Type<X> in1 ()
        {
            return (Type<X>) this.symbolTable.get ( "#input1" );
        }

        public Type<Y> in2 ()
        {
            return (Type<Y>) this.symbolTable.get ( "#input2" );
        }

        public Type<OUT> out ()
        {
            return (Type<OUT>) this.symbolTable.get ( "#output" );
        }
    }



    public static void main (
                             String [] args
                             )
    {
        Type<?> input1_type;
        Type<?> input2_type;
        Type<?> output_type;

        final Type<Object> unknown = new Unknown ();
        final Type<Integer> integer = new IntegerType ();

        final OperationType2<Object, Object, Object> operation =
            new OperationType2<Object, Object, Object> ( unknown,
                                                         unknown,
                                                         unknown );

        input1_type = operation.in1 ();
        input2_type = operation.in2 ();
        output_type = operation.out ();

        System.out.println ( "Types before resolving: "
                             + input1_type
                             + ", "
                             + input2_type
                             + ", "
                             + output_type );

        final Map<String, Type<?>> symbol_table = operation.symbolTable ();

        symbol_table.put ( "#input1", integer );
        symbol_table.put ( "#input2", integer );
        symbol_table.put ( "#output", integer );

        input1_type = operation.in1 ();
        input2_type = operation.in2 ();
        output_type = operation.out ();

        System.out.println ( "Types after resolving: "
                             + input1_type
                             + ", "
                             + input2_type
                             + ", "
                             + output_type );

        final OperationType2<?, ?, ?> op_step =
            (OperationType2<?, ?, ?>) operation;

        final OperationType2<Integer, Integer, Integer> op_ints =
            (OperationType2<Integer, Integer, Integer>) op_step;

        input1_type = operation.in1 ();
        input2_type = operation.in2 ();
        output_type = operation.out ();

        System.out.println ( "Types extra cast: "
                             + input1_type
                             + ", "
                             + input2_type
                             + ", "
                             + output_type );
    }
}
