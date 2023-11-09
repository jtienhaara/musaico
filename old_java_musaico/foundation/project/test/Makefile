#
# Copyright (c) 2009-2018 Johann Tienhaara
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

TEST_CLASSES = \
	static \
	coverage \
	unit \
	integration \
	system \
	stress

SUB_DIRS = \
	$(TEST_CLASSES)

TEST_DIR = .
include $(TEST_DIR)/test.mk

# Don't include rules.mk.  Just take the empty definitions, and
# then we'll define our own "make test".
include $(MUSAICO_DIR)/build/rules/dependencies.mk
# Don't include $(MUSAICO_DIR)/build/rules/main.mk
# Don't include $(MUSAICO_DIR)/build/rules/sub_dirs.mk

# ==================================================================
# Clean up this directory.
.PHONY: main_clean
main_clean:

# Clean up the sub-directories.
.PHONY: sub_clean
clean:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make clean in $$DIR"; \
	  make -C $$DIR clean; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make clean in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# This directory make test.
.PHONY: main_test
main_test:

# Make compile, dist, and so on in all the sub-directories
# whenever "make test" is run.
.PHONY: sub_test
sub_test:
	@for RULE in compile run; \
	do \
	  for DIR in $(SUB_DIRS); \
	  do \
	    echo "Make $$RULE in $$DIR"; \
	    make -C $$DIR $$RULE; \
	    if test $$? -ne 0; \
	    then \
	      echo "Failed to make $$RULE in $$DIR"; \
	      exit 1; \
	    fi; \
	  done; \
	done
