public interface Account
{
    public abstract Money withdraw ( Money amount )
        throws ParametersMustNotBeNull,
               AmountMustNotExceedBalance,
               ReturnNeverNull;
    
}

public class Arbiter
{
    public static boolean isContractInspectionEnabled ()
    {
        return true;
    }

    public static
        <CONTRACT extends Contract>
                          void handleBreach ( CONTRACT contract )
        throws CONTRACT
    {
        throw contract;
    }
}

public class AmountMustNotExceedBalance
    extends ObligationException
{
    private AmountMustNotExceedBalance ( CONTRACT contract )
    {
        super ( contract );
    }

    public Account account ()
    {
        return ( (CONTRACT) this.contract () ).account;
    }

    public Money amount ()
    {
        return ( (CONTRACT) this.contract () ).amount;
    }

    public static final void check ( Account account, Money amount )
    {
        if ( Arbiter.isContractInspectionEnabled () )
        {
            Arbiter.inspect ( new CONTRACT ( account, amount ) );
        }
    }

    private static class CONTRACT
        implements ParametersObligation
    {
        private final account account;
        private final Money money;

        public final CONTRACT ( Account account, Money amount )
        {
            this.account = account;
            this.amount = amount;
        }

        @Override
        public final Object [] actuals ()
        {
            return new Object [] { this.account, this.amount };
        }

        @Override
        public final void enforce ()
            throws AmountMustNotExceedBalance
        {
            Money balance = this.account.balance ();
            if ( balance != null )
            {
                if ( this.amount.compareTo ( balance ) <= 0 )
                {
                    // Contract OK.
                    return;
                }
            }

            // Some kind of breach.  The arbiter might throw the exception
            // up the chain, or she might catch it and do something else
            // (log it, exit, etc).
            throw new AmountMustNotExceedBalance ( this ) );
        }
    }
}

public abstract class ContractException
    extends RuntimeException
{
    private final Contract contract;

    public ContractException ( Contract contract )
    {
        super ( "Contract violation: " + contract );

        this.contract = contract;
    }
}

public class ObligationException
    extends ContractException
{
    public ObligationException (
                                Obligation obligation
                                )
    {
        super ( obligation );
    }
}

public interface Contract
{
    public abstract Object [] actuals ();

    public abstract void enforce ()
        throws ContractException;
}

public interface Obligation
{
    @Override
    public abstract void enforce ()
        throws ObligationException;
}

public abstract class ParametersObligation
    implements Obligation
{
    public String toString ()
    {
        return this.getClass ().getSimpleName ()
            + " "
            + argumentsToString ( this.actuals () );
    }

    private static String argumentsToString ( Object ... arguments )
    {
        StringBuilder sbuf = new StringBuilder ();
        for ( int a = 0; a < arguments.length; a ++ )
        {
            if ( a > 0 )
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + arguments [ a ] );
        }

        return sbuf.toString ();
    }
}

public class ParametersMustNotBeNull
    implements ObligationException
{
    private ParametersMustNotBeNull ( CONTRACT contract )
    {
        super ( contract );
    }

    public static final void check ( Object object, Object ... parameters )
    {
        if ( Arbiter.isContractInspectionEnabled () )
        {
            Arbiter.inspect ( new CONTRACT ( object, parameters ) );
        }
    }

    private static class CONTRACT
    {
        private final Object object;
        private final Object [] parameters;

        public final CONTRACT ( Object object, Object ... parameters )
        {
            this.object = object;
            this.parameters = new Object [ parameters.length ];
            System.arraycopy ( parameters, 0, this.parameters, 0, parameters.length );
        }

        @Override
        public Object [] actuals ()
        {
            Object [] actuals = new Object [ this.parameters.length + 1 ];
            actuals [ 0 ] = this.object;
            System.arraycopy ( this.parameters, 0,
                               actuals, 1, this.parameters.length );
            return actuals;
        }

        @Override
        public final void enforce ()
            throws ParametersMustNotBeNull
        {
            for ( Object parameter : this.parameters )
            {
                if ( parameter == null )
                {
                    throw new ContractException<ParametersMustNotBeNull ( this );
                }
            }
        }
    }
}

