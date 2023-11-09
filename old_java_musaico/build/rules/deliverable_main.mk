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

# Rules for "make analysis" etc only in the current directory.
#
# Included from deliverables.mk (or, in some cases, directly from a Makefile).

# Typically deliverable_dependencies.mk is included to define "make test" to
# rely on this for "make test" only in the current directory,
# as well as sub_dirs.mk to "make test" in the sub-directories.


# ==================================================================
.PHONY: main_clean
main_clean:

# ==================================================================
.PHONY: main_roadmap
main_roadmap:

# ==================================================================
.PHONY: main_analysis
main_analysis:

# ==================================================================
.PHONY: main_design
main_design:

# ==================================================================
.PHONY: main_source
main_source:

# ==================================================================
.PHONY: main_test
main_test:

# ==================================================================
.PHONY: main_documentation
main_documentation:

# ==================================================================
.PHONY: main_examples
main_examples:

# ==================================================================
.PHONY: main_profile
main_profile:
