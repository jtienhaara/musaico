%{
  #include <stdio.h>
  #include <errno.h>
  #include <float.h>  // For LDBL_MIN
  #include <malloc.h>
  #include <math.h>  // For HUGE_VALL
  #include <stdlib.h>  // For strtold(), strfroml(), strtoll()
  #include <string.h>
  #include <unistd.h>  // For read()

  #include "musaico.h"
  #include "musaico_parser.h"
%}

%code provides {
  static int yylex (
          YYSTYPE *lvalp,
          YYLTYPE *llocp,
          musaico_parser_t *parser
          );
  static void yyerror (
          YYLTYPE *locp,
          musaico_parser_t *parser,
          char const *msg
          );
}

%language "C"
%locations
%define api.pure full
%define api.symbol.prefix {MUSAICO_S_}
%define api.token.prefix {MUSAICO_T_}
%define parse.error detailed
%param {musaico_parser_t *parser}

%start musaico_syntax

%define api.value.type union /* Generate YYSTYPE from these types: */
%token <char>           BRACE_CLOSE             '}'
%token <char>           BRACE_OPEN              '{'
%token <char>           BRACKET_CLOSE           ']'
%token <char>           BRACKET_OPEN            '['
%token <char *>         COMMENT_SINGLE_LINE     // // comment etc...
%token <char>           DIVIDED_BY              '/'
%token <char>           DOT                     '.'
%token <char>           EQUAL                   '='
%token <long double>    FLOAT                   // [0-9]+(\.[0-9]+)?
%token <char *>         ID                      // xyz etc
%token <long long>      INT                     // [0-9]+
%token <char>           NEWLINE                 '\n'
%token <char>           SEMI_COLON              ';'
%token <char *>         STRING                  // 'xyz' or "xyz" etc
%token <char>           TIMES                   '*'
%token <char *>         WHITESPACE              // [ \t\r]


%%
musaico_syntax:
empty expression musaico_syntax
| empty
| expression musaico_syntax
| expression
;

empty:
empty_whitespace_first
| empty_comments_first
;

empty_whitespace_first:
whitespace empty_comments_first
| whitespace
;

empty_comments_first:
comments empty_whitespace_first
| comments
;

comments:
comment comments
| comment
;

comment:
COMMENT_SINGLE_LINE NEWLINE
;

expression:
multiplier_expression
| fully_qualified_id op_binary_with_space expression
| fully_qualified_id SEMI_COLON
| constructor
| primitive_expression
;

multiplier_expression:
INT whitespace TIMES whitespace expression
| INT whitespace TIMES expression
| INT TIMES whitespace expression
| INT TIMES expression
;

whitespace:
WHITESPACE whitespace
| NEWLINE whitespace
| WHITESPACE
| NEWLINE
;

primitive_expression:
FLOAT whitespace SEMI_COLON
| FLOAT SEMI_COLON
| INT whitespace SEMI_COLON
| INT SEMI_COLON
| STRING whitespace SEMI_COLON
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
whitespace op_binary whitespace
| whitespace op_binary
| op_binary whitespace
| op_binary
;

op_binary:
EQUAL
;

constructor:
constructor_header constructor_body constructor_footer
| constructor_header constructor_footer
;

constructor_header:
fully_qualified_id whitespace BRACE_OPEN empty
| fully_qualified_id whitespace BRACE_OPEN
| fully_qualified_id BRACE_OPEN empty
| fully_qualified_id BRACE_OPEN
;

constructor_body:
expression empty constructor_body
| expression constructor_body
| expression empty
| expression
;

constructor_footer:
BRACE_CLOSE
;


%%


static int musaico_read_char(
        int fd,
        YYLTYPE *llocp
        )
{
  char next_char = EOF;
  size_t num_chars_read = read(fd, &next_char, sizeof(char));
  if (num_chars_read == (size_t) 0) {
    return (int) EOF;
  }
  else if (next_char == EOF) {
    return (int) EOF;
  }
  llocp->last_column ++;
  return (int) next_char;
}

static int is_id_first_char(
        char id_char
        )
{
  if (id_char >= 'A' && id_char <= 'z') {
    return 1;
  } else if (id_char == '_') {
    return 1;
  } else {
    return 0;
  }
}

