%{
  #include <stdio.h>
%}

%code provides {
  int yylex (YYSTYPE *lvalp, YYLTYPE *llocp);
  void yyerror (YYLTYPE *locp, char const *msg);
}

%language "C"
%locations
%define api.pure full
%define api.symbol.prefix {MUSAICO_S_}
%define api.token.prefix {MUSAICO_T_}

%start declaration

%define api.value.type union /* Generate YYSTYPE from these types: */
%token <char *>     BRACE_CLOSE                 "}"
%token <char *>     BRACE_OPEN                  "{"
%token <char *>     COMMENT_SINGLE_LINE
%token <double>     FLOAT
%token <char *>     ID
%token <long>       INT
%token <char *>     NEWLINE                     "\n"
%token <char *>     SEMI_COLON                  ";"
%token <char *>     WHITESPACE
%token <char *>     WHITESPACE_NO_NEWLINE


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
ID op_binary_with_space expression SEMI_COLON
| ID SEMI_COLON
| constructor
;

op_binary_with_space:
op_binary WHITESPACE
| op_binary
| WHITESPACE op_binary WHITESPACE
| WHITESPACE op_binary
;

op_binary:
'='
;

constructor:
constructor_header declaration constructor_footer
;

constructor_header:
ID WHITESPACE BRACE_OPEN
| ID BRACE_OPEN
;

constructor_footer:
BRACE_CLOSE WHITESPACE
| BRACE_CLOSE
;


%%


int yylex (YYSTYPE *lvalp, YYLTYPE *llocp)
{
  return 0;
}

void yyerror (YYLTYPE *locp, char const *msg)
{
}


/* !!!

input:
  %empty
| input line
;


line:
  '\n'
| exp '\n'      { printf ("%.10g\n", $1); }
;


exp:
  NUM
| exp exp '+'   { $$ = $1 + $2;      }
| exp exp '-'   { $$ = $1 - $2;      }
| exp exp '*'   { $$ = $1 * $2;      }
| exp exp '/'   { $$ = $1 / $2;      }
| exp exp '^'   { $$ = pow ($1, $2); }  / * Exponentiation * /
| exp 'n'       { $$ = -$1;          }  / * Unary minus   * /
;



%union {
  long n;
  tree t;  / * tree is defined in ptypes.h. * /
}


%{
  static void print_token (yytoken_kind_t token, YYSTYPE val);
%}


!!!


%define api.prefix {c}
// Emitted in the header file, after the definition of YYSTYPE.
%code provides
{
  // Tell Flex the expected prototype of yylex.
  #define YY_DECL                             \
    int clex (CSTYPE *yylval, CLTYPE *yylloc)

  // Declare the scanner.
  YY_DECL;
}


!!!
location struct is:
typedef struct YYLTYPE
{
  int first_line;
  int first_column;
  int last_line;
  int last_column;
} YYLTYPE;

!!!
int
yylex (YYSTYPE *lvalp, YYLTYPE *llocp)
{
  …
  *lvalp = value;  / * Put value onto Bison stack. * /
  return INT;      / * Return the kind of the token. * /
  …
}

!!!
*/
