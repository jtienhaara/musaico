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

MUSAICO_DIR = .

DELIVERABLES = \
	roadmap \
	analysis \
	design \
	source \
	test \
	profile \
	examples \
	documentation

# Non-Musaico code that must be built before anything else.
PREPARATION = \
	third_party


# Note: The variable name PROJECTS seems to be used by (gnu) make
#       for the list of directory names.  Use PROJECT_DIRS instead.
PROJECT_DIRS = \
	$(MUSAICO_DIR)/build \
	$(MUSAICO_DIR)/foundation \
	$(MUSAICO_DIR)/platform \
	$(MUSAICO_DIR)/kernel \
	$(MUSAICO_DIR)/kernel_modules

# !!! \
	$(MUSAICO_DIR)/i18n/project \
	$(MUSAICO_DIR)/condition/project \
	$(MUSAICO_DIR)/time/project \
	$(MUSAICO_DIR)/hash/project \
	$(MUSAICO_DIR)/mutex/project \
	$(MUSAICO_DIR)/types/project \
	$(MUSAICO_DIR)/io/project \
		$(MUSAICO_DIR)/io/types/project \
	$(MUSAICO_DIR)/region/project \
		$(MUSAICO_DIR)/region/types/project \
	$(MUSAICO_DIR)/security/project \
		$(MUSAICO_DIR)/security/types/project \
	$(MUSAICO_DIR)/field/project \
	$(MUSAICO_DIR)/buffer/project \
	$(MUSAICO_DIR)/pubsub/project \
	$(MUSAICO_DIR)/state/project \
	$(MUSAICO_DIR)/kernel/project \
		$(MUSAICO_DIR)/kernel/memory/project \
		$(MUSAICO_DIR)/kernel/objectsystem/project \
		$(MUSAICO_DIR)/kernel/task/project \
		$(MUSAICO_DIR)/kernel/process/project \
		$(MUSAICO_DIR)/kernel/driver/project \
		$(MUSAICO_DIR)/kernel/module/project \
		\
		$(MUSAICO_DIR)/kernel/types/project \
		\
		$(MUSAICO_DIR)/kernel/common/project \
		$(MUSAICO_DIR)/kernel/kernels/project \
	$(MUSAICO_DIR)/kernel_modules/drivers/byte_stream/project \
	$(MUSAICO_DIR)/kernel_modules/drivers/file/project \
	$(MUSAICO_DIR)/kernel_modules/drivers/list/project \
	$(MUSAICO_DIR)/kernel_modules/objectsystems/gnosys/project \
	$(MUSAICO_DIR)/kernel_modules/objectsystems/testosys/project

# !!! commented out drivers (maybe permanent, maybe just for now):
# !!!     excel \
# !!!     sql

# !!! commented out object systems (maybe permanent, maybe just for now):
# !!!     documentosys \
# !!!     seriesosys \
# !!!     tableosys \
# !!!     checklist

# Non-Musaico code that must be built after everything else.
CLEANUP = 


include $(MUSAICO_DIR)/build/musaico.mk

SUB_DIRS = \
	$(PREPARATION) \
	$(PROJECT_DIRS) \
	$(CLEANUP)

include $(MUSAICO_DIR)/build/rules/toplevel.mk
