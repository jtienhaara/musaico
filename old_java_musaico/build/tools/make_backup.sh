#!/bin/sh

DATE=`date '+%Y-%m-%d'`
BACKUP_DIR="/home/media3/backup"

rm -f /tmp/make_backup.log
touch /tmp/make_backup.log

echo "$0"  \
        | tee -a /tmp/make_backup.log
echo "$DATE:"  \
        | tee -a /tmp/make_backup.log
echo ""  \
        | tee -a /tmp/make_backup.log

cd ..
RESULT=$?
if test $RESULT -ne 0
then
    echo "Could not cd .."  \
        | tee -a /tmp/make_backup.log
    exit $RESULT
fi

if test ! -d musaico
then
    echo "Where is musaico directory?!?"  \
        | tee -a /tmp/make_backup.log
    exit 1
fi

if test ! -d $BACKUP_DIR
then
    echo "Backup directory $BACKUP_DIR does not exist!"  \
        | tee -a /tmp/make_backup.log
    exit 2
elif test ! -x $BACKUP_DIR -o ! -w $BACKUP_DIR
then
    USER_NAME=`id -u -n`
    echo "Backup directory $BACKUP_DIR is not accessible to $USER_NAME:"  \
        | tee -a /tmp/make_backup.log
    echo -n "    "  \
        | tee -a /tmp/make_backup.log
    ls -ld $BACKUP_DIR  \
        | tee -a /tmp/make_backup.log
    exit 1
fi

for LETTER in "" a b c d e f g h i j k l m n o p q r s t u v w x y z
do
    TAR_FILENAME="$BACKUP_DIR/musaico.${DATE}${LETTER}.tar.gz"

    if test -f $TAR_FILENAME
    then
        continue
    fi

    tar cvzf $TAR_FILENAME \
        --exclude 'musaico/third_party/apache-ant*' \
        --exclude 'musaico/third_party/emma*' \
        --exclude 'musaico/third_party/findbugs*' \
        --exclude 'musaico/third_party/jlint*' \
        --exclude 'musaico/third_party/jmock*' \
        --exclude 'musaico/third_party/junit*' \
        --exclude '*/musaico/*/*.gz' \
        --exclude '*/musaico/*/*.jar' \
        --exclude '*/_build' \
        --exclude '*~' \
        musaico/  \
        2>&1  \
        | tee -a /tmp/make_backup.log
    RESULT=$?

    if test $RESULT -ne 0
    then
        echo "Backup failed!"  \
            | tee -a /tmp/make_backup.log
        exit $RESULT
    fi

    echo "Successfully created $TAR_FILENAME"  \
        | tee -a /tmp/make_backup.log
    echo -n "Size in bytes: "  \
        | tee -a /tmp/make_backup.log
    du -s -b $TAR_FILENAME  \
        | tee -a /tmp/make_backup.log
    echo `date`  \
        | tee -a /tmp/make_backup.log
    exit 0
done

echo "Couldn't find a filename to backup to!"  \
    | tee -a /tmp/make_backup.log
echo "    $TAR_FILENAME"  \
    | tee -a /tmp/make_backup.log
exit 1
