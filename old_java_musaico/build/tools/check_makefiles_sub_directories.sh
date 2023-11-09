for DIR in `find -type d`
do
  OLD_DIR=`pwd`
  cd $DIR
  DIR_PROBLEMS=`~/development/musaico/build/tools/check_makefile.sh`
  cd $OLD_DIR

  if test "$DIR_PROBLEMS" != ""
  then
    echo "--------------- $DIR ---------------"
    echo "$DIR_PROBLEMS"
  fi
done
