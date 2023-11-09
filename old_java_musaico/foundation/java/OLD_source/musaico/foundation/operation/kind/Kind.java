package musaico.foundation.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.Clock;

import musaico.foundation.term.Maybe;
import musaico.foundation.term.Operation;
import musaico.foundation.term.Term;
import musaico.foundation.term.TermPipeline;
import musaico.foundation.term.Transform;
import musaico.foundation.term.Type;

import musaico.foundation.term.abnormal.Error;

import musaico.foundation.term.builder.TermBuilder;

import musaico.foundation.term.countable.No;
import musaico.foundation.term.countable.One;

import musaico.foundation.term.expression.Expression;

import musaico.foundation.term.pipeline.TermTransform;

import musaico.foundation.term.iterators.IteratorMustBeFinite;


public class Kind
{
    private static final long serialVersionUID = 1L;

    public static interface Kind1<P1 extends Object, VALUE extends Object>
        extends Operation<P1, Type<VALUE>>
    {
        @Override
        public abstract Term<Type<VALUE>> apply (
                Term<P1> parameter1
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        public abstract Type<VALUE> construct (
                P1 parameter1
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }


    public static interface Curry<PREDECESSOR extends Operation<PARAMETER, Curry<PREDECESSOR, PARAMETER, INPUT, OUTPUT>>, PARAMETER extends Object, INPUT extends Object, OUTPUT extends Object>
        extends Operation<INPUT, OUTPUT>
    {
        @Override
        public abstract Term<OUTPUT> apply (
                Term<INPUT> input
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        public abstract Term<PARAMETER> parameter ()
            throws ReturnNeverNull.Violation;

        public abstract PREDECESSOR predecessor ()
            throws ReturnNeverNull.Violation;
    }


    public static interface Kind2<P1 extends Object, P2 extends Object, VALUE extends Object>
        extends Operation<P1, Curry<Kind2<P1, P2, VALUE>, P1, P2, Type<VALUE>>>
    {
        @Override
        public abstract Term<Curry<Kind2<P1, P2, VALUE>, P1, P2, Type<VALUE>>> apply (
                Term<P1> parameter1
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        public abstract Type<VALUE> construct (
                P1 parameter1,
                P2 parameter2
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }

    public static Type<Map<String, Integer>> foo ( Kind2<Type<String>, Type<Integer>, Map<String, Integer>> map_kind, One<Type<String>> string_type, One<Type<Integer>> integer_type )
    {
        final Type<Map<String, Integer>> map_type =
            map_kind.apply ( string_type )
                    .orNull ()
                    .apply ( integer_type )
                    .orNull ();
        final Term<Type<String>> p1 =
            map_kind.apply ( string_type ).orNull ().parameter ();
        return map_type;
    }


    public static interface ExtractFromType<EXTRACTION extends Object>
        extends Operation<Type<?>, EXTRACTION>
    {
        @Override
        public abstract Term<EXTRACTION> apply (
                Term<Type<?>> types
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        public abstract void extract (
                Type<?> type,
                TermBuilder<EXTRACTION> extractions
                )
            throws ParametersMustNotBeNull.Violation;
    }


    public abstract static class AbstractExtractFromTypeConstructor<EXTRACTION extends Object>
        implements ExtractFromType<EXTRACTION>
    {
        private static final long serialVersionUID = 1L;

        private final Type<Type<?>> inputType;
        private final Type<EXTRACTION> outputType;
        private final Type<EXTRACTION> errorType;

        public AbstractExtractFromTypeConstructor (
                Type<Type<?>> type_type,
                Type<EXTRACTION> extraction_type
                )
            throws ParametersMustNotBeNull.Violation
        {
            this.inputType = type_type;
            this.outputType = extraction_type;
            this.errorType = this.outputType
                .refine ()
                .allowTerms ( Error.class )
                .buildType ();
        }

        @Override
        public final Type<Type<?>> inputType ()
        {
            return this.inputType;
        }

        @Override
        public final Type<EXTRACTION> outputType ()
        {
            return this.outputType;
        }

        @Override
        public final Type<EXTRACTION> errorType ()
        {
            return this.errorType;
        }

        @Override
        public final Term<EXTRACTION> apply (
                Term<Type<?>> types
                )
        {
            final TermBuilder<EXTRACTION> extractions =
                new TermBuilder<EXTRACTION> ( this.outputType );
            for ( Type<?> type : types )
            {
                this.extract ( type, extractions );
            }

            return extractions.build ();
        }

        @SuppressWarnings("unchecked") //Cast Term<Type<?>>-Term<Type<?>>.Sigh.
        public final void extract (
                Type<?> type,
                TermBuilder<EXTRACTION> extractions
                )
            throws ParametersMustNotBeNull.Violation,
                   IteratorMustBeFinite.Violation
        {
            final Term<Type<?>> type_constructor = (Term<Type<?>>)
                type.typeConstructor ();
            if ( ! ( type_constructor instanceof Expression ) )
            {
                return;
            }

            final Expression<Type<?>> type_constructor_expression =
                (Expression<Type<?>>) type_constructor;
            TermPipeline.TermSink<?> type_constructor_pipeline =
                type_constructor_expression.pipeline ();
            int infinite_loop_protector = 0;
            while ( infinite_loop_protector < 1024 )
            {
                infinite_loop_protector ++;

                final EXTRACTION extraction =
                    this.extract ( type_constructor_pipeline );
                if ( extraction == null )
                {
                    return;
                }

                extractions.add ( extraction );

                if ( ! ( type_constructor_pipeline instanceof Transform ) )
                {
                    return;
                }

                final Transform<?, ?> type_constructor_transform =
                    (Transform<?, ?>) type_constructor_pipeline;
                type_constructor_pipeline =
                    type_constructor_transform.inputOrigin ();
            }

            throw new IteratorMustBeFinite ( 1024L )
                .violation ( this,                      // plaintiff
                             extractions.build ()
                                        .iterator () ); // evidence
        }

        protected abstract EXTRACTION extract (
                TermPipeline.TermSink<?> type_constructor_pipeline
                )
            throws ParametersMustNotBeNull.Violation; // Can be null.
    }


    public static class ExtractTypeConstructorTransforms
        extends AbstractExtractFromTypeConstructor<Transform<?, ?>>
    {
        private static final long serialVersionUID = 1L;

        public ExtractTypeConstructorTransforms (
                Type<Type<?>> type_type,
                Type<Transform<?, ?>> transform_type
                )
            throws ParametersMustNotBeNull.Violation
        {
            super ( type_type, transform_type );
        }

        @Override
        protected final Transform<?, ?> extract (
                TermPipeline.TermSink<?> type_constructor_pipeline
                )
            throws ParametersMustNotBeNull.Violation // Can be null.
        {
            if ( ! ( type_constructor_pipeline instanceof Transform ) )
            {
                return null;
            }

            final Transform<?, ?> type_constructor_transform =
                (Transform<?, ?>) type_constructor_pipeline;

            if ( this.isExtracted ( type_constructor_transform ) )
            {
                return type_constructor_transform;
            }
            else
            {
                return null;
            }
        }

        // Can be overridden.
        public boolean isExtracted (
                Transform<?, ?> transform
                )
        {
            return true;
        }
    }


    public static class ExtractTypeConstructorOperations
        extends AbstractExtractFromTypeConstructor<Operation<?, ?>>
    {
        private static final long serialVersionUID = 1L;

        public ExtractTypeConstructorOperations (
                Type<Type<?>> type_type,
                Type<Operation<?, ?>> operation_type
                )
            throws ParametersMustNotBeNull.Violation
        {
            super ( type_type, operation_type );
        }

        @Override
        protected final Operation<?, ?> extract (
                TermPipeline.TermSink<?> type_constructor_pipeline
                )
            throws ParametersMustNotBeNull.Violation // Can be null.
        {
            if ( ! ( type_constructor_pipeline instanceof Transform ) )
            {
                return null;
            }

            final Transform<?, ?> type_constructor_transform =
                (Transform<?, ?>) type_constructor_pipeline;
            final Operation<?, ?> type_constructor_operation =
                type_constructor_transform.operation ();

            if ( this.isExtracted ( type_constructor_operation ) )
            {
                return type_constructor_operation;
            }
            else
            {
                return null;
            }
        }

        // Can be overridden.
        public boolean isExtracted (
                Operation<?, ?> operation
                )
        {
            return true;
        }
    }


    public static class ExtractTypeKinds
        extends ExtractTypeConstructorOperations
    {
        private static final long serialVersionUID = 1L;

        public ExtractTypeKinds (
                Type<Type<?>> type_type,
                Type<Operation<?, ?>> operation_type
                )
            throws ParametersMustNotBeNull.Violation
        {
            super ( type_type, operation_type );
        }

        public final boolean isExtracted (
                Operation<?, ?> operation
                )
        {
            if ( ( operation instanceof Kind1 )
                 || ( operation instanceof Kind2 ) ) // !!! or Kind3 or ...
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }


    public static interface Enclose1<ELEMENT extends Object, CONTAINER extends Object>
        extends Kind1<Type<ELEMENT>, CONTAINER>
    {
        @Override
        public abstract Term<Type<CONTAINER>> apply (
                Term<Type<ELEMENT>> types
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;

        @Override
        public abstract Type<CONTAINER> construct (
                Type<ELEMENT> enclosed_type
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation;
    }

    public static class Expose1<ELEMENT extends Object, CONTAINER extends Object>
        implements Kind1<Type<CONTAINER>, ELEMENT>
    {
        private static final long serialVersionUID = 1L;
        private final Enclose1<ELEMENT, CONTAINER> enclose;

        public Expose1 ( Enclose1<ELEMENT, CONTAINER> enclose )
        {
            this.enclose = enclose;
        }


        @Override
        public final Type<Type<CONTAINER>> inputType ()
        {
            return this.enclose.outputType ();
        }

        @Override
        public final Type<Type<ELEMENT>> outputType ()
        {
            return this.enclose.inputType ();
        }

        @Override
        public final Type<Type<ELEMENT>> errorType ()
        {
            return this.enclose.inputType ()
                .refine ()
                .allowTerms ( Error.class )
                .buildType ();
        }

        @Override
        public final Term<Type<ELEMENT>> apply (
                Term<Type<CONTAINER>> types
                )
        {
            final TermBuilder<Type<ELEMENT>> builder =
                new TermBuilder<Type<ELEMENT>> ( this.outputType () );
            for ( Type<CONTAINER> container_type : types )
            {
                final Type<ELEMENT> enclosed_type =
                    this.construct ( container_type );
                builder.add ( enclosed_type );
            }

            return builder.build ();
        }

        @Override
        @SuppressWarnings("unchecked") // Various checked casts unrecognized
            // by the compiler.
        public final Type<ELEMENT> construct (
                Type<CONTAINER> container_type
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            final Term<Type<CONTAINER>> type_constructor =
                container_type.typeConstructor ();
            if ( ! ( type_constructor instanceof Expression ) )
            {
                throw ReturnNeverNull.CONTRACT.violation (
                    this,                // plaintiff
                    type_constructor );  // !!! USE CAUSE, PASS NULL
            }

            final Expression<Type<CONTAINER>> type_constructor_expression =
                (Expression<Type<CONTAINER>>) type_constructor;
            TermPipeline.TermSink<?> type_constructor_pipeline =
                type_constructor_expression.pipeline ();
            TermPipeline.TermSink<Type<?>> enclosed_type_pipeline = null;
            int infinite_loop_protector = 0;
            while ( infinite_loop_protector < 1024 )
            {
                infinite_loop_protector ++;

                if ( ! ( type_constructor_pipeline instanceof Transform ) )
                {
                    break;
                }

                final Transform<?, ?> type_constructor_transform =
                    (Transform<?, ?>) type_constructor_pipeline;

                final Operation<?, ?> maybe_kind =
                    type_constructor_transform.operation ();
                if ( maybe_kind instanceof Enclose1 )
                {
                    enclosed_type_pipeline = (TermPipeline.TermSink<Type<?>>)
                        type_constructor_transform.inputOrigin ();
                }

                type_constructor_pipeline =
                    type_constructor_transform.inputOrigin ();
            }

            if ( enclosed_type_pipeline != null )
            {
                final Type<?> enclosed_type =
                    enclosed_type_pipeline.output ()
                                          .orNull ();
                if ( ! this.outputType ().filter ( enclosed_type )
                                         .isKept () )
                {
                    throw ReturnNeverNull.CONTRACT.violation (
                              this, // plaintiff
                              enclosed_type ); // !!! USE CAUSE, PASS NULL
                }

                return (Type<ELEMENT>) enclosed_type;
            }

            /* !!!
            if ( infinite_loop_protector >= 1024 )
            {
                throw new IteratorMustBeFinite ( 1024L )
                    .violation ( this,                      // plaintiff
                                 !!!.iterator () );         // evidence
            }
            !!! */

            throw ReturnNeverNull.CONTRACT.violation (
                      this,             // plaintiff
                      container_type ); // !!! USE CAUSE, PASS NULL
        }
    }


    public abstract static class AbstractEnclose1<ELEMENT extends Object, CONTAINER extends Object>
        implements Enclose1<ELEMENT, CONTAINER>
    {
        private static final long serialVersionUID = 1L;

        private final Type<Type<ELEMENT>> inputType;
        private final Type<Type<CONTAINER>> outputType;
        private final Type<Type<CONTAINER>> errorType;

        private final Type<Object> rootType;

        @SuppressWarnings("unchecked") // Cast Type<Type<?>>-Type<Type<XYZ>>.
        public AbstractEnclose1 (
                Type<Object> root_type,
                Type<Type<?>> type_type )
        {
            this.inputType = (Type<Type<ELEMENT>>)
                ( (Type<?>) type_type );
            this.outputType = (Type<Type<CONTAINER>>)
                ( (Type<?>) type_type );
            this.errorType = this.outputType
                .refine ()
                .allowTerms ( Error.class )
                .buildType ();

            this.rootType = root_type;
        }

        @Override
        public final Type<Type<ELEMENT>> inputType ()
        {
            return this.inputType;
        }

        @Override
        public final Type<Type<CONTAINER>> outputType ()
        {
            return this.outputType;
        }

        @Override
        public final Type<Type<CONTAINER>> errorType ()
        {
            return this.errorType;
        }

        // Every Kind1 must implement construct

        @Override
        public final Term<Type<CONTAINER>> apply (
                Term<Type<ELEMENT>> types
                )
        {
            final TermBuilder<Type<CONTAINER>> out =
                new TermBuilder<Type<CONTAINER>> ( this.outputType );
            for ( Type<ELEMENT> type : types )
            {
                final Type<CONTAINER> output_type =
                    this.construct ( type );
                out.add ( output_type );
            }

            final Term<Type<CONTAINER>> output_types =
                out.build ();
            return output_types;
        }

        protected final Type<Object> rootType ()
        {
            return this.rootType;
        }

        protected final Expression<Type<CONTAINER>> typeConstructor (
                Type<ELEMENT> enclosed_type
                )
            throws ParametersMustNotBeNull.Violation,
                   ReturnNeverNull.Violation
        {
            final One<Type<ELEMENT>> enclose_parameter =
                new One<Type<ELEMENT>> ( this.inputType (),
                                         enclosed_type );
            final Transform<Type<ELEMENT>, Type<CONTAINER>> enclose_transform =
                new TermTransform<Type<ELEMENT>, Type<CONTAINER>> (
                    enclose_parameter, // input
                    this );            // operation

            final Expression<Type<CONTAINER>> enclose_constructor =
                new Expression<Type<CONTAINER>> (
                        enclose_transform, // pipeline
                        Clock.STANDARD );

            return enclose_constructor;
        }
    }


    public static class Enclosed<ELEMENT, CONTAINER>
        extends AbstractEnclose1<ELEMENT, CONTAINER>
    {
        private static final long serialVersionUID = 1L;

        private final Class<CONTAINER> containerClass;
        private final CONTAINER none;

        private final Expose1<ELEMENT, CONTAINER> expose;

        @SuppressWarnings("unchecked") // Cast Class<?>-Class<CONTAINER>,
        public Enclosed (
                Type<Object> root_type,
                Type<Type<?>> type_type,
                Class<?> container_class,
                CONTAINER none
                )
        {
            super ( root_type,
                    type_type );

            if ( ! container_class.isInstance ( none ) )
            {
                throw new IllegalArgumentException ( "!!!" );
            }

            this.containerClass = (Class<CONTAINER>) container_class;
            this.none = none;

            this.expose = new Expose1<ELEMENT, CONTAINER> ( this );
        }

        @Override
        @SuppressWarnings("unchecked") // Generic array creation varargs.
        public final Type<CONTAINER> construct (
                Type<ELEMENT> type
                )
        {
            final Expression<Type<CONTAINER>> enclose_constructor =
                this.typeConstructor ( type );

            final Type<CONTAINER> output_type =
                this.rootType ().refine ()
                                .elementClass ( this.containerClass )
                                .none ( this.none )
                                .typeConstructor ( enclose_constructor )
                                .staticSymbols ()
                                    .edit ()
                                        .append ( this.expose )
                                    .end ()
                                .end ()
                                .buildType ();
            return output_type;
        }
    }


    public static class ListEnclose<ELEMENT extends Object>
        extends AbstractEnclose1<ELEMENT, List<ELEMENT>>
    {
        private static final long serialVersionUID = 1L;

        public ListEnclose (
                Type<Object> root_type,
                Type<Type<?>> type_type )
        {
            super ( root_type,
                    type_type );
        }

        @Override
        @SuppressWarnings("unchecked") // Cast Class<?>-Class<ELEMENT>,
        public final Type<List<ELEMENT>> construct (
                Type<ELEMENT> type
                )
        {
            final Expose1<ELEMENT, List<ELEMENT>> symbol_expose_element_type =
                new Expose1<ELEMENT, List<ELEMENT>> ( this );

            final List<ELEMENT> none = new ArrayList<ELEMENT> ();

            final Expression<Type<List<ELEMENT>>> enclose_constructor =
                this.typeConstructor ( type );

            final Type<List<ELEMENT>> output_type =
                this.rootType ().refine ()
                                .elementClass ( (Class<List<ELEMENT>>)
                                                ( (Class<?>) List.class ) )
                                .none ( none )
                                .typeConstructor ( enclose_constructor )
                                .staticSymbols ()
                                    .edit ()
                                        .append ( symbol_expose_element_type )
                                    .end ()
                                .end ()
                                .buildType ();

            return output_type;
        }
    }


    public static class TermEnclose<ELEMENT extends Object>
        extends AbstractEnclose1<ELEMENT, Term<ELEMENT>>
    {
        private static final long serialVersionUID = 1L;

        public TermEnclose (
                Type<Object> root_type,
                Type<Type<?>> type_type )
        {
            super ( root_type,
                    type_type );
        }

        @Override
        @SuppressWarnings("unchecked") // Cast Class<?>-Class<ELEMENT>,
        public final Type<Term<ELEMENT>> construct (
                Type<ELEMENT> type
                )
        {
            final Expose1<ELEMENT, Term<ELEMENT>> symbol_expose_element_type =
                new Expose1<ELEMENT, Term<ELEMENT>> ( this );

            final Term<ELEMENT> none = new No<ELEMENT> ( type );

            final Type<Term<ELEMENT>> output_type =
                this.rootType ().refine ()
                                .elementClass ( (Class<Term<ELEMENT>>)
                                                ( (Class<?>) Term.class ) )
                                .none ( none )
                                .staticSymbols ()
                                    .edit ()
                                        .append ( symbol_expose_element_type )
                                    .end ()
                                .end ()
                                .buildType ();

            return output_type;
        }
    }




    public static class ArrayEnclose<ELEMENT extends Object>
        extends AbstractEnclose1<ELEMENT, ELEMENT []>
    {
        private static final long serialVersionUID = 1L;

        public ArrayEnclose (
                Type<Object> root_type,
                Type<Type<?>> type_type )
        {
            super ( root_type,
                    type_type );
        }

        @Override
        @SuppressWarnings("unchecked") // Cast Class<?>-Class<ELEMENT>
        public final Type<ELEMENT []> construct (
                Type<ELEMENT> type
                )
        {
            final Expose1<ELEMENT, ELEMENT []> symbol_expose_element_type =
                new Expose1<ELEMENT, ELEMENT []> ( this );

            final ELEMENT [] none = type.array ( 0 );

            final Type<ELEMENT []> output_type =
                this.rootType ().refine ()
                                .elementClass ( (Class<ELEMENT []>)
                                                ( (Class<?>) none.getClass () ) )
                                .none ( none )
                                .staticSymbols ()
                                    .edit ()
                                        .append ( symbol_expose_element_type )
                                    .end ()
                                .end ()
                                .buildType ();

            return output_type;
        }
    }


    public static class Test
    {
        @SuppressWarnings({"rawtypes", "unchecked"}) // new List [ 0 ],
            // cast new List [ 0 ] -> new List<String> [].
        public void test1 (
                Type<Object> root_type,
                Type<Type<?>> type_type,
                Type<String> string_type,
                Type<List<String>> another_list_of_strings_type,
                Type<Number> number_type,
                Type<Double> double_type
                )
        {
            final Type<List<String>> list_of_strings_type =
                new Enclosed<String, List<String>> ( root_type, type_type,
                                                     List.class,
                                                     (List<String>)
                                                     new ArrayList<String> ()
                                                     )
                .construct ( string_type );
            final Type<Term<String>> term_of_strings_type =
                new Enclosed<String, Term<String>> ( root_type, type_type,
                                                     Term.class,
                                                     (Term<String>)
                                                     new No<String> ( string_type ) )
                .construct ( string_type );
            final Type<String []> array_of_strings_type =
                new Enclosed<String, String []> ( root_type, type_type,
                                                  String [].class,
                                                  new String [ 0 ] )
                .construct ( string_type );

            final Type<List<List<String>>> list_of_lists_of_strings_type =
                new Enclosed<List<String>, List<List<String>>> ( root_type, type_type,
                                                                 List.class,
                                                                 (List<List<String>>)
                                                                 new ArrayList<List<String>> () )
                .construct ( another_list_of_strings_type );
            final Type<Term<List<String>>> term_of_lists_of_strings_type =
                new Enclosed<List<String>, Term<List<String>>> ( root_type, type_type,
                                                                 Term.class,
                                                                 (Term<List<String>>)
                                                                 new No<List<String>> ( another_list_of_strings_type ) )
                .construct ( another_list_of_strings_type );
            final Type<List<String> []> array_of_lists_of_strings_type =
                new Enclosed<List<String>, List<String> []> ( root_type, type_type,
                                                              List [].class,
                                                              (List<String> [])
                                                              new List [ 0 ] )
                .construct ( another_list_of_strings_type );

            final Type<List<Number>> list_of_numbers_type =
                new Enclosed<Number, List<Number>> ( root_type, type_type,
                                                     List.class,
                                                     (List<Number>)
                                                     new ArrayList<Number> () )
                .construct ( number_type );
            final Type<Term<Number>> term_of_numbers_type =
                new Enclosed<Number, Term<Number>> ( root_type, type_type,
                                                     Term.class,
                                                     (Term<Number>)
                                                     new No<Number> ( number_type ) )
                .construct ( number_type );
            final Type<Number []> array_of_numbers_type =
                new Enclosed<Number, Number []> ( root_type, type_type,
                                                  Number [].class,
                                                  new Number [ 0 ] )
                .construct ( number_type );

            final Type<List<Double>> list_of_doubles_type =
                new Enclosed<Double, List<Double>> ( root_type, type_type,
                                                     List.class,
                                                     (List<Double>)
                                                     new ArrayList<Double> () )
                .construct ( double_type );
            final Type<Term<Double>> term_of_doubles_type =
                new Enclosed<Double, Term<Double>> ( root_type, type_type,
                                                     Term.class,
                                                     (Term<Double>)
                                                     new No<Double> ( double_type ) )
                .construct ( double_type );
            final Type<Double []> array_of_doubles_type =
                new Enclosed<Double, Double []> ( root_type, type_type,
                                                  Double [].class,
                                                  new Double [ 0 ] )
                .construct ( double_type );

            final One<Double []> vector =
                new One<Double []> ( array_of_doubles_type,
                                     new Double [] { 1.0D, 2.0D, 3.0D, 0D } );
        }


        public void test2 (
                Type<Object> root_type,
                Type<Type<?>> type_type,
                Type<String> string_type,
                Type<List<String>> another_list_of_strings_type,
                Type<Number> number_type,
                Type<Double> double_type
                )
        {
            final Type<List<String>> list_of_strings_type =
                new ListEnclose<String> ( root_type, type_type )
                .construct ( string_type );
            final Type<Term<String>> term_of_strings_type =
                new TermEnclose<String> ( root_type, type_type )
                .construct ( string_type );
            final Type<String []> array_of_strings_type =
                new ArrayEnclose<String> ( root_type, type_type )
                .construct ( string_type );

            final Type<List<List<String>>> list_of_lists_of_strings_type =
                new ListEnclose<List<String>> ( root_type, type_type )
                .construct ( another_list_of_strings_type );
            final Type<Term<List<String>>> term_of_lists_of_strings_type =
                new TermEnclose<List<String>> ( root_type, type_type )
                .construct ( another_list_of_strings_type );
            final Type<List<String> []> array_of_lists_of_strings_type =
                new ArrayEnclose<List<String>> ( root_type, type_type )
                .construct ( another_list_of_strings_type );

            final Type<List<Number>> list_of_numbers_type =
                new ListEnclose<Number> ( root_type, type_type )
                .construct ( number_type );
            final Type<Term<Number>> term_of_numbers_type =
                new TermEnclose<Number> ( root_type, type_type )
                .construct ( number_type );
            final Type<Number []> array_of_numbers_type =
                new ArrayEnclose<Number> ( root_type, type_type )
                .construct ( number_type );

            final Type<List<Double>> list_of_doubles_type =
                new ListEnclose<Double> ( root_type, type_type )
                .construct ( double_type );
            final Type<Term<Double>> term_of_doubles_type =
                new TermEnclose<Double> ( root_type, type_type )
                .construct ( double_type );
            final Type<Double []> array_of_doubles_type =
                new ArrayEnclose<Double> ( root_type, type_type )
                .construct ( double_type );

            final One<Double []> vector =
                new One<Double []> ( array_of_doubles_type,
                                     new Double [] { 1.0D, 2.0D, 3.0D, 0D } );
        }
    }


    /* !!!
    public static class StreamEnclose<ELEMENT extends Object>
        extends AbstractEnclose1<ELEMENT, Stream<ELEMENT>>
    {
        private static final long serialVersionUID = 1L;

        public StreamEnclose (
                Type<Object> root_type,
                Type<Type<?>> type_type )
        {
            super ( root_type,
                    type_type );
        }

        @Override
        @SuppressWarnings("unchecked") // Cast Class<?>-Class<ELEMENT>,
        public final Type<Stream<ELEMENT>> enclose (
                Type<ELEMENT> type
                )
        {
            final Expose1<ELEMENT, Stream<ELEMENT>> symbol_expose_element_type =
                new Expose1<ELEMENT, Stream<ELEMENT>> ( this );

            final Stream<ELEMENT> none = new !!!<ELEMENT> ();

            final Type<Stream<ELEMENT>> output_type =
                this.rootType ().refine ()
                                .elementClass ( (Class<Stream<ELEMENT>>)
                                                ( (Class<?>) Stream.class ) )
                                .none ( none )
                                .staticSymbols ()
                                    .edit ()
                                        .append ( symbol_expose_element_type )
                                    .end ()
                                .end ()
                                .buildType ();

            return output_type;
        }
    }
    !!! */



    /* !!!
    public static interface A<X>
    {
        public abstract Class<X> aClass ();
    }

    public static interface B<X>
        extends A<X>
    {
    }

    public static interface C<X>
        extends A<X>
    {
    }

    
    public static interface Kind2<P1, P2>
    {
        public B<?> construct (
                               A<P1> parameter1,
                               A<P2> parameter2
                               );
    }

    public static interface DependentKind2<T1, P2>
        extends Kind2<T1, P2>
    {
        public B<A<T1>> construct (
                                   A<T1> p1,
                                   A<P2> p2
                                   );
    }

    public static interface ADependentKind2<P1>
    {
        public <ELEM> Term<Type<List<ELEM>>> construct ( Term<Type<ELEM>> types, Term<Long> lengths );
    }

    !!! */

    // !!! public static interface ListKind
    // !!! extends DependentKind2<

    /* !!!
    public static interface Kind1<P1>
    {
        public abstract <ELEMENT extends Object>
            Type<ELEMENT> construct (
                Term<? extends P1> parameter1
                );
    }

    public static interface Kind2<P1, P2>
    {
        public abstract <ELEMENT extends Object>
            Type<ELEMENT> construct (
                Term<? extends P1> parameter1,
                Term<? extends P2> parameter2
                );
    }

    public interface LengthKind
        extends Kind2<Type<?>, Long>
    {
        public abstract <ELEMENT extends Object>
            Type<ELEMENT> construct (
                Term<Type<?>> base_type,
                Term<Long> length
                );
    }
    !!! */



    /** !!!
    public static interface Kind1<GENERIC, P1>
        extends Operation<P1, Type<? extends GENERIC>>
    {
        @Override
        public abstract
            Term<Type<? extends GENERIC>> apply (
                Term<P1> type_parameter
                );
    }
    !!! */


    /* !!!
    public static interface SuperKind1<P1>
    {
        public abstract <GENERIC>
            Kind1<GENERIC, P1> kind ();
    }

    public static interface SuperListKind
        extends SuperKind1<Type<?>>
    {
        @Override
            public abstract <SPECIFIC> Kind1<List<SPECIFIC>, Type<SPECIFIC>> kind ();
    }
    !!! */


    /* !!!
    // Works:
    public static interface ListKind
        extends Kind1<List<?>, Type<?>>
    {
        @Override
        public abstract Term<Type<? extends List<?>>> apply (
            Term<Type<?>> element_type
            );
    }
    !!! */

    /* !!!
    // Does not work:
    public static interface ListKind
        extends Kind1<List<? extends Object>, Type<? extends Object>>
    {
        @Override
        public abstract Term<Type<? extends List<? extends String>>> apply (
            Term<Type<?>> element_type
            );
    }
    !!! */

    /* !!!
    // Does not work:
    public static interface ListKind
        extends Kind1<List<?>, Type<?>>
    {
        @Override
        public abstract Term<Type<? extends ArrayList<?>>> apply (
            Term<Type<?>> element_type
            );
    }
    !!! */



    /* !!!
    public static interface Kind0<GENERIC>
        extends Operation<Type<GENERIC>, Type<? extends GENERIC>>
    {
        @Override
        public abstract
            Term<Type<? extends GENERIC>> apply (
                Term<Type<GENERIC>> input
                );
    }

    public static interface Kind1<GENERIC, P1>
        extends Operation<Type<GENERIC>, Operation<P1, Type<? extends GENERIC>>>
    {
        @Override
        public abstract
            Maybe<Operation<P1, Type<? extends GENERIC>>> apply (
                Term<Type<GENERIC>> input
                );
    }
    !!! */

    /* !!!
    // Does not work:
    public static interface ListKind
        extends Kind1<List<?>, Type<?>>
    {
        @Override
        public abstract <SPECIFIC extends Object>
            Maybe<Operation<Type<SPECIFIC>, Type<? extends List<SPECIFIC>>>> apply (
                Term<Type<List<?>>> generic_list_type
                );
    }
    !!! */

    /* !!!
    // Works:
    public static interface ListKind
        extends Kind1<List<?>, Type<?>>
    {
        @Override
        public abstract
            Maybe<Operation<Type<?>, Type<? extends List<?>>>> apply (
                Term<Type<List<?>>> generic_list_type
                );
    }
    !!! */

    /* !!!
    // Does not work:
    public static interface ListKind
        extends Operation<Type<?>, Type<?>>
    {
        @Override
        public abstract <SPECIFIC extends Object>
            Maybe<Type<List<SPECIFIC>>> apply (
                Term<Type<SPECIFIC>> generic_list_type
                );
    }
    !!! */

    /* !!!
    // Does not work:
    public static interface ListKind
        extends Operation<Object, Object>
    {
        @Override
        public abstract <SPECIFIC extends Object>
            Maybe<Type<List<SPECIFIC>>> apply (
                Term<Type<SPECIFIC>> generic_list_type
                );
    }
    !!! */
}
