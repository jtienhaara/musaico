package musaico.foundation.typing;

import java.io.Serializable;

import java.util.Stack;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


/**
 * <p>
 * Walks through a namespace hierarchy, visiting each namespace
 * to perform some task at each level.
 * </p>
 *
 *
 * <p>
 * In Java every NamespaceWalker must be Serializable in order to
 * play nicely with RMI.
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
public class NamespaceWalker
    implements Serializable
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    // Checks constructor and static method obligations.
    private static final ObjectContracts classContracts =
        new ObjectContracts ( NamespaceWalker.class );


    // Checks method obligations and guarantees.
    private final ObjectContracts contracts;

    // The visitor we hand off to at each Namespace in the hierarchy.
    private final NamespaceVisitor visitor;


    /**
     * <p>
     * Creates a new NamespaceWalker that will tell the specified visitor
     * to visit each Namespace in the hierarchy, interrupted only if
     * the visitor decides not to CONTINUE walking through the hierarchy
     * (for example POP or ABORT).
     * </p>
     *
     * @param visitor The NamespaceVisitor which will visit each
     *                Namespace in the hierarchy, including the first
     *                (top) one.  Must not be null.
     */
    public NamespaceWalker (
                            NamespaceVisitor visitor
                            )
        throws ParametersMustNotBeNull.Violation
    {
        classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                               visitor );

        this.visitor = visitor;

        this.contracts = new ObjectContracts ( this );
    }


    /**
     * <p>
     * Walks through the tree of Namespaces rooted at the specified
     * Namespace.
     * </p>
     *
     * @param namespace The root Namespace from which to start walking
     *                  through the tree.  Must not be null.
     *
     * @return The last visit status (such as CONTINUE, POP or ABORT)
     *         returned by the visitor.  Never null.
     */
    public NamespaceVisitor.VisitStatus walk (
                                              Namespace top_namespace
                                              )
    {
        this.contracts.check ( ParametersMustNotBeNull.CONTRACT,
                               top_namespace );

        System.out.println("!!! walker 1 " + top_namespace );
        final Stack<Namespace> namespaces_to_visit =
            new Stack<Namespace> ();

        System.out.println("!!! walker 2" );
        namespaces_to_visit.push ( top_namespace );

        System.out.println("!!! walker 3" );
        NamespaceVisitor.VisitStatus status =
            NamespaceVisitor.VisitStatus.CONTINUE;
        while ( ! namespaces_to_visit.empty () )
        {
        System.out.println("!!! walker 4" );
            final Namespace namespace = namespaces_to_visit.pop ();

        System.out.println("!!! walker 5 " + namespace );
            status = this.visitor.visit ( namespace );

        System.out.println("!!! walker 6 " + status );
            if ( status == NamespaceVisitor.VisitStatus.ABORT )
            {
        System.out.println("!!! walker 7 abort" );
                break;
            }
            else if ( status == NamespaceVisitor.VisitStatus.POP )
            {
        System.out.println("!!! walker 8 pop" );
                continue;
            }

        System.out.println("!!! walker 9" );
            final int insert_position = namespaces_to_visit.size ();
        System.out.println("!!! walker 10" );
        /* !!!
            for ( Namespace child_namespace
                      : namespace.symbols ( Namespace.TYPE ) )
            {
        System.out.println("!!! walker 11" + child_namespace );
                // Make sure the first child namespace is the first
                // one we pop.
                namespaces_to_visit.add ( insert_position, child_namespace );
        System.out.println("!!! walker 12" );
            }
        System.out.println("!!! walker 13" );
        }
        !!! */

            for ( SymbolID<Namespace> child_namespace_id
                      : namespace.symbolIDs ( Namespace.TYPE ) )
            {
                System.out.println("!!! walker 11a" + child_namespace_id + " visibility: " + child_namespace_id.visibility () );
                if ( NamespaceID.PARENT.equals ( child_namespace_id )
                     || NamespaceID.ROOT.equals ( child_namespace_id )
                     || NamespaceID.NONE.equals ( child_namespace_id ) )
                {
                    // Skip these "special" namespace references.
                    continue;
                }

                final Namespace child_namespace =
                    namespace.symbol ( child_namespace_id ).orNull ();
                if ( child_namespace == null )
                {
                    System.err.println ( "NamespaceWalker: "
                                         + "namespace " + namespace
                                         + " has unexpected null child"
                                         + " namespace '"
                                         + child_namespace_id
                                         + "'; skipping." );
                    continue;
                }

        System.out.println("!!! walker 11" + child_namespace );
                // Make sure the first child namespace is the first
                // one we pop.
                namespaces_to_visit.add ( insert_position, child_namespace );
        System.out.println("!!! walker 12" );
            }
        System.out.println("!!! walker 13" );
        }

        System.out.println("!!! walker 14" );
        return status;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString ()
    {
        return "NamespaceWalker { visitor: " + this.visitor + " }";
    }
}
