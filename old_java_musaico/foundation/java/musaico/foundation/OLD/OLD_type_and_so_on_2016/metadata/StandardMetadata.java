package musaico.foundation.metadata;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.security.SecurityPolicy;

import musaico.foundation.value.Countable;
import musaico.foundation.value.Idempotent;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;
import musaico.foundation.value.ZeroOrOne;

import musaico.foundation.value.builder.ValueBuilder;

import musaico.foundation.value.finite.AbstractMultiple;
import musaico.foundation.value.finite.No;
import musaico.foundation.value.finite.One;
import musaico.foundation.value.finite.ValueMustBeOne;

import musaico.foundation.value.iterators.UnchangingIterator;

import musaico.foundation.value.unidempotent.Mutable;


/**
 * <p>
 * Simple thread-safe metadata for an object.
 * </p>
 *
 *
 * <p>
 * In Java every Metadata must be Serializable
 * in order to play nicely over RMI.
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
 * @see musaico.foundation.metadata.MODULE#COPYRIGHT
 * @see musaico.foundation.metadata.MODULE#LICENSE
 */
public class StandardMetadata
    extends AbstractMultiple<Metadatum>
    implements Metadata, Mutable<Metadatum>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** Sorts metadata by the ASCii or character encoding order of
     *  their toString ()s. */
    public static final Comparator<Metadatum> METADATA_SORTER =
        new Comparator<Metadatum> ()
    {
        /**
         * @see java.util.Comparator.compare(java.lang.Object, java.lang.Object)
         * Final for speed.
         */
        @Override
        public final int compare (
                                  Metadatum left_metadatum,
                                  Metadatum right_metadatum
                                  )
        {
            if ( left_metadatum == null )
            {
                if ( right_metadatum == null  )
                {
                    // Null == null.
                    return 0;
                }
                else
                {
                    // Null > any Metadatum.
                    return Integer.MAX_VALUE;
                }
            }
            else if ( right_metadatum == null )
            {
                // Any Metadatum < null.
                return Integer.MIN_VALUE + 1;
            }

            final Serializable left = left_metadatum.metadatum ();
            final Serializable right = right_metadatum.metadatum ();

            if ( left == null )
            {
                if ( right == null  )
                {
                    // Null == null.
                    return 0;
                }
                else
                {
                    // Null > any Serializable.
                    return Integer.MAX_VALUE;
                }
            }
            else if ( right == null )
            {
                // Any Serializable < null.
                return Integer.MIN_VALUE + 1;
            }

            if ( left_metadatum.metadatumClass () ==
                     right_metadatum.metadatumClass ()
                 && Comparable.class.isAssignableFrom ( left_metadatum.metadatumClass () ) )
            {
                // Comparable metadata.
                @SuppressWarnings("unchecked") // Cast to Comparable<Serializ.>
                final Comparable<Serializable> left_comparable =
                    (Comparable<Serializable>) left;

                return left_comparable.compareTo ( right );
            }

            // Either 2 metadata of different classes,
            // or 2 metadata of the same un-Comparable class.
            // Use toString () and String compare.
            final String left_as_string = "" + left;
            final String right_as_string = "" + right;

            return left_as_string.compareTo ( right_as_string );
        }
    };


    // Checks contracts on static methods and constructors for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( StandardMetadata.class );


    // Checks method obligations and guarantees for us.
    private final ObjectContracts contracts;

    // Synchronize critical sections on this object:
    private final Serializable lock = new String ();

    // The value violation for when we do not have exactly One metadatum.
    private final ValueViolation singleValueViolation;

    // The lookup of metadatum by type.  Initially empty, until someone
    // adds a particular metadatum.  Lock this map directly during
    // every critical section.
    // MUTABLE.
    private final Map<Class<?>, Metadatum> metadata;


    /**
     * <p>
     * Creates a new empty StandardMetadata with a StandardOrigin.
     * </p>
     */
    public StandardMetadata ()
    {
        this ( new StandardOrigin () );
    }


    /**
     * <p>
     * Creates a new empty StandardMetadata with the specified Origin.
     * </p>
     *
     * @param origin The time, thread, and so on, at/in which the
     *               object parent of this Metadata was created.
     *               Must not be null.
     */
    public StandardMetadata (
                             Origin origin
                             )
        throws Parameter1.MustNotBeNull.Violation
    {
        this ( origin,
               null ); // single_value_violation
    }


    /**
     * <p>
     * Creates a new empty StandardMetadata with the specified Origin.
     * </p>
     *
     * @param origin The time, thread, and so on, at/in which the
     *               object parent of this Metadata was created.
     *               Must not be null.
     *
     * @param single_value_violation The violation of some sort of
     *                               "value must have exactly one element"
     *                               contract, which is used whenever
     *                               the caller invokes
     *                               <code> orThrowChecked () </code>
     *                               and so on.  If null then a default
     *                               contract violation is created.
     *                               Can be null.
     */
    public StandardMetadata (
                             Origin origin,
                             ValueViolation single_value_violation
                             )
        throws Parameter1.MustNotBeNull.Violation
    {
        this ( origin,
               single_value_violation,
               null ); // cause.
    }


    /**
     * <p>
     * Creates a new empty StandardMetadata with the specified Origin.
     * </p>
     *
     * @param origin The time, thread, and so on, at/in which the
     *               object parent of this Metadata was created.
     *               Must not be null.
     *
     * @param single_value_violation The violation of some sort of
     *                               "value must have exactly one element"
     *                               contract, which is used whenever
     *                               the caller invokes
     *                               <code> orThrowChecked () </code>
     *                               and so on.  If null then a default
     *                               contract violation is created.
     *                               Can be null.
     *
     * @param cause The optional cause of this value, such as a
     *              non-Idempotent value of which this is a snapshot,
     *              or a Warning, or a Partial value, and so on.
     *              If null, then this value is its own cause.
     *              Can be null.
     */
    public StandardMetadata (
                             Origin origin,
                             ValueViolation single_value_violation,
                             Value<Metadatum> cause
                             )
        throws Parameter1.MustNotBeNull.Violation
    {
        super ( Metadatum.class,
                cause ); // cause.

        classContracts.check ( Parameter1.MustNotBeNull.CONTRACT,
                               origin );


        this.metadata = new HashMap<Class<?>, Metadatum> ();

        this.contracts = new ObjectContracts ( this );

        this.addOrGet ( Origin.class,
                        origin ); // flags[0]: don't carry over to new instance

        if ( single_value_violation == null )
        {
            final ValueMustBeOne must_be_single =
                ValueMustBeOne.CONTRACT;
            this.singleValueViolation =
                new ValueViolation ( must_be_single.violation ( this,
                                                                this ) );
        }
        else
        {
            this.singleValueViolation = single_value_violation;
        }
    }


    /**
     * @see musaico.foundation.metadata.Metadata#addOrGet(java.lang.Class, java.io.Serializable, musaico.foundation.metadata.Metadatum.Flag[])
     */
    @Override
    @SuppressWarnings("unchecked") // Try...cast METADATUM...catch.
    public <METADATUM extends Serializable>
        METADATUM addOrGet (
                            Class<METADATUM> metadatum_class,
                            METADATUM metadatum_if_required,
                            Metadatum.Flag ... flags
                            )
        throws ParametersMustNotBeNull.Violation,
               Parameter3.MustContainNoNulls.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadatum_class,
                               metadatum_if_required,
                               flags );
        this.contracts.check ( Parameter3.MustContainNoNulls.CONTRACT,
                               flags );

        final METADATUM metadatum;
        synchronized ( this.lock )
        {
            Metadatum container =
                this.metadata.get ( metadatum_class );
            METADATUM existing_metadatum = null;
            if ( container != null )
            {
                try
                {
                    existing_metadatum = (METADATUM)
                        container.metadatum ();
                }
                catch ( ClassCastException e )
                {
                    // We have somehow managed to store a metadatum that
                    // is of the wrong class.  Overwrite the corrupted
                    // metadatum with what the caller passed in.
                    existing_metadatum = null;
                }
            }

            if ( existing_metadatum == null )
            {
                container = new Metadatum ( metadatum_class,
                                            metadatum_if_required,
                                            flags );
                this.metadata.put ( metadatum_class, container );
                metadatum = metadatum_if_required;
            }
            else
            {
                metadatum = existing_metadatum;
            }
        }

        return metadatum;
    }


    /**
     * @see musaico.foundation.value.Value#consequence()
     */
    @Override
    public Idempotent<Metadatum> consequence ()
        throws ReturnNeverNull.Violation
    {
        return this.idempotent ();
    }


    /**
     * @see musaico.foundation.metadata.Metadata#get(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked") // Cast known metadatum classes.
    public <METADATUM extends Serializable>
        ZeroOrOne<METADATUM> get (
                                  Class<METADATUM> metadatum_class
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadatum_class );

        METADATUM metadatum = null;
        synchronized ( this.lock )
        {
            final Metadatum container =
                this.metadata.get ( metadatum_class );
            if ( container != null )
            {
                try
                {
                    metadatum = (METADATUM) container.metadatum ();
                }
                catch ( ClassCastException e )
                {
                    // This should be impossible.
                    // Somehow a corrupted metadatum has been stored under
                    // this class.  Fall back on null.
                    metadatum = null;
                }
            }
        }

        if ( metadatum != null )
        {
            return new One<METADATUM> ( metadatum_class,
                                        metadatum );
        }

        final MetadataViolation violation =
            new MetadataViolation ( new RequestedMetadatumMustExist ( metadatum_class ),
                                    this,
                                    this );

        return new No<METADATUM> ( metadatum_class,
                                   violation );
    }


    /**
     * @see musaico.foundation.metadata.Metadata#has(java.lang.Class)
     */
    @Override
    public <METADATUM extends Serializable>
        boolean has (
                     Class<METADATUM> metadatum_class
                     )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               metadatum_class );

        synchronized ( this.lock )
        {
            return this.metadata.containsKey ( metadatum_class );
        }
    }


    /**
     * @see musaico.foundation.value.Value#idempotent()
     */
    @Override
    public final Idempotent<Metadatum> idempotent ()
    {
        final Metadatum [] elements = this.internalElements ();
        final ValueBuilder<Metadatum> builder =
            new ValueBuilder<Metadatum> ( Metadatum.class,
                                          this, // Cause
                                          elements );
        final Idempotent<Metadatum> idempotent =
            builder.build ().idempotent ();

        return idempotent;
    }


    /**
     * @see musaico.foundation.value.AbstractMultiple#internalElements()
     */
    @Override
    protected final Metadatum [] internalElements ()
    {
        // During the constructor, toString () is called while building
        // object contracts.  toString (), in turn, calls internalElements ().
        // At this point we have not yet built any elements, so we
        // return null.
        // Unfortunately this must occur outside the synchronized () block,
        // since nothing in this class's scope is initialized yet.
        if ( this.metadata == null )
        {
            // Back to the constructor calling toString ().
            return null;
        }

        final List<Metadatum> all_metadata;
        synchronized ( this.lock )
        {
            all_metadata =
                new ArrayList<Metadatum> ( this.metadata.values () );
        }

        // Add in default statistics, origin, security policy, if
        // they have not already been created.
        // This provides defaults to the caller, but also ensures
        // we meet the minimum 2 values for AbstractMultiple.
        boolean has_origin = false;
        boolean has_security_policy = false;
        boolean has_statistics = false;
        for ( Metadatum container : all_metadata )
        {
            if ( container.metadatumClass () == Origin.class )
            {
                has_origin = true;
            }
            else if ( container.metadatumClass () == SecurityPolicy.class )
            {
                has_security_policy = true;
            }
            else if ( container.metadatumClass () == Statistics.class )
            {
                has_statistics = true;
            }
        }

        if ( ! has_origin )
        {
            // Do not carry over any default values when renewing.
            final Metadatum origin =
                new Metadatum ( Origin.class,
                                Origin.NONE,
                                new Metadatum.Flag [ 0 ] );
            all_metadata.add ( origin );
        }
        if ( ! has_security_policy )
        {
            // Do not carry over any default values when renewing.
            final Metadatum security =
                new Metadatum ( SecurityPolicy.class,
                                SecurityPolicy.NONE,
                                new Metadatum.Flag [ 0 ] );
            all_metadata.add ( security );
        }
        if ( ! has_statistics )
        {
            // Do not carry over any default values when renewing.
            final Metadatum statistics =
                new Metadatum ( Statistics.class,
                                Statistics.NONE,
                                new Metadatum.Flag [ 0 ] );
            all_metadata.add ( statistics );
        }

        Collections.sort ( all_metadata,
                           StandardMetadata.METADATA_SORTER );

        final Metadatum [] template =
            new Metadatum [ this.metadata.size () ];
        final Metadatum [] internal_elements =
            all_metadata.toArray ( template );

        return internal_elements;
    }


    /**
     * @see musaico.foundation.value.Unidempotent#iterator()
     */
    public final UnchangingIterator<Metadatum> iterator ()
    {
        // Step over all metadata, even the defaults that are
        // added automagically when they are not present in
        // our internal listing.
        // Keep everything nicely sorted.
        final Metadatum [] elements = this.internalElements ();
        final UnchangingIterator<Metadatum> iterator =
            new UnchangingIterator<Metadatum> ( elements );

        return iterator;
    }


    /**
     * @see musaico.foundation.metadata.Metadata#origin()
     */
    @Override
    public ZeroOrOne<Origin> origin ()
        throws ReturnNeverNull.Violation
    {
        return this.get ( Origin.class );
    }


    /**
     * @see musaico.foundation.metadata.Metadata#renew()
     */
    @Override
    public StandardMetadata renew ()
        throws ReturnNeverNull.Violation
    {
        final List<Metadatum> all_metadata;
        synchronized ( this.lock )
        {
            all_metadata =
                new ArrayList<Metadatum> ( this.metadata.values () );
        }

        final StandardMetadata instance =
            new StandardMetadata (); // default Origin.

        synchronized ( instance.lock )
        {
            for ( Metadatum container : all_metadata )
            {
                if ( container.isSet ( Metadatum.Flag.METADATUM_CARRY_OVER ) )
                {
                    instance.metadata.put ( container.metadatumClass (),
                                            container );
                }
            }
        }

        return instance;
    }


    /**
     * @see musaico.foundation.metadata.Metadata#securityPolicy()
     */
    @Override
    public ZeroOrOne<SecurityPolicy> securityPolicy ()
        throws ReturnNeverNull.Violation
    {
        return this.get ( SecurityPolicy.class );
    }


    /**
     * @see musaico.foundation.metadata.Metadata#statistics()
     */
    @Override
    public ZeroOrOne<Statistics> statistics ()
        throws ReturnNeverNull.Violation
    {
        return this.get ( Statistics.class );
    }


    /**
     * @see musaico.foundation.value.NotOne#valueViolation()
     */
    @Override
    public final ValueViolation valueViolation ()
        throws ReturnNeverNull.Violation
    {
        return this.singleValueViolation;
    }
}
