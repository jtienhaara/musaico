#!/bin/sh

if test $# -ne 1
then
    echo "Usage: $0 (settings-file)" >&2
    echo "" >&2
    echo "(Over)writes the specified settings file with environment" >&2
    echo "variable exports required for various Musaico components," >&2
    echo "such as MUSAICO_VERSION, MUSAICO_USER, and so on." >&2
    echo "" >&2
    echo "If the full path to the settings file does not exist," >&2
    echo "this script will create the missing directories." >&2
    echo "" >&2
    echo "(settings-file)" >&2
    echo "    The full path to the settings file to (over)write)." >&2
    echo "    Can be a path relative to the current working directory," >&2
    exit 1
fi

SETTINGS_FILE=$1
MAYBE_RELATIVE=`echo "$SETTINGS_FILE" \
                    | grep -v '^/'`
if test ! -z "$MAYBE_RELATIVE"
then
    ABSOLUTE_SETTINGS_FILE="$PWD/$SETTINGS_FILE"
    SETTINGS_FILE="$ABSOLUTE_SETTINGS_FILE"
fi

echo "Creating Musaico settings $(SETTINGS_FILE)..."

SETTINGS_DIR=`dirname "$SETTINGS_FILE"`
if test $? -ne 0
then
    echo "ERROR dirname must be installed to run Musaico create_settings.sh" >&2
    exit 1
fi

echo "  Ensuring settings directory exists: $SETTINGS_DIR"
mkdir -p "$SETTINGS_DIR" \
    || exit 1

if test -e "$SETTINGS_FILE"
then
    echo "  Removing existing settings file: $SETTINGS_FILE"
    rm -f "$SETTINGS_FILE" \
        || exit 1
fi

#
# Musaico version, used to build container images and so on:
#
export MUSAICO_VERSION=0.0.1

#
# The user who will install, build, run, and so on, Musaico.
# On a root host, this will be whoever is logged in, such as
# user "johann".  Inside a Musaico host (a VM or container and so on),
# this will typically be user "musaico".
#
# Some installers need to know who will be using the tools installed.
# For example, the host.vm.qemu installer adds the $MUSAICO_USER
# to the "kvm" group, so that they are allowed by the OS to run VMs
# with KVM acceleration enabled.
#
echo "  Retrieving MUSAICO_USER:"
export MUSAICO_USER=`id --user --name`
if test $? -ne 0 \
    -o -z "$MUSAICO_USER"
then
    echo "ERROR Failed to retrieve MUSAICO_USER" >&2
    exit 1
fi

#
#
echo "  Retrieving MUSAICO_HOST:"
export MUSAICO_HOST=`hostname`
if test $? -ne 0 \
    -o -z "$MUSAICO_HOST"
then
    echo "ERROR Failed to retrieve MUSAICO_HOST" >&2
    exit 1
fi

echo "  Retrieving MUSAICO_DOMAIN:"
# Can be "":
export MUSAICO_DOMAIN=`dnsdomainname`
if test $? -ne 0
then
    echo "ERROR Failed to retrieve MUSAICO_DOMAIN" >&2
    exit 1
fi

if test -z "$MUSAICO_DOMAIN"
then
    export MUSAICO_EMAIL="$MUSAICO_USER@$MUSAICO_HOST"
else
    export MUSAICO_EMAIL="$MUSAICO_USER@${MUSAICO_HOST}.${MUSAICO_DOMAIN}"
fi

echo "  Musaico settings:"
env \
    | grep '^MUSAICO_' \
    | sort -d \
    | sed 's|^\(.*\)|export \1|' \
    | tee "$SETTINGS_FILE" \
    | sed 's|^\(.*\)$|    \1|' \
    || exit 1

echo "SUCCESS Creating Musaico settings $SETTINGS_FILE."
exit 0
