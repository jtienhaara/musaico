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

MUSAICO_DIR = ../..
include $(MUSAICO_DIR)/Musaico.mk

PROJECT_DIRS = \
	byte_stream/project \
	file/project \
	list/project \

SUB_DIRS = $(PROJECT_DIRS)

include $(MUSAICO_DIR)/build/rules/deliverable_dependencies.mk
include $(MUSAICO_DIR)/build/rules/deliverable_main.mk
include $(MUSAICO_DIR)/build/rules/deliverable_recursion.mk

include $(MUSAICO_DIR)/build/rules/source_toplevel_recursion.mk
