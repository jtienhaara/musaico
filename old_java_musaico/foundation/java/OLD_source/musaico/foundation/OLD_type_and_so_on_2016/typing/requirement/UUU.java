package musaico.foundation.typing.requirement;

import java.io.Serializable;


import musaico.foundation.contract.Contract;
import musaico.foundation.contract.ObjectContracts;

import musaico.foundation.contract.guarantees.ReturnNeverNull;

import musaico.foundation.contract.obligations.ParametersMustNotBeNull;

import musaico.foundation.filter.Filter;
import musaico.foundation.filter.FilterState;

import musaico.foundation.metadata.Metadata;
import musaico.foundation.metadata.StandardMetadata;

import musaico.foundation.security.Capability;
import musaico.foundation.security.Permissions;
import musaico.foundation.security.SecurityContext;
import musaico.foundation.security.SecurityContract;
import musaico.foundation.security.SecurityPolicy;
import musaico.foundation.security.SecurityViolation;

import musaico.foundation.typing.Kind;
import musaico.foundation.typing.Mutation;
import musaico.foundation.typing.Namespace;
import musaico.foundation.typing.Operation;
import musaico.foundation.typing.OperationType;
import musaico.foundation.typing.TypesMustHaveSameValueClasses;
import musaico.foundation.typing.Read;
import musaico.foundation.typing.Symbol;
import musaico.foundation.typing.SymbolID;
import musaico.foundation.typing.SymbolTable;
import musaico.foundation.typing.StandardTag;
import musaico.foundation.typing.Tag;
import musaico.foundation.typing.TagID;
import musaico.foundation.typing.Term;
import musaico.foundation.typing.Type;
import musaico.foundation.typing.TypingViolation;
import musaico.foundation.typing.Visibility;

import musaico.foundation.value.No;
import musaico.foundation.value.One;
import musaico.foundation.value.Value;
import musaico.foundation.value.ValueViolation;

public class UUU
{
    /** The MusaicoModule to which this class or interface belongs. */
    public static final MODULE PARENT_MODULE = MODULE.MODULE;

    /** The version of the parent module, YYYYMMDD format. */
    private static final long serialVersionUID = MODULE.VERSION;


    public static class OperationCapability
        implements Capability, Serializable
    {
        private static final long serialVersionUID = UUU.serialVersionUID;


        private final Operation<?> operation;
        public OperationCapability (
                                    Operation<?> operation
                                    )
        {
            this.operation = operation;
        }

        public boolean equals (
                               Object object
                               )
        {
            if ( object == null )
            {
                return false;
            }
            else if ( object.getClass () != this.getClass () )
            {
                return false;
            }

            OperationCapability that = (OperationCapability) object;
            return this.operation.equals ( that.operation );
        }

        public int hashCode ()
        {
            return 1 + this.operation.hashCode ();
        }
    }




    public static class Secure
        extends Requirement<SecurityContext>
        implements Serializable
    {
        private static final long serialVersionUID = UUU.serialVersionUID;


        // Checks constructor and static method obligations.
        private static final ObjectContracts classContracts =
            new ObjectContracts ( UUU.Secure.class );


        private final SecurityPolicy security;

        public Secure ( SecurityPolicy security )
        {
            this ( "secure", security );
        }

        public Secure ( String name,
                        SecurityPolicy security )
        {
            super ( name,
                    SecurityContext.class );

            this.security = security;
        }

        public FilterState filter (
                                   SatisfactionAttempt<SecurityContext> attempt
                                   )
        {
            final Operation<?> operation = attempt.operation ();
            final SecurityContext security_context = attempt.requiredValue ();

            final Capability capability = new OperationCapability ( operation );
            final Permissions permissions =
                new Permissions ( security_context,
                                  capability );
            try
            {
                this.security.request ( permissions )
                    .orThrowChecked ()
                    .ifNotPermittedThrowViolation ( capability );

                System.out.println ( "!!! DEBUG: read IS permitted" );
                return FilterState.KEPT;
            }
            catch ( ValueViolation no_permissions_violation )
            {
                System.out.println ( "!!! DEBUG1: read NOT permitted" );
                return FilterState.DISCARDED;
            }
            catch ( SecurityViolation security_violation )
            {
                System.out.println ( "!!! DEBUG2: read NOT permitted" );
                return FilterState.DISCARDED;
            }
        }


        /**
         * @see musaico.foundation.typing.Symbol#rename(java.lang.String)
         */
        @Override
        public Secure rename (
                              String name
                              )
            throws ParametersMustNotBeNull.Violation,
            ReturnNeverNull.Violation
        {
            classContracts.check ( ParametersMustNotBeNull.CONTRACT,
                                   name );

            return new Secure ( name,
                                this.security );
        }

        /**
         * @see musaico.foundation.typing.Retypable#retype(java.lang.String, musaico.foundation.typing.Type)
         */
        @Override
        public Secure retype (
                              String name,
                              OperationType<? extends Operation<Mutation>, Mutation> type
                              )
            throws ParametersMustNotBeNull.Violation,
                   TypesMustHaveSameValueClasses.Violation,
                   ReturnNeverNull.Violation
        {
            this.checkRetype ( name, type );

            return new Secure ( name,
                                this.security );
        }
    }