static int is_id_char(
        char id_char
        )
{
  if (id_char >= 'A' && id_char <= 'z') {
    return 1;
  } else if (id_char == '_') {
    return 1;
  } else if (id_char >= '0' && id_char <= '9') {
    return 1;
  } else {
    return 0;
  }
}

static int musaico_lex_brace_close (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // BRACE_CLOSE
  parser->next_char = -1;
  lvalp->MUSAICO_T_BRACE_CLOSE = '}';
  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_LEX,
                              "}",  // context
                              NULL);  // source
  return MUSAICO_T_BRACE_CLOSE;
}

static int musaico_lex_brace_open (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // BRACE_OPEN
  parser->next_char = -1;
  lvalp->MUSAICO_T_BRACE_OPEN = '{';
  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_LEX,
                              "{",  // context
                              NULL);  // source
  return MUSAICO_T_BRACE_OPEN;
}

static int musaico_lex_bracket_close (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // BRACKET_CLOSE
  parser->next_char = -1;
  lvalp->MUSAICO_T_BRACKET_CLOSE = ']';
  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_LEX,
                              "]",  // context
                              NULL);  // source
  return MUSAICO_T_BRACKET_CLOSE;
}

static int musaico_lex_bracket_open (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // BRACKET_OPEN
  parser->next_char = -1;
  lvalp->MUSAICO_T_BRACKET_OPEN = '[';
  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_LEX,
                              "[",  // context
                              NULL);  // source
  return MUSAICO_T_BRACKET_OPEN;
}

static int musaico_lex_divided_by (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // DIVIDED_BY
  // parser->next_char = (something).
  lvalp->MUSAICO_T_DIVIDED_BY = '/';
  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_LEX,
                              "/",   // context
                              NULL);  // source
  return MUSAICO_T_DIVIDED_BY;
}

static int musaico_lex_comment_single_line (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // COMMENT_SINGLE_LINE
  char buffer[4096];
  buffer[0] = '/';
  int b = 1;
  while (parser->next_char != '\n'
         && parser->next_char != EOF) {
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = parser->next_char;
    b ++;
    parser->next_char = musaico_read_char(parser->fd, llocp);
  }
  if (b >= 4096) {
    return MUSAICO_T_YYerror; // !!! error;
  }
  buffer[b] = '\0';
  // parser->next_char = '\n';
  llocp->last_column --;
  char *semantics = (char *) malloc((b + 1) * sizeof(char));
  strcpy(semantics, buffer);  // Everything including the "//".
  lvalp->MUSAICO_T_COMMENT_SINGLE_LINE = semantics;
  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_LEX,
                              semantics,  // context
                              NULL);  // source
  return MUSAICO_T_COMMENT_SINGLE_LINE;
}

static int musaico_lex_dot (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // DOT
    parser->next_char = -1;
    lvalp->MUSAICO_T_DOT = '.';
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                ".",  // context
                                NULL);  // source
    return MUSAICO_T_DOT;
}

static int musaico_lex_equal (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // EQUAL
    parser->next_char = -1;
    lvalp->MUSAICO_T_EQUAL = '=';
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                "=",  // context
                                NULL);  // source
    return MUSAICO_T_EQUAL;
}

static int musaico_lex_float (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // FLOAT
  return MUSAICO_T_YYerror;  // !!!
}

static int musaico_lex_int (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // INT
  return MUSAICO_T_YYerror;  // !!!
}

