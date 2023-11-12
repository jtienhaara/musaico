#ifndef MUSAICO_H_INCLUDED
#define MUSAICO_H_INCLUDED

#include <stdbool.h>
#include <time.h>

typedef struct _STRUCT_musaico                  musaico_t;
typedef struct _STRUCT_musaico_error            musaico_error_t;
typedef struct _STRUCT_musaico_gc               musaico_gc_t;
typedef struct _STRUCT_musaico_gc_reference     musaico_gc_reference_t;
typedef struct _STRUCT_musaico_source           musaico_source_t;
typedef struct _STRUCT_musaico_source_file      musaico_source_file_t;
typedef struct _STRUCT_musaico_trace            musaico_trace_t;
typedef enum _ENUM_musaico_trace_type           musaico_trace_type_t;


struct _STRUCT_musaico_source_file
{
  char *path;
};


struct _STRUCT_musaico_source
{
  union {
    musaico_source_file_t *file;
    // Maybe future socket etc. if needed.
  };

  int line_num;  // Starts at 1
  int char_start;  // Starts at 0
  int char_end;
};


struct _STRUCT_musaico_error
{
  bool is_ok;  // If true, everything else can be ignored.

  char *message;

  //
  // Handles errors by executing the default Musaico error handler.
  // For example, to use the default Musaico error handler
  // on the result of an allocate() operation:
  //     musaico_t *musaico = ...;
  //     char *buffer;
  //     musaico->allocate(musaico, &buffer)->or_fail(musaico)
  //
  void (*or_fail) (
          musaico_t *musaico
          );
};


enum _ENUM_musaico_trace_type
{
  MUSAICO_TRACE_ANALYSE,  // Semantic analysis of Musaico language code.
  MUSAICO_TRACE_COMPILE,  // Compiling Musaico language into other forms.
  MUSAICO_TRACE_ENTER,  // Entering a procedure in Musaico C.  Also PUSH.
  MUSAICO_TRACE_EXIT,  // Exiting a procedure in Musaico C.  Also a POP.
  MUSAICO_TRACE_INFO,  // Miscellaneous info in the Musaico stack trace.
  MUSAICO_TRACE_LEX,  // Lexical analysis of Musaico language code.
  MUSAICO_TRACE_LINT,  // Linting of Musaico language code.
  MUSAICO_TRACE_OPTIMISE,  // Optimising of Musaico language code.
  MUSAICO_TRACE_PARSE,  // Syntactical parsing of Musaico language code.
  MUSAICO_TRACE_POP,  // Miscellaneous pop of the Musaico stack trace.
  MUSAICO_TRACE_PUSH,  // Miscellaneous push of the Musaico stack trace.
  MUSAICO_TRACE_RUNTIME,  // Musaico language runtime after compilation.

  MUSAICO_TRACE_NONE  // Do not create traces with trace_type == NONE.
};


struct _STRUCT_musaico_trace
{
  musaico_t *musaico;  // The Musaico context, including default error handler.

  musaico_trace_type_t trace_type;  // ENTER, EXIT, PARSE, ANALYSE, etc.
  char *context;  // Context without Musaico language source code.
  musaico_source_t *source;  // Musaico language source code context.

  int step_num;  // The n'th trace element in musaico_t.

  // Mutable:
  musaico_error_t *error;

  musaico_trace_t *parent;  // Trace in the parent stack frame.
  musaico_trace_t *prev;  // Previous trace in same stack frame.
  musaico_trace_t *next;  // Next trace in same stack frame.
  musaico_trace_t *child;  // First trace in a child stack frame.
};


//
// Everything is hunky-dory, no error:
//
extern musaico_error_t *MUSAICO_OK;
extern musaico_error_t *MUSAICO_ALLOCATE_FAILED;
extern musaico_error_t *MUSAICO_NULL_POINTER;

struct _STRUCT_musaico_gc_reference
{
  musaico_gc_t *referer;  // The chunk that refers to a memory chunk.
  musaico_gc_t *reference;  // The single memory chunk that is referenced.
  musaico_gc_reference_t *twin;  // One instance in referer, one in reference.

