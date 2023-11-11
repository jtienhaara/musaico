#ifndef MUSAICO_LANGUAGE_H_INCLUDED
#define MUSAICO_LANGUAGE_H_INCLUDED

#include "musaico.h"

typedef struct _STRUCT_musaico_comment_t        musaico_comment_t;
typedef struct _STRUCT_musaico_constructor_t    musaico_constructor_t;
typedef struct _STRUCT_musaico_expression_t     musaico_expression_t;
typedef struct _STRUCT_musaico_id_t             musaico_id_t;
typedef struct _STRUCT_musaico_literal_t        musaico_literal_t;
typedef enum _ENUM_musaico_operator_t           musaico_operator_t;
typedef struct _STRUCT_musaico_operation_t      musaico_operation_t;
typedef struct _STRUCT_musaico_syntax_t         musaico_syntax_t;


struct _STRUCT_musaico_comment
{
  musaico_syntax_t * node;
  char * comment;
};

struct _STRUCT_musaico_constructor
{
  musaico_syntax_t * node;
  musaico_expression_t * parent;
  musaico_id_t * id;
  musaico_syntax_t * body;
};

struct _STRUCT_musaico_id
{
  musaico_syntax_t * node;
  musaico_expression_t * parent;
  char * fully_qualified;
};

struct _STRUCT_musaico_literal
{
  musaico_syntax_t * node;
  musaico_expression_t * parent;
  union
  {
    char * v_string;
    long v_int;
    double v_float;
  };
  int string_length;
  musaico_literal_t * prev;  // 1, 2, 3, 4, 5
  musaico_literal_t * next;  // "Hello", "brave", "new", "world"
};

struct _STRUCT_musaico_expression
{
  musaico_syntax_t * node;
  union
  {
    struct _STRUCT_musaico_constructor constructor;
    struct _STRUCT_musaico_id id;
    struct _STRUCT_musaico_literal literal;
  } expression;
  musaico_operation_t * prev;  // expr * expr * expr...
  musaico_operation_t * next;  // expr - expr + expr...
};

struct _STRUCT_musaico_syntax
{
  musaico_t * musaico;
  musaico_source_t * source;  // Musaico language source code context.

  union
  {
    struct _STRUCT_musaico_comment comment;
    struct _STRUCT_musaico_expression expression;
  } node;

  musaico_syntax_t * parent;  // Parent node in the syntax tree.
  musaico_syntax_t * prev;  // Previous sibling, under the same parent.
  musaico_syntax_t * next;  // Next sibling, under the same parent.
  musaico_syntax_t * child;  // First child node in the syntax tree.
};

enum _ENUM_musaico_operator
{
  AND,  // "&"
  AND_AND,  // "&&"
  ASSIGN,  // "="
  ASSIGN_AND,  // "&="
  ASSIGN_DECREMENT,  // "--"
  ASSIGN_DIVIDE,  // "/="
  ASSIGN_INCREMENT,  // "++"
  ASSIGN_MINUS,  // "-="
  ASSIGN_MODULO,  // "%="
  ASSIGN_OR,  // "|="
  ASSIGN_PLUS,  // "+="
  ASSIGN_POWER,  // "**="
  ASSIGN_TIMES,  // "*="
  ASSIGN_XOR,  // "^="
  AT,  // "@"
  COLON,  // ":"
  COMMA,  // ","
  DIVIDED_BY,  // "/"
  DOLLAR,  // "$"
  EQUALS,  // "=="
  GREATER_THAN,  // ">"
  GREATER_THAN_OR_EQUALS,  // ">="
  HASH,  // "#"
  MATCHES,  // "~"
  LESS_THAN,  // "<"
  LESS_THAN_OR_EQUALS,  // "<="
  MINUS,  // "-"
  MODULO,  // "%"
  NOT,  // "!"
  NOT_EQUALS,  // "!="
  OR,  // "|"
  OR_OR,  // "||"
  PLUS,  // "+"
  POWER,  // "**"
  QUESTION,  // "?"
  TIMES,  // "*"
  XOR  // "^"
};

struct _STRUCT_musaico_operation
{
  musaico_syntax_t *node;
  enum _ENUM_musaico_operator operator;
  musaico_expression_t * left;
  musaico_expression_t * right;
};

#endif // MUSAICO_LANGUAGE_H_INCLUDED
