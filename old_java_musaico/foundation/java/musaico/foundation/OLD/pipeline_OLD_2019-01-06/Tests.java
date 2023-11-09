package musaico.foundation.pipeline;

/**
 * Quick dirty tests to make sure Constructors and Choose's will
 * play the roles we need them to (at least as far as the compilation stage).
 */

public class Tests
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public <PIPELINE extends Sink<Thread, PIPELINE, Thread, PIPELINE, Source<PIPELINE, Thread, PIPELINE>>, CONSTRUCTOR extends Constructor<String, Constructor<Number, Constructor<int[], PIPELINE>>>>
        void test (
            CONSTRUCTOR constructor,
            Choose<PIPELINE> chooser,
            Choice<PIPELINE, PIPELINE> pipeline_choice,
            Choice<PIPELINE, CONSTRUCTOR> constructor_choice
            )
    {
        final Thread constructed =
            constructor.apply ( "Hello,",
                                "world" )
                       .apply ( 17L )
                       .apply ( new int [] { 1, 2, 3 },
                                new int [] { 4, 5, 6 } )
                       .output ();
        final Thread chosen_pipeline =
            chooser.choose ( pipeline_choice )
                   .output ();
        final Thread chosen_constructed =
            chooser.choose ( constructor_choice )
                   .apply ( "Bla" )
                   .apply ( 16D )
                   .apply ( new int [ 0 ] )
                   .output ();
    }


    public static void main (
                             String [] args
                             )
    {
        System.out.println ( "No real tests here,"
                             + " just compiling the source for this class"
                             + " successfully is the test." );
    }
}
