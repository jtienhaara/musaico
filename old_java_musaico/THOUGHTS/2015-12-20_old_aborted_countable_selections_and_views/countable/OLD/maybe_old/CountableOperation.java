package !!!;


/**
 * <p>
 * !!!
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
public !!!
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    public abstract <VALUE extends Object, VIEW_ITEM extends Object, VIEW extends CountableView<VALUE, VIEW_ITEM, VIEW>>
        CountableOperation.State execute (
                                          VIEW view,
                                          List<VALUE> immmutable_elements,
                                          list<VIEW_ITEM> mutable_view_items,
                                          long start_index,
                                          long end_index
                                          );
}
