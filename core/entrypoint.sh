#!/bin/sh

echo "Starting musaico $*..."
MUSAICO_COMMANDS=$@

echo "  Generating in-flight keys:"
/musaico/bin/musaico_keygen.sh "in_flight"

echo "  Sourcing keys:"
for KEY_FILE in /musaico/keys/*.env
do
    . "$KEY_FILE" \
        || exit 1
done

IS_ERROR=false
if test -z "$MUSAICO_AT_REST_PUBLIC_KEY"
then
    echo "ERROR No at_rest AGE key pair has been generated.  Use /musaico/bin/'musaico_keygen.sh at_rest' from the build manifest of your container image type.  MUSAICO_AT_REST_PUBLIC_KEY=$MUSAICO_AT_REST_PUBLIC_KEY" >&2
    IS_ERROR=true
fi
if test -z "$MUSAICO_AT_REST_PRIVATE_KEY"
then
    echo "ERROR No at_rest AGE key pair has been generated.  Use /musaico/bin/'musaico_keygen.sh at_rest' from the build manifest of your container image type.  MUSAICO_AT_REST_PRIVATE_KEY=$MUSAICO_AT_REST_PRIVATE_KEY" >&2
    IS_ERROR=true
fi
if test -z "$MUSAICO_IN_FLIGHT_PUBLIC_KEY"
then
    echo "ERROR No in_flight AGE key pair has been generated.  Use /musaico/bin/'musaico_keygen.sh in_flght' from the build manifest of your container image type.  MUSAICO_IN_FLIGHT_PUBLIC_KEY=$MUSAICO_IN_FLIGHT_PUBLIC_KEY" >&2
    IS_ERROR=true
fi
if test -z "$MUSAICO_IN_FLIGHT_PRIVATE_KEY"
then
    echo "ERROR No in_flight AGE key pair has been generated.  Use /musaico/bin/'musaico_keygen.sh in_flight' from the build manifest of your container image type.  MUSAICO_IN_FLIGHT_PRIVATE_KEY=$MUSAICO_IN_FLIGHT_PRIVATE_KEY" >&2
    IS_ERROR=true
fi
if test "$IS_ERROR" != "false"
then
    exit 2
fi

echo "  EXporting SOPS keys using at_rest key pair:"
export SOPS_AGE_PUBLIC_KEY="$MUSAICO_AT_REST_PUBLIC_KEY"
export SOPS_AGE_KEY="$MUSAICO_AT_REST_PRIVATE_KEY"

echo "  Starting $*:"
$MUSAICO_COMMANDS

echo "SUCCESS running musaico $*"
exit 0
