package musaico.foundation.typing;

import java.io.Serializable;


import musaico.foundation.contract.CheckedViolation;
import musaico.foundation.contract.Contract;
import musaico.foundation.contract.Contracts;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.obligations.Parameter7;
import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.domains.InstanceOfClass;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.value.Finite;
import musaico.foundation.value.Just;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueBuilder;
import musaico.foundation.value.ValueViolation;


/**
 * </p>
 *  settings for name, namespace, none and valueClass.
 *  For example none must be an instance of the valueClass,
 *  and the parent namespace must have neither a Type
 *  of the same name registered, nor a Type of the same
 * </p>
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TypeMustBeValid
    extends AbstractTypingContract<TypeBuilder<?>>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static final TypeMustBeValid CONTRACT =
        new TypeMustBeValid ();

    /**
     * @see musaico.foundation.filter.Filter#filter(java.lang.Object)
     */
    @Override
    public FilterState filter (
                               TypeBuilder<?> type_builder
                               )
    {
        if ( type_builder == null )
        {
            // Null TypeBuilder is certainly not valid.
            return FilterState.DISCARDED;
        }

        final String raw_type_name = type_builder.rawTypeName ();
        final String tag_names = type_builder.tagNames ();
        final Visibility visibility = type_builder.visibility ();
        final Namespace namespace = type_builder.namespace ();
        final Object none = type_builder.none ();
        final Class<?> value_class = type_builder.valueClass ();

        final TypeID type_id = new TypeID ( raw_type_name,
                                            tag_names,
                                            visibility );
        if ( namespace.containsSymbol ( type_id ) )
        {
            // This TypeID already exists in the parent namespace.
            return FilterState.DISCARDED;
        }
        else if ( ! value_class.isInstance ( none ) )
        {
            // The "none" object has to be an instance of the value class.
            return FilterState.DISCARDED;
        }

        // All clear to build the type.
        return FilterState.KEPT;
    }
}
