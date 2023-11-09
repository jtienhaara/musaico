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

# Rules for "make clean" etc in the current dir and sub-dirs.
#
# Included from rules.mk (or, in some cases, directly from a Makefile).

# Usually dir.mk is included to define the dependencies for
# the main_* rules.
#
# And sub_dirs.mk is included to define the dependencies for the
# sub_* rules.


# ==================================================================
.PHONY: all
all: dist


# ==================================================================
.PHONY: clean
clean: main_clean sub_clean


# This directory only clean.
.PHONY: main_clean
main_clean:

# Sub-dirs clean.
.PHONY: sub_clean
sub_clean:


# ==================================================================
.PHONY: prebuild
prebuild: main_prebuild sub_prebuild

# This directory only (not sub-dirs) prebuild.
.PHONY: main_prebuild
main_prebuild:

# Sub-dirs prebuild.
.PHONY: sub_prebuild
sub_prebuild:


# ==================================================================
.PHONY: dependsinfo
dependsinfo: main_dependsinfo sub_dependsinfo

# This directory only (not sub-dirs) dependsinfo.
.PHONY: main_dependsinfo
main_dependsinfo:

# Sub-dirs dependsinfo.
.PHONY: sub_dependsinfo
sub_dependsinfo:


# ==================================================================
.PHONY: dependscheck
dependscheck: main_dependscheck sub_dependscheck

# This directory only (not sub-dirs) dependscheck.
.PHONY: main_dependscheck
main_dependscheck:

# Sub-dirs dependscheck.
.PHONY: sub_dependscheck
sub_dependscheck:


# ==================================================================
.PHONY: compile
compile: main_compile sub_compile

# This directory only (not sub-dirs) compile.
.PHONY: main_compile
main_compile:

# Sub-dirs compile.
.PHONY: sub_compile
sub_compile:


# ==================================================================
.PHONY: optimize
optimize: main_optimize sub_optimize

# This directory only (not sub-dirs) optimize.
.PHONY: main_optimize
main_optimize:

# Sub-dirs optimize.
.PHONY: sub_optimize
sub_optimize:


# ==================================================================
.PHONY: doc
doc: main_doc sub_doc

# This directory only (not sub-dirs) doc.
.PHONY: main_doc
main_doc:

# Sub-dirs doc.
.PHONY: sub_doc
sub_doc:


# ==================================================================
.PHONY: examples
examples: main_examples sub_examples

# This directory only (not sub-dirs) examples.
.PHONY: main_examples
main_examples:

# Sub-dirs examples.
.PHONY: sub_examples
sub_examples:


# ==================================================================
.PHONY: lib
lib: main_lib sub_lib

# This directory only (not sub-dirs) lib.
.PHONY: main_lib
main_lib:

# Sub-dirs lib.
.PHONY: sub_lib
sub_lib:


# ==================================================================
.PHONY: bin
bin: main_bin sub_bin

# This directory only (not sub-dirs) bin.
.PHONY: main_bin
main_bin:

# Sub-dirs bin.
.PHONY: sub_bin
sub_bin:


# ==================================================================
.PHONY: dist
dist: main_dist sub_dist

# This directory only (not sub-dirs) dist.
.PHONY: main_dist
main_dist:

# Sub-dirs dist.
.PHONY: sub_dist
sub_dist:


# ==================================================================
.PHONY: install
install: main_install sub_install

# This directory only (not sub-dirs) install.
.PHONY: main_install
main_install:

# Sub-dirs install.
.PHONY: sub_install
sub_install:


# ==================================================================
.PHONY: uninstall
uninstall: main_uninstall sub_uninstall

# This directory only (not sub-dirs) uninstall.
.PHONY: main_uninstall
main_uninstall:

# Sub-dirs uninstall.
.PHONY: sub_uninstall
sub_uninstall:


# ==================================================================
.PHONY: run
run: main_run sub_run

# This directory only (not sub-dirs) run.
.PHONY: main_run
main_run:

# Sub-dirs run.
.PHONY: sub_run
sub_run:


# ==================================================================
.PHONY: profile
profile: main_profile sub_profile

# This directory only (not sub-dirs) profile.
.PHONY: main_profile
main_profile:

# Sub-dirs profile.
.PHONY: sub_profile
sub_profile:


# ==================================================================
.PHONY: test
test: main_test sub_test

# This directory only (not sub-dirs) test.
.PHONY: main_test
main_test:

# Sub-dirs test.
.PHONY: sub_test
sub_test:


# ==================================================================
.PHONY: help
help: main_help sub_help

# This directory only (not sub-dirs) help.
.PHONY: main_help
main_help:

# Sub-dirs help.
.PHONY: sub_help
sub_help:
