#!/bin/sh

for DIR in `find -type d`
do
    if test -z `echo "$DIR" | sed 's|^.*third_party.*$||'`
    then
        continue
    fi

    MESSAGES_FILE="$DIR/Messages.properties"
    if test -f "$MESSAGES_FILE"
    then
        echo ""
        echo "$MESSAGES_FILE already exists.  Skipping $DIR"
        continue
    fi

    JAVA_EX_FILES=`grep -l 'throw .*xception' $DIR/*.java 2> /dev/null`
    if test -z "$JAVA_EX_FILES"
    then
        continue
    fi

    echo ""
    echo "$DIR :"

    for JAVA_FILE in $JAVA_EX_FILES
    do
        TMP_EXCEPTIONS_FILE="/tmp/exceptions.$$.txt"
        rm -f $TMP_EXCEPTIONS_FILE

        cat $JAVA_FILE \
            | awk '
                  BEGIN {
                      state = "looking";
                  }

                  state == "looking" && $0 ~ /^[ ]*\/\*/ {
                      state = "comment_open";
                  }

                  state == "looking" && $0 ~ /^[ ]*\/\// {
                      state = "one_line_comment";
                  }

                  state == "looking" && $0 ~ /throw .*xception/ {
                      state = "exception";
                      exception = "";
                  }

                  state == "exception" {
                      gsub( "   *", " ", $0 );
                      exception = sprintf( "%s%s", exception, $0 );
                  }

                  state == "exception" && $0 ~ /;[ ]*$/ {
                      state = "looking";

                      gsub( "\"[ ]*\\+[ ]*\"", "", exception );
                      gsub( "\"[ ]*\\+[ ]*", "[%", exception );
                      gsub( "[ ]*\\+[ ]*\"", "%]", exception );

                      gsub( "^[^\"]*\"", "", exception );
                      gsub( "[\" );]*$", "", exception );

                      gsub( "\\[%[^\\]]*$", "&%]", exception );

                      print exception;
                  }

                  state == "comment_open" && $0 ~ /\*\// {
                      state = "looking";
                  }

                  state == "one_line_comment" {
                      state = "looking";
                  }
                  ' \
            | sed 's|\(\[%[^] ]*\)[ ]*);[ ]*$|\1\%\]" );|' \
            > $TMP_EXCEPTIONS_FILE

        EXCEPTIONS=`cat $TMP_EXCEPTIONS_FILE`

        rm -f $TMP_EXCEPTIONS_FILE

        if test -z "$EXCEPTIONS"
        then
            continue
        fi

        echo "    $JAVA_FILE"

        echo "$EXCEPTIONS" \
            | awk '
            {
                id = $0;
                message = $0;

                gsub( " ", "\\ ", id );
                gsub( "=", "\\=", id );

                print id " = " message;
            }
            ' \
            > $TMP_EXCEPTIONS_FILE

        MESSAGES=`cat $TMP_EXCEPTIONS_FILE`

        rm -f $TMP_EXCEPTIONS_FILE

        echo "$MESSAGES" >> $MESSAGES_FILE
        RESULT=$?

        if test $RESULT -ne 0
        then
            echo "Failed! on $JAVA_FILE"
            exit $RESULT
        fi
    done

    if test -f $MESSAGES_FILE
    then
        cat $MESSAGES_FILE | sort -d > ${MESSAGES_FILE}.sorted

        echo "$MESSAGES_FILE"
    fi
done