public class GuaranteeException
    extends ContractException
{
    public GuaranteeException (
                               Guarantee guarantee
                               )
    {
        super ( guarantee );
    }
}

public interface Guarantee
{
    @Override
    public abstract void enforce ()
        throws GuaranteeException;
}

public class ReturnNeverNull
    implements Guarantee
{
    private ReturnNeverNull ( CONTRACT contract )
    {
        super ( contract );
    }

    private static class CONTRACT
        extends Guarantee
    {
        private final Object object;
        private final Object result;

        public CONTRACT ( Object object, Object result )
        {
            this.object = object;
            this.result = result;
        }

        public final void enforce ()
        throws ContractException<ReturnNeverNull>
        {
            if ( this.result == null )
            {
                throw new ReturnNeverNull ( this );
            }
        }

        public String toString ()
        {
            return "" + this.object + " "
                + this.getClass ().getSimpleName ()
                + " " + this.result;
        }
    }
}

========================================


public interface Contract
{
    public Assertion preConditions ();
    public Assertion invariants ();
    public assertion postConditions ();

    public String id ();
}

// Class:
//     - pre-conditions should be checked statically at class load time.
//     - invariants maybe should be checked against all methods (static
//       and instance).
//     - post-conditions can't really be checked.
//
// Constructor, method:
//     - pre-conditions should be checked at start of method.  If
//       interface then just call
//       MyInterface.myMethodContract.check ( params );
//     - invariants: not easy.
//     - post-conditions: end of method.
//       MyInterface.myMethodContract.check ( return_value );


// This does not work because Java does not allow us to throw
// parameterized exceptions:
public interface Account
{
    public abstract Money withdraw ( Money amount )
        throws ContractException<ParametersMustNotBeNull>,
               ContractException<AmountMustNotExceedBalance>,
               ContractException<ReturnNeverNull>;
    
}

public class AmountMustNotExceedBalance
    implements Contract
{
    public static final AmountMustNotExceedBalance CONTRACT =
        new AmountMustNotExceedBalance ();

    private AmnountMustNotExceedBalance ()
    {
    }

    public final void check ( Object ... arguments )
        throws ContractException<AmountMustNotExceedBalance>;
    {
        // 1st argument is instance, remaining are parameters.
        if ( arguments.length == 2
             && ( arguments [ 0 ] instanceof Account )
             && ( arguments [ 1 ] instanceof Money ) )
        {
            Account account = (Account) arguments [ 0 ];
            Money amount = (Money) arguments [ 1 ];
            Money balance = account.balance ();
            if ( balance != null )
            {
                if ( amount.compareTo ( balance ) <= 0 )
                {
                    // Contract OK.
                    return;
                }
            }
        }

        // Some kind of breach.
        throw new ContractException<AmountMustNotExceedBalance> ( this, arguments );
    }
}

public class ContractException<CONTRACT extends Contract>
    extends RuntimeException
{
    private final CONTRACT contract;
    private final Object [] arguments;

    public ContractException ( CONTRACT contract, Object ... arguments )
    {
        super ( "Contract violation: " + argumentsToString ( arguments )
                + " " + contract );
    }

    private static String argumentsToString ( Object ... arguments )
    {
        StringBuilder sbuf = new StringBuilder ();
        for ( int a = 0; a < arguments.length; a ++ )
        {
            if ( a > 0 )
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + arguments [ a ] );
        }

        return sbuf.toString ();
    }

    public CONTRACT contract ()
    {
        return this.contract;
    }

    public Object [] arguments ()
    {
        return this.arguments;
    }
}

public interface Contract
{
    // Every Contract must have a public static final instance
    // called CONTRACT, and a private constructor.

    public abstract void check ( Object ... arguments )
        throws ContractException<? extends Contract>; // implementations override to throw <ME>
}

