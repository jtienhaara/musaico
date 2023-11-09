#
# Copyright (c) 2010 Johann Tienhaara <jtienhaara@yahoo.com>
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

# !!! No rules defined yet...

SUB_DIRS = 

PROJECT_DIR = ..
include $(PROJECT_DIR)/Project.mk

# Don't include Deliverables.mk.  Just take the empty definitions, and
# then we'll define our own "make xyz".
include $(MUSAICO_DIR)/build/rules/deliverable_dependencies.mk

# ==================================================================
# This directory make documentation.
.PHONY: main_documentation
main_documentation:

# Make compile, dist, and so on in all the sub-directories
# whenever "make documentation" is run.
.PHONY: sub_documentation
sub_documentation:
	@for RULE in dist run; \
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
