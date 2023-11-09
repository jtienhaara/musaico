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

# ==================================================================
.PHONY: clean
clean:
	@echo "Removing $(MUSAICO_DIR)/index.html"
	@rm -f $(MUSAICO_DIR)/index.html


# ==================================================================
.PHONY: compile
compile:
	@if test -d source; \
	then \
	  make -C source compile; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make compile in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
# !!! todo: add c, etc.
.PHONY: doc
doc:
	@CURR_DIR=`pwd`; \
	for PLATFORM_DIR in java; \
	do \
	  make -C $$PLATFORM_DIR doc; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make doc in $$PLATFORM_DIR"; \
	    exit 1; \
	  fi; \
	  echo "" >> $(MUSAICO_DIR)/index.html; \
	  echo "" >> $(MUSAICO_DIR)/index.html; \
	  echo "  <li> <a href=\"$$CURR_DIR/!!!!!!!!!_build/doc/index.html\" target=\"_new\">$$CURR_DIR</a> </li>" >> $(MUSAICO_DIR)/index.html; \
	done


# ==================================================================
.PHONY: optimize
optimize:
	@if test -d source; \
	then \
	  make -C source optimize; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make optimize in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
.PHONY: lib
lib:
	@if test -d source; \
	then \
	  make -C source lib; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make lib in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
.PHONY: bin
bin:
	@if test -d source; \
	then \
	  make -C source bin; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make bin in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
.PHONY: dist
dist:
	@if test -d source; \
	then \
	  make -C source dist; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make dist in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
.PHONY: install
install:
	@if test -d source; \
	then \
	  make -C source install; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make install in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
.PHONY: uninstall
uninstall:
	@if test -d source; \
	then \
	  make -C source uninstall; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make uninstall in source"; \
	    exit 1; \
	  fi; \
	fi


# ==================================================================
.PHONY: run
run:
	@if test -d source; \
	then \
	  make -C source run; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make run in source"; \
	    exit 1; \
	  fi; \
	fi
