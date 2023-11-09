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

# Build the main project code, as well as all sub-packages.
PACKAGES = \
	musaico/kernel_modules/drivers/byte_stream

SUB_DIRS = \
	$(PACKAGES)

PLATFORM_DIR = .

# First include the general source variables.
SOURCE_DIR = $(PLATFORM_DIR)/..
include $(SOURCE_DIR)/source.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
