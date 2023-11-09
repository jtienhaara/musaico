#
# Copyright (c) 2011 Johann Tienhaara <jtienhaara@yahoo.com>
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

# PLATFORM_DIR must be defined from the Makefile which includes this.
# For example:
#
#     PLATFORM_DIR = ../../..
#

PLATFORM = java

PLATFORM_SUB_DIR = test/integration/$(PLATFORM)

# First include the general test class variables.
TEST_CLASS_DIR = $(PLATFORM_DIR)/..
include $(TEST_CLASS_DIR)/test_class.mk


PLATFORM_DEPENDENCIES =

SUB_PLATFORMS =

THIRD_PARTY_DEPENDENCIES = 

# Now include the Java variables and rules.
include $(MUSAICO_DIR)/build/platforms/$(PLATFORM).mk
