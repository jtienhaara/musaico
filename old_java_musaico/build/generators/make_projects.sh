#!/bin/sh

#
# Copyright (c) 2009 Johann Tienhaara
# All rights reserved.
#
# This file is part of Musaico.
#
# Musaico is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Musaico is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Musaico.  If not, see <http://www.gnu.org/licenses/>.
#

if test $# -lt 1
then
    echo "Usage: $0 path/to/my/project ..."
    exit 0
fi

TOPLEVELS=$*

# Get the year so that we can change the copyright dates to "this year".
CURR_YEAR=`date '+%Y'`
RESULT=$?
if test $RESULT -ne 0
then
    echo "Could not determine current date to set copyrights"
    exit $RESULT
fi

# Get the current user's name so we can change the copyright owner to
# "User Name".
USER_ID=`id -u -r`
RESULT=$?
if test $RESULT -ne 0
then
    echo "Could not determine numeric user ID to set copyrights"
    exit $RESULT
fi
USER_NAME=`cat /etc/passwd | cut -d: -s -f3,5 | grep "^$USER_ID:" | cut -d: -s -f2 | sed 's|,,*$||g'`
RESULT=$?
if test -z "$USER_NAME"
then
    USER_NAME=`id -u -n`
    RESULT=$?
fi
if test $RESULT -ne 0 -o -z "$USER_NAME"
then
    echo "Could not determine full user name to set copyrights"
    exit $RESULT
fi

# Try to guess the user's email address for copyrights.
# (Haven't bothered to do anything yet.  Maybe in future use uname -n
# and dig to find the mail server, but this is unlikely to return good
# results in many cases.)
USER_EMAIL=""
if test "$USER_NAME" = "Johann Tienhaara" \
    -o "$USER_NAME" = "johann"
then
    USER_NAME="Johann Tienhaara"
    USER_EMAIL="jtienhaara@yahoo.com"
fi

if test -z "$USER_EMAIL"
then
    USER_DETAILS="$USER_NAME"
else
    USER_DETAILS="$USER_NAME <$USER_EMAIL>"
fi


# Tell the user what we're going to do with the copyrights.
COPYRIGHT="Copyright (c) ${CURR_YEAR} ${USER_DETAILS}"
echo "Copyright assignment will look like:"
echo "    $COPYRIGHT"


