#!/usr/bin/make

#
# Copyright (c) 2009, 2011, 2012 Johann Tienhaara
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

PACKAGE = musaico/kernel


PACKAGE_DEPENDENCIES = \
	musaico/time \
	musaico/state \
	musaico/pubsub \
	musaico/types \
	musaico/security \
	musaico/io \
	musaico/region \
	musaico/field \
	musaico/buffer

COMPILABLES = \
	KernelOperation.class \
	KernelObjectException.class \
	KernelModulesRegistry.class \
	KernelObjectsRegistry.class \
	PlatformOperations.class \
	QuotaCategory.class \
	KernelQuotaRules.class \
	Kernel.class \
	KernelPermission.class \
	KernelPermissions.class \
	KernelNamespaces.class \
	KernelType.java


RESOURCES = \
	Messages.properties

SUB_PACKAGES = \
	platforms \
	operations \
	registries \
	rules

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/Rules.mk