public class ParametersMustNotBeNull
    implements Contract
{
    public static final ParametersMustNotBeNull CONTRACT =
        new ParametersMustNotBeNull ();

    private ParametersMustNotBeNull ()
    {
        // Singleton constructor.
    }

    public final void check ( Object ... arguments )
        throws ContractException<ParametersMustNotBeNull>
    {
        // The first argument is the object, the remaining are parameters
        // to some method.
        for ( int p = 1; p < arguments.length; p ++ )
        {
            Object parameter = arguments [ p ];
            if ( parameter == null )
            {
                throw new ContractException<ParametersMustNotBeNull ( this, arguments );
            }
        }
    }
}

public class ReturnNeverNull
    implements Contract
{
    public static final ReturnNeverNull CONTRACT =
        new ReturnNeverNull ();

    private ReturnNeverNull ()
    {
        // Singleton constructor.
    }

    public final void check ( Object ... arguments )
        throws ContractException<ReturnNeverNull>
    {
        // The first argument is the object, the remaining are parameters
        // to some method.
        for ( int p = 1; p < arguments.length; p ++ )
        {
            Object parameter = arguments [ p ];
            if ( parameter == null )
            {
                throw new ContractException<ReturnNeverNull ( this, arguments );
            }
        }
    }
}

========================================


public interface ContractExceptionHandler
{
    // Throws the exception after doing side-effects
    // OR does NOT throw the exception, and allows
    // the whole system to blow up...
    public abstract <EXCEPTION extends ContractException>
        void handleException (
                              EXCEPTION contract_exception
                              )
        throws EXCEPTION;
}

public class ContractException
    extends RuntimeException
{
    private final String [] i18nMessageComponents;
    private final String [] orderedParameters;
}

public abstract class Contract<EXCEPTION extends ContractException>
{
    public static final Contract<ObligationException> IN =
        new Obligations ();

    public static final Contract<GuaranteeException> OUT =
        new Guarantees ();

    public static final Contract<DependencyException> DEPENDENCIES =
        new Dependencies ();

    // Tells unit test harness that this obligation is in one logical branch.
    public static final Contract<ObligationException> BRANCH_IN =
        new Obligations ();

    // Tells unit test harness that this guarantee is in one logical branch.
    public static final Contract<GuaranteeException> BRANCH_OUT =
        new Guarantees ();


    private static ContractErrorLocalizer localizer =
        new DefaultContractErrorLocalizer ();

    public static void setErrorLocalizer ( ContractErrorLocalizer ) { ... }
    public static ContractErrorLocalizer errorLocalizer () { ... }


    public static void addAllExceptionHandler ( ContractExceptionHandler ) { ... }
    public static void removeAllExceptionHandler ( ContractExceptionHandler ) { ... }

    public static void addAllPreFilter ( Contract<EXCEPTION> ) { ... }
    public static void removeAllPreFilter ( Contract<EXCEPTION> ) { ... }

    public static void addAllPostPreFilter ( Contract<EXCEPTION> ) { ... }
    public static void removeAllPostFilter ( Contract<EXCEPTION> ) { ... }


    public static enum Op
    {
        EQUAL_TO,
        NOT_EQUAL_TO,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL_TO,
        LESS_THAN,
        LESS_THAN_OR_EQUAL_TO
    };


    protected abstract EXCEPTION createException ( String message );


    private final List<ContractExceptionHandler> exceptionHandlers =
        new ArrayList<ContractExceptionHandler> ();

    public void addExceptionHandler ( ContractExceptionHandler ) { ... }
    public void removeExceptionHandler ( ContractExceptionHandler ) { ... }
    public ContractExceptionHandler [] exceptionHandlers () { ... }

    private final List<Contract<EXCEPTION> preFilters =
        new ArrayList<Contract<EXCEPTION>> ();

    public void addPreFilter ( Contract<EXCEPTION> ) { ... }
    public void removePreFilter ( Contract<EXCEPTION> ) { ... }

    private final List<Contract<EXCEPTION> postFilters =
        new ArrayList<Contract<EXCEPTION>> ();

    public void addPostPreFilter ( Contract<EXCEPTION> ) { ... }
    public void removePostFilter ( Contract<EXCEPTION> ) { ... }


    public <VALUE extends Object>
        void mustNotBeNull ( VALUE value )
        throws EXCEPTION;

