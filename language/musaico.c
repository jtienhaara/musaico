#include <stdio.h>
#include <malloc.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#include "musaico.h"


// Should only be used by MUSAICO_OK to efficiently not fail.
// Will drop all errors if used unwisely!
void musaico_never_fail(
        musaico_t *musaico
        )
{
}


// Prints an error to stderr, then exits with the specified error code.
void musaico_fail_exit(
        char *proc_name,  // If non-NULL then "ERROR proc_name():"
        char *message,
        ...
        )
{
  // printf-style format plus variable arguments:
  // Copy-pasta'ed from gnu's man page (man 2 printf), then tweaked a bit.
  int n = 0;
  va_list ap;
  va_start(ap, message);
  n = vsnprintf(NULL, (size_t) 0, message, ap);
  va_end(ap);

  char *formatted;
  if (n < 0)
  {
    formatted = message;
  }
  else
  {
    size_t size = (size_t) n + 1;  // +1 '\0'.
    formatted = (char *) malloc(size);
    if (formatted == NULL)
    {
      formatted = message;
    }
    else
    {
      va_start(ap, message);
      n = vsnprintf(formatted, size, message, ap);
      va_end(ap);
    }
  }

  if (proc_name == NULL)
  {
    fprintf(stderr, "ERROR %s\n", formatted);
  }
  else
  {
    fprintf(stderr, "ERROR %s(): %s\n", proc_name, formatted);
  }

  exit(EXIT_FAILURE);
}


// Fails if the last Musaico error trace contains an error code.
// Prints an error to stderr.
void musaico_fail_on_error(
        musaico_t *musaico
        )
{
  if (musaico == NULL)
  {
    musaico_fail_exit("musaico_fail_on_error",
                      "musaico == NULL");
    return;  // Unreachable.
  }

  musaico_trace_t *trace = musaico->trace;
  if (trace == NULL)
  {
    return;
  }

  musaico_error_t *error = trace->error;
  if (error == NULL)
  {
    return;
  }
  else if (error->is_ok == true)
  {
    return;
  }

  // Some kind of error.
  if (error->message == NULL)
  {
    musaico_fail_exit("musaico_fail_on_error",
                      "musaico->trace->error->message == NULL");
    return;  // Unreachable.
  }

  musaico_fail_exit(NULL,
                    error->message);
}


//
// Everything is hunky-dory, no error:
//
char MUSAICO_OK_MESSAGE[3] = "OK";
musaico_error_t MUSAICO_ERROR_CONST_OK =
{
  .is_ok = true,
  .message = MUSAICO_OK_MESSAGE,
  .or_fail = musaico_never_fail
};
musaico_error_t *MUSAICO_OK = &MUSAICO_ERROR_CONST_OK;

//
// Failed to allocate memory, e.g. for musaico_t itself.
//
char MUSAICO_ALLOCATE_FAILED_MESSAGE[26] = "Failed to allocate memory";
musaico_error_t MUSAICO_ERROR_ALLOCATE_FAILED =
  {
    .is_ok = false,
    .message = MUSAICO_ALLOCATE_FAILED_MESSAGE,
    .or_fail = musaico_fail_on_error
  };
musaico_error_t *MUSAICO_ALLOCATE_FAILED = &MUSAICO_ERROR_ALLOCATE_FAILED;

//
// Null pointer, e.g. musaico_t * itself.
//
char MUSAICO_NULL_POINTER_MESSAGE[13] = "Null pointer";
musaico_error_t MUSAICO_ERROR_NULL_POINTER =
  {
    .is_ok = false,
    .message = MUSAICO_NULL_POINTER_MESSAGE,
    .or_fail = musaico_fail_on_error
  };
musaico_error_t *MUSAICO_NULL_POINTER = &MUSAICO_ERROR_NULL_POINTER;


// Never actually returns an error; always either returns MUSAICO_OK,
// or crashes the program on error.
musaico_error_t *musaico_malloc(
        musaico_t *musaico,
        char *type_name,
        void **pointer_pointer,
        size_t size
        )
{
  if (musaico == NULL
      || type_name == NULL
      || pointer_pointer == NULL
      || size <= (size_t) 0)
  {
    musaico_fail_exit("musaico_malloc",
                      "musaico == %ld, type_name = %s, pointer_pointer == %ld, size == %ld",
                      (long) musaico,
                      type_name,
                      (long) pointer_pointer,
                      (long) size);
    return NULL;  // Unreachable.
  }

  *pointer_pointer = malloc(size);
  if (*pointer_pointer == NULL)
  {
    musaico_fail_exit("musaico_malloc",
                      "%s malloc(%ld) returned NULL",
                      type_name,
                      (long) size);
    return NULL;  // Unreachable.
  }

  //
  // Prepare allocated memory for later garbage collection:
  //
  musaico_gc_t *gc = (musaico_gc_t *) malloc(sizeof(musaico_gc_t));
  if (gc == NULL)
  {
    free(*pointer_pointer);
    musaico_fail_exit("musaico_malloc",
                      "%s malloc(musaico_gc_t) returned NULL",
                      type_name);
    return NULL;  // Unreachable.
  }

  int type_name_length = strlen(type_name);
  gc->type_name = (char *) malloc((type_name_length + 1) * sizeof(char));
  if (gc->type_name == NULL)
  {
    free(*pointer_pointer);
    free(gc);
    musaico_fail_exit("musaico_malloc",
                      "%s malloc(type_name) returned NULL",
                      type_name);
    return NULL;  // Unreachable.
  }
  if (strcpy(gc->type_name, type_name) == NULL)
  {
    free(*pointer_pointer);
    free(gc->type_name);
    free(gc);
    musaico_fail_exit("musaico_malloc",
                      "%s strcpy(gc->type_name) returned NULL",
                      type_name);
    return NULL;  // Unreachable.
  }

  gc->musaico = musaico;
  // gc->type_name was created and copied above.
  gc->pointer = *pointer_pointer;
  gc->size = size;
  gc->prev = NULL;
  gc->next = musaico->gc;
  if (musaico->gc != NULL)
  {
    musaico->gc->prev = gc;
  }
  musaico->gc = gc;
  gc->refers_to = NULL;
  gc->referred_from = NULL;

  return MUSAICO_OK;
}


// Never actually returns an error; always either returns MUSAICO_OK,
// or crashes the program on error.
musaico_error_t *musaico_free(
        musaico_t *musaico,
        char *type_name,
        void *pointer
        )
{
  if (musaico == NULL
      || type_name == NULL
      || pointer == NULL)
  {
    musaico_fail_exit("musaico_free",
                      "musaico == %ld, type_name = %s, pointer == %ld",
                      (long) musaico,
                      type_name,
                      (long) pointer);
    return NULL;  // Unreachable.
  }

  //
  // Remove from garbage collection list:
  //
  bool is_gc_found = false;
  for (musaico_gc_t *gc = musaico->gc; gc != NULL; gc = gc->next)
  {
    if (gc->pointer != pointer)
    {
      continue;
    }

    is_gc_found = true;

    if (gc->referred_from != NULL)
    {
      char *referenced_by_type_name;
      if (gc->referred_from->referer == NULL
          || gc->referred_from->referer->type_name == NULL)
      {
        referenced_by_type_name = "UNKNOWN TYPE";
      }
      else
      {
        referenced_by_type_name = gc->referred_from->referer->type_name;
      }
      musaico_fail_exit("musaico_free",
                        "Cannot garbage %s collect %ld size %ld: reference[s] exist from %s, ...",
                        type_name,
                        (long) pointer,
                        (long) gc->size,
                        referenced_by_type_name);
      return NULL;  // Unreachable.
    }

    musaico_gc_reference_t *next = NULL;
    for (musaico_gc_reference_t *reference = gc->refers_to;
         reference != NULL;
         reference = next)
    {
      next = reference->next;

      if (reference->reference == NULL)
      {
        musaico_fail_exit("musaico_free",
                          "Corrupt reference from %s %ld to NULL reference",
                          type_name,
                          (long) pointer);
        return NULL;  // Unreachable.
      }
      else if (reference->reference->type_name == NULL)
      {
        musaico_fail_exit("musaico_free",
                          "Corrupt reference from %s %ld to type_name == NULL pointer == %ld",
                          type_name,
                          (long) pointer,
                          (long) reference->reference->pointer);
        return NULL;  // Unreachable.
      }
      else if (reference->reference->pointer == NULL)
      {
        musaico_fail_exit("musaico_free",
                          "Corrupt reference from %s %ld to %s pointer == NULL",
                          type_name,
                          (long) pointer,
                          reference->reference->type_name);
        return NULL;  // Unreachable.
      }
      else if (reference->twin == NULL)
      {
        musaico_fail_exit("musaico_free",
                          "Asymmetrical reference from %s %ld to %s %ld: %ld->twin == NULL",
                          type_name,
                          (long) pointer,
                          reference->reference->type_name,
                          (long) reference->reference->pointer,
                          (long) reference);
        return NULL;  // Unreachable.
      }
      else if (reference->twin->twin != reference)
      {
        musaico_fail_exit("musaico_free",
                          "Asymmetrical reference from %s %ld to %s %ld: %ld->twin->twin == %ld",
                          type_name,
                          (long) pointer,
                          reference->reference->type_name,
                          (long) reference->reference->pointer,
                          (long) reference,
                          (long) reference->twin->twin);
        return NULL;  // Unreachable.
      }

      musaico_gc_reference_t *twin = reference->twin;

      musaico_gc_t *referenced_gc = reference->reference;

      reference->referer = NULL;
      reference->reference = NULL;
      reference->twin = NULL;
      reference->next = NULL;
      reference->prev = NULL;

      free(reference);

      if (referenced_gc->referred_from == twin)
      {
        referenced_gc->referred_from = twin->next;
      }

      if (twin->prev != NULL)
      {
        twin->prev->next = twin->next;
      }
      if (twin->next != NULL)
      {
        twin->next->prev = twin->prev;
      }
      twin->referer = NULL;
      twin->reference = NULL;
      twin->twin = NULL;
      twin->next = NULL;
      twin->prev = NULL;

      free(twin);

      if (referenced_gc->referred_from == NULL)
      {
        musaico_free(musaico,
                     "musaico_gc_t",
                     referenced_gc->pointer);
      }
    }

    if (musaico->gc == gc)
    {
      musaico->gc = gc->next;
    }

    if (gc->prev != NULL)
    {
      gc->prev->next = gc->next;
    }
    if (gc->next != NULL)
    {
      gc->next->prev = gc->prev;
    }
    gc->musaico = NULL;
    gc->type_name = NULL;
    gc->pointer = NULL;
    gc->size = (size_t) 0;
    gc->next = NULL;
    gc->prev = NULL;
    gc->refers_to = NULL;
    gc->referred_from = NULL;

    free(gc);

    break;
  }

  if (is_gc_found != true)
  {
    musaico_fail_exit("musaico_free",
                      "Cannot free %s pointer that was not created with musaico_malloc(): %ld",
                      type_name,
                      (long) pointer);
    return NULL;  // Unreachable.
  }

  // Now that references have been cleaned up, free the memory chunk.
  free(pointer);

  return MUSAICO_OK;
}


//
// Creates a musaico_error_t.
//
// Relies on musaico not being NULL, otherwise just fails outright.
//
musaico_error_t * musaico_error(
        musaico_t *musaico,
        char *message,
        ...
        )
{
  if (musaico == NULL)
  {
    musaico_fail_exit(NULL,
                      message);
    return NULL;  // Unreachable.
  }

  // printf-style format plus variable arguments:
  // Copy-pasta'ed from gnu's man page (man 2 printf), then tweaked a bit.
  int n = 0;
  va_list ap;
  va_start(ap, message);
  n = vsnprintf(NULL, (size_t) 0, message, ap);
  va_end(ap);

  char *formatted;
  if (n < 0)
  {
    formatted = message;
  }
  else
  {
    size_t size = (size_t) n + 1;  // +1 '\0'.
    formatted = (char *) malloc(size);
    if (formatted == NULL)
    {
      formatted = message;
    }
    else
    {
      va_start(ap, message);
      n = vsnprintf(formatted, size, message, ap);
      va_end(ap);
    }
  }

  musaico_error_t *error;
  musaico_malloc(musaico,
                 "musaico_error_t",
                 (void **) &error,
                 sizeof(musaico_error_t));
  error->is_ok = false;
  error->message = formatted;
  error->or_fail = musaico_fail_on_error;

  if (musaico->trace != NULL
      && musaico->trace->error == NULL)
  {
    musaico->trace->error = error;
  }

  return error;
}




musaico_error_t *musaico_start(
        musaico_t *musaico
        )
{
  if (musaico == NULL)
  {
    musaico_fail_exit("musaico_start",
                      "musaico == NULL");
    return NULL;  // Unreachable.
  }

  musaico->trace_step(musaico,
                      MUSAICO_TRACE_ENTER,
                      "musaico_started",  // context
                      NULL);  // source

  return MUSAICO_OK;
}


musaico_error_t *musaico_stop(
        musaico_t *musaico
        )
{
  if (musaico == NULL)
  {
    musaico_fail_exit("musaico_stop",
                      "musaico == NULL");
    return NULL;  // Unreachable.
  }

  musaico->trace_step(musaico,
                      MUSAICO_TRACE_EXIT,
                      "musaico_started",  // context
                      NULL);  // source

  return MUSAICO_OK;
}


musaico_error_t *musaico_time(
        musaico_t *musaico,
        time_t *tloc
        )
{
  if (musaico == NULL
      || tloc == NULL)
  {
    musaico_fail_exit("musaico_time",
                      "musaico = %ld, tloc = %ld",
                      (long) musaico,
                      (long) tloc);
    return NULL;  // Unreachable.
  }

  time_t result = time(tloc);

  return MUSAICO_OK;
}


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
musaico_error_t *musaico_trace_print(
        musaico_t *musaico,
        int fd
        )
{
  if (musaico == NULL)
  {
    musaico_fail_exit("musaico_trace_print()",
                      "musaico == NULL");
    return NULL;  // Unreachable.
  }
  else if (fd < 0)
  {
    return musaico_error(musaico,
                         "musaico_trace_print(): Invalid file descriptor fd == %d",
                         fd);
  }

  int end_step_num;
  if (musaico->trace == NULL)
  {
    end_step_num = -1;
  }
  else
  {
    end_step_num = musaico->trace->step_num;
  }

  musaico_trace_t *trace = musaico->first_trace;
  int step_num = -1;
  int prev_step_num = -2;
  int num_indents = 0;
  while (trace != NULL
         && step_num > prev_step_num
         && step_num < end_step_num)
  {
    prev_step_num = step_num;
    step_num = trace->step_num;

    musaico_trace_t *next_trace;

    // First, indent:
    switch (trace->trace_type)
    {
    case MUSAICO_TRACE_ENTER:  // Entering a procedure in Musaico C.  Also PUSH.
    case MUSAICO_TRACE_PUSH:  // Miscellaneous push of the Musaico stack trace.
      for (int indent = 0; indent < num_indents; indent ++)
      {
        dprintf(fd, "  ");
      }
      num_indents ++;
      if (trace->child == NULL)
      {
        next_trace = trace->next;
      }
      else
      {
        next_trace = trace->child;
      }
      break;

    case MUSAICO_TRACE_EXIT:  // Exiting a procedure in Musaico C.  Also a POP.
    case MUSAICO_TRACE_POP:  // Miscellaneous pop of the Musaico stack trace.
      num_indents --;
      for (int indent = 0; indent < num_indents; indent ++)
      {
        dprintf(fd, "  ");
      }
      if (trace->next == NULL)
      {
        next_trace = trace->parent;
      }
      else
      {
        next_trace = trace->next;
      }
      break;

    case MUSAICO_TRACE_ANALYSE:  // Semantic analysis of Musaico language code.
    case MUSAICO_TRACE_COMPILE:  // Compiling Musaico language into other forms.
    case MUSAICO_TRACE_INFO:  // Miscellaneous info in the Musaico stack trace.
    case MUSAICO_TRACE_LEX:  // Lexical analysis of Musaico language code.
    case MUSAICO_TRACE_LINT:  // Linting of Musaico language code.
    case MUSAICO_TRACE_OPTIMISE:  // Optimising of Musaico language code.
    case MUSAICO_TRACE_PARSE:  // Syntactical parsing of Musaico language code.
    case MUSAICO_TRACE_RUNTIME:  // Musaico language runtime after compilation.
    case MUSAICO_TRACE_NONE:  // Do not create traces with trace_type == NONE.
    default:
      for (int indent = 0; indent < num_indents; indent ++)
      {
        dprintf(fd, "  ");
      }
      if (trace->next == NULL)
      {
        next_trace = trace->parent;
      }
      else
      {
        next_trace = trace->next;
      }
      break;
    }

    // Now, after indenting, output trace info:
    switch (trace->trace_type)
    {
    case MUSAICO_TRACE_ENTER:  // Entering a procedure in Musaico C.  Also PUSH.
      dprintf(fd, "Enter    ");
      break;
    case MUSAICO_TRACE_PUSH:  // Miscellaneous push of the Musaico stack trace.
      dprintf(fd, "Push     ");
      break;
    case MUSAICO_TRACE_EXIT:  // Exiting a procedure in Musaico C.  Also a POP.
      dprintf(fd, "Exit     ");
      break;
    case MUSAICO_TRACE_POP:  // Miscellaneous pop of the Musaico stack trace.
      dprintf(fd, "Pop      ");
      break;
    case MUSAICO_TRACE_ANALYSE:  // Semantic analysis of Musaico language code.
      dprintf(fd, "Analyse  ");
      break;
    case MUSAICO_TRACE_COMPILE:  // Compiling Musaico language into other forms.
      dprintf(fd, "Compile  ");
      break;
    case MUSAICO_TRACE_INFO:  // Miscellaneous info in the Musaico stack trace.
      dprintf(fd, "Info     ");
      break;
    case MUSAICO_TRACE_LEX:  // Lexical analysis of Musaico language code.
      dprintf(fd, "Lex      ");
      break;
    case MUSAICO_TRACE_LINT:  // Linting of Musaico language code.
      dprintf(fd, "Lint     ");
      break;
    case MUSAICO_TRACE_OPTIMISE:  // Optimising of Musaico language code.
      dprintf(fd, "Optimise ");
      break;
    case MUSAICO_TRACE_PARSE:  // Syntactical parsing of Musaico language code.
      dprintf(fd, "Parse    ");
      break;
    case MUSAICO_TRACE_RUNTIME:  // Musaico language runtime after compilation.
      dprintf(fd, "Runtime  ");
      break;
    case MUSAICO_TRACE_NONE:  // Do not create traces with trace_type == NONE.
      dprintf(fd, "(none)   ");
      break;
    default:
      dprintf(fd, "???      ");
      break;
    }

    if (trace->context != NULL)
    {
      dprintf(fd, trace->context);
      if (trace->source != NULL)
      {
        dprintf(fd, ": ");
      }
    }

    if (trace->source != NULL)
    {
      if (trace->source->file != NULL)
      {
        if (trace->source->file->path == NULL)
        {
          dprintf(fd, "(NULL filename)");
        }
        else
        {
          dprintf(fd, trace->source->file->path);
        }
      }

      dprintf(fd, " %d.%d-%d",
              trace->source->line_num,
              trace->source->char_start,
              trace->source->char_end);
    }

    dprintf(fd, "\n");

    trace = next_trace;
  }

  if (trace != NULL
      || num_indents != 0)
  {
    // Infinite loop and/or borken step_nums or missing EXIT/POP
    // etc somewhere in the traces.
    return musaico_error(musaico,
                         "musaico_trace_print(%d): Broken trace, step_num = %d, prev_step_num = %d, end_step_num = %d, num_indents = %d",
                         fd,
                         step_num,
                         prev_step_num,
                         end_step_num,
                         num_indents);
  }

  return MUSAICO_OK;
}


//
// Adds a trace element to the current stack trace.
//
musaico_error_t *musaico_trace_step(
        musaico_t *musaico,
        musaico_trace_type_t trace_type,
        char *context,  // NULL if source is non-NULL.
        musaico_source_t *source  // Null if context is non-NULL.
        )
{
  if (musaico == NULL)
  {
    musaico_fail_exit("musaico_trace_step()",
                      "musaico == NULL");
    return NULL;  // Unreachable.
  }
  else if (context == NULL
           && source == NULL)
  {
    return musaico_error(musaico,
                         "musaico_trace_step(%d): context == NULL, source == NULL",
                         (int) trace_type);
  }

  musaico_trace_t *trace;
  musaico_error_t *error = musaico->allocate(musaico,
                                             "musaico_trace_t",
                                             (void **) &trace,
                                             sizeof(musaico_trace_t));
  if (error == NULL)
  {
    return musaico_error(musaico,
                         "musaico_trace_step(%d, %s, %ld): allocate() returned NULL",
                         (int) trace_type,
                         context,
                         (long) source);
  }
  else if (error->is_ok != true)
  {
    return error;
  }

  trace->musaico = musaico;
  trace->trace_type = trace_type;
  trace->context = context;
  trace->source = source;

  trace->error = NULL;

  musaico_trace_type_t prev_trace_type;
  if (musaico->trace == NULL)
  {
    trace->step_num = 0;
    prev_trace_type = MUSAICO_TRACE_NONE;
  }
  else
  {
    trace->step_num = musaico->trace->step_num + 1;
    prev_trace_type = musaico->trace->trace_type;
  }

  switch (prev_trace_type)
  {
  case MUSAICO_TRACE_ENTER:  // Entering a procedure in Musaico C.  Also PUSH.
  case MUSAICO_TRACE_PUSH:  // Miscellaneous push of the Musaico stack trace.
    // musaico->trace is not NULL.
    switch (trace_type)
    {
    case MUSAICO_TRACE_EXIT:  // Exiting a procedure in Musaico C.  Also a POP.
    case MUSAICO_TRACE_POP:  // Miscellaneous pop of the Musaico stack trace.
      trace->parent = musaico->trace->parent;
      trace->prev = musaico->trace;
      trace->next = NULL;
      trace->child = NULL;
      musaico->trace->next = trace;
      break;

    default:
      trace->parent = musaico->trace;
      trace->prev = NULL;
      trace->next = NULL;
      trace->child = NULL;
      musaico->trace->child = trace;
      break;
    }
    break;

  default:
    // musaico->trace might be NULL.
    switch (trace_type)
    {
    case MUSAICO_TRACE_EXIT:  // Exiting a procedure in Musaico C.  Also a POP.
    case MUSAICO_TRACE_POP:  // Miscellaneous pop of the Musaico stack trace.
      if (musaico->trace == NULL)
      {
        musaico->destroy(musaico,
                         "musaico_trace_t",
                         trace);
        return musaico_error(musaico,
                             "musaico_trace_step(%d, %s, %ld): Cannot EXIT or POP with no parent stack frame, musaico->trace == NULL",
                             (int) trace_type,
                             context,
                             (long) source);
      }
      else if (musaico->trace->parent == NULL)
      {
        musaico->destroy(musaico,
                         "musaico_trace_t",
                         trace);
        return musaico_error(musaico,
                             "musaico_trace_step(%d, %s, %ld): Cannot EXIT or POP with no parent previous stack frame, musaico->trace->parent == NULL",
                             (int) trace_type,
                             context,
                             (long) source);
      }
      trace->parent = musaico->trace->parent->parent;
      trace->prev = musaico->trace->parent;
      trace->next = NULL;
      trace->child = musaico->trace;

      musaico->trace->parent = trace;
      break;

    default:
      if (musaico->trace == NULL)
      {
        trace->parent = NULL;
      }
      else
      {
        trace->parent = musaico->trace->parent;
        musaico->trace->next = trace;
      }
      trace->prev = musaico->trace;
      trace->next = NULL;
      trace->child = NULL;
      break;
    }
    break;
  }

  if (musaico->first_trace == NULL)
  {
    musaico->first_trace = trace;
  }
  musaico->trace = trace;

  return MUSAICO_OK;
}




// Creates a default, toplevel Musaico structure.
musaico_t *musaico_create(
        char *name
        )
{
  char *musaico_name;
  if (name == NULL)
  {
    musaico_name = "musaico";
  }
  else
  {
    musaico_name = name;
  }

  musaico_t *musaico = (musaico_t *) malloc(sizeof(musaico_t));
  if (musaico == NULL)
  {
    musaico_fail_exit("musaico_create",
                      "malloc(musaico) returned NULL");
    return NULL;
  }
  int name_length = strlen(musaico_name);
  char *name_copy = (char *) malloc((name_length + 1) * sizeof(char));
  if (name_copy == NULL)
  {
    free(musaico);
    musaico_fail_exit("musaico_create",
                      "malloc(\"%s\") returned NULL",
                      musaico_name);
    return NULL;
  }
  char *success = strcpy(name_copy, musaico_name);
  if (success == NULL)
  {
    free(musaico);
    free(name_copy);
    musaico_fail_exit("musaico_create",
                      "strcpy(name_copy, \"%s\") returned NULL",
                      musaico_name);
    return NULL;
  }

  musaico->parent = NULL;
  musaico->name = name_copy;
  musaico->first_trace = NULL;
  musaico->trace = NULL;
  musaico->gc = NULL;

  musaico->allocate = musaico_malloc;
  musaico->destroy = musaico_free;

  musaico->start = musaico_start;
  musaico->stop = musaico_stop;

  musaico->time = musaico_time;

  musaico->trace_print = musaico_trace_print;
  musaico->trace_step = musaico_trace_step;

  return musaico;
}
