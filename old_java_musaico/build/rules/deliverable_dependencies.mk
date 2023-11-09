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

# Dependencies for deliverable makefiles (make roadmap, make test, ...).


# ==================================================================
.PHONY: all
all: $(DELIVERABLES)

# ==================================================================
.PHONY: clean
clean: main_clean sub_clean

# Clean up this directory.
.PHONY: main_clean
main_clean:

# Clean up the sub-directories.
.PHONY: sub_clean
sub_clean:


# ==================================================================
.PHONY: roadmap
roadmap: main_roadmap sub_roadmap

.PHONY: main_roadmap
main_roadmap:

.PHONY: sub_roadmap
sub_roadmap:


# ==================================================================
.PHONY: analysis
analysis: main_analysis sub_analysis

.PHONY: main_analysis
main_analysis:

.PHONY: sub_analysis
sub_analysis:


# ==================================================================
.PHONY: design
design: main_design sub_design

.PHONY: main_design
main_design:

.PHONY: sub_design
sub_design:


# ==================================================================
.PHONY: source
source: main_source sub_source

.PHONY: main_source
main_source:

.PHONY: sub_source
sub_source:


# ==================================================================
.PHONY: test
test: main_test sub_test

.PHONY: main_test
main_test:

.PHONY: sub_test
sub_test:


# ==================================================================
.PHONY: documentation
documentation: main_documentation sub_documentation

.PHONY: main_documentation
main_documentation:

.PHONY: sub_documentation
sub_documentation:


# ==================================================================
.PHONY: 
examples: main_examples sub_examples

.PHONY: main_examples
main_examples:

.PHONY: sub_examples
sub_examples:


# ==================================================================
.PHONY: profile
profile: main_profile sub_profile

.PHONY: main_profile
main_profile:

.PHONY: sub_profile
sub_profile:


# ==================================================================
.PHONY: help
help: os_help