    public <VALUE extends Object>
        void mustNotContainNulls ( VALUE ... values )
        throws EXCEPTION;

    public <VALUE extends Object>
        void mustBeEqual ( VALUE actual, VALUE actual )
        throws EXCEPTION;

    public <VALUE extends Object>
        void mustBeNotEqual ( VALUE actual, VALUE actual )
        throws EXCEPTION;

    public <VALUE extends Comparable>
        void mustBe ( VALUE actual, Op comparison, VALUE actual )
        throws EXCEPTION;

    public <VALUE extends Comparable>
        void must ( String expression, VALUE value, boolean is_holds_true  )
        throws EXCEPTION;
}


public Integer testContracts ( Integer dividend, Integer divisor )
    throws ContractException
{
    Contract.IN.mustNotContainNulls ( dividend, divisor );
    Contract.IN.mustBe ( divisor, Contract.Op.NOT_EQUAL_TO, 0 );

    Integer result = dividend.divide ( divisor );

    Contract.OUT.mustNotBeNull ( result );
    return result;
}



public class ContractTestHarness
{
    public ... ()
    {
        Contract.IN.addPreFilter!!!!!;
    }
}


=============================


// Chaining Conditionals:
//
// It would be nice to be able to do:
Buffer buf = ...;
Position position = ...;
buf.get ( position ).value ( String.class ).orDefault ( "xyz" );
// However if buf.get () returns a Conditional<Instance> then
// we have to write the following more verbose code instead:
buf.get ( position ).orDefault ( Instance.NONE ).value ( String.class ).orDefault ( "xyz" );
// As a special case for Buffer at least it would be nice
// to return a ConditionalInstance:
public interface ConditionalInstance extends Conditional<Instance>, Instance
{
}
public class SuccessfulInstance extends Success<Instance>
    implements ConditionalInstance
{
    // ...Each call to Instance methods proxies to the underyling successful
    // Instance's method...
}
public class FailedInstance extebds Failed<Instance>
    implements ConditionalInstance
{
    // ...Each call to Instance methods proxies to an Instance.NONE method...
}
// That way we can do the less verbose approach above.
// Of course this approach is only really reusable for Instance-derived
// interfaces.


=============================


public interface Instance
    extends Serializable
{
    public abstract boolean isContentSerializable ();
}


Maybe Template is a raw class:
new Template<String> ( "Hello [%parameter1%] [%parameter2%]\n", 123, world );
cast to string --> "Hello 123 world"

Localized extends Template
new Localized<String> ( Locale.FRENCH, "Bonjour [%parameter1%] [%parameter2%]\n", 123, world );

Platformized extends Template
new Platformized<String> ( Platform.WINDOWS, string.replaceAll ( "\\n", "\\n\\r" ), ... );

public class Template<DOMAIN, VALUE>
    implements Serializable
{
    private final TypingEnvironment typing;
    private final DOMAIN domain; // e.g. locale, platform
    private final Type type;
    private final Class<VALUE> templateClass;
    private final VALUE template;
    private final TemplateBuilder<VALUE> templateBuilder;
    private final Instance [] parameters;

    private VALUE finalValue = null; // Set when build () is called.

    public Template<VALUE> (
                            TypingEnvironment typing,
                            DOMAIN domain,
                            Class<VALUE> template_class,
                            VALUE template,
                            Object ... parameters
                            )
    {
        this.typing = typing;
        this.domain = domain;
        this.templateClass = template_class;
        this.template = template;

        this.type = this.typing.typeOf ( this.templateClass );
        this.templateBuilder = this.type.templateBuilder ( this.templateClass );
        if ( this.templateBuilder == null ) { ... throw exception ... }

        this.parameters = new Instance [ parameters.length ];
        int p = 0;
        for ( Object parameter : parameters )
        {
            this.parameters [ p ] = this.typing.prepare ( parameter ).build ();
            if ( ! this.parameters [ p ].isCastableTo ( this.templateClass ) )
                { ... throw exception... }

            p ++;
        }
    }

    public VALUE build ()
    {
        synchronized ( this.lock )
        {
            if ( this.finalValue == null )
            {
                this.finalValue =
                    this.templateBuilder.buildValue ( this.template,
                                                      this.parameters );
            }
        }

        // Once the value is set once, no need to synchronized on
        // the return value any more.
        return this.finalValue;
    }
}


