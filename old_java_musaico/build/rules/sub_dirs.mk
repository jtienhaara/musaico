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

# Rules for "make clean" etc recursively through sub-directories.
#
# Included from rules.mk (or, in some cases, directly from a Makefile).
#
# Makefiles which only respond to specific rules (such as building
# tests whenever "make test" is run or building examples whenever
# "make examples" is run) but do not respond to "make compile",
# "make lib", and so on, should forego including this file
# (and rules.mk) and simply include main.mk to define the
# empty rules.
#
# SUB_DIRS should be defined before including this file.


# ==================================================================
# Makefile-dependent sub-clean
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_clean
sub_clean:
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
# Makefile-dependent sub-prebuild
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_prebuild
sub_prebuild:
# Do nothing.


# ==================================================================
# Makefile-dependent sub-dependsinfo
# (sub-packages, sub-platforms, sub-deliverables, and so on).
.PHONY: sub_dependsinfo
sub_dependsinfo:
# Do nothing.


# ==================================================================
# Makefile-dependent sub-dependscheck
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_dependscheck
sub_dependscheck:
# Do nothing.


# ==================================================================
# Makefile-dependent sub-compile
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_compile
sub_compile:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make compile in $$DIR"; \
	  make -C $$DIR compile; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make compile in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# Makefile-dependent sub-optimize
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_optimize
sub_optimize:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make optimize in $$DIR"; \
	  make -C $$DIR optimize; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make optimize in $$DIR"; \
	    exit 1; \
	  fi; \
	done



# ==================================================================
# Makefile-dependent sub-doc
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_doc
sub_doc:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make doc in $$DIR"; \
	  make -C $$DIR doc; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make doc in $$DIR"; \
	    exit 1; \
	  fi; \
	done



# ==================================================================
# Makefile-dependent sub-examples
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_examples
sub_examples:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make examples in $$DIR"; \
	  make -C $$DIR examples; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make examples in $$DIR"; \
	    exit 1; \
	  fi; \
	done



# ==================================================================
# Makefile-dependent sub-lib
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_lib
sub_lib:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make lib in $$DIR"; \
	  make -C $$DIR lib; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make lib in $$DIR"; \
	    exit 1; \
	  fi; \
	done



# ==================================================================
# Makefile-dependent sub-bin
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_bin
sub_bin:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make bin in $$DIR"; \
	  make -C $$DIR bin; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make bin in $$DIR"; \
	    exit 1; \
	  fi; \
	done



# ==================================================================
# Makefile-dependent sub-dist
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_dist
sub_dist:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make dist in $$DIR"; \
	  make -C $$DIR dist; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make dist in $$DIR"; \
	    exit 1; \
	  fi; \
	done



# ==================================================================
# Makefile-dependent sub-install
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_install
sub_install:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make install in $$DIR"; \
	  make -C $$DIR install; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make install in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# Makefile-dependent sub-uninstall
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_uninstall
sub_uninstall:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make uninstall in $$DIR"; \
	  make -C $$DIR uninstall; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make uninstall in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# Makefile-dependent sub-run
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_run
sub_run:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make run in $$DIR"; \
	  make -C $$DIR run; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make run in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# Makefile-dependent sub-profile
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_profile
sub_profile:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make profile in $$DIR"; \
	  make -C $$DIR profile; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make profile in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# Makefile-dependent sub-test
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_test
sub_test:
	@for DIR in $(SUB_DIRS); \
	do \
	  echo "Make test in $$DIR"; \
	  make -C $$DIR test; \
	  if test $$? -ne 0; \
	  then \
	    echo "Failed to make test in $$DIR"; \
	    exit 1; \
	  fi; \
	done


# ==================================================================
# Makefile-dependent sub-help
# (sub-packages, sub-platforms, sub-deliverables and so on).
.PHONY: sub_help
sub_help:
# Do nothing.