for TOPLEVEL in $TOPLEVELS
do
    echo ""

    # Figure out the full path to the project from the .../musaico root dir.
    TOPLEVEL_DIRS=""
    CURR_DIR="$TOPLEVEL"
    while test "$CURR_DIR" != "." -a "$CURR_DIR" != "/" -a ! -z "$CURR_DIR"
    do
        NEW_DIRS="$CURR_DIR $TOPLEVEL_DIRS"
        TOPLEVEL_DIRS="$NEW_DIRS"

        CURR_DIR=`dirname $CURR_DIR`
    done

    if test "$CURR_DIR" = "/"
    then
        echo "Must specify relative path."
        exit 1
    fi

    # Make the toplevel project directory, and its parent directories if
    # necessary.
    DOT_DOTS=""
    for DIR in $TOPLEVEL_DIRS
    do
        if test -z "$DOT_DOTS"
        then
            NEW_DOT_DOTS=".."
        else
            NEW_DOT_DOTS="${DOT_DOTS}/.."
        fi
        DOT_DOTS="$NEW_DOT_DOTS"

        if test ! -d "$DIR"
        then
            echo "Creating $DIR"
            mkdir $DIR
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 10."
                exit $RESULT
            fi
        fi
    done

    # Copy the make files and includes into the new project directory.
    echo "Creating $TOPLEVEL/Makefile"
    cp build/Templates/Makefile $TOPLEVEL
    RESULT=$?
    if test $RESULT -ne 0
    then
        echo "Failed 20."
        exit $RESULT
    fi

    # Make the x/y/z/project directory.
    PROJECT="$TOPLEVEL/project"
    echo "Creating $PROJECT"
    mkdir $PROJECT
    RESULT=$?
    if test $RESULT -ne 0
    then
        echo "Failed 30."
        exit $RESULT
    fi

    echo "Creating $PROJECT/Makefile, project.mk"
    cp build/Templates/project/Makefile build/Templates/project/project.mk $PROJECT
    RESULT=$?
    if test $RESULT -ne 0
    then
        echo "Failed 40."
        exit $RESULT
    fi

    # Make the sub-directories of the new project directory.
    for DELIVERABLE in analysis design documentation examples profile roadmap \
        source test
    do
        DIR="$PROJECT/$DELIVERABLE"
        # Create the deliverable sub-directory.
        if test ! -d "$DIR"
        then
            echo "Creating $DIR"
            mkdir $DIR
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 50."
                exit $RESULT
            fi
        fi

        # Copy the Makefile for this deliverable.
        if test ! -f "$DIR/Makefile"
        then
            echo "Creating $DIR/Makefile"
            cp build/Templates/project/$DELIVERABLE/Makefile $DIR/Makefile
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 60."
                exit $RESULT
            fi
        fi
    done

    # Copy test.mk into the test directory, etc.
    for MAKE_INCLUDE in test/test.mk
    do
        if test ! -f $PROJECT/$MAKE_INCLUDE
        then
            echo "Creating $PROJECT/$MAKE_INCLUDE"
            cp build/Templates/project/$MAKE_INCLUDE $PROJECT/$MAKE_INCLUDE
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 70."
                exit $RESULT
            fi
        fi
    done

    # Copy all the test sub-directories into the new project's test directory.
    for CODE_DIR in source test/static test/coverage test/unit \
        test/integration test/system test/stress profile examples
    do
        # Create the test sub-directory.
        if test ! -d "$PROJECT/$CODE_DIR"
        then
            echo "Creating $PROJECT/$CODE_DIR"
            mkdir $PROJECT/$CODE_DIR
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 80."
                exit $RESULT
            fi
        fi

        # Copy the Makefile into the test sub-directory.
        if test -f "build/Templates/project/$CODE_DIR/Makefile" \
            -a ! -f "$PROJECT/$CODE_DIR/Makefile"
        then
            echo "Creating $PROJECT/$CODE_DIR/Makefile"
            cp build/Templates/project/$CODE_DIR/Makefile $PROJECT/$CODE_DIR/Makefile
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 90."
                exit $RESULT
            fi
        fi

        # Copy each language platform into the test sub-directory.
        for PLATFORM in c java javascript perl python tcl
        do
            DIR="$PROJECT/$CODE_DIR/$PLATFORM"
            if test ! -d "$DIR"
            then
                echo "Creating $DIR"
                mkdir $DIR
                RESULT=$?
                if test $RESULT -ne 0
                then
                    echo "Failed 100."
                    exit $RESULT
                fi
            fi

            if test -f "build/Templates/project/$CODE_DIR/$PLATFORM/Makefile" \
                -a ! -f "$PROJECT/$CODE_DIR/$PLATFORM/Makefile"
            then
                echo "Creating $PROJECT/$CODE_DIR/$PLATFORM/Makefile"
                cp build/Templates/project/$CODE_DIR/$PLATFORM/Makefile $PROJECT/$CODE_DIR/$PLATFORM/Makefile
                RESULT=$?
                if test $RESULT -ne 0
                then
                    echo "Failed 110."
                    exit $RESULT
                fi
            fi
        done

        # Make the Java platform directory hierarchy.
        if test ! -d "$PROJECT/$CODE_DIR/java/musaico"
        then
            DIR="$PROJECT/$CODE_DIR/java/musaico"
            echo "Creating $DIR"
            mkdir $DIR
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Failed 120."
                exit $RESULT
            fi
        fi

        # Create Java package director(y/ies).
        for PACKAGE_DIR in $TOPLEVEL_DIRS
        do
            DIR="$PROJECT/$CODE_DIR/java/musaico/$PACKAGE_DIR"
            if test ! -d "$DIR"
            then
                echo "Creating $DIR"
                mkdir $DIR
                RESULT=$?
                if test $RESULT -ne 0
                then
                    echo "Failed 130."
                    exit $RESULT
                fi
            fi
        done

        # Copy template files into the Java package directory.
        for TEMPLATE_FILE in `find build/Templates/project/$CODE_DIR -type f`
        do
            TEMPLATE=`echo "$TEMPLATE_FILE" | grep -v '\~$' | sed 's|^build/Templates/project/||' | grep "/Module/" | sed "s|/Module/|/musaico/$PAKAGE_DIR/|"`
            if test -z "$TEMPLATE"
            then
                # This is not a package template (for Java etc).  Skip it.
                continue
            fi

            if test ! -f "$PROJECT/$TEMPLATE"
            then
                echo "Creating template $TEMPLATE"
                cp $TEMPLATE_FILE $PROJECT/$TEMPLATE
                RESULT=$?
                if test $RESULT -ne 0
                then
                    echo "Failed 140."
                    exit $RESULT
                fi
            fi
        done

        # Now copy files that are not templates - keep their filenames as-is.
        for TEMPLATE_FILE in `find build/Templates/project/$CODE_DIR -type f`
        do
            TEMPLATE=`echo "$TEMPLATE_FILE" | grep -v '\~$' | sed 's|^build/Templates/project/||' | grep -v "/Module/"`
            if test -z "$TEMPLATE"
            then
                # This is a package template (for Java etc).  Skip it.
                continue
            fi

            if test ! -f "$PROJECT/$TEMPLATE"
            then
                echo "Creating template $TEMPLATE"
                cp $TEMPLATE_FILE $PROJECT/$TEMPLATE
                RESULT=$?
                if test $RESULT -ne 0
                then
                    echo "Failed 150."
                    exit $RESULT
                fi
            fi
        done
    done


    PACKAGE="$TOPLEVEL"

    # Replace all references to '!!!' in the makefiles and make includes.
    for MAKE_FILE in \
        `find $TOPLEVEL -name 'Makefile'` \
        `find $PROJECT -name '*.mk'`
    do
        TEMP_MAKE_FILE="/tmp/temp_makefile.$$"
        cat $MAKE_FILE \
            | sed "s|musaico/!!!|musaico/$PACKAGE|g" \
            | sed 's|!!!\.class||g' \
            | sed "s|\(MUSAICO_DIR[ ]*=[^\.]*\)\.\.|\1${DOT_DOTS}|g" \
            | sed "s|\(PLATFORM_DIR[ ]*=[^\.]*\.\./\)\.\.|\1${DOT_DOTS}|g" \
            | sed "s|Module|${PACKAGE}|g" \
            > $TEMP_MAKE_FILE
        RESULT=$?
        if test $RESULT -ne 0
        then
            echo "Replacing '!!!' failed on $MAKE_FILE (temp $TEMP_MAKE_FILE)"
            exit $RESULT
        fi

        MAKE_FILE_CHANGES=`diff $MAKE_FILE $TEMP_MAKE_FILE`
        if test ! -z "$MAKE_FILE_CHANGES"
        then
            echo "Modifying $MAKE_FILE:"
            echo "$MAKE_FILE_CHANGES" | sed 's|^\(.*\)$|    \1|'
            mv -f $TEMP_MAKE_FILE $MAKE_FILE
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Modifying $MAKE_FILE failed (from $TEMP_MAKE_FILE)"
                exit $RESULT
            fi
        else
            rm -f $TEMP_MAKE_FILE
            RESULT=$?
            if test $RESULT -ne 0
            then
                echo "Could not rm unused temporary make file $TEMP_MAKE_FILE"
                exit $RESULT
            fi
        fi
    done


    # Change copyrights in ALL files with copyright notices.
    echo "Setting copyrights"
    for FILE in `find $TOPLEVEL -type f -name '*[^~]' -exec grep -l 'Copyright (c) YEAR AUTHOR' \{\} \;`
    do
        TEMP_FILE="/tmp/copyright_$$.txt"
        cat $FILE \
            | sed "s|Copyright (c) YEAR AUTHOR|$COPYRIGHT|" \
            > $TEMP_FILE
        RESULT=$?
        if test $RESULT -ne 0
        then
            echo "Failed to make temporary copyright file $TEMP_FILE from $FILE"
            exit $RESULT
        fi

        mv -f $TEMP_FILE $FILE
        RESULT=$?
        if test $RESULT -ne 0
        then
            echo "Failed to move temporary copyright file $TEMP_FILE to $FILE"
            exit $RESULT
        fi
    done

    echo "Done $TOPLEVEL"
done

# Tell the user what we did with the copyrights.
echo ""
echo "Copyright assignment looks like:"
echo "    $COPYRIGHT"

# Remind the user to update the parent makefiles.
echo ""
echo "Don't forget to update musaico.mk and Makefile, or the parent project.mk!"

exit 0
