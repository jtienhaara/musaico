#
# Make compile etc in sub-directories, probably only used by
# the toplevel Makefile.
#
# Copyright (c) 2009, 2012 Johann Tienhaara
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

# Compile etc rules for the main directory.
# !!! ????? what is this for???  include $(MUSAICO_DIR)/build/rules/source_toplevel_no_recursion.mk

# Compile etc rules for the sub-directories:

# ==================================================================
.PHONY: clean
clean:


# ==================================================================
.PHONY: compile
compile:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR compile; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make compile in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: doc
doc:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR doc; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make doc in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: optimize
optimize:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR optimize; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make optimize in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: lib
lib:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR lib; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make lib in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: bin
bin:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR bin; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make bin in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: dist
dist:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR dist; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make dist in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: install
install:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR install; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make install in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: uninstall
uninstall:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR uninstall; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make uninstall in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
.PHONY: run
run:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR run; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make run in $$DIR"; \
	    exit 1; \
	  fi; \
	done
