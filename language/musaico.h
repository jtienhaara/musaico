#ifndef MUSAICO_H_INCLUDED
#define MUSAICO_H_INCLUDED

#include <stdbool.h>
#include <time.h>

int musaico_parse(
        int fd
        );

int musaico_parse_file(
        char *filename
        );

typedef struct _STRUCT_musaico                  musaico_t;
typedef struct _STRUCT_musaico_error            musaico_error_t;
typedef struct _STRUCT_musaico_source           musaico_source_t;


struct _STRUCT_musaico_source
{
  char * path;
  int line_num;  // Starts at 1
  int char_start;  // Starts at 0
  int char_end;
};


struct _STRUCT_musaico_error
{
  bool is_ok;  // If true, everything else can be ignored.

  musaico_t * musaico;  // The Musaico context, including default error handler.

  char * message;
  char * context;  // Context for errors with no Musaico language source code.
  musaico_source_t * source;  // Musaico language source code context.

  //
  // Handles errors by executing the default Musaico error handler.
  // For example, to use the default Musaico error handler
  // on the result of an allocate() operation:
  //     musaico_t * musaico = ...;
  //     char * buffer;
  //     musaico->allocate(musaico, &buffer)->or_fail()
  //
  void (*or_fail) ();
};

//
// Everything is hunky-dory, no error:
//
extern musaico_error_t * MUSAICO_OK;


struct _STRUCT_musaico
{
  //
  // Allocates the specified amount of memory.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t * musaico = ...;
  //     char * buffer;
  //     musaico->allocate(musaico, &buffer, 1024)->or_fail();
  //     strcpy(buffer, "Hello, world!");
  //     ...
  //     musaico->destroy(musaico, &buffer)->or_fail();
  //
  musaico_error_t * (*allocate) (
          musaico_t * musaico,
          void * pointer,
          size_t size
          );

  //
  // Frees the specified memory region.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t * musaico = ...;
  //     char * buffer;
  //     musaico->allocate(musaico, &buffer, 1024)->or_fail();
  //     strcpy(buffer, "Hello, world!");
  //     ...
  //     musaico->destroy(musaico, &buffer)->or_fail();
  //
  musaico_error_t * (*destroy) (
          musaico_t * musaico,
          void * pointer
          );

  //
  // Starts Musaico, preparing data structures and so on
  // that will be used until stop() is called.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t * musaico = ...;
  //     musaico->start(musaico)->or_fail();
  //     ...
  //     musaico->stop(musaico)->or_fail();
  //
  musaico_error_t * (*start) (
          musaico_t * musaico
          );

  //
  // Stops Musaico, cleaning up data structures prepared by start(),
  // possibly throwing errors (for example, if allocated memory
  // is being tracked, and not all allocated pointers have been
  // destroyed).
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t * musaico = ...;
  //     musaico->start(musaico)->or_fail();
  //     ...
  //     musaico->stop(musaico)->or_fail();
  //
  musaico_error_t * (*stop) (
          musaico_t * musaico
          );

  //
  // Stores the current time in the specified time_t.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     // Retrieve the time, according to Musaico, then, sometime
  //     // later, retrieve it again.
  //     musaico_t * musaico = ...;
  //     time_t time;
  //     musaico->time(musaico, &time)->or_fail();
  //     ...
  //     musaico->time(musaico, &time)->or_fail();
  //
  musaico_error_t * (*time) (
      musaico_t * musaico,
      time_t *tloc
      );
};

#endif // MUSAICO_H_INCLUDED
