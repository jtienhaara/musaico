#!/bin/sh

make check_settings \
    || exit 99

echo "Checking whether $MUSAICO_APP_ID is already installed in $MUSAICO_RUNTIME..."

IS_INSTALLED=true

SOURCE_DIR="$MUSAICO_APP_DIR"
TARGET_DIR="${MUSAICO_RUNTIME}/app/${MUSAICO_APP_ID}/guest"

echo "  Checking for existence of $TARGET_DIR:"
if test ! -d "$TARGET_DIR"
then
    IS_INSTALLED=false
else
    echo "  Checking sha256sum of $SOURCE_DIR:"
    SOURCE_SHA256=`find "$SOURCE_DIR" -type f -exec sha256sum \{\} \; \
                       | sort -d -k 2 \
                       | sha256sum \
                       | awk '{ print $1; }'`
    if test $? -ne 0 \
            -o -z "$SOURCE_SHA256"
    then
        exit 99
    fi

    echo "  Checking sha256sum of $TARGET_DIR:"
    TARGET_SHA256=`find "$TARGET_DIR" -type f -exec sha256sum \{\} \; \
                       | sort -d -k 2 \
                       | sha256sum \
                       | awk '{ print $1; }'`
    if test $? -ne 0 \
            -o -z "$TARGET_SHA256"
    then
        exit 99
    fi

    echo "  Comparing $SOURCE_SHA256 to $TARGET_SHA256:"
    if test "$SOURCE_SHA256" != "$TARGET_SHA256"
    then
	IS_INSTALLED=false
    fi
fi

if test "$IS_INSTALLED" != "true"
then
    echo "$MUSAICO_APP_ID is NOT installed in $MUSAICO_RUNTIME"
    exit 1
else
    echo "$MUSAICO_APP_ID IS installed in $MUSAICO_RUNTIME"
    exit 0
fi
