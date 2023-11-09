package behaviour.events;

import java.lang.reflect.Method;

public class MethodInvoked<BEHAVING_OBJECT extends Object>
    implements BehaviouralEvent<BEHAVING_OBJECT extends Object>
{
    private final Method method;
    private final Value [] parameters;
    private final Variable returnValue;

    public class Builder<BUILDER_BEHAVING_OBJECT extends Object>
    {
        private final Method method;
        private final List<Value> parameters = new ArrayList<Value> ();
        private final Variable returnValue;

        public Builder ( Method method, Variable return_value )
        {
            this.method = method;
            this.returnValue = return_value;
        }

        public Builder ( Method method )
        {
            this ( method, new Variable ( "", "NO RETURN VALUE YET" ) );
        }

        public MethodInvoked<BUILDER_BEHAVING_OBJECT> build ()
        {
            final Value [] template = new Value [ this.parameters.size () ];
            final Value [] parameters = this.parameters.toArray ( template );

            return new MethodInvoked<BUILDER_BEHAVING_OBJECT> ( this.method,
                                                                parameters,
                                                                this.returnValue );
        }
    }

    public MethodInvoked (
                          Method method,
                          Value [] parameters,
                          Variable return_value
                          )
    {
        this.method = method;
        this.parameters = new Value [ parameters.length ];
        System.arraycopy ( parameters, 0,
                           this.parameters, 0, parameters.length );

        this.returnValue = return_value;
    }

    @Override
    public void execute (
                         BEHAVING_OBJECT behaving_object
                         )
    {
        Object [] parameter_values = new Object [ this.parameters.length ];
        for ( int p = 0; p < this.parameters.length; p ++ )
        {
            parameter_values = this.parameters [ p ].value ();
        }

        final Object return_value;
        try
        {
            return_value = this.method.invoke ( behaving_object,
                                                parameter_values );
        }
        catch ( Exception e )
        {
            throw new BehaviourViolation ( "Failed to execute " + this
                                           + " on behaving object "
                                           + behaving_object, e );
        }

        this.returnValue.value ( return_value );
    }

    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "Invoke " );
        sbuf.append ( this.method.getName () );
        sbuf.append ( " (" );
        boolean is_first = true;
        for ( Value parameter : this.parameters )
        {
            if ( is_first )
            {
                sbuf.append ( " " );
                is_first = false;
            }
            else
            {
                sbuf.append ( ", " );
            }

            sbuf.append ( "" + parameter );
        }

        if ( is_first )
        {
            sbuf.append ( ")" );
        }
        else
        {
            sbuf.append ( " )" );
        }

        final String return_value_variable_name = "" + this.returnValue;
        if ( ! return_value_variable_name.equals ( "" ) )
        {
            sbuf.append ( " --> " );
            sbuf.append ( return_value_variable_name );
        }

        return sbuf.toString ();
    }
}
