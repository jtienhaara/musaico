#include <stdio.h>
#include <fcntl.h>  // For open()
#include <stdlib.h>
#include <unistd.h>  // For close()

#include "musaico.h"
#include "musaico_parser.h"


musaico_error_t *musaico_parse(  // musaico.y
        musaico_parser_t *parser
        );


musaico_error_t *musaico_parse_file(
        musaico_t *musaico,
        char *path
        )
{
  if (musaico == NULL)
  {
    return MUSAICO_NULL_POINTER;
  }
  else if (path == NULL)
  {
    // !!! return create new error
    return MUSAICO_NULL_POINTER;
  }

  musaico->trace_step(musaico,
                      MUSAICO_TRACE_ENTER,
                      "musaico_parse_file()",
                      NULL);  // source

  musaico_parser_t *parser;
  musaico->allocate(musaico,
                    "musaico_parser_t",
                    (void **) &parser,
                    sizeof(musaico_parser_t))->or_fail(musaico);

  parser->musaico = musaico;
  parser->fd = open(path, O_RDONLY);
  parser->next_char = -1;

  if (parser->fd < 0)
  {
    // !!! return create new error
    return MUSAICO_NULL_POINTER;
  }

  musaico_error_t *error = musaico_parse(parser);

  close(parser->fd);

  musaico->destroy(musaico,
                   "musaico_parser_t",
                   parser);

  musaico->trace_step(musaico,
                      MUSAICO_TRACE_EXIT,
                      "musaico_parse_file()",
                      NULL);  // source

  return error;
}
