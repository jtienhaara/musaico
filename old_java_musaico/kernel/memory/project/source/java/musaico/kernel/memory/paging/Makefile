#!/usr/bin/make

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

PACKAGE = musaico/kernel/memory/paging


PACKAGE_DEPENDENCIES = \
	musaico/io \
	musaico/kernel/memory


COMPILABLES = \
	SwapConfiguration.class \
	SwapState.class \
	SwapSystem.class \
	Page.class \
	PageOrder.class \
	SwapException.class \
	Swapper.class \
	SwapStep.class \
	SwapOperation.class \
	PageTableException.class \
	PageTable.class \
	KernelPaging.class \
	PagedAreaIdentifier.class \
	PagedArea.class \
	PageFault.class \
	\
	SimpleSwapConfiguration.class \
	SimplePageFault.class

RESOURCES = \
	Messages.properties

SUB_PACKAGES = \
	buffer \
	kernelpagings \
	swap \
	pagetables \
	pagedareas

SUB_DIRS = $(SUB_PACKAGES)

JAVADOC_OVERVIEW = \
	overview.html


# How to build things:
PLATFORM_DIR = ../../../..
include $(PLATFORM_DIR)/Platform.mk

# Toplevel rules for the user (compile, test, doc, lib, and so on).
include $(MUSAICO_DIR)/build/Rules.mk
