package musaico.foundation.term.generators;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Iterator;
import java.util.Random;


import musaico.foundation.contract.Advocate;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.ClassName;

import musaico.foundation.term.Type;

import musaico.foundation.term.infinite.Acyclical;


/**
 * <p>
 * A sequence of pseudo-random numbers.
 * </p>
 *
 * <p>
 * In Java the java.util.Random class provides the sequence.
 * </p>
 *
 *
 * <p>
 * In Java every conditional Term must be Serializable in order to
 * play nicely across RMI.  However users of the conditional Term
 * must be careful, since the values and expected data stored inside
 * might not be Serializable.
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
 * @see musaico.foundation.term.generators.MODULE#COPYRIGHT
 * @see musaico.foundation.term.generators.MODULE#LICENSE
 */
public class PseudoRandom<NUMBER extends Object>
    extends Acyclical<NUMBER>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;




    /** Which distribution of pseudo-random numbers is used from the
     *  underlying java.util.Random generator. */
    public static abstract class Distribution<GENERATOR_NUMBER extends Object>
        implements Serializable
    {
        /** The version of the parent module, YYYYMMDD format. */
        private static final long serialVersionUID = MODULE.VERSION;

        /** Random BigDecimal values over a Gaussian distribution (0-1).
         *  Note that the underlying implementation only has Double
         *  precision. */
        public static final Distribution<BigDecimal> BIG_DECIMAL =
            new Distribution<BigDecimal> ( "standard [ BigDecimal ]" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final BigDecimal next (
                                          Random source
                                          )
            {
                return new BigDecimal ( "" + source.nextGaussian () );
            }
        };

        /** Random Boolean values. */
        public static final Distribution<Boolean> BOOLEAN =
            new Distribution<Boolean> ( "standard.Boolean" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final Boolean next (
                                       Random source
                                       )
            {
                return new Boolean ( source.nextBoolean () );
            }
        };

        /** Random byte [] values. */
        public static final Distribution<byte []> BYTE_ARRAY =
            new Distribution<byte []> ( "standard.bytes" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final byte [] next (
                                       Random source
                                       )
            {
                final byte [] chunk = new byte [ 8 ];
                source.nextBytes ( chunk );
                return chunk;
            }
        };

        /** Random Double values. */
        public static final Distribution<Double> DOUBLE =
            new Distribution<Double> ( "standard.double" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final Double next (
                                      Random source
                                      )
            {
                return new Double ( source.nextDouble () );
            }
        };

        /** Random Float values. */
        public static final Distribution<Float> FLOAT =
            new Distribution<Float> ( "standard.float" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final Float next (
                                     Random source
                                     )
            {
                return new Float ( source.nextFloat () );
            }
        };

        /** Random Double values over a Gaussian distribution (0-1). */
        public static final Distribution<Double> GAUSSIAN =
            new Distribution<Double> ( "gaussian.double" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final Double next (
                                      Random source
                                      )
            {
                return new Double ( source.nextGaussian () );
            }
        };

        /** Random Integer values. */
        public static final Distribution<Integer> INT =
            new Distribution<Integer> ( "standard.integer" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final Integer next (
                                       Random source
                                       )
            {
                return new Integer ( source.nextInt () );
            }
        };

        /** Random Long values. */
        public static final Distribution<Long> LONG =
            new Distribution<Long> ( "standard.long" )
        {
            /** The version of the parent module, YYYYMMDD format. */
            private static final long serialVersionUID =
                PseudoRandom.serialVersionUID;

            /**
             * @see musaico.foundation.term.generators.PseudoRandom.Distribution#next(java.util.Random)
             */
            @Override
            public final Long next (
                                    Random source
                                    )
            {
                return new Long ( source.nextLong () );
            }
        };


        // The name of this Distribution, such as "standard.BigDecimal"
        // or "gaussian.double" and so on.
        private final String name;


        /**
         * <p>
         * Creates a new Distribution.
         * </p>
         *
         * <p>
         * Protected.  For use by individual implementations
         * calling super ( ... ) during their constructors.
         * </p>
         */
        protected Distribution (
                String name
                )
            throws ParametersMustNotBeNull.Violation
        {
            this.name = name;
        }


        /**
         * @param generator The pseudo-random number generator which
         *                  will generate the next number.
         *                  Must not be null.
         *
         * @return The next generated number in the specified Random
         *         distribution.  Never null.
         */
        public abstract GENERATOR_NUMBER next (
                                               Random generator
                                               );


        /**
         * @return The name of this Distribution.  Never null.
         */
        public final String name ()
        {
            return this.name;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString ()
        {
            return ClassName.of ( this.getClass () )
                + " ( " + this.name + " )";
        }
    }




    /** Generates the pseudo-random numbers. */
    public static class NumberIterator<ITERATOR_NUMBER extends Object>
        implements Iterator<ITERATOR_NUMBER>
    {
        // Checks constructor obligations and guarantees for us.
        private static final Advocate classContracts =
            new Advocate ( PseudoRandom.NumberIterator.class );


        // The generator which produces our sequence over our distribution
        // for us.
        private final Random generator;

        // Which distribtion of numbers should we extract from the generator.
        private final PseudoRandom.Distribution<ITERATOR_NUMBER> distribution;

        /**
         * <p>
         * Creates a new PseudoRandom.Iterator, using the specified
         * random number generator to generate the sequence.
         * </p>
         *
         * @param distribution The distribution of random numbers to generate,
         *                     such as
         *                     <code> PseudoRandom.Distribution.INT </code> or
         *                     <code> PseudoRandom.Distribution.GAUSSIAN </code>
         *                     and so on.  Must not be null.
         *
         * @param seed The seed for the pseudo-random number generator.
         *             Must not be null.
         */
        public NumberIterator (
                PseudoRandom.Distribution<ITERATOR_NUMBER> distribution,
                Long seed
                )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   distribution, seed );

            this.distribution = distribution;
            this.generator = new Random ( seed.longValue () );
        }

        /**
         * <p>
         * Creates a new PseudoRandom.Iterator, using the specified
         * random number generator to generate the sequence.
         * </p>
         *
         * @param distribution The distribution of random numbers
         *                     to generate, such as
         *                     <code> PseudoRandom.Distribution.INT </code> or
         *                     <code> PseudoRandom.Distribution.GAUSSIAN </code>
         *                     and so on.  Must not be null.
         *
         * @param generator The pseudo-random number generator which
         *                  generates each number in the sequence.
         *                  Must not be null.
         */
        public NumberIterator (
                               PseudoRandom.Distribution<ITERATOR_NUMBER> distribution,
                               Random generator
                               )
            throws ParametersMustNotBeNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   distribution, generator );

            this.distribution = distribution;
            this.generator = generator;
        }

        /**
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public final boolean hasNext ()
        {
            return true;
        }


        /**
         * @see java.util.Iterator#next()
         */
        @Override
        public final ITERATOR_NUMBER next ()
            throws ReturnNeverNull.Violation
        {
            return this.distribution.next ( this.generator );
        }


        /**
         * @see java.util.Iterator#remove()
         */
        @Override
        public final void remove ()
            throws UnsupportedOperationException
        {
            throw new UnsupportedOperationException ( ClassName.of ( this.getClass () )
                                                      + ".remove () not supported" );
        }
    }




    // Checks constructor obligations and guarantees for us.
    private static final Advocate classContracts =
        new Advocate ( PseudoRandom.class );


    // Which distribution of numbers should we extract from the generator.
    private final PseudoRandom.Distribution<NUMBER> distribution;

    // The original seed for the pseudo-random number generator.
    private final Long seed;


    /**
     * <p>
     * Creates a new PseudoRandom number generator to generate the
     * specified distributions of random numbers.
     * </p>
     *
     * @param type The Type of number Terms to generate.
     *             Must not be null.
     *
     * @param distribution The distribution of random numbers
     *                     to generate, such as
     *                     <code> PseudoRandom.Distribution.INT </code> or
     *                     <code> PseudoRandom.Distribution.GAUSSIAN </code>
     *                     and so on.  Must not be null.
     */
    public PseudoRandom (
                         Type<NUMBER> type,
                         PseudoRandom.Distribution<NUMBER> distribution
                         )
        throws ParametersMustNotBeNull.Violation
    {
        this ( type,
               distribution,
               null ); // No seed, use the default.
    }


    /**
     * <p>
     * Creates a new PseudoRandom number generator to generate the
     * specified distributions of random numbers and the specified
     * initial seed.
     * </p>
     *
     * @param type The Type of number Terms to generate.
     *             Must not be null.
     *
     * @param distribution The distribution of random numbers
     *                     to generate, such as
     *                     <code> PseudoRandom.Distribution.INT </code> or
     *                     <code> PseudoRandom.Distribution.GAUSSIAN </code>
     *                     and so on.  Must not be null.
     *
     * @param seed The seed for the pseudo-random number generator,
     *             or null to use a default seed.  Can be null.
     */
    public PseudoRandom (
                         Type<NUMBER> type,
                         PseudoRandom.Distribution<NUMBER> distribution,
                         Long seed
                         )
        throws ParametersMustNotBeNull.Violation
    {
        super ( type );

        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               distribution );

        this.distribution = distribution;

        if ( seed == null )
        {
            this.seed = new Random ().nextLong ();
        }
        else
        {
            this.seed = seed;
        }
    }


    /**
     * @see musaico.foundation.term.TermPipeline.TermSink#duplicate()
     */
    @Override
    public final PseudoRandom<NUMBER> duplicate ()
        throws ReturnNeverNull.Violation
    {
        return new PseudoRandom<NUMBER> ( this.type (),      // type
                                          this.distribution, // distribution
                                          this.seed );       // seed
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals (
                           Object obj
                           )
    {
        if ( ! super.equals ( obj ) )
        {
            return false;
        }

        // We know that the specified object has the same type as us.
        final PseudoRandom<?> that = ( PseudoRandom<?> ) obj;

        if ( this.distribution == null )
        {
            if ( that.distribution != null )
            {
                // PseudoRandom ( null Distribution )
                //     != PseudoRandom ( any Distribution ).
                return false;
            }
        }
        else if ( that.distribution == null )
        {
            // PseudoRandom ( any Distribution )
            //     != PseudoRandom ( null Distribution ).
            return false;
        }
        else if ( ! this.distribution.equals ( that.distribution ) )
        {
            // PseudoRandom ( Distribution.X )
            //     != PseudoRandom ( Distribution.Y ).
            return false;
        }

        if ( this.seed == null )
        {
            if ( that.seed != null )
            {
                // PseudoRandom ( null seed ) != PseudoRandom ( any seed ).
                return false;
            }
        }
        else if ( that.seed == null )
        {
            // PseudoRandom ( any seed ) != PseudoRandom ( null seed ).
            return false;
        }
        else if ( ! this.seed.equals ( that.seed ) )
        {
            // PseudoRandom ( seed XL ) != PseudoRandom ( seed YL ).
            return false;
        }

        // Everything is all matchy-matchy.
        return true;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        int hash_code = super.hashCode () * 31;
        hash_code +=
            17 * this.distribution.getClass ().getName ().hashCode ();
        hash_code += this.seed.longValue ();

        return hash_code;
    }


    /**
     * <p>
     * Creates a new sequence of pseudo-random numbers.
     * </p>
     *
     * <p>
     * Note that the sequence will be identical to every other
     * sequence produced by this PseudoRandom, even if a null
     * seed was specified.  In order to generate a different
     * sequence, you'll have to create a new PseudoRandom
     * generator.
     * </p>
     *
     * @see musaico.foundation.term.Infinite#infiniteIterator()
     */
    @Override
    public Iterator<NUMBER> infiniteIterator ()
    {
        final PseudoRandom.NumberIterator<NUMBER> iterator =
            new PseudoRandom.NumberIterator<NUMBER> ( this.distribution,
                                                      this.seed );

        return iterator;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " ( " + this.distribution.name () + " )";
    }
}
