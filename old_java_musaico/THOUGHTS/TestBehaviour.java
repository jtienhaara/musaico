import java.math.BigDecimal;

public class TestBehaviour
{
    public static interface Behaviour<CONTROL extends BehaviourControl>
    {
        public CONTROL control ();
        public <VALUE extends Object, BUILT extends Object>
            BehaviourPipeline<VALUE, BUILT, ? extends CONTROL> control ( Pipeline<VALUE, BUILT> pipeline );
    }

    public static interface Pipeline<VALUE extends Object, BUILT extends Object>
    {
        public <CONTROL extends BehaviourControl>
            BehaviourPipeline<VALUE, BUILT, ? extends CONTROL> on ( Behaviour<CONTROL> behaviour );

        public Pipeline<VALUE, BUILT> doSomethingInOriginalPipeline ();
    }

    public static interface BehaviourControl
    {
    }

    public static interface BehaviourPipeline<VALUE extends Object, BUILT extends Object, PIPELINE extends BehaviourControl & BehaviourPipeline<VALUE, BUILT, PIPELINE>>
    {
        public PIPELINE always ();
    }


    public static class Blocking
        implements Behaviour<BlockingControl>
    {
        public static final Blocking BEHAVIOUR =
            new Blocking ();

        @Override
        public BlockingControl control ()
        {
            return new BlockingControl ();
        }

        @Override
        public <VALUE extends Object, BUILT extends Object>
            BlockingPipeline<VALUE, BUILT> control ( Pipeline<VALUE, BUILT> pipeline )
        {
            return new BlockingPipeline<VALUE, BUILT> ( pipeline );
        }
    }

    public static class StandardPipeline<VALUE extends Object, BUILT extends Object>
        implements Pipeline<VALUE, BUILT>
    {
        @Override
            public <CONTROL extends BehaviourControl>
            BehaviourPipeline<VALUE, BUILT, ? extends CONTROL> on ( Behaviour<CONTROL> behaviour )
        {
            return behaviour.control ( this );
        }

        @Override
        public Pipeline<VALUE, BUILT> doSomethingInOriginalPipeline ()
        {
            System.out.println ( "Back to the original pipeline." );
            return this;
        }
    }

    public static class BlockingControl
        implements BehaviourControl
    {
        public Pipeline<?, ?> await ( BigDecimal time )
        {
            System.out.println ( "Waiting up to " + time + " for blocking" );
            return null;
        }
    }

    public static class BlockingPipeline<VALUE extends Object, BUILT extends Object>
        extends BlockingControl
        implements BehaviourPipeline<VALUE, BUILT, BlockingPipeline<VALUE, BUILT>>
    {
        private final Pipeline<VALUE, BUILT> parentPipeline;

        public BlockingPipeline ( Pipeline<VALUE, BUILT> parent_pipeline )
        {
            this.parentPipeline = parent_pipeline;
        }

        @Override
        public BlockingPipeline<VALUE, BUILT> always ()
        {
            return this;
        }

        @Override
        public Pipeline<VALUE, BUILT> await ( BigDecimal time )
        {
            super.await ( time );
            return this.parentPipeline;
        }
    }


    public static interface Term<VALUE extends Object>
    {
    }


    public static void main (
                             String [] args
                             )
    {
        final Pipeline<String, Term<String>> pipeline =
            new StandardPipeline<String, Term<String>> ();

        pipeline.on ( Blocking.BEHAVIOUR ).always ().await ( BigDecimal.TEN )
            .doSomethingInOriginalPipeline ();
    }
}
