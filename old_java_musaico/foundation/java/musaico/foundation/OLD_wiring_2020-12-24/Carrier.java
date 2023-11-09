package musaico.foundation.wiring;

import java.lang.reflect.Array;

import musaico.foundation.structure.StringRepresentation;

public interface Carrier
    extends Tagged<Carrier>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public abstract <DATA extends Object> DATA [] data (
            Class<DATA> data_class,
            DATA [] default_value
            );
}
