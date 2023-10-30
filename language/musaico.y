%{
  #include <stdio.h>
  #include <fcntl.h>
  #include <malloc.h>
  #include <stdlib.h>
  #include <string.h>
  #include <unistd.h>

  #include "musaico.h"
%}

%code provides {
  static int yylex (
          YYSTYPE *lvalp,
          YYLTYPE *llocp,
          int fd,
          int *next_char
          );
  static void yyerror (
          YYLTYPE *locp,
          int fd,
          int *next_char,
          char const *msg
          );
}

%language "C"
%locations
%define api.pure full
%define api.symbol.prefix {MUSAICO_S_}
%define api.token.prefix {MUSAICO_T_}
%define parse.error detailed
%param {int fd}
%param {int *next_char}

%start musaico_syntax

%define api.value.type union /* Generate YYSTYPE from these types: */
%token <char>       BRACE_CLOSE                 '}'
%token <char>       BRACE_OPEN                  '{'
%token <char>       BRACKET_CLOSE               ']'
%token <char>       BRACKET_OPEN                '['
%token <char *>     COMMENT_SINGLE_LINE         // // comment etc...
%token <char>       DOT                         '.'
%token <char>       EQUAL                       '='
%token <double>     FLOAT                       // [0-9]+(\.[0-9]+)?
%token <char *>     ID                          // xyz etc
%token <long>       INT                         // [0-9]+
%token <char>       NEWLINE                     '\n'
%token <char>       SEMI_COLON                  ';'
%token <char *>     STRING                      // 'xyz' or "xyz" etc
%token <char>       TIMES                       '*'
%token <char *>     WHITESPACE                  // [ \t\r]


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
        char prev_char,
        YYLTYPE *llocp
        )
{
  char next_char;
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

// TODO write 100% coverage unit tests for yylex!  Yeesh.
static int yylex (
        YYSTYPE *lvalp,
        YYLTYPE *llocp,
        int fd,
        int *next_char
        )
{
  if (llocp->last_line != llocp->first_line
      || llocp->last_column != llocp->first_column) {
    llocp->first_line = llocp->last_line;
    llocp->first_column = llocp->last_column;
  }

  if (*next_char < 0) {
    *next_char = musaico_read_char(fd, '\0', llocp);
    if (*next_char == EOF) {
      return MUSAICO_T_YYEOF;
    }
  } else {
    llocp->first_column ++;
    llocp->last_column ++;
  }

  if (*next_char == '}') {
    // BRACE_CLOSE
    *next_char = -1;
    lvalp->MUSAICO_T_BRACE_CLOSE = '}';
    return MUSAICO_T_BRACE_CLOSE;
  } else if (*next_char == '{') {
    // BRACE_OPEN
    *next_char = -1;
    lvalp->MUSAICO_T_BRACE_OPEN = '{';
    return MUSAICO_T_BRACE_OPEN;
  } else if (*next_char == ']') {
    // BRACKET_CLOSE
    *next_char = -1;
    lvalp->MUSAICO_T_BRACKET_CLOSE = ']';
    return MUSAICO_T_BRACKET_CLOSE;
  } else if (*next_char == '[') {
    // BRACKET_OPEN
    *next_char = -1;
    lvalp->MUSAICO_T_BRACKET_OPEN = '[';
    return MUSAICO_T_BRACKET_OPEN;
  } else if (*next_char == '/') {
    *next_char = musaico_read_char(fd, *next_char, llocp);
    if (*next_char != '/') {
      return MUSAICO_T_YYerror; // !!! error
    }
    // COMMENT_SINGLE_LINE
    char buffer[4096];
    buffer[0] = '/';
    int b = 1;
    while (*next_char != '\n'
           && *next_char != EOF) {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = *next_char;
      b ++;
      *next_char = musaico_read_char(fd, *next_char, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // *next_char = '\n';
    llocp->last_column --;
    char * semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_COMMENT_SINGLE_LINE = semantics;
    return MUSAICO_T_COMMENT_SINGLE_LINE;
  } else if (*next_char == '.') {
    // DOT
    *next_char = -1;
    lvalp->MUSAICO_T_DOT = '.';
    return MUSAICO_T_DOT;
  } else if (*next_char == '=') {
    // EQUAL
    *next_char = -1;
    lvalp->MUSAICO_T_EQUAL = '=';
    return MUSAICO_T_EQUAL;
  } else if (*next_char >= '0'
             && *next_char <= '9') {
    // FLOAT
    // INT
    yytoken_kind_t token_kind = MUSAICO_T_INT;
    char buffer[4096];
    int b = 0;
    while (*next_char >= '0'
           && *next_char <= '9') {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = *next_char;
      b ++;
      *next_char = musaico_read_char(fd, *next_char, llocp);
      if (*next_char == '.') {
        if (b >= 4096) {
          return MUSAICO_T_YYerror; // !!! error;
        }
        buffer[b] = *next_char;
        b ++;
        *next_char = musaico_read_char(fd, *next_char, llocp);
        if (*next_char < '0'
            || *next_char > '9') {
          return MUSAICO_T_YYerror; // !!! error;
        }
        if (b >= 4096) {
          return MUSAICO_T_YYerror; // !!! error;
        }
        buffer[b] = *next_char;
        b ++;
        token_kind = MUSAICO_T_FLOAT;
        *next_char = musaico_read_char(fd, *next_char, llocp);
      }
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // *next_char = (something).
    llocp->last_column --;
    if (token_kind == MUSAICO_T_FLOAT) {
      double semantics = atof(buffer);
      lvalp->MUSAICO_T_FLOAT = semantics;
    } else {
      long semantics = atol(buffer);
      lvalp->MUSAICO_T_INT = semantics;
    }
    return token_kind;
  } else if (is_id_first_char(*next_char)) {
    // ID
    char buffer[4096];
    int b = 0;
    while (is_id_char(*next_char)) {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = *next_char;
      b ++;
      *next_char = musaico_read_char(fd, *next_char, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // *next_char = (something).
    llocp->last_column --;
    char * semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_ID = semantics;
    return MUSAICO_T_ID;
  } else if (*next_char == '\n') {
    // NEWLINE
    llocp->last_line ++;
    llocp->last_column = 1;
    *next_char = -1;
    lvalp->MUSAICO_T_NEWLINE = '\n';
    return MUSAICO_T_NEWLINE;
  } else if (*next_char == ';') {
    // SEMI_COLON
    *next_char = -1;
    lvalp->MUSAICO_T_SEMI_COLON = ';';
    return MUSAICO_T_SEMI_COLON;
  } else if (*next_char == '"'
             || *next_char == '\'') {
    // STRING
    char buffer[4096];
    int b = 0;
    char delimiter = (char) *next_char;
    int is_backslash = 0;
    *next_char = -1;
    while (is_backslash == 1
           || *next_char != delimiter) {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = *next_char;
      b ++;
      if (is_backslash == 1) {
        is_backslash = 0;
      } else if (*next_char == '\\') {
        is_backslash = 1;
      } else {
        is_backslash = 0;
      }
      *next_char = musaico_read_char(fd, *next_char, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    *next_char = -1;
    char * semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_STRING = semantics;
    return MUSAICO_T_STRING;
  } else if (*next_char == '*') {
    // TIMES
    *next_char = -1;
    lvalp->MUSAICO_T_TIMES = '*';
    return MUSAICO_T_TIMES;
  } else if (*next_char == ' '
             || *next_char == '\r'
             || *next_char == '\t') {
    // WHITESPACE
    char buffer[4096];
    int b = 0;
    while (*next_char == ' '
           || *next_char == '\r'
           || *next_char == '\t') {
      if (b >= 4096) {
        return MUSAICO_T_YYerror; // !!! error;
      }
      buffer[b] = *next_char;
      b ++;
      *next_char = musaico_read_char(fd, *next_char, llocp);
    }
    if (b >= 4096) {
      return MUSAICO_T_YYerror; // !!! error;
    }
    buffer[b] = '\0';
    // *next_char = (something).
    llocp->last_column --;
    char * semantics = (char *) malloc((b + 1) * sizeof(char));
    strcpy(semantics, buffer);
    lvalp->MUSAICO_T_WHITESPACE = semantics;
    return MUSAICO_T_WHITESPACE;
  }
  else {
    return MUSAICO_T_YYUNDEF; // !!! error
  }
}

static void yyerror (
        YYLTYPE *locp,
        int fd,
        int *next_char,
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
int musaico_parse(
        int fd
        )
{
  int next_char = -1;
  int parse_result = yyparse(fd, &next_char);
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
