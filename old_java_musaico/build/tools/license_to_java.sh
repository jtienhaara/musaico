#!/bin/sh

if test $# -lt 2
then
    echo "Usage:"
    echo "    license_to_java.sh AbstractXYZLicense license1.txt license2.txt..."
    echo ""
    exit 0
fi


ABSTRACT_LICENSE=$1
shift

if test `echo "$ABSTRACT_LICENSE" | sed 's|^Abstract||'` = "$ABSTRACT_LICENSE"
then
    echo "Usage:"
    echo "    license_to_java.sh AbstractXYZLicense license1.txt license2.txt..."
    echo ""
    exit 0
fi

CURRENT_DATE_TIME=`date '+%Y-%m-%d %H:%M:%S'`

for LICENSE_PATH in $*
do
    LICENSE_DIR=`dirname "$LICENSE_PATH"`
    LICENSE_FILE=`basename "$LICENSE_PATH"`
    JAVA_CLASS=`echo "$LICENSE_FILE" | sed 's|\.[^\.]*$||' | sed 's|[\.\_\-]||g'`
    JAVA_FILE="${JAVA_CLASS}.java"
    JAVA_PATH="$LICENSE_DIR/$JAVA_FILE"

    echo "package musaico.module;" > $JAVA_PATH
    echo "" >> $JAVA_PATH
    echo "import java.io.Serializable;" >> $JAVA_PATH
    echo "" >> $JAVA_PATH
    echo "" >> $JAVA_PATH
    echo "/**" >> $JAVA_PATH
    echo " * <p>" >> $JAVA_PATH
    echo " * $JAVA_CLASS license generated from" >> $JAVA_PATH
    echo " * license_to_java.sh $ABSTRACT_LICENSE $LICENSE_PATH" >> $JAVA_PATH
    echo " * ${CURRENT_DATE_TIME}." >> $JAVA_PATH
    echo " * </p>" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * <p>" >> $JAVA_PATH
    echo " * The bodies and compatibilities of licenses are taken from" >> $JAVA_PATH
    echo " * <a href=\"https://gnu.org/licenses/\">https://gnu.org/licenses/</a>." >> $JAVA_PATH
    echo " * </p>" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * <p>" >> $JAVA_PATH
    echo " * In Java every License must be Serializable in order to play nicely" >> $JAVA_PATH
    echo " * over RMI." >> $JAVA_PATH
    echo " * </p>" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * <br> </br>" >> $JAVA_PATH
    echo " * <br> </br>" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * <hr> </hr>" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * <br> </br>" >> $JAVA_PATH
    echo " * <br> </br>" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * <pre>" >> $JAVA_PATH
    echo " * License text copyright the author(s)." >> $JAVA_PATH
    echo " *" >> $JAVA_PATH
    echo " * Redistribution and use in source and binary forms, with or without" >> $JAVA_PATH
    echo " * modification, are permitted provided that the following conditions are" >> $JAVA_PATH
    echo " * met:" >> $JAVA_PATH
    echo " * " >> $JAVA_PATH
    echo " *     (1) Redistributions of source code must retain the above copyright" >> $JAVA_PATH
    echo " *     notice, this list of conditions and the following disclaimer. " >> $JAVA_PATH
    echo " * " >> $JAVA_PATH
    echo " *     (2) Redistributions in binary form must reproduce the above copyright" >> $JAVA_PATH
    echo " *     notice, this list of conditions and the following disclaimer in" >> $JAVA_PATH
    echo " *     the documentation and/or other materials provided with the" >> $JAVA_PATH
    echo " *     distribution.  " >> $JAVA_PATH
    echo " *     " >> $JAVA_PATH
    echo " *     (3)The name of the author may not be used to" >> $JAVA_PATH
    echo " *     endorse or promote products derived from this software without" >> $JAVA_PATH
    echo " *     specific prior written permission." >> $JAVA_PATH
    echo " * " >> $JAVA_PATH
    echo " * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR" >> $JAVA_PATH
    echo " * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED" >> $JAVA_PATH
    echo " * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE" >> $JAVA_PATH
    echo " * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT," >> $JAVA_PATH
    echo " * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES" >> $JAVA_PATH
    echo " * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR" >> $JAVA_PATH
    echo " * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)" >> $JAVA_PATH
    echo " * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT," >> $JAVA_PATH
    echo " * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING" >> $JAVA_PATH
    echo " * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE" >> $JAVA_PATH
    echo " * POSSIBILITY OF SUCH DAMAGE." >> $JAVA_PATH
    echo " * </pre>" >> $JAVA_PATH
    echo " */" >> $JAVA_PATH
    echo "public class ${JAVA_CLASS}" >> $JAVA_PATH
    echo "    extends $ABSTRACT_LICENSE" >> $JAVA_PATH
    echo "    implements Serializable" >> $JAVA_PATH
    echo "{" >> $JAVA_PATH
    echo "    private static final long serialVersionUID = 1L;" >> $JAVA_PATH
    echo "" >> $JAVA_PATH
    echo "    /**" >> $JAVA_PATH
    echo "     * @see musaico.module.License#body()" >> $JAVA_PATH
    echo "     */" >> $JAVA_PATH
    echo "    @Override" >> $JAVA_PATH
    echo "    public final String body ()" >> $JAVA_PATH
    echo "    {" >> $JAVA_PATH
    echo "        final String license_body = \"\"" >> $JAVA_PATH
    cat "$LICENSE_PATH" \
        | sed 's|\(["\\]\)|\\\1|g' \
        | awk '{ print "            + \"" $0 "\\n\"" }' \
        >> $JAVA_PATH
    echo "            + \"\";" >> $JAVA_PATH
    echo "" >> $JAVA_PATH
    echo "        return license_body;" >> $JAVA_PATH
    echo "    }" >> $JAVA_PATH
    echo "}" >> $JAVA_PATH
done
