#ifndef MUSAICO_LANGUAGE_H_INCLUDED
#define MUSAICO_LANGUAGE_H_INCLUDED

#include "musaico.h"

typedef struct _STRUCT_musaico_parser           musaico_parser_t;


struct _STRUCT_musaico_parser
{
  // Immutable:
  musaico_t *musaico;

  // Mutable:
  int fd;  // Open file descriptor of the file, socket, etc being parsed.
  int next_char;  // Lookahead character, in case we read too far.
};

extern musaico_error_t *musaico_parse_file(
        musaico_t *musaico,
        char *path
        );

#endif // MUSAICO_LANGUAGE_H_INCLUDED
