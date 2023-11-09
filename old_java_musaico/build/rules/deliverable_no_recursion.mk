#
# By default do NOT "make design" inside the other sub-directories
# (source, test, examples and so on).
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

# ==================================================================
.PHONY: sub_roadmap
sub_roadmap:

# ==================================================================
.PHONY: sub_analysis
sub_analysis:

# ==================================================================
.PHONY: sub_design
sub_design:

# ==================================================================
.PHONY: sub_source
sub_source:

# ==================================================================
.PHONY: sub_test
sub_test:

# ==================================================================
.PHONY: sub_documentation
sub_documentation:

# ==================================================================
.PHONY: sub_examples
sub_examples:

# ==================================================================
.PHONY: sub_profile
sub_profile:
