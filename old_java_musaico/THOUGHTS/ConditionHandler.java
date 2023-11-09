/**
 * <p>
 * A handler for success/failure conditions when retrieving the
 * value of an Instance as a specific raw class.
 * </p>
 *
 * <p>
 * A ConditionHandler can be used to look for alternative values
 * or even filter successful values or thrown exceptions.
 * </p>
 *
 * <p>
 * Code relying on a conditon handler might invoke something along
 * the lines of:
 * </p>
 *
 * <pre>
 *     Instance age = ...;
 *     ConditionHandler in_dog_years = ...; // Divides human age by 7,
 *         // or throws a DogTypeException.
 *     int age_in_dog_years =
 *         age.value ( Integer.class ).handleWith ( in_dog_years )
 *                                    .orDefault ( -1 );
 * </pre>
 *
 * <p>
 * In the example above, the instance is conditionally cast to an
 * Integer, then depending on whether or not the cast was successful,
 * the condition handler is invoked to create a final ConditionalValue.
 * The code then takes either the Integer value from the final result,
 * or defaults to -1 if an exception occurred either during the original
 * cast or during the condition handling.
 * </p>
 */
public interface ConditionHandler
{
    /**
     * !!!
     */
    public abstract <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
        ConditionalValue<STORAGE_VALUE, RAW_VALUE> handleSuccess (
                                                                  ConditionalValue<STORAGE_VALUE, RAW_VALUE> input,
                                                                  RAW_VALUE raw_value
                                                                  );


    /**
     * !!!
     */
    public abstract <STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
        ConditionalValue<STORAGE_VALUE, RAW_VALUE> handleFailure (
                                                                  ConditionalValue<STORAGE_VALUE, RAW_VALUE> input,
                                                                  TypeException exception
                                                                  );
}




/**
 * !!!
 */
public class ConditionalValue<STORAGE_VALUE extends Object, RAW_VALUE extends STORAGE_VALUE>
{
    private final Instance instance;
    private final Type<STORAGE_VALUE> type;
    private final Class<RAW_VALUE> rawClass;

    private final RAW_VALUE rawValue;
    private final TypeException exception;


    /**
     * !!!
     */
    public STORAGE_VALUE orNone ()
    {
        if ( this.exception == null )
        {
            return this.rawValue;
        }
        else
        {
            return this.type.none ();
        }
    }


    /**
     * !!!
     */
    public RAW_VALUE orDefault (
                                RAW_VALUE default_value
                                )
    {
        if ( this.exception == null )
        {
            return this.rawValue;
        }
        else
        {
            return default_value;
        }
    }


    /**
     * !!!
     */
    public RAW_VALUE orChecked ()
        throws TypeException
    {
        if ( this.exception == null )
        {
            return this.rawValue;
        }
        else
        {
            throw this.exception;
        }
    }


    /**
     * !!!
     */
    public RAW_VALUE orRuntime ()
        throws TypeRuntimeException
    {
        if ( this.exception == null )
        {
            return this.rawValue;
        }
        else
        {
            throw new TypeRuntimeException ( this.exception );
        }
    }


    /**
     * !!!
     */
    public ConditionalValue<STORAGE_VALUE, RAW_VALUE> handleWith (
                                                                  ConditionHandler handler
                                                                  )
    {
        if ( this.exception == null )
        {
            return handler.handleSuccess ( this, this.rawValue );
        }
        else
        {
            return handler.handleFailure ( this, this.exception );
        }
    }


    /**
     * !!!
     */
    public Instance instance ()
    {
        return this.instance;
    }

    public Type<STORAGE_VALUE> type ()
    {
        return this.type;
    }


    /**
     * !!!
     */
    public Class<RAW_VALUE> rawClass ()
    {
        return this.rawClass;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( obj == null
             || ! ( obj instanceof ConditionalValue ) )
        {
            return false;
        }

        ConditionalValue<?, ?> that = (ConditionalValue<?, ?>) obj;
        if ( ! this.rawClass ().equals ( that.rawClass () )
             || ! this.type ().equals ( that.type () )
             || ! this.instance ().equals ( that.instance () ) )
        {
            return false;
        }
        else if ( this.rawValue == null )
        {
            if ( that.rawValue == null )
            {
                if ( this.exception == null )
                {
                    if ( that.exception == null )
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else if ( that.excetpion == null )
                {
                    return false;
                }
                else
                {
                    return this.exception.equals ( that.exception );
                }
            }
            else
            {
                return false;
            }
        }
        else if ( that.rawValue == null )
        {
            return false;
        }
        else
        {
            return this.rawValue.equals ( that.rawValue );
        }
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code;
        hash_code = ( this.instance == null ? 0 : this.instance.hashCode () );
        hash_code += ( this.type == null ? 0 : this.type.hashCode () );
        hash_code += ( this.rawClass == null ? 0 : this.rawClass.hashCode () );
        hash_code += ( this.rawValue == null ? 0 : this.rawValue );
        hash_code += ( this.exception == null ? 0 : this.exception );

        return hash_code;
    }


    /**
     * @see java.lang.toString()
     */
    @Override
    public String toString ()
    {
        StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( "" + instance );
        sbuf.append ( ".value ( " );
        sbuf.append ( "\"" );
        sbuf.append ( type.name () );
        sbuf.append ( "\", " );
        sbuf.append ( this.rawClass.getSimpleName () );
        sbuf.append ( ".class )" );

        return sbuf.toString ();
    }
}
