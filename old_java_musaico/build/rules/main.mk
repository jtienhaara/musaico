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

# Rules for "make clean" etc only in the current directory.
#
# Included from rules.mk (or, in some cases, directly from a Makefile).

# Typically dependencies.mk is included to define "make test" to
# rely on this for "make test" only in the current directory,
# as well as sub_dirs.mk to "make test" in the sub-directories.


# ==================================================================
.PHONY: main_clean
main_clean: os_clean platform_clean


# OS-dependent clean.
.PHONY: os_clean
os_clean:

# Platform-dependent clean.
.PHONY: platform_clean
platform_clean:


# ==================================================================
.PHONY: main_prebuild
main_prebuild: os_prebuild platform_prebuild

# OS-dependent prebuild.
.PHONY: os_prebuild
os_prebuild:

# Platform-dependent prebuild.
.PHONY: platform_prebuild
platform_prebuild:


# ==================================================================
.PHONY: main_dependsinfo
main_dependsinfo: os_dependsinfo platform_dependsinfo

# OS-dependent dependsinfo.
.PHONY: os_dependsinfo
os_dependsinfo:

# Platform-dependent dependsinfo.
.PHONY: platform_dependsinfo
platform_dependsinfo:


# ==================================================================
.PHONY: main_dependscheck
main_dependscheck: os_dependscheck platform_dependscheck

# OS-dependent dependscheck.
.PHONY: os_dependscheck
os_dependscheck:

# Platform-dependent dependscheck.
.PHONY: platform_dependscheck
platform_dependscheck:


# ==================================================================
.PHONY: main_compile
main_compile: os_compile platform_compile

# OS-dependent compile.
.PHONY: os_compile
os_compile:

# Platform-dependent compile.
.PHONY: platform_compile
platform_compile:


# ==================================================================
.PHONY: main_optimize
main_optimize: os_optimize platform_optimize

# OS-dependent optimize.
.PHONY: os_optimize
os_optimize:

# Platform-dependent optimize.
.PHONY: platform_optimize
platform_optimize:


# ==================================================================
.PHONY: main_doc
main_doc: os_doc platform_doc

# OS-dependent doc.
.PHONY: os_doc
os_doc:

# Platform-dependent doc.
.PHONY: platform_doc
platform_doc:


# ==================================================================
.PHONY: main_examples
main_examples: os_examples platform_examples

# OS-dependent examples.
.PHONY: os_examples
os_examples:

# Platform-dependent examples.
.PHONY: platform_examples
platform_examples:


# ==================================================================
.PHONY: main_lib
main_lib: os_lib platform_lib

# OS-dependent lib.
.PHONY: os_lib
os_lib:

# Platform-dependent lib.
.PHONY: platform_lib
platform_lib:


# ==================================================================
.PHONY: main_bin
main_bin: os_bin platform_bin

# OS-dependent bin.
.PHONY: os_bin
os_bin:

# Platform-dependent bin.
.PHONY: platform_bin
platform_bin:


# ==================================================================
.PHONY: main_dist
main_dist: main_doc main_examples os_dist platform_dist

# OS-dependent dist.
.PHONY: os_dist
os_dist:

# Platform-dependent dist.
.PHONY: platform_dist
platform_dist:


# ==================================================================
.PHONY: main_install
main_install: os_install platform_install

# OS-dependent install.
.PHONY: os_install
os_install:

# Platform-dependent install.
.PHONY: platform_install
platform_install:


# ==================================================================
.PHONY: main_uninstall
main_uninstall: os_uninstall platform_uninstall

# OS-dependent uninstall.
.PHONY: os_uninstall
os_uninstall:

# Platform-dependent uninstall.
.PHONY: platform_uninstall
platform_uninstall:


# ==================================================================
.PHONY: main_run
main_run: os_run platform_run

# OS-dependent run.
.PHONY: os_run
os_run:

# Platform-dependent run.
.PHONY: platform_run
platform_run:


# ==================================================================
.PHONY: main_profile
main_profile: os_profile platform_profile

# OS-dependent profile.
.PHONY: os_profile
os_profile:

# Platform-dependent profile.
.PHONY: platform_profile
platform_profile:


# ==================================================================
.PHONY: main_test
main_test: os_test platform_test

# OS-dependent test.
.PHONY: os_test
os_test:

# Platform-dependent test.
.PHONY: platform_test
platform_test:


# ==================================================================
.PHONY: main_help
main_help: os_help platform_help

# OS-dependent help.
.PHONY: os_help
os_help:

# Platform-dependent help.
.PHONY: platform_help
platform_help:
