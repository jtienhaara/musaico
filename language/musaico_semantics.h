#ifndef MUSAICO_LANGUAGE_H_INCLUDED
#define MUSAICO_LANGUAGE_H_INCLUDED

#include "musaico.h"

typedef struct _STRUCT_musaico_metadata         musaico_metadata_t;
typedef struct _STRUCT_musaico_named            musaico_named_t;
typedef struct _STRUCT_musaico_namespace        musaico_namespace_t;
typedef struct _STRUCT_musaico_term             musaico_term_t;
typedef struct _STRUCT_musaico_type             musaico_type_t;


struct _STRUCT_musaico_metadata
{
  musaico_t *musaico;
  musaico_source_t *source;  // Musaico language source code context.
};


struct _STRUCT_musaico_named
{
  char *name;  // Just the name of this named object e.g. "image".
  char *id;  // Fully qualified e.g. "host.container.image"

  musaico_namespace_t *parent;  // Can be NULL if root namespace.
};


struct _STRUCT_musaico_namespace
{
  // Generally paired with a named structure, to name the namespace.
  musaico_term_t *first_term
};


struct _STRUCT_musaico_package
{
  musaico_named_t name;
  musaico_namespace_t namespace;
  musaico_term_t term;  // or should term have a union???
};


struct _STRUCT_musaico_term
{
  musaico_term_t *prev;
  musaico_term_t *next;
};


struct _STRUCT_musaico_type
{
  musaico_named_t name;
  musaico_namespace_t namespace;  // For fields and so on.
};


#endif // MUSAICO_LANGUAGE_H_INCLUDED
