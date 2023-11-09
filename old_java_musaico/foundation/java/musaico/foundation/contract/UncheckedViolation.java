package musaico.foundation.contract;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;


import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.Serializables;
import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * A violation of some contract without any specific detail
 * about what went wrong.
 * </p>
 *
 *
 * <br> </br>
 * <br> </br>
 *
 * <hr> </hr>
 *
 * <br> </br>
 * <br> </br>
 *
 *
 * <p>
 * For copyright and licensing information refer to:
 * </p>
 *
 * @see musaico.foundation.contract.MODULE#COPYRIGHT
 * @see musaico.foundation.contract.MODULE#LICENSE
 */
public class UncheckedViolation
    extends RuntimeException
    implements Violation, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The contract which enforced this UncheckedViolation.
    private final Contract<?, ?> contract;

    // A human-readable, non-internationalized explanation of
    // why the contract was violated.  Used by developers, testers
    // and maintainers to troubleshoot and debug exceptions and errors.
    private final String description;

    // The serialized version of the object under contract.
    private final Serializable plaintiff;

    // The serialized version of the evidence data which did
    //  not comply with the contract.
    private final Serializable evidence;


    /**
     * <p>
     * Creates a new UncheckedViolation of the specified contract,
     * on behalf of the specified plaintiff, with the specified
     * violating evidence.
     * </p>
     *
     * @param contract The contract which was violated.
     *                 Must not be null.
     *
     * @param description A human-readable, non-internationalized
     *                    explanation of why the contract was violated.
     *                    Used by developers, testers and maintainers
     *                    to troubleshoot and debug exceptions and errors.
     *                    Must not be null.
     *
     * @param plaintiff The object under contract, such as the object
     *                  whose method obligation was violated, or which
     *                  violated its own method guarantee.  Must not be null.
     *
     * @param evidence The data which violated the contract.
     *                 Can be null.
     */
    public UncheckedViolation (
                               Contract<?, ?> contract,
                               String description,
                               Object plaintiff,
                               Object evidence
                               )
    {
        super ( description );

        this.contract = contract;
        this.description = description;
        this.plaintiff =
            Serializables.makeSerializable ( plaintiff );
        this.evidence =
            Serializables.makeSerializable ( evidence );
    }


    /**
     * <p>
     * Creates a new UncheckedViolation of the specified contract,
     * on behalf of the specified plaintiff, with the specified
     * violating evidence.
     * </p>
     *
     * @param contract The contract which was violated.
     *                 Must not be null.
     *
     * @param description A human-readable, non-internationalized
     *                    explanation of why the contract was violated.
     *                    Used by developers, testers and maintainers
     *                    to troubleshoot and debug exceptions and errors.
     *                    Must not be null.
     *
     * @param plaintiff The object under contract, such as the object
     *                  whose method obligation was violated, or which
     *                  violated its own method guarantee.  Must not be null.
     *
     * @param evidence The data which violated the contract.
     *                 Can be null.
     *
     * @param cause The Throwable which caused this contract violation.
     *              Can be null.
     */
    public UncheckedViolation (
                               Contract<?, ?> contract,
                               String description,
                               Object plaintiff,
                               Object evidence,
                               Throwable cause
                               )
    {
        this ( contract, description, plaintiff, evidence );

        if ( cause != null )
        {
            this.initCause ( cause );
        }
    }


    /**
     * <p>
     * Creates a new UncheckedViolation to wrap the specified
     * contract Violation.
     * </p>
     *
     * @param violation The contract Violation to wrap.
     *                  Must not be null.
     */
    public <VIOLATION extends Throwable & Violation>
                              UncheckedViolation (
                                                  VIOLATION violation
                                                  )
    {
        this ( violation == null
                   ? null
                   : violation.contract (),
               violation == null
                   ? null
                   : violation.description (),
               violation == null
                   ? null
                   : violation.plaintiff (),
               violation == null
                   ? null
                   : violation.evidence (),
               violation  ); // cause
    }


    /**
     * @see musaico.foundation.contract.Violation#contract()
     */
    @Override
    public Contract<?, ?> contract ()
    {
        return this.contract;
    }


    /**
     * @see musaico.foundation.contract.Violation#description()
     */
    @Override
    public final String description ()
    {
        return this.description;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object object
                           )
    {
        if ( object == this )
        {
            return true;
        }
        else if ( object == null )
        {
            return false;
        }
        else if ( object.getClass () != this.getClass () )
        {
            return false;
        }

        final UncheckedViolation that = (UncheckedViolation) object;

        if ( this.contract == null )
        {
            if ( that.contract != null )
            {
                return false;
            }
        }
        else if ( that.contract == null )
        {
            return false;
        }
        else if ( ! this.contract.equals ( that.contract ) )
        {
            return false;
        }

        if ( this.description == null )
        {
            if ( that.description != null )
            {
                return false;
            }
        }
        else if ( that.description == null )
        {
            return false;
        }
        else if ( ! this.description.equals ( that.description ) )
        {
            return false;
        }

        if ( this.plaintiff == null )
        {
            if ( that.plaintiff != null )
            {
                return false;
            }
        }
        else if ( that.plaintiff == null )
        {
            return false;
        }
        else if ( ! this.plaintiff.equals ( that.plaintiff ) )
        {
            return false;
        }

        if ( this.evidence == null )
        {
            if ( that.evidence != null )
            {
                return false;
            }
        }
        else if ( that.evidence == null )
        {
            return false;
        }
        else if ( ! this.evidence.equals ( that.evidence ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = 0;
        if ( this.contract != null )
        {
            hash_code += 31 * this.contract.hashCode ();
        }
        if ( this.plaintiff != null )
        {
            hash_code += 17 * this.plaintiff.hashCode ();
        }
        if ( this.evidence != null )
        {
            hash_code += this.evidence.hashCode ();
        }

        return hash_code;
    }


    /**
     * @see musaico.foundation.contract.Violation#evidence()
     */
    @Override
    public Serializable evidence ()
    {
        return this.evidence;
    }


    /**
     * @see musaico.foundation.contract.Violation#plaintiff
     */
    @Override
    public Serializable plaintiff ()
    {
        return this.plaintiff;
    }


    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    @Override
    public final void printStackTrace ()
    {
        this.printStackTrace ( System.err );
        System.err.flush ();
    }


    /**
     * @see java.lang.Throwable#printStackTrace(java.io.StreamWriter)
     */
    @Override
    public final void printStackTrace (
                                       PrintStream out_stream
                                       )
    {
        final PrintWriter out =
            new PrintWriter (
                new OutputStreamWriter (
                    out_stream ) );
        this.printStackTrace ( out );
        out_stream.flush ();
    }


    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    @Override
    public final void printStackTrace (
                                       PrintWriter out
                                       )
    {
        CheckedViolation.printStackTrace ( this, out );
        out.flush ();
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString ()
    {
        final StringBuilder sbuf = new StringBuilder ();
        sbuf.append ( ClassName.of ( this.getClass () ) );
        sbuf.append ( " (unchecked violation) {" );
        sbuf.append ( " contract = " + this.contract () );
        sbuf.append ( ", plaintiff   = "
                      + StringRepresentation.of (
                            this.plaintiff (),
                            StringRepresentation.DEFAULT_OBJECT_LENGTH
                            ) );
        sbuf.append ( ", evidence = "
                      + StringRepresentation.of (
                            this.evidence (),
                            StringRepresentation.DEFAULT_OBJECT_LENGTH
                            ) );
        sbuf.append ( " }" );
        return sbuf.toString ();
    }
}
