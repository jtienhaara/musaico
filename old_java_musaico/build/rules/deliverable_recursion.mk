#
# Do "make design" in sub-directories.  (Probably will only ever be
# used by the very toplevel Makefile, but it's here to be re-used
# if need be.)
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

# Rules for "make analysis" etc recursively through sub-directories.
#
# Included from deliverables.mk (or, in some cases, directly from a Makefile).
#
# Makefiles which only respond to specific deliverables (such as building
# tests whenever "make test" is run or building examples whenever
# "make examples" is run) but do not respond to "make compile",
# "make lib", and so on, should forego including this file
# (and deliverables.mk) and simply include deliverable_main.mk to define the
# empty deliverable rules.
#
# SUB_DIRS should be defined before including this file.

# ==================================================================
.PHONY: sub_clean
sub_clean:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR clean; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make clean in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_roadmap
sub_roadmap:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR roadmap; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make roadmap in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_analysis
sub_analysis:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR analysis; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make analysis in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_design
sub_design:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR design; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make design in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_source
sub_source:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR source; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make source in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_test
sub_test:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR test; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make test in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_documentation
sub_documentation:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR documentation; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make documentation in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_examples
sub_examples:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR examples; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make examples in $$DIR"; \
	    exit 1; \
	  fi; \
	done

# ==================================================================
.PHONY: sub_profile
sub_profile:
	@for DIR in $(SUB_DIRS); \
	do \
	  make -C $$DIR profile; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make profile in $$DIR"; \
	    exit 1; \
	  fi; \
	done
