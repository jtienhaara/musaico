#!/bin/sh

#
# Generates an AGE private/public key pair, either for
# a Musaico container image type, or for a Musaico container instance.
# Both the container image type's key pair and the container instance's
# key pair are used at runtime by the container instance:
#
#     at_rest:
#         The container image type's key pair is used for data at rest.
#
#     in_flight:
#         The container instance's key pair is used for data in flight.
#
if test $# -ne 1
then
    echo "Usage: $0 (key_pair_id)" >&2
    echo "" >&2
    echo "Generates an AGE key pair, and stores the results" >&2
    echo "in /musaico/keys/(key_pair_id).env with capitalized" >&2
    echo "environment variable names." >&2
    echo "" >&2
    echo "For example, 'musaico_keygen.sh \"at_rest\"' creates" >&2
    echo "/musaico/keys/at_rest.env with environment variables:" >&2
    echo "" >&2
    echo "    export MUSAICO_AT_REST_AGE_PRIVATE_KEY=..." >&2
    echo "    export MUSAICO_AT_REST_AGE_PUBLIC_KEY=..." >&2
    echo "" >&2
    echo "Or 'musaico_keygen.sh \"in_flight\"' creates" >&2
    echo "/musaico/keys/in_flight.env with environment variables:" >&2
    echo "" >&2
    echo "    export MUSAICO_IN_FLIGHT_AGE_PRIVATE_KEY=..." >&2
    echo "    export MUSAICO_IN_FLIGHT_AGE_PUBLIC_KEY=..." >&2
    echo "" >&2
    echo "The /musaico/keys/(key_pair_id).env file can be sourced" >&2
    echo "at runtime inside a shell (/bin/sh etc) with:" >&2
    echo "" >&2
    echo "    . /musaico/keys/(key_pair_id).env" >&2
    echo "" >&2
    echo "(Note the dot.)" >&2
    echo "" >&2
    echo "This command will fail if the specified keys file" >&2
    echo "already exists.  If you want to replace a keys file," >&2
    echo "be sure to delete it before executing musaico_keygen.sh." >&2
    echo "" >&2
    echo "(key_pair_id)" >&2
    echo "    The unique name of the key pair to create." >&2
    echo "    For example, \"at_rest\" or \"in_flight\"" >&2
    echo "    and so on.  Must be a key pair ID that does" >&2
    echo "    not already have a corresponding file" >&2
    echo "    /musaico/keys/(key_pair_id).env." >&2
    echo "" >&2
    exit 1
fi

KEY_PAIR_ID=$1
if test -z "$KEY_PAIR_ID"
then
    echo "ERROR musaico_keygen.sh: Cnvalid (key_pair_id): '$KEY_PAIR_ID'" >&2
    exit 2
fi

KEY_PAIR_FILE=/musaico/keys/${KEY_PAIR_ID}.env
if test -e "$KEY_PAIR_FILE"
then
    echo "ERROR musaico_keygen.sh: Cannot overwrite existing key pair file: $KEY_PAIR_FILE" >&2
    exit 3
fi

KEY_PAIR_ENV_NAME=`echo "$KEY_PAIR_ID" \
                    | awk '{ print(toupper($0)); }'`

#
# Create the key pair file:
#
age-keygen \
    | awk -v "key_pair_env_name=$KEY_PAIR_ENV_NAME" \
          '
           $0 ~ /^# public key: .*$/ {
               print "export MUSAICO_" key_pair_env_name "_PUBLIC_KEY=" $4;
           }
           $1 ~ /^AGE-SECRET-KEY-.*$/ {
               print "export MUSAICO_" key_pair_env_name "_PUBLIC_KEY=" $1; }
          ' \
              >> "$KEY_PAIR_FILE" \
              || exit 4

#
# Tell the caller what the key pair file is called, so they can "." it.
#
echo "$KEY_PAIR_FILE"

exit 0
