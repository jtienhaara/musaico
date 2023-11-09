package musaico.foundation.typing.aspect;


import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;


import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.Read;
import musaico.foundation.typing.StandardTag;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolMustBeUnique;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.Visibility;


public class TTT
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static void main ( String [] args )
        throws SymbolMustBeUnique.Violation
    {
        final SymbolTable symbol_table = new SymbolTable ();
        final Type<String> string_type =
            Kind.ROOT.typeBuilder ( String.class, symbol_table )
                .rawTypeName ( "string" )
                .namespace ( Namespace.ROOT )
                .none ( "" )
                .defaultSubTyping ()
                .build ()
                .orThrowUnchecked ();

        System.out.println ( "SYMBOL TABLE CONTENTS: " + symbol_table.printSymbolTable () );

        final SymbolTable logger_symbol_table = new SymbolTable ();
        final Tag logger = new StandardTag ( Namespace.ROOT,
                                             new TagID ( "logger",
                                                         Visibility.PUBLIC ),
                                             logger_symbol_table,
                                             new StandardMetadata () );
        final Aspect.PrePost logger_pre_post =
            new Aspect.PrePost ()
            {
                private static final long serialVersionUID =
                    TTT.serialVersionUID;

                @Override
                protected void preNoProcessing ( Operation<?> op )
                {
                    System.out.println ( "ENTER " + op );
                }

                @Override
                protected void postNoProcessing ( Operation<?> op )
                {
                    System.out.println ( "EXIT  " + op );
                }
            };

        final Aspect logger_aspect = new Aspect ( "logger", logger_pre_post );
        logger_symbol_table.set ( logger_aspect.id (), logger_aspect );

        final Term<String> t_string =
            string_type.sub ( logger )
                .orThrowUnchecked ()
                .instance ( "Hello, world!" );
        System.out.println ( "Hello world term type "
                             + t_string.type () );
        final String value = t_string.value ().await ().orNone ();
        System.out.println ( "Value = " + value );
    }
}