    public static void main ( String [] args )
        throws Exception
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

        final SecurityPolicy laissez_faire_security =
            new SecurityPolicy ()
            {
                private static final long serialVersionUID =
                    UUU.serialVersionUID;
                @Override
                public Value<Permissions> request (
                    Permissions requested_permissions
                    )
                {
                    return new One<Permissions> ( Permissions.class,
                                                  requested_permissions );
                }
            };

        final SecurityPolicy locked_down_security =
            new SecurityPolicy ()
            {
                private static final long serialVersionUID =
                    UUU.serialVersionUID;
                @Override
                public Value<Permissions> request (
                    Permissions requested_permissions
                    )
                {
                    return new No<Permissions> ( Permissions.class,
                                                 new SecurityViolation ( new SecurityContract ( this ),
                                                                         this,
                                                                         requested_permissions )  );
                }
            };

        final SymbolTable secure_symbol_table =
            new SymbolTable ();
        final Tag secure = new StandardTag ( Namespace.ROOT,
                                             new TagID ( "secure",
                                                         Visibility.PUBLIC ),
                                             secure_symbol_table,
                                             new StandardMetadata () );
        final Secure secure_mutate =
            new Secure ( laissez_faire_security );
        secure_symbol_table.add ( secure_mutate );
        Namespace.ROOT.add ( secure );

        final SymbolTable locked_down_symbol_table =
            new SymbolTable ();
        final Tag locked_down = new StandardTag ( Namespace.ROOT,
                                                  new TagID ( "locked down",
                                                              Visibility.PUBLIC ),
                                                  locked_down_symbol_table,
                                                  new StandardMetadata () );
        final Secure locked_down_mutate =
            new Secure ( locked_down_security );
        locked_down_symbol_table.add ( locked_down_mutate );
        Namespace.ROOT.add ( locked_down );


        final Satisfier<String, SecurityContext> login =
            new Satisfier<String, SecurityContext> ( string_type,
                                                     SecurityContext.class,
                                                     SecurityContext.NONE );

        final String secured_string1 = "Hello, world laissez faire!" ;
        final Term<String> t_secure_string =
            string_type.sub ( secure )
            .orThrowUnchecked ()
            .instance ( secured_string1 );
        final Value<String> secure_value1 =
            t_secure_string.value ().await ();
        System.out.println ( "Secure value - don't bother unlocking    = " + secure_value1 );
        if ( secure_value1.hasValue () )
        {
            throw new IllegalStateException ( "Failed - should not return anything" );
        }
        else
        {
            System.out.println ( "OK." );
        }

        final Value<String> secure_value2 =
            t_secure_string.call ( login ).value ().await ();
        System.out.println ( "Secure value - try to unlock             = " + secure_value2 );
        if ( ! secure_value2.hasValue ()
             || ! secure_value2.orNone ().equals ( secured_string1 ) )
        {
            throw new IllegalStateException ( "Failed - should have returned '" + secured_string1 + "' but actually returned '" + secure_value2.orNone () );
        }
        else
        {
            System.out.println ( "OK." );
        }

        final String secured_string2 = "Hello, world locked down!";
        final Term<String> t_locked_down_string =
            string_type.sub ( locked_down )
            .orThrowUnchecked ()
            .instance ( secured_string2 );
        final Value<String> locked_down_value1 =
            t_locked_down_string.value ().await ();
        System.out.println ( "Locked down value - don't unlock         = " + locked_down_value1 );
        if ( locked_down_value1.hasValue () )
        {
            throw new IllegalStateException ( "Failed - should not return anything" );
        }
        else
        {
            System.out.println ( "OK." );
        }

        final Value<String> locked_down_value2 =
            t_locked_down_string.call ( login ).value ().await ();
        System.out.println ( "Locked down value - try to unlock, can't = " + locked_down_value2 );
        if ( locked_down_value2.hasValue () )
        {
            throw new IllegalStateException ( "Failed - should not return anything" );
        }
        else
        {
            System.out.println ( "OK." );
        }
    }
}
