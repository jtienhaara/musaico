package musaico.build.classweb;

import java.io.Serializable;

import java.util.List;


/**
 * <p>
 * An aspect of a class, such as its package, whether it is a class
 * or interface, the enumerated values if it is an enum, and so on.
 * </p>
 */
public interface ClassDetail<DETAIL extends Object>
    extends ClassDetailPrinter<DETAIL>
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;


    /** Returns the enumerated values of each enum class. */
    public static final ClassEnumeratedValues ENUMERATED_VALUES =
        new ClassEnumeratedValues ();

    /** Returns the inherited classes and interfaces of each class. */
    public static final ClassInheritance INHERITANCE =
        new ClassInheritance ();

    /** Returns all public constants, constructors and methods
     *  from each class. */
    public static final ClassMembers MEMBERS =
        new ClassMembers ( Order.DEFAULT );

    /** Returns the name of each class. */
    public static final ClassName NAME =
        new ClassName ();

    /** Returns the package of a class. */
    public static final ClassPackage PACKAGE =
        new ClassPackage ();

    /** Returns the stereotype of a class, such as <<interface>>
     *  or <<abstract>> and so on. */
    public static final ClassStereotype STEREOTYPE =
        new ClassStereotype ();


    /** All class detail types. */
    public static final ClassDetail<?> [] ALL =
        new ClassDetail<?> []
        {
            ClassDetail.ENUMERATED_VALUES,
            ClassDetail.INHERITANCE,
            ClassDetail.MEMBERS,
            ClassDetail.NAME,
            ClassDetail.PACKAGE,
            ClassDetail.STEREOTYPE
        };


    /**
     * @return The list of zero or more details from the specified
     *         class or interface.  Typically one ClassDetail returns
     *         one set of details.  However a composite ClassDetail
     *         (such as ClassIf) can return multiple sets of details.
     *         Never null.  Never contains any null elements.
     */
    public abstract List<ClassDetails<DETAIL>> fromClass (
            Class<?> class_or_interface
            );

    /**
     * @return True if this ClassDetail type equals the specified one
     *         or if it is a composite and contains it; false if not.
     */
    public abstract boolean isOrHas (
                                     ClassDetail<?> that
                                     );

    /**
     * <p>
     * Replaces this ClassDetail and any child or grandchild
     * that equals the specified replaced ClassDetail type,
     * replacing each one with the specified replacement
     * ClassDetail type instead.  If no changes are made, this
     * ClassDetail is returned as-is.
     * </p>
     */
    public abstract ClassDetail<?> replaceAll (
                                               ClassDetail<?> replaced,
                                               ClassDetail<?> replacement
                                               );
}