static int musaico_lex_number (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // FLOAT
    // INT
    yytoken_kind_t token_kind = MUSAICO_T_INT;
    char buffer[4096];
    int b = 0;
    while (parser->next_char >= '0'
           && parser->next_char <= '9') {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = parser->next_char;
      b ++;
      parser->next_char = musaico_read_char(parser->fd, llocp);
      if (parser->next_char == '.') {
        if (token_kind == MUSAICO_T_FLOAT)
        {
          // Could be a range, something like 3.5..4.5, for example,
          // and we've stumbled on the first dot of the middle "..".
          break;
        }

        if (b >= 4096) {
          return MUSAICO_T_YYerror; // !!! error;
        }
        buffer[b] = parser->next_char;
        b ++;
        parser->next_char = musaico_read_char(parser->fd, llocp);
        if (parser->next_char < '0'
            || parser->next_char > '9') {
          return MUSAICO_T_YYerror; // !!! error;
        }
        if (b >= 4096) {
          return MUSAICO_T_YYerror; // !!! error;
        }
        buffer[b] = parser->next_char;
        b ++;
        token_kind = MUSAICO_T_FLOAT;
        parser->next_char = musaico_read_char(parser->fd, llocp);
      }
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // parser->next_char = (something).
    llocp->last_column --;
    if (token_kind == MUSAICO_T_FLOAT) {
      char *endptr = buffer;  // Point to start of buffer by default.
      long double semantics = strtold(buffer, &endptr);
      if (endptr != (buffer + b)  // Not all chars consumed in parse
          || semantics == HUGE_VALL  // Positive overflow
          || semantics == -HUGE_VALL  // Negative overflow
          || semantics == LDBL_MIN  // Positive underflow
          || semantics == -LDBL_MIN)  // Negative underflow
      {
        return MUSAICO_T_YYerror;  // !!! error
      }

      lvalp->MUSAICO_T_FLOAT = semantics;
      parser->musaico->trace_step(parser->musaico,
                                  MUSAICO_TRACE_LEX,
                                  "float",  // context
                                  NULL);  // source
    } else {
      char *endptr = buffer;  // Point to start of buffer by default.
      long long semantics = strtoll(buffer, &endptr, 10);
      if (endptr != (buffer + b))  // Not all chars consumed in parse
      {
        return MUSAICO_T_YYerror;  // !!! error
      }

      lvalp->MUSAICO_T_INT = semantics;
      parser->musaico->trace_step(parser->musaico,
                                  MUSAICO_TRACE_LEX,
                                  "int",  // context
                                  NULL);  // source
    }
    return token_kind;
}

static int musaico_lex_newline (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // NEWLINE
    llocp->last_line ++;
    llocp->last_column = 1;
    parser->next_char = -1;
    lvalp->MUSAICO_T_NEWLINE = '\n';
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                "\\n",  // context
                                NULL);  // source
    return MUSAICO_T_NEWLINE;
}

static int musaico_lex_id (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // ID
    char buffer[4096];
    int b = 0;
    while (is_id_char(parser->next_char)) {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = parser->next_char;
      b ++;
      parser->next_char = musaico_read_char(parser->fd, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // parser->next_char = (something).
    llocp->last_column --;
    char *semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_ID = semantics;
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                "id",  // context
                                NULL);  // source
    return MUSAICO_T_ID;
}

static int musaico_lex_semi_colon (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // SEMI_COLON
    parser->next_char = -1;
    lvalp->MUSAICO_T_SEMI_COLON = ';';
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                ";",  // context
                                NULL);  // source
    return MUSAICO_T_SEMI_COLON;
}

