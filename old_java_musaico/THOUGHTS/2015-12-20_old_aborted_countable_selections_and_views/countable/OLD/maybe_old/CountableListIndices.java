package musaico.foundation.value;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;
import musaico.foundation.contract.Violation;

import musaico.foundation.contract.obligations.Parameter1;
import musaico.foundation.contract.obligations.Parameter2;
import musaico.foundation.contract.obligations.Parameter3;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.contract.guarantees.Return;
import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.domains.BoundedDomain;

import musaico.foundation.filter.Filter;

import musaico.foundation.order.Order;


/**
 * <p>
 * A mutable offshoot from a Countable value, which can be used to filter,
 * sort, replace, and so on, and build new Countable values.
 * </p>
 *
 * <p>
 * A read-only view of a Countable value, which can be used to filter,
 * find and so on the indices of a Countable value, but will never
 * change the elements.
 * </p>
 *
 *
 * <p>
 * In Java every CountableIndices must be Serializable in order to
 * play nicely across RMI.  However users of the CountableIndices
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
 * @see musaico.foundation.value.MODULE#COPYRIGHT
 * @see musaico.foundation.value.MODULE#LICENSE
 */
public class CountableListIndices<VALUE extends Object>
    extends AbstractCountableListView<VALUE, Long, CountableIndices<VALUE>>
    implements CountableIndices<VALUE>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Enforces static parameter obligations and so on for us.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( CountableListIndices.class );


    /**
     * <p>
     * Creates a new CountableListIndices which will work on the specified
     * Countable and elements.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable indices.  Must not be null.
     *
     * @param elements The 0 or more elements of this countable indices.
     *                 Must not be null.  Must not contain any null elements.
     */
    public CountableListIndices (
                                 Countable<VALUE> countable,
                                 List<VALUE> elements
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( countable,
                Long.class,
                elements );
    }


    /**
     * <p>
     * Creates a new CountableListIndices which will work on the specified
     * Countable and single element.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable indices.  Must not be null.
     *
     * @param element The one element of this countable indices.
     *                Must not be null.
     */
    public CountableListIndices (
                                 Countable<VALUE> countable,
                                 VALUE element
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( countable,
                Long.class,
                element );
    }


    /**
     * <p>
     * Creates a new CountableListIndices which will work on the specified
     * Countable and elements.
     * </p>
     *
     * @param countable The original Countable value which was used to
     *                  create this countable indices.  Must not be null.
     *
     * @param elements The 0 or more elements of this countable indices.
     *                 Must not be null.  Must not contain any null elements.
     *
     * @param none_violation The initial "no value" violation.
     *                       Can be null for default.
     */
    public CountableListIndices (
                                 Countable<VALUE> countable,
                                 List<VALUE> elements,
                                 ValueViolation none_violation
                                 )
        throws ParametersMustNotBeNull.Violation,
               Parameter2.MustContainNoNulls.Violation
    {
        super ( countable,
                Long.class,
                elements,
                none_violation );
    }




    /**
     * @see musaico.foundation.value.CountableIndices#all ()
     */
    @Override
    public Countable<Long> all ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.All.CONTRACT,
                                         (Abnormal<VALUE>) this.originalValue );
        }

        final List<Long> indices = this.viewItems ();

        final Countable<Long> all_indices =
            this.createCountable ( indices,
                                   CountableIndices.MustNotBeEmpty.CONTRACT,
                                   0L );

        return all_indices;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#clamp(musaico.foundation.value.Countable)
     */
    @Override
    public Countable<Long> clamp (
                                  Countable<Long> indices
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.Clamp.CONTRACT,
                                         (Abnormal<?>) this.originalValue );
        }
        else if ( indices instanceof Abnormal )
        {
            return indices;
        }

        if ( indices instanceof Unjust )
        {
            // No indices.
            return indices;
        }

        final List<Long> clamped_indices =
            new ArrayList<Long> ();

        synchronized ( this.lock )
        {
            final int num_elements = this.elements.size ();
            for ( long index : indices )
            {
                final int array_index = this.clampIndex ( index );
                if ( array_index >= 0 )
                {
                    clamped_indices.add ( (long) array_index );
                }
            }
        }

        final Countable<Long> countable_clamped_indices =
            this.createCountable ( clamped_indices,
                                   CountableIndices.MustHaveClampedIndices.CONTRACT,
                                   indices );

        return countable_clamped_indices;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#contains(musaico.foundation.value.Countable)
     */
    @Override
    public boolean contains (
                             Countable<Long> indices
                             )
        throws ParametersMustNotBeNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               indices );

        if ( this.originalValue instanceof Abnormal )
        {
            return false;
        }
        else if ( indices instanceof Abnormal )
        {
            return false;
        }
        else if ( indices instanceof No )
        {
            return true;
        }

        synchronized ( this.lock )
        {
            for ( long index : indices )
            {
                if ( this.clampIndex ( index ) < 0 )
                {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * @see musaico.foundation.value.CountableView#duplicate()
     */
    @Override
    public CountableIndices<VALUE> duplicate ()
    {
        final CountableListIndices<VALUE> new_indices;
        synchronized ( this.lock )
        {
            new_indices =
                new CountableListIndices<VALUE> ( this.originalValue,
                                                  this.elements,
                                                  this.noneViolation );
        }

        return new_indices;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#filter(musaico.foundation.filter.Filter)
     */
    @Override
    public Countable<Long> filter (
                                   Filter<VALUE> filter
                                   )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               filter );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.FilterAll.CONTRACT,
                                         (Abnormal<?>) this.originalValue );
        }

        final List<Long> filtered_indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            for ( int index = 0; index < elements.size (); index ++ )
            {
                final VALUE element = this.elements.get ( index );
                if ( filter.filter ( element ).isKept () )
                {
                    filtered_indices.add ( (long) index );
                }
            }
        }

        final Countable<Long> indices =
            this.createCountable ( filtered_indices,
                                   CountableIndices.MustMatchFilter.CONTRACT,
                                   filter );

        return indices;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#filterFirst(musaico.foundation.filter.Filter)
     */
    @Override
        public ZeroOrOne<Long> filterFirst (
                                            Filter<VALUE> filter
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               filter );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.FilterFirst.CONTRACT,
                                     this.originalValue );
        }

        final List<Long> filtered_indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            for ( int index = 0; index < elements.size (); index ++ )
            {
                final VALUE element = this.elements.get ( index );
                if ( filter.filter ( element ).isKept () )
                {
                    filtered_indices.add ( (long) index );
                    break;
                }
            }
        }

        final ZeroOrOne<Long> first_index =
            this.createZeroOrOne ( filtered_indices,
                                   CountableIndices.MustMatchFirstFilter.CONTRACT,
                                   filter );

        return first_index;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#filterLast(musaico.foundation.filter.Filter)
     */
    @Override
    public ZeroOrOne<Long> filterLast (
                                       Filter<VALUE> filter
                                       )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               filter );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.FilterLast.CONTRACT,
                                     this.originalValue );
        }

        final List<Long> filtered_indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            for ( int index = this.elements.size () - 1;
                  index >= 0;
                  index -- )
            {
                final VALUE element = this.elements.get ( index );
                if ( filter.filter ( element ).isKept () )
                {
                    filtered_indices.add ( (long) index );
                    break;
                }
            }
        }

        final ZeroOrOne<Long> last_index =
            this.createZeroOrOne ( filtered_indices,
                                   CountableIndices.MustMatchLastFilter.CONTRACT,
                                   filter );

        return last_index;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#find(musaico.foundation.value.Countable)
     */
    @Override
    public Countable<Long> find (
                                 Countable<VALUE> sub_value
                                 )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.Find.CONTRACT,
                                         (Abnormal<?>) this.originalValue );
        }
        else if ( sub_value instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.Find.CONTRACT,
                                         (Abnormal<?>) sub_value );
        }

        final Iterator<VALUE> sub_iterator = sub_value.iterator ();
        if ( ! sub_iterator.hasNext () )
        {
            return this.createNone ( CountableIndices.MustMatchFind.CONTRACT,
                                     sub_value );
        }

        final List<Long> indices =
            new ArrayList<Long> ();
        int last_num_found = 0;
        int last_index = -1;
        final VALUE first_element = sub_iterator.next ();
        synchronized ( this.lock )
        {
            int start_index = 0;
            while ( start_index >= 0 )
            {
                start_index =
                    this.indexOf ( first_element, start_index );
                if ( start_index < 0 )
                {
                    break;
                }

                int num_found = 1;
                int end_index = start_index;
                for ( int seq = 0; seq < last_num_found; seq ++ )
                {
                    end_index ++;
                    if ( this.elements.get ( last_index + seq )
                         .equals ( this.elements.get ( end_index ) ) )
                    {
                        num_found ++;
                    }
                    else
                    {
                        break;
                    }
                }

                if ( num_found >= last_num_found )
                {
                    last_index = start_index;
                    boolean is_found_all_remaining_elements = true;
                    boolean is_end = false;
                    while ( sub_iterator.hasNext () )
                    {
                        final VALUE sub_element = sub_iterator.next ();
                        final int index = this.indexOf ( sub_element,
                                                         end_index + 1 );
                        if ( index < 0 )
                        {
                            is_end = true;
                            break;
                        }
                        else if ( index > ( end_index + 1 ) )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }

                        end_index = index;
                        num_found ++;
                    }

                    if ( is_end )
                    {
                        break;
                    }
                    else if ( is_found_all_remaining_elements )
                    {
                        indices.add ( (long) start_index );
                    }

                    last_num_found = num_found;
                }

                start_index ++;
            }
        }

        final Countable<Long> found_all_indices =
            this.createCountable ( indices,
                                   CountableIndices.MustMatchFind.CONTRACT,
                                   sub_value );

        return found_all_indices;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#findFirst(musaico.foundation.value.Countable)
     */
    @Override
    public ZeroOrOne<Long> findFirst (
                                      Countable<VALUE> sub_value
                                      )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustMatchFindFirst.CONTRACT,
                                     this.originalValue );
        }
        else if ( sub_value instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustMatchFindFirst.CONTRACT,
                                     sub_value );
        }

        final Iterator<VALUE> sub_iterator = sub_value.iterator ();
        if ( ! sub_iterator.hasNext () )
        {
            return this.createNone ( CountableIndices.MustMatchFindFirst.CONTRACT,
                                     sub_value );
        }

        int last_num_found = 0;
        int last_index = -1;
        final VALUE first_element = sub_iterator.next ();
        final List<Long> indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            int start_index = 0;
            while ( start_index >= 0 )
            {
                start_index =
                    this.indexOf ( first_element, start_index );
                if ( start_index < 0 )
                {
                    break;
                }

                int num_found = 1;
                int end_index = start_index;
                for ( int seq = 0; seq < last_num_found; seq ++ )
                {
                    end_index ++;
                    if ( this.elements.get ( last_index + seq )
                         .equals ( this.elements.get ( end_index ) ) )
                    {
                        num_found ++;
                    }
                    else
                    {
                        break;
                    }
                }

                if ( num_found >= last_num_found )
                {
                    last_index = start_index;
                    boolean is_found_all_remaining_elements = true;
                    boolean is_end = false;
                    while ( sub_iterator.hasNext () )
                    {
                        final VALUE sub_element = sub_iterator.next ();
                        final int index = this.indexOf ( sub_element,
                                                         end_index + 1 );
                        if ( index < 0 )
                        {
                            is_end = true;
                            break;
                        }
                        else if ( index > ( end_index + 1 ) )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }

                        end_index = index;
                        num_found ++;
                    }

                    if ( is_end )
                    {
                        break;
                    }
                    else if ( is_found_all_remaining_elements )
                    {
                        indices.add ( (long) start_index );
                        break;
                    }

                    last_num_found = num_found;
                }

                start_index ++;
            }
        }

        final ZeroOrOne<Long> first_found_index =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustMatchFindFirst.CONTRACT,
                                   sub_value );

        return first_found_index;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#findLast(musaico.foundation.value.Countable)
     */
    @Override
    public ZeroOrOne<Long> findLast (
                                     Countable<VALUE> sub_value
                                     )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               sub_value );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustMatchFindLast.CONTRACT,
                                     this.originalValue );
        }
        else if ( sub_value instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustMatchFindLast.CONTRACT,
                                     sub_value );
        }

        final Iterator<VALUE> sub_iterator = sub_value.iterator ();
        if ( ! sub_iterator.hasNext () )
        {
            return this.createNone ( CountableIndices.MustMatchFindLast.CONTRACT,
                                     sub_value );
        }

        int last_num_found = 0;
        int last_index = -1;
        final VALUE first_element = sub_iterator.next ();
        final List<Long> indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            int start_index = this.elements.size () - 1;
            while ( start_index >= 0 )
            {
                start_index =
                    this.lastIndexOf ( first_element, start_index );
                if ( start_index < 0 )
                {
                    break;
                }

                int num_found = 1;
                int end_index = start_index;
                for ( int seq = 0; seq < last_num_found; seq ++ )
                {
                    end_index ++;
                    if ( this.elements.get ( last_index + seq )
                         .equals ( this.elements.get ( end_index ) ) )
                    {
                        num_found ++;
                    }
                    else
                    {
                        break;
                    }
                }

                if ( num_found >= last_num_found )
                {
                    last_index = start_index;
                    boolean is_found_all_remaining_elements = true;
                    boolean is_end = false;
                    while ( sub_iterator.hasNext () )
                    {
                        final VALUE sub_element = sub_iterator.next ();
                        final int index = this.indexOf ( sub_element,
                                                         end_index + 1 );
                        if ( index < 0 )
                        {
                            is_end = true;
                            break;
                        }
                        else if ( index > ( end_index + 1 ) )
                        {
                            is_found_all_remaining_elements = false;
                            break;
                        }

                        end_index = index;
                        num_found ++;
                    }

                    if ( is_end )
                    {
                        break;
                    }
                    else if ( is_found_all_remaining_elements )
                    {
                        indices.add ( (long) start_index );
                        break;
                    }

                    last_num_found = num_found;
                }

                start_index --;
            }
        }

        final ZeroOrOne<Long> last_found_index =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustMatchFindLast.CONTRACT,
                                   sub_value );

        return last_found_index;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#first()
     */
    @Override
    public ZeroOrOne<Long> first ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.First.CONTRACT,
                                     this.originalValue );
        }

        final List<Long> indices = new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            if ( this.elements.size () > 0 )
            {
                indices.add ( 0L );
            }
        }

        final ZeroOrOne<Long> first =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustHaveFirst.CONTRACT,
                                   0L );


        return first;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#firstAndLast()
     */
    @Override
    public Countable<Long> firstAndLast ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.FirstAndLast.CONTRACT,
                                         (Abnormal<?>) this.originalValue );
        }

        final List<Long> indices = new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            final int size = this.elements.size ();
            if ( size > 0 )
            {
                indices.add ( 0L );
                indices.add ( (long) size - 1 );
            }
        }

        final Countable<Long> first_and_last =
            this.createCountable ( indices,
                                   CountableIndices.MustHaveFirstAndLast.CONTRACT,
                                   0L );


        return first_and_last;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#index(long)
     */
    @Override
    public ZeroOrOne<Long> index (
                                  long index
                                  )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustHaveIndex.CONTRACT,
                                     this.originalValue );
        }
        else if ( index < 0L )
        {
            return this.createNone ( CountableIndices.MustHaveIndex.CONTRACT,
                                     index );
        }

        final List<Long> indices = new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            final int array_index = this.clampIndex ( index );
            if ( array_index >= 0 )
            {
                indices.add ( (long) array_index );
            }
        }

        final ZeroOrOne<Long> clamped =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustHaveIndex.CONTRACT,
                                   index );


        return clamped;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#instancesOf(java.lang.Class)
     */
    @Override
    public Countable<Long> instancesOf (
                                        Class<? extends VALUE> instance_of_class
                                        )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               instance_of_class );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.InstancesOf.CONTRACT,
                                         (Abnormal<?>) this.originalValue );
        }

        final List<Long> indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            for ( int index = 0; index < this.elements.size (); index ++ )
            {
                final VALUE element = this.elements.get ( index );
                if ( instance_of_class.isInstance ( element ) )
                {
                    indices.add ( (long) index );
                }
            }
        }

        final Countable<Long> found_instances =
            this.createCountable ( indices,
                                   CountableIndices.MustHaveInstancesOf.CONTRACT,
                                   instance_of_class );

        return found_instances;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#instanceOfFirst(java.lang.Class)
     */
    @Override
    public ZeroOrOne<Long> instanceOfFirst (
                                            Class<? extends VALUE> instance_of_class
                                            )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               instance_of_class );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustHaveFirstInstanceOf.CONTRACT,
                                     this.originalValue );
        }

        final List<Long> indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            for ( int index = 0; index < this.elements.size (); index ++ )
            {
                final VALUE element = this.elements.get ( index );
                if ( instance_of_class.isInstance ( element ) )
                {
                    indices.add ( (long) index );
                    break;
                }
            }
        }

        final ZeroOrOne<Long> found_instances =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustHaveFirstInstanceOf.CONTRACT,
                                   instance_of_class );

        return found_instances;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#instanceOfLast(java.lang.Class)
     */
    @Override
    public ZeroOrOne<Long> instanceOfLast (
                                           Class<? extends VALUE> instance_of_class
                                           )
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               instance_of_class );

        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.MustHaveLastInstanceOf.CONTRACT,
                                     this.originalValue );
        }

        final List<Long> indices =
            new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            for ( int index = this.elements.size () - 1; index >= 0; index -- )
            {
                final VALUE element = this.elements.get ( index );
                if ( instance_of_class.isInstance ( element ) )
                {
                    indices.add ( (long) index );
                    break;
                }
            }
        }

        final ZeroOrOne<Long> found_instances =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustHaveLastInstanceOf.CONTRACT,
                                   instance_of_class );

        return found_instances;
    }


    /**
     * @see musaico.foundation.value.CountableIndices#last()
     */
    @Override
    public ZeroOrOne<Long> last ()
        throws ParametersMustNotBeNull.Violation,
               ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createNone ( CountableIndices.Last.CONTRACT,
                                     this.originalValue );
        }

        final List<Long> indices = new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            final int size = this.elements.size ();
            if ( size > 0 )
            {
                indices.add ( (long) size - 1L );
            }
        }

        final ZeroOrOne<Long> last =
            this.createZeroOrOne ( indices,
                                   CountableIndices.MustHaveLast.CONTRACT,
                                   0L );


        return last;
    }



    /**
     * @see musaico.foundation.value.CountableIndices#lastAndFirst()
     */
    @Override
    public Countable<Long> lastAndFirst ()
        throws ReturnNeverNull.Violation
    {
        if ( this.originalValue instanceof Abnormal )
        {
            return this.createAbnormal ( CountableIndices.LastAndFirst.CONTRACT,
                                         (Abnormal<?>) this.originalValue );
        }

        final List<Long> indices = new ArrayList<Long> ();
        synchronized ( this.lock )
        {
            final int size = this.elements.size ();
            if ( size > 0 )
            {
                indices.add ( (long) size - 1L );
                indices.add ( 0L );
            }
        }

        final Countable<Long> last_and_first =
            this.createCountable ( indices,
                                   CountableIndices.MustHaveLastAndFirst.CONTRACT,
                                   0L );

        return last_and_first;
    }


    /**
     * @see musaico.foundation.value.AbstractCountableListView#viewItems()
     */
    @Override
    protected List<Long> viewItems ()
    {
        final long length;
        synchronized ( this.lock )
        {
            length = (long) this.elements.size ();
        }

        final List<Long> indices =
            new ArrayList<Long> ();
        for ( long index = 0L; index < length; index ++ )
        {
            indices.add ( index );
        }

        return indices;
    }
}
