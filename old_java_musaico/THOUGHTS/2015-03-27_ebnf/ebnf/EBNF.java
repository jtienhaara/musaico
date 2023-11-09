package ebnf;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

// Syntax adapted slightly from http://www.cs.cmu.edu/~pattis/misc/ebnf.pdf

public class EBNF
    implements Serializable
{
    public static final ZeroOrMoreRule WHITESPACE =
        new ZeroOrMoreRule ( "whitespace",
            new OrRule ( AbstractRule.NO_NAME, new Node [] {
                    new Literal ( " " ),
                    new Literal ( "\t" ),
                    new Literal ( "\n" ),
                    new Literal ( "\r" )
                } )
          );
    public static final OrRule LOWER_CASE =
        new OrRule ( "lower-case", new Node [] {
                new Literal ( "a" ),
                new Literal ( "b" ),
                new Literal ( "c" ),
                new Literal ( "d" ),
                new Literal ( "e" ),
                new Literal ( "f" ),
                new Literal ( "g" ),
                new Literal ( "h" ),
                new Literal ( "i" ),
                new Literal ( "j" ),
                new Literal ( "k" ),
                new Literal ( "l" ),
                new Literal ( "m" ),
                new Literal ( "n" ),
                new Literal ( "o" ),
                new Literal ( "p" ),
                new Literal ( "q" ),
                new Literal ( "r" ),
                new Literal ( "s" ),
                new Literal ( "t" ),
                new Literal ( "u" ),
                new Literal ( "v" ),
                new Literal ( "w" ),
                new Literal ( "x" ),
                new Literal ( "y" ),
                new Literal ( "z" )
            } );
    public static final OrRule UPPER_CASE =
        new OrRule ( "upper-case", new Node [] {
                new Literal ( "A" ),
                new Literal ( "B" ),
                new Literal ( "C" ),
                new Literal ( "D" ),
                new Literal ( "E" ),
                new Literal ( "F" ),
                new Literal ( "G" ),
                new Literal ( "H" ),
                new Literal ( "I" ),
                new Literal ( "J" ),
                new Literal ( "K" ),
                new Literal ( "L" ),
                new Literal ( "M" ),
                new Literal ( "N" ),
                new Literal ( "O" ),
                new Literal ( "P" ),
                new Literal ( "Q" ),
                new Literal ( "R" ),
                new Literal ( "S" ),
                new Literal ( "T" ),
                new Literal ( "U" ),
                new Literal ( "V" ),
                new Literal ( "W" ),
                new Literal ( "X" ),
                new Literal ( "Y" ),
                new Literal ( "Z" )
            } );
    public static final OrRule DIGIT =
        new OrRule ( "digit", new Node [] {
                new Literal ( "0" ),
                new Literal ( "1" ),
                new Literal ( "2" ),
                new Literal ( "3" ),
                new Literal ( "4" ),
                new Literal ( "5" ),
                new Literal ( "6" ),
                new Literal ( "7" ),
                new Literal ( "8" ),
                new Literal ( "9" )
            } );
    public static final OrRule OTHER_CHARACTER =
        new OrRule ( "other-character", new Node [] {
                new Literal ( "-" ),
                new Literal ( "_" ),
                new Literal ( "\"" ),
                new Literal ( "#" ),
                new Literal ( "&" ),
                new Literal ( "'" ),
                new Literal ( "(" ),
                new Literal ( ")" ),
                new Literal ( "+" ),
                new Literal ( "," ),
                new Literal ( "." ),
                new Literal ( "/" ),
                new Literal ( ":" ),
                new Literal ( ";" ),
                new Literal ( "<" ),
                new Literal ( "=" ),
                new Literal ( ">" ),
                new Literal ( "\\" )
            } );
    public static final OrRule CHARACTER =
        new OrRule ( "character", new Node [] {
                LOWER_CASE,
                UPPER_CASE,
                DIGIT,
                OTHER_CHARACTER
            } );
    /* !!!
    public static final Empty EMPTY_SYMBOL =
        new Empty ();
        !!! */
    public static final SequenceRule LHS =
        new SequenceRule ( "lhs", new Node [] {
                LOWER_CASE,
                new ZeroOrMoreRule ( AbstractRule.NO_NAME,
                    new OrRule ( AbstractRule.NO_NAME, new Node [] {
                        new SequenceRule ( AbstractRule.NO_NAME, new Node [] {
                                new OptionalRule ( AbstractRule.NO_NAME,
                                                   new Literal ( "-" ) ),
                                LOWER_CASE
                            } ),
                        new SequenceRule ( AbstractRule.NO_NAME, new Node [] {
                                new OptionalRule ( AbstractRule.NO_NAME,
                                                   new Literal ( "-" ) ),
                                DIGIT
                            } )
                        } )
                    )
            } );
    private static final ForwardReference<SequenceRule> RHS_PLACEHOLDER =
        new ForwardReference<SequenceRule> ();
    public static final SequenceRule OPTION =
        new SequenceRule ( "option", new Node [] {
                new Literal ( "[" ),
                RHS_PLACEHOLDER,
                new Literal ( "]" )
            } );
    public static final SequenceRule REPETITION =
        new SequenceRule ( "repetition", new Node [] {
                new Literal ( "{" ),
                RHS_PLACEHOLDER,
                new Literal ( "}" )
            } );
    public static final /* !!! OrRule !!! */ SequenceRule SEQUENCE =
        /* !!!
        new OrRule ( "sequence", new Node [] {
                EMPTY_SYMBOL,
                !!! */
                new SequenceRule ( AbstractRule.NO_NAME, new Node [] {
                        WHITESPACE,
                        new ZeroOrMoreRule ( AbstractRule.NO_NAME,
                            new OrRule ( AbstractRule.NO_NAME, new Node [] {
                                    CHARACTER,
                                    LHS,
                                    OPTION,
                                    REPETITION
                                } )
                            )
                        // !!! } )
                } );
    public static final SequenceRule RHS =
        RHS_PLACEHOLDER.resolve (
            new SequenceRule ( "rhs", new Node [] {
                    SEQUENCE,
                    new ZeroOrMoreRule ( AbstractRule.NO_NAME,
                        new SequenceRule ( AbstractRule.NO_NAME, new Node [] {
                            new Literal ( "|" ),
                            SEQUENCE
                            } )
                        )
                } )
            );
    public static final SequenceRule EBNF_RULE =
        new SequenceRule ( "ebnf-rule", new Node [] {
                WHITESPACE,
                LHS,
                WHITESPACE,
                new Literal ( ":" ),
                RHS,
                WHITESPACE
            } );
    public static final ZeroOrMoreRule EBNF_DESCRIPTION =
        new ZeroOrMoreRule ( "ebnf-description",
                EBNF_RULE
            );


    public static void main (
                             String [] args
                             )
    {
        final Set<Node> visited = new HashSet<Node> ();
        final NodeVisitor visitor =
            new NodeVisitor ()
            {
                public final NodeVisitor.VisitStatus visit (
                                                            Node node,
                                                            VisitorState state
                                                            )
                {
                    if ( visited.contains ( node ) )
                    {
                        return NodeVisitor.VisitStatus.FAIL;
                    }

                    if ( node instanceof AbstractRule )
                    {
                        final AbstractRule rule = (AbstractRule) node;
                        if ( rule.name () != AbstractRule.NO_NAME )
                        {
                            System.out.println ( "" + rule );
                            visited.add ( rule );
                            return NodeVisitor.VisitStatus.CONTINUE;
                        }
                    }

                    return NodeVisitor.VisitStatus.POP;
                }
            };

        EBNF_DESCRIPTION.accept ( visitor, new VisitorState () );


        final String ebnf_syntax =
            "lower-case: \"a\"|\"b\"|\"c\"|\"d\"|\"e\"|\"f\"|\"g\"|\"h\"|\"i\"|\"j\"|\"k\"|\"l\"|\"m\"|\"n\"|\"o\"|\"p\"|\"q\"|\"r\"|\"s\"|\"t\"|\"u\"|\"v\"|\"w\"|\"x\"|\"y\"|\"z\"\n"
            + "upper-case: \"A\"|\"B\"|\"C\"|\"D\"|\"E\"|\"F\"|\"G\"|\"H\"|\"I\"|\"J\"|\"K\"|\"L\"|\"M\"|\"N\"|\"O\"|\"P\"|\"Q\"|\"R\"|\"S\"|\"T\"|\"U\"|\"V\"|\"W\"|\"X\"|\"Y\"|\"Z\"\n"
            + "digit: \"0\"|\"1\"|\"2\"|\"3\"|\"4\"|\"5\"|\"6\"|\"7\"|\"8\"|\"9\"\n"
            + "other-character: \"-\"|\"_\"|\"\\\"\"|\"#\"|\"&\"|\"'\"|\"(\"|\")\"|\"+\"|\",\"|\".\"|\"/\"|\":\"|\";\"|\"<\"|\"=\"|\">\"\n"
            + "character: lower-case|upper-case|digit|other-character\n"
            + "empty-symbol: \"()\"\n"
            + "lhs: lower-case{[\"-\"]lower-case|[\"-\"]digit}\n"
            + "option: \"[\"rhs\"]\"\n"
            + "repetition: \"{\"rhs\"}\"\n"
            + "sequence: empty-symbol|{character|lhs|option|repetition}\n"
            + "rhs: sequence{\"|\"sequence}\n"
            + "ebnf-rule: lhs\":\"{\" \"}rhs\n"
            + "ebnf-description: {ebnf-rule}\n";
        final NodeVisitor test_parser =
            new NodeVisitor ()
            {
                private final String syntax = ebnf_syntax;
                private int position = 0;
                private String nextChar = "<not started>";
                private List<Node> mostRecentNodeStack = new ArrayList<Node> ();
                public final NodeVisitor.VisitStatus visit (
                                                            Node node,
                                                            VisitorState state
                                                            )
                {
                    System.err.println("!!! VISIT " + node );
                    int new_position = this.position;

                    try
                    {
                        System.err.println ( "!!! " + node + " @ " + this.position );
                        this.mostRecentNodeStack = state.stack ();

                        if ( this.position == this.syntax.length () )
                        {
                            this.nextChar = "<done parsing>";
                            System.err.println ( "!!! EBNF 1 pop" );
                            return NodeVisitor.VisitStatus.POP;
                        }
                        else if ( this.position > this.syntax.length () )
                        {
                            this.nextChar = "<parser overrun>";
                            System.err.println ( "!!! EBNF 2 abort" );
                            return NodeVisitor.VisitStatus.ABORT;
                        }

                        if ( this.position >= 0
                             && this.position < this.syntax.length () )
                        {
                            this.nextChar =
                                "'" + this.syntax.charAt ( this.position ) + "'";
                        }
                        else
                        {
                            this.nextChar = "<invalid position "
                                + this.position + ">";
                        }

                        if ( node instanceof Empty )
                        {
                            final String remaining_text =
                                this.syntax.substring ( this.position );
                            final String without_newlines =
                                remaining_text.replaceAll ( "^[\n\r\0]+", "" );
                            final int adjust_position = remaining_text.length ()
                                - without_newlines.length ();

                            // !!! System.out.println ( "!!! EMPTY " + adjust_position + " CHARS -> " + without_newlines );
                            this.position += adjust_position;
                            if ( adjust_position == 0 )
                            {
                                System.err.println ( "!!! EMPTY FAIL ++++++++++++++++++++++++++++++++" );
                            System.err.println ( "!!! EBNF 3 fail" );
                                return NodeVisitor.VisitStatus.FAIL;
                            }
                            if ( this.position >= this.syntax.length () )
                            {
                                System.err.println ( "!!! EMPTY ABORT ++++++++++++++++++++++++++++++++" );
                            System.err.println ( "!!! EBNF 4 abort" );
                                return NodeVisitor.VisitStatus.ABORT;
                            }
                            else
                            {
                                new_position = this.position;
                                System.err.println ( "!!! EMPTY CONTINUE ++++++++++++++++++++++++++++++++" );
                            System.err.println ( "!!! EBNF 5 continue" );
                                return NodeVisitor.VisitStatus.CONTINUE;
                            }
                        }

                        if ( ! ( node instanceof Literal ) )
                        {
                            // Probably a composite rule; if so then
                            // carry on to this rule's children.
                            System.err.println ( "!!! NOT LITERAL CONTINUE" );
                            System.err.println ( "!!! EBNF 6 continue" );
                            return NodeVisitor.VisitStatus.CONTINUE;
                        }

                        final Literal literal = (Literal) node;
                        final String literal_text = literal.text ();
                        final String next_text =
                            this.syntax.substring ( this.position );

                        // !!! System.out.println ( "!!! MAYBE LITERAL " + literal_text );
                        if ( ! next_text.startsWith ( literal_text ) )
                        {
                            final String stripped = ( "" + this ).replaceAll ( "[\n\r]+$", "" ); // !!!
                            int nl; // !!!
                            int num_newlines = 0; // !!!
                            for ( nl = stripped.length () - 1; nl >= 0; nl -- ) { if ( stripped.charAt ( nl ) == '\n' ) { num_newlines ++; if ( num_newlines >= 2 ) { break; } } } // !!!
                            final String last_2_lines = stripped.substring ( nl + 1 ); // !!! 
                            // !!! final int last_line_start = stripped.lastIndexOf ( '\n' ) + 1; // !!!
                            // !!! final String last_line = stripped.substring ( last_line_start ); // !!! 
                            System.err.println ( "!!!     FAIL\n" + last_2_lines );
                            // !!! System.err.println ( "!!!     FAIL\n" + this + "\n" );
                            System.err.println ( "!!! EBNF 7 fail" );
                            return NodeVisitor.VisitStatus.FAIL;
                        }

                        new_position = this.position + literal_text.length ();
                        this.position = new_position;

                        System.out.print ( literal_text );

                            System.err.println ( "!!!     CONTINUE\n" + this + "\n==============" );
                            System.err.println ( "!!! EBNF 8 continue" );
                        return NodeVisitor.VisitStatus.CONTINUE;
                    }
                    catch ( RuntimeException e )
                    {
                        System.err.println ( "!!! FAILED:" );
                        e.printStackTrace ();
                            System.err.println ( "!!! EBNF 9 abort" );
                        return NodeVisitor.VisitStatus.ABORT;
                    }
                    finally
                    {
                        this.position = new_position;
                    }
                }
                public String toString ()
                {
                    final StringBuilder sbuf = new StringBuilder ();
                    sbuf.append ( "test_parser\n" );
                    // !!! sbuf.append ( "test_parser parsing:\n" );
                    // !!! sbuf.append ( this.syntax );
                    // !!! sbuf.append ( "\n" );
                    sbuf.append ( "At position:    " + this.position + "\n" );
                    sbuf.append ( "Next character: " + this.nextChar + "\n" );
                    sbuf.append ( "Most recent node stack:\n" );
                    for ( Node node : this.mostRecentNodeStack )
                    {
                        final String node_string = "" + node;
                        final int first_newline = node_string.indexOf ( '\n' );
                        final int length = node_string.length ();
                        final int max = 60;
                        final String node_brief;
                        if ( first_newline >= 0
                             && first_newline < max )
                        {
                            node_brief =
                                node_string.substring ( 0, first_newline )
                                + "...";
                        }
                        else if ( length > max )
                        {
                            node_brief =
                                node_string.substring ( 0, max )
                                + "...";
                        }
                        else
                        {
                            node_brief = node_string;
                        }

                        sbuf.append ( "    " + node_brief + "\n" );
                    }
                    sbuf.append ( "\n" );

                    final int num_context_characters = 17;
                    int adjusted_for_special_chars = 0;
                    for ( int context = position - num_context_characters - 3;
                          context <= position + num_context_characters + 3;
                          context ++ )
                    {
                        if ( context < 0
                             || context >= this.syntax.length () )
                        {
                            sbuf.append ( " " );
                        }
                        else if ( context < ( this.position
                                              - num_context_characters )
                                  || context > ( this.position
                                                 + num_context_characters ) )
                        {
                            sbuf.append ( "." );
                        }
                        else if ( this.syntax.charAt ( context ) == '\n'
                                  || this.syntax.charAt ( context ) == '\r' )
                        {
                            sbuf.append ( "\\n" );
                            if ( context < num_context_characters )
                            {
                                adjusted_for_special_chars ++;
                            }
                        }
                        else
                        {
                            sbuf.append ( this.syntax.charAt ( context ) );
                        }
                    }

                    final int num_spaces =
                        num_context_characters + 3 + adjusted_for_special_chars;
                    sbuf.append ( "\n" );
                    for ( int space = 0;
                          space < num_spaces;
                          space ++ )
                    {
                        sbuf.append ( " " );
                    }
                    sbuf.append ( "^\n" );
                    return sbuf.toString ();
                }
            };
        System.out.println ( "About to parse:\n" + ebnf_syntax );
        System.out.println ( "Parsing:\n" );
        try
        {
            EBNF_DESCRIPTION.accept ( test_parser, new VisitorState () );
        }
        catch ( Throwable t )
        {
            System.err.println ( t.getMessage () );
            System.err.println ( "" );
            System.err.println ( "Parser state:" );
            System.err.println ( "" + test_parser );
            System.exit ( 2 );
        }
    }
}