static int musaico_lex_string (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // STRING
    char buffer[4096];
    int b = 0;
    char delimiter = (char) parser->next_char;
    int is_backslash = 0;
    parser->next_char = -1;
    while (is_backslash == 1
           || parser->next_char != delimiter) {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = parser->next_char;
      b ++;
      if (is_backslash == 1) {
        is_backslash = 0;
      } else if (parser->next_char == '\\') {
        is_backslash = 1;
      } else {
        is_backslash = 0;
      }
      parser->next_char = musaico_read_char(parser->fd, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    parser->next_char = -1;
    char *semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_STRING = semantics;
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                "string",  // context
                                NULL);  // source
    return MUSAICO_T_STRING;
}

static int musaico_lex_times (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // TIMES
    parser->next_char = -1;
    lvalp->MUSAICO_T_TIMES = '*';
    parser->musaico->trace_step(parser->musaico,
                                MUSAICO_TRACE_LEX,
                                "*",  // context
                                NULL);  // source
    return MUSAICO_T_TIMES;
}

static int musaico_lex_whitespace (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
    // WHITESPACE
    char buffer[4096];
    int b = 0;
    while (parser->next_char == ' '
           || parser->next_char == '\r'
           || parser->next_char == '\t') {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = parser->next_char;
      b ++;
      parser->next_char = musaico_read_char(parser->fd, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // parser->next_char = (something).
    llocp->last_column --;
    char *semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_WHITESPACE = semantics;
    // Don't bother creating a trace_step() for whitespace.
    return MUSAICO_T_WHITESPACE;
}


// TODO write 100% coverage unit tests for yylex!  Yeesh.
static int yylex (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        musaico_parser_t *parser
        )
{
  // !!! check parser != NULL, internals look good.
  if (llocp->last_line != llocp->first_line
      || llocp->last_column != llocp->first_column) {
    llocp->first_line = llocp->last_line;
    llocp->first_column = llocp->last_column;
  }

  if (parser->next_char < 0) {
    parser->next_char = musaico_read_char(parser->fd, llocp);
    if (parser->next_char == EOF) {
      parser->musaico->trace_step(parser->musaico,
                                  MUSAICO_TRACE_LEX,
                                  "EOF",  // context
                                  NULL);  // source
      return MUSAICO_T_YYEOF;
    }
  } else {
    llocp->first_column ++;
    llocp->last_column ++;
  }

  if (parser->next_char == '}') {
    return musaico_lex_brace_close(lvalp, llocp, parser);
  } else if (parser->next_char == '{') {
    return musaico_lex_brace_open(lvalp, llocp, parser);
  } else if (parser->next_char == ']') {
    return musaico_lex_bracket_close(lvalp, llocp, parser);
  } else if (parser->next_char == '[') {
    return musaico_lex_bracket_open(lvalp, llocp, parser);
  } else if (parser->next_char == '/') {
    parser->next_char = musaico_read_char(parser->fd, llocp);
    if (parser->next_char == '/') {
      return musaico_lex_comment_single_line(lvalp, llocp, parser);
    }
    else
    {
      return musaico_lex_divided_by(lvalp, llocp, parser);
    }
  } else if (parser->next_char == '.') {
    return musaico_lex_dot(lvalp, llocp, parser);
  } else if (parser->next_char == '=') {
    return musaico_lex_equal(lvalp, llocp, parser);
  } else if (parser->next_char >= '0'
             && parser->next_char <= '9') {
    return musaico_lex_number(lvalp, llocp, parser);
  } else if (is_id_first_char(parser->next_char)) {
    return musaico_lex_id(lvalp, llocp, parser);
  } else if (parser->next_char == '\n') {
    return musaico_lex_newline(lvalp, llocp, parser);
  } else if (parser->next_char == ';') {
    return musaico_lex_semi_colon(lvalp, llocp, parser);
  } else if (parser->next_char == '"'
             || parser->next_char == '\'') {
    return musaico_lex_string(lvalp, llocp, parser);
  } else if (parser->next_char == '*') {
    return musaico_lex_times(lvalp, llocp, parser);
  } else if (parser->next_char == ' '
             || parser->next_char == '\r'
             || parser->next_char == '\t') {
    return musaico_lex_whitespace(lvalp, llocp, parser);
  }
  else {
    return MUSAICO_T_YYUNDEF; // !!! error
  }
}

static void yyerror (
        YYLTYPE *locp,
        musaico_parser_t *parser,
        char const *msg
        )
{
  fprintf(stderr, "ERROR Musaico parser (%d.%d-%d.%d): %s\n",
          locp->first_line,
          locp->first_column,
          locp->last_line,
          locp->last_column,
          msg);
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
musaico_error_t *musaico_parse(
        musaico_parser_t *parser
        )
{
  if (parser == NULL
      || parser->musaico == NULL)
  {
    return MUSAICO_NULL_POINTER;
  }

  int parse_result;

  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_ENTER,
                              "musaico_parse()",
                              NULL);  // source

  parse_result = yyparse(parser);

  parser->musaico->trace_step(parser->musaico,
                              MUSAICO_TRACE_EXIT,
                              "musaico_parse()",
                              NULL);  // source

  if (parse_result == 0)
  {
    return MUSAICO_OK;
  }
  else if (parse_result == 2)
  {
    return MUSAICO_ALLOCATE_FAILED;  // Close enough.
  }

  // !!! TODO error capture.
  return MUSAICO_NULL_POINTER; // !!!!!!
}