public interface TemplateBuilder<VALUE extends Object>
    extends Serializable
{
    public abstract Conditional<VALUE> buildValue (
                                                   VALUE template,
                                                   Object ... parameters
                                                   );
}

public class StringTemplateBuilder
    implements Template<String>
{
    public Conditional<String> buildValue (
                                           String template,
                                           Instance ... parameters
                                           )
    {
        StringBuilder sbuf = new StringBuilder ();
        split template into chunks;
        for ( Instance parameter : parameters )
        {
            ???How to identify which 
}








public class Structure
    implements Space
{
    private final Buffer structureBuffer;

    ... Returns a FieldPosition (Position referring to one Instance in the
        underlying Buffer) per position.
        Instance.value ( String.class ) is the name of the position, so
        you can do FieldPosition.name () and it returns
        Instance.value ( String,class ) from the Instance in the
        underlying structure Buffer at that position. ;

    protected Buffer structureBuffer () { return this.structureBuffer; }
}

public interface FieldPosition
    extends Position
{
    public String name ();
    public long minimum ();
    public long maximum ();
    public Type type ();
}

public class StructuredFieldPosition
    implements FieldPosition
{
    private final Structure structure;
    private final Position offsetIntoStructureBuffer;

    public long minimum ()
    {
        Field field = this.structure.structureBuffer ()
            .value ( this.offsetIntoStructureBuffer, Field.class )
            .orDefault ( Field.NONE );
        return field.minimum ();
    }

    public long maximum () { ...etc... }

    public Type type () { ...etc... }

    public String name ()
    {
        String name = this.structure.structureBuffer ().value ( String.class )
            .orDefault ( "" + this.offsetIntoStructureBuffer );
    }
}

public class UnstructuredFieldPosition
{
    private final Field field;

    public UnstructuredFieldPosition ( Field field ) { ... }

    public UnstructuredFieldPosition ( String name )
    {
        this.field = new Field ( name );
    }

    ...etc constructors...
}


public class Field
{
    private final String name;
    private final Type type;
    private final long minimum; // default constructor --> 1
    private final long maximum; // default constructor --> 1

    public Field ( ... ) // Various constructors, default constructors
    {
        ...
    }
}


A Buffer full of Field instances is the main idea behind Structures.
However a Buffer full of String instances or Number instances will
    suffice, so that pretty much any buffer whose contents can be cast to
    Strings is usable in a Structure. ;


public interface Type<STORAGE_VALUE>
{
    Domain<STORAGE_VALUE> domain ();
    Conditional<Domain<RAW_VALUE extends STORAGE_VALUE>> domain ( Class<RAW_VALUE> raw_class );
}

public interface Domain<VALUE>
{
    public abstract boolean isInDomain ( VALUE );
}

public interface EnumeratedDomain<VALUE>
    extends Domain<VALUE>, Region
{
    @Override public abstract DomainPosition start ();
}

public interface EnumeratedDomainPosition<VALUE>
    extends Position
{
    public abstract VALUE value ();
}

============================================


Instance gets renamed to Term.

Variable is derived from Term.  It has no value?!?  Or something...

A Buffer can be a set of instructions for use as a Function.

A stream has a Type.  A stream of Strings might have a Text[1..n] Type,
    while a stream which inputs or outputs an object of a specific
    Type would have that Type.  An input and an output stream cannot
    be connected if they do not have the same Type.  Two streams of
    the same Type can be concatenated into a stream of Type X[n].
    Two streams of different Types can be concatenated into a stream
    of Type [X, Y].

A Function reads from 0 or more input streams and writes to 0 or more output
  streams.  Its Type is comprised of the set of in and out stream Types.
  If an input stream has type X then feeding it from an output stream
  of type X[n] means all input streams have to have type ABC[n] and
  the function is executed [n] times, once for each set of input parameters.

An Object