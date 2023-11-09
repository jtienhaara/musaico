#!/bin/sh

if test $# -eq 0
then
    DIRS=`find * -type d | grep -v '/third_party.*' | grep -v '/THOUGHTS.*' | grep -v '/lib' | grep -v '/classes' | grep -v '/build.*' | grep -v '/node_modules' | grep -v '/repository' | grep -v '/dist.*' | grep -v '/\..*' | grep -v '/doc' | grep -v '/javadoc' | grep -v '/webapp' | grep -v '/Frameworks' | grep -v 'Resources' | grep -v '/target' | sed 's| |\\\\ |g' | sort -d`
else
    DIRS="$*"
fi

echo "Directories:"
echo "============"
echo "$DIRS"
echo "!!! 1"
# !!! find `echo $DIRS | sed 's|^\(.*\)$|"\1"|'` -maxdepth 0
# !!! echo $DIRS -maxdepth 0 | xargs find
# !!! | sed 's| |\\\\ |g'
# !!!  | sed 's|^\(.*\)$|"\1"|'
echo $DIRS -maxdepth 1 -name "\'*.java\'" \
        -exec grep -v "\'^[ \\t\\(\\{\\}\\)\\;]*$\'" "\'{}\'" "\';\'" \
        | xargs echo find
echo "!!! 2"
exit 0

echo ""
echo "Line counts:"
echo "============"

TOTAL_LINES=0
for FILE_PATTERN in '*.java' '*.c' '*.kt' '*.js' '*.cpp' '*.cmd' '*.cs' '*.css' '*.dtd' '*.groovy' '*.tcl' '*.sh' '*.bash' '*.h' '*.hpp' '*.php' '*.py' '*.ruby' '*.sql' '*.swift'
do
    CURR_LINES=`echo $DIRS -maxdepth 1 -name "\'$FILE_PATTERN\'" \
        -exec grep -v '''^[ \t\(\{\}\)\;]*$''' '''{}''' ''';''' \
        | xargs find \
        | awk '
            BEGIN { state = "code"; };

            state == "code" && ( $1 == "/*" || $1 == "/**" ) {
                state = "comment";
            };

            state == "code" && $1 == "//" {
                state = "comment_one_line";
            };

            state == "code" {
                trimmed_code = $0;
                gsub( "[ \t]*", "", trimmed_code );
                gsub( "//.*$", "", trimmed_code );
                gsub( "/[*].*[*]/", "", trimmed_code );
                gsub( "^package.*;$", "", trimmed_code );
                gsub( "^import.*;$", "", trimmed_code );
                gsub( "^#.*$", "", trimmed_code );

                if ( length( trimmed_code ) > 2 )
                {
                    print trimmed_code;
                }
            };

            state == "comment" && $NF == "*/" {
                state = "code";
            };




















            state == "comment_one_line" {
                state = "code";
            };
            ' \
        | wc -l \
        `
    echo "$FILE_PATTERN : $CURR_LINES"
    NEW_TOTAL_LINES=`expr $TOTAL_LINES + $CURR_LINES`
    TOTAL_LINES=$NEW_TOTAL_LINES
done

echo $TOTAL_LINES
exit 0
