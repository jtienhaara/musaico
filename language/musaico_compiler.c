#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>  // For STDOUT_FILENO.

#include "musaico.h"
#include "musaico_parser.h"


int main(
        int argc,
        char *argv[]
        )
{
  if (argc <= 1) {
    fprintf(stderr, "Usage: %s (source.musaico)...\n",
            argv[0]);
    return 99;
  }

  printf("Compiling Musaico source files...\n");
  bool is_compiled = false;

  musaico_t *musaico = musaico_create("musaico");
  musaico->start(musaico)->or_fail(musaico);

  bool is_parsed = true;
  for (int a = 1; a < argc; a ++) {
    printf("  Compiling %s:\n", argv[a]);
    musaico_error_t *error = musaico_parse_file(musaico, argv[a]);
    if (error == NULL) {
        fprintf(stderr, "ERROR Failed to parse %s: NULL error returned.\n",
                argv[a]);
        return EXIT_FAILURE;
    }
    else if (error->is_ok != true)
    {
      fprintf(stderr, "ERROR Failed to parse %s: %s\n",
              argv[a],
              error->message);
      return EXIT_FAILURE;
    }
  }

  musaico->stop(musaico)->or_fail(musaico);

  musaico->trace_print(musaico, STDOUT_FILENO);

  printf("SUCCESS Compiling Musaico source files...\n");
  return EXIT_SUCCESS;
}
