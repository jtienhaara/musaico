#ifndef MUSAICO_LANGUAGE_H_INCLUDED
#define MUSAICO_LANGUAGE_H_INCLUDED

#include "musaico.h"

typedef struct _STRUCT_musaico_comment          musaico_comment_t;
typedef struct _STRUCT_musaico_constructor      musaico_constructor_t;
typedef struct _STRUCT_musaico_expression       musaico_expression_t;
typedef struct _STRUCT_musaico_id               musaico_id_t;
typedef struct _STRUCT_musaico_literal          musaico_literal_t;
typedef enum _ENUM_musaico_operator             musaico_operator_t;
typedef struct _STRUCT_musaico_operation        musaico_operation_t;
typedef struct _STRUCT_musaico_syntax           musaico_syntax_t;


struct _STRUCT_musaico_comment
{
  musaico_syntax_t *node;
  char *comment;
};

struct _STRUCT_musaico_constructor
{
  musaico_syntax_t *node;
  musaico_expression_t *parent;
  musaico_id_t *id;
  musaico_syntax_t *body;
};

struct _STRUCT_musaico_id
{
  musaico_syntax_t *node;
  musaico_expression_t *parent;
  char *fully_qualified;
};

struct _STRUCT_musaico_literal
{
  musaico_syntax_t *node;
  musaico_expression_t *parent;
  union
  {
    char *v_string;
    long v_int;
    double v_float;
  };
  int string_length;
  musaico_literal_t *prev;  // 1, 2, 3, 4, 5
  musaico_literal_t *next;  // "Hello", "brave", "new", "world"
};

struct _STRUCT_musaico_expression
{
  musaico_syntax_t *node;
  union
  {
    musaico_constructor_t constructor;
    musaico_id_t id;
    musaico_literal_t literal;
  } expression;
  musaico_operation_t *prev;  // expr *expr *expr...
  musaico_operation_t *next;  // expr - expr + expr...
};

struct _STRUCT_musaico_syntax
{
  musaico_t *musaico;
  musaico_source_t *source;  // Musaico language source code context.

  union
  {
    musaico_comment_t comment;
    musaico_expression_t expression;
  } node;

  musaico_syntax_t *parent;  // Parent node in the syntax tree.
  musaico_syntax_t *prev;  // Previous sibling, under the same parent.
  musaico_syntax_t *next;  // Next sibling, under the same parent.
  musaico_syntax_t *child;  // First child node in the syntax tree.
};

enum _ENUM_musaico_operator
{
  MUSAICO_OP_AND,  // "&"
  MUSAICO_OP_AND_AND,  // "&&"
  MUSAICO_OP_ASSIGN,  // "="
  MUSAICO_OP_ASSIGN_AND,  // "&="
  MUSAICO_OP_ASSIGN_DECREMENT,  // "--"
  MUSAICO_OP_ASSIGN_DIVIDE,  // "/="
  MUSAICO_OP_ASSIGN_INCREMENT,  // "++"
  MUSAICO_OP_ASSIGN_MINUS,  // "-="
  MUSAICO_OP_ASSIGN_MODULO,  // "%="
  MUSAICO_OP_ASSIGN_OR,  // "|="
  MUSAICO_OP_ASSIGN_PLUS,  // "+="
  MUSAICO_OP_ASSIGN_POWER,  // "**="
  MUSAICO_OP_ASSIGN_TIMES,  // "*="
  MUSAICO_OP_ASSIGN_XOR,  // "^="
  MUSAICO_OP_AT,  // "@"
  MUSAICO_OP_COLON,  // ":"
  MUSAICO_OP_COMMA,  // ","
  MUSAICO_OP_DIVIDED_BY,  // "/"
  MUSAICO_OP_DOLLAR,  // "$"
  MUSAICO_OP_EQUALS,  // "=="
  MUSAICO_OP_GREATER_THAN,  // ">"
  MUSAICO_OP_GREATER_THAN_OR_EQUALS,  // ">="
  MUSAICO_OP_HASH,  // "#"
  MUSAICO_OP_MATCHES,  // "~"
  MUSAICO_OP_LESS_THAN,  // "<"
  MUSAICO_OP_LESS_THAN_OR_EQUALS,  // "<="
  MUSAICO_OP_MINUS,  // "-"
  MUSAICO_OP_MODULO,  // "%"
  MUSAICO_OP_NOT,  // "!"
  MUSAICO_OP_NOT_EQUALS,  // "!="
  MUSAICO_OP_OR,  // "|"
  MUSAICO_OP_OR_OR,  // "||"
  MUSAICO_OP_PLUS,  // "+"
  MUSAICO_OP_POWER,  // "**"
  MUSAICO_OP_QUESTION,  // "?"
  MUSAICO_OP_TIMES,  // "*"
  MUSAICO_OP_XOR  // "^"
};

struct _STRUCT_musaico_operation
{
  musaico_syntax_t *node;
  musaico_operator_t operator;
  musaico_expression_t *left;
  musaico_expression_t *right;
};

#endif // MUSAICO_LANGUAGE_H_INCLUDED
