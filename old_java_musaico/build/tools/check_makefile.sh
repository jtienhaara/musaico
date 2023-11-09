for JAVA_FILE in *.java
do
  if test "$JAVA_FILE" = "*.java"
  then
      echo "No Java files in this directory."
      break
  fi

  CLASS_FILE=`echo "$JAVA_FILE" | sed "s|\.java|.class|"`
  IN_MAKEFILE=`grep "[^a-zA-Z0-9]$CLASS_FILE" Makefile`
  if test "$IN_MAKEFILE" = ""
  then
    echo "NOT listed in Makefile: $CLASS_FILE"
  fi
done

