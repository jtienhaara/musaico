package musaico.foundation.typing;

import java.io.Serializable;


/**
 * <p>
 * Useful TypeID constants.
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
 * @see musaico.foundation.typing.MODULE#COPYRIGHT
 * @see musaico.foundation.typing.MODULE#LICENSE
 */
public class TypeIDs
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    /** The raw, un-Tagged Type of a Type which might or might not
     *  be Tagged.  For example the ROOT of Type "string" would be
     *  itself, whereas the ROOT of Type "string[hexadecimal][uppercase]"
     *  would be the "string" Type. */
    public static final TypeID ROOT_TYPE =
        new TypeID ( "#root_type",         // raw_type_name
                     "",                   // tag_names
                     Visibility.PRIVATE ); // visibility

    /** The unique ID of SymbolType.NONE. */
    public static final TypeID NO_SYMBOL_TYPE =
        new TypeID ( "nosymboltype",         // raw_type_name
                     "",                     // tag_names
                     Visibility.INVISIBLE ); // visibility
}