  // As soon as a memory chunk has no more references to it,
  // it can be garbage collected.
  musaico_gc_reference_t *prev;
  musaico_gc_reference_t *next;
};


struct _STRUCT_musaico_gc
{
  musaico_t *musaico;

  char *type_name;
  void *pointer;
  size_t size;

  musaico_gc_t *prev;  // Previous memory allocation chunk.
  musaico_gc_t *next;  // Next memory allocation chunk.

  musaico_gc_reference_t *refers_to;  // First reference to a gc item.
  musaico_gc_reference_t *referred_from;  // First reference from a gc item.
  // Once there are no referred_from entries left, can be garbage collected.
};


struct _STRUCT_musaico
{
  musaico_t *parent;  // If threaded, each thread has its own state.
  char *name;

  // Mutable:
  musaico_trace_t *first_trace;  // Start of trace.
  musaico_trace_t *trace;  // Current trace, error thrown, MUSAICO_OK, etc.
  musaico_gc_t *gc;  // Memory allocations can be garbage collected.

  //
  // Allocates the specified amount of memory.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t *musaico = ...;
  //     char *buffer;
  //     musaico->allocate(musaico, "string", &buffer, 1024)->or_fail();
  //     strcpy(buffer, "Hello, world!");
  //     ...
  //     musaico->destroy(musaico, "string", buffer)->or_fail();
  //
  musaico_error_t *(*allocate) (
          musaico_t *musaico,
          char *type_name,
          void **pointer_pointer,
          size_t size
          );

  //
  // Frees the specified memory region.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t *musaico = ...;
  //     char *buffer;
  //     musaico->allocate(musaico, &buffer, 1024)->or_fail();
  //     strcpy(buffer, "Hello, world!");
  //     ...
  //     musaico->destroy(musaico, buffer)->or_fail();
  //
  musaico_error_t *(*destroy) (
          musaico_t *musaico,
          char *type_name,
          void *pointer
          );

  //
  // Starts Musaico, preparing data structures and so on
  // that will be used until stop() is called.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t *musaico = ...;
  //     musaico->start(musaico)->or_fail();
  //     ...
  //     musaico->stop(musaico)->or_fail();
  //
  musaico_error_t *(*start) (
          musaico_t *musaico
          );

  //
  // Stops Musaico, cleaning up data structures prepared by start(),
  // possibly throwing errors (for example, if allocated memory
  // is being tracked, and not all allocated pointers have been
  // destroyed).
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     musaico_t *musaico = ...;
  //     musaico->start(musaico)->or_fail();
  //     ...
  //     musaico->stop(musaico)->or_fail();
  //
  musaico_error_t *(*stop) (
          musaico_t *musaico
          );

  //
  // Stores the current time in the specified time_t.
  // Returns M_OK on success, error details otherwise.
  // Example:
  //     // Retrieve the time, according to Musaico, then, sometime
  //     // later, retrieve it again.
  //     musaico_t *musaico = ...;
  //     time_t time;
  //     musaico->time(musaico, &time)->or_fail();
  //     ...
  //     musaico->time(musaico, &time)->or_fail();
  //
  musaico_error_t *(*time) (
          musaico_t *musaico,
          time_t *tloc
          );

  //
  // Prints full trace sequence and stack.
  //
  // Example:
  //     #include <unistd.h>
  //     ...
  //     musaico_t *musaico = ...;
  //     ...
  //     musaico->trace_print(musaico,
  //                          STDOUT_FILENO);
  //
  musaico_error_t *(*trace_print) (
          musaico_t *musaico,
          int fd
          );

  //
  // Adds a trace element to the current stack trace.
  //
  musaico_error_t *(*trace_step) (
          musaico_t *musaico,
          musaico_trace_type_t trace_type,
          char *context,  // NULL if source is non-NULL.
          musaico_source_t *source  // Null if context is non-NULL.
          );
};

// Creates a default, toplevel Musaico structure.
extern musaico_t *musaico_create(
        char *name
        );

#endif // MUSAICO_H_INCLUDED
