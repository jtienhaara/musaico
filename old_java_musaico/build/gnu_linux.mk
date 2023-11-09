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

# Settings for the Gnu/Linux operating system.

# Should only be included os.mk.

# ==================================================================
# Variables:

BACKUP = /bin/tar cvzf
BACKUP_PRE = --exclude '*~'
BACKUP_POST = 
COPY = /bin/cp -f
COPY_DIRS = $(COPY) -r
PARENT_DIR = dirname
MKDIR = mkdir -p
REMOVE = /bin/rm -f
REMOVE_DIRS = $(REMOVE) -r

COMPILERS_BASE_DIR = /usr


#
# Prevent default from being our first rule:
#
# ==================================================================
.PHONY: all
all:


#
# Special operating system-specific rules:
#

# ==================================================================
.PHONY: os_clean
os_clean:
	$(REMOVE) \
		tags \
		TAGS

# ==================================================================
.PHONY: os_prebuild
os_prebuild:

# ==================================================================
.PHONY: os_dependsinfo
os_dependsinfo:

# ==================================================================
.PHONY: os_dependscheck
os_dependscheck:

# ==================================================================
.PHONY: os_compile
os_compile:
	@if test ! -z "$(COMPILE)"; \
	then \
	  echo "Creating vi tags file for $(COMPILE)"; \
	  $(REMOVE) tags; ctags $(COMPILE); \
	  echo "Creating Emacs TAGS file for $(COMPILE)"; \
	  $(REMOVE) TAGS; etags $(COMPILE); \
	fi

# ==================================================================
.PHONY: os_optimize
os_optimize:

# ==================================================================
.PHONY: os_doc
os_doc:

# ==================================================================
.PHONY: os_examples
os_examples:

# ==================================================================
.PHONY: os_lib
os_lib:

# ==================================================================
.PHONY: os_bin
os_bin:

# ==================================================================
.PHONY: os_dist
os_dist:

# ==================================================================
.PHONY: os_install
os_install:

# ==================================================================
.PHONY: os_uninstall
os_uninstall:

# ==================================================================
.PHONY: os_run
os_run:

# ==================================================================
.PHONY: os_profile
os_profile:

# ==================================================================
.PHONY: os_test
os_test:

# ==================================================================
.PHONY: os_help
os_help:
	@echo "Run make with one of the following targets:"
	@echo "    clean"
	@echo "        Removes all temporary files used for building."
	@echo "    dependsinfo"
	@echo "        Returns all dependencies for the package being compiled."
	@echo "    dependscheck"
	@echo "        Checks all package dependencies, and fails if"
	@echo "        any are not built."
	@echo "    compile"
	@echo "        Compiles the package."
	@echo "    optimize"
	@echo "        Optimizes the compiled package."
	@echo "    doc"
	@echo "        Generates documentation for the package."
	@echo "    examples"
	@echo "        Builds all examples for the package (compile, run, ...)."
	@echo "    lib"
	@echo "        Builds a portable library from the compiled package."
	@echo "    bin"
	@echo "        Builds an executable from the package's library."
	@echo "    dist"
	@echo "        Builds a distribution bundle from the executable,"
	@echo "        documentation, and so on."
	@echo "    install"
	@echo "        Installs the distribution bundle onto the system."
	@echo "    uninstall"
	@echo "        Removes the installed distribution from the system"
	@echo "        (does not touch the distribution bundle)."
	@echo "    run"
	@echo "        Executes the package's executable."
	@echo "    profile"
	@echo "        Builds all profiles for the package (compile, run, ...)."
	@echo "    test"
	@echo "        Builds all tests for the package (compile, run, ...)."
	@echo "    help"
	@echo "        Outputs this help message."
