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

# MUSAICO_DIR must be defined from the Makefile which includes this.
# For example:
#
#     MUSAICO_DIR = ../..
#

ROADMAP_SUB_DIR = roadmap
ANALYSIS_SUB_DIR = analysis
DESIGN_SUB_DIR = design
SOURCE_SUB_DIR = source
TEST_SUB_DIR = test
DOCUMENTATION_SUB_DIR = documentation
EXAMPLES_SUB_DIR = examples
PROFILE_SUB_DIR = profile


MUSAICO_DEPENDENCIES =


#
# Helpful macros:
#

# Directory in which the package exists.
# For example, "musaico/foobar" might contain *.c files for the foobar
# source package, or *.html files for the foobar documentation package,
# and so on.
PACKAGE_DIR = \
	$(PACKAGE)

# Replace slashes in the path with underscores so we
# can turn it into a tar file and so on.
PACKAGE_FILENAME = \
	$(subst /,_,$(PACKAGE))


# ==================================================================

#
# Stupid variables for gmake:
# (http://www.gnu.org/software/automake/manual/make/Syntax-of-Functions.html#Syntax-of-Functions)
#
COMMA = ,
EMPTY =
SPACE = $(EMPTY) $(EMPTY)

COLON = :


# ==================================================================
#
# Include files:
#

# The version of Musaico.
include $(MUSAICO_DIR)/build/version.mk

# Operating system-specific stuff.  Like hacks for Windoze.
include $(MUSAICO_DIR)/build/os.mk

# Third party libraries, testing frameworks, documentation
# generators, and so on.
include $(MUSAICO_DIR)/third_party/third_party.mk

include $(MUSAICO_DIR)/build/rules/rule_makedebug.mk
