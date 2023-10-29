%{
  #include <stdio.h>
  #include <fcntl.h>
  #include <unistd.h>

  #include "musaico.h"
%}

%code provides {
  static int yylex (
          YYSTYPE *lvalp,
          YYLTYPE *llocp,
          int fd
          );
  static void yyerror (
          YYLTYPE *locp,
          int fd,
          char const *msg
          );
}

%language "C"
%locations
%define api.pure full
%define api.symbol.prefix {MUSAICO_S_}
%define api.token.prefix {MUSAICO_T_}
%param {int fd}

%start declaration

%define api.value.type union /* Generate YYSTYPE from these types: */
%token <char *>     BRACE_CLOSE                 "}"
%token <char *>     BRACE_OPEN                  "{"
%token <char *>     BRACKET_CLOSE               "]"
%token <char *>     BRACKET_OPEN                "["
%token <char *>     COMMENT_SINGLE_LINE         // // comment etc...
%token <char *>     DOT                         "."
%token <char *>     EQUAL                       "="
%token <double>     FLOAT                       // [0-9]+(\.[0-9]+)?
%token <char *>     ID                          // xyz etc
%token <long>       INT                         // [0-9]+
%token <char *>     NEWLINE                     "\n"
%token <char *>     SEMI_COLON                  ";"
%token <char *>     STRING                      // 'xyz' or "xyz" etc
%token <char *>     TIMES                       "*"
%token <char *>     WHITESPACE                  // [ \t\r\n]
%token <char *>     WHITESPACE_NO_NEWLINE       // [ \t\r]


%%
declaration:
blank_line 
| comment
| expression
;

comment:
COMMENT_SINGLE_LINE
;

blank_line:
NEWLINE
| WHITESPACE_NO_NEWLINE NEWLINE
;

expression:
multiplier_expression
| fully_qualified_id op_binary_with_space expression SEMI_COLON
| fully_qualified_id SEMI_COLON
| constructor
| primitive_expression
;

multiplier_expression:
INT WHITESPACE TIMES WHITESPACE expression
| INT WHITESPACE TIMES expression
| INT TIMES WHITESPACE expression
| INT TIMES expression
;

primitive_expression:
FLOAT WHITESPACE SEMI_COLON
| FLOAT SEMI_COLON
| INT WHITESPACE SEMI_COLON
| INT SEMI_COLON
| STRING WHITESPACE SEMI_COLON
| STRING SEMI_COLON
;

fully_qualified_id:
ID DOT fully_qualified_id
| ID index DOT fully_qualified_id
| ID index
| ID
;

index:
BRACKET_OPEN INT BRACKET_CLOSE
;

op_binary_with_space:
op_binary WHITESPACE
| op_binary
| WHITESPACE op_binary WHITESPACE
| WHITESPACE op_binary
;

op_binary:
EQUAL
;

constructor:
constructor_header declaration constructor_footer
;

constructor_header:
fully_qualified_id WHITESPACE BRACE_OPEN
| fully_qualified_id BRACE_OPEN
;

constructor_footer:
BRACE_CLOSE WHITESPACE
| BRACE_CLOSE
;


%%


static int yylex (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        int fd
        )
{
  return 0;
}

static void yyerror (
        YYLTYPE *locp,
        int fd,
        char const *msg
        )
{
}


//
// yyparse() returns:
//
//     The value returned by yyparse is 0 if parsing was successful
//     (return is due to end-of-input).
//
//     The value is 1 if parsing failed because of invalid input, i.e.,
//     input that contains a syntax error or that causes YYABORT to be invoked.
//
//     The value is 2 if parsing failed due to memory exhaustion.
//
// From:
//     https://www.gnu.org/software/bison/manual/bison.html#Interface
//
int musaico_parse(
        int fd
        )
{
  int parse_result = yyparse(fd);
  return parse_result;
}

int musaico_parse_file(
        char *filename
        )
{
  int fd = open(filename, O_RDONLY);
  int parse_result = musaico_parse(fd);
  close(fd);

  return parse_result;
}
