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

MUSAICO_DIR = ..

include $(MUSAICO_DIR)/build/os.mk
include $(MUSAICO_DIR)/build/rules/rule_makedebug.mk

include third_party.mk


# ==================================================================
.PHONY: all
all: compile


# ==================================================================
.PHONY: clean
clean:
	@echo "Removing $(THIRD_PARTY_DIRS)"; \
	$(REMOVE_DIRS) $(THIRD_PARTY_DIRS); \
	RESULT=$$?; \
	if test $$RESULT -ne 0; \
	then \
	  echo "Failed to remove third party dirs: $(THIRD_PARTY_DIRS)"; \
	  exit $$RESULT; \
	fi


# ==================================================================
.PHONY: project
project: source


# ==================================================================
.PHONY: analysis
analysis:
	@echo "Nothing to be done in third_party for analysis"


# ==================================================================
.PHONY: design
design:
	@echo "Nothing to be done in third_party for design"


# ==================================================================
.PHONY: source
source: compile


# ==================================================================
.PHONY: test
test:
	@echo "Nothing to be done in third_party for test"


# ==================================================================
.PHONY: documentation
documentation:
	@echo "Nothing to be done in third_party for documentation"


# ==================================================================
.PHONY: examples
examples:
	@echo "Nothing to be done in third_party for examples"


# ==================================================================
.PHONY: profile
profile:
	@echo "Nothing to be done in third_party for profile"


# ==================================================================
.PHONY: compile
compile: unzip


# ==================================================================
.PHONY: unzip
unzip: unzip_java

.PHONY: unzip_java
unzip_java: unzip_ant unzip_emma unzip_findbugs unzip_jlint unzip_jmock \
	unzip_junit

.PHONY: unzip_ant
unzip_ant:
	@if test ! -d $(ANT_DIR); \
	then \
	  for ZIP_FILE in $(ANT_ZIPS); \
	  do \
	    echo "Unzipping $$ZIP_FILE"; \
	    unzip -o $$ZIP_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not unzip $$ZIP_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	  \
	  for TAR_GZ_FILE in $(ANT_TAR_GZS); \
	  do \
	    echo "Untarring $$TAR_GZ_FILE"; \
	    tar xvz --overwrite -f $$TAR_GZ_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not untar $$TAR_GZ_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	fi

.PHONY: unzip_emma
unzip_emma:
	@if test ! -d $(EMMA_DIR); \
	then \
	  for ZIP_FILE in $(EMMA_ZIPS); \
	  do \
	    echo "Unzipping $$ZIP_FILE"; \
	    unzip -o $$ZIP_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not unzip $$ZIP_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	  \
	  for TAR_GZ_FILE in $(EMMA_TAR_GZS); \
	  do \
	    echo "Untarring $$TAR_GZ_FILE"; \
	    tar xvz --overwrite -f $$TAR_GZ_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not untar $$TAR_GZ_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	fi

.PHONY: unzip_findbugs
unzip_findbugs:
	@if test ! -d $(FINDBUGS_DIR); \
	then \
	  for ZIP_FILE in $(FINDBUGS_ZIPS); \
	  do \
	    echo "Unzipping $$ZIP_FILE"; \
	    unzip -o $$ZIP_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not unzip $$ZIP_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	  \
	  for TAR_GZ_FILE in $(FINDBUGS_TAR_GZS); \
	  do \
	    echo "Untarring $$TAR_GZ_FILE"; \
	    tar xvz --overwrite -f $$TAR_GZ_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not untar $$TAR_GZ_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	fi

.PHONY: unzip_jlint
unzip_jlint:
	@if test ! -d $(JLINT_DIR); \
	then \
	  for ZIP_FILE in $(JLINT_ZIPS); \
	  do \
	    echo "Unzipping $$ZIP_FILE"; \
	    unzip -o $$ZIP_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not unzip $$ZIP_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	  \
	  for TAR_GZ_FILE in $(JLINT_TAR_GZS); \
	  do \
	    echo "Untarring $$TAR_GZ_FILE"; \
	    tar xvz --overwrite -f $$TAR_GZ_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not untar $$TAR_GZ_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	fi

.PHONY: unzip_jmock
unzip_jmock:
	@if test ! -d $(JMOCK_DIR); \
	then \
	  for ZIP_FILE in $(JMOCK_ZIPS); \
	  do \
	    echo "Unzipping $$ZIP_FILE"; \
	    unzip -o $$ZIP_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not unzip $$ZIP_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	  \
	  for TAR_GZ_FILE in $(JMOCK_TAR_GZS); \
	  do \
	    echo "Untarring $$TAR_GZ_FILE"; \
	    tar xvz --overwrite -f $$TAR_GZ_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not untar $$TAR_GZ_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	fi

.PHONY: unzip_junit
unzip_junit:
	@if test ! -d $(JUNIT_DIR); \
	then \
	  for ZIP_FILE in $(JUNIT_ZIPS); \
	  do \
	    echo "Unzipping $$ZIP_FILE"; \
	    unzip -o $$ZIP_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not unzip $$ZIP_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	  \
	  for TAR_GZ_FILE in $(JUNIT_TAR_GZS); \
	  do \
	    echo "Untarring $$TAR_GZ_FILE"; \
	    tar xvz --overwrite -f $$TAR_GZ_FILE > /dev/null; \
	    RESULT=$$?; \
	    if test $$RESULT -ne 0; \
	    then \
	      echo "Could not untar $$TAR_GZ_FILE: Error code $$RESULT"; \
	      exit $$RESULT; \
	    fi; \
	  done; \
	fi


# ==================================================================
.PHONY: doc
doc:
	@echo "Nothing to be done in third_party for doc"


# ==================================================================
.PHONY: optimize
optimize:
	@echo "Nothing to be done in third_party for optimize"


# ==================================================================
.PHONY: lib
lib:
	@echo "Nothing to be done in third_party for lib"


# ==================================================================
.PHONY: bin
bin:
	@echo "Nothing to be done in third_party for bin"


# ==================================================================
.PHONY: dist
dist:
	@echo "Nothing to be done in third_party for dist"


# ==================================================================
.PHONY: install
install:
	@echo "Nothing to be done in third_party for install"


# ==================================================================
.PHONY: uninstall
uninstall:
	@echo "Nothing to be done in third_party for uninstall"


# ==================================================================
.PHONY: run
run:
	@echo "Nothing to be done in third_party for run"
