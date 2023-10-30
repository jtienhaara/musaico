#include <stdio.h>

#include "musaico.h"

int main(
        int argc,
        char * argv[]
        )
{
  if (argc <= 1) {
    fprintf(stderr, "Usage: %s (source.musaico)...\n",
            argv[0]);
    return 99;
  }

  printf("Compiling Musaico source files...\n");

  for (int a = 1; a < argc; a ++) {
    printf("  Compiling %s:\n", argv[a]);
    int parse_result = musaico_parse_file(argv[a]);
    if (parse_result != 0) {
      if (parse_result == 1) {
        fprintf(stderr, "ERROR Failed to parse %s due to syntax error(s).\n",
                argv[a]);
      } else if (parse_result == 2) {
        fprintf(stderr, "ERROR Failed to parse %s due to memory exhaustion.\n",
                argv[a]);
      } else {
        fprintf(stderr, "ERROR Failed to parse %s due to unknown error code %d.\n",
                argv[a],
                parse_result);
      }

      return parse_result;
    }
  }

  printf("SUCCESS Compiling Musaico source files...\n");
  return 0;
}
