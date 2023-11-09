package musaico.foundation.contract;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * <p>
 * The contract manager for a specific object.
 * </p>
 *
 * <p>
 * Typically a class will create its own static private final Advocate
 * for enforcing contracts on constructors and static methods, and
 * an object will create its own private final Advocate for enforcing
 * contracts on methods.  Wherever needed, the <code> check () </code>
 * method of the appropriate Advocate is invoked to enforce
 * the appropriate contracts.
 * </p>
 *
 * <p>
 * In Java, the behaviour of Advocate can be controlled by setting
 * system properties.  This control is meant to be similar to the controls
 * used by logging libraries.
 * </p>
 *
 * <p>
 * The properties which can be defined to control the behaviour of the
 * Advocate are as follows:
 * </p>
 *
 * <ul>
 *   <li>
 *     <b> <code> musaico.contracts </code> </b> :
 *       This property sets the <code> Judge </code>
 *       to be used by default by all Advocate instances.
 *   </li>
 *   <li>
 *     <b> <code> musaico.contracts:fully.qualified.package </code> <b> :
 *       This property sets the <code> Judge </code>
 *       to be used by default by all Advocate instances for
 *       classes of a specific package and its sub-packages as well
 *       as object instances of those classes.  Note that this
 *       setting does NOT affect classes or objects which derive
 *       from classes of the specified package.
 *   </li>
 *   <li>
 *     <b> <code> musaico.contracts:fully.qualified.class </code> </b> :
 *       This property sets the <code> Judge </code>
 *       to be used for the Advocate instance for the specified
 *       class, as well as the <code> Judge <code> to be used
 *       by default for instances of that class.  Note that this
 *       setting does NOT affect classes or objects which derive
 *       from the specified class.
 *   </li>
 *   <li>
 *     <b> <code> musaico.contracts:fully.qualified.class:toString </code> </b> :
 *       For those who want stupidly specific control, this
 *       property sets the <code> Judge </code>
 *       to be used for the Advocate instance for the
 *       specified instance of the specified class.  When the
 *       Advocate is created, it looks at the fully qualified
 *       class of the object whose contracts it is enforcing, and
 *       takes the <code> toString () </code> of that object, and
 *       looks to see if there is a specific setting for the object.
 *   </li>
 * </ul>
 *
 * <p>
 * So each Advocate looks for the class of Judge to use
 * according to the order: object - class - packages ... - overall default.
 * </p>
 *
 * <p>
 * The class of Judge determines how/whether Contracts will be
 * inspected and enforced.  For example, normally Contracts are
 * inspected and enforced by throwing exceptions on violation, using
 * the <code> musaico.foundation.contract.StandardJudge </code>
 * class.  However speed nuts can totally disable contract inspection
 * and enforcement by specifying
 * <code> musaico.foundation.contract.NoJudge </code>.  Logging nuts
 * and aspect-oriented nuts might want to capture logging information
 * every time a Contract is broken, before carrying on with the
 * standard enforcement behaviour.  And so on.
 * </p>
 *
 * <p>
 * As a security precaution, the Judge settings are read in only
 * once per process, to prevent malicious or stupid code from overriding
 * settings in the middle of process execution.  The first time any
 * Advocate is created, it will load in the settings.  After that
 * no changes will ever affect its behaviour.
 * </p>
 *
 * @see musaico.foundation.contract.Judge
 *
 *
 * <p>
 * In Java every Advocate is Serializable in order to play
 * nicely over RMI.
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
public class Advocate
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The lookup of Judges to use for different plaintiffs.
     *  Indexed as described in the header of this class.
     *  Created once and then never modified. */
    private static final Map<String, Judge> judges = createJudges ();


    /** The Judge who checks and enforces contracts for our plaintiff. */
    private final Judge judge;

    /** A serializable representation of the parent object, on whose
     *  behalf which we manage contracts. */
    private final Serializable plaintiff;


    /**
     * <p>
     * Creates a new Advocate for the specified plaintiff, using
     * the Judge specified in the system properties.
     * </p>
     *
     * <p>
     * Be careful to only call <code> new Advocate ( plaintiff ) </code>
     * on a plaintiff which is initialized to the point that its
     * <code> toString () </code> method will not throw
     * NullPointerExceptions and so on.  Usually it is best to create
     * the Advocate for a plaintiff as the very last step of
     * the plaintiff's constructor.
     * </p>
     *
     * @param plaintiff The object on whose behalf we will manage contracts.
     *                  A Serializable reference to the specified plaintiff
     *                  will be stored by calling plaintiff.toString ().
     *                  No reference to the actual plaintiff will be kept.
     *                  Must not be null.
     */
    public Advocate (
                     Object plaintiff
                     )
    {
        this ( plaintiff,
               Advocate.judge ( Advocate.judges, plaintiff ) );
    }


    /**
     * <p>
     * Creates a new Advocate for the specified plaintiff and with
     * a specific Judge, overriding the system properties settings.
     * </p>
     *
     * <p>
     * This constructor can be used, for example, when certain contract
     * enforcement should always be handled the same way, and not
     * be configurable.
     * </p>
     *
     * <p>
     * Be careful to only call
     * <code> new Advocate ( plaintiff, Judge ) </code>
     * on a plaintiff which is initialized to the point that its
     * <code> toString () </code> method will not throw
     * NullPointerExceptions and so on.  Usually it is best to create
     * the Advocate for a plaintiff as the very last step of
     * the plaintiff's constructor.
     * </p>
     *
     * @param plaintiff The object on whose behalf we will manage contracts.
     *                  A Serializable reference to the specified plaintiff
     *                  will be stored by calling plaintiff.toString ().
     *                  No reference to the actual plaintiff will be kept.
     *                  Must not be null.
     */
    public Advocate (
                     Object plaintiff,
                     Judge judge
                     )
    {
        this.plaintiff = createSerializable ( plaintiff );
        this.judge = judge;
    }


    /**
     * <p>
     * Looks up the specified plaintiff in the specified judges map,
     * and returns the appropriate Judge to use for that plaintiff.
     * </p>
     *
     * <p>
     * If there is no default Judge defined in the specified map,
     * then Judge.DEFAULT will be returned.
     * </p>
     *
     * @param judges The lookup of Judge by default/package/class/object.
     *               Must not be null.  Must not contain any
     *               null keys or values.
     *
     * @param plaintiff The object under contract.  Must not be null.
     *
     * @return The Judge for the specified plaintiff.  Never null.
     */
    public static final Judge judge (
                                     Map<String, Judge> judges,
                                     Object plaintiff
                                     )
    {
        if ( judges == null
             || plaintiff == null )
        {
            return Judge.DEFAULT;
        }

        String plaintiff_id = plaintiff.toString ();
        final Class<?> plaintiff_class = plaintiff.getClass ();
        final String class_id = plaintiff_class.getName ();
        final String prefix = "musaico.contracts";

        String key = prefix + ":" + class_id + ":" + plaintiff_id;
        Judge judge = judges.get ( key );
        if ( judge != null )
        {
            // Object-specific Judge.
            return judge;
        }

        key = prefix + ":" + class_id;
        judge = judges.get ( key );
        if ( judge != null )
        {
            // Class-specific Judge.
            return judge;
        }

        int index = class_id.length () + 1;
        while ( ( index = class_id.lastIndexOf ( '.', index - 1 ) ) > 0 )
        {
            final String package_id = class_id.substring ( 0, index );
            key = prefix + ":" + package_id;
            judge = judges.get ( key );
            if ( judge != null )
            {
                // Package-specific Judge.
                return judge;
            }
        }

        key = prefix;
        judge = judges.get ( key );
        if ( judge != null )
        {
            // Default Judge.
            return judge;
        }

        // No default Judge defined.  Use Judge.DEFAULT instead.
        return Judge.DEFAULT;
    }


    /**
     * <p>
     * Gives the specified contract to the current judge to inspect
     * and enforce (though nothing will happen if the judge does
     * not do inspections).
     * </p>
     *
     * <p>
     * If the contract has been breached and the current judge enforces
     * contracts by throwing exceptions, then a ContractViolation will
     * be thrown.
     * </p>
     *
     * @param contract The contract to inspect.  Must not be null.
     *
     * @param plaintiff The under contract, such
     *                  as an object whose method enforces
     *                  this contract.  Must not be null.
     *
     * @param evidence The evidence, to be checked by
     *                 this Advocate.  If any of the evidence
     *                 do not match the contract requirements,
     *                 then a violation will be thrown.
     *                 If more than one evidence is passed
     *                 via varargs, then an Object array is created
     *                 for the contracts to check.
     *                 Can be null.  Can contain null elements.
     *
     * @return The evidence.
     *
     * @throws ContractViolation If the judge inspected the contract,
     *                           determined that it was breached,
     *                           and enforced the contract by throwing
     *                           a violation.
     */
    public
        <INSPECTABLE extends Object, EVIDENCE extends INSPECTABLE, VIOLATION extends Throwable & Violation>
        EVIDENCE check (
                        Contract<INSPECTABLE, VIOLATION> contract,
                        EVIDENCE evidence
                        )
        throws VIOLATION
    {
        // Potentially throws ContractViolations up the stack:
        this.judge.inspect ( contract,
                             this.plaintiff,
                             evidence );

        return evidence;
    }


    /**
     * <p>
     * Variant on check () which allows for variable # of arguments
     * to be passed in, for checking arrays (such as checking that
     * all parameters are not null).
     * </p>
     *
     * @see musaico.foundation.contract.Advocate#check(musaico.foundation.contract.Contract, java.lang.Object)
     *
     * @return The evidence.
     */
    @SuppressWarnings("unchecked") // possible heap pollution EVIDENCE varargs
    public
        <EVIDENCE extends Object, VIOLATION extends Throwable & Violation>
            EVIDENCE [] check (
                               Contract<EVIDENCE [], VIOLATION> contract,
                               EVIDENCE ... evidence
                               )
        throws VIOLATION
    {
        // Potentially throws ContractViolations up the stack:
        this.judge.inspect ( contract,
                             this.plaintiff,
                             evidence );

        return evidence;
    }


    /**
     * <p>
     * Creates a lookup of Judges by key, as described in the
     * header documentation for Advocate.
     * </p>
     *
     * <p>
     * The Advocate class itself only ever calls this method
     * once, at class load time.  Anyone else wanting to
     * reuse the loading logic is welcome to call it any time.
     * It will not, however, affect the settings used by Advocate,
     * which are read-once, never modify.
     * </p>
     *
     * @return The newly created lookup of Judges by default/package/
     *         class/object.  Never null.  Never contains any null
     *         keys or values.
     */
    public static Map<String, Judge> createJudges ()
        throws RuntimeException
    {
        final Map<String, Judge> judges =
            new HashMap<String, Judge> ();

        final String prefix = "musaico.contracts";
        final Properties properties = System.getProperties ();
        for ( Object property_name : properties.keySet () )
        {
            if ( ! ( property_name instanceof String ) )
            {
                continue;
            }

            final String key = (String) property_name;

            if ( ! key.startsWith ( prefix ) )
            {
                continue;
            }

            final Object value = properties.getProperty ( key );

            final Judge judge;
            if ( value instanceof String )
            {
                final String class_name = (String) value;

                // Throws a RuntimeException if the class is not even loaded
                // or if it is not a class of Judge.
                try
                {
                    final Class<?> judge_class =
                        Class.forName ( class_name );
                    judge = (Judge) judge_class.newInstance ();
                }
                catch ( Exception e )
                {
                    throw new RuntimeException ( "Failed to load Judge String '"
                                                 + class_name
                                                 + "' for "
                                                 + key,
                                                 e );
                }
            }
            else if ( value instanceof Class )
            {
                Class<?> judge_class = (Class<?>) value;

                // Throws a RuntimeException if the class cannot be
                // instantiated, or is not a class of Judge, and so on.
                try
                {
                    judge = (Judge) judge_class.newInstance ();
                }
                catch ( Exception e )
                {
                    throw new RuntimeException ( "Failed to load Judge Class "
                                                 + judge_class
                                                 + " for "
                                                 + key,
                                                 e );
                }
            }
            else if ( value instanceof Judge )
            {
                judge = (Judge) value;
            }
            else
            {
                throw new RuntimeException ( "Failed to load Judge property '"
                                             + value
                                             + "' for "
                                             + key );
            }

            judges.put ( key, judge );
        }

        if ( ! judges.containsKey ( prefix ) )
        {
            // No default has been defined.  Use Judge.DEFAULT as
            // the default.
            judges.put ( prefix, Judge.DEFAULT );
        }

        return judges;
    }


    /**
     * <p>
     * Creates a Serializable representation of the specified plaintiff.
     * By default <code> plaintiff.toString () </code> is returned.
     * </p>
     *
     * @param plaintiff The object under contract.  Must not be null.
     *
     * @return The Serializable representation of the plaintiff.
     *         Never null.
     */
    protected Serializable createSerializable (
                                               Object plaintiff
                                               )
    {
        return "" + plaintiff;
    }


    /**
     * @see java.lang.Objects#toString()
     */
    @Override
    public String toString ()
    {
        return "Advocate ( " + this.plaintiff + " )"
            + " [ " + this.judge + " ]";
    }
}
