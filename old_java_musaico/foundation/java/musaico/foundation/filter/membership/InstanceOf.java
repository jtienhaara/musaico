package musaico.foundation.filter.membership;

import java.io.Serializable;

import java.util.Arrays;
import java.util.Collection;


import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.structure.ClassName;
import musaico.foundation.structure.StringRepresentation;


/**
 * <p>
 * Keeps all instances of specific class(es).
 * </p>
 *
 *
 * <p>
 * *** Do not forget to add useful new Filters to the classes in
 * *** musaico.foundation.contract.obligation
 * *** and musaico.foundation.contract.guarantee!
 * </p>
 *
 * <p>
 * In Java every Filter must be generic in order to
 * enable composability.
 * </p>
 *
 * <p>
 * In Java every Filter must be Serializable in order to play nicely
 * across RMI.
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
 * @see musaico.foundation.filter.membership.MODULE#COPYRIGHT
 * @see musaico.foundation.filter.membership.MODULE#LICENSE
 */
public class InstanceOf<GRAIN extends Object>
    implements Filter<GRAIN>, Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // The class whose instances will be KEPT.
    private final Class<?> [] classesToKeep;

    // Hash code generated once, at constructor time, from
    // the class names whose instances will be kept.
    private final int hashCode;


    /**
     * <p>
     * Creates a new InstanceOf filter.
     * </p>
     *
     * @param classes_to_keep The class(es) whose instances
     *                        will be kept by this filter.
     *                        If empty, then no objects will be
     *                        kept by this filter.  If any
     *                        elements of classes_to_keep are
     *                        null, they will be ignored.
     *                        If null, then Object.class is used
     *                        by default to keep all objects passed
     *                        through the filter.
     *                        (DO NOT PASS NULL.)
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Class [] -> Class<?> [],
                                                 // generic array creation.
    public InstanceOf (
            Class<?> ... classes_to_keep
            )
    {
        if ( classes_to_keep == null )
        {
            this.classesToKeep = (Class<?> [])
                new Class [] { Object.class };
            this.hashCode = ClassName.of ( Object.class ).hashCode ();
        }
        else
        {
            int num_classes = 0;
            int hash_code = 0;
            for ( Class<?> class_to_keep : classes_to_keep )
            {
                if ( class_to_keep == null )
                {
                    continue;
                }

                num_classes ++;
                hash_code += ClassName.of ( class_to_keep ).hashCode ();
            }

            this.hashCode = hash_code;

            this.classesToKeep = (Class<?> [])
                new Class [ num_classes ];
            if ( num_classes == classes_to_keep.length )
            {
                System.arraycopy ( classes_to_keep, 0,
                                   this.classesToKeep, 0, num_classes );
            }
            else
            {
                int c = 0;
                for ( Class<?> class_to_keep : classes_to_keep )
                {
                    if ( class_to_keep == null )
                    {
                        continue;
                    }

                    this.classesToKeep [ c ] = class_to_keep;
                    c ++;
                }
            }
        }
    }


    /**
     * <p>
     * Creates a new InstanceOf filter.
     * </p>
     *
     * @param classes_to_keep The class(es) whose instances
     *                        will be kept by this filter.
     *                        If empty, then no objects will be
     *                        kept by this filter.  If any
     *                        elements of classes_to_keep are
     *                        null, they will be ignored.
     *                        If null, then Object.class is used
     *                        by default to keep all objects passed
     *                        through the filter.
     *                        (DO NOT PASS NULL.)
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Class [] -> Class<?> [],
                                                 // generic array creation.
    public InstanceOf (
            Collection<Class<?>> classes_to_keep
            )
    {
        if ( classes_to_keep == null )
        {
            this.classesToKeep = (Class<?> [])
                new Class [] { Object.class };
            this.hashCode = ClassName.of ( Object.class ).hashCode ();
        }
        else
        {
            int num_classes = 0;
            int hash_code = 0;
            for ( Class<?> class_to_keep : classes_to_keep )
            {
                if ( class_to_keep == null )
                {
                    continue;
                }

                num_classes ++;
                hash_code += ClassName.of ( class_to_keep ).hashCode ();
            }

            this.hashCode = hash_code;

            if ( num_classes == classes_to_keep.size () )
            {
                final Class<?> [] template = (Class<?> [])
                    new Class [ num_classes ];
                this.classesToKeep = classes_to_keep.toArray ( template );
            }
            else
            {
                this.classesToKeep = (Class<?> [])
                    new Class [ num_classes ];
                int c = 0;
                for ( Class<?> class_to_keep : classes_to_keep )
                {
                    if ( class_to_keep == null )
                    {
                        continue;
                    }

                    this.classesToKeep [ c ] = class_to_keep;
                    c ++;
                }
            }
        }
    }


    /**
     * <p>
     * Creates a new InstanceOf filter.
     * </p>
     *
     * @param classes_to_keep The class(es) whose instances
     *                        will be kept by this filter.
     *                        If empty, then no objects will be
     *                        kept by this filter.  If any
     *                        elements of classes_to_keep are
     *                        null, they will be ignored.
     *                        If null, then Object.class is used
     *                        by default to keep all objects passed
     *                        through the filter.
     *                        (DO NOT PASS NULL.)
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Class [] -> Class<?> [],
                                                 // generic array creation.
    public InstanceOf (
            Iterable<Class<?>> classes_to_keep
            )
    {
        if ( classes_to_keep == null )
        {
            this.classesToKeep = (Class<?> [])
                new Class [] { Object.class };
            this.hashCode = ClassName.of ( Object.class ).hashCode ();
        }
        else
        {
            int num_classes = 0;
            int hash_code = 0;
            for ( Class<?> class_to_keep : classes_to_keep )
            {
                if ( class_to_keep == null )
                {
                    continue;
                }

                num_classes ++;
                hash_code += ClassName.of ( class_to_keep ).hashCode ();
            }

            this.hashCode = hash_code;

            this.classesToKeep = (Class<?> [])
                new Class [ num_classes ];
            int c = 0;
            for ( Class<?> class_to_keep : classes_to_keep )
            {
                if ( class_to_keep == null )
                {
                    continue;
                }

                this.classesToKeep [ c ] = class_to_keep;
                c ++;
            }
        }
    }


    /**
     * @return The Class(es) whose instances will be kept by this filter.
     *         A defensive copy is returned, so the caller can do
     *         whatever you want with the result.  Never null.
     *         Never contains any null elements.
     */
    @SuppressWarnings({"rawtypes", "unchecked"}) // Class [] -> Class<?> [],
                                                 // generic array creation.
    public final Class<?> [] classesToKeep ()
    {
        final Class<?> [] classes_to_keep = (Class<?> [])
            new Class [ this.classesToKeep.length ];
        System.arraycopy ( this.classesToKeep, 0,
                           classes_to_keep, 0, this.classesToKeep.length );
        return classes_to_keep;
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
        else if ( this.getClass () != object.getClass () )
        {
            return false;
        }

        final InstanceOf<?> that = (InstanceOf<?>) object;
        final Class<?> [] this_classes = this.classesToKeep;
        final Class<?> [] that_classes = that.classesToKeep;
        if ( ! Arrays.equals ( this_classes, that_classes ) )
        {
            return false;
        }

        return true;
    }


    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public final FilterState filter (
            GRAIN grain
            )
    {
        if ( grain == null )
        {
            return FilterState.DISCARDED;
        }

        for ( Class<?> class_to_keep : this.classesToKeep )
        {
            if ( class_to_keep.isInstance ( grain ) )
            {
                return FilterState.KEPT;
            }
        }

        return FilterState.DISCARDED;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode ()
    {
        return this.hashCode;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return ClassName.of ( this.getClass () )
            + " [ "
            + StringRepresentation.of ( this.classesToKeep,
                                        StringRepresentation.DEFAULT_ARRAY_LENGTH )
            + " ]";
    }
}
