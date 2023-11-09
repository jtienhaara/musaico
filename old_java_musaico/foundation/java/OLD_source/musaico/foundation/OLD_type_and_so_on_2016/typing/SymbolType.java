package musaico.foundation.typing;


/**
 * <p>
 * The root of every Type of symbol (OperationType, ConstraintType,
 * and so on).  Makes things easier for Namespace.symbolTypes ()
 * (avoids using Type&lt;Generic&gt;, which gets messy quickly).
 * </p>
 *
 *
 * <p>
 * In Java every Symbol must be Serializable in order to play
 * nicely with RMI.
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
public interface SymbolType
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** No SymbolType at all.  Warning: cannot be cast to
     *  Type&lt;? extends Symbol&gt;. */
    public static final NoSymbolType NONE = new NoSymbolType ();


    /**
     * @return This SymbolType as a Type<? extends Symbol>.  Never null.
     */
    public abstract Type<? extends Symbol> asType ();
}
