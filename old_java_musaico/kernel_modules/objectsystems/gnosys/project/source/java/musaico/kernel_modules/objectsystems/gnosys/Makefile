#!/usr/bin/make

#
# Copyright (c) 2011, 2012 Johann Tienhaara <jtienhaara@yahoo.com>
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

PACKAGE = musaico/kernel_modules/objectsystems/gnosys


PACKAGE_DEPENDENCIES = \
	musaico/kernel/memory \
	musaico/kernel/objectsystem \
	musaico/kernel/objectsystems


COMPILABLES = \
	GnONodeMetadata.class \
	GnSuperBlock.class

# !!! 	GnFlatRecordOperations.class \
# !!! 	GnONodeOperations.class \
# !!! 	GnObjectOperations.class \
# !!! 	GnONode.class \
# !!! 	GnSuperBlock.class \
# !!! 	Gnosys.class \
# !!! 	GnosysModule.class

RESOURCES = \
	Messages.properties \
	Module.properties

SUB_PACKAGES = 

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk

test_gnosys:
	java -classpath "$(CLASSPATH_RUN):$(CLASSPATH_RUN_DEPENDENCIES)" \
		musaico.kernel.kernels.TestKernel "musaico.kernel_modules.objectsystems.gnosys"
